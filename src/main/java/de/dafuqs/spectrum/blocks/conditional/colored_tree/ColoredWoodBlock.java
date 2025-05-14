package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredWoodBlock extends RotatedPillarBlock implements RevelationAware, ColoredTree {

	public static final MapCodec<ColoredWoodBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredWoodBlock::getColor)
	).apply(instance, ColoredWoodBlock::new));
	
	private static final Map<InkColor, ColoredWoodBlock> WOOD = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredWoodBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		WOOD.put(color, this);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends ColoredWoodBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.WOOD, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.defaultBlockState(), Blocks.OAK_WOOD.defaultBlockState());
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.OAK_WOOD.asItem());
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredWoodBlock byColor(InkColor color) {
		return WOOD.get(color);
	}
	
	public static Collection<ColoredWoodBlock> all() {
		return WOOD.values();
	}
	
}
