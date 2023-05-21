package de.dafuqs.spectrum.blocks.melon;

import net.minecraft.block.*;
import net.minecraft.item.*;

import java.util.function.*;

public class AttachedGlisteringStemBlock extends AttachedStemBlock {
	
	public AttachedGlisteringStemBlock(GourdBlock gourdBlock, Supplier<Item> supplier, Settings settings) {
		super(gourdBlock, supplier, settings);
	}
	
}
