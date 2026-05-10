package de.dafuqs.spectrum.blocks.ender;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

import static net.minecraft.world.level.block.HopperBlock.*;

public class EnderHopperBlock extends BaseEntityBlock {
	
	public static final MapCodec<EnderHopperBlock> CODEC = simpleCodec(EnderHopperBlock::new);
	
	private final VoxelShape TOP_SHAPE = box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private final VoxelShape MIDDLE_SHAPE = box(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D);
	private final VoxelShape OUTSIDE_SHAPE = Shapes.or(MIDDLE_SHAPE, TOP_SHAPE);
	private final VoxelShape INSIDE_SHAPE = box(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);
	private final VoxelShape DEFAULT_SHAPE = Shapes.join(OUTSIDE_SHAPE, INSIDE_SHAPE, BooleanOp.ONLY_FIRST);
	private final VoxelShape DOWN_SHAPE = Shapes.or(DEFAULT_SHAPE, Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
	private final VoxelShape DOWN_RAYCAST_SHAPE = INSIDE_SHAPE;
	
	public EnderHopperBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends EnderHopperBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnderHopperBlockEntity(pos, state);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayer) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderHopperBlockEntity) {
				((EnderHopperBlockEntity) blockEntity).setOwner((ServerPlayer) placer);
				blockEntity.setChanged();
			}
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return DOWN_SHAPE;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return DOWN_RAYCAST_SHAPE;
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide() ? null : createTickerHelper(type, SpectrumBlockEntities.ENDER_HOPPER, EnderHopperBlockEntity::serverTick);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.updateEnabled(world, pos, state);
	}
	
	private void updateEnabled(Level world, BlockPos pos, BlockState state) {
		boolean bl = !world.hasNeighborSignal(pos);
		if (bl != state.getValue(ENABLED)) {
			world.setBlock(pos, state.setValue(ENABLED, bl), 4);
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EnderHopperBlockEntity enderHopperBlockEntity) {
			EnderHopperBlockEntity.onEntityCollided(pos, entity, enderHopperBlockEntity);
		}
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ENABLED);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderHopperBlockEntity enderHopperBlockEntity) {
				
				if (!enderHopperBlockEntity.hasOwner()) {
					enderHopperBlockEntity.setOwner(player);
				}
				
				if (enderHopperBlockEntity.isOwner(player)) {
					PlayerEnderChestContainer enderChestInventory = player.getEnderChestInventory();
					
					player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> GenericSpectrumContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory, ScreenBackgroundVariant.EARLYGAME), enderHopperBlockEntity.getContainerName()));
					player.awardStat(Stats.OPEN_ENDERCHEST);
					PiglinAi.angerNearbyPiglins(player, true);
				} else {
					player.displayClientMessage(Component.translatable("block.spectrum.ender_hopper_with_owner", enderHopperBlockEntity.getOwnerName()), true);
				}

			}
			return InteractionResult.CONSUME;
		}
	}
	
}