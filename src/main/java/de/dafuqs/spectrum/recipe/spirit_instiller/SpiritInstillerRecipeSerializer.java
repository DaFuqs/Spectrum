package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

public class SpiritInstillerRecipeSerializer implements GatedRecipeSerializer<SpiritInstillerRecipe> {
	
	public final SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory;
	
	public SpiritInstillerRecipeSerializer(SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<SpiritInstillerRecipe> {
		SpiritInstillerRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack outputItemStack,
		                             int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public SpiritInstillerRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		IngredientStack centerIngredient = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "center_ingredient"));
		IngredientStack bowlIngredient1 = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		IngredientStack bowlIngredient2 = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		float experience = JsonHelper.getFloat(jsonObject, "experience", 1.0F);
		
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = false;
		if (JsonHelper.hasPrimitive(jsonObject, "disable_yield_and_efficiency_upgrades")) {
			noBenefitsFromYieldAndEfficiencyUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_and_efficiency_upgrades", false);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, centerIngredient, bowlIngredient1, bowlIngredient2, outputItemStack, craftingTime, experience, noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, SpiritInstillerRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.centerIngredient.write(packetByteBuf);
		recipe.bowlIngredient1.write(packetByteBuf);
		recipe.bowlIngredient2.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.outputItemStack);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public SpiritInstillerRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		IngredientStack centerIngredient = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack bowlIngredient1 = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack bowlIngredient2 = IngredientStack.fromByteBuf(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		float experience = packetByteBuf.readFloat();
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, centerIngredient, bowlIngredient1, bowlIngredient2, outputItemStack, craftingTime, experience, noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
}
