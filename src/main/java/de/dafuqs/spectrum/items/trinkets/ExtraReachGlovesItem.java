package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.FixedSingleInkStorage;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraReachGlovesItem extends InkDrainTrinketItem {
	
	public ExtraReachGlovesItem(Settings settings) {
		super(settings, SpectrumCommon.locate("progression/unlock_gloves_of_dawns_grasp"), InkColors.LIGHT_BLUE, 1677721600); // 5 blocks of extra reach
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.spectrum.gloves_of_dawns_grasp.tooltip").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double extraReach = getExtraReach(storedInk);
		if (extraReach != 0) {
			modifiers.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(uuid, "spectrum:gloves_of_dawns_grasp", extraReach, EntityAttributeModifier.Operation.ADDITION));
			modifiers.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(uuid, "spectrum:gloves_of_dawns_grasp", extraReach, EntityAttributeModifier.Operation.ADDITION));
		}
		
		return modifiers;
	}
	
	public double getExtraReach(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 1 + roundHalf(Math.log(storedInk / 100) / Math.log(64));
		}
	}
	
	public static double roundHalf(double number) {
		return ((int) (number * 2)) / 2D;
	}
	
}