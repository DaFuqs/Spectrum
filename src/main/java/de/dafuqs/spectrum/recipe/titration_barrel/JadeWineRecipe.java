package de.dafuqs.spectrum.recipe.titration_barrel;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.food.JadeWineItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class JadeWineRecipe implements ITitrationBarrelRecipe {
	
	public static final RecipeSerializer<JadeWineRecipe> SERIALIZER = new SpecialRecipeSerializer<>(JadeWineRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("milestones/unlock_spawner_manipulation");
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.GERMINATED_JADE_VINE_SEEDS)));
		add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.JADE_VINE_PETALS)));
	}};
	
	public final Identifier identifier;
	
	public JadeWineRecipe(Identifier identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		int bulbCount = ITitrationBarrelRecipe.getCountInInventory(content, SpectrumItems.GERMINATED_JADE_VINE_SEEDS);
		int petalCount = ITitrationBarrelRecipe.getCountInInventory(content, SpectrumItems.JADE_VINE_PETALS);
		boolean sweetened = ITitrationBarrelRecipe.getCountInInventory(content, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		int yield = ITitrationBarrelRecipe.getYieldBottles(waterBuckets, secondsFermented, temperature);
		
		double bloominess = getBloominess(bulbCount, petalCount);
		double thickness = getThickness(bulbCount, petalCount, waterBuckets);
		double alcPercent = getAlcPercent(secondsFermented, downfall, bloominess, thickness);
		List<StatusEffectInstance> statusEffects = new ArrayList<>();
		
		ItemStack stack = new JadeWineItem.JadeWineBeverageProperties(secondsFermented, (float) alcPercent, (float) thickness, statusEffects, (float) bloominess, sweetened).getStack();
		stack.setCount(yield);
		
		return stack;
	}
	
	// bloominess reduces the possibility of negative effects to trigger (better on the tounge)
	// but also reduces the potency of positive effects a bit
	protected static double getBloominess(int bulbCount, int petalCount) {
		if(bulbCount == 0) {
			return 0;
		}
		return (double) petalCount / (double) bulbCount / 2F;
	}
	
	// the amount of solid to liquid
	// adding more water will increase the amount of bottles the player can harvest from the barrel
	// but too much water will - who would have thought - water it down
	protected static double getThickness(int bulbCount, int petalCount, int waterCount) {
		if(waterCount == 0) {
			return 0;
		}
		return (double) waterCount / (bulbCount + petalCount / 8F);
	}
	
	// the alc % determines the power of effects when drunk
	// it generally increases the longer the wine has fermented
	//
	// another detail: the more rainy the weather (downfall) the more water evaporates
	// in contrast to alcohol, making the drink stronger / weaker in return
	protected static double getAlcPercent(long secondsFermented, float downfall, double bloominess, double thickness) {
		float minecraftDaysFermented = ITitrationBarrelRecipe.minecraftDaysFromSeconds(secondsFermented);
		return Support.logBase(1.08, thickness * minecraftDaysFermented * (0.5D + downfall / 2)) - bloominess;
	}
	
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	public List<IngredientStack> getIngredientStacks() {
		return INGREDIENT_STACKS;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		boolean bulbsFound = false;
		
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if(stack.isOf(SpectrumItems.GERMINATED_JADE_VINE_SEEDS)) {
				bulbsFound = true;
			} else if(!stack.isOf(SpectrumItems.JADE_VINE_PETALS) && !stack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
				return false;
			}
		}
		
		return bulbsFound;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getOutput() {
		return null;
	}
	
	@Override
	public Identifier getId() {
		return identifier;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
