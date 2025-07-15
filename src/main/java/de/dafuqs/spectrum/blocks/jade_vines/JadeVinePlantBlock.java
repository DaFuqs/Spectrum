package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class JadeVinePlantBlock extends Block implements JadeVine, NaturesStaffTriggered {
	
	public static final MapCodec<JadeVinePlantBlock> CODEC = simpleCodec(JadeVinePlantBlock::new);
	
	public static final EnumProperty<JadeVinesPlantPart> PART = EnumProperty.create("part", JadeVinesPlantPart.class);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	
	public JadeVinePlantBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()).setValue(PART, JadeVinesPlantPart.BASE).setValue(AGE, 1));
	}
	
	@Override
	public MapCodec<? extends JadeVinePlantBlock> codec() {
		return CODEC;
	}
	
	public static List<ItemStack> getHarvestedStacks(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, ResourceKey<LootTable> lootTableIdentifier) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.BLOCK_STATE, state)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
				.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(lootTableIdentifier);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
	}
	
	static void setHarvested(@NotNull BlockState blockState, @NotNull ServerLevel world, @NotNull BlockPos blockPos) {
		BlockPos rootsPos = blockState.getValue(PART).getLowestRootsPos(blockPos);
		if (world.getBlockState(rootsPos).getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.setPlantToAge(world, rootsPos, 1);
		}
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		int age = state.getValue(AGE);
		if (age == BlockStateProperties.MAX_AGE_7) {
			if (random.nextFloat() < 0.3) {
				JadeVine.spawnBloomParticlesClient(world, pos);
			}
		} else if (age != 0) {
			JadeVine.spawnParticlesClient(world, pos);
		}
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos) || missingBottom(state, world.getBlockState(pos.below()))) {
			world.scheduleTick(pos, this, 1);
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(world, pos) || missingBottom(state, world.getBlockState(pos.below()))) {
			world.destroyBlock(pos, false);
		}
	}
	
	private boolean missingBottom(BlockState state, BlockState belowState) {
		JadeVinesPlantPart part = state.getValue(PART);
		if (part == JadeVinesPlantPart.TIP) {
			return false;
		} else {
			return !(belowState.getBlock() instanceof JadeVinePlantBlock);
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		JadeVinesGrowthStage growthStage = JadeVinesGrowthStage.fromAge(state.getValue(AGE));
		
		if (growthStage.isFullyGrown()) {
			boolean harvested = false;
			
			if (handStack.is(Items.GLASS_BOTTLE)) {
				if (world.isClientSide) {
					return ItemInteractionResult.SUCCESS;
				} else {
					if (player instanceof ServerPlayer serverPlayerEntity) {
						CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayerEntity, pos, handStack);
					}
					
					handStack.shrink(1);
					setHarvested(state, (ServerLevel) world, pos);
					
					List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, handStack, SpectrumLootTables.JADE_VINE_HARVESTING_NECTAR);
					for (ItemStack harvestedStack : harvestedStacks) {
						player.getInventory().placeItemBackInInventory(harvestedStack);
					}
					harvested = true;
				}
			}
			
			if (!harvested) {
				player.displayClientMessage(Component.translatable("message.spectrum.needs_item_to_harvest").append(Items.GLASS_BOTTLE.getDescription()), true);
			}
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		} else if (growthStage.canHarvestPetals()) {
			if (!world.isClientSide) {
				setHarvested(state, (ServerLevel) world, pos);
				
				List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, player.getMainHandItem(), SpectrumLootTables.JADE_VINE_HARVESTING_PETALS);
				for (ItemStack harvestedStack : harvestedStacks) {
					player.getInventory().placeItemBackInInventory(harvestedStack);
				}
			}
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		}
		
		return super.useItemOn(handStack, state, world, pos, player, hand, hit);
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumItems.GERMINATED_JADE_VINE_BULB.getDefaultInstance();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(PART) == JadeVinesPlantPart.TIP ? TIP_SHAPE : SHAPE;
	}
	
	@Override
	public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
		BlockState upState = world.getBlockState(pos.above());
		Block upBlock = upState.getBlock();
		JadeVinesPlantPart part = state.getValue(PART);
		if (part == JadeVinesPlantPart.BASE) {
			return upBlock instanceof JadeVineRootsBlock;
		} else if (part == JadeVinesPlantPart.MIDDLE) {
			return upBlock instanceof JadeVinePlantBlock && upState.getValue(PART) == JadeVinesPlantPart.BASE;
		} else {
			return upBlock instanceof JadeVinePlantBlock && upState.getValue(PART) == JadeVinesPlantPart.MIDDLE;
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(PART, AGE);
	}
	
	@Override
	public boolean setToAge(Level world, BlockPos blockPos, int age) {
		BlockState currentState = world.getBlockState(blockPos);
		if (currentState.getBlock() instanceof JadeVinePlantBlock) {
			int currentAge = currentState.getValue(AGE);
			if (age != currentAge) {
				world.setBlockAndUpdate(blockPos, currentState.setValue(AGE, age));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			if (!player.isCreative()) {
				dropResources(state, world, pos, null, player, player.getMainHandItem());
			}
		}
		
		return super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
	}
	
	@Override
	public boolean canUseNaturesStaff(Level world, BlockPos pos, BlockState state) {
		return state.getValue(AGE) == 0;
	}
	
	@Override
	public boolean onNaturesStaffUse(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos rootsPos = state.getValue(PART).getLowestRootsPos(pos);
		BlockState rootsState = world.getBlockState(rootsPos);
		if (rootsState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.onNaturesStaffUse(world, rootsPos, rootsState, player);
		}
		JadeVine.spawnParticlesServer((ServerLevel) world, pos, 16);
		return false;
	}
	
	public enum JadeVinesPlantPart implements StringRepresentable {
		BASE,
		MIDDLE,
		TIP;
		
		@Contract(pure = true)
		public @NotNull String toString() {
			return this.getSerializedName();
		}
		
		@Override
		@Contract(pure = true)
		public @NotNull String getSerializedName() {
			return this == BASE ? "base" : this == MIDDLE ? "middle" : "tip";
		}
		
		public BlockPos getLowestRootsPos(BlockPos blockPos) {
			if (this == BASE) {
				return blockPos.above();
			} else if (this == MIDDLE) {
				return blockPos.above(2);
			} else {
				return blockPos.above(3);
			}
		}
		
	}
	
	public enum JadeVinesGrowthStage {
		DEAD,
		LEAVES,
		PETALS,
		BLOOM;
		
		public static JadeVinesGrowthStage fromAge(int age) {
			if (age == 0) {
				return DEAD;
			} else if (age == BlockStateProperties.MAX_AGE_7) {
				return BLOOM;
			} else if (age > 2) {
				return PETALS;
			} else {
				return LEAVES;
			}
		}
		
		public static boolean isFullyGrown(int age) {
			return age == BlockStateProperties.MAX_AGE_7;
		}
		
		public boolean isFullyGrown() {
			return this == BLOOM;
		}
		
		public boolean canHarvestPetals() {
			return this == PETALS || this == BLOOM;
		}
		
	}
	
}
