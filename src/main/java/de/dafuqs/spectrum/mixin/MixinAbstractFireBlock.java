package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractFireBlock.class)
public abstract class MixinAbstractFireBlock {

    @Inject(at = @At("HEAD"), method = "getState", cancellable = true)
    private static void spectrum$getFireState(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		BlockPos blockpos = pos.down();
		BlockState blockstate = world.getBlockState(blockpos);
		if (blockstate.isIn(SpectrumBlockTags.PRIMORDIAL_FIRE_BASE_BLOCKS) || PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG) {
			PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = false;
			cir.setReturnValue((SpectrumBlocks.PRIMORDIAL_FIRE).getStateForPosition(world, pos));
		}
	}

}