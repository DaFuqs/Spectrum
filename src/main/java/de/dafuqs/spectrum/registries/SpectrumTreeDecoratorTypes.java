package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.tree_decorators.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.treedecorator.*;

public class SpectrumTreeDecoratorTypes {
	
	public static final TreeDecoratorType<?> FRONDS = register("fronds", new TreeDecoratorType<>(FrondsDecorator.CODEC));
	
	public static void register() {
	
	}
	
	private static TreeDecoratorType<?> register(String id, TreeDecoratorType<?> treeDecoratorType) {
		return Registry.register(Registries.TREE_DECORATOR_TYPE, SpectrumCommon.locate(id), treeDecoratorType);
	}
	
}
