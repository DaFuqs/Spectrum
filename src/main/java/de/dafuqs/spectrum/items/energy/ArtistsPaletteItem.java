package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.storage.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.registry.entry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtistsPaletteItem extends SpectrumTrinketItem implements InkStorageItem<ArtistsPaletteInkStorage>, LoomPatternProvider {
	
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
	public void setEnergyStorage(ItemStack itemStack, ArtistsPaletteInkStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(tooltip, true);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PALETTE;
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			stacks.add(getFullStack());
		}
	}

}