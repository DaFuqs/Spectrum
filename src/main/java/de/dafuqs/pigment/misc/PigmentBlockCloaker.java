package de.dafuqs.pigment.misc;

import de.dafuqs.pigment.RevelationToast;
import de.dafuqs.pigment.accessor.WorldRendererAccessor;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentClientAdvancements;
import de.dafuqs.pigment.registries.PigmentBlocks;
import de.dafuqs.pigment.registries.PigmentItems;
import de.dafuqs.pigment.registries.PigmentSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PigmentBlockCloaker {

    private static final HashMap<Identifier, List<Cloakable>> advancementBlockSwapTriggers = new HashMap<>();

    private static HashMap<BlockState, BlockState> blockSwaps = new HashMap<>();
    private static final HashMap<Item, Item> itemSwaps = new HashMap<>();

    public static void registerAdvancementSwap(Cloakable cloakableBlock, Identifier advancementIdentifier) {
        if(advancementBlockSwapTriggers.containsKey(advancementIdentifier)) {
            advancementBlockSwapTriggers.get(advancementIdentifier).add(cloakableBlock);
        } else {
            List<Cloakable> blocks = new ArrayList<>();
            blocks.add(cloakableBlock);
            advancementBlockSwapTriggers.put(advancementIdentifier, blocks);
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean checkBlockCloaksForNewAdvancements(AdvancementUpdateS2CPacket packet) {
        List<Cloakable> cloakableBlocksToTrigger = new ArrayList<>();

        for(Map.Entry<Identifier, Advancement.Task> earnedEntry : packet.getAdvancementsToEarn().entrySet()) {
            Identifier earnedAdvancementIdentifier = earnedEntry.getKey();
            if(advancementBlockSwapTriggers.containsKey(earnedAdvancementIdentifier)) {
                cloakableBlocksToTrigger.addAll(advancementBlockSwapTriggers.get(earnedAdvancementIdentifier));
            }
        }
        for(Map.Entry<Identifier, AdvancementProgress> progressedEntry : packet.getAdvancementsToProgress().entrySet()) {
            Identifier progressedAdvancementIdentifier = progressedEntry.getKey();
            if(PigmentClientAdvancements.hasDone(progressedAdvancementIdentifier))
            if(advancementBlockSwapTriggers.containsKey(progressedAdvancementIdentifier)) {
                cloakableBlocksToTrigger.addAll(advancementBlockSwapTriggers.get(progressedAdvancementIdentifier));
            }
        }

        if(cloakableBlocksToTrigger.size() > 0) {
            // uncloak the blocks
            for (Cloakable cloakableBlocks : cloakableBlocksToTrigger) {
                cloakableBlocks.setUncloaked();
            }

            // rerender chunks to show newly swapped blocks
            WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
            ((WorldRendererAccessor) renderer).rebuildAllChunks();

            // popup for user
            RevelationToast.showRevelationToast(MinecraftClient.getInstance(), new ItemStack(PigmentBlocks.ALTAR.asItem()), PigmentSoundEvents.NEW_REVELATION);

            return true;
        } else {
            return false;
        }
    }

    public static void cloakAll() {
        for (List<Cloakable> cloakables : advancementBlockSwapTriggers.values()) {
            for(Cloakable cloakableBlock : cloakables) {
                cloakableBlock.setCloaked();
            }
        }
    }

    // BLOCKS
    public static void swapModel(BlockState sourceBlockState, BlockState targetBlockState) {
        blockSwaps.put(sourceBlockState, targetBlockState);
    }

    public static void unswapModel(BlockState blockState) {
        blockSwaps.remove(blockState);
    }

    public static void unswapAllBlockStates(Block block) {
        HashMap<BlockState, BlockState> modelSwapperCopy = new HashMap<>();
        for(BlockState blockState : blockSwaps.keySet()) {
            if(blockState.getBlock().equals(block)) {
                modelSwapperCopy.remove(blockState);
            }
        }
        blockSwaps = modelSwapperCopy;
    }

    public static boolean isSwapped(BlockState blockState) {
        return blockSwaps.containsKey(blockState);
    }

    public static BlockState getTarget(BlockState blockState) {
        return blockSwaps.get(blockState);
    }
    
    // ITEMS
    public static void swapModel(Item sourceItem, Item targetItem) {
        itemSwaps.put(sourceItem, targetItem);
    }

    public static void unswapModel(Item item) {
        itemSwaps.remove(item);
    }

    public static boolean isSwapped(Item item) {
        return itemSwaps.containsKey(item);
    }

    public static Item getTarget(Item item) {
        return itemSwaps.get(item);
    }


}
