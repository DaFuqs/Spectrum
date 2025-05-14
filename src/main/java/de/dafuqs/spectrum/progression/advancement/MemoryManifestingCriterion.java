package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;

import java.util.*;

public class MemoryManifestingCriterion extends SimpleCriterionTrigger<MemoryManifestingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("memory_manifesting");
	
	public void trigger(ServerPlayer player, Entity manifestedEntity) {
		LootContext lootContext = EntityPredicate.createContext(player, manifestedEntity);
		this.trigger(player, (conditions) -> conditions.matches(lootContext));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ContextAwarePredicate manifestedEntity
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("manifested_entity", ContextAwarePredicate.create()).forGetter(Conditions::manifestedEntity)
		).apply(instance, Conditions::new));
		
		public boolean matches(LootContext context) {
			return this.manifestedEntity.matches(context);
		}
	}
	
}
