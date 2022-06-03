package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.storage.TotalCappedSimpleInkStorage;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.ColorPickerScreenHandler;
import de.dafuqs.spectrum.inventories.PotionWorkshopScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ColorPickerBlockEntity extends LootableContainerBlockEntity implements NamedScreenHandlerFactory, PlayerOwned, InkStorageBlockEntity<TotalCappedSimpleInkStorage> {
	
	protected final PropertyDelegate propertyDelegate;
	
	private UUID ownerUUID;
	
	public static final int INVENTORY_SIZE = 2; // input & output slots
	public DefaultedList<ItemStack> inventory;
	
	public static final long STORAGE_AMOUNT = 64*64*64;
	protected TotalCappedSimpleInkStorage inkStorage;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.COLOR_PICKER, blockPos, blockState);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new TotalCappedSimpleInkStorage(STORAGE_AMOUNT);
		
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				return 0;
			}
			
			@Override
			public void set(int index, int value) {
			
			}
			
			@Override
			public int size() {
				return 0;
			}
		};
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
		return new ColorPickerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
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
}
