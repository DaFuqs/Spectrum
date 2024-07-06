package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LandPathNodeMaker.class)
public class WalkNodeProcessorMixin {
	
	@Inject(method = "inflictsFireDamage(Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
	private static void spectrum$burningBlockPathfinding(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.isIn(SpectrumBlockTags.FIRE_LAND_NODE_MARKERS)) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getCommonNodeType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/ai/pathing/PathNodeType;", at = @At("RETURN"), cancellable = true)
	private static void spectrum$addBlockNodeTypes(BlockView world, BlockPos pos, CallbackInfoReturnable<PathNodeType> cir) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.isIn(SpectrumBlockTags.DAMAGING_LAND_NODE_MARKERS)) {
			cir.setReturnValue(PathNodeType.DAMAGE_OTHER);
		}
	}
	
}