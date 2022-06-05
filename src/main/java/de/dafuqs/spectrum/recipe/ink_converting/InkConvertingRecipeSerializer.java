package de.dafuqs.spectrum.recipe.ink_converting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class InkConvertingRecipeSerializer implements RecipeSerializer<InkConvertingRecipe> {

	public final InkConvertingRecipeSerializer.RecipeFactory<InkConvertingRecipe> recipeFactory;

	public InkConvertingRecipeSerializer(InkConvertingRecipeSerializer.RecipeFactory<InkConvertingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public InkConvertingRecipe read(Identifier identifier, JsonObject jsonObject) {
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "color"));
		long inkAmount = JsonHelper.getLong(jsonObject, "amount");
		
		Identifier requiredAdvancementIdentifier;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = InkConvertingRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
		}

		return this.recipeFactory.create(identifier, ingredient, inkColor, inkAmount, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, InkConvertingRecipe inkConvertingRecipe) {
		inkConvertingRecipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeString(inkConvertingRecipe.color.toString());
		packetByteBuf.writeLong(inkConvertingRecipe.amount);
		packetByteBuf.writeIdentifier(inkConvertingRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public InkConvertingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		long inkAmount = packetByteBuf.readLong();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, ingredient, inkColor, inkAmount, requiredAdvancementIdentifier);
	}

	
	public interface RecipeFactory<InkConvertingRecipe> {
		InkConvertingRecipe create(Identifier id, Ingredient inputIngredient, InkColor inkColor, long inkAmount, Identifier requiredAdvancementIdentifier);
	}

}
