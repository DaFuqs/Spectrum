package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidTags.class)
public interface FluidTagsAccessor {

	@Invoker("register")
	public static Tag.Identified<Fluid> invokeRegister(String id) {
		throw new AssertionError();
	}

}