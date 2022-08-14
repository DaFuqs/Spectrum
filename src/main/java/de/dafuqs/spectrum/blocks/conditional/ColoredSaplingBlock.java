package de.dafuqs.spectrum.blocks.conditional;

import com.google.common.collect.Maps;
import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.worldgen.ColoredSaplingGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.Map;

public class ColoredSaplingBlock extends SaplingBlock implements RevelationAware {
	
	private static final Map<DyeColor, ColoredSaplingBlock> SAPLINGS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredSaplingBlock(Settings settings, DyeColor color) {
		super(new ColoredSaplingGenerator(color), settings);
		this.color = color;
		SAPLINGS.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_colored_saplings");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		// Colored Logs => Oak logs
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int stage = 0; stage < 2; stage++) {
			hashtable.put(this.getDefaultState().with(SaplingBlock.STAGE, stage), Blocks.OAK_SAPLING.getDefaultState().with(SaplingBlock.STAGE, stage));
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_SAPLING.asItem());
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredSaplingBlock byColor(DyeColor color) {
		return SAPLINGS.get(color);
	}
	
}
