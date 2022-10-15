package de.dafuqs.spectrum.recipe.titration_barrel.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.items.beverages.properties.StatusEffectBeverageProperties;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class SuspiciousBrewRecipe extends TitrationBarrelRecipe {
	
	public static final RecipeSerializer<SuspiciousBrewRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SuspiciousBrewRecipe::new);
	public static final Ingredient TAPPING_INGREDIENT = Ingredient.ofStacks(Items.GLASS_BOTTLE.getDefaultStack());
	public static final int MIN_FERMENTATION_TIME_HOURS = 4;
	public static final ItemStack OUTPUT_STACK = SpectrumItems.SUSPICIOUS_BREW.getDefaultStack();
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_suspicious_brew");
	public static final List<IngredientStack> INGREDIENT_STACKS = new ArrayList<>() {{
		add(IngredientStack.of(Ingredient.fromTag(ItemTags.SMALL_FLOWERS)));
	}};
	
	public SuspiciousBrewRecipe(Identifier identifier) {
		super(identifier, "", INGREDIENT_STACKS, OUTPUT_STACK, TAPPING_INGREDIENT, MIN_FERMENTATION_TIME_HOURS, new TitrationBarrelRecipe.FermentationData(0.35F, List.of()), UNLOCK_IDENTIFIER);
	}
	
	@Override
	public ItemStack getOutput() {
		ItemStack flowerStack = Items.DANDELION.getDefaultStack();
		flowerStack.setCount(4);
		return tapWith(List.of(flowerStack), 1.0F, this.minFermentationTimeHours * 60L * 60L, 0.8F, 0.4F); // downfall & temperature are for plains
	}
	
	@Override
	public ItemStack tap(Inventory inventory, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		List<ItemStack> stacks = new ArrayList<>();
		int itemCount = 0;
		for(int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if(!stack.isEmpty()) {
				stacks.add(stack);
				itemCount += stack.getCount();
			}
		}
		float thickness = getThickness(waterBuckets, itemCount);
		return tapWith(stacks, thickness, secondsFermented, downfall, temperature);
	}
	
	public ItemStack tapWith(List<ItemStack> stacks, float thickness, long secondsFermented, float downfall, float temperature) {
		if(secondsFermented / 60 / 60 < this.minFermentationTimeHours) {
			return NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK;
		}
		
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		double alcPercent = getAlcPercent(thickness, downfall, ageIngameDays);
		if(alcPercent >= 100) {
			return PURE_ALCOHOL_STACK;
		} else {
			// add up all stew effects from the input stacks
			Map<StatusEffect, Integer> stewEffects = new HashMap<>();
			for(ItemStack stack : stacks) {
				Optional<Pair<StatusEffect, Integer>> stewEffect = getStewEffectFrom(stack);
				if(stewEffect.isPresent()) {
					StatusEffect effect = stewEffect.get().getLeft();
					int duration = stewEffect.get().getRight() * stack.getCount();
					if(stewEffects.containsKey(effect)) {
						stewEffects.put(effect, stewEffects.get(effect) + duration);
					} else {
						stewEffects.put(effect, duration);
					}
				}
			}
			
			// all 5 % alc the potency is increased by 1, but duration is decreased
			int potencyMod = (int) (alcPercent / 5D);
			List<StatusEffectInstance> finalStatusEffects = new ArrayList<>();
			for(Map.Entry<StatusEffect, Integer> entry : stewEffects.entrySet()) {
				int duration = Math.max(80, entry.getValue() / potencyMod);
				finalStatusEffects.add(new StatusEffectInstance(entry.getKey(), duration, potencyMod));
			}
			
			return new StatusEffectBeverageProperties((long) ageIngameDays, (int) alcPercent, thickness, finalStatusEffects).getStack(OUTPUT_STACK);
		}
	}
	
	// taken from SuspiciousStewItem
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
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
