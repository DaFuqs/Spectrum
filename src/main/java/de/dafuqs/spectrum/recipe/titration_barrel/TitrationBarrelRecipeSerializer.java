package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.FluidInput;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.id.incubus_core.util.RegistryHelper;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.*;

import java.util.*;

public class TitrationBarrelRecipeSerializer implements GatedRecipeSerializer<TitrationBarrelRecipe> {
	
	public final TitrationBarrelRecipeSerializer.RecipeFactory recipeFactory;
	
	public TitrationBarrelRecipeSerializer(TitrationBarrelRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		TitrationBarrelRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> ingredients, FluidInput fluid, ItemStack outputItemStack, Item tappingItem, int minTimeDays, FermentationData fermentationData);
	}
	
	@Override
	public TitrationBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> ingredients = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		
		FluidInput fluidInput = FluidInput.EMPTY;
		if (JsonHelper.hasString(jsonObject, "fluid")) {
			Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
			Fluid fluid = Registries.FLUID.get(fluidIdentifier);
			if (fluid.getDefaultState().isEmpty()) {
				Optional<TagKey<Fluid>> tag = RegistryHelper.tryGetTagKey(Registries.FLUID, fluidIdentifier);
				if (tag.isEmpty()) {
					SpectrumCommon.logError("Titration Recipe " + identifier + " specifies fluid " + fluidIdentifier + " that does not exist! This recipe will not be craftable.");
				} else {
					fluidInput = FluidInput.of(tag.get());
				}
			} else {
				fluidInput = FluidInput.of(fluid);
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
		writeNullableIdentifier(packetByteBuf, recipe.fluid.id());
		
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
		
		FluidInput fluidInput = FluidInput.EMPTY;
		Identifier fluidId = readNullableIdentifier(packetByteBuf);
		if (fluidId != null) {
			Fluid fluid = Registries.FLUID.get(fluidId);
			if (fluid != Fluids.EMPTY) {
				fluidInput = FluidInput.of(fluid);
			} else {
				Optional<TagKey<Fluid>> tag = RegistryHelper.tryGetTagKey(Registries.FLUID, fluidId);
				if (tag.isPresent()) fluidInput = FluidInput.of(tag.get());
			}
		}
		
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
