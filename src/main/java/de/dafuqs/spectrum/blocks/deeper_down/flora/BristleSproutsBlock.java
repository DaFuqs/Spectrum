package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class BristleSproutsBlock extends PlantBlock implements Fertilizable {
	
	public static final float DAMAGE = 2.0F;
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);
	
	public BristleSproutsBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Vec3d vec3d = state.getModelOffset(world, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity && !entity.getType().isIn(SpectrumEntityTypeTags.POKING_DAMAGE_IMMUNE)) {
			if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
				double difX = Math.abs(entity.getX() - entity.lastRenderX);
				double difZ = Math.abs(entity.getZ() - entity.lastRenderZ);
				if (difX >= 0.003 || difZ >= 0.003) {
					entity.damage(SpectrumDamageTypes.bristeSprouts(world), DAMAGE);
				}
			}
		}
    }

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.getRegistryManager()
				.get(RegistryKeys.CONFIGURED_FEATURE)
				.get(SpectrumConfiguredFeatures.BRISTLE_SPROUT_PATCH)
				.generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.265F;
    }
}
