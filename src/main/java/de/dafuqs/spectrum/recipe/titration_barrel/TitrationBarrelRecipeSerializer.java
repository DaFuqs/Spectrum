package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class TitrationBarrelRecipeSerializer implements GatedRecipeSerializer<TitrationBarrelRecipe> {
	
	public final TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory;
	
	public TitrationBarrelRecipeSerializer(TitrationBarrelRecipeSerializer.RecipeFactory<TitrationBarrelRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<TitrationBarrelRecipe> {
		TitrationBarrelRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> ingredients, Fluid fluid, ItemStack outputItemStack, Item tappingItem, int minTimeDays, de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe.FermentationData fermentationData);
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> ingredients = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		
		Fluid fluid = Fluids.EMPTY;
		if (JsonHelper.hasString(jsonObject, "fluid")) {
			Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
			fluid = Registry.FLUID.get(fluidIdentifier);
			if (fluid.getDefaultState().isEmpty()) {
				SpectrumCommon.logError("Fusion Shrine Recipe " + identifier + " specifies fluid " + fluidIdentifier + " that does not exist! This recipe will not be craftable.");
			}
		}
		
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
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, fluid, outputItemStack, tappingItem, minTimeDays, fermentationData);
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
		writeNullableIdentifier(packetByteBuf, Registry.FLUID.getId(recipe.fluid));
		
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
		
		Fluid fluid = Fluids.EMPTY;
		Identifier fluidId = readNullableIdentifier(packetByteBuf);
		if(fluidId != null) {
			fluid = Registry.FLUID.get(fluidId);
		}
		
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		Item tappingItem = Registry.ITEM.get(Identifier.tryParse(packetByteBuf.readString()));
		int minTimeDays = packetByteBuf.readInt();
		
		TitrationBarrelRecipe.FermentationData fermentationData = null;
		if(packetByteBuf.readBoolean()) {
			fermentationData = TitrationBarrelRecipe.FermentationData.read(packetByteBuf);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, fluid, outputItemStack, tappingItem, minTimeDays, fermentationData);
	}
	
}
