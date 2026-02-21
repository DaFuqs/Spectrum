package de.dafuqs.spectrum.blocks.end_portal;

import com.google.common.base.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.predicate.*;

import java.util.*;
import java.util.function.Predicate;

class DynamicEndPortalShaper implements EndPortalShaper {
	
	private final static int MAX_PORTAL_BLOCK_COUNT = 200;
	
	private static final Predicate<BlockState> BORDER_STATE_PREDICATE = BlockStatePredicate
			.forBlock(Blocks.END_PORTAL_FRAME).where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
			.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get()).where(CrackedEndPortalFrameBlock.EYE_TYPE, Predicates.equalTo(CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER)));
	
	public void placePortals(Level level, BlockPos startPos) {
		var portalPositions = findValidPortal(level, startPos);
		for (Set<BlockPos> portal : portalPositions) {
			for (BlockPos pos : portal) {
				level.setBlock(pos, Blocks.END_PORTAL.defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
		
		level.globalLevelEvent(LevelEvent.SOUND_END_PORTAL_SPAWN, startPos, 0);
	}
	
	public void destroyNeighboringPortalBlocks(Level level, BlockPos startPos) {
		for (BlockPos neighbor : getNeighbors(startPos)) {
			BlockState neighborState = level.getBlockState(neighbor);
			if (neighborState.is(Blocks.END_PORTAL)) {
				level.setBlock(neighbor, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
				destroyNeighboringPortalBlocks(level, neighbor);
			}
		}
	}
	
	private Collection<Set<BlockPos>> findValidPortal(LevelAccessor level, BlockPos startPos) {
		Collection<Set<BlockPos>> validPortals = new HashSet<>();
		
		// Gather all air blocks starting from startPos
		for (BlockPos neighbor : getNeighbors(startPos)) {
			Set<BlockPos> portalPositions = new HashSet<>();
			
			if (!isValidPortalPiece(level, neighbor, portalPositions)) {
				continue; // No enclosed air region found
			}
			
			validPortals.add(portalPositions);
		}
		
		return validPortals; // The air region is fully enclosed by PortalFrame
	}
	
	private boolean isValidPortalPiece(LevelAccessor world, BlockPos pos, Set<BlockPos> airRegion) {
		if (world.isOutsideBuildHeight(pos)) {
			return false;
		}
		if (airRegion.contains(pos)) {
			return true;
		}
		
		BlockState state = world.getBlockState(pos);
		if (BORDER_STATE_PREDICATE.test(state)) {
			return true; // that's a border
		} else if (state.isAir() || state.canBeReplaced()) {
			airRegion.add(pos);
		} else {
			return false;
		}
		
		if (airRegion.size() > MAX_PORTAL_BLOCK_COUNT) {
			return false;
		}
		
		for (BlockPos neighbor : getNeighbors(pos)) {
			if (!isValidPortalPiece(world, neighbor, airRegion)) {
				return false;
			}
		}
		
		return true;
	}
	
	private Set<BlockPos> getNeighbors(BlockPos pos) {
		Set<BlockPos> neighbors = new HashSet<>();
		neighbors.add(pos.north());
		neighbors.add(pos.east());
		neighbors.add(pos.south());
		neighbors.add(pos.west());
		return neighbors;
	}
	
}
