package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.altar.AltarBlockEntity;
import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import de.dafuqs.spectrum.blocks.chests.PrivateChestBlockEntity;
import de.dafuqs.spectrum.blocks.chests.RestockingChestBlockEntity;
import de.dafuqs.spectrum.blocks.chests.SpectrumChestBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntity;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.detector.PlayerDetectorBlockEntity;
import de.dafuqs.spectrum.blocks.ender.EnderDropperBlockEntity;
import de.dafuqs.spectrum.blocks.ender.EnderHopperBlockEntity;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntity;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntityRenderer3D;
import de.dafuqs.spectrum.blocks.spirit_tree.OminousSaplingBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class SpectrumBlockEntityRegistry<T extends BlockEntity> {

    public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING;
    public static BlockEntityType<AltarBlockEntity> ALTAR;
    public static BlockEntityType<PlayerDetectorBlockEntity> PLAYER_DETECTOR;
    public static BlockEntityType<EnderDropperBlockEntity> ENDER_DROPPER;
    public static BlockEntityType<EnderHopperBlockEntity> ENDER_HOPPER;
    public static BlockEntityType<SpectrumSkullBlockEntity> SKULL;
    public static BlockEntityType<DeeperDownPortalBlockEntity> DEEPER_DOWN_PORTAL;

    public static BlockEntityType<CompactingChestBlockEntity> COMPACTING_CHEST;
    public static BlockEntityType<RestockingChestBlockEntity> RESTOCKING_CHEST;
    public static BlockEntityType<PrivateChestBlockEntity> PRIVATE_CHEST;

    public static void register() {
        OMINOUS_SAPLING = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ominous_sapling_block_entity"), FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING).build());
        ALTAR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "altar_block_entity"), FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, SpectrumBlocks.ALTAR).build());
        PLAYER_DETECTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "player_detector"), FabricBlockEntityTypeBuilder.create(PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR).build());
        ENDER_DROPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_dropper"), FabricBlockEntityTypeBuilder.create(EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER).build());
        ENDER_HOPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_hopper"), FabricBlockEntityTypeBuilder.create(EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER).build());
        DEEPER_DOWN_PORTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "deeper_down_portal"), FabricBlockEntityTypeBuilder.create(DeeperDownPortalBlockEntity::new, SpectrumBlocks.DEEPER_DOWN_PORTAL).build());
        COMPACTING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "compacting_chest"), FabricBlockEntityTypeBuilder.create(CompactingChestBlockEntity::new, SpectrumBlocks.COMPACTING_CHEST).build());
        RESTOCKING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "restocking_chest"), FabricBlockEntityTypeBuilder.create(RestockingChestBlockEntity::new, SpectrumBlocks.RESTOCKING_CHEST).build());
        PRIVATE_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "private_chest"), FabricBlockEntityTypeBuilder.create(PrivateChestBlockEntity::new, SpectrumBlocks.PRIVATE_CHEST).build());

        // All the skulls
        List<Block> skullBlocks = new ArrayList<>();
        skullBlocks.addAll(SpectrumBlocks.getMobHeads());
        skullBlocks.addAll(SpectrumBlocks.getMobWallHeads());

        Block[] blocks = new Block[skullBlocks.size()];
        blocks = skullBlocks.toArray(blocks);
        SKULL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "skull"), FabricBlockEntityTypeBuilder.create(SpectrumSkullBlockEntity::new, blocks).build());
    }

    public static void registerClient() {
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.PRIVATE_CHEST, SpectrumChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.COMPACTING_CHEST, SpectrumChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.RESTOCKING_CHEST, SpectrumChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.SKULL, SpectrumSkullBlockEntityRenderer3D::new);
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.DEEPER_DOWN_PORTAL, DeeperDownPortalBlockEntityRenderer::new);

        registerTextureAtlasCallback();
    }

    public static void registerTextureAtlasCallback() {
        //Register textures in chest atlas
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((texture, registry) -> {
            registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/private_chest"));
            registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/compacting_chest"));
            registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/restocking_chest"));
        });
    }



}
