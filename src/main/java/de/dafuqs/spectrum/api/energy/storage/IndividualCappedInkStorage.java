package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

@SuppressWarnings("UnstableApiUsage")
public class IndividualCappedInkStorage extends SnapshotParticipant<IndividualCappedInkStorage.Snapshot> implements InkStorage {
	
	protected final long maxEnergyPerColor;
	protected final Map<InkColor, Long> storedEnergy = new Object2LongArrayMap<>();
	protected final ObjectSet<StorageView<InkColor>> views;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	
	// support all ink colors
	public IndividualCappedInkStorage(long maxEnergyPerColor) {
		this(maxEnergyPerColor, SpectrumRegistries.INK_COLORS);
	}
	
	// support selected ink colors
	public IndividualCappedInkStorage(long maxEnergyPerColor, Iterable<InkColor> supportedColors) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		this.currentTotal = 0;

		var set = new ObjectArraySet<InkView>();
		for (InkColor color : supportedColors) {
			this.storedEnergy.put(color, 0L);
			set.add(new InkStorage.InkView(this, color));
		}
		this.views = ObjectSets.unmodifiable(set);
	}
	
	public IndividualCappedInkStorage(long maxEnergyPerColor, Map<InkColor, Long> colors) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		this.storedEnergy.putAll(colors);

		var set = new ObjectArraySet<InkView>();
		for (Map.Entry<InkColor, Long> color : colors.entrySet()) {
			this.storedEnergy.put(color.getKey(), color.getValue());
			this.currentTotal += color.getValue();
			set.add(new InkStorage.InkView(this, color.getKey()));
		}
		this.views = ObjectSets.unmodifiable(set);
	}

	public record Snapshot(Map<InkColor, Long> colors, long currentTotal) {}
	
	@Override
	public boolean accepts(InkColor color) {
		return this.storedEnergy.containsKey(color);
	}
	
	@Override
	public long addEnergy(InkColor color, long amount, boolean simulate) {
		long resultingAmount = this.storedEnergy.get(color) + amount;
		long overflow = Math.max(0, resultingAmount - this.maxEnergyPerColor);
		if (!simulate) {
			this.currentTotal += amount - overflow;
			this.storedEnergy.put(color, resultingAmount - overflow);
		}
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
	public long drainEnergy(InkColor color, long amount, boolean simulate) {
		long storedAmount = this.storedEnergy.getOrDefault(color, 0L);
		long drainedAmount = Math.min(storedAmount, amount);
		if (!simulate) {
			this.storedEnergy.put(color, storedAmount - drainedAmount);
			this.currentTotal -= drainedAmount;
		}
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
	
	public static IndividualCappedInkStorage fromNbt(@NotNull NbtCompound compound) {
		long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
		Map<InkColor, Long> colors = InkStorage.readEnergy(compound.getCompound("Energy"));
		return new IndividualCappedInkStorage(maxEnergyPerColor, colors);
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyPerColor", this.maxEnergyPerColor);
		compound.put("Energy", InkStorage.writeEnergy(this.storedEnergy));
		return compound;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void addTooltip(List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.ink_storage.stores_ink_per_type", getShortenedNumberString(maxEnergyPerColor)));
		
		// we are iterating them this way to preserve the ordering
		for (InkColor color : SpectrumRegistries.INK_COLORS) {
			long amount = this.storedEnergy.getOrDefault(color, 0L);
			if (amount > 0) {
				InkStorage.addInkStoreBulletTooltip(tooltip, color, amount);
			}
		}
	}
	
	@Override
	public long getRoom(InkColor color) {
		return maxEnergyPerColor - this.storedEnergy.get(color);
	}
	
	@Override
	public void fillCompletely() {
		this.currentTotal = 0;
		for (InkColor color : this.storedEnergy.keySet()) {
			storedEnergy.put(color, this.maxEnergyPerColor);
			this.currentTotal += this.maxEnergyPerColor;
		}
	}
	
	@Override
	public void clear() {
		this.storedEnergy.clear();
	}
	
	public Set<InkColor> getSupportedColors() {
		return this.storedEnergy.keySet();
	}

	@Override
	public @NotNull Iterator<StorageView<InkColor>> iterator() {
		return views.iterator();
	}

	@Override
	protected Snapshot createSnapshot() {
		var map = new Object2LongArrayMap<InkColor>();
		map.putAll(storedEnergy);
		return new Snapshot(map, currentTotal);
	}

	@Override
	protected void readSnapshot(Snapshot snapshot) {
		storedEnergy.clear();
		storedEnergy.putAll(snapshot.colors);
		currentTotal = snapshot.currentTotal;
	}
}