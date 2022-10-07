package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuspiciousBrewRecipe implements ITitrationBarrelRecipe {
	
	public static final RecipeSerializer<SuspiciousBrewRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SuspiciousBrewRecipe::new);
	public static final Ingredient TAPPING_STACK = Ingredient.ofStacks(Items.GLASS_BOTTLE.getDefaultStack());
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_suspicious_brew");
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
	}};
	
	public final Identifier identifier;
	
	public SuspiciousBrewRecipe(Identifier identifier) {
		this.identifier = identifier;
		registerInToastManager(SpectrumRecipeTypes.TITRATION_BARREL, this);
	}
	
	private Optional<Pair<StatusEffect, Integer>> getStewEffectFrom(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (block instanceof FlowerBlock flowerBlock) {
				return Optional.of(Pair.of(flowerBlock.getEffectInStew(), flowerBlock.getEffectInStewDuration()));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		int yield = ITitrationBarrelRecipe.getYieldBottles(waterBuckets, secondsFermented, temperature);
		ItemStack stack = getBrew(content, waterBuckets, secondsFermented, downfall);
		stack.setCount(yield);
		return stack;
	}
	
	@Override
	public Ingredient getTappingIngredient() {
		return TAPPING_STACK;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return INGREDIENT_STACKS;
	}
	
	@Override
	public int getMinFermentationTimeHours() {
		return 4;
	}
	
	@Override
	public TitrationBarrelRecipe.FermentationData getFermentationData() {
		return new TitrationBarrelRecipe.FermentationData(1.0F, List.of());
	}
	
	// every real-life day the effect gets a potency of + 1, but duration get's a 25 % hit
	protected static ItemStack getBrew(DefaultedList<ItemStack> content, int waterBuckets, long ticksFermented, float downfall) {
		ItemStack stack = SpectrumItems.SUSPICIOUS_BREW.getDefaultStack();
		//TODO brew effects
		return stack;
	}
	
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if(!stack.isIn(ItemTags.SMALL_FLOWERS)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getOutput() {
		return SpectrumItems.SUSPICIOUS_BREW.getDefaultStack();
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
