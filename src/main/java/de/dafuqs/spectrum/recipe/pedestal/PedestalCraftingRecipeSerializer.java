package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.mixin.accessors.ShapedRecipeAccessor;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PedestalCraftingRecipeSerializer implements GatedRecipeSerializer<PedestalCraftingRecipe> {
	
	public final PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory;
	
	public PedestalCraftingRecipeSerializer(PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PedestalCraftingRecipe> {
		PedestalCraftingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier, int width, int height,
		                              DefaultedList<Ingredient> craftingInputs, HashMap<BuiltinGemstoneColor, Integer> gemInputs,
		                              ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public PedestalCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Map<String, Ingredient> map = ShapedRecipeAccessor.invokeReadSymbols(JsonHelper.getObject(jsonObject, "key"));
		String[] strings = ShapedRecipeAccessor.invokeRemovePadding(ShapedRecipeAccessor.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
		int width = strings[0].length();
		int height = strings.length;
		DefaultedList<Ingredient> craftingInputs = ShapedRecipeAccessor.invokeCreatePatternMatrix(strings, map, width, height);
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		PedestalRecipeTier tier = PedestalRecipeTier.valueOf(JsonHelper.getString(jsonObject, "tier", "basic").toUpperCase(Locale.ROOT));
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		boolean noBenefitsFromYieldUpgrades = false;
		if (JsonHelper.hasPrimitive(jsonObject, "disable_yield_upgrades")) {
			noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		}
		
		HashMap<BuiltinGemstoneColor, Integer> gemInputs = new HashMap<>();
		int amount = JsonHelper.getInt(jsonObject, "cyan", 0);
		if(amount > 0) { gemInputs.put(BuiltinGemstoneColor.CYAN, amount); }
		amount = JsonHelper.getInt(jsonObject, "magenta", 0);
		if(amount > 0) { gemInputs.put(BuiltinGemstoneColor.MAGENTA, amount); }
		amount = JsonHelper.getInt(jsonObject, "yellow", 0);
		if(amount > 0) { gemInputs.put(BuiltinGemstoneColor.YELLOW, amount); }
		amount = JsonHelper.getInt(jsonObject, "black", 0);
		if(amount > 0) { gemInputs.put(BuiltinGemstoneColor.BLACK, amount); }
		amount = JsonHelper.getInt(jsonObject, "white", 0);
		if(amount > 0) { gemInputs.put(BuiltinGemstoneColor.WHITE, amount); }
		
		boolean skipRecipeRemainders = false;
		if (JsonHelper.hasBoolean(jsonObject, "skip_recipe_remainders")) {
			skipRecipeRemainders = JsonHelper.getBoolean(jsonObject, "skip_recipe_remainders", false);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PedestalCraftingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeInt(recipe.width);
		packetByteBuf.writeInt(recipe.height);
		
		for (Ingredient ingredient : recipe.craftingInputs) {
			ingredient.write(packetByteBuf);
		}
		
		packetByteBuf.writeItemStack(recipe.output);
		
		packetByteBuf.writeInt(recipe.tier.ordinal());
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.CYAN));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.MAGENTA));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.YELLOW));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.BLACK));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.WHITE));
		
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeBoolean(recipe.skipRecipeRemainders);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldUpgrades);
	}
	
	
	@Override
	public PedestalCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		int width = packetByteBuf.readInt();
		int height = packetByteBuf.readInt();
		DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		
		for (int k = 0; k < craftingInputs.size(); ++k) {
			craftingInputs.set(k, Ingredient.fromPacket(packetByteBuf));
		}
		ItemStack output = packetByteBuf.readItemStack();
		
		PedestalRecipeTier tier = PedestalRecipeTier.values()[packetByteBuf.readInt()];
		
		int cyan = packetByteBuf.readInt();
		int magenta = packetByteBuf.readInt();
		int yellow = packetByteBuf.readInt();
		int black = packetByteBuf.readInt();
		int white = packetByteBuf.readInt();
		
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean skipRecipeRemainders = packetByteBuf.readBoolean();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		
		HashMap<BuiltinGemstoneColor, Integer> gemInputs = new HashMap<>();
		if (cyan > 0) {
			gemInputs.put(BuiltinGemstoneColor.CYAN, cyan);
		}
		if (magenta > 0) {
			gemInputs.put(BuiltinGemstoneColor.MAGENTA, magenta);
		}
		if (yellow > 0) {
			gemInputs.put(BuiltinGemstoneColor.YELLOW, yellow);
		}
		if (black > 0) {
			gemInputs.put(BuiltinGemstoneColor.BLACK, black);
		}
		if (white > 0) {
			gemInputs.put(BuiltinGemstoneColor.WHITE, white);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
}
