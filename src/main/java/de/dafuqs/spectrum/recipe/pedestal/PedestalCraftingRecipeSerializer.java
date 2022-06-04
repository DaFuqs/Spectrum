package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.mixin.accessors.ShapedRecipeAccessor;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PedestalCraftingRecipeSerializer implements RecipeSerializer<PedestalCraftingRecipe> {

	public final PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory;

	public PedestalCraftingRecipeSerializer(PedestalCraftingRecipeSerializer.RecipeFactory<PedestalCraftingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public PedestalCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
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
		if(JsonHelper.hasPrimitive(jsonObject, "disable_yield_upgrades")) {
			noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		}

		HashMap<BuiltinGemstoneColor, Integer> gemInputs = new HashMap<>();
		if(JsonHelper.hasPrimitive(jsonObject, "cyan")) {
			int amount = JsonHelper.getInt(jsonObject, "cyan", 0);
			gemInputs.put(BuiltinGemstoneColor.CYAN, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "magenta")) {
			int amount = JsonHelper.getInt(jsonObject, "magenta", 0);
			gemInputs.put(BuiltinGemstoneColor.MAGENTA, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "yellow")) {
			int amount = JsonHelper.getInt(jsonObject, "yellow", 0);
			gemInputs.put(BuiltinGemstoneColor.YELLOW, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "white")) {
			int amount = JsonHelper.getInt(jsonObject, "white", 0);
			gemInputs.put(BuiltinGemstoneColor.WHITE, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "black")) {
			int amount = JsonHelper.getInt(jsonObject, "black", 0);
			gemInputs.put(BuiltinGemstoneColor.BLACK, amount);
		}
		
		boolean skipRecipeRemainders = false;
		if(JsonHelper.hasBoolean(jsonObject, "skip_recipe_remainders")) {
			skipRecipeRemainders = JsonHelper.getBoolean(jsonObject, "skip_recipe_remainders", false);
		}
		
		Identifier requiredAdvancementIdentifier;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = PedestalBlock.UNLOCK_IDENTIFIER;
		}

		return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifier);
	}

	@Override
	public void write(PacketByteBuf packetByteBuf, PedestalCraftingRecipe pedestalRecipe) {
		packetByteBuf.writeInt(pedestalRecipe.width);
		packetByteBuf.writeInt(pedestalRecipe.height);
		packetByteBuf.writeString(pedestalRecipe.group);

		for (Ingredient ingredient : pedestalRecipe.craftingInputs) {
			ingredient.write(packetByteBuf);
		}

		packetByteBuf.writeItemStack(pedestalRecipe.output);

		packetByteBuf.writeInt(pedestalRecipe.tier.ordinal());
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.MAGENTA));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.CYAN));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.YELLOW));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.BLACK));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.WHITE));

		packetByteBuf.writeFloat(pedestalRecipe.experience);
		packetByteBuf.writeInt(pedestalRecipe.craftingTime);
		packetByteBuf.writeBoolean(pedestalRecipe.skipRecipeRemainders);
		packetByteBuf.writeBoolean(pedestalRecipe.noBenefitsFromYieldUpgrades);
		packetByteBuf.writeIdentifier(pedestalRecipe.requiredAdvancementIdentifier);
	}
	
	
	@Override
	public PedestalCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		int width = packetByteBuf.readInt();
		int height = packetByteBuf.readInt();
		String group = packetByteBuf.readString();
		DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		
		for(int k = 0; k < craftingInputs.size(); ++k) {
			craftingInputs.set(k, Ingredient.fromPacket(packetByteBuf));
		}
		ItemStack output = packetByteBuf.readItemStack();
		
		PedestalRecipeTier tier = PedestalRecipeTier.values()[packetByteBuf.readInt()];
		
		int magenta = packetByteBuf.readInt();
		int cyan = packetByteBuf.readInt();
		int yellow = packetByteBuf.readInt();
		int black = packetByteBuf.readInt();
		int white = packetByteBuf.readInt();
		
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean skipRecipeRemainders = packetByteBuf.readBoolean();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		HashMap<BuiltinGemstoneColor, Integer> gemInputs = new HashMap<>();
		if(magenta > 0) { gemInputs.put(BuiltinGemstoneColor.MAGENTA, magenta); }
		if(cyan	   > 0) { gemInputs.put(BuiltinGemstoneColor.CYAN, cyan); }
		if(yellow  > 0) { gemInputs.put(BuiltinGemstoneColor.YELLOW, yellow); }
		if(black   > 0) { gemInputs.put(BuiltinGemstoneColor.BLACK, black); }
		if(white   > 0) { gemInputs.put(BuiltinGemstoneColor.WHITE, white); }
		
		return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifier);
	}

	public interface RecipeFactory<PedestalCraftingRecipe> {
		PedestalCraftingRecipe create(Identifier id, String group, PedestalRecipeTier tier, int width, int height,
		         DefaultedList<Ingredient> craftingInputs, HashMap<BuiltinGemstoneColor, Integer> gemInputs,
		         ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades, Identifier requiredAdvancementIdentifier);
	}

}
