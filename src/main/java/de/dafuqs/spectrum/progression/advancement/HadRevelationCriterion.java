package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class HadRevelationCriterion extends AbstractCriterion<HadRevelationCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "had_revelation");
	
	public static HadRevelationCriterion.Conditions create(Identifier id) {
		return new HadRevelationCriterion.Conditions(EntityPredicate.Extended.EMPTY, id);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public HadRevelationCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "revelation_identifier"));
		return new HadRevelationCriterion.Conditions(extended, identifier);
	}
	
	public void trigger(ServerPlayerEntity player, Cloakable cloakable) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(cloakable);
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
			jsonObject.addProperty("revelation_identifier", this.identifier.toString());
			return jsonObject;
		}
		
		public boolean matches(Cloakable cloakable) {
			if (identifier.getPath().isEmpty()) {
				// if "revelation_identifier": "" => trigger with any revelation
				return true;
			} else if (cloakable instanceof Block) {
				Block cloakableBlock = (Block) cloakable;
				return Registry.BLOCK.getId(cloakableBlock).equals(identifier);
			} else if (cloakable instanceof Item) {
				Item cloakableItem = (Item) cloakable;
				return Registry.ITEM.getId(cloakableItem).equals(identifier);
			} else {
				return false;
			}
		}
	}
	
}
