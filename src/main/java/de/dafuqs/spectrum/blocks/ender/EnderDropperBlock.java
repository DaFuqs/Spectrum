package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.inventories.*;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class EnderDropperBlock extends DispenserBlock {
	
	private static final DispenserBehavior BEHAVIOR = new ItemDispenserBehavior();
	
	public EnderDropperBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected DispenserBehavior getBehaviorForItem(ItemStack stack) {
		return BEHAVIOR;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnderDropperBlockEntity(pos, state);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity) {
				((EnderDropperBlockEntity) blockEntity).setOwner((ServerPlayerEntity) placer);
				blockEntity.markDirty();
			}
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity enderDropperBlockEntity) {
				
				if (!enderDropperBlockEntity.hasOwner()) {
					enderDropperBlockEntity.setOwner(player);
				}
				
				if (enderDropperBlockEntity.isOwner(player)) {
					EnderChestInventory enderChestInventory = player.getEnderChestInventory();
					
					player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> GenericSpectrumContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory, ProgressionStage.EARLYGAME), enderDropperBlockEntity.getContainerName()));
					
					PiglinBrain.onGuardedBlockInteracted(player, true);
				} else {
					player.sendMessage(Text.translatable("block.spectrum.ender_dropper_with_owner", enderDropperBlockEntity.getOwnerName()), true);
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	protected void dispense(ServerWorld world, BlockPos pos) {
		BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);
		EnderDropperBlockEntity enderDropperBlockEntity = blockPointerImpl.getBlockEntity();
		
		int i = enderDropperBlockEntity.chooseNonEmptySlot();
		if (i < 0) {
			world.syncWorldEvent(1001, pos, 0); // no items in inv
		} else {
			ItemStack itemStack = enderDropperBlockEntity.getStack(i);
			if (!itemStack.isEmpty()) {
				Direction direction = world.getBlockState(pos).get(FACING);
				if (world.getBlockState(pos.offset(direction)).isAir()) {
					ItemStack itemStack3 = BEHAVIOR.dispense(blockPointerImpl, itemStack);
					enderDropperBlockEntity.setStack(i, itemStack3);
				} else {
					world.syncWorldEvent(1001, pos, 0); // no room to dispense to
				}
			}
		}
	}

	public DispenserBehavior getDefaultBehaviorForItem(ItemStack stack) {
		return super.getBehaviorForItem(stack);
	}

}