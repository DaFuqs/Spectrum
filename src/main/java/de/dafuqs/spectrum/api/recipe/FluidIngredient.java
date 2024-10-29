package de.dafuqs.spectrum.api.recipe;

import com.google.gson.*;
import de.dafuqs.matchbooks.recipe.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class FluidIngredient {
    private final @Nullable Fluid fluid;
    private final @Nullable Identifier tag;
    // Compare against EMPTY to check if empty.
    // In order to represent an empty value, specifically use this field.
    public static FluidIngredient EMPTY = new FluidIngredient(null, null);

    // Can't be both fluid and tag, so ONLY use the provided of() methods
    // NOTE: ALL FluidIngredient-related code assumes that either:
    // 1. there are always EITHER the fluid OR the fluid tag, NOT both
    // 2. the object is empty AND the object is EQUAL TO FluidIngredient.EMPTY.
    // Violation of either of those results in either an AssertionError or
    // undefined behavior. As such, don't even allow creation of invalid obj.
    // FluidIngredient objects with unknown/invalid tags are considered valid.
    private FluidIngredient(@Nullable Fluid fluid, @Nullable Identifier tag) {
        this.fluid = fluid;
        this.tag = tag;
    }

    // NOTE: This is for testing. Doesn't explicitly handle invalid FluidInput.
    @Override
    public String toString() {
        if (this == EMPTY)
            return "FluidIngredient.EMPTY";
        if (this.fluid != null)
            return String.format("FluidIngredient[fluid=%s]", this.fluid);
        // must contain either or be FluidInput.EMPTY(as per FluidInput doc)
        assert this.tag != null;
        return String.format("FluidIngredient[tag=%s]", this.tag);
    }

    public static FluidIngredient of(@NotNull Fluid fluid) {
        Objects.requireNonNull(fluid);
        return new FluidIngredient(fluid, null);
    }
    
    public static FluidIngredient of(@NotNull Identifier tag) {
        Objects.requireNonNull(tag);
        return new FluidIngredient(null, tag);
    }

    public Optional<Fluid> fluid() {
        return Optional.ofNullable(this.fluid);
    }
    public Optional<TagKey<Fluid>> tag() {
        return RegistryHelper.tryGetTagKey(Registries.FLUID, this.tag);
    }
    public boolean isTag() {
        return this.tag != null;
    }

    public Identifier id() {
        return fluid != null ? Registries.FLUID.getId(fluid) : tag;
    }

    // Vanilla-friendly compatibility method.
    // Represents this FluidIngredient as bucket stack(s).
    public @NotNull Ingredient into() {
        if (this == EMPTY) return Ingredient.empty();
        if (this.fluid != null)
            return Ingredient.ofStacks(this.fluid.getBucketItem().getDefaultStack());
        if (this.tag != null) {
            // Handle custom fluid registries
            // in the case of FluidIngredient objects created by other mods.
            Registry<Fluid> registry = Registries.FLUID;
            if(registry == null) return Ingredient.empty();
            Optional<RegistryEntryList.Named<Fluid>> optional = registry.getEntryList(tag().get());
            if(optional.isEmpty()) return Ingredient.empty();
            RegistryEntryList.Named<Fluid> list = optional.get();
            Stream<ItemStack> stacks = list.stream().map((entry) -> entry.value().getBucketItem().getDefaultStack());
            return Ingredient.ofStacks(stacks);
        }

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidIngredient object");
    }

    public boolean test(@NotNull Fluid fluid) {
        Objects.requireNonNull(fluid);
        if (this == EMPTY) return fluid == Fluids.EMPTY;
        if (this.fluid != null) return this.fluid == fluid;
        if (this.tag != null) return fluid.getDefaultState().isIn(tag().get());

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidIngredient object");
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean test(@NotNull FluidVariant variant) {
        Objects.requireNonNull(variant);
        return test(variant.getFluid());
    }

    public static @NotNull FluidIngredient fromIdentifier(@Nullable Identifier id, boolean isTag) {
        if (isTag) {
            return FluidIngredient.of(id);
        } else {
            Fluid fluid = Registries.FLUID.get(id);
            if (fluid.getDefaultState().isEmpty()) return FluidIngredient.EMPTY;
            else return FluidIngredient.of(fluid);
        }
    }

    // Interpret FluidIngredient.EMPTY as an unknown ID error.
    public record JsonParseResult(
            @NotNull FluidIngredient result,
            boolean malformed,
            boolean isTag,
            @Nullable Identifier id
    ) {}

    public static @NotNull JsonParseResult fromJson(JsonObject fluidObject) {
        boolean hasFluid = JsonHelper.hasString(fluidObject, "fluid");
        boolean isTag = JsonHelper.hasString(fluidObject, "tag");

        if ((hasFluid && isTag) || !(hasFluid || isTag)) {
            return new JsonParseResult(FluidIngredient.EMPTY, true, isTag, null);
        } else {
            Identifier id = Identifier.tryParse(JsonHelper.getString(fluidObject, isTag ? "tag" : "fluid"));
            return new JsonParseResult(fromIdentifier(id, isTag), false, isTag, id);
        }
    }
}
