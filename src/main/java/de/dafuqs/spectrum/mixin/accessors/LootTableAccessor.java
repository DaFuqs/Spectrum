package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.loot.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LootTable.class)
public interface LootTableAccessor {
	
	@Accessor(value = "pools")
	LootPool[] getPools();
	
}