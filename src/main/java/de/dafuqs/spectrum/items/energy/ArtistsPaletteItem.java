package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtistsPaletteItem extends SpectrumTrinketItem implements InkStorageItem<ArtistsPaletteItem.ArtistsPaletteInkStorage>, LoomPatternProvider {
	
	private final long maxEnergyTotal;
	
	public ArtistsPaletteItem(Settings settings, long maxEnergyTotal) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/artists_palette"));
		this.maxEnergyTotal = maxEnergyTotal;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.PLAYER_ONLY;
	}
	
	@Override
	public ArtistsPaletteInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return ArtistsPaletteInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new ArtistsPaletteInkStorage(this.maxEnergyTotal);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		if (storage instanceof ArtistsPaletteInkStorage artistsPaletteInkStorage) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.put("EnergyStore", artistsPaletteInkStorage.toNbt());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.pigment_palette.tooltip.target").formatted(Formatting.GRAY));
		getEnergyStorage(stack).addTooltip(tooltip, true);
		addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PALETTE;
	}

	public static class ArtistsPaletteInkStorage extends TotalCappedElementalInkStorage {

		public ArtistsPaletteInkStorage(long maxEnergyTotal) {
			super(maxEnergyTotal);
		}

		public ArtistsPaletteInkStorage(long maxEnergyTotal, long cyan, long magenta, long yellow, long black, long white) {
			super(maxEnergyTotal, cyan, magenta, yellow, black, white);
		}

		public static @Nullable ArtistsPaletteItem.ArtistsPaletteInkStorage fromNbt(@NotNull NbtCompound compound) {
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
}