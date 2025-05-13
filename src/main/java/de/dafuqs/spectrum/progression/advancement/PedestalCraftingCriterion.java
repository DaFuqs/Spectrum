package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class PedestalCraftingCriterion extends AbstractCriterion<PedestalCraftingCriterion.Conditions> {
	
	public static final Identifier ID = SpectrumCommon.locate("crafted_with_pedestal");
	
	public void trigger(ServerPlayerEntity player, ItemStack craftedStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(craftedStack, experience, durationTicks));
	}
	
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return PedestalCraftingCriterion.Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<LootContextPredicate> player,
			Optional<LootContextPredicate> location,
			Optional<ItemPredicate> craftedItemPredicate,
			NumberRange.IntRange experienceRange,
			NumberRange.IntRange craftingDurationTicksRange
	) implements AbstractCriterion.Conditions {
		
		public static final Codec<PedestalCraftingCriterion.Conditions> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(PedestalCraftingCriterion.Conditions::player),
						LootContextPredicate.CODEC.optionalFieldOf("location").forGetter(PedestalCraftingCriterion.Conditions::location),
						ItemPredicate.CODEC.optionalFieldOf("item").forGetter(PedestalCraftingCriterion.Conditions::craftedItemPredicate),
						NumberRange.IntRange.CODEC.optionalFieldOf("gained_experience", NumberRange.IntRange.ANY).forGetter(PedestalCraftingCriterion.Conditions::experienceRange),
						NumberRange.IntRange.CODEC.optionalFieldOf("crafting_duration_ticks", NumberRange.IntRange.ANY).forGetter(PedestalCraftingCriterion.Conditions::craftingDurationTicksRange)
				).apply(instance, PedestalCraftingCriterion.Conditions::new)
		);
		
		@Override
		public void validate(LootContextPredicateValidator validator) {
			AbstractCriterion.Conditions.super.validate(validator);
		}
		
		public boolean matches(ItemStack craftedStack, int experience, int durationTicks) {
			if (this.craftedItemPredicate.isPresent() && !this.craftedItemPredicate.get().test(craftedStack)) {
				return false;
			}
			if (!this.experienceRange.test(experience)) {
				return false;
			}
			return this.craftingDurationTicksRange.test(durationTicks);
		}
	}
	
}
