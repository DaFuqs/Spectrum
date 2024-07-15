package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.entry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtistsPaletteItem extends SpectrumTrinketItem implements InkStorageItem<TotalCappedElementalMixingInkStorage>, LoomPatternProvider, ExtendedItemBarProvider {
	
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
	public TotalCappedElementalMixingInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return TotalCappedElementalMixingInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new TotalCappedElementalMixingInkStorage(this.maxEnergyTotal, Map.of());
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		if (storage instanceof TotalCappedElementalMixingInkStorage artistsPaletteInkStorage) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.put("EnergyStore", artistsPaletteInkStorage.toNbt());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.pigment_palette.tooltip.target").formatted(Formatting.GRAY));
		getEnergyStorage(stack).addTooltip(tooltip);
		addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PALETTE;
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
		
		for (RegistryEntry<InkColor> inkColor : InkColors.elementals()) {
			if (storage.getEnergy(inkColor.value()) > 0)
				colors.add(inkColor.value());
		}
		
		var progress = Math.round(MathHelper.clampedLerp(0, 14, (float) storage.getCurrentTotal() / storage.getMaxTotal()));
		if (colors.size() == 1) {
			var color = colors.get(0);
			return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, color.getColorInt() | 0xFF000000, 2, DEFAULT_BACKGROUND_COLOR);
		}
		
		var delta = MinecraftClient.getInstance().getTickDelta();
		var curColor = colors.get((int) (time % (30L * colors.size()) / 30));
		var nextColor = colors.get((int) ((time % (30L * colors.size()) / 30 + 1) % colors.size()));
		
		
		var blendFactor = (((float) time + delta) % 30) / 30F;
		var blendedColor = ColorHelper.interpolate(
				curColor == InkColors.BLACK ? InkColors.BLACK_TEXT_VEC : curColor.getColorVec(), nextColor == InkColors.BLACK ? InkColors.BLACK_TEXT_VEC : nextColor.getColorVec(), blendFactor);
		
		return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, blendedColor, 2, DEFAULT_BACKGROUND_COLOR);
	}
	
}