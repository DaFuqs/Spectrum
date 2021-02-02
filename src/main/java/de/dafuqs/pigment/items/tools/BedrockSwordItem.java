package de.dafuqs.pigment.items.tools;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class BedrockSwordItem extends SwordItem {

    public BedrockSwordItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}