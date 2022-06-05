package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.storage.PigmentPaletteInkStorage;
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

public class PigmentPaletteItem extends SpectrumTrinketItem implements InkStorageItem<PigmentPaletteInkStorage>, LoomPatternProvider {
	
	private final long maxEnergyPerColor;
	
	public PigmentPaletteItem(Settings settings, long maxEnergyPerColor) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_pigment_palette"));
		this.maxEnergyPerColor = maxEnergyPerColor;
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
	public void setEnergyStorage(ItemStack itemStack, PigmentPaletteInkStorage storage) {
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