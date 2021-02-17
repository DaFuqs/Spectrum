package de.dafuqs.pigment.blocks.fluid;

import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.PigmentFluids;
import de.dafuqs.pigment.PigmentItems;
import de.dafuqs.pigment.sounds.PigmentSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public abstract class MudFluid extends PigmentFluid {

	@Override
	public Fluid getStill() {
		return PigmentFluids.STILL_MUD;
	}

	@Override
	public Fluid getFlowing() {
		return PigmentFluids.FLOWING_MUD;
	}

	@Override
	public Item getBucketItem() {
		return PigmentItems.MUD_BUCKET;
	}

	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return PigmentBlocks.MUD.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == PigmentFluids.STILL_MUD || fluid == PigmentFluids.FLOWING_MUD;
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (random.nextInt(250) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), PigmentSoundEvents.MUD_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}

	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 1;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 3;
	}

	@Override
	public int getTickRate(WorldView worldView) {
		return 50;
	}

	public static class FlowingMud extends MudFluid {

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

	public static class StillMud extends MudFluid {

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