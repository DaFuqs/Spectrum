package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class PastelNodeUpgradeCriterion extends AbstractCriterion<PastelNodeUpgradeCriterion.Conditions> {

    static final Identifier ID = SpectrumCommon.locate("pastel_node_upgrade");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        var upgrade = ItemPredicate.fromJson(obj.get("upgrade"));
        return new PastelNodeUpgradeCriterion.Conditions(playerPredicate, upgrade);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, ItemStack upgrade) {
        this.trigger(player, (c) -> c.matches(upgrade));
    }

    public static class Conditions extends AbstractCriterionConditions {

        private final ItemPredicate upgrade;

        public Conditions(LootContextPredicate playerPredicate, ItemPredicate upgrade) {
            super(PastelNodeUpgradeCriterion.ID, playerPredicate);
            this.upgrade = upgrade;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("upgrade", this.upgrade.toJson());
            return jsonObject;
        }

        public boolean matches(ItemStack stack) {
            return upgrade.test(stack);
        }

    }
}
