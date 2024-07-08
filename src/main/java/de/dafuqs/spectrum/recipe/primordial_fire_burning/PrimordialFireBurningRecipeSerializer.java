package de.dafuqs.spectrum.recipe.primordial_fire_burning;

import com.google.gson.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

public class PrimordialFireBurningRecipeSerializer implements GatedRecipeSerializer<PrimordialFireBurningRecipe> {
	
	public final RecipeFactory recipeFactory;
	
	public PrimordialFireBurningRecipeSerializer(RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		PrimordialFireBurningRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient ingredient, ItemStack output);
	}
	
	@Override
	public PrimordialFireBurningRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, output);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PrimordialFireBurningRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.input.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.output);
	}
	
	@Override
	public PrimordialFireBurningRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, output);
	}
	
}
