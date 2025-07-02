package de.dafuqs.spectrum.progression.advancement;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.MinMaxBounds.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;

import java.util.*;

public class InkProjectileKillingCriterion extends SimpleCriterionTrigger<InkProjectileKillingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("ink_projectile_killing");
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public void trigger(ServerPlayer player, List<Entity> piercingKilledEntities) {
		List<LootContext> list = Lists.newArrayList();
		Set<EntityType<?>> set = Sets.newHashSet();
		
		for (Entity entity : piercingKilledEntities) {
			set.add(entity.getType());
			list.add(EntityPredicate.createContext(player, entity));
		}
		
		this.trigger(player, (conditions) -> conditions.matches(list, set.size()));
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			List<ContextAwarePredicate> victims,
			Ints uniqueEntities
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ContextAwarePredicate.CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(Conditions::victims),
				Ints.CODEC.optionalFieldOf("unique_entity_types", Ints.ANY).forGetter(Conditions::uniqueEntities)
		).apply(instance, Conditions::new));
		
		public boolean matches(Collection<LootContext> victimContexts, int uniqueEntityTypeCount) {
			if (!this.victims.isEmpty()) {
				List<LootContext> list = Lists.newArrayList(victimContexts);
				
				for (ContextAwarePredicate extended : this.victims) {
					boolean bl = false;
					
					Iterator<LootContext> iterator = list.iterator();
					while (iterator.hasNext()) {
						LootContext lootContext = iterator.next();
						if (extended.matches(lootContext)) {
							iterator.remove();
							bl = true;
							break;
						}
					}
					
					if (!bl) {
						return false;
					}
				}
			}
			
			return this.uniqueEntities.matches(uniqueEntityTypeCount);
		}
	}
}
