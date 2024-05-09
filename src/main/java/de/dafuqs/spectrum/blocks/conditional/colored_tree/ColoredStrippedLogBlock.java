package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

public class ColoredStrippedLogBlock extends PillarBlock implements RevelationAware, ColoredTree {
	
	private static final Map<DyeColor, ColoredStrippedLogBlock> LOGS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredStrippedLogBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		LOGS.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.STRIPPED_LOG, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : PillarBlock.AXIS.getValues()) {
			map.put(this.getDefaultState().with(PillarBlock.AXIS, axis), Blocks.STRIPPED_OAK_LOG.getDefaultState().with(PillarBlock.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.STRIPPED_OAK_LOG.asItem());
	}
	
	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredStrippedLogBlock byColor(DyeColor color) {
		return LOGS.get(color);
	}

	public static Collection<ColoredStrippedLogBlock> all() {
		return LOGS.values();
	}
	
}
