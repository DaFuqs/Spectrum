package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.accessor.WorldRendererAccessor;
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

    abstract boolean isCloaked(PlayerEntity playerEntity, BlockState blockState);
    abstract void setCloaked();
    abstract void setUncloaked();
    abstract boolean wasLastCloaked();
    abstract void setLastCloaked(boolean lastCloaked);

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
        Entity entity = lootContextBuilder.get(LootContextParameters.THIS_ENTITY);
        if(entity instanceof PlayerEntity) {
            return (PlayerEntity) entity;
        } else {
            return null;
        }
    }

    @Deprecated
    default List<ItemStack> getCloakedDroppedStacks(BlockState state, LootContext.Builder builder) {
        PlayerEntity lootPlayerEntity = getLootPlayerEntity(builder);

        Identifier identifier;
        BlockState cloakedBlockState = null;
        if(lootPlayerEntity == null || isCloaked(lootPlayerEntity, state)) {
            cloakedBlockState = SpectrumCommon.getModelSwapper().getTarget(state);
            identifier = cloakedBlockState.getBlock().getLootTableId();
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
