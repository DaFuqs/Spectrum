package de.dafuqs.spectrum.helpers;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.id.incubus_core.util.RegistryHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FluidInput {

    private final @Nullable Fluid fluid;
    private final @Nullable TagKey<Fluid> tag;
    // Compare against EMPTY to check if empty.
    // In order to represent an empty value, specifically use this field.
    public static FluidInput EMPTY = new FluidInput(null, null);

    // Can't be both fluid and tag, so ONLY use the provided of() methods
    // NOTE: ALL FluidInput-related code assumes that either:
    // 1. there are always EITHER the fluid OR the fluid tag, NOT both
    // 2. the object is empty AND the object is EQUAL TO FluidInput.EMPTY.
    // Violation of either of those results in either an AssertionError or
    // undefined behavior. As such, don't even allow creation of invalid obj.
    // FluidInput objects with unknown/invalid tag IDs are considered valid.
    private FluidInput(@Nullable Fluid fluid, @Nullable TagKey<Fluid> tag) {
        this.fluid = fluid;
        this.tag = tag;
    }

    // NOTE: This is for testing. Doesn't explicitly handle invalid FluidInput.
    @Override
    public String toString() {
        if (this == EMPTY)
            return "FluidInput.EMPTY";
        if (this.fluid != null)
            return String.format("FluidInput[fluid=%s]", this.fluid);
        // must contain either or be FluidInput.EMPTY(as per FluidInput doc)
        assert this.tag != null;
        return String.format("FluidInput[tag=%s]", this.tag);
    }

    public static FluidInput of(@NotNull Fluid fluid) {
        Objects.requireNonNull(fluid);
        return new FluidInput(fluid, null);
    }

    public static FluidInput of(@NotNull TagKey<Fluid> tag) {
        Objects.requireNonNull(tag);
        return new FluidInput(null, tag);
    }

    public Optional<Fluid> fluid() {
        return Optional.ofNullable(this.fluid);
    }
    public Optional<TagKey<Fluid>> tag() {
        return Optional.ofNullable(this.tag);
    }

    public Identifier id() {
        return fluid != null ? Registries.FLUID.getId(fluid)
                             : tag != null ? tag.id() : null;
    }

    // Vanilla-friendly compatibility method.
    // Represents this FluidInput as bucket stack(s).
    public @NotNull Ingredient into() {
        if (this == EMPTY) return Ingredient.empty();
        if (this.fluid != null)
            return Ingredient.ofStacks(this.fluid.getBucketItem()
                                                 .getDefaultStack());
        if (this.tag != null) {
            // Handle custom fluid registries
            // in the case of FluidInput objects created by other mods.
            Registry<Fluid> registry = RegistryHelper.getRegistryOf(this.tag);
            if(registry == null) return Ingredient.empty();
            Optional<RegistryEntryList.Named<Fluid>> optional =
                    registry.getEntryList(this.tag);
            if(optional.isEmpty()) return Ingredient.empty();
            RegistryEntryList.Named<Fluid> list = optional.get();
            Stream<ItemStack> stacks = list.stream().map(
                    (entry) -> entry.value().getBucketItem().getDefaultStack()
            );
            return Ingredient.ofStacks(stacks);
        }

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }

    public boolean test(@NotNull Fluid fluid) {
        Objects.requireNonNull(fluid);
        if (this == EMPTY && fluid != Fluids.EMPTY) return false;
        if (this.fluid != null) return this.fluid == fluid;
        if (this.tag != null) return fluid.getDefaultState().isIn(this.tag);

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean test(@NotNull FluidVariant variant) {
        Objects.requireNonNull(variant);
        return test(variant.getFluid());
    }
}
