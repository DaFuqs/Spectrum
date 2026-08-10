package de.dafuqs.spectrum.recipe.crafting;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.recipe.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

// We cannot extend ShapedRecipe / ShapelessRecipe, since EMI would force-register its own recipe handler for it in dev.emi.emi.VanillaPlugin. Big sad.
// Our Fallback: EMI hardcodes CustomRecipe to not register its default recipe display.
public class ShapedGatedCraftingRecipe extends GatedCraftingRecipe {
	
	protected final ShapedRecipePattern pattern;

	public ShapedGatedCraftingRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier) {
		super(group, category, result, secret, requiredAdvancementIdentifier);
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
				recipe -> recipe.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
								CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(shapedGatedCraftingRecipe -> shapedGatedCraftingRecipe.category()),
								ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
								Codec.BOOL.optionalFieldOf("secret", false).forGetter(r -> r.secret),
								ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(r -> r.requiredAdvancementIdentifier)
						)
						.apply(recipe, ShapedGatedCraftingRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapedGatedCraftingRecipe> STREAM_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, ShapedGatedCraftingRecipe::getGroup,
				CraftingBookCategory.STREAM_CODEC, ShapedGatedCraftingRecipe::category,
				ShapedRecipePattern.STREAM_CODEC, ShapedGatedCraftingRecipe::getPattern,
				ItemStack.STREAM_CODEC, ShapedGatedCraftingRecipe::getResult,
				ByteBufCodecs.BOOL, ShapedGatedCraftingRecipe::isSecret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ShapedGatedCraftingRecipe::getRequiredAdvancementIdentifier,
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