package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockBrokenCriterion extends AbstractCriterion<BlockBrokenCriterion.Conditions> {

    static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "block_broken");

    public Identifier getId() {
        return ID;
    }

    public BlockBrokenCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {

        Block block = getBlock(jsonObject);
        StatePredicate statePredicate = StatePredicate.fromJson(jsonObject.get("state"));
        if (block != null) {
            statePredicate.check(block.getStateManager(), (name) -> {
                throw new JsonSyntaxException("Block " + block + " has no property " + name);
            });
        }

        return new BlockBrokenCriterion.Conditions(extended, block, statePredicate);
    }

    public void trigger(ServerPlayerEntity player, BlockState blockState) {
        this.test(player, (conditions) -> {
            return conditions.matches(blockState);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {

        private final Block block;
        private final StatePredicate state;

        public Conditions(EntityPredicate.Extended player, @Nullable Block block, StatePredicate state) {
            super(ID, player);
            this.block = block;
            this.state = state;
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            if (this.block != null) {
                jsonObject.addProperty("block", Registry.BLOCK.getId(this.block).toString());
            }

            jsonObject.add("state", this.state.toJson());
            return jsonObject;
        }

        public boolean matches(BlockState blockState) {
            if (this.block != null && !blockState.isOf(this.block) && this.block != Blocks.AIR) {
                return false;
            } else {
                return this.state.test(blockState);
            }
        }
    }

    @Nullable
    private static Block getBlock(@NotNull JsonObject obj) {
        if (obj.has("block")) {
            Identifier identifier = new Identifier(JsonHelper.getString(obj, "block"));
            return Registry.BLOCK.getOrEmpty(identifier).orElse(Blocks.AIR);
        } else {
            return null;
        }
    }

}
