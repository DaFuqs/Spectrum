package de.dafuqs.spectrum.recipe.fluid_converting;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public abstract class FluidConvertingRecipe extends GatedSpectrumRecipe {

	protected final Ingredient input;
	protected final ItemStack output;

	public FluidConvertingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, @NotNull Ingredient input, ItemStack output) {
		super(id, group, secret, requiredAdvancementIdentifier);
		this.input = input;
		this.output = output;
	}
	
	@Override
	public boolean matches(@NotNull Inventory inv, World world) {
		return this.input.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return output.copy();
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return output;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.input);
		return defaultedList;
	}

}
