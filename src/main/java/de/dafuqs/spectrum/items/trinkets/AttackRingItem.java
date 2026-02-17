package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class AttackRingItem extends SpectrumCurioItem {
	
	public static final ResourceLocation ATTACK_RING_DAMAGE_ID = SpectrumCommon.locate("jeopardant");
	
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
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		super.onUnequip(slotContext, newStack, stack);
		
		LivingEntity entity = slotContext.entity();
		if (entity.getAttributes().hasModifier(Attributes.ATTACK_DAMAGE, AttackRingItem.ATTACK_RING_DAMAGE_ID)) {
			Multimap<Holder<Attribute>, AttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
			map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_ID, AttackRingItem.getAttackModifierForEntity(entity), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			entity.getAttributes().removeAttributeModifiers(map);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
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
