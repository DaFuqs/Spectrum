package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {
	
	public static final int BASE_POTION_COUNT_ON_BREWING = 3;
	public static final int ARROW_COUNT_MULTIPLIER = 8;
	
	/**
	 * When potionMod.potentDecreasingEffect is set each status effect is split into separate
	 * instances defined in this list. First value is the new effects new potency mod, second the duration
	 */
	protected static final List<Pair<Float, Float>> SPLIT_EFFECT_POTENCY_AND_DURATION = new ArrayList<>() {{
		add(new Pair<>(1.667F, 0.1F));
		add(new Pair<>(0.75F, 0.334F));
		add(new Pair<>(0.25F, 0.667F));
	}};
	
	public static final Map<StatusEffect, StatusEffect> negativeToPositiveEffect = new HashMap<>() {{
		put(StatusEffects.BAD_OMEN, StatusEffects.HERO_OF_THE_VILLAGE);
		put(StatusEffects.HUNGER, StatusEffects.SATURATION);
		put(StatusEffects.INSTANT_DAMAGE, StatusEffects.INSTANT_HEALTH);
		put(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE);
		put(StatusEffects.SLOWNESS, StatusEffects.SPEED);
		put(StatusEffects.UNLUCK, StatusEffects.LUCK);
		put(StatusEffects.WEAKNESS, StatusEffects.STRENGTH);
		put(StatusEffects.WITHER, StatusEffects.REGENERATION);
		put(SpectrumStatusEffects.STIFFNESS, SpectrumStatusEffects.SWIFTNESS);
		put(SpectrumStatusEffects.DENSITY, SpectrumStatusEffects.LIGHTWEIGHT);
	}};
	
	public static @Nullable PotionWorkshopBrewingRecipe getPositiveRecipe(@NotNull StatusEffect statusEffect) {
		if (statusEffect.getCategory() == StatusEffectCategory.HARMFUL) {
			StatusEffect positiveEffect = negativeToPositiveEffect.getOrDefault(statusEffect, null);
			if (positiveEffect == null) {
				return null;
			}
			for (PotionWorkshopBrewingRecipe positiveRecipe : positiveRecipes) {
				if (positiveRecipe.recipeData.statusEffect() == positiveEffect) {
					return positiveRecipe;
				}
			}
		}
		return null;
	}
	
	public static final List<PotionWorkshopBrewingRecipe> positiveRecipes = new ArrayList<>();
	public static final List<PotionWorkshopBrewingRecipe> negativeRecipes = new ArrayList<>();
	
	public final PotionRecipeEffect recipeData;
	
	protected ItemStack cachedOutput;
	
	public PotionWorkshopBrewingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, int craftingTime,
									   IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3, PotionRecipeEffect recipeData) {
		
		super(id, group, secret, requiredAdvancementIdentifier, craftingTime, recipeData.statusEffect().getColor(), ingredient1, ingredient2, ingredient3);
		this.recipeData = recipeData;
		
		registerInToastManager(getType(), this);
		
		// remember one of each status effect recipe for quick lookup
		if (recipeData.statusEffect().getCategory() == StatusEffectCategory.BENEFICIAL) {
			for (PotionWorkshopBrewingRecipe ae : positiveRecipes) {
				if (ae.recipeData.statusEffect() == recipeData.statusEffect()) {
					return;
				}
			}
			positiveRecipes.add(this);
		} else if (recipeData.statusEffect().getCategory() == StatusEffectCategory.HARMFUL) {
			for (PotionWorkshopBrewingRecipe ae : negativeRecipes) {
				if (ae.recipeData.statusEffect() == recipeData.statusEffect()) {
					return;
				}
			}
			negativeRecipes.add(this);
		}
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return recipeData.applicableToPotions() && itemStack.isOf(Items.GLASS_BOTTLE)
				|| recipeData.applicableToTippedArrows() && itemStack.isOf(Items.ARROW)
				|| itemStack.getItem() instanceof InkPoweredPotionFillable fillable && ((fillable.isWeapon() && recipeData.applicableToWeapons()) || recipeData.applicableToPotionFillabes());
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING;
	}
	
	@Override
	public boolean usesReagents() {
		return true;
	}
	
	@Override
	public int getMinOutputCount(ItemStack baseItemStack) {
		return baseItemStack.isOf(Items.GLASS_BOTTLE) ? BASE_POTION_COUNT_ON_BREWING : 1;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(IngredientStack.ofStacks(SpectrumItems.MERMAIDS_GEM.getDefaultStack()));
		defaultedList.add(IngredientStack.ofStacks(Items.GLASS_BOTTLE.getDefaultStack()));
		addIngredientStacks(defaultedList);
		return defaultedList;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		if (this.cachedOutput == null) {
			this.cachedOutput = getPotion(Items.GLASS_BOTTLE.getDefaultStack(), Items.POTION.getDefaultStack(), new PotionMod(), null, Random.create());
		}
		return this.cachedOutput;
	}
	
	@Override
	public ItemStack craft(Inventory inventory, DynamicRegistryManager drm) {
		ItemStack stack = new ItemStack(Items.POTION);
		PotionUtil.setCustomPotionEffects(stack, List.of(new StatusEffectInstance(recipeData.statusEffect(), recipeData.baseDurationTicks())));
		return stack;
	}
	
	public float getModifiedYield(PotionMod potionMod) {
		return recipeData.baseYield() + potionMod.yield;
	}
	
	public List<ItemStack> getPotions(ItemStack stack, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random, int brewedAmount) {
		// potion type
		ItemStack itemStack;
		if (potionMod.makeSplashing) {
			if (potionMod.makeLingering) {
				itemStack = new ItemStack(Items.LINGERING_POTION);
				if (potionMod.negateDecreasingDuration) {
					potionMod.durationMultiplier += 3;
				}
			} else {
				itemStack = new ItemStack(Items.SPLASH_POTION);
			}
		} else {
			itemStack = new ItemStack(Items.POTION);
		}
		
		List<ItemStack> results = new ArrayList<>();
		for (int i = 0; i < brewedAmount; i++) {
			results.add(getPotion(stack, itemStack.copy(), potionMod, lastRecipe, random));
		}
		
		return results;
	}
	
	public ItemStack getPotion(ItemStack originalStack, ItemStack targetStack, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = generateEffects(originalStack, potionMod, lastRecipe, random);
		
		// apply to potion
		if (effects.isEmpty()) {
			// no effects: thick potion
			PotionUtil.setPotion(targetStack, Potions.THICK);
		} else {
			PotionUtil.setPotion(targetStack, SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(targetStack, potionMod, effects);
		}
		
		if (potionMod.additionalDrinkDurationTicks != 0) {
			NbtCompound compound = targetStack.getOrCreateNbt();
			targetStack.setNbt(compound);
		}
		
		return targetStack;
	}
	
	public ItemStack getTippedArrows(ItemStack stack, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, int amount, Random random) {
		if (potionMod.negateDecreasingDuration) {
			potionMod.durationMultiplier += 7;
		}
		List<InkPoweredStatusEffectInstance> effects = generateEffects(stack, potionMod, lastRecipe, random);
		
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW, amount);
		if (effects.isEmpty()) {
			PotionUtil.setPotion(itemStack, Potions.THICK);
		} else {
			PotionUtil.setPotion(itemStack, SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(itemStack, potionMod, effects);
		}
		
		return itemStack;
	}
	
	public void fillPotionFillable(ItemStack stack, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		if (stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			List<InkPoweredStatusEffectInstance> effects = generateEffects(stack, potionMod, lastRecipe, random);
			inkPoweredPotionFillable.addOrUpgradeEffects(stack, effects);
		}
	}
	
	private static void setCustomPotionEffects(ItemStack stack, PotionMod potionMod, List<InkPoweredStatusEffectInstance> effects) {
		List<StatusEffectInstance> instances = new ArrayList<>();
		for (InkPoweredStatusEffectInstance e : effects) {
			instances.add(e.getStatusEffectInstance());
		}
		PotionUtil.setCustomPotionEffects(stack, instances);
		for (InkPoweredStatusEffectInstance effect : effects) {
			if (effect.getColor() != -1) {
				NbtCompound nbtCompound = stack.getOrCreateNbt();
				nbtCompound.putInt("CustomPotionColor", effect.getColor());
			}
		}
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (potionMod.unidentifiable) {
			nbtCompound.putBoolean("spectrum_unidentifiable", true); // used in PotionItemMixin
		}
	}
	
	private List<InkPoweredStatusEffectInstance> generateEffects(ItemStack baseIngredient, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = new ArrayList<>();
		
		addEffect(potionMod, random, effects); // main effect
		addLastEffect(baseIngredient, potionMod, lastRecipe, random, effects);
		addAdditionalEffects(baseIngredient, potionMod, random, effects);
		addRandomEffects(baseIngredient, potionMod, random, effects);
		
		// split durations, if set
		if (potionMod.potentDecreasingEffect) {
			effects = applyPotentDecreasingEffect(effects, random);
		}
		
		return effects;
	}
	
	private static void addLastEffect(ItemStack baseIngredient, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (lastRecipe != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect) && lastRecipe.recipeData.isApplicableTo(baseIngredient, potionMod)) {
			PotionMod lastEffectMod = new PotionMod();
			lastEffectMod.potencyMultiplier = potionMod.lastEffectPotencyMultiplier;
			lastEffectMod.durationMultiplier = potionMod.lastEffectDurationMultiplier;
			lastEffectMod.modifyFrom(potionMod);
			lastRecipe.addEffect(lastEffectMod, random, effects);
		}
	}
	
	private static void addAdditionalEffects(ItemStack baseIngredient, PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		for (Pair<PotionRecipeEffect, Float> entry : potionMod.additionalEffects) {
			if (random.nextFloat() < entry.getRight() && entry.getLeft().isApplicableTo(baseIngredient, potionMod)) {
				InkPoweredStatusEffectInstance statusEffectInstance = entry.getLeft().getStatusEffectInstance(potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
	}
	
	private void addEffect(PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (potionMod.makeEffectsPositive) {
			PotionWorkshopBrewingRecipe positiveRecipe = getPositiveRecipe(recipeData.statusEffect());
			if (positiveRecipe != null) {
				effects.add(positiveRecipe.recipeData.getStatusEffectInstance(potionMod, random));
				return;
			}
		}
		
		InkPoweredStatusEffectInstance statusEffectInstance = recipeData.getStatusEffectInstance(potionMod, random);
		if (statusEffectInstance != null) {
			effects.add(statusEffectInstance);
		}
	}
	
	private void addRandomEffects(ItemStack baseIngredient, PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		// random positive ones
		int additionalPositiveEffectCount = Support.getIntFromDecimalWithChance(potionMod.additionalRandomPositiveEffectCount, random);
		if (additionalPositiveEffectCount > 0) {
			List<PotionWorkshopBrewingRecipe> randomlySelectedRecipes = pullRandomMatchingRecipes(positiveRecipes, additionalPositiveEffectCount, effects, baseIngredient);
			for (PotionWorkshopBrewingRecipe recipe : randomlySelectedRecipes) {
				InkPoweredStatusEffectInstance statusEffectInstance = recipe.recipeData.getStatusEffectInstance(potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
		
		// random negative ones
		int additionalNegativeEffectCount = Support.getIntFromDecimalWithChance(potionMod.additionalRandomNegativeEffectCount, random);
		if (additionalNegativeEffectCount > 0) {
			List<PotionWorkshopBrewingRecipe> randomlySelectedRecipes = pullRandomMatchingRecipes(potionMod.makeEffectsPositive ? positiveRecipes : negativeRecipes, additionalNegativeEffectCount, effects, baseIngredient);
			for (PotionWorkshopBrewingRecipe recipe : randomlySelectedRecipes) {
				InkPoweredStatusEffectInstance statusEffectInstance = recipe.recipeData.getStatusEffectInstance(potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
	}
	
	private List<PotionWorkshopBrewingRecipe> pullRandomMatchingRecipes(List<PotionWorkshopBrewingRecipe> list, int amount, List<InkPoweredStatusEffectInstance> effects, ItemStack baseIngredient) {
		List<PotionWorkshopBrewingRecipe> results = new ArrayList<>();
		List<PotionWorkshopBrewingRecipe> shuffledPositiveRecipes = new ArrayList<>(list);
		Collections.shuffle(shuffledPositiveRecipes);
		
		int i = 0;
		for (PotionWorkshopBrewingRecipe recipe : shuffledPositiveRecipes) {
			if (i == amount) {
				break;
			}
			if (!recipe.isValidBaseIngredient(baseIngredient)) {
				continue;
			}
			if (containsEffect(effects, recipe.recipeData.statusEffect())) {
				continue;
			}
			results.add(recipe);
			i++;
		}
		
		return results;
	}
	
	private boolean containsEffect(List<InkPoweredStatusEffectInstance> effects, StatusEffect statusEffect) {
		for (InkPoweredStatusEffectInstance existingInstance : effects) {
			if (existingInstance.getStatusEffectInstance().getEffectType() == statusEffect) {
				return true;
			}
		}
		return false;
	}
	
	private List<InkPoweredStatusEffectInstance> applyPotentDecreasingEffect(@NotNull List<InkPoweredStatusEffectInstance> statusEffectInstances, Random random) {
		List<InkPoweredStatusEffectInstance> splitInstances = new ArrayList<>();
		
		for (InkPoweredStatusEffectInstance poweredInstance : statusEffectInstances) {
			StatusEffectInstance instance = poweredInstance.getStatusEffectInstance();
			
			// instant effects, like harming do not get split (that would apply harming 3x
			if (instance.getEffectType().isInstant()) {
				splitInstances.add(poweredInstance);
				continue;
			}
			
			for (Pair<Float, Float> mods : SPLIT_EFFECT_POTENCY_AND_DURATION) {
				int newDuration = (int) (instance.getDuration() * mods.getRight());
				int newAmplifier = Support.getIntFromDecimalWithChance(instance.getAmplifier() * mods.getLeft(), random);
				if (newAmplifier >= 0) {
					splitInstances.add(new InkPoweredStatusEffectInstance(new StatusEffectInstance(instance.getEffectType(), newDuration, newAmplifier, instance.isAmbient(), instance.shouldShowParticles()), poweredInstance.getInkCost(), poweredInstance.getColor(), poweredInstance.isUnidentifiable(), poweredInstance.isIncurable()));
				}
			}
		}
		
		return splitInstances;
	}
	
	public StatusEffect getStatusEffect() {
		return this.recipeData.statusEffect();
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING_ID;
	}
	
}
