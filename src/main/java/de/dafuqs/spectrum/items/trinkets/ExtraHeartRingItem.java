package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.FixedSingleInkDrain;
import de.dafuqs.spectrum.helpers.Support;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraHeartRingItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkDrain> {
	
	public static final InkColor INK_COLOR = InkColors.PINK;
	public static final long MAX_INK = 13421772800L; // 20 extra hearts
	
	public ExtraHeartRingItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_extra_heart_ring"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.extra_heart_ring.tooltip").formatted(Formatting.GRAY));
		
		long storedInk = 0;
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		if(inkStorage.getStoredColor() == INK_COLOR) {
			storedInk = inkStorage.getEnergy(INK_COLOR);
		}
		
		long nextStepInk;
		int pow = 0;
		do {
			nextStepInk = (long) (100 * Math.pow(8, pow));
			pow++;
		} while (storedInk > nextStepInk);
		
		if(nextStepInk == MAX_INK) {
			tooltip.add(new TranslatableText("spectrum.tooltip.ink_drain.tooltip.maxed_out").formatted(Formatting.GRAY));
		}else {
			tooltip.add(new TranslatableText("spectrum.tooltip.ink_drain.tooltip.ink_for_next_step." + inkStorage.getStoredColor().toString(), Support.getShortenedNumberString(nextStepInk - storedInk)).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(INK_COLOR);
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
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public FixedSingleInkDrain getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return FixedSingleInkDrain.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new FixedSingleInkDrain(MAX_INK, INK_COLOR);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, FixedSingleInkDrain storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	public ItemStack getFullStack() {
		return InkStorageItem.super.getFullStack();
	}
	
}