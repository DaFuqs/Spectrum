package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.registry.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class NaturesStaffConversionCriterion extends AbstractCriterion<NaturesStaffConversionCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("natures_staff_conversion");
	
	@Nullable
	private static Block getBlock(JsonObject obj, String propertyName) {
		if (obj.has(propertyName)) {
			Identifier identifier = new Identifier(JsonHelper.getString(obj, propertyName));
			return Registries.BLOCK.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + identifier + "'"));
		} else {
			return null;
		}
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public NaturesStaffConversionCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
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

		return new NaturesStaffConversionCriterion.Conditions(extended, sourceBlock, sourceStatePredicate, targetBlock, targetStatePredicate);
	}
	
	public void trigger(ServerPlayerEntity player, BlockState sourceBlockState, BlockState targetBlockState) {
		this.trigger(player, (conditions) -> conditions.matches(sourceBlockState, targetBlockState));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		@Nullable
		private final Block sourceBlock;
		private final StatePredicate sourceBlockState;
		@Nullable
		private final Block targetBlock;
		private final StatePredicate targetBlockState;
		
		public Conditions(LootContextPredicate player, @Nullable Block sourceBlock, StatePredicate sourceBlockState, @Nullable Block targetBlock, StatePredicate targetBlockState) {
			super(ID, player);
			this.sourceBlock = sourceBlock;
			this.sourceBlockState = sourceBlockState;
			this.targetBlock = targetBlock;
			this.targetBlockState = targetBlockState;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			if (this.sourceBlock != null) {
				jsonObject.addProperty("source_block", Registries.BLOCK.getId(this.sourceBlock).toString());
			}
			jsonObject.add("source_state:", this.sourceBlockState.toJson());
			if (this.targetBlock != null) {
				jsonObject.addProperty("target_block", Registries.BLOCK.getId(this.targetBlock).toString());
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
