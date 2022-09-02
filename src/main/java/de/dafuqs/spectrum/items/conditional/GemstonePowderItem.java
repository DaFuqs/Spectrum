package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.enums.GemstoneColor;
import net.minecraft.item.DyeItem;
import net.minecraft.util.Identifier;

public class GemstonePowderItem extends CloakedItem {
	
	protected final GemstoneColor gemstoneColor;
	
	public GemstonePowderItem(Settings settings, Identifier cloakAdvancementIdentifier, GemstoneColor gemstoneColor) {
		super(settings, cloakAdvancementIdentifier, DyeItem.byColor(gemstoneColor.getDyeColor()));
		this.gemstoneColor = gemstoneColor;
	}
	
	public GemstoneColor getGemstoneColor() {
		return gemstoneColor;
	}
	
}
