package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

public class SpectrumLootConditionTypes {
	
	public static final DeferredRegister<LootItemConditionType> REGISTRAR = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, SpectrumCommon.MOD_ID);
	
	public record SleepersNearbyLootCondition(int rangeBlocks, float base, float max, float bonus_per_sleeping_entity) implements LootItemCondition {
		
		public static final MapCodec<SleepersNearbyLootCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
				Codec.INT.fieldOf("range_blocks").forGetter(SleepersNearbyLootCondition::rangeBlocks),
				Codec.FLOAT.fieldOf("lerp_base").forGetter(SleepersNearbyLootCondition::base),
				Codec.FLOAT.fieldOf("lerp_max").forGetter(SleepersNearbyLootCondition::bonus_per_sleeping_entity),
				Codec.FLOAT.fieldOf("delta_per_sleeping_entity_by_range").forGetter(SleepersNearbyLootCondition::bonus_per_sleeping_entity)
		).apply(instance, SleepersNearbyLootCondition::new));
		
		@Override
		public @NotNull LootItemConditionType getType() {
			return SLEEPERS_NEARBY.get();
		}
		
		@Override
		public boolean test(LootContext lootContext) {
			Vec3 pos = lootContext.getParam(LootContextParams.ORIGIN);
			float sleepingEntitiesDelta = Math.min((float) lootContext.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(BlockPos.containing(pos)).inflate(rangeBlocks), LivingEntity::isSleeping).size() / rangeBlocks, 1F);
			var dropChance = Mth.clampedLerp(base, max, sleepingEntitiesDelta);
			
			return lootContext.getRandom().nextFloat() < dropChance;
		}
	}
	
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> SLEEPERS_NEARBY = register("sleepers_nearby", SleepersNearbyLootCondition.CODEC);
	
	private static DeferredHolder<LootItemConditionType, LootItemConditionType> register(String name, MapCodec<? extends LootItemCondition> codec) {
		return REGISTRAR.register(name, () -> new LootItemConditionType(codec));
	}
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
}
