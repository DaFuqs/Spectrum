package de.dafuqs.spectrum.blocks.potion_workshop;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.interfaces.PotionFillable;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PotionWorkshopBlockEntity extends BlockEntity implements RecipeInputProvider, SidedInventory, PlayerOwned {
	
	// 0: mermaids gem
	// 1: base ingredient
	// 2-4: potion ingredients
	// 5-8: reagents
	// 9-21: 12 inventory slots
	protected static final int INVENTORY_SIZE = 22;
	protected static final Identifier FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_fourth_potion_workshop_reagent_slot");
	
	public static final int MERMAIDS_GEM_INPUT_SLOT_ID = 0;
	public static final int BASE_INPUT_SLOT_ID = 1;
	public static final int FIRST_INGREDIENT_SLOT = 2;
	public static final int FIRST_REAGENT_SLOT = 5;
	public static final int FIRST_INVENTORY_SLOT = 9;
	
	protected DefaultedList<ItemStack> inventory;
	protected boolean inventoryChanged;
	
	protected final PropertyDelegate propertyDelegate;
	protected PotionWorkshopRecipe currentRecipe;
	protected int craftingTime;
	protected int craftingTimeTotal;
	
	protected UUID ownerUUID;
	protected StatusEffect lastBrewedStatusEffect;
	
	protected static final int BASE_POTION_COUNT_ON_BREWING = 3;
	
	public PotionWorkshopBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.POTION_WORKSHOP, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.craftingTime;
					default -> PotionWorkshopBlockEntity.this.craftingTimeTotal;
				};
			}
			
			public void set(int index, int value) {
				switch (index) {
					case 0 -> PotionWorkshopBlockEntity.this.craftingTime = value;
					case 1 -> PotionWorkshopBlockEntity.this.craftingTimeTotal = value;
				}
			}
			
			public int size() {
				return 2;
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
			return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
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
			potionWorkshopBlockEntity.craftingTime = 0;
			shouldMarkDirty = true;
		}
		
		
		if (calculatedRecipe != null) {
			boolean craftingFinished = false;
			// if crafting has not started: check if the inventory has enough room to start
			if(potionWorkshopBlockEntity.craftingTime > 0 || hasRoomInOutputInventoryFor(potionWorkshopBlockEntity, calculatedRecipe.getMinOutputCount())) {
				if (potionWorkshopBlockEntity.craftingTime == potionWorkshopBlockEntity.craftingTimeTotal) {
					potionWorkshopBlockEntity.craftingTime = 0;
					craftingFinished = true;
					if (calculatedRecipe instanceof PotionWorkshopBrewingRecipe brewingRecipe) {
						Item baseItem = potionWorkshopBlockEntity.inventory.get(BASE_INPUT_SLOT_ID).getItem();
						if(baseItem instanceof PotionFillable) {
							fillPotionFillable(potionWorkshopBlockEntity, brewingRecipe);
						} if(baseItem.equals(Items.ARROW)) {
							createTippedArrows(potionWorkshopBlockEntity, brewingRecipe);
						} else {
							brewRecipe(potionWorkshopBlockEntity, brewingRecipe);
						}
					} else {
						craftRecipe(potionWorkshopBlockEntity, (PotionWorkshopCraftingRecipe) calculatedRecipe);
					}
				} else {
					potionWorkshopBlockEntity.craftingTime++;
				}
				
				if (craftingFinished) {
					potionWorkshopBlockEntity.inventoryChanged = true;
				}
				shouldMarkDirty = true;
			}
		}
		
		if (shouldMarkDirty) {
			markDirty(world, blockPos, blockState);
		}
	}
	
	private static void createTippedArrows(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		//TODO
	}
	
	private static void fillPotionFillable(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe brewingRecipe) {
		//TODO
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
		if (potionWorkshopBlockEntity.currentRecipe instanceof PotionWorkshopBrewingRecipe && ((PotionWorkshopBrewingRecipe) potionWorkshopBlockEntity.currentRecipe).matches(potionWorkshopBlockEntity, world)) {
			// unchanged pedestal recipe
			return potionWorkshopBlockEntity.currentRecipe;
		} else if(((PotionWorkshopCraftingRecipe) potionWorkshopBlockEntity.currentRecipe).matches(potionWorkshopBlockEntity, world)) {
			return potionWorkshopBlockEntity.currentRecipe;
		} else {
			// current recipe does not match last recipe
			// => search valid recipe
			PotionWorkshopBrewingRecipe potionWorkshopBrewingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, potionWorkshopBlockEntity, world).orElse(null);
			if (potionWorkshopBrewingRecipe != null) {
				if (potionWorkshopBrewingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world))) {
					newRecipe = potionWorkshopBrewingRecipe;
				}
			} else {
				PotionWorkshopCraftingRecipe potionWorkshopCraftingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, potionWorkshopBlockEntity, world).orElse(null);
				if (potionWorkshopCraftingRecipe != null) {
					if (potionWorkshopCraftingRecipe.canPlayerCraft(potionWorkshopBlockEntity.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world))) {
						newRecipe = potionWorkshopCraftingRecipe;
					}
				}
			}
		}
		
		return newRecipe;
	}
	
	private static void craftRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopCraftingRecipe recipe) {
		decreaseIngredientSlots(potionWorkshopBlockEntity, recipe);
		for(int i : potionWorkshopBlockEntity.getAvailableSlots(Direction.DOWN)) {
			if(potionWorkshopBlockEntity.getStack(i).isEmpty()) {
				potionWorkshopBlockEntity.setStack(i, recipe.getOutput().copy());
			}
		}
	}
	
	private static void brewRecipe(PotionWorkshopBlockEntity potionWorkshopBlockEntity, PotionWorkshopBrewingRecipe recipe) {
		decreaseIngredientSlots(potionWorkshopBlockEntity, recipe);
		
		// process reagents
		PotionMod potionMod = new PotionMod();
		for(int i : new int[]{5,6,7,8}) {
			ItemStack itemStack = potionWorkshopBlockEntity.getStack(i);
			if(!itemStack.isEmpty() && !PotionWorkshopReagents.isReagent(itemStack.getItem())) {
				potionMod = PotionWorkshopReagents.modify(itemStack.getItem(), potionMod, potionWorkshopBlockEntity.world.random);
			}
		}
		
		// calculate outputs
		List<ItemStack> results = new ArrayList<>();
		int brewedPotionsAmount = Support.getIntFromDecimalWithChance(BASE_POTION_COUNT_ON_BREWING + potionMod.flatYieldBonus, potionWorkshopBlockEntity.world.random);
		for(int i = 0; i < brewedPotionsAmount; i++) {
			results.add(recipe.getRandomPotion(potionMod, potionWorkshopBlockEntity.lastBrewedStatusEffect, potionWorkshopBlockEntity.world.random));
		}
		
		// trigger advancements for all brewed potions
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(potionWorkshopBlockEntity.world, potionWorkshopBlockEntity.ownerUUID);
		if(serverPlayerEntity != null) {
			for(ItemStack potion : results) {
				SpectrumAdvancementCriteria.POTION_WORKSHOP_BREWING.trigger(serverPlayerEntity, potion);
			}
		}
	}
	
	public static void decreaseIngredientSlots(@NotNull PotionWorkshopBlockEntity potionWorkshopBlockEntity, @NotNull PotionWorkshopRecipe potionWorkshopRecipe) {
		potionWorkshopBlockEntity.getStack(MERMAIDS_GEM_INPUT_SLOT_ID).decrement(1);
		if(potionWorkshopRecipe.consumesBaseIngredient()) {
			potionWorkshopBlockEntity.getStack(BASE_INPUT_SLOT_ID).decrement(1);
		}
		for(Ingredient ingredient : potionWorkshopRecipe.getOtherIngredients()) {
			for(int i : new int[]{2, 3, 4}) {
				ItemStack currentStack = potionWorkshopBlockEntity.getStack(i);
				if(ingredient.test(currentStack)) {
					currentStack.decrement(1);
					break;
				}
			}
		}
	}
	
}
