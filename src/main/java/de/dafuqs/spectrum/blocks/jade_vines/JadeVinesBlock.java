package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JadeVinesBlock extends BlockWithEntity {
	
	protected static final Identifier PETAL_HARVESTING_LOOT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "dynamic/jade_vine_petal_harvesting");
	protected static final Identifier NECTAR_HARVESTING_LOOT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "dynamic/jade_vine_nectar_harvesting");
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public enum JadeVinesBlockPart implements StringIdentifiable {
		UPPER,
		CENTER,
		LOWER;
		
		@Contract(pure = true)
		public @NotNull String toString() {
			return this.asString();
		}
		
		@Contract(pure = true)
		public @NotNull String asString() {
			return this == UPPER ? "upper" : this == CENTER ? "center" : "lower";
		}
		
		public static int getTopPartOffset(@NotNull BlockState blockState) {
			JadeVinesBlockPart part = blockState.get(PART);
			if(part == UPPER) {
				return 0;
			} else if(part == CENTER) {
				return 1;
			}
			return 2;
		}
		
		public static @Nullable BlockState getDownwardsState(@NotNull BlockState blockState) {
			JadeVinesBlockPart part = blockState.get(PART);
			if(part == UPPER) {
				return blockState.with(PART, CENTER);
			} else if(part == CENTER) {
				return blockState.with(PART, LOWER);
			}
			return null;
		}
	}
	
	public enum JadeVinesGrowthStage {
		DEAD,
		LEAVES,
		PETALS,
		BLOOM;
		
		public static JadeVinesGrowthStage fromAge(int age) {
			if(age == 0) {
				return DEAD;
			} else if(age == Properties.AGE_7_MAX) {
				return BLOOM;
			} else if(age > 2) {
				return PETALS;
			} else {
				return LEAVES;
			}
		}
		
		public boolean isFullyGrown() {
			return this == BLOOM;
		}
		
		public static boolean isFullyGrown(int age) {
			return age == Properties.AGE_7_MAX;
		}
		
		public boolean canHarvestPetals() {
			return this == PETALS || this == BLOOM;
		}
		
		public static boolean dead(int age) {
			return age == 0;
		}
		
	}
	
	public static final EnumProperty<JadeVinesBlockPart> PART = EnumProperty.of("part", JadeVinesBlockPart.class);
	public static final IntProperty AGE = Properties.AGE_7;
	
	public JadeVinesBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(PART, JadeVinesBlockPart.UPPER).with(AGE, 1));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		JadeVinesGrowthStage growthStage = JadeVinesGrowthStage.fromAge(state.get(AGE));
		
		if(growthStage.isFullyGrown()) {
			for (ItemStack handStack : player.getItemsHand()) {
				if(handStack.isOf(Items.GLASS_BOTTLE)) {
					if(world.isClient) {
						return ActionResult.SUCCESS;
					} else {
						handStack.decrement(1);
						setPlantToAge(state, world, pos, 2);
						
						List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, handStack, NECTAR_HARVESTING_LOOT_IDENTIFIER);
						for(ItemStack harvestedStack : harvestedStacks){
							Support.givePlayer(player, harvestedStack);
						}
						
						return ActionResult.CONSUME;
					}
				}
			}
		} else if(growthStage.canHarvestPetals()) {
			if(world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				setPlantToAge(state, world, pos, 2);
				
				List<ItemStack> harvestedStacks = getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), PETAL_HARVESTING_LOOT_IDENTIFIER);
				for(ItemStack harvestedStack : harvestedStacks){
					Support.givePlayer(player, harvestedStack);
				}
				
				return ActionResult.CONSUME;
			}
		}
	
		return super.onUse(state, world, pos, player, hand, hit);
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
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.GERMINATED_JADE_VINE_SEEDS.getDefaultStack();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if(oldState.getBlock() instanceof FenceBlock) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
				jadeVinesBlockEntity.setFenceBlockState(oldState.getBlock().getDefaultState());
			}
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if(!newState.isOf(this)) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
				world.setBlockState(pos, jadeVinesBlockEntity.getFenceBlockState());
			}
		}
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		return canBePlacedOn(world.getBlockState(pos));
	}
	
	public static boolean canBePlacedOn(BlockState blockState) {
		return blockState.isIn(BlockTags.WOODEN_FENCES);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		int age = state.get(AGE);
		if(age != 0) {
			// die in sunlight
			if(doesDie(world, pos)) {
				die(state, world, pos);
				world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
			} else if(canGrow(world, pos, state)) {
				if(tryGrowDownwards(state, world, pos)) {
					world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else if(!JadeVinesGrowthStage.isFullyGrown(age)) {
					grow(state, world, pos);
					world.playSound(null, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				}
			}
		}
	}
	
	public static boolean doesDie(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	/**
	 * Set all 3 blocks (up and down) to dead
	 */
	public static void die(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		setPlantToAge(blockState, world, blockPos, 0);
	}
	
	public static void setPlantToAge(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos, int age) {
		JadeVinesBlockPart jadeVinesBlockPart = blockState.get(PART);
		world.setBlockState(blockPos, blockState.with(AGE, age));
		
		int offset = JadeVinesBlockPart.getTopPartOffset(blockState);
		BlockEntity blockEntity = world.getBlockEntity(blockPos.up(offset));
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			jadeVinesBlockEntity.setLastGrownTime(world.getTimeOfDay());
		}
		
		if(jadeVinesBlockPart == JadeVinesBlockPart.UPPER) {
			setToAge(world, blockPos.down(), JadeVinesBlockPart.CENTER, age);
			setToAge(world, blockPos.down(2), JadeVinesBlockPart.LOWER, age);
		} else if(jadeVinesBlockPart == JadeVinesBlockPart.CENTER) {
			setToAge(world, blockPos.up(), JadeVinesBlockPart.UPPER, age);
			setToAge(world, blockPos.down(), JadeVinesBlockPart.LOWER, age);
		} else {
			setToAge(world, blockPos.up(2), JadeVinesBlockPart.UPPER, age);
			setToAge(world, blockPos.up(), JadeVinesBlockPart.CENTER, age);
		}
	}
	
	/**
	 * Grow all 3 blocks (up + down)
	 */
	public static void grow(BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		int age = blockState.get(AGE);
		if(age != 0 && age != Properties.AGE_7_MAX) {
			age += 1;
			setPlantToAge(blockState, world, blockPos, age);
		}
	}
	
	protected static void setToAge(@NotNull World world, BlockPos blockPos, JadeVinesBlockPart part, int newAge) {
		BlockState state = world.getBlockState(blockPos);
		if(state.getBlock() instanceof JadeVinesBlock && state.get(PART) == part) {
			world.setBlockState(blockPos, state.with(AGE, newAge));
		}
	}
	
	public static boolean canGrow(@NotNull World world, @NotNull BlockPos blockPos, BlockState blockState) {
		int offset = JadeVinesBlockPart.getTopPartOffset(blockState);
		BlockEntity blockEntity = world.getBlockEntity(blockPos.up(offset));
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			return world.getLightLevel(LightType.SKY, blockPos) > 8 && jadeVinesBlockEntity.isLaterNight(world);
		}
		return false;
	}
	
	public static boolean tryGrowDownwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		BlockState downwardsState = JadeVinesBlockPart.getDownwardsState(blockState);
		if(downwardsState != null) {
			BlockPos targetPos = blockPos.down();
			BlockState currentDownState = world.getBlockState(targetPos);
			if(canBePlacedOn(currentDownState)) {
				world.setBlockState(targetPos, downwardsState);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(PART, AGE);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JadeVinesBlockEntity(pos, state);
	}
	
}
