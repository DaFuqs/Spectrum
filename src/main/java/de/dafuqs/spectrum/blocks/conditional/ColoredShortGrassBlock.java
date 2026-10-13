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
import org.jetbrains.annotations.*;

import java.util.*;

public class ColoredShortGrassBlock extends TallGrassBlock implements RevelationAware {
	
	private static final Map<InkColor, ColoredShortGrassBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredShortGrassBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
		RevelationAware.register(this);
	}
	
	public static Collection<ColoredShortGrassBlock> all() {
		return BLOCKS.values();
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredShortGrassBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_COLORED_GRASS;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.defaultBlockState(), Blocks.SHORT_GRASS.defaultBlockState());
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.SHORT_GRASS.asItem());
	}
	
}
