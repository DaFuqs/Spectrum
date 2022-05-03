package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TotalCappedSimplePigmentEnergyStorage implements PigmentEnergyStorage {
	
	protected final long maxEnergyTotal;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	protected final Map<CMYKColor, Long> storedEnergy;
	
	public TotalCappedSimplePigmentEnergyStorage(long maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = 0;
		
		this.storedEnergy = new HashMap<>();
		for(CMYKColor color : CMYKColor.all()) {
			this.storedEnergy.put(color, 0L);
		}
	}
	
	public TotalCappedSimplePigmentEnergyStorage(long maxEnergyTotal, Map<CMYKColor, Long> colors) {
		this.maxEnergyTotal = maxEnergyTotal;
		
		this.storedEnergy = colors;
		for(Map.Entry<CMYKColor, Long> color : colors.entrySet()) {
			this.storedEnergy.put(color.getKey(), color.getValue());
			this.currentTotal += color.getValue();
		}
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return color instanceof ElementalColor;
	}
	
	@Override
	public long addEnergy(CMYKColor color, long amount) {
		long resultingAmount = this.storedEnergy.get(color) + amount;
		if(resultingAmount > this.maxEnergyTotal - this.currentTotal) {
			long overflow = resultingAmount - this.maxEnergyTotal + this.currentTotal;
			this.currentTotal = this.currentTotal + (resultingAmount - this.maxEnergyTotal);
			this.storedEnergy.put(color, this.maxEnergyTotal);
			return overflow;
		} else {
			this.currentTotal += amount;
			this.storedEnergy.put(color, resultingAmount);
			return 0;
		}
	}
	
	@Override
	public boolean requestEnergy(CMYKColor color, long amount) {
		long storedAmount = this.storedEnergy.get(color);
		if(storedAmount < amount) {
			return false;
		} else {
			this.currentTotal -= amount;
			this.storedEnergy.put(color, storedAmount - amount);
			return true;
		}
	}
	
	@Override
	public long drainEnergy(CMYKColor color, long amount) {
		long storedAmount = this.storedEnergy.get(color);
		long drainedAmount = Math.min(storedAmount, amount);
		this.storedEnergy.put(color, storedAmount - drainedAmount);
		this.currentTotal -= drainedAmount;
		return drainedAmount;
	}
	
	@Override
	public long getEnergy(CMYKColor color) {
		return this.storedEnergy.get(color);
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
		return this.currentTotal;
	}
	
	@Override
	public boolean isEmpty() {
		return this.currentTotal == 0;
	}
	
	@Override
	public boolean isFull() {
		return this.currentTotal >= this.maxEnergyTotal;
	}
	
	public static @Nullable TotalCappedSimplePigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			
			Map<CMYKColor, Long> colors = new HashMap<>();
			for(CMYKColor color : CMYKColor.all()) {
				colors.put(color, compound.getLong(color.toString()));
			}
			return new TotalCappedSimplePigmentEnergyStorage(maxEnergyTotal, colors);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		for(Map.Entry<CMYKColor, Long> color : this.storedEnergy.entrySet()) {
			compound.putLong(color.getKey().toString(), color.getValue());
		}
		return compound;
	}
	
}