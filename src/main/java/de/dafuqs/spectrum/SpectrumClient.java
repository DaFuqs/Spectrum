package de.dafuqs.spectrum;

import de.dafuqs.spectrum.entity.SpectrumEntityRenderers;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.items.misc.EnderSpliceItem;
import de.dafuqs.spectrum.particle.SpectrumParticleFactories;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SpectrumClient implements ClientModInitializer {

    @Environment(EnvType.CLIENT)
    public static MinecraftClient minecraftClient;

    @Override
    public void onInitializeClient() {
        SpectrumBlocks.registerClient();
        SpectrumFluids.registerClient();

        SpectrumContainers.register();
        SpectrumScreenHandlerTypes.registerClient();

        registerBowPredicates(SpectrumItems.BEDROCK_BOW);
        registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW);
        registerFishingRodPredicates(SpectrumItems.BEDROCK_FISHING_ROD);
        registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE);
        registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF);

        SpectrumBlockEntityRegistry.registerClient();
        SpectrumEntityTypes.registerClient();
        SpectrumEntityRenderers.registerClient();

        SpectrumS2CPackets.initClient();
        SpectrumParticleFactories.register();

        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
            SpectrumClient.minecraftClient = minecraftClient;
            registerColorProviders();
        });

        EntityRendererRegistry.INSTANCE.register(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, ItemFrameEntityRenderer::new);
        //registerS2CPackets();

    }

    private static void registerColorProviders() {
        // Biome Colors for colored leaves items and blocks
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
    }

    // all packets, that get sent from server to client instance
    /*private static void registerS2CPackets() {
        ClientSidePacketRegistry.INSTANCE.register(SpectrumEntityTypes.SPAWN_PACKET_ID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null) {
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                }
                Entity entity = et.create(MinecraftClient.getInstance().world);
                if (entity == null) {
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                }
                entity.updateTrackedPosition(pos);
                entity.setPos(pos.x, pos.y, pos.z);
                entity.setPitch(pitch);
                entity.setYaw(yaw);
                entity.setId(entityId);
                entity.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, entity);
            });
        });
    }*/

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

    public static void registerEnderSplicePredicates(EnderSpliceItem enderSpliceItem) {
        FabricModelPredicateProviderRegistry.register(enderSpliceItem, new Identifier("bound"), (itemStack, clientWorld, livingEntity, i) -> {
            NbtCompound compoundTag = itemStack.getTag();
            if (compoundTag != null && compoundTag.contains("PosX")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }

    private void registerAnimatedWandPredicates(Item item) {
        FabricModelPredicateProviderRegistry.register(item, new Identifier("in_use"), (itemStack, clientWorld, livingEntity, i) -> {
            return (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack) ? 1.0F : 0.0F;
        });
    }


}
