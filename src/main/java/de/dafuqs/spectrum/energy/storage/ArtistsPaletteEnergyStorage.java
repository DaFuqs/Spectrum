package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static de.dafuqs.spectrum.helpers.Support.getShortenedNumberString;

public class ArtistsPaletteEnergyStorage extends TotalCappedElementalPigmentEnergyStorage {
	
	public ArtistsPaletteEnergyStorage(long maxEnergyTotal) {
		super(maxEnergyTotal);
	}
	
	public ArtistsPaletteEnergyStorage(long maxEnergyTotal, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, cyan, magenta, yellow, black, white);
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
	
	public static @Nullable ArtistsPaletteEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new ArtistsPaletteEnergyStorage(maxEnergyTotal, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
}
