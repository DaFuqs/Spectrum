package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SlimeSizingCriterion extends AbstractCriterion<SlimeSizingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "slime_sizing");
	
	public Identifier getId() {
		return ID;
	}
	
	public SlimeSizingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange sizeRange = NumberRange.IntRange.fromJson(jsonObject.get("size"));
		
		return new SlimeSizingCriterion.Conditions(extended, sizeRange);
	}
	
	public void trigger(ServerPlayerEntity player, int size) {
		this.trigger(player, (conditions) -> conditions.matches(size));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final NumberRange.IntRange sizeRange;
		
		public Conditions(Extended player, NumberRange.IntRange sizeRange) {
			super(SlimeSizingCriterion.ID, player);
			this.sizeRange = sizeRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("size", this.sizeRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(int size) {
			return this.sizeRange.test(size);
		}
	}
	
}
