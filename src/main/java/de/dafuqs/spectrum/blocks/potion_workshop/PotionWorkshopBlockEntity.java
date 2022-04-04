package de.dafuqs.spectrum.blocks.potion_workshop;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.interfaces.PotionFillable;
import de.dafuqs.spectrum.inventories.PotionWorkshopScreenHandler;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReagents;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.BrewedPotionCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PotionWorkshopBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, RecipeInputProvider, SidedInventory, PlayerOwned {
	
	// 0: mermaids gem
	// 1: base ingredient
	// 2-4: potion ingredients
	// 5-8: reagents
	// 9-20: 12 inventory slots
	public static final int INVENTORY_SIZE = 22;
	public static final Identifier FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_fourth_potion_workshop_reagent_slot");
	
	public static final int MERMAIDS_GEM_INPUT_SLOT_ID = 0;
	public static final int BASE_INPUT_SLOT_ID = 1;
	public static final int FIRST_INGREDIENT_SLOT = 2;
	public static final int FIRST_REAGENT_SLOT = 5;
	public static final int FIRST_INVENTORY_SLOT = 9;
	
	protected DefaultedList<ItemStack> inventory;
	protected boolean inventoryChanged;
	
	protected final PropertyDelegate propertyDelegate;
	protected PotionWorkshopRecipe currentRecipe;
	protected int brewTime;
	protected int brewTimeTotal;
	protected int potionColor;
	
	protected UUID ownerUUID;
	protected StatusEffect lastBrewedStatusEffect;
	
	protected static final int BASE_POTION_COUNT_ON_BREWING = 3;
	protected static final int BASE_ARROW_COUNT_ON_BREWING = 12;
	
	public PotionWorkshopBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.POTION_WORKSHOP, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.brewTime;
					case 1 -> PotionWorkshopBlockEntity.this.brewTimeTotal;
					default -> PotionWorkshopBlockEntity.this.potionColor;
				};
			}
			
			public void set(int index, int value) {
				switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.brewTime = value;
					case 1 -> PotionWorkshopBlockEntity.this.brewTimeTotal = value;
					case 2 -> PotionWorkshopBlockEntity.this.potionColor = value;
				}
			}
			
			public int size() {
				return 3;
			}
		};
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
		if(nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if(nbt.contains("LastBrewedStatusEffect")) {
			this.lastBrewedStatusEffect = Registry.STATUS_EFFECT.get(Identifier.tryParse(nbt.getString("LastBrewedStatusEffect")));
		} else {
			this.lastBrewedStatusEffect = null;
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.lastBrewedStatusEffect != null) {
			Identifier lastBrewedStatusEffectIdentifier = Registry.STATUS_EFFECT.getId(this.lastBrewedStatusEffect);
			if(lastBrewedStatusEffectIdentifier != null) {
				nbt.putString("LastBrewedStatusEffect", lastBrewedStatusEffectIdentifier.toString());
			}
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
		} while(itemStack.isEmpty());
		
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
			return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
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
	}
	
	@Override
	public void clear() {
		this.inventory.clear();
		this.inventoryChanged = true;
		this.markDirty();
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if(slot == MERMAIDS_GEM_INPUT_SLOT_ID) {
			return stack.isOf(SpectrumItems.MERMAIDS_GEM);
		} else if(slot == BASE_INPUT_SLOT_ID) {
			return true;
		} else if(slot < FIRST_REAGENT_SLOT) {
			return true; // ingredients
		} else if(slot < FIRST_INVENTORY_SLOT) {
			return PotionWorkshopReagents.reagentEffects.containsKey(stack.getItem());
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
		
		if(!isSameItem) {
			this.inventoryChanged = true;
		}
		this.markDirty();
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if(side == Direction.DOWN) {
			return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		} else if(side == Direction.UP) {
			return new int[]{0, 1, 2, 3, 4};
		} else {
			if(this.hasFourthReagentSlotUnlocked()) {
				return new int[]{5, 6, 7};
			} else {
				return new int[]{5, 6, 7, 8};
			}
		}
	}
	
	@Override
	public boolean canInsert(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
		return isValid(slot, stack);
	}
	
	private boolean hasFourthReagentSlotUnlocked(PlayerEntity playerEntity) {
		if(playerEntity == null) {
			return false;
		} else {
			return Support.hasAdvancement(playerEntity, FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER);
		}
	}
	
	private boolean hasFourthReagentSlotUnlocked() {
		if(this.ownerUUID == null) {
			return false;
		} else {
			return hasFourthReagentSlotUnlocked((getPlayerEntityIfOnline(this.world)));
		}
	}
	
	public static void tick(World world, BlockPos blockPos, BlockState blockState, PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		// check recipe crafted last tick => performance
		boolean shouldMarkDirty = false;
		
		PotionWorkshopRecipe calculatedRecipe = calculateRecipe(world, potionWorkshopBlockEntity);
		potionWorkshopBlockEntity.inventoryChanged = false;
		if (potionWorkshopBlockEntity.currentRecipe != calculatedRecipe) {
			potionWorkshopBlockEntity.currentRecipe = calculatedRecipe;
			potionWorkshopBlockEntity.brewTime = 0;
			if(potionWorkshopBlockEntity.currentRecipe != null) {
				potionWorkshopBlockEntity.brewTimeTotal = calculatedRecipe.getCraftingTime();
				potionWorkshopBlockEntity.potionColor = calculatedRecipe.getColor();
			}
			shouldMarkDirty = true;
		}
		
		
		if (calculatedRecipe != null) {
			// if crafting has not started: check if the inventory has enough room to start
			if(potionWorkshopBlockEntity.brewTime > 0 || hasRoomInOutputInventoryFor(potionWorkshopBlockEntity, calculatedRecipe.getMinOutputCount(potionWorkshopBlockEntity.inventory.get((BASE_INPUT_SLOT_ID))))) {
				if (potionWorkshopBlockEntity.brewTime == potionWorkshopBlockEntity.brewTimeTotal) {
					if (calculatedRecipe instanceof PotionWorkshopBrewingRecipe brewingRecipe) {
						Item baseItem = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getItem();
						if(baseItem instanceof PotionFillable) {
							fillPotionFillable(potionWorkshopBlockEntity, brewingRecipe);
						} else if(baseItem.equals(Items.ARROW)) {
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
		for(int slotID : potionWorkshopBlockEntity.getAvailableSlots(Direction.DOWN)) {
			if(potionWorkshopBlockEntity.getStack(slotID).isEmpty()) {
				outputStacks--;
				if(outputStacks == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static @Nullable PotionWorkshopRecipe calculateRecipe(World world, @NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		if(!potionWorkshopBlockEntity.inventoryChanged) {
			return potionWorkshopBlockEntity.currentRecipe;
		}
		
		PotionWorkshopRecipe newRecipe = null;
		if (potionWorkshopBlockEntity.currentRecipe instanceof PotionWorkshopBrewingRecipe && potionWorkshopBlockEntity.currentRecipe.matches(potionWorkshopBlockEntity, world)) {
			// we check for reagents here instead of the recipe itself because of performance
			if (isBrewingRecipeApplicableToBaseIngredient(potionWorkshopBlockEntity)) {
				return potionWorkshopBlockEntity.currentRecipe;
			}
		} else if(potionWorkshopBlockEntity.currentRecipe instanceof PotionWorkshopCraftingRecipe && potionWorkshopBlockEntity.currentRecipe.matches(potionWorkshopBlockEntity, world)) {
			return potionWorkshopBlockEntity.currentRecipe;
		} else {
			// current recipe does not match last recipe
			// => search valid recipe
			PotionWorkshopBrewingRecipe newPotionWorkshopBrewingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, potionWorkshopBlockEntity, world).orElse(null);
			if (newPotionWorkshopBrewingRecipe != null) {
				if (newPotionWorkshopBrewingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world))) {
					// we check for reagents here instead of the recipe itself because of performance
					if (isBrewingRecipeApplicableToBaseIngredient(potionWorkshopBlockEntity)) {
						newRecipe = newPotionWorkshopBrewingRecipe;
					}
				}
			} else {
				PotionWorkshopCraftingRecipe newPotionWorkshopCraftingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, potionWorkshopBlockEntity, world).orElse(null);
				if (newPotionWorkshopCraftingRecipe != null) {
					if (newPotionWorkshopCraftingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world))) {
						newRecipe = newPotionWorkshopCraftingRecipe;
					}
				}
			}
		}
		
		return newRecipe;
	}
	
	private static boolean isBrewingRecipeApplicableToBaseIngredient(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		if(potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID).isOf(Items.ARROW)) { // arrows require lingering potions as base
			PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
			return potionMod.makeSplashing && potionMod.makeLingering;
		} else if(potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID).getItem() instanceof PotionFillable potionFillable) {
			return !potionFillable.isFull(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID));
		} else {
			return true;
		}
	}
	
	private static void craftRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopCraftingRecipe recipe) {
		// consume ingredients
		decreaseIngredientSlots(potionWorkshopBlockEntity);
		if(recipe.consumesBaseIngredient()) {
			decreaseBaseIngredientSlot(potionWorkshopBlockEntity, 1);
		}
		// if the recipe requires a Knowledge gem as input: remove 10 XP (experience bottle)
		if(recipe.getRequiredExperience() > 0) {
			for(int i : new int[]{BASE_INPUT_SLOT_ID, FIRST_INGREDIENT_SLOT, FIRST_INGREDIENT_SLOT+1, FIRST_INGREDIENT_SLOT+2}) {
				ItemStack slotStack = potionWorkshopBlockEntity.getStack((i));
				if(slotStack.getItem() instanceof ExperienceStorageItem) {
					ExperienceStorageItem.removeStoredExperience(slotStack, recipe.getRequiredExperience());
					potionWorkshopBlockEntity.inventoryChanged = true;
					break;
				}
			}
		}
		
		// output
		InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, recipe.getOutput().copy(), FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + 12);
	}
	
	private static void brewRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		int maxBrewedPotionsAmount = Support.getIntFromDecimalWithChance(BASE_POTION_COUNT_ON_BREWING + potionMod.flatYieldBonus, potionWorkshopBlockEntity.world.random);
		int brewedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxBrewedPotionsAmount);
		
		// consume ingredients
		decreaseIngredientSlots(potionWorkshopBlockEntity);
		decreaseBaseIngredientSlot(potionWorkshopBlockEntity, brewedAmount);
		decreaseReagentSlots(potionWorkshopBlockEntity);
		
		// calculate outputs
		List<ItemStack> results = new ArrayList<>();
		for(int i = 0; i < brewedAmount; i++) {
			results.add(brewingRecipe.getRandomPotion(potionMod, potionWorkshopBlockEntity.lastBrewedStatusEffect, potionWorkshopBlockEntity.world.random));
		}
		
		// trigger advancements for all brewed potions
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world, potionWorkshopBlockEntity.ownerUUID);
		for(ItemStack potion : results) {
			InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potion, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + 12);
			if(serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potion);
				
				Potion potionStack = PotionUtil.getPotion(potion);
				Criteria.BREWED_POTION.trigger(serverPlayerEntity, potionStack);
			}
		}
		
		potionWorkshopBlockEntity.lastBrewedStatusEffect = brewingRecipe.getStatusEffect();
	}
	
	private static void createTippedArrows(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		// process reagents
		PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
		int maxTippedArrowsAmount = Support.getIntFromDecimalWithChance(BASE_ARROW_COUNT_ON_BREWING + potionMod.flatYieldBonus * 4, potionWorkshopBlockEntity.world.random);
		int tippedAmount = Math.min(potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getCount(), maxTippedArrowsAmount);
		
		// consume ingredients
		decreaseIngredientSlots(potionWorkshopBlockEntity);
		decreaseBaseIngredientSlot(potionWorkshopBlockEntity, tippedAmount);
		decreaseReagentSlots(potionWorkshopBlockEntity);
		
		// calculate outputs
		ItemStack tippedArrows = brewingRecipe.getTippedArrows(potionMod, potionWorkshopBlockEntity.lastBrewedStatusEffect, tippedAmount, potionWorkshopBlockEntity.world.random);
		
		// trigger advancements for all brewed potions
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world, potionWorkshopBlockEntity.ownerUUID);
		InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, tippedArrows, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + 12);
		if(serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, tippedArrows);
		}
		
		potionWorkshopBlockEntity.lastBrewedStatusEffect = brewingRecipe.getStatusEffect();
	}
	
	private static void fillPotionFillable(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		ItemStack potionFillableStack = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID);
		if(potionFillableStack.getItem() instanceof PotionFillable) {
			// process reagents
			PotionMod potionMod = getPotionModFromReagents(potionWorkshopBlockEntity);
			
			// consume ingredients
			decreaseIngredientSlots(potionWorkshopBlockEntity);
			decreaseReagentSlots(potionWorkshopBlockEntity);
			potionWorkshopBlockEntity.inventory.set(BASE_INPUT_SLOT_ID, ItemStack.EMPTY);
			
			brewingRecipe.fillPotionFillable(potionFillableStack, potionMod, potionWorkshopBlockEntity.lastBrewedStatusEffect, potionWorkshopBlockEntity.world.random);
			
			// trigger advancements for all brewed potions
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world, potionWorkshopBlockEntity.ownerUUID);
			InventoryHelper.addToInventory(potionWorkshopBlockEntity.inventory, potionFillableStack, FIRST_INVENTORY_SLOT, FIRST_INVENTORY_SLOT + 12);
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potionFillableStack);
			}
			
			potionWorkshopBlockEntity.lastBrewedStatusEffect = brewingRecipe.getStatusEffect();
		}
	}
	
	private static PotionMod getPotionModFromReagents(PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		PotionMod potionMod = new PotionMod();
		for(int i : new int[]{5,6,7,8}) {
			ItemStack itemStack = potionWorkshopBlockEntity.getStack(i);
			if(!itemStack.isEmpty() && PotionWorkshopReagents.isReagent(itemStack.getItem())) {
				potionMod = PotionWorkshopReagents.modify(itemStack.getItem(), potionMod, potionWorkshopBlockEntity.world.random);
			}
		}
		return potionMod;
	}
	
	public static void decreaseBaseIngredientSlot(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, int baseIngredientAmount) {
		potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID).decrement(baseIngredientAmount);
	}
	
	public static void decreaseIngredientSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		potionWorkshopBlockEntity.getStack(MERMAIDS_GEM_INPUT_SLOT_ID).decrement(1);
		for(int i : new int[]{2, 3, 4}) {
			ItemStack currentStack = potionWorkshopBlockEntity.getStack(i);
			if(!currentStack.isEmpty() && !(currentStack.getItem() instanceof ExperienceStorageItem)) {
				currentStack.decrement(1);
			}
		}
	}
	
	public static void decreaseReagentSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
		for(int i : new int[]{5, 6, 7, 8}) {
			ItemStack currentStack = potionWorkshopBlockEntity.getStack(i);
			if(!currentStack.isEmpty()) {
				currentStack.decrement(1);
			}
		}
	}
	
	public void inventoryChanged() {
		this.inventoryChanged = true;
	}
	
	@Override
	public Text getDisplayName() {
		return new TranslatableText("block.spectrum.potion_workshop");
	}
	
	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new PotionWorkshopScreenHandler(syncId, inv, this, this.propertyDelegate);
	}
}
