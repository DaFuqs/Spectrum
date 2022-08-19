package de.dafuqs.spectrum.blocks.cinderhearth;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.IndividualCappedInkStorage;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.CinderhearthScreenHandler;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.random.Random;

import java.util.*;

public class CinderhearthBlockEntity extends LockableContainerBlockEntity implements MultiblockCrafter, Inventory, ExtendedScreenHandlerFactory, InkStorageBlockEntity<IndividualCappedInkStorage> {
	
	public static final int INVENTORY_SIZE = 11;
	public static final int INPUT_SLOT_ID = 0;
	public static final int INK_PROVIDER_SLOT_ID = 1;
	public static final int EXPERIENCE_STORAGE_ITEM_SLOT_ID = 2;
	public static final int FIRST_OUTPUT_SLOT_ID = 3;
	public static final int LAST_OUTPUT_SLOT_ID = 11;
	
	protected DefaultedList<ItemStack> inventory;
	protected boolean inventoryChanged;
	
	public static final Set<InkColor> USED_INK_COLORS = Set.of(InkColors.ORANGE, InkColors.LIGHT_BLUE, InkColors.MAGENTA, InkColors.PURPLE, InkColors.BLACK);
	public static final long INK_STORAGE_SIZE = 64*100;
	protected IndividualCappedInkStorage inkStorage;
	
	private UUID ownerUUID;
	private Map<UpgradeType, Float> upgrades;
	private Recipe currentRecipe; // blasting & cinderhearth
	private int craftingTime;
	private int craftingTimeTotal;
	protected boolean canTransferInk;
	protected boolean inkDirty;
	
	protected CinderHearthStructureType structure = CinderHearthStructureType.NONE;
	
	protected final PropertyDelegate propertyDelegate;
	
	enum CinderHearthStructureType {
		NONE,
		WITH_LAVA,
		WITHOUT_LAVA
	}
	
