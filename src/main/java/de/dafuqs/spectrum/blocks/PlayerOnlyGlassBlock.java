package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.decoration.GemGlassBlock;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PlayerOnlyGlassBlock extends GemGlassBlock {

	// used for tinted glass to make light not shine through
	private final boolean tinted;

	public PlayerOnlyGlassBlock(Settings settings, GemstoneColor gemstoneColor, boolean tinted) {
		super(settings, gemstoneColor);
		this.tinted = tinted;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if(entity instanceof PlayerEntity) {
				return VoxelShapes.empty();
			}
		}
		return state.getOutlineShape(world, pos);
	}

	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return !tinted;
	}

	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		if(tinted) {
			return world.getMaxLightLevel();
		} else {
			return super.getOpacity(state, world, pos);
		}
	}

}
