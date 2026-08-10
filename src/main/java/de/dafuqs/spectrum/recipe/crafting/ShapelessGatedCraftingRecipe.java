package de.dafuqs.spectrum.recipe.crafting;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.common.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

// We cannot extend ShapedRecipe / ShapelessRecipe, since EMI would force-register its own recipe handler for it in dev.emi.emi.VanillaPlugin. Big sad.
// Our Fallback: EMI hardcodes CustomRecipe to not register its default recipe display.
public class ShapelessGatedCraftingRecipe extends GatedCraftingRecipe {
	
	protected final NonNullList<Ingredient> ingredients;
	protected final boolean isSimple;

	public ShapelessGatedCraftingRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier) {
		super(group, category, result, secret, requiredAdvancementIdentifier);
		this.ingredients = ingredients;
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}
	
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (input.ingredientCount() != this.ingredients.size()) {
			return false;
		} else if (!isSimple) {
			var nonEmptyItems = new ArrayList<ItemStack>(input.ingredientCount());
			for (ItemStack item : input.items())
				if (!item.isEmpty())
					nonEmptyItems.add(item);
			return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
		} else {
			return input.size() == 1 && this.ingredients.size() == 1
					? this.ingredients.getFirst().test(input.getItem(0))
					: input.stackedContents().canCraft(this, null);
		}
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.ingredients.size();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.GATED_SHAPELESS_CRAFTING_RECIPE_SERIALIZER;
	}
	
	public static class Serializer implements RecipeSerializer<ShapelessGatedCraftingRecipe> {
		public static final MapCodec<ShapelessGatedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
				recipe -> recipe.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
								CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(CustomRecipe::category),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
								Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(
												list -> {
													Ingredient[] aingredient = list.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
													if (aingredient.length == 0) {
														return DataResult.error(() -> "No ingredients for shapeless recipe");
													} else {
														return aingredient.length > 9
																? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(9))
																: DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
													}
												},
												DataResult::success
										)
										.forGetter(p_300975_ -> p_300975_.ingredients),
								Codec.BOOL.optionalFieldOf("secret", false).forGetter(r -> r.secret),
								ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(r -> r.requiredAdvancementIdentifier)
						)
						.apply(recipe, ShapelessGatedCraftingRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessGatedCraftingRecipe> STREAM_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, ShapelessGatedCraftingRecipe::getGroup,
				CraftingBookCategory.STREAM_CODEC, ShapelessGatedCraftingRecipe::category,
				ItemStack.STREAM_CODEC, ShapelessGatedCraftingRecipe::getResult,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)), ShapelessGatedCraftingRecipe::getIngredients,
				ByteBufCodecs.BOOL, ShapelessGatedCraftingRecipe::isSecret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), r -> r.requiredAdvancementIdentifier,
				ShapelessGatedCraftingRecipe::new
		);
		
		@Override
		public MapCodec<ShapelessGatedCraftingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessGatedCraftingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
		
	}
	
}