package de.dafuqs.pigment;

import de.dafuqs.pigment.inventories.AltarScreen;
import de.dafuqs.pigment.inventories.PigmentContainers;
import de.dafuqs.pigment.inventories.PigmentScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PigmentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PigmentBlocks.registerClient();
        PigmentFluids.registerClient();

        PigmentContainers.register();
        ScreenRegistry.register(PigmentScreenHandlerTypes.ALTAR, AltarScreen::new);

        registerBowPredicates(PigmentItems.BEDROCK_BOW);
        registerCrossbowPredicates(PigmentItems.BEDROCK_CROSSBOW);
        registerFishingRodPredicates(PigmentItems.BEDROCK_FISHING_ROD);

        PigmentEntityTypes.registerClient();
        PigmentEntityRenderers.registerClient();

        PigmentClientsidePacketRegistry.initClient();

        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
            registerColorProviders();
        });
    }

    private static void registerColorProviders() {
        // Biome Colors for colored leaves items and blocks
        // They don't use it, but their decay as oak leaves do
        @Nullable BlockColorProvider oakLeavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
        if(oakLeavesBlockColorProvider != null) {
            for(DyeColor dyeColor : DyeColor.values()) {
                Block block = PigmentBlocks.getColoredLeavesBlock(dyeColor);
                ColorProviderRegistry.BLOCK.register(oakLeavesBlockColorProvider, block);
            }
        }

        @Nullable ItemColorProvider oakLeavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
        if(oakLeavesItemColorProvider != null) {
            for(DyeColor dyeColor : DyeColor.values()) {
                Item item = PigmentBlocks.getColoredLeavesItem(dyeColor);
                ColorProviderRegistry.ITEM.register(oakLeavesItemColorProvider, item);
            }
        }
    }

    // Vanilla models see: ModelPredicateProviderRegistry
    public static void registerBowPredicates(BowItem bowItem) {
        FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pull"), (itemStack, world, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (float)(itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
    }

    public static void registerCrossbowPredicates(CrossbowItem crossbowItem) {
        FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(itemStack) ? 0.0F : (float)(itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(itemStack);
            }
        });

        FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });

        FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });

        FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && CrossbowItem.isCharged(itemStack) && CrossbowItem.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }

    public static void registerFishingRodPredicates(FishingRodItem fishingRodItem) {
        FabricModelPredicateProviderRegistry.register(fishingRodItem, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                boolean bl = livingEntity.getMainHandStack() == itemStack;
                boolean bl2 = livingEntity.getOffHandStack() == itemStack;
                if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) {
                    bl2 = false;
                }

                return (bl || bl2) && livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).fishHook != null ? 1.0F : 0.0F;
            }
        });
    }

}
