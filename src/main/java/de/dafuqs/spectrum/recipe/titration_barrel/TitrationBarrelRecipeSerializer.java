package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TitrationBarrelRecipeSerializer implements GatedRecipeSerializer<TitrationBarrelRecipe> {
	
	public final TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory;
	
	public TitrationBarrelRecipeSerializer(TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<TitrationBarrelRecipe> {
		TitrationBarrelRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> ingredients, ItemStack outputItemStack, Item tappingItem, int minTimeDays, de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe.FermentationData fermentationData);
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
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
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, outputItemStack, tappingItem, minTimeDays, fermentationData);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, TitrationBarrelRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeShort(recipe.inputStacks.size());
		for (IngredientStack ingredientStack : recipe.inputStacks) {
			ingredientStack.write(packetByteBuf);
		}
		packetByteBuf.writeItemStack(recipe.outputItemStack);
		packetByteBuf.writeString(Registry.ITEM.getId(recipe.tappingItem).toString());
		packetByteBuf.writeInt(recipe.minFermentationTimeHours);
		
		if(recipe.fermentationData == null) {
			packetByteBuf.writeBoolean(false);
		} else {
			packetByteBuf.writeBoolean(true);
			recipe.fermentationData.write(packetByteBuf);
		}
		
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		short craftingInputCount = packetByteBuf.readShort();
		List<IngredientStack> ingredients = IngredientStack.decodeByteBuf(packetByteBuf, craftingInputCount);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		Item tappingItem = Registry.ITEM.get(Identifier.tryParse(packetByteBuf.readString()));
		int minTimeDays = packetByteBuf.readInt();
		
		TitrationBarrelRecipe.FermentationData fermentationData = null;
		if(packetByteBuf.readBoolean()) {
			fermentationData = TitrationBarrelRecipe.FermentationData.read(packetByteBuf);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, outputItemStack, tappingItem, minTimeDays, fermentationData);
	}
	
}
