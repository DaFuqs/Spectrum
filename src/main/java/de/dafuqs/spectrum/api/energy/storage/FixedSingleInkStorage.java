package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class FixedSingleInkStorage extends SingleInkStorage {
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color) {
		super(maxEnergy, color);
	}
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color, long amount) {
		super(maxEnergy, color, amount);
	}
	
	public static FixedSingleInkStorage fromNbt(@NotNull NbtCompound compound) {
		long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
		Optional<InkColor> color = InkColor.ofIdString(compound.getString("Color"));
		if (color.isPresent()) {
			long amount = compound.getLong("Amount");
			return new FixedSingleInkStorage(maxEnergyTotal, color.get(), amount);
		}
		return new FixedSingleInkStorage(maxEnergyTotal, InkColors.CYAN, 0);
	}

	@Override
	public boolean accepts(InkColor color) {
		return this.variant == color;
	}
	
	@Override
	public long getRoom(InkColor color) {
		if (this.variant == color) {
			return this.maxEnergy - this.amount;
		} else {
			return 0;
		}
	}

	@Override
	public long addEnergy(InkColor color, long amount, boolean simulate) {
		long overflow = color == variant ? Math.max(0, this.amount + amount - maxEnergy) : amount;
		if (!simulate) this.amount += amount - overflow;
		return overflow;
	}

	@Override
	public long getCapacity(InkColor variant) {
		return this.variant == variant ? maxEnergy : 0;
	}
}