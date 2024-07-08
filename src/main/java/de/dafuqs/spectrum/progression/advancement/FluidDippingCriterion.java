package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class FluidDippingCriterion extends AbstractCriterion<FluidDippingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("fluid_dipping");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, ItemStack previousStack, ItemStack targetStack) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, previousStack, targetStack));
	}
	
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		FluidPredicate fluidPredicate = FluidPredicate.fromJson(jsonObject.get("fluid"));
		ItemPredicate previousStackPredicate = ItemPredicate.fromJson(jsonObject.get("source_stack"));
		ItemPredicate targetStackPredicate = ItemPredicate.fromJson(jsonObject.get("target_stack"));
		return new FluidDippingCriterion.Conditions(playerPredicate, fluidPredicate, previousStackPredicate, targetStackPredicate);
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final FluidPredicate fluidPredicate;
		private final ItemPredicate previousStackPredicate;
		private final ItemPredicate targetStackPredicate;
		
		public Conditions(LootContextPredicate player, FluidPredicate fluidPredicate, ItemPredicate previousStackPredicate, ItemPredicate targetStackPredicate) {
			super(ID, player);
			this.fluidPredicate = fluidPredicate;
			this.previousStackPredicate = previousStackPredicate;
			this.targetStackPredicate = targetStackPredicate;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("fluid", this.fluidPredicate.toJson());
			jsonObject.add("previous_stack", this.previousStackPredicate.toJson());
			jsonObject.add("target_stack", this.targetStackPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerWorld world, BlockPos pos, ItemStack previousStack, ItemStack targetStack) {
			return this.fluidPredicate.test(world, pos) && this.previousStackPredicate.test(previousStack) && this.targetStackPredicate.test(targetStack);
		}
	}
	
}
