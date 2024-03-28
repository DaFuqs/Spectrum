package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.InkColor;
import de.dafuqs.spectrum.api.energy.color.InkColors;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.render.ExtendedItemBarProvider;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.fabricmc.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class InkAssortmentItem extends Item implements InkStorageItem<IndividualCappedInkStorage>, ExtendedItemBarProvider {
	
	private final long maxEnergy;
	
	public InkAssortmentItem(Settings settings, long maxEnergy) {
		super(settings);
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return IndividualCappedInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new IndividualCappedInkStorage(this.maxEnergy);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		if (storage instanceof IndividualCappedInkStorage individualCappedInkStorage) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.put("EnergyStore", individualCappedInkStorage.toNbt());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(tooltip, true);
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