package de.dafuqs.spectrum.compat.gobber;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.SpectrumItems.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.registries.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.blockWithItem;
import static de.dafuqs.spectrum.registries.SpectrumBlocks.cluster;

public class GobberCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.BLUE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_NETHER_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_nether_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.RED_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_NETHER_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_nether_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_NETHER_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_NETHER_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_nether_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_NETHER_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_END_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_end_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.GREEN_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_END_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_end_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_END_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_END_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_end_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_END_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static DeferredBlock<Block> PURE_GLOBETTE_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.BLUE)));
	public static DeferredBlock<Block> PURE_GLOBETTE_NETHER_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_nether_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.RED)));
	public static DeferredBlock<Block> PURE_GLOBETTE_END_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_end_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.GREEN)));
	
	public static DeferredItem<Item> PURE_GLOBETTE = SpectrumItems.register("pure_globette", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_GLOBETTE_NETHER = SpectrumItems.register("pure_globette_nether", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_GLOBETTE_END = SpectrumItems.register("pure_globette_end", () -> new Item(IS.of()));
	
	@Override
	public void register(IEventBus modBus) {
	
	}
	
	@Override
	public void registerClient(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(SMALL_GLOBETTE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_GLOBETTE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GLOBETTE_CLUSTER.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(SMALL_GLOBETTE_NETHER_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_GLOBETTE_NETHER_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GLOBETTE_NETHER_CLUSTER.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(SMALL_GLOBETTE_END_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_GLOBETTE_END_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GLOBETTE_END_CLUSTER.get(), RenderType.cutout());
	}
}
