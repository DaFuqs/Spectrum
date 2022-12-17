package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class MalachiteGreatswordItem extends SwordItem {
	
	private final MalachiteWorkstaffItem.Variant variant;
	
	public MalachiteGreatswordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, MalachiteWorkstaffItem.Variant variant) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		this.variant = variant;
	}
	
}
