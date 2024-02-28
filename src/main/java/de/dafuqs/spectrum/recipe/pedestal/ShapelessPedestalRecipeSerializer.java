package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

import java.util.*;

public class ShapelessPedestalRecipeSerializer extends PedestalRecipeSerializer<ShapelessPedestalRecipe> {
	
	public final ShapelessPedestalRecipeSerializer.RecipeFactory recipeFactory;
	
	public ShapelessPedestalRecipeSerializer(ShapelessPedestalRecipeSerializer.RecipeFactory recipeFactory) {
		super();
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		ShapelessPedestalRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier,
									   List<IngredientStack> inputs, Map<GemstoneColor, Integer> powderInputs,
									   ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public ShapelessPedestalRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		PedestalRecipeTier tier = PedestalRecipeTier.valueOf(JsonHelper.getString(jsonObject, "tier", "basic").toUpperCase(Locale.ROOT));
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		boolean noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		Map<GemstoneColor, Integer> gemInputs = readGemstonePowderInputs(jsonObject);
		
		boolean skipRecipeRemainders = false;
		if (JsonHelper.hasBoolean(jsonObject, "skip_recipe_remainders")) {
			skipRecipeRemainders = JsonHelper.getBoolean(jsonObject, "skip_recipe_remainders", false);
		}
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> inputs = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		if (inputs.size() > 9) {
			throw new JsonParseException("Recipe cannot have more than 9 ingredients. Has " + inputs.size());
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, inputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, ShapelessPedestalRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		packetByteBuf.writeInt(recipe.tier.ordinal());
		packetByteBuf.writeInt(recipe.inputs.size());
		for (IngredientStack ingredient : recipe.inputs) {
			ingredient.write(packetByteBuf);
		}
		writeGemstonePowderInputs(packetByteBuf, recipe);
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeBoolean(recipe.skipRecipeRemainders);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public ShapelessPedestalRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		PedestalRecipeTier tier = PedestalRecipeTier.values()[packetByteBuf.readInt()];
		int inputCount = packetByteBuf.readInt();
		List<IngredientStack> inputs = IngredientStack.decodeByteBuf(packetByteBuf, inputCount);
		Map<GemstoneColor, Integer> gemInputs = readGemstonePowderInputs(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean skipRecipeRemainders = packetByteBuf.readBoolean();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, inputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
}
