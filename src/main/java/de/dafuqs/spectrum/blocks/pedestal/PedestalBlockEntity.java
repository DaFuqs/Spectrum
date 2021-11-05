package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.AutoCraftingInventory;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import de.dafuqs.spectrum.items.CraftingTabletItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;

import java.util.*;

public class PedestalBlockEntity extends LockableContainerBlockEntity implements RecipeInputProvider, SidedInventory, PlayerOwned, ExtendedScreenHandlerFactory, BlockEntityClientSerializable {

	private UUID ownerUUID;
	private String ownerName;
	private PedestalBlock.PedestalVariant pedestalVariant;

	protected DefaultedList<ItemStack> inventory;

	private boolean wasPoweredBefore;
	private float storedXP;
	private int craftingTime;
	private int craftingTimeTotal;

	protected final PropertyDelegate propertyDelegate;
	private static final RecipeType<? extends PedestalCraftingRecipe> recipeType = SpectrumRecipeTypes.PEDESTAL;
	private Recipe lastRecipe;

	private static AutoCraftingInventory autoCraftingInventory;
	private PedestalRecipeTier cachedMaxPedestalTier;
	private long cachedMaxPedestalTierTick;

	private final boolean checkedForUpgrades = false;
	private int speedUpgrades = 0;

	public static final int INVENTORY_SIZE = 16; // 9 crafting, 5 gems, 1 craftingTablet, 1 output
	public static final int CRAFTING_TABLET_SLOT_ID = 14;
	public static final int OUTPUT_SLOT_ID = 15;

