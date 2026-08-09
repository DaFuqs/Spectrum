package de.dafuqs.spectrum.blocks.end_portal;

import com.google.common.base.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.pattern.*;
import net.minecraft.world.level.block.state.predicate.*;

class FixedEndPortalShaper implements EndPortalShaper {
	
	private static BlockPattern COMPLETED_FRAME;
	private static BlockPattern END_PORTAL;
	
	public void placePortals(Level world, BlockPos blockPos) {
		BlockPattern.BlockPatternMatch result = getCompletedFramePattern().find(world, blockPos);
		if (result != null) {
			// since the custom portal does not have
			// fixed directions we can estimate the
			// portal position based on some simple checks instead
			BlockPos portalTopLeft = result.getFrontTopLeft().offset(-3, 0, -3);
			if (world.getBlockState(portalTopLeft.offset(7, 0, 0)).getBlock().equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())) {
				portalTopLeft = portalTopLeft.offset(4, 0, 0);
			} else if (world.getBlockState(portalTopLeft.offset(0, 0, 7)).getBlock().equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())) {
				portalTopLeft = portalTopLeft.offset(0, 0, 4);
			}
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					world.setBlock(portalTopLeft.offset(i, 0, j), Blocks.END_PORTAL.defaultBlockState(), 2);
				}
			}
			
			world.globalLevelEvent(LevelEvent.SOUND_END_PORTAL_SPAWN, portalTopLeft.offset(1, 0, 1), 0);
		}
	}
	
	public void destroyNeighboringPortalBlocks(Level world, BlockPos blockPos) {
		BlockPattern.BlockPatternMatch result = getActiveEndPortalPattern().find(world, blockPos);
		if (result != null) {
			// since the custom portal does not have
			// fixed directions we can estimate the
			// portal position based on some simple checks instead
			BlockPos portalTopLeft = result.getFrontTopLeft().offset(-3, 0, -3);
			Block b1 = world.getBlockState(portalTopLeft.offset(7, 0, 0)).getBlock();
			Block b2 = world.getBlockState(portalTopLeft.offset(0, 0, 7)).getBlock();
			if (b1.equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()) || b1.equals(Blocks.END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.offset(4, 0, 0);
			} else if (b2.equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()) || b2.equals(Blocks.END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.offset(0, 0, 4);
			}
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					world.setBlock(portalTopLeft.offset(i, 0, j), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
				}
			}
			
			world.globalLevelEvent(LevelEvent.SOUND_END_PORTAL_SPAWN, portalTopLeft.offset(1, 0, 1), 0);
		}
	}
	
	public static BlockPattern getCompletedFramePattern() {
		if (COMPLETED_FRAME == null) {
			COMPLETED_FRAME = BlockPatternBuilder.start()
					.aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
					.where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
					.where('^', BlockInWorld.hasState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
									.where(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.SOUTH))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())
											.where(CrackedEndPortalFrameBlock.EYE_TYPE, Predicates.equalTo(CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.where(CrackedEndPortalFrameBlock.FACING_VERTICAL, Predicates.equalTo(false)))))
					.where('>', BlockInWorld.hasState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
									.where(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.WEST))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())
											.where(CrackedEndPortalFrameBlock.EYE_TYPE, Predicates.equalTo(CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.where(CrackedEndPortalFrameBlock.FACING_VERTICAL, Predicates.equalTo(true)))))
					.where('v', BlockInWorld.hasState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
									.where(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.NORTH))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())
											.where(CrackedEndPortalFrameBlock.EYE_TYPE, Predicates.equalTo(CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.where(CrackedEndPortalFrameBlock.FACING_VERTICAL, Predicates.equalTo(false)))))
					.where('<', BlockInWorld.hasState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
									.where(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.EAST))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get())
											.where(CrackedEndPortalFrameBlock.EYE_TYPE, Predicates.equalTo(CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.where(CrackedEndPortalFrameBlock.FACING_VERTICAL, Predicates.equalTo(true)))))
					.build();
		}
		return COMPLETED_FRAME;
	}
	
	public static BlockPattern getActiveEndPortalPattern() {
		if (END_PORTAL == null) {
			END_PORTAL = BlockPatternBuilder.start()
					.aisle("?vvv?", ">ppp<", ">ppp<", ">ppp<", "?^^^?")
					.where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
					.where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()))))
					.where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()))))
					.where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()))))
					.where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()))))
					.where('p', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL)))
					.build();
		}
		return END_PORTAL;
	}
	
}
