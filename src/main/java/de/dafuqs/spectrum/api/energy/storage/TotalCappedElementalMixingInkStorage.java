package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import net.fabricmc.api.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

public class TotalCappedElementalMixingInkStorage extends TotalCappedInkStorage {
	
	public TotalCappedElementalMixingInkStorage(long maxEnergyTotal, Map<InkColor, Long> storedEnergy) {
		super(maxEnergyTotal, storedEnergy);
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return color.isIn(InkColorTags.ELEMENTAL_COLORS);
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long amount) {
		if (color.isIn(InkColorTags.ELEMENTAL_COLORS)) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(color);
			if (storedAmount < amount) {
				return false;
			} else {
				this.currentTotal -= amount;
				this.storedEnergy.put(color, storedAmount - amount);
				return true;
			}
		}
		
		// mix!
		Optional<Map<InkColor, Float>> requiredElementals = InkColorMixes.getColorsToMix(color);
		if (requiredElementals.isEmpty()) {
			return false;
		}
		
		// check if we have enough
		for (Map.Entry<InkColor, Float> entry : requiredElementals.get().entrySet()) {
			long storedAmount = this.storedEnergy.get(entry.getKey());
			long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
			if (storedAmount < requiredAmount) {
				return false;
			}
		}
		
		// yes, we got stored enough. Drain
		for (Map.Entry<InkColor, Float> entry : requiredElementals.get().entrySet()) {
			long storedAmount = this.storedEnergy.get(entry.getKey());
			long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
			this.currentTotal -= requiredAmount;
			this.storedEnergy.put(entry.getKey(), storedAmount - requiredAmount);
		}
		return true;
	}
	
	@Override
	public long drainEnergy(InkColor color, long amount) {
		if (color.isIn(InkColorTags.ELEMENTAL_COLORS)) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(color);
			long drainedAmount = Math.min(storedAmount, amount);
			this.storedEnergy.put(color, storedAmount - drainedAmount);
			
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		}
		
		// mix!
		Optional<Map<InkColor, Float>> requiredElementals = InkColorMixes.getColorsToMix(color);
		if (requiredElementals.isEmpty()) {
			return 0;
		}
		
		// calculate the max amount that can be drained over all colors
		float percentageAbleToDrain = 1.0F;
		for (Map.Entry<InkColor, Float> entry : requiredElementals.get().entrySet()) {
			long storedAmount = this.storedEnergy.get(entry.getKey());
			long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
			if (storedAmount < requiredAmount) {
				percentageAbleToDrain = Math.min(percentageAbleToDrain, storedAmount / (float) requiredAmount);
			}
		}
		
		// drain
		for (Map.Entry<InkColor, Float> entry : requiredElementals.get().entrySet()) {
			long storedAmount = this.storedEnergy.get(entry.getKey());
			long drainedAmount = (int) Math.ceil(entry.getValue() * amount * percentageAbleToDrain);
			this.storedEnergy.put(entry.getKey(), storedAmount - drainedAmount);
		}
		
		long drainedAmount = (int) Math.floor(percentageAbleToDrain * amount);
		this.currentTotal -= drainedAmount;
		return drainedAmount;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		if (color.isIn(InkColorTags.ELEMENTAL_COLORS)) {
			// can be output directly
			return this.storedEnergy.get(color);
		}
		
		// mix!
		long maxAmount = Long.MAX_VALUE;
		Optional<Map<InkColor, Float>> requiredElementals = InkColorMixes.getColorsToMix(color);
		if (requiredElementals.isEmpty()) {
			return 0;
		}
		
		for (Map.Entry<InkColor, Float> entry : requiredElementals.get().entrySet()) {
			long mixedAmount = (long) Math.floor(entry.getValue() * this.storedEnergy.get(entry.getKey()));
			maxAmount = Math.min(maxAmount, mixedAmount);
		}
		return maxAmount;
	}
	
	public static TotalCappedElementalMixingInkStorage fromNbt(@NotNull NbtCompound compound) {
		long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
		Map<InkColor, Long> energy = InkStorage.readEnergy(compound.contains("Energy") ? compound.getCompound("Energy") : compound);
		return new TotalCappedElementalMixingInkStorage(maxEnergyTotal, energy);
	}
	
	@Override
	public void fillCompletely() {
		this.storedEnergy.clear();
		List<InkColor> elementals = InkColors.elementals();
		if (elementals.isEmpty()) {
			// in case the tag is empty or something queries that method
			// before tags are fully synced
			// looking at you, Mouse Wheelie
			return;
		}
		
		long energyPerColor = this.maxEnergyTotal / elementals.size();
		for (InkColor color : elementals) {
			this.storedEnergy.put(color, energyPerColor);
		}
		this.currentTotal = energyPerColor * elementals.size(); // in case rounding is weird
	}
	
	@Override
	public void clear() {
		this.storedEnergy.clear();
	}
	
	@Override
	public long getRoom(InkColor color) {
		if (color.isIn(InkColorTags.ELEMENTAL_COLORS)) {
			return this.maxEnergyTotal - this.currentTotal;
		}
		return 0;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void addTooltip(List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.artists_palette.tooltip", getShortenedNumberString(this.maxEnergyTotal)));
		tooltip.add(Text.translatable("item.spectrum.artists_palette.tooltip.mix_on_demand"));
		addInkContentTooltip(tooltip);
	}
	
}
