package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BaseFireBlock.class)
public abstract class AbstractFireBlockMixin {
	
	@Inject(at = @At("HEAD"), method = "getState", cancellable = true)
	private static void spectrum$getFireState(BlockGetter world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		BlockPos blockpos = pos.below();
		BlockState blockstate = world.getBlockState(blockpos);
		if (blockstate.is(SpectrumBlockTags.PRIMORDIAL_FIRE_BASE_BLOCKS) || PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG) {
			PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = false;
			cir.setReturnValue(SpectrumBlocks.PRIMORDIAL_FIRE.get().getStateForPosition(world, pos));
		}
	}
	
}