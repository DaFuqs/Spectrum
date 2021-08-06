package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.accessor.WorldRendererAccessor;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import de.dafuqs.spectrum.toast.RevelationToast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.util.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ClientBlockCloaker {

    private static final List<BlockState> activeBlockSwaps = new ArrayList<>();
    private static final List<Item> activeItemSwaps = new ArrayList<>();

    public static boolean checkCloaksForNewAdvancements(AdvancementUpdateS2CPacket packet, boolean showToast) {
        List<Cloakable> cloakableBlocksToTrigger = new ArrayList<>();

        for(Map.Entry<Identifier, Advancement.Task> earnedEntry : packet.getAdvancementsToEarn().entrySet()) {
            Identifier earnedAdvancementIdentifier = earnedEntry.getKey();
            if(ClientAdvancements.hasDone(earnedAdvancementIdentifier)) {
                cloakableBlocksToTrigger.addAll(BlockCloakManager.getBlocksToUncloak(earnedAdvancementIdentifier));
            }
        }
        for(Map.Entry<Identifier, AdvancementProgress> progressedEntry : packet.getAdvancementsToProgress().entrySet()) {
            Identifier progressedAdvancementIdentifier = progressedEntry.getKey();
            if(ClientAdvancements.hasDone(progressedAdvancementIdentifier)) {
                cloakableBlocksToTrigger.addAll(BlockCloakManager.getBlocksToUncloak(progressedAdvancementIdentifier));
            }
        }

        if(cloakableBlocksToTrigger.size() > 0) {
            // uncloak the blocks
            for (Cloakable cloakableBlock : cloakableBlocksToTrigger) {
                uncloak(cloakableBlock);
            }

            rebuildAllChunks();

            // popup for user
            if(showToast) {
                RevelationToast.showRevelationToast(MinecraftClient.getInstance(), new ItemStack(SpectrumBlocks.ALTAR.asItem()), SpectrumSoundEvents.NEW_REVELATION);
            }

            return true;
        } else {
            return false;
        }
    }

    // rerender chunks to show newly swapped blocks
    private static void rebuildAllChunks() {
        WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
        ((WorldRendererAccessor) renderer).rebuildAllChunks();
    }

    private static void uncloak(Cloakable cloakable) {
        Set<BlockState> cloakBlockStates = cloakable.getBlockStateCloaks().keySet();
        activeBlockSwaps.removeAll(cloakBlockStates);

        Item cloakItem = cloakable.getItemCloak().getLeft();
        activeItemSwaps.remove(cloakItem);
    }

    private static void cloak(Cloakable cloakable) {
        Set<BlockState> cloakBlockStates = cloakable.getBlockStateCloaks().keySet();
        activeBlockSwaps.addAll(cloakBlockStates);

        Item cloakItem = cloakable.getItemCloak().getLeft();
        activeItemSwaps.add(cloakItem);
    }

    // BLOCKS
    public static boolean isCloaked(BlockState blockState) {
        return activeBlockSwaps.contains(blockState);
    }

    public static BlockState getCloakTarget(BlockState blockState) {
        if(isCloaked(blockState)) {
            return BlockCloakManager.getBlockStateCloak(blockState);
        } else {
            return blockState;
        }
    }
    
    // ITEMS
    public static boolean isCloaked(Item item) {
        return activeItemSwaps.contains(item);
    }

    public static Item getCloakTarget(Item item) {
        if(isCloaked(item)) {
            return BlockCloakManager.getItemCloak(item);
        } else {
            return item;
        }
    }

    public static void cloakAll() {
        HashMap<Identifier, List<Cloakable>> registeredCloaks = BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks();
        for(List<Cloakable> registeredCloak : registeredCloaks.values()) {
            for(Cloakable cloakable : registeredCloak) {
                cloak(cloakable);
            }
        }
    }

}
