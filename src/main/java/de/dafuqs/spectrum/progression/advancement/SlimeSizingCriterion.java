package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class SlimeSizingCriterion extends AbstractCriterion<SlimeSizingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("slime_sizing");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
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
		
		@Override
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
