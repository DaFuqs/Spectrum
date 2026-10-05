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
		var toolInexorable = SpectrumEnchantmentHelper.hasEnchantment(entity.level().registryAccess(), SpectrumEnchantmentKeys.INEXORABLE, entity.getItemInHand(entity.getUsedItemHand()));
		
		var armorAttributes = BuiltInRegistries.ATTRIBUTE.getTag(SpectrumEntityAttributeKeys.INEXORABLE_ARMOR_EFFECTIVE);
		var toolAttributes = BuiltInRegistries.ATTRIBUTE.getTag(SpectrumEntityAttributeKeys.INEXORABLE_HANDHELD_EFFECTIVE);
		
		if (armorInexorable && armorAttributes.isPresent()) {
			removeAttributes(entity, armorAttributes.get());
		}
		
		if (toolInexorable && toolAttributes.isPresent()) {
			removeAttributes(entity, toolAttributes.get());
		}
	}
	
	private static void removeAttributes(LivingEntity entity, HolderSet.Named<Attribute> entries) {
		for (Holder<Attribute> attributeRegistryEntry : entries) {
				var attributeInstance = entity.getAttribute(attributeRegistryEntry);
				
			if (attributeInstance == null)
				return;
			
			var badMods = attributeInstance.getModifiers()
					.stream()
					.filter(modifier -> modifier.amount() < 0)
					.toList();
			
			badMods.forEach(modifier -> attributeInstance.removeModifier(modifier.id()));
		}
	}
	
	public static boolean isArmorActive(LivingEntity entity) {
		return SpectrumEnchantmentHelper.hasEnchantment(entity.level().registryAccess(), SpectrumEnchantmentKeys.INEXORABLE, entity.getItemBySlot(EquipmentSlot.CHEST));
	}
}
