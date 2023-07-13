package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.items.tooltip.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;

@Environment(EnvType.CLIENT)
public class SpectrumTooltipComponents {
	
	public static void registerTooltipComponents() {
		TooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof CraftingTabletTooltipData craftingTabletTooltipData) {
				return new CraftingTabletTooltipComponent(craftingTabletTooltipData);
			} else if (data instanceof VoidBundleTooltipData voidBundleTooltipData) {
				return new VoidBundleTooltipComponent(voidBundleTooltipData);
			} else if (data instanceof PresentTooltipData presentTooltipData) {
				return new PresentTooltipComponent(presentTooltipData);
			}
			return null;
		}));
	}
	
}
