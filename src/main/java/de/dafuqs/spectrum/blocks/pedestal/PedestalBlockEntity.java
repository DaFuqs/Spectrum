package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
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
import vazkii.patchouli.api.*;

import java.util.*;

public class PedestalBlockEntity extends LockableContainerBlockEntity implements MultiblockCrafter, RecipeInputProvider, SidedInventory, ExtendedScreenHandlerFactory {

	public static final int INVENTORY_SIZE = 16; // 9 crafting, 5 gems, 1 craftingTablet, 1 output
	public static final int CRAFTING_TABLET_SLOT_ID = 14;
	public static final int OUTPUT_SLOT_ID = 15;
	protected AutoCraftingInventory autoCraftingInventory;
	protected final PropertyDelegate propertyDelegate;
	protected UUID ownerUUID;
	protected PedestalVariant pedestalVariant;
	protected DefaultedList<ItemStack> inventory;
	protected boolean shouldCraft;
	protected float storedXP;
	protected int craftingTime;
	protected int craftingTimeTotal;
	protected @Nullable Recipe currentRecipe;
	protected PedestalRecipeTier cachedMaxPedestalTier;
	protected long cachedMaxPedestalTierTick;
	protected UpgradeHolder upgrades;
	protected boolean inventoryChanged;
	
