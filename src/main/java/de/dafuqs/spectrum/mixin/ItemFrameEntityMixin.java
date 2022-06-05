package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.CelestialPocketWatchItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
	
	@Shadow
	public abstract ItemStack getHeldItemStack();
	
	@Inject(method = "interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setRotation(I)V"))
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (getHeldItemStack().isOf(SpectrumItems.CELESTIAL_POCKETWATCH) && (((ItemFrameEntity) (Object) this).world instanceof ServerWorld serverWorld)) {
			CelestialPocketWatchItem.advanceTime((ServerPlayerEntity) player, serverWorld);
		}
	}
	
}
