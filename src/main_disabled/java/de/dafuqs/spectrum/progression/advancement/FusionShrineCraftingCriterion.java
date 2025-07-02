package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class FusionShrineCraftingCriterion extends SimpleCriterionTrigger<FusionShrineCraftingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("crafted_with_fusion_shrine");
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			List<ItemPredicate> itemPredicates,
			MinMaxBounds.Ints experienceRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(Conditions::itemPredicates),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("gained_experience", MinMaxBounds.Ints.ANY).forGetter(Conditions::experienceRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack itemStack, int experience) {
			if (this.experienceRange.matches(experience)) {
				List<ItemPredicate> list = new ObjectArrayList<>(this.itemPredicates);
				if (list.isEmpty()) {
					return true;
				} else {
					if (!itemStack.isEmpty()) {
						list.removeIf((itemPredicate) -> itemPredicate.test(itemStack));
					}
					return list.isEmpty();
				}
			} else {
				return false;
			}
		}
	}
	
}
