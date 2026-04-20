package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelTappingCriterion extends SimpleCriterionTrigger<TitrationBarrelTappingCriterion.Conditions> {
	
	public static final String NAME = "titration_barrel_tapping";
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int ingameDaysAge, int ingredientCount) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, ingameDaysAge, ingredientCount));
	}
	
	@Override
	public @NotNull Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			List<ItemPredicate> tappedItemsPredicate,
			MinMaxBounds.Ints ingameDaysAgeRange,
			MinMaxBounds.Ints ingredientCountRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(Conditions::tappedItemsPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("age_ingame_days", MinMaxBounds.Ints.ANY).forGetter(Conditions::ingameDaysAgeRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("ingredient_count", MinMaxBounds.Ints.ANY).forGetter(Conditions::ingredientCountRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack itemStack, int ingameDaysAge, int ingredientCount) {
			if (!this.ingameDaysAgeRange.matches(ingameDaysAge)) return false;
			if (!this.ingredientCountRange.matches(ingredientCount)) return false;
			
			List<ItemPredicate> list = new ObjectArrayList<>(this.tappedItemsPredicate);
			if (list.isEmpty()) {
				return true;
			}
			if (!itemStack.isEmpty()) {
				list.removeIf((itemPredicate) -> itemPredicate.test(itemStack));
			}
			return list.isEmpty();
		}
	}
	
}
