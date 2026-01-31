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
import static de.dafuqs.spectrum.registries.SpectrumItems.item;

public class AE2Compat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredBlock<SpectrumClusterBlock> SMALL_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> CERTUS_QUARTZ_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("certus_quartz_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.PURPLE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> FLUIX_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("fluix_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static DeferredBlock<Block> PURE_CERTUS_QUARTZ_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_certus_quartz_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	public static DeferredBlock<Block> PURE_FLUIX_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_fluix_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)), InkColors.YELLOW)));
	
	public static DeferredItem<Item> PURE_CERTUS_QUARTZ = SpectrumItems.register(SpectrumItems.simple(item("pure_certus_quartz", () -> new Item(IS.of()), InkColors.YELLOW)));
	public static DeferredItem<Item> PURE_FLUIX = SpectrumItems.register(SpectrumItems.simple(item("pure_fluix", () -> new Item(IS.of()), InkColors.YELLOW)));
	
	@Override
	public void register() {
		// TODO: port
		/*
		NeoForge.EVENT_BUS.register(new CreativeSubTabEvent(SpectrumItemGroups.MAIN, SpectrumItemGroups.PURE_RESOURCES, new CreativeModeTab.Output() {
			@Override
			public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
				accept(PURE_CERTUS_QUARTZ);
				accept(SMALL_CERTUS_QUARTZ_BUD);
				accept(LARGE_CERTUS_QUARTZ_BUD);
				accept(CERTUS_QUARTZ_CLUSTER);
				accept(PURE_CERTUS_QUARTZ_BLOCK);
				
				accept(PURE_FLUIX);
				accept(SMALL_FLUIX_BUD);
				accept(LARGE_FLUIX_BUD);
				accept(FLUIX_CLUSTER);
				accept(PURE_FLUIX_BLOCK);
			}
		}));
	}
	
	@Override
	public void registerClient() {
	
	}
	
}
