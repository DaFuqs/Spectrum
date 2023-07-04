package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import dev.emi.stepheightentityattribute.*;
import dev.emi.trinkets.api.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;

import java.util.*;

public class SevenLeagueBootsItem extends SpectrumTrinketItem implements ExtendedEnchantable {
	
	public static final UUID STEP_BOOST_UUID = UUID.fromString("47b19f03-3211-4b4d-abf1-0c544a19dc70");
	private static final EntityAttributeModifier STEP_BOOST_MODIFIER = new EntityAttributeModifier(STEP_BOOST_UUID, "spectrum:speed_boots", 0.75, EntityAttributeModifier.Operation.ADDITION);
	
	public SevenLeagueBootsItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/seven_league_boots"));
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
		double speedBoost = 0.05 * (powerLevel + 1);
		modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "spectrum:movement_speed", speedBoost, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		modifiers.put(StepHeightEntityAttributeMain.STEP_HEIGHT, STEP_BOOST_MODIFIER);
		
		return modifiers;
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.POWER;
	}
	
	@Override
	public int getEnchantability() {
		return 8;
	}
	
}