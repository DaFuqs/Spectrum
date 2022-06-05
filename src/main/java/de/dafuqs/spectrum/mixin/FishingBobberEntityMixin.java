package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
	
	/**
	 * The fishing bobber checks if the "owner" player still has a fishing rod in his hands
	 * The fishing rod item is hardcoded => We have to add a check for the bedrock fishing rod
	 *
	 * @param playerEntity
	 * @param callbackInfoReturnable
	 */
	@Inject(at = @At("HEAD"), method = "removeIfInvalid(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true)
	private void removeIfInvalid(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemStack itemStack = playerEntity.getMainHandStack();
		ItemStack itemStack2 = playerEntity.getOffHandStack();
		boolean bl = itemStack.isOf(SpectrumItems.BEDROCK_FISHING_ROD);
		boolean bl2 = itemStack2.isOf(SpectrumItems.BEDROCK_FISHING_ROD);
		if (!playerEntity.isRemoved() && playerEntity.isAlive() && (bl || bl2) && (((FishingBobberEntity) (Object) this).squaredDistanceTo(playerEntity) < 1024.0D)) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
	
}
