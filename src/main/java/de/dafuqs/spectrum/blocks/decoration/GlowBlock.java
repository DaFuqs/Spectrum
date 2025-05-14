package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class GlowBlock extends Block {

	public static final MapCodec<GlowBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(GlowBlock::getColor)
	).apply(i, GlowBlock::new));
	
	private static final Map<InkColor, GlowBlock> GLOWBLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public GlowBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		GLOWBLOCKS.put(color, this);
	}

	@Override
	public MapCodec<? extends GlowBlock> codec() {
		return CODEC;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static GlowBlock byColor(InkColor color) {
		return GLOWBLOCKS.get(color);
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1.0F;
	}
	
}