	public PedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.PEDESTAL, blockPos, blockState);

		if(blockState.getBlock() instanceof PedestalBlock) {
			this.pedestalVariant = ((PedestalBlock)(blockState.getBlock())).getVariant();
		} else {
			this.pedestalVariant = PedestalBlock.PedestalVariant.BASIC_AMETHYST;
		}

		if(autoCraftingInventory == null) {
			autoCraftingInventory = new AutoCraftingInventory(3, 3);
		}

		this.inventory  = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

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
	
	public void setVariant(PedestalBlock.@NotNull PedestalVariant pedestalVariant) {
		this.pedestalVariant = pedestalVariant;
		this.propertyDelegate.set(2, pedestalVariant.ordinal());
	}

	@Override
	public Text getContainerName() {
		return new TranslatableText("block.spectrum.pedestal");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new PedestalScreenHandler(syncId, playerInventory, this, this.propertyDelegate, this.pedestalVariant.ordinal(), this.getHighestAvailableRecipeTierWithStructure().ordinal(), this.pos);
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
		} while(itemStack.isEmpty());

		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setStack(int slot, @NotNull ItemStack stack) {
		ItemStack itemStack = this.inventory.get(slot);
		boolean isSimilarItem = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}

		if (slot < CRAFTING_TABLET_SLOT_ID && !isSimilarItem) {
			this.craftingTimeTotal = getCraftingTime(this.world, recipeType, this);
			this.craftingTime = 0;
			this.markDirty();
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		for (ItemStack itemStack : this.inventory) {
			recipeMatcher.addInput(itemStack);
		}
	}
	
	public static void updateInClientWorld(PedestalBlockEntity pedestalBlockEntity) {
		pedestalBlockEntity.sync();
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = this.writeNbt(new NbtCompound());
		if(this.lastRecipe != null) {
			nbtCompound.putString("LastRecipe", this.lastRecipe.getId().toString());
		} else {
			nbtCompound.putString("LastRecipe", "");
		}
		return this.writeNbt(new NbtCompound());
	}

	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
		if(nbt.contains("StoredXP")) {
			this.storedXP = nbt.getFloat("StoredXP");
		}
		if(nbt.contains("CraftingTime")) {
			this.craftingTime = nbt.getShort("CraftingTime");
		}
		if(nbt.contains("CraftingTimeTotal")) {
			this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		}
		if(nbt.contains("SpeedUpgrades")) {
			this.speedUpgrades = nbt.getShort("SpeedUpgrades");
		}
		if(nbt.contains("LastRecipe")) {
			String recipeString = nbt.getString("LastRecipe");
			if(!recipeString.isEmpty()) {
				Optional<? extends Recipe> optionalRecipe;
				if (SpectrumClient.minecraftClient != null) {
					optionalRecipe = MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(recipeString));
				} else {
					optionalRecipe = world.getRecipeManager().get(new Identifier(recipeString));
				}
				if(optionalRecipe.isPresent()) {
					this.lastRecipe = optionalRecipe.get();
				} else {
					this.lastRecipe = null;
				}
			} else {
				this.lastRecipe = null;
			}
		} else {
			this.lastRecipe = null;
		}
		if(nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if(nbt.contains("OwnerName")) {
			this.ownerName = nbt.getString("OwnerName");
		} else {
			this.ownerName = "???";
		}
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putFloat("StoredXP", this.storedXP);
		nbt.putShort("CraftingTime", (short)this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);
		nbt.putShort("SpeedUpgrades", (short)this.speedUpgrades);
		if(this.lastRecipe != null) {
			nbt.putString("LastRecipe", this.lastRecipe.getId().toString());
		}

		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.ownerName != null) {
			nbt.putString("OwnerName", this.ownerName);
		}
		Inventories.writeNbt(nbt, this.inventory);
		return nbt;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	public boolean isCrafting() {
		return this.craftingTime > 0;
	}

	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {
		Recipe currentRecipe = pedestalBlockEntity.getCurrentRecipe();
		if (currentRecipe instanceof PedestalCraftingRecipe pedestalCraftingRecipe) {
			HashMap<GemstoneColor, Integer> gemstonePowderInputs = pedestalCraftingRecipe.getGemstonePowderInputs();
			
			for (Map.Entry<GemstoneColor, Integer> entry : gemstonePowderInputs.entrySet()) {
				int amount = entry.getValue();
				if (amount > 0) {
					ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(entry.getKey());
					
					float particleAmount = Support.getWholeIntFromFloatWithChance(amount / 8.0F, world.random);
					for (int i = 0; i < particleAmount; i++) {
						float randomX = 2.0F - world.getRandom().nextFloat() * 4;
						float randomZ = 2.0F - world.getRandom().nextFloat() * 4;
						world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
					}
				}
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {
		// only craft when there is redstone power
		Block block = world.getBlockState(blockPos).getBlock();
		if(block instanceof PedestalBlock && blockState.get(PedestalBlock.POWERED)) {
			if(!pedestalBlockEntity.checkedForUpgrades) {
				pedestalBlockEntity.updateUpgrades();
			}

			// check recipe crafted last tick => performance
			PedestalCraftingRecipe pedestalCraftingRecipe = null;
			CraftingRecipe craftingRecipe = null;
			boolean shouldMarkDirty = false;

			Recipe validRecipe = calculateRecipe(world, pedestalBlockEntity);
			if(validRecipe instanceof PedestalCraftingRecipe) {
				pedestalCraftingRecipe = (PedestalCraftingRecipe) validRecipe;
				pedestalBlockEntity.lastRecipe = validRecipe;
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(pedestalCraftingRecipe.getCraftingTime() / pedestalBlockEntity.getCraftingSpeedMultiplierThroughUpgrades());
			} else if(validRecipe instanceof CraftingRecipe) {
				craftingRecipe = (CraftingRecipe) validRecipe;
				pedestalBlockEntity.lastRecipe = validRecipe;
				pedestalBlockEntity.craftingTimeTotal = (int) Math.ceil(20 / pedestalBlockEntity.getCraftingSpeedMultiplierThroughUpgrades());
			} else {
				// no valid recipe
				pedestalBlockEntity.craftingTime = 0;
			}

			int maxCountPerStack = pedestalBlockEntity.getMaxCountPerStack();
			boolean crafting = pedestalBlockEntity.isCrafting();

			// Pedestal crafting
			boolean craftingFinished = false;
			if (canAcceptRecipeOutput(pedestalCraftingRecipe, pedestalBlockEntity.inventory, maxCountPerStack)) {
				pedestalBlockEntity.craftingTime++;
				if (pedestalBlockEntity.craftingTime == pedestalBlockEntity.craftingTimeTotal) {
					pedestalBlockEntity.craftingTime = 0;
					craftingFinished = craftPedestalRecipe(pedestalBlockEntity, pedestalCraftingRecipe, pedestalBlockEntity.inventory, maxCountPerStack);
					shouldMarkDirty = true;
				}
			// Vanilla crafting
			} else if(canAcceptRecipeOutput(craftingRecipe, pedestalBlockEntity.inventory, maxCountPerStack)) {
				pedestalBlockEntity.craftingTime++;
				if (pedestalBlockEntity.craftingTime == pedestalBlockEntity.craftingTimeTotal) {
					pedestalBlockEntity.craftingTime = 0;
					craftingFinished = pedestalBlockEntity.craftVanillaRecipe(craftingRecipe, pedestalBlockEntity.inventory, maxCountPerStack);
					shouldMarkDirty = true;
				}
			// No crafting
			} else {
				pedestalBlockEntity.craftingTime = 0;
			}

			boolean wasCraftingBefore = pedestalBlockEntity.isCrafting();
			if (crafting != wasCraftingBefore) {
				shouldMarkDirty = true;
			}
			if((!pedestalBlockEntity.wasPoweredBefore && pedestalBlockEntity.craftingTime > 0) || (pedestalBlockEntity.craftingTime == 1 && pedestalBlockEntity.craftingTimeTotal > 1)) {
				SpectrumS2CPackets.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.PEDESTAL_CRAFTING, (ServerWorld) pedestalBlockEntity.world, pedestalBlockEntity.getPos(), pedestalBlockEntity.craftingTimeTotal - pedestalBlockEntity.craftingTime);
			}

			// try to output the currently stored output stack
			ItemStack outputItemStack = pedestalBlockEntity.inventory.get(OUTPUT_SLOT_ID);
			if (outputItemStack != ItemStack.EMPTY) {
				if (world.getBlockState(blockPos.up()).isAir()) {
					spawnOutputAsItemEntity(world, blockPos, pedestalBlockEntity, outputItemStack);
					playCraftingFinishedSoundEvent(pedestalBlockEntity, craftingRecipe, pedestalCraftingRecipe);
				} else {
					BlockEntity aboveBlockEntity = world.getBlockEntity(blockPos.up());
					if (aboveBlockEntity instanceof Inventory) {
						boolean putIntoAboveInventorySuccess = tryPutOutputIntoAboveInventory(world, blockPos, pedestalBlockEntity, (Inventory) aboveBlockEntity, outputItemStack, craftingRecipe, pedestalCraftingRecipe);
						if(putIntoAboveInventorySuccess) {
							playCraftingFinishedSoundEvent(pedestalBlockEntity, craftingRecipe, pedestalCraftingRecipe);
						} else {
							// play sound when the entity can not put it's output anywhere
							if (craftingFinished) {
								pedestalBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH);
							}
						}
					} else {
						// play sound when the entity can not put it's output anywhere
						if (craftingFinished) {
							pedestalBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH);
						}
					}
				}
			}

			if (shouldMarkDirty) {
				markDirty(world, blockPos, blockState);
			}
			pedestalBlockEntity.wasPoweredBefore = true;
		} else {
			pedestalBlockEntity.wasPoweredBefore = false;
		}
	}

	@Contract(pure = true)
	public static PedestalBlock.PedestalVariant getVariant(@NotNull PedestalBlockEntity pedestalBlockEntity) {
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
			int spawnedXPAmount = Support.getWholeIntFromFloatWithChance(pedestalBlockEntity.storedXP, pedestalBlockEntity.getWorld().random);
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, pedestalBlockEntity.pos.getX() + 0.5, pedestalBlockEntity.pos.getY() + 1, pedestalBlockEntity.pos.getZ() + 0.5, spawnedXPAmount);
			world.spawnEntity(experienceOrbEntity);
			pedestalBlockEntity.storedXP = 0;
		}

		// only triggered on server side. Therefore, has to be sent to client via S2C packet
		SpectrumS2CPackets.sendPlayPedestalCraftingFinishedParticle(world, blockPos, outputItemStack);
	}

	public static boolean tryPutOutputIntoAboveInventory(World world, BlockPos blockPos, PedestalBlockEntity pedestalBlockEntity, Inventory targetInventory, ItemStack outputItemStack, CraftingRecipe craftingRecipe, PedestalCraftingRecipe pedestalCraftingRecipe) {
		ItemStack remainingStack = InventoryHelper.addToInventory(outputItemStack, targetInventory, Direction.DOWN);
		if(remainingStack.isEmpty()) {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);
			return true;
		} else {
			pedestalBlockEntity.inventory.set(OUTPUT_SLOT_ID, remainingStack);
			return false;
		}
	}

	public static void playCraftingFinishedSoundEvent(PedestalBlockEntity pedestalBlockEntity, @Nullable CraftingRecipe craftingRecipe, @Nullable PedestalCraftingRecipe pedestalCraftingRecipe) {
		if (craftingRecipe != null) {
			pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_GENERIC);
		} else if (pedestalCraftingRecipe != null) {
			pedestalBlockEntity.playSound(pedestalCraftingRecipe.getSoundEvent(pedestalBlockEntity.world.random));
		}
	}

	public static @Nullable Recipe calculateRecipe(World world, @NotNull PedestalBlockEntity pedestalBlockEntity) {
		if (pedestalBlockEntity.lastRecipe instanceof PedestalCraftingRecipe && ((PedestalCraftingRecipe) pedestalBlockEntity.lastRecipe).matches(pedestalBlockEntity, world)) {
			return pedestalBlockEntity.lastRecipe;
		} else {
			autoCraftingInventory.setInputInventory(pedestalBlockEntity.inventory.subList(0, 9));
			if(pedestalBlockEntity.lastRecipe instanceof CraftingRecipe && ((CraftingRecipe) pedestalBlockEntity.lastRecipe).matches(autoCraftingInventory, world)) {
				return pedestalBlockEntity.lastRecipe;
			} else {
				// current inventory does not match last recipe
				// => search valid recipe
				pedestalBlockEntity.craftingTime = 0;

				PedestalCraftingRecipe pedestalCraftingRecipe = world.getRecipeManager().getFirstMatch(recipeType, pedestalBlockEntity, world).orElse(null);
				if (pedestalCraftingRecipe != null) {
					
					// check if the recipe is an upgrade. If it is don't allow to craft it if the current tier is already sufficient
					PedestalBlock.PedestalVariant newPedestalVariant = PedestalCraftingRecipe.getUpgradedPedestalVariantForOutput(pedestalCraftingRecipe.getOutput());
					if(newPedestalVariant != null && newPedestalVariant.ordinal() <= PedestalBlockEntity.getVariant(pedestalBlockEntity).ordinal()) {
						if(pedestalBlockEntity.lastRecipe != null) {
							updateInClientWorld(pedestalBlockEntity);
						}
						return null;
					}
					
					if (pedestalCraftingRecipe.canCraft(pedestalBlockEntity)) {
						pedestalBlockEntity.lastRecipe = pedestalCraftingRecipe;
						pedestalBlockEntity.craftingTimeTotal = pedestalCraftingRecipe.getCraftingTime();
						updateInClientWorld(pedestalBlockEntity);
						return pedestalCraftingRecipe;
					} else {
						SpectrumS2CPackets.sendCancelBlockBoundSoundInstance((ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos());
						if(pedestalBlockEntity.lastRecipe != null) {
							updateInClientWorld(pedestalBlockEntity);
						}
						return null;
					}
				} else {
					CraftingRecipe craftingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, autoCraftingInventory, world).orElse(null);
					if (craftingRecipe != null) {
						pedestalBlockEntity.lastRecipe = craftingRecipe;
						pedestalBlockEntity.craftingTimeTotal = 20;
						updateInClientWorld(pedestalBlockEntity);
						return craftingRecipe;
					} else {
						SpectrumS2CPackets.sendCancelBlockBoundSoundInstance((ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos());
						if(pedestalBlockEntity.lastRecipe != null) {
							updateInClientWorld(pedestalBlockEntity);
						}
						pedestalBlockEntity.lastRecipe = null;
						return null;
					}
				}
			}
		}
	}

	private void playSound(SoundEvent soundEvent) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.9F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
	}

	private static int getCraftingTime(World world, RecipeType<? extends PedestalCraftingRecipe> recipeType, Inventory inventory) {
		return world.getRecipeManager().getFirstMatch(recipeType, inventory, world).map(PedestalCraftingRecipe::getCraftingTime).orElse(20);
	}

	private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
		if (recipe != null) {
			ItemStack output = recipe.getOutput();
			if (output.isEmpty()) {
				return false;
			} else {
				ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
				if (existingOutput.isEmpty()) {
					return true;
				} else if (!existingOutput.isItemEqualIgnoreDamage(output)) {
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

	private static boolean craftPedestalRecipe(PedestalBlockEntity pedestalBlockEntity, @Nullable PedestalCraftingRecipe recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
		if (canAcceptRecipeOutput(recipe, defaultedList, maxCountPerStack)) {

			// -1 for all crafting inputs
			for(int i = 0; i < 9; i++) {
				ItemStack itemStack = defaultedList.get(i);
				if(!itemStack.isEmpty()) {
					Item recipeReminderItem = itemStack.getItem().getRecipeRemainder();
					if(recipeReminderItem == null) {
						itemStack.decrement(1);
					} else {
						pedestalBlockEntity.inventory.set(i, new ItemStack(recipeReminderItem, 1));
					}
				}
			}

			// -X for all the pigment inputs
			for(GemstoneColor gemstoneColor : GemstoneColor.values()) {
				int gemstonePowderAmount = recipe.getGemstonePowderAmount(gemstoneColor);
				pedestalBlockEntity.inventory.get(getSlotForGemstonePowder(gemstoneColor)).decrement(gemstonePowderAmount);
			}

			ItemStack recipeOutput = recipe.getOutput();

			// if it was a recipe to upgrade the pedestal itself
			// => upgrade
			PedestalBlock.PedestalVariant newPedestalVariant = PedestalCraftingRecipe.getUpgradedPedestalVariantForOutput(recipeOutput);
			if(newPedestalVariant != null && newPedestalVariant.ordinal() > getVariant(pedestalBlockEntity).ordinal()) {
				// It is an upgrade recipe (output is a pedestal block item)
				// => Upgrade
				pedestalBlockEntity.playSound(SpectrumSoundEvents.PEDESTAL_UPGRADE);
				PedestalBlock.upgradeToVariant(pedestalBlockEntity.world, pedestalBlockEntity.getPos(), newPedestalVariant);
				SpectrumS2CPackets.spawnPedestalUpgradeParticles(pedestalBlockEntity.world, pedestalBlockEntity.pos, newPedestalVariant);
				
				pedestalBlockEntity.pedestalVariant = newPedestalVariant;
				
				// reset the recipe
				pedestalBlockEntity.lastRecipe = null;
				updateInClientWorld(pedestalBlockEntity);
				pedestalBlockEntity.markDirty();
			} else {
				// Not an upgrade recipe => Add output to output slot
				ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
				if (existingOutput.isEmpty()) {
					defaultedList.set(OUTPUT_SLOT_ID, recipeOutput.copy());
				} else {
					existingOutput.increment(recipeOutput.getCount());
					defaultedList.set(OUTPUT_SLOT_ID, existingOutput);
				}
			}

			// Add recipe XP
			pedestalBlockEntity.storedXP = recipe.getExperience();

			// if the recipe unlocks an advancement unlock it
			pedestalBlockEntity.grantPlayerCraftingAdvancement(recipe);

			return true;
		} else {
			return false;
		}
	}
	
	private boolean craftVanillaRecipe(@Nullable CraftingRecipe recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
		if (canAcceptRecipeOutput(recipe, defaultedList, maxCountPerStack)) {

			// -1 for all crafting inputs
			for(int i = 0; i < 9; i++) {
				ItemStack itemStack = defaultedList.get(i);
				if(!itemStack.isEmpty()) {
					Item recipeReminderItem = itemStack.getItem().getRecipeRemainder();
					if(recipeReminderItem == null) {
						itemStack.decrement(1);
					} else {
						inventory.set(i, new ItemStack(recipeReminderItem, 1));
					}
				}
			}

			ItemStack recipeOutput = recipe.getOutput();
			ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
			if (existingOutput.isEmpty()) {
				defaultedList.set(OUTPUT_SLOT_ID, recipeOutput.copy());
			} else {
				existingOutput.increment(recipeOutput.getCount());
				defaultedList.set(OUTPUT_SLOT_ID, existingOutput);
			}

			return true;
		} else {
			return false;
		}
	}

	private void grantPlayerCraftingAdvancement(PedestalCraftingRecipe recipe) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, this.ownerUUID);
		if(serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.PEDESTAL_CRAFTING.trigger(serverPlayerEntity, recipe.getOutput());
		}
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if(slot < 9) {
			return true;
		} else if(slot == CRAFTING_TABLET_SLOT_ID && stack.isOf(SpectrumItems.CRAFTING_TABLET)) {
			return true;
		} else {
			return stack.getItem().equals(getGemstonePowderItemForSlot(slot));
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

	public static int getSlotForGemstonePowder(GemstoneColor gemstoneColor) {
		return switch (gemstoneColor) {
			case CYAN -> 9;
			case MAGENTA -> 10;
			case YELLOW -> 11;
			case BLACK -> 12;
			default -> 13; // WHITE
		};
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if(side == Direction.DOWN) {
			return new int[]{OUTPUT_SLOT_ID};
		} else if(side == Direction.UP) {
			return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
		} else {
			switch (this.pedestalVariant) {
				case BASIC_AMETHYST, BASIC_CITRINE, BASIC_TOPAZ, CMY -> {
					return new int[]{9, 10, 11};
				}
				case ONYX -> {
					return new int[]{9, 10, 11, 12};
				}
				default -> {
					return new int[]{9, 10, 11, 12, 13};
				}
			}
		}
	}

	@Override
	public boolean canInsert(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
		if(stack.isOf(getGemstonePowderItemForSlot(slot))) {
			return true;
		}

		if(slot < 9 && inventory.get(CRAFTING_TABLET_SLOT_ID).isOf(SpectrumItems.CRAFTING_TABLET)) {
			ItemStack craftingTabletItem = inventory.get(CRAFTING_TABLET_SLOT_ID);

			if(inventory.get(slot).getCount() > 0) {
				return false;
			}

			Recipe storedRecipe = CraftingTabletItem.getStoredRecipe(this.world, craftingTabletItem);

			int width = 3;
			if(storedRecipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) storedRecipe;
				width = shapedRecipe.getWidth();
				if(slot % 3 >= width) {
					return false;
				}
			} else if(storedRecipe instanceof PedestalCraftingRecipe) {
				PedestalCraftingRecipe pedestalCraftingRecipe = (PedestalCraftingRecipe) storedRecipe;
				width = pedestalCraftingRecipe.getWidth();
				if(slot % 3 >= width) {
					return false;
				}
			} else if(storedRecipe instanceof ShapelessRecipe) {
				// just put it in already
			} else {
				return false;
			}

			int resultRecipeSlot = getCraftingRecipeSlotDependingOnWidth(slot, width, 3);
			if(resultRecipeSlot < storedRecipe.getIngredients().size()) {
				Ingredient ingredient = (Ingredient) storedRecipe.getIngredients().get(resultRecipeSlot);
				return ingredient.test(stack);
			} else {
				return false;
			}
		} else {
			return slot < CRAFTING_TABLET_SLOT_ID;
		}
	}

	private int getCraftingRecipeSlotDependingOnWidth(int slot, int recipeWidth, int gridWidth) {
		int line = slot / gridWidth;
		int posInLine = slot % gridWidth;
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

	@Override
	public String getOwnerName() {
		return this.ownerName;
	}

	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.ownerName = playerEntity.getName().asString();
		setCustomName(new TranslatableText("block.spectrum.pedestal.title_with_owner", ownerName));
	}
	
	public Recipe getCurrentRecipe() {
		return this.lastRecipe;
	}

	public ItemStack getCraftingOutput() {
		Recipe recipe = calculateRecipe(this.world, this);
		if(recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeInt(this.pedestalVariant.ordinal());
		buf.writeInt(this.getHighestAvailableRecipeTierWithStructure().ordinal());
		buf.writeBlockPos(this.pos);
	}

	private PedestalRecipeTier getHighestAvailableRecipeTierForVariant() {
		return getHighestAvailableRecipeTierForVariant(this.pedestalVariant);
	}

	@Contract(pure = true)
	public static PedestalRecipeTier getHighestAvailableRecipeTierForVariant(PedestalBlock.@NotNull PedestalVariant pedestalVariant) {
		switch (pedestalVariant) {
			case CMY -> {
				return PedestalRecipeTier.SIMPLE;
			}
			case ONYX -> {
				return PedestalRecipeTier.ADVANCED;
			}
			case MOONSTONE -> {
				return PedestalRecipeTier.COMPLEX;
			}
			default -> {
				// BASIC_TOPAZ, BASIC_AMETHYST, BASIC_CITRINE, no structure
				return PedestalRecipeTier.BASIC;
			}
		}
	}

	public PedestalRecipeTier getHighestAvailableRecipeTierWithStructure() {
		if(world.getTime() == this.cachedMaxPedestalTierTick) {
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
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getPlayerEntityIfOnline(world), multiblock);
					found = true;
				}
			}
			if (!found && highestAvailableRecipeTierForVariant.ordinal() >= PedestalRecipeTier.ADVANCED.ordinal()) {
				IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK);
				boolean valid = multiblock.validate(world, pos.down(), BlockRotation.NONE);				if (valid) {
					highestAvailableRecipeTier = PedestalRecipeTier.ADVANCED;
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getPlayerEntityIfOnline(world), multiblock);
					found = true;
				}
			}
			if (!found && highestAvailableRecipeTierForVariant.ordinal() >= PedestalRecipeTier.SIMPLE.ordinal()) {
				IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER);
				boolean valid = multiblock.validate(world, pos.down(), BlockRotation.NONE);
				if (valid) {
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger((ServerPlayerEntity) this.getPlayerEntityIfOnline(world), multiblock);
					highestAvailableRecipeTier = PedestalRecipeTier.SIMPLE;
				}
			}

			this.cachedMaxPedestalTier = highestAvailableRecipeTier;
			this.cachedMaxPedestalTierTick = world.getTime();
			return highestAvailableRecipeTier;
		}
	}

	/**
	 * Search for upgrades at valid positions and apply
	 */
	public void updateUpgrades() {
		this.speedUpgrades = 0;
		if(world.getBlockState(pos.add(3, 2, 3)).getBlock().equals(SpectrumBlocks.PEDESTAL_SPEED_UPGRADE)) {
			this.speedUpgrades++;
		}
		if(world.getBlockState(pos.add(3, 2, -3)).getBlock().equals(SpectrumBlocks.PEDESTAL_SPEED_UPGRADE)) {
			this.speedUpgrades++;
		}
		if(world.getBlockState(pos.add(-3, 2, 3)).getBlock().equals(SpectrumBlocks.PEDESTAL_SPEED_UPGRADE)) {
			this.speedUpgrades++;
		}
		if(world.getBlockState(pos.add(-3, 2, -3)).getBlock().equals(SpectrumBlocks.PEDESTAL_SPEED_UPGRADE)) {
			this.speedUpgrades++;
		}

		if(this.speedUpgrades == 4) {
			ServerPlayerEntity owner = (ServerPlayerEntity) getPlayerEntityIfOnline(world);
			if(owner != null) {
				Support.grantAdvancementCriterion(owner, "use_all_pedestal_upgrades", "used_all");
			}
		}
	}

	@Contract(pure = true)
	private double getCraftingSpeedMultiplierThroughUpgrades() {
		return Math.exp(this.speedUpgrades * 0.4);
	}
	
	@Override
	public void fromClientTag(NbtCompound tag) {
		this.readNbt(tag);
	}
	
	@Override
	public NbtCompound toClientTag(NbtCompound tag) {
		return this.toInitialChunkDataNbt();
	}
}
