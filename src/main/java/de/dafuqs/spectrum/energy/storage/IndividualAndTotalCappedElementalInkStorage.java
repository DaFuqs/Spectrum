package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.ElementalColor;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IndividualAndTotalCappedElementalInkStorage extends TotalCappedElementalInkStorage {
	
	protected final long maxEnergyPerColor;
	
	public IndividualAndTotalCappedElementalInkStorage(long maxEnergyTotal, long maxEnergyPerColor) {
		super(maxEnergyTotal);
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	public IndividualAndTotalCappedElementalInkStorage(long maxEnergyTotal, long maxEnergyPerColor, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, cyan, magenta, yellow, black, white);
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	public static @Nullable IndividualAndTotalCappedElementalInkStorage fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new IndividualAndTotalCappedElementalInkStorage(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount) {
		if (color instanceof ElementalColor elementalColor) {
			long currentAmount = this.storedEnergy.get(color);
			long freeTotalEnergy = this.maxEnergyTotal - this.currentTotal;
			long freeColorEnergy = this.maxEnergyPerColor - currentAmount;
			long free = Math.min(freeTotalEnergy, freeColorEnergy);
			
			if (amount > free) {
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
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.putLong("MaxEnergyPerColor", this.maxEnergyPerColor);
		compound.putLong("Cyan", this.storedEnergy.get(InkColors.CYAN));
		compound.putLong("Magenta", this.storedEnergy.get(InkColors.MAGENTA));
		compound.putLong("Yellow", this.storedEnergy.get(InkColors.YELLOW));
		compound.putLong("Black", this.storedEnergy.get(InkColors.BLACK));
		compound.putLong("White", this.storedEnergy.get(InkColors.WHITE));
		return compound;
	}
	
}