package de.dafuqs.spectrum.data;

import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;

public class DatagenProxy {
	
	public static final boolean IS_DATAGEN = System.getProperty("fabric-api.datagen") != null;
	
	public interface TagBuilderCallback<T> {
		FabricTagProvider<T>.FabricTagBuilder build(FabricTagProvider<T>.FabricTagBuilder provider);
	}
	
	public interface KeyedTagBuilderCallback<T> {
		FabricTagProvider<T>.FabricTagBuilder build(ResourceKey<T> key, FabricTagProvider<T>.FabricTagBuilder provider);
	}
	
	public interface ProvidedTagBuilderBuilder<T> {
		FabricTagProvider<T>.FabricTagBuilder build(TagKey<T> key);
	}
	
	public record BootstrapContext<T>(
			net.minecraft.data.worldgen.BootstrapContext<T> registerable,
			HolderGetter<Item> items,
			HolderGetter<Block> blocks,
			HolderGetter<Enchantment> enchantments
	) {
		public BootstrapContext(net.minecraft.data.worldgen.BootstrapContext<T> registerable) {
			this(
					registerable,
					registerable.lookup(Registries.ITEM),
					registerable.lookup(Registries.BLOCK),
					registerable.lookup(Registries.ENCHANTMENT)
			);
		}
	}
	
}
