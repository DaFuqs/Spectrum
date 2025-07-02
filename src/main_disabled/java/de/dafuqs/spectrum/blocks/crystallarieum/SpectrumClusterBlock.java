package de.dafuqs.spectrum.blocks.crystallarieum;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class SpectrumClusterBlock extends AmethystClusterBlock {
	
	public enum GrowthStage {
		SMALL(3, 4),
		MEDIUM(4, 3),
		LARGE(5, 3),
		CLUSTER(7, 3);
		
		public final int height;
		public final int xzOffset;
		
		GrowthStage(int height, int xzOffset) {
			this.height = height;
			this.xzOffset = xzOffset;
		}
		
	}
	
	protected final GrowthStage growthStage;
	
	public SpectrumClusterBlock(BlockBehaviour.Properties settings, GrowthStage growthStage) {
		super(growthStage.height, growthStage.xzOffset, settings);
		this.growthStage = growthStage;
	}
	
	public GrowthStage getGrowthStage() {
		return growthStage;
	}

//	@Override
//	public MapCodec<? extends CloakedOreBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
}
