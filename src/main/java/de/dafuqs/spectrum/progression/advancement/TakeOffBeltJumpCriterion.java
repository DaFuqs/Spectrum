package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

import java.util.*;

public class TakeOffBeltJumpCriterion extends SimpleCriterionTrigger<TakeOffBeltJumpCriterion.Conditions> {
	
	public static final String NAME = "take_off_belt_jump";
	
	public static TakeOffBeltJumpCriterion.Conditions create(ItemPredicate itemPredicate, MinMaxBounds.Ints chargesRange) {
		return new TakeOffBeltJumpCriterion.Conditions(Optional.empty(), itemPredicate, chargesRange);
	}
	
	public void trigger(ServerPlayer player) {
		Optional<ItemStack> takeOffBelt = SpectrumCurioItem.getFirstEquipped(player, SpectrumItems.TAKE_OFF_BELT.get());
		if(takeOffBelt.isEmpty()) {
			return;
		}
		
		this.trigger(player, conditions -> conditions.matches(takeOffBelt.get(), TakeOffBeltItem.getCurrentCharge(player)));
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
