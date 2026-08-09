package de.dafuqs.spectrum.recipe;

import com.google.common.base.*;
import com.mojang.serialization.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.crafting.*;

import java.util.function.Supplier;

/**
 * A copy of the old SpecialRecipeSerializer, which simply ignores any meaningful recipe serialization.
 * <p>Recipes that use this serializer do not transport any data over the network, besides their ID.
 */
public class EmptyRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
	
	private final Supplier<T> instance;
	
	public EmptyRecipeSerializer(Supplier<T> factory) {
		this.instance = Suppliers.memoize(factory::get);
	}
	
	@Override
	public MapCodec<T> codec() {
		return MapCodec.unit(instance);
	}
	
	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return StreamCodec.unit(instance.get());
	}
	
}
