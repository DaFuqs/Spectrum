package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class GemstoneGlassBlock extends TransparentBlock {

	public final MapCodec<GemstoneGlassBlock> codec;

	@Nullable
	final GemstoneColor gemstoneColor;
	
	public GemstoneGlassBlock(Properties settings, @Nullable GemstoneColor gemstoneColor) {
		super(settings);
		this.gemstoneColor = gemstoneColor;
		this.codec = RecordCodecBuilder.mapCodec(i -> i.group(
				propertiesCodec(),
				SpectrumRegistries.GEMSTONE_COLOR.byNameCodec().fieldOf("color").forGetter(b -> b.gemstoneColor)
		).apply(i, GemstoneGlassBlock::new));
	}

	@Override
	public MapCodec<? extends GemstoneGlassBlock> codec() {
		return codec;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.is(this)) {
			return true;
		}
		
		if (state.getBlock() instanceof GemstoneGlassBlock sourceGemstoneGlassBlock && stateFrom.getBlock() instanceof GemstoneGlassBlock targetGemstoneGlassBlock) {
			return sourceGemstoneGlassBlock.gemstoneColor == targetGemstoneGlassBlock.gemstoneColor;
		}
		return super.skipRendering(state, stateFrom, direction);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
}
