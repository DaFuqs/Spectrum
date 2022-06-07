package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.interfaces.PlayerOwnedWithName;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnderHopperBlockEntity extends BlockEntity implements PlayerOwnedWithName {
	
	private final VoxelShape INSIDE_SHAPE = Block.createCuboidShape(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private final VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
	private final VoxelShape INPUT_AREA_SHAPE = VoxelShapes.union(INSIDE_SHAPE, ABOVE_SHAPE);
	
	private UUID ownerUUID;
	private String ownerName;
	
	private int transferCooldown;
	
	public EnderHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.ENDER_HOPPER, blockPos, blockState);
	}
	
	public static void onEntityCollided(World world, BlockPos pos, BlockState state, Entity entity, EnderHopperBlockEntity enderHopperBlockEntity) {
		if (entity instanceof ItemEntity && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset((-pos.getX()), (-pos.getY()), (-pos.getZ()))), enderHopperBlockEntity.getInputAreaShape(), BooleanBiFunction.AND)) {
			insertIntoEnderChest(world, enderHopperBlockEntity, (ItemEntity) entity);
		}
	}
	
	public static void serverTick(World world, BlockPos pos, BlockState state, EnderHopperBlockEntity enderHopperBlockEntity) {
		--enderHopperBlockEntity.transferCooldown;
		if (!enderHopperBlockEntity.needsCooldown()) {
			enderHopperBlockEntity.setCooldown(0);
			
			Inventory sourceInventory = getInputInventory(world, enderHopperBlockEntity);
			if (sourceInventory != null) {
				// if there is a chest on top of the hopper: use that as source
				insertIntoEnderChest(world, enderHopperBlockEntity, sourceInventory);
			} else {
				// otherwise search for item stacks
				List<ItemEntity> entities = getInputItemEntities(world, enderHopperBlockEntity);
				
				for (ItemEntity entity : entities) {
					insertIntoEnderChest(world, enderHopperBlockEntity, entity);
				}
			}
			enderHopperBlockEntity.setCooldown(8);
		}
	}
	
	public static List<ItemEntity> getInputItemEntities(World world, EnderHopperBlockEntity enderHopperBlockEntity) {
		return enderHopperBlockEntity.getInputAreaShape().getBoundingBoxes().stream().flatMap((box) -> {
			return world.getEntitiesByClass(ItemEntity.class, box.offset(enderHopperBlockEntity.getHopperX() - 0.5D, enderHopperBlockEntity.getHopperY() - 0.5D, enderHopperBlockEntity.getHopperZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream();
		}).collect(Collectors.toList());
	}
	
	private static boolean insertIntoEnderChest(World world, EnderHopperBlockEntity enderHopperBlockEntity, Inventory sourceInventory) {
		UUID ownerUUID = enderHopperBlockEntity.getOwnerUUID();
		if (ownerUUID != null) {
			PlayerEntity playerEntity = enderHopperBlockEntity.getPlayerEntityIfOnline(world);
			if (playerEntity != null) {
				for (int i = 0; i < sourceInventory.size(); i++) {
					ItemStack sourceItemStack = sourceInventory.getStack(i).copy();
					if (!sourceItemStack.isEmpty() && InventoryHelper.canExtract(sourceInventory, sourceItemStack, i, Direction.DOWN)) {
						ItemStack remainderStack = addToEnderInventory(sourceItemStack, playerEntity, false);
						
						sourceInventory.setStack(i, remainderStack);
						if (!remainderStack.isEmpty()) {
							enderHopperBlockEntity.setCooldown(40);
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean insertIntoEnderChest(World world, EnderHopperBlockEntity enderHopperBlockEntity, ItemEntity itemEntity) {
		UUID ownerUUID = enderHopperBlockEntity.getOwnerUUID();
		if (ownerUUID != null) {
			PlayerEntity playerEntity = enderHopperBlockEntity.getPlayerEntityIfOnline(world);
			if (playerEntity != null) {
				ItemStack sourceItemStack = itemEntity.getStack();
				if (!sourceItemStack.isEmpty()) {
					ItemStack remainderStack = addToEnderInventory(sourceItemStack, playerEntity, false);
					
					if (remainderStack.isEmpty()) {
						itemEntity.discard();
					} else {
						itemEntity.setStack(remainderStack);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public static ItemStack addToEnderInventory(ItemStack additionStack, PlayerEntity playerEntity, boolean test) {
		EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();
		
		for (int i = 0; i < enderChestInventory.size(); i++) {
			ItemStack currentStack = enderChestInventory.getStack(i);
			boolean doneStuff = false;
			if (currentStack.isEmpty()) {
				int maxStackCount = currentStack.getMaxCount();
				int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);
				
				if (!test) {
					ItemStack newStack = additionStack.copy();
					newStack.setCount(maxAcceptCount);
					enderChestInventory.setStack(i, newStack);
				}
				additionStack.setCount(additionStack.getCount() - maxAcceptCount);
				doneStuff = true;
			} else if (additionStack.isItemEqual(currentStack)) {
				// add to stack;
				int maxStackCount = currentStack.getMaxCount();
				int canAcceptCount = maxStackCount - currentStack.getCount();
				
				if (canAcceptCount > 0) {
					if (!test) {
						enderChestInventory.getStack(i).increment(Math.min(additionStack.getCount(), canAcceptCount));
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
	
	@Nullable
	private static Inventory getInputInventory(World world, EnderHopperBlockEntity enderHopperBlockEntity) {
		return InventoryHelper.getInventoryAt(world, enderHopperBlockEntity.getHopperX(), enderHopperBlockEntity.getHopperY() + 1.0D, enderHopperBlockEntity.getHopperZ());
	}
	
	protected Text getContainerName() {
		if (hasOwner()) {
			return new TranslatableText("block.spectrum.ender_hopper").append(new TranslatableText("container.spectrum.owned_by_player", this.ownerName));
		} else {
			return new TranslatableText("block.spectrum.ender_hopper");
		}
	}
	
	public double getHopperX() {
		return this.pos.getX() + 0.5D;
	}
	
	public double getHopperY() {
		return this.pos.getY() + 0.5D;
	}
	
	public double getHopperZ() {
		return this.pos.getZ() + 0.5D;
	}
	
	private VoxelShape getInputAreaShape() {
		return INPUT_AREA_SHAPE;
	}
	
	public ItemStack getStack(int slot) {
		PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
		EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
		return enderInventory.getStack(slot);
	}
	
	private boolean isFull() {
		PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
		EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
		
		for (int i = 0; i < enderInventory.size(); i++) {
			ItemStack itemStack1 = enderInventory.getStack(i);
			if (itemStack1.isEmpty() || itemStack1.getCount() == itemStack1.getMaxCount()) {
				return false;
			}
		}
		return true;
	}
	
	private void setCooldown(int cooldown) {
		this.transferCooldown = cooldown;
	}
	
	private boolean needsCooldown() {
		return this.transferCooldown > 0;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public String getOwnerName() {
		return this.ownerName;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.ownerName = playerEntity.getName().asString();
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if (tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (tag.contains("OwnerName")) {
			this.ownerName = tag.getString("OwnerName");
		} else {
			this.ownerName = null;
		}
	}
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if (this.ownerUUID != null) {
			tag.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			tag.putString("OwnerName", this.ownerName);
		}
	}
	
}
