package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;

public class BedrockAxeItem extends AxeItem {

    public BedrockAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}