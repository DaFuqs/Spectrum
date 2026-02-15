package de.dafuqs.spectrum.compat.ae2;

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
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.registries.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class AE2Compat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredBlock<SpectrumClusterBlock> SMALL_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> CERTUS_QUARTZ_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("certus_quartz_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.PURPLE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> FLUIX_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("fluix_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static DeferredBlock<Block> PURE_CERTUS_QUARTZ_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_certus_quartz_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	public static DeferredBlock<Block> PURE_FLUIX_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_fluix_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	
	public static DeferredItem<Item> PURE_CERTUS_QUARTZ = SpectrumItems.register("pure_certus_quartz", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_FLUIX = SpectrumItems.register("pure_fluix", () -> new Item(IS.of()));
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void registerClient() {
	
	}
	
}
