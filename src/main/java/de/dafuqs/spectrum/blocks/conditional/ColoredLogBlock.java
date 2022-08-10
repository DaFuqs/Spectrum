package de.dafuqs.spectrum.blocks.conditional;

import com.google.common.collect.Maps;
import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.PigmentItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.Hashtable;
import java.util.Map;

public class ColoredLogBlock extends PillarBlock implements RevelationAware {
	
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
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_colored_trees");
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
