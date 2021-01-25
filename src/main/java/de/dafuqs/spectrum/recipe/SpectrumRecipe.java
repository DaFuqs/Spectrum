/*package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import de.dafuqs.spectrum.recipe.ingredient.SpectrumIngredient;
import de.dafuqs.spectrum.recipe.util.DefaultedListCollector;
import de.dafuqs.spectrum.recipe.util.IngredientManager;
import de.dafuqs.spectrum.recipe.util.RecipeUtils;
import de.dafuqs.spectrum.recipe.util.SerializationUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public class SpectrumRecipe implements Recipe<Inventory> {

	private final SpectrumRecipeType<?> type;
	private final Identifier name;

	private DefaultedList<SpectrumIngredient> ingredients = DefaultedList.of();
	private DefaultedList<ItemStack> outputs = DefaultedList.of();
	protected int time;

	public SpectrumRecipe(SpectrumRecipeType<?> type, Identifier name) {
		this.type = type;
		this.name = name;
	}

	public SpectrumRecipe(SpectrumRecipeType<?> type, Identifier name, DefaultedList<SpectrumIngredient> ingredients, DefaultedList<ItemStack> outputs, int time) {
		this(type, name);
		this.ingredients = ingredients;
		this.outputs = outputs;
		this.time = time;
	}

	public void deserialize(JsonObject jsonObject) {
		//Crash if the recipe has already been deserialized
		Validate.isTrue(ingredients.isEmpty());

		time = JsonHelper.getInt(jsonObject, "time");

		ingredients = SerializationUtil.stream(JsonHelper.getArray(jsonObject, "ingredients"))
				.map(IngredientManager::deserialize)
				.collect(DefaultedListCollector.toList());

		JsonArray resultsJson = JsonHelper.getArray(jsonObject, "results");
		outputs = RecipeUtils.deserializeItems(resultsJson);
	}

	public void serialize(JsonObject jsonObject) {
		jsonObject.addProperty("time", time);

		JsonArray ingredientsArray = new JsonArray();
		getSpectrumIngredients().stream().map(SpectrumIngredient::toJson).forEach(ingredientsArray::add);
		jsonObject.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		for (ItemStack stack : outputs) {
			JsonObject stackObject = new JsonObject();
			stackObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
			if (stack.getCount() > 1) {
				stackObject.addProperty("count", stack.getCount());
			}
			if (stack.hasTag()) {
				stackObject.add("nbt", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.getTag()));
			}
			resultsArray.add(stackObject);
		}
		jsonObject.add("results", resultsArray);
	}

	public void serialize(PacketByteBuf byteBuf) {

	}

	public void deserialize(PacketByteBuf byteBuf) {

	}

	@Override
	public Identifier getId() {
		return name;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return type;
	}

	@Override
	public net.minecraft.recipe.RecipeType<?> getType() {
		return type;
	}

	public SpectrumRecipeType<?> getSpectrumRecipeType() {
		return type;
	}

	public DefaultedList<SpectrumIngredient> getSpectrumIngredients() {
		return ingredients;
	}

	public int getTime() {
		return time;
	}

	public boolean onCraft(BlockEntity blockEntity) {
		return true;
	}

	@Override
	public boolean matches(Inventory inv, World worldIn) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack craft(Inventory inv) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean fits(int width, int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack getOutput() {
		if (outputs.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return outputs.get(0);
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(Inventory p_179532_1_) {
		throw new UnsupportedOperationException();
	}

}*/