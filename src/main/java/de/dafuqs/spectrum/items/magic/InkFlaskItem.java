package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.storage.SingleInkStorage;
import de.dafuqs.spectrum.items.SpectrumBannerPatternItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InkFlaskItem extends Item implements InkStorageItem<SingleInkStorage>, LoomPatternProvider {
	
	private final long maxEnergy;
	
	public InkFlaskItem(Settings settings, long maxEnergy) {
		super(settings);
		this.maxEnergy = maxEnergy;
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
	public void setEnergyStorage(ItemStack itemStack, SingleInkStorage storage) {
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
	
	public ItemStack getFullStack(InkColor color) {
		ItemStack stack = this.getDefaultStack();
		SingleInkStorage storage = getEnergyStorage(stack);
		storage.fillCompletely();
		storage.convertColor(color);
		setEnergyStorage(stack, storage);
		return stack;
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.INK_FLASK;
	}
	
}