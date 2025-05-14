package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class EnchanterCraftingCriterion extends SimpleCriterionTrigger<EnchanterCraftingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("enchanter_crafting");
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate itemPredicate,
			MinMaxBounds.Ints spentExperience
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<EnchanterCraftingCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(Conditions::itemPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("spent_experience", MinMaxBounds.Ints.ANY).forGetter(Conditions::spentExperience)
		).apply(instance, EnchanterCraftingCriterion.Conditions::new));
		
		public boolean matches(ItemStack stack, int spentExperience) {
			if (!this.itemPredicate.test(stack)) {
				return false;
			} else {
				return this.spentExperience.matches(spentExperience);
			}
		}
	}
	
}
