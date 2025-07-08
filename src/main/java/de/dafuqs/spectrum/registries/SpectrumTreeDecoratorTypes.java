package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.tree_decorators.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class SpectrumTreeDecoratorTypes {
	
	private static final DeferredRegister<TreeDecoratorType<?>> REGISTRAR = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, SpectrumCommon.MOD_ID);
	
	public static final Holder<TreeDecoratorType<?>> FRONDS = register("fronds", () -> new TreeDecoratorType<>(FrondsDecorator.CODEC));
	
	private static Holder<TreeDecoratorType<?>> register(String id, Supplier<TreeDecoratorType<?>> treeDecoratorType) {
		return REGISTRAR.register(id, treeDecoratorType);
	}
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
