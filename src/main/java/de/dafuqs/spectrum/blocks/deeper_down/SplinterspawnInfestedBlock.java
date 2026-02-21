package de.dafuqs.spectrum.blocks.deeper_down;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SplinterspawnInfestedBlock extends Block {
	
	public static final MapCodec<SplinterspawnInfestedBlock> CODEC = RecordCodecBuilder.mapCodec(
			(instance) -> instance.group(
					BuiltInRegistries.BLOCK.byNameCodec().fieldOf("host").forGetter(SplinterspawnInfestedBlock::getHostBlock),
					propertiesCodec()
			).apply(instance, SplinterspawnInfestedBlock::new));
	
	private final Block hostBlock;
	private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();
	private static final Map<BlockState, BlockState> HOST_TO_INFESTED_STATES = Maps.newIdentityHashMap();
	private static final Map<BlockState, BlockState> INFESTED_TO_HOST_STATES = Maps.newIdentityHashMap();
	
	public MapCodec<? extends SplinterspawnInfestedBlock> codec() {
		return CODEC;
	}
	
	public SplinterspawnInfestedBlock(Block hostBlock, BlockBehaviour.Properties properties) {
		super(properties.destroyTime(hostBlock.defaultDestroyTime() / 2.0F).explosionResistance(0.75F));
		this.hostBlock = hostBlock;
		BLOCK_BY_HOST_BLOCK.put(hostBlock, this);
	}
	
	public Block getHostBlock() {
		return this.hostBlock;
	}
	
	public static boolean isCompatibleHostBlock(BlockState state) {
		return state.is(SpectrumBlocks.SHALE_CLAY) || state.is(SpectrumBlocks.PYRITE);
	}
	
	private void spawnInfestation(ServerLevel level, BlockPos pos) {
		Splinterspawn splinterspawn = SpectrumEntityTypes.SPLINTERSPAWN.create(level);
		if (splinterspawn != null) {
			splinterspawn.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
			level.addFreshEntity(splinterspawn);
			splinterspawn.spawnAnim();
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(level, pos, state, player);
		
		@Nullable BlockState newState = state;
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		for (Direction direction : directions) {
			BlockPos offsetPos = pos.relative(direction);
			BlockState offsetState = level.getBlockState(offsetPos);
			if (isCompatibleHostBlock(offsetState)) {
				newState = getUninfestedState(state);
				BlockState newOffsetState = getInfestedState(offsetState);
				level.setBlockAndUpdate(offsetPos, newOffsetState);
				level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, offsetPos, getId(newOffsetState));
				
				playSound(level, offsetPos, level.random);
				break;
			}
		}
		return newState;
	}
	
	@Override
	protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
		super.spawnAfterBreak(state, level, pos, stack, dropExperience);
		
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			return;
		}
		
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.AUTO_KILLS_SILVERFISH)) {
			Silverfish splinterspawn = SpectrumEntityTypes.SPLINTERSPAWN.create(level);
			if (splinterspawn != null) {
				spawnEntityKillAndDropXP(level, pos, dropExperience, splinterspawn);
			}
			return;
		}
		
		if (level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !EnchantmentHelper.hasTag(stack, EnchantmentTags.PREVENTS_INFESTED_SPAWNS)) {
			this.spawnInfestation(level, pos);
		}
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.relative(direction);
			if (!level.getBlockState(offsetPos).isSolid() && random.nextInt(300) == 0) {
				playSound(level, pos, random);
				break;
			}
		}
	}
	
	protected void playSound(Level level, BlockPos pos, RandomSource random) {
		level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.ENTITY_SPLINTERSPAWN_AMBIENT, SoundSource.AMBIENT, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
	}
	
	public static void spawnEntityKillAndDropXP(ServerLevel level, BlockPos pos, boolean dropExperience, Silverfish splinterspawn) {
		splinterspawn.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
		level.addFreshEntity(splinterspawn);
		splinterspawn.spawnAnim();
		int experienceAmount = Support.getIntFromDecimalWithChance(splinterspawn.getExperienceReward(level, null) * SpectrumCommon.CONFIG.PestControlExperienceMultiplier, level.random);
		splinterspawn.kill();
		
		if (dropExperience) {
			ExperienceOrb experienceOrb = new ExperienceOrb(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, experienceAmount);
			level.addFreshEntity(experienceOrb);
		}
	}
	
	public static BlockState getInfestedState(BlockState notInfested) {
		BlockState target = HOST_TO_INFESTED_STATES.getOrDefault(notInfested, null);
		if (target == null) {
			Block targetBlock = BLOCK_BY_HOST_BLOCK.get(notInfested.getBlock());
			target = targetBlock.withPropertiesOf(notInfested);
			HOST_TO_INFESTED_STATES.put(notInfested, target);
		}
		return target;
	}
	
	public BlockState getUninfestedState(BlockState infested) {
		BlockState target = INFESTED_TO_HOST_STATES.getOrDefault(infested, null);
		if (target == null) {
			Block targetBlock = ((SplinterspawnInfestedBlock) infested.getBlock()).hostBlock;
			target = targetBlock.withPropertiesOf(infested);
			INFESTED_TO_HOST_STATES.put(infested, target);
		}
		return target;
	}
	
}
