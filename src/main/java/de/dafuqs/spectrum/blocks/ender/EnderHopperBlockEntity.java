package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

public class EnderHopperBlockEntity extends BlockEntity implements PlayerOwnedWithName {
	
	private final VoxelShape INSIDE_SHAPE = Block.box(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private final VoxelShape ABOVE_SHAPE = Block.box(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
	private final VoxelShape INPUT_AREA_SHAPE = Shapes.or(INSIDE_SHAPE, ABOVE_SHAPE);
	
	private @Nullable UUID ownerUUID;
	private @Nullable String ownerName;
	
	private int transferCooldown;
	
	public EnderHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.ENDER_HOPPER, blockPos, blockState);
	}
	
	public static void onEntityCollided(BlockPos pos, Entity entity, EnderHopperBlockEntity enderHopperBlockEntity) {
		if (entity instanceof ItemEntity itemEntity && Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().move((-pos.getX()), (-pos.getY()), (-pos.getZ()))), enderHopperBlockEntity.getInputAreaShape(), BooleanOp.AND)) {
			insertIntoEnderChest(enderHopperBlockEntity, itemEntity);
		}
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos pos, BlockState state, EnderHopperBlockEntity enderHopperBlockEntity) {
		--enderHopperBlockEntity.transferCooldown;
		if (!enderHopperBlockEntity.needsCooldown()) {
			enderHopperBlockEntity.setCooldown(0);
			
			Container sourceInventory = getInputInventory(world, enderHopperBlockEntity);
			if (sourceInventory != null) {
				// if there is a chest on top of the hopper: use that as source
				insertIntoEnderChest(enderHopperBlockEntity, sourceInventory);
			} else {
				// otherwise, search for item stacks
				List<ItemEntity> entities = getInputItemEntities(world, enderHopperBlockEntity);
				
				for (ItemEntity entity : entities) {
					insertIntoEnderChest(enderHopperBlockEntity, entity);
				}
			}
			enderHopperBlockEntity.setCooldown(8);
		}
	}
	
	public static List<ItemEntity> getInputItemEntities(Level world, EnderHopperBlockEntity enderHopperBlockEntity) {
		return enderHopperBlockEntity.getInputAreaShape().toAabbs().stream().flatMap((box) -> world.getEntitiesOfClass(ItemEntity.class, box.move(enderHopperBlockEntity.getHopperX() - 0.5D, enderHopperBlockEntity.getHopperY() - 0.5D, enderHopperBlockEntity.getHopperZ() - 0.5D), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
	}
	
	private static void insertIntoEnderChest(EnderHopperBlockEntity enderHopperBlockEntity, Container sourceInventory) {
		UUID ownerUUID = enderHopperBlockEntity.getOwnerUUID();
		if (ownerUUID != null) {
			Player playerEntity = enderHopperBlockEntity.getOwnerIfOnline();
			if (playerEntity != null) {
				for (int i = 0; i < sourceInventory.getContainerSize(); i++) {
					ItemStack sourceItemStack = sourceInventory.getItem(i).copy();
					if (!sourceItemStack.isEmpty() && InventoryHelper.canExtract(sourceInventory, sourceItemStack, i, Direction.DOWN)) {
						ItemStack remainderStack = addToEnderInventory(sourceItemStack, playerEntity, false);
						
						sourceInventory.setItem(i, remainderStack);
						if (!remainderStack.isEmpty()) {
							enderHopperBlockEntity.setCooldown(40);
						}
						return;
					}
				}
			}
		}
	}
	
	private static void insertIntoEnderChest(EnderHopperBlockEntity enderHopperBlockEntity, ItemEntity itemEntity) {
		Player playerEntity = enderHopperBlockEntity.getOwnerIfOnline();
		if (playerEntity != null) {
			ItemStack sourceItemStack = itemEntity.getItem();
			if (!sourceItemStack.isEmpty()) {
				ItemStack remainderStack = addToEnderInventory(sourceItemStack, playerEntity, false);
				
				if (remainderStack.isEmpty()) {
					itemEntity.discard();
				} else {
					itemEntity.setItem(remainderStack);
				}
			}
		}
	}
	
	public static ItemStack addToEnderInventory(ItemStack additionStack, Player playerEntity, boolean test) {
		PlayerEnderChestContainer enderChestInventory = playerEntity.getEnderChestInventory();
		
		for (int i = 0; i < enderChestInventory.getContainerSize(); i++) {
			ItemStack currentStack = enderChestInventory.getItem(i);
			boolean doneStuff = false;
			if (currentStack.isEmpty()) {
				int maxStackCount = currentStack.getMaxStackSize();
				int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);
				
				if (!test) {
					ItemStack newStack = additionStack.copy();
					newStack.setCount(maxAcceptCount);
					enderChestInventory.setItem(i, newStack);
				}
				additionStack.setCount(additionStack.getCount() - maxAcceptCount);
				doneStuff = true;
			} else if (ItemStack.isSameItemSameComponents(currentStack, additionStack)) {
				// add to stack;
				int maxStackCount = currentStack.getMaxStackSize();
				int canAcceptCount = maxStackCount - currentStack.getCount();
				
				if (canAcceptCount > 0) {
					if (!test) {
						enderChestInventory.getItem(i).grow(Math.min(additionStack.getCount(), canAcceptCount));
					}
					if (canAcceptCount >= additionStack.getCount()) {
						additionStack.setCount(0);
					} else {
						additionStack.setCount(additionStack.getCount() - canAcceptCount);
					}
					doneStuff = true;
				}
			}
			
			// if there were changes: check if all stacks have count 0
			if (doneStuff) {
				if (additionStack.getCount() == 0) {
					return ItemStack.EMPTY;
				}
			}
		}
		return additionStack;
	}

	private static @Nullable Container getInputInventory(Level world, EnderHopperBlockEntity enderHopperBlockEntity) {
		return InventoryHelper.getInventoryAt(world, enderHopperBlockEntity.getHopperX(), enderHopperBlockEntity.getHopperY() + 1.0D, enderHopperBlockEntity.getHopperZ());
	}
	
	protected Component getContainerName() {
		return hasOwner() ? Component.translatable("block.spectrum.ender_hopper.owner", this.ownerName) : Component.translatable("block.spectrum.ender_hopper");
	}
	
	public double getHopperX() {
		return this.worldPosition.getX() + 0.5D;
	}
	
	public double getHopperY() {
		return this.worldPosition.getY() + 0.5D;
	}
	
	public double getHopperZ() {
		return this.worldPosition.getZ() + 0.5D;
	}
	
	private VoxelShape getInputAreaShape() {
		return INPUT_AREA_SHAPE;
	}
	
	private void setCooldown(int cooldown) {
		this.transferCooldown = cooldown;
	}
	
	private boolean needsCooldown() {
		return this.transferCooldown > 0;
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public @Nullable String getOwnerName() {
		return this.ownerName;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.ownerName = playerEntity.getName().getString();
		setChanged();
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		this.ownerUUID = tag.contains("OwnerUUID") ? tag.getUUID("OwnerUUID") : null;
		this.ownerName = tag.contains("OwnerName") ? tag.getString("OwnerName") : null;
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.ownerUUID != null) tag.putUUID("OwnerUUID", this.ownerUUID);
		if (this.ownerName != null) tag.putString("OwnerName", this.ownerName);
	}
}
