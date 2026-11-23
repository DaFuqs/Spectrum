package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

public interface PaintbrushTriggered {
	
	/**
	 * Use as first entry of onUse() for a block
	 */
	default ItemInteractionResult checkAndDoPaintbrushTrigger(BlockState state, Level world, BlockPos pos, @Nullable Player player, InteractionHand hand) {
		ItemInteractionResult actionResult = onPaintBrushTrigger(state, world, pos, player, hand);
			if (actionResult.consumesAction()) {
				world.playSound(null, pos, SpectrumSoundEvents.PAINTBRUSH_TRIGGER, SoundSource.PLAYERS, 1.0F, 1.0F);
			} else {
				world.playSound(null, pos, SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			return actionResult;
	}
	
	/**
	 * Do custom logic here
	 * The Pedestal uses it to start crafting, for example
	 */
	ItemInteractionResult onPaintBrushTrigger(BlockState state, Level world, BlockPos pos, @Nullable Player player, InteractionHand hand);
	
}
