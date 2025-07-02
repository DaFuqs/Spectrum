package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

public class SevenLeagueBootsItem extends SpectrumTrinketItem {
	
	public static final ResourceLocation MOVEMENT_SPEED_ATTRIBUTE_ID = SpectrumCommon.locate("seven_league_boots_movement_speed");
	public static final ResourceLocation STEP_UP_ATTRIBUTE_ID = SpectrumCommon.locate("seven_league_boots_step_up");
	
	public SevenLeagueBootsItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/seven_league_boots"));
	}
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		int powerLevel = SpectrumEnchantmentHelper.getLevel(entity.level().registryAccess(), Enchantments.POWER, stack);
		double speedBoost = 0.05 * (powerLevel + 1);
		modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_ATTRIBUTE_ID, speedBoost, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		modifiers.put(Attributes.STEP_HEIGHT, new AttributeModifier(STEP_UP_ATTRIBUTE_ID, 0.75, AttributeModifier.Operation.ADD_VALUE));
		
		return modifiers;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public int getEnchantmentValue() {
		return 8;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.POWER);
	}
	
}
