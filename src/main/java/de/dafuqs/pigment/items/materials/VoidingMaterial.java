package de.dafuqs.pigment.items.materials;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class VoidingMaterial implements ToolMaterial {

    @Override
    public int getDurability() {
        return 1143; // almost like diamond
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 20.0F; // almost instamines stone
    }

    @Override
    public float getAttackDamage() {
        return 1.0F;
    }

    @Override
    public int getMiningLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null; // can not be repaired
    }

}