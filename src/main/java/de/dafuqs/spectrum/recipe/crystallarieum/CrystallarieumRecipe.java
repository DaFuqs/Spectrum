package de.dafuqs.spectrum.recipe.crystallarieum;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.fluids.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;

public class CrystallarieumRecipe extends GatedSpectrumRecipe<SingleRecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/crystallarieum");
	public static final int GROWTH_SPEED_V = 0;
	public static final int CONSUMPTION_V = 7;
	public static final int CONSUME_CHANCE_V = 14;
	
	protected final static Map<BlockState, RecipeHolder<CrystallarieumRecipe>> STATE_CACHE = new HashMap<>();
	
	protected final Ingredient ingredient;
	protected final List<BlockState> growthStages;
	protected final int secondsPerGrowthStage;
	protected final InkColor inkColor;
	protected final int inkPerSecond;
	protected final boolean growsWithoutAdditive;
	protected final List<CrystallarieumAdditive> additives;
	protected final FluidIngredient medium;
	protected final List<ItemStack> additionalResults; // these aren't actual results, but recipe managers will treat it as such, showing this recipe as a way to get them. Use for drops of the growth blocks, for example
	
	public CrystallarieumRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, Ingredient ingredient, List<BlockState> growthStages, int secondsPerGrowthStage, InkColor inkColor, int inkPerSecond, boolean growsWithoutAdditive, List<CrystallarieumAdditive> additives, FluidIngredient medium, List<ItemStack> additionalResults) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.ingredient = ingredient;
		this.growthStages = growthStages;
		this.secondsPerGrowthStage = secondsPerGrowthStage;
		this.inkColor = inkColor;
		this.inkPerSecond = inkPerSecond;
		this.growsWithoutAdditive = growsWithoutAdditive;
		this.additives = additives;
		this.medium = medium;
		this.additionalResults = additionalResults;
		
		registerInToastManager(getType(), this);
	}

	public static @Nullable RecipeHolder<CrystallarieumRecipe> getRecipeForState(Level world, BlockState state) {
		return STATE_CACHE.computeIfAbsent(state, s -> {
			var recipes = world.getRecipeManager().getAllRecipesFor(SpectrumRecipeTypes.CRYSTALLARIEUM);
			for (var recipe : recipes) {
				if (recipe.value().growthStages.contains(s))
					return recipe;
			}
			return null;
		});
	}
	
	public static int growthSpeedOffsetU(CrystallarieumAdditive additive) {
		int offset = 0;
		float accel = additive.growthAccelerationMod();
		
		if (accel > 0.2) {
			if (accel >= 5)
				offset = 70 + 7 * 4;
			else if (accel > 1)
				offset = 70 + 7 * 3;
			else if (accel == 1)
				offset = 70 + 7 * 2;
			else if (accel < 1)
				offset = 70 + 7;
		}
		return offset;
	}
	
	public static int consumptionOffsetU(CrystallarieumAdditive additive, int offsetU) {
		float drain = additive.inkConsumptionMod();
		
		if (drain >= 5)
			offsetU = 0;
		else if (drain > 1)
			offsetU = 7;
		else if (drain == 1)
			offsetU = 7 * 2;
		else if (drain < 0.2)
			offsetU = 7 * 4;
		else if (drain < 1)
			offsetU = 7 * 3;
		return 70 + offsetU;
	}
	
	public static int consumeChanceOffsetU(CrystallarieumAdditive additive, int offsetU) {
		float chance = additive.consumeChancePerSecond();
		
		if (chance >= 0.25)
			offsetU = 0;
		else if (chance < 0.0001)
			offsetU = 7 * 4;
		else if (chance <= 0.02)
			offsetU = 7 * 3;
		else if (chance < 0.05)
			offsetU = 7 * 2;
		else if (chance < 0.25)
			offsetU = 7;
		return 70 + offsetU;
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level world) {
		return ingredient.test(input.getItem(0));
	}
	
	@Override
	@Deprecated
	public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider registryLookup) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryLookup) {
		List<BlockState> states = getGrowthStages();
		return states.getLast().getBlock().asItem().getDefaultInstance();
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.CRYSTALLARIEUM);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.CRYSTALLARIEUM_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.CRYSTALLARIEUM;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "crystallarieum_growing";
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> defaultedList = NonNullList.create();
		defaultedList.add(ingredient);
		return defaultedList;
	}
	
	public FluidIngredient getFluidIngredient() {
		return medium;
	}
	
	public Ingredient getIngredientStack() {
		return ingredient;
	}
	
	public CrystallarieumAdditive getAdditive(ItemStack itemStack) {
		for (CrystallarieumAdditive additive : this.additives) {
			if (additive.ingredient().test(itemStack)) {
				return additive;
			}
		}
		return CrystallarieumAdditive.EMPTY;
	}
	
	public List<BlockState> getGrowthStages() {
		return growthStages;
	}
	
	public int getSecondsPerGrowthStage() {
		return secondsPerGrowthStage;
	}
	
	public InkColor getInkColor() {
		return inkColor;
	}
	
	public int getInkPerSecond() {
		return inkPerSecond;
	}
	
	public boolean growsWithoutAdditive() {
		return growsWithoutAdditive;
	}
	
	public List<CrystallarieumAdditive> getAdditives() {
		return this.additives;
	}
	
	public List<ItemStack> getAdditionalResults() {
		return additionalResults;
	}
	
	public Optional<BlockState> getNextState(RecipeHolder<CrystallarieumRecipe> recipe, BlockState currentState) {
		for (Iterator<BlockState> it = recipe.value().getGrowthStages().iterator(); it.hasNext(); ) {
			BlockState state = it.next();
			if (state.equals(currentState)) {
				if (it.hasNext()) {
					return Optional.of(it.next());
				}
			}
		}
		return Optional.empty();
	}
	
	public static class Serializer implements RecipeSerializer<CrystallarieumRecipe> {
		
		private static final MapCodec<CrystallarieumRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
				BlockState.CODEC.listOf().fieldOf("growth_stage_states").forGetter(recipe -> recipe.growthStages),
				Codec.INT.fieldOf("seconds_per_growth_stage").forGetter(recipe -> recipe.secondsPerGrowthStage),
				InkColor.CODEC.fieldOf("ink_color").forGetter(recipe -> recipe.inkColor),
				Codec.INT.xmap(
						d -> d == 0 ? 0 : (1 << (d - 1)),
						e -> e == 0 ? 0 : (31 - Integer.numberOfLeadingZeros(e)) + 1
				).fieldOf("ink_cost_tier").forGetter(recipe -> recipe.inkPerSecond),
				Codec.BOOL.optionalFieldOf("grows_without_additive", false).forGetter(recipe -> recipe.growsWithoutAdditive),
				CrystallarieumAdditive.CODEC.listOf().fieldOf("additives").forGetter(recipe -> recipe.additives),
				FluidIngredient.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.medium),
				ItemStack.CODEC.listOf().optionalFieldOf("additional_recipe_manager_results", ImmutableList.of()).forGetter(recipe -> recipe.additionalResults)
		).apply(i, CrystallarieumRecipe::new));
		
		private static final StreamCodec<RegistryFriendlyByteBuf, CrystallarieumRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
				PacketCodecHelper.BLOCK_STATE.apply(ByteBufCodecs.list()), recipe -> recipe.growthStages,
				ByteBufCodecs.VAR_INT, recipe -> recipe.secondsPerGrowthStage,
				InkColor.PACKET_CODEC, recipe -> recipe.inkColor,
				ByteBufCodecs.VAR_INT, recipe -> recipe.inkPerSecond,
				ByteBufCodecs.BOOL, recipe -> recipe.growsWithoutAdditive,
				CrystallarieumAdditive.PACKET_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.additives,
				FluidIngredient.STREAM_CODEC, recipe -> recipe.medium,
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.additionalResults,
				CrystallarieumRecipe::new
		);
		
		@Override
		public MapCodec<CrystallarieumRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CrystallarieumRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
	
}
