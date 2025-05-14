package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;

import java.util.*;

public class PotionWorkshopBrewingCriterion extends SimpleCriterionTrigger<PotionWorkshopBrewingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("potion_workshop_brewing");
	
	@SuppressWarnings("deprecation")
	public void trigger(ServerPlayer player, ItemStack itemStack, int brewedCount) {
		this.trigger(player, conditions -> {
			List<MobEffectInstance> effects;
			if (itemStack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
				effects = inkPoweredPotionFillable.getVanillaEffects(itemStack);
			} else {
				PotionContents potionComponent = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
				effects = potionComponent.customEffects();
			}
			
			int highestAmplifier = 0;
			int longestDuration = 0;
			for (MobEffectInstance instance : effects) {
				if (instance.getAmplifier() > highestAmplifier) {
					highestAmplifier = instance.getAmplifier();
				}
				if (instance.getDuration() > longestDuration) {
					longestDuration = instance.getDuration();
				}
			}
			
			List<MobEffect> uniqueEffects = new ArrayList<>();
			for (MobEffectInstance instance : effects) {
				if (!uniqueEffects.contains(instance.getEffect().value())) {
					uniqueEffects.add(instance.getEffect().value());
				}
			}
			
			return conditions.matches(itemStack, effects, brewedCount, highestAmplifier, longestDuration, effects.size(), uniqueEffects.size());
		});
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate itemPredicate,
			MobEffectsPredicate statusEffectsPredicate,
			MinMaxBounds.Ints brewedCountRange,
			MinMaxBounds.Ints maxAmplifierRange,
			MinMaxBounds.Ints maxDurationRange,
			MinMaxBounds.Ints effectCountRange,
			MinMaxBounds.Ints uniqueEffectCountRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(Conditions::itemPredicate),
				MobEffectsPredicate.CODEC.optionalFieldOf("effects", new MobEffectsPredicate(Map.of())).forGetter(Conditions::statusEffectsPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("brewed_count", MinMaxBounds.Ints.ANY).forGetter(Conditions::brewedCountRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("highest_amplifier", MinMaxBounds.Ints.ANY).forGetter(Conditions::maxAmplifierRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("longest_duration", MinMaxBounds.Ints.ANY).forGetter(Conditions::maxDurationRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("effect_count", MinMaxBounds.Ints.ANY).forGetter(Conditions::effectCountRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("unique_effect_count", MinMaxBounds.Ints.ANY).forGetter(Conditions::uniqueEffectCountRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack stack, List<MobEffectInstance> effects, int brewedCount, int maxAmplifier, int maxDuration, int effectCount, int uniqueEffectCount) {
			if (this.brewedCountRange.matches(brewedCount) &&
					this.maxAmplifierRange.matches(maxAmplifier) &&
					this.maxDurationRange.matches(maxDuration) &&
					this.effectCountRange.matches(effectCount) &&
					this.uniqueEffectCountRange.matches(uniqueEffectCount) &&
					this.itemPredicate.test(stack)) {
				Map<Holder<MobEffect>, MobEffectInstance> effectMap = new HashMap<>();
				for (MobEffectInstance instance : effects) {
					if (!effectMap.containsKey(instance.getEffect())) {
						effectMap.put(instance.getEffect(), instance);
					}
				}
				
				return this.statusEffectsPredicate.matches(effectMap);
			}
			
			return false;
		}
	}
	
}
