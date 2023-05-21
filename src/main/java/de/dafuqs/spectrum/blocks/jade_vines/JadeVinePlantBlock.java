package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class JadeVinePlantBlock extends Block implements JadeVine, NaturesStaffItem.NaturesStaffTriggered {
	
	public static final EnumProperty<JadeVinesPlantPart> PART = EnumProperty.of("part", JadeVinesPlantPart.class);
	public static final IntProperty AGE = Properties.AGE_7;
	
	public JadeVinePlantBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(PART, JadeVinesPlantPart.BASE).with(AGE, 1));
	}
	
	public static List<ItemStack> getHarvestedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, Identifier lootTableIdentifier) {
		LootContext.Builder builder = (new LootContext.Builder(world)).random(world.random)
				.parameter(LootContextParameters.BLOCK_STATE, state)
				.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
				.parameter(LootContextParameters.TOOL, stack)
				.optionalParameter(LootContextParameters.THIS_ENTITY, entity)
				.optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);

		LootTable lootTable = world.getServer().getLootManager().getTable(lootTableIdentifier);
		return lootTable.generateLoot(builder.build(LootContextTypes.BLOCK));
	}

	static void setHarvested(@NotNull BlockState blockState, @NotNull ServerWorld world, @NotNull BlockPos blockPos) {
		BlockPos rootsPos = blockState.get(PART).getLowestRootsPos(blockPos);
		if (world.getBlockState(rootsPos).getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.setPlantToAge(world, rootsPos, 1);
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		int age = state.get(AGE);
		if (age == Properties.AGE_7_MAX) {
			if (random.nextFloat() < 0.3) {
				JadeVine.spawnBloomParticlesClient(world, pos);
			}
		} else if (age != 0) {
			JadeVine.spawnParticlesClient(world, pos);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos) || missingBottom(state, world.getBlockState(pos.down()))) {
			world.scheduleBlockTick(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos) || missingBottom(state, world.getBlockState(pos.down()))) {
			world.breakBlock(pos, false);
		}
	}

	private boolean missingBottom(BlockState state, BlockState belowState) {
		JadeVinesPlantPart part = state.get(PART);
		if (part == JadeVinesPlantPart.TIP) {
			return false;
		} else {
			return !(belowState.getBlock() instanceof JadeVinePlantBlock);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		JadeVinesGrowthStage growthStage = JadeVinesGrowthStage.fromAge(state.get(AGE));

		if (growthStage.isFullyGrown()) {
			for (ItemStack handStack : player.getHandItems()) {
				if (handStack.isOf(Items.GLASS_BOTTLE)) {
					if (world.isClient) {
						return ActionResult.SUCCESS;
					} else {
						if (player instanceof ServerPlayerEntity serverPlayerEntity) {
							Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayerEntity, pos, handStack);
						}
						
						handStack.decrement(1);
						setHarvested(state, (ServerWorld) world, pos);
						
						List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, handStack, NECTAR_HARVESTING_LOOT_IDENTIFIER);
						for (ItemStack harvestedStack : harvestedStacks) {
							player.getInventory().offerOrDrop(harvestedStack);
						}
						player.sendMessage(Text.translatable("message.spectrum.needs_item_to_harvest").append(Items.GLASS_BOTTLE.getName()), true);
						return ActionResult.CONSUME;
					}
				}
			}
			
			return ActionResult.success(world.isClient);
		} else if (growthStage.canHarvestPetals()) {
			if (!world.isClient) {
				setHarvested(state, (ServerWorld) world, pos);
				
				List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), PETAL_HARVESTING_LOOT_IDENTIFIER);
				for (ItemStack harvestedStack : harvestedStacks) {
					player.getInventory().offerOrDrop(harvestedStack);
				}
			}
			return ActionResult.success(world.isClient);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.GERMINATED_JADE_VINE_BULB.getDefaultStack();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(PART) == JadeVinesPlantPart.TIP ? TIP_SHAPE : SHAPE;
	}

	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		BlockState upState = world.getBlockState(pos.up());
		Block upBlock = upState.getBlock();
		JadeVinesPlantPart part = state.get(PART);
		if (part == JadeVinesPlantPart.BASE) {
			return upBlock instanceof JadeVineRootsBlock;
		} else if (part == JadeVinesPlantPart.MIDDLE) {
			return upBlock instanceof JadeVinePlantBlock && upState.get(PART) == JadeVinesPlantPart.BASE;
		} else {
			return upBlock instanceof JadeVinePlantBlock && upState.get(PART) == JadeVinesPlantPart.MIDDLE;
		}
	}

	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(PART, AGE);
	}

	@Override
	public boolean setToAge(World world, BlockPos blockPos, int age) {
		BlockState currentState = world.getBlockState(blockPos);
		if (currentState.getBlock() instanceof JadeVinePlantBlock) {
			int currentAge = currentState.get(AGE);
			if (age != currentAge) {
				world.setBlockState(blockPos, currentState.with(AGE, age));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (!player.isCreative()) {
				dropStacks(state, world, pos, null, player, player.getMainHandStack());
			}
		}
		
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
	}
	
	@Override
	public boolean canUseNaturesStaff(World world, BlockPos pos, BlockState state) {
		return state.get(AGE) == 0;
	}
	
	@Override
	public boolean onNaturesStaffUse(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos rootsPos = state.get(PART).getLowestRootsPos(pos);
		BlockState rootsState = world.getBlockState(rootsPos);
		if (rootsState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.onNaturesStaffUse(world, rootsPos, rootsState, player);
		}
		JadeVine.spawnParticlesServer((ServerWorld) world, pos, 16);
		return false;
	}
	
	enum JadeVinesPlantPart implements StringIdentifiable {
		BASE,
		MIDDLE,
		TIP;
		
		@Contract(pure = true)
		public @NotNull String toString() {
			return this.asString();
		}
		
		@Override
		@Contract(pure = true)
		public @NotNull String asString() {
			return this == BASE ? "base" : this == MIDDLE ? "middle" : "tip";
		}

		public BlockPos getLowestRootsPos(BlockPos blockPos) {
			if (this == BASE) {
				return blockPos.up();
			} else if (this == MIDDLE) {
				return blockPos.up(2);
			} else {
				return blockPos.up(3);
			}
		}

	}

	enum JadeVinesGrowthStage {
		DEAD,
		LEAVES,
		PETALS,
		BLOOM;

		public static JadeVinesGrowthStage fromAge(int age) {
			if (age == 0) {
				return DEAD;
			} else if (age == Properties.AGE_7_MAX) {
				return BLOOM;
			} else if (age > 2) {
				return PETALS;
			} else {
				return LEAVES;
			}
		}

		public static boolean isFullyGrown(int age) {
			return age == Properties.AGE_7_MAX;
		}

		public boolean isFullyGrown() {
			return this == BLOOM;
		}

		public boolean canHarvestPetals() {
			return this == PETALS || this == BLOOM;
		}

	}

}
