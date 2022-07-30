package de.dafuqs.spectrum.recipe.crystallarieum;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CrystallarieumRecipe implements Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_crystallarieum");
	
	protected final Identifier id;
	protected final String group;
	
	protected final Ingredient inputIngredient;
	protected final List<BlockState> growthStages;
	protected final int secondsPerGrowthStage;
	protected final InkColor inkColor;
	protected final int inkPerSecond;
	protected final boolean growsWithoutCatalyst;
	protected final List<CrystallarieumCatalyst> catalysts;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	
	protected final static Map<Ingredient, CrystallarieumRecipe> ingredientMap = new HashMap<>();
	protected final static Map<BlockState, CrystallarieumRecipe> stateMap = new HashMap<>();
	
	public CrystallarieumRecipe(Identifier id, String group, Ingredient inputIngredient, List<BlockState> growthStages, int secondsPerGrowthStage, InkColor inkColor, int inkPerSecond, boolean growsWithoutCatalyst, List<CrystallarieumCatalyst> catalysts, @Nullable Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.inputIngredient = inputIngredient;
		this.growthStages = growthStages;
		this.secondsPerGrowthStage = secondsPerGrowthStage;
		this.inkColor = inkColor;
		this.inkPerSecond = inkPerSecond;
		this.growsWithoutCatalyst = growsWithoutCatalyst;
		this.catalysts = catalysts;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		
		ingredientMap.put(inputIngredient, this);
		for(BlockState growthStage : growthStages) {
			stateMap.put(growthStage, this);
		}
	}
	
	@Nullable
	public static CrystallarieumRecipe getRecipeForStack(ItemStack itemStack) {
		for(Map.Entry<Ingredient, CrystallarieumRecipe> entry : ingredientMap.entrySet()) {
			if(entry.getKey().test(itemStack)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	@Nullable
	public static CrystallarieumRecipe getRecipeForState(BlockState state) {
		return stateMap.getOrDefault(state, null);
	}
	
	public static void clearCache() {
		ingredientMap.clear();
		stateMap.clear();
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return this.inputIngredient.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		List<BlockState> states = getBlockStates();
		return states.get(states.size()-1).getBlock().asItem().getDefaultStack();
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.CRYSTALLARIEUM);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.CRYSTALLARIEUM_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.CRYSTALLARIEUM;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CrystallarieumRecipe crystallarieumRecipe) {
			return crystallarieumRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Nullable
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.crystallarieum_recipe_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.crystallarieum_recipes_unlocked.title");
	}
	
	public List<BlockState> getBlockStates() {
		return this.growthStages;
	}
	
	public Ingredient getIngredientStack() {
		return this.inputIngredient;
	}
	
	public Optional<CrystallarieumCatalyst> getCatalyst(ItemStack itemStack) {
		for(CrystallarieumCatalyst catalyst : this.catalysts) {
			if(catalyst.ingredient.test(itemStack)) {
				return Optional.of(catalyst);
			}
		}
		return Optional.empty();
	}
	
	public List<BlockState> getGrowthStages() {
		return growthStages;
	}
	
	public int getSecondsPerGrowthStage() {
		return secondsPerGrowthStage;
	}
	
	public InkColor getInkColor() {
		return inkColor;
	}
	
	public int getInkPerSecond() {
		return inkPerSecond;
	}
	
	public boolean growsWithoutCatalyst() {
		return growsWithoutCatalyst;
	}
	
	public List<CrystallarieumCatalyst> getCatalysts() {
		return this.catalysts;
	}
}
