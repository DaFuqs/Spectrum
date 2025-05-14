package de.dafuqs.spectrum.recipe.potion_workshop;


import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {
	
	public static final int BASE_POTION_COUNT_ON_BREWING = 3;
	public static final int ARROW_COUNT_MULTIPLIER = 8;
	
	/**
	 * When potionMod.potentDecreasingEffect is set each status effect is split into separate
	 * instances defined in this list. First value is the new effects new potency mod, second the duration
	 */
	protected static final List<Tuple<Float, Float>> SPLIT_EFFECT_POTENCY_AND_DURATION = new ArrayList<>() {{
		add(new Tuple<>(1.667F, 0.1F));
		add(new Tuple<>(0.75F, 0.334F));
		add(new Tuple<>(0.25F, 0.667F));
	}};
	
	public static final Map<Holder<MobEffect>, Holder<MobEffect>> negativeToPositiveEffect = new HashMap<>() {{
		put(MobEffects.BAD_OMEN, MobEffects.HERO_OF_THE_VILLAGE);
		put(MobEffects.HUNGER, MobEffects.SATURATION);
		put(MobEffects.HARM, MobEffects.HEAL);
		put(MobEffects.DIG_SLOWDOWN, MobEffects.DIG_SPEED);
		put(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.MOVEMENT_SPEED);
		put(MobEffects.UNLUCK, MobEffects.LUCK);
		put(MobEffects.WEAKNESS, MobEffects.DAMAGE_BOOST);
		put(MobEffects.WITHER, MobEffects.REGENERATION);
		put(SpectrumStatusEffects.STIFFNESS, SpectrumStatusEffects.SWIFTNESS);
		put(SpectrumStatusEffects.DENSITY, SpectrumStatusEffects.LIGHTWEIGHT);
	}};
	
	public static @Nullable PotionWorkshopBrewingRecipe getPositiveRecipe(@NotNull Holder<MobEffect> statusEffect) {
		if (statusEffect.value().getCategory() == MobEffectCategory.HARMFUL) {
			Holder<MobEffect> positiveEffect = negativeToPositiveEffect.getOrDefault(statusEffect, null);
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
			String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, int craftingTime,
			IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3,
			PotionRecipeEffect recipeData
	) {
		
		super(group, secret, requiredAdvancementIdentifier, craftingTime, recipeData.statusEffect().value().getColor(), ingredient1, ingredient2, ingredient3);
		this.recipeData = recipeData;
		
		registerInToastManager(getType(), this);
		
		// remember one of each status effect recipe for quick lookup
		if (recipeData.statusEffect().value().getCategory() == MobEffectCategory.BENEFICIAL) {
			for (PotionWorkshopBrewingRecipe ae : positiveRecipes) {
				if (ae.recipeData.statusEffect().value() == recipeData.statusEffect()) {
					return;
				}
			}
			positiveRecipes.add(this);
		} else if (recipeData.statusEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
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
		return recipeData.applicableToPotions() && itemStack.is(Items.GLASS_BOTTLE)
				|| recipeData.applicableToTippedArrows() && itemStack.is(Items.ARROW)
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
		return baseItemStack.is(Items.GLASS_BOTTLE) ? BASE_POTION_COUNT_ON_BREWING : 1;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		NonNullList<IngredientStack> defaultedList = NonNullList.create();
		defaultedList.add(IngredientStack.ofItems(SpectrumItems.MERMAIDS_GEM));
		defaultedList.add(IngredientStack.ofItems(Items.GLASS_BOTTLE));
		addIngredientStacks(defaultedList);
		return defaultedList;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		if (this.cachedOutput == null) {
			this.cachedOutput = getPotion(Items.GLASS_BOTTLE.getDefaultInstance(), Items.POTION.getDefaultInstance(), new PotionMod.Builder().build(), null, RandomSource.create());
		}
		return this.cachedOutput;
	}
	
	@Override
	public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider drm) {
		ItemStack stack = new ItemStack(Items.POTION);
		stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.empty(), List.of(new MobEffectInstance(recipeData.statusEffect(), recipeData.baseDurationTicks()))));
		return stack;
	}
	
	public float getModifiedYield(PotionMod potionMod) {
		return recipeData.baseYield() + potionMod.yield();
	}
	
	public List<ItemStack> getPotions(ItemStack stack, PotionMod potionMod, @Nullable RecipeHolder<PotionWorkshopBrewingRecipe> lastRecipe, RandomSource random, int brewedAmount) {
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
	
	public ItemStack getPotion(ItemStack originalStack, ItemStack targetStack, PotionMod potionMod, @Nullable RecipeHolder<PotionWorkshopBrewingRecipe> lastRecipe, RandomSource random) {
		List<InkPoweredStatusEffectInstance> effects = generateEffects(originalStack, potionMod, lastRecipe, random);
		
		// apply to potion
		if (effects.isEmpty()) {
			// no effects: thick potion
			targetStack = PotionContents.createItemStack(targetStack.getItem(), Potions.THICK);
		} else {
			targetStack = PotionContents.createItemStack(targetStack.getItem(), SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(targetStack, potionMod, effects);
		}
		
		return targetStack;
	}
	
	public ItemStack getTippedArrows(ItemStack stack, PotionMod potionMod, @Nullable RecipeHolder<PotionWorkshopBrewingRecipe> lastRecipe, int amount, RandomSource random) {
		if (potionMod.flags().negateDecreasingDuration()) {
			potionMod = new PotionMod.Builder(potionMod).durationMultiplier(potionMod.durationMultiplier() + 7).build();
		}
		List<InkPoweredStatusEffectInstance> effects = generateEffects(stack, potionMod, lastRecipe, random);
		
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW, amount);
		if (effects.isEmpty()) {
			itemStack = PotionContents.createItemStack(itemStack.getItem(), Potions.THICK);
		} else {
			itemStack = PotionContents.createItemStack(itemStack.getItem(), SpectrumPotions.PIGMENT_POTION);
			setCustomPotionEffects(itemStack, potionMod, effects);
		}
		
		return itemStack;
	}
	
	public void fillPotionFillable(ItemStack stack, PotionMod potionMod, @Nullable RecipeHolder<PotionWorkshopBrewingRecipe> lastRecipe, RandomSource random) {
		if (stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			List<InkPoweredStatusEffectInstance> effects = generateEffects(stack, potionMod, lastRecipe, random);
			inkPoweredPotionFillable.addOrUpgradeEffects(stack, effects);
		}
	}
	
	private static void setCustomPotionEffects(ItemStack stack, PotionMod potionMod, List<InkPoweredStatusEffectInstance> effects) {
		List<MobEffectInstance> instances = new ArrayList<>();
		for (InkPoweredStatusEffectInstance e : effects) {
			instances.add(e.getStatusEffectInstance());
		}
		OptionalInt potionColor = PotionContents.getColorOptional(instances);
		
		PotionContents potionComponent = new PotionContents(Optional.of(SpectrumPotions.PIGMENT_POTION), Optional.of(potionColor.orElse(0)), instances);
		stack.set(DataComponents.POTION_CONTENTS, potionComponent);
		
		if (potionMod.flags().unidentifiable() || potionMod.additionalDrinkDurationTicks() != 0) {
			stack.set(SpectrumDataComponentTypes.CUSTOM_POTION_DATA, new CustomPotionDataComponent(potionMod.flags().unidentifiable(), potionMod.additionalDrinkDurationTicks()));
		}
	}
	
	private List<InkPoweredStatusEffectInstance> generateEffects(ItemStack baseIngredient, PotionMod potionMod, @Nullable RecipeHolder<PotionWorkshopBrewingRecipe> lastRecipe, RandomSource random) {
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
	
	private static void addLastEffect(ItemStack baseIngredient, PotionMod potionMod, PotionWorkshopBrewingRecipe lastRecipe, RandomSource random, List<InkPoweredStatusEffectInstance> effects) {
		if (lastRecipe != null && (potionMod.chanceToAddLastEffect() >= 1 || random.nextFloat() < potionMod.chanceToAddLastEffect()) && lastRecipe.recipeData.isApplicableTo(baseIngredient, potionMod)) {
			PotionMod lastEffectMod = new PotionMod.Builder(potionMod)
					.potencyMultiplier(potionMod.lastEffectPotencyMultiplier())
					.durationMultiplier(potionMod.lastEffectDurationMultiplier())
					.build();
			lastRecipe.addEffect(lastEffectMod, random, effects);
		}
	}
	
	private static void addAdditionalEffects(ItemStack baseIngredient, PotionMod potionMod, RandomSource random, List<InkPoweredStatusEffectInstance> effects) {
		for (Tuple<PotionRecipeEffect, Float> entry : potionMod.flags().additionalEffects()) {
			if (random.nextFloat() < entry.getB() && entry.getA().isApplicableTo(baseIngredient, potionMod)) {
				InkPoweredStatusEffectInstance statusEffectInstance = entry.getA().getStatusEffectInstance(potionMod, random);
				if (statusEffectInstance != null) {
					effects.add(statusEffectInstance);
				}
			}
		}
	}
	
	private void addEffect(PotionMod potionMod, RandomSource random, List<InkPoweredStatusEffectInstance> effects) {
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
	
	private void addRandomEffects(ItemStack baseIngredient, PotionMod potionMod, RandomSource random, List<InkPoweredStatusEffectInstance> effects) {
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
	
	private boolean containsEffect(List<InkPoweredStatusEffectInstance> effects, MobEffect statusEffect) {
		for (InkPoweredStatusEffectInstance existingInstance : effects) {
			if (existingInstance.getStatusEffectInstance().getEffect() == statusEffect) {
				return true;
			}
		}
		return false;
	}
	
	private List<InkPoweredStatusEffectInstance> applyPotentDecreasingEffect(@NotNull List<InkPoweredStatusEffectInstance> statusEffectInstances, RandomSource random) {
		List<InkPoweredStatusEffectInstance> splitInstances = new ArrayList<>();
		
		for (InkPoweredStatusEffectInstance poweredInstance : statusEffectInstances) {
			MobEffectInstance instance = poweredInstance.getStatusEffectInstance();
			
			// instant effects, like harming do not get split (that would apply harming 3x
			if (instance.getEffect().value().isInstantenous()) {
				splitInstances.add(poweredInstance);
				continue;
			}
			
			for (Tuple<Float, Float> mods : SPLIT_EFFECT_POTENCY_AND_DURATION) {
				int newDuration = (int) (instance.getDuration() * mods.getB());
				int newAmplifier = Support.getIntFromDecimalWithChance(instance.getAmplifier() * mods.getA(), random);
				if (newAmplifier >= 0) {
					splitInstances.add(new InkPoweredStatusEffectInstance(new MobEffectInstance(instance.getEffect(), newDuration, newAmplifier, instance.isAmbient(), instance.isVisible()), poweredInstance.getInkCost(), poweredInstance.getColor(), poweredInstance.isUnidentifiable(), poweredInstance.isIncurable()));
				}
			}
		}
		
		return splitInstances;
	}
	
	public MobEffect getStatusEffect() {
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
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(c -> c.requiredAdvancementIdentifier),
				Codec.INT.optionalFieldOf("time", 200).forGetter(c -> c.craftingTime),
				IngredientStack.Serializer.CODEC.fieldOf("ingredient1").forGetter(c -> c.ingredient1),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient2", IngredientStack.EMPTY).forGetter(c -> c.ingredient2),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient3", IngredientStack.EMPTY).forGetter(c -> c.ingredient3),
				PotionRecipeEffect.CODEC.forGetter(c -> c.recipeData)
		).apply(i, PotionWorkshopBrewingRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopBrewingRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				ByteBufCodecs.VAR_INT, c -> c.craftingTime,
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
		public StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopBrewingRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
