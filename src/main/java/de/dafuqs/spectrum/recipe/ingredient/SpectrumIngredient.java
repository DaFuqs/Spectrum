package de.dafuqs.spectrum.recipe.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class SpectrumIngredient implements Predicate<ItemStack> {

	private final Identifier ingredientType;

	public SpectrumIngredient(Identifier ingredientType) {
		this.ingredientType = ingredientType;
	}

	@Override
	public abstract boolean test(ItemStack itemStack);

	public abstract Ingredient getPreview();

	public abstract List<ItemStack> getPreviewStacks();

	public abstract JsonObject toJson();

	public abstract int getCount();

	public final JsonObject writeToJson() {
		JsonObject jsonObject = toJson();
		jsonObject.addProperty("type", ingredientType.toString());
		return jsonObject;
	}

	public <T extends SpectrumIngredient> void ifType(Class<T> clazz, Consumer<T> consumer) {
		if (this.getClass().isAssignableFrom(clazz)) {
			//noinspection unchecked
			consumer.accept((T) this);
		}
	}

}