package de.dafuqs.spectrum.api.recipe;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

//TODO Refactor this to use registryEntryList or something, or at least an Either
public class FluidIngredient {
	
	public static final MapCodec<FluidIngredient> MAP_CODEC = Codec.mapEither(
			BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid"),
			ResourceLocation.CODEC.fieldOf("tag")
	).xmap(FluidIngredient::new, c -> c.ingredient);
	
	public static final Codec<FluidIngredient> CODEC = Codec.withAlternative(
			Codec.withAlternative(
					BuiltInRegistries.FLUID.byNameCodec().xmap(FluidIngredient::of, ingredient -> ingredient.fluid().get()),
					TagKey.hashedCodec(Registries.FLUID).xmap(FluidIngredient::of, ingredient -> ingredient.tag().get())
			),
			MAP_CODEC.codec()
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredient> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.either(ByteBufCodecs.registry(Registries.FLUID), ResourceLocation.STREAM_CODEC), o -> o.ingredient,
			FluidIngredient::new
	);
	
	private final Either<Fluid, ResourceLocation> ingredient;
	// Compare against EMPTY to check if empty.
	// In order to represent an empty value, specifically use this field.
	public static FluidIngredient EMPTY = new FluidIngredient(Either.left(Fluids.EMPTY));
	
	// Can't be both fluid and tag, so ONLY use the provided of() methods
	// NOTE: ALL FluidIngredient-related code assumes that either:
	// 1. there are always EITHER the fluid OR the fluid tag, NOT both
	// 2. the object is empty AND the object is EQUAL TO FluidIngredient.EMPTY.
	// Violation of either of those results in either an AssertionError or
	// undefined behavior. As such, don't even allow creation of invalid obj.
	// FluidIngredient objects with unknown/invalid tags are considered valid.
	private FluidIngredient(Either<Fluid, ResourceLocation> ingredient) {
		this.ingredient = ingredient;
	}
	
	// NOTE: This is for testing. Doesn't explicitly handle invalid FluidInput.
	@Override
	public String toString() {
		if (this == EMPTY)
			return "FluidIngredient.EMPTY";
		if (this.ingredient.left().isPresent())
			return String.format("FluidIngredient[fluid=%s]", this.ingredient.left().get());
		assert this.ingredient.right().isPresent();
		return String.format("FluidIngredient[tag=%s]", this.ingredient.right().get());
	}
	
	public static FluidIngredient of(@NotNull Fluid fluid) {
		return new FluidIngredient(Either.left(fluid));
	}
	
	public static FluidIngredient of(@NotNull TagKey<Fluid> tag) {
		return new FluidIngredient(Either.right(tag.location()));
	}
	
	public static FluidIngredient of(@NotNull ResourceLocation tag) {
		return new FluidIngredient(Either.right(tag));
	}
	
	public Optional<Fluid> fluid() {
		return this.ingredient.left();
	}
	
	public Optional<TagKey<Fluid>> tag() {
		return this.ingredient.right().flatMap(tag ->
				BuiltInRegistries.FLUID.getTagNames().filter(tagKey -> tagKey.location().equals(tag)).findFirst());
	}
	
	public boolean isTag() {
		return this.ingredient.right().isPresent();
	}
	
	public ResourceLocation id() {
		return ingredient.map(BuiltInRegistries.FLUID::getKey, tag -> tag);
	}
	
	// Vanilla-friendly compatibility method.
	// Represents this FluidIngredient as bucket stack(s).
	public @NotNull Ingredient into() {
		return this.ingredient.map(
				fluid -> fluid == Fluids.EMPTY ? Ingredient.EMPTY : Ingredient.of(fluid.getBucket().getDefaultInstance()),
				tag -> {
					// Handle custom fluid registries
					// in the case of FluidIngredient objects created by other mods.
					Registry<Fluid> registry = BuiltInRegistries.FLUID;
					if (registry == null) return Ingredient.of();
					Optional<HolderSet.Named<Fluid>> optional = registry.getTag(tag().get());
					if (optional.isEmpty()) return Ingredient.of();
					HolderSet.Named<Fluid> list = optional.get();
					Stream<ItemStack> stacks = list.stream().map((entry) -> entry.value().getBucket().getDefaultInstance());
					return Ingredient.of(stacks);
				});
	}
	
	public boolean test(@NotNull Fluid fluid) {
		return this.ingredient.map(fl -> fl == fluid, tag -> fluid.defaultFluidState().is(tag().get()));
	}
	
	public boolean test(@NotNull FluidVariant variant) {
		return test(variant.getFluid());
	}
	
}
