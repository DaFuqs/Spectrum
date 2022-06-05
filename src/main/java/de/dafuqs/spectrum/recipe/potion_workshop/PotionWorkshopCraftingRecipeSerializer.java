package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlock;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class PotionWorkshopCraftingRecipeSerializer implements RecipeSerializer<PotionWorkshopCraftingRecipe> {
	
	public final PotionWorkshopCraftingRecipeSerializer.RecipeFactory<PotionWorkshopCraftingRecipe> recipeFactory;
	
	public PotionWorkshopCraftingRecipeSerializer(PotionWorkshopCraftingRecipeSerializer.RecipeFactory<PotionWorkshopCraftingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public PotionWorkshopCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		Ingredient baseIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base_ingredient"));
		Ingredient ingredient1 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		Ingredient ingredient2;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient2")) {
			ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		} else {
			ingredient2 = Ingredient.EMPTY;
		}
		Ingredient ingredient3;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient3")) {
			ingredient3 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient3"));
		} else {
			ingredient3 = Ingredient.EMPTY;
		}
		
		int requiredExperience = JsonHelper.getInt(jsonObject, "required_experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		int color = JsonHelper.getInt(jsonObject, "color", 0xc03058);
		boolean consumeBaseIngredient = JsonHelper.getBoolean(jsonObject, "use_up_base_ingredient", false);
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// Potion Workshop Recipe has no unlock advancement set. Will be set to the unlock advancement of the Potion Workshop itself
			requiredAdvancementIdentifier = PotionWorkshopBlock.UNLOCK_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, baseIngredient, consumeBaseIngredient, requiredExperience, ingredient1, ingredient2, ingredient3, output, craftingTime, color, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopCraftingRecipe potionWorkshopCraftingRecipe) {
		packetByteBuf.writeString(potionWorkshopCraftingRecipe.group);
		potionWorkshopCraftingRecipe.baseIngredient.write(packetByteBuf);
		packetByteBuf.writeBoolean(potionWorkshopCraftingRecipe.consumeBaseIngredient);
		packetByteBuf.writeInt(potionWorkshopCraftingRecipe.requiredExperience);
		potionWorkshopCraftingRecipe.ingredient1.write(packetByteBuf);
		potionWorkshopCraftingRecipe.ingredient2.write(packetByteBuf);
		potionWorkshopCraftingRecipe.ingredient3.write(packetByteBuf);
		packetByteBuf.writeItemStack(potionWorkshopCraftingRecipe.output);
		packetByteBuf.writeInt(potionWorkshopCraftingRecipe.craftingTime);
		packetByteBuf.writeInt(potionWorkshopCraftingRecipe.color);
		packetByteBuf.writeIdentifier(potionWorkshopCraftingRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public PotionWorkshopCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient baseIngredient = Ingredient.fromPacket(packetByteBuf);
		boolean consumeBaseIngredient = packetByteBuf.readBoolean();
		int requiredExperience = packetByteBuf.readInt();
		Ingredient ingredient1 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		int color = packetByteBuf.readInt();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		return this.recipeFactory.create(identifier, group, baseIngredient, consumeBaseIngredient, requiredExperience, ingredient1, ingredient2, ingredient3, output, craftingTime, color, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<PotionWorkshopCraftingRecipe> {
		PotionWorkshopCraftingRecipe create(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, int requiredExperience, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, int color, Identifier requiredAdvancementIdentifier);
	}
	
}
