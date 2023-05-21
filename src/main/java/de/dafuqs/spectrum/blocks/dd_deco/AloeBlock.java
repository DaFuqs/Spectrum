package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class AloeBlock extends PlantBlock implements Fertilizable {

    protected static final IntProperty AGE = Properties.AGE_4;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
    protected static final double GROW_CHANCE = 0.4;
    protected static final int MAX_LIGHT_LEVEL = 10;

    public AloeBlock(Settings settings) {
        super(settings);
    }

    @Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(AGE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBaseLightLevel(pos, 0) <= MAX_LIGHT_LEVEL && super.canPlaceAt(state, world, pos);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(SpectrumBlockTags.ALOE_PLANTABLE);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < Properties.AGE_4_MAX;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.getBaseLightLevel(pos, 0) <= MAX_LIGHT_LEVEL && random.nextFloat() > GROW_CHANCE;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (canPlaceAt(state, world, pos)) {
            int age = state.get(AGE);
            if (age < Properties.AGE_4_MAX) {
                world.setBlockState(pos, state.with(AGE, age + 1));
                world.playSound(null, pos, state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (world.getBlockState(pos.down()).isIn(SpectrumBlockTags.ALOE_CONVERTED)) {
                    world.setBlockState(pos.down(), Blocks.SAND.getDefaultState());
                }
            }
        } else {
            Block.replace(state, Blocks.AIR.getDefaultState(), world, pos, 10, 512);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        grow(world, random, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int age = state.get(AGE);
        if (age > 1) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                world.setBlockState(pos, state.with(AGE, age - 1));
                player.getInventory().offerOrDrop(this.asItem().getDefaultStack());
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

}
