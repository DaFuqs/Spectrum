package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

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
	
}
