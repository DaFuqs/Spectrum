package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.items.magic_items.PaintBrushItem;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PaintbrushTriggered {
	
	default ActionResult checkAndDoPaintbrushTrigger(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(player.getStackInHand(hand).getItem() instanceof PaintBrushItem) {
			ActionResult actionResult = onPaintBrushTrigger(state, world, pos, player, hand, hit);
			if(actionResult.isAccepted()) {
				world.playSound(null, pos, SpectrumSoundEvents.ENCHANTER_DING, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			return actionResult;
		}
		return ActionResult.PASS;
	}
	
	ActionResult onPaintBrushTrigger(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit);
	
}
