package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.PigmentColor;

public class SinglePigmentEnergyStorage implements PigmentEnergyStorage {
	
	private final int maxEnergyTotal;
	
	private CMYKColor storedColor;
	private int storedEnergy;
	
	/**
	 * Stores a single type of Pigment Energy
	 * Can only be filled with a new type if it is empty
	 **/
	public SinglePigmentEnergyStorage(int maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.storedColor = PigmentColor.CYAN;
		this.storedEnergy = 0;
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return this.storedEnergy == 0 || this.storedColor == color;
	}
	
	@Override
	public int addEnergy(CMYKColor color, int amount) {
		if(color == storedColor) {
			int resultingAmount = this.storedEnergy + amount;
			this.storedEnergy = resultingAmount;
			if(resultingAmount > this.maxEnergyTotal) {
				int overflow = this.storedEnergy - this.maxEnergyTotal;
				this.storedEnergy = this.maxEnergyTotal;
				return overflow;
			}
			return 0;
		} else if(this.storedEnergy == 0) {
			this.storedColor = color;
			this.storedEnergy = amount;
		}
		return amount;
	}
	
	/*@Override
	public boolean requestEnergy(Map<CMYKColor, Integer> colors) {
		if (colors.size() == 0) {
			return true;
		} else if (colors.size() == 1 && colors.containsKey(this.storedColor)) {
			int amount = colors.get(this.storedColor);
			if(this.storedEnergy >= amount) {
				this.storedEnergy -= amount;
				return true;
			}
		}
		return false;
	}*/
	
	@Override
	public boolean requestEnergy(CMYKColor color, int amount) {
		if (color == this.storedColor && amount >= this.storedEnergy) {
			this.storedEnergy -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	public int drainEnergy(CMYKColor color, int amount) {
		if (color == this.storedColor) {
			int drainedAmount = Math.min(this.storedEnergy, amount);
			this.storedEnergy -= drainedAmount;
			return drainedAmount;
		} else {
			return 0;
		}
	}
	
	@Override
	public int getEnergy(CMYKColor color) {
		if (color == this.storedColor) {
			return this.storedEnergy;
		} else {
			return 0;
		}
	}
	
	/*@Override
	public Map<ICMYKColor, Integer> getEnergy() {
		return Map.of(this.storedColor, this.storedEnergy);
	}*/
	
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
		return this.storedEnergy;
	}
	
	@Override
	public boolean isEmpty() {
		return this.storedEnergy == 0;
	}
	
	@Override
	public boolean isFull() {
		return this.storedEnergy >= this.maxEnergyTotal;
	}
	
}