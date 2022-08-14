package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PotionFillable;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.random.Random;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {
	
	public static final List<StatusEffect> availablePositiveEffects = new ArrayList<>();
	public static final List<Integer> availablePositiveEffectDurations = new ArrayList<>();
	public static final List<Float> availablePositiveEffectPotencyMods = new ArrayList<>();
	public static final List<StatusEffect> availableNegativeEffects = new ArrayList<>();
	public static final List<Integer> availableNegativeEffectDurations = new ArrayList<>();
	public static final List<Float> availableNegativeEffectPotencyMods = new ArrayList<>();
	/**
	 * When potionMod.potentDecreasingEffect is set each status effect is split into separate
	 * instances defined in this list. First value is the new effects new potency mod, second the duration
	 */
	protected static final List<Pair<Float, Float>> SPLIT_EFFECT_POTENCY_AND_DURATION = new ArrayList<>() {{
		add(new Pair<>(2.0F, 0.15F));
		add(new Pair<>(0.75F, 0.5F));
		add(new Pair<>(0.25F, 1.0F));
	}};
	protected final StatusEffect statusEffect;
	protected final int baseDurationTicks;
	protected final float potencyModifier;
	
	protected final boolean applicableToPotions;
	protected final boolean applicableToTippedArrows;
	protected final boolean applicableToPotionFillabes;
	
	protected ItemStack cachedOutput;
	
	public PotionWorkshopBrewingRecipe(Identifier id, String group, int craftingTime, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, int color, boolean applicableToPotions, boolean applicableToTippedArrows, boolean applicableToPotionFillabes, Identifier requiredAdvancementIdentifier) {
		super(id, group, craftingTime, color, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.statusEffect = statusEffect;
		this.baseDurationTicks = baseDurationTicks;
		this.potencyModifier = potencyModifier;
		this.applicableToPotions = applicableToPotions;
		this.applicableToTippedArrows = applicableToTippedArrows;
		this.applicableToPotionFillabes = applicableToPotionFillabes;
		
		if (statusEffect.isBeneficial()) {
			if (!availablePositiveEffects.contains(statusEffect)) {
				availablePositiveEffects.add(statusEffect);
				availablePositiveEffectDurations.add(baseDurationTicks);
				availablePositiveEffectPotencyMods.add(potencyModifier);
			}
		} else {
			if (!availableNegativeEffects.contains(statusEffect)) {
				availableNegativeEffects.add(statusEffect);
				availableNegativeEffectDurations.add(baseDurationTicks);
				availableNegativeEffectPotencyMods.add(potencyModifier);
			}
		}
		
		registerInToastManager(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, this);
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return (itemStack.isOf(Items.GLASS_BOTTLE) && applicableToPotions) || (itemStack.isOf(Items.ARROW) && applicableToTippedArrows) || itemStack.getItem() instanceof PotionFillable;
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
		return baseItemStack.isOf(Items.GLASS_BOTTLE) ? 3 : 1;
	}
	
	@Override
	public int getColor() {
		if (this.color == -1) {
			return this.statusEffect.getColor();
		} else {
			return this.color;
		}
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
			this.cachedOutput = brewRandomPotion(new PotionMod(), null, Random.create());
		}
		return this.cachedOutput;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
	public ItemStack brewRandomPotion(PotionMod potionMod, StatusEffect lastBrewedStatusEffect, Random random) {
		List<StatusEffectInstance> effects = new ArrayList<>();
		
		if (potionMod.makeSplashing && potionMod.makeLingering) {
			float typeDurationMod = potionMod.negateDecreasingDuration ? 1.0F : 0.25F;
			potionMod.flatDurationBonusTicks *= typeDurationMod;
			potionMod.multiplicativeDurationModifier *= typeDurationMod;
		}
		
		addMainEffect(potionMod, random, effects);
		addRandomAdditionalEffects(potionMod, random, effects);
		
		// last effect
		if (lastBrewedStatusEffect != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect)) {
			applyLastBrewedStatusEffect(potionMod, lastBrewedStatusEffect, random, effects);
		}
		
		// split durations, if set
		if (potionMod.potentDecreasingEffect) {
			effects = applyPotentDecreasingEffect(effects, random);
		}
		
		// potion type
		ItemStack itemStack;
		if (potionMod.makeSplashing) {
			if (potionMod.makeLingering) {
				itemStack = new ItemStack(Items.LINGERING_POTION);
			} else {
				itemStack = new ItemStack(Items.SPLASH_POTION);
			}
		} else {
			itemStack = new ItemStack(Items.POTION);
		}
		
		// apply to potion
		if (effects.size() == 0) {
			PotionUtil.setPotion(itemStack, Potions.THICK);
		} else {
			PotionUtil.setCustomPotionEffects(itemStack, effects);
		}
		
		if (potionMod.makeSplashing) {
			if (potionMod.makeLingering) {
				itemStack.setCustomName(Text.translatable("item.spectrum.lingering_potion"));
			} else {
				itemStack.setCustomName(Text.translatable("item.spectrum.splash_potion"));
			}
		} else {
			itemStack.setCustomName(Text.translatable("item.spectrum.potion"));
		}
		if (potionMod.fastDrinkable) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			compound.putBoolean("SpectrumFastDrinkable", true);
			itemStack.setNbt(compound);
		}
		
		// potion color
		setColor(itemStack, potionMod, effects);
		
		return itemStack;
	}
	
	public ItemStack getTippedArrows(PotionMod potionMod, StatusEffect lastBrewedStatusEffect, int amount, Random random) {
		List<StatusEffectInstance> effects = new ArrayList<>();
		
		addMainEffect(potionMod, random, effects);
		addRandomAdditionalEffects(potionMod, random, effects);
		if (lastBrewedStatusEffect != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect)) {
			applyLastBrewedStatusEffect(potionMod, lastBrewedStatusEffect, random, effects);
		}
		
		// split durations, if set
		if (potionMod.potentDecreasingEffect) {
			effects = applyPotentDecreasingEffect(effects, random);
		}
		
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW, amount);
		if (effects.size() == 0) {
			PotionUtil.setPotion(itemStack, Potions.THICK);
		} else {
			PotionUtil.setCustomPotionEffects(itemStack, effects);
		}
		
		itemStack.setCustomName(Text.translatable("item.spectrum.tipped_arrow"));
		setColor(itemStack, potionMod, effects);
		
		return itemStack;
	}
	
	public void fillPotionFillable(ItemStack potionFillableStack, PotionMod potionMod, StatusEffect lastBrewedStatusEffect, Random random) {
		if (potionFillableStack.getItem() instanceof PotionFillable potionFillable) {
			List<StatusEffectInstance> newEffects = new ArrayList<>();
			
			addMainEffect(potionMod, random, newEffects);
			addRandomAdditionalEffects(potionMod, random, newEffects);
			if (lastBrewedStatusEffect != null && (potionMod.chanceToAddLastEffect >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect)) {
				applyLastBrewedStatusEffect(potionMod, lastBrewedStatusEffect, random, newEffects);
			}
			// split durations, if set
			if (potionMod.potentDecreasingEffect) {
				newEffects = applyPotentDecreasingEffect(newEffects, random);
			}
			
			potionFillable.addEffects(potionFillableStack, newEffects);
			setColor(potionFillableStack, potionMod, newEffects);
		}
	}
	
	private void addMainEffect(PotionMod potionMod, Random random, List<StatusEffectInstance> effects) {
		if (potionMod.makeEffectsPositive) {
			StatusEffect positiveEffect = StatusEffectHelper.getPositiveVariant(this.statusEffect);
			if (positiveEffect != null) {
				int index = availablePositiveEffects.indexOf(positiveEffect);
				if (index != -1) {
					int baseDuration = availablePositiveEffectDurations.get(index);
					float basePotency = availablePositiveEffectPotencyMods.get(index);
					effects.add(getStatusEffectInstance(positiveEffect, baseDuration, basePotency, potionMod, random));
				}
			}
		} else {
			StatusEffectInstance statusEffectInstance = getStatusEffectInstance(this.statusEffect, this.baseDurationTicks, this.potencyModifier, potionMod, random);
			if (statusEffectInstance != null) {
				effects.add(statusEffectInstance);
			}
		}
	}
	
	private void addRandomAdditionalEffects(PotionMod potionMod, Random random, List<StatusEffectInstance> effects) {
		// random positive ones
		if (availablePositiveEffects.size() > 0) {
			int additionalPositiveEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomPositiveEffectCount, random);
			for (int i = 0; i < additionalPositiveEffects; i++) {
				int r;
				int tries = 0;
				StatusEffect selectedStatusEffect;
				do {
					r = random.nextInt(availablePositiveEffects.size());
					selectedStatusEffect = availablePositiveEffects.get(r);
					if (isEffectInList(effects, selectedStatusEffect)) {
						selectedStatusEffect = null;
						tries++;
					}
				} while (selectedStatusEffect == null && tries < 5);
				if (selectedStatusEffect != null) {
					StatusEffectInstance statusEffectInstance = getStatusEffectInstance(selectedStatusEffect, availablePositiveEffectDurations.get(r), availablePositiveEffectPotencyMods.get(r), potionMod, random);
					if (statusEffectInstance != null) {
						effects.add(statusEffectInstance);
					}
				}
			}
		}
		
		// random negative ones
		if (availableNegativeEffects.size() > 0) {
			int additionalNegativeEffects = Support.getIntFromDecimalWithChance(potionMod.additionalRandomNegativeEffectCount, random);
			for (int i = 0; i < additionalNegativeEffects; i++) {
				int r;
				int tries = 0;
				StatusEffect selectedStatusEffect;
				
				do {
					r = random.nextInt(availableNegativeEffects.size());
					selectedStatusEffect = availableNegativeEffects.get(r);
					
					if (potionMod.makeEffectsPositive) {
						StatusEffect positiveEffect = StatusEffectHelper.getPositiveVariant(selectedStatusEffect);
						if (positiveEffect != null) {
							selectedStatusEffect = positiveEffect;
						}
					}
					if (isEffectInList(effects, selectedStatusEffect)) {
						selectedStatusEffect = null;
						tries++;
					}
				} while (selectedStatusEffect == null && tries < 5);
				if (selectedStatusEffect != null) {
					StatusEffectInstance statusEffectInstance = getStatusEffectInstance(selectedStatusEffect, availableNegativeEffectDurations.get(r), availableNegativeEffectPotencyMods.get(r), potionMod, random);
					if (statusEffectInstance != null) {
						effects.add(statusEffectInstance);
					}
				}
			}
		}
	}
	
	private boolean isEffectInList(List<StatusEffectInstance> effects, StatusEffect selectedStatusEffect) {
		for (StatusEffectInstance existingInstance : effects) {
			if (existingInstance.getEffectType() == selectedStatusEffect) {
				return true;
			}
		}
		return false;
	}
	
	private void applyLastBrewedStatusEffect(PotionMod potionMod, StatusEffect lastBrewedStatusEffect, Random random, List<StatusEffectInstance> effects) {
		StatusEffect effect = lastBrewedStatusEffect;
		if (potionMod.makeEffectsPositive) {
			effect = StatusEffectHelper.getPositiveVariant(lastBrewedStatusEffect);
		}
		if (effect != null) {
			int baseDuration = 0;
			float basePotency = 0;
			if (effect.isBeneficial()) {
				int index = availablePositiveEffects.indexOf(effect);
				if (index != -1) {
					baseDuration = availablePositiveEffectDurations.get(index);
					basePotency = availablePositiveEffectPotencyMods.get(index);
				}
			} else {
				int index = availableNegativeEffects.indexOf(effect);
				if (index != -1) {
					baseDuration = availableNegativeEffectDurations.get(index);
					basePotency = availableNegativeEffectPotencyMods.get(index);
				}
			}
			
			if (basePotency >= 0) {
				StatusEffectInstance statusEffectInstance = getStatusEffectInstance(effect, baseDuration, basePotency * potionMod.lastEffectPotencyModifier, potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
	}
	
	public void setColor(ItemStack itemStack, PotionMod potionMod, List<StatusEffectInstance> effects) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (potionMod.unidentifiable) {
			nbtCompound.putInt("CustomPotionColor", 0x2f2f2f); // dark gray
			nbtCompound.putBoolean("spectrum_unidentifiable", true); // used in PotionItemMixin
			itemStack.setNbt(nbtCompound);
		} else if (effects.size() > 0) {
			nbtCompound.putInt("CustomPotionColor", getColor());
		}
	}
	
	public @Nullable StatusEffectInstance getStatusEffectInstance(@NotNull StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, @NotNull PotionMod potionMod, Random random) {
		int durationTicks = 1;
		if (!statusEffect.isInstant()) {
			durationTicks = (int) (((baseDurationTicks * potionMod.multiplicativeDurationModifier) + potionMod.flatDurationBonusTicks));
		}
		
		float posNegBonus = potionMod.flatPotencyBonusNegativeEffects;
		if (statusEffect.isBeneficial()) {
			posNegBonus = potionMod.flatPotencyBonusPositiveEffects;
		}
		
		int potency = 0;
		if (potencyModifier > 0.0F) {
			potency = Support.getIntFromDecimalWithChance(potencyModifier * potionMod.multiplicativePotencyModifier + potionMod.flatPotencyBonus + posNegBonus, random) - 1;
		}
		// if the result of the potency calculation was negative
		// because of a very low potencyModifier (not because the player was greedy and
		// got mali because of low multiplicativePotencyModifier) => set to 0 again
		if (potency < 0 && potionMod.multiplicativePotencyModifier >= 1.0) {
			potency = 0;
		}
		
		if (potency >= 0 && (statusEffect.isInstant() || durationTicks > 0)) {
			return new StatusEffectInstance(statusEffect, durationTicks, potency, !potionMod.noParticles, !potionMod.noParticles);
		} else {
			// when the effect is so borked that the effect would be too weak
			return null;
		}
	}
	
	public List<StatusEffectInstance> applyPotentDecreasingEffect(@NotNull List<StatusEffectInstance> statusEffectInstances, Random random) {
		List<StatusEffectInstance> splitInstances = new ArrayList<>();
		
		for (StatusEffectInstance instance : statusEffectInstances) {
			for (Pair<Float, Float> mods : SPLIT_EFFECT_POTENCY_AND_DURATION) {
				int newDuration = (int) (instance.getDuration() * mods.getRight());
				int newAmplifier = Support.getIntFromDecimalWithChance(instance.getAmplifier() * mods.getLeft(), random);
				if (newAmplifier > 0) {
					splitInstances.add(new StatusEffectInstance(instance.getEffectType(), newDuration, newAmplifier, instance.isAmbient(), instance.shouldShowParticles()));
				}
			}
		}
		
		return splitInstances;
	}
	
	public StatusEffect getStatusEffect() {
		return this.statusEffect;
	}
	
}
