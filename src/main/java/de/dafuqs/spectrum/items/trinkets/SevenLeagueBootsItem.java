package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SevenLeagueBootsItem extends SpectrumTrinketItem implements EnchanterEnchantable {
	
	public static final UUID STEP_BOOST_UUID = UUID.fromString("47b19f03-3211-4b4d-abf1-0c544a19dc70");
	private static final EntityAttributeModifier STEP_BOOST_MODIFIER = new EntityAttributeModifier(STEP_BOOST_UUID, "spectrum:speed_boots", 0.75, EntityAttributeModifier.Operation.ADDITION);
	
	public SevenLeagueBootsItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_seven_league_boots"));
	}
	
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