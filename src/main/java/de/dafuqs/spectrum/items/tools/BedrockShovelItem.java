package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class BedrockShovelItem extends ShovelItem {

    public BedrockShovelItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }


}