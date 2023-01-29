package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
	
	@Shadow
	public abstract ItemStack getHeldItemStack();
	
	@Inject(method = "interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setRotation(I)V"))
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (getHeldItemStack().isOf(SpectrumItems.CELESTIAL_POCKETWATCH) && (((ItemFrameEntity) (Object) this).world instanceof ServerWorld serverWorld)) {
			CelestialPocketWatchItem.tryAdvanceTime(serverWorld, (ServerPlayerEntity) player);
		}
	}
	
}
