package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.progression.ClientRecipeToastManager;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PotionWorkshopCraftingRecipe extends PotionWorkshopRecipe {
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	protected final int requiredExperience;
	protected final ItemStack output;
	
	public PotionWorkshopCraftingRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, int requiredExperience, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, int color, Identifier requiredAdvancementIdentifier) {
		super(id, group, craftingTime, color, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.output = output;
		this.baseIngredient = baseIngredient;
		this.requiredExperience = requiredExperience;
		this.consumeBaseIngredient = consumeBaseIngredient;
		
		if(FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			registerInClientToastManager();
		}
	}
	
	@Environment(EnvType.CLIENT)
	private void registerInClientToastManager() {
		ClientRecipeToastManager.registerUnlockablePotionWorkshopRecipe(this);
	}
	
	
	public Ingredient getBaseIngredient() {
		return baseIngredient;
	}
	
	public boolean consumesBaseIngredient() {
		return consumeBaseIngredient;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public boolean usesReagents() {
		return false;
	}
	
	public int getRequiredExperience() {
		return this.requiredExperience;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return baseIngredient.test(itemStack);
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofStacks(SpectrumItems.MERMAIDS_GEM.getDefaultStack()));
		defaultedList.add(this.baseIngredient);
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}
	
	@Override
	public boolean matches(@NotNull Inventory inv, World world) {
		if(enoughExperienceSupplied(inv)) {
			return super.matches(inv, world);
		}
		return false;
	}
	
	// we just test for a single ExperienceStorageItem here instead
	// of iterating over every item. The specification mentions that
	// Only one is supported and just a single ExperienceStorageItem
	// should be used per recipe, tough
	private boolean enoughExperienceSupplied(Inventory inv) {
		if(this.requiredExperience > 0) {
			for(int i : new int[]{PotionWorkshopBlockEntity.BASE_INPUT_SLOT_ID, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT,
					PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT+1, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT+2}) {
				
				if((inv.getStack(i).getItem() instanceof ExperienceStorageItem)) {
					return ExperienceStorageItem.getStoredExperience(inv.getStack(i)) >= requiredExperience;
				}
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}
	
	@Override
	public int getMinOutputCount(ItemStack itemStack) {
		return 1;
	}
	
}
