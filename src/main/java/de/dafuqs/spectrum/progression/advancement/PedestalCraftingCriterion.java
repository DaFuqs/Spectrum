package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class PedestalCraftingCriterion extends SimpleCriterionTrigger<PedestalCraftingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("crafted_with_pedestal");
	
	public void trigger(ServerPlayer player, ItemStack craftedStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(craftedStack, experience, durationTicks));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return PedestalCraftingCriterion.Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<ContextAwarePredicate> location,
			Optional<ItemPredicate> craftedItemPredicate,
			MinMaxBounds.Ints experienceRange,
			MinMaxBounds.Ints craftingDurationTicksRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<PedestalCraftingCriterion.Conditions> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(PedestalCraftingCriterion.Conditions::player),
						ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(PedestalCraftingCriterion.Conditions::location),
						ItemPredicate.CODEC.optionalFieldOf("item").forGetter(PedestalCraftingCriterion.Conditions::craftedItemPredicate),
						MinMaxBounds.Ints.CODEC.optionalFieldOf("gained_experience", MinMaxBounds.Ints.ANY).forGetter(PedestalCraftingCriterion.Conditions::experienceRange),
						MinMaxBounds.Ints.CODEC.optionalFieldOf("crafting_duration_ticks", MinMaxBounds.Ints.ANY).forGetter(PedestalCraftingCriterion.Conditions::craftingDurationTicksRange)
				).apply(instance, PedestalCraftingCriterion.Conditions::new)
		);
		
		@Override
		public void validate(CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
		}
		
		public boolean matches(ItemStack craftedStack, int experience, int durationTicks) {
			if (this.craftedItemPredicate.isPresent() && !this.craftedItemPredicate.get().test(craftedStack)) {
				return false;
			}
			if (!this.experienceRange.matches(experience)) {
				return false;
			}
			return this.craftingDurationTicksRange.matches(durationTicks);
		}
	}
	
}
