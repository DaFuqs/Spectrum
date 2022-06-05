package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BlockBrokenCriterion extends AbstractCriterion<BlockBrokenCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "block_broken");
	
	public Identifier getId() {
		return ID;
	}
	
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
