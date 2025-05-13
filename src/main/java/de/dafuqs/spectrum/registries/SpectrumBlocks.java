package de.dafuqs.spectrum.registries;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;
import static de.dafuqs.spectrum.registries.SpectrumItems.*;
import static net.minecraft.block.Blocks.*;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.amphora.*;
import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.cinderhearth.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.conditional.amaranth.*;
import de.dafuqs.spectrum.blocks.conditional.blood_orchid.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.conditional.resonant_lily.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.deeper_down.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.blocks.deeper_down.groundcover.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.blocks.ender.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.gemstone.*;
import de.dafuqs.spectrum.blocks.geology.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.blocks.lava_sponge.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.blocks.rock_candy.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.blocks.spirit_sallow.*;
import de.dafuqs.spectrum.blocks.statues.*;
import de.dafuqs.spectrum.blocks.structure.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.blocks.weathering.*;
import de.dafuqs.spectrum.data.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.type.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.*;
import net.minecraft.block.enums.*;
import net.minecraft.block.piston.*;
import net.minecraft.client.render.*;
import net.minecraft.data.client.*;
import net.minecraft.data.family.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.*;

@SuppressWarnings({"unused"})
public class SpectrumBlocks {
	
	private static Settings settings(MapColor mapColor, BlockSoundGroup blockSoundGroup, float strength) {
		return AbstractBlock.Settings.create().mapColor(mapColor).sounds(blockSoundGroup).strength(strength);
	}
	
	private static Settings settings(MapColor mapColor, BlockSoundGroup blockSoundGroup, float strength, float resistance) {
		return settings(mapColor, blockSoundGroup, strength).resistance(resistance);
	}
	
	private static Settings craftingBlock(MapColor mapColor, BlockSoundGroup blockSoundGroup) {
		return settings(mapColor, blockSoundGroup, 5.0F, 8.0F).solidBlock(SpectrumBlocks::never).blockVision(SpectrumBlocks::never).nonOpaque().requiresTool();
	}
	
	public static final DeferredRegistrar COMMON_REGISTRAR = new DeferredRegistrar();
	public static final DeferredRegistrar CLIENT_REGISTRAR = new DeferredRegistrar();
	
