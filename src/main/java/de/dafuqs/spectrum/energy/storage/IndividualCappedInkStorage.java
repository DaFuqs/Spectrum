package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.color.InkColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.getShortenedNumberString;

public class IndividualCappedInkStorage implements InkStorage {
	
	protected final long maxEnergyPerColor;
	protected final Map<InkColor, Long> storedEnergy;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	
	// support all ink colors
	public IndividualCappedInkStorage(long maxEnergyPerColor) {
		this(maxEnergyPerColor, new HashSet<>(InkColor.all()));
	}
	
	// support selected ink colors
	public IndividualCappedInkStorage(long maxEnergyPerColor, Set<InkColor> supportedColors) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		this.currentTotal = 0;
		
		this.storedEnergy = new HashMap<>();
		for (InkColor color : supportedColors) {
			this.storedEnergy.put(color, 0L);
		}
	}
	
	public IndividualCappedInkStorage(long maxEnergyPerColor, Map<InkColor, Long> colors) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		this.storedEnergy = colors;
		
		for (Map.Entry<InkColor, Long> color : colors.entrySet()) {
			this.storedEnergy.put(color.getKey(), color.getValue());
			this.currentTotal += color.getValue();
		}
	}
	
	public static @Nullable IndividualCappedInkStorage fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyPerColor", NbtElement.LONG_TYPE)) {
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			
			Map<InkColor, Long> colors = new HashMap<>();
			for (InkColor color : InkColor.all()) {
				if (compound.contains(color.toString())) {
					colors.put(color, compound.getLong(color.toString()));
				}
			}
			return new IndividualCappedInkStorage(maxEnergyPerColor, colors);
		}
		return null;
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return this.storedEnergy.containsKey(color);
	}
	
	@Override
	public long addEnergy(InkColor color, long amount) {
		long resultingAmount = this.storedEnergy.get(color) + amount;
		if (resultingAmount > this.maxEnergyPerColor) {
			long overflow = resultingAmount - this.maxEnergyPerColor;
			this.currentTotal = this.maxEnergyPerColor;
			this.storedEnergy.put(color, this.maxEnergyPerColor);
			return overflow;
		} else {
			this.currentTotal += amount;
			this.storedEnergy.put(color, resultingAmount);
			return 0;
		}
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
		return this.maxEnergyPerColor * this.storedEnergy.size();
	}
	
	@Override
	public long getMaxPerColor() {
		return this.maxEnergyPerColor;
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
		return this.currentTotal >= this.getMaxTotal();
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyPerColor", this.maxEnergyPerColor);
		for (Map.Entry<InkColor, Long> color : this.storedEnergy.entrySet()) {
			compound.putLong(color.getKey().toString(), color.getValue());
		}
		return compound;
	}
	
	@Environment(EnvType.CLIENT)
	public void addTooltip(List<Text> tooltip, boolean includeHeader) {
		if (includeHeader) {
			tooltip.add(Text.translatable("item.spectrum.pigment_palette.tooltip", getShortenedNumberString(maxEnergyPerColor)));
		}
		for (Map.Entry<InkColor, Long> color : this.storedEnergy.entrySet()) {
			if (color.getValue() > 0) {
				tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.bullet." + color.getKey().toString().toLowerCase(Locale.ROOT), getShortenedNumberString(color.getValue())));
			}
		}
	}
	
	@Override
	public long getRoom(InkColor color) {
		return maxEnergyPerColor - this.storedEnergy.get(color);
	}
	
	public void fillCompletely() {
		this.currentTotal = 0;
		for (InkColor color : this.storedEnergy.keySet()) {
			storedEnergy.put(color, this.maxEnergyPerColor);
			this.currentTotal += this.maxEnergyPerColor;
		}
	}
	
	@Override
	public void clear() {
		this.storedEnergy.replaceAll((c, v) -> 0L);
	}
	
	public Set<InkColor> getSupportedColors() {
		return this.storedEnergy.keySet();
	}
	
}