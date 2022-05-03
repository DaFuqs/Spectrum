package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.CompoundColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class ElementalPigmentEnergyStorage implements PigmentEnergyStorage {
	
	protected final int maxEnergyTotal;
	protected int currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	protected final Map<ElementalColor, Integer> storedEnergy;
	
	public ElementalPigmentEnergyStorage(int maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = 0;
		
		this.storedEnergy = new HashMap<>();
		for(ElementalColor color : CMYKColor.elementals()) {
			this.storedEnergy.put(color, 0);
		}
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return color instanceof ElementalColor;
	}
	
	@Override
	public int addEnergy(CMYKColor color, int amount) {
		if(color instanceof ElementalColor elementalColor) {
			int resultingAmount = this.storedEnergy.get(color) + amount;
			if(resultingAmount > this.maxEnergyTotal) {
				int overflow = resultingAmount - this.maxEnergyTotal;
				this.storedEnergy.put(elementalColor, this.maxEnergyTotal);
				return overflow;
			} else {
				this.storedEnergy.put(elementalColor, resultingAmount);
				return 0;
			}
		}
		return amount;
	}
	
	@Override
	public boolean requestEnergy(CMYKColor color, int amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			int storedAmount = this.storedEnergy.get(elementalColor);
			if(storedAmount < amount) {
				return false;
			} else {
				this.currentTotal -= amount;
				this.storedEnergy.put(elementalColor, storedAmount - amount);
				return true;
			}
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			
			// check if we have enough
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				int storedAmount = this.storedEnergy.get(entry.getKey());
				int requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					return false;
				}
			}
			
			// yes, we got stored enough. Drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				int storedAmount = this.storedEnergy.get(entry.getKey());
				int requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				this.currentTotal -= requiredAmount;
				this.storedEnergy.put(entry.getKey(), storedAmount - requiredAmount);
			}
			return true;
		}
		return false;
	}
	
	public int drainEnergy(CMYKColor color, int amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			int storedAmount = this.storedEnergy.get(elementalColor);
			int drainedAmount = Math.min(storedAmount, amount);
			this.storedEnergy.put(elementalColor, storedAmount - drainedAmount);
			
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			
			// calculate the max amount that can be drained over all colors
			float percentageAbleToDrain = 1.0F;
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				int storedAmount = this.storedEnergy.get(entry.getKey());
				int requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					percentageAbleToDrain = Math.min(percentageAbleToDrain, storedAmount / (float) requiredAmount);
				}
			}
			
			// drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				int storedAmount = this.storedEnergy.get(entry.getKey());
				int drainedAmount = (int) Math.ceil(entry.getValue() * amount * percentageAbleToDrain);
				this.storedEnergy.put(entry.getKey(), storedAmount - drainedAmount);
			}
			
			int drainedAmount = (int) Math.floor(percentageAbleToDrain * amount);
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		}
		return 0;
	}
	
	@Override
	public int getEnergy(CMYKColor color) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			return this.storedEnergy.get(elementalColor);
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			int maxAmount = Integer.MAX_VALUE;
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			for (Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				int mixedAmount = (int) Math.floor(entry.getValue() * this.storedEnergy.get(entry.getKey()));
				maxAmount = Math.min(maxAmount, mixedAmount);
			}
			return maxAmount;
		}
		return 0;
	}
	
	@Override
	public int getMaxTotal() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public int getMaxPerColor() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public int getCurrentTotal() {
		throw new NotImplementedException("TODO");
	}
	
	@Override
	public boolean isEmpty() {
		return this.currentTotal == 0;
	}
	
	@Override
	public boolean isFull() {
		return this.currentTotal >= this.maxEnergyTotal;
	}
	
	// should never be necessary
	public void reCalculateCurrentTotal() {
		this.currentTotal = 0;
		for(int value : this.storedEnergy.values()) {
			this.currentTotal += value;
		}
	}
	
}