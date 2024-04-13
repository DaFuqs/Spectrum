package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.ExtendedItemBarProvider;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.entry.*;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkFlaskItem extends Item implements InkStorageItem<SingleInkStorage>, LoomPatternProvider, ExtendedItemBarProvider {
	
	private final long maxEnergy;
	
	public InkFlaskItem(Settings settings, long maxEnergy) {
		super(settings);
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public SingleInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return SingleInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new SingleInkStorage(this.maxEnergy);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		if (storage instanceof SingleInkStorage singleInkStorage) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.put("EnergyStore", singleInkStorage.toNbt());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(tooltip, true);
		addBannerPatternProviderTooltip(tooltip);
	}
	
	public ItemStack getFullStack(InkColor color) {
		ItemStack stack = this.getDefaultStack();
		SingleInkStorage storage = getEnergyStorage(stack);
		storage.fillCompletely();
		storage.convertColor(color);
		setEnergyStorage(stack, storage);
		return stack;
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.INK_FLASK;
	}

	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}

	@Override
	public BarSignature getSignature(@Nullable PlayerEntity player, @NotNull ItemStack stack, int index) {
		var storage = getEnergyStorage(stack);
		var color = storage.getStoredColor();

		if (storage.isEmpty())
			return ExtendedItemBarProvider.PASS;

		var progress = Math.round(MathHelper.clampedLerp(0, 14, (float) storage.getCurrentTotal() / storage.getMaxTotal()));
		return new BarSignature(1, 13, 14, progress, 1, ColorHelper.colorVecToRGB(color == InkColors.BLACK ? ColorHelper.colorIntToVec(InkColors.ALT_BLACK) : color.getColor()), 2, DEFAULT_BACKGROUND_COLOR);
	}
}