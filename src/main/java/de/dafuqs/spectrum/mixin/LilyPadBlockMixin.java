package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LilyPadBlock.class)
public class LilyPadBlockMixin {
    @ModifyReturnValue(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("RETURN"))
    public boolean spectrum$extendLilyPlaceables(boolean original, BlockState floor, BlockView world, BlockPos pos) {
        if (original)
            return true;
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return (fluidState.getFluid() == SpectrumFluids.MUD || fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) && fluidState2.getFluid() == Fluids.EMPTY;
    }
}
