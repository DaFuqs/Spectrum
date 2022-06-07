package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.dafuqs.spectrum.SpectrumCommon;
import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttackRingItem extends SpectrumTrinketItem {
	
	public static final UUID ATTACK_RING_DAMAGE_UUID = UUID.fromString("15d1fb68-6440-404a-aa31-7bf3310d3f52");
	
	public AttackRingItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_jeopardant"));
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
			tooltip.add(new TranslatableText("item.spectrum.jeopardant.tooltip.damage_zero"));
		} else {
			tooltip.add(new TranslatableText("item.spectrum.jeopardant.tooltip.damage", mod));
		}
	}
	
}