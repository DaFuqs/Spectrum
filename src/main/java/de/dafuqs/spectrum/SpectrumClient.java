package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.blocks.fluid.SpectrumFluids;
import de.dafuqs.spectrum.inventories.AltarScreen;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;

public class SpectrumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpectrumBlocks.registerClient();
        SpectrumFluids.registerClient();

        SpectrumContainers.register();
        ScreenRegistry.register(SpectrumScreenHandlerTypes.ALTAR, AltarScreen::new);

        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
            // Biome Colors for colored leaves
            // They don't use it, but their decay as oak leaves do
            @Nullable BlockColorProvider oakLeavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
            if(oakLeavesBlockColorProvider != null) {
                for(DyeColor dyeColor : DyeColor.values()) {
                    Block block = SpectrumBlocks.getColoredLeavesBlock(dyeColor);
                    ColorProviderRegistry.BLOCK.register(oakLeavesBlockColorProvider, block);
                }
            }

            @Nullable ItemColorProvider oakLeavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
            if(oakLeavesItemColorProvider != null) {
                for(DyeColor dyeColor : DyeColor.values()) {
                    Item item = SpectrumBlocks.getColoredLeavesItem(dyeColor);
                    ColorProviderRegistry.ITEM.register(oakLeavesItemColorProvider, item);
                }
            }
        });
    }




}
