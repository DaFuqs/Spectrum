package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class HasAdvancementCriterion extends AbstractCriterion<HasAdvancementCriterion.Conditions> {
	
	public static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "has_advancement");
	
	public static HasAdvancementCriterion.Conditions create(Identifier id) {
		return new HasAdvancementCriterion.Conditions(EntityPredicate.Extended.EMPTY, id);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public HasAdvancementCriterion.Conditions conditionsFromAdvancementIdentifier(EntityPredicate.Extended extended, Identifier identifier) {
		return new HasAdvancementCriterion.Conditions(extended, identifier);
	}
	
	public HasAdvancementCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "advancement_identifier"));
		return new HasAdvancementCriterion.Conditions(extended, identifier);
	}
	
	public void trigger(ServerPlayerEntity player, Advancement advancement) {
		this.trigger(player, (conditions) -> conditions.matches(advancement));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Identifier advancementIdentifier;
		
		public Conditions(EntityPredicate.Extended player, Identifier advancementIdentifier) {
			super(ID, player);
			this.advancementIdentifier = advancementIdentifier;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("advancement_identifier", this.advancementIdentifier.toString());
			return jsonObject;
		}
		
		public boolean matches(Advancement advancement) {
			return this.advancementIdentifier.equals(advancement.getId());
		}
		
		public Identifier getAdvancementIdentifier() {
			return advancementIdentifier;
		}
	}
	
}
