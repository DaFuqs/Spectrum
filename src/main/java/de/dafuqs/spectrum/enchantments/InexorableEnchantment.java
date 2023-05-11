package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;

public class InexorableEnchantment extends SpectrumEnchantment{

    public InexorableEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.ARMOR_CHEST, slotTypes, unlockAdvancementIdentifier);
    }

    @Override
    public int getMinPower(int level) {
        return 50;
    }

    @Override
    public int getMaxPower(int level) {
        return 100;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        var item = stack.getItem();

        if (item instanceof ArmorItem armor)
            return armor.getSlotType() == EquipmentSlot.CHEST;

        return item instanceof ToolItem;
    }

    public static boolean isArmorActive(LivingEntity entity) {
        return EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, entity.getEquippedStack(EquipmentSlot.CHEST)) > 0;
    }
}
