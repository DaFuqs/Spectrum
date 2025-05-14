package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public interface PaintbrushTriggered {
	
	/**
	 * Use as first entry of onUse() for a block
	 */
	default ItemInteractionResult checkAndDoPaintbrushTrigger(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.getItemInHand(hand).getItem() instanceof PaintbrushItem) {
			ItemInteractionResult actionResult = onPaintBrushTrigger(state, world, pos, player, hand, hit);
			if (actionResult.consumesAction()) {
				world.playSound(null, pos, SpectrumSoundEvents.PAINTBRUSH_TRIGGER, SoundSource.PLAYERS, 1.0F, 1.0F);
			} else {
				world.playSound(null, pos, SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			return actionResult;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
	/**
	 * Do custom logic here
	 * The Pedestal uses it to start crafting, for example
	 */
	ItemInteractionResult onPaintBrushTrigger(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit);
	
}
