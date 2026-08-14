package de.dafuqs.spectrum.recipe.pedestal;


import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class ShapelessPedestalRecipe extends PedestalRecipe {
	
	public ShapelessPedestalRecipe(
			String group, Optional<ResourceLocation> requiredAdvancement, Optional<ResourceLocation> revealSecretAdvancement, List<ItemStack> additionalResults,
			PedestalRecipeTier tier, List<IngredientStack> craftingInputs, Map<GemstoneColor, Integer> gemstonePowderInputs, ItemStack output,
			float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades
	) {
		super(group, requiredAdvancement, revealSecretAdvancement, additionalResults, tier, craftingInputs, gemstonePowderInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public boolean matches(PedestalRecipeInput recipeInput, Level world) {
		return matchIngredientStacksExclusively(recipeInput, getIngredientStacks(), recipeInput.getCraftingGridSlots()) && super.matches(recipeInput, world);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SHAPELESS_PEDESTAL_RECIPE_SERIALIZER;
	}
	
	@Override
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		super.consumeIngredients(pedestal);
		
		for (int slot : CRAFTING_GRID_SLOTS) {
			for (IngredientStack ingredientStack : this.inputs) {
				ItemStack slotStack = pedestal.getItem(slot);
				if (ingredientStack.test(slotStack)) {
					decrementGridSlot(pedestal, slot, ingredientStack.getCount(), slotStack);
					break;
				}
			}
		}
	}
	
	public static class Serializer implements RecipeSerializer<ShapelessPedestalRecipe> {
		
		public static final MapCodec<ShapelessPedestalRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancement),
				ResourceLocation.CODEC.optionalFieldOf("reveal_secret_advancement").forGetter(recipe -> recipe.revealSecretAdvancement),
				ItemStack.CODEC.listOf().optionalFieldOf("additional_recipe_viewer_results", List.of()).forGetter(recipe -> recipe.additionalResults),
				PedestalRecipeTier.CODEC.optionalFieldOf("tier", PedestalRecipeTier.BASIC).forGetter(recipe -> recipe.tier),
				IngredientStack.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.inputs),
				CodecHelper.registryMap(SpectrumRegistries.GEMSTONE_COLOR, Codec.INT).fieldOf("colors").forGetter(recipe -> recipe.powderInputs),
				ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
				Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(recipe -> recipe.experience),
				Codec.INT.optionalFieldOf("time", 200).forGetter(recipe -> recipe.craftingTime),
				Codec.BOOL.optionalFieldOf("skip_recipe_remainders", false).forGetter(recipe -> recipe.skipRecipeRemainders),
				Codec.BOOL.optionalFieldOf("disable_yield_upgrades", false).forGetter(recipe -> recipe.noBenefitsFromYieldUpgrades)
		).apply(i, ShapelessPedestalRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessPedestalRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancement,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.revealSecretAdvancement,
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.additionalResults,
				PedestalRecipeTier.PACKET_CODEC, recipe -> recipe.tier,
				IngredientStack.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.inputs,
				ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(SpectrumRegistries.GEMSTONE_COLOR.key()), ByteBufCodecs.VAR_INT), recipe -> recipe.powderInputs,
				ItemStack.STREAM_CODEC, recipe -> recipe.output,
				ByteBufCodecs.FLOAT, recipe -> recipe.experience,
				ByteBufCodecs.VAR_INT, recipe -> recipe.craftingTime,
				ByteBufCodecs.BOOL, recipe -> recipe.skipRecipeRemainders,
				ByteBufCodecs.BOOL, recipe -> recipe.noBenefitsFromYieldUpgrades,
				ShapelessPedestalRecipe::new
		);
		
		@Override
		public MapCodec<ShapelessPedestalRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessPedestalRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
	
}
