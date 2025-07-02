package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class PastelNodeUpgradeCriterion extends SimpleCriterionTrigger<PastelNodeUpgradeCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("pastel_node_upgrade");
	
	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, conditions -> conditions.matches(stack));
	}
	
	@Override
	public Codec<PastelNodeUpgradeCriterion.Conditions> codec() {
		return PastelNodeUpgradeCriterion.Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate item
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<PastelNodeUpgradeCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(PastelNodeUpgradeCriterion.Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("upgrade", ItemPredicate.Builder.item().build()).forGetter(PastelNodeUpgradeCriterion.Conditions::item)
		).apply(instance, PastelNodeUpgradeCriterion.Conditions::new));
		
		public boolean matches(ItemStack stack) {
			return this.item.test(stack);
		}
		
	}
}
