package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColoredTallGrassBlock extends DoublePlantBlock implements RevelationAware {
	
	private static final Map<InkColor, ColoredTallGrassBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredTallGrassBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
		RevelationAware.register(this);
	}
	
	public static Collection<ColoredTallGrassBlock> all() {
		return BLOCKS.values();
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredTallGrassBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_COLORED_GRASS;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> blockStateCloaks = new Object2ObjectArrayMap<>();
		blockStateCloaks.put(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), Blocks.TALL_GRASS.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER));
		blockStateCloaks.put(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), Blocks.TALL_GRASS.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
		return blockStateCloaks;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.TALL_GRASS.asItem());
	}
	
}
