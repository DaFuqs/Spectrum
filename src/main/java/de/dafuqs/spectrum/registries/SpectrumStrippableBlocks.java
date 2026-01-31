package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

public class SpectrumStrippableBlocks {
	
	// TODO: handle in the blocks themselves instead (see net.neoforged.neoforge.common.extensions.IBlockExtension)
	
	public static void tillable() {
		TillableBlockRegistry.register(SpectrumBlocks.SLUSH, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SLUSH.defaultBlockState());
		TillableBlockRegistry.register(SpectrumBlocks.OVERGROWN_SLUSH, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SLUSH.defaultBlockState());
		TillableBlockRegistry.register(SpectrumBlocks.SHALE_CLAY, HoeItem::onlyIfAirAbove, SpectrumBlocks.TILLED_SHALE_CLAY.defaultBlockState());
	}
	
}
