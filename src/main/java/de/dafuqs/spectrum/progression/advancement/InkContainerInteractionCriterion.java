package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

import java.util.*;

public class InkContainerInteractionCriterion extends SimpleCriterionTrigger<InkContainerInteractionCriterion.Conditions> {
	
	public static final String NAME = "ink_container_interaction";
	
	public void trigger(ServerPlayer player, ItemStack stack, InkStorage storage, InkColor changeColor, long changeAmount) {
		this.trigger(player, (conditions) -> conditions.matches(stack, storage.getEnergy(), changeColor, changeAmount));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate itemPredicate,
			Map<InkColor, LongRange> colorRanges,
			InkColorPredicate changeInkColorPredicate,
			LongRange changeRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(Conditions::itemPredicate),
				CodecHelper.registryMap(SpectrumRegistries.INK_COLOR, LongRange.CODEC).forGetter(Conditions::colorRanges),
				InkColorPredicate.CODEC.optionalFieldOf("change_color", InkColorPredicate.ANY).forGetter(Conditions::changeInkColorPredicate),
				LongRange.CODEC.optionalFieldOf("change_amount", LongRange.ANY).forGetter(Conditions::changeRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemStack stack, Map<InkColor, Long> colors, InkColor changeColor, long change) {
			return itemPredicate.test(stack)
					&& changeRange.test(change)
					&& changeInkColorPredicate.test(changeColor)
					&& colorRanges.entrySet().stream().allMatch(entry -> colorRanges.get(entry.getKey()).test(colors.get(entry.getKey())));
			
		}
	}
	
}