package de.dafuqs.spectrum.recipe.crystallarieum;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumRecipe extends GatedSpectrumRecipe {

	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_crystallarieum");

	protected final Ingredient inputIngredient;
	protected final List<BlockState> growthStages;
	protected final int secondsPerGrowthStage;
	protected final InkColor inkColor;
	protected final int inkPerSecond;
	protected final boolean growsWithoutCatalyst;
	protected final List<CrystallarieumCatalyst> catalysts;
	protected final List<ItemStack> additionalOutputs; // these aren't actual outputs, but recipe managers will treat it as such, showing this recipe as a way to get them. Use for drops of the growth blocks, for example

	protected final static Map<Ingredient, CrystallarieumRecipe> ingredientMap = new HashMap<>();
	protected final static Map<BlockState, CrystallarieumRecipe> stateMap = new HashMap<>();

	public CrystallarieumRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, List<BlockState> growthStages, int secondsPerGrowthStage, InkColor inkColor, int inkPerSecond, boolean growsWithoutCatalyst, List<CrystallarieumCatalyst> catalysts, List<ItemStack> additionalOutputs) {
		super(id, group, secret, requiredAdvancementIdentifier);

		this.inputIngredient = inputIngredient;
		this.growthStages = growthStages;
		this.secondsPerGrowthStage = secondsPerGrowthStage;
		this.inkColor = inkColor;
		this.inkPerSecond = inkPerSecond;
		this.growsWithoutCatalyst = growsWithoutCatalyst;
		this.catalysts = catalysts;
		this.additionalOutputs = additionalOutputs;
		
		ingredientMap.put(inputIngredient, this);
		for (BlockState growthStage : growthStages) {
			stateMap.put(growthStage, this);
		}
		
		registerInToastManager(getType(), this);
	}
	
	@Nullable
	public static CrystallarieumRecipe getRecipeForStack(ItemStack itemStack) {
		for (Map.Entry<Ingredient, CrystallarieumRecipe> entry : ingredientMap.entrySet()) {
			if (entry.getKey().test(itemStack)) {
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
		List<BlockState> states = getGrowthStages();
		return states.get(states.size() - 1).getBlock().asItem().getDefaultStack();
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.CRYSTALLARIEUM);
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
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.CRYSTALLARIEUM_ID;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	public Ingredient getIngredientStack() {
		return this.inputIngredient;
	}
	
	public Optional<CrystallarieumCatalyst> getCatalyst(ItemStack itemStack) {
		for (CrystallarieumCatalyst catalyst : this.catalysts) {
			if (catalyst.ingredient.test(itemStack)) {
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

	public List<ItemStack> getAdditionalOutputs() {
		return additionalOutputs;
	}

}
