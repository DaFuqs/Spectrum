package de.dafuqs.spectrum.recipe.ink_converting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class InkConvertingRecipeSerializer implements GatedRecipeSerializer<InkConvertingRecipe> {
	
	public final InkConvertingRecipeSerializer.RecipeFactory<InkConvertingRecipe> recipeFactory;
	
	public InkConvertingRecipeSerializer(InkConvertingRecipeSerializer.RecipeFactory<InkConvertingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<InkConvertingRecipe> {
		InkConvertingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, InkColor inkColor, long inkAmount);
	}
	
	@Override
	public InkConvertingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "color"));
		long inkAmount = JsonHelper.getLong(jsonObject, "amount");
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, inkColor, inkAmount);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, InkConvertingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeString(recipe.color.toString());
		packetByteBuf.writeLong(recipe.amount);
	}
	
	@Override
	public InkConvertingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		long inkAmount = packetByteBuf.readLong();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, inkColor, inkAmount);
	}
	
}
