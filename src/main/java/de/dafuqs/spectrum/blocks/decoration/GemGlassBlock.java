package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class GemGlassBlock extends GlassBlock {
	
	@Nullable
	GemstoneColor gemstoneColor;

	public GemGlassBlock(Settings settings, @Nullable GemstoneColor gemstoneColor) {
		super(settings);
		this.gemstoneColor = gemstoneColor;
	}

	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if(stateFrom.isOf(this)) {
			return true;
		}
		
		if(state.getBlock() instanceof GemGlassBlock sourceGemGlassBlock && stateFrom.getBlock() instanceof GemGlassBlock targetGemGlassBlock) {
			return sourceGemGlassBlock.gemstoneColor == targetGemGlassBlock.gemstoneColor;
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}

	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

}
