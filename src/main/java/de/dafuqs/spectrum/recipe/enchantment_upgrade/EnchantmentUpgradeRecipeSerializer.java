package de.dafuqs.spectrum.recipe.enchantment_upgrade;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class EnchantmentUpgradeRecipeSerializer implements GatedRecipeSerializer<EnchantmentUpgradeRecipe> {
	
	public static final List<EnchantmentUpgradeRecipe> enchantmentUpgradeRecipesToInject = new ArrayList<>();
	
	public final EnchantmentUpgradeRecipeSerializer.RecipeFactory<EnchantmentUpgradeRecipe> recipeFactory;
	
	public EnchantmentUpgradeRecipeSerializer(EnchantmentUpgradeRecipeSerializer.RecipeFactory<EnchantmentUpgradeRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<EnchantmentUpgradeRecipe> {
		EnchantmentUpgradeRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Enchantment enchantment, int enchantmentDestinationLevel, int requiredExperience, Item requiredItem, int requiredItemCount);
	}
	
	@Override
	public EnchantmentUpgradeRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Identifier enchantmentIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "enchantment"));
		
		if (!Registry.ENCHANTMENT.containsId(enchantmentIdentifier)) {
			throw new JsonParseException("Enchantment Upgrade Recipe " + identifier + " has an enchantment set that does not exist or is disabled: " + enchantmentIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing enchantment
		}
		
		Enchantment enchantment = Registry.ENCHANTMENT.get(enchantmentIdentifier);
		
		JsonArray levelArray = JsonHelper.getArray(jsonObject, "levels");
		int level;
		int requiredExperience;
		Item requiredItem;
		int requiredItemCount;
		EnchantmentUpgradeRecipe recipe = null;
		for (int i = 0; i < levelArray.size(); i++) {
			JsonObject currentElement = levelArray.get(i).getAsJsonObject();
			level = i + 2;
			requiredExperience = JsonHelper.getInt(currentElement, "experience");
			requiredItem = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(currentElement, "item")));
			requiredItemCount = JsonHelper.getInt(currentElement, "item_count");
			
			recipe = this.recipeFactory.create(SpectrumCommon.locate(identifier.getPath() + "_level_" + (i + 2)), group, secret, requiredAdvancementIdentifier, enchantment, level, requiredExperience, requiredItem, requiredItemCount);
			if (!enchantmentUpgradeRecipesToInject.contains(recipe) && i < levelArray.size() - 1) { // we return the last one, no need to inject
				enchantmentUpgradeRecipesToInject.add(recipe);
			}
		}
		
		return recipe;
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, EnchantmentUpgradeRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeIdentifier(Registry.ENCHANTMENT.getId(recipe.enchantment));
		packetByteBuf.writeInt(recipe.enchantmentDestinationLevel);
		packetByteBuf.writeInt(recipe.requiredExperience);
		packetByteBuf.writeIdentifier(Registry.ITEM.getId(recipe.requiredItem));
		packetByteBuf.writeInt(recipe.requiredItemCount);
	}
	
	@Override
	public EnchantmentUpgradeRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Enchantment enchantment = Registry.ENCHANTMENT.get(packetByteBuf.readIdentifier());
		int enchantmentDestinationLevel = packetByteBuf.readInt();
		int requiredExperience = packetByteBuf.readInt();
		Item requiredItem = Registry.ITEM.get(packetByteBuf.readIdentifier());
		int requiredItemCount = packetByteBuf.readInt();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, enchantment, enchantmentDestinationLevel, requiredExperience, requiredItem, requiredItemCount);
	}
	
}
