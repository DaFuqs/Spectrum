package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

public class FixedSingleInkStorage extends SingleInkStorage {
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color) {
		super(maxEnergy);
		this.storedColor = color;
	}
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color, long amount) {
		super(maxEnergy, color, amount);
	}
	
	public static @Nullable FixedSingleInkStorage fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			InkColor color = InkColor.of(compound.getString("Color"));
			long amount = compound.getLong("Amount");
			return new FixedSingleInkStorage(maxEnergyTotal, color, amount);
		}
		return null;
	}
	
	@Override
	public InkColor getStoredColor() {
		return storedColor;
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