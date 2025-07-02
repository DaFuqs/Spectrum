package de.dafuqs.spectrum.compat.ae2;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.SpectrumItems.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;
import static de.dafuqs.spectrum.registries.SpectrumBlocks.simple;
import static de.dafuqs.spectrum.registries.SpectrumItems.simple;
import static de.dafuqs.spectrum.registries.SpectrumItems.item;

public class AE2Compat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static Block SMALL_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_certus_quartz_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block LARGE_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_certus_quartz_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block CERTUS_QUARTZ_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("certus_quartz_cluster", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block SMALL_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_fluix_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.PURPLE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block LARGE_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_fluix_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block FLUIX_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("fluix_cluster", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static Block PURE_CERTUS_QUARTZ_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_certus_quartz_block", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	public static Block PURE_FLUIX_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_fluix_block", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	
	public static Item PURE_CERTUS_QUARTZ = SpectrumItems.register(simple(item("pure_certus_quartz", new Item(IS.of()), InkColors.YELLOW)));
	public static Item PURE_FLUIX = SpectrumItems.register(simple(item("pure_fluix", new Item(IS.of()), InkColors.YELLOW)));
	
	@Override
	public void register() {
		SpectrumItems.ITEM_REGISTRAR.flush();
		SpectrumBlocks.COMMON_REGISTRAR.flush();
		
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_PURE_RESOURCES).register(entries -> {
			entries.accept(PURE_CERTUS_QUARTZ);
			entries.accept(SMALL_CERTUS_QUARTZ_BUD);
			entries.accept(LARGE_CERTUS_QUARTZ_BUD);
			entries.accept(CERTUS_QUARTZ_CLUSTER);
			entries.accept(PURE_CERTUS_QUARTZ_BLOCK);
			
			entries.accept(PURE_FLUIX);
			entries.accept(SMALL_FLUIX_BUD);
			entries.accept(LARGE_FLUIX_BUD);
			entries.accept(FLUIX_CLUSTER);
			entries.accept(PURE_FLUIX_BLOCK);
		});
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void registerClient() {
		SpectrumBlocks.CLIENT_REGISTRAR.flush();
	}
	
}
