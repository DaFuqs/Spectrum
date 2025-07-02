package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.trinkets.*;
import dev.emi.trinkets.api.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.*;

public class TrinketChangeCriterion extends SimpleCriterionTrigger<TrinketChangeCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("trinket_change");
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, (conditions) -> {
			Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);
			if (trinketComponent.isPresent()) {
				List<ItemStack> equippedStacks = new ArrayList<>();
				int spectrumStacks = 0;
				for (Tuple<SlotReference, ItemStack> t : trinketComponent.get().getAllEquipped()) {
					equippedStacks.add(t.getB());
					if (t.getB().getItem() instanceof SpectrumTrinketItem) {
						spectrumStacks++;
					}
				}
				return conditions.matches(equippedStacks, equippedStacks.size(), spectrumStacks);
			}
			return false;
		});
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<List<ItemPredicate>> itemPredicates,
			Optional<MinMaxBounds.Ints> totalCountRange,
			Optional<MinMaxBounds.Ints> spectrumCountRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<TrinketChangeCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TrinketChangeCriterion.Conditions::player),
				ItemPredicate.CODEC.listOf().optionalFieldOf("items").forGetter(TrinketChangeCriterion.Conditions::itemPredicates),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("total_count").forGetter(TrinketChangeCriterion.Conditions::totalCountRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("spectrum_count").forGetter(TrinketChangeCriterion.Conditions::spectrumCountRange)
		).apply(instance, TrinketChangeCriterion.Conditions::new));
		
		public boolean matches(List<ItemStack> trinketStacks, int totalCount, int spectrumCount) {
			if ((this.totalCountRange.isPresent() && this.totalCountRange.get().matches(totalCount))
					|| (this.spectrumCountRange.isPresent() && this.spectrumCountRange.get().matches(spectrumCount))) {
				int i = this.itemPredicates.orElse(List.of()).size();
				if (i == 0) {
					return true;
				} else {
					List<ItemPredicate> requiredTrinkets = new ObjectArrayList<>(this.itemPredicates.get());
					for (ItemStack trinketStack : trinketStacks) {
						if (requiredTrinkets.isEmpty()) {
							return true;
						}
						if (!trinketStack.isEmpty()) {
							requiredTrinkets.removeIf((item) -> item.test(trinketStack));
						}
					}
					
					return requiredTrinkets.isEmpty();
				}
			}
			return false;
		}
	}
	
}
