package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AttackRingItem extends SpectrumTrinketItem {
	
	public static final UUID ATTACK_RING_DAMAGE_UUID = UUID.fromString("15d1fb68-6440-404a-aa31-7bf3310d3f52");
	
	public AttackRingItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/jeopardant"));
	}
	
	public static double getAttackModifierForEntity(LivingEntity entity) {
		if (entity == null) {
			return 0;
		} else {
			double mod = entity.getMaxHealth() / (entity.getHealth() * entity.getHealth() + 1); // starting with 1 % damage at 14 health up to 300 % damage at 1/20 health
			return Math.max(0, 1 + Math.log10(mod));
		}
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		if (entity.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE, AttackRingItem.ATTACK_RING_DAMAGE_UUID)) {
			Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
			EntityAttributeModifier modifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:jeopardant", AttackRingItem.getAttackModifierForEntity(entity), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
			map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, modifier);
			entity.getAttributes().removeModifiers(map);
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		long mod = Math.round(getAttackModifierForEntity(MinecraftClient.getInstance().player) * 100);
		if (mod == 0) {
			tooltip.add(Text.translatable("item.spectrum.jeopardant.tooltip.damage_zero"));
		} else {
			tooltip.add(Text.translatable("item.spectrum.jeopardant.tooltip.damage", mod));
		}
	}
	
}