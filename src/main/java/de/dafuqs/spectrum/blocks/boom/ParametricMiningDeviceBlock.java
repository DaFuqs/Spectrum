package de.dafuqs.spectrum.blocks.boom;

import appeng.entity.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.neoforge.event.*;
import org.jspecify.annotations.Nullable;

import javax.annotation.*;

import java.util.*;

public class ParametricMiningDeviceBlock extends PlacedItemBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final MapCodec<ParametricMiningDeviceBlock> CODEC = simpleCodec(ParametricMiningDeviceBlock::new);
	
	public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
		put(Direction.UP, Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D));
		put(Direction.DOWN, Block.box(4.0D, 12.0D, 4.0D, 12.0D, 16.0D, 12.0D));
		put(Direction.NORTH, Block.box(4.0D, 4.0D, 12.0D, 12.0D, 12.0D, 16.0D));
		put(Direction.SOUTH, Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 4.0D));
		put(Direction.EAST, Block.box(0.0D, 4.0D, 4.0D, 4.0D, 12.0D, 12.0D));
		put(Direction.WEST, Block.box(12.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D));
	}};
	
	public ParametricMiningDeviceBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	
	@Override
	public MapCodec<? extends ParametricMiningDeviceBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		return level.getBlockState(blockPos).isFaceSturdy(level, blockPos, direction);
	}
	
	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getClickedFace());
	}
	
	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}
	
	// actual logic
	// press to boom
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		
		if ((level.getBlockEntity(pos) instanceof PlacedItemBlockEntity blockEntity)) {
			ItemStack stack = blockEntity.getStack();
			
			Direction facing = state.getValue(FACING);
			int powerLevel = SpectrumEnchantmentHelper.getLevel(level.registryAccess(), Enchantments.POWER, stack);
			BlockPos explosionPos = pos.relative(facing.getOpposite(), (ExplosionWithStack.BASE_EXPLOSION_LEVEL + powerLevel) / 2);
			Vec3 explosionCenter = Vec3.atCenterOf(explosionPos);
			
			level.removeBlock(pos, false);
			// if explosions spawn inside a block they just destroy that block and nothing else
			// so if that block is not explosion resistant, we yeet it
			if (level.getBlockState(explosionPos).getBlock().getExplosionResistance() < 50) {
				level.removeBlock(explosionPos, false);
			}
			ExplosionWithStack.explode((ServerLevel) level, player, stack, explosionCenter, false);
			
			if(ExplosionWithStack.shouldPreserveExplosive(level, stack)) {
				level.addFreshEntity(new ItemEntity(level, explosionCenter.x, explosionCenter.y, explosionCenter.z, stack));
			}
		}
		
		return InteractionResult.CONSUME;
	}
	
}
