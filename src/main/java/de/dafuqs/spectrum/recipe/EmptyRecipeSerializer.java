package de.dafuqs.spectrum.recipe;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

import java.util.function.*;

/**
 * A copy of the old SpecialRecipeSerializer, which simply ignores any meaningful recipe serialization.
 * <p>Recipes that use this serializer do not transport any data over the network, besides their ID.
 */
public class EmptyRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
	private final Function<Identifier, T> factory;
	
	public EmptyRecipeSerializer(Function<Identifier, T> factory) {
		this.factory = factory;
	}
	
	@Override
	public T read(Identifier id, JsonObject json) {
		return this.factory.apply(id);
	}
	
	@Override
	public T read(Identifier id, PacketByteBuf buf) {
		return this.factory.apply(id);
	}
	
	@Override
	public void write(PacketByteBuf buf, T recipe) {
	
	}
}
