package de.dafuqs.spectrum.recipe;

import com.mojang.serialization.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.crafting.*;

import java.util.function.*;

/**
 * A copy of the old SpecialRecipeSerializer, which simply ignores any meaningful recipe serialization.
 * <p>Recipes that use this serializer do not transport any data over the network, besides their ID.
 */
public class EmptyRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> packetCodec;
	
	public EmptyRecipeSerializer(Supplier<T> factory) {
		T instance = factory.get();
		this.codec = MapCodec.unit(instance);
		this.packetCodec = StreamCodec.unit(instance);
	}
	
	@Override
	public MapCodec<T> codec() {
		return codec;
	}
	
	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return packetCodec;
	}
	
}
