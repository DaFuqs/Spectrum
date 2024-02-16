package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

import java.util.*;

public class ShapedPedestalRecipeSerializer extends PedestalRecipeSerializer<ShapedPedestalRecipe> {
	
	public final ShapedPedestalRecipeSerializer.RecipeFactory recipeFactory;
	
	public ShapedPedestalRecipeSerializer(ShapedPedestalRecipeSerializer.RecipeFactory recipeFactory) {
		super();
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		ShapedPedestalRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier, int width, int height,
									List<IngredientStack> inputs, Map<GemstoneColor, Integer> powderInputs,
									ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public ShapedPedestalRecipe read(Identifier identifier, JsonObject jsonObject) {
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
		
		Map<String, IngredientStack> inputMap = RecipeUtils.readIngredientStackSymbols(JsonHelper.getObject(jsonObject, "key"));
		String[] strings = ShapedRecipeAccessor.invokeRemovePadding(ShapedRecipeAccessor.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
		int height = strings.length;
		int width = strings[0].length();
		List<IngredientStack> inputs = RecipeUtils.createIngredientStackPatternMatrix(strings, inputMap, width, height);
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, width, height, inputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, ShapedPedestalRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		packetByteBuf.writeInt(recipe.tier.ordinal());
		packetByteBuf.writeInt(recipe.width);
		packetByteBuf.writeInt(recipe.height);
		for (IngredientStack ingredientStack : recipe.inputs) {
			ingredientStack.write(packetByteBuf);
		}
		writeGemstonePowderInputs(packetByteBuf, recipe);
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeBoolean(recipe.skipRecipeRemainders);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public ShapedPedestalRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		PedestalRecipeTier tier = PedestalRecipeTier.values()[packetByteBuf.readInt()];
		int width = packetByteBuf.readInt();
		int height = packetByteBuf.readInt();
		List<IngredientStack> inputs = IngredientStack.decodeByteBuf(packetByteBuf, width * height);
		Map<GemstoneColor, Integer> gemInputs = readGemstonePowderInputs(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean skipRecipeRemainders = packetByteBuf.readBoolean();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, width, height, inputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
}
