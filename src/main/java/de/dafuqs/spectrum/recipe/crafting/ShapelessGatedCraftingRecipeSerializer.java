package de.dafuqs.spectrum.recipe.crafting;

import com.google.gson.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import org.jetbrains.annotations.*;

public class ShapelessGatedCraftingRecipeSerializer implements GatedRecipeSerializer<ShapelessGatedCraftingRecipe> {

	public final ShapelessGatedCraftingRecipeSerializer.RecipeFactory recipeFactory;

	public ShapelessGatedCraftingRecipeSerializer(ShapelessGatedCraftingRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	public interface RecipeFactory {
		ShapelessGatedCraftingRecipe create(Identifier id, String group, CraftingRecipeCategory category, ItemStack output, DefaultedList<Ingredient> input, boolean secret, @Nullable Identifier requiredAdvancementIdentifier);
	}

	@Override
	public ShapelessGatedCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String string = JsonHelper.getString(jsonObject, "group", "");
		CraftingRecipeCategory craftingRecipeCategory = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", (String) null), CraftingRecipeCategory.MISC);
		DefaultedList<Ingredient> defaultedList = ShapelessRecipeSerializerAccessor.invokeGetIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
		if (defaultedList.isEmpty()) {
			throw new JsonParseException("No ingredients for shapeless recipe");
		} else if (defaultedList.size() > 9) {
			throw new JsonParseException("Too many ingredients for shapeless recipe");
		} else {
			ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
			boolean secret = readSecret(jsonObject);
			@Nullable Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
			return new ShapelessGatedCraftingRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList, secret, requiredAdvancementIdentifier);
		}
	}


	@Override
	public void write(PacketByteBuf packetByteBuf, ShapelessGatedCraftingRecipe recipe) {
		packetByteBuf.writeString(recipe.getGroup());
		packetByteBuf.writeEnumConstant(recipe.getCategory());
		packetByteBuf.writeVarInt(recipe.getIngredients().size());
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredient.write(packetByteBuf);
		}
		packetByteBuf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
		packetByteBuf.writeBoolean(recipe.isSecret());
		writeNullableIdentifier(packetByteBuf, recipe.getRecipeTypeUnlockIdentifier());
	}


	@Override
	public ShapelessGatedCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String string = packetByteBuf.readString();
		CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
		int i = packetByteBuf.readVarInt();
		DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
		defaultedList.replaceAll(ignored -> Ingredient.fromPacket(packetByteBuf));
		boolean secret = packetByteBuf.readBoolean();
		@Nullable Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);

		ItemStack itemStack = packetByteBuf.readItemStack();
		return new ShapelessGatedCraftingRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList, secret, requiredAdvancementIdentifier);
	}

}
