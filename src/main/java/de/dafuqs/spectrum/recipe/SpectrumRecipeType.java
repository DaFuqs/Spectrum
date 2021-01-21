package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.util.ConditionManager;
import de.dafuqs.spectrum.recipe.util.RecipeUtils;
import de.dafuqs.spectrum.recipe.util.SerializationUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiFunction;

public class SpectrumRecipeType<R extends SpectrumRecipe> implements RecipeType, RecipeSerializer {

	private final BiFunction<SpectrumRecipeType<R>, Identifier, R> recipeFunction;
	private final Identifier typeId;

	public SpectrumRecipeType(BiFunction<SpectrumRecipeType<R>, Identifier, R> recipeFunction, Identifier typeId) {
		this.recipeFunction = recipeFunction;
		this.typeId = typeId;
	}

	@Override
	public R read(Identifier recipeId, JsonObject json) {
		Identifier type = new Identifier(JsonHelper.getString(json, "type"));
		if (!type.equals(typeId)) {
			throw new RuntimeException("Recipe type not supported!");
		}

		R recipe = newRecipe(recipeId);

		try{
			if(!ConditionManager.shouldLoadRecipe(json)) {
				return recipe;
			}

			recipe.deserialize(json);
		} catch (Throwable t){
			t.printStackTrace();
			SpectrumCommon.LOGGER.error("Failed to read recipe: " + recipeId);
		}
		return recipe;

	}

	public JsonObject toJson(R recipe) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", typeId.toString());

		recipe.serialize(jsonObject);

		return jsonObject;
	}

	public R fromJson(Identifier recipeType, JsonObject json) {
		return read(recipeType, json);
	}

	R newRecipe(Identifier recipeId) {
		return recipeFunction.apply(this, recipeId);
	}

	@Override
	public R read(Identifier recipeId, PacketByteBuf buffer) {
		String input = buffer.readString(buffer.readInt());
		R r = read(recipeId, SerializationUtil.GSON_FLAT.fromJson(input, JsonObject.class));
		r.deserialize(buffer);
		return r;
	}

	@Override
	public void write(PacketByteBuf buffer, Recipe recipe) {
		JsonObject jsonObject = toJson((R) recipe);
		String output = SerializationUtil.GSON_FLAT.toJson(jsonObject);
		buffer.writeInt(output.length());
		buffer.writeString(output);
		((R) recipe).serialize(buffer);
	}

	public Identifier getName() {
		return typeId;
	}

	public List<R> getRecipes(World world) {
		return RecipeUtils.getRecipes(world, this);
	}

}