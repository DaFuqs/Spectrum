package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.List;

public class AltarCraftingCriterion extends AbstractCriterion<AltarCraftingCriterion.Conditions> {

    static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "crafted_with_altar");

    public Identifier getId() {
        return ID;
    }

    public AltarCraftingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
        return new AltarCraftingCriterion.Conditions(extended, itemPredicates);
    }

    public void trigger(ServerPlayerEntity player, ItemStack itemStack) {
        this.test(player, (conditions) -> {
            return conditions.matches(itemStack);
        });
    }

    public static AltarCraftingCriterion.Conditions create(ItemPredicate[] item) {
        return new AltarCraftingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item);
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ItemPredicate[] itemPredicates;

        public Conditions(EntityPredicate.Extended player, ItemPredicate[] itemPredicates) {
            super(ID, player);
            this.itemPredicates = itemPredicates;
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.addProperty("items", this.itemPredicates.toString());
            return jsonObject;
        }

        public boolean matches(ItemStack itemStack) {
            List<ItemPredicate> list = new ObjectArrayList(this.itemPredicates);
            if (list.isEmpty()) {
                return true;
            } else {
                if (!itemStack.isEmpty()) {
                    list.removeIf((itemPredicate) -> {
                        return itemPredicate.test(itemStack);
                    });
                }
                return list.isEmpty();
            }
        }
    }

}
