package de.dafuqs.spectrum.blocks.mob_head;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public class SpectrumSkullBlockItem extends StandingAndWallBlockItem {
	
	protected final SpectrumSkullType type;
	
	public SpectrumSkullBlockItem(Block standingBlock, Block wallBlock, Item.Properties settings, SpectrumSkullType type) {
		super(standingBlock, wallBlock, settings, Direction.DOWN);
		this.type = type;
	}
	
}
