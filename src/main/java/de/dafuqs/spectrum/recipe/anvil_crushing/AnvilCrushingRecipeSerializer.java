package de.dafuqs.spectrum.recipe.anvil_crushing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class AnvilCrushingRecipeSerializer implements RecipeSerializer<AnvilCrushingRecipe> {
	
	public final AnvilCrushingRecipeSerializer.RecipeFactory<AnvilCrushingRecipe> recipeFactory;
	
	public AnvilCrushingRecipeSerializer(AnvilCrushingRecipeSerializer.RecipeFactory<AnvilCrushingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public AnvilCrushingRecipe read(Identifier identifier, JsonObject jsonObject) {
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		float crushedItemsPerPointOfDamage = JsonHelper.getFloat(jsonObject, "crushedItemsPerPointOfDamage");
		float experience = JsonHelper.getFloat(jsonObject, "experience");
		
		String particleEffectString = JsonHelper.getString(jsonObject, "particleEffectIdentifier");
		Identifier particleEffectIdentifier = new Identifier(particleEffectString);
		
		int particleCount = 1;
		if (JsonHelper.hasNumber(jsonObject, "particleCount")) {
			particleCount = JsonHelper.getInt(jsonObject, "particleCount");
		}
		
		String soundEventString = JsonHelper.getString(jsonObject, "soundEventIdentifier");
		Identifier soundEventIdentifier = new Identifier(soundEventString);
		
		return this.recipeFactory.create(identifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, particleCount, soundEventIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, AnvilCrushingRecipe anvilCrushingRecipe) {
		anvilCrushingRecipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(anvilCrushingRecipe.outputItemStack);
		packetByteBuf.writeFloat(anvilCrushingRecipe.crushedItemsPerPointOfDamage);
		packetByteBuf.writeFloat(anvilCrushingRecipe.experience);
		packetByteBuf.writeIdentifier(anvilCrushingRecipe.particleEffect);
		packetByteBuf.writeInt(anvilCrushingRecipe.particleCount);
		packetByteBuf.writeIdentifier(anvilCrushingRecipe.soundEvent);
	}
	
	@Override
	public AnvilCrushingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		float crushedItemsPerPointOfDamage = packetByteBuf.readFloat();
		float experience = packetByteBuf.readFloat();
		Identifier particleEffectIdentifier = packetByteBuf.readIdentifier();
		int particleCount = packetByteBuf.readInt();
		Identifier soundEventIdentifier = packetByteBuf.readIdentifier();
		return this.recipeFactory.create(identifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, particleCount, soundEventIdentifier);
	}
	
	
	public interface RecipeFactory<AnvilCrushingRecipe> {
		AnvilCrushingRecipe create(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage, float experience, Identifier particleEffectIdentifier, int particleCount, Identifier soundEventIdentifier);
	}
	
}
