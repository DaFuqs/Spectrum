package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
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
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalUpgradeBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalUpgradeBlockBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalUpgradeBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_tree.OminousSaplingBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class SpectrumBlockEntityRegistry<T extends BlockEntity> {

    public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING;
    public static BlockEntityType<PedestalBlockEntity> PEDESTAL;
    public static BlockEntityType<PlayerDetectorBlockEntity> PLAYER_DETECTOR;
    public static BlockEntityType<EnderDropperBlockEntity> ENDER_DROPPER;
    public static BlockEntityType<EnderHopperBlockEntity> ENDER_HOPPER;
    public static BlockEntityType<ParticleSpawnerBlockEntity> PARTICLE_SPAWNER;
    public static BlockEntityType<PedestalUpgradeBlockEntity> PEDESTAL_SPEED_UPGRADE;
    public static BlockEntityType<SpectrumSkullBlockEntity> SKULL;
    public static BlockEntityType<DeeperDownPortalBlockEntity> DEEPER_DOWN_PORTAL;

    public static BlockEntityType<CompactingChestBlockEntity> COMPACTING_CHEST;
    public static BlockEntityType<RestockingChestBlockEntity> RESTOCKING_CHEST;
    public static BlockEntityType<PrivateChestBlockEntity> PRIVATE_CHEST;

    public static void register() {
        OMINOUS_SAPLING = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ominous_sapling_block_entity"), FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING).build());
        PEDESTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pedestal_block_entity"), FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, SpectrumBlocks.PEDESTAL_BASIC_AMETHYST, SpectrumBlocks.PEDESTAL_BASIC_TOPAZ, SpectrumBlocks.PEDESTAL_BASIC_CITRINE, SpectrumBlocks.PEDESTAL_ALL_BASIC, SpectrumBlocks.PEDESTAL_ONYX, SpectrumBlocks.PEDESTAL_MOONSTONE).build());
        PLAYER_DETECTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "player_detector"), FabricBlockEntityTypeBuilder.create(PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR).build());
        ENDER_DROPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_dropper"), FabricBlockEntityTypeBuilder.create(EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER).build());
        ENDER_HOPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_hopper"), FabricBlockEntityTypeBuilder.create(EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER).build());
        PARTICLE_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "particle_spawner"), FabricBlockEntityTypeBuilder.create(ParticleSpawnerBlockEntity::new, SpectrumBlocks.PARTICLE_SPAWNER).build());
        PEDESTAL_SPEED_UPGRADE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pedestal_speed_upgrade"), FabricBlockEntityTypeBuilder.create(PedestalUpgradeBlockEntity::new, SpectrumBlocks.PEDESTAL_SPEED_UPGRADE).build());
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
        BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.PEDESTAL_SPEED_UPGRADE, PedestalUpgradeBlockBlockEntityRenderer::new);
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

        //Register textures in block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((texture, registry) -> {
            registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/pedestal_upgrade_speed"));
        });
    }



}
