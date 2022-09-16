package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.items.tooltip.*;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class SpectrumTooltipComponents {
	
	public static void registerTooltipComponents() {
		TooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof CraftingTabletTooltipData) {
				return new CraftingTabletTooltipComponent((CraftingTabletTooltipData) data);
			} else if (data instanceof VoidBundleTooltipData) {
				return new VoidBundleTooltipComponent((VoidBundleTooltipData) data);
			} else if (data instanceof PresentTooltipData) {
				return new PresentTooltipComponent((PresentTooltipData) data);
			}
			return null;
		}));
	}
	
}
