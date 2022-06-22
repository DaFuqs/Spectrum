package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlockEntity;
import de.dafuqs.spectrum.blocks.bottomless_bundle.BottomlessBundleBlockEntity;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumBlockEntity;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntity;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.ender.EnderDropperBlockEntity;
import de.dafuqs.spectrum.blocks.ender.EnderHopperBlockEntity;
import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity;
import de.dafuqs.spectrum.blocks.energy.CrystalApothecaryBlockEntity;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlockEntity;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.memory.MemoryBlockEntity;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntity;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.particle_spawner.CreativeParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.BlockPlacerBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.PlayerDetectorBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.RedstoneCalculatorBlockEntity;
import de.dafuqs.spectrum.blocks.redstone.RedstoneWirelessBlockEntity;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.spirit_sallow.OminousSaplingBlockEntity;
import de.dafuqs.spectrum.blocks.stonesetting_workshop.StonesettingWorkshopBlockEntity;
import de.dafuqs.spectrum.blocks.structure.PreservationControllerBlockEntity;
import de.dafuqs.spectrum.blocks.structure.TreasureChestBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlock;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
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

import static de.dafuqs.spectrum.SpectrumCommon.locate;

public class SpectrumBlockEntityRegistry<T extends BlockEntity> {
	
	public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING;
	public static BlockEntityType<PedestalBlockEntity> PEDESTAL;
	public static BlockEntityType<FusionShrineBlockEntity> FUSION_SHRINE;
	public static BlockEntityType<EnchanterBlockEntity> ENCHANTER;

	public static BlockEntityType<StonesettingWorkshopBlockEntity> STONESETTING_WORKSHOP;
	public static BlockEntityType<ItemBowlBlockEntity> ITEM_BOWL;
	public static BlockEntityType<EnderDropperBlockEntity> ENDER_DROPPER;
	public static BlockEntityType<EnderHopperBlockEntity> ENDER_HOPPER;
	public static BlockEntityType<ParticleSpawnerBlockEntity> PARTICLE_SPAWNER;
	public static BlockEntityType<CreativeParticleSpawnerBlockEntity> CREATIVE_PARTICLE_SPAWNER;
	public static BlockEntityType<UpgradeBlockEntity> UPGRADE_BLOCK;
	public static BlockEntityType<SpectrumSkullBlockEntity> SKULL;
	public static BlockEntityType<DeeperDownPortalBlockEntity> DEEPER_DOWN_PORTAL;
	public static BlockEntityType<ShootingStarBlockEntity> SHOOTING_STAR;
	public static BlockEntityType<BottomlessBundleBlockEntity> BOTTOMLESS_BUNDLE;
	public static BlockEntityType<PotionWorkshopBlockEntity> POTION_WORKSHOP;
	public static BlockEntityType<CrystallarieumBlockEntity> CRYSTALLARIEUM;
	
	public static BlockEntityType<CrystalApothecaryBlockEntity> CRYSTAL_APOTHECARY;
	public static BlockEntityType<ColorPickerBlockEntity> COLOR_PICKER;
	
	public static BlockEntityType<CompactingChestBlockEntity> COMPACTING_CHEST;
	public static BlockEntityType<RestockingChestBlockEntity> RESTOCKING_CHEST;
	public static BlockEntityType<PrivateChestBlockEntity> PRIVATE_CHEST;
	public static BlockEntityType<SuckingChestBlockEntity> SUCKING_CHEST;
	public static BlockEntityType<TreasureChestBlockEntity> TREASURE_CHEST;
	
	public static BlockEntityType<PlayerDetectorBlockEntity> PLAYER_DETECTOR;
	public static BlockEntityType<RedstoneCalculatorBlockEntity> REDSTONE_CALCULATOR;
	public static BlockEntityType<RedstoneWirelessBlockEntity> REDSTONE_WIRELESS;
	public static BlockEntityType<BlockPlacerBlockEntity> BLOCK_PLACER;
	public static BlockEntityType<BlockFlooderBlockEntity> BLOCK_FLOODER;
	public static BlockEntityType<SpiritInstillerBlockEntity> SPIRIT_INSTILLER;
	public static BlockEntityType<MemoryBlockEntity> MEMORY;
	public static BlockEntityType<JadeVineRootsBlockEntity> JADE_VINE_ROOTS;
	
	public static BlockEntityType<PastelNetworkConnectionNode> CONNECTION_NODE;
	public static BlockEntityType<PastelNetworkProviderNodeBlockEntity> PROVIDER_NODE;
	public static BlockEntityType<PastelNetworkStorageNodeBlockEntity> STORAGE_NODE;
	public static BlockEntityType<PastelNetworkPusherNodeBlockEntity> PUSHER_NODE;
	public static BlockEntityType<PastelNetworkPullerNodeBlockEntity> PULLER_NODE;
	
