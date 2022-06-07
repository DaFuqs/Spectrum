package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtistsPaletteInkStorage extends TotalCappedElementalInkStorage {
	
	public ArtistsPaletteInkStorage(long maxEnergyTotal) {
		super(maxEnergyTotal);
	}
	
	public ArtistsPaletteInkStorage(long maxEnergyTotal, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, cyan, magenta, yellow, black, white);
	}
	
	public static @Nullable ArtistsPaletteInkStorage fromNbt(@NotNull NbtCompound compound) {
		if (compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new ArtistsPaletteInkStorage(maxEnergyTotal, cyan, magenta, yellow, black, white);
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
