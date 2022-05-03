package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SinglePigmentEnergyStorage implements PigmentEnergyStorage {
	
	private final long maxEnergyTotal;
	
	private CMYKColor storedColor;
	private long storedEnergy;
	
	/**
	 * Stores a single type of Pigment Energy
	 * Can only be filled with a new type if it is empty
	 **/
	public SinglePigmentEnergyStorage(int maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.storedColor = PigmentColors.CYAN;
		this.storedEnergy = 0;
	}
	
	public SinglePigmentEnergyStorage(long maxEnergyTotal, CMYKColor color, long amount) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.storedColor = color;
		this.storedEnergy = amount;
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return this.storedEnergy == 0 || this.storedColor == color;
	}
	
	@Override
	public long addEnergy(CMYKColor color, long amount) {
		if(color == storedColor) {
			long resultingAmount = this.storedEnergy + amount;
			this.storedEnergy = resultingAmount;
			if(resultingAmount > this.maxEnergyTotal) {
				long overflow = this.storedEnergy - this.maxEnergyTotal;
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
	
	@Override
	public boolean requestEnergy(CMYKColor color, long amount) {
		if (color == this.storedColor && amount >= this.storedEnergy) {
			this.storedEnergy -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	public long drainEnergy(CMYKColor color, long amount) {
		if (color == this.storedColor) {
			long drainedAmount = Math.min(this.storedEnergy, amount);
			this.storedEnergy -= drainedAmount;
			return drainedAmount;
		} else {
			return 0;
		}
	}
	
	@Override
	public long getEnergy(CMYKColor color) {
		if (color == this.storedColor) {
			return this.storedEnergy;
		} else {
			return 0;
		}
	}
	
	@Override
	public long getMaxTotal() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public long getMaxPerColor() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public long getCurrentTotal() {
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
	
	public static @Nullable SinglePigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			CMYKColor color = CMYKColor.of(compound.getString("Color"));
			long amount = compound.getLong("Amount");
			return new SinglePigmentEnergyStorage(maxEnergyTotal, color, amount);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.putString("Color", this.storedColor.toString());
		compound.putLong("Amount", this.storedEnergy);
		return compound;
	}
	
}