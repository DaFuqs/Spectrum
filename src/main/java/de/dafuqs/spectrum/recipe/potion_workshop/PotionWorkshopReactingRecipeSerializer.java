package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlock;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PotionWorkshopReactingRecipeSerializer implements RecipeSerializer<PotionWorkshopReactingRecipe> {
	
	public final PotionWorkshopReactingRecipeSerializer.RecipeFactory<PotionWorkshopReactingRecipe> recipeFactory;
	
	public PotionWorkshopReactingRecipeSerializer(PotionWorkshopReactingRecipeSerializer.RecipeFactory<PotionWorkshopReactingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public PotionWorkshopReactingRecipe read(Identifier identifier, JsonObject jsonObject) {
		if (!jsonObject.has("modifiers")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		}
		
		Item item = ShapedRecipe.getItem(jsonObject);
		List<PotionMod> mods;
		
		if (JsonHelper.hasArray(jsonObject, "modifiers")) {
			JsonArray modifiers = JsonHelper.getArray(jsonObject, "modifiers");
			mods = StreamSupport.stream(modifiers.spliterator(), false).map((jsonElement) -> PotionMod.fromJson(jsonElement.getAsJsonObject())).collect(Collectors.toList());
		} else {
			JsonObject modifiers = JsonHelper.getObject(jsonObject, "modifiers");
			mods = Collections.singletonList(PotionMod.fromJson(modifiers));
		}
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = PotionWorkshopBlock.UNLOCK_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, item, mods, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopReactingRecipe recipe) {
		packetByteBuf.writeIdentifier(Registry.ITEM.getId(recipe.item));
		
		packetByteBuf.writeInt(recipe.modifiers.size());
		for(PotionMod mod : recipe.modifiers) {
			mod.write(packetByteBuf);
		}
		packetByteBuf.writeIdentifier(recipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public PotionWorkshopReactingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Item item = Registry.ITEM.get(packetByteBuf.readIdentifier());
		
		List<PotionMod> mods = new ArrayList<>();
		int modCount = packetByteBuf.readInt();
		for(int i = 0; i < modCount; i++) {
			mods.add(PotionMod.fromPacket(packetByteBuf));
		}
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, item, mods, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<PotionWorkshopReactingRecipe> {
		PotionWorkshopReactingRecipe create(Identifier id, Item item, List<PotionMod> mods, Identifier requiredAdvancementIdentifier);
	}
	
}
