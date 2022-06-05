package de.dafuqs.spectrum.blocks.melon;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class AttachedGlisteringStemBlock extends AttachedStemBlock {
	
	public AttachedGlisteringStemBlock(GourdBlock gourdBlock, Supplier<Item> supplier, Settings settings) {
		super(gourdBlock, supplier, settings);
	}
	
}
