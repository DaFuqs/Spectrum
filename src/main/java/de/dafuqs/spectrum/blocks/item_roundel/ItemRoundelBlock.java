package de.dafuqs.spectrum.blocks.item_roundel;

import de.dafuqs.spectrum.blocks.InWorldInteractionBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemRoundelBlock extends InWorldInteractionBlock {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public ItemRoundelBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ItemRoundelBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntities.ITEM_ROUNDEL, ItemRoundelBlockEntity::clientTick);
		} else {
			return null;
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
				ItemStack handStack = player.getStackInHand(hand);
				exchangeSingle(world, pos, player, hand, handStack, itemRoundelBlockEntity);
			}
			return ActionResult.CONSUME;
		}
	}
	
}
