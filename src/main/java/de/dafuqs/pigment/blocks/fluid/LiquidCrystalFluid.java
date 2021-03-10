package de.dafuqs.pigment.blocks.fluid;

import de.dafuqs.pigment.registries.PigmentBlocks;
import de.dafuqs.pigment.registries.PigmentFluids;
import de.dafuqs.pigment.items.PigmentItems;
import de.dafuqs.pigment.sound.PigmentSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public abstract class LiquidCrystalFluid extends PigmentFluid {

	@Override
	public Fluid getStill() {
		return PigmentFluids.STILL_LIQUID_CRYSTAL;
	}

	@Override
	public Fluid getFlowing() {
		return PigmentFluids.FLOWING_LIQUID_CRYSTAL;
	}

	@Override
	public Item getBucketItem() {
		return PigmentItems.LIQUID_CRYSTAL_BUCKET;
	}

	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return PigmentBlocks.LIQUID_CRYSTAL.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == PigmentFluids.STILL_LIQUID_CRYSTAL || fluid == PigmentFluids.FLOWING_LIQUID_CRYSTAL;
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (random.nextInt(150) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), PigmentSoundEvents.LIQUID_CRYSTAL_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		// if liquid crystal collides with a flower of any kind:
		// drop a resonant lily instead
		if(state.isIn(BlockTags.FLOWERS)) {
			Block.dropStacks(PigmentBlocks.RESONANT_LILY.getDefaultState(), world, pos, null);
		} else {
			final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
			Block.dropStacks(state, world, pos, blockEntity);
		}
	}

	@Override
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	public static class FlowingLiquidCrystal extends LiquidCrystalFluid {

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}

	}

	public static class StillLiquidCrystal extends LiquidCrystalFluid {

		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}

	}
}