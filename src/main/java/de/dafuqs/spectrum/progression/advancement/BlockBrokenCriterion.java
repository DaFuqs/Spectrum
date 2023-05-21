package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class BlockBrokenCriterion extends AbstractCriterion<BlockBrokenCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("block_broken");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public BlockBrokenCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		BrokenBlockPredicate brokenBlockPredicate = BrokenBlockPredicate.fromJson(jsonObject.get("broken_block"));
		return new BlockBrokenCriterion.Conditions(extended, brokenBlockPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, BlockState minedBlock) {
		this.trigger(player, (conditions) -> conditions.matches(minedBlock));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final BrokenBlockPredicate brokenBlockPredicate;
		
		public Conditions(EntityPredicate.Extended player, @Nullable BrokenBlockPredicate brokenBlockPredicate) {
			super(ID, player);
			this.brokenBlockPredicate = brokenBlockPredicate;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("broken_block", this.brokenBlockPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(BlockState blockState) {
			return this.brokenBlockPredicate.test(blockState);
		}
	}
	
}
