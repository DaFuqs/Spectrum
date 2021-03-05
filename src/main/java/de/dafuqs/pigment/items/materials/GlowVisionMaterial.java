package de.dafuqs.pigment.items.materials;

import de.dafuqs.pigment.sound.PigmentSoundEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class GlowVisionMaterial implements ArmorMaterial {

    public static final GlowVisionMaterial INSTANCE = new GlowVisionMaterial();


    @Override
    public int getDurability(EquipmentSlot slot) {
        switch (slot) {
            case LEGS:
                return 2;
            case CHEST:
                return 3;
            default:
                return 1;
        }
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 7;
    }

    @Override
    public SoundEvent getEquipSound() {
        return PigmentSoundEvents.ITEM_ARMOR_EQUIP_GLOW_VISION;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.GLOW_INK_SAC);
    }

    @Override
    public String getName() {
        return "glow_vision";
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}