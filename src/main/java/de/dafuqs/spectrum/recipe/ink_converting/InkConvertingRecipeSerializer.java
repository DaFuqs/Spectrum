package de.dafuqs.spectrum.recipe.ink_converting;

import com.google.gson.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

import java.util.*;

public class InkConvertingRecipeSerializer implements GatedRecipeSerializer<InkConvertingRecipe> {
	
	public final InkConvertingRecipeSerializer.RecipeFactory recipeFactory;
	
	public InkConvertingRecipeSerializer(InkConvertingRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		InkConvertingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, InkColor inkColor, long inkAmount);
	}
	
	@Override
	public InkConvertingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		
		String inkColorString = JsonHelper.getString(jsonObject, "ink_color");
		Optional<InkColor> inkColor = InkColor.ofIdString(inkColorString);
		if (inkColor.isEmpty()) {
			throw new JsonParseException("InkColor " + inkColorString + " in Ink Converting recipe " + identifier + " does not exist.");
		}
		long inkAmount = JsonHelper.getLong(jsonObject, "amount");
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, inkColor.get(), inkAmount);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, InkConvertingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeIdentifier(recipe.color.getID());
		packetByteBuf.writeLong(recipe.amount);
	}
	
	@Override
	public InkConvertingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		InkColor inkColor = InkColor.ofId(packetByteBuf.readIdentifier()).get();
		long inkAmount = packetByteBuf.readLong();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, inkColor, inkAmount);
	}
	
}
