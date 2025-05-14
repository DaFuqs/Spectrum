package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemFrame.class)
public abstract class ItemFrameEntityMixin {
	
	@Shadow
	public abstract ItemStack getItem();
	
	@Inject(method = "interact",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setRotation(I)V"))
	public void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (getItem().is(SpectrumItems.CELESTIAL_POCKETWATCH) && (((ItemFrame) (Object) this).level() instanceof ServerLevel serverWorld)) {
			CelestialPocketWatchItem.tryAdvanceTime(serverWorld, (ServerPlayer) player);
		}
	}
	
}
