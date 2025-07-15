package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

public class GemstonePowderItem extends CloakedItem {
	
	protected final GemstoneColor gemstoneColor;
	
	public GemstonePowderItem(Properties settings, ResourceLocation cloakAdvancementIdentifier, GemstoneColor gemstoneColor, Item cloak) {
		super(settings, cloakAdvancementIdentifier, cloak);
		this.gemstoneColor = gemstoneColor;
	}
	
}
