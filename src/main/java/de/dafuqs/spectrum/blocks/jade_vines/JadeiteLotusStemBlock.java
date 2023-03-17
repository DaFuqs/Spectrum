package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class JadeiteLotusStemBlock extends PlantBlock implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public static final EnumProperty<StemComponent> STEM_PART = StemComponent.PROPERTY;
    public static final BooleanProperty INVERTED = BooleanProperty.of("inverted");

    public JadeiteLotusStemBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STEM_PART, StemComponent.BASE).with(INVERTED, false).with(WATERLOGGED, false));
    }

    public static BlockState getStemVariant(boolean top, boolean inverted) {
        return SpectrumBlocks.JADEITE_LOTUS_STEM.getDefaultState().with(STEM_PART, top ? StemComponent.STEMALT : StemComponent.STEM).with(INVERTED, inverted);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var handStack = player.getStackInHand(hand);

        if (handStack.isIn(ConventionalItemTags.SHEARS) && state.get(STEM_PART) == StemComponent.BASE) {

            world.setBlockState(pos, state.with(STEM_PART, StemComponent.STEM));
            player.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
            handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));

            return ActionResult.success(world.isClient());
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();
        var floor = world.getBlockState(pos.up());
        var side = ctx.getSide();

        if (!side.getAxis().isVertical())
            return null;

        var inverted = side == Direction.UP;

        var state = super.getPlacementState(ctx);

        if (state == null)
            return null;

        if (inverted) {
            state = state.with(INVERTED, true);
            floor = world.getBlockState(pos.down());
        }

        if (floor.isOf(this) ) {

            if (floor.get(STEM_PART) != StemComponent.STEMALT) {
                state = state.with(STEM_PART, StemComponent.STEMALT);
            }
            else if (floor.get(STEM_PART) != StemComponent.STEM) {
                state = state.with(STEM_PART, StemComponent.STEM);
            }
        }

        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var floor = state.get(INVERTED) ? pos.down() : pos.up();
        return this.canPlantOnTop(world.getBlockState(floor), world, floor);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isSideSolid(world, pos, Direction.UP, SideShapeType.RIGID) || floor.isSideSolid(world, pos, Direction.DOWN, SideShapeType.RIGID) || floor.isOf(this);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        scheduleBreakAttempt(world, pos, state.get(INVERTED), true);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        scheduleBreakAttempt(world, pos, state.get(INVERTED), false);
        return state;
    }

    private void scheduleBreakAttempt(WorldAccess world, BlockPos pos, boolean inverted, boolean force) {
        var floor = inverted ? pos.down() : pos.up();
        if (force || !canPlantOnTop(world.getBlockState(floor), world, floor))
            world.createAndScheduleBlockTick(inverted ? pos.up() : pos.down(), this, 1);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var floor = state.get(INVERTED) ? pos.down() : pos.up();
        if (!canPlantOnTop(world.getBlockState(floor), world, floor))
            world.breakBlock(pos, true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STEM_PART, INVERTED, WATERLOGGED);
        super.appendProperties(builder);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