	public static BlockEntityType<PreservationControllerBlockEntity> PRESERVATION_CONTROLLER;
	
	public static void register() {
		OMINOUS_SAPLING = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ominous_sapling_block_entity"), FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING).build());
		PEDESTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pedestal_block_entity"), FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, SpectrumBlocks.PEDESTAL_BASIC_AMETHYST, SpectrumBlocks.PEDESTAL_BASIC_TOPAZ, SpectrumBlocks.PEDESTAL_BASIC_CITRINE, SpectrumBlocks.PEDESTAL_ALL_BASIC, SpectrumBlocks.PEDESTAL_ONYX, SpectrumBlocks.PEDESTAL_MOONSTONE).build());
		FUSION_SHRINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine_block_entity"), FabricBlockEntityTypeBuilder.create(FusionShrineBlockEntity::new, SpectrumBlocks.FUSION_SHRINE_BASALT, SpectrumBlocks.FUSION_SHRINE_CALCITE).build());
		ENCHANTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "enchanter_block_entity"), FabricBlockEntityTypeBuilder.create(EnchanterBlockEntity::new, SpectrumBlocks.ENCHANTER).build());
		STONESETTING_WORKSHOP = Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("stonesetting_workshop"), FabricBlockEntityTypeBuilder.create(StonesettingWorkshopBlockEntity::new, SpectrumBlocks.STONESETTING_WORKSHOP).build());
		ITEM_BOWL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "item_bowl_block_entity"), FabricBlockEntityTypeBuilder.create(ItemBowlBlockEntity::new, SpectrumBlocks.ITEM_BOWL_BASALT, SpectrumBlocks.ITEM_BOWL_CALCITE).build());
		ENDER_DROPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_dropper"), FabricBlockEntityTypeBuilder.create(EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER).build());
		ENDER_HOPPER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "ender_hopper"), FabricBlockEntityTypeBuilder.create(EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER).build());
		PARTICLE_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "particle_spawner"), FabricBlockEntityTypeBuilder.create(ParticleSpawnerBlockEntity::new, SpectrumBlocks.PARTICLE_SPAWNER).build());
		CREATIVE_PARTICLE_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "creative_particle_spawner"), FabricBlockEntityTypeBuilder.create(CreativeParticleSpawnerBlockEntity::new, SpectrumBlocks.CREATIVE_PARTICLE_SPAWNER).build());
		DEEPER_DOWN_PORTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "deeper_down_portal"), FabricBlockEntityTypeBuilder.create(DeeperDownPortalBlockEntity::new, SpectrumBlocks.DEEPER_DOWN_PORTAL).build());
		COMPACTING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "compacting_chest"), FabricBlockEntityTypeBuilder.create(CompactingChestBlockEntity::new, SpectrumBlocks.COMPACTING_CHEST).build());
		RESTOCKING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "restocking_chest"), FabricBlockEntityTypeBuilder.create(RestockingChestBlockEntity::new, SpectrumBlocks.RESTOCKING_CHEST).build());
		PRIVATE_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "private_chest"), FabricBlockEntityTypeBuilder.create(PrivateChestBlockEntity::new, SpectrumBlocks.PRIVATE_CHEST).build());
		SUCKING_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "sucking_chest"), FabricBlockEntityTypeBuilder.create(SuckingChestBlockEntity::new, SpectrumBlocks.SUCKING_CHEST).build());
		TREASURE_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "treasure_chest"), FabricBlockEntityTypeBuilder.create(TreasureChestBlockEntity::new, SpectrumBlocks.TREASURE_CHEST).build());
		PLAYER_DETECTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "player_detector"), FabricBlockEntityTypeBuilder.create(PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR).build());
		REDSTONE_CALCULATOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "redstone_calculator"), FabricBlockEntityTypeBuilder.create(RedstoneCalculatorBlockEntity::new, SpectrumBlocks.REDSTONE_CALCULATOR).build());
		REDSTONE_WIRELESS = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "redstone_wireless"), FabricBlockEntityTypeBuilder.create(RedstoneWirelessBlockEntity::new, SpectrumBlocks.REDSTONE_WIRELESS).build());
		BLOCK_PLACER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "block_placer"), FabricBlockEntityTypeBuilder.create(BlockPlacerBlockEntity::new, SpectrumBlocks.BLOCK_PLACER).build());
		BLOCK_FLOODER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "block_flooder"), FabricBlockEntityTypeBuilder.create(BlockFlooderBlockEntity::new, SpectrumBlocks.BLOCK_FLOODER).build());
		SHOOTING_STAR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "shooting_star"), FabricBlockEntityTypeBuilder.create(ShootingStarBlockEntity::new, SpectrumBlocks.COLORFUL_SHOOTING_STAR, SpectrumBlocks.FIERY_SHOOTING_STAR, SpectrumBlocks.GEMSTONE_SHOOTING_STAR, SpectrumBlocks.GLISTERING_SHOOTING_STAR, SpectrumBlocks.PRISTINE_SHOOTING_STAR).build());
		BOTTOMLESS_BUNDLE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "bottomless_bundle"), FabricBlockEntityTypeBuilder.create(BottomlessBundleBlockEntity::new, SpectrumBlocks.BOTTOMLESS_BUNDLE).build());
		POTION_WORKSHOP = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "potion_workshop"), FabricBlockEntityTypeBuilder.create(PotionWorkshopBlockEntity::new, SpectrumBlocks.POTION_WORKSHOP).build());
		SPIRIT_INSTILLER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "spirit_instiller"), FabricBlockEntityTypeBuilder.create(SpiritInstillerBlockEntity::new, SpectrumBlocks.SPIRIT_INSTILLER).build());
		MEMORY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "memory"), FabricBlockEntityTypeBuilder.create(MemoryBlockEntity::new, SpectrumBlocks.MEMORY).build());
		JADE_VINE_ROOTS = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "jade_vine_roots"), FabricBlockEntityTypeBuilder.create(JadeVineRootsBlockEntity::new, SpectrumBlocks.JADE_VINE_ROOTS).build());
		CRYSTALLARIEUM = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "crystallarieum"), FabricBlockEntityTypeBuilder.create(CrystallarieumBlockEntity::new, SpectrumBlocks.CRYSTALLARIEUM).build());
		CRYSTAL_APOTHECARY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "crystal_apothecary"), FabricBlockEntityTypeBuilder.create(CrystalApothecaryBlockEntity::new, SpectrumBlocks.CRYSTAL_APOTHECARY).build());
		COLOR_PICKER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "color_picker"), FabricBlockEntityTypeBuilder.create(ColorPickerBlockEntity::new, SpectrumBlocks.COLOR_PICKER).build());
		
		// All the pastel network nodes
		CONNECTION_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "connection_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkConnectionNode::new, SpectrumBlocks.CONNECTION_NODE).build());
		PROVIDER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "provider_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkProviderNodeBlockEntity::new, SpectrumBlocks.PROVIDER_NODE).build());
		STORAGE_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "storage_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkStorageNodeBlockEntity::new, SpectrumBlocks.STORAGE_NODE).build());
		PUSHER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pusher_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkPusherNodeBlockEntity::new, SpectrumBlocks.PUSHER_NODE).build());
		PULLER_NODE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "puller_node"), FabricBlockEntityTypeBuilder.create(PastelNetworkPullerNodeBlockEntity::new, SpectrumBlocks.PULLER_NODE).build());
		
		PRESERVATION_CONTROLLER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, "preservation_controller"), FabricBlockEntityTypeBuilder.create(PreservationControllerBlockEntity::new, SpectrumBlocks.PRESERVATION_CONTROLLER).build());
		
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
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.PRIVATE_CHEST, PrivateChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.COMPACTING_CHEST, CompactingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.RESTOCKING_CHEST, RestockingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.TREASURE_CHEST, SpectrumChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.SUCKING_CHEST, SuckingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.UPGRADE_BLOCK, UpgradeBlockBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.ENCHANTER, EnchanterBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.ITEM_BOWL, ItemBowlBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.SKULL, SpectrumSkullBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.DEEPER_DOWN_PORTAL, DeeperDownPortalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.SPIRIT_INSTILLER, SpiritInstillerBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.JADE_VINE_ROOTS, JadeVineRootsBlockEntityRenderer::new);
		
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.CONNECTION_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.PROVIDER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.STORAGE_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.PUSHER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntityRegistry.PULLER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		
		registerTextureAtlasCallback();
	}
	
	private static void registerTextureAtlasCallback() {
		// textures that are only referenced in code have to be added to the texture atlas manually
		
		//Register textures in chest atlas
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((texture, registry) -> {
			registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/private_chest"));
			registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/treasure_chest"));
		});
		
		//Register textures in block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((texture, registry) -> {
			registry.register(new Identifier(SpectrumCommon.MOD_ID, "entity/pedestal_upgrade_speed"));
		});
	}
	
}
