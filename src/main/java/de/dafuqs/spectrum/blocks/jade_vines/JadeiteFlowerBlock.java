package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class JadeiteFlowerBlock extends SpectrumFacingBlock {
    
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 8, 0, 16, 16, 16);
    
    public JadeiteFlowerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState());
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var facing = state.get(FACING);
        var root = pos.offset(facing.getOpposite());
        var supportBlock = world.getBlockState(root);
        return (facing.getAxis().isVertical() && supportBlock.isOf(SpectrumBlocks.JADEITE_LOTUS_STEM)) || supportBlock.isSideSolid(world, root, facing, SideShapeType.CENTER);
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SpectrumItems.JADEITE_LOTUS_BULB.getDefaultStack();
    }
    
    @Override
	@SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        var amount = random.nextInt(18) + 9;
        for (int i = 0; i < amount; i++) {
            var xOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var yOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var zOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            world.addImportantParticle(ParticleTypes.END_ROD, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025);
        }
    }

    @Override
	@SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }
    }

}
