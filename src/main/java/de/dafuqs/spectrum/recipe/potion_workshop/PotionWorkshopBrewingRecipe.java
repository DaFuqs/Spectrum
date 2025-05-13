package de.dafuqs.spectrum.recipe.potion_workshop;


import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.potion.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
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
	
	public static final Map<RegistryEntry<StatusEffect>, RegistryEntry<StatusEffect>> negativeToPositiveEffect = new HashMap<>() {{
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
	
	public static @Nullable PotionWorkshopBrewingRecipe getPositiveRecipe(@NotNull RegistryEntry<StatusEffect> statusEffect) {
		if (statusEffect.value().getCategory() == StatusEffectCategory.HARMFUL) {
			RegistryEntry<StatusEffect> positiveEffect = negativeToPositiveEffect.getOrDefault(statusEffect, null);
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
	
	public PotionWorkshopBrewingRecipe(
			String group, boolean secret, Optional<Identifier> requiredAdvancementIdentifier, int craftingTime,
			IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3,
			PotionRecipeEffect recipeData
	) {
		
		super(group, secret, requiredAdvancementIdentifier, craftingTime, recipeData.statusEffect().value().getColor(), ingredient1, ingredient2, ingredient3);
		this.recipeData = recipeData;
		
		registerInToastManager(getType(), this);
		
		// remember one of each status effect recipe for quick lookup
		if (recipeData.statusEffect().value().getCategory() == StatusEffectCategory.BENEFICIAL) {
			for (PotionWorkshopBrewingRecipe ae : positiveRecipes) {
				if (ae.recipeData.statusEffect().value() == recipeData.statusEffect()) {
					return;
				}
			}
			positiveRecipes.add(this);
		} else if (recipeData.statusEffect().value().getCategory() == StatusEffectCategory.HARMFUL) {
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
		return SpectrumRecipeSerializers.POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
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
		defaultedList.add(IngredientStack.ofItems(SpectrumItems.MERMAIDS_GEM));
		defaultedList.add(IngredientStack.ofItems(Items.GLASS_BOTTLE));
		addIngredientStacks(defaultedList);
		return defaultedList;
	}
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registryManager) {
		if (this.cachedOutput == null) {
			this.cachedOutput = getPotion(Items.GLASS_BOTTLE.getDefaultStack(), Items.POTION.getDefaultStack(), new PotionMod.Builder().build(), null, Random.create());
		}
		return this.cachedOutput;
	}
	
	@Override
	public ItemStack craft(RecipeInput inventory, RegistryWrapper.WrapperLookup drm) {
		ItemStack stack = new ItemStack(Items.POTION);
		stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of(new StatusEffectInstance(recipeData.statusEffect(), recipeData.baseDurationTicks()))));
		return stack;
	}
	
	public float getModifiedYield(PotionMod potionMod) {
		return recipeData.baseYield() + potionMod.yield();
	}
	
	public List<ItemStack> getPotions(ItemStack stack, PotionMod potionMod, @Nullable RecipeEntry<PotionWorkshopBrewingRecipe> lastRecipe, Random random, int brewedAmount) {
		var builder = new PotionMod.Builder(potionMod);
		// potion type
		ItemStack itemStack;
		if (potionMod.flags().makeSplashing()) {
			if (potionMod.flags().makeLingering()) {
				itemStack = new ItemStack(Items.LINGERING_POTION);
				if (potionMod.flags().negateDecreasingDuration()) {
					builder.durationMultiplier(builder.durationMultiplier + 3);
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
	
	public ItemStack getPotion(ItemStack originalStack, ItemStack targetStack, PotionMod potionMod, @Nullable RecipeEntry<PotionWorkshopBrewingRecipe> lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = generateEffects(originalStack, potionMod, lastRecipe, random);
		
		// apply to potion
		if (effects.isEmpty()) {
			// no effects: thick potion
			targetStack = PotionContentsComponent.createStack(targetStack.getItem(), Potions.THICK);
		} else {
			targetStack = PotionContentsComponent.createStack(targetStack.getItem(), SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(targetStack, potionMod, effects);
		}
		
		return targetStack;
	}
	
	public ItemStack getTippedArrows(ItemStack stack, PotionMod potionMod, @Nullable RecipeEntry<PotionWorkshopBrewingRecipe> lastRecipe, int amount, Random random) {
		if (potionMod.flags().negateDecreasingDuration()) {
			potionMod = new PotionMod.Builder(potionMod).durationMultiplier(potionMod.durationMultiplier() + 7).build();
		}
		List<InkPoweredStatusEffectInstance> effects = generateEffects(stack, potionMod, lastRecipe, random);
		
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW, amount);
		if (effects.isEmpty()) {
			itemStack = PotionContentsComponent.createStack(itemStack.getItem(), Potions.THICK);
		} else {
			itemStack = PotionContentsComponent.createStack(itemStack.getItem(), SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(itemStack, potionMod, effects);
		}
		
		return itemStack;
	}
	
	public void fillPotionFillable(ItemStack stack, PotionMod potionMod, @Nullable RecipeEntry<PotionWorkshopBrewingRecipe> lastRecipe, Random random) {
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
		OptionalInt potionColor = PotionContentsComponent.mixColors(instances);
		
		PotionContentsComponent potionComponent = new PotionContentsComponent(Optional.of(SpectrumPotions.PIGMENT_POTION), Optional.of(potionColor.orElse(0)), instances);
		stack.set(DataComponentTypes.POTION_CONTENTS, potionComponent);
		
		if (potionMod.flags().unidentifiable() || potionMod.additionalDrinkDurationTicks() != 0) {
			stack.set(SpectrumDataComponentTypes.CUSTOM_POTION_DATA, new CustomPotionDataComponent(potionMod.flags().unidentifiable(), potionMod.additionalDrinkDurationTicks()));
		}
	}
	
	private List<InkPoweredStatusEffectInstance> generateEffects(ItemStack baseIngredient, PotionMod potionMod, @Nullable RecipeEntry<PotionWorkshopBrewingRecipe> lastRecipe, Random random) {
		List<InkPoweredStatusEffectInstance> effects = new ArrayList<>();
		
		addEffect(potionMod, random, effects); // main effect
		if (lastRecipe != null) {
			addLastEffect(baseIngredient, potionMod, lastRecipe.value(), random, effects);
		}
		addAdditionalEffects(baseIngredient, potionMod, random, effects);
		addRandomEffects(baseIngredient, potionMod, random, effects);
		
		// split durations, if set
		if (potionMod.flags().potentDecreasingEffect()) {
			effects = applyPotentDecreasingEffect(effects, random);
		}
		
		return effects;
	}
	
	private static void addLastEffect(ItemStack baseIngredient, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (lastRecipe != null && (potionMod.chanceToAddLastEffect() >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect()) && lastRecipe.recipeData.isApplicableTo(baseIngredient, potionMod)) {
			PotionMod lastEffectMod = new PotionMod.Builder(potionMod)
					.potencyMultiplier(potionMod.lastEffectPotencyMultiplier())
					.durationMultiplier(potionMod.lastEffectDurationMultiplier())
					.build();
			lastRecipe.addEffect(lastEffectMod, random, effects);
		}
	}
	
	private static void addAdditionalEffects(ItemStack baseIngredient, PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		for (Pair<PotionRecipeEffect, Float> entry : potionMod.flags().additionalEffects()) {
			if (random.nextFloat() < entry.getRight() && entry.getLeft().isApplicableTo(baseIngredient, potionMod)) {
				InkPoweredStatusEffectInstance statusEffectInstance = entry.getLeft().getStatusEffectInstance(potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
	}
	
	private void addEffect(PotionMod potionMod, Random random, List<InkPoweredStatusEffectInstance> effects) {
		if (potionMod.flags().makeEffectsPositive()) {
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
		int additionalPositiveEffectCount = Support.getIntFromDecimalWithChance(potionMod.additionalRandomPositiveEffectCount(), random);
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
		int additionalNegativeEffectCount = Support.getIntFromDecimalWithChance(potionMod.additionalRandomNegativeEffectCount(), random);
		if (additionalNegativeEffectCount > 0) {
			List<PotionWorkshopBrewingRecipe> randomlySelectedRecipes = pullRandomMatchingRecipes(potionMod.flags().makeEffectsPositive() ? positiveRecipes : negativeRecipes, additionalNegativeEffectCount, effects, baseIngredient);
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
			if (containsEffect(effects, recipe.recipeData.statusEffect().value())) {
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
			if (instance.getEffectType().value().isInstant()) {
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
		return this.recipeData.statusEffect().value();
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "potion_workshop_brewing";
	}
	
	public static class Serializer implements RecipeSerializer<PotionWorkshopBrewingRecipe> {
		
		public static final MapCodec<PotionWorkshopBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(c -> c.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(c -> c.secret),
				Identifier.CODEC.optionalFieldOf("required_advancement").forGetter(c -> c.requiredAdvancementIdentifier),
				Codec.INT.optionalFieldOf("time", 200).forGetter(c -> c.craftingTime),
				IngredientStack.Serializer.CODEC.fieldOf("ingredient1").forGetter(c -> c.ingredient1),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient2", IngredientStack.EMPTY).forGetter(c -> c.ingredient2),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient3", IngredientStack.EMPTY).forGetter(c -> c.ingredient3),
				PotionRecipeEffect.CODEC.forGetter(c -> c.recipeData)
		).apply(i, PotionWorkshopBrewingRecipe::new));
		
		public static final PacketCodec<RegistryByteBuf, PotionWorkshopBrewingRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				PacketCodecs.STRING, c -> c.group,
				PacketCodecs.BOOL, c -> c.secret,
				PacketCodecs.optional(Identifier.PACKET_CODEC), c -> c.requiredAdvancementIdentifier,
				PacketCodecs.VAR_INT, c -> c.craftingTime,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient1,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient2,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient3,
				PotionRecipeEffect.PACKET_CODEC, c -> c.recipeData,
				PotionWorkshopBrewingRecipe::new
		);
		
		@Override
		public MapCodec<PotionWorkshopBrewingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, PotionWorkshopBrewingRecipe> packetCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
