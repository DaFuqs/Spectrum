package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.FixedSingleInkDrain;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraHeartRingItem extends InkDrainTrinketItem {
	
	public ExtraHeartRingItem(Settings settings) {
		super(settings, InkColors.PINK, 13421772800L, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_extra_heart_ring")); // 20 extra hearts
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.spectrum.extra_heart_ring.tooltip").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		int extraHearts = getExtraHearts(storedInk);
		if(extraHearts != 0) {
			modifiers.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uuid, "spectrum:extra_heart_ring", extraHearts, EntityAttributeModifier.Operation.ADDITION));
		}
		
		return modifiers;
	}
	
	public int getExtraHearts(long storedInk) {
		if(storedInk < 100) {
			return 0;
		} else {
			return 2 + 2 * (int) (Math.log(storedInk / 100) / Math.log(8));
		}
	}
	
}