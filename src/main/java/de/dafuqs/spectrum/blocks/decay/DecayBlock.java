package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.tick.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DecayBlock extends Block {
	
	public enum Conversion implements StringIdentifiable {
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
		public String asString() {
			return this.name;
		}
	}
	
	public static final EnumProperty<Conversion> CONVERSION = EnumProperty.of("conversion", Conversion.class);
	
	protected final float spreadChance;
	protected final boolean canSpreadToBlockEntities;
	protected final float damageOnTouching;
	protected final int tier;
	
	public DecayBlock(Settings settings, float spreadChance, boolean canSpreadToBlockEntities, int tier, float damageOnTouching) {
		super(settings);
		this.spreadChance = spreadChance;
		this.canSpreadToBlockEntities = canSpreadToBlockEntities;
		this.damageOnTouching = damageOnTouching;
		this.tier = tier;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(CONVERSION);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof LivingEntity && !entity.isFireImmune() && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
			entity.damage(SpectrumDamageTypes.decay(world), damageOnTouching);
		}
		super.onSteppedOn(world, pos, state, entity);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient && SpectrumCommon.CONFIG.LogPlacingOfDecay && placer != null) {
			SpectrumCommon.logInfo(state.getBlock().getName().getString() + " was placed in " + world.getRegistryKey().getValue() + " at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " by " + placer.getEntityName());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(CONVERSION).equals(Conversion.SPECIAL)) {
			world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	private boolean canSpreadTo(World world, BlockPos targetBlockPos, BlockState stateAtTargetPos) {
		if (SpectrumCommon.CONFIG.DecayIsStoppedByClaimMods && !GenericClaimModsCompat.canModify(world, targetBlockPos, null)) {
			return false;
		}
		
		return (this.canSpreadToBlockEntities || world.getBlockEntity(targetBlockPos) == null)
				&& (!(stateAtTargetPos.getBlock() instanceof DecayBlock decayBlock) || this.tier > decayBlock.tier) // decay can convert decay of a lower tier
				&& (stateAtTargetPos.getBlock() == Blocks.BEDROCK || (stateAtTargetPos.getBlock().getHardness() > -1.0F && stateAtTargetPos.getBlock().getBlastResistance() < 10000.0F));
	}

	/**
	 * If a neighboring block is updated (placed by a player?), and that can be converted
	 * schedule a tick to convert it faster. => User gets quick reaction
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block previousBlock, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, previousBlock, fromPos, notify);
		
		if (previousBlock == Blocks.AIR) {
			BlockState updatedState = world.getBlockState(fromPos);
			Block updatedBlock = updatedState.getBlock();
			
			if (!(updatedBlock instanceof DecayBlock) && !(updatedBlock instanceof DecayAwayBlock)) {
				@Nullable BlockState spreadState = this.getSpreadState(state, updatedState, world, fromPos);
				if (spreadState != null) {
					world.scheduleBlockTick(pos, this, 40 + world.random.nextInt(200), TickPriority.EXTREMELY_LOW);
				}
			}
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.randomTick(state, world, pos, random);
		
		trySpreadToRandomNeighboringBlock(state, world, pos);
	}
	
	// jump to neighboring blocks
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.spreadChance < 1.0F) {
			if (random.nextFloat() > this.spreadChance) {
				return;
			}
		}

		Direction randomDirection = Direction.random(random);
		trySpreadInDirection(world, state, pos, randomDirection);
	}

	private void trySpreadToRandomNeighboringBlock(BlockState state, ServerWorld world, BlockPos pos) {
		List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
		Collections.shuffle(directions);

		for (Direction direction : directions) {
			if (trySpreadInDirection(world, state, pos, direction)) {
				break;
			}
		}
	}
	
	protected boolean trySpreadInDirection(@NotNull World world, BlockState state, @NotNull BlockPos originPos, Direction direction) {
		BlockPos targetPos = originPos.offset(direction);
		BlockState targetBlockState = world.getBlockState(targetPos);
		
		if (canSpreadTo(world, targetPos, targetBlockState)) {
			@Nullable BlockState spreadState = this.getSpreadState(state, targetBlockState, world, targetPos);
			if (spreadState != null) {
				if (world.setBlockState(targetPos, spreadState)) {
					world.playSound(null, targetPos, spreadState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 0.5F, 1.0F);
					return true;
				}
			}
		}
		return false;
	}
	
	protected abstract @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, World world, BlockPos stateToSpreadToPos);
	
}
