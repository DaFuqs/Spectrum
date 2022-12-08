package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.items.tooltip.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

@Environment(EnvType.CLIENT)
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
