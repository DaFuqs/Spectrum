package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.tools.AoEBreakingTool;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerGameModeFabricMixin {
	
	@Shadow
	@Final
	protected ServerPlayerEntity player;

	@Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"))
	private void spectrum$tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		ServerPlayerEntity player = this.player;
		ItemStack stack = player.getMainHandStack();
		if(stack.getItem() instanceof AoEBreakingTool tool) {
			tool.onTryBreakBlock(stack, pos, player);
		}
	}
}