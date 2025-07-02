package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeFabricMixin {
	
	@Shadow
	@Final
	protected ServerPlayer player;
	
	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;)V"))
	private void spectrum$tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		ServerPlayer player = this.player;
		ItemStack stack = player.getMainHandItem();
		if(stack.getItem() instanceof AoEBreakingTool tool) {
			tool.onTryBreakBlock(stack, pos, player);
		}
	}
}