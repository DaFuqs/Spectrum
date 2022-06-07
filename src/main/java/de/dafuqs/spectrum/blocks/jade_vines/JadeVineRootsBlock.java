package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class JadeVineRootsBlock extends BlockWithEntity implements JadeVine {
	
	public static final BooleanProperty DEAD = JadeVine.DEAD;
	
	public JadeVineRootsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(DEAD, false));
	}
	
	public static boolean canBePlantedOn(BlockState blockState) {
		return blockState.isIn(BlockTags.WOODEN_FENCES);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!state.get(DEAD)) {
			JadeVine.spawnParticlesClient(world, pos);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
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
		if (oldState.getBlock() instanceof FenceBlock) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
				jadeVineRootsBlockEntity.setFenceBlockState(oldState.getBlock().getDefaultState());
			}
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.isOf(this)) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
				world.setBlockState(pos, jadeVineRootsBlockEntity.getFenceBlockState());
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		return canBePlantedOn(world.getBlockState(pos));
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(DEAD);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JadeVineRootsBlockEntity(pos, state);
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return !state.get(DEAD);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		if (hasRandomTicks(state)) {
			// die in sunlight, or then the bulb / plant was destroyed
			int age = getAge(world, pos, state);
			if (JadeVine.doesDie(world, pos) || age < 0) {
				setDead(world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
			} else if (canGrow(world, pos)) {
				if (world.random.nextBoolean() && tryGrowUpwards(state, world, pos)) {
					rememberGrownTime(world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else if (tryGrowDownwards(state, world, pos)) {
					rememberGrownTime(world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
				} else {
					int targetAge = age;
					if (age == Properties.AGE_7_MAX - 1) {
						// only reach full bloom on full moon nights
						if (world.getMoonPhase() == 0) { // 0 = full moon
							targetAge = Properties.AGE_7_MAX;
						}
					} else if (age == Properties.AGE_7_MAX) {
						// 2 days after full moon: revert to petal stage
						if (world.getMoonPhase() > 2) {
							targetAge = Properties.AGE_7_MAX - 1;
						}
					} else {
						targetAge = age + 1;
					}
					if (targetAge != age) {
						boolean couldGrow = setPlantToAge(world, pos, targetAge);
						if (couldGrow) {
							world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 0.5F, 0.9F + 0.2F * world.random.nextFloat() * 0.2F);
						}
					}
					rememberGrownTime(world, pos);
				}
			}
		}
	}
	
	boolean setPlantToAge(@NotNull ServerWorld world, @NotNull BlockPos blockPos, int age) {
		setToAge(world, blockPos, age);
		
		boolean anyGrown = false;
		
		// all upper roots
		int i = 1;
		while (true) {
			BlockPos upPos = blockPos.up(i);
			BlockState upState = world.getBlockState(upPos);
			if (upState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
				if (jadeVineRootsBlock.setToAge(world, upPos, age)) {
					anyGrown = true;
					JadeVine.spawnParticlesServer(world, upPos, 8);
				}
			} else {
				break;
			}
			i++;
		}
		
		// all lower roots
		i = 1;
		while (true) {
			BlockPos downPos = blockPos.down(i);
			BlockState downState = world.getBlockState(downPos);
			if (downState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
				if (jadeVineRootsBlock.setToAge(world, downPos, age)) {
					anyGrown = true;
					JadeVine.spawnParticlesServer(world, downPos, 8);
				}
			} else {
				break;
			}
			i++;
		}
		
		// bulb / plant
		BlockPos plantPos = blockPos.down(i);
		BlockState plantState = world.getBlockState(plantPos);
		Block plantBlock = plantState.getBlock();
		if (plantBlock instanceof JadeVinePlantBlock jadeVinePlantBlock) {
			if (jadeVinePlantBlock.setToAge(world, plantPos, age) && jadeVinePlantBlock.setToAge(world, plantPos.down(), age) && jadeVinePlantBlock.setToAge(world, plantPos.down(2), age)) {
				anyGrown = true;
				JadeVine.spawnParticlesServer(world, plantPos, 16);
				JadeVine.spawnParticlesServer(world, plantPos.down(), 16);
				JadeVine.spawnParticlesServer(world, plantPos.down(2), 16);
			}
		} else if (plantBlock instanceof JadeVineBulbBlock jadeVineBulbBlock) {
			if (jadeVineBulbBlock.setToAge(world, plantPos, age)) {
				anyGrown = true;
				JadeVine.spawnParticlesServer(world, plantPos, 16);
			}
		} else if (plantState.isAir() && age > 0) {
			// plant was destroyed? => grow a new bulb
			world.setBlockState(plantPos, SpectrumBlocks.JADE_VINE_BULB.getDefaultState());
			anyGrown = true;
			JadeVine.spawnParticlesServer(world, plantPos, 16);
		}
		
		return anyGrown;
	}
	
	// -1 means the plant is not valid anymore and should die off
	// (like the bulb being removed and only roots left)
	int getAge(World world, BlockPos blockPos, BlockState blockState) {
		if (blockState.get(DEAD)) {
			return 0;
		} else {
			BlockPos lowestRootsPos = getLowestRootsPos(world, blockPos);
			BlockState plantState = world.getBlockState(lowestRootsPos.down());
			Block plantBlock = plantState.getBlock();
			if (plantBlock instanceof JadeVinePlantBlock) {
				return plantState.get(JadeVinePlantBlock.AGE);
			} else if (plantBlock instanceof JadeVineBulbBlock) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	boolean canGrow(@NotNull World world, @NotNull BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(getLowestRootsPos(world, blockPos));
		if (blockEntity instanceof JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
			return world.getLightLevel(LightType.SKY, blockPos) > 8 && jadeVineRootsBlockEntity.isLaterNight(world);
		}
		return false;
	}
	
	boolean tryGrowUpwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		blockPos = blockPos.up();
		while (world.getBlockState(blockPos).getBlock() instanceof JadeVineRootsBlock) {
			// search up until no jade vines roots are hit anymore
			blockPos = blockPos.up();
		}
		
		BlockState targetState = world.getBlockState(blockPos);
		if (canBePlantedOn(targetState)) {
			world.setBlockState(blockPos, blockState);
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
				jadeVineRootsBlockEntity.setFenceBlockState(targetState.getBlock().getDefaultState());
			}
			return true;
		}
		return false;
	}
	
	boolean tryGrowDownwards(@NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos) {
		blockPos = blockPos.down();
		while (world.getBlockState(blockPos).getBlock() instanceof JadeVineRootsBlock) {
			// search down until no jade vines roots are hit anymore
			blockPos = blockPos.down();
		}
		
		BlockState targetState = world.getBlockState(blockPos);
		if (targetState.getBlock() instanceof JadeVineBulbBlock) {
			// is there room to grow the whole plant?
			if (world.getBlockState(blockPos.down()).isAir() && world.getBlockState(blockPos.down(2)).isAir()) {
				world.setBlockState(blockPos, SpectrumBlocks.JADE_VINES.getDefaultState().with(JadeVinePlantBlock.PART, JadeVinePlantBlock.JadeVinesPlantPart.BASE));
				world.setBlockState(blockPos.down(), SpectrumBlocks.JADE_VINES.getDefaultState().with(JadeVinePlantBlock.PART, JadeVinePlantBlock.JadeVinesPlantPart.MIDDLE));
				world.setBlockState(blockPos.down(2), SpectrumBlocks.JADE_VINES.getDefaultState().with(JadeVinePlantBlock.PART, JadeVinePlantBlock.JadeVinesPlantPart.TIP));
				return true;
			}
		} else if (targetState.isAir()) {
			world.setBlockState(blockPos, SpectrumBlocks.JADE_VINE_BULB.getDefaultState());
			return true;
		} else if (canBePlantedOn(targetState)) {
			world.setBlockState(blockPos, SpectrumBlocks.JADE_VINE_ROOTS.getDefaultState());
			
			long lastGrowTime = -1;
			BlockEntity currentBlockEntity = world.getBlockEntity(blockPos.up());
			if (currentBlockEntity instanceof JadeVineRootsBlockEntity rootsBlockEntity) {
				lastGrowTime = rootsBlockEntity.getLastGrownTime();
			}
			
			BlockEntity newBlockEntity = world.getBlockEntity(blockPos);
			if (newBlockEntity instanceof JadeVineRootsBlockEntity rootsBlockEntity) {
				rootsBlockEntity.setFenceBlockState(targetState.getBlock().getDefaultState());
				if (lastGrowTime > 0) {
					rootsBlockEntity.setLastGrownTime(lastGrowTime);
				} else {
					rootsBlockEntity.setLastGrownTime(world.getTime());
				}
			}
			return true;
		}
		return false;
	}
	
	void setDead(@NotNull ServerWorld world, @NotNull BlockPos blockPos) {
		setPlantToAge(world, blockPos, 0);
	}
	
	void rememberGrownTime(@NotNull World world, @NotNull BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(getLowestRootsPos(world, blockPos));
		if (blockEntity instanceof JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
			jadeVineRootsBlockEntity.setLastGrownTime(world.getTimeOfDay());
		}
	}
	
	// each root saves and renders the stick these roots are growing on,
	// the lowest root in a stack is considered the "main" one, also keeping track
	// when the plant has grown last
	// => search for the lowest upper state in this column
	public BlockPos getLowestRootsPos(@NotNull World world, @NotNull BlockPos blockPos) {
		int i = 0;
		do {
			if (world.getBlockState(blockPos.down(i + 1)).getBlock() instanceof JadeVineRootsBlock) {
				i--;
			} else {
				break;
			}
		} while (blockPos.getY() - i >= world.getBottomY());
		return blockPos.down(i);
	}
	
	@Override
	public boolean setToAge(@NotNull World world, BlockPos blockPos, int age) {
		BlockState currentState = world.getBlockState(blockPos);
		boolean dead = currentState.get(DEAD);
		if (age == 0 && !dead) {
			world.setBlockState(blockPos, currentState.with(DEAD, true));
			return true;
		} else if (age > 0 && dead) {
			world.setBlockState(blockPos, currentState.with(DEAD, false));
			return true;
		}
		return false;
	}
	
}
