package de.dafuqs.spectrum.blocks.potion_workshop;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, RecipeInputProvider, SidedInventory, PlayerOwned {
	
	// 0: mermaids gem
	// 1: base ingredient
	// 2-4: potion ingredients
	// 5-8: reagents
	// 9-20: 12 inventory slots
	public static final int INVENTORY_SIZE = 22;
	public static final int MERMAIDS_GEM_INPUT_SLOT_ID = 0;
	public static final int BASE_INPUT_SLOT_ID = 1;
	public static final int FIRST_INGREDIENT_SLOT = 2;
	public static final int FIRST_REAGENT_SLOT = 5;
	public static final int FIRST_INVENTORY_SLOT = 9;
	public static final int INVENTORY_SLOT_COUNT = 12;
	public static final int[] INGREDIENT_SLOTS = new int[]{2, 3, 4};
	public static final int[] REAGENT_SLOTS = new int[]{5, 6, 7, 8};
	
	public static final Identifier FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("milestones/unlock_fourth_potion_workshop_reagent_slot");
	
	protected final PropertyDelegate propertyDelegate;
	protected DefaultedList<ItemStack> inventory;
	protected boolean inventoryChanged;
	protected PotionWorkshopRecipe currentRecipe;
	protected int brewTime;
	protected int brewTimeTotal;
	protected int potionColor;
	protected UUID ownerUUID;
	protected PotionWorkshopBrewingRecipe lastBrewedRecipe;
	
	public PotionWorkshopBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.POTION_WORKSHOP, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.brewTime;
					case 1 -> PotionWorkshopBlockEntity.this.brewTimeTotal;
					default -> PotionWorkshopBlockEntity.this.potionColor;
				};
			}
			
			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.brewTime = value;
					case 1 -> PotionWorkshopBlockEntity.this.brewTimeTotal = value;
					case 2 -> PotionWorkshopBlockEntity.this.potionColor = value;
				}
			}
			
			@Override
			public int size() {
				return 3;
			}
		};
	}
	
	public static void tick(World world, BlockPos blockPos, BlockState blockState, PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		// check recipe crafted last tick => performance
		boolean shouldMarkDirty = false;
		
		PotionWorkshopRecipe calculatedRecipe = calculateRecipe(world, potionWorkshopBlockEntity);
		if (potionWorkshopBlockEntity.currentRecipe != calculatedRecipe) {
			potionWorkshopBlockEntity.currentRecipe = calculatedRecipe;
			potionWorkshopBlockEntity.brewTime = 0;
			if (potionWorkshopBlockEntity.currentRecipe != null) {
				potionWorkshopBlockEntity.brewTimeTotal = calculatedRecipe.getCraftingTime();
				potionWorkshopBlockEntity.potionColor = calculatedRecipe.getColor();
			}
			shouldMarkDirty = true;
		}
		potionWorkshopBlockEntity.inventoryChanged = false;
		
		if (calculatedRecipe != null) {
			// if crafting has not started: check if the inventory has enough room to start
			if (potionWorkshopBlockEntity.brewTime > 0 || hasRoomInOutputInventoryFor(potionWorkshopBlockEntity, calculatedRecipe.getMinOutputCount(potionWorkshopBlockEntity.inventory.get((BASE_INPUT_SLOT_ID))))) {
				if (potionWorkshopBlockEntity.brewTime == potionWorkshopBlockEntity.brewTimeTotal) {
					if (calculatedRecipe instanceof PotionWorkshopBrewingRecipe brewingRecipe) {
						Item baseItem = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getItem();
						if (baseItem instanceof InkPoweredPotionFillable) {
							fillPotionFillable(potionWorkshopBlockEntity, brewingRecipe);
						} else if (baseItem.equals(Items.ARROW)) {
							createTippedArrows(potionWorkshopBlockEntity, brewingRecipe);
						} else {
							brewRecipe(potionWorkshopBlockEntity, brewingRecipe);
						}
					} else {
						craftRecipe(potionWorkshopBlockEntity, (PotionWorkshopCraftingRecipe) calculatedRecipe);
					}
					
					potionWorkshopBlockEntity.brewTime = 0;
					potionWorkshopBlockEntity.inventoryChanged = true;
					potionWorkshopBlockEntity.playSound(SoundEvents.BLOCK_BREWING_STAND_BREW);
				} else {
					potionWorkshopBlockEntity.brewTime++;
				}
				
				shouldMarkDirty = true;
			}
		}
		
		if (shouldMarkDirty) {
			markDirty(world, blockPos, blockState);
		}
	}
	
	public static boolean hasRoomInOutputInventoryFor(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int outputStacks) {
		for (int slotID : potionWorkshopBlockEntity.getAvailableSlots(Direction.DOWN)) {
			if (potionWorkshopBlockEntity.getStack(slotID).isEmpty()) {
				outputStacks--;
				if (outputStacks == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static @Nullable PotionWorkshopRecipe calculateRecipe(World world, @NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		if (!potionWorkshopBlockEntity.inventoryChanged) {
			return potionWorkshopBlockEntity.currentRecipe;
		}
		
		PotionWorkshopRecipe newRecipe = null;
		if (potionWorkshopBlockEntity.currentRecipe instanceof PotionWorkshopBrewingRecipe potionWorkshopBrewingRecipe && potionWorkshopBlockEntity.currentRecipe.matches(potionWorkshopBlockEntity, world)) {
			// we check for reagents here instead of the recipe itself because of performance
			if (isBrewingRecipeApplicable(potionWorkshopBrewingRecipe, potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID), potionWorkshopBlockEntity)) {
				return potionWorkshopBlockEntity.currentRecipe;
			}
		} else if (potionWorkshopBlockEntity.currentRecipe instanceof PotionWorkshopCraftingRecipe && potionWorkshopBlockEntity.currentRecipe.matches(potionWorkshopBlockEntity, world)) {
			newRecipe = potionWorkshopBlockEntity.currentRecipe;
		} else {
			// current recipe does not match last recipe
			// => search valid recipe
			PotionWorkshopBrewingRecipe newPotionWorkshopBrewingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, potionWorkshopBlockEntity, world).orElse(null);
			if (newPotionWorkshopBrewingRecipe != null) {
				if (newPotionWorkshopBrewingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getOwnerIfOnline())) {
					// we check for reagents here instead of the recipe itself because of performance
					if (isBrewingRecipeApplicable(newPotionWorkshopBrewingRecipe, potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID), potionWorkshopBlockEntity)) {
						return newPotionWorkshopBrewingRecipe;
					}
				}
			} else {
				PotionWorkshopCraftingRecipe newPotionWorkshopCraftingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, potionWorkshopBlockEntity, world).orElse(null);
				if (newPotionWorkshopCraftingRecipe != null) {
					if (newPotionWorkshopCraftingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getOwnerIfOnline())) {
						newRecipe = newPotionWorkshopCraftingRecipe;
					}
				}
			}
		}
		
		return newRecipe;
	}
	
	private static boolean hasUniqueReagents(PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		List<Item> reagentItems = new ArrayList<>();
		for (int slot : REAGENT_SLOTS) {
			ItemStack reagentStack = potionWorkshopBlockEntity.getStack(slot);
			if (!reagentStack.isEmpty()) {
				if (reagentItems.contains(reagentStack.getItem())) {
					return false;
				} else {
					reagentItems.add(reagentStack.getItem());
				}
			}
		}
		return true;
	}
	
	private static boolean isBrewingRecipeApplicable(PotionWorkshopBrewingRecipe recipe, ItemStack baseIngredient, PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		return hasUniqueReagents(potionWorkshopBlockEntity) && recipe.recipeData.isApplicableTo(baseIngredient, getPotionModFromReagents(potionWorkshopBlockEntity));
	}
	
	private static void craftRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopCraftingRecipe recipe) {
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		if (recipe.consumesBaseIngredient()) {
			decrementBaseIngredientSlot(potionWorkshopBlockEntity, recipe.getBaseIngredient().getCount());
		}
		
		// output
		InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, recipe.getOutput().copy(), FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
	}
	
	private static void brewRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		
		int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(PotionWorkshopBrewingRecipe.BASE_POTION_COUNT_ON_BREWING + potionMod.yield, potionWorkshopBlockEntity.world.random);
		int brewedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxBrewedPotionsAmount);
		
		// calculate outputs
		List<ItemStack> results = new ArrayList<>();
		for (int i = 0; i < brewedAmount; i++) {
			results.add(brewingRecipe.getPotion(potionWorkshopBlockEntity.getStack(PotionWorkshopBlockEntity.BASE_INPUT_SLOT_ID), potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, potionWorkshopBlockEntity.world.random));
		}
		
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		decrementBaseIngredientSlot(potionWorkshopBlockEntity, brewedAmount);
		decrementReagentSlots(potionWorkshopBlockEntity);
		
		// trigger advancements for all brewed potions
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) potionWorkshopBlockEntity.getOwnerIfOnline();
		if (brewedAmount <= 0) {
			SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, ItemStack.EMPTY, 0);
		} else {
			for (ItemStack potion : results) {
				InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potion, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potion, brewedAmount);
					
					Potion potionStack = PotionUtil.getPotion(potion);
					Criteria.BREWED_POTION.trigger(serverPlayerEntity, potionStack);
				}
			}
		}
		
		potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
	}
	
	private static void createTippedArrows(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		
		int maxTippedArrowsAmount = Support.getIntFromDecimalWithChance(PotionWorkshopBrewingRecipe.BASE_ARROW_COUNT_ON_BREWING + potionMod.yield * 4, potionWorkshopBlockEntity.world.random);
		int tippedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxTippedArrowsAmount);
		
		// calculate outputs
		ItemStack arrows = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		ItemStack tippedArrows = brewingRecipe.getTippedArrows(arrows, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, tippedAmount, potionWorkshopBlockEntity.world.random);
		
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		decrementBaseIngredientSlot(potionWorkshopBlockEntity, tippedAmount);
		decrementReagentSlots(potionWorkshopBlockEntity);
		
		// trigger advancements for all brewed potions
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) potionWorkshopBlockEntity.getOwnerIfOnline();
		InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, tippedArrows, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, tippedArrows, tippedArrows.getCount());
		}
		
		potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
	}
	
	private static void fillPotionFillable(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		ItemStack potionFillableStack = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		if (potionFillableStack.getItem() instanceof InkPoweredPotionFillable) {
			// process reagents
			PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
			
			brewingRecipe.fillPotionFillable(potionFillableStack, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, potionWorkshopBlockEntity.world.random);
			
			// consume ingredients
			decrementIngredientSlots(potionWorkshopBlockEntity);
			decrementReagentSlots(potionWorkshopBlockEntity);
			potionWorkshopBlockEntity.inventory.set(BASE_INPUT_SLOT_ID, ItemStack.EMPTY);
			
			// trigger advancements for all brewed potions
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) potionWorkshopBlockEntity.getOwnerIfOnline();
			InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potionFillableStack, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack, 1);
			}
			
			potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
		}
	}
	
	private static PotionMod getPotionModFromReagents(PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		PotionMod potionMod = new PotionMod();
		for (int slot : REAGENT_SLOTS) {
			ItemStack slotStack = potionWorkshopBlockEntity.getStack(slot);
			if (!slotStack.isEmpty()) {
				PotionWorkshopReactingRecipe.combine(potionMod, slotStack, potionWorkshopBlockEntity.world.random);
			}
		}
		return potionMod;
	}
	
	public static void decrementBaseIngredientSlot(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int amount) {
		if (amount > 0) {
			potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID).decrement(amount);
		}
	}
	
	public static void decrementIngredientSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		potionWorkshopBlockEntity.getStack(MERMAIDS_GEM_INPUT_SLOT_ID).decrement(1);
		
		PotionWorkshopRecipe recipe = potionWorkshopBlockEntity.currentRecipe;
		int requiredExperience = recipe.getRequiredExperience();
		for (IngredientStack ingredientStack : recipe.getOtherIngredients()) {
			for (int slot : INGREDIENT_SLOTS) {
				ItemStack slotStack = potionWorkshopBlockEntity.getStack(slot);
				if (ingredientStack.test(slotStack)) {
					// if the recipe requires experience: remove XP from the item (like the experience bottle recipe)
					if (slotStack.getItem() instanceof ExperienceStorageItem && ExperienceStorageItem.removeStoredExperience(slotStack, requiredExperience)) {
						requiredExperience = 0;
					} else {
						ItemStack currentRemainder = slotStack.getRecipeRemainder();
						slotStack.decrement(ingredientStack.getCount());
						if (!currentRemainder.isEmpty()) {
							currentRemainder = currentRemainder.copy();
							currentRemainder.setCount(ingredientStack.getCount());
							InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, currentRemainder, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
						}
					}
					
					break;
				}
			}
		}
	}
	
	public static void decrementReagentSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		for (int i : REAGENT_SLOTS) {
			ItemStack currentStack = potionWorkshopBlockEntity.getStack(i);
			if (!currentStack.isEmpty()) {
				currentStack.decrement(1);
			}
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("LastBrewedRecipe") && this.world != null) {
			String recipeString = nbt.getString("LastBrewedRecipe");
			if (!recipeString.isEmpty() && SpectrumCommon.minecraftServer != null) {
				Optional<? extends Recipe<?>> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof PotionWorkshopBrewingRecipe brewingRecipe) {
					this.lastBrewedRecipe = brewingRecipe;
				}
			}
		} else {
			this.lastBrewedRecipe = null;
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.lastBrewedRecipe != null) {
			nbt.putString("LastBrewedRecipe", this.lastBrewedRecipe.getId().toString());
		}
	}
	
	private void playSound(SoundEvent soundEvent) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.9F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot >= FIRST_INVENTORY_SLOT;
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
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		recipeMatcher.addInput(this.inventory.get(2));
		recipeMatcher.addInput(this.inventory.get(3));
		recipeMatcher.addInput(this.inventory.get(4));
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		markDirty();
	}
	
	@Override
	public void clear() {
		this.inventory.clear();
		this.inventoryChanged = true;
		this.markDirty();
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == MERMAIDS_GEM_INPUT_SLOT_ID) {
			return stack.isOf(SpectrumItems.MERMAIDS_GEM);
		} else if (slot == BASE_INPUT_SLOT_ID) {
			return true;
		} else if (slot < FIRST_REAGENT_SLOT) {
			return true; // ingredients
		} else if (slot < FIRST_INVENTORY_SLOT) {
			return PotionWorkshopReactingRecipe.isReagent(stack.getItem());
		} else {
			return false;
		}
	}
	
	@Override
	public void setStack(int slot, @NotNull ItemStack stack) {
		ItemStack itemStack = this.inventory.get(slot);
		boolean isSameItem = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
		
		if (!isSameItem) {
			this.inventoryChanged = true;
		}
		this.markDirty();
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		} else if (side == Direction.UP) {
			return new int[]{0, 1, 2, 3, 4};
		} else {
			if (this.hasFourthReagentSlotUnlocked()) {
				return new int[]{5, 6, 7, 8};
			} else {
				return new int[]{5, 6, 7};
			}
		}
	}
	
	@Override
	public boolean canInsert(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
		return isValid(slot, stack);
	}
	
	private boolean hasFourthReagentSlotUnlocked(PlayerEntity playerEntity) {
		if (playerEntity == null) {
			return false;
		} else {
			return AdvancementHelper.hasAdvancement(playerEntity, FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER);
		}
	}
	
	private boolean hasFourthReagentSlotUnlocked() {
		if (this.ownerUUID == null) {
			return false;
		} else {
			return hasFourthReagentSlotUnlocked(getOwnerIfOnline());
		}
	}
	
	public void inventoryChanged() {
		this.inventoryChanged = true;
	}
	
	@Override
	public Text getDisplayName() {
		return Text.translatable("block.spectrum.potion_workshop");
	}
	
	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new PotionWorkshopScreenHandler(syncId, inv, this, this.propertyDelegate);
	}
}
