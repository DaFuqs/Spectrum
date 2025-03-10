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

public class NephriteBlossomStemBlock extends PlantBlock {

	public static final EnumProperty<StemComponent> STEM_PART = StemComponent.PROPERTY;

	public NephriteBlossomStemBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(STEM_PART, StemComponent.BASE));
	}
	
	public static BlockState getStemVariant(boolean top) {
		return SpectrumBlocks.NEPHRITE_BLOSSOM_STEM.getDefaultState().with(STEM_PART, top ? StemComponent.STEMALT : StemComponent.STEM);
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
		builder.add(STEM_PART);
	}

}
