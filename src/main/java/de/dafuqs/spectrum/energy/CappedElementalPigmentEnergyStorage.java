package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import org.apache.commons.lang3.NotImplementedException;

public class CappedElementalPigmentEnergyStorage extends ElementalPigmentEnergyStorage {
	
	private final int maxEnergyPerColor;
	
	public CappedElementalPigmentEnergyStorage(int maxEnergyTotal, int maxEnergyPerColor) {
		super(maxEnergyTotal);
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	@Override
	public int addEnergy(CMYKColor color, int amount) {
		if(color instanceof ElementalColor elementalColor) {
			int currentAmount = this.storedEnergy.get(color);
			
			int freeTotalEnergy = this.maxEnergyTotal - this.currentTotal;
			int freeColorEnergy = this.maxEnergyPerColor - currentAmount;
			int free = Math.min(freeTotalEnergy, freeColorEnergy);
			
			if(amount > free) {
				// overflow
				this.storedEnergy.put(elementalColor, currentAmount + free);
				return amount - free;
			} else {
				// fits nicely
				this.storedEnergy.put(elementalColor, currentAmount + amount);
				return 0;
			}
		}
		return amount;
	}
	
	@Override
	public int getMaxPerColor() {
		return this.maxEnergyPerColor;
	}
	
}