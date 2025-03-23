package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
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

public class JadeiteLotusStemBlock extends PlantBlock {

	public static final EnumProperty<StemComponent> STEM_PART = StemComponent.PROPERTY;
	public static final BooleanProperty INVERTED = BooleanProperty.of("inverted");
	
	public JadeiteLotusStemBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(STEM_PART, StemComponent.BASE).with(INVERTED, false));
	}
	
	public static BlockState getStemVariant(boolean top, boolean inverted) {
		return SpectrumBlocks.JADEITE_LOTUS_STEM.getDefaultState().with(STEM_PART, top ? StemComponent.STEMALT : StemComponent.STEM).with(INVERTED, inverted);
	}
	
	@Override
    @SuppressWarnings("deprecation")
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
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, this, 1);
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

    @Override
	@SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(STEM_PART, INVERTED);
	}
}
