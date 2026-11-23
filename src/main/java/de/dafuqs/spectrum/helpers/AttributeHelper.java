package de.dafuqs.spectrum.helpers;

import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

public class AttributeHelper {
	
	public static float computeValue(Entity entity, ItemStack stack, Holder<Attribute> attribute, EquipmentSlot equipmentSlot) {
		var value = new AttributeInstance(attribute, $ -> {});
		if (entity instanceof LivingEntity living && living.getAttributes().hasAttribute(attribute)) {
			value.setBaseValue(living.getAttributeBaseValue(attribute));
		}
		var modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		modifiers.forEach(equipmentSlot, (currentAttribute, modifier) -> {
			if (currentAttribute.is(attribute)) {
				value.addOrUpdateTransientModifier(modifier);
			}
		});
		return (float) value.getValue();
	}
	
}
