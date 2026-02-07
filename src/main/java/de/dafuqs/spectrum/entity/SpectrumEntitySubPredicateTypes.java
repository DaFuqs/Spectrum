package de.dafuqs.spectrum.entity;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.predicates.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumEntitySubPredicateTypes {
	
	private static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> REGISTER = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, SpectrumCommon.MOD_ID);
	
	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<EggLayingWoolyPigPredicate>> EGG_LAYING_WOOLY_PIG = REGISTER.register("egg_laying_wooly_pig", () -> EggLayingWoolyPigPredicate.CODEC);
	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<ShulkerPredicate>> SHULKER = REGISTER.register("shulker", () -> ShulkerPredicate.CODEC);
	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<KindlingPredicate>> KINDLING = REGISTER.register("kindling", () -> KindlingPredicate.CODEC);
	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<LizardPredicate>> LIZARD = REGISTER.register("lizard", () -> LizardPredicate.CODEC);
	
	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
	
}
