package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import vazkii.patchouli.api.IMultiblock;

public class CompletedMultiblockCriterion extends AbstractCriterion<CompletedMultiblockCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "completed_multiblock");
	
	public static CompletedMultiblockCriterion.Conditions create(Identifier id) {
		return new CompletedMultiblockCriterion.Conditions(EntityPredicate.Extended.EMPTY, id);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public CompletedMultiblockCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "multiblock_identifier"));
		return new CompletedMultiblockCriterion.Conditions(extended, identifier);
	}
	
	public void trigger(ServerPlayerEntity player, IMultiblock iMultiblock) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(iMultiblock);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Identifier identifier;
		
		public Conditions(EntityPredicate.Extended player, Identifier identifier) {
			super(ID, player);
			this.identifier = identifier;
		}
		
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
