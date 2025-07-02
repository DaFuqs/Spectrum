package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.*;

public class TakeOffBeltJumpCriterion extends SimpleCriterionTrigger<TakeOffBeltJumpCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("take_off_belt_jump");
	
	public static TakeOffBeltJumpCriterion.Conditions create(ItemPredicate itemPredicate, MinMaxBounds.Ints chargesRange) {
		return new TakeOffBeltJumpCriterion.Conditions(Optional.empty(), itemPredicate, chargesRange);
	}
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, (conditions) -> {
			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
			if (component.isPresent()) {
				List<Tuple<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.TAKE_OFF_BELT);
				if (!equipped.isEmpty()) {
					ItemStack firstBelt = equipped.getFirst().getB();
					if (firstBelt != null) {
						int charge = TakeOffBeltItem.getCurrentCharge(player);
						if (charge > 0) {
							return conditions.matches(firstBelt, charge);
						}
					}
				}
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
			ItemPredicate itemPredicate,
			MinMaxBounds.Ints chargesRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(Conditions::itemPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("charges", MinMaxBounds.Ints.ANY).forGetter(Conditions::chargesRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack beltStack, int charge) {
			return itemPredicate.test(beltStack) && this.chargesRange.matches(charge);
		}
	}
	
}
