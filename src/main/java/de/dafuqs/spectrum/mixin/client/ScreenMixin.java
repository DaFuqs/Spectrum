package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.items.tooltip.CraftingTabletTooltipComponent;
import de.dafuqs.spectrum.items.tooltip.CraftingTabletTooltipData;
import de.dafuqs.spectrum.items.tooltip.VoidBundleTooltipComponent;
import de.dafuqs.spectrum.items.tooltip.VoidBundleTooltipData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {

	@Inject(method = "method_32635", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onComponentConstruct(List<TooltipComponent> list, TooltipData data, CallbackInfo info) {
		if (data instanceof CraftingTabletTooltipData) {
			list.add(1, new CraftingTabletTooltipComponent((CraftingTabletTooltipData) data));
			info.cancel();
		} else if(data instanceof VoidBundleTooltipData) {
			list.add(1, new VoidBundleTooltipComponent((VoidBundleTooltipData) data));
			info.cancel();
		}
	}

}