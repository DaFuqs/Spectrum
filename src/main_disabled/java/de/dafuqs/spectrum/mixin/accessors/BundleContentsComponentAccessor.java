package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.apache.commons.lang3.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(BundleContents.class)
public interface BundleContentsComponentAccessor {

    @Invoker
	static Fraction invokeGetWeight(ItemStack stack) {
        throw new AssertionError();
    }

}
