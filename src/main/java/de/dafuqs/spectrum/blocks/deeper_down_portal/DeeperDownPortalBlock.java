package de.dafuqs.spectrum.blocks.deeper_down_portal;

import de.dafuqs.spectrum.dimension.DeeperDownDimension;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class DeeperDownPortalBlock extends EndPortalBlock {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.5D, 16.0D);
	
	public DeeperDownPortalBlock(Settings settings) {
		super(settings);
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DeeperDownPortalBlockEntity(pos, state);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world instanceof ServerWorld
				&& !entity.hasVehicle()
				&& !entity.hasPassengers()
				&& entity.canUsePortals()
				&& VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset((-pos.getX()), (-pos.getY()), (-pos.getZ()))), state.getOutlineShape(world, pos), BooleanBiFunction.AND)) {
			
			RegistryKey<World> registryKey = world.getRegistryKey() == World.OVERWORLD ? DeeperDownDimension.DEEPER_DOWN_DIMENSION_KEY : World.OVERWORLD;
			
			ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(registryKey);
			if (serverWorld != null) {
				entity.moveToWorld(serverWorld);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double d = (double) pos.getX() + random.nextDouble();
		double e = (double) pos.getY() + 0.3D;
		double f = (double) pos.getZ() + random.nextDouble();
		world.addParticle(SpectrumParticleTypes.VOID_FOG, d, e, f, 0.0D, 0.1D, 0.0D);
	}
	
}
