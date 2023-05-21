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
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		BlockPredicate blockPredicate = BlockPredicate.fromJson(jsonObject.get("block"));
		return new CrystallarieumGrownCriterion.Conditions(extended, itemPredicate, blockPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, ServerWorld world, BlockPos pos) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, world, pos));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		private final BlockPredicate blockPredicate;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, BlockPredicate blockPredicate) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			this.blockPredicate = blockPredicate;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("item", this.itemPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack stack, ServerWorld world, BlockPos blockPos) {
			return this.itemPredicate.test(stack) && this.blockPredicate.test(world, blockPos);
		}
	}
	
}
