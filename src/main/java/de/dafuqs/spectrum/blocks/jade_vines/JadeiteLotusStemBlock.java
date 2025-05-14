package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class JadeiteLotusStemBlock extends BushBlock {
	
	public static final MapCodec<JadeiteLotusStemBlock> CODEC = simpleCodec(JadeiteLotusStemBlock::new);
	
	public static final EnumProperty<StemComponent> STEM_PART = StemComponent.PROPERTY;
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
	
	public JadeiteLotusStemBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(STEM_PART, StemComponent.BASE).setValue(INVERTED, false));
	}
	
	@Override
	public MapCodec<? extends JadeiteLotusStemBlock> codec() {
		return CODEC;
	}
	
	public static BlockState getStemVariant(boolean top, boolean inverted) {
		return SpectrumBlocks.JADEITE_LOTUS_STEM.defaultBlockState().setValue(STEM_PART, top ? StemComponent.STEMALT : StemComponent.STEM).setValue(INVERTED, inverted);
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (handStack.is(ConventionalItemTags.SHEAR_TOOLS) && state.getValue(STEM_PART) == StemComponent.BASE) {
			BlockState newState = state.setValue(STEM_PART, StemComponent.STEM);
			world.setBlockAndUpdate(pos, newState);
			player.playNotifySound(SoundEvents.MOOSHROOM_SHEAR, SoundSource.BLOCKS, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
			handStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		
		return super.useItemOn(handStack, state, world, pos, player, hand, hit);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var world = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var floor = world.getBlockState(pos.above());
		var side = ctx.getClickedFace();
		
		if (!side.getAxis().isVertical())
			return null;
		
		var inverted = side == Direction.UP;
		
		var state = super.getStateForPlacement(ctx);
		
		if (state == null)
			return null;
		
		if (inverted) {
			state = state.setValue(INVERTED, true);
			floor = world.getBlockState(pos.below());
		}
		
		if (floor.is(this)) {
			
			if (floor.getValue(STEM_PART) != StemComponent.STEMALT) {
				state = state.setValue(STEM_PART, StemComponent.STEMALT);
			} else if (floor.getValue(STEM_PART) != StemComponent.STEM) {
				state = state.setValue(STEM_PART, StemComponent.STEM);
			}
		}
		
		return state;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		var floor = state.getValue(INVERTED) ? pos.below() : pos.above();
		return this.mayPlaceOn(world.getBlockState(floor), world, floor);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.isFaceSturdy(world, pos, Direction.UP, SupportType.RIGID) || floor.isFaceSturdy(world, pos, Direction.DOWN, SupportType.RIGID) || floor.is(this);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos)) {
			world.scheduleTick(pos, this, 1);
		}
		
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(state, world, pos, random);
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STEM_PART, INVERTED);
	}
}
