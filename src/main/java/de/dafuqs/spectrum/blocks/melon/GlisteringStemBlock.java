package de.dafuqs.spectrum.blocks.melon;

import net.minecraft.block.GourdBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class GlisteringStemBlock extends StemBlock {
	
	public GlisteringStemBlock(GourdBlock gourdBlock, Supplier<Item> supplier, Settings settings) {
		super(gourdBlock, supplier, settings);
	}
	
}
