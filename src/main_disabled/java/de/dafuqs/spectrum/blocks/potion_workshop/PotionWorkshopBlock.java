package de.dafuqs.spectrum.blocks.potion_workshop;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class PotionWorkshopBlock extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final MapCodec<PotionWorkshopBlock> CODEC = simpleCodec(PotionWorkshopBlock::new);
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/potion_workshop");
	
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
	
	public PotionWorkshopBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends PotionWorkshopBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PotionWorkshopBlockEntity(pos, state);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : Support.checkType(type, SpectrumBlockEntities.POTION_WORKSHOP, PotionWorkshopBlockEntity::tick);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return InteractionResult.CONSUME;
		}
	}
	
	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
			potionWorkshopBlockEntity.setOwner(player);
			player.openMenu((MenuProvider) blockEntity);
		}
	}

}
