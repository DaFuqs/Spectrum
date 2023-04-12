package de.dafuqs.spectrum.mixin.accessors;

import com.google.common.collect.*;
import net.minecraft.predicate.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(value = TypeSpecificPredicate.Deserializers.class)
public interface TypeSpecificPredicateDeserializerMixin {
	
	@Mutable
	@Accessor("TYPES")
	static void setTypes(BiMap<String, TypeSpecificPredicate.Deserializer> types) {
		throw new AssertionError();
	}
	
}
