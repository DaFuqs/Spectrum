package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	
	
	@Shadow
	public ServerPlayerEntity player;
	
	@Inject(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", ordinal = 0), cancellable = true)
	private void handleSwapInteractions(PlayerActionC2SPacket packet, CallbackInfo ci) {
		
		var mainStack = player.getStackInHand(Hand.MAIN_HAND);
		var offStack = player.getStackInHand(Hand.OFF_HAND);
		var mainItem = mainStack.getItem();
		var offItem = offStack.getItem();
		
		if (mainItem instanceof SplittableItem splittable && splittable.canSplit(player, Hand.MAIN_HAND, mainStack)) {
			splitItem(mainStack, splittable);
			ci.cancel();
		} else if (offItem instanceof SplittableItem splittable && splittable.canSplit(player, Hand.OFF_HAND, offStack)) {
			splitItem(offStack, splittable);
			ci.cancel();
		} else if (mainItem instanceof MergeableItem mergeable && offItem instanceof MergeableItem && mergeable.canMerge(player, mainStack, offStack)) {
			mergeItems(mainStack, offStack, mergeable);
			ci.cancel();
		}
	}
	
	@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$1")
	static class NetworkEntityValidationMixin {
		
		@Final
		@Shadow(aliases = "field_28963")
		private ServerPlayNetworkHandler this$0;
		
		@Final
		@Shadow(aliases = "field_28962")
		private Entity innerEntity;
		
		@Inject(method = "attack", at = @At(value = "HEAD"), cancellable = true)
		public void allowNonLivingEntityAttack(CallbackInfo ci) {
			if (innerEntity instanceof NonLivingAttackable) {
				this$0.player.attack(innerEntity);
				ci.cancel();
			}
		}
	}
	
	@Unique
	private void splitItem(ItemStack stack, SplittableItem splittable) {
		var split = splittable.getResult(player, stack);
		player.setStackInHand(Hand.MAIN_HAND, split);
		player.setStackInHand(Hand.OFF_HAND, split.copy());
		player.clearActiveItem();
		splittable.playSound(player);
	}
	
	@Unique
	private void mergeItems(ItemStack firstHalf, ItemStack secondHalf, MergeableItem mergeable) {
		player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
		player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
		player.setStackInHand(Hand.MAIN_HAND, mergeable.getResult(player, firstHalf, secondHalf));
		player.clearActiveItem();
		mergeable.playSound(player);
	}
}
