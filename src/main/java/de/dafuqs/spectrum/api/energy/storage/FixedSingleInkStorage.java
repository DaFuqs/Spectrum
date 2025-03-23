package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FixedSingleInkStorage extends SingleInkStorage {
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color) {
		super(maxEnergy);
		this.storedColor = color;
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
		return this.storedColor == color;
	}
	
	@Override
	public long getRoom(InkColor color) {
		if (this.storedColor == color) {
			return this.maxEnergy - this.storedEnergy;
		} else {
			return 0;
		}
	}
	
}