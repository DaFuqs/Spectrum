package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class ConsumedPairedFoodCriterion extends SimpleCriterionTrigger<ConsumedPairedFoodCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("consumed_paired_food");
	
	public void trigger(ServerPlayer player, ItemStack teaStack, ItemStack sconeStack) {
		this.trigger(player, (conditions) -> conditions.matches(teaStack, sconeStack));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate consumedItem,
			ItemPredicate pairedItem
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<ConsumedPairedFoodCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(ConsumedPairedFoodCriterion.Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("consumed_item", ItemPredicate.Builder.item().build()).forGetter(ConsumedPairedFoodCriterion.Conditions::consumedItem),
				ItemPredicate.CODEC.optionalFieldOf("paired_item", ItemPredicate.Builder.item().build()).forGetter(ConsumedPairedFoodCriterion.Conditions::pairedItem)
		).apply(instance, ConsumedPairedFoodCriterion.Conditions::new));
		
		public boolean matches(ItemStack teaStack, ItemStack sconeStack) {
			return consumedItem.test(teaStack) && pairedItem.test(sconeStack);
		}
	}
	
}