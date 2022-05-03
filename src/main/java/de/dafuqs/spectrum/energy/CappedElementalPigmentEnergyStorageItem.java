package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.storage.IndividualAndTotalCappedElementalPigmentEnergyStorage;
import de.dafuqs.spectrum.energy.storage.PigmentEnergyStorage;
import net.minecraft.item.ItemStack;

public interface CappedElementalPigmentEnergyStorageItem {
	
	PigmentEnergyStorage getEnergyStorage(ItemStack itemStack);
	void setEnergyStorage(ItemStack itemStack, IndividualAndTotalCappedElementalPigmentEnergyStorage storage);
	
}