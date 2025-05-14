package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(BundleContents.Mutable.class)
public interface BundleContentsComponentBuilderAccessor {
	
	@Accessor
	List<ItemStack> getItems();
	
}
