package de.dafuqs.spectrum.recipe.pedestal;

import com.google.common.collect.*;
import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

import java.util.*;

public class PedestalCraftingRecipeSerializer implements GatedRecipeSerializer<PedestalCraftingRecipe> {
	
	public final PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory;
	
	public PedestalCraftingRecipeSerializer(PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PedestalCraftingRecipe> {
		PedestalCraftingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier, int width, int height,
		                              DefaultedList<IngredientStack> craftingInputs, HashMap<BuiltinGemstoneColor, Integer> gemInputs,
		                              ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public PedestalCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Map<String, IngredientStack> map = readIngredientStackSymbols(JsonHelper.getObject(jsonObject, "key"));
		String[] strings = ShapedRecipeAccessor.invokeRemovePadding(ShapedRecipeAccessor.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
		int width = strings[0].length();
		int height = strings.length;
		DefaultedList<IngredientStack> craftingInputs = createIngredientStackPatternMatrix(strings, map, width, height);
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
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.CYAN, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "magenta", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.MAGENTA, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "yellow", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.YELLOW, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "black", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.BLACK, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "white", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.WHITE, amount);
		}
		
		boolean skipRecipeRemainders = false;
		if (JsonHelper.hasBoolean(jsonObject, "skip_recipe_remainders")) {
			skipRecipeRemainders = JsonHelper.getBoolean(jsonObject, "skip_recipe_remainders", false);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	static DefaultedList<IngredientStack> createIngredientStackPatternMatrix(String[] pattern, Map<String, IngredientStack> symbols, int width, int height) {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.ofSize(width * height, IngredientStack.EMPTY);
		Set<String> set = Sets.newHashSet(symbols.keySet());
		set.remove(" ");
		
		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				var ingredient = symbols.get(string);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
				}
				
				set.remove(string);
				defaultedList.set(j + width * i, ingredient);
			}
		}
		
		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return defaultedList;
		}
	}
	
	static Map<String, IngredientStack> readIngredientStackSymbols(JsonObject json) {
		Map<String, IngredientStack> map = Maps.newHashMap();
		Iterator var2 = json.entrySet().iterator();
		
		while (var2.hasNext()) {
			Map.Entry<String, JsonElement> entry = (Map.Entry) var2.next();
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}
			
			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}
			
			map.put(entry.getKey(), RecipeParser.ingredientStackFromJson((JsonObject) entry.getValue()));
		}
		
		map.put(" ", IngredientStack.EMPTY);
		return map;
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PedestalCraftingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeInt(recipe.width);
		packetByteBuf.writeInt(recipe.height);
		
		for (IngredientStack ingredient : recipe.craftingInputs) {
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
		DefaultedList<IngredientStack> craftingInputs = DefaultedList.ofSize(width * height, IngredientStack.EMPTY);
		
		for (int k = 0; k < craftingInputs.size(); ++k) {
			try {
				craftingInputs.set(k, IngredientStack.fromByteBuf(packetByteBuf));
			} catch (Exception e) {
				SpectrumCommon.logError("!");
			}
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
