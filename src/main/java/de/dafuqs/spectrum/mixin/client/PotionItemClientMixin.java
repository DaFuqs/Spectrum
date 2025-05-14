package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin({PotionItem.class, LingeringPotionItem.class, TippedArrowItem.class})
public abstract class PotionItemClientMixin {
	
	@Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
	private void spectrum$makePotionUnidentifiable(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type, CallbackInfo ci) {
		CustomPotionDataComponent component = stack.get(SpectrumDataComponentTypes.CUSTOM_POTION_DATA);
		if (component != null && component.unidentifiable()) {
			tooltip.add(Component.translatable("item.spectrum.potion.tooltip.unidentifiable"));
			ci.cancel();
		}
	}
	
}
