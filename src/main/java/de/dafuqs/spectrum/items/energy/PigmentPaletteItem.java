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

public class PigmentPaletteItem extends SpectrumTrinketItem implements InkStorageItem<PigmentPaletteItem.PigmentPaletteInkStorage>, LoomPatternProvider {
	
	private final long maxEnergyPerColor;
	
	public PigmentPaletteItem(Settings settings, long maxEnergyPerColor) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/pigment_palette"));
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.PLAYER_ONLY;
	}
	
	@Override
	public PigmentPaletteInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return PigmentPaletteInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new PigmentPaletteInkStorage(this.maxEnergyPerColor);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		if (storage instanceof PigmentPaletteInkStorage pigmentPaletteInkStorage) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.put("EnergyStore", pigmentPaletteInkStorage.toNbt());
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

	public static class PigmentPaletteInkStorage extends IndividualCappedInkStorage {

		public PigmentPaletteInkStorage(long maxEnergyPerColor) {
			super(maxEnergyPerColor);
		}

		public PigmentPaletteInkStorage(long maxEnergyPerColor, Map<InkColor, Long> colors) {
			super(maxEnergyPerColor, colors);
		}

		public static @Nullable PigmentPaletteItem.PigmentPaletteInkStorage fromNbt(@NotNull NbtCompound compound) {
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
}