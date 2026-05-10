package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.neoforge.common.*;
import javax.annotation.*;

public class SawbladeHollyBushBlock extends BushBlock implements BonemealableBlock {
	
	public static final MapCodec<SawbladeHollyBushBlock> CODEC = simpleCodec(SawbladeHollyBushBlock::new);
	
	public static final float DAMAGE = 2.0F;
	
	public static final int MAX_TINY_AGE = 0;
	public static final int MAX_SMALL_AGE = 2;
	public static final int MAX_AGE = BlockStateProperties.MAX_AGE_7;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	private static final VoxelShape SMALL_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
	private static final VoxelShape LARGE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
	
	public SawbladeHollyBushBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}
	
	@Override
	public MapCodec<? extends SawbladeHollyBushBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int i = state.getValue(AGE);
		if (i < MAX_AGE && random.nextInt(5) == 0 && world.getRawBrightness(pos.above(), 0) >= 9) {
			BlockState blockState = state.setValue(AGE, i + 1);
			world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
		}
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (state.getValue(AGE) == 0) {
			return;
		}
		
		if (entity instanceof LivingEntity && !entity.getType().is(SpectrumEntityTypeTags.POKING_DAMAGE_IMMUNE)) {
			entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
			if (!world.isClientSide() && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
				double difX = Math.abs(entity.getX() - entity.xOld);
				double difZ = Math.abs(entity.getZ() - entity.zOld);
				if (difX >= 0.003 || difZ >= 0.003) {
					entity.hurt(SpectrumDamageTypes.bristeSprouts(world), DAMAGE);
				}
			}
		}
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY.get());
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(SpectrumBlockTags.SAWBLADE_HOLLY_PLANTABLE);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(AGE) <= MAX_TINY_AGE) {
			return SMALL_SHAPE;
		} else {
			return state.getValue(AGE) <= MAX_SMALL_AGE ? LARGE_SHAPE : super.getShape(state, world, pos, context);
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int age = state.getValue(AGE);
		
		if (canBeSheared(age) && handStack.is(Tags.Items.TOOLS_SHEAR)) {
			if (!world.isClientSide()) {
				for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, player.getMainHandItem(), SpectrumLootTableKeys.SAWBLADE_HOLLY_SHEARING)) {
					popResource(world, pos, stack);
				}
				handStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			}
			
			BlockState newState = state.setValue(AGE, state.getValue(AGE) - 1);
			world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.SHEAR, pos, GameEvent.Context.of(player, newState));
			world.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		} else if (age == MAX_AGE) {
			if (!world.isClientSide()) {
				for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, player.getMainHandItem(), SpectrumLootTableKeys.SAWBLADE_HOLLY_HARVESTING)) {
					popResource(world, pos, stack);
				}
			}
			world.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
			
			BlockState newState = state.setValue(AGE, 4);
			world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.SHEAR, pos, GameEvent.Context.of(player, newState));
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		} else {
			return super.useItemOn(handStack, state, world, pos, player, hand, hit);
		}
	}
	
	public static boolean canBeSheared(int age) {
		return age > MAX_SMALL_AGE;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		int newAge = Math.min(MAX_AGE, state.getValue(AGE) + (random.nextBoolean() ? 1 : 2));
		world.setBlock(pos, state.setValue(AGE, newAge), Block.UPDATE_CLIENTS);
	}
	
	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		return PathType.DAMAGE_OTHER;
	}
	
	@Override
	public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
		return PathType.DANGER_OTHER;
	}
	
}
