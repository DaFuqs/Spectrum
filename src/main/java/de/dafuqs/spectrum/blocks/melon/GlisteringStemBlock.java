package de.dafuqs.spectrum.blocks.melon;

import net.minecraft.block.*;
import net.minecraft.item.*;

import java.util.function.*;

public class GlisteringStemBlock extends StemBlock {
	
	public GlisteringStemBlock(GourdBlock gourdBlock, Supplier<Item> supplier, Settings settings) {
		super(gourdBlock, supplier, settings);
	}
	
}
