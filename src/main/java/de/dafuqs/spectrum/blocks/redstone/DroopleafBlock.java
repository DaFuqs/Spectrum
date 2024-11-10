package de.dafuqs.spectrum.blocks.redstone;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.blocks.FluidLogging;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

import static de.dafuqs.spectrum.blocks.FluidLogging.State.getForFluidState;

@SuppressWarnings("deprecation")
public class DroopleafBlock extends HorizontalFacingBlock implements Fertilizable, FluidLogging.SpectrumFluidLoggable {
    public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.NONE_WATER_AND_MUD;
    public static final BooleanProperty MUDDY = BooleanProperty.of("muddy");
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 15.0, 16.0);
    private static final VoxelShape SHORT_SHAPE = Block.createCuboidShape(0.0, 5.0, 0.0, 16.0, 7.0, 16.0);
    private static final Map<Direction, VoxelShape> SHAPES_FOR_DIRECTION = ImmutableMap.of(Direction.NORTH, VoxelShapes.combine(DroopleafStemBlock.NORTH_SHAPE, BASE_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.SOUTH, VoxelShapes.combine(DroopleafStemBlock.SOUTH_SHAPE, BASE_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.EAST, VoxelShapes.combine(DroopleafStemBlock.EAST_SHAPE, BASE_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.WEST, VoxelShapes.combine(DroopleafStemBlock.WEST_SHAPE, BASE_SHAPE, BooleanBiFunction.ONLY_FIRST));
    private static final Map<Direction, VoxelShape> SHORT_SHAPES_FOR_DIRECTION = ImmutableMap.of(Direction.NORTH, VoxelShapes.combine(DroopleafStemBlock.NORTH_SHAPE, SHORT_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.SOUTH, VoxelShapes.combine(DroopleafStemBlock.SOUTH_SHAPE, SHORT_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.EAST, VoxelShapes.combine(DroopleafStemBlock.EAST_SHAPE, SHORT_SHAPE, BooleanBiFunction.ONLY_FIRST), Direction.WEST, VoxelShapes.combine(DroopleafStemBlock.WEST_SHAPE, SHORT_SHAPE, BooleanBiFunction.ONLY_FIRST));

    private final Map<BlockState, VoxelShape> shapes;


    public DroopleafBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState().with(LOGGED, FluidLogging.State.NOT_LOGGED).with(FACING, Direction.NORTH).with(Properties.UNSTABLE, false).with(Properties.SHORT, false).with(MUDDY,false)));
        this.shapes = this.getShapesForStates(DroopleafBlock::getShapeForState);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOGGED, Properties.UNSTABLE, Properties.SHORT, MUDDY);
    }
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
        FluidLogging.State preFluidState = getForFluidState(ctx.getWorld().getFluidState(ctx.getBlockPos()));
        FluidLogging.State fluidState = preFluidState!=FluidLogging.State.LIQUID_CRYSTAL ? preFluidState : FluidLogging.State.NOT_LOGGED;
        if(blockState.isOf(this) || blockState.isOf(SpectrumBlocks.DROOPLEAF_STEM))
        {
            if(blockState.isOf(this) && !blockState.get(MUDDY))
            {
                ctx.getWorld().setBlockState(ctx.getBlockPos().down(), blockState.with(Properties.UNSTABLE, false), Block.NOTIFY_LISTENERS);
                ctx.getWorld().scheduleBlockTick(ctx.getBlockPos().down(), blockState.getBlock(), 50);
            }
            return SpectrumBlocks.DROOPLEAF_STEM.getStateWithProperties(blockState).with(LOGGED, fluidState);
        }
        else
        {
            return this.getDefaultState().with(LOGGED, fluidState).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(Properties.UNSTABLE, false).with(MUDDY, fluidState == FluidLogging.State.MUD);
        }
    }
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isOf(this) || blockState.isOf(SpectrumBlocks.DROOPLEAF_STEM) || blockState.isIn(BlockTags.BIG_DRIPLEAF_PLACEABLE);
    }
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem());
    }
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(FACING) == direction.getOpposite() || direction == Direction.UP ? 15 : 0;
    }

    private static VoxelShape getShapeForState(BlockState state) {
        if(state.get(Properties.SHORT))
        {
            return VoxelShapes.union(Block.createCuboidShape(0.0, 3.0, 0.0, 16.0, 7.0, 16.0),SHORT_SHAPES_FOR_DIRECTION.get(state.get(FACING)));
        }
        return VoxelShapes.union(Block.createCuboidShape(0.0, 11.0, 0.0, 16.0, 15.0, 16.0),SHAPES_FOR_DIRECTION.get(state.get(FACING)));
    }
    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return state.get(LOGGED) == FluidLogging.State.NOT_LOGGED && (fluid == Fluids.WATER || fluid == SpectrumFluids.MUD);
    }
    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.get(LOGGED) == FluidLogging.State.NOT_LOGGED) {
            if (!world.isClient()) {
                if (fluidState.getFluid() == Fluids.WATER) {
                    world.setBlockState(pos, state.with(LOGGED, FluidLogging.State.WATER), Block.NOTIFY_ALL);
                    world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
                } else if (fluidState.getFluid() == SpectrumFluids.MUD) {
                    if (world.getBlockState(pos.down()).getBlock() != SpectrumBlocks.DROOPLEAF_STEM) {
                        world.setBlockState(pos, state.with(MUDDY, true).with(LOGGED, FluidLogging.State.MUD), Block.NOTIFY_ALL);
                    }
                    else{
                        world.setBlockState(pos, state.with(LOGGED, FluidLogging.State.MUD), Block.NOTIFY_ALL);
                    }
                    world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
                }
            }

            return true;
        } else {
            return false;
        }
    }
    @Override
    public ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
        FluidLogging.State fluidLog = state.get(LOGGED);

        if (fluidLog == FluidLogging.State.WATER) {
            world.setBlockState(pos, state.with(LOGGED, FluidLogging.State.NOT_LOGGED), Block.NOTIFY_ALL);
            if (!state.canPlaceAt(world, pos)) {
                world.breakBlock(pos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else if (fluidLog == FluidLogging.State.MUD) {
            if(world.getBlockState(pos.down()).getBlock() != SpectrumBlocks.DROOPLEAF_STEM)
            {
                world.setBlockState(pos, state.with(MUDDY, false).with(LOGGED, FluidLogging.State.NOT_LOGGED), Block.NOTIFY_ALL);
                world.scheduleBlockTick(pos, this, 50);
            }
            else
            {
                world.setBlockState(pos, state.with(LOGGED, FluidLogging.State.NOT_LOGGED), Block.NOTIFY_ALL);
            }
            if (!state.canPlaceAt(world, pos)) {
                world.breakBlock(pos, true);
            }
            return new ItemStack(SpectrumItems.MUD_BUCKET);
        }
        return ItemStack.EMPTY;
    }
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        if(state.get(MUDDY))
        {
            return false;
        }
        BlockState blockState = world.getBlockState(pos.up());
        return state.get(Properties.SHORT) || canGrowInto(blockState);
    }
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        if(state.get(Properties.SHORT))
        {
            world.setBlockState(pos, state.with(Properties.SHORT, false), Block.NOTIFY_LISTENERS);
            world.scheduleBlockTick(pos, SpectrumBlocks.DROOPLEAF, 50);
        }
        else if (canGrowInto(world, blockPos, blockState)) {
            Direction direction = state.get(FACING);
            DroopleafStemBlock.placeStemAt(world, pos, getForFluidState(state.getFluidState()), direction);
            placeDroopleafAt(world, blockPos, getForFluidState(blockState.getFluidState()), direction);
        }

    }

    private static boolean canGrowInto(BlockState state) {
        return state.isOf(SpectrumBlocks.DROOPLEAF_STEM);
    }
    protected static boolean canGrowInto(HeightLimitView world, BlockPos pos, BlockState state) {
        return !world.isOutOfHeightLimit(pos) && canGrowInto(state);
    }
    protected static void placeDroopleafAt(WorldAccess world, BlockPos pos, FluidLogging.State fluidState, Direction direction) {
        BlockState blockState = SpectrumBlocks.DROOPLEAF.getDefaultState().with(LOGGED, fluidState).with(FACING, direction).with(Properties.UNSTABLE, false).with(Properties.SHORT,true);
        world.scheduleBlockTick(pos, SpectrumBlocks.DROOPLEAF, 50);
        world.setBlockState(pos, blockState, 3);
    }
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(LOGGED).getFluidState();
    }
    private static boolean isEntityAbove(BlockPos pos, Entity entity, boolean isShort) {
        if(isShort)
        {
            return entity.isOnGround() && entity.getPos().y > (double)((float)pos.getY() + 0.1875F);
        }
        return entity.isOnGround() && entity.getPos().y > (double)((float)pos.getY() + 0.6875F);
    }
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            if (!state.get(Properties.UNSTABLE) && !state.get(MUDDY)) {
                if (isEntityAbove(pos, entity, state.get(Properties.SHORT))) {
                    world.setBlockState(pos, state.with(Properties.UNSTABLE, true), Block.NOTIFY_LISTENERS);
                    if(world.getBlockTickScheduler().isQueued(pos,this))
                    {
                        ((ServerWorld)world).getBlockTickScheduler().clearNextTicks(BlockBox.create(pos,pos));
                    }
                    world.scheduleBlockTick(pos, this, 10);
                }
            }

        }
    }
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!state.get(Properties.UNSTABLE) && !state.get(MUDDY)) {
            BlockPos pos = hit.getBlockPos();
            if(!state.get(Properties.SHORT))
            {
                playDropSound(world, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN);
                world.setBlockState(pos, state.with(Properties.SHORT, true).with(Properties.UNSTABLE,false), Block.NOTIFY_LISTENERS);
                world.scheduleBlockTick(pos, this, 50);
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            else if(world.getBlockState(pos.down()).getBlock() == SpectrumBlocks.DROOPLEAF_STEM) {
                world.setBlockState(pos, state.with(Properties.UNSTABLE, true), Block.NOTIFY_LISTENERS);
                dropLeaf(state, world, pos);
            }
        }
    }
    private static void dropLeaf(BlockState state, World world, BlockPos pos) {
        playDropSound(world, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN);
        world.setBlockState(pos, SpectrumBlocks.DROOPLEAF_STEM.getStateWithProperties(state), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.down(), state.with(Properties.UNSTABLE, false).with(Properties.SHORT, false).with(LOGGED, getForFluidState(world.getFluidState(pos.down()))), Block.NOTIFY_LISTENERS);
        world.updateNeighbors(pos, SpectrumBlocks.DROOPLEAF_STEM);
        world.updateNeighbors(pos.down(), SpectrumBlocks.DROOPLEAF);
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos.down());
        world.scheduleBlockTick(pos.down(), state.getBlock(), 50);
    }
    private static void riseLeaf(BlockState state, World world, BlockPos pos)
    {
        playDropSound(world, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP);
        world.setBlockState(pos, SpectrumBlocks.DROOPLEAF_STEM.getStateWithProperties(state), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.up(), state.with(Properties.SHORT, true).with(LOGGED, getForFluidState(world.getFluidState(pos.up()))), Block.NOTIFY_LISTENERS);
        world.updateNeighbors(pos, SpectrumBlocks.DROOPLEAF_STEM);
        world.updateNeighbors(pos.up(), SpectrumBlocks.DROOPLEAF);
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos.up());
        world.scheduleBlockTick(pos.up(), state.getBlock(), 50);
    }
    private static void playDropSound(World world, BlockPos pos, SoundEvent soundEvent) {
        float f = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, f);
    }
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(MUDDY)) {
            if (state.get(Properties.UNSTABLE)) {
                if(!state.get(Properties.SHORT))
                {
                    playDropSound(world, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN);
                    world.setBlockState(pos, state.with(Properties.SHORT, true).with(Properties.UNSTABLE,false), Block.NOTIFY_LISTENERS);
                    world.scheduleBlockTick(pos, this, 50);
                    world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                }
                else if(world.getBlockState(pos.down()).getBlock() == SpectrumBlocks.DROOPLEAF_STEM)
                {
                    dropLeaf(state, world, pos);
                }
                else{
                    world.setBlockState(pos, state.with(Properties.UNSTABLE, false), Block.NOTIFY_LISTENERS);
                    world.scheduleBlockTick(pos, state.getBlock(), 50);
                }
            } else {
                if(state.get(Properties.SHORT))
                {
                    playDropSound(world, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP);
                    world.setBlockState(pos, state.with(Properties.SHORT, false), Block.NOTIFY_LISTENERS);
                    world.scheduleBlockTick(pos, this, 50);
                    world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                }
                else if(world.getBlockState(pos.up()).getBlock() == SpectrumBlocks.DROOPLEAF_STEM)
                {
                    world.scheduleBlockTick(pos.up(), this, 50);
                    riseLeaf(state, world, pos);
                }
            }
        }
        else if (state.get(Properties.UNSTABLE)){
            world.setBlockState(pos, state.with(Properties.UNSTABLE, false), Block.NOTIFY_LISTENERS);
        }
    }
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(state.get(Properties.SHORT))
        {
            return Block.createCuboidShape(0.0, 3.0, 0.0, 16.0, 7.0, 16.0);
        }
        return Block.createCuboidShape(0.0, 11.0, 0.0, 16.0, 15.0, 16.0);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapes.get(state);
    }
}
