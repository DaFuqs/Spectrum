package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

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