package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.crafting.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumIngredientTypes {
	
	private static final DeferredRegister<IngredientType<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, SpectrumCommon.MOD_ID);
	
	public static final IngredientType<IngredientStack> INGREDIENT_STACK = new IngredientType<>(IngredientStack.MAP_CODEC, IngredientStack.STREAM_CODEC);
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register("ingredient_stack", () -> INGREDIENT_STACK);
		
		REGISTRAR.register(eventBus);
	}
	
}
