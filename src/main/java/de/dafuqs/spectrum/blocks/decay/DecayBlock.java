package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.ticks.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DecayBlock extends DropExperienceBlock {
	
	public enum Conversion implements StringRepresentable {
		NONE("none"),
		DEFAULT("default"),
		SPECIAL("special");
		
		private final String name;
		
		Conversion(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
	
	public static final EnumProperty<Conversion> CONVERSION = EnumProperty.create("conversion", Conversion.class);
	
	protected final float spreadChance;
	protected final boolean canSpreadToBlockEntities;
	protected final float damageOnTouching;
	protected final int tier;
	
	public DecayBlock(Properties settings, float spreadChance, boolean canSpreadToBlockEntities, int tier, float damageOnTouching, IntProvider xpRange) {
		super(xpRange, settings);
		this.spreadChance = spreadChance;
		this.canSpreadToBlockEntities = canSpreadToBlockEntities;
		this.damageOnTouching = damageOnTouching;
		this.tier = tier;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(CONVERSION);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof LivingEntity livingEntity && !entity.fireImmune()) {
			var frostWalker = world.registryAccess().lookup(Registries.ENCHANTMENT)
					.flatMap(impl -> impl.get(Enchantments.FROST_WALKER))
					.map(e -> EnchantmentHelper.getEnchantmentLevel(e, livingEntity))
					.orElse(0);
			if (frostWalker == 0)
				entity.hurt(SpectrumDamageTypes.decay(world), damageOnTouching);
		}
		super.stepOn(world, pos, state, entity);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		
		if (!world.isClientSide && SpectrumCommon.CONFIG.LogPlacingOfDecay && placer != null) {
			SpectrumCommon.logInfo(state.getBlock().getName().getString() + " was placed in " + world.dimension().location() + " at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " by " + placer.getName());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(CONVERSION).equals(Conversion.SPECIAL)) {
			world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	private boolean canSpreadTo(Level world, BlockPos targetBlockPos, BlockState stateAtTargetPos) {
		if (SpectrumCommon.CONFIG.DecayIsStoppedByClaimMods && !GenericClaimModsCompat.canModify(world, targetBlockPos, null)) {
			return false;
		}
		
		return (this.canSpreadToBlockEntities || world.getBlockEntity(targetBlockPos) == null)
				&& (!(stateAtTargetPos.getBlock() instanceof DecayBlock decayBlock) || this.tier > decayBlock.tier) // decay can convert decay of a lower tier
				&& (stateAtTargetPos.getBlock() == Blocks.BEDROCK || (stateAtTargetPos.getBlock().defaultDestroyTime() > -1.0F && stateAtTargetPos.getBlock().getExplosionResistance() < 10000.0F));
	}

	/**
	 * If a neighboring block is updated (placed by a player?), and that can be converted
	 * schedule a tick to convert it faster. => User gets quick reaction
	 */
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block previousBlock, BlockPos fromPos, boolean notify) {
		super.neighborChanged(state, world, pos, previousBlock, fromPos, notify);
		
		if (previousBlock == Blocks.AIR) {
			BlockState updatedState = world.getBlockState(fromPos);
			Block updatedBlock = updatedState.getBlock();
			
			if (!(updatedBlock instanceof DecayBlock) && !(updatedBlock instanceof DecayAwayBlock)) {
				@Nullable BlockState spreadState = this.getSpreadState(state, updatedState, world, fromPos);
				if (spreadState != null) {
					world.scheduleTick(pos, this, 40 + world.random.nextInt(200), TickPriority.EXTREMELY_LOW);
				}
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		this.randomTick(state, world, pos, random);
		
		trySpreadToRandomNeighboringBlock(state, world, pos);
	}
	
	// jump to neighboring blocks
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (this.spreadChance < 1.0F) {
			if (random.nextFloat() > this.spreadChance) {
				return;
			}
		}
		
		Direction randomDirection = Direction.getRandom(random);
		trySpreadInDirection(world, state, pos, randomDirection);
	}
	
	private void trySpreadToRandomNeighboringBlock(BlockState state, ServerLevel world, BlockPos pos) {
		List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
		Collections.shuffle(directions);

		for (Direction direction : directions) {
			if (trySpreadInDirection(world, state, pos, direction)) {
				break;
			}
		}
	}
	
	protected boolean trySpreadInDirection(@NotNull Level world, BlockState state, @NotNull BlockPos originPos, Direction direction) {
		BlockPos targetPos = originPos.relative(direction);
		BlockState targetBlockState = world.getBlockState(targetPos);
		
		if (canSpreadTo(world, targetPos, targetBlockState)) {
			@Nullable BlockState spreadState = this.getSpreadState(state, targetBlockState, world, targetPos);
			if (spreadState != null) {
				if (world.setBlockAndUpdate(targetPos, spreadState)) {
					world.playSound(null, targetPos, spreadState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
					return true;
				}
			}
		}
		return false;
	}
	
	protected abstract @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, Level world, BlockPos stateToSpreadToPos);
	
}
