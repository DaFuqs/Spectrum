package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.ExtendedItemBarProvider;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PigmentPaletteItem extends SpectrumTrinketItem implements InkStorageItem<PigmentPaletteItem.PigmentPaletteInkStorage>, LoomPatternProvider, ExtendedItemBarProvider {
	
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

	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}

	@Override
	public ExtendedItemBarProvider.BarSignature getSignature(@Nullable PlayerEntity player, @NotNull ItemStack stack, int index) {
		var storage = getEnergyStorage(stack);
		var colors = new ArrayList<InkColor>();

		if (player == null || storage.isEmpty())
			return ExtendedItemBarProvider.PASS;

		var time = player.getWorld().getTime() % 864000;

		for (InkColor inkColor : SpectrumRegistries.INK_COLORS) {
			if (storage.getEnergy(inkColor) > 0)
				colors.add(inkColor);
		}

		var progress = Math.round(MathHelper.clampedLerp(0, 14, (float) storage.getCurrentTotal() / storage.getMaxTotal()));
		if (colors.size() == 1) {
			var color = colors.get(0);
			return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, ColorHelper.colorVecToRGB(color.getColor()) | 0xFF000000, 2, DEFAULT_BACKGROUND_COLOR);
		}

		var delta = MinecraftClient.getInstance().getTickDelta();
		var curColor = colors.get((int) (time % (30L * colors.size()) / 30));
		var nextColor = colors.get((int) ((time % (30L * colors.size()) / 30 + 1) % colors.size()));


		var blendFactor = (((float) time + delta) % 30) / 30F;
		var blendedColor = ColorHelper.interpolate(
				curColor == InkColors.BLACK ? ColorHelper.colorIntToVec(InkColors.ALT_BLACK) : curColor.getColor(), nextColor == InkColors.BLACK ? ColorHelper.colorIntToVec(InkColors.ALT_BLACK) : nextColor.getColor(), blendFactor);

		return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, blendedColor, 2, DEFAULT_BACKGROUND_COLOR);
	}
}