package de.dafuqs.spectrum.compat.gobber;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.SpectrumItems.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.registries.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.blockWithItem;
import static de.dafuqs.spectrum.registries.SpectrumBlocks.cluster;

public class GobberCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.BLUE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_NETHER_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_nether_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.RED_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_NETHER_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_nether_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_NETHER_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_NETHER_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_nether_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_NETHER_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_GLOBETTE_END_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_globette_end_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.GREEN_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_GLOBETTE_END_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_globette_end_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_END_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> GLOBETTE_END_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("globette_end_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOBETTE_END_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static DeferredBlock<Block> PURE_GLOBETTE_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.BLUE)));
	public static DeferredBlock<Block> PURE_GLOBETTE_NETHER_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_nether_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.RED)));
	public static DeferredBlock<Block> PURE_GLOBETTE_END_BLOCK = SpectrumBlocks.register(SpectrumBlocks.simple(blockWithItem("pure_globette_end_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)), InkColors.GREEN)));
	
	public static DeferredItem<Item> PURE_GLOBETTE = SpectrumItems.register("pure_globette", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_GLOBETTE_NETHER = SpectrumItems.register("pure_globette_nether", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_GLOBETTE_END = SpectrumItems.register("pure_globette_end", () -> new Item(IS.of()));
	
	@Override
	public void register() {
		// TODO: port
		/*
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_PURE_RESOURCES).register(entries -> {
			entries.accept(PURE_GLOBETTE);
			entries.accept(SMALL_GLOBETTE_BUD);
			entries.accept(LARGE_GLOBETTE_BUD);
			entries.accept(GLOBETTE_CLUSTER);
			entries.accept(PURE_GLOBETTE_BLOCK);
			
			entries.accept(PURE_GLOBETTE_NETHER);
			entries.accept(SMALL_GLOBETTE_NETHER_BUD);
			entries.accept(LARGE_GLOBETTE_NETHER_BUD);
			entries.accept(GLOBETTE_NETHER_CLUSTER);
			entries.accept(PURE_GLOBETTE_NETHER_BLOCK);
			
			entries.accept(PURE_GLOBETTE_END);
			entries.accept(SMALL_GLOBETTE_END_BUD);
			entries.accept(LARGE_GLOBETTE_END_BUD);
			entries.accept(GLOBETTE_END_CLUSTER);
			entries.accept(PURE_GLOBETTE_END_BLOCK);
		});*/
	}
	
	@Override
	public void registerClient() {
	
	}
}
