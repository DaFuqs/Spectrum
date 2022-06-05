package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.enums.ProgressionStage;
import de.dafuqs.spectrum.inventories.GenericSpectrumContainerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnderDropperBlock extends DispenserBlock {
	
	private static final DispenserBehavior BEHAVIOR = new ItemDispenserBehavior();
	
	public EnderDropperBlock(Settings settings) {
		super(settings);
	}
	
	protected DispenserBehavior getBehaviorForItem(ItemStack stack) {
		return BEHAVIOR;
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnderDropperBlockEntity(pos, state);
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity) {
				((EnderDropperBlockEntity) blockEntity).setOwner((ServerPlayerEntity) placer);
				blockEntity.markDirty();
			}
		}
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity) {
				EnderDropperBlockEntity enderDropperBlockEntity = (EnderDropperBlockEntity) blockEntity;
				
				if (!enderDropperBlockEntity.hasOwner()) {
					enderDropperBlockEntity.setOwner(player);
				}
				
				if (enderDropperBlockEntity.isOwner(player)) {
					EnderChestInventory enderChestInventory = player.getEnderChestInventory();
					
					player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
						return GenericSpectrumContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory, ProgressionStage.EARLYGAME);
					}, enderDropperBlockEntity.getContainerName()));
					
					PiglinBrain.onGuardedBlockInteracted(player, true);
				} else {
					player.sendMessage(new TranslatableText("block.spectrum.ender_dropper_with_owner", enderDropperBlockEntity.getOwnerName()), false);
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
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
}