package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PigmentPaletteEnergyStorage extends CappedElementalPigmentEnergyStorage {
	
	public PigmentPaletteEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor) {
		super(maxEnergyTotal, maxEnergyPerColor);
	}
	
	public PigmentPaletteEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
	}
	
	public long addEnergy(CMYKColor color, long amount, ServerPlayerEntity serverPlayerEntity) {
		long leftoverEnergy = super.addEnergy(color, amount);
		if(leftoverEnergy != amount) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, this, amount - leftoverEnergy);
		}
		return leftoverEnergy;
	}
	
	public boolean requestEnergy(CMYKColor color, long amount, ServerPlayerEntity serverPlayerEntity) {
		boolean success = super.requestEnergy(color, amount);
		if(success) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, this, -amount);
		}
		return success;
	}
	
	public long drainEnergy(CMYKColor color, long amount, ServerPlayerEntity serverPlayerEntity) {
		long drainedAmount = super.drainEnergy(color, amount);
		if(drainedAmount != 0) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, this, -drainedAmount);
		}
		return drainedAmount;
	}
	
	public static @Nullable CappedElementalPigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new PigmentPaletteEnergyStorage(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
}
