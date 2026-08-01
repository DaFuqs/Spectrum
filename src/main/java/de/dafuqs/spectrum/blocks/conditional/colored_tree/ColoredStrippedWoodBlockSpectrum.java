package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ColoredStrippedWoodBlockSpectrum extends FlammableRotatedPillarBlock implements RevelationAware, ColoredTree {
	
	public static final MapCodec<ColoredStrippedWoodBlockSpectrum> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredStrippedWoodBlockSpectrum::getColor)
	).apply(instance, ColoredStrippedWoodBlockSpectrum::new));
	
	private static final Map<InkColor, ColoredStrippedWoodBlockSpectrum> WOOD = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredStrippedWoodBlockSpectrum(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		WOOD.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends ColoredStrippedWoodBlockSpectrum> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.STRIPPED_WOOD, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : RotatedPillarBlock.AXIS.getPossibleValues()) {
			map.put(this.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), Blocks.STRIPPED_OAK_WOOD.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.STRIPPED_OAK_WOOD.asItem());
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}

	public static @Nullable ColoredStrippedWoodBlockSpectrum byColor(InkColor color) {
		return WOOD.get(color);
	}
	
	public static Collection<ColoredStrippedWoodBlockSpectrum> all() {
		return WOOD.values();
	}
	
}
