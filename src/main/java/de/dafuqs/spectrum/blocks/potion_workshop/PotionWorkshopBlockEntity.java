package de.dafuqs.spectrum.blocks.potion_workshop;


import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class PotionWorkshopBlockEntity extends BlockEntity implements MenuProvider, StackedContentsCompatible, ImplementedInventory, WorldlyContainer, PlayerOwned {
	
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
	
	protected final ContainerData propertyDelegate;
	protected NonNullList<ItemStack> inventory;
	protected boolean inventoryChanged;
	protected RecipeHolder<? extends PotionWorkshopRecipe> currentRecipe;
	protected int brewTime;
	protected int brewTimeTotal;
	protected int potionColor;
	protected UUID ownerUUID;
	protected RecipeHolder<PotionWorkshopBrewingRecipe> lastBrewedRecipe;
	
	public PotionWorkshopBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.POTION_WORKSHOP, pos, state);
		this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		
		this.propertyDelegate = new ContainerData() {
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
			public int getCount() {
				return 3;
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	public static void tick(Level world, BlockPos blockPos, BlockState blockState, PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		// check recipe crafted last tick => performance
		boolean shouldMarkDirty = false;
		
		var calculatedRecipe = calculateRecipe(world, potionWorkshopBlockEntity);
		if (potionWorkshopBlockEntity.currentRecipe != calculatedRecipe) {
			potionWorkshopBlockEntity.currentRecipe = calculatedRecipe;
			potionWorkshopBlockEntity.brewTime = 0;
			if (potionWorkshopBlockEntity.currentRecipe != null) {
				potionWorkshopBlockEntity.brewTimeTotal = calculatedRecipe.value().getCraftingTime();
				potionWorkshopBlockEntity.potionColor = calculatedRecipe.value().getColor();
			}
			shouldMarkDirty = true;
		}
		potionWorkshopBlockEntity.inventoryChanged = false;
		
		if (calculatedRecipe != null) {
			// if crafting has not started: check if the inventory has enough room to start
			if (potionWorkshopBlockEntity.brewTime > 0 || hasRoomInOutputInventoryFor(potionWorkshopBlockEntity, calculatedRecipe.value().getMinOutputCount(potionWorkshopBlockEntity.inventory.get((BASE_INPUT_SLOT_ID))))) {
				if (potionWorkshopBlockEntity.brewTime == potionWorkshopBlockEntity.brewTimeTotal) {
					if (calculatedRecipe.value() instanceof PotionWorkshopBrewingRecipe) {
						Item baseItem = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getItem();
						if (baseItem instanceof InkPoweredPotionFillable) {
							fillPotionFillable(potionWorkshopBlockEntity, (RecipeHolder<PotionWorkshopBrewingRecipe>) calculatedRecipe);
						} else if (baseItem.equals(Items.ARROW)) {
							createTippedArrows(potionWorkshopBlockEntity, (RecipeHolder<PotionWorkshopBrewingRecipe>) calculatedRecipe);
						} else {
							brewRecipe(potionWorkshopBlockEntity, (RecipeHolder<PotionWorkshopBrewingRecipe>) calculatedRecipe);
						}
					} else if (calculatedRecipe.value() instanceof PotionWorkshopCraftingRecipe) {
						craftRecipe(potionWorkshopBlockEntity, (RecipeHolder<PotionWorkshopCraftingRecipe>) calculatedRecipe);
					}
					
					potionWorkshopBlockEntity.brewTime = 0;
					potionWorkshopBlockEntity.inventoryChanged = true;
					potionWorkshopBlockEntity.playSound(SoundEvents.BREWING_STAND_BREW);
				} else {
					potionWorkshopBlockEntity.brewTime++;
				}
				
				shouldMarkDirty = true;
			}
		}
		
		if (shouldMarkDirty) {
			setChanged(world, blockPos, blockState);
		}
	}
	
	public static boolean hasRoomInOutputInventoryFor(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int count) {
		for (int slotID : potionWorkshopBlockEntity.getSlotsForFace(Direction.DOWN)) {
			if (potionWorkshopBlockEntity.getItem(slotID).isEmpty()) {
				count--;
				if (count == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static @Nullable RecipeHolder<? extends PotionWorkshopRecipe> calculateRecipe(Level world, @NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		if (!potionWorkshopBlockEntity.inventoryChanged) {
			return potionWorkshopBlockEntity.currentRecipe;
		}
		
		RecipeHolder<? extends PotionWorkshopRecipe> newRecipe = null;
		var current = potionWorkshopBlockEntity.currentRecipe == null ? null : potionWorkshopBlockEntity.currentRecipe.value();
		if (current instanceof PotionWorkshopBrewingRecipe potionWorkshopBrewingRecipe && current.matches(potionWorkshopBlockEntity.getRecipeInput(), world)) {
			// we check for reagents here instead of the recipe itself because of performance
			if (isBrewingRecipeApplicable(potionWorkshopBrewingRecipe, potionWorkshopBlockEntity.getItem(BASE_INPUT_SLOT_ID), potionWorkshopBlockEntity)) {
				return potionWorkshopBlockEntity.currentRecipe;
			}
		} else if (current instanceof PotionWorkshopCraftingRecipe && current.matches(potionWorkshopBlockEntity.getRecipeInput(), world)) {
			newRecipe = potionWorkshopBlockEntity.currentRecipe;
		} else {
			// current recipe does not match last recipe
			// => search valid recipe
			var newPotionWorkshopBrewingRecipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, potionWorkshopBlockEntity.getRecipeInput(), world).orElse(null);
			if (newPotionWorkshopBrewingRecipe != null) {
				if (newPotionWorkshopBrewingRecipe.value().canPlayerCraft(potionWorkshopBlockEntity.getOwnerIfOnline())) {
					// we check for reagents here instead of the recipe itself for performance reasons
					if (isBrewingRecipeApplicable(newPotionWorkshopBrewingRecipe.value(), potionWorkshopBlockEntity.getItem(BASE_INPUT_SLOT_ID), potionWorkshopBlockEntity)) {
						return newPotionWorkshopBrewingRecipe;
					}
				}
			} else {
				var newPotionWorkshopCraftingRecipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, potionWorkshopBlockEntity.getRecipeInput(), world).orElse(null);
				if (newPotionWorkshopCraftingRecipe != null) {
					if (newPotionWorkshopCraftingRecipe.value().canPlayerCraft(potionWorkshopBlockEntity.getOwnerIfOnline())) {
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
			ItemStack reagentStack = potionWorkshopBlockEntity.getItem(slot);
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
		return hasUniqueReagents(potionWorkshopBlockEntity)
				&& recipe.recipeData.isApplicableTo(baseIngredient, potionMod)
				&& !(potionMod.flags().incurable() && recipe.recipeData.statusEffect().is(SpectrumStatusEffectTags.CANNOT_BE_INCURABLE));
	}
	
	private static void craftRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, RecipeHolder<PotionWorkshopCraftingRecipe> recipe) {
		var world = potionWorkshopBlockEntity.level;
		if (world == null) return;
		
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		if (recipe.value().consumesBaseIngredient()) {
			decrementBaseIngredientSlot(potionWorkshopBlockEntity, recipe.value().getBaseIngredient().getCount());
		}
		
		// output
		addToInventoryOrSpawn(potionWorkshopBlockEntity, recipe.value().assemble(potionWorkshopBlockEntity.getRecipeInput(), potionWorkshopBlockEntity.level.registryAccess()));
	}
	
	private static void brewRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, RecipeHolder<PotionWorkshopBrewingRecipe> brewingRecipe) {
		Level world = potionWorkshopBlockEntity.getLevel();
		if (world == null) return;
		
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		
		int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.value().getModifiedYield(potionMod), world.random);
		int brewedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxBrewedPotionsAmount);
		
		// calculate outputs
		ItemStack bottles = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		List<ItemStack> results = brewingRecipe.value().getPotions(bottles, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, world.random, brewedAmount);
		
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		decrementBaseIngredientSlot(potionWorkshopBlockEntity, brewedAmount);
		decrementReagentSlots(potionWorkshopBlockEntity);
		
		// trigger advancements for all brewed potions
		ServerPlayer serverPlayerEntity = (ServerPlayer) potionWorkshopBlockEntity.getOwnerIfOnline();
		if (brewedAmount <= 0) {
			SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, ItemStack.EMPTY, 0);
		} else {
			for (ItemStack potion : results) {
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potion, brewedAmount);
					potion.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().ifPresent(
							p -> CriteriaTriggers.BREWED_POTION.trigger(serverPlayerEntity, p));
				}
				
				addToInventoryOrSpawn(potionWorkshopBlockEntity, potion);
			}
		}
		
		potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
	}
	
	private static void createTippedArrows(PotionWorkshopBlockEntity potionWorkshopBlockEntity, RecipeHolder<PotionWorkshopBrewingRecipe> brewingRecipe) {
		Level world = potionWorkshopBlockEntity.getLevel();
		if (world == null) return;
		
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		
		// the multiplication happening after the decimal chance rounding is not a mistake it is me being evil ~ Azzyy
		// we are nice to our players this one time ~Dafuqs
		int maxTippedArrowsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.value().getModifiedYield(potionMod) * PotionWorkshopBrewingRecipe.ARROW_COUNT_MULTIPLIER, world.random);
		int tippedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxTippedArrowsAmount);
		
		// calculate outputs
		ItemStack arrows = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		ItemStack tippedArrows = brewingRecipe.value().getTippedArrows(arrows, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, tippedAmount, world.random);
		
		// consume ingredients
		decrementIngredientSlots(potionWorkshopBlockEntity);
		decrementBaseIngredientSlot(potionWorkshopBlockEntity, tippedAmount);
		decrementReagentSlots(potionWorkshopBlockEntity);
		
		// trigger advancements for all brewed potions
		ServerPlayer serverPlayerEntity = (ServerPlayer) potionWorkshopBlockEntity.getOwnerIfOnline();
		InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, tippedArrows, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, tippedArrows, tippedArrows.getCount());
		}
		
		potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
	}
	
	private static void fillPotionFillable(PotionWorkshopBlockEntity potionWorkshopBlockEntity, RecipeHolder<PotionWorkshopBrewingRecipe> brewingRecipe) {
		ItemStack potionFillableStack = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		if (potionFillableStack.getItem() instanceof InkPoweredPotionFillable && potionWorkshopBlockEntity.level != null) {
			// process reagents
			PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
			
			// consume ingredients
			decrementIngredientSlots(potionWorkshopBlockEntity);
			decrementReagentSlots(potionWorkshopBlockEntity);
			
			int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(brewingRecipe.value().getModifiedYield(potionMod), potionWorkshopBlockEntity.level.random);
			if (maxBrewedPotionsAmount < 1) {
				ServerPlayer serverPlayerEntity = (ServerPlayer) potionWorkshopBlockEntity.getOwnerIfOnline();
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack, 0);
				}
				return;
			}
			
			brewingRecipe.value().fillPotionFillable(potionFillableStack, potionMod, potionWorkshopBlockEntity.lastBrewedRecipe, potionWorkshopBlockEntity.level.random);
			potionWorkshopBlockEntity.inventory.set(BASE_INPUT_SLOT_ID, ItemStack.EMPTY);
			InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potionFillableStack, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
			
			// trigger advancements for all brewed potions
			ServerPlayer serverPlayerEntity = (ServerPlayer) potionWorkshopBlockEntity.getOwnerIfOnline();
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack, 1);
			}
			
			potionWorkshopBlockEntity.lastBrewedRecipe = brewingRecipe;
		}
	}
	
	private static PotionMod getPotionModFromReagents(PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		Level world = potionWorkshopBlockEntity.getLevel();
		var builder = new PotionMod.Builder();
		if (world != null) {
			for (int slot : REAGENT_SLOTS) {
				ItemStack slotStack = potionWorkshopBlockEntity.getItem(slot);
				if (!slotStack.isEmpty()) {
					PotionWorkshopReactingRecipe.combine(builder, slotStack, world.random);
				}
			}
		}
		return builder.build();
	}
	
	public static void decrementBaseIngredientSlot(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int amount) {
		if (amount > 0) {
			decrementUsingRemainder(potionWorkshopBlockEntity, potionWorkshopBlockEntity.getItem(BASE_INPUT_SLOT_ID), amount);
		}
	}
	
	public static void decrementIngredientSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		potionWorkshopBlockEntity.getItem(MERMAIDS_GEM_INPUT_SLOT_ID).shrink(1);
		if (potionWorkshopBlockEntity.level == null) return;
		
		var recipe = potionWorkshopBlockEntity.currentRecipe;
		int requiredExperience = recipe.value().getRequiredExperience();
		for (IngredientStack ingredientStack : recipe.value().getOtherIngredients()) {
			for (int slot : INGREDIENT_SLOTS) {
				ItemStack slotStack = potionWorkshopBlockEntity.getItem(slot);
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
			ItemStack currentStack = potionWorkshopBlockEntity.getItem(i);
			if (!currentStack.isEmpty()) {
				decrementUsingRemainder(potionWorkshopBlockEntity, currentStack, 1);
			}
		}
	}
	
	private static void decrementUsingRemainder(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, ItemStack currentStack, int amount) {
		ItemStack currentRemainder = currentStack.getRecipeRemainder();
		currentStack.shrink(amount);
		if (!currentRemainder.isEmpty()) {
			addToInventoryOrSpawn(potionWorkshopBlockEntity, currentRemainder);
		}
	}
	
	private static void addToInventoryOrSpawn(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, ItemStack currentRemainder) {
		currentRemainder = InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, currentRemainder, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + INVENTORY_SLOT_COUNT);
		if (!currentRemainder.isEmpty()) {
			Containers.dropItemStack(potionWorkshopBlockEntity.level, potionWorkshopBlockEntity.worldPosition.getX(), potionWorkshopBlockEntity.worldPosition.getY(), potionWorkshopBlockEntity.worldPosition.getZ(), currentRemainder);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		if (nbt.contains("LastBrewedRecipe") && this.getLevel() != null) {
			var id = ResourceLocation.parse(nbt.getString("LastBrewedRecipe"));
			var optRecipe = registryLookup.lookup(Registries.RECIPE)
					.flatMap(impl -> impl.get(ResourceKey.create(Registries.RECIPE, id)));
			if (optRecipe.isPresent() && optRecipe.get().value() instanceof PotionWorkshopBrewingRecipe brewingRecipe) {
				this.lastBrewedRecipe = new RecipeHolder<>(id, brewingRecipe);
			}
		} else {
			this.lastBrewedRecipe = null;
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.lastBrewedRecipe != null) {
			nbt.putString("LastBrewedRecipe", this.lastBrewedRecipe.id().toString());
		}
	}
	
	private void playSound(SoundEvent soundEvent) {
		if (level == null) return;
		RandomSource random = level.random;
		level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), soundEvent, SoundSource.BLOCKS, 0.9F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return slot >= FIRST_INVENTORY_SLOT;
	}
	
	public SimpleRecipeInput getRecipeInput() {
		return new SimpleRecipeInput(inventory);
	}
	
	@Override
	public NonNullList<ItemStack> getItems() {
		return inventory;
	}
	
	@Override
	public boolean stillValid(Player player) {
		if (level == null || level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	@Override
	public void fillStackedContents(StackedContents recipeMatcher) {
		recipeMatcher.accountStack(this.inventory.get(2));
		recipeMatcher.accountStack(this.inventory.get(3));
		recipeMatcher.accountStack(this.inventory.get(4));
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	@Override
	public void inventoryChanged() {
		this.inventoryChanged = true;
		this.setChanged();
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (stack.is(SpectrumItems.MERMAIDS_GEM)) {
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
	public void setItem(int slot, @NotNull ItemStack stack) {
		ItemStack itemStack = this.inventory.get(slot);
		if (!ItemStack.isSameItemSameComponents(stack, itemStack))
			this.inventoryChanged = true;
		getItems().set(slot, stack);
		if (stack.getCount() > stack.getMaxStackSize())
			stack.setCount(stack.getMaxStackSize());
		setChanged();
	}
	
	@Override
	public int[] getSlotsForFace(Direction side) {
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
	public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
		return canPlaceItem(slot, stack);
	}
	
	private boolean hasFourthReagentSlotUnlocked(Player playerEntity) {
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
	
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.spectrum.potion_workshop");
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		return new PotionWorkshopScreenHandler(syncId, inv, this, this.propertyDelegate);
	}
}
