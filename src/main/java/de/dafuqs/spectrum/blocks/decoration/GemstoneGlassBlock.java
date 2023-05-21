package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.enums.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class GemstoneGlassBlock extends GlassBlock {
	
	@Nullable
	final
	GemstoneColor gemstoneColor;
	
	public GemstoneGlassBlock(Settings settings, @Nullable GemstoneColor gemstoneColor) {
		super(settings);
		this.gemstoneColor = gemstoneColor;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.isOf(this)) {
			return true;
		}
		
		if (state.getBlock() instanceof GemstoneGlassBlock sourceGemstoneGlassBlock && stateFrom.getBlock() instanceof GemstoneGlassBlock targetGemstoneGlassBlock) {
			return sourceGemstoneGlassBlock.gemstoneColor == targetGemstoneGlassBlock.gemstoneColor;
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
}
