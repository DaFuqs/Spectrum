package de.dafuqs.spectrum.compat.ae2;

import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class AE2Compat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
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
	public void register() {
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
	public void registerClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(SMALL_CERTUS_QUARTZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(LARGE_CERTUS_QUARTZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CERTUS_QUARTZ_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SMALL_FLUIX_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(LARGE_FLUIX_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FLUIX_CLUSTER, RenderLayer.getCutout());
	}
}
