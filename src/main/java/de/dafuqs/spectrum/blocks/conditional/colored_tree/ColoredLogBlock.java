package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

public class ColoredLogBlock extends PillarBlock implements RevelationAware, ColoredTree {
	
	private static final Map<DyeColor, ColoredLogBlock> LOGS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredLogBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		LOGS.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(ColoredTree.TreePart.LOG, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (Direction.Axis axis : PillarBlock.AXIS.getValues()) {
			hashtable.put(this.getDefaultState().with(PillarBlock.AXIS, axis), Blocks.OAK_LOG.getDefaultState().with(PillarBlock.AXIS, axis));
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_LOG.asItem());
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredLogBlock byColor(DyeColor color) {
		return LOGS.get(color);
	}
	
}
