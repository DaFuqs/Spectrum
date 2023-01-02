package de.dafuqs.spectrum.recipe.anvil_crushing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class AnvilCrushingRecipeSerializer implements GatedRecipeSerializer<AnvilCrushingRecipe> {
	
	public final AnvilCrushingRecipeSerializer.RecipeFactory<AnvilCrushingRecipe> recipeFactory;
	
	public AnvilCrushingRecipeSerializer(AnvilCrushingRecipeSerializer.RecipeFactory<AnvilCrushingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<AnvilCrushingRecipe> {
		AnvilCrushingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage, float experience, Identifier particleEffectIdentifier, int particleCount, Identifier soundEventIdentifier);
	}
	
	@Override
	public AnvilCrushingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		float crushedItemsPerPointOfDamage = JsonHelper.getFloat(jsonObject, "crushedItemsPerPointOfDamage");
		float experience = JsonHelper.getFloat(jsonObject, "experience");
		Identifier particleEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "particleEffectIdentifier"));
		
		int particleCount = 1;
		if (JsonHelper.hasNumber(jsonObject, "particleCount")) {
			particleCount = JsonHelper.getInt(jsonObject, "particleCount");
		}
		
		String soundEventString = JsonHelper.getString(jsonObject, "soundEventIdentifier");
		Identifier soundEventIdentifier = new Identifier(soundEventString);
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, particleCount, soundEventIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, AnvilCrushingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.outputItemStack);
		packetByteBuf.writeFloat(recipe.crushedItemsPerPointOfDamage);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeIdentifier(recipe.particleEffectIdentifier);
		packetByteBuf.writeInt(recipe.particleCount);
		packetByteBuf.writeIdentifier(recipe.soundEvent);
	}
	
	@Override
	public AnvilCrushingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		float crushedItemsPerPointOfDamage = packetByteBuf.readFloat();
		float experience = packetByteBuf.readFloat();
		Identifier particleEffectIdentifier = packetByteBuf.readIdentifier();
		int particleCount = packetByteBuf.readInt();
		Identifier soundEventIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, particleCount, soundEventIdentifier);
	}
	
}
