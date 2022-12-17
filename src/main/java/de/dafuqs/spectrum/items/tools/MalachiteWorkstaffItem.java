package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.ToolMaterial;

public class MalachiteWorkstaffItem extends MultiToolItem {
	
	public enum Variant {
		GLASS,
		MOONSTONE
	}
	
	private final Variant variant;
	
	public MalachiteWorkstaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, Variant variant) {
		super(material, attackDamage, attackSpeed, settings);
		this.variant = variant;
	}
	
}
