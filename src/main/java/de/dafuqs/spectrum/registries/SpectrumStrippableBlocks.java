package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import net.minecraft.world.item.*;

public class SpectrumStrippableBlocks {
	
	/*
	TODO: handle in the blocks themselves instead (see net.neoforged.neoforge.common.extensions.IBlockExtension)
	
	@Override
	public @Nullable BlockState getToolModifiedState(@NotNull BlockState state, UseOnContext context, @NotNull ItemAbility itemAbility, boolean simulate) {
		ItemStack itemStack = context.getItemInHand();
		if (!itemStack.canPerformAction(itemAbility)) {
			return null;
		} else if (ItemAbilities.AXE_STRIP == itemAbility) {
			return ColoredStrippedLogBlock.byColor(this.color).defaultBlockState().setValue(AXIS, state.getValue(AXIS));
		}
		return null;
	}
	
	 */
	
	/*
	public static void strippable() {
		StrippableBlockRegistry.register(SpectrumBlocks.SLATE_NOXCAP_STEM, SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.EBONY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_EBONY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.IVORY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_IVORY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.CHESTNUT_NOXCAP_STEM, SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.SLATE_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_SLATE_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.EBONY_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_EBONY_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.IVORY_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_IVORY_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.CHESTNUT_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_HYPHAE);
		
		StrippableBlockRegistry.register(SpectrumBlocks.WEEPING_GALA_LOG, SpectrumBlocks.STRIPPED_WEEPING_GALA_LOG);
		StrippableBlockRegistry.register(SpectrumBlocks.WEEPING_GALA_WOOD, SpectrumBlocks.STRIPPED_WEEPING_GALA_WOOD);
		
		for (InkColor color : InkColors.all()) {
			StrippableBlockRegistry.register(ColoredLogBlock.byColor(color), ColoredStrippedLogBlock.byColor(color));
			StrippableBlockRegistry.register(ColoredWoodBlock.byColor(color), ColoredStrippedWoodBlock.byColor(color));
		}
	}
	
	public static void tillable() {
		TillableBlockRegistry.register(SpectrumBlocks.SLUSH, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SLUSH.defaultBlockState());
		TillableBlockRegistry.register(SpectrumBlocks.OVERGROWN_SLUSH, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SLUSH.defaultBlockState());
		TillableBlockRegistry.register(SpectrumBlocks.SHALE_CLAY, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SHALE_CLAY.defaultBlockState());
	}*/
	
}
