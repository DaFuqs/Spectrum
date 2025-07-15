package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.items.tooltip.*;
import net.neoforged.api.distmarker.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;

@OnlyIn(Dist.CLIENT)
public class SpectrumTooltipComponents {
	
	public static void registerTooltipComponents() {
		TooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof CraftingTabletTooltipData craftingTabletTooltipData) {
				return new CraftingTabletTooltipComponent(craftingTabletTooltipData);
			} else if (data instanceof BottomlessBundleTooltipData bottomlessBundleTooltipData) {
				return new BottomlessBundleTooltipComponent(bottomlessBundleTooltipData);
			} else if (data instanceof PresentTooltipData presentTooltipData) {
				return new PresentTooltipComponent(presentTooltipData);
			}
			return null;
		}));
	}
	
}
