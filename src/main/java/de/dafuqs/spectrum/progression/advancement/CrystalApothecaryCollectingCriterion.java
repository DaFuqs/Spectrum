package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class CrystalApothecaryCollectingCriterion extends SimpleCriterionTrigger<CrystalApothecaryCollectingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("collect_using_crystal_apothecary");
	
	public void trigger(ServerPlayer player, ItemStack itemStack) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack));
	}
	
	@Override
	public Codec<CrystalApothecaryCollectingCriterion.Conditions> codec() {
		return CrystalApothecaryCollectingCriterion.Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate item
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<CrystalApothecaryCollectingCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(CrystalApothecaryCollectingCriterion.Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(CrystalApothecaryCollectingCriterion.Conditions::item)
		).apply(instance, CrystalApothecaryCollectingCriterion.Conditions::new));
		
		public boolean matches(ItemStack stack) {
			return this.item.test(stack);
		}
		
	}
	
}