	public CinderhearthBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CINDERHEARTH, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE, USED_INK_COLORS);
		
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				if (index == 0) {
					return CinderhearthBlockEntity.this.craftingTime;
				}
				return CinderhearthBlockEntity.this.craftingTimeTotal;
			}
			
			public void set(int index, int value) {
				switch (index) {
					case 0 -> CinderhearthBlockEntity.this.craftingTime = value;
					case 1 -> CinderhearthBlockEntity.this.craftingTimeTotal = value;
				}
			}
			
			public int size() {
				return 2;
			}
		};
	}
	
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods2(world, pos, Support.rotationFromDirection(world.getBlockState(pos).get(CinderhearthBlock.FACING)), 2, 1, 1, this.ownerUUID);
		this.updateInClientWorld();
		this.markDirty();
	}
	
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.markDirty();
	}
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.cinderhearth");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new CinderhearthScreenHandler(syncId, playerInventory, this.pos, this.propertyDelegate);
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(pos);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		Inventories.readNbt(nbt, this.inventory);
		if(nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = IndividualCappedInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.canTransferInk = nbt.getBoolean("Paused");
		this.inventoryChanged = nbt.getBoolean("InventoryChanged");
		if(nbt.contains("Structure", NbtElement.INT_TYPE)) {
			this.structure = CinderHearthStructureType.values()[nbt.getInt("Structure")];
		} else {
			this.structure = CinderHearthStructureType.NONE;
		}
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty() && world != null) {
				Optional<? extends Recipe> optionalRecipe = world.getRecipeManager().get(new Identifier(recipeString));
				this.currentRecipe = optionalRecipe.orElse(null);
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		nbt.put("InkStorage", this.inkStorage.toNbt());
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbt.putBoolean("Paused", this.canTransferInk);
		nbt.putBoolean("InventoryChanged", this.inventoryChanged);
		nbt.putInt("Structure", this.structure.ordinal());
		if (this.upgrades != null) {
			nbt.put("Upgrades", Upgradeable.toNbt(this.upgrades));
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, CinderhearthBlockEntity cinderhearthBlockEntity) {
		if (cinderhearthBlockEntity.upgrades == null) {
			cinderhearthBlockEntity.calculateUpgrades();
		}
		cinderhearthBlockEntity.inkDirty = false;
		
		if (cinderhearthBlockEntity.canTransferInk) {
			boolean didSomething = false;
			ItemStack stack = cinderhearthBlockEntity.getStack(INK_PROVIDER_SLOT_ID);
			if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
				InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
				didSomething = InkStorage.transferInk(itemStorage, cinderhearthBlockEntity.inkStorage) != 0;
				if (didSomething) {
					inkStorageItem.setEnergyStorage(stack, itemStorage);
				}
			}
			if (didSomething) {
				cinderhearthBlockEntity.markDirty();
				cinderhearthBlockEntity.setInkDirty();
			} else {
				cinderhearthBlockEntity.canTransferInk = false;
			}
		}
		
		if (cinderhearthBlockEntity.inventoryChanged) {
			calculateRecipe(world, cinderhearthBlockEntity);
			cinderhearthBlockEntity.inventoryChanged = false;
			cinderhearthBlockEntity.updateInClientWorld();
		}
		
		if (cinderhearthBlockEntity.currentRecipe != null) {
			if (cinderhearthBlockEntity.craftingTime % 60 == 1) {
				if (!checkRecipeRequirements(world, blockPos, cinderhearthBlockEntity)) {
					cinderhearthBlockEntity.craftingTime = 0;
					return;
				}
			}
			
			if (cinderhearthBlockEntity.currentRecipe != null) {
				if(world.getTime() % 20 == 0) {
					int usedOrangeInk = (int) (4 / cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY), InkColors.BLACK));
					if(cinderhearthBlockEntity.inkStorage.drainEnergy(InkColors.ORANGE, usedOrangeInk) != usedOrangeInk) {
						cinderhearthBlockEntity.currentRecipe = null;
						cinderhearthBlockEntity.craftingTime = 0;
						cinderhearthBlockEntity.craftingTimeTotal = 0;
						cinderhearthBlockEntity.markDirty();
						return;
					}
					cinderhearthBlockEntity.setInkDirty();
				}
				cinderhearthBlockEntity.craftingTime++;
				
				
				
				if (cinderhearthBlockEntity.craftingTime == cinderhearthBlockEntity.craftingTimeTotal) {
					if(cinderhearthBlockEntity.currentRecipe instanceof CinderhearthRecipe cinderhearthRecipe) {
						craftCinderhearthRecipe(world, cinderhearthBlockEntity, cinderhearthRecipe);
					} else if(cinderhearthBlockEntity.currentRecipe instanceof BlastingRecipe blastingRecipe) {
						craftBlastingRecipe(world, cinderhearthBlockEntity, blastingRecipe);
					}
				}
				
				cinderhearthBlockEntity.markDirty();
			}
		}
	}
	
	private static void calculateRecipe(@NotNull World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		// test the cached recipe => faster
		if (cinderhearthBlockEntity.currentRecipe != null) {
			if (cinderhearthBlockEntity.currentRecipe.matches(cinderhearthBlockEntity, world)) {
				return;
			}
		}
		
		cinderhearthBlockEntity.currentRecipe = null;
		cinderhearthBlockEntity.craftingTime = 0;
		cinderhearthBlockEntity.craftingTimeTotal = 0;
		
		// cached recipe did not match => calculate new
		ItemStack instillerStack = cinderhearthBlockEntity.getStack(0);
		if (!instillerStack.isEmpty()) {
			CinderhearthRecipe cinderhearthRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.CINDERHEARTH, cinderhearthBlockEntity, world).orElse(null);
			if(cinderhearthRecipe == null) {
				BlastingRecipe blastingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.BLASTING, cinderhearthBlockEntity, world).orElse(null);
				if(blastingRecipe != null) {
					cinderhearthBlockEntity.currentRecipe = blastingRecipe;
					cinderhearthBlockEntity.craftingTimeTotal = (int) Math.ceil(blastingRecipe.getCookTime() / cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED), InkColors.MAGENTA, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY)));
				}
			} else {
				cinderhearthBlockEntity.currentRecipe = cinderhearthRecipe;
				cinderhearthBlockEntity.craftingTimeTotal = (int) Math.ceil(cinderhearthRecipe.getCraftingTime() / cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED), InkColors.MAGENTA, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY)));
			}
		}
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(cinderhearthBlockEntity.ownerUUID);
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		cinderhearthBlockEntity.structure = CinderhearthBlock.verifyStructure(world, blockPos, null);
		if (cinderhearthBlockEntity.structure == CinderHearthStructureType.NONE) {
			world.playSound(null, cinderhearthBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + cinderhearthBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + cinderhearthBlockEntity.world.random.nextFloat() * 0.2F);
			return false;
		}
		
		if(cinderhearthBlockEntity.currentRecipe instanceof GatedRecipe gatedRecipe) {
			return gatedRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		return true;
	}
	
	public static void craftBlastingRecipe(World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity, @NotNull BlastingRecipe blastingRecipe) {
		// output
		ItemStack inputStack = cinderhearthBlockEntity.getStack(INPUT_SLOT_ID);
		ItemStack output = blastingRecipe.getOutput().copy();
		float yieldMod = inputStack.isIn(SpectrumItemTags.NO_CINDERHEARTH_DOUBLING) ? 1.0F : cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.YIELD), InkColors.LIGHT_BLUE, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY));
		if(yieldMod > 1) {
			output.setCount(Math.min(output.getMaxCount(), Support.getIntFromDecimalWithChance(output.getCount() * yieldMod, world.random)));
		}
		
		boolean couldAdd = InventoryHelper.addToInventory(cinderhearthBlockEntity, output, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		if(couldAdd) {
			cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY), InkColors.BLACK);
			
			Item remainder = inputStack.getItem().getRecipeRemainder();
			
			// use up input ingredient
			inputStack.decrement(1);
			
			if (remainder != null) {
				boolean remainderAdded = InventoryHelper.addToInventory(cinderhearthBlockEntity, remainder.getDefaultStack(), FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
				if(!remainderAdded) {
					cinderhearthBlockEntity.setStack(CinderhearthBlockEntity.INPUT_SLOT_ID, remainder.getDefaultStack());
				}
			}
			
			
			// grant experience
			float experienceMod = cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.EXPERIENCE), InkColors.PURPLE, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY));
			ExperienceStorageItem.addStoredExperience(cinderhearthBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), blastingRecipe.getExperience() * experienceMod, world.random);
			
			// effects
			playCraftingFinishedEffects(cinderhearthBlockEntity);
			
			// reset
			cinderhearthBlockEntity.craftingTime = 0;
			cinderhearthBlockEntity.inventoryChanged();
		} else {
			// prevents trying to craft more until the inventory is freed up
			cinderhearthBlockEntity.craftingTime = 0;
			cinderhearthBlockEntity.currentRecipe = null;
		}
	}
	
	public static void craftCinderhearthRecipe(World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity, @NotNull CinderhearthRecipe cinderhearthRecipe) {
		// output
		ItemStack inputStack = cinderhearthBlockEntity.getStack(INPUT_SLOT_ID);
		float yieldMod = inputStack.isIn(SpectrumItemTags.NO_CINDERHEARTH_DOUBLING) ? 1.0F : cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.YIELD), InkColors.LIGHT_BLUE, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY));
		List<ItemStack> outputs = cinderhearthRecipe.getRolledOutputs(world.random, yieldMod);
		
		DefaultedList<ItemStack> backupInventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		for(int i = 0; i < cinderhearthBlockEntity.inventory.size(); i++) {
			backupInventory.set(i, cinderhearthBlockEntity.inventory.get(i));
		}
		
		boolean couldAdd = InventoryHelper.addToInventory(cinderhearthBlockEntity, outputs, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		if(couldAdd) {
			cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY), InkColors.BLACK);
			Item remainder = inputStack.getItem().getRecipeRemainder();
			
			// use up input ingredient
			inputStack.decrement(1);
			
			if(remainder != null) {
				boolean remainderAdded = InventoryHelper.addToInventory(cinderhearthBlockEntity, remainder.getDefaultStack(), FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
				if(!remainderAdded) {
					cinderhearthBlockEntity.setStack(CinderhearthBlockEntity.INPUT_SLOT_ID, remainder.getDefaultStack());
				}
			}
			
			// grant experience
			float experienceMod = cinderhearthBlockEntity.drainInkForMod(cinderhearthBlockEntity.upgrades.get(UpgradeType.EXPERIENCE), InkColors.PURPLE, cinderhearthBlockEntity.upgrades.get(UpgradeType.EFFICIENCY));
			ExperienceStorageItem.addStoredExperience(cinderhearthBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), cinderhearthRecipe.getExperience() * experienceMod, cinderhearthBlockEntity.world.random);
			
			// effects
			playCraftingFinishedEffects(cinderhearthBlockEntity);
			
			// reset
			cinderhearthBlockEntity.craftingTime = 0;
			cinderhearthBlockEntity.inventoryChanged();
		} else {
			cinderhearthBlockEntity.inventory = backupInventory;
			
			// prevents trying to craft more until the inventory is freed up
			cinderhearthBlockEntity.craftingTimeTotal = 0;
			cinderhearthBlockEntity.currentRecipe = null;
			cinderhearthBlockEntity.inventoryChanged = false;
		}
	}
	
	public static void playCraftingFinishedEffects(@NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		Direction.Axis axis = null;
		Direction direction = null;
		
		for(Map.Entry<UpgradeType, Float> entry : cinderhearthBlockEntity.upgrades.entrySet()) {
			float value = entry.getValue();
			if(value > 1.0) {
				if(axis == null) {
					BlockState state = cinderhearthBlockEntity.world.getBlockState(cinderhearthBlockEntity.pos);
					direction = state.get(CinderhearthBlock.FACING);
					axis = direction.getAxis();
				}
				
				double d = (double)cinderhearthBlockEntity.pos.getX() + 0.5D;
				double e = cinderhearthBlockEntity.pos.getY() + 0.4;
				double f = (double)cinderhearthBlockEntity.pos.getZ() + 0.5D;
				double g2 = -3D / 16D;
				double h2 = 4D / 16D;
				double i2 = axis == Direction.Axis.X ? (double) direction.getOffsetX() * g2 : h2;
				double k2 = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * g2 : h2;
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) cinderhearthBlockEntity.world,
						new Vec3d(d + i2, cinderhearthBlockEntity.pos.getY() + 1.1, f + k2),
						ParticleTypes.CAMPFIRE_COSY_SMOKE,
						3,
						new Vec3d(0.05D, 0.00D, 0.05D),
						new Vec3d(0.0D, 0.3D, 0.0D));
			}
		}
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack removedStack = Inventories.splitStack(this.inventory, slot, amount);
		this.inventoryChanged();
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		ItemStack removedStack = Inventories.removeStack(this.inventory, slot);
		this.inventoryChanged();
		return removedStack;
	}
	
	@Override
	public void setStack(int slot, @NotNull ItemStack stack) {
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
		this.inventoryChanged();
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	public void inventoryChanged() {
		this.inventoryChanged = true;
		this.canTransferInk = true;
		this.markDirty();
	}
	
	@Override
	public void clear() {
		this.inventory.clear();
		this.inventoryChanged();
	}
	
	public Map<UpgradeType, Float> getUpgrades() {
		return this.upgrades;
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		for (ItemStack itemStack : this.inventory) {
			recipeMatcher.addInput(itemStack);
		}
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage() {
		return this.inkStorage;
	}
	
	@Override
	public void setInkDirty() {
		this.inkDirty = true;
	}
	
	@Override
	public boolean getInkDirty() {
		return this.inkDirty;
	}
	
	public Recipe getCurrentRecipe() {
		return currentRecipe;
	}
	
}
