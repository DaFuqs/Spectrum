package de.dafuqs.spectrum.recipe.enchanter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlock;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class EnchanterRecipeSerializer implements RecipeSerializer<EnchanterRecipe> {
	
	public final EnchanterRecipeSerializer.RecipeFactory<EnchanterRecipe> recipeFactory;
	
	public EnchanterRecipeSerializer(EnchanterRecipeSerializer.RecipeFactory<EnchanterRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public EnchanterRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
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
		
		Identifier requiredAdvancementIdentifier = null;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// Recipe has no unlock advancement set. Will be set to the unlock advancement of the Enchanter itself
			requiredAdvancementIdentifier = EnchanterBlock.UNLOCK_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, craftingInputs, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, EnchanterRecipe enchanterRecipe) {
		packetByteBuf.writeString(enchanterRecipe.group);
		
		packetByteBuf.writeShort(enchanterRecipe.inputs.size());
		for (Ingredient ingredient : enchanterRecipe.inputs) {
			ingredient.write(packetByteBuf);
		}
		
		packetByteBuf.writeItemStack(enchanterRecipe.output);
		packetByteBuf.writeInt(enchanterRecipe.craftingTime);
		packetByteBuf.writeInt(enchanterRecipe.requiredExperience);
		packetByteBuf.writeBoolean(enchanterRecipe.noBenefitsFromYieldAndEfficiencyUpgrades);
		packetByteBuf.writeIdentifier(enchanterRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public EnchanterRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		
		short craftingInputCount = packetByteBuf.readShort();
		DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(craftingInputCount, Ingredient.EMPTY);
		for (short i = 0; i < craftingInputCount; i++) {
			ingredients.set(i, Ingredient.fromPacket(packetByteBuf));
		}
		
		ItemStack output = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		int requiredExperience = packetByteBuf.readInt();
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, ingredients, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<EnchanterRecipe> {
		EnchanterRecipe create(Identifier id, String group, DefaultedList<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, @Nullable Identifier requiredAdvancementIdentifier);
	}
	
}
