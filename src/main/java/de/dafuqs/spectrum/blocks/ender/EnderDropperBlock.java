package de.dafuqs.spectrum.blocks.ender;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
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
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.items.*;
import org.jspecify.annotations.Nullable;

public class EnderDropperBlock extends DispenserBlock {
	
	public static final MapCodec<EnderDropperBlock> CODEC = simpleCodec(EnderDropperBlock::new);
	
	private static final DispenseItemBehavior BEHAVIOR = new DefaultDispenseItemBehavior();
	
	public EnderDropperBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends EnderDropperBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected DispenseItemBehavior getDispenseMethod(Level world, ItemStack stack) {
		return BEHAVIOR;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnderDropperBlockEntity(pos, state);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayer serverPlayer) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity dropperEntity) {
				dropperEntity.setOwner(serverPlayer);
				blockEntity.setChanged();
			}
		}
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		// we don't want to drop the players ender chest content when destroyed
		if (state.hasBlockEntity() && !state.is(newState.getBlock())) {
			level.removeBlockEntity(pos);
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderDropperBlockEntity enderDropperBlockEntity) {
				
				if (!enderDropperBlockEntity.hasOwner()) {
					enderDropperBlockEntity.setOwner(player);
				}
				
				if (enderDropperBlockEntity.isOwner(player)) {
					PlayerEnderChestContainer enderChestInventory = player.getEnderChestInventory();
					
					player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> GenericSpectrumContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory, ScreenBackgroundVariant.EARLYGAME), enderDropperBlockEntity.getDefaultName()));
					
					PiglinAi.angerNearbyPiglins(player, true);
				} else {
					player.displayClientMessage(Component.translatable("block.spectrum.ender_dropper.owner", enderDropperBlockEntity.getOwnerName()), true);
				}
			}
			return InteractionResult.CONSUME;
		}
	}
	
	@Override
	protected void dispenseFrom(ServerLevel world, BlockState state, BlockPos pos) {
		EnderDropperBlockEntity enderDropperBlockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.ENDER_DROPPER.get()).orElse(null);
		if (enderDropperBlockEntity == null) {
			return;
		}
		
		BlockSource blockPointer = new BlockSource(world, pos, state, enderDropperBlockEntity);
		int i = enderDropperBlockEntity.getRandomSlot(world.getRandom());
		if (i < 0) {
			world.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0); // no items in inv
		} else {
			ItemStack itemStack = enderDropperBlockEntity.getItem(i); // empty if owner not online
			if (!itemStack.isEmpty()) {
				Direction direction = world.getBlockState(pos).getValue(FACING);
				if (world.getBlockState(pos.relative(direction)).isAir()) {
					ItemStack itemStack3 = BEHAVIOR.dispense(blockPointer, itemStack);
					enderDropperBlockEntity.setItem(i, itemStack3);
				} else {
					IItemHandler target = world.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.getOpposite());
					if (target != null) {
						ItemStack moved = ItemHandlerHelper.insertItemStacked(target, itemStack.copyWithCount(1), false);
						// return without triggering fail event if successfully moved
						if (moved.isEmpty()) {
							itemStack.shrink(1);
							Player owner = enderDropperBlockEntity.getOwnerIfOnline(world);
							if(owner != null) {
								owner.getEnderChestInventory().setChanged();
							}
							return;
						}
					}
					world.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0); // no room to dispense to
				}
			}
		}
	}
	
}