	public PedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PEDESTAL, blockPos, blockState);
		
		if (blockState.getBlock() instanceof PedestalBlock) {
			this.pedestalVariant = ((PedestalBlock) (blockState.getBlock())).getVariant();
		} else {
			this.pedestalVariant = BuiltinPedestalVariant.BASIC_AMETHYST;
		}
		autoCraftingInventory = new AutoCraftingInventory(3, 3);
		
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return switch (index) {
					case 0 -> PedestalBlockEntity.this.craftingTime;
					default -> PedestalBlockEntity.this.craftingTimeTotal;
				};
			}
			
			public void set(int index, int value) {
				switch (index) {
					case 0 -> PedestalBlockEntity.this.craftingTime = value;
					case 1 -> PedestalBlockEntity.this.craftingTimeTotal = value;
				}
			}
			
			public int size() {
				return 2;
			}
		};
	}
	
	public static void updateInClientWorld(PedestalBlockEntity pedestalBlockEntity) {
		((ServerWorld) pedestalBlockEntity.world).getChunkManager().markForUpdate(pedestalBlockEntity.pos);
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {
		Recipe<?> currentRecipe = pedestalBlockEntity.getCurrentRecipe();
		if (currentRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
			HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = pedestalCraftingRecipe.getGemstonePowderInputs();
			
			for (Map.Entry<BuiltinGemstoneColor, Integer> entry : gemstonePowderInputs.entrySet()) {
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
			if (currentRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
				HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = pedestalCraftingRecipe.getGemstonePowderInputs();
				
				for (Map.Entry<BuiltinGemstoneColor, Integer> entry : gemstonePowderInputs.entrySet()) {
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
			if (calculatedRecipe instanceof PedestalCraftingRecipe calculatedPedestalCraftingRecipe) {
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(calculatedPedestalCraftingRecipe.getCraftingTime() / pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED));
			} else {
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(SpectrumCommon.CONFIG.VanillaRecipeCraftingTimeTicks / pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED));
			}
			pedestalBlockEntity.markDirty();
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos());
			updateInClientWorld(pedestalBlockEntity);
		}
		
		// only craft when there is redstone power
		if (pedestalBlockEntity.craftingTime == 0 && !pedestalBlockEntity.shouldCraft && !(blockState.getBlock() instanceof PedestalBlock && blockState.get(PedestalBlock.POWERED))) {
			return;
		}
		
		int maxCountPerStack = pedestalBlockEntity.getMaxCountPerStack();
		// Pedestal crafting
		boolean craftingFinished = false;
		if (calculatedRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe && pedestalBlockEntity.canAcceptRecipeOutput(calculatedRecipe, pedestalBlockEntity, maxCountPerStack)) {
			pedestalBlockEntity.craftingTime++;
			if (pedestalBlockEntity.craftingTime == pedestalBlockEntity.craftingTimeTotal) {
				pedestalBlockEntity.craftingTime = 0;
				craftingFinished = pedestalBlockEntity.craftPedestalRecipe(pedestalBlockEntity, pedestalCraftingRecipe, pedestalBlockEntity, maxCountPerStack);
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
					pedestalBlockEntity.inventoryChanged = true;
				}
				shouldMarkDirty = true;
			}
		}
		
		if (pedestalBlockEntity.craftingTime == 1 && pedestalBlockEntity.craftingTimeTotal > 1) {
			SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.PEDESTAL_CRAFTING, (ServerWorld) pedestalBlockEntity.world, pedestalBlockEntity.getPos(), pedestalBlockEntity.craftingTimeTotal - pedestalBlockEntity.craftingTime);
		}
		
		// try to output the currently stored output stack
		ItemStack outputItemStack = pedestalBlockEntity.inventory.get(OUTPUT_SLOT_ID);
		if (outputItemStack != ItemStack.EMPTY) {
			if (world.getBlockState(blockPos.up()).getCollisionShape(world, blockPos.up()).isEmpty()) {
				spawnOutputAsItemEntity(world, blockPos, pedestalBlockEntity, outputItemStack);
				playCraftingFinishedSoundEvent(pedestalBlockEntity, calculatedRecipe);
			} else {
				BlockEntity aboveBlockEntity = world.getBlockEntity(blockPos.up());
				if (aboveBlockEntity instanceof Inventory aboveInventory) {
					boolean putIntoAboveInventorySuccess = tryPutOutputIntoAboveInventory(pedestalBlockEntity, aboveInventory, outputItemStack);
					if (putIntoAboveInventorySuccess) {
						playCraftingFinishedSoundEvent(pedestalBlockEntity, calculatedRecipe);
					} else {
						// play sound when the entity can not put its output anywhere
						if (craftingFinished) {
							pedestalBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH);
						}
					}
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
		ItemEntity itemEntity = new ItemEntity(world, pedestalBlockEntity.pos.getX() + 0.5, pedestalBlockEntity.pos.getY() + 1, pedestalBlockEntity.pos.getZ() + 0.5, outputItemStack);
		itemEntity.addVelocity(0, 0.1, 0);
		world.spawnEntity(itemEntity);
		pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);
		
		// spawn XP
		if (pedestalBlockEntity.storedXP > 0) {
			int spawnedXPAmount = Support.getIntFromDecimalWithChance(pedestalBlockEntity.storedXP, pedestalBlockEntity.getWorld().random);
			MultiblockCrafter.spawnExperience(world, pedestalBlockEntity.pos, spawnedXPAmount);
			pedestalBlockEntity.storedXP = 0;
		}
		
		// only triggered on server side. Therefore, has to be sent to client via S2C packet
		SpectrumS2CPacketSender.sendPlayPedestalCraftingFinishedParticle(world, blockPos, outputItemStack);
	}
	
	public static boolean tryPutOutputIntoAboveInventory(PedestalBlockEntity pedestalBlockEntity, Inventory targetInventory, ItemStack outputItemStack) {
		if (targetInventory instanceof HopperBlockEntity) {
			return false;
		}
		
		ItemStack remainingStack = InventoryHelper.smartAddToInventory(outputItemStack, targetInventory, Direction.DOWN);
		if (remainingStack.isEmpty()) {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);
			return true;
		} else {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, remainingStack);
			return false;
		}
	}
	
	public static void playCraftingFinishedSoundEvent(PedestalBlockEntity pedestalBlockEntity, Recipe craftingRecipe) {
		if (craftingRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
			pedestalBlockEntity.playSound(pedestalCraftingRecipe.getSoundEvent(pedestalBlockEntity.world.random));
		} else {
			pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_GENERIC);
		}
	}
	
	public static @Nullable Recipe calculateRecipe(World world, @NotNull PedestalBlockEntity pedestalBlockEntity) {
		if (!pedestalBlockEntity.inventoryChanged) {
			return pedestalBlockEntity.currentRecipe;
		}
		
		// unchanged pedestal recipe?
		if (pedestalBlockEntity.currentRecipe instanceof PedestalCraftingRecipe && ((PedestalCraftingRecipe) pedestalBlockEntity.currentRecipe).matches(pedestalBlockEntity, world)) {
			return pedestalBlockEntity.currentRecipe;
		}
		
		// unchanged vanilla recipe?
		pedestalBlockEntity.autoCraftingInventory.setInputInventory(pedestalBlockEntity.inventory.subList(0, 9));
		if (pedestalBlockEntity.currentRecipe instanceof CraftingRecipe && ((CraftingRecipe) pedestalBlockEntity.currentRecipe).matches(pedestalBlockEntity.autoCraftingInventory, world)) {
			return pedestalBlockEntity.currentRecipe;
		}
		
		// current recipe does not match last recipe
		// => search valid recipe
		PedestalCraftingRecipe pedestalCraftingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.PEDESTAL, pedestalBlockEntity, world).orElse(null);
		if (pedestalCraftingRecipe == null) {
			return world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, pedestalBlockEntity.autoCraftingInventory, world).orElse(null);
		}
		
		if (!pedestalCraftingRecipe.canCraft(pedestalBlockEntity)) {
			return null;
		}
		return pedestalCraftingRecipe;
	}
	
	private boolean canAcceptRecipeOutput(@Nullable Recipe recipe, Inventory inventory, int maxCountPerStack) {
		if (recipe != null) {
			ItemStack output;
			if (recipe instanceof PedestalCraftingRecipe) {
				output = recipe.craft(inventory, null);
			} else {
				autoCraftingInventory.setInputInventory(inventory, 0, 9);
				output = recipe.craft(autoCraftingInventory, null);
			}
			if (output.isEmpty()) {
				return false;
			} else {
				ItemStack existingOutput = this.getStack(OUTPUT_SLOT_ID);
				if (existingOutput.isEmpty()) {
					return true;
				} else if (ItemStack.areItemsEqual(existingOutput, output)) {
					return false;
				} else if (existingOutput.getCount() < maxCountPerStack && existingOutput.getCount() < existingOutput.getMaxCount()) {
					return true;
				} else {
					return existingOutput.getCount() < output.getMaxCount();
				}
			}
		} else {
			return false;
		}
	}
	
	private boolean craftPedestalRecipe(PedestalBlockEntity pedestalBlockEntity, @Nullable PedestalCraftingRecipe recipe, Inventory inventory, int maxCountPerStack) {
		if (canAcceptRecipeOutput(recipe, inventory, maxCountPerStack)) {
			ItemStack recipeOutput = recipe.craftAndDecrement(pedestalBlockEntity);
			ItemStack recipeOutputCopy = recipeOutput.copy();
			
			// if it was a recipe to upgrade the pedestal itself
			// => upgrade
			PedestalVariant newPedestalVariant = PedestalCraftingRecipe.getUpgradedPedestalVariantForOutput(recipeOutput);
			if (newPedestalVariant != null && newPedestalVariant.isBetterThan(getVariant(pedestalBlockEntity))) {
				// It is an upgrade recipe (output is a pedestal block item)
				// => Upgrade
				pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_UPGRADE);
				PedestalBlock.upgradeToVariant(pedestalBlockEntity.world, pedestalBlockEntity.getPos(), newPedestalVariant);
				SpectrumS2CPacketSender.spawnPedestalUpgradeParticles(pedestalBlockEntity.world, pedestalBlockEntity.pos, newPedestalVariant);
				
				pedestalBlockEntity.pedestalVariant = newPedestalVariant;
				pedestalBlockEntity.currentRecipe = null; // reset the recipe, otherwise pedestal would remember crafting the update
			} else {
				int resultAmountBeforeMod = recipeOutput.getCount();
				double yieldModifier = recipe.areYieldUpgradesDisabled() ? 1.0 : pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.YIELD);
				int resultAmountAfterMod = Support.getIntFromDecimalWithChance(resultAmountBeforeMod * yieldModifier, pedestalBlockEntity.world.random);
				
				// Not an upgrade recipe => Add output to output slot
				ItemStack existingOutput = inventory.getStack(OUTPUT_SLOT_ID);
				if (existingOutput.isEmpty()) {
					recipeOutput.setCount(Math.min(existingOutput.getMaxCount(), resultAmountAfterMod));
					inventory.setStack(OUTPUT_SLOT_ID, recipeOutput);
				} else {
					// protection against stacks > max stack size
					int finalAmount = Math.min(existingOutput.getMaxCount(), existingOutput.getCount() + resultAmountAfterMod);
					existingOutput.setCount(finalAmount);
					inventory.setStack(OUTPUT_SLOT_ID, existingOutput);
				}
			}
			
			// Add recipe XP
			double experienceModifier = pedestalBlockEntity.upgrades.getEffectiveValue(UpgradeType.EXPERIENCE);
			float recipeExperienceBeforeMod = recipe.getExperience();
			float experienceAfterMod = (float) (recipeExperienceBeforeMod * experienceModifier);
			pedestalBlockEntity.storedXP += experienceAfterMod;
			
			// if the recipe unlocks an advancement unlock it
			pedestalBlockEntity.grantPlayerPedestalCraftingAdvancement(recipeOutputCopy, (int) experienceAfterMod);
			
			pedestalBlockEntity.markDirty();
			pedestalBlockEntity.inventoryChanged = true;
			updateInClientWorld(pedestalBlockEntity);
			
			return true;
		} else {
			return false;
		}
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
	
	public static int getSlotForGemstonePowder(BuiltinGemstoneColor gemstoneColor) {
		return switch (gemstoneColor) {
			case CYAN -> 9;
			case MAGENTA -> 10;
			case YELLOW -> 11;
			case BLACK -> 12;
			default -> 13; // WHITE
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
		return new PedestalScreenHandler(syncId, playerInventory, this, this.propertyDelegate, this.pedestalVariant.getRecipeTier().ordinal(), this.getHighestAvailableRecipeTierWithStructure().ordinal(), this.pos);
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
		if (this.world.getBlockEntity(this.pos) != this) {
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
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
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
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		}
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		} else {
			this.upgrades = new UpgradeHolder();
		}
		if (nbt.contains("inventory_changed")) {
			this.inventoryChanged = nbt.getBoolean("inventory_changed");
		}
		
		this.currentRecipe = null;
		if (nbt.contains("CurrentRecipe")) {
			Identifier recipeIdentifier = new Identifier(nbt.getString("CurrentRecipe"));
			this.currentRecipe = MultiblockCrafter.getRecipeFromId(this.world, recipeIdentifier);
		}
	}
	
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
		
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
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
				recipeOutput.onCraft(this.world, player, recipeOutput.getCount());
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
				Item recipeReminderItem = itemStack.getItem().getRecipeRemainder();
				if (recipeReminderItem == null) {
					itemStack.decrement(1);
				} else {
					if (this.inventory.get(i).getCount() == 1) {
						this.inventory.set(i, new ItemStack(recipeReminderItem, 1));
					} else {
						this.inventory.get(i).decrement(1);
						
						ItemStack remainderStack = recipeReminderItem.getDefaultStack();
						ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, remainderStack);
						itemEntity.addVelocity(0, 0.05, 0);
						world.spawnEntity(itemEntity);
					}
				}
			}
		}
	}
	
	private void grantPlayerPedestalCraftingAdvancement(ItemStack output, int experience) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) getOwnerIfOnline();
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.PEDESTAL_CRAFTING.trigger(serverPlayerEntity, output, experience);
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
			return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
		} else {
			switch (this.pedestalVariant.getRecipeTier()) {
				case COMPLEX -> {
					return new int[]{9, 10, 11, 12, 13};
				}
				case ADVANCED -> {
					return new int[]{9, 10, 11, 12};
				}
				default -> {
					return new int[]{9, 10, 11};
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

			Recipe<?> storedRecipe = CraftingTabletItem.getStoredRecipe(this.world, craftingTabletItem);
			
			int width = 3;
			if (storedRecipe instanceof ShapedRecipe shapedRecipe) {
				width = shapedRecipe.getWidth();
				if (slot % 3 >= width) {
					return false;
				}
			} else if (storedRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
				width = pedestalCraftingRecipe.getWidth();
				if (slot % 3 >= width) {
					return false;
				}
			} else if (storedRecipe instanceof ShapelessRecipe) {
				// just put it in already
			} else {
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
	
	public Recipe getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	public ItemStack getCurrentCraftingRecipeOutput() {
		if (this.currentRecipe == null) {
			return ItemStack.EMPTY;
		} else {
			if(currentRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
				return pedestalCraftingRecipe.craft(this);
			} else {
				autoCraftingInventory.setInputInventory(this, 0, 9);
				return this.currentRecipe.craft(autoCraftingInventory, null);
			}
		}
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeInt(this.pedestalVariant.getRecipeTier().ordinal());
		buf.writeInt(this.getHighestAvailableRecipeTierWithStructure().ordinal());
		buf.writeBlockPos(this.pos);
	}
	
	private PedestalRecipeTier getHighestAvailableRecipeTierForVariant() {
		return this.pedestalVariant.getRecipeTier();
	}
	
	public PedestalRecipeTier getHighestAvailableRecipeTierWithStructure() {
		if (this.world.getTime() == this.cachedMaxPedestalTierTick) {
			return cachedMaxPedestalTier;
		} else {
			PedestalRecipeTier highestAvailableRecipeTierForVariant = getHighestAvailableRecipeTierForVariant();
			
			boolean found = false;
			PedestalRecipeTier highestAvailableRecipeTier = PedestalRecipeTier.BASIC;
			if (highestAvailableRecipeTierForVariant.ordinal() >= PedestalRecipeTier.COMPLEX.ordinal()) {
				IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_CHECK);
				boolean valid = multiblock.validate(world, pos.down(), BlockRotation.NONE);
				if (valid) {
					highestAvailableRecipeTier = PedestalRecipeTier.COMPLEX;
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
					found = true;
				}
			}
			if (!found && highestAvailableRecipeTierForVariant.ordinal() >= PedestalRecipeTier.ADVANCED.ordinal()) {
				IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK);
				boolean valid = multiblock.validate(world, pos.down(), BlockRotation.NONE);
				if (valid) {
					highestAvailableRecipeTier = PedestalRecipeTier.ADVANCED;
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
					found = true;
				}
			}
			if (!found && highestAvailableRecipeTierForVariant.ordinal() >= PedestalRecipeTier.SIMPLE.ordinal()) {
				IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_CHECK);
				boolean valid = multiblock.validate(world, pos.down(), BlockRotation.NONE);
				if (valid) {
					highestAvailableRecipeTier = PedestalRecipeTier.SIMPLE;
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getOwnerIfOnline(), multiblock);
				}
			}

			this.cachedMaxPedestalTier = highestAvailableRecipeTier;
			this.cachedMaxPedestalTierTick = world.getTime();
			return highestAvailableRecipeTier;
		}
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
	}
	
	public void setInventoryChanged() {
		this.inventoryChanged = true;
	}
	
}
