package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PairedFoodEatenCriterion extends SimpleCriterionTrigger<PairedFoodEatenCriterion.Conditions> {
	
	public static final String NAME = "consumed_paired_food";
	
	public void trigger(ServerPlayer player, ItemStack teaStack, ItemStack sconeStack) {
		this.trigger(player, (conditions) -> conditions.matches(teaStack, sconeStack));
	}
	
	@Override
	public @NotNull Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate teaItem,
			ItemPredicate sconeItem
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<PairedFoodEatenCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(PairedFoodEatenCriterion.Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("eaten_item", ItemPredicate.Builder.item().build()).forGetter(PairedFoodEatenCriterion.Conditions::teaItem),
				ItemPredicate.CODEC.optionalFieldOf("paired_item", ItemPredicate.Builder.item().build()).forGetter(PairedFoodEatenCriterion.Conditions::sconeItem)
		).apply(instance, PairedFoodEatenCriterion.Conditions::new));
		
		public boolean matches(ItemStack teaStack, ItemStack sconeStack) {
			return teaItem.test(teaStack) && sconeItem.test(sconeStack);
		}
	}
	
}