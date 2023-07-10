package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.energy.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkDrainTrinketItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkStorage> {
	
	/**
	 * TODO: set to the original value again, once ink networking is in. Currently the original max value cannot be achieved.
	 * Players WILL grind out that amount of pigment in some way and will then complain
	 */
	public static final int MAX_INK = 3276800; // 1677721600;
	public final InkColor inkColor;
	
	public InkDrainTrinketItem(Settings settings, Identifier unlockIdentifier, InkColor inkColor) {
		super(settings, unlockIdentifier);
		this.inkColor = inkColor;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		
		if (storedInk >= MAX_INK) {
			tooltip.add(Text.translatable("spectrum.tooltip.ink_drain.tooltip.maxed_out").formatted(Formatting.GRAY));
		} else {
			long nextStepInk;
			int pow = 0;
			do {
				nextStepInk = (long) (100 * Math.pow(8, pow));
				pow++;
			} while (storedInk >= nextStepInk);
			
			tooltip.add(Text.translatable("spectrum.tooltip.ink_drain.tooltip.ink_for_next_step." + inkStorage.getStoredColor().toString(), Support.getShortenedNumberString(nextStepInk - storedInk)).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return isMaxedOut(stack);
	}
	
	private boolean isMaxedOut(ItemStack stack) {
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		return storedInk >= MAX_INK;
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return isMaxedOut(stack) ? Rarity.EPIC : super.getRarity(stack);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.NEVER;
	}
	
	@Override
	public FixedSingleInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return FixedSingleInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new FixedSingleInkStorage(MAX_INK, inkColor);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, FixedSingleInkStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	public ItemStack getFullStack() {
		return InkStorageItem.super.getFullStack();
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			stacks.add(getFullStack());
		}
	}
	
}