package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

public class TitrationBarrelRecipeSerializer implements GatedRecipeSerializer<TitrationBarrelRecipe> {
	
	public final TitrationBarrelRecipeSerializer.RecipeFactory recipeFactory;
	
	public TitrationBarrelRecipeSerializer(TitrationBarrelRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		TitrationBarrelRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> ingredients, FluidIngredient fluid, ItemStack outputItemStack, Item tappingItem, int minTimeDays, FermentationData fermentationData);
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> ingredients = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());

		FluidIngredient fluidInput = FluidIngredient.EMPTY;
		if (JsonHelper.hasJsonObject(jsonObject, "fluid")) {
			JsonObject fluidObject = JsonHelper.getObject(jsonObject, "fluid");
			FluidIngredient.JsonParseResult result = FluidIngredient.fromJson(fluidObject);
			fluidInput = result.result();
			if (result.malformed()) {
				// Currently handling malformed input leniently. May throw an error in the future.
				SpectrumCommon.logError("Titration Recipe " + identifier + "contains a malformed fluid input tag! This recipe will not be craftable.");
			} else if (result.result() == FluidIngredient.EMPTY) {
				if (result.isTag()) {
					SpectrumCommon.logError("Titration Recipe " + identifier + " specifies fluid tag " + result.id() + " that does not exist! This recipe will not be craftable.");
				} else {
					SpectrumCommon.logError("Titration Recipe " + identifier + " specifies fluid " + result.id() + " that does not exist! This recipe will not be craftable.");
				}
			}
		}

		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		int minTimeDays = JsonHelper.getInt(jsonObject, "min_fermentation_time_hours", 24);
		
		Item tappingItem = Items.AIR;
		if (JsonHelper.hasString(jsonObject, "tapping_item")) {
			tappingItem = Registries.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "tapping_item")));
		}
		
		FermentationData fermentationData = null;
		if (JsonHelper.hasJsonObject(jsonObject, "fermentation_data")) {
			fermentationData = FermentationData.fromJson(JsonHelper.getObject(jsonObject, "fermentation_data"));
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, fluidInput, outputItemStack, tappingItem, minTimeDays, fermentationData);
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
		writeFluidIngredient(packetByteBuf, recipe.fluid);
		
		packetByteBuf.writeItemStack(recipe.outputItemStack);
		packetByteBuf.writeString(Registries.ITEM.getId(recipe.tappingItem).toString());
		packetByteBuf.writeInt(recipe.minFermentationTimeHours);
		
		if (recipe.fermentationData == null) {
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

		FluidIngredient fluidInput = readFluidIngredient(packetByteBuf);
		
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		Item tappingItem = Registries.ITEM.get(Identifier.tryParse(packetByteBuf.readString()));
		int minTimeDays = packetByteBuf.readInt();
		
		FermentationData fermentationData = null;
		if (packetByteBuf.readBoolean()) {
			fermentationData = FermentationData.read(packetByteBuf);
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, fluidInput, outputItemStack, tappingItem, minTimeDays, fermentationData);
	}
	
}
