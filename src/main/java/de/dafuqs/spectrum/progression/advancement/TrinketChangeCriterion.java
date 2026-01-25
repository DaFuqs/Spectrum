package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.*;
import org.jetbrains.annotations.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;

public class TrinketChangeCriterion extends SimpleCriterionTrigger<TrinketChangeCriterion.Conditions> {
	
	public static final String NAME = "trinket_change";
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, (conditions) -> {
			Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosInventory(player);
			if (curiosHandler.isPresent()) {
				List<ItemStack> equippedStacks = new ArrayList<>();
				int spectrumStacks = 0;
				IItemHandlerModifiable equippedCurios = curiosHandler.get().getEquippedCurios();
				for (int i = 0; i < equippedCurios.getSlots(); i++) {
					ItemStack stack = equippedCurios.getStackInSlot(i);
					
					equippedStacks.add(stack);
					if (stack.is(SpectrumItemTags.TRINKETS)) {
						spectrumStacks++;
					}
				}
				return conditions.matches(equippedStacks, equippedStacks.size(), spectrumStacks);
			}
			return false;
		});
	}
	
	@Override
	public @NotNull Codec<Conditions> codec() {
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
