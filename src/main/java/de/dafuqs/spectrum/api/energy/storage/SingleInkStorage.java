package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

@SuppressWarnings("UnstableApiUsage")
public class SingleInkStorage extends SingleVariantStorage<InkColor> implements InkStorage {
	
	protected final long maxEnergy;
	
	/**
	 * Stores a single type of Pigment Energy
	 * Can only be filled with a new type if it is empty
	 **/
	public SingleInkStorage(long maxEnergy) {
		this(maxEnergy, InkColors.BLANK, 0);
	}

	public SingleInkStorage(long maxEnergy, InkColor color) {
		this(maxEnergy, color, 0);
	}
	
	public SingleInkStorage(long maxEnergy, InkColor color, long amount) {
		this.maxEnergy = maxEnergy;
		this.variant = color;
		this.amount = amount;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergy);
		compound.putString("Color", this.variant.getID().toString());
		compound.putLong("Amount", this.amount);
		return compound;
	}
	
	public static SingleInkStorage fromNbt(@NotNull NbtCompound compound) {
		long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
		Optional<InkColor> color = InkColor.ofIdString(compound.getString("Color"));
		if (color.isPresent()) {
			long amount = compound.getLong("Amount");
			return new SingleInkStorage(maxEnergyTotal, color.get(), amount);
		}
		return new SingleInkStorage(maxEnergyTotal, InkColors.CYAN, 0);
	}
	
	public InkColor getStoredColor() {
		return variant;
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return this.amount == 0 || this.variant == color;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount, boolean simulate) {
		long overflow = color == variant ? Math.max(0, this.amount + amount - maxEnergy) : amount;
		if (!simulate) {
			if (this.amount == 0) this.variant = color;
			this.amount += amount - overflow;
		}
		return overflow;
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long amount) {
		if (color == this.variant && amount >= this.amount) {
			this.amount -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public long drainEnergy(InkColor color, long amount, boolean simulate) {
		long drainedAmount = color == this.variant ? Math.min(this.amount, amount) : 0;
		if (!simulate) this.amount -= drainedAmount;
		return drainedAmount;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		if (color == this.variant) {
			return this.amount;
		} else {
			return 0;
		}
	}
	
	@Override
	@Deprecated
	public Map<InkColor, Long> getEnergy() {
		return Map.of(this.variant, this.amount);
	}
	
	@Override
	@Deprecated
	public void setEnergy(Map<InkColor, Long> colors, long total) {
		for (Map.Entry<InkColor, Long> color : colors.entrySet()) {
			long value = color.getValue();
			if (value > 0) {
				this.variant = color.getKey();
				this.amount = color.getValue();
			}
		}
	}
	
	@Override
	public long getMaxTotal() {
		return this.maxEnergy;
	}
	
	@Override
	public long getMaxPerColor() {
		return this.maxEnergy;
	}
	
	@Override
	public long getCurrentTotal() {
		return this.amount;
	}
	
	@Override
	public boolean isEmpty() {
		return this.amount == 0;
	}
	
	@Override
	public boolean isFull() {
		return this.amount >= this.maxEnergy;
	}
	
	@Override
	public void addTooltip(List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.ink_storage.stores_up_to_ink_per_type", getShortenedNumberString(this.maxEnergy)));
		if (this.amount > 0) {
			InkStorage.addInkStoreBulletTooltip(tooltip, this.variant, this.amount);
		}
	}
	
	@Override
	public long getRoom(InkColor color) {
		if (this.amount == 0 || this.variant == color) {
			return this.maxEnergy - this.amount;
		} else {
			return 0;
		}
	}
	
	@Override
	public void fillCompletely() {
		this.amount = this.maxEnergy;
	}
	
	@Override
	public void clear() {
		this.amount = 0;
	}
	
	public void convertColor(InkColor color) {
		this.variant = color;
	}

	// transfer api womble
	@Override
	protected InkColor getBlankVariant() {
		return InkColor.blank();
	}

	@Override
	public long getCapacity(InkColor variant) {
		return this.amount == 0 || this.variant == variant ? this.maxEnergy : 0;
	}

	@Override
	protected boolean canInsert(InkColor variant) {
		return accepts(variant);
	}

	@Override
	protected boolean canExtract(InkColor variant) {
		return accepts(variant);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putString("color", variant.getID().toString());
		nbt.putLong("amount", amount);
	}
}