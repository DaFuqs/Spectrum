package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		
		if(state.get(AGE) > 0) {
			double x = pos.getX() + 0.2 + (random.nextFloat() * 0.6);
			double y = pos.getY() + 0.2 + (random.nextFloat() * 0.6);
			double z = pos.getZ() + 0.2 + (random.nextFloat() * 0.6);
			
			double velX = 0.06 - random.nextFloat() * 0.12;
			double velY = 0.06 - random.nextFloat() * 0.12;
			double velZ = 0.06 - random.nextFloat() * 0.12;
			
			world.addParticle(SpectrumParticleTypes.JADE_VINES, x, y, z, velX, velY, velZ);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			world.createAndScheduleBlockTick(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			BlockState fenceBlockState = null;
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
				fenceBlockState = jadeVinesBlockEntity.getFenceBlockState();
			}
			
			world.breakBlock(pos, false);
			if(fenceBlockState != null) {
				world.setBlockState(pos, fenceBlockState);
			}
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
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
						setHarvested(state, world, pos);
						
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
				setHarvested(state, world, pos);
				
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
		JadeVinesBlockPart part = state.get(PART);
		if(part == JadeVinesBlockPart.UPPER) {
			return state.isOf(this) || canBePlantedOn(world.getBlockState(pos));
		} else if(part == JadeVinesBlockPart.CENTER) {
			BlockState upState = world.getBlockState(pos.up());
			return upState.getBlock() == state.getBlock() && upState.get(PART) == JadeVinesBlockPart.UPPER;
		} else {
			BlockState upState = world.getBlockState(pos.up());
			return upState.getBlock() == state.getBlock() && upState.get(PART) == JadeVinesBlockPart.CENTER;
		}
	}
	
	public static boolean canBePlantedOn(BlockState blockState) {
		return blockState.isIn(BlockTags.WOODEN_FENCES);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		int age = state.get(AGE);
		if(age != 0) {
			// die in sunlight
			if(doesDie(world, pos)) {
				setDead(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
			} else if(canGrow(world, pos, state)) {
				if(world.random.nextInt(4) == 0 && tryGrowUpwards(state, world, pos)) {
					rememberGrownTime(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else if(tryGrowDownwards(state, world, pos)) {
					rememberGrownTime(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else if(!JadeVinesGrowthStage.isFullyGrown(age)) {
					grow(state, world, pos);
					rememberGrownTime(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				}
			}
		}
	}
	
	public static boolean doesDie(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	public static void setDead(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		setPlantToAge(blockState, world, blockPos, 0);
	}
	
	public static void setHarvested(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		setPlantToAge(blockState, world, blockPos, 2);
	}
	
	public static void setPlantToAge(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos, int age) {
		JadeVinesBlockPart jadeVinesBlockPart = blockState.get(PART);
		world.setBlockState(blockPos, blockState.with(AGE, age));
		
		if(jadeVinesBlockPart == JadeVinesBlockPart.LOWER) {
			setToAge(world, blockPos.up(), JadeVinesBlockPart.CENTER, age);
			
			int yUp = 2;
			while(setToAge(world, blockPos.up(yUp), JadeVinesBlockPart.UPPER, age)) {
				yUp++;
			}
		} else if(jadeVinesBlockPart == JadeVinesBlockPart.CENTER) {
			int yUp = 1;
			while(setToAge(world, blockPos.up(yUp), JadeVinesBlockPart.UPPER, age)) {
				yUp++;
			}
			setToAge(world, blockPos.down(), JadeVinesBlockPart.LOWER, age);
		} else {
			int yUp = 1;
			while(setToAge(world, blockPos.up(yUp), JadeVinesBlockPart.UPPER, age)) {
				yUp++;
			}
			int yDown = 1;
			while(setToAge(world, blockPos.down(yDown), JadeVinesBlockPart.UPPER, age)) {
				yDown++;
			}
			if(setToAge(world, blockPos.down(yDown), JadeVinesBlockPart.CENTER, age)) {
				setToAge(world, blockPos.down(yDown+1), JadeVinesBlockPart.LOWER, age);
			}
		}
	}
	
	private static void rememberGrownTime(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(getMainBlockEntityPos(world, blockPos, blockState));
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			jadeVinesBlockEntity.setLastGrownTime(world.getTimeOfDay());
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
	
	protected static boolean setToAge(@NotNull World world, BlockPos blockPos, JadeVinesBlockPart part, int newAge) {
		BlockState state = world.getBlockState(blockPos);
		if(state.getBlock() instanceof JadeVinesBlock && state.get(PART) == part) {
			world.setBlockState(blockPos, state.with(AGE, newAge));
			return true;
		}
		return false;
	}
	
	// only the upper block states have BlockEntities
	// each one saves and renders the stick these roots are growing on,
	// the lowest of those is considered the "main" one, also keeping track
	// when the plant has grown last
	public static BlockPos getMainBlockEntityPos(@NotNull World world, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		JadeVinesBlockPart part = blockState.get(PART);
		if(part == JadeVinesBlockPart.CENTER) {
			return blockPos.up();
		} else if(part == JadeVinesBlockPart.LOWER) {
			return blockPos.up(2);
		} else {
			// search for the lowest upper state in this column
			for(int i = 1; i < 20; i++) {
				if(blockPos.getY() - i < world.getBottomY()) {
					return blockPos;
				}
				BlockState downState = world.getBlockState(blockPos.down(i));
				if(!(downState.getBlock() instanceof JadeVinesBlock) || downState.get(PART) != JadeVinesBlockPart.UPPER) {
					return blockPos.down(i-1);
				}
			}
			return blockPos;
		}
	}
	
	public static boolean canGrow(@NotNull World world, @NotNull BlockPos blockPos, BlockState blockState) {
		BlockEntity blockEntity = world.getBlockEntity(getMainBlockEntityPos(world, blockPos, blockState));
		if (blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
			return world.getLightLevel(LightType.SKY, blockPos) > 8 && jadeVinesBlockEntity.isLaterNight(world);
		}
		return false;
	}
	
	public static boolean tryGrowUpwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		blockPos = blockPos.up();
		while(world.getBlockState(blockPos).getBlock() instanceof JadeVinesBlock) {
			// search up until no jade vines block is hit
			blockPos = blockPos.up();
		}
		
		BlockState targetState = world.getBlockState(blockPos);
		if(canBePlantedOn(targetState)) {
			world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.UPPER).with(AGE, blockState.get(AGE)));
			
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if(blockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
				jadeVinesBlockEntity.setFenceBlockState(targetState.getBlock().getDefaultState());
			}
			return true;
		}
		return false;
	}
	
	public static boolean tryGrowDownwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		blockPos = blockPos.down();
		while(world.getBlockState(blockPos).getBlock() instanceof JadeVinesBlock) {
			// search up until no jade vines block is hit
			blockPos = blockPos.down();
		}
		
		BlockState targetState = world.getBlockState(blockPos);
		BlockState lastPlantState = world.getBlockState(blockPos.up());
		JadeVinesBlockPart lastPart = lastPlantState.get(PART);
		if(lastPart == JadeVinesBlockPart.LOWER) {
			return false;
		} else if(lastPart == JadeVinesBlockPart.CENTER) {
			if(targetState.isAir()) {
				world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.LOWER));
				return true;
			}
		} else {
			if (canBePlantedOn(targetState)) {
				if(lastPlantState.get(AGE) == 1) {
					world.setBlockState(blockPos.up(), lastPlantState.with(AGE, 2));
					world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.UPPER).with(AGE, 2));
				} else {
					world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.UPPER).with(AGE, blockState.get(AGE)));
				}
				
				long lastGrowTime = -1;
				BlockEntity currentBlockEntity = world.getBlockEntity(blockPos.up());
				if (currentBlockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
					lastGrowTime = jadeVinesBlockEntity.getLastGrownTime();
				}
				
				BlockEntity newBlockEntity = world.getBlockEntity(blockPos);
				if (newBlockEntity instanceof JadeVinesBlockEntity jadeVinesBlockEntity) {
					jadeVinesBlockEntity.setFenceBlockState(targetState.getBlock().getDefaultState());
					if(lastGrowTime > 0) {
						jadeVinesBlockEntity.setLastGrownTime(lastGrowTime);
					} else {
						jadeVinesBlockEntity.setLastGrownTime(world.getTime());
					}
				}
				

				return true;
			} else if (targetState.isAir()) {
				if(lastPlantState.get(AGE) == 1) {
					world.setBlockState(blockPos.up(), lastPlantState.with(AGE, 2));
					world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.CENTER).with(AGE, 2));
				} else {
					world.setBlockState(blockPos, blockState.with(PART, JadeVinesBlockPart.CENTER).with(AGE, blockState.get(AGE)));
				}
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
		if(state.get(PART) == JadeVinesBlockPart.UPPER) {
			return new JadeVinesBlockEntity(pos, state);
		} else {
			return null;
		}
	}
	
}
