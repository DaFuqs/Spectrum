package de.dafuqs.spectrum.api.ink.storage;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.network.chat.*;

import java.util.*;

public class TotalCappedInkStorage extends InkStorage {
	
	protected final long maxEnergyTotal;
	protected Map<InkColor, Long> storedEnergy = new Object2LongArrayMap<>();
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
	
	@Override
	public long getRoom(InkColor color) {
		return this.maxEnergyTotal - this.currentTotal;
	}
	
	@Override
	public void fillCompletely() {
		this.storedEnergy.clear();
		
		int inkColorCount = SpectrumRegistries.INK_COLOR.size();
		long energyPerColor = this.maxEnergyTotal / inkColorCount;
		for (InkColor color : InkColors.all()) {
			this.storedEnergy.put(color, energyPerColor);
		}
		this.currentTotal = energyPerColor * inkColorCount; // in case rounding is weird
	}
	
	@Override
	public void clearContent() {
		Set<InkColor> supportedColors = this.storedEnergy.keySet();
		Map<InkColor, Long> newContent = new HashMap<>();
		for (InkColor color : supportedColors) {
			newContent.put(color, 0L);
		}
		this.storedEnergy = newContent;
		this.currentTotal = 0;
	}
	
	@Override
	public List<Component> getTooltip() {
		List<Component> tooltip = new ArrayList<>();
		long maxEnergyPerColor = getMaxPerColor();
		tooltip.add(Component.translatable("item.spectrum.ink_storage.stores_ink_per_type", Support.getShortenedNumberString(maxEnergyPerColor)));
		appendInkStoreBulletTooltips(tooltip);
		return tooltip;
	}
	
	protected void appendInkStoreBulletTooltips(List<Component> tooltip) {
		if(isEmpty()) {
			tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.empty"));
			return;
		}
		
		for (InkColor color : SpectrumRegistries.INK_COLOR) { // we are iterating them this way to preserve the ordering
			long amount = this.storedEnergy.getOrDefault(color, -1L);
			if (amount > 0) {
				tooltip.add(InkStorage.getInkStoreBulletTooltip(color, amount));
			}
		}
	}
	
}