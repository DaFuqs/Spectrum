package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class InertiaUsedCriterion extends AbstractCriterion<InertiaUsedCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "inertia_used");
	
	@Nullable
	private static Block getBlock(JsonObject obj) {
		if (obj.has("block")) {
			Identifier identifier = new Identifier(JsonHelper.getString(obj, "block"));
			return Registry.BLOCK.getOrEmpty(identifier).orElseThrow(() -> {
				return new JsonSyntaxException("Unknown block type '" + identifier + "'");
			});
		} else {
			return null;
		}
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public InertiaUsedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Block block = getBlock(jsonObject);
		StatePredicate statePredicate = StatePredicate.fromJson(jsonObject.get("state"));
		if (block != null) {
			statePredicate.check(block.getStateManager(), (name) -> {
				throw new JsonSyntaxException("Block " + block + " has no property " + name);
			});
		}
		NumberRange.IntRange amountRange = NumberRange.IntRange.fromJson(jsonObject.get("amount"));
		
		return new InertiaUsedCriterion.Conditions(extended, block, statePredicate, amountRange);
	}
	
	public void trigger(ServerPlayerEntity player, BlockState state, int amount) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(state, amount);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		@Nullable
		private final Block block;
		private final StatePredicate state;
		private final NumberRange.IntRange amountRange;
		
		public Conditions(Extended player, @Nullable Block block, StatePredicate state, NumberRange.IntRange amountRange) {
			super(InertiaUsedCriterion.ID, player);
			this.block = block;
			this.state = state;
			this.amountRange = amountRange;
		}
		
		public static InertiaUsedCriterion.Conditions block(Block block, NumberRange.IntRange amountRange) {
			return new InertiaUsedCriterion.Conditions(Extended.EMPTY, block, StatePredicate.ANY, amountRange);
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			if (this.block != null) {
				jsonObject.addProperty("block", Registry.BLOCK.getId(this.block).toString());
			}
			
			jsonObject.add("state", this.state.toJson());
			return jsonObject;
		}
		
		public boolean matches(BlockState state, int amount) {
			if (this.block != null && !state.isOf(this.block)) {
				return false;
			} else {
				return this.state.test(state) && amountRange.test(amount);
			}
		}
	}
}
