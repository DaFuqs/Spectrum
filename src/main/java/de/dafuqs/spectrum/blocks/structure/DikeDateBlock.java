package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class DikeDateBlock extends AbstractGlassBlock {

	public DikeDateBlock(Settings settings) {
		super(settings);
	}

	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if(entity instanceof LivingEntity livingEntity) {
				int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
				if(charges > 0) {
					return VoxelShapes.empty();
				}
			}
		}
		return VoxelShapes.fullCube();
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}
	
}
