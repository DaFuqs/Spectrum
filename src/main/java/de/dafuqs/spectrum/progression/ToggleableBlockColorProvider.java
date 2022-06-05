package de.dafuqs.spectrum.progression;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class ToggleableBlockColorProvider implements BlockColorProvider {
	
	BlockColorProvider vanillaProvider;
	boolean shouldApply;
	
	public ToggleableBlockColorProvider(BlockColorProvider vanillaProvider) {
		this.vanillaProvider = vanillaProvider;
		this.shouldApply = true;
	}
	
	public void setShouldApply(boolean shouldApply) {
		this.shouldApply = shouldApply;
	}
	
	@Override
	public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
		if (shouldApply && vanillaProvider != null) {
			return vanillaProvider.getColor(state, world, pos, tintIndex);
		} else {
			// no tint
			return 16777215;
		}
	}
	
}