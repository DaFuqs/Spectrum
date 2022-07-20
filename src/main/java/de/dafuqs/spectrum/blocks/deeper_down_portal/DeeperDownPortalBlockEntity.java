package de.dafuqs.spectrum.blocks.deeper_down_portal;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DeeperDownPortalBlockEntity extends BlockEntity {
	
	protected DeeperDownPortalBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	public DeeperDownPortalBlockEntity(BlockPos pos, BlockState state) {
		this(SpectrumBlockEntities.DEEPER_DOWN_PORTAL, pos, state);
	}
	
	@Environment(EnvType.CLIENT)
	public boolean shouldDrawSide(Direction direction) {
		return direction == Direction.UP;
	}
}