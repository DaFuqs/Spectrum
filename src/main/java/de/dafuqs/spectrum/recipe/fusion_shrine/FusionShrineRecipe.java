package de.dafuqs.spectrum.recipe.fusion_shrine;


import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.predicate.location.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FusionShrineRecipe extends GatedStackSpectrumRecipe<StorageRecipeInput<SingleVariantStorage<FluidVariant>>> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("build_fusion_shrine");
	
	protected final List<IngredientStack> craftingInputs;
	protected final FluidIngredient fluid;
	protected final ItemStack output;
	protected final float experience;
	protected final int craftingTime;
	// since there are a few recipes that are basically compacting recipes
	// they could be crafted ingots>block and block>ingots back
	// In that case:
	// - the player should not get XP
	// - Yield upgrades disabled (item multiplication)
	protected final boolean yieldUpgradesDisabled;
	protected final boolean playCraftingFinishedEffects;
	
	protected final List<WorldConditionsPredicate> worldConditionsPredicates;
	@NotNull
	protected final FusionShrineRecipeWorldEffect startWorldEffect;
	@NotNull
	protected final List<FusionShrineRecipeWorldEffect> duringWorldEffects;
	@NotNull
	protected final FusionShrineRecipeWorldEffect finishWorldEffect;
	@Nullable
	protected final Component description;
	// copy all components from the first stack in the ingredients to the output stack
	protected final boolean copyComponents;
	
	public FusionShrineRecipe(
			String group,
			boolean secret,
			Optional<ResourceLocation> requiredAdvancementIdentifier,
			List<IngredientStack> craftingInputs,
			FluidIngredient fluid,
			ItemStack output,
			float experience,
			int craftingTime,
			boolean yieldUpgradesDisabled,
			boolean playCraftingFinishedEffects,
			boolean copyComponents,
			List<WorldConditionsPredicate> worldConditionsPredicates,
			@NotNull FusionShrineRecipeWorldEffect startWorldEffect,
			@NotNull List<FusionShrineRecipeWorldEffect> duringWorldEffects,
			@NotNull FusionShrineRecipeWorldEffect finishWorldEffect,
			@Nullable Component description
	) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.craftingInputs = craftingInputs;
		this.fluid = fluid;
		this.output = output;
		this.experience = experience;
		this.craftingTime = craftingTime;
		this.yieldUpgradesDisabled = yieldUpgradesDisabled;
		this.playCraftingFinishedEffects = playCraftingFinishedEffects;
		
		this.worldConditionsPredicates = worldConditionsPredicates;
		this.startWorldEffect = startWorldEffect;
		this.duringWorldEffects = duringWorldEffects;
		this.finishWorldEffect = finishWorldEffect;
		this.description = description;
		this.copyComponents = copyComponents;
		
		registerInToastManager(getType(), this);
	}
	
	/**
	 * Only tests the items.
	 * The required fluid has to be tested manually by the crafting block.
	 */
	@Override
	public boolean matches(StorageRecipeInput<SingleVariantStorage<FluidVariant>> recipeInput, Level world) {
		SingleVariantStorage<FluidVariant> fluidStorage = recipeInput.getFluidStorage();
		if (!this.fluid.test(fluidStorage.variant)) {
			return false;
		}
		if (this.fluid != FluidIngredient.EMPTY) {
			if (fluidStorage.getAmount() != fluidStorage.getCapacity()) {
				return false;
			}
		}
		return matchIngredientStacksExclusively(recipeInput, getIngredientStacks());
	}
	
	@Override
	public ItemStack assemble(StorageRecipeInput<SingleVariantStorage<FluidVariant>> inv, HolderLookup.Provider drm) {
		return output.copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return craftingInputs.size() <= width * height;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return output;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.FUSION_SHRINE_BASALT);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.FUSION_SHRINE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.FUSION_SHRINE;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return this.craftingInputs;
	}
	
	public float getExperience() {
		return experience;
	}
	
	/**
	 * Returns a boolean depending on if any of the recipe conditions are met.
	 * These can always be true, be a specific day or moon phase, weather, a command, biome, etc.
	 * <p></>
	 * ME WHEN I CALL STREAM#ANYMATCH WHEN AN EMPTY SET SHOULD RETURN TRUE ~ Azzyypaaras
	 */
	public boolean areConditionMetCurrently(ServerLevel world, BlockPos pos) {
		if (worldConditionsPredicates.isEmpty())
			return true;
		return this.worldConditionsPredicates.stream().anyMatch(p -> p.test(world, pos));
	}
	
	public FluidIngredient getFluid() {
		return this.fluid;
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	/**
	 * @param tick The crafting tick if the fusion shrine recipe
	 * @return The effect that should be played for the given recipe tick
	 */
	public FusionShrineRecipeWorldEffect getWorldEffectForTick(int tick, int totalTicks) {
		if (tick == 1) {
			return this.startWorldEffect;
		}
		if (tick == totalTicks) {
			return this.finishWorldEffect;
		}
		if (this.duringWorldEffects.isEmpty()) {
			return null;
		}
		if (this.duringWorldEffects.size() == 1) {
			return this.duringWorldEffects.getFirst();
		}
		
		// we really have to calculate the current effect, huh?
		float parts = (float) totalTicks / this.duringWorldEffects.size();
		int index = (int) (tick / (parts));
		FusionShrineRecipeWorldEffect effect = this.duringWorldEffects.get(index);
		if (effect.isOneTimeEffect() && index != (int) parts) {
			return null;
		}
		
		return effect;
	}
	
	public Optional<Component> getDescription() {
		if (this.description == null) {
			return Optional.empty();
		}
		return Optional.of(this.description);
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "fusion_shrine";
	}
	
	// calculate the max number of items that will be crafted.
	// note that we only check each ingredient once if a match was found.
	// custom recipes therefore should not use items / tags that match multiple items
	// at once, since we cannot rely on positions in a grid like vanilla does in its crafting table.
	public void craft(Level world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		ItemStack firstStack = ItemStack.EMPTY;
		var memory = ItemStack.EMPTY;
		
		int maxAmount = 1;
		ItemStack output = assemble(new StorageRecipeInput<>(fusionShrineBlockEntity.getItems(), fusionShrineBlockEntity.fluidStorage), world.registryAccess());
		if (!output.isEmpty()) {
			maxAmount = output.getMaxStackSize();
			for (IngredientStack ingredientStack : getIngredientStacks()) {
				for (int i = 0; i < fusionShrineBlockEntity.getContainerSize(); i++) {
					ItemStack currentStack = fusionShrineBlockEntity.getItem(i);
					if (ingredientStack.test(currentStack)) {
						if (firstStack.isEmpty()) {
							firstStack = currentStack;
						}
						int ingredientStackAmount = ingredientStack.getCount();
						maxAmount = Math.min(maxAmount, currentStack.getCount() / ingredientStackAmount);
						break;
					}
				}
			}
			
			memory = firstStack.copy();
			if (maxAmount > 0) {
				double efficiencyModifier = fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);
				decrementIngredients(world, fusionShrineBlockEntity, maxAmount, efficiencyModifier);
			}
		} else {
			for (IngredientStack ingredientStack : getIngredientStacks()) {
				double efficiencyModifier = fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);
				
				for (int i = 0; i < fusionShrineBlockEntity.getContainerSize(); i++) {
					ItemStack currentStack = fusionShrineBlockEntity.getItem(i);
					if (ingredientStack.test(currentStack)) {
						int reducedAmountAfterMod = Support.getIntFromDecimalWithChance(ingredientStack.getCount() / efficiencyModifier, world.random);
						currentStack.shrink(reducedAmountAfterMod);
						break;
					}
				}
			}
		}
		
		if (this.copyComponents) {
			var originalEnchantments = output.getEnchantments();
			output = memory.transmuteCopy(output.getItem(), output.getCount());
			for (Holder<Enchantment> enchantment : originalEnchantments.keySet()) {
				output.enchant(enchantment, originalEnchantments.getLevel(enchantment));
			}
		}
		
		spawnCraftingResultAndXP(world, fusionShrineBlockEntity, output, maxAmount); // spawn results
	}
	
	private void decrementIngredients(Level world, FusionShrineBlockEntity fusionShrineBlockEntity, int recipesCrafted, double efficiencyModifier) {
		for (IngredientStack ingredientStack : getIngredientStacks()) {
			for (int i = 0; i < fusionShrineBlockEntity.getContainerSize(); i++) {
				ItemStack currentStack = fusionShrineBlockEntity.getItem(i);
				if (ingredientStack.test(currentStack)) {
					int reducedAmount = recipesCrafted * ingredientStack.getCount();
					int reducedAmountAfterMod = efficiencyModifier == 1 ? reducedAmount : Support.getIntFromDecimalWithChance(reducedAmount / efficiencyModifier, world.random);
					
					ItemStack currentRemainder = currentStack.getRecipeRemainder();
					currentStack.shrink(reducedAmountAfterMod);
					
					if (!currentRemainder.isEmpty()) {
						currentRemainder = currentRemainder.copy();
						currentRemainder.setCount(reducedAmountAfterMod);
						InventoryHelper.smartAddToInventory(currentRemainder, fusionShrineBlockEntity, null);
					}
					
					break;
				}
			}
		}
	}
	
	protected void spawnCraftingResultAndXP(@NotNull Level world, @NotNull FusionShrineBlockEntity fusionShrineBlockEntity, @NotNull ItemStack stack, int recipeCount) {
		int resultAmountBeforeMod = recipeCount * stack.getCount();
		double yieldModifier = yieldUpgradesDisabled ? 1.0 : fusionShrineBlockEntity.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.YIELD);
		int resultAmountAfterMod = Support.getIntFromDecimalWithChance(resultAmountBeforeMod * yieldModifier, world.random);
		
		int intExperience = Support.getIntFromDecimalWithChance(recipeCount * experience, world.random);
		MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, fusionShrineBlockEntity.getBlockPos().above(2), stack, resultAmountAfterMod, MultiblockCrafter.RECIPE_STACK_VELOCITY);
		
		if (experience > 0) {
			MultiblockCrafter.spawnExperience(world, fusionShrineBlockEntity.getBlockPos(), intExperience);
		}
		
		//only triggered on server side. Therefore, has to be sent to client via S2C packet
		fusionShrineBlockEntity.grantPlayerFusionCraftingAdvancement(stack, intExperience);
	}
	
	public boolean shouldPlayCraftingFinishedEffects() {
		return this.playCraftingFinishedEffects;
	}
	
	public static class Serializer implements RecipeSerializer<FusionShrineRecipe> {
		
		public static final MapCodec<FusionShrineRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				IngredientStack.Serializer.CODEC.listOf(0, 7).fieldOf("ingredients").forGetter(recipe -> recipe.craftingInputs),
				FluidIngredient.CODEC.optionalFieldOf("fluid", FluidIngredient.EMPTY).forGetter(recipe -> recipe.fluid),
				ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(recipe -> recipe.output),
				Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(recipe -> recipe.experience),
				Codec.INT.optionalFieldOf("time", 200).forGetter(recipe -> recipe.craftingTime),
				Codec.BOOL.optionalFieldOf("disable_yield_upgrades", false).forGetter(recipe -> recipe.yieldUpgradesDisabled),
				Codec.BOOL.optionalFieldOf("play_crafting_finished_effects", true).forGetter(recipe -> recipe.playCraftingFinishedEffects),
				Codec.BOOL.optionalFieldOf("copy_components", false).forGetter(recipe -> recipe.copyComponents),
				CodecHelper.singleOrList(WorldConditionsPredicate.CODEC).optionalFieldOf("world_conditions", List.of()).forGetter(recipe -> recipe.worldConditionsPredicates),
				FusionShrineRecipeWorldEffect.CODEC.fieldOf("start_crafting_effect").forGetter(recipe -> recipe.startWorldEffect),
				FusionShrineRecipeWorldEffect.CODEC.listOf().optionalFieldOf("during_crafting_effects", List.of()).forGetter(recipe -> recipe.duringWorldEffects),
				FusionShrineRecipeWorldEffect.CODEC.fieldOf("finish_crafting_effect").forGetter(recipe -> recipe.finishWorldEffect),
				ComponentSerialization.CODEC.optionalFieldOf("description", Component.empty()).forGetter(recipe -> recipe.description)
		).apply(i, FusionShrineRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, FusionShrineRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				IngredientStack.Serializer.PACKET_CODEC.apply(ByteBufCodecs.list(7)), recipe -> recipe.craftingInputs,
				FluidIngredient.PACKET_CODEC, recipe -> recipe.fluid,
				ItemStack.OPTIONAL_STREAM_CODEC, recipe -> recipe.output,
				ByteBufCodecs.FLOAT, recipe -> recipe.experience,
				ByteBufCodecs.VAR_INT, recipe -> recipe.craftingTime,
				ByteBufCodecs.BOOL, recipe -> recipe.yieldUpgradesDisabled,
				ByteBufCodecs.BOOL, recipe -> recipe.playCraftingFinishedEffects,
				ByteBufCodecs.BOOL, recipe -> recipe.copyComponents,
				WorldConditionsPredicate.PACKET_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.worldConditionsPredicates,
				FusionShrineRecipeWorldEffect.PACKET_CODEC, recipe -> recipe.startWorldEffect,
				FusionShrineRecipeWorldEffect.PACKET_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.duringWorldEffects,
				FusionShrineRecipeWorldEffect.PACKET_CODEC, recipe -> recipe.finishWorldEffect,
				ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, recipe -> recipe.description,
				FusionShrineRecipe::new
		);
		
		@Override
		public MapCodec<FusionShrineRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FusionShrineRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
