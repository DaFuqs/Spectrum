package de.dafuqs.spectrum.blocks.mob_head;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;

public class SpectrumSkullBlockItem extends VerticallyAttachableBlockItem {
	
	protected final SpectrumSkullType type;
	
	public SpectrumSkullBlockItem(Block standingBlock, Block wallBlock, Item.Settings settings, SpectrumSkullType type) {
		super(standingBlock, wallBlock, settings, Direction.DOWN);
		this.type = type;
	}
	
}
