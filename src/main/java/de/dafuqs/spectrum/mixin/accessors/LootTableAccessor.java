package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTable.class)
public interface LootTableAccessor {
	
	@Accessor(value = "pools")
	LootPool[] getPools();
	
}