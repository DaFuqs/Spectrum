package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class InexorableEnchantment extends SpectrumEnchantment {
    
    public InexorableEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.ARMOR_CHEST, slotTypes, unlockAdvancementIdentifier);
    }
    
    public static void checkAndRemoveSlowdownModifiers(LivingEntity entity) {
        var armorInexorable = isArmorActive(entity);
        var toolInexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, entity.getStackInHand(entity.getActiveHand())) > 0;
        
        var armorAttributes = Registry.ATTRIBUTE.getEntryList(SpectrumMiscTags.INEXORABLE_ARMOR_EFFECTIVE);
        var toolAttributes = Registry.ATTRIBUTE.getEntryList(SpectrumMiscTags.INEXORABLE_HANDHELD_EFFECTIVE);
        
        if (armorInexorable && armorAttributes.isPresent()) {
            for (RegistryEntry<EntityAttribute> attributeRegistryEntry : armorAttributes.get()) {
                
                var attributeInstance = entity.getAttributeInstance(attributeRegistryEntry.value());
                
                if (attributeInstance == null)
                    continue;
                
                var badMods = attributeInstance.getModifiers()
                        .stream()
                        .filter(modifier -> modifier.getValue() < 0)
                        .toList();
                
                badMods.forEach(modifier -> attributeInstance.removeModifier(modifier.getId()));
            }
        }
        
        if (toolInexorable && toolAttributes.isPresent()) {
            for (RegistryEntry<EntityAttribute> attributeRegistryEntry : toolAttributes.get()) {
                
                var attributeInstance = entity.getAttributeInstance(attributeRegistryEntry.value());
                
                if (attributeInstance == null)
                    continue;
                
                var badMods = attributeInstance.getModifiers()
                        .stream()
                        .filter(modifier -> modifier.getValue() < 0)
                        .toList();
                
                badMods.forEach(modifier -> attributeInstance.removeModifier(modifier.getId()));
            }
        }
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
