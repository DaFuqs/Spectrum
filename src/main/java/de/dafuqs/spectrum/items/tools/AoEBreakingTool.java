package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.helpers.AoEHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface AoEBreakingTool {
	
	default void onTryBreakBlock(ItemStack stack, BlockPos pos, PlayerEntity player) {
		BlockHitResult hitResult = (BlockHitResult) player.raycast(10, 1, false);
		if (!player.world.isClient && hitResult.getType() == HitResult.Type.BLOCK) {
			Direction side = hitResult.getSide();
			AoEHelper.doAoEBlockBreaking(player, stack, pos, side, getRange(stack));
		}
	}
	
	int getRange(ItemStack stack);
	
}
