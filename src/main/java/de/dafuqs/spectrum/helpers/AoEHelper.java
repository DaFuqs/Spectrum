package de.dafuqs.spectrum.helpers;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

// reworked and yarn version of Botanias AoE breaking mechanism at
// https://github.com/VazkiiMods/Botania/blob/7d526461b21cac3d4e2a084a063d469c4065951f/Xplat/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
// hereby used and credited per the Botania license at https://botaniamod.net/license.html
// Shoutout and thanks a bunch to Vazkii, Willie and artemisSystem!
public class AoEHelper {
	
	public static void doAoEBlockBreaking(PlayerEntity player, ItemStack stack, BlockPos pos, Direction side, int radius) {
		if (radius <= 0) {
			return;
		}
		
		World world = player.world;
		if (world.isAir(pos)) {
			return;
		}
		
		Predicate<BlockState> minableBlocksPredicate = state -> {
			boolean suitableTool = !state.isToolRequired() || stack.isSuitableFor(state);
			boolean suitableSpeed = stack.getMiningSpeedMultiplier(state) > 1;
			return suitableTool && suitableSpeed;
		};

		BlockState targetState = world.getBlockState(pos);
		if (!minableBlocksPredicate.test(targetState)) {
			return;
		}
		
		boolean doX = side.getOffsetX() == 0;
		boolean doY = side.getOffsetY() == 0;
		boolean doZ = side.getOffsetZ() == 0;

		Vec3i beginDiff = new Vec3i(doX ? -radius : 0, doY ? -1 : 0, doZ ? -radius : 0);
		Vec3i endDiff = new Vec3i(doX ? radius : 0, doY ? radius * 2 - 1 : 0, doZ ? radius : 0);

		removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, minableBlocksPredicate);
	}

	private static boolean recursive = false;

	private static void removeBlocksInIteration(PlayerEntity player, ItemStack stack, World world, BlockPos centerPos, Vec3i startDelta, Vec3i endDelta, Predicate<BlockState> filter) {
		if (recursive) {
			return;
		}

		recursive = true;
		try {
			for (BlockPos blockPos : BlockPos.iterate(centerPos.add(startDelta), centerPos.add(endDelta))) {
				if (!blockPos.equals(centerPos)) {
					breakBlockWithDrops(player, stack, world, blockPos, filter);
				}
			}
		} finally {
			recursive = false;
		}
	}

	public static void breakBlocksAround(PlayerEntity player, ItemStack stack, BlockPos pos, int radius, @Nullable Predicate<BlockState> predicate) {
		if (radius <= 0) {
			return;
		}

		World world = player.world;

		Predicate<BlockState> minableBlocksPredicate = state -> {
			boolean suitableTool = !state.isToolRequired() || stack.isSuitableFor(state);
			boolean suitableSpeed = stack.getMiningSpeedMultiplier(state) > 1;
			return suitableTool && suitableSpeed;
		};
		if (predicate != null) {
			minableBlocksPredicate = minableBlocksPredicate.and(predicate);
		}

		BlockState targetState = world.getBlockState(pos);
		if (!minableBlocksPredicate.test(targetState)) {
			return;
		}

		for (BlockPos blockPos : BlockPos.iterateOutwards(pos, radius, radius, radius)) {
			breakBlockWithDrops(player, stack, world, blockPos, minableBlocksPredicate);
		}
	}

	public static void breakBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Predicate<BlockState> filter) {
		if (!world.isChunkLoaded(pos)) {
			return;
		}

		BlockState blockstate = world.getBlockState(pos);
		if (!world.isClient && !blockstate.isAir() && blockstate.calcBlockBreakingDelta(player, world, pos) > 0 && filter.test(blockstate)) {
			ItemStack save = player.getMainHandStack();
			player.setStackInHand(Hand.MAIN_HAND, stack);
			((ServerPlayerEntity) player).networkHandler.sendPacket(new WorldEventS2CPacket(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockstate), false));
			((ServerPlayerEntity) player).interactionManager.tryBreakBlock(pos);
			player.setStackInHand(Hand.MAIN_HAND, save);
		}
	}
	
}
