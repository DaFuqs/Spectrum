package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FixedSingleInkDrain extends SingleInkStorage {
	
	public FixedSingleInkDrain(long maxEnergy, InkColor color) {
		super(maxEnergy);
		this.storedColor = color;
	}
	
	public FixedSingleInkDrain(long maxEnergy, InkColor color, long amount) {
		super(maxEnergy, color, amount);
	}
	
	public InkColor getStoredColor() {
		return storedColor;
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return this.storedColor == color;
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long amount) {
		return false;
	}
	
	public long drainEnergy(InkColor color, long amount) {
		return 0;
	}
	
	public static @Nullable FixedSingleInkDrain fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			InkColor color = InkColor.of(compound.getString("Color"));
			long amount = compound.getLong("Amount");
			return new FixedSingleInkDrain(maxEnergyTotal, color, amount);
		}
		return null;
	}
	
}