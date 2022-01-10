package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlockEntity;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntity;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.ender.EnderDropperBlockEntity;
import de.dafuqs.spectrum.blocks.ender.EnderHopperBlockEntity;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntity;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.redstone.BlockPlacerBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.PlayerDetectorBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.RedstoneCalculatorBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.RedstoneWirelessBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_sallow.OminousSaplingBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlock;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class SpectrumBlockEntityRegistry<T extends BlockEntity> {

	public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING;
	public static BlockEntityType<PedestalBlockEntity> PEDESTAL;
	public static BlockEntityType<FusionShrineBlockEntity> FUSION_SHRINE;
	public static BlockEntityType<EnchanterBlockEntity> ENCHANTER;
	public static BlockEntityType<ItemBowlBlockEntity> ITEM_BOWL;
	public static BlockEntityType<EnderDropperBlockEntity> ENDER_DROPPER;
	public static BlockEntityType<EnderHopperBlockEntity> ENDER_HOPPER;
	public static BlockEntityType<ParticleSpawnerBlockEntity> PARTICLE_SPAWNER;
	public static BlockEntityType<UpgradeBlockEntity> UPGRADE_BLOCK;
	public static BlockEntityType<SpectrumSkullBlockEntity> SKULL;
	public static BlockEntityType<DeeperDownPortalBlockEntity> DEEPER_DOWN_PORTAL;

	public static BlockEntityType<CompactingChestBlockEntity> COMPACTING_CHEST;
	public static BlockEntityType<RestockingChestBlockEntity> RESTOCKING_CHEST;
	public static BlockEntityType<PrivateChestBlockEntity> PRIVATE_CHEST;
	public static BlockEntityType<SuckingChestBlockEntity> SUCKING_CHEST;

	public static BlockEntityType<PlayerDetectorBlockEntity> PLAYER_DETECTOR;
	public static BlockEntityType<RedstoneCalculatorBlockEntity> REDSTONE_CALCULATOR;
	public static BlockEntityType<RedstoneWirelessBlockEntity> REDSTONE_WIRELESS;
	public static BlockEntityType<BlockPlacerBlockEntity> BLOCK_PLACER;
	public static BlockEntityType<BlockFlooderBlockEntity> BLOCK_FLOODER;
	
	public static BlockEntityType<PastelNetworkConnectionNode> CONNECTION_NODE;
	public static BlockEntityType<PastelNetworkProviderNode> PROVIDER_NODE;
	public static BlockEntityType<PastelNetworkStorageNode> STORAGE_NODE;
	public static BlockEntityType<PastelNetworkPusherNode> PUSHER_NODE;
	public static BlockEntityType<PastelNetworkPullerNode> PULLER_NODE;

	public static void register() {
		OMINOUS_SAPLING = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ominous_sapling_block_entity"), FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING).build());
		PEDESTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pedestal_block_entity"), FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, SpectrumBlocks.PEDESTAL_BASIC_AMETHYST, SpectrumBlocks.PEDESTAL_BASIC_TOPAZ, SpectrumBlocks.PEDESTAL_BASIC_CITRINE, SpectrumBlocks.PEDESTAL_ALL_BASIC, SpectrumBlocks.PEDESTAL_ONYX, SpectrumBlocks.PEDESTAL_MOONSTONE).build());
		FUSION_SHRINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine_block_entity"), FabricBlockEntityTypeBuilder.create(FusionShrineBlockEntity::new, SpectrumBlocks.FUSION_SHRINE_BASALT, SpectrumBlocks.FUSION_SHRINE_CALCITE).build());
		ENCHANTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "enchanter_block_entity"), FabricBlockEntityTypeBuilder.create(EnchanterBlockEntity::new, SpectrumBlocks.ENCHANTER).build());
		ITEM_BOWL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "item_bowl_block_entity"), FabricBlockEntityTypeBuilder.create(ItemBowlBlockEntity::new, SpectrumBlocks.ITEM_BOWL_BASALT, SpectrumBlocks.ITEM_BOWL_CALCITE).build());
		ENDER_DROPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_dropper"), FabricBlockEntityTypeBuilder.create(EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER).build());
		ENDER_HOPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_hopper"), FabricBlockEntityTypeBuilder.create(EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER).build());
		PARTICLE_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "particle_spawner"), FabricBlockEntityTypeBuilder.create(ParticleSpawnerBlockEntity::new, SpectrumBlocks.PARTICLE_SPAWNER).build());
		DEEPER_DOWN_PORTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "deeper_down_portal"), FabricBlockEntityTypeBuilder.create(DeeperDownPortalBlockEntity::new, SpectrumBlocks.DEEPER_DOWN_PORTAL).build());
		COMPACTING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "compacting_chest"), FabricBlockEntityTypeBuilder.create(CompactingChestBlockEntity::new, SpectrumBlocks.COMPACTING_CHEST).build());
		RESTOCKING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "restocking_chest"), FabricBlockEntityTypeBuilder.create(RestockingChestBlockEntity::new, SpectrumBlocks.RESTOCKING_CHEST).build());
		PRIVATE_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "private_chest"), FabricBlockEntityTypeBuilder.create(PrivateChestBlockEntity::new, SpectrumBlocks.PRIVATE_CHEST).build());
		SUCKING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "sucking_chest"), FabricBlockEntityTypeBuilder.create(SuckingChestBlockEntity::new, SpectrumBlocks.SUCKING_CHEST).build());
		PLAYER_DETECTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "player_detector"), FabricBlockEntityTypeBuilder.create(PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR).build());
		REDSTONE_CALCULATOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "redstone_calculator"), FabricBlockEntityTypeBuilder.create(RedstoneCalculatorBlockEntity::new, SpectrumBlocks.REDSTONE_CALCULATOR).build());
		REDSTONE_WIRELESS = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "redstone_wireless"), FabricBlockEntityTypeBuilder.create(RedstoneWirelessBlockEntity::new, SpectrumBlocks.REDSTONE_WIRELESS).build());
		BLOCK_PLACER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "block_placer"), FabricBlockEntityTypeBuilder.create(BlockPlacerBlockEntity::new, SpectrumBlocks.BLOCK_PLACER).build());
		BLOCK_FLOODER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "block_flooder"), FabricBlockEntityTypeBuilder.create(BlockFlooderBlockEntity::new, SpectrumBlocks.BLOCK_FLOODER).build());
		
		// All the pastel network nodes
		CONNECTION_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "connection_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkConnectionNode::new, SpectrumBlocks.CONNECTION_NODE).build());
		PROVIDER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "provider_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkProviderNode::new, SpectrumBlocks.PROVIDER_NODE).build());
		STORAGE_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "storage_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkStorageNode::new, SpectrumBlocks.STORAGE_NODE).build());
		PUSHER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pusher_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkPusherNode::new, SpectrumBlocks.PUSHER_NODE).build());
		PULLER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "puller_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkPullerNode::new, SpectrumBlocks.PULLER_NODE).build());
		
		// All the upgrades
		List<Block> upgradeBlocksList = UpgradeBlock.getRegisteredUpgradeBlocks();
		Block[] upgradeBlocksArray = new Block[upgradeBlocksList.size()];
		upgradeBlocksArray = upgradeBlocksList.toArray(upgradeBlocksArray);
		UPGRADE_BLOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "upgrade_block"), FabricBlockEntityTypeBuilder.create(UpgradeBlockEntity::new, upgradeBlocksArray).build());
		
		// All the skulls
		List<Block> skullBlocksList = new ArrayList<>();
		skullBlocksList.addAll(SpectrumBlocks.getMobHeads());
		skullBlocksList.addAll(SpectrumBlocks.getMobWallHeads());

		Block[] skullBlocksArray = new Block[skullBlocksList.size()];
		skullBlocksArray = skullBlocksList.toArray(skullBlocksArray);
		SKULL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "skull"), FabricBlockEntityTypeBuilder.create(SpectrumSkullBlockEntity::new, skullBlocksArray).build());
	}

	public static void registerClient() {
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.PRIVATE_CHEST, PrivateChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.COMPACTING_CHEST, CompactingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.RESTOCKING_CHEST, RestockingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.SUCKING_CHEST, SuckingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.UPGRADE_BLOCK, UpgradeBlockBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.ENCHANTER, EnchanterBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.ITEM_BOWL, ItemBowlBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.SKULL, SpectrumSkullBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SpectrumBlockEntityRegistry.DEEPER_DOWN_PORTAL, DeeperDownPortalBlockEntityRenderer::new);

		registerTextureAtlasCallback();
	}

	private static void registerTextureAtlasCallback() {
		//Register textures in chest atlas
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((texture, registry) -> {
			registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/private_chest"));
			//registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/sucking_chest"));
		});

		//Register textures in block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((texture, registry) -> {
			registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/pedestal_upgrade_speed"));
		});
	}

}
