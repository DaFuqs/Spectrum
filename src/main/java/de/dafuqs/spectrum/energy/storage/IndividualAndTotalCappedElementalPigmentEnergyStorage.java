package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IndividualAndTotalCappedElementalPigmentEnergyStorage extends TotalCappedElementalPigmentEnergyStorage {
	
	protected final long maxEnergyPerColor;
	
	public IndividualAndTotalCappedElementalPigmentEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor) {
		super(maxEnergyTotal);
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	public IndividualAndTotalCappedElementalPigmentEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, cyan, magenta, yellow, black, white);
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	@Override
	public long addEnergy(CMYKColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			long currentAmount = this.storedEnergy.get(color);
			long freeTotalEnergy = this.maxEnergyTotal - this.currentTotal;
			long freeColorEnergy = this.maxEnergyPerColor - currentAmount;
			long free = Math.min(freeTotalEnergy, freeColorEnergy);
			
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
	public long getMaxPerColor() {
		return this.maxEnergyPerColor;
	}
	
	public static @Nullable IndividualAndTotalCappedElementalPigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new IndividualAndTotalCappedElementalPigmentEnergyStorage(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.putLong("MaxEnergyPerColor", this.maxEnergyPerColor);
		compound.putLong("Cyan", this.storedEnergy.get(PigmentColors.CYAN));
		compound.putLong("Magenta", this.storedEnergy.get(PigmentColors.MAGENTA));
		compound.putLong("Yellow", this.storedEnergy.get(PigmentColors.YELLOW));
		compound.putLong("Black", this.storedEnergy.get(PigmentColors.BLACK));
		compound.putLong("White", this.storedEnergy.get(PigmentColors.WHITE));
		return compound;
	}
	
}