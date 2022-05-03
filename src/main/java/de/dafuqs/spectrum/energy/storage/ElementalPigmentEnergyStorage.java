package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.CompoundColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ElementalPigmentEnergyStorage implements PigmentEnergyStorage {
	
	protected final long maxEnergyTotal;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	protected final Map<ElementalColor, Long> storedEnergy = new HashMap<>();
	
	public ElementalPigmentEnergyStorage(long maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = 0;

		for(ElementalColor color : CMYKColor.elementals()) {
			this.storedEnergy.put(color, 0L);
		}
	}
	
	public ElementalPigmentEnergyStorage(long maxEnergyTotal, long cyan, long magenta, long yellow, long black, long white) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = cyan+magenta+yellow+black+white;
		
		this.storedEnergy.put(PigmentColors.CYAN, cyan);
		this.storedEnergy.put(PigmentColors.MAGENTA, magenta);
		this.storedEnergy.put(PigmentColors.YELLOW, yellow);
		this.storedEnergy.put(PigmentColors.BLACK, black);
		this.storedEnergy.put(PigmentColors.WHITE, white);
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return color instanceof ElementalColor;
	}
	
	@Override
	public long addEnergy(CMYKColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			long resultingAmount = this.storedEnergy.get(color) + amount;
			if(resultingAmount > this.maxEnergyTotal) {
				long overflow = resultingAmount - this.maxEnergyTotal + this.currentTotal;
				this.currentTotal = this.currentTotal + (resultingAmount - this.maxEnergyTotal);
				this.storedEnergy.put(elementalColor, this.maxEnergyTotal);
				return overflow;
			} else {
				this.currentTotal += amount;
				this.storedEnergy.put(elementalColor, resultingAmount);
				return 0;
			}
		}
		return amount;
	}
	
	@Override
	public boolean requestEnergy(CMYKColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(elementalColor);
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
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					return false;
				}
			}
			
			// yes, we got stored enough. Drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				this.currentTotal -= requiredAmount;
				this.storedEnergy.put(entry.getKey(), storedAmount - requiredAmount);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public long drainEnergy(CMYKColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(elementalColor);
			long drainedAmount = Math.min(storedAmount, amount);
			this.storedEnergy.put(elementalColor, storedAmount - drainedAmount);
			
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			
			// calculate the max amount that can be drained over all colors
			float percentageAbleToDrain = 1.0F;
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					percentageAbleToDrain = Math.min(percentageAbleToDrain, storedAmount / (float) requiredAmount);
				}
			}
			
			// drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long drainedAmount = (int) Math.ceil(entry.getValue() * amount * percentageAbleToDrain);
				this.storedEnergy.put(entry.getKey(), storedAmount - drainedAmount);
			}
			
			long drainedAmount = (int) Math.floor(percentageAbleToDrain * amount);
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		}
		return 0;
	}
	
	@Override
	public long getEnergy(CMYKColor color) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			return this.storedEnergy.get(elementalColor);
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			long maxAmount = Long.MAX_VALUE;
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			for (Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long mixedAmount = (long) Math.floor(entry.getValue() * this.storedEnergy.get(entry.getKey()));
				maxAmount = Math.min(maxAmount, mixedAmount);
			}
			return maxAmount;
		}
		return 0;
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
	
	public static @Nullable ElementalPigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new ElementalPigmentEnergyStorage(maxEnergyTotal, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.putLong("Cyan", this.storedEnergy.get(PigmentColors.CYAN));
		compound.putLong("Magenta", this.storedEnergy.get(PigmentColors.MAGENTA));
		compound.putLong("Yellow", this.storedEnergy.get(PigmentColors.YELLOW));
		compound.putLong("Black", this.storedEnergy.get(PigmentColors.BLACK));
		compound.putLong("White", this.storedEnergy.get(PigmentColors.WHITE));
		return compound;
	}
	
}