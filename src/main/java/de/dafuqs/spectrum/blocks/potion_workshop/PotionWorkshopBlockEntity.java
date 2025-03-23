package de.dafuqs.spectrum.blocks.potion_workshop;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.registries.*;
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
import java.util.stream.*;

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
	public static final int[] INGREDIENT_SLOTS = IntStream.rangeClosed(2, 4).toArray();
	public static final int[] REAGENT_SLOTS = IntStream.rangeClosed(5, 8).toArray();
	
	private static final int[] ACCESSIBLE_SLOTS_UP = {0, 1, 2, 3, 4};
	private static final int[] ACCESSIBLE_SLOTS_SIDE_WITHOUT_UNLOCK = {5, 6, 7};
	private static final int[] ACCESSIBLE_SLOTS_SIDE_WITH_UNLOCK = {5, 6, 7, 8};
	private static final int[] ACCESSIBLE_SLOTS_DOWN = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
	
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
	
	public static boolean hasRoomInOutputInventoryFor(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int count) {
		for (int slotID : potionWorkshopBlockEntity.getAvailableSlots(Direction.DOWN)) {
			if (potionWorkshopBlockEntity.getStack(slotID).isEmpty()) {
				count--;
				if (count == 0) {
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
					// we check for reagents here instead of the recipe itself for performance reasons
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
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		return hasUniqueReagents(potionWorkshopBlockEntity) && recipe.recipeData.isApplicableTo(baseIngredient, potionMod)
		       && !(potionMod.incurable && SpectrumStatusEffectTags.cannotBeIncurable(recipe.recipeData.statusEffect()));
	}
	
	private static void craftRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopCraftingRecipe recipe) {
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		if (recipe.consumesBaseIngredient()) {
			decrementBaseIngredientSlot(potionWorkshopBlockEntity, recipe.getBaseIngredient().getCount());
		}
		
		// output
		addToInventoryOrSpawn(potionWorkshopBlockEntity, recipe.craft(potionWorkshopBlockEntity, potionWorkshopBlockEntity.world.getRegistryManager()));
	}
	
	private static void brewRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		World world = potionWorkshopBlockEntity.getWorld();
		
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		
		int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.getModifiedYield(potionMod), world.random);
		int brewedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxBrewedPotionsAmount);
		
		// calculate outputs
		ItemStack bottles = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		List<ItemStack> results = brewingRecipe.getPotions(bottles, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, world.random, brewedAmount);
		
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
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potion, brewedAmount);
					
					Potion potionStack = PotionUtil.getPotion(potion);
					Criteria.BREWED_POTION.trigger(serverPlayerEntity, potionStack);
				}
				
				addToInventoryOrSpawn(potionWorkshopBlockEntity, potion);
			}
		}
		
		potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
	}
	
	private static void createTippedArrows(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		World world = potionWorkshopBlockEntity.getWorld();
		
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);

		// the multiplication happening after the decimal chance rounding is not a mistake it is me being evil ~ Azzyy
		// we are nice to our players this one time ~Dafuqs
		int maxTippedArrowsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.getModifiedYield(potionMod) * PotionWorkshopBrewingRecipe.ARROW_COUNT_MULTIPLIER, world.random);
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
			
			// consume ingredients
			decrementIngredientSlots(potionWorkshopBlockEntity);
			decrementReagentSlots(potionWorkshopBlockEntity);
			
			int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.getModifiedYield(potionMod), potionWorkshopBlockEntity.world.random);
			if (maxBrewedPotionsAmount < 1) {
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) potionWorkshopBlockEntity.getOwnerIfOnline();
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack, 0);
				}
				return;
			}
			
			brewingRecipe.fillPotionFillable(potionFillableStack, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, potionWorkshopBlockEntity.world.random);
			potionWorkshopBlockEntity.inventory.set(BASE_INPUT_SLOT_ID, ItemStack.EMPTY);
			InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potionFillableStack, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
			
			// trigger advancements for all brewed potions
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) potionWorkshopBlockEntity.getOwnerIfOnline();
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack, 1);
			}
			
			potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
		}
	}
	
	private static PotionMod getPotionModFromReagents(PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		World world = potionWorkshopBlockEntity.getWorld();
		
		PotionMod potionMod = new PotionMod();
		for (int slot : REAGENT_SLOTS) {
			ItemStack slotStack = potionWorkshopBlockEntity.getStack(slot);
			if (!slotStack.isEmpty()) {
				PotionWorkshopReactingRecipe.combine(potionMod, slotStack, world.random);
			}
		}
		return potionMod;
	}
	
	public static void decrementBaseIngredientSlot(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int amount) {
		if (amount > 0) {
			decrementUsingRemainder(potionWorkshopBlockEntity, potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID), amount);
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
						decrementUsingRemainder(potionWorkshopBlockEntity, slotStack, 1);
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
				decrementUsingRemainder(potionWorkshopBlockEntity, currentStack, 1);
			}
		}
	}
	
	private static void decrementUsingRemainder(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, ItemStack currentStack, int amount) {
		ItemStack currentRemainder = currentStack.getRecipeRemainder();
		currentStack.decrement(amount);
		if (!currentRemainder.isEmpty()) {
			addToInventoryOrSpawn(potionWorkshopBlockEntity, currentRemainder);
		}
	}
	
	private static void addToInventoryOrSpawn(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, ItemStack currentRemainder) {
		currentRemainder = InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, currentRemainder, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
		if (!currentRemainder.isEmpty()) {
			ItemScatterer.spawn(potionWorkshopBlockEntity.world, potionWorkshopBlockEntity.pos.getX(), potionWorkshopBlockEntity.pos.getY(), potionWorkshopBlockEntity.pos.getZ(), currentRemainder);
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		if (nbt.contains("LastBrewedRecipe") && this.getWorld() != null) {
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
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
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
		if (this.getWorld().getBlockEntity(this.pos) != this) {
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
		if (stack.isOf(SpectrumItems.MERMAIDS_GEM)) {
			return slot == MERMAIDS_GEM_INPUT_SLOT_ID;
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
		boolean isSameItem = ItemStack.canCombine(stack, itemStack);
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
			return ACCESSIBLE_SLOTS_DOWN;
		} else if (side == Direction.UP) {
			return ACCESSIBLE_SLOTS_UP;
		} else {
			if (this.hasFourthReagentSlotUnlocked()) {
				return ACCESSIBLE_SLOTS_SIDE_WITH_UNLOCK;
			} else {
				return ACCESSIBLE_SLOTS_SIDE_WITHOUT_UNLOCK;
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
			return AdvancementHelper.hasAdvancement(playerEntity, SpectrumAdvancements.FOURTH_BREWING_SLOT);
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
