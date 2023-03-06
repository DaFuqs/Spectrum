package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class SnappingIvyBlock extends PlantBlock implements Fertilizable {
    
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    public static final BooleanProperty SNAPPED = BooleanProperty.of("snapped");
    
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    protected static final Vec3d MOVEMENT_SLOWDOWN_VECTOR = new Vec3d(0.5, 0.75, 0.5);
    
    public SnappingIvyBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X).with(SNAPPED, false));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, SNAPPED);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(SpectrumBlockTags.SNAPPING_IVY_PLANTABLE);
    }
    
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }
    
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        DDConfiguredFeatures.SNAPPING_IVY_PATCH.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
    }
    
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(AXIS, state.get(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getPlayerFacing().getAxis());
    }
    
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(SNAPPED);
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(SNAPPED)) {
            BlockState newState = state.with(SNAPPED, false);
            world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
            world.playSound(null, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
        }
    }
    
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        boolean snapped = state.get(SNAPPED);
        
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, MOVEMENT_SLOWDOWN_VECTOR);
            if (!snapped) {
                entity.damage(DamageSource.SWEET_BERRY_BUSH, 5.0F);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 1));
                
                BlockState newState = state.with(SNAPPED, true);
                world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
                world.playSound(null, pos, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
            }
        }
    }
    
}
