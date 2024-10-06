package de.dafuqs.spectrum.blocks.pedestal;

import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PedestalBlockEntity extends LockableContainerBlockEntity implements MultiblockCrafter, RecipeInputProvider, SidedInventory, ExtendedScreenHandlerFactory {
	
	public static final int INVENTORY_SIZE = 16; // 9 crafting, 5 gems, 1 craftingTablet, 1 output
	public static final int CRAFTING_TABLET_SLOT_ID = 14;
	public static final int OUTPUT_SLOT_ID = 15;
	
	private static final int[] ACCESSIBLE_SLOTS_UP = {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private static final int[] ACCESSIBLE_SLOTS_BASIC = {9, 10, 11};
	private static final int[] ACCESSIBLE_SLOTS_ADVANCED = {9, 10, 11, 12};
	private static final int[] ACCESSIBLE_SLOTS_COMPLEX = {9, 10, 11, 12, 13};
	
	protected final AutoCraftingInventory autoCraftingInventory;
	protected final PropertyDelegate propertyDelegate;
	protected UUID ownerUUID;
	protected PedestalVariant pedestalVariant;
	protected DefaultedList<ItemStack> inventory;
	protected boolean shouldCraft;
	protected float storedXP;
	protected int craftingTime;
	protected int craftingTimeTotal;
	public @Nullable Recipe<?> currentRecipe;
	protected PedestalRecipeTier cachedMaxPedestalTier;
	protected long cachedMaxPedestalTierTick;
	protected UpgradeHolder upgrades;
	protected boolean inventoryChanged;
	
	public PedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PEDESTAL, blockPos, blockState);
		
		if (blockState.getBlock() instanceof PedestalBlock pedestalBlock) {
			this.pedestalVariant = pedestalBlock.getVariant();
		} else {
			this.pedestalVariant = BuiltinPedestalVariant.BASIC_AMETHYST;
		}
		autoCraftingInventory = new AutoCraftingInventory(3, 3);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> PedestalBlockEntity.this.craftingTime;
					default -> PedestalBlockEntity.this.craftingTimeTotal;
				};
			}
			
			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> PedestalBlockEntity.this.craftingTime = value;
					case 1 -> PedestalBlockEntity.this.craftingTimeTotal = value;
				}
			}
			
			@Override
			public int size() {
				return 2;
			}
		};
	}
	
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {
		Recipe<?> currentRecipe = pedestalBlockEntity.getCurrentRecipe();
		if (currentRecipe instanceof PedestalRecipe pedestalRecipe) {
			Map<GemstoneColor, Integer> gemstonePowderInputs = pedestalRecipe.getPowderInputs();
			
			for (Map.Entry<GemstoneColor, Integer> entry : gemstonePowderInputs.entrySet()) {
				int amount = entry.getValue();
				if (amount > 0) {
					ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(entry.getKey().getDyeColor());
					
					float particleAmount = Support.getIntFromDecimalWithChance(amount * 0.125, world.random);
					for (int i = 0; i < particleAmount; i++) {
						float randomX = 2.0F - world.getRandom().nextFloat() * 5;
						float randomZ = 2.0F - world.getRandom().nextFloat() * 5;
						world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
					}
				}
			}
		}
	}
	
	public static void spawnCraftingStartParticles(@NotNull World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
			Recipe<?> currentRecipe = pedestalBlockEntity.getCurrentRecipe();
			if (currentRecipe instanceof PedestalRecipe pedestalRecipe) {
				Map<GemstoneColor, Integer> gemstonePowderInputs = pedestalRecipe.getPowderInputs();
				
				for (Map.Entry<GemstoneColor, Integer> entry : gemstonePowderInputs.entrySet()) {
					int amount = entry.getValue();
					if (amount > 0) {
						ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(entry.getKey().getDyeColor());
						
						amount = amount * 4;
						Random random = world.random;
						for (int i = 0; i < amount; i++) {
							Direction direction = Direction.random(random);
							if (direction != Direction.DOWN) {
								BlockPos offsetPos = blockPos.offset(direction);
								BlockState offsetState = world.getBlockState(offsetPos);
								if (!offsetState.isSideSolidFullSquare(world, offsetPos, direction.getOpposite())) {
									double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
									double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetY() * 0.6D;
									double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
									world.addParticle(particleEffect, (double) blockPos.getX() + d, (double) blockPos.getY() + e, (double) blockPos.getZ() + f, 0.0D, 0.03D, 0.0D);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {
		if (pedestalBlockEntity.upgrades == null) {
			pedestalBlockEntity.calculateUpgrades();
		}
		
		// check recipe crafted last tick => performance
		boolean shouldMarkDirty = false;
		
		Recipe<?> calculatedRecipe = calculateRecipe(world, pedestalBlockEntity);
		pedestalBlockEntity.inventoryChanged = false;
		if (pedestalBlockEntity.currentRecipe != calculatedRecipe) {
			pedestalBlockEntity.shouldCraft = false;
			pedestalBlockEntity.currentRecipe = calculatedRecipe;
			pedestalBlockEntity.craftingTime = 0;
			if (calculatedRecipe instanceof PedestalRecipe calculatedPedestalRecipe) {
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(calculatedPedestalRecipe.getCraftingTime() / pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED));
				
				PlayerEntity player = pedestalBlockEntity.getOwnerIfOnline();
				if (player instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.PEDESTAL_RECIPE_CALCULATED.trigger(serverPlayerEntity, calculatedPedestalRecipe.craft(pedestalBlockEntity, world.getRegistryManager()), (int) calculatedPedestalRecipe.getExperience(), pedestalBlockEntity.craftingTimeTotal);
				}
			} else {
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(SpectrumCommon.CONFIG.VanillaRecipeCraftingTimeTicks / pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED));
			}
			pedestalBlockEntity.markDirty();
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos());
			pedestalBlockEntity.updateInClientWorld();
		}
		
		// only craft when there is redstone power
		if (pedestalBlockEntity.craftingTime == 0 && !pedestalBlockEntity.shouldCraft && !(blockState.getBlock() instanceof PedestalBlock && blockState.get(PedestalBlock.POWERED))) {
			return;
		}
		
		int maxCountPerStack = pedestalBlockEntity.getMaxCountPerStack();
		// Pedestal crafting
		boolean craftingFinished = false;
		if (calculatedRecipe instanceof PedestalRecipe pedestalRecipe && pedestalBlockEntity.canAcceptRecipeOutput(calculatedRecipe, pedestalBlockEntity, maxCountPerStack)) {
			pedestalBlockEntity.craftingTime++;
			if (pedestalBlockEntity.craftingTime == pedestalBlockEntity.craftingTimeTotal) {
				pedestalBlockEntity.craftingTime = 0;
				craftingFinished = craftPedestalRecipe(pedestalBlockEntity, pedestalRecipe, pedestalBlockEntity, maxCountPerStack);
				if (craftingFinished) {
					pedestalBlockEntity.inventoryChanged = true;
				}
				shouldMarkDirty = true;
			}
			// Vanilla crafting
		} else if (calculatedRecipe instanceof CraftingRecipe vanillaCraftingRecipe && pedestalBlockEntity.canAcceptRecipeOutput(calculatedRecipe, pedestalBlockEntity, maxCountPerStack)) {
			pedestalBlockEntity.craftingTime++;
			if (pedestalBlockEntity.craftingTime == pedestalBlockEntity.craftingTimeTotal) {
				pedestalBlockEntity.craftingTime = 0;
				craftingFinished = pedestalBlockEntity.craftVanillaRecipe(vanillaCraftingRecipe, pedestalBlockEntity, maxCountPerStack);
				if (craftingFinished) {
					playCraftingFinishedSoundEvent(pedestalBlockEntity, calculatedRecipe);
					pedestalBlockEntity.inventoryChanged = true;
				}
				shouldMarkDirty = true;
			}
		}
		
		if (pedestalBlockEntity.craftingTime == 1 && pedestalBlockEntity.craftingTimeTotal > 1) {
			SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.PEDESTAL_CRAFTING, (ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos(), pedestalBlockEntity.craftingTimeTotal - pedestalBlockEntity.craftingTime);
		}
		
		// try to output the currently stored output stack
		ItemStack outputItemStack = pedestalBlockEntity.inventory.get(OUTPUT_SLOT_ID);
		if (outputItemStack != ItemStack.EMPTY) {
			if (world.getBlockState(blockPos.up()).getCollisionShape(world, blockPos.up()).isEmpty()) {
				spawnOutputAsItemEntity(world, blockPos, pedestalBlockEntity, outputItemStack);
			} else {
				boolean couldOutput = false;
				BlockEntity belowBlockEntity = world.getBlockEntity(blockPos.down());
				if (belowBlockEntity instanceof Inventory belowInventory) {
					couldOutput = tryPutIntoInventory(pedestalBlockEntity, belowInventory, outputItemStack);
				}
				if (!couldOutput) {
					BlockEntity aboveBlockEntity = world.getBlockEntity(blockPos.up());
					if (aboveBlockEntity instanceof Inventory aboveInventory && !(aboveBlockEntity instanceof HopperBlockEntity)) {
						couldOutput = tryPutIntoInventory(pedestalBlockEntity, aboveInventory, outputItemStack);
					}
				}
				if (couldOutput) {
					shouldMarkDirty = true;
				} else {
					// play sound when the entity can not put its output anywhere
					if (craftingFinished) {
						pedestalBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH);
					}
				}
			}
		}
		
		if (shouldMarkDirty) {
			markDirty(world, blockPos, blockState);
		}
	}
	
	@Contract(pure = true)
	public static PedestalVariant getVariant(@NotNull PedestalBlockEntity pedestalBlockEntity) {
		return pedestalBlockEntity.pedestalVariant;
	}
	
	public static void spawnOutputAsItemEntity(World world, BlockPos blockPos, @NotNull PedestalBlockEntity pedestalBlockEntity, ItemStack outputItemStack) {
		// spawn crafting output
		MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, pedestalBlockEntity.pos, outputItemStack, outputItemStack.getCount(), new Vec3d(0, 0.1, 0));
		pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);
		
		// spawn XP
		MultiblockCrafter.spawnExperience(world, pedestalBlockEntity.pos, pedestalBlockEntity.storedXP, world.random);
		pedestalBlockEntity.storedXP = 0;
		
		// only triggered on server side. Therefore, has to be sent to client via S2C packet
		SpectrumS2CPacketSender.sendPlayPedestalCraftingFinishedParticle(world, blockPos, outputItemStack);
	}
	
	public static boolean tryPutIntoInventory(PedestalBlockEntity pedestalBlockEntity, Inventory targetInventory, ItemStack outputItemStack) {
		ItemStack remainingStack = InventoryHelper.smartAddToInventory(outputItemStack, targetInventory, Direction.DOWN);
		if (remainingStack.isEmpty()) {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);
			return true;
		} else {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, remainingStack);
			return false;
		}
	}
	
	public static void playCraftingFinishedSoundEvent(PedestalBlockEntity pedestalBlockEntity, Recipe<?> craftingRecipe) {
		World world = pedestalBlockEntity.getWorld();
		if (craftingRecipe instanceof PedestalRecipe pedestalRecipe) {
			pedestalBlockEntity.playSound(pedestalRecipe.getSoundEvent(world.random));
		} else {
			pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_GENERIC);
		}
	}
	
	public static @Nullable Recipe<?> calculateRecipe(World world, @NotNull PedestalBlockEntity pedestalBlockEntity) {
		if (!pedestalBlockEntity.inventoryChanged) {
			return pedestalBlockEntity.currentRecipe;
		}
		
		// unchanged pedestal recipe?
		if (pedestalBlockEntity.currentRecipe instanceof PedestalRecipe pedestalRecipe && pedestalRecipe.matches(pedestalBlockEntity, world)) {
			return pedestalBlockEntity.currentRecipe;
		}
		
		// unchanged vanilla recipe?
		if (SpectrumCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			pedestalBlockEntity.autoCraftingInventory.setInputInventory(pedestalBlockEntity.inventory.subList(0, 9));
			if (pedestalBlockEntity.currentRecipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.matches(pedestalBlockEntity.autoCraftingInventory, world)) {
				return pedestalBlockEntity.currentRecipe;
			}
		}
		
		// current recipe does not match last recipe
		// => search valid recipe
		PedestalRecipe pedestalRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.PEDESTAL, pedestalBlockEntity, world).orElse(null);
		if (pedestalRecipe == null) {
			if (SpectrumCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
				return world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, pedestalBlockEntity.autoCraftingInventory, world).orElse(null);
			}
			return null;
		}
		
		if (!pedestalRecipe.canCraft(pedestalBlockEntity)) {
			return null;
		}
		return pedestalRecipe;
	}
	
	private boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, Inventory inventory, int maxCountPerStack) {
		if (recipe != null) {
			ItemStack output;
			if (recipe instanceof PedestalRecipe pedestalRecipe) {
				output = pedestalRecipe.craft(inventory, null);
			} else if (recipe instanceof CraftingRecipe craftingRecipe) {
				autoCraftingInventory.setInputInventory(inventory, 0, 9);
				output = craftingRecipe.craft(autoCraftingInventory, null);
			} else {
				output = ItemStack.EMPTY;
			}
			
			if (output.isEmpty()) {
				return false;
			}
			
			ItemStack existingOutput = this.getStack(OUTPUT_SLOT_ID);
			if (existingOutput.isEmpty()) {
				return true;
			}
			
			if (!ItemStack.canCombine(existingOutput, output)) {
				return false;
			}
			
			if (existingOutput.getCount() < maxCountPerStack && existingOutput.getCount() < existingOutput.getMaxCount()) {
				return true;
			}
			
			return existingOutput.getCount() < output.getMaxCount();
		}
		
		return false;
	}
	
	private static boolean craftPedestalRecipe(PedestalBlockEntity pedestalBlockEntity, @Nullable PedestalRecipe recipe, Inventory inventory, int maxCountPerStack) {
		if (!pedestalBlockEntity.canAcceptRecipeOutput(recipe, inventory, maxCountPerStack)) {
			return false;
		}
		ItemStack outputStack = recipe.craft(pedestalBlockEntity, pedestalBlockEntity.getWorld().getRegistryManager());
		recipe.consumeIngredients(pedestalBlockEntity);
		
		if (!recipe.areYieldUpgradesDisabled()) {
			double yieldModifier = pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.YIELD);
			if (yieldModifier != 1.0) {
				int modifiedCount = Support.getIntFromDecimalWithChance(outputStack.getCount() * yieldModifier, pedestalBlockEntity.world.random);
				outputStack.setCount(Math.min(outputStack.getMaxCount(), modifiedCount));
			}
		}
		
		// Add the XP for this recipe to the pedestal
		float experience = recipe.getExperience() * pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.EXPERIENCE);
		pedestalBlockEntity.storedXP += experience;
		
		PlayerEntity player = pedestalBlockEntity.getOwnerIfOnline();
		if (player != null) {
			outputStack.onCraft(pedestalBlockEntity.world, player, outputStack.getCount());
		}
		
		// trigger advancements
		pedestalBlockEntity.grantPlayerPedestalCraftingAdvancement(outputStack, (int) experience, pedestalBlockEntity.craftingTimeTotal);
		
		// if it was a recipe to upgrade the pedestal itself
		// => upgrade
		PedestalVariant newPedestalVariant = PedestalRecipe.getUpgradedPedestalVariantForOutput(outputStack);
		if (newPedestalVariant != null && newPedestalVariant.isBetterThan(getVariant(pedestalBlockEntity))) {
			// It is an upgrade recipe (output is a pedestal block item)
			// => Upgrade
			pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_UPGRADE);
			PedestalBlock.upgradeToVariant(pedestalBlockEntity.world, pedestalBlockEntity.getPos(), newPedestalVariant);
			SpectrumS2CPacketSender.spawnPedestalUpgradeParticles(pedestalBlockEntity.world, pedestalBlockEntity.pos, newPedestalVariant);
			
			pedestalBlockEntity.pedestalVariant = newPedestalVariant;
			pedestalBlockEntity.currentRecipe = null; // reset the recipe, otherwise pedestal would remember crafting the update
		} else {
			// Not an upgrade recipe => Add output to output slot
			ItemStack existingOutput = inventory.getStack(OUTPUT_SLOT_ID);
			if (!existingOutput.isEmpty()) {
				// merge existing & newly crafted stacks
				// protection against stacks > max stack size
				outputStack.setCount(Math.min(existingOutput.getMaxCount(), existingOutput.getCount() + outputStack.getCount()));
			}
			inventory.setStack(OUTPUT_SLOT_ID, outputStack);
		}
		
		pedestalBlockEntity.markDirty();
		pedestalBlockEntity.inventoryChanged = true;
		pedestalBlockEntity.updateInClientWorld();
		
		return true;
	}
	
	public static Item getGemstonePowderItemForSlot(int slot) {
		return switch (slot) {
			case 9 -> SpectrumItems.TOPAZ_POWDER;
			case 10 -> SpectrumItems.AMETHYST_POWDER;
			case 11 -> SpectrumItems.CITRINE_POWDER;
			case 12 -> SpectrumItems.ONYX_POWDER;
			case 13 -> SpectrumItems.MOONSTONE_POWDER;
			default -> Items.AIR;
		};
	}
	
	public static int getSlotForGemstonePowder(GemstoneColor gemstoneColor) {
		return switch (gemstoneColor.getDyeColor()) {
			case CYAN -> 9;
			case MAGENTA -> 10;
			case YELLOW -> 11;
			case BLACK -> 12;
			case WHITE -> 13;
			default -> -1;
		};
	}
	
	public void setVariant(PedestalVariant pedestalVariant) {
		this.pedestalVariant = pedestalVariant;
		this.propertyDelegate.set(2, pedestalVariant.getRecipeTier().ordinal());
	}
	
	@Override
	public Text getContainerName() {
		return Text.translatable("block.spectrum.pedestal");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new PedestalScreenHandler(syncId, playerInventory, this, this.propertyDelegate, this.pedestalVariant.getRecipeTier().ordinal(), this.getHighestAvailableRecipeTier().ordinal(), this.pos);
	}
	
	@Override
	public int size() {
		return this.inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		Iterator<ItemStack> var1 = this.inventory.iterator();
		
		ItemStack itemStack;
		do {
			if (!var1.hasNext()) {
				return true;
			}
			
			itemStack = var1.next();
		} while (itemStack.isEmpty());
		
		return false;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack removedStack = Inventories.splitStack(this.inventory, slot, amount);
		this.inventoryChanged = true;
		this.markDirty();
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		ItemStack removedStack = Inventories.removeStack(this.inventory, slot);
		this.inventoryChanged = true;
		this.markDirty();
		return removedStack;
	}
	
	@Override
	public void setStack(int slot, @NotNull ItemStack stack) {
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
		
		this.inventoryChanged = true;
		this.markDirty();
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.getWorld().getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		for (ItemStack itemStack : this.inventory) {
			recipeMatcher.addInput(itemStack);
		}
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	// Called when the chunk is first loaded to initialize this be or manually synced via updateInClientWorld()
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
		
		if (nbt.contains("StoredXP")) {
			this.storedXP = nbt.getFloat("StoredXP");
		}
		if (nbt.contains("CraftingTime")) {
			this.craftingTime = nbt.getShort("CraftingTime");
		}
		if (nbt.contains("CraftingTimeTotal")) {
			this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		}
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		} else {
			this.upgrades = new UpgradeHolder();
		}
		if (nbt.contains("inventory_changed")) {
			this.inventoryChanged = nbt.getBoolean("inventory_changed");
		}
		
		this.currentRecipe = null;
		this.currentRecipe = MultiblockCrafter.getRecipeFromNbt(world, nbt, Recipe.class);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putFloat("StoredXP", this.storedXP);
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbt.putBoolean("inventory_changed", this.inventoryChanged);
		
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
		
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		Inventories.writeNbt(nbt, this.inventory);
	}
	
	@Override
	public void clear() {
		this.inventory.clear();
	}
	
	public boolean isCrafting() {
		return this.craftingTime > 0;
	}
	
	private void playSound(SoundEvent soundEvent) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.9F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
	}
	
	private boolean craftVanillaRecipe(@Nullable CraftingRecipe recipe, Inventory inventory, int maxCountPerStack) {
		if (canAcceptRecipeOutput(recipe, inventory, maxCountPerStack)) {
			autoCraftingInventory.setInputInventory(inventory, 0, 9);
			
			ItemStack recipeOutput = recipe.craft(autoCraftingInventory, null);
			PlayerEntity player = getOwnerIfOnline();
			if (player != null) { // some recipes may assume the player is never null (since this is the case in vanilla)
				recipeOutput.onCraft(this.getWorld(), player, recipeOutput.getCount());
			}
			
			// -1 for all crafting inputs
			decrementInputStacks(inventory);
			
			ItemStack existingOutput = inventory.getStack(OUTPUT_SLOT_ID);
			if (existingOutput.isEmpty()) {
				inventory.setStack(OUTPUT_SLOT_ID, recipeOutput.copy());
			} else {
				existingOutput.increment(recipeOutput.getCount());
				inventory.setStack(OUTPUT_SLOT_ID, existingOutput);
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	private void decrementInputStacks(Inventory inventory) {
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = inventory.getStack(i);
			if (!itemStack.isEmpty()) {
				ItemStack remainder = itemStack.getRecipeRemainder();
				if (remainder.isEmpty()) {
					itemStack.decrement(1);
				} else {
					if (this.inventory.get(i).getCount() == 1) {
						this.inventory.set(i, remainder);
					} else {
						this.inventory.get(i).decrement(1);
						ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, remainder);
						itemEntity.addVelocity(0, 0.05, 0);
						world.spawnEntity(itemEntity);
					}
				}
			}
		}
	}
	
	private void grantPlayerPedestalCraftingAdvancement(ItemStack output, int experience, int duration) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) getOwnerIfOnline();
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.PEDESTAL_CRAFTING.trigger(serverPlayerEntity, output, experience, duration);
		}
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot < 9) {
			return true;
		} else if (slot == CRAFTING_TABLET_SLOT_ID && stack.isOf(SpectrumItems.CRAFTING_TABLET)) {
			return true;
		} else {
			return stack.getItem().equals(getGemstonePowderItemForSlot(slot));
		}
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return new int[]{OUTPUT_SLOT_ID};
		} else if (side == Direction.UP) {
			return ACCESSIBLE_SLOTS_UP;
		} else {
			switch (this.pedestalVariant.getRecipeTier()) {
				case COMPLEX -> {
					return ACCESSIBLE_SLOTS_COMPLEX;
				}
				case ADVANCED -> {
					return ACCESSIBLE_SLOTS_ADVANCED;
				}
				default -> {
					return ACCESSIBLE_SLOTS_BASIC;
				}
			}
		}
	}
	
	@Override
	public boolean canInsert(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
		if (stack.isOf(getGemstonePowderItemForSlot(slot))) {
			return true;
		}
		
		if (slot < 9 && inventory.get(CRAFTING_TABLET_SLOT_ID).isOf(SpectrumItems.CRAFTING_TABLET)) {
			ItemStack craftingTabletItem = inventory.get(CRAFTING_TABLET_SLOT_ID);
			
			if (inventory.get(slot).getCount() > 0) {
				return false;
			}
			
			Recipe<?> storedRecipe = CraftingTabletItem.getStoredRecipe(this.getWorld(), craftingTabletItem);
			
			int width = 3;
			if (storedRecipe instanceof ShapedRecipe shapedRecipe) {
				width = shapedRecipe.getWidth();
				if (slot % 3 >= width) {
					return false;
				}
			} else if (storedRecipe instanceof ShapedPedestalRecipe pedestalRecipe) {
				width = pedestalRecipe.getWidth();
				if (slot % 3 >= width) {
					return false;
				}
			} else if (!(storedRecipe instanceof ShapelessRecipe) && !(storedRecipe instanceof ShapelessPedestalRecipe)) {
				return false;
			}
			
			int resultRecipeSlot = getCraftingRecipeSlotDependingOnWidth(slot, width);
			if (resultRecipeSlot < storedRecipe.getIngredients().size()) {
				Ingredient ingredient = storedRecipe.getIngredients().get(resultRecipeSlot);
				return ingredient.test(stack);
			} else {
				return false;
			}
		} else {
			return slot < CRAFTING_TABLET_SLOT_ID;
		}
	}
	
	private int getCraftingRecipeSlotDependingOnWidth(int slot, int recipeWidth) {
		int line = slot / 3;
		int posInLine = slot % 3;
		return line * recipeWidth + posInLine;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot == OUTPUT_SLOT_ID;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	public @Nullable Recipe<?> getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	public ItemStack getCurrentCraftingRecipeOutput() {
		if (this.currentRecipe == null) {
			return ItemStack.EMPTY;
		}
		
		if (this.currentRecipe instanceof PedestalRecipe pedestalRecipe) {
			return pedestalRecipe.craft(this, this.world.getRegistryManager());
		}
		
		if (this.currentRecipe instanceof CraftingRecipe craftingRecipe) {
			autoCraftingInventory.setInputInventory(this, 0, 9);
			return craftingRecipe.craft(autoCraftingInventory, null);
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeInt(this.pedestalVariant.getRecipeTier().ordinal());
		buf.writeInt(this.getHighestAvailableRecipeTier().ordinal());
		buf.writeBlockPos(this.pos);
	}
	
	public PedestalRecipeTier getHighestAvailableRecipeTier() {
		if (this.getWorld().getTime() <= this.cachedMaxPedestalTierTick + 20) {
			return cachedMaxPedestalTier;
		} else {
			PedestalRecipeTier pedestalTier = getPedestalTier();
			PedestalRecipeTier structureTier = getStructureTier();
			
			PedestalRecipeTier denominator = PedestalRecipeTier.values()[Math.min(pedestalTier.ordinal(), structureTier.ordinal())];
			this.cachedMaxPedestalTier = denominator;
			this.cachedMaxPedestalTierTick = world.getTime();
			
			return denominator;
		}
	}
	
	private PedestalRecipeTier getPedestalTier() {
		return this.pedestalVariant.getRecipeTier();
	}
	
	
	@NotNull
	private PedestalRecipeTier getStructureTier() {
		Multiblock multiblock;
		
		multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_COMPLEX);
		if (multiblock.validate(world, pos.down(), BlockRotation.NONE)) {
			SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
			return PedestalRecipeTier.COMPLEX;
		}
		
		multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_COMPLEX_WITHOUT_MOONSTONE);
		if (multiblock.validate(world, pos.down(), BlockRotation.NONE)) {
			SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
			return PedestalRecipeTier.ADVANCED;
		}
		
		multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_ADVANCED);
		if (multiblock.validate(world, pos.down(), BlockRotation.NONE)) {
			SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
			return PedestalRecipeTier.ADVANCED;
		}
		
		multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_SIMPLE);
		if (multiblock.validate(world, pos.down(), BlockRotation.NONE)) {
			SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
			return PedestalRecipeTier.SIMPLE;
		}
		
		return PedestalRecipeTier.BASIC;
	}
	
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	/**
	 * Search for upgrades at valid positions and apply
	 */
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods4(world, pos, 3, 2, this.ownerUUID);
		this.markDirty();
	}
	
	@Override
	public UpgradeHolder getUpgradeHolder() {
		return this.upgrades;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		markDirty();
	}
	
	public void setInventoryChanged() {
		this.inventoryChanged = true;
	}
	
}
