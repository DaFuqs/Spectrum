package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PotionWorkshopReactingRecipeSerializer implements GatedRecipeSerializer<PotionWorkshopReactingRecipe> {
	
	public final PotionWorkshopReactingRecipeSerializer.RecipeFactory<PotionWorkshopReactingRecipe> recipeFactory;
	
	public PotionWorkshopReactingRecipeSerializer(PotionWorkshopReactingRecipeSerializer.RecipeFactory<PotionWorkshopReactingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PotionWorkshopReactingRecipe> {
		PotionWorkshopReactingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Item item, List<PotionMod> mods);
	}
	
	@Override
	public PotionWorkshopReactingRecipe read(Identifier identifier, JsonObject jsonObject) {
		if (!jsonObject.has("modifiers")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		}
		
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Item item = ShapedRecipe.getItem(jsonObject);
		List<PotionMod> mods;
		
		if (JsonHelper.hasArray(jsonObject, "modifiers")) {
			JsonArray modifiers = JsonHelper.getArray(jsonObject, "modifiers");
			mods = StreamSupport.stream(modifiers.spliterator(), false).map((jsonElement) -> PotionMod.fromJson(jsonElement.getAsJsonObject())).collect(Collectors.toList());
		} else {
			JsonObject modifiers = JsonHelper.getObject(jsonObject, "modifiers");
			mods = Collections.singletonList(PotionMod.fromJson(modifiers));
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, item, mods);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopReactingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeIdentifier(Registries.ITEM.getId(recipe.item));
		
		packetByteBuf.writeInt(recipe.modifiers.size());
		for (PotionMod mod : recipe.modifiers) {
			mod.write(packetByteBuf);
		}
	}
	
	@Override
	public PotionWorkshopReactingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Item item = Registries.ITEM.get(packetByteBuf.readIdentifier());
		
		List<PotionMod> mods = new ArrayList<>();
		int modCount = packetByteBuf.readInt();
		for (int i = 0; i < modCount; i++) {
			mods.add(PotionMod.fromPacket(packetByteBuf));
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, item, mods);
	}
	
}
