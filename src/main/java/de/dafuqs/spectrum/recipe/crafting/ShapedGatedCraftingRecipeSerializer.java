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

import java.util.*;

public class ShapedGatedCraftingRecipeSerializer implements GatedRecipeSerializer<ShapedGatedCraftingRecipe> {

	public final ShapedGatedCraftingRecipeSerializer.RecipeFactory recipeFactory;

	public ShapedGatedCraftingRecipeSerializer(ShapedGatedCraftingRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	public interface RecipeFactory {
		ShapedGatedCraftingRecipe create(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean notification, boolean secret, @Nullable Identifier requiredAdvancementIdentifier);
	}

	@Override
	public ShapedGatedCraftingRecipe read(Identifier id, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		CraftingRecipeCategory category = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
		Map<String, Ingredient> map = ShapedRecipeAccessor.invokeReadSymbols(JsonHelper.getObject(jsonObject, "key"));
		String[] strings = ShapedRecipeAccessor.invokeRemovePadding(ShapedRecipeAccessor.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
		int width = strings[0].length();
		int height = strings.length;
		DefaultedList<Ingredient> input = ShapedRecipeAccessor.invokeCreatePatternMatrix(strings, map, width, height);
		ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
		boolean bl = JsonHelper.getBoolean(jsonObject, "show_notification", true);
		boolean secret = readSecret(jsonObject);
		@Nullable Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		return new ShapedGatedCraftingRecipe(id, group, category, width, height, input, output, bl, secret, requiredAdvancementIdentifier);
	}


	@Override
	public void write(PacketByteBuf packetByteBuf, ShapedGatedCraftingRecipe recipe) {
		packetByteBuf.writeVarInt(recipe.getWidth());
		packetByteBuf.writeVarInt(recipe.getHeight());
		packetByteBuf.writeString(recipe.getGroup());
		packetByteBuf.writeEnumConstant(recipe.getCategory());
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredient.write(packetByteBuf);
		}
		packetByteBuf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
		packetByteBuf.writeBoolean(recipe.showNotification());
		packetByteBuf.writeBoolean(recipe.isSecret());
		writeNullableIdentifier(packetByteBuf, recipe.getRecipeTypeUnlockIdentifier());
	}


	@Override
	public ShapedGatedCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		int i = packetByteBuf.readVarInt();
		int j = packetByteBuf.readVarInt();
		String string = packetByteBuf.readString();
		CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
		DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
		defaultedList.replaceAll(ignored -> Ingredient.fromPacket(packetByteBuf));
		ItemStack itemStack = packetByteBuf.readItemStack();
		boolean bl = packetByteBuf.readBoolean();
		boolean secret = packetByteBuf.readBoolean();
		@Nullable Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		return new ShapedGatedCraftingRecipe(identifier, string, craftingRecipeCategory, i, j, defaultedList, itemStack, bl, secret, requiredAdvancementIdentifier);
	}

}
