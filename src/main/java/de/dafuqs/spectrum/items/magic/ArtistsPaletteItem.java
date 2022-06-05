package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.storage.ArtistsPaletteInkStorage;
import de.dafuqs.spectrum.items.SpectrumBannerPatternItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtistsPaletteItem extends SpectrumTrinketItem implements InkStorageItem<ArtistsPaletteInkStorage>, LoomPatternProvider {
	
	private final long maxEnergyTotal;
	
	public ArtistsPaletteItem(Settings settings, long maxEnergyTotal) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_artists_palette"));
		this.maxEnergyTotal = maxEnergyTotal;
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
		getEnergyStorage(stack).addTooltip(world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.PALETTE;
	}
	
}