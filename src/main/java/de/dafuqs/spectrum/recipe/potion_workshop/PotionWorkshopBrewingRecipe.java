package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {
	
	public static final int BASE_POTION_COUNT_ON_BREWING = 3;
	public static final int BASE_ARROW_COUNT_ON_BREWING = 12;
	
	/**
	 * When potionMod.potentDecreasingEffect is set each status effect is split into separate
	 * instances defined in this list. First value is the new effects new potency mod, second the duration
	 */
	protected static final List<Pair<Float, Float>> SPLIT_EFFECT_POTENCY_AND_DURATION = new ArrayList<>() {{
		add(new Pair<>(2.0F, 0.15F));
		add(new Pair<>(0.75F, 0.5F));
		add(new Pair<>(0.25F, 1.0F));
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
				if (positiveRecipe.statusEffect == positiveEffect) {
					return positiveRecipe;
				}
			}
		}
		return null;
	}
	
	public static final List<PotionWorkshopBrewingRecipe> positiveRecipes = new ArrayList<>();
	public static final List<PotionWorkshopBrewingRecipe> negativeRecipes = new ArrayList<>();
	
	protected final StatusEffect statusEffect;
	protected final int baseDurationTicks;
	protected final float potencyModifier;
	
	protected final boolean applicableToPotions;
	protected final boolean applicableToTippedArrows;
	protected final boolean applicableToPotionFillabes;
	
	protected final InkColor inkColor;
	protected final int inkAmount;
	
	protected ItemStack cachedOutput;
	
	public PotionWorkshopBrewingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
									   int craftingTime, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect,
									   int baseDurationTicks, float potencyModifier, boolean applicableToPotions, boolean applicableToTippedArrows, boolean applicableToPotionFillabes, InkColor inkColor, int inkAmount) {
		
		super(id, group, secret, requiredAdvancementIdentifier, craftingTime, statusEffect.getColor(), ingredient1, ingredient2, ingredient3);
		this.statusEffect = statusEffect;
		this.baseDurationTicks = baseDurationTicks;
		this.potencyModifier = potencyModifier;
		this.applicableToPotions = applicableToPotions;
		this.applicableToTippedArrows = applicableToTippedArrows;
		this.applicableToPotionFillabes = applicableToPotionFillabes;
		this.inkColor = inkColor;
		this.inkAmount = inkAmount;
		
		registerInToastManager(getType(), this);
		
		// remember one of each status effect recipe for quick lookup
		if (statusEffect.getCategory() == StatusEffectCategory.BENEFICIAL) {
			for (PotionWorkshopBrewingRecipe ae : positiveRecipes) {
				if (ae.statusEffect == statusEffect) {
					return;
				}
			}
			positiveRecipes.add(this);
		} else if (statusEffect.getCategory() == StatusEffectCategory.HARMFUL) {
			for (PotionWorkshopBrewingRecipe ae : negativeRecipes) {
				if (ae.statusEffect == statusEffect) {
					return;
				}
			}
			negativeRecipes.add(this);
		}
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return (applicableToPotions && itemStack.isOf(Items.GLASS_BOTTLE)) || (applicableToTippedArrows && itemStack.isOf(Items.ARROW)) || (applicableToPotionFillabes && itemStack.getItem() instanceof InkPoweredPotionFillable);
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
	
	public boolean isApplicableToPotions() {
		return applicableToPotions;
	}
	
	public boolean isApplicableToTippedArrows() {
		return applicableToTippedArrows;
	}
	
	public boolean isApplicableToPotionFillabes() {
		return applicableToPotionFillabes;
	}
	
	@Override
	public int getMinOutputCount(ItemStack baseItemStack) {
		return baseItemStack.isOf(Items.GLASS_BOTTLE) ? BASE_POTION_COUNT_ON_BREWING : 1;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofStacks(SpectrumItems.MERMAIDS_GEM.getDefaultStack()));
		defaultedList.add(Ingredient.ofStacks(Items.GLASS_BOTTLE.getDefaultStack()));
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}
	
	@Override
	public ItemStack getOutput() {
		if (this.cachedOutput == null) {
			this.cachedOutput = getPotion(new PotionMod(), null, Random.create());
		}
		return this.cachedOutput;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		ItemStack stack = new ItemStack(Items.POTION);
		PotionUtil.setCustomPotionEffects(stack, List.of(new StatusEffectInstance(this.statusEffect, this.baseDurationTicks)));
		return stack;
	}
	
	public ItemStack getPotion(PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = generateEffects(potionMod, lastRecipe, random);
		
		// potion type
		ItemStack itemStack;
		if (potionMod.makeSplashing) {
			itemStack = potionMod.makeLingering ? new ItemStack(Items.LINGERING_POTION) : new ItemStack(Items.SPLASH_POTION);
		} else {
			itemStack = new ItemStack(Items.POTION);
		}
		
		// apply to potion
		if (effects.size() == 0) {
			// no effects: thick potion
			PotionUtil.setPotion(itemStack, Potions.THICK);
		} else {
			PotionUtil.setPotion(itemStack, SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(itemStack, effects);
		}
		
		if (potionMod.additionalDrinkDurationTicks != 0) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			itemStack.setNbt(compound);
		}
		
		return itemStack;
	}
	
	public ItemStack getTippedArrows(PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, int amount, Random random) {
		List<InkPoweredStatusEffectInstance> effects = generateEffects(potionMod, lastRecipe, random);
		
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW, amount);
		if (effects.size() == 0) {
			PotionUtil.setPotion(itemStack, Potions.THICK);
		} else {
			PotionUtil.setPotion(itemStack, SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(itemStack, effects);
		}
		
		return itemStack;
	}
	
	public void fillPotionFillable(ItemStack potionFillableStack, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		if (potionFillableStack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			List<InkPoweredStatusEffectInstance> effects = generateEffects(potionMod, lastRecipe, random);
			inkPoweredPotionFillable.addOrUpgradeEffects(potionFillableStack, effects);
		}
	}
	
	private static void setCustomPotionEffects(ItemStack stack, List<InkPoweredStatusEffectInstance> effects) {
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
	}
	
	private List<InkPoweredStatusEffectInstance> generateEffects(PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = new ArrayList<>();
		
		addEffect(potionMod, random, effects); // main effect
		addLastEffect(potionMod, lastRecipe, random, effects);
		addRandomEffects(potionMod, random, effects);
		// split durations, if set
		if (potionMod.potentDecreasingEffect) {
			effects = applyPotentDecreasingEffect(effects, random);
		}
		return effects;
	}
	
	private static void addLastEffect(PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (lastRecipe != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect)) {
			PotionMod lastEffectMod = new PotionMod();
			lastEffectMod.potencyMultiplier = potionMod.lastEffectPotencyMultiplier;
			lastEffectMod.durationMultiplier = potionMod.lastEffectDurationMultiplier;
			lastEffectMod.modifyFrom(potionMod);
			lastRecipe.addEffect(lastEffectMod, random, effects);
		}
	}
	
	private void addEffect(PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (potionMod.makeEffectsPositive) {
			PotionWorkshopBrewingRecipe positiveRecipe = getPositiveRecipe(this.statusEffect);
			if (positiveRecipe != null) {
				effects.add(positiveRecipe.getStatusEffectInstance(potionMod, random));
				return;
			}
		}
		
		InkPoweredStatusEffectInstance statusEffectInstance = getStatusEffectInstance(potionMod, random);
		if (statusEffectInstance != null) {
			effects.add(statusEffectInstance);
		}
	}
	
	private void addRandomEffects(PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		// random positive ones
		if (positiveRecipes.size() > 0) {
			int additionalPositiveEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomPositiveEffectCount, random);
			for (int i = 0; i < additionalPositiveEffects; i++) {
				int r;
				int tries = 0;
				PotionWorkshopBrewingRecipe selectedStatusEffect;
				do {
					r = random.nextInt(positiveRecipes.size());
					selectedStatusEffect = positiveRecipes.get(r);
					if (containsEffect(effects, selectedStatusEffect.statusEffect)) {
						selectedStatusEffect = null;
						tries++;
					}
				} while (selectedStatusEffect == null && tries < 5);
				if (selectedStatusEffect != null) {
					InkPoweredStatusEffectInstance statusEffectInstance = getStatusEffectInstance(potionMod, random);
					if (statusEffectInstance != null) {
						effects.add(statusEffectInstance);
					}
				}
			}
		}
		
		// random negative ones
		if (negativeRecipes.size() > 0) {
			int additionalNegativeEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomNegativeEffectCount, random);
			for (int i = 0; i < additionalNegativeEffects; i++) {
				int r;
				int tries = 0;
				PotionWorkshopBrewingRecipe selectedRecipe;
				
				do {
					r = random.nextInt(negativeRecipes.size());
					selectedRecipe = negativeRecipes.get(r);
					
					if (potionMod.makeEffectsPositive) {
						selectedRecipe = this;
						PotionWorkshopBrewingRecipe positiveRecipe = getPositiveRecipe(this.statusEffect);
						if (positiveRecipe != null) {
							selectedRecipe = positiveRecipe;
						}
					}
					if (containsEffect(effects, selectedRecipe.statusEffect)) {
						selectedRecipe = null;
						tries++;
					}
				} while (selectedRecipe == null && tries < 5);
				if (selectedRecipe != null) {
					InkPoweredStatusEffectInstance statusEffectInstance = selectedRecipe.getStatusEffectInstance(potionMod, random);
					if (statusEffectInstance != null) {
						effects.add(statusEffectInstance);
					}
				}
			}
		}
	}
	
	private boolean containsEffect(List<InkPoweredStatusEffectInstance> effects, StatusEffect statusEffect) {
		for (InkPoweredStatusEffectInstance existingInstance : effects) {
			if (existingInstance.getStatusEffectInstance().getEffectType() == statusEffect) {
				return true;
			}
		}
		return false;
	}
	
	private void setColor(ItemStack itemStack, PotionMod potionMod, boolean potionWithNoEffects, Random random) {
		if (potionWithNoEffects) {
			return;
		}
		
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		
		nbtCompound.putInt("CustomPotionColor", color);
		if (potionMod.unidentifiable) {
			nbtCompound.putBoolean("spectrum_unidentifiable", true); // used in PotionItemMixin
		}
	}
	
	private @Nullable InkPoweredStatusEffectInstance getStatusEffectInstance(@NotNull PotionMod potionMod, Random random) {
		float potency = potionMod.flatPotencyBonus;
		int durationTicks = baseDurationTicks + potionMod.flatDurationBonusTicks;
		switch (statusEffect.getCategory()) {
			case BENEFICIAL -> {
				potency += potionMod.flatPotencyBonusPositiveEffects;
				durationTicks += potionMod.flatDurationBonusPositiveEffects;
			}
			case HARMFUL -> {
				potency += potionMod.flatPotencyBonusNegativeEffects;
				durationTicks += potionMod.flatDurationBonusNegativeEffects;
			}
		}
		durationTicks = statusEffect.isInstant() ? 1 : (int) (durationTicks * potionMod.durationMultiplier);
		
		if (this.potencyModifier == 0.0F) {
			potency = 0; // effects that only have 1 level, like night vision
		} else {
			potency = (((1 + potency) * potionMod.potencyMultiplier) - 1) * this.potencyModifier;
			potency = Support.getIntFromDecimalWithChance(potency, random);
			
			// if the result of the potency calculation was negative because of a very low recipe base potencyModifier
			// (not because the player was greedy and got mali because of low multiplicativePotencyModifier)
			// => set to 0 again
			if (potency < 0 && potionMod.potencyMultiplier == 0.0) {
				potency = 0;
			}
		}
		
		if (potency >= 0 && durationTicks > 0) {
			int effectColor = potionMod.getColor(random);
			return new InkPoweredStatusEffectInstance(new StatusEffectInstance(statusEffect, durationTicks, (int) potency, !potionMod.noParticles, !potionMod.noParticles), getInkCost(), effectColor);
		} else {
			// the effect is so borked that the effect would be too weak
			return null;
		}
	}
	
	private List<InkPoweredStatusEffectInstance> applyPotentDecreasingEffect(@NotNull List<InkPoweredStatusEffectInstance> statusEffectInstances, Random random) {
		List<InkPoweredStatusEffectInstance> splitInstances = new ArrayList<>();
		
		for (InkPoweredStatusEffectInstance poweredInstance : statusEffectInstances) {
			StatusEffectInstance instance = poweredInstance.getStatusEffectInstance();
			for (Pair<Float, Float> mods : SPLIT_EFFECT_POTENCY_AND_DURATION) {
				int newDuration = (int) (instance.getDuration() * mods.getRight());
				int newAmplifier = Support.getIntFromDecimalWithChance(instance.getAmplifier() * mods.getLeft(), random);
				if (newAmplifier > 0) {
					splitInstances.add(new InkPoweredStatusEffectInstance(new StatusEffectInstance(instance.getEffectType(), newDuration, newAmplifier, instance.isAmbient(), instance.shouldShowParticles()), poweredInstance.getInkCost(), poweredInstance.getColor()));
				}
			}
		}
		
		return splitInstances;
	}
	
	public StatusEffect getStatusEffect() {
		return this.statusEffect;
	}
	
	public InkCost getInkCost() {
		return new InkCost(inkColor, inkAmount);
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING_ID;
	}
	
}
