package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlockEntity;
import de.dafuqs.spectrum.blocks.bottomless_bundle.BottomlessBundleBlockEntity;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.cinderhearth.CinderhearthBlockEntity;
import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumBlockEntity;
import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumBlockEntityRenderer;
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
import de.dafuqs.spectrum.blocks.stonesetting_workshop.StoneSettingWorkshopBlockEntityRenderer;
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

public class SpectrumBlockEntities<T extends BlockEntity> {
	
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
	public static BlockEntityType<CinderhearthBlockEntity> CINDERHEARTH;
	
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
	
	private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, id), FabricBlockEntityTypeBuilder.create(factory, blocks).build());
	}
	
	public static void register() {
		OMINOUS_SAPLING = register("ominous_sapling_block_entity", OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING);
		PEDESTAL = register("pedestal_block_entity", PedestalBlockEntity::new, SpectrumBlocks.PEDESTAL_BASIC_AMETHYST, SpectrumBlocks.PEDESTAL_BASIC_TOPAZ, SpectrumBlocks.PEDESTAL_BASIC_CITRINE, SpectrumBlocks.PEDESTAL_ALL_BASIC, SpectrumBlocks.PEDESTAL_ONYX, SpectrumBlocks.PEDESTAL_MOONSTONE);
		FUSION_SHRINE = register("fusion_shrine_block_entity", FusionShrineBlockEntity::new, SpectrumBlocks.FUSION_SHRINE_BASALT, SpectrumBlocks.FUSION_SHRINE_CALCITE);
		ENCHANTER = register("enchanter_block_entity", EnchanterBlockEntity::new, SpectrumBlocks.ENCHANTER);
		STONESETTING_WORKSHOP = register("stonesetting_workshop", StonesettingWorkshopBlockEntity::new, SpectrumBlocks.STONESETTING_WORKSHOP);
		ITEM_BOWL = register("item_bowl_block_entity", ItemBowlBlockEntity::new, SpectrumBlocks.ITEM_BOWL_BASALT, SpectrumBlocks.ITEM_BOWL_CALCITE);
		ENDER_DROPPER = register("ender_dropper", EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER);
		ENDER_HOPPER = register("ender_hopper", EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER);
		PARTICLE_SPAWNER = register("particle_spawner", ParticleSpawnerBlockEntity::new, SpectrumBlocks.PARTICLE_SPAWNER);
		CREATIVE_PARTICLE_SPAWNER = register("creative_particle_spawner", CreativeParticleSpawnerBlockEntity::new, SpectrumBlocks.CREATIVE_PARTICLE_SPAWNER);
		DEEPER_DOWN_PORTAL = register("deeper_down_portal", DeeperDownPortalBlockEntity::new, SpectrumBlocks.DEEPER_DOWN_PORTAL);
		COMPACTING_CHEST = register("compacting_chest", CompactingChestBlockEntity::new, SpectrumBlocks.COMPACTING_CHEST);
		RESTOCKING_CHEST = register("restocking_chest", RestockingChestBlockEntity::new, SpectrumBlocks.RESTOCKING_CHEST);
		PRIVATE_CHEST = register("private_chest", PrivateChestBlockEntity::new, SpectrumBlocks.PRIVATE_CHEST);
		SUCKING_CHEST = register("sucking_chest", SuckingChestBlockEntity::new, SpectrumBlocks.SUCKING_CHEST);
		TREASURE_CHEST = register("treasure_chest", TreasureChestBlockEntity::new, SpectrumBlocks.TREASURE_CHEST);
		PLAYER_DETECTOR = register("player_detector", PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR);
		REDSTONE_CALCULATOR = register("redstone_calculator", RedstoneCalculatorBlockEntity::new, SpectrumBlocks.REDSTONE_CALCULATOR);
		REDSTONE_WIRELESS = register("redstone_wireless", RedstoneWirelessBlockEntity::new, SpectrumBlocks.REDSTONE_WIRELESS);
		BLOCK_PLACER = register("block_placer", BlockPlacerBlockEntity::new, SpectrumBlocks.BLOCK_PLACER);
		BLOCK_FLOODER = register("block_flooder", BlockFlooderBlockEntity::new, SpectrumBlocks.BLOCK_FLOODER);
		SHOOTING_STAR = register("shooting_star", ShootingStarBlockEntity::new, SpectrumBlocks.COLORFUL_SHOOTING_STAR, SpectrumBlocks.FIERY_SHOOTING_STAR, SpectrumBlocks.GEMSTONE_SHOOTING_STAR, SpectrumBlocks.GLISTERING_SHOOTING_STAR, SpectrumBlocks.PRISTINE_SHOOTING_STAR);
		BOTTOMLESS_BUNDLE = register("bottomless_bundle", BottomlessBundleBlockEntity::new, SpectrumBlocks.BOTTOMLESS_BUNDLE);
		POTION_WORKSHOP = register("potion_workshop", PotionWorkshopBlockEntity::new, SpectrumBlocks.POTION_WORKSHOP);
		SPIRIT_INSTILLER = register("spirit_instiller", SpiritInstillerBlockEntity::new, SpectrumBlocks.SPIRIT_INSTILLER);
		MEMORY = register("memory", MemoryBlockEntity::new, SpectrumBlocks.MEMORY);
		JADE_VINE_ROOTS = register("jade_vine_roots", JadeVineRootsBlockEntity::new, SpectrumBlocks.JADE_VINE_ROOTS);
		CRYSTALLARIEUM = register("crystallarieum", CrystallarieumBlockEntity::new, SpectrumBlocks.CRYSTALLARIEUM);
		CRYSTAL_APOTHECARY = register("crystal_apothecary", CrystalApothecaryBlockEntity::new, SpectrumBlocks.CRYSTAL_APOTHECARY);
		COLOR_PICKER = register("color_picker", ColorPickerBlockEntity::new, SpectrumBlocks.COLOR_PICKER);
		CINDERHEARTH = register("cinderhearth", CinderhearthBlockEntity::new, SpectrumBlocks.CINDERHEARTH);
		
		// All the pastel network nodes
		CONNECTION_NODE = register("connection_node", PastelNetworkConnectionNode::new, SpectrumBlocks.CONNECTION_NODE);
		PROVIDER_NODE = register("provider_node", PastelNetworkProviderNodeBlockEntity::new, SpectrumBlocks.PROVIDER_NODE);
		STORAGE_NODE = register("storage_node", PastelNetworkStorageNodeBlockEntity::new, SpectrumBlocks.STORAGE_NODE);
		PUSHER_NODE = register("pusher_node", PastelNetworkPusherNodeBlockEntity::new, SpectrumBlocks.PUSHER_NODE);
		PULLER_NODE = register("puller_node", PastelNetworkPullerNodeBlockEntity::new, SpectrumBlocks.PULLER_NODE);
		
		PRESERVATION_CONTROLLER = register("preservation_controller", PreservationControllerBlockEntity::new, SpectrumBlocks.PRESERVATION_CONTROLLER);
		
		// All the upgrades
		List<Block> upgradeBlocksList = UpgradeBlock.getRegisteredUpgradeBlocks();
		Block[] upgradeBlocksArray = new Block[upgradeBlocksList.size()];
		upgradeBlocksArray = upgradeBlocksList.toArray(upgradeBlocksArray);
		UPGRADE_BLOCK = register("upgrade_block", UpgradeBlockEntity::new, upgradeBlocksArray);
		
		// All the skulls
		List<Block> skullBlocksList = new ArrayList<>();
		skullBlocksList.addAll(SpectrumBlocks.getMobHeads());
		skullBlocksList.addAll(SpectrumBlocks.getMobWallHeads());
		
		Block[] skullBlocksArray = new Block[skullBlocksList.size()];
		skullBlocksArray = skullBlocksList.toArray(skullBlocksArray);
		SKULL = register("skull", SpectrumSkullBlockEntity::new, skullBlocksArray);
	}
	
	public static void registerClient() {
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.PRIVATE_CHEST, PrivateChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.COMPACTING_CHEST, CompactingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.RESTOCKING_CHEST, RestockingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.TREASURE_CHEST, SpectrumChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.SUCKING_CHEST, SuckingChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.UPGRADE_BLOCK, UpgradeBlockBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.FUSION_SHRINE, FusionShrineBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.ENCHANTER, EnchanterBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.ITEM_BOWL, ItemBowlBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.SKULL, SpectrumSkullBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.DEEPER_DOWN_PORTAL, DeeperDownPortalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.SPIRIT_INSTILLER, SpiritInstillerBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.JADE_VINE_ROOTS, JadeVineRootsBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.STONESETTING_WORKSHOP, StoneSettingWorkshopBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.CRYSTALLARIEUM, CrystallarieumBlockEntityRenderer::new);
		
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.CONNECTION_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.PROVIDER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.STORAGE_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.PUSHER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(SpectrumBlockEntities.PULLER_NODE, PastelNetworkNodeBlockEntityRenderer::new);
		
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
