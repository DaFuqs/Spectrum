package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;

public class InexorableHelper {
	
	public static void checkAndRemoveSlowdownModifiers(LivingEntity entity) {
		var armorInexorable = isArmorActive(entity);
		var toolInexorable = SpectrumEnchantmentHelper.hasEnchantment(entity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, entity.getItemInHand(entity.getUsedItemHand()));
		
		var armorAttributes = BuiltInRegistries.ATTRIBUTE.getTag(SpectrumAttributeTags.INEXORABLE_ARMOR_EFFECTIVE);
		var toolAttributes = BuiltInRegistries.ATTRIBUTE.getTag(SpectrumAttributeTags.INEXORABLE_HANDHELD_EFFECTIVE);
		
		if (armorInexorable && armorAttributes.isPresent()) {
			for (Holder<Attribute> attributeRegistryEntry : armorAttributes.get()) {
				
				var attributeInstance = entity.getAttribute(attributeRegistryEntry);
				
				if (attributeInstance == null)
					continue;
				
				var badMods = attributeInstance.getModifiers()
						.stream()
						.filter(modifier -> modifier.amount() < 0)
						.toList();
				
				badMods.forEach(modifier -> attributeInstance.removeModifier(modifier.id()));
			}
		}
		
		if (toolInexorable && toolAttributes.isPresent()) {
			for (Holder<Attribute> attributeRegistryEntry : toolAttributes.get()) {
				
				var attributeInstance = entity.getAttribute(attributeRegistryEntry);
				
				if (attributeInstance == null)
					continue;
				
				var badMods = attributeInstance.getModifiers()
						.stream()
						.filter(modifier -> modifier.amount() < 0)
						.toList();
				
				badMods.forEach(modifier -> attributeInstance.removeModifier(modifier.id()));
			}
		}
	}
	
	public static boolean isArmorActive(LivingEntity entity) {
		return SpectrumEnchantmentHelper.hasEnchantment(entity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, entity.getItemBySlot(EquipmentSlot.CHEST));
	}
}
