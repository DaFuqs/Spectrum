package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.SpectrumCommon;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SevenLeagueBootsItem extends SpectrumTrinketItem {
	
    private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_seven_league_boots");
	
	public static final UUID STEP_BOOST_UUID = UUID.fromString("47b19f03-3211-4b4d-abf1-0c544a19dc70");
	private static final EntityAttributeModifier STEP_BOOST_MODIFIER = new EntityAttributeModifier(STEP_BOOST_UUID, "spectrum:speed_boots", 0.75, EntityAttributeModifier.Operation.ADDITION);
	
	public SevenLeagueBootsItem(Settings settings) {
		super(settings);
	}
	
	@Override
    protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		// +25% movement speed
		modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "spectrum:movement_speed", 0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		modifiers.put(StepHeightEntityAttributeMain.STEP_HEIGHT, STEP_BOOST_MODIFIER);
		
		return modifiers;
	}
 
}