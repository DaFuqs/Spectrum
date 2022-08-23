package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.storage.TotalCappedInkStorage;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.ColorPickerScreenHandler;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class ColorPickerBlockEntity extends LootableContainerBlockEntity implements ExtendedScreenHandlerFactory, PlayerOwned, InkStorageBlockEntity<TotalCappedInkStorage> {
	
	public static final int INVENTORY_SIZE = 2; // input & output slots
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long STORAGE_AMOUNT = 64 * 64 * 64;
	public DefaultedList<ItemStack> inventory;
	protected TotalCappedInkStorage inkStorage;
	protected boolean paused;
	protected boolean inkDirty;
	protected @Nullable InkConvertingRecipe cachedRecipe;
	protected @Nullable InkColor selectedColor;
	private UUID ownerUUID;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COLOR_PICKER, blockPos, blockState);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new TotalCappedInkStorage(STORAGE_AMOUNT);
		this.selectedColor = null;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, ColorPickerBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.inkDirty = false;
			if (!blockEntity.paused) {
				boolean convertedPigment = false;
				if (world.getTime() % TICKS_PER_CONVERSION == 0) {
					convertedPigment = blockEntity.tryConvertPigmentToEnergy((ServerWorld) world);
				}
				boolean filledContainer = blockEntity.tryFillInkContainer(); // that's an OR
				
				if (convertedPigment || filledContainer) {
					blockEntity.updateInClientWorld();
					blockEntity.setInkDirty();
					blockEntity.markDirty();
				} else {
					blockEntity.paused = true;
				}
			}
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
		if (nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = TotalCappedInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("SelectedColor", NbtElement.STRING_TYPE)) {
			this.selectedColor = InkColor.of(nbt.getString("SelectedColor"));
		}
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		nbt.put("InkStorage", this.inkStorage.toNbt());
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.selectedColor != null) {
			nbt.putString("SelectedColor", this.selectedColor.toString());
		}
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.color_picker");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, this.pos, this.selectedColor);
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
	public TotalCappedInkStorage getEnergyStorage() {
		return inkStorage;
	}
	
	@Override
	public void setInkDirty() {
		this.inkDirty = true;
	}
	
	@Override
	public boolean getInkDirty() {
		return inkDirty;
	}
	
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
		this.paused = false;
		updateInClientWorld();
	}
	
	public ItemStack removeStack(int slot, int amount) {
		ItemStack itemStack = super.removeStack(slot, amount);
		this.paused = false;
		updateInClientWorld();
		return itemStack;
	}
	
	public ItemStack removeStack(int slot) {
		ItemStack itemStack = super.removeStack(slot);
		this.paused = false;
		updateInClientWorld();
		return itemStack;
	}
	
	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
		this.paused = false;
		updateInClientWorld();
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(pos);
		if(this.selectedColor == null) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			buf.writeString(selectedColor.toString());
		}
	}
	
	protected boolean tryConvertPigmentToEnergy(ServerWorld world) {
		InkConvertingRecipe recipe = getInkConvertingRecipe(world);
		if (recipe != null) {
			InkColor color = recipe.getInkColor();
			long amount = recipe.getInkAmount();
			if (this.inkStorage.getEnergy(color) + amount <= this.inkStorage.getMaxPerColor()) {
				inventory.get(INPUT_SLOT_ID).decrement(1);
				this.inkStorage.addEnergy(color, amount);
				
				world.playSound(null, pos, SpectrumSoundEvents.ENCHANTER_DING, SoundCategory.BLOCKS, SpectrumCommon.CONFIG.BlockSoundVolume, 1.0F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world,
						new Vec3d(pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5),
						SpectrumParticleTypes.getFluidRisingParticle(color.getDyeColor()),
						5,
						new Vec3d(0.22, 0.0, 0.22),
						new Vec3d(0.0, 0.1, 0.0)
				);
				
				return true;
			}
		}
		return false;
	}
	
	protected @Nullable InkConvertingRecipe getInkConvertingRecipe(World world) {
		// is the current stack empty?
		ItemStack inputStack = inventory.get(INPUT_SLOT_ID);
		if (inputStack.isEmpty()) {
			this.cachedRecipe = null;
			return null;
		}
		
		// does the cached recipe match?
		if (this.cachedRecipe != null) {
			if (this.cachedRecipe.getIngredients().get(0).test(inputStack)) {
				return this.cachedRecipe;
			}
		}
		
		// search matching recipe
		Optional<InkConvertingRecipe> recipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.INK_CONVERTING, this, world);
		if (recipe.isPresent()) {
			this.cachedRecipe = recipe.get();
			return this.cachedRecipe;
		} else {
			this.cachedRecipe = null;
			return null;
		}
	}
	
	protected boolean tryFillInkContainer() {
		long transferredAmount = 0;
		
		ItemStack stack = inventory.get(OUTPUT_SLOT_ID);
		if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
			InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
			
			if (this.selectedColor == null) {
				boolean searchedOwner = false;
				ServerPlayerEntity owner = null;
				for (InkColor color : InkColor.all()) {
					long amount = InkStorage.transferInk(inkStorage, itemStorage, color);
					
					if (amount > 0) {
						if (!searchedOwner) {
							owner = (ServerPlayerEntity) getOwnerIfOnline();
						}
						if (owner != null) {
							SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(owner, stack, itemStorage, color, amount);
						}
					}
					
					transferredAmount += amount;
				}
			} else {
				transferredAmount = InkStorage.transferInk(inkStorage, itemStorage, this.selectedColor);
				
				if (transferredAmount > 0) {
					PlayerEntity owner = getOwnerIfOnline();
					if (owner instanceof ServerPlayerEntity serverPlayerEntity) {
						SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, itemStorage, this.selectedColor, transferredAmount);
					}
				}
			}
			
			if (transferredAmount > 0) {
				inkStorageItem.setEnergyStorage(stack, itemStorage);
			}
		}
		
		return transferredAmount > 0;
	}
	
	public void setSelectedColor(InkColor inkColor) {
		this.selectedColor = inkColor;
		this.paused = false;
		this.markDirty();
	}
	
	public InkColor getSelectedColor() {
		return this.selectedColor;
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
}
