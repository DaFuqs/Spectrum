package de.dafuqs.spectrum.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.entity.type_specific_predicates.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.predicate.entity.*;

public class SpectrumTypeSpecificPredicates {
	
	public static final TypeSpecificPredicate.Deserializer EGG_LAYING_WOOLY_PIG = EggLayingWoolyPigPredicate::fromJson;
	public static final TypeSpecificPredicate.Deserializer SHULKER = ShulkerPredicate::fromJson;
	public static final TypeSpecificPredicate.Deserializer KINDLING = KindlingPredicate::fromJson;
	public static final TypeSpecificPredicate.Deserializer LIZARD = LizardPredicate::fromJson;

	public static void register() {
		// creating a new map, in case the previous one was immutable (it usually is)
		BiMap<String, TypeSpecificPredicate.Deserializer> newMap = HashBiMap.create(TypeSpecificPredicate.Deserializers.TYPES);
		
		// TypeSpecificPredicates are not handled via identifiers, but we'll add our mod id anyway,
		// in case of collisions with future vanilla updates or other mods
		newMap.put("spectrum:egg_laying_wooly_pig", EGG_LAYING_WOOLY_PIG);
		newMap.put("spectrum:shulker", SHULKER);
		newMap.put("spectrum:kindling", KINDLING);
		newMap.put("spectrum:lizard", LIZARD);

		TypeSpecificPredicateDeserializerMixin.setTypes(newMap);
	}
	
}
