package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionMod;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {
	
	public static final List<StatusEffect> availablePositiveEffects = new ArrayList<>();
	public static final List<Integer> availablePositiveEffectDurations = new ArrayList<>();
	public static final List<Float> availablePositiveEffectPotencyMods = new ArrayList<>();
	public static final List<StatusEffect> availableNegativeEffects = new ArrayList<>();
	public static final List<Integer> availableNegativeEffectDurations = new ArrayList<>();
	public static final List<Float> availableNegativeEffectPotencyMods = new ArrayList<>();
	
	protected final StatusEffect statusEffect;
	protected final int baseDurationTicks;
	protected final float potencyModifier;
	
	protected final boolean applicableToPotions; // TODO: USE
	protected final boolean applicableToTippedArrows; // TODO: USE

	public PotionWorkshopBrewingRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, boolean applicableToPotions, boolean applicableToTippedArrows, Identifier requiredAdvancementIdentifier) {
		super(id, group, baseIngredient, consumeBaseIngredient, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.statusEffect = statusEffect;
		this.baseDurationTicks = baseDurationTicks;
		this.potencyModifier = potencyModifier;
		this.applicableToPotions = applicableToPotions;
		this.applicableToTippedArrows = applicableToTippedArrows;
		
		if(statusEffect.isBeneficial()) {
			availablePositiveEffects.add(statusEffect);
			availablePositiveEffectDurations.add(baseDurationTicks);
			availablePositiveEffectPotencyMods.add(potencyModifier);
		} else {
			availableNegativeEffects.add(statusEffect);
			availableNegativeEffectDurations.add(baseDurationTicks);
			availableNegativeEffectPotencyMods.add(potencyModifier);
		}
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING;
	}
	
	@Override
	public boolean usesReagents() {
		return true;
	}
	
	@Override
	public int getMinOutputCount() {
		return 3;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
	public ItemStack getRandomPotion(PotionMod potionMod, StatusEffect lastBrewedStatusEffect, Random random) {
		  /*
			* public int flatDurationBonusTicks = 0;
			* public float flatPotencyBonus = 0.0F;
			* public float multiplicativeDurationBonus = 0.0F;
			* public float multiplicativePotencyBonus = 0.0F;
			* public float flatPotencyBonusPositiveEffects = 0.0F;
			* public float flatPotencyBonusNegativeEffects = 0.0F;
			* public int additionalRandomPositiveEffectCount = 0;
			* public int additionalRandomNegativeEffectCount = 0;
			public float chanceToAddLastEffect = 0.0F;
			public float lastEffectPotencyMod = 0.0F;
			public boolean makeSplashing = false;
			public boolean makeLingering = false;
			* public boolean noParticles = false;
			public boolean unidentifiable = false;
			public boolean makeEffectsPositive = false;
			public boolean potentDecreasingEffect = false;
			* public boolean negateDecreasingDuration = false;
		 */
		
		List<StatusEffectInstance> effects = new ArrayList<>();
		// the main effect
		effects.add(getStatusEffectInstance(this.statusEffect, this.baseDurationTicks, this.potencyModifier, potionMod, random));
		
		// random positive ones
		if(availablePositiveEffects.size() > 0) {
			int additionalPositiveEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomPositiveEffectCount, random);
			for (int i = 0; i < additionalPositiveEffects; i++) {
				int r = random.nextInt(availablePositiveEffects.size());
				effects.add(getStatusEffectInstance(availablePositiveEffects.get(r), availablePositiveEffectDurations.get(r), availablePositiveEffectPotencyMods.get(r), potionMod, random));
			}
		}
		
		// random negative ones
		if(availableNegativeEffects.size() > 0) {
			int additionalNegativeEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomNegativeEffectCount, random);
			for (int i = 0; i < additionalNegativeEffects; i++) {
				int r = random.nextInt(availableNegativeEffects.size());
				effects.add(getStatusEffectInstance(availableNegativeEffects.get(r), availableNegativeEffectDurations.get(r), availableNegativeEffectPotencyMods.get(r), potionMod, random));
			}
		}
		
		// last effect
		if(lastBrewedStatusEffect != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect)) {
			// TODO
			//effects.add(getStatusEffectInstance(lastBrewedStatusEffect, lastBrewedStatusEffect , potionMod, random));
		}
		
		// split durations, if set
		return ItemStack.EMPTY;
	}
	
	public ItemStack getRandomTippedArrows(int arrowAmount, PotionMod potionMod, StatusEffect lastBrewedStatusEffect, Random random) {
		return ItemStack.EMPTY;
	}
	
	public StatusEffectInstance getStatusEffectInstance(@NotNull StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, @NotNull PotionMod potionMod, Random random) {
		float typeDurationMod = 1.0F;
		if(potionMod.makeSplashing) {
			if(potionMod.makeLingering) {
				typeDurationMod = potionMod.negateDecreasingDuration ? 0.25F : 0.5F;
			} else {
				typeDurationMod = potionMod.negateDecreasingDuration ? 0.5F : 1.0F;
			}
		}
		
		int durationTicks = (int) (((this.baseDurationTicks * potionMod.multiplicativeDurationBonus) + potionMod.flatDurationBonusTicks) * typeDurationMod);
		
		float posNegBonus = potionMod.flatPotencyBonusNegativeEffects;
		if(statusEffect.isBeneficial()) {
			posNegBonus = potionMod.flatPotencyBonusPositiveEffects;
		}
		int potency = Support.getIntFromDecimalWithChance( (1 + this.potencyModifier) * potionMod.multiplicativePotencyBonus + potionMod.flatPotencyBonus + posNegBonus, random);
		
		return new StatusEffectInstance(this.statusEffect, durationTicks, potency, !potionMod.noParticles, !potionMod.noParticles);
	}
	
}
