package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

public class NephriteBlossomStemBlock extends PlantBlock implements Waterloggable {

    public static final EnumProperty<StemComponent> STEM_PART = StemComponent.PROPERTY;
	
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	public NephriteBlossomStemBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(STEM_PART, StemComponent.BASE).with(WATERLOGGED, false));
	}
	
	public static BlockState getStemVariant(boolean top) {
		return SpectrumBlocks.NEPHRITE_BLOSSOM_STEM.getDefaultState().with(STEM_PART, top ? StemComponent.STEMALT : StemComponent.STEM);
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.NEPHRITE_BLOSSOM_BULB.getDefaultStack();
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var handStack = player.getStackInHand(hand);
		
		if (handStack.isIn(ConventionalItemTags.SHEARS) && state.get(STEM_PART) == StemComponent.BASE) {
			BlockState newState = state.with(STEM_PART, StemComponent.STEM);
			world.setBlockState(pos, newState);
			player.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
			handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
    
            return ActionResult.success(world.isClient());
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();
        var floor = world.getBlockState(pos.down());

        var state = super.getPlacementState(ctx);

        if (state == null)
            return null;

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
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(this) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        scheduleBreakAttempt(world, pos, true);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        scheduleBreakAttempt(world, pos, false);
        return state;
    }

    private void scheduleBreakAttempt(WorldAccess world, BlockPos pos, boolean force) {
        var down = pos.down();
        if (force || !canPlantOnTop(world.getBlockState(down), world, down))
            world.createAndScheduleBlockTick(pos.up(), this, 1);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var down = pos.down();
        if (!canPlantOnTop(world.getBlockState(down), world, down))
            world.breakBlock(pos, true);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STEM_PART, WATERLOGGED);
        super.appendProperties(builder);
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
