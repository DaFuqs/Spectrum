package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.registry.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class TallDragonjagBlock extends TallPlantBlock implements Dragonjag, Fertilizable, NaturesStaffItem.NaturesStaffTriggered {
    
    protected static final VoxelShape SHAPE_UPPER = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);
    protected static final VoxelShape SHAPE_UPPER_DEAD = Block.createCuboidShape(2.0, 0.0, 2.0, 10.0, 3.0, 14.0);
    protected static final VoxelShape SHAPE_LOWER = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    
    public static final BooleanProperty DEAD = BooleanProperty.of("dead");
    protected static final Map<Dragonjag.Variant, TallDragonjagBlock> VARIANTS = new HashMap<>();
    protected final Dragonjag.Variant variant;
    
    public TallDragonjagBlock(Settings settings, Dragonjag.Variant variant) {
        super(settings);
        this.variant = variant;
        VARIANTS.put(variant, this);
        this.setDefaultState(this.stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(DEAD, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return state.get(DEAD) ? SHAPE_UPPER_DEAD : SHAPE_UPPER;
        }
        return SHAPE_LOWER;
    }
    
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return Dragonjag.canPlantOnTop(floor, world, pos);
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SmallDragonjagBlock.getBlockForVariant(this.variant).getPickStack(world, pos, state);
    }
    
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, DEAD);
    }
    
    @Override
    public Dragonjag.Variant getVariant() {
        return variant;
    }
    
    public static TallDragonjagBlock getBlockForVariant(Variant variant) {
        return VARIANTS.get(variant);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(DEAD);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(DEAD);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		boolean success = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getOrEmpty(SpectrumConfiguredFeatures.DRAGONJAGS.get(this.variant)).get().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
        if (success) {
            setDead(world, pos, state, true);
        }
    }
    
    private void setDead(World world, BlockPos pos, BlockState state, boolean dead) {
        BlockState posState = world.getBlockState(pos);
        if (posState.isOf(this)) {
            world.setBlockState(pos, posState.with(DEAD, dead));
        }
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            posState = world.getBlockState(pos.up());
            if (posState.isOf(this)) {
                world.setBlockState(pos.up(), posState.with(DEAD, dead));
            }
        } else {
            posState = world.getBlockState(pos.down());
            if (posState.isOf(this)) {
                world.setBlockState(pos.down(), posState.with(DEAD, dead));
            }
        }
    }
    
    @Override
    public boolean canUseNaturesStaff(World world, BlockPos pos, BlockState state) {
        return state.get(DEAD);
    }
    
    @Override
    public boolean onNaturesStaffUse(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        setDead(world, pos, state, false);
        return true;
    }
    
}
