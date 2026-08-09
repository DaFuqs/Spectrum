package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * Advanced fishing criterion that can also:
 * - match the fluid that was fished in
 * - fished entities
 */
public class SpectrumFishingRodHookedCriterion extends SimpleCriterionTrigger<SpectrumFishingRodHookedCriterion.Conditions> {
	
	public static final String NAME = "fishing_rod_hooked";
	
	public void trigger(ServerPlayer player, ItemStack rod, SpectrumFishingHook bobber, @Nullable Entity fishedEntity, Collection<ItemStack> fishingLoots) {
		LootContext bobberContext = EntityPredicate.createContext(player, bobber);
		@Nullable LootContext hookedEntityContext = bobber.getHookedIn() == null ? null : EntityPredicate.createContext(player, bobber.getHookedIn());
		@Nullable LootContext fishedEntityContext = fishedEntity == null ? null : EntityPredicate.createContext(player, fishedEntity);
		this.trigger(player, (conditions) -> conditions.matches(rod, bobberContext, hookedEntityContext, fishedEntityContext, fishingLoots, (ServerLevel) bobber.level(), bobber.blockPosition()));
		
		// also trigger vanilla fishing criterion
		// since that one requires a FishingBobberEntity and SpectrumFishingBobberEntity
		// does not extend that we have to do some hacky shenanigans running trigger() directly
		LootContext hookedEntityOrBobberContext = EntityPredicate.createContext(player, (bobber.getHookedIn() != null ? bobber.getHookedIn() : bobber));
		Predicate<FishingRodHookedTrigger.TriggerInstance> instancePredicate = triggerInstance -> triggerInstance.matches(rod, hookedEntityOrBobberContext, fishingLoots);
		((SimpleCriterionAccessor) CriteriaTriggers.FISHING_ROD_HOOKED).spectrum$invokeTrigger(player, instancePredicate);
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<ItemPredicate> rod,
			Optional<ContextAwarePredicate> bobber,
			Optional<ContextAwarePredicate> hookedEntity,
			Optional<ContextAwarePredicate> fishedEntity,
			Optional<ItemPredicate> caughtItem,
			Optional<FluidPredicate> fluidPredicate
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("rod").forGetter(Conditions::rod),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("bobber").forGetter(Conditions::bobber),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("hooked_entity").forGetter(Conditions::hookedEntity),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("fished_entity").forGetter(Conditions::fishedEntity),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Conditions::caughtItem),
				FluidPredicate.CODEC.optionalFieldOf("fluid").forGetter(Conditions::fluidPredicate)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack rod, LootContext bobberContext, @Nullable LootContext hookedEntityContext, @Nullable LootContext fishedEntityContext, Collection<ItemStack> fishingLoots, ServerLevel world, BlockPos blockPos) {
			if (this.rod.isPresent() && !this.rod.get().test(rod)) return false;
			if (this.bobber.isPresent() && !this.bobber.get().matches(bobberContext)) return false;
			if (this.fluidPredicate.isPresent() && !this.fluidPredicate.get().matches(world, blockPos)) return false;
			
			if (this.fishedEntity.isPresent()) {
				if (fishedEntityContext == null || !this.fishedEntity.get().matches(fishedEntityContext))
					return false;
			}

			if (this.hookedEntity.isPresent()) {
				if (hookedEntityContext == null || !this.hookedEntity.get().matches(hookedEntityContext))
					return false;
			}
			
			if (this.caughtItem.isPresent()) {
				if (hookedEntityContext != null) {
					Entity entity = hookedEntityContext.getParamOrNull(LootContextParams.THIS_ENTITY);
					if (entity instanceof ItemEntity itemEntity &&
							this.caughtItem.get().test(itemEntity.getItem())) return true;
				}
				for (ItemStack itemStack : fishingLoots) {
					if (this.caughtItem.get().test(itemStack)) return true;
				}
				
				return false;
			}
			
			return true;
		}
	}
	
}
