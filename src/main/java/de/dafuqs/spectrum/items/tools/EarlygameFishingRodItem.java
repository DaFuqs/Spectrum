package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;

public class EarlygameFishingRodItem extends SpectrumFishingRodItem {
	
	public EarlygameFishingRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(FluidTags.WATER)
				|| fluidState.isIn(FluidTags.LAVA)
				|| fluidState.isIn(SpectrumFluidTags.MUD)
				|| fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL);
	}
	
}