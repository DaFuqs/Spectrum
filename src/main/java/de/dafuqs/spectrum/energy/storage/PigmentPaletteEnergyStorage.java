package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PigmentPaletteEnergyStorage extends IndividualCappedSimplePigmentEnergyStorage {
	
	public PigmentPaletteEnergyStorage(long maxEnergyPerColor) {
		super(maxEnergyPerColor);
	}
	
	public PigmentPaletteEnergyStorage(long maxEnergyPerColor, Map<CMYKColor, Long> colors) {
		super(maxEnergyPerColor, colors);
	}
	
	public long addEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long leftoverEnergy = super.addEnergy(color, amount);
		if(leftoverEnergy != amount) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, amount - leftoverEnergy);
		}
		return leftoverEnergy;
	}
	
	public boolean requestEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		boolean success = super.requestEnergy(color, amount);
		if(success) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, -amount);
		}
		return success;
	}
	
	public long drainEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long drainedAmount = super.drainEnergy(color, amount);
		if(drainedAmount != 0) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, this, color, -drainedAmount);
		}
		return drainedAmount;
	}
	
	public static @Nullable PigmentPaletteEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyPerColor", NbtElement.LONG_TYPE)) {
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			
			Map<CMYKColor, Long> colors = new HashMap<>();
			for(CMYKColor color : CMYKColor.all()) {
				colors.put(color, compound.getLong(color.toString()));
			}
			return new PigmentPaletteEnergyStorage(maxEnergyPerColor, colors);
		}
		return null;
	}
	
}
