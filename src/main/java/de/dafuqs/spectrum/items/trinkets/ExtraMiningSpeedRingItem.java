package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
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
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraMiningSpeedRingItem extends InkDrainTrinketItem {
	
	public ExtraMiningSpeedRingItem(Settings settings) {
		super(settings, SpectrumCommon.locate("progression/unlock_ring_of_pursuit"), InkColors.MAGENTA, 13421772800L); // 2x dig speed
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.ring_of_pursuit.tooltip").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		double miningSpeedMod = getExtraMiningSpeed(storedInk);
		if (miningSpeedMod != 0) {
			modifiers.put(AdditionalEntityAttributes.DIG_SPEED, new EntityAttributeModifier(uuid, "spectrum:ring_of_pursuit", miningSpeedMod, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		}
		
		return modifiers;
	}
	
	public double getExtraMiningSpeed(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 0.1 + 0.1 * (int) (Math.log(storedInk / 100) / Math.log(8));
		}
	}
	
}