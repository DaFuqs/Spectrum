package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(RedstoneOreBlock.class)
public abstract class RedstoneOreMixin {
	
	@Inject(method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand).getItem() instanceof BuildingStaffItem) {
			cir.setReturnValue(ActionResult.PASS);
		}
	}
	
}
