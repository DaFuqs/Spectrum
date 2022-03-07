package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SpiritInstillerRecipeSerializer implements RecipeSerializer<SpiritInstillerRecipe> {

	public final SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory;

	public SpiritInstillerRecipeSerializer(SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public SpiritInstillerRecipe read(Identifier identifier, JsonObject jsonObject) {
		Ingredient ingredient1 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		Ingredient centerIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "center_ingredient"));
		ItemStack outputItemStack = RecipeUtils.outputWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		float experience = JsonHelper.getFloat(jsonObject, "experience");
		
		return this.recipeFactory.create(identifier, ingredient1, ingredient2, centerIngredient, outputItemStack, experience);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, SpiritInstillerRecipe spiritInstillerRecipe) {
		spiritInstillerRecipe.inputIngredient1.write(packetByteBuf);
		spiritInstillerRecipe.inputIngredient2.write(packetByteBuf);
		spiritInstillerRecipe.centerIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(spiritInstillerRecipe.outputItemStack);
		packetByteBuf.writeFloat(spiritInstillerRecipe.experience);
	}
	
	@Override
	public SpiritInstillerRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Ingredient ingredient1 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
		Ingredient centerIngredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		return this.recipeFactory.create(identifier, ingredient1, ingredient2, centerIngredient, outputItemStack, experience);
	}

	
	public interface RecipeFactory<SpiritInstillerRecipe> {
		SpiritInstillerRecipe create(Identifier id, Ingredient inputIngredient1, Ingredient inputIngredient2, Ingredient centerIngredient, ItemStack outputItemStack, float experience);
	}

}
