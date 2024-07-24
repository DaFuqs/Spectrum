package de.dafuqs.spectrum.mixin.accessors;

import com.google.gson.*;
import net.minecraft.recipe.*;
import net.minecraft.util.collection.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ShapelessRecipe.Serializer.class)
public interface ShapelessRecipeSerializerAccessor {

	@Invoker(value = "getIngredients")
	static DefaultedList<Ingredient> invokeGetIngredients(JsonArray json) {
		throw new AssertionError();
	}

}