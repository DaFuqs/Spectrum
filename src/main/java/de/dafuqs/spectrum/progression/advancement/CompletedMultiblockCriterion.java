package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;

public class CompletedMultiblockCriterion extends AbstractCriterion<CompletedMultiblockCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("completed_multiblock");
	
	public static CompletedMultiblockCriterion.Conditions create(Identifier id) {
		return new CompletedMultiblockCriterion.Conditions(EntityPredicate.Extended.EMPTY, id);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public CompletedMultiblockCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "multiblock_identifier"));
		return new CompletedMultiblockCriterion.Conditions(extended, identifier);
	}
	
	public void trigger(ServerPlayerEntity player, IMultiblock iMultiblock) {
		this.trigger(player, (conditions) -> conditions.matches(iMultiblock));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Identifier identifier;
		
		public Conditions(EntityPredicate.Extended player, Identifier identifier) {
			super(ID, player);
			this.identifier = identifier;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("multiblock_identifier", this.identifier.toString());
			return jsonObject;
		}
		
		public boolean matches(IMultiblock iMultiblock) {
			return iMultiblock.getID().equals(identifier);
		}
	}
	
}
