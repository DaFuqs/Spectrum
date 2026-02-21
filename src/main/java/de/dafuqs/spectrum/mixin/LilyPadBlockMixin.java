package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WaterlilyBlock.class)
public class LilyPadBlockMixin {
	@ModifyReturnValue(method = "mayPlaceOn", at = @At("RETURN"))
	public boolean spectrum$extendLilyPlaceables(boolean original, BlockState floor, BlockGetter world, BlockPos pos) {
        if (original)
            return true;
        FluidState localState = world.getFluidState(pos);
		FluidState aboveState = world.getFluidState(pos.above());
		return (localState.getType() == SpectrumFluids.SLUDGE.get() || localState.getType() == SpectrumFluids.LIQUID_CRYSTAL.get()) && aboveState.getType() == Fluids.EMPTY;
    }
}
