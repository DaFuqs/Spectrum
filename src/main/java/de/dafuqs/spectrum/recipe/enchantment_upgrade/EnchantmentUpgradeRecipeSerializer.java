package de.dafuqs.spectrum.recipe.enchantment_upgrade;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentUpgradeRecipeSerializer<T extends EnchantmentUpgradeRecipe> implements RecipeSerializer<T> {
	
	public static List<EnchantmentUpgradeRecipe> enchantmentUpgradeRecipesToInject = new ArrayList<>();
	
	public final EnchantmentUpgradeRecipeSerializer.RecipeFactory<T> recipeFactory;

	public EnchantmentUpgradeRecipeSerializer(EnchantmentUpgradeRecipeSerializer.RecipeFactory<T> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public T read(Identifier identifier, JsonObject jsonObject) {
		Identifier enchantmentIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "enchantment"));
		
		if(!Registry.ENCHANTMENT.containsId(enchantmentIdentifier)) {
			throw new JsonParseException("Enchantment Upgrade Recipe " + identifier + " has a enchantment set that does not exist or is disabled: " + enchantmentIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing enchantment
		}
		
		Enchantment enchantment = Registry.ENCHANTMENT.get(enchantmentIdentifier);
		Identifier requiredAdvancementIdentifier = null;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		}
		
		JsonArray levelArray = JsonHelper.getArray(jsonObject, "levels");
		int level;
		int requiredExperience;
		Item requiredItem;
		int requiredItemCount;
		T recipe = null;
		for(int i = 0; i < levelArray.size(); i++) {
			JsonObject currentElement = levelArray.get(i).getAsJsonObject();
			level = i + 2;
			requiredExperience = JsonHelper.getInt(currentElement, "experience");
			requiredItem = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(currentElement, "item")));
			requiredItemCount = JsonHelper.getInt(currentElement, "item_count");
			
			recipe = this.recipeFactory.create(new Identifier(SpectrumCommon.MOD_ID, identifier.getPath() + "_level_" + (i + 2)), enchantment, level, requiredExperience, requiredItem, requiredItemCount, requiredAdvancementIdentifier);
			if(!enchantmentUpgradeRecipesToInject.contains(recipe)) {
				enchantmentUpgradeRecipesToInject.add(recipe);
			}
		}
		
		return recipe;
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, T enchantmentUpgradeRecipe) {
		packetByteBuf.writeIdentifier(Registry.ENCHANTMENT.getId(enchantmentUpgradeRecipe.enchantment));
		packetByteBuf.writeInt(enchantmentUpgradeRecipe.enchantmentDestinationLevel);
		packetByteBuf.writeInt(enchantmentUpgradeRecipe.requiredExperience);
		packetByteBuf.writeIdentifier(Registry.ITEM.getId(enchantmentUpgradeRecipe.requiredItem));
		packetByteBuf.writeInt(enchantmentUpgradeRecipe.requiredItemCount);
		packetByteBuf.writeIdentifier(enchantmentUpgradeRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Enchantment enchantment = Registry.ENCHANTMENT.get(packetByteBuf.readIdentifier());
		int enchantmentDestinationLevel = packetByteBuf.readInt();
		int requiredExperience = packetByteBuf.readInt();
		Item requiredItem = Registry.ITEM.get(packetByteBuf.readIdentifier());
		int requiredItemCount = packetByteBuf.readInt();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, enchantment, enchantmentDestinationLevel, requiredExperience, requiredItem, requiredItemCount, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<T extends EnchantmentUpgradeRecipe> {
		T create(Identifier id, Enchantment enchantment, int enchantmentDestinationLevel, int requiredExperience, Item requiredItem, int requiredItemCount, @Nullable Identifier requiredAdvancementIdentifier);
	}

}
