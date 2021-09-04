package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.accessor.WorldRendererAccessor;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import de.dafuqs.spectrum.toast.RevelationToast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class ClientBlockCloaker {

    private static final List<BlockState> activeBlockSwaps = new ArrayList<>();
    private static final List<Item> activeItemSwaps = new ArrayList<>();

    public static void process(List<Identifier> doneAdvancements, boolean showToast) {
        List<Cloakable> cloakableBlocksToTrigger = new ArrayList<>();
        for(Identifier doneAdvancement: doneAdvancements) {
            cloakableBlocksToTrigger.addAll(BlockCloakManager.getBlocksToUncloak(doneAdvancement));
        }

        if(cloakableBlocksToTrigger.size() > 0) {
            // uncloak the blocks
            for (Cloakable cloakable : cloakableBlocksToTrigger) {
                uncloak(cloakable);
            }

            rebuildAllChunks();

            // popup for user
            if(showToast) {
                RevelationToast.showRevelationToast(MinecraftClient.getInstance(), new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST.asItem()), SpectrumSoundEvents.NEW_REVELATION);
            }
        }
    }

    // rerender chunks to show newly swapped blocks
    static void rebuildAllChunks() {
        WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
        ((WorldRendererAccessor) renderer).rebuildAllChunks();
    }

    static void uncloak(Cloakable cloakable) {
        Set<BlockState> cloakBlockStates = cloakable.getBlockStateCloaks().keySet();
        activeBlockSwaps.removeAll(cloakBlockStates);

        Item cloakItem = cloakable.getItemCloak().getLeft();
        activeItemSwaps.remove(cloakItem);

        cloakable.onUncloak();
    }

    private static void cloak(Cloakable cloakable) {
        Set<BlockState> cloakBlockStates = cloakable.getBlockStateCloaks().keySet();
        activeBlockSwaps.addAll(cloakBlockStates);

        Item cloakItem = cloakable.getItemCloak().getLeft();
        activeItemSwaps.add(cloakItem);

        cloakable.onCloak();
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
        activeItemSwaps.clear();
        activeBlockSwaps.clear();
        HashMap<Identifier, List<Cloakable>> registeredCloaks = BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks();
        for(List<Cloakable> registeredCloak : registeredCloaks.values()) {
            for(Cloakable cloakable : registeredCloak) {
                cloak(cloakable);
            }
        }
    }

}
