package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class HummingstoneHymnCriterion extends AbstractCriterion<HummingstoneHymnCriterion.Conditions> {

	static final Identifier ID = SpectrumCommon.locate("hummingstone_hymn");

	public static HummingstoneHymnCriterion.Conditions create(LocationPredicate location) {
		return new HummingstoneHymnCriterion.Conditions(EntityPredicate.Extended.EMPTY, location);
	}

	public Identifier getId() {
		return ID;
	}

	public HummingstoneHymnCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		LocationPredicate locationPredicate = LocationPredicate.fromJson(jsonObject.get("location"));
		return new HummingstoneHymnCriterion.Conditions(extended, locationPredicate);
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(world, pos);
		});
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final LocationPredicate location;

		public Conditions(EntityPredicate.Extended player, LocationPredicate location) {
			super(ID, player);
			this.location = location;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("location", this.location.toJson());
			return jsonObject;
		}

		public boolean matches(ServerWorld world, BlockPos pos) {
			return this.location.test(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5);
		}
	}

}
