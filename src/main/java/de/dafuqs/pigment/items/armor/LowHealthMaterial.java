package de.dafuqs.pigment.items.armor;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class LowHealthMaterial implements ToolMaterial {

    @Override
    public int getDurability() {
        return 8;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 4.0F;
    }

    @Override
    public float getAttackDamage() {
        return 2.0F;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}