package de.dafuqs.spectrum.helpers;

import net.minecraft.core.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

import java.util.function.*;

// reworked and yarn version of Botanias AoE breaking mechanism at
// https://github.com/VazkiiMods/Botania/blob/7d526461b21cac3d4e2a084a063d469c4065951f/Xplat/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
// hereby used and credited per the Botania license at https://botaniamod.net/license.html
// Shoutout and thanks a bunch to Vazkii, Willie and artemisSystem!
public class AoEHelper {
	
	public static void doAoEBlockBreaking(LevelAccessor level, BlockPos pos, Player player, ItemStack stack, Direction side, int radius) {
		if (radius <= 0) {
			return;
		}
		
		Predicate<BlockState> minableBlocksPredicate = state -> {
			boolean suitableTool = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
			boolean suitableSpeed = stack.getDestroySpeed(state) > 0;
			return suitableTool && suitableSpeed;
		};
		
		boolean doX = side.getStepX() == 0;
		boolean doY = side.getStepY() == 0;
		boolean doZ = side.getStepZ() == 0;

		Vec3i beginDiff = new Vec3i(doX ? -radius : 0, doY ? -1 : 0, doZ ? -radius : 0);
		Vec3i endDiff = new Vec3i(doX ? radius : 0, doY ? radius * 2 - 1 : 0, doZ ? radius : 0);

		removeBlocksInIteration(level, pos, player, stack, beginDiff, endDiff, minableBlocksPredicate);
	}

	private static boolean recursive = false;
	
	private static void removeBlocksInIteration(LevelAccessor level, BlockPos centerPos, Player player, ItemStack stack, Vec3i startDelta, Vec3i endDelta, Predicate<BlockState> filter) {
		if (recursive) {
			return;
		}

		recursive = true;
		try {
			for (BlockPos blockPos : BlockPos.betweenClosed(centerPos.offset(startDelta), centerPos.offset(endDelta))) {
				if (!blockPos.equals(centerPos)) {
					breakBlockWithDrops(player, stack, level, blockPos, filter);
				}
			}
		} finally {
			recursive = false;
		}
	}
	
	public static void breakBlocksAround(Player player, ItemStack stack, BlockPos pos, int radius, @Nullable Predicate<BlockState> predicate) {
		if (radius <= 0) {
			return;
		}
		
		Level world = player.level();

		Predicate<BlockState> minableBlocksPredicate = state -> {
			boolean suitableTool = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
			boolean suitableSpeed = stack.getDestroySpeed(state) > 1;
			return suitableTool && suitableSpeed;
		};
		if (predicate != null) {
			minableBlocksPredicate = minableBlocksPredicate.and(predicate);
		}

		BlockState targetState = world.getBlockState(pos);
		if (!minableBlocksPredicate.test(targetState)) {
			return;
		}
		
		for (BlockPos blockPos : BlockPos.withinManhattan(pos, radius, radius, radius)) {
			breakBlockWithDrops(player, stack, world, blockPos, minableBlocksPredicate);
		}
	}
	
	public static void breakBlockWithDrops(Player player, ItemStack stack, LevelAccessor world, BlockPos pos, Predicate<BlockState> filter) {
		ChunkPos chunkPos = world.getChunk(pos).getPos();
		if (world.hasChunk(chunkPos.x, chunkPos.z)) {
			BlockState blockstate = world.getBlockState(pos);
			if (!world.isClientSide() && !blockstate.isAir() && blockstate.getDestroyProgress(player, world, pos) > 0 && filter.test(blockstate)) {
				ItemStack save = player.getMainHandItem();
				player.setItemInHand(InteractionHand.MAIN_HAND, stack);
				((ServerPlayer) player).connection.send(new ClientboundLevelEventPacket(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockstate), false));
				((ServerPlayer) player).gameMode.destroyBlock(pos);
				player.setItemInHand(InteractionHand.MAIN_HAND, save);
			}
		}
		
	}
	
}
