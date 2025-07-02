package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;

import java.util.*;

public class AttackRingItem extends SpectrumTrinketItem {
	
	public static final ResourceLocation ATTACK_RING_DAMAGE_ID = SpectrumCommon.locate("jeopardant");
	public static final String ATTACK_RING_DAMAGE_NAME = "spectrum:jeopardant";
	
	public AttackRingItem(Properties settings) {
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
		if (entity.getAttributes().hasModifier(Attributes.ATTACK_DAMAGE, AttackRingItem.ATTACK_RING_DAMAGE_ID)) {
			Multimap<Holder<Attribute>, AttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
			map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_ID, AttackRingItem.getAttackModifierForEntity(entity), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			entity.getAttributes().removeAttributeModifiers(map);
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		Minecraft client = Minecraft.getInstance();
		long mod = Math.round(getAttackModifierForEntity(client.player) * 100);
		if (mod == 0) {
			tooltip.add(Component.translatable("item.spectrum.jeopardant.tooltip.damage_zero"));
		} else {
			tooltip.add(Component.translatable("item.spectrum.jeopardant.tooltip.damage", mod));
		}
	}
	
}
