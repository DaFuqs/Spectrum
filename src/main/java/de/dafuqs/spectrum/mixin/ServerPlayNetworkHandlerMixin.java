package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {
	
	@Shadow
	public ServerPlayer player;
	
	@Inject(method = "handlePlayerAction", at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerPlayer.getItemInHand (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), cancellable = true)
	private void handleSwapInteractions(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
		
		var mainStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		var offStack = player.getItemInHand(InteractionHand.OFF_HAND);
		var mainItem = mainStack.getItem();
		var offItem = offStack.getItem();
		
		if (mainItem instanceof SplittableItem splittable && splittable.canSplit(player, InteractionHand.MAIN_HAND, mainStack)) {
			spectrum$splitItem(mainStack, splittable);
			ci.cancel();
		} else if (offItem instanceof SplittableItem splittable && splittable.canSplit(player, InteractionHand.OFF_HAND, offStack)) {
			spectrum$splitItem(offStack, splittable);
			ci.cancel();
		} else if (mainItem instanceof MergeableItem mergeable && offItem instanceof MergeableItem && mergeable.canMerge(player, mainStack, offStack)) {
			spectrum$mergeItems(mainStack, offStack, mergeable);
			ci.cancel();
		}
	}
	
	@Unique
	private void spectrum$splitItem(ItemStack stack, SplittableItem splittable) {
		var split = splittable.getSplitResult(player, stack);
		player.setItemInHand(InteractionHand.MAIN_HAND, split);
		player.setItemInHand(InteractionHand.OFF_HAND, split.copy());
		player.stopUsingItem();
		splittable.playSound(player);
	}
	
	@Unique
	private void spectrum$mergeItems(ItemStack firstHalf, ItemStack secondHalf, MergeableItem mergeable) {
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		player.setItemInHand(InteractionHand.MAIN_HAND, mergeable.getMergeResult(player, firstHalf, secondHalf));
		player.stopUsingItem();
		mergeable.playSound(player);
	}
	
}
