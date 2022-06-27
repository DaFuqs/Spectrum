package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FireblazeSmeltingCriterion extends AbstractCriterion<FireblazeSmeltingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "fireblaze_smelting");
	
	public Identifier getId() {
		return ID;
	}
	
	public FireblazeSmeltingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate input = ItemPredicate.fromJson(jsonObject.get("input"));
		ItemPredicate output = ItemPredicate.fromJson(jsonObject.get("output"));
		NumberRange.FloatRange speedMultiplierRange = NumberRange.FloatRange.fromJson(jsonObject.get("speed_multiplier"));
		NumberRange.FloatRange yieldMultiplierRange = NumberRange.FloatRange.fromJson(jsonObject.get("yield_multiplier"));
		NumberRange.FloatRange experienceMultiplierRange = NumberRange.FloatRange.fromJson(jsonObject.get("experience_multiplier"));
		
		return new FireblazeSmeltingCriterion.Conditions(extended, input, output, speedMultiplierRange, yieldMultiplierRange, experienceMultiplierRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack input, ItemStack output, float speedMultiplier, float yieldMultiplier, float experienceMultiplier) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(input, output, speedMultiplier, yieldMultiplier, experienceMultiplier);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final ItemPredicate input;
		private final ItemPredicate output;
		private final NumberRange.FloatRange speedMultiplierRange;
		private final NumberRange.FloatRange yieldMultiplierRange;
		private final NumberRange.FloatRange experienceMultiplierRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate input, ItemPredicate output, NumberRange.FloatRange speedMultiplierRange, NumberRange.FloatRange yieldMultiplierRange, NumberRange.FloatRange experienceMultiplierRange) {
			super(ID, player);
			this.input = input;
			this.output = output;
			this.speedMultiplierRange = speedMultiplierRange;
			this.yieldMultiplierRange = yieldMultiplierRange;
			this.experienceMultiplierRange = experienceMultiplierRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("input", this.input.toJson());
			jsonObject.add("output", this.output.toJson());
			jsonObject.add("speed_multiplier", this.speedMultiplierRange.toJson());
			jsonObject.add("yield_multiplier", this.yieldMultiplierRange.toJson());
			jsonObject.add("experience_multiplier", this.experienceMultiplierRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack input, ItemStack output, float speedMultiplier, float yieldMultiplier, float experienceMultiplier) {
			return (this.input.test(input) && this.output.test(output) && this.speedMultiplierRange.test(speedMultiplier) && this.yieldMultiplierRange.test(yieldMultiplier) && this.experienceMultiplierRange.test(experienceMultiplier));
		}
		
	}
	
}
