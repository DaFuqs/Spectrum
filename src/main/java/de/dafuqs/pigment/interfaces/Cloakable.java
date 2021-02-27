package de.dafuqs.pigment.interfaces;

import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import de.dafuqs.pigment.Support;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.shape.VoxelShape;

import java.util.Collections;
import java.util.List;

public interface Cloakable {

    VoxelShape EMPTY_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    void setCloaked();
    void setUncloaked();
    Identifier getCloakAdvancementIdentifier();

    default void setupCloak() {
        PigmentBlockCloaker.registerAdvancementSwap(this, getCloakAdvancementIdentifier());
    }

    default boolean isCloaked(PlayerEntity playerEntity) {
        return !Support.hasAdvancement(playerEntity, getCloakAdvancementIdentifier());
    }

    default PlayerEntity getLootPlayerEntity(LootContext.Builder lootContextBuilder) {
        if(lootContextBuilder.getNullable(LootContextParameters.THIS_ENTITY) == null) {
            return null;
        } else {
            Entity entity = lootContextBuilder.get(LootContextParameters.THIS_ENTITY);
            if(entity instanceof PlayerEntity) {
                return (PlayerEntity) entity;
            } else {
                return null;
            }
        }
    }

    @Deprecated
    default List<ItemStack> getCloakedDroppedStacks(BlockState state, LootContext.Builder builder) {
        PlayerEntity lootPlayerEntity = getLootPlayerEntity(builder);

        Identifier identifier;
        BlockState cloakedBlockState = null;
        if(lootPlayerEntity == null || isCloaked(lootPlayerEntity)) {
            cloakedBlockState = PigmentBlockCloaker.getTarget(state);
            if(cloakedBlockState == null) {
                identifier = state.getBlock().getLootTableId();
            } else {
                identifier = cloakedBlockState.getBlock().getLootTableId();
            }
        } else {
            identifier = state.getBlock().getLootTableId();
        }

        if (identifier == LootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootContext lootContext;
            if(cloakedBlockState == null) {
                lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
            } else {
                lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, cloakedBlockState).build(LootContextTypes.BLOCK);
            }
            ServerWorld serverWorld = lootContext.getWorld();
            LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
            return lootTable.generateLoot(lootContext);
        }
    }

}
