package de.dafuqs.spectrum.recipe.cinderhearth;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class CinderhearthRecipe extends GatedStackSpectrumRecipe<SingleRecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/cinderhearth");
	
	protected final IngredientStack ingredient;
	protected final int time;
	protected final float experience;
	protected final List<StackWithChance> resultsWithChance;
	
	public CinderhearthRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, IngredientStack ingredient, int time, float experience, List<StackWithChance> resultsWithChance) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.ingredient = ingredient;
		this.time = time;
		this.experience = experience;
		this.resultsWithChance = resultsWithChance;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level world) {
		return ingredient.test(input.getItem(0));
	}
	
	@Override
	@Deprecated
	public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registryLookup) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryLookup) {
		return resultsWithChance.getFirst().stack();
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.CINDERHEARTH);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.CINDERHEARTH_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.CINDERHEARTH;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "cinderhearth";
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return List.of(this.ingredient);
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getCraftingTime() {
		return time;
	}
	
	public List<ItemStack> getRolledOutputs(RandomSource random, float yieldMod) {
		List<ItemStack> output = new ArrayList<>();
		for (StackWithChance possibleOutput : resultsWithChance) {
			float chance = possibleOutput.chance();
			if (chance >= 1.0 || random.nextFloat() < chance * yieldMod) {
				ItemStack currentOutputStack = possibleOutput.stack();
				if (yieldMod > 1) {
					int totalCount = Support.getIntFromDecimalWithChance(currentOutputStack.getCount() * yieldMod, random);
					while (totalCount > 0) { // if the rolled count exceeds the max stack size we need to split them (unstackable items, counts > 64, ...)
						int count = Math.min(totalCount, currentOutputStack.getMaxStackSize());
						ItemStack outputStack = currentOutputStack.copy();
						outputStack.setCount(count);
						output.add(outputStack);
						totalCount -= count;
					}
				} else {
					output.add(currentOutputStack.copy());
				}
			}
		}
		return output;
	}
	
	public List<ItemStack> getPossibleOutputs() {
		List<ItemStack> outputs = new ArrayList<>();
		for (StackWithChance pair : resultsWithChance) {
			outputs.add(pair.stack());
		}
		return outputs;
	}
	
	public List<StackWithChance> getResultsWithChance() {
		return resultsWithChance;
	}
	
	public static class Serializer implements RecipeSerializer<CinderhearthRecipe> {
		
		public static final MapCodec<CinderhearthRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				IngredientStack.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
				Codec.INT.fieldOf("time").forGetter(recipe -> recipe.time),
				Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(recipe -> recipe.experience),
				StackWithChance.CODEC.listOf().fieldOf("results").forGetter(recipe -> recipe.resultsWithChance)
		).apply(i, CinderhearthRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, CinderhearthRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				IngredientStack.STREAM_CODEC, recipe -> recipe.ingredient,
				ByteBufCodecs.VAR_INT, recipe -> recipe.time,
				ByteBufCodecs.FLOAT, recipe -> recipe.experience,
				StackWithChance.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.resultsWithChance,
				CinderhearthRecipe::new
		);
		
		@Override
		public MapCodec<CinderhearthRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CinderhearthRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
	
}
