package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class EnchantmentUpgradedCriterion extends SimpleCriterionTrigger<EnchantmentUpgradedCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("enchantment_upgraded");
	
	public void trigger(ServerPlayer player, ItemEnchantments enchantmentsComponent, int spentExperience) {
		this.trigger(player, (conditions) -> conditions.matches(enchantmentsComponent, spentExperience));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			EnchantmentPredicate enchantmentPredicate,
			MinMaxBounds.Ints spentExperience
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				EnchantmentPredicate.CODEC.optionalFieldOf("enchantments", new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.ANY)).forGetter(Conditions::enchantmentPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("spentExperience", MinMaxBounds.Ints.ANY).forGetter(Conditions::spentExperience)
		).apply(instance, Conditions::new));
		
		public boolean matches(ItemEnchantments itemEnchantmentsComponent, int spentExperience) {
			if (this.enchantmentPredicate.containedIn(itemEnchantmentsComponent)) {
				return this.spentExperience.matches(spentExperience);
			}
			return false;
		}
	}
	
}
