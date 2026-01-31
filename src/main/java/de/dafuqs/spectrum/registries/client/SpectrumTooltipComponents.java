package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.items.tooltip.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.neoforge.client.event.*;


public class SpectrumTooltipComponents {
	
	public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(CraftingTabletTooltipData.class, CraftingTabletTooltipComponent::new);
		event.register(BottomlessBundleTooltipData.class, BottomlessBundleTooltipComponent::new);
		event.register(PresentTooltipData.class, PresentTooltipComponent::new);
	}
	
}
