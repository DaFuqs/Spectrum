package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PigmentPaletteInkStorage extends IndividualCappedInkStorage {
	
	public PigmentPaletteInkStorage(long maxEnergyPerColor) {
		super(maxEnergyPerColor);
	}
	
	public PigmentPaletteInkStorage(long maxEnergyPerColor, Map<InkColor, Long> colors) {
		super(maxEnergyPerColor, colors);
	}
	
	public static @Nullable PigmentPaletteInkStorage fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyPerColor", NbtElement.LONG_TYPE)) {
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			
			Map<InkColor, Long> colors = new HashMap<>();
			for (InkColor color : InkColor.all()) {
				colors.put(color, compound.getLong(color.toString()));
			}
			return new PigmentPaletteInkStorage(maxEnergyPerColor, colors);
		}
		return null;
	}
	
	public long addEnergy(InkColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long leftoverEnergy = super.addEnergy(color, amount);
		if (leftoverEnergy != amount) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, amount - leftoverEnergy);
		}
		return leftoverEnergy;
	}
	
	public boolean requestEnergy(InkColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		boolean success = super.requestEnergy(color, amount);
		if (success) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, -amount);
		}
		return success;
	}
	
	public long drainEnergy(InkColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long drainedAmount = super.drainEnergy(color, amount);
		if (drainedAmount != 0) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, -drainedAmount);
		}
		return drainedAmount;
	}
	
}
