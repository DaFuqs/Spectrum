package de.dafuqs.spectrum.blocks.end_portal;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;

public interface EndPortalShaper {
	
	EndPortalShaper FIXED = new FixedEndPortalShaper();
	EndPortalShaper DYNAMIC = new DynamicEndPortalShaper();
	
	// Search for a valid end portal position. Found => create portal!
	static void checkAndFillEndPortal(Level world, BlockPos blockPos) {
		if (SpectrumCommon.CONFIG.AllowDynamicEndPortalShape) {
			DYNAMIC.placePortals(world, blockPos);
		} else {
			FIXED.placePortals(world, blockPos);
		}
	}
	
	// Search for now invalid end portal positions
	static void destroyPortals(Level world, BlockPos blockPos) {
		if (SpectrumCommon.CONFIG.AllowDynamicEndPortalShape) {
			DYNAMIC.destroyNeighboringPortalBlocks(world, blockPos);
		} else {
			FIXED.destroyNeighboringPortalBlocks(world, blockPos);
		}
	}
	
	void placePortals(Level world, BlockPos blockPos);
	
	void destroyNeighboringPortalBlocks(Level world, BlockPos blockPos);
	
}
