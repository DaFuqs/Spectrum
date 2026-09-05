package de.dafuqs.spectrum.recipe.crafting;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

// We cannot extend ShapedRecipe / ShapelessRecipe, since EMI would force-register its own recipe handler for it in dev.emi.emi.VanillaPlugin. Big sad.
// Our Fallback: EMI hardcodes CustomRecipe to not register its default recipe display.
public class ShapedGatedCraftingRecipe extends GatedCraftingRecipe {
	
	protected final ShapedRecipePattern pattern;

	public ShapedGatedCraftingRecipe(String group, Optional<ResourceLocation> requiredAdvancement, Optional<ResourceLocation> revealSecretAdvancement, List<ItemStack> additionalResults,
									 CraftingBookCategory category, ItemStack result, ShapedRecipePattern pattern) {
		super(group, requiredAdvancement, revealSecretAdvancement, additionalResults, category, result);
		this.pattern = pattern;
	}
	
	public int getWidth() {
		return this.pattern.width();
	}
	
	public int getHeight() {
		return this.pattern.height();
	}
	
	public NonNullList<Ingredient> getIngredients() {
		return this.pattern.ingredients();
	}
	
	@Override
	public boolean matches(CraftingInput input, Level level) {
		return this.pattern.matches(input);
	}
	
	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		return this.getResultItem(registries).copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= this.pattern.width() && height >= this.pattern.height();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.GATED_SHAPED_CRAFTING_RECIPE_SERIALIZER;
	}
	
	public ShapedRecipePattern getPattern() {
		return this.pattern;
	}
	
	public static class Serializer implements RecipeSerializer<ShapedGatedCraftingRecipe> {
		public static final MapCodec<ShapedGatedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancement),
								ResourceLocation.CODEC.optionalFieldOf("reveal_secret_advancement").forGetter(recipe -> recipe.revealSecretAdvancement),
								ItemStack.CODEC.listOf().optionalFieldOf("additional_recipe_viewer_results", List.of()).forGetter(recipe -> recipe.additionalResults),
								CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(shapedGatedCraftingRecipe -> shapedGatedCraftingRecipe.category()),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
								ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern)
						)
						.apply(instance, ShapedGatedCraftingRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapedGatedCraftingRecipe> STREAM_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancement,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.revealSecretAdvancement,
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.additionalResults,
				CraftingBookCategory.STREAM_CODEC, ShapedGatedCraftingRecipe::category,
				ItemStack.STREAM_CODEC, ShapedGatedCraftingRecipe::getResult,
				ShapedRecipePattern.STREAM_CODEC, ShapedGatedCraftingRecipe::getPattern,
				ShapedGatedCraftingRecipe::new
		);
		
		@Override
		public MapCodec<ShapedGatedCraftingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapedGatedCraftingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
		
	}
	
}