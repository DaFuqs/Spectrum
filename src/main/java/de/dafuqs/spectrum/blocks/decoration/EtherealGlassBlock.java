package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class EtherealGlassBlock extends AbstractGlassBlock {
	
	public static final int MAX_AGE = 5;
	public static final BooleanProperty EXTEND = BooleanProperty.of("extend");
	public static final IntProperty AGE = Properties.AGE_5;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	
	public EtherealGlassBlock(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return state.get(AGE) == 0 || !(direction == Direction.UP);
	}
	
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (state.get(AGE) != MAX_AGE) {
			world.setBlockState(pos, state.with(AGE, MAX_AGE), 3);
			for (Direction direction : Direction.Type.HORIZONTAL) {
				BlockPos offsetPos = pos.offset(direction);
				BlockState directionState = world.getBlockState(offsetPos);
				if (directionState.getBlock() instanceof EtherealGlassBlock) {
					world.setBlockState(offsetPos, directionState.with(AGE, MAX_AGE - 1).with(EXTEND, true), Block.NOTIFY_LISTENERS);
					world.createAndScheduleBlockTick(offsetPos, this, 2);
				}
			}
		}
		super.onSteppedOn(world, pos, state, entity);
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		boolean extend = state.get(EXTEND);
		if (extend && age > 1) {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				BlockPos offsetPos = pos.offset(direction);
				BlockState directionState = world.getBlockState(offsetPos);
				if (directionState.getBlock() instanceof EtherealGlassBlock && age > directionState.get(AGE)) {
					world.setBlockState(offsetPos, directionState.with(AGE, age - 1).with(EXTEND, true), Block.NOTIFY_LISTENERS);
					world.createAndScheduleBlockTick(offsetPos, this, 2);
				}
			}
			world.setBlockState(pos, state.with(EXTEND, false));
		} else if (!extend && this.increaseAge(state, world, pos)) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			
			for (Direction direction : Direction.values()) {
				mutable.set(pos, direction);
				BlockState blockState = world.getBlockState(mutable);
				if (blockState.isOf(this) && !this.increaseAge(blockState, world, mutable)) {
					world.createAndScheduleBlockTick(mutable, this, 4);
				}
			}
		}
		if (age > 0) {
			world.createAndScheduleBlockTick(pos, this, 10);
		}
	}
	
	private boolean increaseAge(BlockState state, World world, BlockPos pos) {
		int i = state.get(AGE);
		if (i > 0) {
			world.setBlockState(pos, state.with(AGE, i - 1).with(EXTEND, false), Block.NOTIFY_LISTENERS);
			return false;
		} else {
			return true;
		}
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof LivingEntity) {
				return state.getOutlineShape(world, pos);
			}
		}
		return VoxelShapes.empty();
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, EXTEND);
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		int age = state.get((AGE));
		if ((world.isClient && MinecraftClient.getInstance().player.getMainHandStack().isOf(SpectrumBlocks.ETHEREAL_PLATFORM.asItem()))) {
			age = Math.max(age, 3);
		}
		if (age > 0) {
			for (int i = 0; i < age; i++) {
				double d = pos.getX() + random.nextFloat();
				double e = pos.getY() + 1.01;
				double f = pos.getZ() + random.nextFloat();
				world.addParticle(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, d, e, f, 0.0D, 0.03D, 0.0D);
			}
		}
	}
	
}
