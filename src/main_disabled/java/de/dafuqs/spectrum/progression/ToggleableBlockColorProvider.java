package de.dafuqs.spectrum.progression;

import net.minecraft.client.color.block.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class ToggleableBlockColorProvider implements BlockColor {
	
	final BlockColor vanillaProvider;
	boolean shouldApply;
	
	public ToggleableBlockColorProvider(BlockColor vanillaProvider) {
		this.vanillaProvider = vanillaProvider;
		this.shouldApply = true;
	}
	
	public void setShouldApply(boolean shouldApply) {
		this.shouldApply = shouldApply;
	}
	
	@Override
	public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
		if (shouldApply && vanillaProvider != null) {
			return vanillaProvider.getColor(state, world, pos, tintIndex);
		} else {
			// no tint
			return 16777215;
		}
	}
	
}