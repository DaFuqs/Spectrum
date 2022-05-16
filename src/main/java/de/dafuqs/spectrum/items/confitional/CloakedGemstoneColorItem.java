package de.dafuqs.spectrum.items.confitional;

import de.dafuqs.spectrum.enums.GemstoneColor;
import net.minecraft.item.DyeItem;
import net.minecraft.util.Identifier;

public class CloakedGemstoneColorItem extends CloakedItem {
	
	GemstoneColor gemstoneColor;
	
	public CloakedGemstoneColorItem(Settings settings, Identifier cloakAdvancementIdentifier, GemstoneColor gemstoneColor) {
		super(settings, cloakAdvancementIdentifier, DyeItem.byColor(gemstoneColor.getDyeColor()));
		this.gemstoneColor = gemstoneColor;
	}
	
}
