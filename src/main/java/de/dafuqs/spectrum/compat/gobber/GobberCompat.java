package de.dafuqs.spectrum.compat.gobber;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.client.render.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class GobberCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
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
	public void register() {
		// BLOCKS
		SMALL_GLOBETTE_BUD = new SpectrumClusterBlock(FabricBlockSettings.create().pistonBehavior(PistonBehavior.DESTROY).hardness(1.0f).mapColor(Blocks.BLUE_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), SpectrumClusterBlock.GrowthStage.SMALL);
		LARGE_GLOBETTE_BUD = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_BUD), SpectrumClusterBlock.GrowthStage.LARGE);
		GLOBETTE_CLUSTER = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER);
		SMALL_GLOBETTE_NETHER_BUD = new SpectrumClusterBlock(FabricBlockSettings.create().pistonBehavior(PistonBehavior.DESTROY).hardness(1.0f).mapColor(Blocks.RED_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), SpectrumClusterBlock.GrowthStage.SMALL);
		LARGE_GLOBETTE_NETHER_BUD = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_NETHER_BUD), SpectrumClusterBlock.GrowthStage.LARGE);
		GLOBETTE_NETHER_CLUSTER = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_NETHER_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER);
		SMALL_GLOBETTE_END_BUD = new SpectrumClusterBlock(FabricBlockSettings.create().pistonBehavior(PistonBehavior.DESTROY).hardness(1.0f).mapColor(Blocks.GREEN_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), SpectrumClusterBlock.GrowthStage.SMALL);
		LARGE_GLOBETTE_END_BUD = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_END_BUD), SpectrumClusterBlock.GrowthStage.LARGE);
		GLOBETTE_END_CLUSTER = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_GLOBETTE_END_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER);
		
		PURE_GLOBETTE_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
		PURE_GLOBETTE_NETHER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
		PURE_GLOBETTE_END_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
		
		FabricItemSettings settings = SpectrumItems.IS.of();
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
		PURE_GLOBETTE = new Item(SpectrumItems.IS.of());
		PURE_GLOBETTE_NETHER = new Item(SpectrumItems.IS.of());
		PURE_GLOBETTE_END = new Item(SpectrumItems.IS.of());
		
		SpectrumItems.register("pure_globette", PURE_GLOBETTE, DyeColor.BLUE);
		SpectrumItems.register("pure_globette_nether", PURE_GLOBETTE_NETHER, DyeColor.RED);
		SpectrumItems.register("pure_globette_end", PURE_GLOBETTE_END, DyeColor.GREEN);
		
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_PURE_RESOURCES).register(entries -> {
			entries.add(PURE_GLOBETTE);
			entries.add(SMALL_GLOBETTE_BUD);
			entries.add(LARGE_GLOBETTE_BUD);
			entries.add(GLOBETTE_CLUSTER);
			entries.add(PURE_GLOBETTE_BLOCK);
			
			entries.add(PURE_GLOBETTE_NETHER);
			entries.add(SMALL_GLOBETTE_NETHER_BUD);
			entries.add(LARGE_GLOBETTE_NETHER_BUD);
			entries.add(GLOBETTE_NETHER_CLUSTER);
			entries.add(PURE_GLOBETTE_NETHER_BLOCK);
			
			entries.add(PURE_GLOBETTE_END);
			entries.add(SMALL_GLOBETTE_END_BUD);
			entries.add(LARGE_GLOBETTE_END_BUD);
			entries.add(GLOBETTE_END_CLUSTER);
			entries.add(PURE_GLOBETTE_END_BLOCK);
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
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
