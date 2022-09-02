package de.dafuqs.spectrum.recipe.enchanter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class EnchanterRecipeSerializer implements GatedRecipeSerializer<EnchanterRecipe> {
	
	public final EnchanterRecipeSerializer.RecipeFactory<EnchanterRecipe> recipeFactory;
	
	public EnchanterRecipeSerializer(EnchanterRecipeSerializer.RecipeFactory<EnchanterRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<EnchanterRecipe> {
		EnchanterRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, DefaultedList<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public EnchanterRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(ingredientArray.size());
		for (int i = 0; i < ingredientArray.size(); i++) {
			craftingInputs.add(Ingredient.fromJson(ingredientArray.get(i)));
		}
		
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		int requiredExperience = JsonHelper.getInt(jsonObject, "required_experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = false;
		if (JsonHelper.hasPrimitive(jsonObject, "disable_yield_and_efficiency_upgrades")) {
			noBenefitsFromYieldAndEfficiencyUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_and_efficiency_upgrades", false);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingInputs, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, EnchanterRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeShort(recipe.inputs.size());
		for (Ingredient ingredient : recipe.inputs) {
			ingredient.write(packetByteBuf);
		}
		
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeInt(recipe.requiredExperience);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
	@Override
	public EnchanterRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		short craftingInputCount = packetByteBuf.readShort();
		DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(craftingInputCount, Ingredient.EMPTY);
		for (short i = 0; i < craftingInputCount; i++) {
			ingredients.set(i, Ingredient.fromPacket(packetByteBuf));
		}
		
		ItemStack output = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		int requiredExperience = packetByteBuf.readInt();
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades);
	}
	
}
