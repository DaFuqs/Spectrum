package de.dafuqs.spectrum.blocks.potion_workshop;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PotionWorkshopBlock extends BlockWithEntity {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_potion_workshop");
	
	public static final BooleanProperty HAS_CONTENT = BooleanProperty.of("has_content");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
	
	public PotionWorkshopBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(HAS_CONTENT, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HAS_CONTENT);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PotionWorkshopBlockEntity(pos, state);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.POTION_WORKSHOP, PotionWorkshopBlockEntity::tick);
		}
		return null;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!(newState.getBlock() instanceof PotionWorkshopBlock)) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
				ItemScatterer.spawn(world, pos, potionWorkshopBlockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
			potionWorkshopBlockEntity.setOwner(player);
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
		}
	}
	
}
