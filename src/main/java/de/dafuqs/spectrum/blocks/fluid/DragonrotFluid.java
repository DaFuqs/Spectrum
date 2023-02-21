package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public abstract class DragonrotFluid extends SpectrumFluid {

    @Override
    public Fluid getStill() {
        return SpectrumFluids.DRAGONROT;
    }

    @Override
    public Fluid getFlowing() {
        return SpectrumFluids.FLOWING_DRAGONROT;
    }

    @Override
    public Item getBucketItem() {
        return SpectrumItems.DRAGONROT_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return SpectrumBlocks.DRAGONROT.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == SpectrumFluids.DRAGONROT || fluid == SpectrumFluids.FLOWING_DRAGONROT;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (random.nextInt(1000) == 0) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.DRAGONROT_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 3;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return 3;
    }

    @Override
    public int getTickRate(WorldView worldView) {
        return 40;
    }

    @Override
    public ParticleEffect getParticle() {
        return SpectrumParticleTypes.DRIPPING_DRAGONROT;
    }

    @Override
    public ParticleEffect getSplashParticle() {
        return SpectrumParticleTypes.DRAGONROT_SPLASH;
    }

    public static class FlowingDragonrot extends DragonrotFluid {

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

    public static class StillDragonrot extends DragonrotFluid {

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