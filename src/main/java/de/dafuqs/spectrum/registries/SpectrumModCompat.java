package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

import java.util.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class SpectrumModCompat {
	
	protected static final Map<String, ModIntegrationPack> INTEGRATION_PACKS = new HashMap<>();
	
	protected abstract static class ModIntegrationPack {
		abstract void register();
		
		abstract void registerClient();
	}
	
	protected static void registerContainer(String modId, ModIntegrationPack container) {
		if (!SpectrumCommon.CONFIG.IntegrationPacksToSkipLoading.contains(modId) && FabricLoader.getInstance().isModLoaded(modId)) {
			INTEGRATION_PACKS.put(modId, container);
		}
	}
	
	public static void register() {
		registerContainer("ae2", new AE2Compat());
		registerContainer("gobber2", new GobberCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register();
		}
	}
	
	public static void registerClient() {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient();
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
	public static class AE2Compat extends ModIntegrationPack {
		
		public static Block SMALL_CERTUS_QUARTZ_BUD;
		public static Block LARGE_CERTUS_QUARTZ_BUD;
		public static Block CERTUS_QUARTZ_CLUSTER;
		public static Block SMALL_FLUIX_BUD;
		public static Block LARGE_FLUIX_BUD;
		public static Block FLUIX_CLUSTER;
		
		public static Block PURE_CERTUS_QUARTZ_BLOCK;
		public static Block PURE_FLUIX_BLOCK;
		
		public static Item PURE_CERTUS_QUARTZ;
		public static Item PURE_FLUIX;
		
		@Override
		void register() {
			// BLOCKS
			SMALL_CERTUS_QUARTZ_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(MapColor.TERRACOTTA_WHITE).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
			LARGE_CERTUS_QUARTZ_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_CERTUS_QUARTZ_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
			CERTUS_QUARTZ_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_CERTUS_QUARTZ_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
			SMALL_FLUIX_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.PURPLE_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
			LARGE_FLUIX_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_FLUIX_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
			FLUIX_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_FLUIX_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
			
			PURE_CERTUS_QUARTZ_BLOCK = new Block(FabricBlockSettings.of(Material.GLASS, MapColor.PALE_YELLOW).strength(0.3F).sounds(BlockSoundGroup.GLASS));
			PURE_FLUIX_BLOCK = new Block(FabricBlockSettings.of(Material.GLASS, MapColor.PALE_YELLOW).strength(0.3F).sounds(BlockSoundGroup.GLASS));
			
			FabricItemSettings settings = SpectrumItems.Tab.RESOURCES.settings();
			registerBlockWithItem("small_certus_quartz_bud", SMALL_CERTUS_QUARTZ_BUD, settings, DyeColor.YELLOW);
			registerBlockWithItem("large_certus_quartz_bud", LARGE_CERTUS_QUARTZ_BUD, settings, DyeColor.YELLOW);
			registerBlockWithItem("certus_quartz_cluster", CERTUS_QUARTZ_CLUSTER, settings, DyeColor.YELLOW);
			
			registerBlockWithItem("small_fluix_bud", SMALL_FLUIX_BUD, settings, DyeColor.YELLOW);
			registerBlockWithItem("large_fluix_bud", LARGE_FLUIX_BUD, settings, DyeColor.YELLOW);
			registerBlockWithItem("fluix_cluster", FLUIX_CLUSTER, settings, DyeColor.YELLOW);
			
			registerBlockWithItem("pure_certus_quartz_block", PURE_CERTUS_QUARTZ_BLOCK, settings, DyeColor.YELLOW);
			registerBlockWithItem("pure_fluix_block", PURE_FLUIX_BLOCK, settings, DyeColor.YELLOW);
			
			// ITEMS
			PURE_CERTUS_QUARTZ = new Item(SpectrumItems.Tab.RESOURCES.settings());
			PURE_FLUIX = new Item(SpectrumItems.Tab.RESOURCES.settings());
			SpectrumItems.register("pure_certus_quartz", PURE_CERTUS_QUARTZ, DyeColor.YELLOW);
			SpectrumItems.register("pure_fluix", PURE_FLUIX, DyeColor.YELLOW);
		}
		
		@Override
		void registerClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(SMALL_CERTUS_QUARTZ_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(LARGE_CERTUS_QUARTZ_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(CERTUS_QUARTZ_CLUSTER, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(SMALL_FLUIX_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(LARGE_FLUIX_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(FLUIX_CLUSTER, RenderLayer.getCutout());
		}
	}
	
	public static class GobberCompat extends ModIntegrationPack {
		
		public static Block SMALL_GLOBETTE_BUD;
		public static Block LARGE_GLOBETTE_BUD;
		public static Block GLOBETTE_CLUSTER;
		public static Block SMALL_GLOBETTE_NETHER_BUD;
		public static Block LARGE_GLOBETTE_NETHER_BUD;
		public static Block GLOBETTE_NETHER_CLUSTER;
		public static Block SMALL_GLOBETTE_END_BUD;
		public static Block LARGE_GLOBETTE_END_BUD;
		public static Block GLOBETTE_END_CLUSTER;
		
		public static Block PURE_GLOBETTE_BLOCK;
		public static Block PURE_GLOBETTE_NETHER_BLOCK;
		public static Block PURE_GLOBETTE_END_BLOCK;
		
		public static Item PURE_GLOBETTE;
		public static Item PURE_GLOBETTE_NETHER;
		public static Item PURE_GLOBETTE_END;
		
		@Override
		void register() {
			// BLOCKS
			SMALL_GLOBETTE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.BLUE_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
			LARGE_GLOBETTE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
			GLOBETTE_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
			SMALL_GLOBETTE_NETHER_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.RED_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
			LARGE_GLOBETTE_NETHER_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_NETHER_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
			GLOBETTE_NETHER_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_NETHER_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
			SMALL_GLOBETTE_END_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.GREEN_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
			LARGE_GLOBETTE_END_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_END_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
			GLOBETTE_END_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_END_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
			
			PURE_GLOBETTE_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
			PURE_GLOBETTE_NETHER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
			PURE_GLOBETTE_END_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
			
			FabricItemSettings settings = SpectrumItems.Tab.RESOURCES.settings();
			registerBlockWithItem("small_globette_bud", SMALL_GLOBETTE_BUD, settings, DyeColor.BLUE);
			registerBlockWithItem("large_globette_bud", LARGE_GLOBETTE_BUD, settings, DyeColor.BLUE);
			registerBlockWithItem("globette_cluster", GLOBETTE_CLUSTER, settings, DyeColor.BLUE);
			
			registerBlockWithItem("small_globette_nether_bud", SMALL_GLOBETTE_NETHER_BUD, settings, DyeColor.RED);
			registerBlockWithItem("large_globette_nether_bud", LARGE_GLOBETTE_NETHER_BUD, settings, DyeColor.RED);
			registerBlockWithItem("globette_nether_cluster", GLOBETTE_NETHER_CLUSTER, settings, DyeColor.RED);
			
			registerBlockWithItem("small_globette_end_bud", SMALL_GLOBETTE_END_BUD, settings, DyeColor.GREEN);
			registerBlockWithItem("large_globette_end_bud", LARGE_GLOBETTE_END_BUD, settings, DyeColor.GREEN);
			registerBlockWithItem("globette_end_cluster", GLOBETTE_END_CLUSTER, settings, DyeColor.GREEN);
			
			registerBlockWithItem("pure_globette_block", PURE_GLOBETTE_BLOCK, settings, DyeColor.BLUE);
			registerBlockWithItem("pure_globette_nether_block", PURE_GLOBETTE_NETHER_BLOCK, settings, DyeColor.RED);
			registerBlockWithItem("pure_globette_end_block", PURE_GLOBETTE_END_BLOCK, settings, DyeColor.GREEN);
			
			// ITEMS
			PURE_GLOBETTE = new Item(SpectrumItems.Tab.RESOURCES.settings());
			PURE_GLOBETTE_NETHER = new Item(SpectrumItems.Tab.RESOURCES.settings());
			PURE_GLOBETTE_END = new Item(SpectrumItems.Tab.RESOURCES.settings());
			
			SpectrumItems.register("pure_globette", PURE_GLOBETTE, DyeColor.BLUE);
			SpectrumItems.register("pure_globette_nether", PURE_GLOBETTE_NETHER, DyeColor.RED);
			SpectrumItems.register("pure_globette_end", PURE_GLOBETTE_END, DyeColor.GREEN);
		}
		
		@Override
		void registerClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(SMALL_GLOBETTE_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(LARGE_GLOBETTE_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(GLOBETTE_CLUSTER, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(SMALL_GLOBETTE_END_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(LARGE_GLOBETTE_END_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(GLOBETTE_END_CLUSTER, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(SMALL_GLOBETTE_NETHER_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(LARGE_GLOBETTE_NETHER_BUD, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(GLOBETTE_NETHER_CLUSTER, RenderLayer.getCutout());
		}
	}
	
}
