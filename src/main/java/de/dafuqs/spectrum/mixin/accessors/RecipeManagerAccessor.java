package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.recipe.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
	
	@Accessor("recipes")
	Map<RecipeType<?>, Map<Identifier, Recipe<?>>> getRecipes();
	
}