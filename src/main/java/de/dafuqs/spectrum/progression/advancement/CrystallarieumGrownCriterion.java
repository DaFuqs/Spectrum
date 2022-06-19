package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CrystallarieumGrownCriterion extends AbstractCriterion<CrystallarieumGrownCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "crystallarieum_growing");
	
	public static CrystallarieumGrownCriterion.Conditions create(ItemPredicate item, BlockPredicate blockPredicate) {
		return new CrystallarieumGrownCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, blockPredicate);
	}
	
	public Identifier getId() {
		return ID;
	}
	
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
