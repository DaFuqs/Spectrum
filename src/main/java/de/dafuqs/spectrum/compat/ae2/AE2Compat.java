package de.dafuqs.spectrum.compat.ae2;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.gemstone.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.create.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.SpectrumItems.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.registries.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class AE2Compat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredBlock<SpectrumClusterBlock> SMALL_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_CERTUS_QUARTZ_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_certus_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> CERTUS_QUARTZ_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("certus_quartz_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_CERTUS_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.PURPLE_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_FLUIX_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_fluix_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> FLUIX_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("fluix_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_FLUIX_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER)), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static DeferredBlock<Block> PURE_CERTUS_QUARTZ_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_certus_quartz_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)))));
	public static DeferredBlock<Block> PURE_FLUIX_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_fluix_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.3F).sound(SoundType.GLASS)))));
	
	public static DeferredItem<Item> PURE_CERTUS_QUARTZ = SpectrumItems.register("pure_certus_quartz", () -> new Item(IS.of()));
	public static DeferredItem<Item> PURE_FLUIX = SpectrumItems.register("pure_fluix", () -> new Item(IS.of()));
	
	@Override
	public void register(IEventBus modBus) {
		NeoForge.EVENT_BUS.addListener(AE2Compat::addItemsToSubTabs);
	}
	
	@Override
	public void registerClient(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(SMALL_CERTUS_QUARTZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_CERTUS_QUARTZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CERTUS_QUARTZ_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_FLUIX_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_FLUIX_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FLUIX_CLUSTER.get(), RenderType.cutout());
	}
	
	@SubscribeEvent
	public static void addItemsToSubTabs(CreativeSubTabEvent event) {
		ResourceLocation subGroupId = event.subGroup().getIdentifier();
		
		if (subGroupId.equals(ItemGroupIDs.SUBTAB_PURE_RESOURCES)) {
			event.getItemDisplayBuilder().accept(PURE_CERTUS_QUARTZ);
			event.getItemDisplayBuilder().accept(SMALL_CERTUS_QUARTZ_BUD);
			event.getItemDisplayBuilder().accept(LARGE_CERTUS_QUARTZ_BUD);
			event.getItemDisplayBuilder().accept(CERTUS_QUARTZ_CLUSTER);
			event.getItemDisplayBuilder().accept(PURE_CERTUS_QUARTZ_BLOCK);
			
			event.getItemDisplayBuilder().accept(PURE_FLUIX);
			event.getItemDisplayBuilder().accept(SMALL_FLUIX_BUD);
			event.getItemDisplayBuilder().accept(LARGE_FLUIX_BUD);
			event.getItemDisplayBuilder().accept(FLUIX_CLUSTER);
			event.getItemDisplayBuilder().accept(PURE_FLUIX_BLOCK);
		}
	}
	
}
