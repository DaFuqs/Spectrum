package de.dafuqs.spectrum.entity;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.entity.predicates.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;

public class SpectrumEntitySubPredicateTypes {
	
	public static final MapCodec<EggLayingWoolyPigPredicate> EGG_LAYING_WOOLY_PIG = EggLayingWoolyPigPredicate.CODEC;
	public static final MapCodec<ShulkerPredicate> SHULKER = ShulkerPredicate.CODEC;
	public static final MapCodec<KindlingPredicate> KINDLING = KindlingPredicate.CODEC;
	public static final MapCodec<LizardPredicate> LIZARD = LizardPredicate.CODEC;

	public static void register() {
		// EntitySubPredicateTypes are not handled via identifiers, but we'll add our mod id anyway,
		// in case of collisions with future vanilla updates or other mods
		Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, "spectrum:egg_laying_wooly_pig", EGG_LAYING_WOOLY_PIG);
		Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, "spectrum:shulker", SHULKER);
		Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, "spectrum:kindling", KINDLING);
		Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, "spectrum:lizard", LIZARD);
	}
	
}
