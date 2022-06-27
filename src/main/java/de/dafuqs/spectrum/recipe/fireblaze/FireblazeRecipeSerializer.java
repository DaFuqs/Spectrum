package de.dafuqs.spectrum.recipe.fireblaze;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FireblazeRecipeSerializer implements RecipeSerializer<FireblazeRecipe> {
	
	public final RecipeFactory<FireblazeRecipe> recipeFactory;
	
	public FireblazeRecipeSerializer(RecipeFactory<FireblazeRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public FireblazeRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		Ingredient inputIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
		int time = JsonHelper.getInt(jsonObject, "time");
		float experience = JsonHelper.getFloat(jsonObject, "experience");
		
		List<Pair<ItemStack, Float>> outputsWithChance = new ArrayList<>();
		for(JsonElement outputEntry : JsonHelper.getArray(jsonObject, "results")) {
			JsonObject outputObject = outputEntry.getAsJsonObject();
			ItemStack outputStack = RecipeUtils.itemStackWithNbtFromJson(outputObject);
			float outputChance = 1.0F;
			if(JsonHelper.hasNumber(outputObject, "chance")) {
				outputChance = JsonHelper.getFloat(outputObject, "chance");
			}
			outputsWithChance.add(new Pair<>(outputStack, outputChance));
		}
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// Recipe has no unlock advancement set. Will be set to the unlock advancement of the Enchanter itself
			requiredAdvancementIdentifier = FireblazeRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, inputIngredient, time, experience, outputsWithChance, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, FireblazeRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeInt(recipe.time);
		packetByteBuf.writeFloat(recipe.experience);

		packetByteBuf.writeInt(recipe.outputsWithChance.size());
		for(Pair<ItemStack, Float> output : recipe.outputsWithChance) {
			packetByteBuf.writeItemStack(output.getLeft());
			packetByteBuf.writeFloat(output.getRight());
		}
		
		packetByteBuf.writeIdentifier(recipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public FireblazeRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient inputIngredient = Ingredient.fromPacket(packetByteBuf);
		int time = packetByteBuf.readInt();
		float experience = packetByteBuf.readFloat();
		
		int outputCount = packetByteBuf.readInt();
		List<Pair<ItemStack, Float>> outputsWithChance = new ArrayList<>(outputCount);
		for(int i = 0; i < outputCount; i++) {
			outputsWithChance.add(new Pair<>(packetByteBuf.readItemStack(), packetByteBuf.readFloat()));
		}
		
		@Nullable Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, inputIngredient, time, experience, outputsWithChance, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<FireblazeRecipe> {
		FireblazeRecipe create(Identifier id, String group, Ingredient inputIngredient, int time, float experience, List<Pair<ItemStack, Float>> outputsWithChance, Identifier requiredAdvancementIdentifier);
	}
	
}