	public static final Block PEDESTAL_BASIC_TOPAZ = register(pedestal(blockWithItem("pedestal_basic_topaz", new PedestalBlock(craftingBlock(MapColor.DIAMOND_BLUE, SpectrumBlockSoundGroups.TOPAZ_BLOCK), BuiltinPedestalVariant.BASIC_TOPAZ), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.BASIC_TOPAZ, "item.spectrum.pedestal.tooltip.basic_topaz"), InkColors.WHITE)));
	public static final Block PEDESTAL_BASIC_AMETHYST = register(pedestal(blockWithItem("pedestal_basic_amethyst", new PedestalBlock(craftingBlock(MapColor.PURPLE, BlockSoundGroup.AMETHYST_BLOCK), BuiltinPedestalVariant.BASIC_AMETHYST), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.BASIC_AMETHYST, "item.spectrum.pedestal.tooltip.basic_amethyst"), InkColors.WHITE)));
	public static final Block PEDESTAL_BASIC_CITRINE = register(pedestal(blockWithItem("pedestal_basic_citrine", new PedestalBlock(craftingBlock(MapColor.YELLOW, SpectrumBlockSoundGroups.CITRINE_BLOCK), BuiltinPedestalVariant.BASIC_CITRINE), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.BASIC_CITRINE, "item.spectrum.pedestal.tooltip.basic_citrine"), InkColors.WHITE)));
	public static final Block PEDESTAL_ALL_BASIC = register(pedestal(blockWithItem("pedestal_all_basic", new PedestalBlock(craftingBlock(MapColor.PURPLE, BlockSoundGroup.AMETHYST_BLOCK), BuiltinPedestalVariant.CMY), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.CMY, "item.spectrum.pedestal.tooltip.all_basic"), InkColors.WHITE)));
	public static final Block PEDESTAL_ONYX = register(pedestal(blockWithItem("pedestal_onyx", new PedestalBlock(craftingBlock(MapColor.BLACK, SpectrumBlockSoundGroups.ONYX_BLOCK), BuiltinPedestalVariant.ONYX), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.ONYX, "item.spectrum.pedestal.tooltip.onyx"), InkColors.WHITE)));
	public static final Block PEDESTAL_MOONSTONE = register(pedestal(blockWithItem("pedestal_moonstone", new PedestalBlock(craftingBlock(MapColor.WHITE, SpectrumBlockSoundGroups.MOONSTONE_BLOCK), BuiltinPedestalVariant.MOONSTONE), block -> new PedestalBlockItem(block, IS.of(1), BuiltinPedestalVariant.MOONSTONE, "item.spectrum.pedestal.tooltip.moonstone"), InkColors.WHITE)));
	
	public static final Block FUSION_SHRINE_BASALT = register(cutout(singleton(blockWithItem("fusion_shrine_basalt", new FusionShrineBlock(craftingBlock(MapColor.BLACK, BlockSoundGroup.BASALT).luminance(value -> value.get(FusionShrineBlock.LIGHT_LEVEL))), IS.of(1), InkColors.GRAY), SpectrumTexturedModels.FUSION_SHRINE)));
	public static final Block FUSION_SHRINE_CALCITE = register(cutout(singleton(blockWithItem("fusion_shrine_calcite", new FusionShrineBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE).luminance(value -> value.get(FusionShrineBlock.LIGHT_LEVEL))), IS.of(1), InkColors.GRAY), SpectrumTexturedModels.FUSION_SHRINE)));
	
	public static final Block ENCHANTER = register(cutout(singleton(blockWithItem("enchanter", new EnchanterBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(1), InkColors.PURPLE), ModelIds::getBlockModelId)));
	public static final Block ITEM_BOWL_BASALT = register(cutout(singleton(blockWithItem("item_bowl_basalt", new ItemBowlBlock(craftingBlock(MapColor.BLACK, BlockSoundGroup.BASALT)), IS.of(16), InkColors.PINK), SpectrumTexturedModels.BOWL)));
	public static final Block ITEM_BOWL_CALCITE = register(cutout(singleton(blockWithItem("item_bowl_calcite", new ItemBowlBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(16), InkColors.PINK), SpectrumTexturedModels.BOWL)));
	public static final Block ITEM_ROUNDEL = register(singleton(blockWithItem("item_roundel", new ItemRoundelBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(16), InkColors.PINK), SpectrumTexturedModels.ROUNDEL));
	public static final Block POTION_WORKSHOP = register(translucent(defaultNorthHorizontalFacing(blockWithItem("potion_workshop", new PotionWorkshopBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(1), InkColors.PURPLE), ModelIds::getBlockModelId)));
	public static final Block SPIRIT_INSTILLER = register(singleton(cutout(blockWithItem("spirit_instiller", new SpiritInstillerBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(1), InkColors.WHITE)), ModelIds::getBlockModelId).withPredefinedItemModel());
	public static final CrystallarieumBlock CRYSTALLARIEUM = register(cutout(singleton(blockWithItem("crystallarieum", new CrystallarieumBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(1), InkColors.BROWN), ModelIds::getBlockModelId)).withPredefinedItemModel());
	public static final Block CINDERHEARTH = register(defaultNorthHorizontalFacing(blockWithItem("cinderhearth", new CinderhearthBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(1).fireproof(), InkColors.ORANGE), ModelIds::getBlockModelId));
	
	public static final Block COLOR_PICKER = register(cutout(defaultWestHorizontalFacing(blockWithItem("color_picker", new ColorPickerBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(8), InkColors.GREEN), ModelIds::getBlockModelId)));
	public static final Block CRYSTAL_APOTHECARY = register(singleton(blockWithItem("crystal_apothecary", new CrystalApothecaryBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE)), IS.of(8), InkColors.GREEN), ModelIds::getBlockModelId));
	
	private static Settings gemstone(MapColor mapColor, BlockSoundGroup blockSoundGroup, int luminance) {
		return settings(mapColor, blockSoundGroup, 1.5F).solid().nonOpaque().luminance((state) -> luminance).pistonBehavior(PistonBehavior.DESTROY);
	}
	
	private static Settings gemstoneBlock(MapColor mapColor, BlockSoundGroup blockSoundGroup) {
		return settings(mapColor, blockSoundGroup, 1.5F).requiresTool();
	}
	
	public static <T extends SpectrumClusterBlock> BlockRegistrar<T> cluster(BlockRegistrar<T> registrar, Model model) {
		return cutout(registrar).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(TexturedModel.makeFactory(TextureMap::cross, model).upload(block, ctx.modelCollector))).coordinate(SpectrumModelHelper.createUpDefaultFacingVariantMap())).withBlockItemModel((ctx, block) -> {
			switch (block.getGrowthStage()) {
				case SpectrumClusterBlock.GrowthStage.SMALL -> SpectrumModels.SMALL_BUD_ITEM.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(block), ctx.writer);
				case SpectrumClusterBlock.GrowthStage.MEDIUM -> SpectrumModels.MEDIUM_BUD_ITEM.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(block), ctx.writer);
				case SpectrumClusterBlock.GrowthStage.LARGE -> SpectrumModels.LARGE_BUD_ITEM.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(block), ctx.writer);
				case SpectrumClusterBlock.GrowthStage.CLUSTER -> SpectrumModels.CLUSTER_ITEM.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(block), ctx.writer);
			}
		});
	}
	
	public static final SpectrumClusterBlock TOPAZ_CLUSTER = register(cluster(blockWithItem("topaz_cluster", new SpectrumClusterBlock(gemstone(MapColor.CYAN, SpectrumBlockSoundGroups.TOPAZ_CLUSTER, 8), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), Models.CROSS));
	public static final SpectrumClusterBlock LARGE_TOPAZ_BUD = register(cluster(blockWithItem("large_topaz_bud", new SpectrumClusterBlock(gemstone(MapColor.CYAN, SpectrumBlockSoundGroups.LARGE_TOPAZ_BUD, 6), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), Models.CROSS));
	public static final SpectrumClusterBlock MEDIUM_TOPAZ_BUD = register(cluster(blockWithItem("medium_topaz_bud", new SpectrumClusterBlock(gemstone(MapColor.CYAN, SpectrumBlockSoundGroups.MEDIUM_TOPAZ_BUD, 4), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.CYAN), Models.CROSS));
	public static final SpectrumClusterBlock SMALL_TOPAZ_BUD = register(cluster(blockWithItem("small_topaz_bud", new SpectrumClusterBlock(gemstone(MapColor.CYAN, SpectrumBlockSoundGroups.SMALL_TOPAZ_BUD, 2), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), Models.CROSS));
	public static final SpectrumBuddingBlock BUDDING_TOPAZ = register(simple(blockWithItem("budding_topaz", new SpectrumBuddingBlock(gemstoneBlock(MapColor.CYAN, SpectrumBlockSoundGroups.TOPAZ_BLOCK).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly(), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME), InkColors.CYAN)));
	public static final SpectrumGemstoneBlock TOPAZ_BLOCK = register(blockWithItem("topaz_block", new SpectrumGemstoneBlock(gemstoneBlock(MapColor.CYAN, SpectrumBlockSoundGroups.TOPAZ_BLOCK), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME), InkColors.CYAN).withBlockModel(SpectrumModelHelper::simpleMirroredBlockModel));
	
	public static final SpectrumClusterBlock CITRINE_CLUSTER = register(cluster(blockWithItem("citrine_cluster", new SpectrumClusterBlock(gemstone(MapColor.YELLOW, SpectrumBlockSoundGroups.CITRINE_CLUSTER, 9), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), Models.CROSS));
	public static final SpectrumClusterBlock LARGE_CITRINE_BUD = register(cluster(blockWithItem("large_citrine_bud", new SpectrumClusterBlock(gemstone(MapColor.YELLOW, SpectrumBlockSoundGroups.LARGE_CITRINE_BUD, 7), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), Models.CROSS));
	public static final SpectrumClusterBlock MEDIUM_CITRINE_BUD = register(cluster(blockWithItem("medium_citrine_bud", new SpectrumClusterBlock(gemstone(MapColor.YELLOW, SpectrumBlockSoundGroups.MEDIUM_CITRINE_BUD, 5), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.YELLOW), Models.CROSS));
	public static final SpectrumClusterBlock SMALL_CITRINE_BUD = register(cluster(blockWithItem("small_citrine_bud", new SpectrumClusterBlock(gemstone(MapColor.YELLOW, SpectrumBlockSoundGroups.SMALL_CITRINE_BUD, 3), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), Models.CROSS));
	public static final SpectrumBuddingBlock BUDDING_CITRINE = register(simple(blockWithItem("budding_citrine", new SpectrumBuddingBlock(gemstoneBlock(MapColor.YELLOW, SpectrumBlockSoundGroups.CITRINE_BLOCK).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly(), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME), InkColors.YELLOW)));
	public static final SpectrumGemstoneBlock CITRINE_BLOCK = register(blockWithItem("citrine_block", new SpectrumGemstoneBlock(gemstoneBlock(MapColor.YELLOW, SpectrumBlockSoundGroups.CITRINE_BLOCK), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME), InkColors.YELLOW).withBlockModel(SpectrumModelHelper::simpleMirroredBlockModel));
	
	public static final SpectrumClusterBlock ONYX_CLUSTER = register(cluster(blockWithItem("onyx_cluster", new SpectrumClusterBlock(gemstone(MapColor.BLACK, SpectrumBlockSoundGroups.ONYX_CLUSTER, 6), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BLACK), Models.CROSS));
	public static final SpectrumClusterBlock LARGE_ONYX_BUD = register(cluster(blockWithItem("large_onyx_bud", new SpectrumClusterBlock(gemstone(MapColor.BLACK, SpectrumBlockSoundGroups.LARGE_ONYX_BUD, 5), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BLACK), Models.CROSS));
	public static final SpectrumClusterBlock MEDIUM_ONYX_BUD = register(cluster(blockWithItem("medium_onyx_bud", new SpectrumClusterBlock(gemstone(MapColor.BLACK, SpectrumBlockSoundGroups.MEDIUM_ONYX_BUD, 3), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.BLACK), Models.CROSS));
	public static final SpectrumClusterBlock SMALL_ONYX_BUD = register(cluster(blockWithItem("small_onyx_bud", new SpectrumClusterBlock(gemstone(MapColor.BLACK, SpectrumBlockSoundGroups.SMALL_ONYX_BUD, 1), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BLACK), Models.CROSS));
	public static final SpectrumBuddingBlock BUDDING_ONYX = register(simple(blockWithItem("budding_onyx", new SpectrumBuddingBlock(gemstoneBlock(MapColor.BLACK, SpectrumBlockSoundGroups.ONYX_BLOCK).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly(), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME), InkColors.BLACK)));
	public static final SpectrumGemstoneBlock ONYX_BLOCK = register(blockWithItem("onyx_block", new SpectrumGemstoneBlock(gemstoneBlock(MapColor.BLACK, SpectrumBlockSoundGroups.ONYX_BLOCK), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME), InkColors.BLACK).withBlockModel(SpectrumModelHelper::simpleMirroredBlockModel));
	
	public static final SpectrumClusterBlock MOONSTONE_CLUSTER = register(cluster(blockWithItem("moonstone_cluster", new SpectrumClusterBlock(gemstone(MapColor.WHITE, SpectrumBlockSoundGroups.MOONSTONE_CLUSTER, 15), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.WHITE), Models.CROSS));
	public static final SpectrumClusterBlock LARGE_MOONSTONE_BUD = register(cluster(blockWithItem("large_moonstone_bud", new SpectrumClusterBlock(gemstone(MapColor.WHITE, SpectrumBlockSoundGroups.LARGE_MOONSTONE_BUD, 12), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.WHITE), Models.CROSS));
	public static final SpectrumClusterBlock MEDIUM_MOONSTONE_BUD = register(cluster(blockWithItem("medium_moonstone_bud", new SpectrumClusterBlock(gemstone(MapColor.WHITE, SpectrumBlockSoundGroups.MEDIUM_MOONSTONE_BUD, 9), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.WHITE), Models.CROSS));
	public static final SpectrumClusterBlock SMALL_MOONSTONE_BUD = register(cluster(blockWithItem("small_moonstone_bud", new SpectrumClusterBlock(gemstone(MapColor.WHITE, SpectrumBlockSoundGroups.SMALL_MOONSTONE_BUD, 6), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.WHITE), Models.CROSS));
	public static final SpectrumBuddingBlock BUDDING_MOONSTONE = register(simple(blockWithItem("budding_moonstone", new SpectrumBuddingBlock(gemstoneBlock(MapColor.WHITE, SpectrumBlockSoundGroups.MOONSTONE_BLOCK).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly(), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME), InkColors.WHITE)));
	public static final SpectrumGemstoneBlock MOONSTONE_BLOCK = register(blockWithItem("moonstone_block", new SpectrumGemstoneBlock(gemstoneBlock(MapColor.WHITE, SpectrumBlockSoundGroups.MOONSTONE_BLOCK), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME), InkColors.WHITE).withBlockModel(SpectrumModelHelper::simpleMirroredBlockModel));
	
	public static final ColoredFallingBlock TOPAZ_POWDER_BLOCK = register(simple(blockWithItem("topaz_powder_block", new ColoredFallingBlock(new ColorCode(DyeColor.CYAN.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.CYAN)), InkColors.CYAN)));
	public static final ColoredFallingBlock AMETHYST_POWDER_BLOCK = register(simple(blockWithItem("amethyst_powder_block", new ColoredFallingBlock(new ColorCode(DyeColor.MAGENTA.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.MAGENTA)), InkColors.MAGENTA)));
	public static final ColoredFallingBlock CITRINE_POWDER_BLOCK = register(simple(blockWithItem("citrine_powder_block", new ColoredFallingBlock(new ColorCode(DyeColor.YELLOW.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.YELLOW)), InkColors.YELLOW)));
	public static final ColoredFallingBlock ONYX_POWDER_BLOCK = register(simple(blockWithItem("onyx_powder_block", new ColoredFallingBlock(new ColorCode(DyeColor.BLACK.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.BLACK)), InkColors.BLACK)));
	public static final ColoredFallingBlock MOONSTONE_POWDER_BLOCK = register(simple(blockWithItem("moonstone_powder_block", new ColoredFallingBlock(new ColorCode(DyeColor.WHITE.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.WHITE)), InkColors.WHITE)));
	
	public static final Block VEGETAL_BLOCK = register(translucent(singleton(burnable(blockWithItem("vegetal_block", new Block(settings(MapColor.PALE_GREEN, BlockSoundGroup.FUNGUS, 2.0F).nonOpaque()), InkColors.GREEN), 8000), TexturedModel.makeFactory(TextureMap::texture, SpectrumModels.TRANSLUCENT_OUTER1))));
	public static final Block NEOLITH_BLOCK = register(blockWithItem("neolith_block", new SpectrumFacingBlock(settings(MapColor.PURPLE, BlockSoundGroup.COPPER, 6.0F).requiresTool().instrument(NoteBlockInstrument.BASEDRUM).luminance(state -> 13).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), InkColors.PINK).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(TexturedModel.CUBE_BOTTOM_TOP.upload(block, ctx.modelCollector))).coordinate(SpectrumModelHelper.createUpDefaultFacingVariantMap())));
	public static final Block BEDROCK_DUST_BLOCK = register(simple(blockWithItem("bedrock_dust_block", new BlockWithTooltip(settings(MapColor.STONE_GRAY, BlockSoundGroup.STONE, 100.0F, 3600.0F).pistonBehavior(PistonBehavior.BLOCK).requiresTool().instrument(NoteBlockInstrument.BASEDRUM), Text.translatable("spectrum.tooltip.dragon_and_wither_immune")), IS.of(Rarity.UNCOMMON), InkColors.BLACK)));
	
	public static final SpectrumClusterBlock BISMUTH_CLUSTER = register(cluster(blockWithItem("bismuth_cluster", new SpectrumClusterBlock(gemstone(MapColor.DARK_AQUA, BlockSoundGroup.CHAIN, 8), SpectrumClusterBlock.GrowthStage.CLUSTER), IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final SpectrumClusterBlock LARGE_BISMUTH_BUD = register(cluster(blockWithItem("large_bismuth_bud", new BismuthBudBlock(gemstone(MapColor.DARK_AQUA, BlockSoundGroup.CHAIN, 6).ticksRandomly(), SpectrumClusterBlock.GrowthStage.LARGE, BISMUTH_CLUSTER), IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final SpectrumClusterBlock SMALL_BISMUTH_BUD = register(cluster(blockWithItem("small_bismuth_bud", new BismuthBudBlock(gemstone(MapColor.DARK_AQUA, BlockSoundGroup.CHAIN, 4).ticksRandomly(), SpectrumClusterBlock.GrowthStage.SMALL, LARGE_BISMUTH_BUD), IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block BISMUTH_BLOCK = register(simple(blockWithItem("bismuth_block", new Block(gemstoneBlock(MapColor.DARK_AQUA, BlockSoundGroup.CHAIN)), InkColors.CYAN)));
	
	// DD BLOCKS
	private static final float BLACKSLAG_HARDNESS = 5.0F;
	private static final float BLACKSLAG_RESISTANCE = 7.0F;
	
	private static Settings blackslag(BlockSoundGroup blockSoundGroup) {
		return settings(MapColor.GRAY, blockSoundGroup, BLACKSLAG_HARDNESS, BLACKSLAG_RESISTANCE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool();
	}
	
	public static final Block BLACKSLAG = register(blockWithItem("blackslag", new BlackslagBlock(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK).withBlockModel((ctx, block) -> SpectrumModelHelper.createMirroredVariantsSupplier(block, TexturedModel.END_FOR_TOP_CUBE_COLUMN, SpectrumTexturedModels.CUBE_COLUMN_MIRRORED, ctx.modelCollector).coordinate(SpectrumModelHelper.createAxisRotatedVariantMap())));
	public static final Block BLACKSLAG_STAIRS = register(blockWithItem("blackslag_stairs", new StairsBlock(BLACKSLAG.getDefaultState(), blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final Block BLACKSLAG_SLAB = register(blockWithItem("blackslag_slab", new SlabBlock(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final Block BLACKSLAG_WALL = register(blockWithItem("blackslag_wall", new WallBlock(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final BlockFamily BLACKSLAG_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(BLACKSLAG).stairs(BLACKSLAG_STAIRS).slab(BLACKSLAG_SLAB).wall(BLACKSLAG_WALL).build(), SpectrumTexturedModels.cubeBottomTopWall(b -> b, "", b -> b, "_top", b -> b, "_top", b -> b, ""));
	
	public static final Block INFESTED_BLACKSLAG = register(parented(blockWithItem("infested_blackslag", new InfestedBlock(BLACKSLAG, blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK), b -> BLACKSLAG));
	
	public static final Block COBBLED_BLACKSLAG = register(blockWithItem("cobbled_blackslag", new Block(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final Block COBBLED_BLACKSLAG_STAIRS = register(blockWithItem("cobbled_blackslag_stairs", new StairsBlock(COBBLED_BLACKSLAG.getDefaultState(), blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final Block COBBLED_BLACKSLAG_SLAB = register(blockWithItem("cobbled_blackslag_slab", new SlabBlock(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final Block COBBLED_BLACKSLAG_WALL = register(blockWithItem("cobbled_blackslag_wall", new WallBlock(blackslag(BlockSoundGroup.DEEPSLATE)), InkColors.BLACK));
	public static final BlockFamily COBBLED_BLACKSLAG_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(COBBLED_BLACKSLAG).stairs(COBBLED_BLACKSLAG_STAIRS).slab(COBBLED_BLACKSLAG_SLAB).wall(COBBLED_BLACKSLAG_WALL).build());
	
	public static final Block BLACKSLAG_TILES = register(blockWithItem("blackslag_tiles", new Block(blackslag(BlockSoundGroup.DEEPSLATE_TILES)), InkColors.BLACK));
	public static final Block BLACKSLAG_TILE_STAIRS = register(blockWithItem("blackslag_tile_stairs", new StairsBlock(BLACKSLAG_TILES.getDefaultState(), Settings.copy(BLACKSLAG_TILES)), InkColors.BLACK));
	public static final Block BLACKSLAG_TILE_SLAB = register(blockWithItem("blackslag_tile_slab", new SlabBlock(Settings.copy(BLACKSLAG_TILES)), InkColors.BLACK));
	public static final Block BLACKSLAG_TILE_WALL = register(blockWithItem("blackslag_tile_wall", new WallBlock(Settings.copy(BLACKSLAG_TILES)), InkColors.BLACK));
	public static final Block CRACKED_BLACKSLAG_TILES = register(blockWithItem("cracked_blackslag_tiles", new Block(Settings.copy(BLACKSLAG_TILES)), InkColors.BLACK));
	public static final BlockFamily BLACKSLAG_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BLACKSLAG_TILES).stairs(BLACKSLAG_TILE_STAIRS).slab(BLACKSLAG_TILE_SLAB).wall(BLACKSLAG_TILE_WALL).cracked(CRACKED_BLACKSLAG_TILES).build());
	
	public static final Block BLACKSLAG_BRICKS = register(blockWithItem("blackslag_bricks", new Block(blackslag(BlockSoundGroup.DEEPSLATE_BRICKS)), InkColors.BLACK));
	public static final Block BLACKSLAG_BRICK_STAIRS = register(blockWithItem("blackslag_brick_stairs", new StairsBlock(BLACKSLAG_BRICKS.getDefaultState(), Settings.copy(BLACKSLAG_BRICKS)), InkColors.BLACK));
	public static final Block BLACKSLAG_BRICK_SLAB = register(blockWithItem("blackslag_brick_slab", new SlabBlock(Settings.copy(BLACKSLAG_BRICKS)), InkColors.BLACK));
	public static final Block BLACKSLAG_BRICK_WALL = register(blockWithItem("blackslag_brick_wall", new WallBlock(Settings.copy(BLACKSLAG_BRICKS)), InkColors.BLACK));
	public static final Block CRACKED_BLACKSLAG_BRICKS = register(blockWithItem("cracked_blackslag_bricks", new Block(Settings.copy(BLACKSLAG_BRICKS)), InkColors.BLACK));
	public static final BlockFamily BLACKSLAG_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BLACKSLAG_BRICKS).stairs(BLACKSLAG_BRICK_STAIRS).slab(BLACKSLAG_BRICK_SLAB).wall(BLACKSLAG_BRICK_WALL).cracked(CRACKED_BLACKSLAG_BRICKS).build());
	
	public static final Block POLISHED_BLACKSLAG = register(blockWithItem("polished_blackslag", new Block(blackslag(BlockSoundGroup.POLISHED_DEEPSLATE)), InkColors.BLACK));
	public static final Block POLISHED_BLACKSLAG_STAIRS = register(blockWithItem("polished_blackslag_stairs", new StairsBlock(POLISHED_BLACKSLAG.getDefaultState(), Settings.copy(POLISHED_BLACKSLAG)), InkColors.BLACK));
	public static final Block POLISHED_BLACKSLAG_SLAB = register(blockWithItem("polished_blackslag_slab", new SlabBlock(Settings.copy(POLISHED_BLACKSLAG)), InkColors.BLACK));
	public static final Block POLISHED_BLACKSLAG_WALL = register(blockWithItem("polished_blackslag_wall", new WallBlock(Settings.copy(POLISHED_BLACKSLAG)), InkColors.BLACK));
	public static final Block POLISHED_BLACKSLAG_BUTTON = register(blockWithItem("polished_blackslag_button", new ButtonBlock(SpectrumBlockSetTypes.POLISHED_BLACKSLAG, 5, Settings.create().pistonBehavior(PistonBehavior.DESTROY).noCollision().strength(0.5F)), InkColors.BLACK));
	public static final Block POLISHED_BLACKSLAG_PRESSURE_PLATE = register(blockWithItem("polished_blackslag_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_BLACKSLAG, Settings.create().mapColor(MapColor.BLACK).requiresTool().noCollision().strength(0.5F)), InkColors.BLACK));
	public static final Block CHISELED_POLISHED_BLACKSLAG = register(blockWithItem("chiseled_polished_blackslag", new Block(blackslag(BlockSoundGroup.DEEPSLATE_BRICKS)), InkColors.BLACK));
	public static final BlockFamily POLISHED_BLACKSLAG_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(POLISHED_BLACKSLAG).stairs(POLISHED_BLACKSLAG_STAIRS).slab(POLISHED_BLACKSLAG_SLAB).wall(POLISHED_BLACKSLAG_WALL).button(POLISHED_BLACKSLAG_BUTTON).pressurePlate(POLISHED_BLACKSLAG_PRESSURE_PLATE).chiseled(CHISELED_POLISHED_BLACKSLAG).build());
	
	public static final Block POLISHED_BLACKSLAG_PILLAR = register(axisRotated(blockWithItem("polished_blackslag_pillar", new PillarBlock(Settings.copy(BLACKSLAG_BRICKS)), InkColors.BLACK), TexturedModel.makeFactory(b -> SpectrumTextureMaps.sideEnd(b, "", CHISELED_POLISHED_BLACKSLAG, ""), Models.CUBE_COLUMN)));
	public static final Block ANCIENT_CHISELED_POLISHED_BLACKSLAG = register(simple(blockWithItem("ancient_chiseled_polished_blackslag", new Block(blackslag(BlockSoundGroup.DEEPSLATE_BRICKS)), InkColors.BLACK)));
	
	public static final Block SHALE_CLAY = register(singleton(blockWithItem("shale_clay", new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, blackslag(BlockSoundGroup.MUD_BRICKS)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block TILLED_SHALE_CLAY = register(singleton(blockWithItem("tilled_shale_clay", new TilledShaleClayBlock(Settings.copy(SHALE_CLAY), SHALE_CLAY.getDefaultState()), InkColors.BROWN), SpectrumTexturedModels.farmland(b -> SHALE_CLAY, "_side", b -> b, "")));
	
	public static final Block POLISHED_SHALE_CLAY = register(blockWithItem("polished_shale_clay", new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("polished_shale_clay_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, POLISHED_SHALE_CLAY.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("polished_shale_clay_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily POLISHED_SHALE_CLAY_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(POLISHED_SHALE_CLAY).stairs(POLISHED_SHALE_CLAY_STAIRS).slab(POLISHED_SHALE_CLAY_SLAB).build());
	
	public static final Block EXPOSED_POLISHED_SHALE_CLAY = register(blockWithItem("exposed_polished_shale_clay", new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("exposed_polished_shale_clay_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_POLISHED_SHALE_CLAY.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("exposed_polished_shale_clay_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily EXPOSED_POLISHED_SHALE_CLAY_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(EXPOSED_POLISHED_SHALE_CLAY).stairs(EXPOSED_POLISHED_SHALE_CLAY_STAIRS).slab(EXPOSED_POLISHED_SHALE_CLAY_SLAB).build());
	
	public static final Block WEATHERED_POLISHED_SHALE_CLAY = register(blockWithItem("weathered_polished_shale_clay", new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("weathered_polished_shale_clay_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_POLISHED_SHALE_CLAY.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("weathered_polished_shale_clay_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily WEATHERED_POLISHED_SHALE_CLAY_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(WEATHERED_POLISHED_SHALE_CLAY).stairs(WEATHERED_POLISHED_SHALE_CLAY_STAIRS).slab(WEATHERED_POLISHED_SHALE_CLAY_SLAB).build());
	
	public static final Block SHALE_CLAY_BRICKS = register(blockWithItem("shale_clay_bricks", new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("shale_clay_brick_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_BRICKS.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block SHALE_CLAY_BRICK_SLAB = register(blockWithItem("shale_clay_brick_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily SHALE_CLAY_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(SHALE_CLAY_BRICKS).stairs(SHALE_CLAY_BRICK_STAIRS).slab(SHALE_CLAY_BRICK_SLAB).build());
	
	public static final Block EXPOSED_SHALE_CLAY_BRICKS = register(blockWithItem("exposed_shale_clay_bricks", new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("exposed_shale_clay_brick_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_BRICKS.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_SHALE_CLAY_BRICK_SLAB = register(blockWithItem("exposed_shale_clay_brick_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily EXPOSED_SHALE_CLAY_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(EXPOSED_SHALE_CLAY_BRICKS).stairs(EXPOSED_SHALE_CLAY_BRICK_STAIRS).slab(EXPOSED_SHALE_CLAY_BRICK_SLAB).build());
	
	public static final Block WEATHERED_SHALE_CLAY_BRICKS = register(blockWithItem("weathered_shale_clay_bricks", new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("weathered_shale_clay_brick_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_BRICKS.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_SHALE_CLAY_BRICK_SLAB = register(blockWithItem("weathered_shale_clay_brick_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily WEATHERED_SHALE_CLAY_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(WEATHERED_SHALE_CLAY_BRICKS).stairs(WEATHERED_SHALE_CLAY_BRICK_STAIRS).slab(WEATHERED_SHALE_CLAY_BRICK_SLAB).build());
	
	public static final Block SHALE_CLAY_TILES = register(blockWithItem("shale_clay_tiles", new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block SHALE_CLAY_TILE_STAIRS = register(blockWithItem("shale_clay_tile_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_TILES.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block SHALE_CLAY_TILE_SLAB = register(blockWithItem("shale_clay_tile_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily SHALE_CLAY_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(SHALE_CLAY_TILES).stairs(SHALE_CLAY_TILE_STAIRS).slab(SHALE_CLAY_TILE_SLAB).build());
	
	public static final Block EXPOSED_SHALE_CLAY_TILES = register(blockWithItem("exposed_shale_clay_tiles", new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_SHALE_CLAY_TILE_STAIRS = register(blockWithItem("exposed_shale_clay_tile_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_TILES.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block EXPOSED_SHALE_CLAY_TILE_SLAB = register(blockWithItem("exposed_shale_clay_tile_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily EXPOSED_SHALE_CLAY_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(EXPOSED_SHALE_CLAY_TILES).stairs(EXPOSED_SHALE_CLAY_TILE_STAIRS).slab(EXPOSED_SHALE_CLAY_TILE_SLAB).build());
	
	public static final Block WEATHERED_SHALE_CLAY_TILES = register(blockWithItem("weathered_shale_clay_tiles", new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_SHALE_CLAY_TILE_STAIRS = register(blockWithItem("weathered_shale_clay_tile_stairs", new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_TILES.getDefaultState(), Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final Block WEATHERED_SHALE_CLAY_TILE_SLAB = register(blockWithItem("weathered_shale_clay_tile_slab", new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, Settings.copy(SHALE_CLAY)), InkColors.BROWN));
	public static final BlockFamily WEATHERED_SHALE_CLAY_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(WEATHERED_SHALE_CLAY_TILES).stairs(WEATHERED_SHALE_CLAY_TILE_STAIRS).slab(WEATHERED_SHALE_CLAY_TILE_SLAB).build());
	
	public static final Block ROCK_CRYSTAL = register(simple(blockWithItem("rock_crystal", new Block(settings(MapColor.OFF_WHITE, BlockSoundGroup.NETHER_BRICKS, 200F).requiresTool()), InkColors.BROWN)));
	
	public static final Block PYRITE = register(axisRotated(blockWithItem("pyrite", new PillarBlock(settings(MapColor.TERRACOTTA_YELLOW, BlockSoundGroup.CHAIN, 50.0F).requiresTool()), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block PYRITE_SLAB = register(blockWithItem("pyrite_slab", new SlabBlock(Settings.copy(PYRITE)), InkColors.BROWN));
	public static final Block PYRITE_STAIRS = register(blockWithItem("pyrite_stairs", new StairsBlock(PYRITE.getDefaultState(), Settings.copy(PYRITE)), InkColors.BROWN));
	public static final Block PYRITE_WALL = register(blockWithItem("pyrite_wall", new WallBlock(Settings.copy(PYRITE)), InkColors.BROWN));
	public static final BlockFamily PYRITE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(PYRITE).stairs(PYRITE_STAIRS).slab(PYRITE_SLAB).wall(PYRITE_WALL).build(), TexturedModel.makeFactory(b -> SpectrumTextureMaps.sideTopBottomWall(b, "_side", b, "_top", b, "_top", b, "_side"), Models.CUBE_ALL));
	
	public static final Block PYRITE_PILE = register(axisRotated(blockWithItem("pyrite_pile", new PillarBlock(Settings.copy(PYRITE)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block PYRITE_PLATING = register(simple(blockWithItem("pyrite_plating", new Block(Settings.copy(PYRITE)), InkColors.BROWN)));
	public static final Block PYRITE_TUBING = register(axisRotated(blockWithItem("pyrite_tubing", new PillarBlock(Settings.copy(PYRITE)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block PYRITE_RELIEF = register(axisRotated(blockWithItem("pyrite_relief", new PillarBlock(Settings.copy(PYRITE)), InkColors.BROWN), SpectrumTexturedModels.cubeColumn(b -> b, "_side", b -> PYRITE_TUBING, "_top")));
	public static final Block PYRITE_STACK = register(simple(blockWithItem("pyrite_stack", new Block(Settings.copy(PYRITE)), InkColors.BROWN)));
	public static final Block PYRITE_PANELING = register(singleton(blockWithItem("pyrite_paneling", new Block(Settings.copy(PYRITE)), InkColors.BROWN), SpectrumTexturedModels.cubeColumn(b -> b, "", b -> PYRITE_PLATING, "")));
	public static final Block PYRITE_VENT = register(singleton(blockWithItem("pyrite_vent", new Block(Settings.copy(PYRITE)), InkColors.BROWN), SpectrumTexturedModels.cubeColumn(b -> b, "", b -> PYRITE_PLATING, "")));
	public static final Block PYRITE_RIPPER = register(mippedCutout(blockWithItem("pyrite_ripper", new PyriteRipperBlock(Settings.copy(PYRITE).nonOpaque().allowsSpawning(SpectrumBlocks::never).blockVision(SpectrumBlocks::never)), InkColors.BROWN)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.FACING, PyriteRipperBlock.MIRRORED)
			.register(Direction.EAST, false, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))
			.register(Direction.NORTH, false, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R90))
			.register(Direction.SOUTH, false, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R270))
			.register(Direction.WEST, false, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
			.register(Direction.UP, false, SpectrumModelHelper.createModelVariant(block, ""))
			.register(Direction.DOWN, false, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R180))
			.register(Direction.EAST, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").put(VariantSettings.Y, VariantSettings.Rotation.R180))
			.register(Direction.NORTH, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").put(VariantSettings.Y, VariantSettings.Rotation.R90))
			.register(Direction.SOUTH, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").put(VariantSettings.Y, VariantSettings.Rotation.R270))
			.register(Direction.WEST, true, SpectrumModelHelper.createModelVariant(block, "_mirrored"))
			.register(Direction.UP, true, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.Y, VariantSettings.Rotation.R90))
			.register(Direction.DOWN, true, SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.X, VariantSettings.Rotation.R180).put(VariantSettings.Y, VariantSettings.Rotation.R90)))));
	public static final Block PYRITE_PROJECTOR = register(singleton(blockWithItem("pyrite_projector", new ProjectorBlock(Settings.copy(PYRITE), "pyrite_projector_projection", 16, 14, 1.375F, 1F, 16F), InkColors.BROWN), ModelIds::getBlockModelId));
	
	//TODO naming convention suggests that it should be 'pyrite_tile_slab', etc.
	public static final Block PYRITE_TILES = register(simple(blockWithItem("pyrite_tiles", new Block(Settings.copy(PYRITE)), InkColors.BROWN)));
	public static final Block PYRITE_TILES_SLAB = register(blockWithItem("pyrite_tiles_slab", new SlabBlock(Settings.copy(PYRITE_TILES)), InkColors.BROWN));
	public static final Block PYRITE_TILES_STAIRS = register(blockWithItem("pyrite_tiles_stairs", new StairsBlock(PYRITE_TILES.getDefaultState(), Settings.copy(PYRITE_TILES)), InkColors.BROWN));
	public static final Block PYRITE_TILES_WALL = register(blockWithItem("pyrite_tiles_wall", new WallBlock(Settings.copy(PYRITE_TILES)), InkColors.BROWN));
	public static final BlockFamily PYRITE_TILE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(PYRITE_TILES).stairs(PYRITE_TILES_STAIRS).slab(PYRITE_TILES_SLAB).wall(PYRITE_TILES_WALL).build(), TexturedModel.makeFactory(b -> TextureMap.all(PYRITE_PLATING), Models.CUBE_ALL));
	
	public static final Block DRAGONBONE = register(axisRotated(blockWithItem("dragonbone", new DragonboneBlock(Settings.copy(BONE_BLOCK).strength(-1.0F, 22.0F).pistonBehavior(PistonBehavior.BLOCK)), InkColors.GREEN), TexturedModel.CUBE_COLUMN));
	public static final Block CRACKED_DRAGONBONE = register(axisRotated(blockWithItem("cracked_dragonbone", new PillarBlock(Settings.copy(BONE_BLOCK).strength(100.0F, 1200.0F).pistonBehavior(PistonBehavior.BLOCK)), InkColors.GREEN), TexturedModel.CUBE_COLUMN));
	
	public static final Block POLISHED_BONE_ASH = register(blockWithItem("polished_bone_ash", new Block(AbstractBlock.Settings.copy(CRACKED_DRAGONBONE).hardness(1500.0F).mapColor(MapColor.WHITE)), InkColors.CYAN));
	public static final Block POLISHED_BONE_ASH_STAIRS = register(blockWithItem("polished_bone_ash_stairs", new StairsBlock(POLISHED_BONE_ASH.getDefaultState(), Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN));
	public static final Block POLISHED_BONE_ASH_SLAB = register(blockWithItem("polished_bone_ash_slab", new SlabBlock(Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN));
	public static final Block POLISHED_BONE_ASH_WALL = register(blockWithItem("polished_bone_ash_wall", new WallBlock(Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN));
	public static final BlockFamily POLISHED_BONE_ASH_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(POLISHED_BONE_ASH).stairs(POLISHED_BONE_ASH_STAIRS).slab(POLISHED_BONE_ASH_SLAB).wall(POLISHED_BONE_ASH_WALL).build());
	
	public static final Block POLISHED_BONE_ASH_PILLAR = register(axisRotated(blockWithItem("polished_bone_ash_pillar", new PillarBlock(AbstractBlock.Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN), TexturedModel.CUBE_COLUMN));
	public static final Block BONE_ASH_SHINGLES = register(blockWithItem("bone_ash_shingles", new ShinglesBlock(AbstractBlock.Settings.copy(POLISHED_BONE_ASH).nonOpaque()), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelIds.getBlockModelId(block)).coordinate(SpectrumModelHelper.createEastDefaultHorizontalFacingVariantMap())));
	
	public static final Block BONE_ASH_BRICKS = register(blockWithItem("bone_ash_bricks", new Block(AbstractBlock.Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN));
	public static final Block BONE_ASH_BRICK_STAIRS = register(blockWithItem("bone_ash_brick_stairs", new StairsBlock(BONE_ASH_BRICKS.getDefaultState(), Settings.copy(BONE_ASH_BRICKS)), InkColors.CYAN));
	public static final Block BONE_ASH_BRICK_SLAB = register(blockWithItem("bone_ash_brick_slab", new SlabBlock(Settings.copy(BONE_ASH_BRICKS)), InkColors.CYAN));
	public static final Block BONE_ASH_BRICK_WALL = register(blockWithItem("bone_ash_brick_wall", new WallBlock(Settings.copy(BONE_ASH_BRICKS)), InkColors.CYAN));
	public static final BlockFamily BONE_ASH_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BONE_ASH_BRICKS).stairs(BONE_ASH_BRICK_STAIRS).slab(BONE_ASH_BRICK_SLAB).wall(BONE_ASH_BRICK_WALL).build());
	
	public static final Block BONE_ASH_TILES = register(blockWithItem("bone_ash_tiles", new Block(AbstractBlock.Settings.copy(POLISHED_BONE_ASH)), InkColors.CYAN));
	public static final Block BONE_ASH_TILE_STAIRS = register(blockWithItem("bone_ash_tile_stairs", new StairsBlock(BONE_ASH_TILES.getDefaultState(), Settings.copy(BONE_ASH_TILES)), InkColors.CYAN));
	public static final Block BONE_ASH_TILE_SLAB = register(blockWithItem("bone_ash_tile_slab", new SlabBlock(Settings.copy(BONE_ASH_TILES)), InkColors.CYAN));
	public static final Block BONE_ASH_TILE_WALL = register(blockWithItem("bone_ash_tile_wall", new WallBlock(Settings.copy(BONE_ASH_TILES)), InkColors.CYAN));
	public static final BlockFamily BONE_ASH_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BONE_ASH_TILES).stairs(BONE_ASH_TILE_STAIRS).slab(BONE_ASH_TILE_SLAB).wall(BONE_ASH_TILE_WALL).build());
	
	public static final Block SLUSH = register(simple(blockWithItem("slush", new PillarBlock(blackslag(BlockSoundGroup.MUDDY_MANGROVE_ROOTS)), InkColors.BROWN)));
	public static final Block OVERGROWN_SLUSH = register(snowy(blockWithItem("overgrown_slush", new SlushVegetationBlock(blackslag(BlockSoundGroup.MUDDY_MANGROVE_ROOTS)), InkColors.BROWN), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> SLUSH, "", b -> b, "_top"), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> SLUSH, "", b -> b, "_snow_top")));
	public static final Block TILLED_SLUSH = register(singleton(blockWithItem("tilled_slush", new TilledSlushBlock(Settings.copy(SLUSH), SLUSH.getDefaultState()), InkColors.BROWN), SpectrumTexturedModels.farmland(b -> SLUSH, "", b -> b, "")));
	
	public static final Block BLACK_MATERIA = register(simple(blockWithItem("black_materia", new BlackMateriaBlock(settings(MapColor.TERRACOTTA_BLACK, BlockSoundGroup.SAND, 0.0F).instrument(NoteBlockInstrument.SNARE).ticksRandomly()), InkColors.GRAY)));
	public static final Block BLACK_SLUDGE = register(simple(blockWithItem("black_sludge", new Block(settings(MapColor.TERRACOTTA_BLACK, BlockSoundGroup.SAND, 0.5F).instrument(NoteBlockInstrument.SNARE)), InkColors.GRAY)));
	public static final Block SAG_LEAF = register(cross(block("sag_leaf", new BlackSludgePlantBlock(AbstractBlock.Settings.copy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	public static final Block SAG_BUBBLE = register(cross(block("sag_bubble", new BlackSludgePlantBlock(AbstractBlock.Settings.copy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	public static final Block SMALL_SAG_BUBBLE = register(cross(block("small_sag_bubble", new BlackSludgePlantBlock(AbstractBlock.Settings.copy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	
	public static final PrimordialFireBlock PRIMORDIAL_FIRE = register(cutout(block("primordial_fire", new PrimordialFireBlock(Settings.copy(FIRE).mapColor(MapColor.PURPLE).luminance((state) -> 10)))).withBlockModel((ctx, block) -> {
		When noSides = When.create().set(PrimordialFireBlock.UP, false).set(PrimordialFireBlock.NORTH, false).set(PrimordialFireBlock.SOUTH, false).set(PrimordialFireBlock.WEST, false).set(PrimordialFireBlock.EAST, false);
		TextureMap fire0 = new TextureMap().put(TextureKey.FIRE, TextureMap.getSubId(block, "_0"));
		TextureMap fire1 = new TextureMap().put(TextureKey.FIRE, TextureMap.getSubId(block, "_1"));
		Identifier side0 = SpectrumModels.FIRE_SIDE.upload(block, "_side0", fire0, ctx.modelCollector);
		Identifier side1 = SpectrumModels.FIRE_SIDE.upload(block, "_side1", fire1, ctx.modelCollector);
		Identifier sideAlt0 = SpectrumModels.FIRE_SIDE_ALT.upload(block, "_side_alt0", fire0, ctx.modelCollector);
		Identifier sideAlt1 = SpectrumModels.FIRE_SIDE_ALT.upload(block, "_side_alt1", fire1, ctx.modelCollector);
		return MultipartBlockStateSupplier.create(block)
				.with(noSides, SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_FLOOR.upload(block, "_floor0", fire0, ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_FLOOR.upload(block, "_floor1", fire1, ctx.modelCollector)))
				.with(When.create().set(PrimordialFireBlock.UP, true), SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_UP.upload(block, "_up0", fire0, ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_UP.upload(block, "_up1", fire1, ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_UP_ALT.upload(block, "_up_alt0", fire0, ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumModels.FIRE_UP_ALT.upload(block, "_up_alt1", fire1, ctx.modelCollector)))
				.with(When.anyOf(noSides, When.create().set(PrimordialFireBlock.NORTH, true)), SpectrumModelHelper.createModelVariant(side0), SpectrumModelHelper.createModelVariant(side1), SpectrumModelHelper.createModelVariant(sideAlt0), SpectrumModelHelper.createModelVariant(sideAlt1))
				.with(When.anyOf(noSides, When.create().set(PrimordialFireBlock.SOUTH, true)), SpectrumModelHelper.createModelVariant(side0).put(VariantSettings.Y, VariantSettings.Rotation.R180), SpectrumModelHelper.createModelVariant(side1).put(VariantSettings.Y, VariantSettings.Rotation.R180), SpectrumModelHelper.createModelVariant(sideAlt0).put(VariantSettings.Y, VariantSettings.Rotation.R180), SpectrumModelHelper.createModelVariant(sideAlt1).put(VariantSettings.Y, VariantSettings.Rotation.R180))
				.with(When.anyOf(noSides, When.create().set(PrimordialFireBlock.WEST, true)), SpectrumModelHelper.createModelVariant(side0).put(VariantSettings.Y, VariantSettings.Rotation.R270), SpectrumModelHelper.createModelVariant(side1).put(VariantSettings.Y, VariantSettings.Rotation.R270), SpectrumModelHelper.createModelVariant(sideAlt0).put(VariantSettings.Y, VariantSettings.Rotation.R270), SpectrumModelHelper.createModelVariant(sideAlt1).put(VariantSettings.Y, VariantSettings.Rotation.R270))
				.with(When.anyOf(noSides, When.create().set(PrimordialFireBlock.EAST, true)), SpectrumModelHelper.createModelVariant(side0).put(VariantSettings.Y, VariantSettings.Rotation.R90), SpectrumModelHelper.createModelVariant(side1).put(VariantSettings.Y, VariantSettings.Rotation.R90), SpectrumModelHelper.createModelVariant(sideAlt0).put(VariantSettings.Y, VariantSettings.Rotation.R90), SpectrumModelHelper.createModelVariant(sideAlt1).put(VariantSettings.Y, VariantSettings.Rotation.R90));
	}));
	public static final Block PRIMORDIAL_WALL_TORCH = register(cutout(defaultEastHorizontalFacing(block("primordial_wall_torch", new WallTorchBlock(SpectrumParticleTypes.PRIMORDIAL_FLAME, Settings.copy(SOUL_WALL_TORCH).luminance(s -> 13))), ModelIds::getBlockModelId)));
	public static final Block PRIMORDIAL_TORCH = register(cutout(singleton(blockWithItem("primordial_torch", new TorchBlock(SpectrumParticleTypes.PRIMORDIAL_FLAME, Settings.copy(SOUL_TORCH).luminance(s -> 13)), block -> new VerticallyAttachableBlockItem(block, PRIMORDIAL_WALL_TORCH, IS.of(), Direction.DOWN), InkColors.ORANGE), ModelIds::getBlockModelId).withItemModel(SpectrumModelHelper::registerItemModel)));
	
	public static <T extends Block> BlockRegistrar<T> moonstoneChiseled(BlockRegistrar<T> registrar, Identifier capTexture) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_down")).withBlockModel((ctx, block) -> {
			TextureMap textureMap = SpectrumTextureMaps.sideLine(capTexture, TextureMap.getId(block));
			Identifier base = SpectrumModels.MOONSTONE_CHISELED.upload(block, textureMap, ctx.modelCollector);
			Identifier down = SpectrumModels.MOONSTONE_CHISELED_DOWN.upload(block, "_down", textureMap, ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createDownDefaultFacingVariantMap(ModelIds.getBlockModelId(block), ModelIds.getBlockSubModelId(block, "_down")));
		});
	}
	
	public static final Block SMOOTH_BASALT_STAIRS = register(blockWithItem("smooth_basalt_stairs", new StairsBlock(BASALT.getDefaultState(), AbstractBlock.Settings.copy(BASALT)), InkColors.BROWN));
	public static final Block SMOOTH_BASALT_SLAB = register(blockWithItem("smooth_basalt_slab", new SlabBlock(AbstractBlock.Settings.copy(BASALT)), InkColors.BROWN));
	public static final Block SMOOTH_BASALT_WALL = register(blockWithItem("smooth_basalt_wall", new WallBlock(AbstractBlock.Settings.copy(BASALT)), InkColors.BROWN));
	public static final BlockFamily SMOOTH_BASALT_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(SMOOTH_BASALT).stairs(SMOOTH_BASALT_STAIRS).slab(SMOOTH_BASALT_SLAB).wall(SMOOTH_BASALT_WALL).build(), TexturedModel.CUBE_ALL);
	
	public static final Block POLISHED_BASALT = register(blockWithItem("polished_basalt", new Block(settings(MapColor.BLACK, BlockSoundGroup.BASALT, 2.0F, 5.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresTool()), InkColors.BROWN));
	public static final Block POLISHED_BASALT_STAIRS = register(blockWithItem("polished_basalt_stairs", new StairsBlock(POLISHED_BASALT.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block POLISHED_BASALT_SLAB = register(blockWithItem("polished_basalt_slab", new SlabBlock(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block POLISHED_BASALT_WALL = register(blockWithItem("polished_basalt_wall", new WallBlock(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block POLISHED_BASALT_BUTTON = register(blockWithItem("polished_basalt_button", new ButtonBlock(SpectrumBlockSetTypes.POLISHED_BASALT, 5, Settings.create().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)), InkColors.BROWN));
	public static final Block POLISHED_BASALT_PRESSURE_PLATE = register(blockWithItem("polished_basalt_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_BASALT, Settings.create().mapColor(MapColor.BLACK).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)), InkColors.BROWN));
	public static final Block CHISELED_POLISHED_BASALT = register(blockWithItem("chiseled_polished_basalt", new Block(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final BlockFamily POLISHED_BASALT_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(POLISHED_BASALT).stairs(POLISHED_BASALT_STAIRS).slab(POLISHED_BASALT_SLAB).wall(POLISHED_BASALT_WALL).button(POLISHED_BASALT_BUTTON).pressurePlate(POLISHED_BASALT_PRESSURE_PLATE).chiseled(CHISELED_POLISHED_BASALT).build());
	
	public static final Block POLISHED_BASALT_PILLAR = register(axisRotated(blockWithItem("polished_basalt_pillar", new PillarBlock(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block POLISHED_BASALT_CREST = register(blockWithItem("polished_basalt_crest", new CardinalFacingBlock(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, TexturedModel.CUBE_COLUMN).coordinate(SpectrumModelHelper.createCardinalFacingVariantMap())));
	public static final Block NOTCHED_POLISHED_BASALT = register(singleton(blockWithItem("notched_polished_basalt", new Block(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	
	public static final Block BASALT_BRICKS = register(blockWithItem("basalt_bricks", new Block(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block BASALT_BRICK_STAIRS = register(blockWithItem("basalt_brick_stairs", new StairsBlock(BASALT_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(BASALT_BRICKS)), InkColors.BROWN));
	public static final Block BASALT_BRICK_SLAB = register(blockWithItem("basalt_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(BASALT_BRICKS)), InkColors.BROWN));
	public static final Block BASALT_BRICK_WALL = register(blockWithItem("basalt_brick_wall", new WallBlock(AbstractBlock.Settings.copy(BASALT_BRICKS)), InkColors.BROWN));
	public static final Block CRACKED_BASALT_BRICKS = register(blockWithItem("cracked_basalt_bricks", new Block(AbstractBlock.Settings.copy(BASALT_BRICKS)), InkColors.BROWN));
	public static final BlockFamily BASALT_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BASALT_BRICKS).stairs(BASALT_BRICK_STAIRS).slab(BASALT_BRICK_SLAB).wall(BASALT_BRICK_WALL).cracked(CRACKED_BASALT_BRICKS).build());
	
	public static final Block BASALT_TILES = register(blockWithItem("basalt_tiles", new Block(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block BASALT_TILE_STAIRS = register(blockWithItem("basalt_tile_stairs", new StairsBlock(BASALT_TILES.getDefaultState(), AbstractBlock.Settings.copy(BASALT_TILES)), InkColors.BROWN));
	public static final Block BASALT_TILE_SLAB = register(blockWithItem("basalt_tile_slab", new SlabBlock(AbstractBlock.Settings.copy(BASALT_TILES)), InkColors.BROWN));
	public static final Block BASALT_TILE_WALL = register(blockWithItem("basalt_tile_wall", new WallBlock(AbstractBlock.Settings.copy(BASALT_TILES)), InkColors.BROWN));
	public static final Block CRACKED_BASALT_TILES = register(blockWithItem("cracked_basalt_tiles", new Block(AbstractBlock.Settings.copy(BASALT_TILES)), InkColors.BROWN));
	public static final BlockFamily BASALT_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BASALT_TILES).stairs(BASALT_TILE_STAIRS).slab(BASALT_TILE_SLAB).wall(BASALT_TILE_WALL).cracked(CRACKED_BASALT_TILES).build());
	
	public static final Block PLANED_BASALT = register(blockWithItem("planed_basalt", new Block(AbstractBlock.Settings.copy(POLISHED_BASALT)), InkColors.BROWN));
	public static final Block PLANED_BASALT_STAIRS = register(blockWithItem("planed_basalt_stairs", new StairsBlock(PLANED_BASALT.getDefaultState(), AbstractBlock.Settings.copy(PLANED_BASALT)), InkColors.BROWN));
	public static final Block PLANED_BASALT_SLAB = register(blockWithItem("planed_basalt_slab", new SlabBlock(AbstractBlock.Settings.copy(PLANED_BASALT)), InkColors.BROWN));
	public static final Block PLANED_BASALT_WALL = register(blockWithItem("planed_basalt_wall", new WallBlock(AbstractBlock.Settings.copy(PLANED_BASALT)), InkColors.BROWN));
	public static final BlockFamily PLANED_BASALT_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(PLANED_BASALT).stairs(PLANED_BASALT_STAIRS).slab(PLANED_BASALT_SLAB).wall(PLANED_BASALT_WALL).build());
	
	public static final Block TOPAZ_CHISELED_BASALT = register(simple(blockWithItem("topaz_chiseled_basalt", new Block(AbstractBlock.Settings.copy(BASALT_BRICKS).luminance(s -> 6)), InkColors.CYAN)));
	public static final Block AMETHYST_CHISELED_BASALT = register(simple(blockWithItem("amethyst_chiseled_basalt", new Block(AbstractBlock.Settings.copy(BASALT_BRICKS).luminance(s -> 5)), InkColors.MAGENTA)));
	public static final Block CITRINE_CHISELED_BASALT = register(simple(blockWithItem("citrine_chiseled_basalt", new Block(AbstractBlock.Settings.copy(BASALT_BRICKS).luminance(s -> 7)), InkColors.YELLOW)));
	public static final Block ONYX_CHISELED_BASALT = register(simple(blockWithItem("onyx_chiseled_basalt", new Block(AbstractBlock.Settings.copy(BASALT_BRICKS).luminance(s -> 3)), InkColors.BLACK)));
	public static final Block MOONSTONE_CHISELED_BASALT = register(moonstoneChiseled(blockWithItem("moonstone_chiseled_basalt", new SpectrumLineFacingBlock(AbstractBlock.Settings.copy(BASALT_BRICKS).luminance(s -> 12)), InkColors.WHITE), SpectrumTextures.BASALT_CAP));
	
	public static final Block CALCITE_STAIRS = register(blockWithItem("calcite_stairs", new StairsBlock(CALCITE.getDefaultState(), AbstractBlock.Settings.copy(CALCITE)), InkColors.BROWN));
	public static final Block CALCITE_SLAB = register(blockWithItem("calcite_slab", new SlabBlock(AbstractBlock.Settings.copy(CALCITE)), InkColors.BROWN));
	public static final Block CALCITE_WALL = register(blockWithItem("calcite_wall", new WallBlock(AbstractBlock.Settings.copy(CALCITE)), InkColors.BROWN));
	public static final BlockFamily CALCITE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(CALCITE).stairs(CALCITE_STAIRS).slab(CALCITE_SLAB).wall(CALCITE_WALL).build(), TexturedModel.CUBE_ALL);
	
	public static final Block POLISHED_CALCITE = register(blockWithItem("polished_calcite", new Block(settings(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.CALCITE, 2.0F, 5.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresTool()), InkColors.BROWN));
	public static final Block POLISHED_CALCITE_STAIRS = register(blockWithItem("polished_calcite_stairs", new StairsBlock(POLISHED_CALCITE.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block POLISHED_CALCITE_SLAB = register(blockWithItem("polished_calcite_slab", new SlabBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block POLISHED_CALCITE_WALL = register(blockWithItem("polished_calcite_wall", new WallBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block POLISHED_CALCITE_BUTTON = register(blockWithItem("polished_calcite_button", new ButtonBlock(SpectrumBlockSetTypes.POLISHED_CALCITE, 5, Settings.create().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)), InkColors.BROWN));
	public static final Block POLISHED_CALCITE_PRESSURE_PLATE = register(blockWithItem("polished_calcite_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_CALCITE, Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)), InkColors.BROWN));
	public static final Block CHISELED_POLISHED_CALCITE = register(blockWithItem("chiseled_polished_calcite", new Block(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final BlockFamily POLISHED_CALCITE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(POLISHED_CALCITE).stairs(POLISHED_CALCITE_STAIRS).slab(POLISHED_CALCITE_SLAB).wall(POLISHED_CALCITE_WALL).button(POLISHED_CALCITE_BUTTON).pressurePlate(POLISHED_CALCITE_PRESSURE_PLATE).chiseled(CHISELED_POLISHED_CALCITE).build());
	
	public static final Block POLISHED_CALCITE_PILLAR = register(axisRotated(blockWithItem("polished_calcite_pillar", new PillarBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	public static final Block POLISHED_CALCITE_CREST = register(blockWithItem("polished_calcite_crest", new CardinalFacingBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, TexturedModel.CUBE_COLUMN).coordinate(SpectrumModelHelper.createCardinalFacingVariantMap())));
	public static final Block NOTCHED_POLISHED_CALCITE = register(singleton(blockWithItem("notched_polished_calcite", new Block(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	
	public static final Block CALCITE_BRICKS = register(blockWithItem("calcite_bricks", new Block(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block CALCITE_BRICK_STAIRS = register(blockWithItem("calcite_brick_stairs", new StairsBlock(CALCITE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(CALCITE_BRICKS)), InkColors.BROWN));
	public static final Block CALCITE_BRICK_SLAB = register(blockWithItem("calcite_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(CALCITE_BRICKS)), InkColors.BROWN));
	public static final Block CALCITE_BRICK_WALL = register(blockWithItem("calcite_brick_wall", new WallBlock(AbstractBlock.Settings.copy(CALCITE_BRICKS)), InkColors.BROWN));
	public static final Block CRACKED_CALCITE_BRICKS = register(blockWithItem("cracked_calcite_bricks", new Block(AbstractBlock.Settings.copy(CALCITE_BRICKS)), InkColors.BROWN));
	public static final BlockFamily CALCITE_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(CALCITE_BRICKS).stairs(CALCITE_BRICK_STAIRS).slab(CALCITE_BRICK_SLAB).wall(CALCITE_BRICK_WALL).cracked(CRACKED_CALCITE_BRICKS).build());
	
	public static final Block CALCITE_TILES = register(blockWithItem("calcite_tiles", new Block(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block CALCITE_TILE_STAIRS = register(blockWithItem("calcite_tile_stairs", new StairsBlock(CALCITE_TILES.getDefaultState(), AbstractBlock.Settings.copy(CALCITE_TILES)), InkColors.BROWN));
	public static final Block CALCITE_TILE_SLAB = register(blockWithItem("calcite_tile_slab", new SlabBlock(AbstractBlock.Settings.copy(CALCITE_TILES)), InkColors.BROWN));
	public static final Block CALCITE_TILE_WALL = register(blockWithItem("calcite_tile_wall", new WallBlock(AbstractBlock.Settings.copy(CALCITE_TILES)), InkColors.BROWN));
	public static final Block CRACKED_CALCITE_TILES = register(blockWithItem("cracked_calcite_tiles", new Block(AbstractBlock.Settings.copy(CALCITE_TILES)), InkColors.BROWN));
	public static final BlockFamily CALCITE_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(CALCITE_TILES).stairs(CALCITE_TILE_STAIRS).slab(CALCITE_TILE_SLAB).wall(CALCITE_TILE_WALL).cracked(CRACKED_CALCITE_TILES).build());
	
	public static final Block PLANED_CALCITE = register(blockWithItem("planed_calcite", new Block(AbstractBlock.Settings.copy(POLISHED_CALCITE)), InkColors.BROWN));
	public static final Block PLANED_CALCITE_STAIRS = register(blockWithItem("planed_calcite_stairs", new StairsBlock(PLANED_CALCITE.getDefaultState(), AbstractBlock.Settings.copy(PLANED_CALCITE)), InkColors.BROWN));
	public static final Block PLANED_CALCITE_SLAB = register(blockWithItem("planed_calcite_slab", new SlabBlock(AbstractBlock.Settings.copy(PLANED_CALCITE)), InkColors.BROWN));
	public static final Block PLANED_CALCITE_WALL = register(blockWithItem("planed_calcite_wall", new WallBlock(AbstractBlock.Settings.copy(PLANED_CALCITE)), InkColors.BROWN));
	public static final BlockFamily PLANED_CALCITE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(PLANED_CALCITE).stairs(PLANED_CALCITE_STAIRS).slab(PLANED_CALCITE_SLAB).wall(PLANED_CALCITE_WALL).build());
	
	public static final Block TOPAZ_CHISELED_CALCITE = register(simple(blockWithItem("topaz_chiseled_calcite", new Block(AbstractBlock.Settings.copy(CALCITE_BRICKS).luminance(s -> 6)), InkColors.CYAN)));
	public static final Block AMETHYST_CHISELED_CALCITE = register(simple(blockWithItem("amethyst_chiseled_calcite", new Block(AbstractBlock.Settings.copy(CALCITE_BRICKS).luminance(s -> 5)), InkColors.MAGENTA)));
	public static final Block CITRINE_CHISELED_CALCITE = register(simple(blockWithItem("citrine_chiseled_calcite", new Block(AbstractBlock.Settings.copy(CALCITE_BRICKS).luminance(s -> 7)), InkColors.YELLOW)));
	public static final Block ONYX_CHISELED_CALCITE = register(simple(blockWithItem("onyx_chiseled_calcite", new Block(AbstractBlock.Settings.copy(CALCITE_BRICKS).luminance(s -> 3)), InkColors.BLACK)));
	public static final Block MOONSTONE_CHISELED_CALCITE = register(moonstoneChiseled(blockWithItem("moonstone_chiseled_calcite", new SpectrumLineFacingBlock(AbstractBlock.Settings.copy(CALCITE_BRICKS).luminance(s -> 12)), InkColors.WHITE), SpectrumTextures.CALCITE_CAP));
	
	public static PillarBlock registerGemstoneLight(String name, Block gemBlock, Block baseBlock, Identifier capTexture, InkColor color) {
		return register(translucent(axisRotated(blockWithItem(name, new PillarBlock(AbstractBlock.Settings.copy(baseBlock).luminance(s -> 15).nonOpaque().solid()), color), TexturedModel.makeFactory(block -> SpectrumTextureMaps.sideTopInside(TextureMap.getId(block), capTexture, TextureMap.getId(gemBlock)), SpectrumModels.MULTILAYER_LIGHT))));
	}
	
	public static final Block TOPAZ_BASALT_LIGHT = registerGemstoneLight("topaz_basalt_light", TOPAZ_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.CYAN);
	public static final Block AMETHYST_BASALT_LIGHT = registerGemstoneLight("amethyst_basalt_light", AMETHYST_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.MAGENTA);
	public static final Block CITRINE_BASALT_LIGHT = registerGemstoneLight("citrine_basalt_light", CITRINE_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.YELLOW);
	public static final Block ONYX_BASALT_LIGHT = registerGemstoneLight("onyx_basalt_light", ONYX_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.BLACK);
	public static final Block MOONSTONE_BASALT_LIGHT = registerGemstoneLight("moonstone_basalt_light", MOONSTONE_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.WHITE);
	public static final Block TOPAZ_CALCITE_LIGHT = registerGemstoneLight("topaz_calcite_light", TOPAZ_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.CYAN);
	public static final Block AMETHYST_CALCITE_LIGHT = registerGemstoneLight("amethyst_calcite_light", AMETHYST_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.MAGENTA);
	public static final Block CITRINE_CALCITE_LIGHT = registerGemstoneLight("citrine_calcite_light", CITRINE_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.YELLOW);
	public static final Block ONYX_CALCITE_LIGHT = registerGemstoneLight("onyx_calcite_light", ONYX_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.BLACK);
	public static final Block MOONSTONE_CALCITE_LIGHT = registerGemstoneLight("moonstone_calcite_light", MOONSTONE_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.WHITE);
	
	// GLASS
	private static Settings gemstoneGlass(BlockSoundGroup soundGroup, MapColor mapColor) {
		return AbstractBlock.Settings.copy(GLASS).sounds(soundGroup).mapColor(mapColor);
	}
	
	public static <T extends Block> BlockRegistrar<T> glassPane(BlockRegistrar<T> registrar, Block glassBlock) {
		return translucent(registrar).withBlockModel((ctx, block) -> SpectrumModelHelper.glassPaneBlockModel(ctx, block, glassBlock));
	}
	
	public static final Block TOPAZ_GLASS = register(translucent(simple(blockWithItem("topaz_glass", new GemstoneGlassBlock(gemstoneGlass(SpectrumBlockSoundGroups.TOPAZ_CLUSTER, MapColor.CYAN), BuiltinGemstoneColor.CYAN), InkColors.CYAN))));
	public static final Block AMETHYST_GLASS = register(translucent(simple(blockWithItem("amethyst_glass", new GemstoneGlassBlock(gemstoneGlass(BlockSoundGroup.AMETHYST_CLUSTER, MapColor.MAGENTA), BuiltinGemstoneColor.MAGENTA), InkColors.MAGENTA))));
	public static final Block CITRINE_GLASS = register(translucent(simple(blockWithItem("citrine_glass", new GemstoneGlassBlock(gemstoneGlass(SpectrumBlockSoundGroups.CITRINE_CLUSTER, MapColor.YELLOW), BuiltinGemstoneColor.YELLOW), InkColors.YELLOW))));
	public static final Block ONYX_GLASS = register(translucent(simple(blockWithItem("onyx_glass", new GemstoneGlassBlock(gemstoneGlass(SpectrumBlockSoundGroups.ONYX_CLUSTER, MapColor.BLACK), BuiltinGemstoneColor.BLACK), InkColors.BLACK))));
	public static final Block MOONSTONE_GLASS = register(translucent(simple(blockWithItem("moonstone_glass", new GemstoneGlassBlock(gemstoneGlass(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER, MapColor.WHITE), BuiltinGemstoneColor.WHITE), InkColors.WHITE))));
	public static final Block RADIANT_GLASS = register(translucent(simple(blockWithItem("radiant_glass", new RadiantGlassBlock(gemstoneGlass(BlockSoundGroup.GLASS, MapColor.PALE_YELLOW).luminance(value -> 12)), InkColors.WHITE))));
	
	public static final Block TOPAZ_GLASS_PANE = register(glassPane(blockWithItem("topaz_glass_pane", new PaneBlock(gemstoneGlass(SpectrumBlockSoundGroups.TOPAZ_CLUSTER, MapColor.CYAN)), InkColors.CYAN), TOPAZ_GLASS));
	public static final Block AMETHYST_GLASS_PANE = register(glassPane(blockWithItem("amethyst_glass_pane", new PaneBlock(gemstoneGlass(BlockSoundGroup.AMETHYST_CLUSTER, MapColor.MAGENTA)), InkColors.MAGENTA), AMETHYST_GLASS));
	public static final Block CITRINE_GLASS_PANE = register(glassPane(blockWithItem("citrine_glass_pane", new PaneBlock(gemstoneGlass(SpectrumBlockSoundGroups.CITRINE_CLUSTER, MapColor.YELLOW)), InkColors.YELLOW), CITRINE_GLASS));
	public static final Block ONYX_GLASS_PANE = register(glassPane(blockWithItem("onyx_glass_pane", new PaneBlock(gemstoneGlass(SpectrumBlockSoundGroups.ONYX_CLUSTER, MapColor.BLACK)), InkColors.BLACK), ONYX_GLASS));
	public static final Block MOONSTONE_GLASS_PANE = register(glassPane(blockWithItem("moonstone_glass_pane", new PaneBlock(gemstoneGlass(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER, MapColor.WHITE)), InkColors.WHITE), MOONSTONE_GLASS));
	public static final Block RADIANT_GLASS_PANE = register(glassPane(blockWithItem("radiant_glass_pane", new PaneBlock(gemstoneGlass(BlockSoundGroup.GLASS, MapColor.PALE_YELLOW).luminance(value -> 12)), InkColors.WHITE), RADIANT_GLASS));
	
	public static final Block ETHEREAL_PLATFORM = register(translucent(simple(blockWithItem("ethereal_platform", new EtherealPlatformBlock(gemstoneGlass(BlockSoundGroup.AMETHYST_BLOCK, MapColor.CLEAR).pistonBehavior(PistonBehavior.NORMAL)), InkColors.LIGHT_GRAY))));
	public static final Block UNIVERSE_SPYHOLE = register(translucent(simple(blockWithItem("universe_spyhole", new TransparentBlock(settings(MapColor.CLEAR, SpectrumBlockSoundGroups.CITRINE_BLOCK, 1.5F).requiresTool().blockVision(SpectrumBlocks::never)), InkColors.LIGHT_GRAY))));
	
	private static Settings chime(AbstractBlock block) {
		return AbstractBlock.Settings.copy(block).pistonBehavior(PistonBehavior.DESTROY).hardness(1.0F).nonOpaque();
	}
	
	public static final Block TOPAZ_CHIME = register(translucent(singleton(blockWithItem("topaz_chime", new GemstoneChimeBlock(chime(TOPAZ_CLUSTER), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.CYAN), InkColors.CYAN), SpectrumTexturedModels.CHIME)));
	public static final Block AMETHYST_CHIME = register(translucent(singleton(blockWithItem("amethyst_chime", new GemstoneChimeBlock(chime(AMETHYST_CLUSTER), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.MAGENTA), InkColors.MAGENTA), SpectrumTexturedModels.CHIME)));
	public static final Block CITRINE_CHIME = register(translucent(singleton(blockWithItem("citrine_chime", new GemstoneChimeBlock(chime(CITRINE_CLUSTER), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.YELLOW), InkColors.YELLOW), SpectrumTexturedModels.CHIME)));
	public static final Block ONYX_CHIME = register(translucent(singleton(blockWithItem("onyx_chime", new GemstoneChimeBlock(chime(ONYX_CLUSTER), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.BLACK), InkColors.BLACK), SpectrumTexturedModels.CHIME)));
	public static final Block MOONSTONE_CHIME = register(translucent(singleton(blockWithItem("moonstone_chime", new GemstoneChimeBlock(chime(MOONSTONE_CLUSTER), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.WHITE), InkColors.WHITE), SpectrumTexturedModels.CHIME)));
	
	private static Settings pylon(AbstractBlock block) {
		return AbstractBlock.Settings.copy(block).nonOpaque();
	}
	
	public static final Block TOPAZ_PYLON = register(pylon(blockWithItem("topaz_pylon", new PylonBlock(pylon(TOPAZ_BLOCK)), InkColors.CYAN)));
	public static final Block AMETHYST_PYLON = register(pylon(blockWithItem("amethyst_pylon", new PylonBlock(pylon(AMETHYST_BLOCK)), InkColors.MAGENTA)));
	public static final Block CITRINE_PYLON = register(pylon(blockWithItem("citrine_pylon", new PylonBlock(pylon(CITRINE_BLOCK)), InkColors.YELLOW)));
	public static final Block ONYX_PYLON = register(pylon(blockWithItem("onyx_pylon", new PylonBlock(pylon(ONYX_BLOCK)), InkColors.BLACK)));
	public static final Block MOONSTONE_PYLON = register(pylon(blockWithItem("moonstone_pylon", new PylonBlock(pylon(MOONSTONE_BLOCK)), InkColors.WHITE)));
	
	public static final Block SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("semi_permeable_glass", new AlternatePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(GLASS), GLASS, false), InkColors.WHITE), b -> GLASS)));
	public static final Block TINTED_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("tinted_semi_permeable_glass", new AlternatePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(TINTED_GLASS), TINTED_GLASS, true), InkColors.BLACK), b -> TINTED_GLASS)));
	public static final Block RADIANT_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("radiant_semi_permeable_glass", new AlternatePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(RADIANT_GLASS), RADIANT_GLASS, false), InkColors.YELLOW), b -> RADIANT_GLASS)));
	public static final Block TOPAZ_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("topaz_semi_permeable_glass", new GemstonePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(TOPAZ_GLASS), BuiltinGemstoneColor.CYAN), InkColors.CYAN), b -> TOPAZ_GLASS)));
	public static final Block AMETHYST_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("amethyst_semi_permeable_glass", new GemstonePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(AMETHYST_GLASS), BuiltinGemstoneColor.MAGENTA), InkColors.MAGENTA), b -> AMETHYST_GLASS)));
	public static final Block CITRINE_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("citrine_semi_permeable_glass", new GemstonePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(CITRINE_GLASS), BuiltinGemstoneColor.YELLOW), InkColors.YELLOW), b -> CITRINE_GLASS)));
	public static final Block ONYX_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("onyx_semi_permeable_glass", new GemstonePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(ONYX_GLASS), BuiltinGemstoneColor.BLACK), InkColors.BLACK), b -> ONYX_GLASS)));
	public static final Block MOONSTONE_SEMI_PERMEABLE_GLASS = register(translucent(parented(blockWithItem("moonstone_semi_permeable_glass", new GemstonePlayerOnlyGlassBlock(AbstractBlock.Settings.copy(MOONSTONE_GLASS), BuiltinGemstoneColor.WHITE), InkColors.WHITE), b -> MOONSTONE_GLASS)));
	
	public static final RegistryKey<Block> GLISTERING_MELON = singleton(new BlockRegistrar<>("glistering_melon").withBlock(() -> new Block(AbstractBlock.Settings.copy(MELON))).withItem(block -> new BlockItem(block, IS.of()), InkColors.LIME), TexturedModel.CUBE_COLUMN).blockKey();
	public static final RegistryKey<Block> ATTACHED_GLISTERING_MELON_STEM = cutout(new BlockRegistrar<>("attached_glistering_melon_stem").withBlock(() -> new AttachedStemBlock(RegistryKey.of(RegistryKeys.BLOCK, locate("glistering_melon_stem")), GLISTERING_MELON, SpectrumItems.GLISTERING_MELON_SEEDS, AbstractBlock.Settings.copy(ATTACHED_MELON_STEM)))).blockKey();
	public static final RegistryKey<Block> GLISTERING_MELON_STEM = cutout(new BlockRegistrar<>("glistering_melon_stem").withBlock(() -> new StemBlock(GLISTERING_MELON, ATTACHED_GLISTERING_MELON_STEM, SpectrumItems.GLISTERING_MELON_SEEDS, AbstractBlock.Settings.copy(MELON_STEM))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_7).register(age -> SpectrumModelHelper.createModelVariant(Models.STEM_GROWTH_STAGES[age].upload(block, TextureMap.stem(block), ctx.modelCollector))))).withBlockModel((ctx, block) -> {
		Block attached = Registries.BLOCK.get(ATTACHED_GLISTERING_MELON_STEM);
		ctx.excludeFromSimpleItemModelGeneration(block); // Needed b/c vanilla auto-generates an incorrect seeds model for some reason
		return SpectrumModelHelper.createVariantsSupplier(attached, Models.STEM_FRUIT.upload(attached, TextureMap.stemAndUpper(block, attached), ctx.modelCollector)).coordinate(SpectrumModelHelper.createWestDefaultHorizontalFacingVariantMap());
	})).blockKey();
	
	public static final Block PRESENT = register(cutout(blockWithItem("present", new PresentBlock(AbstractBlock.Settings.copy(WHITE_WOOL)), block -> new PresentBlockItem(block, IS.of(1)), InkColors.LIGHT_GRAY)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(PresentBlock.VARIANT).register(variant -> SpectrumModelHelper.createModelVariant(SpectrumModels.PRESENT.upload(block, "_" + variant.asString(), new TextureMap().put(TextureKey.TEXTURE, TextureMap.getSubId(block, "_" + variant.asString())).put(TextureKey.PARTICLE, TextureMap.getId(variant.woolBase)), ctx.modelCollector))))).withPredefinedItemModel());
	public static final Block TITRATION_BARREL = register(blockWithItem("titration_barrel", new TitrationBarrelBlock(AbstractBlock.Settings.copy(OAK_PLANKS).mapColor(MapColor.RED)), InkColors.MAGENTA).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createUpDefaultHorizontalFacingVariantMap()).coordinate(BlockStateVariantMap.create(TitrationBarrelBlock.BARREL_STATE).register(state -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeBottomTop(b -> b, "_side", b -> b, "_top_" + state.asString(), b -> b, "_bottom").upload(block, state == TitrationBarrelBlock.BarrelState.EMPTY ? "" : "_" + state.asString(), ctx.modelCollector))))));
	
	public static final Block PARAMETRIC_MINING_DEVICE = register(defaultUpFacing(blockWithItem("parametric_mining_device", new ParametricMiningDeviceBlock(AbstractBlock.Settings.copy(BLACKSLAG).nonOpaque().breakInstantly()), block -> new ParametricMiningDeviceItem(block, IS.of(8)), InkColors.RED), ModelIds::getBlockModelId).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final Block THREAT_CONFLUX = register(blockWithItem("threat_conflux", new ThreatConfluxBlock(AbstractBlock.Settings.copy(BLACKSLAG).nonOpaque().breakInstantly()), block -> new ThreatConfluxItem(block, IS.of(8)), InkColors.RED).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(ThreatConfluxBlock.ARMED).register(armed -> SpectrumModelHelper.createModelVariant(block, armed == ThreatConfluxBlock.ArmedState.NOT_ARMED ? "" : "_armed")))));
	
	public static final Block BLOCK_FLOODER = register(simple(block("block_flooder", new BlockFlooderBlock(settings(MapColor.LIGHT_BLUE_GRAY, BlockSoundGroup.ROOTED_DIRT, 0.0F)))));
	public static final Block BOTTOMLESS_BUNDLE = register(cutout(blockWithItem("bottomless_bundle", new BottomlessBundleBlock(settings(MapColor.PALE_PURPLE, BlockSoundGroup.WOOL, 1.0F).nonOpaque().pistonBehavior(PistonBehavior.DESTROY)), block -> new BottomlessBundleItem(block, IS.of(1)), InkColors.LIGHT_GRAY))
			.withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(BottomlessBundleBlock.LOCKED, ModelIds.getBlockSubModelId(block, "_locked"), ModelIds.getBlockSubModelId(block, "_unlocked"))))
			.withPredefinedItemModel());
	
	//TODO these names don't match
	public static final Block WAND_LIGHT_BLOCK = register(singleton(block("wand_light", new WandLightBlock(AbstractBlock.Settings.copy(LIGHT).sounds(SpectrumBlockSoundGroups.WAND_LIGHT).breakInstantly())), SpectrumTexturedModels.particle(SpectrumTextures.SHIMMERSTONE_LIGHT)));
	public static final Block DECAYING_LIGHT_BLOCK = register(parented(block("decaying_light", new DecayingLightBlock(AbstractBlock.Settings.copy(WAND_LIGHT_BLOCK).ticksRandomly())), b -> WAND_LIGHT_BLOCK));
	
	private static Settings decay(MapColor mapColor, BlockSoundGroup soundGroup, float strength, float resistance, PistonBehavior pistonBehavior) {
		return settings(mapColor, soundGroup, strength, resistance).pistonBehavior(pistonBehavior).ticksRandomly().allowsSpawning((state, world, pos, type) -> false);
	}
	
	public static <T extends Block> BlockRegistrar<T> decay(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> {
			Identifier none = Models.CUBE_ALL.upload(block, "_none", SpectrumTextureMaps.all(block, "_none"), ctx.modelCollector);
			Identifier def = Models.CUBE_ALL.upload(block, "_default", SpectrumTextureMaps.all(block, "_default"), ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(DecayBlock.CONVERSION).register(DecayBlock.Conversion.NONE, SpectrumModelHelper.createModelVariant(none)).register(DecayBlock.Conversion.DEFAULT, SpectrumModelHelper.createModelVariant(def)).register(DecayBlock.Conversion.SPECIAL, SpectrumModelHelper.createModelVariant(def)));
		});
	}
	
	public static final Block FADING = register(decay(block("fading", new FadingBlock(decay(MapColor.DARK_GREEN, BlockSoundGroup.GRASS, 0.5F, 0.5F, PistonBehavior.DESTROY)))));
	public static final Block FAILING = register(decay(block("failing", new FailingBlock(decay(MapColor.BLACK, BlockSoundGroup.STONE, 20.0F, 50.0F, PistonBehavior.BLOCK)))));
	public static final Block RUIN = register(decay(block("ruin", new RuinBlock(decay(MapColor.BLACK, BlockSoundGroup.STONE, 100.0F, 3600000.0F, PistonBehavior.BLOCK)))));
	public static final Block FORFEITURE = register(decay(block("forfeiture", new ForfeitureBlock(decay(MapColor.BLACK, BlockSoundGroup.STONE, 100.0F, 3600000.0F, PistonBehavior.BLOCK)))));
	public static final Block DECAY_AWAY = register(simple(block("decay_away", new DecayAwayBlock(AbstractBlock.Settings.copy(DIRT).pistonBehavior(PistonBehavior.DESTROY)))));
	
	// ROCK CANDY
	private static Settings rockCandy(AbstractBlock block) {
		return AbstractBlock.Settings.copy(block).pistonBehavior(PistonBehavior.DESTROY).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly();
	}
	
	private static final ToIntFunction<BlockState> ROCK_CANDY_LUMINANCE = state -> Math.max(15, state.get(Properties.AGE_2) * 3 + (state.get(SugarStickBlock.LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? LiquidCrystalFluidBlock.LUMINANCE : 8));
	public static final Block SUGAR_STICK = register(sugarStick(blockWithItem("sugar_stick", new SugarStickBlock(rockCandy(SMALL_AMETHYST_BUD), RockCandy.RockCandyVariant.SUGAR), InkColors.PINK), b -> b));
	public static final Block TOPAZ_SUGAR_STICK = register(sugarStick(blockWithItem("topaz_sugar_stick", new SugarStickBlock(rockCandy(SMALL_TOPAZ_BUD), RockCandy.RockCandyVariant.TOPAZ), InkColors.PINK), b -> TOPAZ_GLASS));
	public static final Block AMETHYST_SUGAR_STICK = register(sugarStick(blockWithItem("amethyst_sugar_stick", new SugarStickBlock(rockCandy(SMALL_AMETHYST_BUD), RockCandy.RockCandyVariant.AMETHYST), InkColors.PINK), b -> AMETHYST_GLASS));
	public static final Block CITRINE_SUGAR_STICK = register(sugarStick(blockWithItem("citrine_sugar_stick", new SugarStickBlock(rockCandy(SMALL_CITRINE_BUD), RockCandy.RockCandyVariant.CITRINE), InkColors.PINK), b -> CITRINE_GLASS));
	public static final Block ONYX_SUGAR_STICK = register(sugarStick(blockWithItem("onyx_sugar_stick", new SugarStickBlock(rockCandy(SMALL_ONYX_BUD), RockCandy.RockCandyVariant.ONYX), InkColors.PINK), b -> ONYX_GLASS));
	public static final Block MOONSTONE_SUGAR_STICK = register(sugarStick(blockWithItem("moonstone_sugar_stick", new SugarStickBlock(rockCandy(SMALL_MOONSTONE_BUD), RockCandy.RockCandyVariant.MOONSTONE), InkColors.PINK), b -> MOONSTONE_GLASS));
	
	// PASTEL NETWORK
	private static Settings pastelNode(BlockSoundGroup soundGroup) {
		return settings(MapColor.CLEAR, soundGroup, 1.5F).pistonBehavior(PistonBehavior.DESTROY).nonOpaque().requiresTool();
	}
	
	public static final Block CONNECTION_NODE = register(cutout(defaultUpFacing(blockWithItem("connection_node", new PastelNodeBlock(pastelNode(BlockSoundGroup.AMETHYST_CLUSTER), PastelNodeType.CONNECTION), IS.of(16), InkColors.LIGHT_GRAY), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_GENERIC_NODE))).withPredefinedItemModel());
	public static final Block PROVIDER_NODE = register(cutout(defaultUpFacing(blockWithItem("provider_node", new PastelNodeBlock(pastelNode(BlockSoundGroup.AMETHYST_CLUSTER), PastelNodeType.PROVIDER), IS.of(16), InkColors.MAGENTA), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_PUSH_NODE))).withPredefinedItemModel());
	public static final Block STORAGE_NODE = register(cutout(defaultUpFacing(blockWithItem("storage_node", new PastelNodeBlock(pastelNode(SpectrumBlockSoundGroups.TOPAZ_CLUSTER), PastelNodeType.STORAGE), IS.of(16), InkColors.CYAN), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_STORE_NODE))).withPredefinedItemModel());
	public static final Block BUFFER_NODE = register(cutout(defaultUpFacing(blockWithItem("buffer_node", new PastelNodeBlock(pastelNode(SpectrumBlockSoundGroups.TOPAZ_CLUSTER), PastelNodeType.BUFFER), IS.of(16), InkColors.GREEN), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_STORE_NODE))).withPredefinedItemModel());
	public static final Block SENDER_NODE = register(cutout(defaultUpFacing(blockWithItem("sender_node", new PastelNodeBlock(pastelNode(SpectrumBlockSoundGroups.CITRINE_CLUSTER), PastelNodeType.SENDER), IS.of(16), InkColors.YELLOW), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_PUSH_NODE))).withPredefinedItemModel());
	public static final Block GATHER_NODE = register(cutout(defaultUpFacing(blockWithItem("gather_node", new PastelNodeBlock(pastelNode(SpectrumBlockSoundGroups.ONYX_CLUSTER), PastelNodeType.GATHER), IS.of(16), InkColors.BLACK), SpectrumTexturedModels.parented(SpectrumModels.PASTEL_PULL_NODE))).withPredefinedItemModel());
	
	// COLORED BLOCK FAMILIES
	
	public static ColoredPlankBlock registerColoredPlanks(String name, InkColor color) {
		return register(blockWithItem(name, new ColoredPlankBlock(copyWithMapColor(OAK_PLANKS, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color));
	}
	
	public static ColoredStairsBlock registerColoredStairs(String name, ColoredPlankBlock baseBlock) {
		return register(blockWithItem(name, new ColoredStairsBlock(baseBlock.getDefaultState(), copyWithMapColor(OAK_STAIRS, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()));
	}
	
	public static ColoredPressurePlateBlock registerColoredPressurePlate(String name, ColoredPlankBlock baseBlock) {
		return register(blockWithItem(name, new ColoredPressurePlateBlock(copyWithMapColor(OAK_PRESSURE_PLATE, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()));
	}
	
	public static ColoredFenceBlock registerColoredFence(String name, ColoredPlankBlock baseBlock) {
		return register(burnable(blockWithItem(name, new ColoredFenceBlock(copyWithMapColor(OAK_FENCE, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()), 300));
	}
	
	public static ColoredFenceGateBlock registerColoredFenceGate(String name, ColoredPlankBlock baseBlock) {
		return register(burnable(blockWithItem(name, new ColoredFenceGateBlock(copyWithMapColor(OAK_FENCE_GATE, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()), 300));
	}
	
	public static ColoredWoodenButtonBlock registerColoredButton(String name, ColoredPlankBlock baseBlock) {
		return register(blockWithItem(name, new ColoredWoodenButtonBlock(copyWithMapColor(OAK_BUTTON, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()));
	}
	
	public static ColoredSlabBlock registerColoredSlab(String name, ColoredPlankBlock baseBlock) {
		return register(blockWithItem(name, new ColoredSlabBlock(copyWithMapColor(OAK_SLAB, baseBlock.getDefaultMapColor()), baseBlock.getColor()), baseBlock.getColor()));
	}
	
	public static final ColoredPlankBlock BLACK_PLANKS = registerColoredPlanks("black_planks", InkColors.BLACK);
	public static final ColoredStairsBlock BLACK_STAIRS = registerColoredStairs("black_stairs", BLACK_PLANKS);
	public static final ColoredPressurePlateBlock BLACK_PRESSURE_PLATE = registerColoredPressurePlate("black_pressure_plate", BLACK_PLANKS);
	public static final ColoredFenceBlock BLACK_FENCE = registerColoredFence("black_fence", BLACK_PLANKS);
	public static final ColoredFenceGateBlock BLACK_FENCE_GATE = registerColoredFenceGate("black_fence_gate", BLACK_PLANKS);
	public static final ColoredWoodenButtonBlock BLACK_BUTTON = registerColoredButton("black_button", BLACK_PLANKS);
	public static final ColoredSlabBlock BLACK_SLAB = registerColoredSlab("black_slab", BLACK_PLANKS);
	public static final BlockFamily BLACK_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BLACK_PLANKS).stairs(BLACK_STAIRS).pressurePlate(BLACK_PRESSURE_PLATE).fence(BLACK_FENCE).fenceGate(BLACK_FENCE_GATE).button(BLACK_BUTTON).slab(BLACK_SLAB).build());
	
	public static final ColoredPlankBlock BLUE_PLANKS = registerColoredPlanks("blue_planks", InkColors.BLUE);
	public static final ColoredStairsBlock BLUE_STAIRS = registerColoredStairs("blue_stairs", BLUE_PLANKS);
	public static final ColoredPressurePlateBlock BLUE_PRESSURE_PLATE = registerColoredPressurePlate("blue_pressure_plate", BLUE_PLANKS);
	public static final ColoredFenceBlock BLUE_FENCE = registerColoredFence("blue_fence", BLUE_PLANKS);
	public static final ColoredFenceGateBlock BLUE_FENCE_GATE = registerColoredFenceGate("blue_fence_gate", BLUE_PLANKS);
	public static final ColoredWoodenButtonBlock BLUE_BUTTON = registerColoredButton("blue_button", BLUE_PLANKS);
	public static final ColoredSlabBlock BLUE_SLAB = registerColoredSlab("blue_slab", BLUE_PLANKS);
	public static final BlockFamily BLUE_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BLUE_PLANKS).stairs(BLUE_STAIRS).pressurePlate(BLUE_PRESSURE_PLATE).fence(BLUE_FENCE).fenceGate(BLUE_FENCE_GATE).button(BLUE_BUTTON).slab(BLUE_SLAB).build());
	
	public static final ColoredPlankBlock BROWN_PLANKS = registerColoredPlanks("brown_planks", InkColors.BROWN);
	public static final ColoredStairsBlock BROWN_STAIRS = registerColoredStairs("brown_stairs", BROWN_PLANKS);
	public static final ColoredPressurePlateBlock BROWN_PRESSURE_PLATE = registerColoredPressurePlate("brown_pressure_plate", BROWN_PLANKS);
	public static final ColoredFenceBlock BROWN_FENCE = registerColoredFence("brown_fence", BROWN_PLANKS);
	public static final ColoredFenceGateBlock BROWN_FENCE_GATE = registerColoredFenceGate("brown_fence_gate", BROWN_PLANKS);
	public static final ColoredWoodenButtonBlock BROWN_BUTTON = registerColoredButton("brown_button", BROWN_PLANKS);
	public static final ColoredSlabBlock BROWN_SLAB = registerColoredSlab("brown_slab", BROWN_PLANKS);
	public static final BlockFamily BROWN_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BROWN_PLANKS).stairs(BROWN_STAIRS).pressurePlate(BROWN_PRESSURE_PLATE).fence(BROWN_FENCE).fenceGate(BROWN_FENCE_GATE).button(BROWN_BUTTON).slab(BROWN_SLAB).build());
	
	public static final ColoredPlankBlock CYAN_PLANKS = registerColoredPlanks("cyan_planks", InkColors.CYAN);
	public static final ColoredStairsBlock CYAN_STAIRS = registerColoredStairs("cyan_stairs", CYAN_PLANKS);
	public static final ColoredPressurePlateBlock CYAN_PRESSURE_PLATE = registerColoredPressurePlate("cyan_pressure_plate", CYAN_PLANKS);
	public static final ColoredFenceBlock CYAN_FENCE = registerColoredFence("cyan_fence", CYAN_PLANKS);
	public static final ColoredFenceGateBlock CYAN_FENCE_GATE = registerColoredFenceGate("cyan_fence_gate", CYAN_PLANKS);
	public static final ColoredWoodenButtonBlock CYAN_BUTTON = registerColoredButton("cyan_button", CYAN_PLANKS);
	public static final ColoredSlabBlock CYAN_SLAB = registerColoredSlab("cyan_slab", CYAN_PLANKS);
	public static final BlockFamily CYAN_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(CYAN_PLANKS).stairs(CYAN_STAIRS).pressurePlate(CYAN_PRESSURE_PLATE).fence(CYAN_FENCE).fenceGate(CYAN_FENCE_GATE).button(CYAN_BUTTON).slab(CYAN_SLAB).build());
	
	public static final ColoredPlankBlock GRAY_PLANKS = registerColoredPlanks("gray_planks", InkColors.GRAY);
	public static final ColoredStairsBlock GRAY_STAIRS = registerColoredStairs("gray_stairs", GRAY_PLANKS);
	public static final ColoredPressurePlateBlock GRAY_PRESSURE_PLATE = registerColoredPressurePlate("gray_pressure_plate", GRAY_PLANKS);
	public static final ColoredFenceBlock GRAY_FENCE = registerColoredFence("gray_fence", GRAY_PLANKS);
	public static final ColoredFenceGateBlock GRAY_FENCE_GATE = registerColoredFenceGate("gray_fence_gate", GRAY_PLANKS);
	public static final ColoredWoodenButtonBlock GRAY_BUTTON = registerColoredButton("gray_button", GRAY_PLANKS);
	public static final ColoredSlabBlock GRAY_SLAB = registerColoredSlab("gray_slab", GRAY_PLANKS);
	public static final BlockFamily GRAY_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(GRAY_PLANKS).stairs(GRAY_STAIRS).pressurePlate(GRAY_PRESSURE_PLATE).fence(GRAY_FENCE).fenceGate(GRAY_FENCE_GATE).button(GRAY_BUTTON).slab(GRAY_SLAB).build());
	
	public static final ColoredPlankBlock GREEN_PLANKS = registerColoredPlanks("green_planks", InkColors.GREEN);
	public static final ColoredStairsBlock GREEN_STAIRS = registerColoredStairs("green_stairs", GREEN_PLANKS);
	public static final ColoredPressurePlateBlock GREEN_PRESSURE_PLATE = registerColoredPressurePlate("green_pressure_plate", GREEN_PLANKS);
	public static final ColoredFenceBlock GREEN_FENCE = registerColoredFence("green_fence", GREEN_PLANKS);
	public static final ColoredFenceGateBlock GREEN_FENCE_GATE = registerColoredFenceGate("green_fence_gate", GREEN_PLANKS);
	public static final ColoredWoodenButtonBlock GREEN_BUTTON = registerColoredButton("green_button", GREEN_PLANKS);
	public static final ColoredSlabBlock GREEN_SLAB = registerColoredSlab("green_slab", GREEN_PLANKS);
	public static final BlockFamily GREEN_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(GREEN_PLANKS).stairs(GREEN_STAIRS).pressurePlate(GREEN_PRESSURE_PLATE).fence(GREEN_FENCE).fenceGate(GREEN_FENCE_GATE).button(GREEN_BUTTON).slab(GREEN_SLAB).build());
	
	public static final ColoredPlankBlock LIGHT_BLUE_PLANKS = registerColoredPlanks("light_blue_planks", InkColors.LIGHT_BLUE);
	public static final ColoredStairsBlock LIGHT_BLUE_STAIRS = registerColoredStairs("light_blue_stairs", LIGHT_BLUE_PLANKS);
	public static final ColoredPressurePlateBlock LIGHT_BLUE_PRESSURE_PLATE = registerColoredPressurePlate("light_blue_pressure_plate", LIGHT_BLUE_PLANKS);
	public static final ColoredFenceBlock LIGHT_BLUE_FENCE = registerColoredFence("light_blue_fence", LIGHT_BLUE_PLANKS);
	public static final ColoredFenceGateBlock LIGHT_BLUE_FENCE_GATE = registerColoredFenceGate("light_blue_fence_gate", LIGHT_BLUE_PLANKS);
	public static final ColoredWoodenButtonBlock LIGHT_BLUE_BUTTON = registerColoredButton("light_blue_button", LIGHT_BLUE_PLANKS);
	public static final ColoredSlabBlock LIGHT_BLUE_SLAB = registerColoredSlab("light_blue_slab", LIGHT_BLUE_PLANKS);
	public static final BlockFamily LIGHT_BLUE_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(LIGHT_BLUE_PLANKS).stairs(LIGHT_BLUE_STAIRS).pressurePlate(LIGHT_BLUE_PRESSURE_PLATE).fence(LIGHT_BLUE_FENCE).fenceGate(LIGHT_BLUE_FENCE_GATE).button(LIGHT_BLUE_BUTTON).slab(LIGHT_BLUE_SLAB).build());
	
	public static final ColoredPlankBlock LIGHT_GRAY_PLANKS = registerColoredPlanks("light_gray_planks", InkColors.LIGHT_GRAY);
	public static final ColoredStairsBlock LIGHT_GRAY_STAIRS = registerColoredStairs("light_gray_stairs", LIGHT_GRAY_PLANKS);
	public static final ColoredPressurePlateBlock LIGHT_GRAY_PRESSURE_PLATE = registerColoredPressurePlate("light_gray_pressure_plate", LIGHT_GRAY_PLANKS);
	public static final ColoredFenceBlock LIGHT_GRAY_FENCE = registerColoredFence("light_gray_fence", LIGHT_GRAY_PLANKS);
	public static final ColoredFenceGateBlock LIGHT_GRAY_FENCE_GATE = registerColoredFenceGate("light_gray_fence_gate", LIGHT_GRAY_PLANKS);
	public static final ColoredWoodenButtonBlock LIGHT_GRAY_BUTTON = registerColoredButton("light_gray_button", LIGHT_GRAY_PLANKS);
	public static final ColoredSlabBlock LIGHT_GRAY_SLAB = registerColoredSlab("light_gray_slab", LIGHT_GRAY_PLANKS);
	public static final BlockFamily LIGHT_GRAY_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(LIGHT_GRAY_PLANKS).stairs(LIGHT_GRAY_STAIRS).pressurePlate(LIGHT_GRAY_PRESSURE_PLATE).fence(LIGHT_GRAY_FENCE).fenceGate(LIGHT_GRAY_FENCE_GATE).button(LIGHT_GRAY_BUTTON).slab(LIGHT_GRAY_SLAB).build());
	
	public static final ColoredPlankBlock LIME_PLANKS = registerColoredPlanks("lime_planks", InkColors.LIME);
	public static final ColoredStairsBlock LIME_STAIRS = registerColoredStairs("lime_stairs", LIME_PLANKS);
	public static final ColoredPressurePlateBlock LIME_PRESSURE_PLATE = registerColoredPressurePlate("lime_pressure_plate", LIME_PLANKS);
	public static final ColoredFenceBlock LIME_FENCE = registerColoredFence("lime_fence", LIME_PLANKS);
	public static final ColoredFenceGateBlock LIME_FENCE_GATE = registerColoredFenceGate("lime_fence_gate", LIME_PLANKS);
	public static final ColoredWoodenButtonBlock LIME_BUTTON = registerColoredButton("lime_button", LIME_PLANKS);
	public static final ColoredSlabBlock LIME_SLAB = registerColoredSlab("lime_slab", LIME_PLANKS);
	public static final BlockFamily LIME_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(LIME_PLANKS).stairs(LIME_STAIRS).pressurePlate(LIME_PRESSURE_PLATE).fence(LIME_FENCE).fenceGate(LIME_FENCE_GATE).button(LIME_BUTTON).slab(LIME_SLAB).build());
	
	public static final ColoredPlankBlock MAGENTA_PLANKS = registerColoredPlanks("magenta_planks", InkColors.MAGENTA);
	public static final ColoredStairsBlock MAGENTA_STAIRS = registerColoredStairs("magenta_stairs", MAGENTA_PLANKS);
	public static final ColoredPressurePlateBlock MAGENTA_PRESSURE_PLATE = registerColoredPressurePlate("magenta_pressure_plate", MAGENTA_PLANKS);
	public static final ColoredFenceBlock MAGENTA_FENCE = registerColoredFence("magenta_fence", MAGENTA_PLANKS);
	public static final ColoredFenceGateBlock MAGENTA_FENCE_GATE = registerColoredFenceGate("magenta_fence_gate", MAGENTA_PLANKS);
	public static final ColoredWoodenButtonBlock MAGENTA_BUTTON = registerColoredButton("magenta_button", MAGENTA_PLANKS);
	public static final ColoredSlabBlock MAGENTA_SLAB = registerColoredSlab("magenta_slab", MAGENTA_PLANKS);
	public static final BlockFamily MAGENTA_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(MAGENTA_PLANKS).stairs(MAGENTA_STAIRS).pressurePlate(MAGENTA_PRESSURE_PLATE).fence(MAGENTA_FENCE).fenceGate(MAGENTA_FENCE_GATE).button(MAGENTA_BUTTON).slab(MAGENTA_SLAB).build());
	
	public static final ColoredPlankBlock ORANGE_PLANKS = registerColoredPlanks("orange_planks", InkColors.ORANGE);
	public static final ColoredStairsBlock ORANGE_STAIRS = registerColoredStairs("orange_stairs", ORANGE_PLANKS);
	public static final ColoredPressurePlateBlock ORANGE_PRESSURE_PLATE = registerColoredPressurePlate("orange_pressure_plate", ORANGE_PLANKS);
	public static final ColoredFenceBlock ORANGE_FENCE = registerColoredFence("orange_fence", ORANGE_PLANKS);
	public static final ColoredFenceGateBlock ORANGE_FENCE_GATE = registerColoredFenceGate("orange_fence_gate", ORANGE_PLANKS);
	public static final ColoredWoodenButtonBlock ORANGE_BUTTON = registerColoredButton("orange_button", ORANGE_PLANKS);
	public static final ColoredSlabBlock ORANGE_SLAB = registerColoredSlab("orange_slab", ORANGE_PLANKS);
	public static final BlockFamily ORANGE_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(ORANGE_PLANKS).stairs(ORANGE_STAIRS).pressurePlate(ORANGE_PRESSURE_PLATE).fence(ORANGE_FENCE).fenceGate(ORANGE_FENCE_GATE).button(ORANGE_BUTTON).slab(ORANGE_SLAB).build());
	
	public static final ColoredPlankBlock PINK_PLANKS = registerColoredPlanks("pink_planks", InkColors.PINK);
	public static final ColoredStairsBlock PINK_STAIRS = registerColoredStairs("pink_stairs", PINK_PLANKS);
	public static final ColoredPressurePlateBlock PINK_PRESSURE_PLATE = registerColoredPressurePlate("pink_pressure_plate", PINK_PLANKS);
	public static final ColoredFenceBlock PINK_FENCE = registerColoredFence("pink_fence", PINK_PLANKS);
	public static final ColoredFenceGateBlock PINK_FENCE_GATE = registerColoredFenceGate("pink_fence_gate", PINK_PLANKS);
	public static final ColoredWoodenButtonBlock PINK_BUTTON = registerColoredButton("pink_button", PINK_PLANKS);
	public static final ColoredSlabBlock PINK_SLAB = registerColoredSlab("pink_slab", PINK_PLANKS);
	public static final BlockFamily PINK_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(PINK_PLANKS).stairs(PINK_STAIRS).pressurePlate(PINK_PRESSURE_PLATE).fence(PINK_FENCE).fenceGate(PINK_FENCE_GATE).button(PINK_BUTTON).slab(PINK_SLAB).build());
	
	public static final ColoredPlankBlock PURPLE_PLANKS = registerColoredPlanks("purple_planks", InkColors.PURPLE);
	public static final ColoredStairsBlock PURPLE_STAIRS = registerColoredStairs("purple_stairs", PURPLE_PLANKS);
	public static final ColoredPressurePlateBlock PURPLE_PRESSURE_PLATE = registerColoredPressurePlate("purple_pressure_plate", PURPLE_PLANKS);
	public static final ColoredFenceBlock PURPLE_FENCE = registerColoredFence("purple_fence", PURPLE_PLANKS);
	public static final ColoredFenceGateBlock PURPLE_FENCE_GATE = registerColoredFenceGate("purple_fence_gate", PURPLE_PLANKS);
	public static final ColoredWoodenButtonBlock PURPLE_BUTTON = registerColoredButton("purple_button", PURPLE_PLANKS);
	public static final ColoredSlabBlock PURPLE_SLAB = registerColoredSlab("purple_slab", PURPLE_PLANKS);
	public static final BlockFamily PURPLE_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(PURPLE_PLANKS).stairs(PURPLE_STAIRS).pressurePlate(PURPLE_PRESSURE_PLATE).fence(PURPLE_FENCE).fenceGate(PURPLE_FENCE_GATE).button(PURPLE_BUTTON).slab(PURPLE_SLAB).build());
	
	public static final ColoredPlankBlock RED_PLANKS = registerColoredPlanks("red_planks", InkColors.RED);
	public static final ColoredStairsBlock RED_STAIRS = registerColoredStairs("red_stairs", RED_PLANKS);
	public static final ColoredPressurePlateBlock RED_PRESSURE_PLATE = registerColoredPressurePlate("red_pressure_plate", RED_PLANKS);
	public static final ColoredFenceBlock RED_FENCE = registerColoredFence("red_fence", RED_PLANKS);
	public static final ColoredFenceGateBlock RED_FENCE_GATE = registerColoredFenceGate("red_fence_gate", RED_PLANKS);
	public static final ColoredWoodenButtonBlock RED_BUTTON = registerColoredButton("red_button", RED_PLANKS);
	public static final ColoredSlabBlock RED_SLAB = registerColoredSlab("red_slab", RED_PLANKS);
	public static final BlockFamily RED_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(RED_PLANKS).stairs(RED_STAIRS).pressurePlate(RED_PRESSURE_PLATE).fence(RED_FENCE).fenceGate(RED_FENCE_GATE).button(RED_BUTTON).slab(RED_SLAB).build());
	
	public static final ColoredPlankBlock WHITE_PLANKS = registerColoredPlanks("white_planks", InkColors.WHITE);
	public static final ColoredStairsBlock WHITE_STAIRS = registerColoredStairs("white_stairs", WHITE_PLANKS);
	public static final ColoredPressurePlateBlock WHITE_PRESSURE_PLATE = registerColoredPressurePlate("white_pressure_plate", WHITE_PLANKS);
	public static final ColoredFenceBlock WHITE_FENCE = registerColoredFence("white_fence", WHITE_PLANKS);
	public static final ColoredFenceGateBlock WHITE_FENCE_GATE = registerColoredFenceGate("white_fence_gate", WHITE_PLANKS);
	public static final ColoredWoodenButtonBlock WHITE_BUTTON = registerColoredButton("white_button", WHITE_PLANKS);
	public static final ColoredSlabBlock WHITE_SLAB = registerColoredSlab("white_slab", WHITE_PLANKS);
	public static final BlockFamily WHITE_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(WHITE_PLANKS).stairs(WHITE_STAIRS).pressurePlate(WHITE_PRESSURE_PLATE).fence(WHITE_FENCE).fenceGate(WHITE_FENCE_GATE).button(WHITE_BUTTON).slab(WHITE_SLAB).build());
	
	public static final ColoredPlankBlock YELLOW_PLANKS = registerColoredPlanks("yellow_planks", InkColors.YELLOW);
	public static final ColoredStairsBlock YELLOW_STAIRS = registerColoredStairs("yellow_stairs", YELLOW_PLANKS);
	public static final ColoredPressurePlateBlock YELLOW_PRESSURE_PLATE = registerColoredPressurePlate("yellow_pressure_plate", YELLOW_PLANKS);
	public static final ColoredFenceBlock YELLOW_FENCE = registerColoredFence("yellow_fence", YELLOW_PLANKS);
	public static final ColoredFenceGateBlock YELLOW_FENCE_GATE = registerColoredFenceGate("yellow_fence_gate", YELLOW_PLANKS);
	public static final ColoredWoodenButtonBlock YELLOW_BUTTON = registerColoredButton("yellow_button", YELLOW_PLANKS);
	public static final ColoredSlabBlock YELLOW_SLAB = registerColoredSlab("yellow_slab", YELLOW_PLANKS);
	public static final BlockFamily YELLOW_COLORED_PLANKS_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(YELLOW_PLANKS).stairs(YELLOW_STAIRS).pressurePlate(YELLOW_PRESSURE_PLATE).fence(YELLOW_FENCE).fenceGate(YELLOW_FENCE_GATE).button(YELLOW_BUTTON).slab(YELLOW_SLAB).build());
	
	//DD FLORA
	public static Settings overgrownBlackslag(MapColor color, BlockSoundGroup soundGroup) {
		return settings(color, soundGroup, BLACKSLAG_HARDNESS, BLACKSLAG_RESISTANCE).ticksRandomly();
	}
	
	public static final Block SAWBLADE_GRASS = register(snowy(blockWithItem("sawblade_grass", new BlackslagVegetationBlock(overgrownBlackslag(MapColor.PALE_YELLOW, BlockSoundGroup.AZALEA_LEAVES)), InkColors.LIME), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG, "_top", b -> b, "_top"), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG, "_top", b -> b, "_snow_top")));
	public static final Block SHIMMEL = register(snowy(blockWithItem("shimmel", new BlackslagVegetationBlock(overgrownBlackslag(MapColor.TERRACOTTA_GRAY, BlockSoundGroup.WART_BLOCK)), InkColors.LIME), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG, "_top", b -> BLACKSLAG, "_top"), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG, "_top", b -> BLACKSLAG, "_top")));
	public static final Block OVERGROWN_BLACKSLAG = register(snowy(blockWithItem("overgrown_blackslag", new BlackslagVegetationBlock(overgrownBlackslag(MapColor.DARK_GREEN, BlockSoundGroup.VINE).velocityMultiplier(0.925F)), InkColors.LIME), SpectrumTexturedModels.overgrown(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG, "_top", b -> b, "_fronds"), SpectrumTexturedModels.overgrown(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG, "_top", b -> b, "_snow_fronds")));
	public static final Block ROTTEN_GROUND = register(blockWithItem("rotten_ground", new RottenGroundBlock(Settings.copy(MUD).mapColor(MapColor.PALE_PURPLE).sounds(BlockSoundGroup.HONEY).velocityMultiplier(0.775F).jumpVelocityMultiplier(0.9F)), InkColors.LIME).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(TexturedModel.CUBE_ALL.upload(block, ctx.modelCollector)).put(VariantSettings.WEIGHT, 4), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "_bony").upload(block, "_bony", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "_boil").upload(block, "_boil", ctx.modelCollector)))));
	
	public static final float ASH_STRENGTH = 2F;
	
	public static Settings ash(BlockSoundGroup soundGroup) {
		return settings(MapColor.OFF_WHITE, soundGroup, ASH_STRENGTH, ASH_STRENGTH).requiresTool();
	}
	
	public static final Block ASHEN_BLACKSLAG = register(singleton(blockWithItem("ashen_blackslag", new BlackslagBlock(blackslag(BlockSoundGroup.DEEPSLATE).mapColor(MapColor.OFF_WHITE)), InkColors.LIGHT_GRAY), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG, "_top", b -> b, "_top")));
	public static final Block ASH = register(blockWithItem("ash", new AshBlock(ash(BlockSoundGroup.POWDER_SNOW)), InkColors.GRAY).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "").upload(block, "", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "2").upload(block, "2", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "3").upload(block, "3", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "4").upload(block, "4", ctx.modelCollector)))));
	public static final Block ASH_PILE = register(blockWithItem("ash_pile", new AshPileBlock(ash(BlockSoundGroup.POWDER_SNOW).replaceable().blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= 8).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIGHT_GRAY).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_height2")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.LAYERS).registerVariants(height -> {
		Identifier ash = TextureMap.getId(ASH);
		Identifier ash2 = TextureMap.getSubId(ASH, "2");
		Identifier ash3 = TextureMap.getSubId(ASH, "3");
		Identifier ash4 = TextureMap.getSubId(ASH, "4");
		if (height == 8) return List.of(SpectrumModelHelper.createModelVariant(ash), SpectrumModelHelper.createModelVariant(ash2), SpectrumModelHelper.createModelVariant(ash3), SpectrumModelHelper.createModelVariant(ash4));
		Model layerModel = new Model(Optional.of(ModelIds.getBlockSubModelId(SNOW, "_height" + height * 2)), Optional.empty(), TextureKey.PARTICLE, TextureKey.TEXTURE);
		return List.of(
				SpectrumModelHelper.createModelVariant(layerModel.upload(SpectrumCommon.locate("block/ash_pile_height" + height * 2), TextureMap.all(ash), ctx.modelCollector)),
				SpectrumModelHelper.createModelVariant(layerModel.upload(SpectrumCommon.locate("block/ash2_pile_height" + height * 2), TextureMap.all(ash2), ctx.modelCollector)),
				SpectrumModelHelper.createModelVariant(layerModel.upload(SpectrumCommon.locate("block/ash3_pile_height" + height * 2), TextureMap.all(ash3), ctx.modelCollector)),
				SpectrumModelHelper.createModelVariant(layerModel.upload(SpectrumCommon.locate("block/ash4_pile_height" + height * 2), TextureMap.all(ash4), ctx.modelCollector))
		);
	}))));
	
	public static final Block VARIA_SPROUT = register(cutout(blockWithItem("varia_sprout", new AshFloraBlock(settings(MapColor.WHITE, BlockSoundGroup.NETHER_STEM, 0F).breakInstantly().luminance(state -> 11).offset(OffsetType.XZ).dynamicBounds().noCollision().postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), InkColors.WHITE)).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block,
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "").upload(block, "", ctx.modelCollector)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_2").upload(block, "_2", ctx.modelCollector)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_3").upload(block, "_3", ctx.modelCollector)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_4").upload(block, "_4", ctx.modelCollector)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_5").upload(block, "_5", ctx.modelCollector)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_6").upload(block, "_6", ctx.modelCollector)))));
	
	public static final ToIntFunction<BlockState> LANTERN_LIGHT_PROVIDER = (state -> state.get(RedstoneLampBlock.LIT) ? 15 : 0);
	
	public static FungusBlock registerNoxshroom(String name, RegistryKey<ConfiguredFeature<?, ?>> feature, MapColor mapColor) {
		return register(cutout(blockWithItem(name, new FungusBlock(feature, SHIMMEL, settings(mapColor, BlockSoundGroup.FUNGUS, 0.0F).noCollision()), InkColors.LIME)).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_type_1").upload(block, "_type_1", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_type_2").upload(block, "_type_2", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_type_3").upload(block, "_type_3", ctx.modelCollector)))));
	}
	
	public static final FungusBlock SLATE_NOXSHROOM = registerNoxshroom("slate_noxshroom", SpectrumConfiguredFeatures.SLATE_NOXFUNGUS, MapColor.GRAY);
	public static final FungusBlock EBONY_NOXSHROOM = registerNoxshroom("ebony_noxshroom", SpectrumConfiguredFeatures.EBONY_NOXFUNGUS, MapColor.TERRACOTTA_BLACK);
	public static final FungusBlock IVORY_NOXSHROOM = registerNoxshroom("ivory_noxshroom", SpectrumConfiguredFeatures.IVORY_NOXFUNGUS, MapColor.OFF_WHITE);
	public static final FungusBlock CHESTNUT_NOXSHROOM = registerNoxshroom("chestnut_noxshroom", SpectrumConfiguredFeatures.CHESTNUT_NOXFUNGUS, MapColor.DULL_RED);
	
	public static final FlowerPotBlock POTTED_SLATE_NOXSHROOM = register(pottedPlant(block("potted_slate_noxshroom", new FlowerPotBlock(SLATE_NOXSHROOM, pottedPlant())), false));
	public static final FlowerPotBlock POTTED_EBONY_NOXSHROOM = register(pottedPlant(block("potted_ebony_noxshroom", new FlowerPotBlock(EBONY_NOXSHROOM, pottedPlant())), false));
	public static final FlowerPotBlock POTTED_IVORY_NOXSHROOM = register(pottedPlant(block("potted_ivory_noxshroom", new FlowerPotBlock(IVORY_NOXSHROOM, pottedPlant())), false));
	public static final FlowerPotBlock POTTED_CHESTNUT_NOXSHROOM = register(pottedPlant(block("potted_chestnut_noxshroom", new FlowerPotBlock(CHESTNUT_NOXSHROOM, pottedPlant())), false));
	
	public static Settings noxcap(MapColor color) {
		return settings(color, BlockSoundGroup.NETHER_STEM, 4.0F).instrument(NoteBlockInstrument.BASS);
	}
	
	public static PillarBlock registerNoxwoodLightBlock(String name, Block gillsBlock, MapColor color) {
		return register(axisRotated(blockWithItem(name, new PillarBlock(noxcap(color).luminance(state -> 15)), InkColors.LIME), TexturedModel.makeFactory(b -> SpectrumTextureMaps.sideTopInside(b, "", b, "_top", gillsBlock, ""), SpectrumModels.MULTILAYER_LIGHT)));
	}
	
	public static <T extends FlexLanternBlock> T registerNoxwoodLantern(String name, T flexLanternBlock, InkColor color) {
		return register(cutout(blockWithItem(name, flexLanternBlock, color)).withItemModel((ctx, item) -> SpectrumModelHelper.registerItemModel(ctx, item, "_item")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.HANGING, DiagonalBlock.DIAGONAL, FlexLanternBlock.TALL).register((hanging, diagonal, tall) -> {
			String suffix = (hanging ? "_hanging" : "") + (diagonal ? "_diagonal" : "") + (tall ? "_tall" : "_small");
			return SpectrumModelHelper.createModelVariant(SpectrumModels.noxwoodLantern(suffix).upload(block, suffix, TextureMap.all(block), ctx.modelCollector));
		}))));
	}
	
	private static final int NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS = 30;
	
	public static final PillarBlock STRIPPED_SLATE_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_slate_noxcap_stem", new PillarBlock(noxcap(MapColor.GRAY)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block STRIPPED_SLATE_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_slate_noxcap_hyphae", new PillarBlock(noxcap(MapColor.GRAY)), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> STRIPPED_SLATE_NOXCAP_STEM, "", b -> STRIPPED_SLATE_NOXCAP_STEM, "")));
	public static final PillarBlock SLATE_NOXCAP_STEM = register(axisRotated(blockWithItem("slate_noxcap_stem", new StrippingLootPillarBlock(noxcap(MapColor.GRAY), STRIPPED_SLATE_NOXCAP_STEM, SpectrumLootTables.SLATE_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block SLATE_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("slate_noxcap_hyphae", new StrippingLootPillarBlock(noxcap(MapColor.GRAY), STRIPPED_SLATE_NOXCAP_HYPHAE, SpectrumLootTables.SLATE_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> SLATE_NOXCAP_STEM, "", b -> SLATE_NOXCAP_STEM, "")));
	public static final Block SLATE_NOXCAP_BLOCK = register(singleton(blockWithItem("slate_noxcap_block", new Block(noxcap(MapColor.GRAY)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final PillarBlock SLATE_NOXCAP_GILLS = register(axisRotated(blockWithItem("slate_noxcap_gills", new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(state -> 9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	public static final Block SLATE_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("slate_noxwood_pillar", new PillarBlock(noxcap(MapColor.GRAY)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block SLATE_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("slate_noxwood_lamp", new RedstoneLampBlock(noxcap(MapColor.GRAY).luminance(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final Block SLATE_NOXWOOD_LIGHT = registerNoxwoodLightBlock("slate_noxwood_light", SLATE_NOXCAP_GILLS, MapColor.GRAY);
	public static final Block SLATE_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("slate_noxwood_amphora", new AmphoraBlock(noxcap(MapColor.GRAY)), InkColors.LIME), b -> SLATE_NOXWOOD_LIGHT, "_top"));
	public static final Block SLATE_NOXWOOD_LANTERN = registerNoxwoodLantern("slate_noxwood_lantern", new FlexLanternBlock(AbstractBlock.Settings.copy(LANTERN).luminance(s -> 13).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME);
	
	public static final Block SLATE_NOXWOOD_PLANKS = register(blockWithItem("slate_noxwood_planks", new Block(noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final StairsBlock SLATE_NOXWOOD_STAIRS = register(blockWithItem("slate_noxwood_stairs", new StairsBlock(SLATE_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final SlabBlock SLATE_NOXWOOD_SLAB = register(blockWithItem("slate_noxwood_slab", new SlabBlock(noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final FenceBlock SLATE_NOXWOOD_FENCE = register(blockWithItem("slate_noxwood_fence", new FenceBlock(noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final FenceGateBlock SLATE_NOXWOOD_FENCE_GATE = register(blockWithItem("slate_noxwood_fence_gate", new FenceGateBlock(SpectrumWoodTypes.SLATE_NOXWOOD, noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final Block SLATE_NOXWOOD_DOOR = register(cutout(blockWithItem("slate_noxwood_door", new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.GRAY)), InkColors.LIME)));
	public static final Block SLATE_NOXWOOD_TRAPDOOR = register(cutout(blockWithItem("slate_noxwood_trapdoor", new TrapdoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.GRAY)), InkColors.LIME)));
	public static final Block SLATE_NOXWOOD_BUTTON = register(blockWithItem("slate_noxwood_button", new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.GRAY).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME));
	public static final Block SLATE_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("slate_noxwood_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.GRAY)), InkColors.LIME));
	public static final BlockFamily SLATE_NOXWOOD_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(SLATE_NOXWOOD_PLANKS).stairs(SLATE_NOXWOOD_STAIRS).slab(SLATE_NOXWOOD_SLAB).fence(SLATE_NOXWOOD_FENCE).fenceGate(SLATE_NOXWOOD_FENCE_GATE).door(SLATE_NOXWOOD_DOOR).trapdoor(SLATE_NOXWOOD_TRAPDOOR).button(SLATE_NOXWOOD_BUTTON).pressurePlate(SLATE_NOXWOOD_PRESSURE_PLATE).build());
	
	public static final PillarBlock STRIPPED_EBONY_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_ebony_noxcap_stem", new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block STRIPPED_EBONY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_ebony_noxcap_hyphae", new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> STRIPPED_EBONY_NOXCAP_STEM, "", b -> STRIPPED_EBONY_NOXCAP_STEM, "")));
	public static final PillarBlock EBONY_NOXCAP_STEM = register(axisRotated(blockWithItem("ebony_noxcap_stem", new StrippingLootPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK), STRIPPED_EBONY_NOXCAP_STEM, SpectrumLootTables.EBONY_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block EBONY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("ebony_noxcap_hyphae", new StrippingLootPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK), STRIPPED_EBONY_NOXCAP_HYPHAE, SpectrumLootTables.EBONY_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> EBONY_NOXCAP_STEM, "", b -> EBONY_NOXCAP_STEM, "")));
	public static final Block EBONY_NOXCAP_BLOCK = register(singleton(blockWithItem("ebony_noxcap_block", new Block(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final PillarBlock EBONY_NOXCAP_GILLS = register(axisRotated(blockWithItem("ebony_noxcap_gills", new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(state -> 9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	public static final Block EBONY_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("ebony_noxwood_pillar", new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block EBONY_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("ebony_noxwood_lamp", new RedstoneLampBlock(noxcap(MapColor.TERRACOTTA_BLACK).luminance(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final Block EBONY_NOXWOOD_LIGHT = registerNoxwoodLightBlock("ebony_noxwood_light", EBONY_NOXCAP_GILLS, MapColor.TERRACOTTA_BLACK);
	public static final Block EBONY_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("ebony_noxwood_amphora", new AmphoraBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), b -> EBONY_NOXWOOD_LIGHT, "_top"));
	public static final Block EBONY_NOXWOOD_LANTERN = registerNoxwoodLantern("ebony_noxwood_lantern", new FlexLanternBlock(AbstractBlock.Settings.copy(LANTERN).luminance(s -> 13).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME);
	
	public static final Block EBONY_NOXWOOD_PLANKS = register(blockWithItem("ebony_noxwood_planks", new Block(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final StairsBlock EBONY_NOXWOOD_STAIRS = register(blockWithItem("ebony_noxwood_stairs", new StairsBlock(EBONY_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final SlabBlock EBONY_NOXWOOD_SLAB = register(blockWithItem("ebony_noxwood_slab", new SlabBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final FenceBlock EBONY_NOXWOOD_FENCE = register(blockWithItem("ebony_noxwood_fence", new FenceBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final FenceGateBlock EBONY_NOXWOOD_FENCE_GATE = register(blockWithItem("ebony_noxwood_fence_gate", new FenceGateBlock(SpectrumWoodTypes.EBONY_NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final Block EBONY_NOXWOOD_DOOR = register(cutout(blockWithItem("ebony_noxwood_door", new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME)));
	public static final Block EBONY_NOXWOOD_TRAPDOOR = register(cutout(blockWithItem("ebony_noxwood_trapdoor", new TrapdoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME)));
	public static final Block EBONY_NOXWOOD_BUTTON = register(blockWithItem("ebony_noxwood_button", new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.TERRACOTTA_BLACK).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME));
	public static final Block EBONY_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("ebony_noxwood_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final BlockFamily EBONY_NOXWOOD_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(EBONY_NOXWOOD_PLANKS).stairs(EBONY_NOXWOOD_STAIRS).slab(EBONY_NOXWOOD_SLAB).fence(EBONY_NOXWOOD_FENCE).fenceGate(EBONY_NOXWOOD_FENCE_GATE).door(EBONY_NOXWOOD_DOOR).trapdoor(EBONY_NOXWOOD_TRAPDOOR).button(EBONY_NOXWOOD_BUTTON).pressurePlate(EBONY_NOXWOOD_PRESSURE_PLATE).build());
	
	public static final PillarBlock STRIPPED_IVORY_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_ivory_noxcap_stem", new PillarBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block STRIPPED_IVORY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_ivory_noxcap_hyphae", new PillarBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> STRIPPED_IVORY_NOXCAP_STEM, "", b -> STRIPPED_IVORY_NOXCAP_STEM, "")));
	public static final PillarBlock IVORY_NOXCAP_STEM = register(axisRotated(blockWithItem("ivory_noxcap_stem", new StrippingLootPillarBlock(noxcap(MapColor.OFF_WHITE), STRIPPED_IVORY_NOXCAP_STEM, SpectrumLootTables.IVORY_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block IVORY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("ivory_noxcap_hyphae", new StrippingLootPillarBlock(noxcap(MapColor.OFF_WHITE), STRIPPED_IVORY_NOXCAP_HYPHAE, SpectrumLootTables.IVORY_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> IVORY_NOXCAP_STEM, "", b -> IVORY_NOXCAP_STEM, "")));
	public static final Block IVORY_NOXCAP_BLOCK = register(singleton(blockWithItem("ivory_noxcap_block", new Block(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final PillarBlock IVORY_NOXCAP_GILLS = register(axisRotated(blockWithItem("ivory_noxcap_gills", new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(state -> 9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	public static final Block IVORY_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("ivory_noxwood_pillar", new PillarBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block IVORY_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("ivory_noxwood_lamp", new RedstoneLampBlock(noxcap(MapColor.OFF_WHITE).luminance(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final Block IVORY_NOXWOOD_LIGHT = registerNoxwoodLightBlock("ivory_noxwood_light", IVORY_NOXCAP_GILLS, MapColor.OFF_WHITE);
	public static final Block IVORY_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("ivory_noxwood_amphora", new AmphoraBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), b -> IVORY_NOXWOOD_LIGHT, "_top"));
	public static final Block IVORY_NOXWOOD_LANTERN = registerNoxwoodLantern("ivory_noxwood_lantern", new FlexLanternBlock(AbstractBlock.Settings.copy(LANTERN).luminance(s -> 13).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME);
	
	public static final Block IVORY_NOXWOOD_PLANKS = register(blockWithItem("ivory_noxwood_planks", new Block(noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final StairsBlock IVORY_NOXWOOD_STAIRS = register(blockWithItem("ivory_noxwood_stairs", new StairsBlock(IVORY_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final SlabBlock IVORY_NOXWOOD_SLAB = register(blockWithItem("ivory_noxwood_slab", new SlabBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final FenceBlock IVORY_NOXWOOD_FENCE = register(blockWithItem("ivory_noxwood_fence", new FenceBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final FenceGateBlock IVORY_NOXWOOD_FENCE_GATE = register(blockWithItem("ivory_noxwood_fence_gate", new FenceGateBlock(SpectrumWoodTypes.CHESTNUT_NOXWOOD, noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final Block IVORY_NOXWOOD_DOOR = register(cutout(blockWithItem("ivory_noxwood_door", new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.OFF_WHITE)), InkColors.LIME)));
	public static final Block IVORY_NOXWOOD_TRAPDOOR = register(cutout(blockWithItem("ivory_noxwood_trapdoor", new TrapdoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.OFF_WHITE)), InkColors.LIME)));
	public static final Block IVORY_NOXWOOD_BUTTON = register(blockWithItem("ivory_noxwood_button", new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.OFF_WHITE).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME));
	public static final Block IVORY_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("ivory_noxwood_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.OFF_WHITE)), InkColors.LIME));
	public static final BlockFamily IVORY_NOXWOOD_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(IVORY_NOXWOOD_PLANKS).stairs(IVORY_NOXWOOD_STAIRS).slab(IVORY_NOXWOOD_SLAB).fence(IVORY_NOXWOOD_FENCE).fenceGate(IVORY_NOXWOOD_FENCE_GATE).door(IVORY_NOXWOOD_DOOR).trapdoor(IVORY_NOXWOOD_TRAPDOOR).button(IVORY_NOXWOOD_BUTTON).pressurePlate(IVORY_NOXWOOD_PRESSURE_PLATE).build());
	
	public static final PillarBlock STRIPPED_CHESTNUT_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_chestnut_noxcap_stem", new PillarBlock(noxcap(MapColor.DULL_RED)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block STRIPPED_CHESTNUT_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_chestnut_noxcap_hyphae", new PillarBlock(noxcap(MapColor.OFF_WHITE)), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> STRIPPED_CHESTNUT_NOXCAP_STEM, "", b -> STRIPPED_CHESTNUT_NOXCAP_STEM, "")));
	public static final PillarBlock CHESTNUT_NOXCAP_STEM = register(axisRotated(blockWithItem("chestnut_noxcap_stem", new StrippingLootPillarBlock(noxcap(MapColor.DULL_RED), STRIPPED_CHESTNUT_NOXCAP_STEM, SpectrumLootTables.CHESTNUT_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block CHESTNUT_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("chestnut_noxcap_hyphae", new StrippingLootPillarBlock(noxcap(MapColor.OFF_WHITE), STRIPPED_CHESTNUT_NOXCAP_HYPHAE, SpectrumLootTables.CHESTNUT_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModels.cubeColumn(b -> CHESTNUT_NOXCAP_STEM, "", b -> CHESTNUT_NOXCAP_STEM, "")));
	public static final Block CHESTNUT_NOXCAP_BLOCK = register(singleton(blockWithItem("chestnut_noxcap_block", new Block(noxcap(MapColor.DULL_RED)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final PillarBlock CHESTNUT_NOXCAP_GILLS = register(axisRotated(blockWithItem("chestnut_noxcap_gills", new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(state -> 9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	public static final Block CHESTNUT_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("chestnut_noxwood_pillar", new PillarBlock(noxcap(MapColor.DULL_RED)), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block CHESTNUT_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("chestnut_noxwood_lamp", new RedstoneLampBlock(noxcap(MapColor.DULL_RED).luminance(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final Block CHESTNUT_NOXWOOD_LIGHT = registerNoxwoodLightBlock("chestnut_noxwood_light", CHESTNUT_NOXCAP_GILLS, MapColor.DULL_RED);
	public static final Block CHESTNUT_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("chestnut_noxwood_amphora", new AmphoraBlock(noxcap(MapColor.DULL_RED)), InkColors.LIME), b -> CHESTNUT_NOXWOOD_LIGHT, "_top"));
	public static final Block CHESTNUT_NOXWOOD_LANTERN = registerNoxwoodLantern("chestnut_noxwood_lantern", new FlexLanternBlock(AbstractBlock.Settings.copy(LANTERN).luminance(s -> 13).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME);
	
	public static final Block CHESTNUT_NOXWOOD_PLANKS = register(blockWithItem("chestnut_noxwood_planks", new Block(noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final StairsBlock CHESTNUT_NOXWOOD_STAIRS = register(blockWithItem("chestnut_noxwood_stairs", new StairsBlock(CHESTNUT_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final SlabBlock CHESTNUT_NOXWOOD_SLAB = register(blockWithItem("chestnut_noxwood_slab", new SlabBlock(noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final FenceBlock CHESTNUT_NOXWOOD_FENCE = register(blockWithItem("chestnut_noxwood_fence", new FenceBlock(noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final FenceGateBlock CHESTNUT_NOXWOOD_FENCE_GATE = register(blockWithItem("chestnut_noxwood_fence_gate", new FenceGateBlock(SpectrumWoodTypes.IVORY_NOXWOOD, noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final Block CHESTNUT_NOXWOOD_DOOR = register(cutout(blockWithItem("chestnut_noxwood_door", new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.DULL_RED)), InkColors.LIME)));
	public static final Block CHESTNUT_NOXWOOD_TRAPDOOR = register(cutout(blockWithItem("chestnut_noxwood_trapdoor", new TrapdoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.DULL_RED)), InkColors.LIME)));
	public static final Block CHESTNUT_NOXWOOD_BUTTON = register(blockWithItem("chestnut_noxwood_button", new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.DULL_RED).pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME));
	public static final Block CHESTNUT_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("chestnut_noxwood_pressure_plate", new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.DULL_RED)), InkColors.LIME));
	public static final BlockFamily CHESTNUT_NOXWOOD_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(CHESTNUT_NOXWOOD_PLANKS).stairs(CHESTNUT_NOXWOOD_STAIRS).slab(CHESTNUT_NOXWOOD_SLAB).fence(CHESTNUT_NOXWOOD_FENCE).fenceGate(CHESTNUT_NOXWOOD_FENCE_GATE).door(CHESTNUT_NOXWOOD_DOOR).trapdoor(CHESTNUT_NOXWOOD_TRAPDOOR).button(CHESTNUT_NOXWOOD_BUTTON).pressurePlate(CHESTNUT_NOXWOOD_PRESSURE_PLATE).build());
	
	public static Settings galaWood(MapColor color) {
		return settings(color, BlockSoundGroup.CHERRY_WOOD, 30.0F).instrument(NoteBlockInstrument.BASS).burnable();
	}
	
	public static final WeepingGalaSprigBlock WEEPING_GALA_SPRIG = register(cross(blockWithItem("weeping_gala_sprig", new WeepingGalaSprigBlock(copyWithMapColor(OAK_SAPLING, MapColor.BRIGHT_TEAL)), InkColors.LIME)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final FlowerPotBlock POTTED_WEEPING_GALA_SPRIG = register(pottedPlant(block("potted_weeping_gala_sprig", new FlowerPotBlock(WEEPING_GALA_SPRIG, pottedPlant())), false));
	
	public static final Block WEEPING_GALA_LEAVES = register(singleton(blockWithItem("weeping_gala_leaves", new LeavesBlock(copyWithMapColor(OAK_LEAVES, MapColor.BRIGHT_TEAL)), InkColors.LIME), TexturedModel.LEAVES));
	public static final Block WEEPING_GALA_LOG = register(burnable(log(blockWithItem("weeping_gala_log", new PillarBlock(galaWood(MapColor.BROWN)), InkColors.LIME)), 600));
	public static final Block STRIPPED_WEEPING_GALA_LOG = register(burnable(log(blockWithItem("stripped_weeping_gala_log", new PillarBlock(galaWood(MapColor.BROWN)), InkColors.LIME)), 600));
	public static final Block WEEPING_GALA_WOOD = register(burnable(wood(blockWithItem("weeping_gala_wood", new PillarBlock(galaWood(MapColor.BROWN)), InkColors.LIME), WEEPING_GALA_LOG), 600));
	public static final Block STRIPPED_WEEPING_GALA_WOOD = register(burnable(wood(blockWithItem("stripped_weeping_gala_wood", new PillarBlock(galaWood(MapColor.BROWN)), InkColors.LIME), STRIPPED_WEEPING_GALA_LOG), 600));
	
	public static final Block WEEPING_GALA_FRONDS = register(cross(block("weeping_gala_fronds", new WeepingGalaFrondsBlock(AbstractBlock.Settings.copy(WEEPING_GALA_LEAVES).noCollision()))));
	public static final Block WEEPING_GALA_FRONDS_PLANT = register(cutout(block("weeping_gala_fronds_plant", new WeepingGalaFrondsTipBlock(AbstractBlock.Settings.copy(WEEPING_GALA_LEAVES).noCollision().luminance(s -> s.get(WeepingGalaFrondsTipBlock.FORM).getLuminance())))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(WeepingGalaFrondsTipBlock.FORM)
			.register(WeepingGalaFrondsTipBlock.Form.TIP, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> WEEPING_GALA_FRONDS, "_tip").upload(block, ctx.modelCollector)))
			.register(WeepingGalaFrondsTipBlock.Form.SPRIG, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> WEEPING_GALA_FRONDS, "_sprig").upload(block, "_sprig", ctx.modelCollector)))
			.register(WeepingGalaFrondsTipBlock.Form.RESIN, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> WEEPING_GALA_FRONDS, "_sprig_resin").upload(block, "_resin", ctx.modelCollector))))));
	
	public static final BlockSetType GALA_BLOCK_SET_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.CHERRY).build(SpectrumCommon.locate("gala"));
	public static final WoodType GALA_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.CHERRY).build(SpectrumCommon.locate("gala"), GALA_BLOCK_SET_TYPE);
	
	public static final Block WEEPING_GALA_PLANKS = register(burnable(blockWithItem("weeping_gala_planks", new Block(galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final Block WEEPING_GALA_STAIRS = register(burnable(blockWithItem("weeping_gala_stairs", new StairsBlock(WEEPING_GALA_PLANKS.getDefaultState(), galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final Block WEEPING_GALA_SLAB = register(burnable(blockWithItem("weeping_gala_slab", new SlabBlock(galaWood(MapColor.BROWN)), InkColors.LIME), 300));
	public static final Block WEEPING_GALA_FENCE = register(burnable(blockWithItem("weeping_gala_fence", new FenceBlock(galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final Block WEEPING_GALA_FENCE_GATE = register(burnable(blockWithItem("weeping_gala_fence_gate", new FenceGateBlock(GALA_WOOD_TYPE, galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final Block WEEPING_GALA_DOOR = register(burnable(blockWithItem("weeping_gala_door", new DoorBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.BROWN)), InkColors.LIME), 400));
	public static final Block WEEPING_GALA_TRAPDOOR = register(burnable(blockWithItem("weeping_gala_trapdoor", new TrapdoorBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final Block WEEPING_GALA_BUTTON = register(burnable(blockWithItem("weeping_gala_button", createWoodenButtonBlock(GALA_BLOCK_SET_TYPE), InkColors.LIME), 200));
	public static final Block WEEPING_GALA_PRESSURE_PLATE = register(burnable(blockWithItem("weeping_gala_pressure_plate", new PressurePlateBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.BROWN)), InkColors.LIME), 600));
	public static final BlockFamily WEEPING_GALA_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(WEEPING_GALA_PLANKS).stairs(WEEPING_GALA_STAIRS).slab(WEEPING_GALA_SLAB).fence(WEEPING_GALA_FENCE).fenceGate(WEEPING_GALA_FENCE_GATE).door(WEEPING_GALA_DOOR).trapdoor(WEEPING_GALA_TRAPDOOR).button(WEEPING_GALA_BUTTON).pressurePlate(WEEPING_GALA_PRESSURE_PLATE).build());
	
	public static final Block WEEPING_GALA_PILLAR = register(axisRotated(blockWithItem("weeping_gala_pillar", new PillarBlock(galaWood(MapColor.BROWN)), InkColors.LIME), TexturedModel.CUBE_COLUMN));
	public static final Block WEEPING_GALA_BARREL = register(barrellike(blockWithItem("weeping_gala_barrel", new BarrelBlock(galaWood(MapColor.BROWN)), InkColors.LIME), b -> b, "_bottom"));
	public static final Block WEEPING_GALA_AMPHORA = register(barrellike(blockWithItem("weeping_gala_amphora", new AmphoraBlock(galaWood(MapColor.BROWN)), InkColors.LIME), b -> b, "_bottom"));
	public static final Block WEEPING_GALA_LANTERN = register(translucent(blockWithItem("weeping_gala_lantern", new FlexLanternBlock(galaWood(MapColor.BROWN).luminance(state -> 13).nonOpaque().pistonBehavior(PistonBehavior.DESTROY)), InkColors.LIME)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.HANGING).register(false, BlockStateVariant.create()).register(true, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R180))).coordinate(BlockStateVariantMap.create(DiagonalBlock.DIAGONAL, FlexLanternBlock.TALL).register((diagonal, tall) -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.baseTransLantern(diagonal, tall).upload(block, (diagonal ? "_diagonal" : "") + (tall ? "_tall" : "_small"), ctx.modelCollector))))).withItemModel((ctx, item) -> SpectrumModelHelper.registerItemModel(ctx, item, "_item")));
	public static final Block WEEPING_GALA_LAMP = register(redstoneLamp(blockWithItem("weeping_gala_lamp", new RedstoneLampBlock(galaWood(MapColor.BROWN).luminance(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final Block WEEPING_GALA_LIGHT = register(translucent(axisRotated(blockWithItem("weeping_gala_light", new PillarBlock(galaWood(MapColor.BROWN).luminance(state -> 15).nonOpaque()), InkColors.LIME), SpectrumTexturedModels.BASE_TRANS_LIGHT_CORE)));
	
	public static Settings basalMarble() {
		return settings(MapColor.GRAY, BlockSoundGroup.DRIPSTONE_BLOCK, 8.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresTool();
	}
	
	public static final Block BASAL_MARBLE = register(axisRotated(blockWithItem("basal_marble", new PillarBlock(basalMarble()), InkColors.BROWN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BASAL_MARBLE_STAIRS = register(blockWithItem("basal_marble_stairs", new StairsBlock(BASAL_MARBLE.getDefaultState(), basalMarble()), InkColors.BROWN));
	public static final Block BASAL_MARBLE_SLAB = register(blockWithItem("basal_marble_slab", new SlabBlock(basalMarble()), InkColors.BROWN));
	public static final Block BASAL_MARBLE_WALL = register(blockWithItem("basal_marble_wall", new WallBlock(basalMarble()), InkColors.BROWN));
	public static final BlockFamily BASAL_MARBLE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(BASAL_MARBLE).stairs(BASAL_MARBLE_STAIRS).slab(BASAL_MARBLE_SLAB).wall(BASAL_MARBLE_WALL).build(), TexturedModel.CUBE_ALL);
	
	public static final Block BASAL_MARBLE_PILLAR = register(axisRotated(blockWithItem("basal_marble_pillar", new PillarBlock(basalMarble()), InkColors.BROWN), TexturedModel.CUBE_COLUMN));
	
	public static final Block POLISHED_BASAL_MARBLE = register(defaultUpFacing(blockWithItem("polished_basal_marble", new SpectrumFacingBlock(basalMarble()), InkColors.BROWN), TexturedModel.CUBE_BOTTOM_TOP));
	public static final Block POLISHED_BASAL_MARBLE_STAIRS = register(blockWithItem("polished_basal_marble_stairs", new StairsBlock(POLISHED_BASAL_MARBLE.getDefaultState(), basalMarble()), InkColors.BROWN));
	public static final Block POLISHED_BASAL_MARBLE_SLAB = register(blockWithItem("polished_basal_marble_slab", new SlabBlock(basalMarble()), InkColors.BROWN));
	public static final Block POLISHED_BASAL_MARBLE_WALL = register(blockWithItem("polished_basal_marble_wall", new WallBlock(basalMarble()), InkColors.BROWN));
	public static final BlockFamily POLISHED_BASAL_MARBLE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(POLISHED_BASAL_MARBLE).stairs(POLISHED_BASAL_MARBLE_STAIRS).slab(POLISHED_BASAL_MARBLE_SLAB).wall(POLISHED_BASAL_MARBLE_WALL).build(), TexturedModel.makeFactory(b -> SpectrumTextureMaps.sideTopBottomWall(b, "_side", b, "_top", b, "_bottom", b, "_side"), Models.CUBE_BOTTOM_TOP));
	
	public static final Block BASAL_MARBLE_TILES = register(blockWithItem("basal_marble_tiles", new Block(basalMarble()), InkColors.BROWN));
	public static final Block BASAL_MARBLE_TILE_STAIRS = register(blockWithItem("basal_marble_tile_stairs", new StairsBlock(BASAL_MARBLE_TILES.getDefaultState(), Settings.copy(BASAL_MARBLE_TILES)), InkColors.BROWN));
	public static final Block BASAL_MARBLE_TILE_SLAB = register(blockWithItem("basal_marble_tile_slab", new SlabBlock(Settings.copy(BASAL_MARBLE_TILES)), InkColors.BROWN));
	public static final Block BASAL_MARBLE_TILE_WALL = register(blockWithItem("basal_marble_tile_wall", new WallBlock(Settings.copy(BASAL_MARBLE_TILES)), InkColors.BROWN));
	public static final BlockFamily BASAL_MARBLE_TILE_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BASAL_MARBLE_TILES).stairs(BASAL_MARBLE_TILE_STAIRS).slab(BASAL_MARBLE_TILE_SLAB).wall(BASAL_MARBLE_TILE_WALL).build());
	
	public static final Block BASAL_MARBLE_BRICKS = register(blockWithItem("basal_marble_bricks", new Block(basalMarble()), InkColors.BROWN));
	public static final Block BASAL_MARBLE_BRICK_STAIRS = register(blockWithItem("basal_marble_brick_stairs", new StairsBlock(BASAL_MARBLE_BRICKS.getDefaultState(), Settings.copy(BASAL_MARBLE_BRICKS)), InkColors.BROWN));
	public static final Block BASAL_MARBLE_BRICK_SLAB = register(blockWithItem("basal_marble_brick_slab", new SlabBlock(Settings.copy(BASAL_MARBLE_BRICKS)), InkColors.BROWN));
	public static final Block BASAL_MARBLE_BRICK_WALL = register(blockWithItem("basal_marble_brick_wall", new WallBlock(Settings.copy(BASAL_MARBLE_BRICKS)), InkColors.BROWN));
	public static final BlockFamily BASAL_MARBLE_BRICK_FAMILY = SpectrumModelHelper.registerBlockFamily(new BlockFamily.Builder(BASAL_MARBLE_BRICKS).stairs(BASAL_MARBLE_BRICK_STAIRS).slab(BASAL_MARBLE_BRICK_SLAB).wall(BASAL_MARBLE_BRICK_WALL).build());
	
	public static final Block LONGING_CHIMERA = register(cutout(defaultNorthHorizontalFacing(blockWithItem("longing_chimera", new GrotesqueBlock(basalMarble().nonOpaque(), 12, 15, "block.spectrum.longing_chimera.tooltip"), InkColors.BROWN), ModelIds::getBlockModelId)));
	
	public static SmallDragonjagBlock registerSmallDragonjagBlock(String name, Dragonjag.Variant variant) {
		return register(cutout(singleton(blockWithItem(name, new SmallDragonjagBlock(settings(variant.getMapColor(), BlockSoundGroup.GRASS, 1.0F), variant), InkColors.LIME), SpectrumTexturedModels.doubleCross(b -> b, ""))).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel));
	}
	
	public static final Block SMALL_RED_DRAGONJAG = registerSmallDragonjagBlock("small_red_dragonjag", Dragonjag.Variant.RED);
	public static final Block SMALL_YELLOW_DRAGONJAG = registerSmallDragonjagBlock("small_yellow_dragonjag", Dragonjag.Variant.YELLOW);
	public static final Block SMALL_PINK_DRAGONJAG = registerSmallDragonjagBlock("small_pink_dragonjag", Dragonjag.Variant.PINK);
	public static final Block SMALL_PURPLE_DRAGONJAG = registerSmallDragonjagBlock("small_purple_dragonjag", Dragonjag.Variant.PURPLE);
	public static final Block SMALL_BLACK_DRAGONJAG = registerSmallDragonjagBlock("small_black_dragonjag", Dragonjag.Variant.BLACK);
	
	public static TallDragonjagBlock registerTallDragonjagBlock(String name, Dragonjag.Variant variant) {
		return register(cutout(block(name, new TallDragonjagBlock(settings(variant.getMapColor(), BlockSoundGroup.GRASS, 1.0F), variant))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(TallPlantBlock.HALF, TallDragonjagBlock.DEAD).register((half, dead) -> {
			String suffix = (half == DoubleBlockHalf.UPPER ? "_top" : "_bottom") + (dead ? "_dead" : "");
			return SpectrumModelHelper.createModelVariant((half == DoubleBlockHalf.UPPER ? SpectrumTexturedModels.cross(b -> b, suffix) : SpectrumTexturedModels.doubleCross(b -> b, suffix)).upload(block, suffix, ctx.modelCollector));
		}))));
	}
	
	public static final Block TALL_YELLOW_DRAGONJAG = registerTallDragonjagBlock("tall_yellow_dragonjag", Dragonjag.Variant.YELLOW);
	public static final Block TALL_RED_DRAGONJAG = registerTallDragonjagBlock("tall_red_dragonjag", Dragonjag.Variant.RED);
	public static final Block TALL_PINK_DRAGONJAG = registerTallDragonjagBlock("tall_pink_dragonjag", Dragonjag.Variant.PINK);
	public static final Block TALL_PURPLE_DRAGONJAG = registerTallDragonjagBlock("tall_purple_dragonjag", Dragonjag.Variant.PURPLE);
	public static final Block TALL_BLACK_DRAGONJAG = registerTallDragonjagBlock("tall_black_dragonjag", Dragonjag.Variant.BLACK);
	
	//Flora
	public static final Block ALOE = register(cutout(block("aloe", new AloeBlock(settings(MapColor.DARK_GREEN, BlockSoundGroup.GRASS, 1.0F).noCollision().ticksRandomly().nonOpaque()))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_4).register(age -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, age.toString()).upload(block, age.toString(), ctx.modelCollector))))));
	public static final Block SAWBLADE_HOLLY_BUSH = register(cutout(block("sawblade_holly_bush", new SawbladeHollyBushBlock(settings(MapColor.TERRACOTTA_GREEN, BlockSoundGroup.GRASS, 0.0F).noCollision().ticksRandomly().nonOpaque().luminance(s -> s.get(SawbladeHollyBushBlock.AGE) == SawbladeHollyBushBlock.MAX_AGE ? 10 : 0)))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_7)
			.register(0, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "0").upload(block, "_stage0", ctx.modelCollector)))
			.register(1, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "1").upload(block, "_stage1", ctx.modelCollector)))
			.register(2, SpectrumModelHelper.createModelVariant(block, "_stage1"))
			.register(3, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "2").upload(block, "_stage2", ctx.modelCollector)))
			.register(4, SpectrumModelHelper.createModelVariant(block, "_stage2")).register(5, SpectrumModelHelper.createModelVariant(block, "_stage2"))
			.register(6, SpectrumModelHelper.createModelVariant(block, "_stage2"))
			.register(7, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "3").upload(block, "_stage3", ctx.modelCollector))))));
	public static final Block BRISTLE_SPROUTS = register(cutout(blockWithItem("bristle_sprouts", new BristleSproutsBlock(settings(MapColor.PALE_GREEN, BlockSoundGroup.GRASS, 0.0F).noCollision().nonOpaque().offset(OffsetType.XZ).replaceable()), InkColors.LIME)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_1")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_1").upload(block, "_1", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_2").upload(block, "_2", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_3").upload(block, "_3", ctx.modelCollector)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, "_4").upload(block, "_4", ctx.modelCollector)))));
	public static final Block DOOMBLOOM = register(cutout(block("doombloom", new DoomBloomBlock(SpectrumStatusEffects.STIFFNESS, 8, settings(MapColor.PALE_GREEN, BlockSoundGroup.GRASS, 0.0F).ticksRandomly().noCollision().luminance((state) -> state.get(DoomBloomBlock.AGE) * 2).nonOpaque()))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_4).register(age -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, age.toString()).upload(block, age.toString(), ctx.modelCollector))))));
	public static final Block SNAPPING_IVY = register(cutout(blockWithItem("snapping_ivy", new SnappingIvyBlock(settings(MapColor.PALE_GREEN, BlockSoundGroup.GRASS, 3.0F).noCollision().nonOpaque()), InkColors.RED)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(SnappingIvyBlock.SNAPPED, ModelIds.getBlockSubModelId(block, "_snapped"), ModelIds.getBlockModelId(block))).coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_AXIS).register(Direction.Axis.X, BlockStateVariant.create()).register(Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90)))));
	
	public static final Block ABYSSAL_VINES = register(cutout(block("abyssal_vines", new AbyssalVineBlock(settings(MapColor.DARK_GREEN, BlockSoundGroup.CAVE_VINES, 2.0F).noCollision().offset(OffsetType.XYZ).ticksRandomly().nonOpaque().luminance(state -> state.get(Properties.BERRIES) ? 13 : 0)))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(TriStateVineBlock.LIFE_STAGE, AbyssalVineBlock.BERRIES).register((stage, berries) -> {
		String suffix = (stage == TriStateVineBlock.LifeStage.STALK ? "" : "_tip") + (berries ? "_fruiting" : "");
		if (stage == TriStateVineBlock.LifeStage.MATURE) return SpectrumModelHelper.createModelVariant(block, suffix);
		return SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, suffix).upload(block, suffix, ctx.modelCollector));
	}))));
	public static final Block NIGHTDEW = register(cutout(block("nightdew", new NightdewBlock(settings(MapColor.TEAL, BlockSoundGroup.CAVE_VINES, 0.0F).noCollision().offset(OffsetType.XYZ).ticksRandomly().nonOpaque().breakInstantly()))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(TriStateVineBlock.LIFE_STAGE).register(stage -> {
		String suffix = (stage == TriStateVineBlock.LifeStage.STALK ? "" : "_tip");
		if (stage == TriStateVineBlock.LifeStage.MATURE) return SpectrumModelHelper.createModelVariant(block, suffix);
		return SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, suffix).upload(block, suffix, ctx.modelCollector));
	}))));
	public static final Block SWEET_PEA = register(simplePlant(blockWithItem("sweet_pea", new FlowerBlock(StatusEffects.NIGHT_VISION, 5, settings(MapColor.MAGENTA, BlockSoundGroup.GRASS, 0.0F).offset(OffsetType.XZ).noCollision().nonOpaque().luminance(s -> 11).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), InkColors.YELLOW)));
	public static final Block APRICOTTI = register(simplePlant(blockWithItem("apricotti", new FlowerBlock(StatusEffects.GLOWING, 5, settings(MapColor.ORANGE, BlockSoundGroup.GRASS, 0.0F).offset(OffsetType.XZ).noCollision().nonOpaque().luminance(s -> 11).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), InkColors.YELLOW)));
	public static final Block HUMMING_BELL = register(simplePlant(blockWithItem("humming_bell", new FlowerBlock(SpectrumStatusEffects.LIGHTWEIGHT, 5, settings(MapColor.LIGHT_BLUE, BlockSoundGroup.GRASS, 0.0F).offset(OffsetType.XZ).noCollision().nonOpaque().luminance(s -> 9).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), InkColors.LIME)));
	
	public static final Block HUMMINGSTONE_GLASS = register(translucent(simple(blockWithItem("hummingstone_glass", new TransparentBlock(settings(MapColor.PALE_YELLOW, BlockSoundGroup.GLASS, 5.0F, 100.0F).nonOpaque().requiresTool()), InkColors.LIGHT_BLUE))));
	public static final Block HUMMINGSTONE_GLASS_PANE = register(glassPane(blockWithItem("hummingstone_glass_pane", new PaneBlock(Settings.copy(HUMMINGSTONE_GLASS)), InkColors.LIGHT_BLUE), HUMMINGSTONE_GLASS));
	public static final Block HUMMINGSTONE = register(translucent(blockWithItem("hummingstone", new HummingstoneBlock(Settings.copy(HUMMINGSTONE_GLASS).luminance((state) -> 14)), InkColors.LIGHT_BLUE)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(HummingstoneBlock.HUMMING, SpectrumTexturedModels.cubeAll(b -> b, "_humming").upload(block, "_humming", ctx.modelCollector), TexturedModel.CUBE_ALL.upload(block, ctx.modelCollector)))));
	public static final Block WAXED_HUMMINGSTONE = register(translucent(parented(blockWithItem("waxed_hummingstone", new TransparentBlock(Settings.copy(HUMMINGSTONE)), InkColors.LIGHT_BLUE), b -> HUMMINGSTONE)));
	
	public static final Block MOSS_BALL = register(cutout(blockWithItem("moss_ball", new MossBallBlock(settings(MapColor.DARK_GREEN, BlockSoundGroup.WET_GRASS, 1F).noCollision().nonOpaque().offset(OffsetType.XYZ)), InkColors.GREEN)).withBlockModel((ctx, block) -> {
		List<BlockStateVariant> variants = new ArrayList<>(SpectrumModelHelper.createHorizontalRotationVariantList(ModelIds.getBlockSubModelId(block, "_tuft")));
		variants.add(SpectrumModelHelper.createModelVariant(block, "").put(VariantSettings.WEIGHT, 4));
		return VariantsBlockStateSupplier.create(block, variants.toArray(BlockStateVariant[]::new));
	}));
	public static final Block GIANT_MOSS_BALL = register(blockWithItem("giant_moss_ball", new GiantMossBallBlock(settings(MapColor.DARK_GREEN, BlockSoundGroup.WET_GRASS, 10F).noCollision().nonOpaque().offset(OffsetType.XYZ)), InkColors.GREEN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelIds.getBlockModelId(block))));
	
	public static final Block RESPLENDENT_BLOCK = register(defaultUpFacing(blockWithItem("resplendent_block", new CushionedFacingBlock(Settings.copy(RED_WOOL)), IS.of(Rarity.UNCOMMON), InkColors.YELLOW), TexturedModel.CUBE_BOTTOM_TOP));
	public static final Block RESPLENDENT_CUSHION = register(singleton(blockWithItem("resplendent_cushion", new CushionBlock(Settings.copy(RESPLENDENT_BLOCK).nonOpaque().allowsSpawning(SpectrumBlocks::never)), IS.of(Rarity.UNCOMMON), InkColors.YELLOW), SpectrumTexturedModels.CUSHION));
	public static final Block RESPLENDENT_CARPET = register(singleton(blockWithItem("resplendent_carpet", new CushionedCarpetBlock(Settings.copy(RED_CARPET)), IS.of(Rarity.UNCOMMON), InkColors.YELLOW), TexturedModel.CARPET));
	public static final Block RESPLENDENT_BED = register(cutout(blockWithItem("resplendent_bed", new SpectrumBedBlock(DyeColor.RED, Settings.copy(RED_BED)), IS.of(1, Rarity.UNCOMMON), InkColors.YELLOW)).withPredefinedItemModel().withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createSouthDefaultHorizontalFacingVariantMap()).coordinate(BlockStateVariantMap.create(BedBlock.PART).register(BedPart.HEAD, SpectrumModelHelper.createModelVariant(block, "_head")).register(BedPart.FOOT, SpectrumModelHelper.createModelVariant(block, "_foot")))));
	
	// JADE VINES
	public static Settings jadeVine() {
		return settings(MapColor.PALE_GREEN, BlockSoundGroup.WOOL, 0.1F).noCollision().nonOpaque();
	}
	
	public static final Block JADE_VINE_ROOTS = register(cutout(block("jade_vine_roots", new JadeVineRootsBlock(jadeVine().ticksRandomly().luminance((state) -> state.get(JadeVineRootsBlock.DEAD) ? 0 : 4)))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(JadeVineBulbBlock.DEAD, SpectrumModels.JADE_VINE_ROOTS.upload(block, "_dead", SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelCollector), SpectrumModels.JADE_VINE_ROOTS.upload(block, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelCollector)))));
	public static final Block JADE_VINE_BULB = register(cutout(block("jade_vine_bulb", new JadeVineBulbBlock(jadeVine().luminance((state) -> state.get(JadeVineBulbBlock.DEAD) ? 0 : 5)))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(JadeVineBulbBlock.DEAD, SpectrumModels.JADE_VINE_BULB.upload(block, "_dead", SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE_BULB, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelCollector), SpectrumModels.JADE_VINE_BULB.upload(block, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_BULB, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelCollector)))));
	public static final Block JADE_VINES = register(cutout(block("jade_vines", new JadeVinePlantBlock(jadeVine().luminance((state) -> state.get(JadeVinePlantBlock.AGE) == 0 ? 0 : 5)))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_7, JadeVinePlantBlock.PART).register((age, part) -> {
		Model model = SpectrumModels.jadeVines(part);
		String suffix = "_" + part.asString() + (age == 0 ? "_dead" : age <= 2 ? "_leaves" : age <= 6 ? "_petals" : "_bloom");
		if (age == 0) return SpectrumModelHelper.createModelVariant(model.upload(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelCollector));
		if (age == 1) return SpectrumModelHelper.createModelVariant(model.upload(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelCollector));
		if (age == 3) return SpectrumModelHelper.createModelVariant(model.upload(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_PETALS, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelCollector));
		if (age == 7) return SpectrumModelHelper.createModelVariant(model.upload(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_BLOOMING, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelCollector));
		return SpectrumModelHelper.createModelVariant(block, suffix);
	}))));
	public static final Block JADE_VINE_PETAL_BLOCK = register(cutout(simple(blockWithItem("jade_vine_petal_block", new JadeVinePetalBlock(jadeVine().luminance(state -> 3)), InkColors.LIME))));
	public static final Block JADE_VINE_PETAL_CARPET = register(cutout(singleton(blockWithItem("jade_vine_petal_carpet", new CarpetBlock(jadeVine().luminance(state -> 3)), InkColors.LIME), SpectrumTexturedModels.carpet(b -> JADE_VINE_PETAL_BLOCK, ""))));
	
	public static final Block NEPHRITE_BLOSSOM_STEM = register(cutout(blockWithItem("nephrite_blossom_stem", new NephriteBlossomStemBlock(settings(MapColor.PINK, BlockSoundGroup.WOOL, 2.0F).nonOpaque().noCollision()), InkColors.PINK)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_bottom")).withBlockModel((ctx, block) -> {
		Identifier bottom = SpectrumTexturedModels.cross(b -> b, "_bottom").upload(block, "_bottom", ctx.modelCollector);
		Identifier top = SpectrumTexturedModels.cross(b -> b, "_top").upload(block, "_top", ctx.modelCollector);
		Identifier fronds = ModelIds.getBlockSubModelId(block, "_base");
		return MultipartBlockStateSupplier.create(block)
				.with(When.create().set(NephriteBlossomStemBlock.STEM_PART, StemComponent.STEM), SpectrumModelHelper.createModelVariant(bottom))
				.with(When.create().set(NephriteBlossomStemBlock.STEM_PART, StemComponent.STEMALT), SpectrumModelHelper.createModelVariant(top))
				.with(When.create().set(NephriteBlossomStemBlock.STEM_PART, StemComponent.BASE), SpectrumModelHelper.createModelVariant(fronds))
				.with(When.create().set(NephriteBlossomStemBlock.STEM_PART, StemComponent.BASE), SpectrumModelHelper.createModelVariant(bottom));
	}));
	public static final Block NEPHRITE_BLOSSOM_LEAVES = register(cutout(blockWithItem("nephrite_blossom_leaves", new NephriteBlossomLeavesBlock(settings(MapColor.PINK, BlockSoundGroup.GRASS, 0.2F).nonOpaque().ticksRandomly().luminance(state -> 13)), InkColors.PINK)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_2).register(age -> {
		String suffix = age == 0 ? "" : age == 1 ? "_flowering" : "_fruiting";
		return SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.leaves(b -> b, suffix).upload(block, suffix, ctx.modelCollector));
	}))));
	public static final Block NEPHRITE_BLOSSOM_BULB = register(cross(blockWithItem("nephrite_blossom_bulb", new NephriteBlossomBulbBlock(AbstractBlock.Settings.copy(NEPHRITE_BLOSSOM_STEM)), IS.of(16), InkColors.PINK)).withItemModel(SpectrumModelHelper::registerItemModel));
	
	public static Settings jadeite() {
		return settings(MapColor.WHITE_GRAY, BlockSoundGroup.WOOL, 0.1F).noCollision().nonOpaque().luminance(state -> 12).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always);
	}
	
	public static final Block JADEITE_LOTUS_STEM = register(cutout(blockWithItem("jadeite_lotus_stem", new JadeiteLotusStemBlock(settings(MapColor.BLACK, BlockSoundGroup.WOOL, 2.0F).nonOpaque().noCollision()), InkColors.LIME)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_top")).withBlockModel((ctx, block) -> {
		Identifier bottom = SpectrumTexturedModels.cross(b -> b, "_bottom").upload(block, "_bottom", ctx.modelCollector);
		Identifier top = SpectrumTexturedModels.cross(b -> b, "_top").upload(block, "_top", ctx.modelCollector);
		Identifier base = ModelIds.getBlockSubModelId(block, "_base");
		return MultipartBlockStateSupplier.create(block).with(When.create().set(JadeiteLotusStemBlock.STEM_PART, StemComponent.STEM), SpectrumModelHelper.createModelVariant(bottom)).with(When.create().set(JadeiteLotusStemBlock.STEM_PART, StemComponent.STEMALT), SpectrumModelHelper.createModelVariant(top)).with(When.create().set(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE), SpectrumModelHelper.createModelVariant(bottom)).with(When.create().set(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE).set(Properties.INVERTED, false), SpectrumModelHelper.createModelVariant(base)).with(When.create().set(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE).set(Properties.INVERTED, true), SpectrumModelHelper.createModelVariant(base).put(VariantSettings.X, VariantSettings.Rotation.R180));
	}));
	public static final Block JADEITE_LOTUS_FLOWER = register(cutout(defaultUpFacing(blockWithItem("jadeite_lotus_flower", new JadeiteLotusFlowerBlock(settings(MapColor.WHITE, BlockSoundGroup.WOOL, 2.0F).luminance(state -> 14).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always)), IS.of(16), InkColors.LIME), ModelIds::getBlockModelId)));
	public static final Block JADEITE_LOTUS_BULB = register(cross(blockWithItem("jadeite_lotus_bulb", new JadeiteLotusBulbBlock(AbstractBlock.Settings.copy(JADEITE_LOTUS_STEM).nonOpaque()), IS.of(16), InkColors.LIME)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final Block JADEITE_PETAL_BLOCK = register(cutout(simple(blockWithItem("jadeite_petal_block", new JadeVinePetalBlock(jadeite()), InkColors.LIME))));
	public static final Block JADEITE_PETAL_CARPET = register(cutout(singleton(blockWithItem("jadeite_petal_carpet", new CarpetBlock(jadeite()), InkColors.LIME), SpectrumTexturedModels.carpet(b -> JADEITE_PETAL_BLOCK, ""))));
	
	private static Settings ore() {
		return AbstractBlock.Settings.copy(IRON_ORE);
	}
	
	private static Settings deepslateOre() {
		return AbstractBlock.Settings.copy(DEEPSLATE_IRON_ORE);
	}
	
	private static Settings blackslagOre() {
		return AbstractBlock.Settings.copy(BLACKSLAG).strength(BLACKSLAG_HARDNESS * 1.5F, BLACKSLAG_RESISTANCE * 2F).requiresTool();
	}
	
	private static Settings netherrackOre() {
		return AbstractBlock.Settings.copy(NETHERRACK).strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE).requiresTool();
	}
	
	private static Settings endstoneOre() {
		return AbstractBlock.Settings.copy(END_STONE).strength(3.0F, 3.0F).requiresTool();
	}
	
	public static final Block SHIMMERSTONE_ORE = register(simple(blockWithItem("shimmerstone_ore", new ShimmerstoneOreBlock(UniformIntProvider.create(2, 4), ore().ticksRandomly(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, STONE.getDefaultState()), InkColors.YELLOW)));
	public static final Block DEEPSLATE_SHIMMERSTONE_ORE = register(simple(blockWithItem("deepslate_shimmerstone_ore", new ShimmerstoneOreBlock(UniformIntProvider.create(2, 4), deepslateOre().ticksRandomly(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, DEEPSLATE.getDefaultState()), InkColors.YELLOW)));
	public static final Block BLACKSLAG_SHIMMERSTONE_ORE = register(singleton(blockWithItem("blackslag_shimmerstone_ore", new ShimmerstoneOreBlock(UniformIntProvider.create(2, 4), blackslagOre().ticksRandomly(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, BLACKSLAG.getDefaultState()), InkColors.YELLOW), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block SHIMMERSTONE_BLOCK = register(simple(blockWithItem("shimmerstone_block", new ShimmerstoneBlock(settings(MapColor.YELLOW, BlockSoundGroup.GLASS, 2.0F).luminance((state) -> 15)), InkColors.YELLOW)));
	
	public static final AzuriteOreBlock AZURITE_ORE = register(simpleMirrored(blockWithItem("azurite_ore", new AzuriteOreBlock(UniformIntProvider.create(4, 7), ore().ticksRandomly(), SpectrumAdvancements.REVEAL_AZURITE, STONE.getDefaultState()), InkColors.BLUE)));
	public static final Block DEEPSLATE_AZURITE_ORE = register(simpleMirrored(blockWithItem("deepslate_azurite_ore", new AzuriteOreBlock(UniformIntProvider.create(4, 7), deepslateOre().ticksRandomly(), SpectrumAdvancements.REVEAL_AZURITE, DEEPSLATE.getDefaultState()), InkColors.BLUE)));
	public static final Block BLACKSLAG_AZURITE_ORE = register(simpleMirrored(blockWithItem("blackslag_azurite_ore", new AzuriteOreBlock(UniformIntProvider.create(4, 7), blackslagOre().ticksRandomly(), SpectrumAdvancements.REVEAL_AZURITE, BLACKSLAG.getDefaultState()), InkColors.BLUE)));
	public static final Block AZURITE_BLOCK = register(defaultUpFacing(blockWithItem("azurite_block", new SpectrumFacingBlock(AbstractBlock.Settings.copy(LAPIS_BLOCK).mapColor(MapColor.BLUE)), InkColors.BLUE), TexturedModel.CUBE_BOTTOM_TOP));
	public static final Block AZURITE_CLUSTER = register(cluster(blockWithItem("azurite_cluster", new SpectrumClusterBlock(gemstone(MapColor.BLUE, SpectrumBlockSoundGroups.SMALL_ONYX_BUD, 2), SpectrumClusterBlock.GrowthStage.CLUSTER), IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_AZURITE_BUD = register(cluster(blockWithItem("large_azurite_bud", new SpectrumClusterBlock(gemstone(MapColor.BLUE, SpectrumBlockSoundGroups.LARGE_ONYX_BUD, 3), SpectrumClusterBlock.GrowthStage.LARGE), IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_AZURITE_BUD = register(cluster(blockWithItem("small_azurite_bud", new SpectrumClusterBlock(gemstone(MapColor.BLUE, SpectrumBlockSoundGroups.ONYX_CLUSTER, 5), SpectrumClusterBlock.GrowthStage.SMALL), IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static final Block MALACHITE_ORE = register(simple(blockWithItem("malachite_ore", new CloakedOreBlock(UniformIntProvider.create(7, 11), ore(), SpectrumAdvancements.REVEAL_MALACHITE, STONE.getDefaultState()), IS.of(Rarity.UNCOMMON), InkColors.GREEN)));
	public static final Block DEEPSLATE_MALACHITE_ORE = register(simple(blockWithItem("deepslate_malachite_ore", new CloakedOreBlock(UniformIntProvider.create(7, 11), deepslateOre(), SpectrumAdvancements.REVEAL_MALACHITE, DEEPSLATE.getDefaultState()), IS.of(Rarity.UNCOMMON), InkColors.GREEN)));
	public static final Block BLACKSLAG_MALACHITE_ORE = register(singleton(blockWithItem("blackslag_malachite_ore", new CloakedOreBlock(UniformIntProvider.create(7, 11), blackslagOre(), SpectrumAdvancements.REVEAL_MALACHITE, BLACKSLAG.getDefaultState()), IS.of(Rarity.UNCOMMON), InkColors.GREEN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block MALACHITE_BLOCK = register(defaultUpFacing(blockWithItem("malachite_block", new SpectrumFacingBlock(gemstoneBlock(MapColor.EMERALD_GREEN, BlockSoundGroup.CHAIN)), IS.of(Rarity.UNCOMMON), InkColors.GREEN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block MALACHITE_CLUSTER = register(cluster(blockWithItem("malachite_cluster", new SpectrumClusterBlock(gemstone(MapColor.EMERALD_GREEN, BlockSoundGroup.CHAIN, 9), SpectrumClusterBlock.GrowthStage.CLUSTER), IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_MALACHITE_BUD = register(cluster(blockWithItem("large_malachite_bud", new SpectrumClusterBlock(gemstone(MapColor.EMERALD_GREEN, BlockSoundGroup.CHAIN, 7), SpectrumClusterBlock.GrowthStage.LARGE), IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_MALACHITE_BUD = register(cluster(blockWithItem("small_malachite_bud", new SpectrumClusterBlock(gemstone(MapColor.EMERALD_GREEN, BlockSoundGroup.CHAIN, 5), SpectrumClusterBlock.GrowthStage.SMALL), IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static final Block BLOODSTONE_BLOCK = register(defaultUpFacing(blockWithItem("bloodstone_block", new SpectrumFacingBlock(gemstoneBlock(MapColor.RED, SpectrumBlockSoundGroups.ONYX_CLUSTER)), IS.of(Rarity.UNCOMMON), InkColors.RED), TexturedModel.CUBE_COLUMN));
	public static final Block BLOODSTONE_CLUSTER = register(cluster(blockWithItem("bloodstone_cluster", new SpectrumClusterBlock(gemstone(MapColor.RED, SpectrumBlockSoundGroups.SMALL_ONYX_BUD, 6), SpectrumClusterBlock.GrowthStage.CLUSTER), IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_BLOODSTONE_BUD = register(cluster(blockWithItem("large_bloodstone_bud", new SpectrumClusterBlock(gemstone(MapColor.RED, SpectrumBlockSoundGroups.SMALL_ONYX_BUD, 4), SpectrumClusterBlock.GrowthStage.LARGE), IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_BLOODSTONE_BUD = register(cluster(blockWithItem("small_bloodstone_bud", new SpectrumClusterBlock(gemstone(MapColor.RED, SpectrumBlockSoundGroups.ONYX_CLUSTER, 3), SpectrumClusterBlock.GrowthStage.SMALL), IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static final Block STRATINE_ORE = register(simple(blockWithItem("stratine_ore", new CloakedOreBlock(UniformIntProvider.create(3, 5), netherrackOre(), SpectrumAdvancements.REVEAL_STRATINE, NETHERRACK.getDefaultState()), block -> new FloatBlockItem(block, IS.of().fireproof(), -0.01F), InkColors.RED)));
	public static final Block PALTAERIA_ORE = register(simple(blockWithItem("paltaeria_ore", new CloakedOreBlock(UniformIntProvider.create(2, 4), endstoneOre(), SpectrumAdvancements.REVEAL_PALTAERIA, END_STONE.getDefaultState()), block -> new FloatBlockItem(block, IS.of(), 0.01F), InkColors.CYAN)));
	
	private static Settings gravityBlock(MapColor mapColor) {
		return settings(mapColor, BlockSoundGroup.METAL, 4.0F, 6.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresTool();
	}
	
	public static final FloatBlock PALTAERIA_FLOATBLOCK = register(singleton(blockWithItem("paltaeria_floatblock", new FloatBlock(gravityBlock(MapColor.LIGHT_BLUE), 0.2F), block -> new FloatBlockItem(block, IS.of().fireproof(), -0.02F), InkColors.RED), SpectrumTexturedModels.cubeBottomTop(b -> b, "", b -> b, "_top", b -> b, "_bottom")));
	public static final FloatBlock STRATINE_FLOATBLOCK = register(singleton(blockWithItem("stratine_floatblock", new FloatBlock(gravityBlock(MapColor.DARK_RED), -0.2F), block -> new FloatBlockItem(block, IS.of(), 0.02F), InkColors.CYAN), SpectrumTexturedModels.cubeBottomTop(b -> b, "", b -> b, "_top", b -> b, "_bottom")));
	public static final FloatBlock HOVER_BLOCK = register(singleton(blockWithItem("hover_block", new FloatBlock(gravityBlock(MapColor.DIAMOND_BLUE), 0.0F), block -> new FloatBlockItem(block, IS.of(), 0F) {
		@Override
		public double applyGravity(ItemStack stack, World world, Entity entity) {
			return 0;
		}
		
		@Override
		public void applyGravity(ItemStack stack, World world, ItemEntity itemEntity) {
			itemEntity.setNoGravity(true);
		}
	}, InkColors.GREEN), TexturedModel.CUBE_COLUMN));
	
	public static final Block BLACKSLAG_COAL_ORE = register(singleton(blockWithItem("blackslag_coal_ore", new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), blackslagOre()), InkColors.BLACK), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_COPPER_ORE = register(singleton(blockWithItem("blackslag_copper_ore", new ExperienceDroppingBlock(ConstantIntProvider.create(0), blackslagOre()), InkColors.BLACK), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_IRON_ORE = register(singleton(blockWithItem("blackslag_iron_ore", new ExperienceDroppingBlock(ConstantIntProvider.create(0), blackslagOre()), InkColors.BROWN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_GOLD_ORE = register(singleton(blockWithItem("blackslag_gold_ore", new ExperienceDroppingBlock(ConstantIntProvider.create(0), blackslagOre()), InkColors.YELLOW), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_LAPIS_ORE = register(singleton(blockWithItem("blackslag_lapis_ore", new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), blackslagOre()), InkColors.BLUE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_DIAMOND_ORE = register(singleton(blockWithItem("blackslag_diamond_ore", new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), blackslagOre()), InkColors.LIGHT_BLUE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_REDSTONE_ORE = register(singleton(blockWithItem("blackslag_redstone_ore", new RedstoneOreBlock(blackslagOre().ticksRandomly().luminance(createLightLevelFromLitBlockState(9))), InkColors.RED), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_EMERALD_ORE = register(singleton(blockWithItem("blackslag_emerald_ore", new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), blackslagOre()), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	// FUNCTIONAL BLOCKS
	public static final Block HEARTBOUND_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("heartbound_chest", new HeartboundChestBlock(settings(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.STONE, -1.0F, 3600000.0F).requiresTool().nonOpaque()), InkColors.BLUE), ModelIds::getBlockModelId));
	public static final Block COMPACTING_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("compacting_chest", new CompactingChestBlock(settings(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.STONE, 4.0F, 4.0F).requiresTool().nonOpaque()), InkColors.YELLOW), ModelIds::getBlockModelId));
	public static final Block FABRICATION_CHEST = register(cutout(defaultNorthHorizontalFacing(blockWithItem("fabrication_chest", new FabricationChestBlock(settings(MapColor.ORANGE, BlockSoundGroup.STONE, 4.0F, 4.0F).requiresTool().nonOpaque()), InkColors.YELLOW), ModelIds::getBlockModelId)).withPredefinedItemModel());
	public static final Block BLACK_HOLE_CHEST = register(translucent(defaultNorthHorizontalFacing(blockWithItem("black_hole_chest", new BlackHoleChestBlock(settings(MapColor.BLACK, BlockSoundGroup.STONE, 4.0F, 4.0F).requiresTool().nonOpaque()), InkColors.LIGHT_GRAY), ModelIds::getBlockModelId)));
	public static final Block PARTICLE_SPAWNER = register(cutout(blockWithItem("particle_spawner", new ParticleSpawnerBlock(settings(MapColor.TERRACOTTA_WHITE, BlockSoundGroup.AMETHYST_BLOCK, 5.0F, 6.0F).requiresTool().nonOpaque()), InkColors.PINK)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_off")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.POWERED, SpectrumModels.PARTICLE_SPAWNER.upload(block, SpectrumTextureMaps.top(block, "_top"), ctx.modelCollector), SpectrumModels.PARTICLE_SPAWNER.upload(block, "_off", SpectrumTextureMaps.top(block, "_top_off"), ctx.modelCollector)))));
	public static final Block CREATIVE_PARTICLE_SPAWNER = register(cutout(singleton(blockWithItem("creative_particle_spawner", new CreativeParticleSpawnerBlock(AbstractBlock.Settings.copy(PARTICLE_SPAWNER).strength(-1.0F, 3600000.8F).dropsNothing()), block -> new BlockItem(block, IS.of(Rarity.EPIC)), InkColors.PINK), (Function<Block, Identifier>) b -> ModelIds.getBlockModelId(PARTICLE_SPAWNER))).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, PARTICLE_SPAWNER, "_off")));
	public static final Block BEDROCK_ANVIL = register(defaultSouthHorizontalFacing(blockWithItem("bedrock_anvil", new BedrockAnvilBlock(AbstractBlock.Settings.copy(ANVIL).requiresTool().strength(8.0F, 8.0F).sounds(BlockSoundGroup.METAL)), InkColors.BLACK), ModelIds::getBlockModelId));
	
	// SOLID LIQUID CRYSTAL
	public static final Block FROSTBITE_CRYSTAL = register(simple(blockWithItem("frostbite_crystal", new Block(AbstractBlock.Settings.copy(GLOWSTONE).mapColor(MapColor.LIGHT_BLUE_GRAY)), InkColors.LIGHT_BLUE)));
	public static final Block BLAZING_CRYSTAL = register(simple(blockWithItem("blazing_crystal", new Block(AbstractBlock.Settings.copy(GLOWSTONE).mapColor(MapColor.ORANGE)), IS.of().fireproof(), InkColors.ORANGE)));
	
	public static final Block QUITOXIC_REEDS = register(cross(blockWithItem("quitoxic_reeds", new QuitoxicReedsBlock(settings(MapColor.CLEAR, BlockSoundGroup.GRASS, 0.0F).noCollision().offset(AbstractBlock.OffsetType.XYZ).ticksRandomly().luminance(state -> state.get(QuitoxicReedsBlock.LOGGED).getLuminance())), InkColors.PURPLE)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final Block MERMAIDS_BRUSH = register(cutout(block("mermaids_brush", new MermaidsBrushBlock(settings(MapColor.CLEAR, BlockSoundGroup.WET_GRASS, 0.0F).noCollision().ticksRandomly().luminance(state -> state.get(MermaidsBrushBlock.LOGGED).getLuminance())))).withBlockModel((ctx, block) -> {
		Identifier none = SpectrumTexturedModels.cross(b -> b, "_none").upload(block, "_none", ctx.modelCollector);
		Identifier some = SpectrumTexturedModels.cross(b -> b, "_some").upload(block, "_some", ctx.modelCollector);
		Identifier full = SpectrumTexturedModels.cross(b -> b, "_full").upload(block, "_full", ctx.modelCollector);
		return VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_7).register(age -> SpectrumModelHelper.createModelVariant(age < 3 ? none : age < 6 ? some : full)));
	}));
	public static final Block RADIATING_ENDER = register(simple(blockWithItem("radiating_ender", new RadiatingEnderBlock(AbstractBlock.Settings.copy(EMERALD_BLOCK).mapColor(MapColor.PURPLE)), InkColors.PURPLE)));
	public static final Block AMARANTH = register(cutout(block("amaranth", new AmaranthCropBlock(settings(MapColor.CLEAR, BlockSoundGroup.CROP, 0.0F).noCollision().ticksRandomly()))).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_7, TallCropBlock.HALF).register((age, half) -> {
		String suffix;
		if (half == DoubleBlockHalf.LOWER) {
			suffix = "_stage" + ((age + 1) / 2) + "_lower";
			if (age > 0 && age % 2 == 0) return SpectrumModelHelper.createModelVariant(block, suffix);
		} else {
			suffix = "_stage" + Math.max(2, ((age + 1) / 2)) + "_upper";
			if (age < 4 || age == 6) return SpectrumModelHelper.createModelVariant(block, suffix);
		}
		return SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, suffix).upload(block, suffix, ctx.modelCollector));
	}))));
	
	public static final Block MEMORY = register(translucent(singleton(blockWithItem("memory", new MemoryBlock(settings(MapColor.CLEAR, BlockSoundGroup.AMETHYST_BLOCK, 0.0F).blockVision(SpectrumBlocks::never).nonOpaque().ticksRandomly()), block -> new MemoryItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.LIGHT_GRAY), ModelIds::getBlockModelId)).withItemModel((ctx, item) -> SpectrumModelHelper.registerLayeredItemModel(ctx, item, Models.GENERATED_THREE_LAYERS, "_base", "_overlay", "_brighten")));
	public static final Block CRACKED_END_PORTAL_FRAME = register(blockWithItem("cracked_end_portal_frame", new CrackedEndPortalFrameBlock(settings(MapColor.PALE_PURPLE, BlockSoundGroup.GLASS, -1.0F, 3600000.0F).instrument(NoteBlockInstrument.BASEDRUM).luminance((state) -> 1)), IS.of().fireproof(), InkColors.PURPLE).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_none")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(CrackedEndPortalFrameBlock.FACING_VERTICAL).register(false, BlockStateVariant.create()).register(true, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90))).coordinate(BlockStateVariantMap.create(CrackedEndPortalFrameBlock.EYE_TYPE).register(type -> SpectrumModelHelper.createModelVariant(ModelIds.getBlockSubModelId(block, "_" + type.asString()))))));
	public static final Block LAVA_SPONGE = register(simple(blockWithItem("lava_sponge", new LavaSpongeBlock(AbstractBlock.Settings.copy(SPONGE).mapColor(MapColor.ORANGE)), IS.of().fireproof(), InkColors.ORANGE)));
	public static final Block WET_LAVA_SPONGE = register(simple(burnable(blockWithItem("wet_lava_sponge", new WetLavaSpongeBlock(AbstractBlock.Settings.copy(WET_SPONGE).mapColor(MapColor.ORANGE).luminance(s -> 9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), block -> new WetLavaSpongeItem(block, IS.of(1).fireproof().recipeRemainder(LAVA_SPONGE.asItem())), InkColors.ORANGE), 12800)));
	
	public static final Block LIGHT_LEVEL_DETECTOR = register(detector(blockWithItem("light_level_detector", new BlockLightDetectorBlock(AbstractBlock.Settings.copy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final Block WEATHER_DETECTOR = register(detector(blockWithItem("weather_detector", new WeatherDetectorBlock(AbstractBlock.Settings.copy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final Block ITEM_DETECTOR = register(detector(blockWithItem("item_detector", new ItemDetectorBlock(AbstractBlock.Settings.copy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final Block PLAYER_DETECTOR = register(detector(blockWithItem("player_detector", new PlayerDetectorBlock(AbstractBlock.Settings.copy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final Block CREATURE_DETECTOR = register(detector(blockWithItem("creature_detector", new EntityDetectorBlock(AbstractBlock.Settings.copy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final Block REDSTONE_TIMER = register(cutout(blockWithItem("redstone_timer", new RedstoneTimerBlock(AbstractBlock.Settings.copy(REPEATER)), InkColors.RED)).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block);
		Identifier on = SpectrumModels.REDSTONE_TIMER.upload(block, new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getId(REDSTONE_TORCH)), ctx.modelCollector);
		Identifier off = SpectrumModels.REDSTONE_TIMER.upload(block, "_off", new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getSubId(REDSTONE_TORCH, "_off")), ctx.modelCollector);
		for (Direction direction : Direction.Type.HORIZONTAL) {
			VariantSettings.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(Properties.POWERED, true), SpectrumModelHelper.createModelVariant(on).put(VariantSettings.Y, rotation));
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(Properties.POWERED, false), SpectrumModelHelper.createModelVariant(off).put(VariantSettings.Y, rotation));
			for (RedstoneTimerBlock.TimingStep step : RedstoneTimerBlock.TimingStep.values()) {
				multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTimerBlock.ACTIVE_TIME, step), SpectrumModelHelper.createModelVariant(block, "_left_" + step.ordinal()).put(VariantSettings.UVLOCK, true).put(VariantSettings.Y, rotation));
				multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTimerBlock.INACTIVE_TIME, step), SpectrumModelHelper.createModelVariant(block, "_right_" + step.ordinal()).put(VariantSettings.UVLOCK, true).put(VariantSettings.Y, rotation));
			}
		}
		return multipart;
	}));
	public static final Block REDSTONE_CALCULATOR = register(cutout(blockWithItem("redstone_calculator", new RedstoneCalculatorBlock(AbstractBlock.Settings.copy(REPEATER)), InkColors.RED)).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block);
		for (Direction direction : Direction.Type.HORIZONTAL) {
			VariantSettings.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(Properties.POWERED, true), SpectrumModelHelper.createModelVariant(block, "_base").put(VariantSettings.Y, rotation));
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(Properties.POWERED, false), SpectrumModelHelper.createModelVariant(block, "_base_off").put(VariantSettings.Y, rotation));
			for (RedstoneCalculatorBlock.CalculationMode mode : RedstoneCalculatorBlock.CalculationMode.values()) {
				multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneCalculatorBlock.CALCULATION_MODE, mode), SpectrumModelHelper.createModelVariant(block, "_" + mode.asString()).put(VariantSettings.UVLOCK, true).put(VariantSettings.Y, rotation));
			}
		}
		return multipart;
	}));
	public static final Block REDSTONE_TRANSCEIVER = register(cutout(blockWithItem("redstone_transceiver", new RedstoneTransceiverBlock(AbstractBlock.Settings.copy(REPEATER)), InkColors.RED)).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block);
		Identifier senderOn = SpectrumModels.REDSTONE_TRANSCEIVER_SENDER.upload(block, "_sender", new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getId(REDSTONE_TORCH)), ctx.modelCollector);
		Identifier senderOff = SpectrumModels.REDSTONE_TRANSCEIVER_SENDER.upload(block, "_sender_off", new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getSubId(REDSTONE_TORCH, "_off")), ctx.modelCollector);
		Identifier receiverOn = SpectrumModels.REDSTONE_TRANSCEIVER_RECEIVER.upload(block, "_receiver", new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getId(REDSTONE_TORCH)), ctx.modelCollector);
		Identifier receiverOff = SpectrumModels.REDSTONE_TRANSCEIVER_RECEIVER.upload(block, "_receiver_off", new TextureMap().put(SpectrumTextureKeys.LIGHT, TextureMap.getSubId(REDSTONE_TORCH, "_off")), ctx.modelCollector);
		for (Direction direction : Direction.Type.HORIZONTAL) {
			VariantSettings.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTransceiverBlock.SENDER, true).set(Properties.POWERED, true), SpectrumModelHelper.createModelVariant(senderOn).put(VariantSettings.Y, rotation));
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTransceiverBlock.SENDER, true).set(Properties.POWERED, false), SpectrumModelHelper.createModelVariant(senderOff).put(VariantSettings.Y, rotation));
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTransceiverBlock.SENDER, false).set(Properties.POWERED, true), SpectrumModelHelper.createModelVariant(receiverOn).put(VariantSettings.Y, rotation));
			multipart.with(When.create().set(Properties.HORIZONTAL_FACING, direction).set(RedstoneTransceiverBlock.SENDER, false).set(Properties.POWERED, false), SpectrumModelHelper.createModelVariant(receiverOff).put(VariantSettings.Y, rotation));
		}
		for (DyeColor color : DyeColor.values()) {
			Identifier channel = SpectrumModels.REDSTONE_TRANSCEIVER_CHANNEL.upload(block, "_channel_" + color.asString(), SpectrumTextureMaps.all(SpectrumCommon.locate("block/" + color.asString() + "_block")), ctx.modelCollector);
			multipart.with(When.create().set(RedstoneTransceiverBlock.CHANNEL, color), SpectrumModelHelper.createModelVariant(channel));
		}
		return multipart;
	}));
	public static final Block BLOCK_PLACER = register(blockWithItem("block_placer", new BlockPlacerBlock(AbstractBlock.Settings.copy(DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, SpectrumTexturedModels.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_CALCITE, "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side")).coordinate(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap())));
	public static final Block BLOCK_DETECTOR = register(blockWithItem("block_detector", new BlockDetectorBlock(AbstractBlock.Settings.copy(DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap()).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.TRIGGERED, SpectrumTexturedModels.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_BASALT, "_top", b -> b, "_front", b -> b, "_back_active", b -> b, "_side").upload(block, "_active", ctx.modelCollector), SpectrumTexturedModels.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_BASALT, "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side").upload(block, ctx.modelCollector)))));
	public static final Block BLOCK_BREAKER = register(blockWithItem("block_breaker", new BlockBreakerBlock(AbstractBlock.Settings.copy(DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, SpectrumTexturedModels.complexOrientable(b -> b, "_side", b -> b, "_top", b -> POLISHED_BONE_ASH_PILLAR, "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side")).coordinate(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap())));
	public static final EnderDropperBlock ENDER_DROPPER = register(orientable(blockWithItem("ender_dropper", new EnderDropperBlock(AbstractBlock.Settings.copy(DROPPER).mapColor(MapColor.GRAY).requiresTool().strength(15F, 60.0F)), InkColors.PURPLE)));
	public static final Block ENDER_HOPPER = register(singleton(blockWithItem("ender_hopper", new EnderHopperBlock(AbstractBlock.Settings.copy(HOPPER).mapColor(MapColor.GRAY).requiresTool().strength(15F, 60.0F)), InkColors.PURPLE), ModelIds::getBlockModelId).withItemModel(SpectrumModelHelper::registerItemModel));
	
	public static final Block OMINOUS_SAPLING = register(simplePlant(blockWithItem("ominous_sapling", new OminousSaplingBlock(AbstractBlock.Settings.copy(OAK_SAPLING)), block -> new OminousSaplingBlockItem(block, IS.of()), InkColors.GREEN)));
	
	public static final Block SPIRIT_SALLOW_LEAVES = register(singleton(blockWithItem("spirit_sallow_leaves", new SpiritSallowLeavesBlock(AbstractBlock.Settings.copy(OAK_LEAVES).mapColor(MapColor.OFF_WHITE).luminance((state) -> 8)), InkColors.GREEN), TexturedModel.LEAVES));
	public static final Block SPIRIT_SALLOW_LOG = register(log(blockWithItem("spirit_sallow_log", new PillarBlock(AbstractBlock.Settings.copy(OAK_WOOD).mapColor(MapColor.GRAY)), InkColors.GREEN)));
	public static final Block SPIRIT_SALLOW_ROOTS = register(blockWithItem("spirit_sallow_roots", new PillarBlock(AbstractBlock.Settings.copy(OAK_WOOD).mapColor(MapColor.GRAY)), InkColors.GREEN).withBlockModel((ctx, block) -> {
		TextureMap textureMap = SpectrumTextureMaps.sideEnd(block, "", block, "");
		Identifier vertical = Models.CUBE_COLUMN.upload(block, textureMap, ctx.modelCollector);
		Identifier horizontal = Models.CUBE_COLUMN_HORIZONTAL.upload(block, textureMap, ctx.modelCollector);
		return BlockStateModelGenerator.createAxisRotatedBlockState(block, vertical, horizontal);
	}));
	public static final Block SPIRIT_SALLOW_HEART = register(singleton(blockWithItem("spirit_sallow_heart", new Block(AbstractBlock.Settings.copy(OAK_WOOD).mapColor(MapColor.GRAY).luminance(s -> 11)), InkColors.GREEN), SpectrumTexturedModels.cubeColumn(b -> b, "", b -> SPIRIT_SALLOW_LOG, "_top")));
	
	public static final Block SACRED_SOIL = register(blockWithItem("sacred_soil", new ExtraTickFarmlandBlock(AbstractBlock.Settings.copy(FARMLAND).mapColor(MapColor.LIGHT_BLUE_GRAY), DIRT.getDefaultState()), InkColors.LIME).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.MOISTURE).register(moisture -> SpectrumModelHelper.createModelVariant(block, moisture == 7 ? "_moist" : "")))));
	
	private static Settings spiritVines(MapColor mapColor) {
		return settings(mapColor, BlockSoundGroup.CAVE_VINES, 0.0F).noCollision();
	}
	
	public static final SpiritVinesPlantBlock CYAN_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("cyan_spirit_sallow_vines_body", new SpiritVinesPlantBlock(spiritVines(MapColor.CYAN), BuiltinGemstoneColor.CYAN))));
	public static final SpiritVinesPlantBlock MAGENTA_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("magenta_spirit_sallow_vines_body", new SpiritVinesPlantBlock(spiritVines(MapColor.MAGENTA), BuiltinGemstoneColor.MAGENTA))));
	public static final SpiritVinesPlantBlock YELLOW_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("yellow_spirit_sallow_vines_body", new SpiritVinesPlantBlock(spiritVines(MapColor.YELLOW), BuiltinGemstoneColor.YELLOW))));
	public static final SpiritVinesPlantBlock BLACK_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("black_spirit_sallow_vines_body", new SpiritVinesPlantBlock(spiritVines(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK))));
	public static final SpiritVinesPlantBlock WHITE_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("white_spirit_sallow_vines_body", new SpiritVinesPlantBlock(spiritVines(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE))));
	
	public static final SpiritVinesPlantStemBlock CYAN_SPIRIT_SALLOW_VINES = register(spiritVines(block("cyan_spirit_sallow_vines_head", new SpiritVinesPlantStemBlock(spiritVines(MapColor.CYAN), BuiltinGemstoneColor.CYAN))));
	public static final SpiritVinesPlantStemBlock MAGENTA_SPIRIT_SALLOW_VINES = register(spiritVines(block("magenta_spirit_sallow_vines_head", new SpiritVinesPlantStemBlock(spiritVines(MapColor.MAGENTA), BuiltinGemstoneColor.MAGENTA))));
	public static final SpiritVinesPlantStemBlock YELLOW_SPIRIT_SALLOW_VINES = register(spiritVines(block("yellow_spirit_sallow_vines_head", new SpiritVinesPlantStemBlock(spiritVines(MapColor.YELLOW), BuiltinGemstoneColor.YELLOW))));
	public static final SpiritVinesPlantStemBlock BLACK_SPIRIT_SALLOW_VINES = register(spiritVines(block("black_spirit_sallow_vines_head", new SpiritVinesPlantStemBlock(spiritVines(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK))));
	public static final SpiritVinesPlantStemBlock WHITE_SPIRIT_SALLOW_VINES = register(spiritVines(block("white_spirit_sallow_vines_head", new SpiritVinesPlantStemBlock(spiritVines(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE))));
	
	public static final Block STUCK_STORM_STONE = register(cutout(defaultWestHorizontalFacing(block("stuck_storm_stone", new StuckStormStoneBlock(settings(MapColor.CLEAR, BlockSoundGroup.SMALL_AMETHYST_BUD, 0.0F).noCollision().nonOpaque().suffocates(SpectrumBlocks::never).noBlockBreakParticles().blockVision(SpectrumBlocks::never).replaceable())), ModelIds::getBlockModelId)));
	public static final Block DEEPER_DOWN_PORTAL = register(block("deeper_down_portal", new DeeperDownPortalBlock(settings(MapColor.BLACK, BlockSoundGroup.INTENTIONALLY_EMPTY, -1.0F, 3600000.0F).pistonBehavior(PistonBehavior.BLOCK).luminance(state -> 8).dropsNothing())).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(DeeperDownPortalBlock.FACING_UP, ModelIds.getBlockSubModelId(block, "_up"), ModelIds.getBlockModelId(block)))));
	
	private static Settings upgrade() {
		return AbstractBlock.Settings.copy(POLISHED_BASALT).solid();
	}
	
	public static final Block UPGRADE_SPEED = register(parented(blockWithItem("upgrade_speed", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 1, InkColors.MAGENTA_COLOR), block -> new UpgradeBlockItem(block, IS.of(16), "upgrade_speed"), InkColors.LIGHT_GRAY), b -> b));
	public static final Block UPGRADE_SPEED2 = register(parented(blockWithItem("upgrade_speed2", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 2, InkColors.MAGENTA_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.UNCOMMON), "upgrade_speed2"), InkColors.LIGHT_GRAY), b -> UPGRADE_SPEED));
	public static final Block UPGRADE_SPEED3 = register(parented(blockWithItem("upgrade_speed3", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 8, InkColors.MAGENTA_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.RARE), "upgrade_speed3"), InkColors.LIGHT_GRAY), b -> UPGRADE_SPEED));
	public static final Block UPGRADE_EFFICIENCY = register(parented(blockWithItem("upgrade_efficiency", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EFFICIENCY, 1, InkColors.YELLOW_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.UNCOMMON), "upgrade_efficiency"), InkColors.LIGHT_GRAY), b -> b));
	public static final Block UPGRADE_EFFICIENCY2 = register(parented(blockWithItem("upgrade_efficiency2", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EFFICIENCY, 4, InkColors.YELLOW_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.RARE), "upgrade_efficiency2"), InkColors.LIGHT_GRAY), b -> UPGRADE_EFFICIENCY));
	public static final Block UPGRADE_YIELD = register(parented(blockWithItem("upgrade_yield", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.YIELD, 1, InkColors.CYAN_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.UNCOMMON), "upgrade_yield"), InkColors.LIGHT_GRAY), b -> b));
	public static final Block UPGRADE_YIELD2 = register(parented(blockWithItem("upgrade_yield2", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.YIELD, 4, InkColors.CYAN_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.RARE), "upgrade_yield2"), InkColors.LIGHT_GRAY), b -> UPGRADE_YIELD));
	public static final Block UPGRADE_EXPERIENCE = register(parented(blockWithItem("upgrade_experience", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EXPERIENCE, 1, InkColors.PURPLE_COLOR), block -> new UpgradeBlockItem(block, IS.of(16), "upgrade_experience"), InkColors.LIGHT_GRAY), b -> b));
	public static final Block UPGRADE_EXPERIENCE2 = register(parented(blockWithItem("upgrade_experience2", new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EXPERIENCE, 4, InkColors.PURPLE_COLOR), block -> new UpgradeBlockItem(block, IS.of(16, Rarity.UNCOMMON), "upgrade_experience2"), InkColors.LIGHT_GRAY), b -> UPGRADE_EXPERIENCE));
	
	public static final Block REDSTONE_SAND = register(simple(blockWithItem("redstone_sand", new RedstoneGravityBlock(AbstractBlock.Settings.copy(SAND).mapColor(MapColor.BRIGHT_RED)), InkColors.RED)));
	public static final Block ENDER_GLASS = register(translucent(blockWithItem("ender_glass", new EnderGlassBlock(AbstractBlock.Settings.copy(GLASS).mapColor(MapColor.PURPLE).nonOpaque().solidBlock(SpectrumBlocks::never).allowsSpawning((state, world, pos, entityType) -> state.get(EnderGlassBlock.TRANSPARENCY_STATE) == EnderGlassBlock.TransparencyState.SOLID).suffocates((state, world, pos) -> state.get(EnderGlassBlock.TRANSPARENCY_STATE) == EnderGlassBlock.TransparencyState.SOLID).blockVision((state, world, pos) -> state.get(EnderGlassBlock.TRANSPARENCY_STATE) == EnderGlassBlock.TransparencyState.SOLID)), InkColors.PURPLE)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_solid")).withBlockModel((ctx, block) ->
			VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(EnderGlassBlock.TRANSPARENCY_STATE)
					.register(transparency -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cubeAll(b -> b, "_" + transparency.asString()).upload(block, "_" + transparency.asString(), ctx.modelCollector))))));
	public static final Block CLOVER = register(singleton(cutout(blockWithItem("clover", new CloverBlock(AbstractBlock.Settings.copy(SHORT_GRASS).offset(AbstractBlock.OffsetType.XZ)), IS.of(), InkColors.LIME)), ModelIds::getBlockModelId).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final Block FOUR_LEAF_CLOVER = register(singleton(cutout(blockWithItem("four_leaf_clover", new FourLeafCloverBlock(AbstractBlock.Settings.copy(SHORT_GRASS).offset(AbstractBlock.OffsetType.XZ)), block -> new FourLeafCloverItem(block, IS.of(), SpectrumAdvancements.REVEAL_FOUR_LEAF_CLOVER, CLOVER.asItem()), InkColors.LIME)), ModelIds::getBlockModelId).withItemModel(SpectrumModelHelper::registerItemModel));
	
	private static final UniformIntProvider gemOreExperienceProvider = UniformIntProvider.create(1, 4);
	public static final Block TOPAZ_ORE = register(simple(blockWithItem("topaz_ore", new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, STONE.getDefaultState()), InkColors.CYAN)));
	public static final Block AMETHYST_ORE = register(simple(blockWithItem("amethyst_ore", new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, STONE.getDefaultState()), InkColors.MAGENTA)));
	public static final Block CITRINE_ORE = register(simple(blockWithItem("citrine_ore", new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, STONE.getDefaultState()), InkColors.YELLOW)));
	public static final Block ONYX_ORE = register(simple(blockWithItem("onyx_ore", new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, STONE.getDefaultState()), InkColors.BLACK)));
	public static final Block MOONSTONE_ORE = register(simple(blockWithItem("moonstone_ore", new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, STONE.getDefaultState()), InkColors.WHITE)));
	
	public static final Block DEEPSLATE_TOPAZ_ORE = register(simple(blockWithItem("deepslate_topaz_ore", new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, DEEPSLATE.getDefaultState()), InkColors.CYAN)));
	public static final Block DEEPSLATE_AMETHYST_ORE = register(simple(blockWithItem("deepslate_amethyst_ore", new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, DEEPSLATE.getDefaultState()), InkColors.MAGENTA)));
	public static final Block DEEPSLATE_CITRINE_ORE = register(simple(blockWithItem("deepslate_citrine_ore", new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, DEEPSLATE.getDefaultState()), InkColors.YELLOW)));
	public static final Block DEEPSLATE_ONYX_ORE = register(simple(blockWithItem("deepslate_onyx_ore", new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, DEEPSLATE.getDefaultState()), InkColors.BLACK)));
	public static final Block DEEPSLATE_MOONSTONE_ORE = register(simple(blockWithItem("deepslate_moonstone_ore", new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, DEEPSLATE.getDefaultState()), InkColors.WHITE)));
	
	public static final Block BLACKSLAG_TOPAZ_ORE = register(singleton(blockWithItem("blackslag_topaz_ore", new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, BLACKSLAG.getDefaultState()), InkColors.CYAN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_AMETHYST_ORE = register(singleton(blockWithItem("blackslag_amethyst_ore", new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, BLACKSLAG.getDefaultState()), InkColors.MAGENTA), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_CITRINE_ORE = register(singleton(blockWithItem("blackslag_citrine_ore", new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, BLACKSLAG.getDefaultState()), InkColors.YELLOW), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_ONYX_ORE = register(singleton(blockWithItem("blackslag_onyx_ore", new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, BLACKSLAG.getDefaultState()), InkColors.BLACK), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLACKSLAG_MOONSTONE_ORE = register(singleton(blockWithItem("blackslag_moonstone_ore", new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, BLACKSLAG.getDefaultState()), InkColors.WHITE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	private static Settings polishedGemBlock(MapColor mapColor, BlockSoundGroup soundGroup) {
		return settings(mapColor, soundGroup, 5.0F, 6.0F);
	}
	
	public static final Block POLISHED_TOPAZ_BLOCK = register(singleton(blockWithItem("polished_topaz_block", new Block(polishedGemBlock(MapColor.CYAN, SpectrumBlockSoundGroups.TOPAZ_BLOCK)), InkColors.CYAN), TexturedModel.SIDE_TOP_BOTTOM_WALL));
	public static final Block POLISHED_AMETHYST_BLOCK = register(singleton(blockWithItem("polished_amethyst_block", new Block(polishedGemBlock(MapColor.MAGENTA, BlockSoundGroup.AMETHYST_BLOCK)), InkColors.MAGENTA), TexturedModel.SIDE_TOP_BOTTOM_WALL));
	public static final Block POLISHED_CITRINE_BLOCK = register(singleton(blockWithItem("polished_citrine_block", new Block(polishedGemBlock(MapColor.YELLOW, SpectrumBlockSoundGroups.CITRINE_BLOCK)), InkColors.YELLOW), TexturedModel.SIDE_TOP_BOTTOM_WALL));
	public static final Block POLISHED_ONYX_BLOCK = register(singleton(blockWithItem("polished_onyx_block", new Block(polishedGemBlock(MapColor.BLACK, SpectrumBlockSoundGroups.ONYX_BLOCK)), InkColors.BLACK), TexturedModel.SIDE_TOP_BOTTOM_WALL));
	public static final Block POLISHED_MOONSTONE_BLOCK = register(singleton(blockWithItem("polished_moonstone_block", new Block(polishedGemBlock(MapColor.WHITE, SpectrumBlockSoundGroups.MOONSTONE_BLOCK)), InkColors.WHITE), TexturedModel.SIDE_TOP_BOTTOM_WALL));
	
	private static AbstractBlock.Settings copyWithMapColor(Block baseBlock, MapColor color) {
		return AbstractBlock.Settings.copy(baseBlock).mapColor(color);
	}
	
	public static Settings pottedPlant() {
		return Settings.create().breakInstantly().nonOpaque().pistonBehavior(PistonBehavior.DESTROY);
	}
	
	public static final AmaranthBushelBlock AMARANTH_BUSHEL = register(cross(blockWithItem("amaranth_bushel", new AmaranthBushelBlock(SpectrumStatusEffects.NOURISHING, 8, settings(MapColor.CLEAR, BlockSoundGroup.CROP, 0.0F).noCollision()), InkColors.RED)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final PottedAmaranthBushelBlock POTTED_AMARANTH_BUSHEL = register(pottedPlant(block("potted_amaranth_bushel", new PottedAmaranthBushelBlock(AMARANTH_BUSHEL, pottedPlant())), false));
	
	public static final ResonantLilyBlock RESONANT_LILY = register(simplePlant(blockWithItem("resonant_lily", new ResonantLilyBlock(StatusEffects.REGENERATION, 5, AbstractBlock.Settings.copy(POPPY).mapColor(MapColor.WHITE)), InkColors.GREEN)));
	public static final PottedResonantLilyBlock POTTED_RESONANT_LILY = register(pottedPlant(block("potted_resonant_lily", new PottedResonantLilyBlock(RESONANT_LILY, pottedPlant())), false));
	
	public static final BloodOrchidBlock BLOOD_ORCHID = register(cutout(blockWithItem("blood_orchid", new BloodOrchidBlock(SpectrumStatusEffects.FRENZY, 10, AbstractBlock.Settings.copy(POPPY).offset(AbstractBlock.OffsetType.NONE).ticksRandomly()), InkColors.RED)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "5")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(BloodOrchidBlock.AGE).register(stage -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.cross(b -> b, stage.toString()).upload(block, stage.toString(), ctx.modelCollector))))));
	public static final PottedBloodOrchidBlock POTTED_BLOOD_ORCHID = register(cutout(singleton(block("potted_blood_orchid", new PottedBloodOrchidBlock(BLOOD_ORCHID, pottedPlant())), SpectrumTexturedModels.flowerPotCross(b -> BLOOD_ORCHID, "5", false))));
	
	public static ColoredSaplingBlock registerColoredSapling(String name, InkColor color, SaplingGenerator generator) {
		return register(simplePlant(blockWithItem(name, new ColoredSaplingBlock(copyWithMapColor(OAK_SAPLING, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color, generator), color)));
	}
	
	public static final ColoredSaplingBlock BLACK_SAPLING = registerColoredSapling("black_sapling", InkColors.BLACK, SpectrumSaplingGenerators.BLACK_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock BLUE_SAPLING = registerColoredSapling("blue_sapling", InkColors.BLUE, SpectrumSaplingGenerators.BLUE_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock BROWN_SAPLING = registerColoredSapling("brown_sapling", InkColors.BROWN, SpectrumSaplingGenerators.BROWN_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock CYAN_SAPLING = registerColoredSapling("cyan_sapling", InkColors.CYAN, SpectrumSaplingGenerators.CYAN_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock GRAY_SAPLING = registerColoredSapling("gray_sapling", InkColors.GRAY, SpectrumSaplingGenerators.GRAY_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock GREEN_SAPLING = registerColoredSapling("green_sapling", InkColors.GREEN, SpectrumSaplingGenerators.GREEN_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock LIGHT_BLUE_SAPLING = registerColoredSapling("light_blue_sapling", InkColors.LIGHT_BLUE, SpectrumSaplingGenerators.LIGHT_BLUE_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock LIGHT_GRAY_SAPLING = registerColoredSapling("light_gray_sapling", InkColors.LIGHT_GRAY, SpectrumSaplingGenerators.LIGHT_GRAY_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock LIME_SAPLING = registerColoredSapling("lime_sapling", InkColors.LIME, SpectrumSaplingGenerators.LIME_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock MAGENTA_SAPLING = registerColoredSapling("magenta_sapling", InkColors.MAGENTA, SpectrumSaplingGenerators.MAGENTA_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock ORANGE_SAPLING = registerColoredSapling("orange_sapling", InkColors.ORANGE, SpectrumSaplingGenerators.ORANGE_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock PINK_SAPLING = registerColoredSapling("pink_sapling", InkColors.PINK, SpectrumSaplingGenerators.PINK_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock PURPLE_SAPLING = registerColoredSapling("purple_sapling", InkColors.PURPLE, SpectrumSaplingGenerators.PURPLE_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock RED_SAPLING = registerColoredSapling("red_sapling", InkColors.RED, SpectrumSaplingGenerators.RED_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock WHITE_SAPLING = registerColoredSapling("white_sapling", InkColors.WHITE, SpectrumSaplingGenerators.WHITE_COLORED_SAPLING_GENERATOR);
	public static final ColoredSaplingBlock YELLOW_SAPLING = registerColoredSapling("yellow_sapling", InkColors.YELLOW, SpectrumSaplingGenerators.YELLOW_COLORED_SAPLING_GENERATOR);
	
	public static PottedColoredSaplingBlock registerPottedColoredSapling(String name, ColoredSaplingBlock saplingBlock) {
		return register(pottedPlant(block(name, new PottedColoredSaplingBlock(saplingBlock, pottedPlant(), saplingBlock.getColor())), false));
	}
	
	public static final PottedColoredSaplingBlock POTTED_BLACK_SAPLING = registerPottedColoredSapling("potted_black_sapling", BLACK_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_BLUE_SAPLING = registerPottedColoredSapling("potted_blue_sapling", BLUE_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_BROWN_SAPLING = registerPottedColoredSapling("potted_brown_sapling", BROWN_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_CYAN_SAPLING = registerPottedColoredSapling("potted_cyan_sapling", CYAN_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_GRAY_SAPLING = registerPottedColoredSapling("potted_gray_sapling", GRAY_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_GREEN_SAPLING = registerPottedColoredSapling("potted_green_sapling", GREEN_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_LIGHT_BLUE_SAPLING = registerPottedColoredSapling("potted_light_blue_sapling", LIGHT_BLUE_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_LIGHT_GRAY_SAPLING = registerPottedColoredSapling("potted_light_gray_sapling", LIGHT_GRAY_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_LIME_SAPLING = registerPottedColoredSapling("potted_lime_sapling", LIME_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_MAGENTA_SAPLING = registerPottedColoredSapling("potted_magenta_sapling", MAGENTA_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_ORANGE_SAPLING = registerPottedColoredSapling("potted_orange_sapling", ORANGE_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_PINK_SAPLING = registerPottedColoredSapling("potted_pink_sapling", PINK_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_PURPLE_SAPLING = registerPottedColoredSapling("potted_purple_sapling", PURPLE_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_RED_SAPLING = registerPottedColoredSapling("potted_red_sapling", RED_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_WHITE_SAPLING = registerPottedColoredSapling("potted_white_sapling", WHITE_SAPLING);
	public static final PottedColoredSaplingBlock POTTED_YELLOW_SAPLING = registerPottedColoredSapling("potted_yellow_sapling", YELLOW_SAPLING);
	
	public static ColoredLogBlock registerColoredLog(String name, InkColor color) {
		return register(log(blockWithItem(name, new ColoredLogBlock(copyWithMapColor(OAK_LOG, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color)));
	}
	
	public static final ColoredLogBlock BLACK_LOG = registerColoredLog("black_log", InkColors.BLACK);
	public static final ColoredLogBlock BLUE_LOG = registerColoredLog("blue_log", InkColors.BLUE);
	public static final ColoredLogBlock BROWN_LOG = registerColoredLog("brown_log", InkColors.BROWN);
	public static final ColoredLogBlock CYAN_LOG = registerColoredLog("cyan_log", InkColors.CYAN);
	public static final ColoredLogBlock GRAY_LOG = registerColoredLog("gray_log", InkColors.GRAY);
	public static final ColoredLogBlock GREEN_LOG = registerColoredLog("green_log", InkColors.GREEN);
	public static final ColoredLogBlock LIGHT_BLUE_LOG = registerColoredLog("light_blue_log", InkColors.LIGHT_BLUE);
	public static final ColoredLogBlock LIGHT_GRAY_LOG = registerColoredLog("light_gray_log", InkColors.LIGHT_GRAY);
	public static final ColoredLogBlock LIME_LOG = registerColoredLog("lime_log", InkColors.LIME);
	public static final ColoredLogBlock MAGENTA_LOG = registerColoredLog("magenta_log", InkColors.MAGENTA);
	public static final ColoredLogBlock ORANGE_LOG = registerColoredLog("orange_log", InkColors.ORANGE);
	public static final ColoredLogBlock PINK_LOG = registerColoredLog("pink_log", InkColors.PINK);
	public static final ColoredLogBlock PURPLE_LOG = registerColoredLog("purple_log", InkColors.PURPLE);
	public static final ColoredLogBlock RED_LOG = registerColoredLog("red_log", InkColors.RED);
	public static final ColoredLogBlock WHITE_LOG = registerColoredLog("white_log", InkColors.WHITE);
	public static final ColoredLogBlock YELLOW_LOG = registerColoredLog("yellow_log", InkColors.YELLOW);
	
	public static ColoredWoodBlock registerColoredWood(String name, ColoredLogBlock logBlock) {
		return register(wood(blockWithItem(name, new ColoredWoodBlock(copyWithMapColor(OAK_WOOD, logBlock.getDefaultMapColor()), logBlock.getColor()), logBlock.getColor()), logBlock));
	}
	
	public static final ColoredWoodBlock BLACK_WOOD = registerColoredWood("black_wood", BLACK_LOG);
	public static final ColoredWoodBlock BLUE_WOOD = registerColoredWood("blue_wood", BLUE_LOG);
	public static final ColoredWoodBlock BROWN_WOOD = registerColoredWood("brown_wood", BROWN_LOG);
	public static final ColoredWoodBlock CYAN_WOOD = registerColoredWood("cyan_wood", CYAN_LOG);
	public static final ColoredWoodBlock GRAY_WOOD = registerColoredWood("gray_wood", GRAY_LOG);
	public static final ColoredWoodBlock GREEN_WOOD = registerColoredWood("green_wood", GREEN_LOG);
	public static final ColoredWoodBlock LIGHT_BLUE_WOOD = registerColoredWood("light_blue_wood", LIGHT_BLUE_LOG);
	public static final ColoredWoodBlock LIGHT_GRAY_WOOD = registerColoredWood("light_gray_wood", LIGHT_GRAY_LOG);
	public static final ColoredWoodBlock LIME_WOOD = registerColoredWood("lime_wood", LIME_LOG);
	public static final ColoredWoodBlock MAGENTA_WOOD = registerColoredWood("magenta_wood", MAGENTA_LOG);
	public static final ColoredWoodBlock ORANGE_WOOD = registerColoredWood("orange_wood", ORANGE_LOG);
	public static final ColoredWoodBlock PINK_WOOD = registerColoredWood("pink_wood", PINK_LOG);
	public static final ColoredWoodBlock PURPLE_WOOD = registerColoredWood("purple_wood", PURPLE_LOG);
	public static final ColoredWoodBlock RED_WOOD = registerColoredWood("red_wood", RED_LOG);
	public static final ColoredWoodBlock WHITE_WOOD = registerColoredWood("white_wood", WHITE_LOG);
	public static final ColoredWoodBlock YELLOW_WOOD = registerColoredWood("yellow_wood", YELLOW_LOG);
	
	public static ColoredStrippedLogBlock registerColoredStrippedLog(String name, InkColor color) {
		return register(log(blockWithItem(name, new ColoredStrippedLogBlock(copyWithMapColor(STRIPPED_OAK_LOG, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color)));
	}
	
	public static final ColoredStrippedLogBlock STRIPPED_BLACK_LOG = registerColoredStrippedLog("stripped_black_log", InkColors.BLACK);
	public static final ColoredStrippedLogBlock STRIPPED_BLUE_LOG = registerColoredStrippedLog("stripped_blue_log", InkColors.BLUE);
	public static final ColoredStrippedLogBlock STRIPPED_BROWN_LOG = registerColoredStrippedLog("stripped_brown_log", InkColors.BROWN);
	public static final ColoredStrippedLogBlock STRIPPED_CYAN_LOG = registerColoredStrippedLog("stripped_cyan_log", InkColors.CYAN);
	public static final ColoredStrippedLogBlock STRIPPED_GRAY_LOG = registerColoredStrippedLog("stripped_gray_log", InkColors.GRAY);
	public static final ColoredStrippedLogBlock STRIPPED_GREEN_LOG = registerColoredStrippedLog("stripped_green_log", InkColors.GREEN);
	public static final ColoredStrippedLogBlock STRIPPED_LIGHT_BLUE_LOG = registerColoredStrippedLog("stripped_light_blue_log", InkColors.LIGHT_BLUE);
	public static final ColoredStrippedLogBlock STRIPPED_LIGHT_GRAY_LOG = registerColoredStrippedLog("stripped_light_gray_log", InkColors.LIGHT_GRAY);
	public static final ColoredStrippedLogBlock STRIPPED_LIME_LOG = registerColoredStrippedLog("stripped_lime_log", InkColors.LIME);
	public static final ColoredStrippedLogBlock STRIPPED_MAGENTA_LOG = registerColoredStrippedLog("stripped_magenta_log", InkColors.MAGENTA);
	public static final ColoredStrippedLogBlock STRIPPED_ORANGE_LOG = registerColoredStrippedLog("stripped_orange_log", InkColors.ORANGE);
	public static final ColoredStrippedLogBlock STRIPPED_PINK_LOG = registerColoredStrippedLog("stripped_pink_log", InkColors.PINK);
	public static final ColoredStrippedLogBlock STRIPPED_PURPLE_LOG = registerColoredStrippedLog("stripped_purple_log", InkColors.PURPLE);
	public static final ColoredStrippedLogBlock STRIPPED_RED_LOG = registerColoredStrippedLog("stripped_red_log", InkColors.RED);
	public static final ColoredStrippedLogBlock STRIPPED_WHITE_LOG = registerColoredStrippedLog("stripped_white_log", InkColors.WHITE);
	public static final ColoredStrippedLogBlock STRIPPED_YELLOW_LOG = registerColoredStrippedLog("stripped_yellow_log", InkColors.YELLOW);
	
	public static ColoredStrippedWoodBlock registerColoredStrippedWood(String name, ColoredStrippedLogBlock logBlock) {
		return register(wood(blockWithItem(name, new ColoredStrippedWoodBlock(copyWithMapColor(STRIPPED_OAK_WOOD, logBlock.getDefaultMapColor()), logBlock.getColor()), logBlock.getColor()), logBlock));
	}
	
	public static final ColoredStrippedWoodBlock STRIPPED_BLACK_WOOD = registerColoredStrippedWood("stripped_black_wood", STRIPPED_BLACK_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_BLUE_WOOD = registerColoredStrippedWood("stripped_blue_wood", STRIPPED_BLUE_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_BROWN_WOOD = registerColoredStrippedWood("stripped_brown_wood", STRIPPED_BROWN_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_CYAN_WOOD = registerColoredStrippedWood("stripped_cyan_wood", STRIPPED_CYAN_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_GRAY_WOOD = registerColoredStrippedWood("stripped_gray_wood", STRIPPED_GRAY_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_GREEN_WOOD = registerColoredStrippedWood("stripped_green_wood", STRIPPED_GREEN_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_LIGHT_BLUE_WOOD = registerColoredStrippedWood("stripped_light_blue_wood", STRIPPED_LIGHT_BLUE_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_LIGHT_GRAY_WOOD = registerColoredStrippedWood("stripped_light_gray_wood", STRIPPED_LIGHT_GRAY_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_LIME_WOOD = registerColoredStrippedWood("stripped_lime_wood", STRIPPED_LIME_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_MAGENTA_WOOD = registerColoredStrippedWood("stripped_magenta_wood", STRIPPED_MAGENTA_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_ORANGE_WOOD = registerColoredStrippedWood("stripped_orange_wood", STRIPPED_ORANGE_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_PINK_WOOD = registerColoredStrippedWood("stripped_pink_wood", STRIPPED_PINK_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_PURPLE_WOOD = registerColoredStrippedWood("stripped_purple_wood", STRIPPED_PURPLE_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_RED_WOOD = registerColoredStrippedWood("stripped_red_wood", STRIPPED_RED_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_WHITE_WOOD = registerColoredStrippedWood("stripped_white_wood", STRIPPED_WHITE_LOG);
	public static final ColoredStrippedWoodBlock STRIPPED_YELLOW_WOOD = registerColoredStrippedWood("stripped_yellow_wood", STRIPPED_YELLOW_LOG);
	
	public static ColoredLeavesBlock registerColoredLeaves(String name, InkColor color) {
		return register(singleton(blockWithItem(name, new ColoredLeavesBlock(copyWithMapColor(OAK_LEAVES, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color), TexturedModel.LEAVES));
	}
	
	public static final ColoredLeavesBlock BLACK_LEAVES = registerColoredLeaves("black_leaves", InkColors.BLACK);
	public static final ColoredLeavesBlock BLUE_LEAVES = registerColoredLeaves("blue_leaves", InkColors.BLUE);
	public static final ColoredLeavesBlock BROWN_LEAVES = registerColoredLeaves("brown_leaves", InkColors.BROWN);
	public static final ColoredLeavesBlock CYAN_LEAVES = registerColoredLeaves("cyan_leaves", InkColors.CYAN);
	public static final ColoredLeavesBlock GRAY_LEAVES = registerColoredLeaves("gray_leaves", InkColors.GRAY);
	public static final ColoredLeavesBlock GREEN_LEAVES = registerColoredLeaves("green_leaves", InkColors.GREEN);
	public static final ColoredLeavesBlock LIGHT_BLUE_LEAVES = registerColoredLeaves("light_blue_leaves", InkColors.LIGHT_BLUE);
	public static final ColoredLeavesBlock LIGHT_GRAY_LEAVES = registerColoredLeaves("light_gray_leaves", InkColors.LIGHT_GRAY);
	public static final ColoredLeavesBlock LIME_LEAVES = registerColoredLeaves("lime_leaves", InkColors.LIME);
	public static final ColoredLeavesBlock MAGENTA_LEAVES = registerColoredLeaves("magenta_leaves", InkColors.MAGENTA);
	public static final ColoredLeavesBlock ORANGE_LEAVES = registerColoredLeaves("orange_leaves", InkColors.ORANGE);
	public static final ColoredLeavesBlock PINK_LEAVES = registerColoredLeaves("pink_leaves", InkColors.PINK);
	public static final ColoredLeavesBlock PURPLE_LEAVES = registerColoredLeaves("purple_leaves", InkColors.PURPLE);
	public static final ColoredLeavesBlock RED_LEAVES = registerColoredLeaves("red_leaves", InkColors.RED);
	public static final ColoredLeavesBlock WHITE_LEAVES = registerColoredLeaves("white_leaves", InkColors.WHITE);
	public static final ColoredLeavesBlock YELLOW_LEAVES = registerColoredLeaves("yellow_leaves", InkColors.YELLOW);
	
	public static GlowBlock registerGlowBlock(String name, InkColor color) {
		return register(simple(blockWithItem(name, new GlowBlock(settings(color.getDyeColor().orElse(DyeColor.LIME).getMapColor(), BlockSoundGroup.BASALT, 2.5F).requiresTool().luminance(state -> 1).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always), color), color)));
	}
	
	public static final Block BLACK_GLOWBLOCK = registerGlowBlock("black_glowblock", InkColors.BLACK);
	public static final Block BLUE_GLOWBLOCK = registerGlowBlock("blue_glowblock", InkColors.BLUE);
	public static final Block BROWN_GLOWBLOCK = registerGlowBlock("brown_glowblock", InkColors.BROWN);
	public static final Block CYAN_GLOWBLOCK = registerGlowBlock("cyan_glowblock", InkColors.CYAN);
	public static final Block GRAY_GLOWBLOCK = registerGlowBlock("gray_glowblock", InkColors.GRAY);
	public static final Block GREEN_GLOWBLOCK = registerGlowBlock("green_glowblock", InkColors.GREEN);
	public static final Block LIGHT_BLUE_GLOWBLOCK = registerGlowBlock("light_blue_glowblock", InkColors.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_GLOWBLOCK = registerGlowBlock("light_gray_glowblock", InkColors.LIGHT_GRAY);
	public static final Block LIME_GLOWBLOCK = registerGlowBlock("lime_glowblock", InkColors.LIME);
	public static final Block MAGENTA_GLOWBLOCK = registerGlowBlock("magenta_glowblock", InkColors.MAGENTA);
	public static final Block ORANGE_GLOWBLOCK = registerGlowBlock("orange_glowblock", InkColors.ORANGE);
	public static final Block PINK_GLOWBLOCK = registerGlowBlock("pink_glowblock", InkColors.PINK);
	public static final Block PURPLE_GLOWBLOCK = registerGlowBlock("purple_glowblock", InkColors.PURPLE);
	public static final Block RED_GLOWBLOCK = registerGlowBlock("red_glowblock", InkColors.RED);
	public static final Block WHITE_GLOWBLOCK = registerGlowBlock("white_glowblock", InkColors.WHITE);
	public static final Block YELLOW_GLOWBLOCK = registerGlowBlock("yellow_glowblock", InkColors.YELLOW);
	
	public static ColoredLightBlock registerColoredLightBlock(String name, InkColor color) {
		return register(translucent(blockWithItem(name, new ColoredLightBlock(AbstractBlock.Settings.copy(REDSTONE_LAMP).mapColor(color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color)).withBlockModel((ctx, block) -> {
			Identifier off = TexturedModel.CUBE_ALL.upload(block, ctx.modelCollector);
			Identifier on = SpectrumModels.COLORED_LAMP_ON.upload(block, "_on", SpectrumTextureMaps.innerOuter(block, "_on", block, "_outer"), ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.LIT, on, off));
		}));
	}
	
	public static final Block BLACK_LAMP = registerColoredLightBlock("black_lamp", InkColors.BLACK);
	public static final Block BLUE_LAMP = registerColoredLightBlock("blue_lamp", InkColors.BLUE);
	public static final Block BROWN_LAMP = registerColoredLightBlock("brown_lamp", InkColors.BROWN);
	public static final Block CYAN_LAMP = registerColoredLightBlock("cyan_lamp", InkColors.CYAN);
	public static final Block GRAY_LAMP = registerColoredLightBlock("gray_lamp", InkColors.GRAY);
	public static final Block GREEN_LAMP = registerColoredLightBlock("green_lamp", InkColors.GREEN);
	public static final Block LIGHT_BLUE_LAMP = registerColoredLightBlock("light_blue_lamp", InkColors.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_LAMP = registerColoredLightBlock("light_gray_lamp", InkColors.LIGHT_GRAY);
	public static final Block LIME_LAMP = registerColoredLightBlock("lime_lamp", InkColors.LIME);
	public static final Block MAGENTA_LAMP = registerColoredLightBlock("magenta_lamp", InkColors.MAGENTA);
	public static final Block ORANGE_LAMP = registerColoredLightBlock("orange_lamp", InkColors.ORANGE);
	public static final Block PINK_LAMP = registerColoredLightBlock("pink_lamp", InkColors.PINK);
	public static final Block PURPLE_LAMP = registerColoredLightBlock("purple_lamp", InkColors.PURPLE);
	public static final Block RED_LAMP = registerColoredLightBlock("red_lamp", InkColors.RED);
	public static final Block WHITE_LAMP = registerColoredLightBlock("white_lamp", InkColors.WHITE);
	public static final Block YELLOW_LAMP = registerColoredLightBlock("yellow_lamp", InkColors.YELLOW);
	
	public static PigmentBlock registerPigmentBlock(String name, InkColor color) {
		return register(simple(blockWithItem(name, new PigmentBlock(settings(color.getDyeColor().orElse(DyeColor.LIME).getMapColor(), BlockSoundGroup.WOOL, 1.0F), color), color)));
	}
	
	public static final Block BLACK_BLOCK = registerPigmentBlock("black_block", InkColors.BLACK);
	public static final Block BLUE_BLOCK = registerPigmentBlock("blue_block", InkColors.BLUE);
	public static final Block BROWN_BLOCK = registerPigmentBlock("brown_block", InkColors.BROWN);
	public static final Block CYAN_BLOCK = registerPigmentBlock("cyan_block", InkColors.CYAN);
	public static final Block GRAY_BLOCK = registerPigmentBlock("gray_block", InkColors.GRAY);
	public static final Block GREEN_BLOCK = registerPigmentBlock("green_block", InkColors.GREEN);
	public static final Block LIGHT_BLUE_BLOCK = registerPigmentBlock("light_blue_block", InkColors.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_BLOCK = registerPigmentBlock("light_gray_block", InkColors.LIGHT_GRAY);
	public static final Block LIME_BLOCK = registerPigmentBlock("lime_block", InkColors.LIME);
	public static final Block MAGENTA_BLOCK = registerPigmentBlock("magenta_block", InkColors.MAGENTA);
	public static final Block ORANGE_BLOCK = registerPigmentBlock("orange_block", InkColors.ORANGE);
	public static final Block PINK_BLOCK = registerPigmentBlock("pink_block", InkColors.PINK);
	public static final Block PURPLE_BLOCK = registerPigmentBlock("purple_block", InkColors.PURPLE);
	public static final Block RED_BLOCK = registerPigmentBlock("red_block", InkColors.RED);
	public static final Block WHITE_BLOCK = registerPigmentBlock("white_block", InkColors.WHITE);
	public static final Block YELLOW_BLOCK = registerPigmentBlock("yellow_block", InkColors.YELLOW);
	
	public static ColoredSporeBlossomBlock registerColoredSporeBlossomBlock(String name, InkColor color, ColoredFallingSporeBlossomParticleEffect falling, ColoredSporeBlossomAirParticleEffect air) {
		return register(cutout(singleton(blockWithItem(name, new ColoredSporeBlossomBlock(AbstractBlock.Settings.copy(SPORE_BLOSSOM).mapColor(color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color, falling, air), color), TexturedModel.makeFactory(b -> SpectrumTextureMaps.flowerParticle(b, "", b, ""), SpectrumModels.SPORE_BLOSSOM))));
	}
	
	public static final Block BLACK_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("black_spore_blossom", InkColors.BLACK, ColoredFallingSporeBlossomParticleEffect.BLACK, ColoredSporeBlossomAirParticleEffect.BLACK);
	public static final Block BLUE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("blue_spore_blossom", InkColors.BLUE, ColoredFallingSporeBlossomParticleEffect.BLUE, ColoredSporeBlossomAirParticleEffect.BLUE);
	public static final Block BROWN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("brown_spore_blossom", InkColors.BROWN, ColoredFallingSporeBlossomParticleEffect.BROWN, ColoredSporeBlossomAirParticleEffect.BROWN);
	public static final Block CYAN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("cyan_spore_blossom", InkColors.CYAN, ColoredFallingSporeBlossomParticleEffect.CYAN, ColoredSporeBlossomAirParticleEffect.CYAN);
	public static final Block GRAY_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("gray_spore_blossom", InkColors.GRAY, ColoredFallingSporeBlossomParticleEffect.GRAY, ColoredSporeBlossomAirParticleEffect.GRAY);
	public static final Block GREEN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("green_spore_blossom", InkColors.GREEN, ColoredFallingSporeBlossomParticleEffect.GREEN, ColoredSporeBlossomAirParticleEffect.GREEN);
	public static final Block LIGHT_BLUE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("light_blue_spore_blossom", InkColors.LIGHT_BLUE, ColoredFallingSporeBlossomParticleEffect.LIGHT_BLUE, ColoredSporeBlossomAirParticleEffect.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("light_gray_spore_blossom", InkColors.LIGHT_GRAY, ColoredFallingSporeBlossomParticleEffect.LIGHT_GRAY, ColoredSporeBlossomAirParticleEffect.LIGHT_GRAY);
	public static final Block LIME_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("lime_spore_blossom", InkColors.LIME, ColoredFallingSporeBlossomParticleEffect.LIME, ColoredSporeBlossomAirParticleEffect.LIME);
	public static final Block MAGENTA_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("magenta_spore_blossom", InkColors.MAGENTA, ColoredFallingSporeBlossomParticleEffect.MAGENTA, ColoredSporeBlossomAirParticleEffect.MAGENTA);
	public static final Block ORANGE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("orange_spore_blossom", InkColors.ORANGE, ColoredFallingSporeBlossomParticleEffect.ORANGE, ColoredSporeBlossomAirParticleEffect.ORANGE);
	public static final Block PINK_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("pink_spore_blossom", InkColors.PINK, ColoredFallingSporeBlossomParticleEffect.PINK, ColoredSporeBlossomAirParticleEffect.PINK);
	public static final Block PURPLE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("purple_spore_blossom", InkColors.PURPLE, ColoredFallingSporeBlossomParticleEffect.PURPLE, ColoredSporeBlossomAirParticleEffect.PURPLE);
	public static final Block RED_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("red_spore_blossom", InkColors.RED, ColoredFallingSporeBlossomParticleEffect.RED, ColoredSporeBlossomAirParticleEffect.RED);
	public static final Block WHITE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("white_spore_blossom", InkColors.WHITE, ColoredFallingSporeBlossomParticleEffect.WHITE, ColoredSporeBlossomAirParticleEffect.WHITE);
	public static final Block YELLOW_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("yellow_spore_blossom", InkColors.YELLOW, ColoredFallingSporeBlossomParticleEffect.YELLOW, ColoredSporeBlossomAirParticleEffect.YELLOW);
	
	public static ShimmerstoneLightBlock registerShimmerstoneLight(String name, BlockSoundGroup soundGroup, Supplier<Identifier> outerSupplier) {
		return register(blockWithItem(name, new ShimmerstoneLightBlock(settings(MapColor.CLEAR, soundGroup, 1.0F).nonOpaque().requiresTool().luminance(state -> 15).pistonBehavior(PistonBehavior.DESTROY)), InkColors.YELLOW).withBlockModel((ctx, block) -> {
			Identifier outer = outerSupplier.get();
			Identifier base = SpectrumModels.SHIMMERSTONE_LIGHT.upload(block, SpectrumTextureMaps.innerOuterParticle(SpectrumTextures.SHIMMERSTONE_LIGHT, outer, outer), ctx.modelCollector);
			Identifier mirrored = SpectrumModels.SHIMMERSTONE_LIGHT_MIRRORED.upload(block, "_mirrored", SpectrumTextureMaps.innerOuterParticle(SpectrumTextures.SHIMMERSTONE_LIGHT, outer, outer), ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createNorthDefaultFacingVariantMap()).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.INVERTED, mirrored, base));
		}));
	}
	
	public static final Block STONE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("stone_shimmerstone_light", BlockSoundGroup.STONE, () -> SpectrumTextures.STONE_FLAT_LIGHT);
	public static final Block BASALT_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("basalt_shimmerstone_light", BlockSoundGroup.BASALT, () -> SpectrumTextures.BASALT_FLAT_LIGHT);
	public static final Block CALCITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("calcite_shimmerstone_light", BlockSoundGroup.CALCITE, () -> SpectrumTextures.CALCITE_FLAT_LIGHT);
	public static final Block DEEPSLATE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("deepslate_shimmerstone_light", BlockSoundGroup.DEEPSLATE, () -> SpectrumTextures.DEEPSLATE_FLAT_LIGHT);
	public static final Block BLACKSLAG_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("blackslag_shimmerstone_light", BlockSoundGroup.DEEPSLATE, () -> SpectrumTextures.BLACKSLAG_FLAT_LIGHT);
	public static final Block GRANITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("granite_shimmerstone_light", BlockSoundGroup.STONE, () -> ModelIds.getBlockModelId(POLISHED_GRANITE));
	public static final Block DIORITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("diorite_shimmerstone_light", BlockSoundGroup.STONE, () -> ModelIds.getBlockModelId(POLISHED_DIORITE));
	public static final Block ANDESITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("andesite_shimmerstone_light", BlockSoundGroup.STONE, () -> ModelIds.getBlockModelId(POLISHED_ANDESITE));
	
	// CRYSTALLARIEUM
	private static Settings crystallarieumGrowable(Block baseBlock) {
		return AbstractBlock.Settings.copy(baseBlock).strength(1.5F).nonOpaque().solid().requiresTool().pistonBehavior(PistonBehavior.DESTROY);
	}
	
	public static final Block SMALL_COAL_BUD = register(cluster(blockWithItem("small_coal_bud", new SpectrumClusterBlock(crystallarieumGrowable(COAL_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_COAL_BUD = register(cluster(blockWithItem("large_coal_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_COAL_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block COAL_CLUSTER = register(cluster(blockWithItem("coal_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_COAL_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_IRON_BUD = register(cluster(blockWithItem("small_iron_bud", new SpectrumClusterBlock(crystallarieumGrowable(IRON_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_IRON_BUD = register(cluster(blockWithItem("large_iron_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_IRON_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block IRON_CLUSTER = register(cluster(blockWithItem("iron_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_IRON_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_GOLD_BUD = register(cluster(blockWithItem("small_gold_bud", new SpectrumClusterBlock(crystallarieumGrowable(GOLD_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_GOLD_BUD = register(cluster(blockWithItem("large_gold_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_GOLD_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block GOLD_CLUSTER = register(cluster(blockWithItem("gold_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_GOLD_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_DIAMOND_BUD = register(cluster(blockWithItem("small_diamond_bud", new SpectrumClusterBlock(crystallarieumGrowable(DIAMOND_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_DIAMOND_BUD = register(cluster(blockWithItem("large_diamond_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_DIAMOND_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block DIAMOND_CLUSTER = register(cluster(blockWithItem("diamond_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_DIAMOND_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_EMERALD_BUD = register(cluster(blockWithItem("small_emerald_bud", new SpectrumClusterBlock(crystallarieumGrowable(EMERALD_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_EMERALD_BUD = register(cluster(blockWithItem("large_emerald_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_EMERALD_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block EMERALD_CLUSTER = register(cluster(blockWithItem("emerald_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_EMERALD_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_REDSTONE_BUD = register(cluster(blockWithItem("small_redstone_bud", new SpectrumClusterBlock(crystallarieumGrowable(REDSTONE_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_REDSTONE_BUD = register(cluster(blockWithItem("large_redstone_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_REDSTONE_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block REDSTONE_CLUSTER = register(cluster(blockWithItem("redstone_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_REDSTONE_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.RED), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_LAPIS_BUD = register(cluster(blockWithItem("small_lapis_bud", new SpectrumClusterBlock(crystallarieumGrowable(LAPIS_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.PURPLE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_LAPIS_BUD = register(cluster(blockWithItem("large_lapis_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_LAPIS_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.PURPLE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LAPIS_CLUSTER = register(cluster(blockWithItem("lapis_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_LAPIS_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.PURPLE), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_COPPER_BUD = register(cluster(blockWithItem("small_copper_bud", new SpectrumClusterBlock(crystallarieumGrowable(COPPER_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_COPPER_BUD = register(cluster(blockWithItem("large_copper_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_COPPER_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block COPPER_CLUSTER = register(cluster(blockWithItem("copper_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_COPPER_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_QUARTZ_BUD = register(cluster(blockWithItem("small_quartz_bud", new SpectrumClusterBlock(crystallarieumGrowable(QUARTZ_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_QUARTZ_BUD = register(cluster(blockWithItem("large_quartz_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_QUARTZ_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block QUARTZ_CLUSTER = register(cluster(blockWithItem("quartz_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_QUARTZ_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_NETHERITE_SCRAP_BUD = register(cluster(blockWithItem("small_netherite_scrap_bud", new SpectrumClusterBlock(crystallarieumGrowable(ANCIENT_DEBRIS), SpectrumClusterBlock.GrowthStage.SMALL), IS.of().fireproof(), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_NETHERITE_SCRAP_BUD = register(cluster(blockWithItem("large_netherite_scrap_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_NETHERITE_SCRAP_BUD), SpectrumClusterBlock.GrowthStage.LARGE), IS.of().fireproof(), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block NETHERITE_SCRAP_CLUSTER = register(cluster(blockWithItem("netherite_scrap_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_NETHERITE_SCRAP_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), IS.of().fireproof(), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_ECHO_BUD = register(cluster(blockWithItem("small_echo_bud", new SpectrumClusterBlock(crystallarieumGrowable(SCULK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_ECHO_BUD = register(cluster(blockWithItem("large_echo_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_ECHO_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block ECHO_CLUSTER = register(cluster(blockWithItem("echo_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_ECHO_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_GLOWSTONE_BUD = register(cluster(blockWithItem("small_glowstone_bud", new SpectrumClusterBlock(crystallarieumGrowable(GLOWSTONE).luminance(state -> 4), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_GLOWSTONE_BUD = register(cluster(blockWithItem("large_glowstone_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_GLOWSTONE_BUD).luminance(state -> 8), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block GLOWSTONE_CLUSTER = register(cluster(blockWithItem("glowstone_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_GLOWSTONE_BUD).luminance(state -> 14), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block SMALL_PRISMARINE_BUD = register(cluster(blockWithItem("small_prismarine_bud", new SpectrumClusterBlock(crystallarieumGrowable(SCULK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block LARGE_PRISMARINE_BUD = register(cluster(blockWithItem("large_prismarine_bud", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_PRISMARINE_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static final Block PRISMARINE_CLUSTER = register(cluster(blockWithItem("prismarine_cluster", new SpectrumClusterBlock(AbstractBlock.Settings.copy(SMALL_PRISMARINE_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	
	public static final Block PURE_COAL_BLOCK = register(simple(burnable(blockWithItem("pure_coal_block", new Block(AbstractBlock.Settings.copy(COAL_BLOCK)), InkColors.BROWN), 32000)));
	public static final Block PURE_IRON_BLOCK = register(simple(blockWithItem("pure_iron_block", new Block(AbstractBlock.Settings.copy(IRON_BLOCK)), InkColors.BROWN)));
	public static final Block PURE_GOLD_BLOCK = register(simple(blockWithItem("pure_gold_block", new Block(AbstractBlock.Settings.copy(GOLD_BLOCK)), InkColors.BROWN)));
	public static final Block PURE_DIAMOND_BLOCK = register(simple(blockWithItem("pure_diamond_block", new Block(AbstractBlock.Settings.copy(DIAMOND_BLOCK)), InkColors.CYAN)));
	public static final Block PURE_EMERALD_BLOCK = register(simple(blockWithItem("pure_emerald_block", new Block(AbstractBlock.Settings.copy(EMERALD_BLOCK)), InkColors.CYAN)));
	public static final Block PURE_REDSTONE_BLOCK = register(simple(blockWithItem("pure_redstone_block", new PureRedstoneBlock(AbstractBlock.Settings.copy(REDSTONE_BLOCK)), InkColors.RED)));
	public static final Block PURE_LAPIS_BLOCK = register(simple(blockWithItem("pure_lapis_block", new Block(AbstractBlock.Settings.copy(LAPIS_BLOCK)), InkColors.PURPLE)));
	public static final Block PURE_COPPER_BLOCK = register(simple(blockWithItem("pure_copper_block", new Block(AbstractBlock.Settings.copy(COPPER_BLOCK)), InkColors.BROWN)));
	public static final Block PURE_QUARTZ_BLOCK = register(simple(blockWithItem("pure_quartz_block", new Block(AbstractBlock.Settings.copy(QUARTZ_BLOCK)), InkColors.BROWN)));
	public static final Block PURE_GLOWSTONE_BLOCK = register(simple(blockWithItem("pure_glowstone_block", new Block(AbstractBlock.Settings.copy(GLOWSTONE)), InkColors.YELLOW)));
	public static final Block PURE_PRISMARINE_BLOCK = register(simple(blockWithItem("pure_prismarine_block", new Block(AbstractBlock.Settings.copy(PRISMARINE)), InkColors.CYAN)));
	public static final Block PURE_NETHERITE_SCRAP_BLOCK = register(simple(blockWithItem("pure_netherite_scrap_block", new Block(AbstractBlock.Settings.copy(ANCIENT_DEBRIS)), IS.of().fireproof(), InkColors.BROWN)));
	public static final Block PURE_ECHO_BLOCK = register(simple(blockWithItem("pure_echo_block", new Block(AbstractBlock.Settings.copy(DIAMOND_BLOCK)), InkColors.BROWN)));
	
	private static Settings preservationBlock() {
		return settings(MapColor.LIGHT_BLUE_GRAY, BlockSoundGroup.STONE, -1.0F, 3600000.0F).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(SpectrumBlocks::never).solid();
	}
	
	public static final Block PRESERVATION_CONTROLLER = register(cutout(singleton(blockWithItem("preservation_controller", new PreservationControllerBlock(preservationBlock().luminance(state -> 1).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always)), InkColors.BLUE), ModelIds::getBlockModelId)).withPredefinedItemModel());
	public static final Block DIKE_GATE = register(translucent(simple(blockWithItem("dike_gate", new DikeGateBlock(preservationBlock().luminance(state -> 3).sounds(BlockSoundGroup.GLASS).nonOpaque().emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never)), InkColors.BLUE))));
	public static final Block DREAM_GATE = register(translucent(simple(blockWithItem("dream_gate", new DreamGateBlock(preservationBlock().luminance(state -> 3).sounds(BlockSoundGroup.GLASS).nonOpaque().emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never)), InkColors.BLUE))));
	public static final Block INVISIBLE_WALL = register(translucent(singleton(blockWithItem("invisible_wall", new InvisibleWallBlock(preservationBlock().luminance(state -> 3).sounds(BlockSoundGroup.GLASS).nonOpaque().blockVision(SpectrumBlocks::never)), InkColors.BLUE), SpectrumTexturedModels.particle(b -> GLASS, ""))).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, ETHEREAL_PLATFORM)));
	public static final Block PRESERVATION_CHEST = register(singleton(blockWithItem("preservation_chest", new TreasureChestBlock(preservationBlock()), InkColors.BLUE), ModelIds::getBlockModelId));
	
	public static final Block DOWNSTONE = register(simple(blockWithItem("downstone", new Block(preservationBlock()), InkColors.BLUE))); // "raw" preservation stone, used in the Deeper Down bottom in place of bedrock
	
	public static final Block PRESERVATION_STONE = register(blockWithItem("preservation_stone", new Block(preservationBlock()), InkColors.BLUE).withBlockModel((ctx, block) -> {
		List<Identifier> modelIds = new ArrayList<>();
		int[] tops = new int[]{0, 3, 1, 1, 2, 2, 0, 3, 1, 2, 3};
		modelIds.add(SpectrumTexturedModels.cubeBottomTop(b -> b, "", b -> b, "_top_" + tops[0], b -> b, "_bottom").upload(block, ctx.modelCollector));
		for (int i = 1; i <= 10; i++) modelIds.add(SpectrumTexturedModels.cubeBottomTop(b -> b, "_" + i, b -> b, "_top_" + tops[i], b -> b, "_bottom").upload(block, "_" + i, ctx.modelCollector));
		List<BlockStateVariant> variants = new ArrayList<>();
		for (VariantSettings.Rotation rotation : VariantSettings.Rotation.values()) {
			variants.add(SpectrumModelHelper.createModelVariant(modelIds.getFirst()).put(VariantSettings.WEIGHT, 10));
			if (rotation != VariantSettings.Rotation.R0) variants.getLast().put(VariantSettings.Y, rotation);
			for (int i = 1; i <= 10; i++) {
				variants.add(SpectrumModelHelper.createModelVariant(modelIds.get(i)));
				if (rotation != VariantSettings.Rotation.R0) variants.getLast().put(VariantSettings.Y, rotation);
			}
		}
		return VariantsBlockStateSupplier.create(block, variants.toArray(BlockStateVariant[]::new));
	}));
	public static final Block PRESERVATION_STAIRS = register(blockWithItem("preservation_stairs", new StairsBlock(PRESERVATION_STONE.getDefaultState(), preservationBlock()), InkColors.BLUE));
	public static final Block PRESERVATION_SLAB = register(blockWithItem("preservation_slab", new SlabBlock(preservationBlock()), InkColors.BLUE));
	public static final Block PRESERVATION_WALL = register(blockWithItem("preservation_wall", new WallBlock(preservationBlock()), InkColors.BLUE));
	public static final BlockFamily PRESERVATION_STONE_FAMILY = SpectrumModelHelper.registerBlockFamilyExceptBase(new BlockFamily.Builder(PRESERVATION_STONE).stairs(PRESERVATION_STAIRS).slab(PRESERVATION_SLAB).wall(PRESERVATION_WALL).build(), TexturedModel.CUBE_ALL);
	
	public static final Block POWDER_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("powder_chiseled_preservation_stone", new Block(preservationBlock().luminance(state -> 2)), InkColors.BLUE), SpectrumTexturedModels.cubeColumn(b -> b, "", b -> PRESERVATION_STONE, "_top_generic")));
	public static final Block DIKE_CHISELED_PRESERVATION_STONE = register(simple(blockWithItem("dike_chiseled_preservation_stone", new Block(preservationBlock().luminance(state -> 6)), InkColors.BLUE)));
	public static final Block DREAM_CHISELED_PRESERVATION_STONE = register(simple(blockWithItem("dream_chiseled_preservation_stone", new Block(preservationBlock().luminance(state -> 6)), InkColors.BLUE)));
	public static final Block DEEP_LIGHT_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("deep_light_chiseled_preservation_stone", new DeepLightBlock(preservationBlock().luminance(state -> 2)), InkColors.BLUE), SpectrumTexturedModels.cubeColumn(b -> b, "", b -> PRESERVATION_STONE, "_top_generic")));
	
	//TODO not sure which is correct, but this should probably be renamed
	public static final Block TREASURE_ITEM_BOWL = register(cutout(singleton(blockWithItem("item_bowl_enlightenment", new TreasureItemBowlBlock(preservationBlock().nonOpaque().solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never)), InkColors.BLUE), TexturedModel.makeFactory(b -> new TextureMap().put(TextureKey.SIDE, TextureMap.getSubId(b, "_side")).put(TextureKey.TOP, TextureMap.getSubId(b, "_top")).put(TextureKey.BOTTOM, locate("block/item_bowl_preservation_bottom")).put(SpectrumTextureKeys.INNER, locate("block/item_bowl_preservation_bottom")), SpectrumModels.BOWL))));
	
	public static final Block DIKE_GATE_FOUNTAIN = register(defaultUpFacing(blockWithItem("dike_gate_fountain", new SpectrumFacingBlock(preservationBlock()), InkColors.BLUE), SpectrumTexturedModels.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> PRESERVATION_STONE, "", b -> PRESERVATION_STONE, "")));
	public static final Block PRESERVATION_BRICKS = register(simple(blockWithItem("preservation_bricks", new Block(preservationBlock()), InkColors.BLUE)));
	public static final Block SHIMMERING_PRESERVATION_BRICKS = register(simple(blockWithItem("shimmering_preservation_bricks", new Block(preservationBlock().luminance(s -> 5)), InkColors.BLUE)));
	public static final Block COURIER_STATUE = register(cutout(blockWithItem("courier_statue", new StatueBlock(preservationBlock()), InkColors.BLUE)).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_top")).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createNorthDefaultHorizontalFacingVariantMap()).coordinate(BlockStateVariantMap.create(StatueBlock.HALF).register(DoubleBlockHalf.LOWER, SpectrumModelHelper.createModelVariant(block, "_bottom")).register(DoubleBlockHalf.UPPER, SpectrumModelHelper.createModelVariant(block, "_top")))));
	public static final Block MANXI = register(singleton(block("manxi", new ManxiBlock(preservationBlock().nonOpaque().noCollision().dropsNothing())), (Function<Block, Identifier>) b -> SpectrumModels.MOB_HEAD));
	
	public static final Block BLACK_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("black_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.BLACK), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BLUE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("blue_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.BLUE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block BROWN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("brown_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.BROWN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block CYAN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("cyan_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.CYAN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block GRAY_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("gray_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.GRAY), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block GREEN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("green_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.GREEN), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block LIGHT_BLUE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("light_blue_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.LIGHT_BLUE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block LIGHT_GRAY_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("light_gray_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.LIGHT_GRAY), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block LIME_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("lime_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.LIME), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block MAGENTA_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("magenta_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.MAGENTA), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block ORANGE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("orange_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.ORANGE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block PINK_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("pink_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.PINK), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block PURPLE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("purple_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.PURPLE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block RED_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("red_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.RED), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block WHITE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("white_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.WHITE), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	public static final Block YELLOW_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("yellow_chiseled_preservation_stone", new Block(preservationBlock()), InkColors.YELLOW), TexturedModel.END_FOR_TOP_CUBE_COLUMN));
	
	public static final Block PRESERVATION_GLASS = register(translucent(simple(blockWithItem("preservation_glass", new TransparentBlock(preservationBlock().sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never)), InkColors.BLUE))));
	public static final Block TINTED_PRESERVATION_GLASS = register(translucent(simple(blockWithItem("tinted_preservation_glass", new TintedGlassBlock(AbstractBlock.Settings.copy(PRESERVATION_GLASS)), InkColors.BLUE))));
	public static final Block PRESERVATION_ROUNDEL = register(singleton(blockWithItem("preservation_roundel", new PreservationRoundelBlock(preservationBlock().nonOpaque().solid()), InkColors.BLUE), SpectrumTexturedModels.ROUNDEL));
	public static final Block PRESERVATION_BLOCK_DETECTOR = register(blockWithItem("preservation_block_detector", new PreservationBlockDetectorBlock(preservationBlock()), InkColors.BLUE).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.complexOrientable(b -> b, "_side", b -> b, "_top", b -> PRESERVATION_STONE, "_top_generic", b -> b, "_front", b -> b, "_back", b -> b, "_side").upload(block, ctx.modelCollector))).coordinate(SpectrumModelHelper.createNorthDefaultFacingVariantMap())));
	
	private static Settings shootingStar() {
		return AbstractBlock.Settings.copy(STONE).nonOpaque();
	}
	
	public static final ShootingStarBlock GLISTERING_SHOOTING_STAR = register(cutout(singleton(blockWithItem("glistering_shooting_star", new ShootingStarBlock(shootingStar(), ShootingStar.Type.GLISTERING), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModels.SHOOTING_STAR)));
	public static final ShootingStarBlock FIERY_SHOOTING_STAR = register(cutout(singleton(blockWithItem("fiery_shooting_star", new ShootingStarBlock(shootingStar(), ShootingStar.Type.FIERY), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModels.SHOOTING_STAR)));
	public static final ShootingStarBlock COLORFUL_SHOOTING_STAR = register(cutout(singleton(blockWithItem("colorful_shooting_star", new ShootingStarBlock(shootingStar(), ShootingStar.Type.COLORFUL), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModels.SHOOTING_STAR)));
	public static final ShootingStarBlock PRISTINE_SHOOTING_STAR = register(cutout(singleton(blockWithItem("pristine_shooting_star", new ShootingStarBlock(shootingStar(), ShootingStar.Type.PRISTINE), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModels.SHOOTING_STAR)));
	public static final ShootingStarBlock GEMSTONE_SHOOTING_STAR = register(cutout(singleton(blockWithItem("gemstone_shooting_star", new ShootingStarBlock(shootingStar(), ShootingStar.Type.GEMSTONE), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModels.SHOOTING_STAR)));
	public static final Block STARDUST_BLOCK = register(simple(blockWithItem("stardust_block", new ColoredFallingBlock(new ColorCode(DyeColor.PURPLE.getFireworkColor()), AbstractBlock.Settings.copy(SAND).mapColor(MapColor.PURPLE)), IS.of(1, Rarity.UNCOMMON), InkColors.BLACK)));
	
	public static final Block INCANDESCENT_AMALGAM = register(cutout(singleton(blockWithItem("incandescent_amalgam", new IncandescentAmalgamBlock(AbstractBlock.Settings.create().breakInstantly().nonOpaque()), block -> new IncandescentAmalgamItem(block, IS.of(16).food(SpectrumFoodComponents.INCANDESCENT_AMALGAM)), InkColors.RED), ModelIds::getBlockModelId)).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel));
	
	private static Settings idol(BlockSoundGroup soundGroup) {
		return settings(MapColor.TERRACOTTA_WHITE, soundGroup, 3.0F).requiresTool().nonOpaque();
	}
	
	public static final Block AXOLOTL_IDOL = register(idol(blockWithItem("axolotl_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.AXOLOTL_IDOL), ParticleTypes.HEART, StatusEffects.REGENERATION, 0, 100), InkColors.PINK))); // heals 2 hp / 1 hear
	public static final Block BAT_IDOL = register(idol(blockWithItem("bat_idol", new AoEStatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.BAT_IDOL), ParticleTypes.INSTANT_EFFECT, StatusEffects.GLOWING, 0, 200, 8), InkColors.PINK)));
	public static final Block BEE_IDOL = register(idol(blockWithItem("bee_idol", new BonemealingIdolBlock(idol(SpectrumBlockSoundGroups.BEE_IDOL), ParticleTypes.DRIPPING_HONEY), InkColors.PINK)));
	public static final Block BLAZE_IDOL = register(idol(blockWithItem("blaze_idol", new FirestarterIdolBlock(idol(SpectrumBlockSoundGroups.BLAZE_IDOL), ParticleTypes.FLAME), InkColors.PINK)));
	public static final Block CAT_IDOL = register(idol(blockWithItem("cat_idol", new FallDamageNegatingIdolBlock(idol(SpectrumBlockSoundGroups.CAT_IDOL), ParticleTypes.ENCHANTED_HIT), InkColors.PINK)));
	public static final Block CHICKEN_IDOL = register(idol(blockWithItem("chicken_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.CHICKEN_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.SLOW_FALLING, 0, 100), InkColors.PINK)));
	public static final Block COW_IDOL = register(idol(blockWithItem("cow_idol", new MilkingIdolBlock(idol(SpectrumBlockSoundGroups.COW_IDOL), ParticleTypes.ENCHANTED_HIT, 6), InkColors.PINK)));
	public static final Block CREEPER_IDOL = register(idol(blockWithItem("creeper_idol", new ExplosionIdolBlock(idol(SpectrumBlockSoundGroups.CREEPER_IDOL), ParticleTypes.EXPLOSION, 3, false, Explosion.DestructionType.DESTROY), InkColors.PINK)));
	public static final Block ENDER_DRAGON_IDOL = register(idol(blockWithItem("ender_dragon_idol", new ProjectileIdolBlock(idol(SpectrumBlockSoundGroups.ENDER_DRAGON_IDOL), ParticleTypes.DRAGON_BREATH, EntityType.DRAGON_FIREBALL, SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER, world);
			markerEntity.setPos(position.getX(), position.getY(), position.getZ());
			
			Vec3d targetPosition = Vec3d.ofCenter(mobBlockPos.offset(side, 50));
			var velocity = targetPosition.subtract(markerEntity.getPos());
			
			DragonFireballEntity entity = new DragonFireballEntity(world, markerEntity, velocity);
			
			markerEntity.discard();
			return entity;
		}
	}, InkColors.PINK)));
	public static final Block ENDERMAN_IDOL = register(idol(blockWithItem("enderman_idol", new RandomTeleportingIdolBlock(idol(SpectrumBlockSoundGroups.ENDERMAN_IDOL), ParticleTypes.REVERSE_PORTAL, 16, 16), InkColors.PINK)));
	public static final Block ENDERMITE_IDOL = register(idol(blockWithItem("endermite_idol", new LineTeleportingIdolBlock(idol(SpectrumBlockSoundGroups.ENDERMITE_IDOL), ParticleTypes.REVERSE_PORTAL, 16), InkColors.PINK)));
	public static final Block EVOKER_IDOL = register(idol(blockWithItem("evoker_idol", new EntitySummoningIdolBlock(idol(SpectrumBlockSoundGroups.EVOKER_IDOL), ParticleTypes.ANGRY_VILLAGER, EntityType.VEX) {
		@Override
		public void afterSummon(ServerWorld world, Entity entity) {
			((VexEntity) entity).setLifeTicks(20 * (30 + world.random.nextInt(90)));
		}
	}, InkColors.PINK)));
	public static final Block FISH_IDOL = register(idol(blockWithItem("fish_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.FISH_IDOL), ParticleTypes.SPLASH, StatusEffects.WATER_BREATHING, 0, 200), InkColors.PINK)));
	public static final Block FOX_IDOL = register(idol(blockWithItem("fox_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.FOX_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.HASTE, 0, 200), InkColors.PINK)));
	public static final Block GHAST_IDOL = register(idol(blockWithItem("ghast_idol", new ProjectileIdolBlock(idol(SpectrumBlockSoundGroups.GHAST_IDOL), ParticleTypes.SMOKE, EntityType.FIREBALL, SoundEvents.ENTITY_GHAST_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER, world);
			markerEntity.setPos(position.getX(), position.getY(), position.getZ());
			
			Vec3d targetPosition = Vec3d.ofCenter(mobBlockPos.offset(side, 50));
			var velocity = targetPosition.subtract(markerEntity.getPos());
			
			FireballEntity entity = new FireballEntity(world, markerEntity, velocity, 1);
			
			markerEntity.discard();
			return entity;
		}
	}, InkColors.PINK)));
	public static final Block GLOW_SQUID_IDOL = register(idol(blockWithItem("glow_squid_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.GLOW_SQUID_IDOL), ParticleTypes.GLOW_SQUID_INK, StatusEffects.GLOWING, 0, 200), InkColors.PINK)));
	public static final Block GOAT_IDOL = register(idol(blockWithItem("goat_idol", new KnockbackIdolBlock(idol(SpectrumBlockSoundGroups.GOAT_IDOL), ParticleTypes.ENCHANTED_HIT, 5.0F, 0.5F), InkColors.PINK))); // knocks mostly sideways
	public static final Block GUARDIAN_IDOL = register(idol(blockWithItem("guardian_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.GUARDIAN_IDOL), ParticleTypes.BUBBLE, StatusEffects.MINING_FATIGUE, 2, 200), InkColors.PINK)));
	public static final Block HORSE_IDOL = register(idol(blockWithItem("horse_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.HORSE_IDOL), ParticleTypes.INSTANT_EFFECT, StatusEffects.STRENGTH, 0, 100), InkColors.PINK)));
	public static final Block ILLUSIONER_IDOL = register(idol(blockWithItem("illusioner_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.ILLUSIONER_IDOL), ParticleTypes.ANGRY_VILLAGER, StatusEffects.INVISIBILITY, 0, 100), InkColors.PINK)));
	public static final Block OCELOT_IDOL = register(idol(blockWithItem("ocelot_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.OCELOT_IDOL), ParticleTypes.INSTANT_EFFECT, StatusEffects.NIGHT_VISION, 0, 100), InkColors.PINK)));
	public static final Block PARROT_IDOL = register(idol(blockWithItem("parrot_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.PARROT_IDOL), ParticleTypes.INSTANT_EFFECT, StatusEffects.ABSORPTION, 0, 100), InkColors.PINK)));
	public static final Block PHANTOM_IDOL = register(idol(blockWithItem("phantom_idol", new InsomniaIdolBlock(idol(SpectrumBlockSoundGroups.PHANTOM_IDOL), ParticleTypes.POOF, 24000), InkColors.PINK))); // +1 ingame day without sleep
	public static final Block PIG_IDOL = register(idol(blockWithItem("pig_idol", new FeedingIdolBlock(idol(SpectrumBlockSoundGroups.PIG_IDOL), ParticleTypes.INSTANT_EFFECT, 6), InkColors.PINK)));
	public static final Block PIGLIN_IDOL = register(idol(blockWithItem("piglin_idol", new PiglinTradeIdolBlock(idol(SpectrumBlockSoundGroups.PIGLIN_IDOL), ParticleTypes.HEART), InkColors.PINK)));
	public static final Block POLAR_BEAR_IDOL = register(idol(blockWithItem("polar_bear_idol", new FreezingIdolBlock(idol(SpectrumBlockSoundGroups.POLAR_BEAR_IDOL), ParticleTypes.SNOWFLAKE), InkColors.PINK)));
	public static final Block PUFFERFISH_IDOL = register(idol(blockWithItem("pufferfish_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.PUFFERFISH_IDOL), ParticleTypes.SPLASH, StatusEffects.NAUSEA, 0, 200), InkColors.PINK)));
	public static final Block RABBIT_IDOL = register(idol(blockWithItem("rabbit_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.RABBIT_IDOL), ParticleTypes.INSTANT_EFFECT, StatusEffects.JUMP_BOOST, 3, 100), InkColors.PINK)));
	public static final Block SHEEP_IDOL = register(idol(blockWithItem("sheep_idol", new ShearingIdolBlock(idol(SpectrumBlockSoundGroups.SHEEP_IDOL), ParticleTypes.ENCHANTED_HIT, 6), InkColors.PINK)));
	public static final Block SHULKER_IDOL = register(idol(blockWithItem("shulker_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.SHULKER_IDOL), ParticleTypes.END_ROD, StatusEffects.LEVITATION, 0, 100), InkColors.PINK)));
	public static final Block SILVERFISH_IDOL = register(idol(blockWithItem("silverfish_idol", new SilverfishInsertingIdolBlock(idol(SpectrumBlockSoundGroups.SILVERFISH_IDOL), ParticleTypes.EXPLOSION), InkColors.PINK)));
	public static final Block SKELETON_IDOL = register(idol(blockWithItem("skeleton_idol", new ProjectileIdolBlock(idol(SpectrumBlockSoundGroups.SKELETON_IDOL), ParticleTypes.INSTANT_EFFECT, EntityType.ARROW, SoundEvents.ENTITY_ARROW_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			ArrowEntity arrowEntity = new ArrowEntity(world, position.getX(), position.getY(), position.getZ(), ItemStack.EMPTY, null);
			arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			return arrowEntity;
		}
	}, InkColors.PINK)));
	public static final Block SLIME_IDOL = register(idol(blockWithItem("slime_idol", new SlimeSizingIdolBlock(idol(SpectrumBlockSoundGroups.SLIME_IDOL), ParticleTypes.ITEM_SLIME, 6, 8), InkColors.PINK)));
	public static final Block SNOW_GOLEM_IDOL = register(idol(blockWithItem("snow_golem_idol", new ProjectileIdolBlock(idol(SpectrumBlockSoundGroups.SNOW_GOLEM_IDOL), ParticleTypes.SNOWFLAKE, EntityType.SNOWBALL, SoundEvents.ENTITY_ARROW_SHOOT, 3.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			world.playSound(null, mobBlockPos.getX(), mobBlockPos.getY(), mobBlockPos.getZ(), SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, SoundCategory.BLOCKS, 1.0F, 0.4F / world.random.nextFloat() * 0.4F + 0.8F);
			return new SnowballEntity(world, position.getX(), position.getY(), position.getZ());
		}
	}, InkColors.PINK)));
	public static final Block SPIDER_IDOL = register(idol(blockWithItem("spider_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.SPIDER_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.POISON, 0, 100), InkColors.PINK)));
	public static final Block SQUID_IDOL = register(idol(blockWithItem("squid_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.SQUID_IDOL), ParticleTypes.SQUID_INK, StatusEffects.BLINDNESS, 0, 200), InkColors.PINK)));
	public static final Block STRAY_IDOL = register(idol(blockWithItem("stray_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.STRAY_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.SLOWNESS, 2, 100), InkColors.PINK)));
	public static final Block STRIDER_IDOL = register(idol(blockWithItem("strider_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.STRIDER_IDOL), ParticleTypes.DRIPPING_LAVA, StatusEffects.FIRE_RESISTANCE, 0, 200), InkColors.PINK)));
	public static final Block TURTLE_IDOL = register(idol(blockWithItem("turtle_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.TURTLE_IDOL), ParticleTypes.DRIPPING_WATER, StatusEffects.RESISTANCE, 1, 200), InkColors.PINK)));
	public static final Block WITCH_IDOL = register(idol(blockWithItem("witch_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.WITCH_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.WEAKNESS, 0, 200), InkColors.PINK)));
	public static final Block WITHER_IDOL = register(idol(blockWithItem("wither_idol", new ExplosionIdolBlock(idol(SpectrumBlockSoundGroups.WITHER_IDOL), ParticleTypes.EXPLOSION, 7.0F, true, Explosion.DestructionType.DESTROY), InkColors.PINK)));
	public static final Block WITHER_SKELETON_IDOL = register(idol(blockWithItem("wither_skeleton_idol", new StatusEffectIdolBlock(idol(SpectrumBlockSoundGroups.WITHER_SKELETON_IDOL), ParticleTypes.ENCHANTED_HIT, StatusEffects.WITHER, 0, 100), InkColors.PINK)));
	public static final Block ZOMBIE_IDOL = register(idol(blockWithItem("zombie_idol", new VillagerConvertingIdolBlock(idol(SpectrumBlockSoundGroups.ZOMBIE_IDOL), ParticleTypes.ENCHANTED_HIT), InkColors.PINK)));
	
	// FLUIDS
	private static Settings fluid(MapColor mapColor) {
		return settings(mapColor, BlockSoundGroup.INTENTIONALLY_EMPTY, 100.0F).replaceable().noCollision().pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid();
	}
	
	public static final Block LIQUID_CRYSTAL = register(singleton(block("liquid_crystal", new LiquidCrystalFluidBlock(SpectrumFluids.LIQUID_CRYSTAL, BLAZING_CRYSTAL.getDefaultState(), fluid(MapColor.DULL_PINK).luminance((state) -> LiquidCrystalFluidBlock.LUMINANCE).replaceable())), SpectrumTexturedModels.particle(b -> b, "_still")));
	public static final Block GOO = register(singleton(block("goo", new GooFluidBlock(SpectrumFluids.GOO, MUD.getDefaultState(), fluid(MapColor.TERRACOTTA_BROWN).replaceable())), SpectrumTexturedModels.particle(b -> b, "_still")));
	public static final Block MIDNIGHT_SOLUTION = register(singleton(block("midnight_solution", new MidnightSolutionFluidBlock(SpectrumFluids.MIDNIGHT_SOLUTION, BLACK_MATERIA.getDefaultState(), fluid(MapColor.DARK_AQUA).replaceable())), SpectrumTexturedModels.particle(b -> b, "_still")));
	public static final Block DRAGONROT = register(singleton(block("dragonrot", new DragonrotFluidBlock(SpectrumFluids.DRAGONROT, BLACKSTONE.getDefaultState(), fluid(MapColor.PALE_PURPLE).luminance((state) -> 15).replaceable())), SpectrumTexturedModels.particle(b -> b, "_still")));
	
	static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}
	
	static boolean always(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
	static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}
	
	public static <T extends Block> T register(BlockRegistrar<T> registrar) {
		return Objects.requireNonNull(registrar.block(), "Attempted to register a null block");
	}
	
	public static <T extends Block> BlockRegistrar<T> block(String name, T block) {
		return new BlockRegistrar<T>(name).withBlock(block);
	}
	
	public static <T extends Block> BlockRegistrar<T> block(String name, Supplier<T> blockFactory) {
		return new BlockRegistrar<T>(name).withBlock(blockFactory);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, T block, InkColor color) {
		return blockWithItem(name, block, IS.of(), color);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, T block, Item.Settings settings, InkColor color) {
		return blockWithItem(name, block, b -> new BlockItem(b, settings), color);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, T block, Function<T, Item> itemFactory, InkColor color) {
		return block(name, block).withItem(itemFactory, color);
	}
	
	public static <T extends Block> BlockRegistrar<T> cutout(BlockRegistrar<T> registrar) {
		return registrar.withCutoutRenderLayer();
	}
	
	public static <T extends Block> BlockRegistrar<T> mippedCutout(BlockRegistrar<T> registrar) {
		return registrar.withMippedCutoutRenderLayer();
	}
	
	public static <T extends Block> BlockRegistrar<T> translucent(BlockRegistrar<T> registrar) {
		return registrar.withTranslucentRenderLayer();
	}
	
	public static <T extends Block> BlockRegistrar<T> simple(BlockRegistrar<T> registrar) {
		return singleton(registrar, TexturedModel.CUBE_ALL);
	}
	
	public static <T extends Block> BlockRegistrar<T> simpleMirrored(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createMirroredVariantsSupplier(block, TexturedModel.CUBE_ALL, TexturedModel.CUBE_MIRRORED_ALL, ctx.modelCollector));
	}
	
	public static <T extends Block> BlockRegistrar<T> singleton(BlockRegistrar<T> registrar, TexturedModel.Factory factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory));
	}
	
	public static <T extends Block> BlockRegistrar<T> singleton(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdSupplier) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdSupplier.apply(block)));
	}
	
	public static <T extends Block> BlockRegistrar<T> parented(BlockRegistrar<T> registrar, UnaryOperator<Block> parent) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, parent.apply(block))).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelIds.getBlockModelId(parent.apply(block))));
	}
	
	public static <T extends Block> BlockRegistrar<T> axisRotated(BlockRegistrar<T> registrar, TexturedModel.Factory factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory).coordinate(SpectrumModelHelper.createAxisRotatedVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultUpFacing(BlockRegistrar<T> registrar, TexturedModel.Factory factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory).coordinate(SpectrumModelHelper.createUpDefaultFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultUpFacing(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).coordinate(SpectrumModelHelper.createUpDefaultFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultNorthHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).coordinate(SpectrumModelHelper.createNorthDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultSouthHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).coordinate(SpectrumModelHelper.createSouthDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultWestHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).coordinate(SpectrumModelHelper.createWestDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultEastHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, Identifier> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).coordinate(SpectrumModelHelper.createEastDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> cross(BlockRegistrar<T> registrar) {
		return cutout(registrar).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumTexturedModels.cross(b -> b, "").upload(block, ctx.modelCollector)));
	}
	
	public static <T extends Block> BlockRegistrar<T> simplePlant(BlockRegistrar<T> registrar) {
		return cross(registrar).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel);
	}
	
	public static <T extends FlowerPotBlock> BlockRegistrar<T> pottedPlant(BlockRegistrar<T> registrar, boolean tinted) {
		return cutout(registrar).withBlockModel((ctx, block) -> SpectrumModelHelper.pottedPlantBlockModel(ctx, block, tinted));
	}
	
	public static <T extends Block> BlockRegistrar<T> log(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel(SpectrumModelHelper::logBlockModel);
	}
	
	public static <T extends Block> BlockRegistrar<T> wood(BlockRegistrar<T> registrar, Block logBlock) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.woodBlockModel(ctx, block, logBlock));
	}
	
	public static <T extends Block> BlockRegistrar<T> snowy(BlockRegistrar<T> registrar, TexturedModel.Factory base, TexturedModel.Factory snowy) {
		return registrar.withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.SNOWY).register(false, SpectrumModelHelper.createHorizontalRotationVariantList(base.upload(block, ctx.modelCollector))).register(true, SpectrumModelHelper.createHorizontalRotationVariantList(snowy.upload(block, "_snow", ctx.modelCollector)))));
	}
	
	public static <T extends Block> BlockRegistrar<T> redstoneLamp(BlockRegistrar<T> registrar) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_off")).withBlockModel((ctx, block) -> {
			Identifier off = SpectrumTexturedModels.cubeAll(b -> b, "_off").upload(block, "_off", ctx.modelCollector);
			Identifier on = SpectrumTexturedModels.cubeAll(b -> b, "_on").upload(block, "_on", ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.LIT, on, off));
		});
	}
	
	public static <T extends Block> BlockRegistrar<T> barrellike(BlockRegistrar<T> registrar, UnaryOperator<Block> bottomBlock, String bottomSuffix) {
		return registrar.withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createUpDefaultFacingVariantMap()).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.OPEN, SpectrumTexturedModels.cubeBottomTop(b -> b, "_side", b -> b, "_top_open", bottomBlock, bottomSuffix).upload(block, "_open", ctx.modelCollector), SpectrumTexturedModels.cubeBottomTop(b -> b, "_side", b -> b, "_top", bottomBlock, bottomSuffix).upload(block, ctx.modelCollector))));
	}
	
	public static <T extends Block> BlockRegistrar<T> spiritVines(BlockRegistrar<T> registrar) {
		return cutout(registrar).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(SpiritVine.CRYSTALS, SpectrumTexturedModels.cross(b -> b, "_crystals").upload(block, "_crystals", ctx.modelCollector), SpectrumTexturedModels.cross(b -> b, "_none").upload(block, "_none", ctx.modelCollector))));
	}
	
	public static <T extends Block> BlockRegistrar<T> idol(BlockRegistrar<T> registrar) {
		return translucent(registrar).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, SpectrumModels.MOB_BLOCK)).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(IdolBlock.COOLDOWN, SpectrumModels.MOB_BLOCK, SpectrumModels.MOB_BLOCK_COOLDOWN)));
	}
	
	public static <T extends Block> BlockRegistrar<T> pedestal(BlockRegistrar<T> registrar) {
		return cutout(singleton(registrar, TexturedModel.makeFactory(b -> new TextureMap().put(SpectrumTextureKeys.PEDESTAL, TextureMap.getId(b)).put(TextureKey.PARTICLE, TextureMap.getSubId(b, "_breaking")), SpectrumModels.PEDESTAL)));
	}
	
	public static <T extends Block> BlockRegistrar<T> sugarStick(BlockRegistrar<T> registrar, UnaryOperator<Block> sugarBlock) {
		return registrar.withItemModel(SpectrumModelHelper::registerItemModel).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.AGE_2).register(age -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModels.sugarStick(age, sugarBlock).upload(block, age.toString(), ctx.modelCollector)))));
	}
	
	public static <T extends Block> BlockRegistrar<T> detector(BlockRegistrar<T> registrar) {
		return burnable(registrar, 300).withBlockModel((ctx, block) -> VariantsBlockStateSupplier.create(block).coordinate(SpectrumModelHelper.createBooleanModelMap(Properties.INVERTED,
				SpectrumModels.SLAB_DETECTOR.upload(block, "_inverted", SpectrumTextureMaps.sideTop(block, "_side", block, "_inverted_top"), ctx.modelCollector),
				SpectrumModels.SLAB_DETECTOR.upload(block, SpectrumTextureMaps.sideTop(block, "_side", block, "_top"), ctx.modelCollector))));
	}
	
	public static <T extends Block> BlockRegistrar<T> burnable(BlockRegistrar<T> registrar, int burnTicks) {
		return registrar.withBurnTime(burnTicks);
	}
	
	public static <T extends Block> BlockRegistrar<T> orientable(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> {
			Identifier horizontal = Models.ORIENTABLE.upload(block, new TextureMap().put(TextureKey.TOP, TextureMap.getSubId(block, "_top")).put(TextureKey.SIDE, TextureMap.getSubId(block, "_side")).put(TextureKey.FRONT, TextureMap.getSubId(block, "_front")), ctx.modelCollector);
			Identifier vertical = Models.ORIENTABLE_VERTICAL.upload(block, new TextureMap().put(TextureKey.SIDE, TextureMap.getSubId(block, "_top")).put(TextureKey.FRONT, TextureMap.getSubId(block, "_front_vertical")), ctx.modelCollector);
			return VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.FACING).register(Direction.DOWN, SpectrumModelHelper.createModelVariant(vertical).put(VariantSettings.X, VariantSettings.Rotation.R180)).register(Direction.UP, SpectrumModelHelper.createModelVariant(vertical)).register(Direction.NORTH, SpectrumModelHelper.createModelVariant(horizontal)).register(Direction.EAST, SpectrumModelHelper.createModelVariant(horizontal).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.SOUTH, SpectrumModelHelper.createModelVariant(horizontal).put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.WEST, SpectrumModelHelper.createModelVariant(horizontal).put(VariantSettings.Y, VariantSettings.Rotation.R270)));
		});
	}
	
	public static <T extends Block> BlockRegistrar<T> pylon(BlockRegistrar<T> registrar) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_head")).withBlockModel((ctx, block) -> {
			Identifier head = ModelIds.getBlockSubModelId(block, "_head");
			Identifier body = ModelIds.getBlockSubModelId(block, "_body");
			Identifier waist = ModelIds.getBlockSubModelId(block, "_waist");
			Identifier foot = ModelIds.getBlockSubModelId(block, "_foot");
			Identifier end = ModelIds.getBlockSubModelId(block, "_end");
			Identifier pedestal = SpectrumModels.BALCITE_PYLON_PEDESTAL;
			SpectrumModels.BASE_PYLON_BODY.upload(head, SpectrumTextureMaps.sideEnd(head, end), ctx.modelCollector);
			SpectrumModels.BASE_PYLON_BODY.upload(body, SpectrumTextureMaps.sideEnd(body, end), ctx.modelCollector);
			SpectrumModels.BASE_PYLON_BODY.upload(waist, SpectrumTextureMaps.sideEnd(waist, end), ctx.modelCollector);
			SpectrumModels.BASE_PYLON_BODY.upload(foot, SpectrumTextureMaps.sideEnd(foot, end), ctx.modelCollector);
			return MultipartBlockStateSupplier.create(block)
					.with(When.create().set(Properties.FACING, Direction.DOWN).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head).put(VariantSettings.X, VariantSettings.Rotation.R180))
					.with(When.create().set(Properties.FACING, Direction.DOWN).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body).put(VariantSettings.X, VariantSettings.Rotation.R180))
					.with(When.create().set(Properties.FACING, Direction.DOWN).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist).put(VariantSettings.X, VariantSettings.Rotation.R180))
					.with(When.create().set(Properties.FACING, Direction.DOWN).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot).put(VariantSettings.X, VariantSettings.Rotation.R180))
					.with(When.create().set(Properties.FACING, Direction.DOWN).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal).put(VariantSettings.X, VariantSettings.Rotation.R180))
					.with(When.create().set(Properties.FACING, Direction.UP).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head))
					.with(When.create().set(Properties.FACING, Direction.UP).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body))
					.with(When.create().set(Properties.FACING, Direction.UP).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist))
					.with(When.create().set(Properties.FACING, Direction.UP).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot))
					.with(When.create().set(Properties.FACING, Direction.UP).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal))
					.with(When.create().set(Properties.FACING, Direction.NORTH).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head).put(VariantSettings.X, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.NORTH).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body).put(VariantSettings.X, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.NORTH).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist).put(VariantSettings.X, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.NORTH).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot).put(VariantSettings.X, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.NORTH).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal).put(VariantSettings.X, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.SOUTH).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head).put(VariantSettings.X, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.SOUTH).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body).put(VariantSettings.X, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.SOUTH).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist).put(VariantSettings.X, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.SOUTH).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot).put(VariantSettings.X, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.SOUTH).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal).put(VariantSettings.X, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.WEST).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.WEST).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.WEST).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.WEST).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.WEST).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270))
					.with(When.create().set(Properties.FACING, Direction.EAST).set(PylonBlock.SECTION, PylonBlock.Section.HEAD), SpectrumModelHelper.createModelVariant(head).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.EAST).set(PylonBlock.SECTION, PylonBlock.Section.BODY), SpectrumModelHelper.createModelVariant(body).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.EAST).set(PylonBlock.SECTION, PylonBlock.Section.WAIST), SpectrumModelHelper.createModelVariant(waist).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.EAST).set(PylonBlock.SECTION, PylonBlock.Section.FOOT), SpectrumModelHelper.createModelVariant(foot).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))
					.with(When.create().set(Properties.FACING, Direction.EAST).set(PylonBlock.PEDESTAL, true), SpectrumModelHelper.createModelVariant(pedestal).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90));
		});
	}
	
	public static class BlockRegistrar<T extends Block> {
		
		private final Identifier id;
		private boolean hasBlock = false;
		private boolean hasItem = false;
		@Nullable
		private T block = null;
		@Nullable
		private Item item = null;
		
		public BlockRegistrar(String name) {
			this.id = locate(name);
		}
		
		public BlockRegistrar<T> with(Consumer<T> callback) {
			callback.accept(block);
			return this;
		}
		
		public BlockRegistrar<T> withBlock(T block) {
			if (hasBlock) throw new UnsupportedOperationException("Attempted to register two blocks with id " + id);
			hasBlock = true;
			this.block = block;
			COMMON_REGISTRAR.defer(() -> Registry.register(Registries.BLOCK, id, this.block));
			return this;
		}
		
		public BlockRegistrar<T> withBlock(Supplier<T> blockFactory) {
			if (hasBlock) throw new UnsupportedOperationException("Attempted to register two blocks with id " + id);
			hasBlock = true;
			COMMON_REGISTRAR.defer(() -> Registry.register(Registries.BLOCK, id, (block = blockFactory.get())));
			return this;
		}
		
		public BlockRegistrar<T> withItem(Function<T, Item> callback, InkColor color) {
			if (hasItem) throw new UnsupportedOperationException("Attempted to register two items with id " + id);
			hasItem = true;
			COMMON_REGISTRAR.defer(() -> {
				item = callback.apply(block);
				Registry.register(Registries.ITEM, id, item);
				ItemColors.ITEM_COLORS.registerColorMapping(item, color);
			});
			return this;
		}
		
		public BlockRegistrar<T> withCutoutRenderLayer() {
			CLIENT_REGISTRAR.defer(() -> {
				Objects.requireNonNull(block);
				BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
			});
			return this;
		}
		
		public BlockRegistrar<T> withMippedCutoutRenderLayer() {
			CLIENT_REGISTRAR.defer(() -> {
				Objects.requireNonNull(block);
				BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
			});
			return this;
		}
		
		public BlockRegistrar<T> withTranslucentRenderLayer() {
			CLIENT_REGISTRAR.defer(() -> {
				Objects.requireNonNull(block);
				BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
			});
			return this;
		}
		
		public BlockRegistrar<T> withBlockModel(BiFunction<BlockStateModelGenerator, ? super T, BlockStateSupplier> callback) {
			SpectrumModelHelper.BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
				Objects.requireNonNull(block);
				ctx.blockStateCollector.accept(callback.apply(ctx, block));
			});
			return this;
		}
		
		public BlockRegistrar<T> withBlockItemModel(BiConsumer<ItemModelGenerator, ? super T> callback) {
			SpectrumModelHelper.ITEM_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(block);
					callback.accept(ctx, block);
				}
			});
			return this;
		}
		
		public BlockRegistrar<T> withItemModel(BiConsumer<ItemModelGenerator, Item> callback) {
			SpectrumModelHelper.ITEM_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(block);
					callback.accept(ctx, block.asItem());
				}
			});
			return this;
		}
		
		public BlockRegistrar<T> withPredefinedItemModel() {
			SpectrumModelHelper.BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(block);
					ctx.excludeFromSimpleItemModelGeneration(block);
				}
			});
			return this;
		}
		
		public BlockRegistrar<T> withBurnTime(int burnTicks) {
			FUEL_REGISTRAR.defer(() -> {
				if (hasItem) FuelRegistry.INSTANCE.add(block, burnTicks);
			});
			return this;
		}
		
		@Nullable
		public T block() {
			return block;
		}
		
		@Nullable
		public Item item() {
			return item;
		}
		
		public RegistryKey<Block> blockKey() {
			return RegistryKey.of(RegistryKeys.BLOCK, id);
		}
		
		public RegistryKey<Item> itemKey() {
			return RegistryKey.of(RegistryKeys.ITEM, id);
		}
		
	}
	
	public static void register() {
		// All the mob heads
		for (SpectrumSkullType type : SpectrumSkullType.values()) {
			BlockRegistrar<SpectrumSkullBlock> registrar = block(type.asString() + "_head", new SpectrumSkullBlock(type, AbstractBlock.Settings.copy(SKELETON_SKULL).instrument(NoteBlockInstrument.CUSTOM_HEAD))).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, SpectrumModels.SKULL_ITEM)).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumModels.MOB_HEAD));
			Block wallHead = register(block(type.asString() + "_wall_head", new SpectrumWallSkullBlock(type, AbstractBlock.Settings.copy(SKELETON_SKULL).dropsLike(registrar.block()))).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumModels.MOB_HEAD)));
			register(registrar.withItem(block -> new SpectrumSkullBlockItem(block, wallHead, IS.of(), type), InkColors.GRAY));
		}
		
		COMMON_REGISTRAR.flush();
	}
	
	public static void registerClient() {
		CLIENT_REGISTRAR.flush();
	}
	
}