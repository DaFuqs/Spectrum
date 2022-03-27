package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.enums.GemstoneColor;
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

import java.util.*;

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
		ItemStack output = RecipeUtils.outputWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));

		PedestalRecipeTier tier = PedestalRecipeTier.valueOf(JsonHelper.getString(jsonObject, "tier", "basic").toUpperCase(Locale.ROOT));
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		boolean noBenefitsFromYieldUpgrades = false;
		if(JsonHelper.hasPrimitive(jsonObject, "disable_yield_upgrades")) {
			noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		}

		HashMap<GemstoneColor, Integer> gemInputs = new HashMap<>();
		if(JsonHelper.hasPrimitive(jsonObject, "cyan")) {
			int amount = JsonHelper.getInt(jsonObject, "cyan", 0);
			gemInputs.put(GemstoneColor.CYAN, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "magenta")) {
			int amount = JsonHelper.getInt(jsonObject, "magenta", 0);
			gemInputs.put(GemstoneColor.MAGENTA, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "yellow")) {
			int amount = JsonHelper.getInt(jsonObject, "yellow", 0);
			gemInputs.put(GemstoneColor.YELLOW, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "white")) {
			int amount = JsonHelper.getInt(jsonObject, "white", 0);
			gemInputs.put(GemstoneColor.WHITE, amount);
		}
		if(JsonHelper.hasPrimitive(jsonObject, "black")) {
			int amount = JsonHelper.getInt(jsonObject, "black", 0);
			gemInputs.put(GemstoneColor.BLACK, amount);
		}
		
		boolean skipRecipeRemainders = false;
		if(JsonHelper.hasBoolean(jsonObject, "skip_recipe_remainders")) {
			skipRecipeRemainders = JsonHelper.getBoolean(jsonObject, "skip_recipe_remainders", false);
		}

		List<Identifier> requiredAdvancementIdentifiers = new ArrayList<>();
		if(JsonHelper.hasArray(jsonObject, "required_advancements")) {
			JsonArray requiredAdvancementsArray = JsonHelper.getArray(jsonObject, "required_advancements");
			for(int i = 0; i < requiredAdvancementsArray.size(); i++) {
				Identifier requiredAdvancementIdentifier = Identifier.tryParse(requiredAdvancementsArray.get(i).getAsString());
				requiredAdvancementIdentifiers.add(requiredAdvancementIdentifier);
			}
		} else {
			// Recipe has no unlock advancement set. Will be set to the unlock advancement of the Enchanter itself
			requiredAdvancementIdentifiers.add(PedestalBlock.UNLOCK_IDENTIFIER);
		}

		return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifiers);
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
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(GemstoneColor.MAGENTA));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(GemstoneColor.CYAN));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(GemstoneColor.YELLOW));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(GemstoneColor.BLACK));
		packetByteBuf.writeInt(pedestalRecipe.getGemstonePowderAmount(GemstoneColor.WHITE));

		packetByteBuf.writeFloat(pedestalRecipe.experience);
		packetByteBuf.writeInt(pedestalRecipe.craftingTime);
		packetByteBuf.writeBoolean(pedestalRecipe.skipRecipeRemainders);
		packetByteBuf.writeBoolean(pedestalRecipe.noBenefitsFromYieldUpgrades);

		packetByteBuf.writeInt(pedestalRecipe.requiredAdvancementIdentifiers.size());
		for(int i = 0; i < pedestalRecipe.requiredAdvancementIdentifiers.size(); i++) {
			packetByteBuf.writeIdentifier(pedestalRecipe.requiredAdvancementIdentifiers.get(i));
		}
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
		
		int requiredAdvancementAmount = packetByteBuf.readInt();
		List<Identifier> requiredAdvancementIdentifiers = new ArrayList<>();
		for(int i = 0; i < requiredAdvancementAmount; i++) {
			requiredAdvancementIdentifiers.add(packetByteBuf.readIdentifier());
		}
		
		HashMap<GemstoneColor, Integer> gemInputs = new HashMap<>();
		if(magenta > 0) { gemInputs.put(GemstoneColor.MAGENTA, magenta); }
		if(cyan	> 0) { gemInputs.put(GemstoneColor.CYAN, cyan); }
		if(yellow  > 0) { gemInputs.put(GemstoneColor.YELLOW, yellow); }
		if(black   > 0) { gemInputs.put(GemstoneColor.BLACK, black); }
		if(white   > 0) { gemInputs.put(GemstoneColor.WHITE, white); }
		
		return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifiers);
	}

	public interface RecipeFactory<PedestalCraftingRecipe> {
		PedestalCraftingRecipe create(Identifier id, String group, PedestalRecipeTier tier, int width, int height,
		         DefaultedList<Ingredient> craftingInputs, HashMap<GemstoneColor, Integer> gemInputs,
		         ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades, List<Identifier> requiredAdvancementIdentifiers);
	}

}
