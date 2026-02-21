package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
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
import net.minecraft.world.phys.shapes.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

public abstract class TriStateVineBlock extends BushBlock implements BonemealableBlock {
	
	public static final EnumProperty<LifeStage> LIFE_STAGE = EnumProperty.create("life_stage", LifeStage.class);
	private final int minHeight;
	private final float growthTickChance, spreadChance, overgrowth;
	
	public TriStateVineBlock(Properties settings, int minHeight, float growthChance, float spreadChance, float overgrowth) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(LIFE_STAGE, LifeStage.GROWING));
		this.minHeight = minHeight;
		this.growthTickChance = growthChance;
		this.spreadChance = spreadChance;
		this.overgrowth = overgrowth;
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var reference = BlockReference.of(state, pos);
		var creative = player.getAbilities().instabuild;
		
		if (handStack.is(Tags.Items.TOOLS_SHEAR)) {
			if (reference.getProperty(LIFE_STAGE) != LifeStage.GROWING)
				return ItemInteractionResult.FAIL;
			
			if (!creative)
				handStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			
			reference.setProperty(LIFE_STAGE, LifeStage.MATURE);
			reference.update(world);
			
			world.playSound(null, pos, SpectrumSoundEvents.VINE_SHEAR, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(world.random, 0.6F, 1.0F));
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, reference.getState()));
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		} else if (handStack.is(SpectrumItems.MOONSTRUCK_NECTAR)) {
			if (reference.getProperty(LIFE_STAGE) != LifeStage.MATURE)
				return ItemInteractionResult.FAIL;
			
			if (!creative)
				handStack.shrink(1);
			
			reference.setProperty(LIFE_STAGE, LifeStage.GROWING);
			reference.update(world);
			
			world.playSound(null, pos, SpectrumSoundEvents.VINE_INFUSE, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(world.random, 0.6F, 1.0F));
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, reference.getState()));
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var world = ctx.getLevel();
		var pos = ctx.getClickedPos();
		
		var state = defaultBlockState();
		var roof = BlockReference.of(world, pos.above());
		
		if (!canSurvive(world.getBlockState(pos), world, pos) || !world.isEmptyBlock(pos))
			return null;
		
		if (roof.isOf(this)) {
			state = state.setValue(LIFE_STAGE, roof.getProperty(LIFE_STAGE));
			roof.setProperty(LIFE_STAGE, LifeStage.STALK);
			roof.update(world);
		}
		
		return state;
	}
	
	abstract boolean hasGrowthActions();
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LIFE_STAGE) != LifeStage.MATURE;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextFloat() >= growthTickChance)
			return;
		
		var reference = BlockReference.of(state, pos);
		var stage = reference.getProperty(LIFE_STAGE);
		
		if (hasGrowthActions() && random.nextBoolean() || stage != LifeStage.GROWING) {
			performBonemeal(world, random, pos, state);
		} else {
			if (!isBonemealSuccess(world, random, pos, state) || random.nextFloat() >= spreadChance)
				return;
			
			reference.setProperty(LIFE_STAGE, LifeStage.STALK);
			reference.update(world);
			
			var sprigState = defaultBlockState();
			var height = getCurrentHeight(world, reference.pos);
			
			if (height >= minHeight && random.nextFloat() >= overgrowth) {
				sprigState = sprigState.setValue(LIFE_STAGE, LifeStage.MATURE);
			}
			
			world.setBlockAndUpdate(reference.pos.below(), sprigState);
		}
	}
	
	protected int getCurrentHeight(Level world, BlockPos pos) {
		var state = world.getBlockState(pos);
		var count = 0;
		
		while (state.is(this)) {
			count++;
			state = world.getBlockState(pos.above(count));
        }

        return count;
    }
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			var roof = BlockReference.of(level, pos.above());
			
			if (roof.isOf(this)) {
				roof.setProperty(LIFE_STAGE, getLowestLifeStage(level, pos.below(), state.getValue(LIFE_STAGE)));
				roof.update(level);
			}
			
			scheduleBreakCheck(level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
    }
	
	public LifeStage getLowestLifeStage(LevelAccessor world, BlockPos pos, LifeStage stage) {
		var state = world.getBlockState(pos);
		var lastStage = stage;
		while (state.is(this)) {
			lastStage = state.getValue(LIFE_STAGE);
			pos = pos.below();
			state = world.getBlockState(pos);
		}
		return lastStage;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return world.isEmptyBlock(pos.below());
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (canSurvive(state, world, pos))
			return;
		
		scheduleBreakCheck(world, pos);
		world.destroyBlock(pos, true);
	}
	
	private void scheduleBreakCheck(LevelAccessor world, BlockPos pos) {
		var underside = pos.below();
		if (world.getBlockState(underside).is(this))
			world.scheduleTick(underside, this, 1);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			world.scheduleTick(pos, this, 1);
		}
		
		return state;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		var roof = pos.above();
		var roofState = world.getBlockState(roof);
		
		if (roofState.is(this))
			return true;
		
		return mayPlaceOn(roofState, world, roof);
	}
	
	@Override
	public boolean mayPlaceOn(BlockState roof, BlockGetter world, BlockPos pos) {
		return roof.isFaceSturdy(world, pos, Direction.DOWN);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIFE_STAGE);
	}
	
	public enum LifeStage implements StringRepresentable {
		STALK("stalk"),
		GROWING("growing"),
		MATURE("mature");
		
		private final String name;
		
		LifeStage(String name) {
			this.name = name;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
	
	@Override
	public float getMaxHorizontalOffset() {
		return 0.1F;
	}
	
	@Override
	public float getMaxVerticalOffset() {
		return -0.15F;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 vec3d = state.getOffset(world, pos);
		return super.getShape(state, world, pos, context).move(vec3d.x, vec3d.y, vec3d.z);
	}
}
