package de.dafuqs.spectrum.blocks.cinderhearth;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CinderhearthBlockEntity extends BaseContainerBlockEntity implements MultiblockCrafter, WorldlyContainer, ExtendedScreenHandlerFactory<BlockPos>, InkStorageBlockEntity<IndividualCappedInkStorage>, StackedContentsCompatible {
	
	public static final int INVENTORY_SIZE = 11;
	public static final int INPUT_SLOT_ID = 0;
	public static final int INK_PROVIDER_SLOT_ID = 1;
	public static final int EXPERIENCE_STORAGE_ITEM_SLOT_ID = 2;
	public static final int FIRST_OUTPUT_SLOT_ID = 3;
	public static final int LAST_OUTPUT_SLOT_ID = 10;
	public static final int[] OUTPUT_SLOT_IDS = new int[]{3, 4, 5, 6, 7, 8, 9, 10};
	
	protected NonNullList<ItemStack> inventory;
	protected boolean inventoryChanged;
	
	public static final List<InkColor> USED_INK_COLORS = List.of(InkColors.ORANGE, InkColors.MAGENTA, InkColors.LIGHT_BLUE, InkColors.PURPLE, InkColors.BLACK);
	public static final long INK_STORAGE_SIZE = 8 * 64 * 100;
	public static final long INK_COST_PER_TICK = 8;
	protected IndividualCappedInkStorage inkStorage;
	
	private UUID ownerUUID;
	private UpgradeHolder upgrades;
	private RecipeHolder<?> currentRecipe; // blasting & cinderhearth
	private boolean usesEfficiency;
	protected boolean canTransferInk;
	protected boolean inkDirty;
	
	protected CinderHearthStructureType structure = CinderHearthStructureType.NONE;
	
	protected final CraftingDelegate propertyDelegate = new CraftingDelegate();
	
	@Override
	public int[] getSlotsForFace(Direction side) {
		switch (side) {
			case UP -> {
				return new int[]{INPUT_SLOT_ID};
			}
			case DOWN -> {
				return OUTPUT_SLOT_IDS;
			}
			default -> {
				return new int[]{INK_PROVIDER_SLOT_ID, EXPERIENCE_STORAGE_ITEM_SLOT_ID};
			}
		}
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		switch (slot) {
			case INK_PROVIDER_SLOT_ID -> {
				return stack.getItem() instanceof InkStorageItem<?> inkStorageItem && (inkStorageItem.getDrainability().canDrain(false));
			}
			case EXPERIENCE_STORAGE_ITEM_SLOT_ID -> {
				return stack.getItem() instanceof ExperienceStorageItem;
			}
			default -> {
				return true;
			}
		}
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return slot >= FIRST_OUTPUT_SLOT_ID;
	}
	
	public enum CinderHearthStructureType {
		NONE,
		WITH_LAVA,
		WITHOUT_LAVA
	}
	
	public CinderhearthBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CINDERHEARTH, pos, state);
		this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE, USED_INK_COLORS);
	}
	
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.setChanged();
	}
	
	@Override
	public void calculateUpgrades() {
		if (level == null) return;
		this.upgrades = Upgradeable.calculateUpgradeMods2(level, worldPosition, Support.rotationFromDirection(level.getBlockState(worldPosition).getValue(CinderhearthBlock.FACING)), 2, 1, 1, this.ownerUUID);
		this.updateInClientWorld();
		this.setChanged();
	}
	
	public void updateInClientWorld() {
		if (level instanceof ServerLevel serverWorld)
			serverWorld.getChunkSource().blockChanged(worldPosition);
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.setChanged();
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.cinderhearth");
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> inventory) {
		this.inventory = inventory;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new CinderhearthScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
	@Override
	public BlockPos getScreenOpeningData(ServerPlayer serverPlayerEntity) {
		return worldPosition;
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("InkStorage")).ifPresent(storage ->
				this.inkStorage = new IndividualCappedInkStorage(storage.maxPerColor(), storage.storedEnergy()));
		this.propertyDelegate.craftingTime = nbt.getShort("CraftingTime");
		this.propertyDelegate.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.usesEfficiency = nbt.getBoolean("UsesEfficiency");
		this.canTransferInk = nbt.getBoolean("Paused");
		this.inventoryChanged = nbt.getBoolean("InventoryChanged");
		if (nbt.contains("Structure", Tag.TAG_ANY_NUMERIC)) {
			this.structure = CinderHearthStructureType.values()[nbt.getInt("Structure")];
		} else {
			this.structure = CinderHearthStructureType.NONE;
		}
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		this.currentRecipe = MultiblockCrafter.getRecipeHolderFromNbt(level, nbt);
		if (nbt.contains("Upgrades", Tag.TAG_LIST)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", Tag.TAG_COMPOUND));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		CodecHelper.writeNbt(nbt, "InkStorage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
		nbt.putShort("CraftingTime", (short) this.propertyDelegate.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.propertyDelegate.craftingTimeTotal);
		nbt.putBoolean("UsesEfficiency", this.usesEfficiency);
		nbt.putBoolean("Paused", this.canTransferInk);
		nbt.putBoolean("InventoryChanged", this.inventoryChanged);
		nbt.putInt("Structure", this.structure.ordinal());
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.id().toString());
		}
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, CinderhearthBlockEntity cinderhearthBlockEntity) {
		if (cinderhearthBlockEntity.upgrades == null) {
			cinderhearthBlockEntity.calculateUpgrades();
		}
		cinderhearthBlockEntity.inkDirty = false;
		
		if (cinderhearthBlockEntity.canTransferInk) {
			boolean didSomething = false;
			ItemStack stack = cinderhearthBlockEntity.getItem(INK_PROVIDER_SLOT_ID);
			if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
				InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
				didSomething = InkStorage.transferInk(itemStorage, cinderhearthBlockEntity.inkStorage) != 0;
				if (didSomething) {
					inkStorageItem.setEnergyStorage(stack, itemStorage);
				}
			}
			if (didSomething) {
				cinderhearthBlockEntity.setChanged();
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
			if (!canContinue(world, blockPos, cinderhearthBlockEntity)) {
				cinderhearthBlockEntity.currentRecipe = null;
				cinderhearthBlockEntity.propertyDelegate.craftingTime = 0;
				cinderhearthBlockEntity.propertyDelegate.craftingTimeTotal = 0;
				cinderhearthBlockEntity.setChanged();
				return;
			}
			
			if (cinderhearthBlockEntity.propertyDelegate.craftingTime == 0) {
				var recipe = cinderhearthBlockEntity.currentRecipe;
				
				cinderhearthBlockEntity.usesEfficiency = cinderhearthBlockEntity.drainInkForUpgradesRequired(cinderhearthBlockEntity, UpgradeType.EFFICIENCY, InkColors.BLACK, false);
				
				int baseTime = recipe.value() instanceof AbstractCookingRecipe abstractCookingRecipe ? abstractCookingRecipe.getCookingTime()
						: recipe.value() instanceof CinderhearthRecipe cinderhearthRecipe ? cinderhearthRecipe.getCraftingTime()
						: -1;
				if (baseTime >= 0) {
					float speedModifier = cinderhearthBlockEntity.drainInkForUpgrades(cinderhearthBlockEntity, UpgradeType.SPEED, InkColors.MAGENTA, cinderhearthBlockEntity.usesEfficiency);
					cinderhearthBlockEntity.propertyDelegate.craftingTimeTotal = (int) Math.ceil(baseTime / speedModifier);
				}
			}
			
			cinderhearthBlockEntity.propertyDelegate.craftingTime++;
			
			if (cinderhearthBlockEntity.propertyDelegate.craftingTime == cinderhearthBlockEntity.propertyDelegate.craftingTimeTotal) {
				if (cinderhearthBlockEntity.getCurrentRecipe() instanceof CinderhearthRecipe cinderhearthRecipe) {
					craftCinderhearthRecipe(world, cinderhearthBlockEntity, cinderhearthRecipe);
				} else if (cinderhearthBlockEntity.getCurrentRecipe() instanceof BlastingRecipe blastingRecipe) {
					craftBlastingRecipe(world, cinderhearthBlockEntity, blastingRecipe);
				}
			}
			
			cinderhearthBlockEntity.setChanged();
		}
	}
	
	private static boolean canContinue(Level world, BlockPos blockPos, CinderhearthBlockEntity cinderhearthBlockEntity) {
		if (!canAcceptRecipeOutput(world, cinderhearthBlockEntity.currentRecipe, cinderhearthBlockEntity)) {
			return false;
		}
		
		if (cinderhearthBlockEntity.propertyDelegate.craftingTime % 20 == 0) {
			if (!checkRecipeRequirements(world, blockPos, cinderhearthBlockEntity)) {
				return false;
			}
			// consume orange ink
			return cinderhearthBlockEntity.drainInkForUpgradesRequired(cinderhearthBlockEntity, InkColors.ORANGE, INK_COST_PER_TICK, cinderhearthBlockEntity.usesEfficiency);
		}
		
		return true;
	}
	
	protected static boolean canAcceptRecipeOutput(Level world, RecipeHolder<?> recipe, Container inventory) {
		if (recipe != null) {
			ItemStack outputStack = recipe.value().getResultItem(world.registryAccess());
			if (outputStack.isEmpty()) {
				return true;
			} else {
				int outputSpaceFound = 0;
				for (int slot : OUTPUT_SLOT_IDS) {
					ItemStack slotStack = inventory.getItem(slot);
					if (slotStack.isEmpty()) {
						return true;
					} else if (ItemStack.isSameItemSameComponents(slotStack, outputStack)) {
						outputSpaceFound += outputStack.getMaxStackSize() - slotStack.getCount();
						if (outputSpaceFound >= outputStack.getCount()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private static void calculateRecipe(@NotNull Level world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		var input = new SingleRecipeInput(cinderhearthBlockEntity.getItem(0));
		
		// test the cached recipe => faster
		if (cinderhearthBlockEntity.getCurrentRecipe() instanceof CinderhearthRecipe recipe) {
			if (recipe.matches(input, world))
				return;
		} else if (cinderhearthBlockEntity.getCurrentRecipe() instanceof BlastingRecipe recipe) {
			if (recipe.matches(input, world))
				return;
		}
		
		cinderhearthBlockEntity.currentRecipe = null;
		cinderhearthBlockEntity.propertyDelegate.craftingTime = 0;
		cinderhearthBlockEntity.propertyDelegate.craftingTimeTotal = -1;
		
		// cached recipe did not match => calculate new
		ItemStack inputStack = cinderhearthBlockEntity.getItem(0);
		if (!inputStack.isEmpty()) {
			world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.CINDERHEARTH, input, world).ifPresentOrElse(
					r -> cinderhearthBlockEntity.currentRecipe = r,
					() -> world.getRecipeManager().getRecipeFor(RecipeType.BLASTING, input, world).ifPresent(
							r -> cinderhearthBlockEntity.currentRecipe = r));
		}
	}
	
	private static boolean checkRecipeRequirements(Level world, BlockPos blockPos, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		Player lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(cinderhearthBlockEntity.ownerUUID);
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		cinderhearthBlockEntity.structure = CinderhearthBlock.verifyStructure(world, blockPos, null);
		if (cinderhearthBlockEntity.structure == CinderHearthStructureType.NONE) {
			world.playSound(null, cinderhearthBlockEntity.getBlockPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
			return false;
		}
		
		if (cinderhearthBlockEntity.getCurrentRecipe() instanceof GatedRecipe<?> gatedRecipe) {
			return gatedRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		return true;
	}
	
	public static void craftBlastingRecipe(Level world, @NotNull CinderhearthBlockEntity cinderhearth, @NotNull BlastingRecipe blastingRecipe) {
		// calculate outputs
		ItemStack inputStack = cinderhearth.getItem(INPUT_SLOT_ID);
		float yieldMod = inputStack.is(SpectrumItemTags.NO_CINDERHEARTH_DOUBLING) ? 1.0F : cinderhearth.drainInkForUpgrades(cinderhearth, UpgradeType.YIELD, InkColors.LIGHT_BLUE, cinderhearth.usesEfficiency);
		ItemStack output = blastingRecipe.getResultItem(world.registryAccess()).copy();
		List<ItemStack> outputs = new ArrayList<>();
		if (yieldMod > 1) {
			int outputCount = Support.getIntFromDecimalWithChance(output.getCount() * yieldMod, world.random);
			while (outputCount > 0) { // if the rolled count exceeds the max stack size we need to split them (unstackable items, counts > 64, ...)
				int count = Math.min(outputCount, output.getMaxStackSize());
				ItemStack outputStack = output.copy();
				outputStack.setCount(count);
				outputs.add(outputStack);
				outputCount -= count;
			}
		} else {
			outputs.add(output.copy());
		}
		
		// craft
		craftRecipe(cinderhearth, inputStack, outputs, blastingRecipe.getExperience());
	}
	
	public static void craftCinderhearthRecipe(Level world, @NotNull CinderhearthBlockEntity cinderhearth, @NotNull CinderhearthRecipe cinderhearthRecipe) {
		// calculate outputs
		ItemStack inputStack = cinderhearth.getItem(INPUT_SLOT_ID);
		float yieldMod = inputStack.is(SpectrumItemTags.NO_CINDERHEARTH_DOUBLING) ? 1.0F : cinderhearth.drainInkForUpgrades(cinderhearth, UpgradeType.YIELD, InkColors.LIGHT_BLUE, cinderhearth.usesEfficiency);
		List<ItemStack> outputs = cinderhearthRecipe.getRolledOutputs(world.random, yieldMod);
		
		// craft
		craftRecipe(cinderhearth, inputStack, outputs, cinderhearthRecipe.getExperience());
	}
	
	private static void craftRecipe(@NotNull CinderhearthBlockEntity cinderhearth, ItemStack inputStack, List<ItemStack> outputs, float experience) {
		var world = cinderhearth.level;
		if (world == null) return;
		
		NonNullList<ItemStack> backupInventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		for (int i = 0; i < cinderhearth.inventory.size(); i++) {
			backupInventory.set(i, cinderhearth.inventory.get(i));
		}
		
		boolean couldAdd = InventoryHelper.addToInventory(cinderhearth, outputs, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID + 1);
		if (couldAdd) {
			ItemStack remainder = inputStack.getRecipeRemainder();
			
			// use up input ingredient
			ItemStack inputStackCopy = inputStack.copy();
			int amountToDecrementInput = cinderhearth.getCurrentRecipe() instanceof CinderhearthRecipe cinderhearthRecipe ? cinderhearthRecipe.getIngredientStacks().getFirst().getCount() : 1;
			inputStack.shrink(amountToDecrementInput);
			
			if (remainder.isEmpty()) {
				boolean remainderAdded = InventoryHelper.addToInventory(cinderhearth, remainder, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID + 1);
				if (!remainderAdded) {
					cinderhearth.setItem(CinderhearthBlockEntity.INPUT_SLOT_ID, remainder);
				}
			}
			
			// effects
			playCraftingFinishedEffects(cinderhearth);
			
			// reset
			cinderhearth.propertyDelegate.craftingTime = 0;
			cinderhearth.inventoryChanged();
			
			// grant experience & advancements
			float experienceMod = cinderhearth.drainInkForUpgrades(cinderhearth, UpgradeType.EXPERIENCE, InkColors.PURPLE, cinderhearth.usesEfficiency);
			int finalExperience = Support.getIntFromDecimalWithChance(experience * experienceMod, world.random);
			ExperienceStorageItem.addStoredExperience(world.registryAccess(), cinderhearth.getItem(EXPERIENCE_STORAGE_ITEM_SLOT_ID), finalExperience);
			cinderhearth.grantPlayerCinderhearthSmeltingAdvancement(inputStackCopy, outputs, finalExperience);
		} else {
			cinderhearth.inventory = backupInventory;
			
			// prevents trying to craft more until the inventory is freed up
			cinderhearth.propertyDelegate.craftingTimeTotal = -1;
			cinderhearth.currentRecipe = null;
			cinderhearth.inventoryChanged = false;
		}
	}
	
	public void grantPlayerCinderhearthSmeltingAdvancement(ItemStack input, List<ItemStack> outputs, int experience) {
		ServerPlayer serverPlayerEntity = (ServerPlayer) getOwnerIfOnline();
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.CINDERHEARTH_SMELTING.trigger(serverPlayerEntity, input, outputs, experience, this.upgrades);
		}
	}
	
	public static void playCraftingFinishedEffects(@NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		Direction.Axis axis = null;
		Direction direction = Direction.UP;
		if (!(cinderhearthBlockEntity.level instanceof ServerLevel world)) return;
		
		for (Map.Entry<UpgradeType, Integer> entry : cinderhearthBlockEntity.upgrades.entrySet()) {
			if (entry.getValue() > 1) {
				if (axis == null) {
					BlockState state = world.getBlockState(cinderhearthBlockEntity.worldPosition);
					direction = state.getValue(CinderhearthBlock.FACING);
					axis = direction.getAxis();
				}
				
				double d = (double) cinderhearthBlockEntity.worldPosition.getX() + 0.5D;
				double f = (double) cinderhearthBlockEntity.worldPosition.getZ() + 0.5D;
				double g2 = -3D / 16D;
				double h2 = 4D / 16D;
				double i2 = axis == Direction.Axis.X ? (double) direction.getStepX() * g2 : h2;
				double k2 = axis == Direction.Axis.Z ? (double) direction.getStepZ() * g2 : h2;
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world,
						new Vec3(d + i2, cinderhearthBlockEntity.worldPosition.getY() + 1.1, f + k2),
						ParticleTypes.CAMPFIRE_COSY_SMOKE,
						3,
						new Vec3(0.05D, 0.00D, 0.05D),
						new Vec3(0.0D, 0.3D, 0.0D));
			}
		}
	}
	
	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}
	
	@Override
	public ItemStack getItem(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack removedStack = ContainerHelper.removeItem(this.inventory, slot, amount);
		this.inventoryChanged();
		return removedStack;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack removedStack = ContainerHelper.takeItem(this.inventory, slot);
		this.inventoryChanged();
		return removedStack;
	}
	
	@Override
	public void setItem(int slot, @NotNull ItemStack stack) {
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		this.inventoryChanged();
	}
	
	@Override
	public boolean stillValid(Player player) {
		if (level == null || level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	public void inventoryChanged() {
		this.inventoryChanged = true;
		this.canTransferInk = true;
		this.setChanged();
	}
	
	@Override
	public void clearContent() {
		this.inventory.clear();
		this.inventoryChanged();
	}
	
	@Override
	public UpgradeHolder getUpgradeHolder() {
		return this.upgrades;
	}
	
	@Override
	public List<Vec3i> getUpgradePosOffsets() {
		return CinderhearthBlock.UPGRADE_BLOCK_OFFSETS;
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
	
	public RecipeHolder<?> getCurrentRecipeHolder() {
		return currentRecipe;
	}
	
	public Recipe<?> getCurrentRecipe() {
		return currentRecipe == null ? null : currentRecipe.value();
	}
	
	@Override
	public void fillStackedContents(StackedContents finder) {
		this.inventory.forEach(finder::accountStack);
	}
	
}
