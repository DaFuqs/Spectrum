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

public class CrystallarieumGrownCriterion extends AbstractCriterion<CrystallarieumGrownCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("crystallarieum_growing");
	
	public static CrystallarieumGrownCriterion.Conditions create(ItemPredicate item, BlockPredicate blockPredicate) {
		return new CrystallarieumGrownCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, blockPredicate);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public CrystallarieumGrownCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		BlockPredicate grownBlockPredicate = BlockPredicate.fromJson(jsonObject.get("grown_block"));
		ItemPredicate catalystPredicate = ItemPredicate.fromJson(jsonObject.get("used_catalyst"));
		return new CrystallarieumGrownCriterion.Conditions(extended, catalystPredicate, grownBlockPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, ItemStack catalystStack) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, catalystStack));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate catalystPredicate;
		private final BlockPredicate blockPredicate;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate catalystPredicate, BlockPredicate blockPredicate) {
			super(ID, player);
			this.catalystPredicate = catalystPredicate;
			this.blockPredicate = blockPredicate;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("grown_block", this.blockPredicate.toJson());
			jsonObject.add("used_catalyst", this.catalystPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerWorld world, BlockPos blockPos, ItemStack catalystStack) {
			return this.blockPredicate.test(world, blockPos) && this.catalystPredicate.test(catalystStack);
		}
	}
	
}
