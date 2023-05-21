package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public class BlockPlacerBlock extends DispenserBlock {
	
	public static DispenserBehavior BLOCK_PLACEMENT_BEHAVIOR;
	
	public BlockPlacerBlock(Settings settings) {
		super(settings);
		
		BLOCK_PLACEMENT_BEHAVIOR = new BlockPlacementDispenserBehavior();
		/*BLOCK_PLACEMENT_BEHAVIOR = (pointer, stack) -> {
			if(stack.getItem() instanceof BlockItem blockItem) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				ActionResult result = blockItem.place(new AutomaticItemPlacementContext(pointer.getWorld(), pointer.getPos().add(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()), direction, stack, direction.getOpposite()));
				if(result.isAccepted()) {
					stack.decrement(1);
				}
			}
			return stack;
		};*/
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlockPlacerBlockEntity(pos, state);
	}
	
	@Override
	protected DispenserBehavior getBehaviorForItem(@NotNull ItemStack stack) {
		if (stack.getItem() instanceof BlockItem) {
			return BLOCK_PLACEMENT_BEHAVIOR;
		}
		// If it is not a block item do not handle the selected item at all
		// this is not to overlap with dispenser / dropper behavior
		// and not to dispense items into walls
		return DispenserBehavior.NOOP;
	}
	
}
