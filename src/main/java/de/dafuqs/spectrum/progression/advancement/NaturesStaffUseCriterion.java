package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class NaturesStaffUseCriterion extends AbstractCriterion<NaturesStaffUseCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "natures_staff_conversion");
	
	@Nullable
	private static Block getBlock(JsonObject obj, String propertyName) {
		if (obj.has(propertyName)) {
			Identifier identifier = new Identifier(JsonHelper.getString(obj, propertyName));
			return Registry.BLOCK.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + identifier + "'"));
		} else {
			return null;
		}
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public NaturesStaffUseCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Block sourceBlock = getBlock(jsonObject, "source_block");
		StatePredicate sourceStatePredicate = StatePredicate.fromJson(jsonObject.get("source_state"));
		
		if (sourceBlock != null) {
			sourceStatePredicate.check(sourceBlock.getStateManager(), (name) -> {
				throw new JsonSyntaxException("Block " + sourceBlock + " has no property " + name);
			});
		}
		
		Block targetBlock = getBlock(jsonObject, "target_block");
		StatePredicate targetStatePredicate = StatePredicate.fromJson(jsonObject.get("target_state"));
		if (targetBlock != null) {
			targetStatePredicate.check(targetBlock.getStateManager(), (name) -> {
				throw new JsonSyntaxException("Block " + targetBlock + " has no property " + name);
			});
		}
		
		return new NaturesStaffUseCriterion.Conditions(extended, sourceBlock, sourceStatePredicate, targetBlock, targetStatePredicate);
	}
	
	public void trigger(ServerPlayerEntity player, BlockState sourceBlockState, BlockState targetBlockState) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(sourceBlockState, targetBlockState);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		@Nullable
		private final Block sourceBlock;
		private final StatePredicate sourceBlockState;
		@Nullable
		private final Block targetBlock;
		private final StatePredicate targetBlockState;
		
		public Conditions(EntityPredicate.Extended player, Block sourceBlock, StatePredicate sourceBlockState, Block targetBlock, StatePredicate targetBlockState) {
			super(ID, player);
			this.sourceBlock = sourceBlock;
			this.sourceBlockState = sourceBlockState;
			this.targetBlock = targetBlock;
			this.targetBlockState = targetBlockState;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			if (this.sourceBlock != null) {
				jsonObject.addProperty("source_block", Registry.BLOCK.getId(this.sourceBlock).toString());
			}
			jsonObject.add("source_state:", this.sourceBlockState.toJson());
			if (this.targetBlock != null) {
				jsonObject.addProperty("target_block", Registry.BLOCK.getId(this.targetBlock).toString());
			}
			jsonObject.add("target_state", this.targetBlockState.toJson());
			return jsonObject;
		}
		
		public boolean matches(BlockState sourceBlockState, BlockState targetBlockState) {
			if (this.sourceBlock != null && !sourceBlockState.isOf(this.sourceBlock)) {
				return false;
			}
			if (!this.sourceBlockState.test(sourceBlockState)) {
				return false;
			}
			if (this.targetBlock != null && !targetBlockState.isOf(this.targetBlock)) {
				return false;
			} else {
				return this.targetBlockState.test(targetBlockState);
			}
		}
		
	}
	
}
