package de.dafuqs.spectrum.api.recipe;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface GatedRecipeSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {
	
	default String readGroup(JsonObject jsonObject) {
		return JsonHelper.getString(jsonObject, "group", "");
	}
	
	default boolean readSecret(JsonObject jsonObject) {
		return JsonHelper.getBoolean(jsonObject, "secret", false);
	}
	
	default Identifier readRequiredAdvancementIdentifier(JsonObject jsonObject) {
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			return new Identifier(JsonHelper.getString(jsonObject, "required_advancement"));
		}
		return null;
	}

	// NOTE: All 4 of these methods could be static, as they are not overridden, nor does it make sense to override them.
	default void writeNullableIdentifier(PacketByteBuf buf, @Nullable Identifier identifier) {
		if (identifier == null) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			buf.writeIdentifier(identifier);
		}
	}
	
	default @Nullable Identifier readNullableIdentifier(PacketByteBuf buf) {
		boolean notNull = buf.readBoolean();
		if (notNull) {
			return buf.readIdentifier();
		}
		return null;
	}

	default @NotNull FluidIngredient readFluidIngredient(PacketByteBuf buf) {
		boolean isTag = buf.readBoolean();
		Identifier id = readNullableIdentifier(buf);
		return FluidIngredient.fromIdentifier(id, isTag);
	}

	default void writeFluidIngredient(PacketByteBuf buf, @NotNull FluidIngredient ingredient) {
		Objects.requireNonNull(ingredient);
		buf.writeBoolean(ingredient.isTag());
		writeNullableIdentifier(buf, ingredient.id());
	}

}
