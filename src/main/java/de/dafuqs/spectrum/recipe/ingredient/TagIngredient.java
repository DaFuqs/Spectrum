package de.dafuqs.spectrum.recipe.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.dafuqs.spectrum.recipe.util.IngredientManager;
import de.dafuqs.spectrum.recipe.util.SimpleTag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Like the vanilla tag ingredient, but can additionally hold an item count
public class TagIngredient extends SpectrumIngredient {

	private final Identifier tagIdentifier;
	private final Tag<Item> tag;
	private final Optional<Integer> count;

	public TagIngredient(Identifier tagIdentifier, Tag<Item> tag, Optional<Integer> count) {
		super(IngredientManager.TAG_RECIPE_TYPE);
		this.tagIdentifier = tagIdentifier;
		this.tag = tag;
		this.count = count;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		if (count.isPresent() && count.get() > itemStack.getCount()) {
			return false;
		}
		return itemStack.isIn(tag);
	}

	@Override
	public Ingredient getPreview() {
		return Ingredient.ofStacks(getPreviewStacks().stream());
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return tag.values().stream().map(ItemStack::new).peek(itemStack -> itemStack.setCount(count.orElse(1))).collect(Collectors.toList());
	}

	public static SpectrumIngredient deserialize(JsonObject json) {
		Optional<Integer> count = Optional.empty();
		if (json.has("count")) {
			count = Optional.of(JsonHelper.getInt(json, "count"));
		}

		if (json.has("server_sync")) {
			Identifier tagIdent = new Identifier(JsonHelper.getString(json, "tag_identifier"));
			List<Item> items = new ArrayList<>();
			for (int i = 0; i < JsonHelper.getInt(json, "items"); i++) {
				Identifier identifier = new Identifier(JsonHelper.getString(json, "item_" + i));
				Item item = Registry.ITEM.get(identifier);
				Validate.isTrue(item != Items.AIR, "item cannot be air");
				items.add(item);
			}
			return new TagIngredient(tagIdent, new SimpleTag<>(items), count);
		}


		Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));
		Tag<Item> tag = null;
		// TODO
		//Tag<Item> tag = ServerTagManagerHolder.getTagManager(). .getItems().getTag(identifier);
		if (tag == null) {
			throw new JsonSyntaxException("Unknown item tag '" + identifier + "'");
		}
		return new TagIngredient(identifier, tag, count);
	}

	@Override
	public JsonObject toJson() {
		//Tags are not synced across the server so we sync all the items
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("server_sync", true);

		Item[] items = tag.values().toArray(new Item[0]);
		jsonObject.addProperty("items", items.length);
		for (int i = 0; i < items.length; i++) {
			jsonObject.addProperty("item_" + i, Registry.ITEM.getId(items[i]).toString());
		}

		count.ifPresent(integer -> jsonObject.addProperty("count", integer));
		jsonObject.addProperty("tag_identifier", tagIdentifier.toString());
		return jsonObject;
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}
}