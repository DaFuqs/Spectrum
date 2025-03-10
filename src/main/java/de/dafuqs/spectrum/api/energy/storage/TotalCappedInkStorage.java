package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

public class TotalCappedInkStorage implements InkStorage {
	
	protected final long maxEnergyTotal;
	protected final Map<InkColor, Long> storedEnergy = new Object2LongArrayMap<>();
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	
	public TotalCappedInkStorage(long maxEnergyTotal, Map<InkColor, Long> energy) {
		this.maxEnergyTotal = maxEnergyTotal;

		this.currentTotal = 0;
		this.storedEnergy.putAll(energy);
		for (Map.Entry<InkColor, Long> color : energy.entrySet()) {
			this.currentTotal += color.getValue();
		}
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return true;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount) {
		long overflow = Math.max(0, amount + this.currentTotal - this.maxEnergyTotal);
		long amountToAdd = amount - overflow;
		this.currentTotal += amountToAdd;
		this.storedEnergy.put(color, this.storedEnergy.getOrDefault(color, 0L) + amountToAdd);
		return overflow;
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long amount) {
		long storedAmount = this.storedEnergy.getOrDefault(color, 0L);
		if (storedAmount < amount) {
			return false;
		} else {
			this.currentTotal -= amount;
			this.storedEnergy.put(color, storedAmount - amount);
			return true;
		}
	}
	
	@Override
	public long drainEnergy(InkColor color, long amount) {
		long storedAmount = this.storedEnergy.getOrDefault(color, 0L);
		long drainedAmount = Math.min(storedAmount, amount);
		this.storedEnergy.put(color, storedAmount - drainedAmount);
		this.currentTotal -= drainedAmount;
		return drainedAmount;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		return this.storedEnergy.getOrDefault(color, 0L);
	}
	
	@Override
	@Deprecated
	public Map<InkColor, Long> getEnergy() {
		return this.storedEnergy;
	}
	
	@Override
	@Deprecated
	public void setEnergy(Map<InkColor, Long> colors, long total) {
		this.storedEnergy.putAll(colors);
		this.currentTotal = total;
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
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.put("Energy", InkStorage.writeEnergy(this.storedEnergy));
		return compound;
	}
	
	public static TotalCappedInkStorage fromNbt(@NotNull NbtCompound compound) {
		long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
		Map<InkColor, Long> colors = InkStorage.readEnergy(compound.contains("Energy") ? compound.getCompound("Energy") : compound);
		return new TotalCappedInkStorage(maxEnergyTotal, colors);
	}
	
	@Override
	public long getRoom(InkColor color) {
		return this.maxEnergyTotal - this.currentTotal;
	}
	
	@Override
	public void fillCompletely() {
		this.storedEnergy.clear();
		
		int inkColorCount = SpectrumRegistries.INK_COLORS.size();
		long energyPerColor = this.maxEnergyTotal / inkColorCount;
		for (InkColor color : InkColors.all()) {
			this.storedEnergy.put(color, energyPerColor);
		}
		this.currentTotal = energyPerColor * inkColorCount; // in case rounding is weird
	}
	
	@Override
	public void clear() {
		this.storedEnergy.clear();
		this.currentTotal = 0;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void addTooltip(List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.total_capped_simple_pigment_energy_storage.tooltip", getShortenedNumberString(maxEnergyTotal)));
		addInkContentTooltip(tooltip);
	}
	
	protected void addInkContentTooltip(List<Text> tooltip) {
		// we are iterating them this way to preserve the ordering in which they were registered
		for (InkColor color : SpectrumRegistries.INK_COLORS) {
			long amount = this.storedEnergy.getOrDefault(color, 0L);
			if (amount > 0) {
				InkStorage.addInkStoreBulletTooltip(tooltip, color, amount);
			}
		}
	}
	
}