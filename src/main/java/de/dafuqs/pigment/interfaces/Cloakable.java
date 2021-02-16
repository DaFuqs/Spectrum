package de.dafuqs.pigment.interfaces;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.accessor.WorldRendererAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
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

import java.util.Collections;
import java.util.List;

public interface Cloakable {

    boolean isCloaked(PlayerEntity playerEntity, BlockState blockState);
    void setCloaked();
    void setUncloaked();
    boolean wasLastCloaked();
    void setLastCloaked(boolean lastCloaked);

    // TODO: Way to check this on the server without ClientPlayerEntity
    default void checkCloak(BlockState state) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

        boolean isCloaked = isCloaked(clientPlayerEntity, state);
        if (isCloaked != wasLastCloaked()) {
            if (clientPlayerEntity != null && MinecraftClient.getInstance().world != null) {
                if (MinecraftClient.getInstance().worldRenderer != null && MinecraftClient.getInstance().player != null) {

                    if(isCloaked) {
                        setCloaked();
                    } else {
                        setUncloaked();
                    }

                    WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
                    ((WorldRendererAccessor) renderer).rebuildAllChunks();
                }
            }
            setLastCloaked(isCloaked);
        }
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
        if(lootPlayerEntity == null || isCloaked(lootPlayerEntity, state)) {
            cloakedBlockState = PigmentCommon.getModelSwapper().getTarget(state);
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
