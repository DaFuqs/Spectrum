package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TitrationBarrelRecipeSerializer implements RecipeSerializer<TitrationBarrelRecipe> {
	
	public final TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory;
	
	public TitrationBarrelRecipeSerializer(TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> ingredients = RecipeUtils.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		int minTimeDays = JsonHelper.getInt(jsonObject, "min_fermentation_time_hours", 24);
		
		Item tappingItem = Items.AIR;
		if(JsonHelper.hasString(jsonObject, "tapping_item")) {
			tappingItem = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "tapping_item")));
		}
		
		TitrationBarrelRecipe.FermentationData fermentationData = null;
		if(JsonHelper.hasJsonObject(jsonObject, "fermentation_data")) {
			fermentationData = TitrationBarrelRecipe.FermentationData.fromJson(JsonHelper.getObject(jsonObject, "fermentation_data"));
		}
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = TitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, ingredients, outputItemStack, tappingItem, minTimeDays, fermentationData, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, TitrationBarrelRecipe titrationBarrelRecipe) {
		packetByteBuf.writeString(titrationBarrelRecipe.group);
		packetByteBuf.writeShort(titrationBarrelRecipe.inputStacks.size());
		for (IngredientStack ingredientStack : titrationBarrelRecipe.inputStacks) {
			ingredientStack.write(packetByteBuf);
		}
		packetByteBuf.writeItemStack(titrationBarrelRecipe.outputItemStack);
		packetByteBuf.writeString(Registry.ITEM.getId(titrationBarrelRecipe.tappingItem).toString());
		packetByteBuf.writeInt(titrationBarrelRecipe.minFermentationTimeHours);
		
		titrationBarrelRecipe.fermentationData.write(packetByteBuf);
		
		packetByteBuf.writeIdentifier(titrationBarrelRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		short craftingInputCount = packetByteBuf.readShort();
		List<IngredientStack> ingredients = IngredientStack.decodeByteBuf(packetByteBuf, craftingInputCount);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		Item tappingItem = Registry.ITEM.get(Identifier.tryParse(packetByteBuf.readString()));
		int minTimeDays = packetByteBuf.readInt();
		
		TitrationBarrelRecipe.FermentationData fermentationData = TitrationBarrelRecipe.FermentationData.read(packetByteBuf);
		
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, ingredients, outputItemStack, tappingItem, minTimeDays, fermentationData, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<TitrationBarrelRecipe> {
		TitrationBarrelRecipe create(Identifier id, String group, List<IngredientStack> ingredients, ItemStack outputItemStack, Item tappingItem, int minTimeDays, de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe.FermentationData fermentationData, Identifier requiredAdvancementIdentifier);
	}
	
}
