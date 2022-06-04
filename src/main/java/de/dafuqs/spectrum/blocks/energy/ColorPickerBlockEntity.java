package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.storage.TotalCappedSimpleInkStorage;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.ColorPickerScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class ColorPickerBlockEntity extends LootableContainerBlockEntity implements ExtendedScreenHandlerFactory, PlayerOwned, InkStorageBlockEntity<TotalCappedSimpleInkStorage> {
	
	private UUID ownerUUID;
	
	public static final int INVENTORY_SIZE = 2; // input & output slots
	public DefaultedList<ItemStack> inventory;
	
	public static final long STORAGE_AMOUNT = 64*64*64;
	protected TotalCappedSimpleInkStorage inkStorage;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.COLOR_PICKER, blockPos, blockState);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new TotalCappedSimpleInkStorage(STORAGE_AMOUNT);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, ColorPickerBlockEntity blockEntity) {
	
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if(nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = TotalCappedSimpleInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		if(nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("InkStorage", this.inkStorage.toNbt());
		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.color_picker");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, this.pos);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
	}
	
	@Override
	public TotalCappedSimpleInkStorage getEnergyStorage() {
		return inkStorage;
	}

	
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(pos);
	}
	
	// TODO
	/*
	
	public static void tick(World world, BlockPos pos, BlockState state, CrystalApothecaryBlockEntity blockEntity) {
		if (!world.isClient && !paused) {
			boolean didSomething = false;
			if (world.getTime() % 20 == 0) {
				didSomething = tryConvertPigmentToEnergy(); // that's an XOR
			}
			didSomething = didSomething ^ tryFillInkContainer();
			
			if(didSomething) {
				this.markDirty();
			} else {
				this.paused = true;
			}
		}
	}
	
	protected boolean tryConvertPigmentToEnergy() {
		ItemStack stack = inventory.get(PIGMENT_ITEM_SLOT);
		if(stack.getItem() instanceOf PigmentItem pigmentItem) {
			CMYKColor cmykColor = pigmentItem.getCMYKColor();
			if(this.inkStorage.getEnergy(cmykColor) + INK_PER_PIGMENT_ITEM <= this.inkStorage.getMaxPerColor()) {
				stack.decrement();
				this.inkStorage.addEnergy(cmykColor, INK_PER_PIGMENT_ITEM);
				this.playSound(cmykColor.getSoundEvent());
				this.playParticles(cmykColor.getParticleType());
			}
		}
	}
	
	protected boolean tryFillInkContainer() {
		boolean didSomething = false;
		
		ItemStack stack = inventory.get(INK_STORAGE_ITEM_SLOT);
		if(stack.getItem() instanceOf InkStorageItem inkStorageItem) {
			InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
			
			if(this.selectedColor == null) {
				for(CMYKColor color : CMYKColor.values()) {
					didSomething = didSomething ^ transferEnergy(inkStorage, itemStorage, color, INK_TRANSFER_AMOUNT_PER_TICK);
				}
			} else {
				didSomething = didSomething ^ transferEnergy(inkStorage, itemStorage, this.selectedColor, INK_TRANSFER_AMOUNT_PER_TICK);
			}
			
			inkStorageItem.setEnergyStorage(stack, itemStorage);
		}
		
		return didSomething;
	}
	
	// TODO: move to InkStorage class
	// TODO: move to "pressure" system instead of fixed amount where more energy is transferred when source is very full
	public static boolean transferEnergy(InkStorage source, InkStorage destination, CMYKColor color, int amount) {
		long sourceAmount = source.getEnergy(color);
		if(sourceAmount >= 0) {
			long destinationAmount = destination.getEnergy(color);
			long destinationRoom = destination.getRoom(color);
			
			long transferAmount = Math.min(INK_TRANSFER_AMOUNT_PER_TICK, Math.min(sourceAmount, destinationRoom);
			if(transferAmount > 0) {
				destination.addEnergy(color, transferAmount);
				sourceAmount.drainEnergy(color, transferAmount);
				return true;
			}
		}
		return false;
	}*/
	
}
