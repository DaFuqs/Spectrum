package de.dafuqs.spectrum.progression;

import net.minecraft.block.*;
import net.minecraft.client.color.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ToggleableBlockColorProvider implements BlockColorProvider {
	
	final BlockColorProvider vanillaProvider;
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