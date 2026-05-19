package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
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
import de.dafuqs.spectrum.blocks.end_portal.*;
import de.dafuqs.spectrum.blocks.ender.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.gemstone.*;
import de.dafuqs.spectrum.blocks.geology.*;
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
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.models.*;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.registries.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;
import static de.dafuqs.spectrum.data.SpectrumModelHelper.*;
import static de.dafuqs.spectrum.registries.SpectrumItems.*;
import static net.minecraft.world.level.block.Blocks.*;

@SuppressWarnings({"unused"})
public class SpectrumBlocks {
	
	private static BlockBehaviour.Properties settings(MapColor mapColor, SoundType blockSoundGroup, float strength) {
		return BlockBehaviour.Properties.of().mapColor(mapColor).sound(blockSoundGroup).strength(strength);
	}
	
	private static BlockBehaviour.Properties settings(MapColor mapColor, SoundType blockSoundGroup, float strength, float resistance) {
		return settings(mapColor, blockSoundGroup, strength).explosionResistance(resistance);
	}
	
	private static BlockBehaviour.Properties craftingBlock(MapColor mapColor, SoundType blockSoundGroup) {
		return settings(mapColor, blockSoundGroup, 5.0F, 8.0F).isRedstoneConductor(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never).noOcclusion().requiresCorrectToolForDrops();
	}
	
	public static final DeferredRegister.Blocks REGISTRAR = DeferredRegister.createBlocks(MOD_ID);
	private static final DeferredWorkQueue CLIENT_QUEUE = new DeferredWorkQueue("spectrum_client");
	
	public static final DeferredBlock<Block> PEDESTAL_BASIC_TOPAZ = register(pedestal(blockWithItem("pedestal_basic_topaz", () -> new PedestalBlock(craftingBlock(MapColor.DIAMOND, SpectrumSoundTypes.TOPAZ_BLOCK), BuiltinPedestalVariant.BASIC_TOPAZ), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.BASIC_TOPAZ, "item.spectrum.pedestal.tooltip.basic_topaz"), InkColors.WHITE)));
	public static final DeferredBlock<Block> PEDESTAL_BASIC_AMETHYST = register(pedestal(blockWithItem("pedestal_basic_amethyst", () -> new PedestalBlock(craftingBlock(MapColor.COLOR_PURPLE, SoundType.AMETHYST), BuiltinPedestalVariant.BASIC_AMETHYST), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.BASIC_AMETHYST, "item.spectrum.pedestal.tooltip.basic_amethyst"), InkColors.WHITE)));
	public static final DeferredBlock<Block> PEDESTAL_BASIC_CITRINE = register(pedestal(blockWithItem("pedestal_basic_citrine", () -> new PedestalBlock(craftingBlock(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_BLOCK), BuiltinPedestalVariant.BASIC_CITRINE), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.BASIC_CITRINE, "item.spectrum.pedestal.tooltip.basic_citrine"), InkColors.WHITE)));
	public static final DeferredBlock<Block> PEDESTAL_ALL_BASIC = register(pedestal(blockWithItem("pedestal_all_basic", () -> new PedestalBlock(craftingBlock(MapColor.COLOR_PURPLE, SoundType.AMETHYST), BuiltinPedestalVariant.CMY), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.CMY, "item.spectrum.pedestal.tooltip.all_basic"), InkColors.WHITE)));
	public static final DeferredBlock<Block> PEDESTAL_ONYX = register(pedestal(blockWithItem("pedestal_onyx", () -> new PedestalBlock(craftingBlock(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_BLOCK), BuiltinPedestalVariant.ONYX), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.ONYX, "item.spectrum.pedestal.tooltip.onyx"), InkColors.WHITE)));
	public static final DeferredBlock<Block> PEDESTAL_MOONSTONE = register(pedestal(blockWithItem("pedestal_moonstone", () -> new PedestalBlock(craftingBlock(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_BLOCK), BuiltinPedestalVariant.MOONSTONE), block -> new PedestalBlockItem(block, IS.of(), BuiltinPedestalVariant.MOONSTONE, "item.spectrum.pedestal.tooltip.moonstone"), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> FUSION_SHRINE_BASALT = register(singleton(blockWithItem("fusion_shrine_basalt", () -> new FusionShrineBlock(craftingBlock(MapColor.COLOR_BLACK, SoundType.BASALT).lightLevel(value -> value.getValue(FusionShrineBlock.LIGHT_LEVEL))), () -> IS.of(), InkColors.GRAY), SpectrumTexturedModelProviders.FUSION_SHRINE));
	public static final DeferredBlock<Block> FUSION_SHRINE_CALCITE = register(singleton(blockWithItem("fusion_shrine_calcite", () -> new FusionShrineBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE).lightLevel(value -> value.getValue(FusionShrineBlock.LIGHT_LEVEL))), () -> IS.of(), InkColors.GRAY), SpectrumTexturedModelProviders.FUSION_SHRINE));
	
	public static final DeferredBlock<Block> ENCHANTER = register(singletonWithSoup(blockWithItem("enchanter", () -> new EnchanterBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.PURPLE), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<Block> ITEM_BOWL_BASALT = register(singleton(blockWithItem("item_bowl_basalt", () -> new ItemBowlBlock(craftingBlock(MapColor.COLOR_BLACK, SoundType.BASALT)), () -> IS.of(), InkColors.PINK), TexturedModel.createDefault(b -> new TextureMapping().put(TextureSlot.TEXTURE, SpectrumCommon.locate("block/item_bowl_basalt")).put(TextureSlot.SIDE, SpectrumCommon.locate("block/polished_basalt_pillar_side")).put(SpectrumTextureSlots.BASE, SpectrumCommon.locate("block/polished_basalt")), SpectrumModelTemplates.BOWL)));
	public static final DeferredBlock<Block> ITEM_BOWL_CALCITE = register(singleton(blockWithItem("item_bowl_calcite", () -> new ItemBowlBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.PINK), TexturedModel.createDefault(b -> new TextureMapping().put(TextureSlot.TEXTURE, SpectrumCommon.locate("block/item_bowl_calcite")).put(TextureSlot.SIDE, SpectrumCommon.locate("block/polished_calcite_pillar_side")).put(SpectrumTextureSlots.BASE, SpectrumCommon.locate("block/polished_calcite")), SpectrumModelTemplates.BOWL)));
	public static final DeferredBlock<Block> ITEM_ROUNDEL = register(singleton(blockWithItem("item_roundel", () -> new ItemRoundelBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.PINK), SpectrumTexturedModelProviders.ROUNDEL));
	public static final DeferredBlock<Block> POTION_WORKSHOP = register(defaultNorthHorizontalFacing(blockWithItem("potion_workshop", () -> new PotionWorkshopBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.PURPLE), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<SpiritInstillerBlock> SPIRIT_INSTILLER = register(singletonWithSoup(blockWithItem("spirit_instiller", () -> new SpiritInstillerBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.WHITE), ModelLocationUtils::getModelLocation).withPredefinedItemModel());
	public static final DeferredBlock<CrystallarieumBlock> CRYSTALLARIEUM = register(singletonWithSoup(blockWithItem("crystallarieum", () -> new CrystallarieumBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.BROWN), ModelLocationUtils::getModelLocation).withPredefinedItemModel());
	public static final DeferredBlock<Block> CINDERHEARTH = register(defaultNorthHorizontalFacing(blockWithItem("cinderhearth", () -> new CinderhearthBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of().fireResistant(), InkColors.ORANGE), ModelLocationUtils::getModelLocation));
	
	public static final DeferredBlock<Block> COLOR_PICKER = register(defaultWestHorizontalFacing(blockWithItem("color_picker", () -> new ColorPickerBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.GREEN), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<Block> CRYSTAL_APOTHECARY = register(singletonWithSoup(blockWithItem("crystal_apothecary", () -> new CrystalApothecaryBlock(craftingBlock(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE)), () -> IS.of(), InkColors.GREEN), ModelLocationUtils::getModelLocation));
	
	private static BlockBehaviour.Properties gemstone(MapColor mapColor, SoundType blockSoundGroup, int luminance) {
		return settings(mapColor, blockSoundGroup, 1.5F).forceSolidOn().noOcclusion().lightLevel((state) -> luminance).pushReaction(PushReaction.DESTROY);
	}
	
	private static BlockBehaviour.Properties gemstoneBlock(MapColor mapColor, SoundType blockSoundGroup) {
		return settings(mapColor, blockSoundGroup, 1.5F).requiresCorrectToolForDrops();
	}
	
	public static <T extends SpectrumClusterBlock> BlockRegistrar<T> cluster(BlockRegistrar<T> registrar, ModelTemplate model) {
		return registrar;
		/*return cutout(registrar).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(TexturedModel.createDefault(TextureMapping::cross, model).create(block, ctx.modelOutput))).with(SpectrumModelHelper.createUpDefaultFacingVariantMap())).withBlockItemModel((ctx, block) -> {
			switch (block.getGrowthStage()) {
				case SpectrumClusterBlock.GrowthStage.SMALL -> SpectrumModels.SMALL_BUD_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), ctx.output);
				case SpectrumClusterBlock.GrowthStage.MEDIUM -> SpectrumModels.MEDIUM_BUD_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), ctx.output);
				case SpectrumClusterBlock.GrowthStage.LARGE -> SpectrumModels.LARGE_BUD_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), ctx.output);
				case SpectrumClusterBlock.GrowthStage.CLUSTER -> SpectrumModels.CLUSTER_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), ctx.output);
			}
		});*/
	}
	
	public static final DeferredBlock<SpectrumClusterBlock> TOPAZ_CLUSTER = register(cluster(blockWithItem("topaz_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_CYAN, SpectrumSoundTypes.TOPAZ_CLUSTER, 8), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_TOPAZ_BUD = register(cluster(blockWithItem("large_topaz_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_CYAN, SpectrumSoundTypes.LARGE_TOPAZ_BUD, 6), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> MEDIUM_TOPAZ_BUD = register(cluster(blockWithItem("medium_topaz_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_CYAN, SpectrumSoundTypes.MEDIUM_TOPAZ_BUD, 4), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.CYAN), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_TOPAZ_BUD = register(cluster(blockWithItem("small_topaz_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_CYAN, SpectrumSoundTypes.SMALL_TOPAZ_BUD, 2), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), ModelTemplates.CROSS));
	public static final DeferredBlock<Block> BUDDING_TOPAZ = register(simple(blockWithItem("budding_topaz", () -> new SpectrumBuddingBlock(gemstoneBlock(MapColor.COLOR_CYAN, SpectrumSoundTypes.TOPAZ_BLOCK).pushReaction(PushReaction.DESTROY).randomTicks(), SMALL_TOPAZ_BUD.get(), MEDIUM_TOPAZ_BUD.get(), LARGE_TOPAZ_BUD.get(), TOPAZ_CLUSTER.get(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME), InkColors.CYAN)));
	public static final DeferredBlock<Block> TOPAZ_BLOCK = register(simple(blockWithItem("topaz_block", () -> new SpectrumGemstoneBlock(gemstoneBlock(MapColor.COLOR_CYAN, SpectrumSoundTypes.TOPAZ_BLOCK), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME), InkColors.CYAN)));
	
	public static final DeferredBlock<SpectrumClusterBlock> CITRINE_CLUSTER = register(cluster(blockWithItem("citrine_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_CLUSTER, 9), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_CITRINE_BUD = register(cluster(blockWithItem("large_citrine_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_YELLOW, SpectrumSoundTypes.LARGE_CITRINE_BUD, 7), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> MEDIUM_CITRINE_BUD = register(cluster(blockWithItem("medium_citrine_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_YELLOW, SpectrumSoundTypes.MEDIUM_CITRINE_BUD, 5), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.YELLOW), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_CITRINE_BUD = register(cluster(blockWithItem("small_citrine_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_YELLOW, SpectrumSoundTypes.SMALL_CITRINE_BUD, 3), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), ModelTemplates.CROSS));
	public static final DeferredBlock<Block> BUDDING_CITRINE = register(simple(blockWithItem("budding_citrine", () -> new SpectrumBuddingBlock(gemstoneBlock(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_BLOCK).pushReaction(PushReaction.DESTROY).randomTicks(), SMALL_CITRINE_BUD.get(), MEDIUM_CITRINE_BUD.get(), LARGE_CITRINE_BUD.get(), CITRINE_CLUSTER.get(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME), InkColors.YELLOW)));
	public static final DeferredBlock<Block> CITRINE_BLOCK = register(simple(blockWithItem("citrine_block", () -> new SpectrumGemstoneBlock(gemstoneBlock(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_BLOCK), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME), InkColors.YELLOW)));
	
	public static final DeferredBlock<SpectrumClusterBlock> ONYX_CLUSTER = register(cluster(blockWithItem("onyx_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_CLUSTER, 6), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BLACK), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_ONYX_BUD = register(cluster(blockWithItem("large_onyx_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLACK, SpectrumSoundTypes.LARGE_ONYX_BUD, 5), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BLACK), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> MEDIUM_ONYX_BUD = register(cluster(blockWithItem("medium_onyx_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLACK, SpectrumSoundTypes.MEDIUM_ONYX_BUD, 3), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.BLACK), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_ONYX_BUD = register(cluster(blockWithItem("small_onyx_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLACK, SpectrumSoundTypes.SMALL_ONYX_BUD, 1), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BLACK), ModelTemplates.CROSS));
	public static final DeferredBlock<Block> BUDDING_ONYX = register(simple(blockWithItem("budding_onyx", () -> new SpectrumBuddingBlock(gemstoneBlock(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_BLOCK).pushReaction(PushReaction.DESTROY).randomTicks(), SMALL_ONYX_BUD.get(), MEDIUM_ONYX_BUD.get(), LARGE_ONYX_BUD.get(), ONYX_CLUSTER.get(), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME), InkColors.BLACK)));
	public static final DeferredBlock<Block> ONYX_BLOCK = register(simple(blockWithItem("onyx_block", () -> new SpectrumGemstoneBlock(gemstoneBlock(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_BLOCK), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME), InkColors.BLACK)));
	
	public static final DeferredBlock<SpectrumClusterBlock> MOONSTONE_CLUSTER = register(cluster(blockWithItem("moonstone_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_CLUSTER, 15), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.WHITE), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_MOONSTONE_BUD = register(cluster(blockWithItem("large_moonstone_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.SNOW, SpectrumSoundTypes.LARGE_MOONSTONE_BUD, 12), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.WHITE), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> MEDIUM_MOONSTONE_BUD = register(cluster(blockWithItem("medium_moonstone_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.SNOW, SpectrumSoundTypes.MEDIUM_MOONSTONE_BUD, 9), SpectrumClusterBlock.GrowthStage.MEDIUM), InkColors.WHITE), ModelTemplates.CROSS));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_MOONSTONE_BUD = register(cluster(blockWithItem("small_moonstone_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.SNOW, SpectrumSoundTypes.SMALL_MOONSTONE_BUD, 6), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.WHITE), ModelTemplates.CROSS));
	public static final DeferredBlock<Block> BUDDING_MOONSTONE = register(simple(blockWithItem("budding_moonstone", () -> new SpectrumBuddingBlock(gemstoneBlock(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_BLOCK).pushReaction(PushReaction.DESTROY).randomTicks(), SMALL_MOONSTONE_BUD.get(), MEDIUM_MOONSTONE_BUD.get(), LARGE_MOONSTONE_BUD.get(), MOONSTONE_CLUSTER.get(), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME), InkColors.WHITE)));
	public static final DeferredBlock<Block> MOONSTONE_BLOCK = register(simple(blockWithItem("moonstone_block", () -> new SpectrumGemstoneBlock(gemstoneBlock(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_BLOCK), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> TOPAZ_POWDER_BLOCK = register(simple(blockWithItem("topaz_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.CYAN.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.COLOR_CYAN)), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_POWDER_BLOCK = register(simple(blockWithItem("amethyst_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.MAGENTA.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.COLOR_MAGENTA)), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_POWDER_BLOCK = register(simple(blockWithItem("citrine_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.YELLOW.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.COLOR_YELLOW)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_POWDER_BLOCK = register(simple(blockWithItem("onyx_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.BLACK.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.COLOR_BLACK)), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_POWDER_BLOCK = register(simple(blockWithItem("moonstone_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.WHITE.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.SNOW)), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> VEGETAL_BLOCK = register(singleton(blockWithItem("vegetal_block", () -> new FlammableBlock(settings(MapColor.GRASS, SoundType.FUNGUS, 2.0F).noOcclusion()), InkColors.GREEN), TexturedModel.createDefault(TextureMapping::defaultTexture, SpectrumModelTemplates.TRANSLUCENT_OUTER1)));
	public static final DeferredBlock<Block> NEOLITH_BLOCK = register(simple(blockWithItem("neolith_block", () -> new Block(settings(MapColor.COLOR_PURPLE, SoundType.COPPER, 6.0F).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).lightLevel(state -> 13).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.PINK)));
	public static final DeferredBlock<Block> BEDROCK_DUST_BLOCK = register(simple(blockWithItem("bedrock_dust_block", () -> new BlockWithTooltip(settings(MapColor.STONE, SoundType.STONE, 100.0F, 3600.0F).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM), Component.translatable("spectrum.tooltip.dragon_and_wither_immune")), () -> IS.of(Rarity.UNCOMMON), InkColors.BLACK)));
	
	public static final DeferredBlock<SpectrumClusterBlock> BISMUTH_CLUSTER = register(cluster(blockWithItem("bismuth_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.WARPED_STEM, SoundType.CHAIN, 8), SpectrumClusterBlock.GrowthStage.CLUSTER), () -> IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<BismuthBudBlock> LARGE_BISMUTH_BUD = register(cluster(blockWithItem("large_bismuth_bud", () -> new BismuthBudBlock(gemstone(MapColor.WARPED_STEM, SoundType.CHAIN, 6).randomTicks(), SpectrumClusterBlock.GrowthStage.LARGE, (SpectrumClusterBlock) BISMUTH_CLUSTER.get()), () -> IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<BismuthBudBlock> SMALL_BISMUTH_BUD = register(cluster(blockWithItem("small_bismuth_bud", () -> new BismuthBudBlock(gemstone(MapColor.WARPED_STEM, SoundType.CHAIN, 4).randomTicks(), SpectrumClusterBlock.GrowthStage.SMALL, (BismuthBudBlock) LARGE_BISMUTH_BUD.get()), () -> IS.of(Rarity.UNCOMMON), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<Block> BISMUTH_BLOCK = register(simple(blockWithItem("bismuth_block", () -> new Block(gemstoneBlock(MapColor.WARPED_STEM, SoundType.CHAIN)), InkColors.CYAN)));
	
	// DD BLOCKS
	private static final float BLACKSLAG_HARDNESS = 5.0F;
	private static final float BLACKSLAG_RESISTANCE = 7.0F;
	
	private static BlockBehaviour.Properties blackslag(SoundType blockSoundGroup) {
		return settings(MapColor.COLOR_GRAY, blockSoundGroup, BLACKSLAG_HARDNESS, BLACKSLAG_RESISTANCE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops();
	}
	
	public static final DeferredBlock<BlackslagBlock> BLACKSLAG = register(blockWithItem("blackslag", () -> new BlackslagBlock(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK).withBlockModel((ctx, block) -> SpectrumModelHelper.createMirroredVariantsSupplier(block, TexturedModel.COLUMN_ALT, SpectrumTexturedModelProviders.CUBE_COLUMN_MIRRORED, ctx.modelOutput).with(SpectrumModelHelper.createAxisRotatedVariantMap())));
	public static final DeferredBlock<Block> BLACKSLAG_STAIRS = register(blockWithItem("blackslag_stairs", () -> new StairBlock(BLACKSLAG.get().defaultBlockState(), blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_SLAB = register(blockWithItem("blackslag_slab", () -> new SlabBlock(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_WALL = register(blockWithItem("blackslag_wall", () -> new WallBlock(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	
	public static final DeferredBlock<Block> INFESTED_BLACKSLAG = register(parented(blockWithItem("infested_blackslag", () -> new InfestedBlock(BLACKSLAG.get(), blackslag(SoundType.DEEPSLATE)), InkColors.BLACK), b -> BLACKSLAG.get()));
	
	public static final DeferredBlock<Block> COBBLED_BLACKSLAG = register(blockWithItem("cobbled_blackslag", () -> new Block(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> COBBLED_BLACKSLAG_STAIRS = register(blockWithItem("cobbled_blackslag_stairs", () -> new StairBlock(COBBLED_BLACKSLAG.get().defaultBlockState(), blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> COBBLED_BLACKSLAG_SLAB = register(blockWithItem("cobbled_blackslag_slab", () -> new SlabBlock(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> COBBLED_BLACKSLAG_WALL = register(blockWithItem("cobbled_blackslag_wall", () -> new WallBlock(blackslag(SoundType.DEEPSLATE)), InkColors.BLACK));
	
	public static final DeferredBlock<Block> BLACKSLAG_TILES = register(blockWithItem("blackslag_tiles", () -> new Block(blackslag(SoundType.DEEPSLATE_TILES)), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_TILE_STAIRS = register(blockWithItem("blackslag_tile_stairs", () -> new StairBlock(BLACKSLAG_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_TILES.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_TILE_SLAB = register(blockWithItem("blackslag_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_TILES.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_TILE_WALL = register(blockWithItem("blackslag_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_TILES.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> CRACKED_BLACKSLAG_TILES = register(blockWithItem("cracked_blackslag_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_TILES.get())), InkColors.BLACK));
	
	public static final DeferredBlock<Block> BLACKSLAG_BRICKS = register(blockWithItem("blackslag_bricks", () -> new Block(blackslag(SoundType.DEEPSLATE_BRICKS)), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_BRICK_STAIRS = register(blockWithItem("blackslag_brick_stairs", () -> new StairBlock(BLACKSLAG_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_BRICKS.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_BRICK_SLAB = register(blockWithItem("blackslag_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_BRICKS.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> BLACKSLAG_BRICK_WALL = register(blockWithItem("blackslag_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_BRICKS.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> CRACKED_BLACKSLAG_BRICKS = register(blockWithItem("cracked_blackslag_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_BRICKS.get())), InkColors.BLACK));
	
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG = register(blockWithItem("polished_blackslag", () -> new Block(blackslag(SoundType.POLISHED_DEEPSLATE)), InkColors.BLACK));
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_STAIRS = register(blockWithItem("polished_blackslag_stairs", () -> new StairBlock(POLISHED_BLACKSLAG.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_BLACKSLAG.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_SLAB = register(blockWithItem("polished_blackslag_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BLACKSLAG.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_WALL = register(blockWithItem("polished_blackslag_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BLACKSLAG.get())), InkColors.BLACK));
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_BUTTON = register(blockWithItem("polished_blackslag_button", () -> new ButtonBlock(SpectrumBlockSetTypes.POLISHED_BLACKSLAG, 5, BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F)), InkColors.BLACK));
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_PRESSURE_PLATE = register(blockWithItem("polished_blackslag_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_BLACKSLAG, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().noCollission().strength(0.5F)), InkColors.BLACK));
	public static final DeferredBlock<Block> CHISELED_POLISHED_BLACKSLAG = register(blockWithItem("chiseled_polished_blackslag", () -> new Block(blackslag(SoundType.DEEPSLATE_BRICKS)), InkColors.BLACK));
	
	public static final DeferredBlock<Block> POLISHED_BLACKSLAG_PILLAR = register(axisRotated(blockWithItem("polished_blackslag_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG_BRICKS.get())), InkColors.BLACK), TexturedModel.createDefault(b -> SpectrumTextureMaps.sideEnd(b, "", CHISELED_POLISHED_BLACKSLAG.get(), ""), ModelTemplates.CUBE_COLUMN)));
	public static final DeferredBlock<Block> ANCIENT_CHISELED_POLISHED_BLACKSLAG = register(simple(blockWithItem("ancient_chiseled_polished_blackslag", () -> new Block(blackslag(SoundType.DEEPSLATE_BRICKS)), InkColors.BLACK)));
	
	public static final DeferredBlock<Block> SHALE_CLAY = register(singleton(blockWithItem("shale_clay", () -> new ShaleClayBlock(Weathering.WeatheringLevel.UNAFFECTED, blackslag(SoundType.MUD_BRICKS)), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> TILLED_SHALE_CLAY = register(singleton(blockWithItem("tilled_shale_clay", () -> new ImmutableFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get()), SHALE_CLAY.get().defaultBlockState()), InkColors.BROWN), SpectrumTexturedModelProviders.farmland(b -> SHALE_CLAY.get(), "_side", b -> b, "")));
	
	public static final DeferredBlock<Block> POLISHED_SHALE_CLAY = register(blockWithItem("polished_shale_clay", () -> new ShaleClayBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("polished_shale_clay_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, POLISHED_SHALE_CLAY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("polished_shale_clay_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> EXPOSED_POLISHED_SHALE_CLAY = register(blockWithItem("exposed_polished_shale_clay", () -> new ShaleClayBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("exposed_polished_shale_clay_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_POLISHED_SHALE_CLAY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("exposed_polished_shale_clay_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> WEATHERED_POLISHED_SHALE_CLAY = register(blockWithItem("weathered_polished_shale_clay", () -> new ShaleClayBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_POLISHED_SHALE_CLAY_STAIRS = register(blockWithItem("weathered_polished_shale_clay_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_POLISHED_SHALE_CLAY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_POLISHED_SHALE_CLAY_SLAB = register(blockWithItem("weathered_polished_shale_clay_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> SHALE_CLAY_BRICKS = register(blockWithItem("shale_clay_bricks", () -> new ShaleClayBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("shale_clay_brick_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> SHALE_CLAY_BRICK_SLAB = register(blockWithItem("shale_clay_brick_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_BRICKS = register(blockWithItem("exposed_shale_clay_bricks", () -> new ShaleClayBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("exposed_shale_clay_brick_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_BRICK_SLAB = register(blockWithItem("exposed_shale_clay_brick_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_BRICKS = register(blockWithItem("weathered_shale_clay_bricks", () -> new ShaleClayBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_BRICK_STAIRS = register(blockWithItem("weathered_shale_clay_brick_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_BRICK_SLAB = register(blockWithItem("weathered_shale_clay_brick_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> SHALE_CLAY_TILES = register(blockWithItem("shale_clay_tiles", () -> new ShaleClayBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> SHALE_CLAY_TILE_STAIRS = register(blockWithItem("shale_clay_tile_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> SHALE_CLAY_TILE_SLAB = register(blockWithItem("shale_clay_tile_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_TILES = register(blockWithItem("exposed_shale_clay_tiles", () -> new ShaleClayBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_TILE_STAIRS = register(blockWithItem("exposed_shale_clay_tile_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> EXPOSED_SHALE_CLAY_TILE_SLAB = register(blockWithItem("exposed_shale_clay_tile_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_TILES = register(blockWithItem("weathered_shale_clay_tiles", () -> new ShaleClayBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_TILE_STAIRS = register(blockWithItem("weathered_shale_clay_tile_stairs", () -> new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> WEATHERED_SHALE_CLAY_TILE_SLAB = register(blockWithItem("weathered_shale_clay_tile_slab", () -> new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> ROCK_CRYSTAL = register(simple(blockWithItem("rock_crystal", () -> new Block(settings(MapColor.QUARTZ, SoundType.NETHER_BRICKS, 200F).requiresCorrectToolForDrops()), InkColors.BROWN)));
	
	public static final DeferredBlock<Block> PYRITE = register(axisRotated(blockWithItem("pyrite", () -> new RotatedPillarBlock(settings(MapColor.TERRACOTTA_YELLOW, SoundType.CHAIN, 50.0F).requiresCorrectToolForDrops()), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> PYRITE_SLAB = register(blockWithItem("pyrite_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> PYRITE_STAIRS = register(blockWithItem("pyrite_stairs", () -> new StairBlock(PYRITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> PYRITE_WALL = register(blockWithItem("pyrite_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> PYRITE_PILE = register(axisRotated(blockWithItem("pyrite_pile", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> PYRITE_PLATING = register(simple(blockWithItem("pyrite_plating", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN)));
	public static final DeferredBlock<Block> PYRITE_TUBING = register(axisRotated(blockWithItem("pyrite_tubing", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> PYRITE_RELIEF = register(axisRotated(blockWithItem("pyrite_relief", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), SpectrumTexturedModelProviders.cubeColumn(b -> b, "_side", b -> PYRITE_TUBING.get(), "_top")));
	public static final DeferredBlock<Block> PYRITE_STACK = register(simple(blockWithItem("pyrite_stack", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN)));
	public static final DeferredBlock<Block> PYRITE_PANELING = register(singleton(blockWithItem("pyrite_paneling", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), SpectrumTexturedModelProviders.cubeColumn(b -> b, "", b -> PYRITE_PLATING.get(), "")));
	public static final DeferredBlock<Block> PYRITE_VENT = register(singleton(blockWithItem("pyrite_vent", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), SpectrumTexturedModelProviders.cubeColumn(b -> b, "", b -> PYRITE_PLATING.get(), "")));
	public static final DeferredBlock<PyriteRipperBlock> PYRITE_RIPPER = register(blockWithItem("pyrite_ripper", () -> new PyriteRipperBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get()).noOcclusion().isValidSpawn(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never)), InkColors.BROWN).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.FACING, PyriteRipperBlock.MIRRORED)
			.select(Direction.EAST, false, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.select(Direction.NORTH, false, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
			.select(Direction.SOUTH, false, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
			.select(Direction.WEST, false, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.select(Direction.UP, false, SpectrumModelHelper.createModelVariant(block, ""))
			.select(Direction.DOWN, false, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
			.select(Direction.EAST, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
			.select(Direction.NORTH, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.select(Direction.SOUTH, true, SpectrumModelHelper.createModelVariant(block, "_mirrored").with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.select(Direction.WEST, true, SpectrumModelHelper.createModelVariant(block, "_mirrored"))
			.select(Direction.UP, true, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.select(Direction.DOWN, true, SpectrumModelHelper.createModelVariant(block, "").with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))));
	public static final DeferredBlock<Block> PYRITE_PROJECTOR = register(singletonWithSoup(blockWithItem("pyrite_projector", () -> new ProjectorBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE.get()), "pyrite_projector_projection", 16, 14, 1.375F, 1F, 16F), InkColors.BROWN), ModelLocationUtils::getModelLocation));
	
	public static final DeferredBlock<Block> PYRITE_TILES = register(simple(blockWithItem("pyrite_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN)));
	public static final DeferredBlock<Block> PYRITE_TILE_SLAB = register(blockWithItem("pyrite_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> PYRITE_TILE_STAIRS = register(blockWithItem("pyrite_tile_stairs", () -> new StairBlock(PYRITE_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PYRITE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> PYRITE_TILE_WALL = register(blockWithItem("pyrite_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(PYRITE_TILES.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> SPLINTERSPAWN_INFESTED_PYRITE = register(parented(blockWithItem("splinterspawn_infested_pyrite", () -> new RotatedPillarSplinterspawnInfestedBlock(PYRITE.get(), BlockBehaviour.Properties.ofFullCopy(PYRITE.get())), InkColors.BROWN), b -> PYRITE.get()));
	public static final DeferredBlock<Block> SPLINTERSPAWN_INFESTED_SHALE_CLAY = register(parented(blockWithItem("splinterspawn_infested_shale_clay", () -> new SplinterspawnInfestedBlock(SHALE_CLAY.get(), BlockBehaviour.Properties.ofFullCopy(SHALE_CLAY.get())), InkColors.BROWN), b -> SHALE_CLAY.get()));
	
	public static final DeferredBlock<Block> DRAGONBONE = register(axisRotated(blockWithItem("dragonbone", () -> new DragonboneBlock(BlockBehaviour.Properties.ofFullCopy(BONE_BLOCK).strength(-1.0F, 22.0F).pushReaction(PushReaction.BLOCK)), InkColors.GREEN), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> CRACKED_DRAGONBONE = register(axisRotated(blockWithItem("cracked_dragonbone", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(BONE_BLOCK).strength(100.0F, 1200.0F).pushReaction(PushReaction.BLOCK)), () -> IS.of().component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_EXPLOSION)), InkColors.GREEN), TexturedModel.COLUMN));

	public static final DeferredBlock<Block> POLISHED_BONE_ASH = register(blockWithItem("polished_bone_ash", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CRACKED_DRAGONBONE.get()).destroyTime(1500.0F).mapColor(MapColor.SNOW)), InkColors.CYAN));
	public static final DeferredBlock<Block> POLISHED_BONE_ASH_STAIRS = register(blockWithItem("polished_bone_ash_stairs", () -> new StairBlock(POLISHED_BONE_ASH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> POLISHED_BONE_ASH_SLAB = register(blockWithItem("polished_bone_ash_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> POLISHED_BONE_ASH_WALL = register(blockWithItem("polished_bone_ash_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN));
	
	public static final DeferredBlock<Block> POLISHED_BONE_ASH_PILLAR = register(axisRotated(blockWithItem("polished_bone_ash_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN), TexturedModel.COLUMN));
	public static final DeferredBlock<ShinglesBlock> BONE_ASH_SHINGLES = register(blockWithItem("bone_ash_shingles", () -> new ShinglesBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get()).noOcclusion()), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelLocationUtils.getModelLocation(block)).with(SpectrumModelHelper.createEastDefaultHorizontalFacingVariantMap())));
	
	public static final DeferredBlock<Block> BONE_ASH_BRICKS = register(blockWithItem("bone_ash_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_BRICK_STAIRS = register(blockWithItem("bone_ash_brick_stairs", () -> new StairBlock(BONE_ASH_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BONE_ASH_BRICKS.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_BRICK_SLAB = register(blockWithItem("bone_ash_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BONE_ASH_BRICKS.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_BRICK_WALL = register(blockWithItem("bone_ash_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BONE_ASH_BRICKS.get())), InkColors.CYAN));
	
	public static final DeferredBlock<Block> BONE_ASH_TILES = register(blockWithItem("bone_ash_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BONE_ASH.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_TILE_STAIRS = register(blockWithItem("bone_ash_tile_stairs", () -> new StairBlock(BONE_ASH_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BONE_ASH_TILES.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_TILE_SLAB = register(blockWithItem("bone_ash_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BONE_ASH_TILES.get())), InkColors.CYAN));
	public static final DeferredBlock<Block> BONE_ASH_TILE_WALL = register(blockWithItem("bone_ash_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BONE_ASH_TILES.get())), InkColors.CYAN));
	
	public static final DeferredBlock<Block> SLUSH = register(simple(blockWithItem("slush", () -> new SlushBlock(blackslag(SoundType.MUDDY_MANGROVE_ROOTS)), InkColors.BROWN)));
	public static final DeferredBlock<Block> OVERGROWN_SLUSH = register(snowy(blockWithItem("overgrown_slush", () -> new OvergrownSlushBlock(blackslag(SoundType.MUDDY_MANGROVE_ROOTS)), InkColors.BROWN), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> SLUSH.get(), "", b -> b, "_top"), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> SLUSH.get(), "", b -> b, "_snow_top")));
	public static final DeferredBlock<Block> TILLED_SLUSH = register(singleton(blockWithItem("tilled_slush", () -> new TilledSlushBlock(BlockBehaviour.Properties.ofFullCopy(SLUSH.get()), SLUSH.get().defaultBlockState()), InkColors.BROWN), SpectrumTexturedModelProviders.farmland(b -> SLUSH.get(), "", b -> b, "")));
	
	public static final DeferredBlock<Block> BLACK_MATERIA = register(simple(blockWithItem("black_materia", () -> new BlackMateriaBlock(settings(MapColor.TERRACOTTA_BLACK, SoundType.SAND, 0.0F).instrument(NoteBlockInstrument.SNARE).randomTicks()), InkColors.GRAY)));
	public static final DeferredBlock<Block> BLACK_SLUDGE = register(simple(blockWithItem("black_sludge", () -> new Block(settings(MapColor.TERRACOTTA_BLACK, SoundType.SAND, 0.5F).instrument(NoteBlockInstrument.SNARE)), InkColors.GRAY)));
	public static final DeferredBlock<Block> SAG_LEAF = register(cross(block("sag_leaf", () -> new BlackSludgePlantBlock(BlockBehaviour.Properties.ofFullCopy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	public static final DeferredBlock<Block> SAG_BUBBLE = register(cross(block("sag_bubble", () -> new BlackSludgePlantBlock(BlockBehaviour.Properties.ofFullCopy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	public static final DeferredBlock<Block> SMALL_SAG_BUBBLE = register(cross(block("small_sag_bubble", () -> new BlackSludgePlantBlock(BlockBehaviour.Properties.ofFullCopy(SHORT_GRASS).mapColor(MapColor.TERRACOTTA_BLACK)))));
	
	public static final DeferredBlock<PrimordialFireBlock> PRIMORDIAL_FIRE = register(block("primordial_fire", () -> new PrimordialFireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE).mapColor(MapColor.COLOR_PURPLE).lightLevel((state) -> 10))).withBlockModel((ctx, block) -> {
		Condition noSides = Condition.condition().term(PrimordialFireBlock.UP, false).term(PrimordialFireBlock.NORTH, false).term(PrimordialFireBlock.SOUTH, false).term(PrimordialFireBlock.WEST, false).term(PrimordialFireBlock.EAST, false);
		TextureMapping fire0 = new TextureMapping().put(TextureSlot.FIRE, TextureMapping.getBlockTexture(block, "_0"));
		TextureMapping fire1 = new TextureMapping().put(TextureSlot.FIRE, TextureMapping.getBlockTexture(block, "_1"));
		ResourceLocation side0 = SpectrumModelTemplates.FIRE_SIDE.createWithSuffix(block, "_side0", fire0, ctx.modelOutput);
		ResourceLocation side1 = SpectrumModelTemplates.FIRE_SIDE.createWithSuffix(block, "_side1", fire1, ctx.modelOutput);
		ResourceLocation sideAlt0 = SpectrumModelTemplates.FIRE_SIDE_ALT.createWithSuffix(block, "_side_alt0", fire0, ctx.modelOutput);
		ResourceLocation sideAlt1 = SpectrumModelTemplates.FIRE_SIDE_ALT.createWithSuffix(block, "_side_alt1", fire1, ctx.modelOutput);
		return MultiPartGenerator.multiPart(block)
				.with(noSides, createModelVariant(SpectrumModelTemplates.FIRE_FLOOR.createWithSuffix(block, "_floor0", fire0, ctx.modelOutput)), createModelVariant(SpectrumModelTemplates.FIRE_FLOOR.createWithSuffix(block, "_floor1", fire1, ctx.modelOutput)))
				.with(Condition.condition().term(PrimordialFireBlock.UP, true), createModelVariant(SpectrumModelTemplates.FIRE_UP.createWithSuffix(block, "_up0", fire0, ctx.modelOutput)), createModelVariant(SpectrumModelTemplates.FIRE_UP.createWithSuffix(block, "_up1", fire1, ctx.modelOutput)), createModelVariant(SpectrumModelTemplates.FIRE_UP_ALT.createWithSuffix(block, "_up_alt0", fire0, ctx.modelOutput)), createModelVariant(SpectrumModelTemplates.FIRE_UP_ALT.createWithSuffix(block, "_up_alt1", fire1, ctx.modelOutput)))
				.with(Condition.or(noSides, Condition.condition().term(PrimordialFireBlock.NORTH, true)), createModelVariant(side0), createModelVariant(side1), createModelVariant(sideAlt0), createModelVariant(sideAlt1))
				.with(Condition.or(noSides, Condition.condition().term(PrimordialFireBlock.SOUTH, true)), createModelVariant(side0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), createModelVariant(side1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), createModelVariant(sideAlt0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), createModelVariant(sideAlt1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.with(Condition.or(noSides, Condition.condition().term(PrimordialFireBlock.WEST, true)), createModelVariant(side0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270), createModelVariant(side1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270), createModelVariant(sideAlt0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270), createModelVariant(sideAlt1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.with(Condition.or(noSides, Condition.condition().term(PrimordialFireBlock.EAST, true)), createModelVariant(side0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), createModelVariant(side1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), createModelVariant(sideAlt0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), createModelVariant(sideAlt1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}));
	public static final DeferredBlock<Block> PRIMORDIAL_WALL_TORCH = register(defaultEastHorizontalFacing(block("primordial_wall_torch", () -> new WallTorchBlock(SpectrumParticleTypes.PRIMORDIAL_FLAME, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_WALL_TORCH).lightLevel(s -> 13))), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<TorchBlock> PRIMORDIAL_TORCH = register(singletonWithSoup(blockWithItem("primordial_torch", () -> new TorchBlock(SpectrumParticleTypes.PRIMORDIAL_FLAME, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_TORCH).lightLevel(s -> 13)), block -> new StandingAndWallBlockItem(block, PRIMORDIAL_WALL_TORCH.get(), IS.of(), Direction.DOWN), InkColors.ORANGE), ModelLocationUtils::getModelLocation).withItemModel(SpectrumModelHelper::registerItemModel));
	
	public static <T extends Block> BlockRegistrar<T> moonstoneChiseled(BlockRegistrar<T> registrar, ResourceLocation capTexture) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_down")).withBlockModel((ctx, block) -> {
			TextureMapping textureMap = SpectrumTextureMaps.sideLine(capTexture, TextureMapping.getBlockTexture(block));
			ResourceLocation base = SpectrumModelTemplates.MOONSTONE_CHISELED.create(block, textureMap, ctx.modelOutput);
			ResourceLocation down = SpectrumModelTemplates.MOONSTONE_CHISELED_DOWN.createWithSuffix(block, "_down", textureMap, ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createDownDefaultFacingVariantMap(ModelLocationUtils.getModelLocation(block), ModelLocationUtils.getModelLocation(block, "_down")));
		});
	}
	
	public static final DeferredBlock<Block> SMOOTH_BASALT_STAIRS = register(blockWithItem("smooth_basalt_stairs", () -> new StairBlock(BASALT.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BASALT)), InkColors.BROWN));
	public static final DeferredBlock<Block> SMOOTH_BASALT_SLAB = register(blockWithItem("smooth_basalt_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BASALT)), InkColors.BROWN));
	public static final DeferredBlock<Block> SMOOTH_BASALT_WALL = register(blockWithItem("smooth_basalt_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BASALT)), InkColors.BROWN));
	
	public static final DeferredBlock<Block> POLISHED_BASALT = register(blockWithItem("polished_basalt", () -> new Block(settings(MapColor.COLOR_BLACK, SoundType.BASALT, 2.0F, 5.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASALT_STAIRS = register(blockWithItem("polished_basalt_stairs", () -> new StairBlock(POLISHED_BASALT.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASALT_SLAB = register(blockWithItem("polished_basalt_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASALT_WALL = register(blockWithItem("polished_basalt_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASALT_BUTTON = register(blockWithItem("polished_basalt_button", () -> new ButtonBlock(SpectrumBlockSetTypes.POLISHED_BASALT, 5, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASALT_PRESSURE_PLATE = register(blockWithItem("polished_basalt_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_BASALT, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)), InkColors.BROWN));
	public static final DeferredBlock<Block> CHISELED_POLISHED_BASALT = register(blockWithItem("chiseled_polished_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> POLISHED_BASALT_PILLAR = register(axisRotated(blockWithItem("polished_basalt_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<CardinalFacingBlock> POLISHED_BASALT_CREST = register(blockWithItem("polished_basalt_crest", () -> new CardinalFacingBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, TexturedModel.COLUMN).with(SpectrumModelHelper.createCardinalFacingVariantMap())));
	public static final DeferredBlock<Block> NOTCHED_POLISHED_BASALT = register(singleton(blockWithItem("notched_polished_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN), TexturedModel.COLUMN));
	
	public static final DeferredBlock<Block> BASALT_BRICKS = register(blockWithItem("basalt_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_BRICK_STAIRS = register(blockWithItem("basalt_brick_stairs", () -> new StairBlock(BASALT_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_BRICK_SLAB = register(blockWithItem("basalt_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_BRICK_WALL = register(blockWithItem("basalt_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CRACKED_BASALT_BRICKS = register(blockWithItem("cracked_basalt_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> BASALT_TILES = register(blockWithItem("basalt_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_TILE_STAIRS = register(blockWithItem("basalt_tile_stairs", () -> new StairBlock(BASALT_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BASALT_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_TILE_SLAB = register(blockWithItem("basalt_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BASALT_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASALT_TILE_WALL = register(blockWithItem("basalt_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BASALT_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CRACKED_BASALT_TILES = register(blockWithItem("cracked_basalt_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_TILES.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> TOPAZ_CHISELED_BASALT = register(simple(blockWithItem("topaz_chiseled_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get()).lightLevel(s -> 6)), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_CHISELED_BASALT = register(simple(blockWithItem("amethyst_chiseled_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get()).lightLevel(s -> 5)), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_CHISELED_BASALT = register(simple(blockWithItem("citrine_chiseled_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get()).lightLevel(s -> 7)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_CHISELED_BASALT = register(simple(blockWithItem("onyx_chiseled_basalt", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get()).lightLevel(s -> 3)), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_CHISELED_BASALT = register(moonstoneChiseled(blockWithItem("moonstone_chiseled_basalt", () -> new SpectrumLineFacingBlock(BlockBehaviour.Properties.ofFullCopy(BASALT_BRICKS.get()).lightLevel(s -> 12)), InkColors.WHITE), SpectrumTextures.BASALT_CAP));
	
	public static final DeferredBlock<Block> CALCITE_STAIRS = register(blockWithItem("calcite_stairs", () -> new StairBlock(CALCITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CALCITE)), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_SLAB = register(blockWithItem("calcite_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE)), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_WALL = register(blockWithItem("calcite_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE)), InkColors.BROWN));
	
	public static final DeferredBlock<Block> POLISHED_CALCITE = register(blockWithItem("polished_calcite", () -> new Block(settings(MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, 2.0F, 5.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_CALCITE_STAIRS = register(blockWithItem("polished_calcite_stairs", () -> new StairBlock(POLISHED_CALCITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_CALCITE_SLAB = register(blockWithItem("polished_calcite_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_CALCITE_WALL = register(blockWithItem("polished_calcite_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_CALCITE_BUTTON = register(blockWithItem("polished_calcite_button", () -> new ButtonBlock(SpectrumBlockSetTypes.POLISHED_CALCITE, 5, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_CALCITE_PRESSURE_PLATE = register(blockWithItem("polished_calcite_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.POLISHED_CALCITE, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)), InkColors.BROWN));
	public static final DeferredBlock<Block> CHISELED_POLISHED_CALCITE = register(blockWithItem("chiseled_polished_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> POLISHED_CALCITE_PILLAR = register(axisRotated(blockWithItem("polished_calcite_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN), TexturedModel.COLUMN));
	public static final DeferredBlock<CardinalFacingBlock> POLISHED_CALCITE_CREST = register(blockWithItem("polished_calcite_crest", () -> new CardinalFacingBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, TexturedModel.COLUMN).with(SpectrumModelHelper.createCardinalFacingVariantMap())));
	public static final DeferredBlock<Block> NOTCHED_POLISHED_CALCITE = register(singleton(blockWithItem("notched_polished_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN), TexturedModel.COLUMN));
	
	public static final DeferredBlock<Block> CALCITE_BRICKS = register(blockWithItem("calcite_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_BRICK_STAIRS = register(blockWithItem("calcite_brick_stairs", () -> new StairBlock(CALCITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_BRICK_SLAB = register(blockWithItem("calcite_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_BRICK_WALL = register(blockWithItem("calcite_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CRACKED_CALCITE_BRICKS = register(blockWithItem("cracked_calcite_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> CALCITE_TILES = register(blockWithItem("calcite_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_CALCITE.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_TILE_STAIRS = register(blockWithItem("calcite_tile_stairs", () -> new StairBlock(CALCITE_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CALCITE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_TILE_SLAB = register(blockWithItem("calcite_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CALCITE_TILE_WALL = register(blockWithItem("calcite_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> CRACKED_CALCITE_TILES = register(blockWithItem("cracked_calcite_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_TILES.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> TOPAZ_CHISELED_CALCITE = register(simple(blockWithItem("topaz_chiseled_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get()).lightLevel(s -> 6)), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_CHISELED_CALCITE = register(simple(blockWithItem("amethyst_chiseled_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get()).lightLevel(s -> 5)), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_CHISELED_CALCITE = register(simple(blockWithItem("citrine_chiseled_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get()).lightLevel(s -> 7)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_CHISELED_CALCITE = register(simple(blockWithItem("onyx_chiseled_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get()).lightLevel(s -> 3)), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_CHISELED_CALCITE = register(moonstoneChiseled(blockWithItem("moonstone_chiseled_calcite", () -> new SpectrumLineFacingBlock(BlockBehaviour.Properties.ofFullCopy(CALCITE_BRICKS.get()).lightLevel(s -> 12)), InkColors.WHITE), SpectrumTextures.CALCITE_CAP));
	
	public static DeferredBlock<Block> registerGemstoneLight(String name, DeferredBlock<Block> gemBlock, DeferredBlock<Block> baseBlock, ResourceLocation capTexture, InkColor color) {
		return register(axisRotated(blockWithItem(name, () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock.get()).lightLevel(s -> 15).noOcclusion().forceSolidOn()), color), TexturedModel.createDefault(block -> SpectrumTextureMaps.sideTopInside(TextureMapping.getBlockTexture(block), capTexture, TextureMapping.getBlockTexture(gemBlock.get())), SpectrumModelTemplates.MULTILAYER_LIGHT)));
	}
	
	public static DeferredBlock<Block> registerGemstoneLight(String name, Block gemBlock, DeferredBlock<Block> baseBlock, ResourceLocation capTexture, InkColor color) {
		return register(axisRotated(blockWithItem(name, () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock.get()).lightLevel(s -> 15).noOcclusion().forceSolidOn()), color), TexturedModel.createDefault(block -> SpectrumTextureMaps.sideTopInside(TextureMapping.getBlockTexture(block), capTexture, TextureMapping.getBlockTexture(gemBlock)), SpectrumModelTemplates.MULTILAYER_LIGHT)));
	}
	
	public static final DeferredBlock<Block> TOPAZ_BASALT_LIGHT = registerGemstoneLight("topaz_basalt_light", TOPAZ_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.CYAN);
	public static final DeferredBlock<Block> AMETHYST_BASALT_LIGHT = registerGemstoneLight("amethyst_basalt_light", AMETHYST_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.MAGENTA);
	public static final DeferredBlock<Block> CITRINE_BASALT_LIGHT = registerGemstoneLight("citrine_basalt_light", CITRINE_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.YELLOW);
	public static final DeferredBlock<Block> ONYX_BASALT_LIGHT = registerGemstoneLight("onyx_basalt_light", ONYX_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.BLACK);
	public static final DeferredBlock<Block> MOONSTONE_BASALT_LIGHT = registerGemstoneLight("moonstone_basalt_light", MOONSTONE_BLOCK, POLISHED_BASALT, SpectrumTextures.BASALT_CAP, InkColors.WHITE);
	public static final DeferredBlock<Block> TOPAZ_CALCITE_LIGHT = registerGemstoneLight("topaz_calcite_light", TOPAZ_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.CYAN);
	public static final DeferredBlock<Block> AMETHYST_CALCITE_LIGHT = registerGemstoneLight("amethyst_calcite_light", AMETHYST_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.MAGENTA);
	public static final DeferredBlock<Block> CITRINE_CALCITE_LIGHT = registerGemstoneLight("citrine_calcite_light", CITRINE_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.YELLOW);
	public static final DeferredBlock<Block> ONYX_CALCITE_LIGHT = registerGemstoneLight("onyx_calcite_light", ONYX_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.BLACK);
	public static final DeferredBlock<Block> MOONSTONE_CALCITE_LIGHT = registerGemstoneLight("moonstone_calcite_light", MOONSTONE_BLOCK, POLISHED_CALCITE, SpectrumTextures.CALCITE_CAP, InkColors.WHITE);
	
	// GLASS
	private static BlockBehaviour.Properties gemstoneGlass(SoundType soundGroup, MapColor mapColor) {
		return BlockBehaviour.Properties.ofFullCopy(GLASS).sound(soundGroup).mapColor(mapColor);
	}
	
	public static final DeferredBlock<Block> TOPAZ_GLASS = register(simple(blockWithItem("topaz_glass", () -> new GemstoneGlassBlock(gemstoneGlass(SpectrumSoundTypes.TOPAZ_CLUSTER, MapColor.COLOR_CYAN), BuiltinGemstoneColor.CYAN), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_GLASS = register(simple(blockWithItem("amethyst_glass", () -> new GemstoneGlassBlock(gemstoneGlass(SoundType.AMETHYST_CLUSTER, MapColor.COLOR_MAGENTA), BuiltinGemstoneColor.MAGENTA), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_GLASS = register(simple(blockWithItem("citrine_glass", () -> new GemstoneGlassBlock(gemstoneGlass(SpectrumSoundTypes.CITRINE_CLUSTER, MapColor.COLOR_YELLOW), BuiltinGemstoneColor.YELLOW), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_GLASS = register(simple(blockWithItem("onyx_glass", () -> new GemstoneGlassBlock(gemstoneGlass(SpectrumSoundTypes.ONYX_CLUSTER, MapColor.COLOR_BLACK), BuiltinGemstoneColor.BLACK), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_GLASS = register(simple(blockWithItem("moonstone_glass", () -> new GemstoneGlassBlock(gemstoneGlass(SpectrumSoundTypes.MOONSTONE_CLUSTER, MapColor.SNOW), BuiltinGemstoneColor.WHITE), InkColors.WHITE)));
	public static final DeferredBlock<Block> RADIANT_GLASS = register(simple(blockWithItem("radiant_glass", () -> new RadiantGlassBlock(gemstoneGlass(SoundType.GLASS, MapColor.SAND).lightLevel(value -> 12)), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> TOPAZ_GLASS_PANE = register(blockWithItem("topaz_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SpectrumSoundTypes.TOPAZ_CLUSTER, MapColor.COLOR_CYAN)), InkColors.CYAN));
	public static final DeferredBlock<Block> AMETHYST_GLASS_PANE = register(blockWithItem("amethyst_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SoundType.AMETHYST_CLUSTER, MapColor.COLOR_MAGENTA)), InkColors.MAGENTA));
	public static final DeferredBlock<Block> CITRINE_GLASS_PANE = register(blockWithItem("citrine_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SpectrumSoundTypes.CITRINE_CLUSTER, MapColor.COLOR_YELLOW)), InkColors.YELLOW));
	public static final DeferredBlock<Block> ONYX_GLASS_PANE = register(blockWithItem("onyx_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SpectrumSoundTypes.ONYX_CLUSTER, MapColor.COLOR_BLACK)), InkColors.BLACK));
	public static final DeferredBlock<Block> MOONSTONE_GLASS_PANE = register(blockWithItem("moonstone_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SpectrumSoundTypes.MOONSTONE_CLUSTER, MapColor.SNOW)), InkColors.WHITE));
	public static final DeferredBlock<Block> RADIANT_GLASS_PANE = register(blockWithItem("radiant_glass_pane", () -> new IronBarsBlock(gemstoneGlass(SoundType.GLASS, MapColor.SAND).lightLevel(value -> 12)), InkColors.WHITE));
	
	public static final DeferredBlock<Block> ETHEREAL_PLATFORM = register(simple(blockWithItem("ethereal_platform", () -> new EtherealPlatformBlock(gemstoneGlass(SoundType.AMETHYST, MapColor.NONE).pushReaction(PushReaction.NORMAL)), InkColors.LIGHT_GRAY)));
	public static final DeferredBlock<Block> UNIVERSE_SPYHOLE = register(simple(blockWithItem("universe_spyhole", () -> new TransparentBlock(settings(MapColor.NONE, SpectrumSoundTypes.CITRINE_BLOCK, 1.5F).requiresCorrectToolForDrops().isViewBlocking(SpectrumBlocks::never)), InkColors.LIGHT_GRAY)));
	
	private static BlockBehaviour.Properties chime(BlockBehaviour block) {
		return BlockBehaviour.Properties.ofFullCopy(block).pushReaction(PushReaction.DESTROY).destroyTime(1.0F).noOcclusion();
	}
	
	public static final DeferredBlock<Block> TOPAZ_CHIME = register(singleton(blockWithItem("topaz_chime", () -> new GemstoneChimeBlock(chime(TOPAZ_CLUSTER.get()), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.CYAN), InkColors.CYAN), SpectrumTexturedModelProviders.CHIME));
	public static final DeferredBlock<Block> AMETHYST_CHIME = register(singleton(blockWithItem("amethyst_chime", () -> new GemstoneChimeBlock(chime(Blocks.AMETHYST_CLUSTER), SoundEvents.AMETHYST_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.MAGENTA), InkColors.MAGENTA), SpectrumTexturedModelProviders.CHIME));
	public static final DeferredBlock<Block> CITRINE_CHIME = register(singleton(blockWithItem("citrine_chime", () -> new GemstoneChimeBlock(chime(CITRINE_CLUSTER.get()), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.YELLOW), InkColors.YELLOW), SpectrumTexturedModelProviders.CHIME));
	public static final DeferredBlock<Block> ONYX_CHIME = register(singleton(blockWithItem("onyx_chime", () -> new GemstoneChimeBlock(chime(ONYX_CLUSTER.get()), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.BLACK), InkColors.BLACK), SpectrumTexturedModelProviders.CHIME));
	public static final DeferredBlock<Block> MOONSTONE_CHIME = register(singleton(blockWithItem("moonstone_chime", () -> new GemstoneChimeBlock(chime(MOONSTONE_CLUSTER.get()), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.WHITE), InkColors.WHITE), SpectrumTexturedModelProviders.CHIME));
	
	private static BlockBehaviour.Properties pylon(BlockBehaviour block) {
		return BlockBehaviour.Properties.ofFullCopy(block).noOcclusion();
	}
	
	public static final DeferredBlock<Block> TOPAZ_PYLON = register(pylon(blockWithItem("topaz_pylon", () -> new PylonBlock(pylon(TOPAZ_BLOCK.get())), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_PYLON = register(pylon(blockWithItem("amethyst_pylon", () -> new PylonBlock(pylon(AMETHYST_BLOCK)), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_PYLON = register(pylon(blockWithItem("citrine_pylon", () -> new PylonBlock(pylon(CITRINE_BLOCK.get())), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_PYLON = register(pylon(blockWithItem("onyx_pylon", () -> new PylonBlock(pylon(ONYX_BLOCK.get())), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_PYLON = register(pylon(blockWithItem("moonstone_pylon", () -> new PylonBlock(pylon(MOONSTONE_BLOCK.get())), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("semi_permeable_glass", () -> new AlternatePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), Blocks.GLASS, false), InkColors.WHITE), b -> Blocks.GLASS));
	public static final DeferredBlock<Block> TINTED_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("tinted_semi_permeable_glass", () -> new AlternatePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TINTED_GLASS), Blocks.TINTED_GLASS, true), InkColors.BLACK), b -> Blocks.TINTED_GLASS));
	public static final DeferredBlock<Block> RADIANT_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("radiant_semi_permeable_glass", () -> new AlternatePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(RADIANT_GLASS.get()), RADIANT_GLASS.get(), false), InkColors.YELLOW), b -> RADIANT_GLASS.get()));
	public static final DeferredBlock<Block> TOPAZ_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("topaz_semi_permeable_glass", () -> new GemstonePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(TOPAZ_GLASS.get()), BuiltinGemstoneColor.CYAN), InkColors.CYAN), b -> TOPAZ_GLASS.get()));
	public static final DeferredBlock<Block> AMETHYST_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("amethyst_semi_permeable_glass", () -> new GemstonePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(AMETHYST_GLASS.get()), BuiltinGemstoneColor.MAGENTA), InkColors.MAGENTA), b -> AMETHYST_GLASS.get()));
	public static final DeferredBlock<Block> CITRINE_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("citrine_semi_permeable_glass", () -> new GemstonePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(CITRINE_GLASS.get()), BuiltinGemstoneColor.YELLOW), InkColors.YELLOW), b -> CITRINE_GLASS.get()));
	public static final DeferredBlock<Block> ONYX_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("onyx_semi_permeable_glass", () -> new GemstonePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(ONYX_GLASS.get()), BuiltinGemstoneColor.BLACK), InkColors.BLACK), b -> ONYX_GLASS.get()));
	public static final DeferredBlock<Block> MOONSTONE_SEMI_PERMEABLE_GLASS = register(parented(blockWithItem("moonstone_semi_permeable_glass", () -> new GemstonePlayerOnlyGlassBlock(BlockBehaviour.Properties.ofFullCopy(MOONSTONE_GLASS.get()), BuiltinGemstoneColor.WHITE), InkColors.WHITE), b -> MOONSTONE_GLASS.get()));
	
	public static final DeferredBlock<Block> GLISTERING_MELON = register(singleton(new BlockRegistrar<>("glistering_melon").withBlock(() -> new Block(BlockBehaviour.Properties.ofFullCopy(MELON))).withItem(block -> new BlockItem(block, IS.of())), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> ATTACHED_GLISTERING_MELON_STEM = register(new BlockRegistrar<>("attached_glistering_melon_stem").withBlock(() -> new AttachedStemBlock(ResourceKey.create(Registries.BLOCK, SpectrumCommon.locate("glistering_melon_stem")), GLISTERING_MELON.getKey(), SpectrumItems.GLISTERING_MELON_SEEDS.getKey(), BlockBehaviour.Properties.ofFullCopy(Blocks.ATTACHED_MELON_STEM))));
	public static final DeferredBlock<Block> GLISTERING_MELON_STEM = register(new BlockRegistrar<>("glistering_melon_stem").withBlock(() -> new StemBlock(GLISTERING_MELON.getKey(), ATTACHED_GLISTERING_MELON_STEM.getKey(), SpectrumItems.GLISTERING_MELON_SEEDS.getKey(), BlockBehaviour.Properties.ofFullCopy(Blocks.MELON_STEM))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_7).generate(age -> SpectrumModelHelper.createModelVariant(ModelTemplates.STEMS[age].create(block, TextureMapping.stem(block), ctx.modelOutput))))).withBlockModel((ctx, block) -> {
		Block attached = BuiltInRegistries.BLOCK.get(ATTACHED_GLISTERING_MELON_STEM.getKey());
		// TODO: datagen
		// ctx.skipAutoItemBlock(block); // Needed b/c vanilla auto-generates an incorrect seeds model for some reason
		return SpectrumModelHelper.createVariantsSupplier(attached, ModelTemplates.ATTACHED_STEM.create(attached, TextureMapping.attachedStem(block, attached), ctx.modelOutput)).with(SpectrumModelHelper.createWestDefaultHorizontalFacingVariantMap());
	}));
	
	public static final DeferredBlock<PresentBlock> PRESENT = register(blockWithItem("present", () -> new PresentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)), block1 -> new PresentBlockItem(block1, IS.of().component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)), InkColors.LIGHT_GRAY).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(PresentBlock.VARIANT).generate(variant -> SpectrumModelHelper.createModelVariant(SpectrumModelTemplates.PRESENT.createWithSuffix(block, "_" + variant.getSerializedName(), new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(block, "_" + variant.getSerializedName())).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(variant.woolBase)), ctx.modelOutput))))).withPredefinedItemModel());
	public static final DeferredBlock<TitrationBarrelBlock> TITRATION_BARREL = register(blockWithItem("titration_barrel", () -> new TitrationBarrelBlock(BlockBehaviour.Properties.ofFullCopy(OAK_PLANKS).mapColor(MapColor.COLOR_RED)), InkColors.MAGENTA).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createUpDefaultHorizontalFacingVariantMap()).with(PropertyDispatch.property(TitrationBarrelBlock.BARREL_STATE).generate(state -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "_side", b -> b, "_top_" + state.getSerializedName(), b -> b, "_bottom").createWithSuffix(block, state == TitrationBarrelBlock.BarrelState.EMPTY ? "" : "_" + state.getSerializedName(), ctx.modelOutput))))));
	
	public static final DeferredBlock<ParametricMiningDeviceBlock> PARAMETRIC_MINING_DEVICE = register(blockWithItem("parametric_mining_device", () -> new ParametricMiningDeviceBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG.get()).noOcclusion().instabreak()), block -> new ParametricMiningDeviceItem(block, IS.of(16).component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_EXPLOSION))), InkColors.RED));
	public static final DeferredBlock<ThreatConfluxBlock> THREAT_CONFLUX = register(blockWithItem("threat_conflux", () -> new ThreatConfluxBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSLAG.get()).noOcclusion().instabreak()), block -> new ThreatConfluxItem(block, IS.of(16).component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_EXPLOSION))), InkColors.RED).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(ThreatConfluxBlock.ARMED).generate(armed -> SpectrumModelHelper.createModelVariant(block, armed == ThreatConfluxBlock.ArmedState.NOT_ARMED ? "" : "_armed")))));
	
	public static final DeferredBlock<Block> BLOCK_FLOODER = register(simple(block("block_flooder", () -> new BlockFlooderBlock(settings(MapColor.CLAY, SoundType.ROOTED_DIRT, 0.0F)))));
	public static final DeferredBlock<BottomlessBundleBlock> BOTTOMLESS_BUNDLE = register(blockWithItem("bottomless_bundle", () -> new BottomlessBundleBlock(settings(MapColor.ICE, SoundType.WOOL, 1.0F).noOcclusion().pushReaction(PushReaction.DESTROY)), block1 -> new BottomlessBundleItem(block1, IS.of(1)), InkColors.LIGHT_GRAY)
			.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(BottomlessBundleBlock.LOCKED, ModelLocationUtils.getModelLocation(block, "_locked"), ModelLocationUtils.getModelLocation(block, "_unlocked"))))
			.withPredefinedItemModel());
	
	public static final DeferredBlock<Block> PERSISTENT_LIGHT = register(singleton(block("persistent_light", () -> new PersistentLightBlock(BlockBehaviour.Properties.ofFullCopy(LIGHT).replaceable().sound(SpectrumSoundTypes.LIGHT).instabreak())), SpectrumTexturedModelProviders.particle(SpectrumTextures.SHIMMERSTONE_LIGHT)));
	public static final DeferredBlock<Block> TRANSIENT_LIGHT = register(parented(block("transient_light", () -> new TransientLightBlock(BlockBehaviour.Properties.ofFullCopy(PERSISTENT_LIGHT.get()).randomTicks())), b -> PERSISTENT_LIGHT.get()));
	
	private static BlockBehaviour.Properties decay(MapColor mapColor, SoundType soundGroup, float strength, float resistance, PushReaction pistonBehavior) {
		return settings(mapColor, soundGroup, strength, resistance).pushReaction(pistonBehavior).randomTicks().isValidSpawn((state, world, pos, type) -> false);
	}
	
	public static <T extends Block> BlockRegistrar<T> decay(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> {
			ResourceLocation none = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_none", SpectrumTextureMaps.all(block, "_none"), ctx.modelOutput);
			ResourceLocation def = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_default", SpectrumTextureMaps.all(block, "_default"), ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(DecayBlock.CONVERSION).select(DecayBlock.Conversion.NONE, createModelVariant(none)).select(DecayBlock.Conversion.DEFAULT, createModelVariant(def)).select(DecayBlock.Conversion.SPECIAL, createModelVariant(def)));
		});
	}
	
	public static final DeferredBlock<Block> FADING = register(decay(block("fading", () -> new FadingBlock(decay(MapColor.PLANT, SoundType.GRASS, 0.5F, 0.5F, PushReaction.DESTROY)))));
	public static final DeferredBlock<Block> FAILING = register(decay(block("failing", () -> new FailingBlock(decay(MapColor.COLOR_BLACK, SoundType.STONE, 20.0F, 50.0F, PushReaction.BLOCK)))));
	public static final DeferredBlock<Block> RUIN = register(decay(block("ruin", () -> new RuinBlock(decay(MapColor.COLOR_BLACK, SoundType.STONE, 100.0F, 3600000.0F, PushReaction.BLOCK)))));
	public static final DeferredBlock<Block> FORFEITURE = register(decay(block("forfeiture", () -> new ForfeitureBlock(decay(MapColor.COLOR_BLACK, SoundType.STONE, 100.0F, 3600000.0F, PushReaction.BLOCK)))));
	public static final DeferredBlock<Block> DECAY_AWAY = register(simple(block("decay_away", () -> new DecayAwayBlock(BlockBehaviour.Properties.ofFullCopy(DIRT).pushReaction(PushReaction.DESTROY)))));
	
	// PASTEL NETWORK
	private static BlockBehaviour.Properties pastelNode(SoundType soundGroup) {
		return settings(MapColor.NONE, soundGroup, 1.0F).pushReaction(PushReaction.DESTROY).noOcclusion();
	}
	
	public static final DeferredBlock<PastelNodeBlock> CONNECTION_NODE = register(blockWithItem("connection_node", () -> new PastelNodeBlock(pastelNode(SoundType.AMETHYST_CLUSTER), PastelNodeType.CONNECTION), () -> IS.of(), InkColors.LIGHT_GRAY).withPredefinedItemModel());
	public static final DeferredBlock<PastelNodeBlock> PROVIDER_NODE = register(blockWithItem("provider_node", () -> new PastelNodeBlock(pastelNode(SoundType.AMETHYST_CLUSTER), PastelNodeType.PROVIDER), () -> IS.of(), InkColors.MAGENTA).withPredefinedItemModel());
	public static final DeferredBlock<PastelNodeBlock> STORAGE_NODE = register(blockWithItem("storage_node", () -> new PastelNodeBlock(pastelNode(SpectrumSoundTypes.TOPAZ_CLUSTER), PastelNodeType.STORAGE), () -> IS.of(), InkColors.CYAN).withPredefinedItemModel());
	public static final DeferredBlock<PastelNodeBlock> SENDER_NODE = register(blockWithItem("sender_node", () -> new PastelNodeBlock(pastelNode(SpectrumSoundTypes.CITRINE_CLUSTER), PastelNodeType.SENDER), () -> IS.of(), InkColors.YELLOW).withPredefinedItemModel());
	public static final DeferredBlock<PastelNodeBlock> GATHER_NODE = register(blockWithItem("gather_node", () -> new PastelNodeBlock(pastelNode(SpectrumSoundTypes.ONYX_CLUSTER), PastelNodeType.GATHER), () -> IS.of(), InkColors.BLACK).withPredefinedItemModel());
	
	// COLORED BLOCK FAMILIES
	
	public static DeferredBlock<ColoredPlankBlock> registerColoredPlanks(String name, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredPlankBlock(copyWithMapColor(OAK_PLANKS, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredStairBlock> registerColoredStairs(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredStairBlock(baseBlock.get().defaultBlockState(), copyWithMapColor(OAK_STAIRS, baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredPressurePlateBlock> registerColoredPressurePlate(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredPressurePlateBlock(copyWithMapColor(OAK_PRESSURE_PLATE, baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredFenceBlock> registerColoredFence(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredFenceBlock(copyWithMapColor(OAK_FENCE, baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredFenceGateBlock> registerColoredFenceGate(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredFenceGateBlock(copyWithMapColor(OAK_FENCE_GATE, baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredWoodenButtonBlock> registerColoredButton(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredWoodenButtonBlock(copyWithMapColor(OAK_BUTTON, baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static DeferredBlock<ColoredSlabBlock> registerColoredSlab(String name, DeferredBlock<ColoredPlankBlock> baseBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredSlabBlock(copyWithMapColor(baseBlock.get(), baseBlock.get().defaultMapColor()), color), color));
	}
	
	public static final DeferredBlock<ColoredPlankBlock> BLACK_PLANKS = registerColoredPlanks("black_planks", InkColors.BLACK);
	public static final DeferredBlock<ColoredStairBlock> BLACK_STAIRS = registerColoredStairs("black_stairs", BLACK_PLANKS, InkColors.BLACK);
	public static final DeferredBlock<ColoredPressurePlateBlock> BLACK_PRESSURE_PLATE = registerColoredPressurePlate("black_pressure_plate", BLACK_PLANKS, InkColors.BLACK);
	public static final DeferredBlock<ColoredFenceBlock> BLACK_FENCE = registerColoredFence("black_fence", BLACK_PLANKS, InkColors.BLACK);
	public static final DeferredBlock<ColoredFenceGateBlock> BLACK_FENCE_GATE = registerColoredFenceGate("black_fence_gate", BLACK_PLANKS, InkColors.BLACK);
	public static final DeferredBlock<ColoredWoodenButtonBlock> BLACK_BUTTON = registerColoredButton("black_button", BLACK_PLANKS, InkColors.BLACK);
	public static final DeferredBlock<ColoredSlabBlock> BLACK_SLAB = registerColoredSlab("black_slab", BLACK_PLANKS, InkColors.BLACK);
	
	public static final DeferredBlock<ColoredPlankBlock> BLUE_PLANKS = registerColoredPlanks("blue_planks", InkColors.BLUE);
	public static final DeferredBlock<ColoredStairBlock> BLUE_STAIRS = registerColoredStairs("blue_stairs", BLUE_PLANKS, InkColors.BLUE);
	public static final DeferredBlock<ColoredPressurePlateBlock> BLUE_PRESSURE_PLATE = registerColoredPressurePlate("blue_pressure_plate", BLUE_PLANKS, InkColors.BLUE);
	public static final DeferredBlock<ColoredFenceBlock> BLUE_FENCE = registerColoredFence("blue_fence", BLUE_PLANKS, InkColors.BLUE);
	public static final DeferredBlock<ColoredFenceGateBlock> BLUE_FENCE_GATE = registerColoredFenceGate("blue_fence_gate", BLUE_PLANKS, InkColors.BLUE);
	public static final DeferredBlock<ColoredWoodenButtonBlock> BLUE_BUTTON = registerColoredButton("blue_button", BLUE_PLANKS, InkColors.BLUE);
	public static final DeferredBlock<ColoredSlabBlock> BLUE_SLAB = registerColoredSlab("blue_slab", BLUE_PLANKS, InkColors.BLUE);
	
	public static final DeferredBlock<ColoredPlankBlock> BROWN_PLANKS = registerColoredPlanks("brown_planks", InkColors.BROWN);
	public static final DeferredBlock<ColoredStairBlock> BROWN_STAIRS = registerColoredStairs("brown_stairs", BROWN_PLANKS, InkColors.BROWN);
	public static final DeferredBlock<ColoredPressurePlateBlock> BROWN_PRESSURE_PLATE = registerColoredPressurePlate("brown_pressure_plate", BROWN_PLANKS, InkColors.BROWN);
	public static final DeferredBlock<ColoredFenceBlock> BROWN_FENCE = registerColoredFence("brown_fence", BROWN_PLANKS, InkColors.BROWN);
	public static final DeferredBlock<ColoredFenceGateBlock> BROWN_FENCE_GATE = registerColoredFenceGate("brown_fence_gate", BROWN_PLANKS, InkColors.BROWN);
	public static final DeferredBlock<ColoredWoodenButtonBlock> BROWN_BUTTON = registerColoredButton("brown_button", BROWN_PLANKS, InkColors.BROWN);
	public static final DeferredBlock<ColoredSlabBlock> BROWN_SLAB = registerColoredSlab("brown_slab", BROWN_PLANKS, InkColors.BROWN);
	
	public static final DeferredBlock<ColoredPlankBlock> CYAN_PLANKS = registerColoredPlanks("cyan_planks", InkColors.CYAN);
	public static final DeferredBlock<ColoredStairBlock> CYAN_STAIRS = registerColoredStairs("cyan_stairs", CYAN_PLANKS, InkColors.CYAN);
	public static final DeferredBlock<ColoredPressurePlateBlock> CYAN_PRESSURE_PLATE = registerColoredPressurePlate("cyan_pressure_plate", CYAN_PLANKS, InkColors.CYAN);
	public static final DeferredBlock<ColoredFenceBlock> CYAN_FENCE = registerColoredFence("cyan_fence", CYAN_PLANKS, InkColors.CYAN);
	public static final DeferredBlock<ColoredFenceGateBlock> CYAN_FENCE_GATE = registerColoredFenceGate("cyan_fence_gate", CYAN_PLANKS, InkColors.CYAN);
	public static final DeferredBlock<ColoredWoodenButtonBlock> CYAN_BUTTON = registerColoredButton("cyan_button", CYAN_PLANKS, InkColors.CYAN);
	public static final DeferredBlock<ColoredSlabBlock> CYAN_SLAB = registerColoredSlab("cyan_slab", CYAN_PLANKS, InkColors.CYAN);
	
	public static final DeferredBlock<ColoredPlankBlock> GRAY_PLANKS = registerColoredPlanks("gray_planks", InkColors.GRAY);
	public static final DeferredBlock<ColoredStairBlock> GRAY_STAIRS = registerColoredStairs("gray_stairs", GRAY_PLANKS, InkColors.GRAY);
	public static final DeferredBlock<ColoredPressurePlateBlock> GRAY_PRESSURE_PLATE = registerColoredPressurePlate("gray_pressure_plate", GRAY_PLANKS, InkColors.GRAY);
	public static final DeferredBlock<ColoredFenceBlock> GRAY_FENCE = registerColoredFence("gray_fence", GRAY_PLANKS, InkColors.GRAY);
	public static final DeferredBlock<ColoredFenceGateBlock> GRAY_FENCE_GATE = registerColoredFenceGate("gray_fence_gate", GRAY_PLANKS, InkColors.GRAY);
	public static final DeferredBlock<ColoredWoodenButtonBlock> GRAY_BUTTON = registerColoredButton("gray_button", GRAY_PLANKS, InkColors.GRAY);
	public static final DeferredBlock<ColoredSlabBlock> GRAY_SLAB = registerColoredSlab("gray_slab", GRAY_PLANKS, InkColors.GRAY);
	
	public static final DeferredBlock<ColoredPlankBlock> GREEN_PLANKS = registerColoredPlanks("green_planks", InkColors.GREEN);
	public static final DeferredBlock<ColoredStairBlock> GREEN_STAIRS = registerColoredStairs("green_stairs", GREEN_PLANKS, InkColors.GREEN);
	public static final DeferredBlock<ColoredPressurePlateBlock> GREEN_PRESSURE_PLATE = registerColoredPressurePlate("green_pressure_plate", GREEN_PLANKS, InkColors.GREEN);
	public static final DeferredBlock<ColoredFenceBlock> GREEN_FENCE = registerColoredFence("green_fence", GREEN_PLANKS, InkColors.GREEN);
	public static final DeferredBlock<ColoredFenceGateBlock> GREEN_FENCE_GATE = registerColoredFenceGate("green_fence_gate", GREEN_PLANKS, InkColors.GREEN);
	public static final DeferredBlock<ColoredWoodenButtonBlock> GREEN_BUTTON = registerColoredButton("green_button", GREEN_PLANKS, InkColors.GREEN);
	public static final DeferredBlock<ColoredSlabBlock> GREEN_SLAB = registerColoredSlab("green_slab", GREEN_PLANKS, InkColors.GREEN);
	
	public static final DeferredBlock<ColoredPlankBlock> LIGHT_BLUE_PLANKS = registerColoredPlanks("light_blue_planks", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredStairBlock> LIGHT_BLUE_STAIRS = registerColoredStairs("light_blue_stairs", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredPressurePlateBlock> LIGHT_BLUE_PRESSURE_PLATE = registerColoredPressurePlate("light_blue_pressure_plate", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredFenceBlock> LIGHT_BLUE_FENCE = registerColoredFence("light_blue_fence", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredFenceGateBlock> LIGHT_BLUE_FENCE_GATE = registerColoredFenceGate("light_blue_fence_gate", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredWoodenButtonBlock> LIGHT_BLUE_BUTTON = registerColoredButton("light_blue_button", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredSlabBlock> LIGHT_BLUE_SLAB = registerColoredSlab("light_blue_slab", LIGHT_BLUE_PLANKS, InkColors.LIGHT_BLUE);
	
	public static final DeferredBlock<ColoredPlankBlock> LIGHT_GRAY_PLANKS = registerColoredPlanks("light_gray_planks", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredStairBlock> LIGHT_GRAY_STAIRS = registerColoredStairs("light_gray_stairs", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredPressurePlateBlock> LIGHT_GRAY_PRESSURE_PLATE = registerColoredPressurePlate("light_gray_pressure_plate", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredFenceBlock> LIGHT_GRAY_FENCE = registerColoredFence("light_gray_fence", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredFenceGateBlock> LIGHT_GRAY_FENCE_GATE = registerColoredFenceGate("light_gray_fence_gate", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredWoodenButtonBlock> LIGHT_GRAY_BUTTON = registerColoredButton("light_gray_button", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredSlabBlock> LIGHT_GRAY_SLAB = registerColoredSlab("light_gray_slab", LIGHT_GRAY_PLANKS, InkColors.LIGHT_GRAY);
	
	public static final DeferredBlock<ColoredPlankBlock> LIME_PLANKS = registerColoredPlanks("lime_planks", InkColors.LIME);
	public static final DeferredBlock<ColoredStairBlock> LIME_STAIRS = registerColoredStairs("lime_stairs", LIME_PLANKS, InkColors.LIME);
	public static final DeferredBlock<ColoredPressurePlateBlock> LIME_PRESSURE_PLATE = registerColoredPressurePlate("lime_pressure_plate", LIME_PLANKS, InkColors.LIME);
	public static final DeferredBlock<ColoredFenceBlock> LIME_FENCE = registerColoredFence("lime_fence", LIME_PLANKS, InkColors.LIME);
	public static final DeferredBlock<ColoredFenceGateBlock> LIME_FENCE_GATE = registerColoredFenceGate("lime_fence_gate", LIME_PLANKS, InkColors.LIME);
	public static final DeferredBlock<ColoredWoodenButtonBlock> LIME_BUTTON = registerColoredButton("lime_button", LIME_PLANKS, InkColors.LIME);
	public static final DeferredBlock<ColoredSlabBlock> LIME_SLAB = registerColoredSlab("lime_slab", LIME_PLANKS, InkColors.LIME);
	
	public static final DeferredBlock<ColoredPlankBlock> MAGENTA_PLANKS = registerColoredPlanks("magenta_planks", InkColors.MAGENTA);
	public static final DeferredBlock<ColoredStairBlock> MAGENTA_STAIRS = registerColoredStairs("magenta_stairs", MAGENTA_PLANKS, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredPressurePlateBlock> MAGENTA_PRESSURE_PLATE = registerColoredPressurePlate("magenta_pressure_plate", MAGENTA_PLANKS, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredFenceBlock> MAGENTA_FENCE = registerColoredFence("magenta_fence", MAGENTA_PLANKS, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredFenceGateBlock> MAGENTA_FENCE_GATE = registerColoredFenceGate("magenta_fence_gate", MAGENTA_PLANKS, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredWoodenButtonBlock> MAGENTA_BUTTON = registerColoredButton("magenta_button", MAGENTA_PLANKS, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredSlabBlock> MAGENTA_SLAB = registerColoredSlab("magenta_slab", MAGENTA_PLANKS, InkColors.MAGENTA);
	
	public static final DeferredBlock<ColoredPlankBlock> ORANGE_PLANKS = registerColoredPlanks("orange_planks", InkColors.ORANGE);
	public static final DeferredBlock<ColoredStairBlock> ORANGE_STAIRS = registerColoredStairs("orange_stairs", ORANGE_PLANKS, InkColors.ORANGE);
	public static final DeferredBlock<ColoredPressurePlateBlock> ORANGE_PRESSURE_PLATE = registerColoredPressurePlate("orange_pressure_plate", ORANGE_PLANKS, InkColors.ORANGE);
	public static final DeferredBlock<ColoredFenceBlock> ORANGE_FENCE = registerColoredFence("orange_fence", ORANGE_PLANKS, InkColors.ORANGE);
	public static final DeferredBlock<ColoredFenceGateBlock> ORANGE_FENCE_GATE = registerColoredFenceGate("orange_fence_gate", ORANGE_PLANKS, InkColors.ORANGE);
	public static final DeferredBlock<ColoredWoodenButtonBlock> ORANGE_BUTTON = registerColoredButton("orange_button", ORANGE_PLANKS, InkColors.ORANGE);
	public static final DeferredBlock<ColoredSlabBlock> ORANGE_SLAB = registerColoredSlab("orange_slab", ORANGE_PLANKS, InkColors.ORANGE);
	
	public static final DeferredBlock<ColoredPlankBlock> PINK_PLANKS = registerColoredPlanks("pink_planks", InkColors.PINK);
	public static final DeferredBlock<ColoredStairBlock> PINK_STAIRS = registerColoredStairs("pink_stairs", PINK_PLANKS, InkColors.PINK);
	public static final DeferredBlock<ColoredPressurePlateBlock> PINK_PRESSURE_PLATE = registerColoredPressurePlate("pink_pressure_plate", PINK_PLANKS, InkColors.PINK);
	public static final DeferredBlock<ColoredFenceBlock> PINK_FENCE = registerColoredFence("pink_fence", PINK_PLANKS, InkColors.PINK);
	public static final DeferredBlock<ColoredFenceGateBlock> PINK_FENCE_GATE = registerColoredFenceGate("pink_fence_gate", PINK_PLANKS, InkColors.PINK);
	public static final DeferredBlock<ColoredWoodenButtonBlock> PINK_BUTTON = registerColoredButton("pink_button", PINK_PLANKS, InkColors.PINK);
	public static final DeferredBlock<ColoredSlabBlock> PINK_SLAB = registerColoredSlab("pink_slab", PINK_PLANKS, InkColors.PINK);
	
	public static final DeferredBlock<ColoredPlankBlock> PURPLE_PLANKS = registerColoredPlanks("purple_planks", InkColors.PURPLE);
	public static final DeferredBlock<ColoredStairBlock> PURPLE_STAIRS = registerColoredStairs("purple_stairs", PURPLE_PLANKS, InkColors.PURPLE);
	public static final DeferredBlock<ColoredPressurePlateBlock> PURPLE_PRESSURE_PLATE = registerColoredPressurePlate("purple_pressure_plate", PURPLE_PLANKS, InkColors.PURPLE);
	public static final DeferredBlock<ColoredFenceBlock> PURPLE_FENCE = registerColoredFence("purple_fence", PURPLE_PLANKS, InkColors.PURPLE);
	public static final DeferredBlock<ColoredFenceGateBlock> PURPLE_FENCE_GATE = registerColoredFenceGate("purple_fence_gate", PURPLE_PLANKS, InkColors.PURPLE);
	public static final DeferredBlock<ColoredWoodenButtonBlock> PURPLE_BUTTON = registerColoredButton("purple_button", PURPLE_PLANKS, InkColors.PURPLE);
	public static final DeferredBlock<ColoredSlabBlock> PURPLE_SLAB = registerColoredSlab("purple_slab", PURPLE_PLANKS, InkColors.PURPLE);
	
	public static final DeferredBlock<ColoredPlankBlock> RED_PLANKS = registerColoredPlanks("red_planks", InkColors.RED);
	public static final DeferredBlock<ColoredStairBlock> RED_STAIRS = registerColoredStairs("red_stairs", RED_PLANKS, InkColors.RED);
	public static final DeferredBlock<ColoredPressurePlateBlock> RED_PRESSURE_PLATE = registerColoredPressurePlate("red_pressure_plate", RED_PLANKS, InkColors.RED);
	public static final DeferredBlock<ColoredFenceBlock> RED_FENCE = registerColoredFence("red_fence", RED_PLANKS, InkColors.RED);
	public static final DeferredBlock<ColoredFenceGateBlock> RED_FENCE_GATE = registerColoredFenceGate("red_fence_gate", RED_PLANKS, InkColors.RED);
	public static final DeferredBlock<ColoredWoodenButtonBlock> RED_BUTTON = registerColoredButton("red_button", RED_PLANKS, InkColors.RED);
	public static final DeferredBlock<ColoredSlabBlock> RED_SLAB = registerColoredSlab("red_slab", RED_PLANKS, InkColors.RED);
	
	public static final DeferredBlock<ColoredPlankBlock> WHITE_PLANKS = registerColoredPlanks("white_planks", InkColors.WHITE);
	public static final DeferredBlock<ColoredStairBlock> WHITE_STAIRS = registerColoredStairs("white_stairs", WHITE_PLANKS, InkColors.WHITE);
	public static final DeferredBlock<ColoredPressurePlateBlock> WHITE_PRESSURE_PLATE = registerColoredPressurePlate("white_pressure_plate", WHITE_PLANKS, InkColors.WHITE);
	public static final DeferredBlock<ColoredFenceBlock> WHITE_FENCE = registerColoredFence("white_fence", WHITE_PLANKS, InkColors.WHITE);
	public static final DeferredBlock<ColoredFenceGateBlock> WHITE_FENCE_GATE = registerColoredFenceGate("white_fence_gate", WHITE_PLANKS, InkColors.WHITE);
	public static final DeferredBlock<ColoredWoodenButtonBlock> WHITE_BUTTON = registerColoredButton("white_button", WHITE_PLANKS, InkColors.WHITE);
	public static final DeferredBlock<ColoredSlabBlock> WHITE_SLAB = registerColoredSlab("white_slab", WHITE_PLANKS, InkColors.WHITE);
	
	public static final DeferredBlock<ColoredPlankBlock> YELLOW_PLANKS = registerColoredPlanks("yellow_planks", InkColors.YELLOW);
	public static final DeferredBlock<ColoredStairBlock> YELLOW_STAIRS = registerColoredStairs("yellow_stairs", YELLOW_PLANKS, InkColors.YELLOW);
	public static final DeferredBlock<ColoredPressurePlateBlock> YELLOW_PRESSURE_PLATE = registerColoredPressurePlate("yellow_pressure_plate", YELLOW_PLANKS, InkColors.YELLOW);
	public static final DeferredBlock<ColoredFenceBlock> YELLOW_FENCE = registerColoredFence("yellow_fence", YELLOW_PLANKS, InkColors.YELLOW);
	public static final DeferredBlock<ColoredFenceGateBlock> YELLOW_FENCE_GATE = registerColoredFenceGate("yellow_fence_gate", YELLOW_PLANKS, InkColors.YELLOW);
	public static final DeferredBlock<ColoredWoodenButtonBlock> YELLOW_BUTTON = registerColoredButton("yellow_button", YELLOW_PLANKS, InkColors.YELLOW);
	public static final DeferredBlock<ColoredSlabBlock> YELLOW_SLAB = registerColoredSlab("yellow_slab", YELLOW_PLANKS, InkColors.YELLOW);
	
	//DD FLORA
	public static BlockBehaviour.Properties overgrownBlackslag(MapColor color, SoundType soundGroup) {
		return settings(color, soundGroup, BLACKSLAG_HARDNESS, BLACKSLAG_RESISTANCE).randomTicks();
	}
	
	public static final DeferredBlock<Block> SAWBLADE_GRASS = register(snowy(blockWithItem("sawblade_grass", () -> new BlackslagVegetationBlock(overgrownBlackslag(MapColor.SAND, SoundType.AZALEA_LEAVES)), InkColors.LIME), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG.get(), "_top", b -> b, "_top"), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG.get(), "_top", b -> b, "_snow_top")));
	public static final DeferredBlock<Block> SHIMMEL = register(snowy(blockWithItem("shimmel", () -> new BlackslagVegetationBlock(overgrownBlackslag(MapColor.TERRACOTTA_GRAY, SoundType.WART_BLOCK)), InkColors.LIME), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG.get(), "_top", b -> BLACKSLAG.get(), "_top"), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG.get(), "_top", b -> BLACKSLAG.get(), "_top")));
	public static final DeferredBlock<Block> OVERGROWN_BLACKSLAG = register(snowy(blockWithItem("overgrown_blackslag", () -> new BlackslagVegetationBlock(overgrownBlackslag(MapColor.PLANT, SoundType.VINE).speedFactor(0.925F)), InkColors.LIME), SpectrumTexturedModelProviders.overgrown(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG.get(), "_top", b -> b, "_fronds"), SpectrumTexturedModelProviders.overgrown(b -> b, "_snow_side", b -> b, "_snow_top", b -> BLACKSLAG.get(), "_top", b -> b, "_snow_fronds")));
	public static final DeferredBlock<RottenGroundBlock> ROTTEN_GROUND = register(blockWithItem("rotten_ground", () -> new RottenGroundBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD).mapColor(MapColor.STONE).sound(SoundType.HONEY_BLOCK).jumpFactor(0.9F).strength(5F, 15F).isValidSpawn((blockState, blockGetter, blockPos, entityType) -> true)), InkColors.GRAY).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(TexturedModel.CUBE.create(block, ctx.modelOutput)).with(VariantProperties.WEIGHT, 4), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_bony").createWithSuffix(block, "_bony", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_boil").createWithSuffix(block, "_boil", ctx.modelOutput)))));
	
	public static final float ASH_STRENGTH = 2F;
	
	public static BlockBehaviour.Properties ash(SoundType soundGroup) {
		return settings(MapColor.QUARTZ, soundGroup, ASH_STRENGTH);
	}
	
	public static final DeferredBlock<Block> ASHEN_BLACKSLAG = register(singleton(blockWithItem("ashen_blackslag", () -> new RotatedPillarBlock(blackslag(SoundType.DEEPSLATE).mapColor(MapColor.QUARTZ)), InkColors.LIGHT_GRAY), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> BLACKSLAG.get(), "_top", b -> b, "_top")));
	public static final DeferredBlock<AshBlock> ASH = register(blockWithItem("ash", () -> new AshBlock(ash(SoundType.POWDER_SNOW)), InkColors.GRAY).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "").createWithSuffix(block, "", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "2").createWithSuffix(block, "2", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "3").createWithSuffix(block, "3", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "4").createWithSuffix(block, "4", ctx.modelOutput)))));
	public static final DeferredBlock<AshPileBlock> ASH_PILE = register(blockWithItem("ash_pile", () -> new AshPileBlock(ash(SoundType.POWDER_SNOW).replaceable().isViewBlocking((state, world, pos) -> state.getValue(SnowLayerBlock.LAYERS) >= 8).pushReaction(PushReaction.DESTROY)), InkColors.LIGHT_GRAY).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_height2")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.LAYERS).generateList(height -> {
		ResourceLocation ash = TextureMapping.getBlockTexture(ASH.get());
		ResourceLocation ash2 = TextureMapping.getBlockTexture(ASH.get(), "2");
		ResourceLocation ash3 = TextureMapping.getBlockTexture(ASH.get(), "3");
		ResourceLocation ash4 = TextureMapping.getBlockTexture(ASH.get(), "4");
		if (height == 8) return List.of(SpectrumModelHelper.createModelVariant(ash), SpectrumModelHelper.createModelVariant(ash2), SpectrumModelHelper.createModelVariant(ash3), SpectrumModelHelper.createModelVariant(ash4));
		ModelTemplate layerModel = new ModelTemplate(Optional.of(ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height" + height * 2)), Optional.empty(), TextureSlot.PARTICLE, TextureSlot.TEXTURE);
		return List.of(
				createModelVariant(layerModel.create(SpectrumCommon.locate("block/ash_pile_height" + height * 2), TextureMapping.cube(ash), ctx.modelOutput)),
				createModelVariant(layerModel.create(SpectrumCommon.locate("block/ash2_pile_height" + height * 2), TextureMapping.cube(ash2), ctx.modelOutput)),
				createModelVariant(layerModel.create(SpectrumCommon.locate("block/ash3_pile_height" + height * 2), TextureMapping.cube(ash3), ctx.modelOutput)),
				createModelVariant(layerModel.create(SpectrumCommon.locate("block/ash4_pile_height" + height * 2), TextureMapping.cube(ash4), ctx.modelOutput))
		);
	}))));
	
	public static final DeferredBlock<AshFloraBlock> VARIA_SPROUT = register(blockWithItem("varia_sprout", () -> new AshFloraBlock(settings(MapColor.SNOW, SoundType.STEM, 0F).instabreak().lightLevel(state -> 11).offsetType(BlockBehaviour.OffsetType.XZ).dynamicShape().noCollission().hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.WHITE).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block,
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "").createWithSuffix(block, "", ctx.modelOutput)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_2").createWithSuffix(block, "_2", ctx.modelOutput)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_3").createWithSuffix(block, "_3", ctx.modelOutput)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_4").createWithSuffix(block, "_4", ctx.modelOutput)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_5").createWithSuffix(block, "_5", ctx.modelOutput)),
			SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_6").createWithSuffix(block, "_6", ctx.modelOutput)))));
	
	public static final ToIntFunction<BlockState> LANTERN_LIGHT_PROVIDER = (state -> state.getValue(RedstoneLampBlock.LIT) ? 15 : 0);
	
	public static DeferredBlock<FungusBlock> registerNoxshroom(String name, ResourceKey<ConfiguredFeature<?, ?>> feature, MapColor mapColor) {
		return register(blockWithItem(name, () -> new FungusBlock(feature, SHIMMEL.get(), settings(mapColor, SoundType.FUNGUS, 0.0F).noCollission()), InkColors.LIME)
				.withBlockItemModel((ctx, block) -> ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), SpectrumTextureMaps.layer0(block, "_type_1"), ctx.output)).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block,
						createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_type_1").createWithSuffix(block, "_type_1", ctx.modelOutput)),
						createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_type_2").createWithSuffix(block, "_type_2", ctx.modelOutput)),
						createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_type_3").createWithSuffix(block, "_type_3", ctx.modelOutput)))));
	}
	
	public static final DeferredBlock<FungusBlock> SLATE_NOXSHROOM = registerNoxshroom("slate_noxshroom", SpectrumConfiguredFeatureKeys.SLATE_NOXFUNGUS, MapColor.COLOR_GRAY);
	public static final DeferredBlock<FungusBlock> EBONY_NOXSHROOM = registerNoxshroom("ebony_noxshroom", SpectrumConfiguredFeatureKeys.EBONY_NOXFUNGUS, MapColor.TERRACOTTA_BLACK);
	public static final DeferredBlock<FungusBlock> IVORY_NOXSHROOM = registerNoxshroom("ivory_noxshroom", SpectrumConfiguredFeatureKeys.IVORY_NOXFUNGUS, MapColor.QUARTZ);
	public static final DeferredBlock<FungusBlock> CHESTNUT_NOXSHROOM = registerNoxshroom("chestnut_noxshroom", SpectrumConfiguredFeatureKeys.CHESTNUT_NOXFUNGUS, MapColor.CRIMSON_NYLIUM);
	
	public static final DeferredBlock<FlowerPotBlock> POTTED_SLATE_NOXSHROOM = register(pottedPlantWithCustomTexture(block("potted_slate_noxshroom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, SLATE_NOXSHROOM, pottedPlant())), "_type_1"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_EBONY_NOXSHROOM = register(pottedPlantWithCustomTexture(block("potted_ebony_noxshroom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, EBONY_NOXSHROOM, pottedPlant())), "_type_1"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_IVORY_NOXSHROOM = register(pottedPlantWithCustomTexture(block("potted_ivory_noxshroom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, IVORY_NOXSHROOM, pottedPlant())), "_type_1"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_CHESTNUT_NOXSHROOM = register(pottedPlantWithCustomTexture(block("potted_chestnut_noxshroom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, CHESTNUT_NOXSHROOM, pottedPlant())), "_type_1"));
	
	public static BlockBehaviour.Properties noxcap(MapColor color) {
		return settings(color, SoundType.STEM, 4.0F).instrument(NoteBlockInstrument.BASS);
	}
	
	public static DeferredBlock<RotatedPillarBlock> registerNoxwoodLightBlock(String name, DeferredBlock<?> gillsBlock, MapColor color) {
		return register(axisRotated(blockWithItem(name, () -> new FlammableRotatedPillarBlock(noxcap(color).lightLevel(state -> 15)), InkColors.LIME), TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopInside(b, "", b, "_top", gillsBlock.get(), ""), SpectrumModelTemplates.MULTILAYER_LIGHT)));
	}
	
	public static<T extends FlexLanternBlock> DeferredBlock<T> registerNoxwoodLantern(String name, Supplier<T> flexLanternBlock, InkColor color) {
		return register(blockWithItem(name, flexLanternBlock, color)
				.withItemModel((ctx, item) -> SpectrumModelHelper.registerItemModel(ctx, item, "_item"))
				.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.HANGING, DiagonalBlock.DIAGONAL, FlexLanternBlock.TALL)
						.generate((hanging, diagonal, tall) -> { String suffix = (hanging ? "_hanging" : "") + (diagonal ? "_diagonal" : "") + (tall ? "_tall" : "_small");
			return SpectrumModelHelper.createModelVariant(SpectrumModelTemplates.noxwoodLantern(suffix).createWithSuffix(block, suffix, TextureMapping.cube(block), ctx.modelOutput));
		}))));
	}
	
	private static final int NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS = 30;
	
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SLATE_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_slate_noxcap_stem", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SLATE_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_slate_noxcap_hyphae", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM.get(), "", b -> STRIPPED_SLATE_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> SLATE_NOXCAP_STEM = register(axisRotated(blockWithItem("slate_noxcap_stem", () -> new StrippingLootPillarBlock(noxcap(MapColor.COLOR_GRAY), STRIPPED_SLATE_NOXCAP_STEM, SpectrumLootTableKeys.SLATE_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> SLATE_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("slate_noxcap_hyphae", () -> new StrippingLootPillarBlock(noxcap(MapColor.COLOR_GRAY), STRIPPED_SLATE_NOXCAP_HYPHAE, SpectrumLootTableKeys.SLATE_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> SLATE_NOXCAP_STEM.get(), "", b -> SLATE_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> SLATE_NOXCAP_BLOCK = register(singleton(blockWithItem("slate_noxcap_block", () -> new FlammablePlankBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> SLATE_NOXCAP_GILLS = register(axisRotated(blockWithItem("slate_noxcap_gills", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.DIAMOND).lightLevel(state -> 9).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	
	public static final DeferredBlock<Block> SLATE_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("slate_noxwood_pillar", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("slate_noxwood_lamp", () -> new FlammableRedstoneLampBlock(noxcap(MapColor.COLOR_GRAY).lightLevel(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final DeferredBlock<RotatedPillarBlock> SLATE_NOXWOOD_LIGHT = registerNoxwoodLightBlock("slate_noxwood_light", SLATE_NOXCAP_GILLS, MapColor.COLOR_GRAY);
	public static final DeferredBlock<Block> SLATE_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("slate_noxwood_amphora", () -> new AmphoraBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME), b -> SLATE_NOXWOOD_LIGHT.get(), "_top"));
	public static final DeferredBlock<FlexLanternBlock> SLATE_NOXWOOD_LANTERN = registerNoxwoodLantern("slate_noxwood_lantern", () -> new FlexLanternBlock(BlockBehaviour.Properties.ofFullCopy(LANTERN).lightLevel(s -> 13).pushReaction(PushReaction.DESTROY)), InkColors.LIME);
	
	public static final DeferredBlock<Block> SLATE_NOXWOOD_PLANKS = register(blockWithItem("slate_noxwood_planks", () -> new FlammablePlankBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_STAIRS = register(blockWithItem("slate_noxwood_stairs", () -> new FlammableStairBlock(SLATE_NOXWOOD_PLANKS.get().defaultBlockState(), noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_SLAB = register(blockWithItem("slate_noxwood_slab", () -> new FlammableSlabBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_FENCE = register(blockWithItem("slate_noxwood_fence", () -> new FlammableFenceBlock(noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_FENCE_GATE = register(blockWithItem("slate_noxwood_fence_gate", () -> new FlammableFenceGateBlock(SpectrumWoodTypes.SLATE_NOXWOOD, noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_DOOR = register(blockWithItem("slate_noxwood_door", () -> new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_TRAPDOOR = register(blockWithItem("slate_noxwood_trapdoor", () -> new TrapDoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_BUTTON = register(blockWithItem("slate_noxwood_button", () -> new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.COLOR_GRAY).pushReaction(PushReaction.DESTROY)), InkColors.LIME));
	public static final DeferredBlock<Block> SLATE_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("slate_noxwood_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.COLOR_GRAY)), InkColors.LIME));
	
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EBONY_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_ebony_noxcap_stem", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EBONY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_ebony_noxcap_hyphae", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> STRIPPED_EBONY_NOXCAP_STEM.get(), "", b -> STRIPPED_EBONY_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> EBONY_NOXCAP_STEM = register(axisRotated(blockWithItem("ebony_noxcap_stem", () -> new StrippingLootPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK), STRIPPED_EBONY_NOXCAP_STEM, SpectrumLootTableKeys.EBONY_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> EBONY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("ebony_noxcap_hyphae", () -> new StrippingLootPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK), STRIPPED_EBONY_NOXCAP_HYPHAE, SpectrumLootTableKeys.EBONY_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> EBONY_NOXCAP_STEM.get(), "", b -> EBONY_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> EBONY_NOXCAP_BLOCK = register(singleton(blockWithItem("ebony_noxcap_block", () -> new FlammablePlankBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> EBONY_NOXCAP_GILLS = register(axisRotated(blockWithItem("ebony_noxcap_gills", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.DIAMOND).lightLevel(state -> 9).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	
	public static final DeferredBlock<Block> EBONY_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("ebony_noxwood_pillar", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("ebony_noxwood_lamp", () -> new FlammableRedstoneLampBlock(noxcap(MapColor.TERRACOTTA_BLACK).lightLevel(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final DeferredBlock<RotatedPillarBlock> EBONY_NOXWOOD_LIGHT = registerNoxwoodLightBlock("ebony_noxwood_light", EBONY_NOXCAP_GILLS, MapColor.TERRACOTTA_BLACK);
	public static final DeferredBlock<Block> EBONY_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("ebony_noxwood_amphora", () -> new AmphoraBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME), b -> EBONY_NOXWOOD_LIGHT.get(), "_top"));
	public static final DeferredBlock<FlexLanternBlock> EBONY_NOXWOOD_LANTERN = registerNoxwoodLantern("ebony_noxwood_lantern", () -> new FlexLanternBlock(BlockBehaviour.Properties.ofFullCopy(LANTERN).lightLevel(s -> 13).pushReaction(PushReaction.DESTROY)), InkColors.LIME);
	
	public static final DeferredBlock<Block> EBONY_NOXWOOD_PLANKS = register(blockWithItem("ebony_noxwood_planks", () -> new FlammablePlankBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_STAIRS = register(blockWithItem("ebony_noxwood_stairs", () -> new FlammableStairBlock(EBONY_NOXWOOD_PLANKS.get().defaultBlockState(), noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_SLAB = register(blockWithItem("ebony_noxwood_slab", () -> new FlammableSlabBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_FENCE = register(blockWithItem("ebony_noxwood_fence", () -> new FlammableFenceBlock(noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_FENCE_GATE = register(blockWithItem("ebony_noxwood_fence_gate", () -> new FlammableFenceGateBlock(SpectrumWoodTypes.EBONY_NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_DOOR = register(blockWithItem("ebony_noxwood_door", () -> new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_TRAPDOOR = register(blockWithItem("ebony_noxwood_trapdoor", () -> new TrapDoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_BUTTON = register(blockWithItem("ebony_noxwood_button", () -> new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.TERRACOTTA_BLACK).pushReaction(PushReaction.DESTROY)), InkColors.LIME));
	public static final DeferredBlock<Block> EBONY_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("ebony_noxwood_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.TERRACOTTA_BLACK)), InkColors.LIME));
	
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_IVORY_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_ivory_noxcap_stem", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_IVORY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_ivory_noxcap_hyphae", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> STRIPPED_IVORY_NOXCAP_STEM.get(), "", b -> STRIPPED_IVORY_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> IVORY_NOXCAP_STEM = register(axisRotated(blockWithItem("ivory_noxcap_stem", () -> new StrippingLootPillarBlock(noxcap(MapColor.QUARTZ), STRIPPED_IVORY_NOXCAP_STEM, SpectrumLootTableKeys.IVORY_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> IVORY_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("ivory_noxcap_hyphae", () -> new StrippingLootPillarBlock(noxcap(MapColor.QUARTZ), STRIPPED_IVORY_NOXCAP_HYPHAE, SpectrumLootTableKeys.IVORY_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> IVORY_NOXCAP_STEM.get(), "", b -> IVORY_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> IVORY_NOXCAP_BLOCK = register(singleton(blockWithItem("ivory_noxcap_block", () -> new FlammablePlankBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> IVORY_NOXCAP_GILLS = register(axisRotated(blockWithItem("ivory_noxcap_gills", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.DIAMOND).lightLevel(state -> 9).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	
	public static final DeferredBlock<Block> IVORY_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("ivory_noxwood_pillar", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("ivory_noxwood_lamp", () -> new FlammableRedstoneLampBlock(noxcap(MapColor.QUARTZ).lightLevel(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final DeferredBlock<RotatedPillarBlock> IVORY_NOXWOOD_LIGHT = registerNoxwoodLightBlock("ivory_noxwood_light", IVORY_NOXCAP_GILLS, MapColor.QUARTZ);
	public static final DeferredBlock<Block> IVORY_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("ivory_noxwood_amphora", () -> new AmphoraBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), b -> IVORY_NOXWOOD_LIGHT.get(), "_top"));
	public static final DeferredBlock<FlexLanternBlock> IVORY_NOXWOOD_LANTERN = registerNoxwoodLantern("ivory_noxwood_lantern", () -> new FlexLanternBlock(BlockBehaviour.Properties.ofFullCopy(LANTERN).lightLevel(s -> 13).pushReaction(PushReaction.DESTROY)), InkColors.LIME);
	
	public static final DeferredBlock<Block> IVORY_NOXWOOD_PLANKS = register(blockWithItem("ivory_noxwood_planks", () -> new FlammablePlankBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_STAIRS = register(blockWithItem("ivory_noxwood_stairs", () -> new FlammableStairBlock(IVORY_NOXWOOD_PLANKS.get().defaultBlockState(), noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_SLAB = register(blockWithItem("ivory_noxwood_slab", () -> new FlammableSlabBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_FENCE = register(blockWithItem("ivory_noxwood_fence", () -> new FlammableFenceBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_FENCE_GATE = register(blockWithItem("ivory_noxwood_fence_gate", () -> new FlammableFenceGateBlock(SpectrumWoodTypes.CHESTNUT_NOXWOOD, noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_DOOR = register(blockWithItem("ivory_noxwood_door", () -> new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_TRAPDOOR = register(blockWithItem("ivory_noxwood_trapdoor", () -> new TrapDoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.QUARTZ)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_BUTTON = register(blockWithItem("ivory_noxwood_button", () -> new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.QUARTZ).pushReaction(PushReaction.DESTROY)), InkColors.LIME));
	public static final DeferredBlock<Block> IVORY_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("ivory_noxwood_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.QUARTZ)), InkColors.LIME));
	
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CHESTNUT_NOXCAP_STEM = register(axisRotated(blockWithItem("stripped_chestnut_noxcap_stem", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CHESTNUT_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("stripped_chestnut_noxcap_hyphae", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.QUARTZ)), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> STRIPPED_CHESTNUT_NOXCAP_STEM.get(), "", b -> STRIPPED_CHESTNUT_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> CHESTNUT_NOXCAP_STEM = register(axisRotated(blockWithItem("chestnut_noxcap_stem", () -> new StrippingLootPillarBlock(noxcap(MapColor.CRIMSON_NYLIUM), STRIPPED_CHESTNUT_NOXCAP_STEM, SpectrumLootTableKeys.CHESTNUT_NOXCAP_STRIPPING), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> CHESTNUT_NOXCAP_HYPHAE = register(axisRotated(blockWithItem("chestnut_noxcap_hyphae", () -> new StrippingLootPillarBlock(noxcap(MapColor.QUARTZ), STRIPPED_CHESTNUT_NOXCAP_HYPHAE, SpectrumLootTableKeys.CHESTNUT_NOXCAP_STRIPPING), InkColors.LIME), SpectrumTexturedModelProviders.cubeColumn(b -> CHESTNUT_NOXCAP_STEM.get(), "", b -> CHESTNUT_NOXCAP_STEM.get(), "")));
	public static final DeferredBlock<Block> CHESTNUT_NOXCAP_BLOCK = register(singleton(blockWithItem("chestnut_noxcap_block", () -> new FlammablePlankBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> CHESTNUT_NOXCAP_GILLS = register(axisRotated(blockWithItem("chestnut_noxcap_gills", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.DIAMOND).lightLevel(state -> 9).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_PILLAR = register(axisRotated(blockWithItem("chestnut_noxwood_pillar", () -> new FlammableRotatedPillarBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_LAMP = register(redstoneLamp(blockWithItem("chestnut_noxwood_lamp", () -> new FlammableRedstoneLampBlock(noxcap(MapColor.CRIMSON_NYLIUM).lightLevel(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final DeferredBlock<RotatedPillarBlock> CHESTNUT_NOXWOOD_LIGHT = registerNoxwoodLightBlock("chestnut_noxwood_light", CHESTNUT_NOXCAP_GILLS, MapColor.CRIMSON_NYLIUM);
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_AMPHORA = register(barrellike(blockWithItem("chestnut_noxwood_amphora", () -> new AmphoraBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME), b -> CHESTNUT_NOXWOOD_LIGHT.get(), "_top"));
	public static final DeferredBlock<FlexLanternBlock> CHESTNUT_NOXWOOD_LANTERN = registerNoxwoodLantern("chestnut_noxwood_lantern", () -> new FlexLanternBlock(BlockBehaviour.Properties.ofFullCopy(LANTERN).lightLevel(s -> 13).pushReaction(PushReaction.DESTROY)), InkColors.LIME);
	
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_PLANKS = register(blockWithItem("chestnut_noxwood_planks", () -> new FlammablePlankBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_STAIRS = register(blockWithItem("chestnut_noxwood_stairs", () -> new FlammableStairBlock(CHESTNUT_NOXWOOD_PLANKS.get().defaultBlockState(), noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_SLAB = register(blockWithItem("chestnut_noxwood_slab", () -> new FlammableSlabBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_FENCE = register(blockWithItem("chestnut_noxwood_fence", () -> new FlammableFenceBlock(noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_FENCE_GATE = register(blockWithItem("chestnut_noxwood_fence_gate", () -> new FlammableFenceGateBlock(SpectrumWoodTypes.IVORY_NOXWOOD, noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_DOOR = register(blockWithItem("chestnut_noxwood_door", () -> new DoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_TRAPDOOR = register(blockWithItem("chestnut_noxwood_trapdoor", () -> new TrapDoorBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_BUTTON = register(blockWithItem("chestnut_noxwood_button", () -> new ButtonBlock(SpectrumBlockSetTypes.NOXWOOD, NOXCAP_BUTTON_BLOCK_PRESS_TIME_TICKS, noxcap(MapColor.CRIMSON_NYLIUM).pushReaction(PushReaction.DESTROY)), InkColors.LIME));
	public static final DeferredBlock<Block> CHESTNUT_NOXWOOD_PRESSURE_PLATE = register(blockWithItem("chestnut_noxwood_pressure_plate", () -> new PressurePlateBlock(SpectrumBlockSetTypes.NOXWOOD, noxcap(MapColor.CRIMSON_NYLIUM)), InkColors.LIME));
	
	public static BlockBehaviour.Properties galaWood(MapColor color) {
		return settings(color, SoundType.CHERRY_WOOD, 30.0F).instrument(NoteBlockInstrument.BASS).ignitedByLava();
	}
	
	public static final DeferredBlock<WeepingGalaSprigBlock> WEEPING_GALA_SPRIG = register(cross(blockWithItem("weeping_gala_sprig", () -> new WeepingGalaSprigBlock(copyWithMapColor(OAK_SAPLING, MapColor.WARPED_WART_BLOCK)), InkColors.LIME)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final DeferredBlock<FlowerPotBlock> POTTED_WEEPING_GALA_SPRIG = register(pottedPlant(block("potted_weeping_gala_sprig", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, () -> WEEPING_GALA_SPRIG.get(), pottedPlant())), false));
	
	public static final DeferredBlock<Block> WEEPING_GALA_LEAVES = register(singleton(blockWithItem("weeping_gala_leaves", () -> new FlammableLeavesBlock(copyWithMapColor(OAK_LEAVES, MapColor.WARPED_WART_BLOCK)), InkColors.LIME), TexturedModel.LEAVES));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_WEEPING_GALA_LOG = register(log(blockWithItem("stripped_weeping_gala_log", () -> new FlammableRotatedPillarBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME)));
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_WEEPING_GALA_WOOD = register(blockWithItem("stripped_weeping_gala_wood", () -> new FlammableRotatedPillarBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_LOG = register(log(blockWithItem("weeping_gala_log", () -> new FlammableLogBlock(galaWood(MapColor.COLOR_BROWN), STRIPPED_WEEPING_GALA_WOOD), InkColors.LIME)));
	public static final DeferredBlock<Block> WEEPING_GALA_WOOD = register(blockWithItem("weeping_gala_wood", () -> new FlammableLogBlock(galaWood(MapColor.COLOR_BROWN), STRIPPED_WEEPING_GALA_LOG), InkColors.LIME));
	
	public static final DeferredBlock<Block> WEEPING_GALA_FRONDS = register(cross(block("weeping_gala_fronds", () -> new WeepingGalaFrondsBlock(BlockBehaviour.Properties.ofFullCopy(WEEPING_GALA_LEAVES.get()).noCollission()))));
	public static final DeferredBlock<WeepingGalaFrondsTipBlock> WEEPING_GALA_FRONDS_PLANT = register(block("weeping_gala_fronds_plant", () -> new WeepingGalaFrondsTipBlock(BlockBehaviour.Properties.ofFullCopy(WEEPING_GALA_LEAVES.get()).noCollission().lightLevel(s -> s.getValue(WeepingGalaFrondsTipBlock.FORM).getLuminance()))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(WeepingGalaFrondsTipBlock.FORM)
			.select(WeepingGalaFrondsTipBlock.Form.TIP, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> WEEPING_GALA_FRONDS.get(), "_tip").create(block, ctx.modelOutput)))
			.select(WeepingGalaFrondsTipBlock.Form.SPRIG, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> WEEPING_GALA_FRONDS.get(), "_sprig").createWithSuffix(block, "_sprig", ctx.modelOutput)))
			.select(WeepingGalaFrondsTipBlock.Form.RESIN, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> WEEPING_GALA_FRONDS.get(), "_sprig_resin").createWithSuffix(block, "_resin", ctx.modelOutput))))));
	
	public static final BlockSetType GALA_BLOCK_SET_TYPE = new BlockSetType("spectrum_gala");
	public static final WoodType GALA_WOOD_TYPE = new WoodType("spectrum_gala", GALA_BLOCK_SET_TYPE);
	
	public static final DeferredBlock<Block> WEEPING_GALA_PLANKS = register(blockWithItem("weeping_gala_planks", () -> new FlammablePlankBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_STAIRS = register(blockWithItem("weeping_gala_stairs", () -> new FlammableStairBlock(WEEPING_GALA_PLANKS.get().defaultBlockState(), galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_SLAB = register(blockWithItem("weeping_gala_slab", () -> new FlammableSlabBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_FENCE = register(blockWithItem("weeping_gala_fence", () -> new FlammableFenceBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_FENCE_GATE = register(blockWithItem("weeping_gala_fence_gate", () -> new FlammableFenceGateBlock(GALA_WOOD_TYPE, galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_DOOR = register(blockWithItem("weeping_gala_door", () -> new DoorBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_TRAPDOOR = register(blockWithItem("weeping_gala_trapdoor", () -> new TrapDoorBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_BUTTON = register(blockWithItem("weeping_gala_button", () -> woodenButton(GALA_BLOCK_SET_TYPE), InkColors.LIME));
	public static final DeferredBlock<Block> WEEPING_GALA_PRESSURE_PLATE = register(blockWithItem("weeping_gala_pressure_plate", () -> new PressurePlateBlock(GALA_BLOCK_SET_TYPE, galaWood(MapColor.COLOR_BROWN)), InkColors.LIME));
	
	public static final DeferredBlock<Block> WEEPING_GALA_PILLAR = register(axisRotated(blockWithItem("weeping_gala_pillar", () -> new FlammableRotatedPillarBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME), TexturedModel.COLUMN));
	public static final DeferredBlock<Block> WEEPING_GALA_BARREL = register(barrellike(blockWithItem("weeping_gala_barrel", () -> new BarrelBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME), b -> b, "_bottom"));
	public static final DeferredBlock<Block> WEEPING_GALA_AMPHORA = register(barrellike(blockWithItem("weeping_gala_amphora", () -> new AmphoraBlock(galaWood(MapColor.COLOR_BROWN)), InkColors.LIME), b -> b, "_bottom"));
	public static final DeferredBlock<WeepingGalaLanternBlock> WEEPING_GALA_LANTERN = register(blockWithItem("weeping_gala_lantern", () -> new WeepingGalaLanternBlock(galaWood(MapColor.COLOR_BROWN).lightLevel(state -> 13).noOcclusion().pushReaction(PushReaction.DESTROY)), InkColors.LIME).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.HANGING).select(false, Variant.variant()).select(true, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))).with(PropertyDispatch.properties(DiagonalBlock.DIAGONAL, FlexLanternBlock.TALL).generate((diagonal, tall) -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.baseTransLantern(diagonal, tall).createWithSuffix(block, (diagonal ? "_diagonal" : "") + (tall ? "_tall" : "_small"), ctx.modelOutput))))).withItemModel((ctx, item) -> SpectrumModelHelper.registerItemModel(ctx, item, "_item")));
	public static final DeferredBlock<Block> WEEPING_GALA_LAMP = register(redstoneLamp(blockWithItem("weeping_gala_lamp", () -> new FlammableRedstoneLampBlock(galaWood(MapColor.COLOR_BROWN).lightLevel(LANTERN_LIGHT_PROVIDER)), InkColors.LIME)));
	public static final DeferredBlock<Block> WEEPING_GALA_LIGHT = register(axisRotated(blockWithItem("weeping_gala_light", () -> new FlammableRotatedPillarBlock(galaWood(MapColor.COLOR_BROWN).lightLevel(state -> 15).noOcclusion()), InkColors.LIME), SpectrumTexturedModelProviders.BASE_TRANS_LIGHT_CORE));
	
	public static BlockBehaviour.Properties basalMarble() {
		return settings(MapColor.COLOR_GRAY, SoundType.DRIPSTONE_BLOCK, 8.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops();
	}
	
	public static final DeferredBlock<Block> BASAL_MARBLE = register(axisRotated(blockWithItem("basal_marble", () -> new RotatedPillarBlock(basalMarble()), InkColors.BROWN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BASAL_MARBLE_STAIRS = register(blockWithItem("basal_marble_stairs", () -> new StairBlock(BASAL_MARBLE.get().defaultBlockState(), basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_SLAB = register(blockWithItem("basal_marble_slab", () -> new SlabBlock(basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_WALL = register(blockWithItem("basal_marble_wall", () -> new WallBlock(basalMarble()), InkColors.BROWN));
	
	public static final DeferredBlock<Block> BASAL_MARBLE_PILLAR = register(axisRotated(blockWithItem("basal_marble_pillar", () -> new RotatedPillarBlock(basalMarble()), InkColors.BROWN), TexturedModel.COLUMN));
	
	public static final DeferredBlock<Block> POLISHED_BASAL_MARBLE = register(defaultUpFacing(blockWithItem("polished_basal_marble", () -> new SpectrumFacingBlock(basalMarble()), InkColors.BROWN), TexturedModel.CUBE_TOP_BOTTOM));
	public static final DeferredBlock<Block> POLISHED_BASAL_MARBLE_STAIRS = register(blockWithItem("polished_basal_marble_stairs", () -> new StairBlock(POLISHED_BASAL_MARBLE.get().defaultBlockState(), basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASAL_MARBLE_SLAB = register(blockWithItem("polished_basal_marble_slab", () -> new SlabBlock(basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> POLISHED_BASAL_MARBLE_WALL = register(blockWithItem("polished_basal_marble_wall", () -> new WallBlock(basalMarble()), InkColors.BROWN));
	
	public static final DeferredBlock<Block> BASAL_MARBLE_TILES = register(blockWithItem("basal_marble_tiles", () -> new Block(basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_TILE_STAIRS = register(blockWithItem("basal_marble_tile_stairs", () -> new StairBlock(BASAL_MARBLE_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_TILE_SLAB = register(blockWithItem("basal_marble_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_TILES.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_TILE_WALL = register(blockWithItem("basal_marble_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_TILES.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> BASAL_MARBLE_BRICKS = register(blockWithItem("basal_marble_bricks", () -> new Block(basalMarble()), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_BRICK_STAIRS = register(blockWithItem("basal_marble_brick_stairs", () -> new StairBlock(BASAL_MARBLE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_BRICK_SLAB = register(blockWithItem("basal_marble_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_BRICKS.get())), InkColors.BROWN));
	public static final DeferredBlock<Block> BASAL_MARBLE_BRICK_WALL = register(blockWithItem("basal_marble_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BASAL_MARBLE_BRICKS.get())), InkColors.BROWN));
	
	public static final DeferredBlock<Block> LONGING_CHIMERA = register(defaultNorthHorizontalFacing(blockWithItem("longing_chimera", () -> new GrotesqueBlock(basalMarble().noOcclusion(), 12, 15, "block.spectrum.longing_chimera.tooltip"), InkColors.BROWN), ModelLocationUtils::getModelLocation));
	
	public static DeferredBlock<SmallDragonjagBlock> registerSmallDragonjagBlock(String name, Dragonjag.Variant variant) {
		return register(singleton(blockWithItem(name, () -> new SmallDragonjagBlock(settings(variant.getMapColor(), SoundType.GRASS, 1.0F), variant), InkColors.LIME), SpectrumTexturedModelProviders.doubleCross(b -> b, "")).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel));
	}
	
	public static final DeferredBlock<SmallDragonjagBlock> SMALL_RED_DRAGONJAG = registerSmallDragonjagBlock("small_red_dragonjag", Dragonjag.Variant.RED);
	public static final DeferredBlock<SmallDragonjagBlock> SMALL_YELLOW_DRAGONJAG = registerSmallDragonjagBlock("small_yellow_dragonjag", Dragonjag.Variant.YELLOW);
	public static final DeferredBlock<SmallDragonjagBlock> SMALL_PINK_DRAGONJAG = registerSmallDragonjagBlock("small_pink_dragonjag", Dragonjag.Variant.PINK);
	public static final DeferredBlock<SmallDragonjagBlock> SMALL_PURPLE_DRAGONJAG = registerSmallDragonjagBlock("small_purple_dragonjag", Dragonjag.Variant.PURPLE);
	public static final DeferredBlock<SmallDragonjagBlock> SMALL_BLACK_DRAGONJAG = registerSmallDragonjagBlock("small_black_dragonjag", Dragonjag.Variant.BLACK);
	
	public static DeferredBlock<TallDragonjagBlock> registerTallDragonjagBlock(String name, Dragonjag.Variant variant) {
		return register(block(name, () -> new TallDragonjagBlock(settings(variant.getMapColor(), SoundType.GRASS, 1.0F), variant)).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(DoublePlantBlock.HALF, TallDragonjagBlock.DEAD).generate((half, dead) -> {
			String suffix = (half == DoubleBlockHalf.UPPER ? "_top" : "_bottom") + (dead ? "_dead" : "");
			return createModelVariant((half == DoubleBlockHalf.UPPER ? SpectrumTexturedModelProviders.cross(b -> b, suffix) : SpectrumTexturedModelProviders.doubleCross(b -> b, suffix)).createWithSuffix(block, suffix, ctx.modelOutput));
		}))));
	}
	
	public static final DeferredBlock<TallDragonjagBlock> TALL_YELLOW_DRAGONJAG = registerTallDragonjagBlock("tall_yellow_dragonjag", Dragonjag.Variant.YELLOW);
	public static final DeferredBlock<TallDragonjagBlock> TALL_RED_DRAGONJAG = registerTallDragonjagBlock("tall_red_dragonjag", Dragonjag.Variant.RED);
	public static final DeferredBlock<TallDragonjagBlock> TALL_PINK_DRAGONJAG = registerTallDragonjagBlock("tall_pink_dragonjag", Dragonjag.Variant.PINK);
	public static final DeferredBlock<TallDragonjagBlock> TALL_PURPLE_DRAGONJAG = registerTallDragonjagBlock("tall_purple_dragonjag", Dragonjag.Variant.PURPLE);
	public static final DeferredBlock<TallDragonjagBlock> TALL_BLACK_DRAGONJAG = registerTallDragonjagBlock("tall_black_dragonjag", Dragonjag.Variant.BLACK);
	
	//Flora
	public static final DeferredBlock<AloeBlock> ALOE = register(block("aloe", () -> new AloeBlock(settings(MapColor.PLANT, SoundType.GRASS, 1.0F).noCollission().randomTicks().noOcclusion())).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_4).generate(age -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, age.toString()).createWithSuffix(block, age.toString(), ctx.modelOutput))))));
	public static final DeferredBlock<SawbladeHollyBushBlock> SAWBLADE_HOLLY_BUSH = register(block("sawblade_holly_bush", () -> new SawbladeHollyBushBlock(settings(MapColor.TERRACOTTA_GREEN, SoundType.GRASS, 0.0F).noCollission().randomTicks().noOcclusion().lightLevel(s -> s.getValue(SawbladeHollyBushBlock.AGE) == SawbladeHollyBushBlock.MAX_AGE ? 10 : 0))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_7)
			.select(0, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "0").createWithSuffix(block, "_stage0", ctx.modelOutput)))
			.select(1, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "1").createWithSuffix(block, "_stage1", ctx.modelOutput)))
			.select(2, SpectrumModelHelper.createModelVariant(block, "_stage1"))
			.select(3, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "2").createWithSuffix(block, "_stage2", ctx.modelOutput)))
			.select(4, SpectrumModelHelper.createModelVariant(block, "_stage2")).select(5, SpectrumModelHelper.createModelVariant(block, "_stage2"))
			.select(6, SpectrumModelHelper.createModelVariant(block, "_stage2"))
			.select(7, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "3").createWithSuffix(block, "_stage3", ctx.modelOutput))))));
	public static final DeferredBlock<BristleSproutsBlock> BRISTLE_SPROUTS = register(blockWithItem("bristle_sprouts", () -> new BristleSproutsBlock(settings(MapColor.GRASS, SoundType.GRASS, 0.0F).noCollission().noOcclusion().offsetType(BlockBehaviour.OffsetType.XZ).replaceable()), InkColors.LIME).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_1")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_1").createWithSuffix(block, "_1", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_2").createWithSuffix(block, "_2", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_3").createWithSuffix(block, "_3", ctx.modelOutput)), SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, "_4").createWithSuffix(block, "_4", ctx.modelOutput)))));
	public static final DeferredBlock<DoomBloomBlock> DOOMBLOOM = register(block("doombloom", () -> new DoomBloomBlock(SpectrumMobEffects.STIFFNESS, 8, settings(MapColor.GRASS, SoundType.GRASS, 0.0F).randomTicks().noCollission().lightLevel((state) -> state.getValue(DoomBloomBlock.AGE) * 2).noOcclusion())).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_4).generate(age -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, age.toString()).createWithSuffix(block, age.toString(), ctx.modelOutput))))));
	public static final DeferredBlock<SnappingIvyBlock> SNAPPING_IVY = register(blockWithItem("snapping_ivy", () -> new SnappingIvyBlock(settings(MapColor.GRASS, SoundType.GRASS, 3.0F).noCollission().noOcclusion()), InkColors.RED).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(SnappingIvyBlock.SNAPPED, ModelLocationUtils.getModelLocation(block, "_snapped"), ModelLocationUtils.getModelLocation(block))).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS).select(Direction.Axis.X, Variant.variant()).select(Direction.Axis.Z, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))));
	
	public static final DeferredBlock<AbyssalVineBlock> ABYSSAL_VINES = register(block("abyssal_vines", () -> new AbyssalVineBlock(settings(MapColor.PLANT, SoundType.CAVE_VINES, 2.0F).noCollission().offsetType(BlockBehaviour.OffsetType.XYZ).randomTicks().noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.BERRIES) ? 13 : 0)))
			.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(TriStateVineBlock.LIFE_STAGE, AbyssalVineBlock.BERRIES).generate((stage, berries) -> {
				String suffix = (stage == TriStateVineBlock.LifeStage.STALK ? "" : "_tip") + (berries ? "_fruiting" : "");
				if (stage == TriStateVineBlock.LifeStage.MATURE) return SpectrumModelHelper.createModelVariant(block, suffix);
				return SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, suffix).createWithSuffix(block, suffix, ctx.modelOutput));
			}))));
	public static final DeferredBlock<NightdewBlock> NIGHTDEW = register(block("nightdew", () -> new NightdewBlock(settings(MapColor.WARPED_NYLIUM, SoundType.CAVE_VINES, 0.0F).noCollission().offsetType(BlockBehaviour.OffsetType.XYZ).randomTicks().noOcclusion().instabreak())).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(TriStateVineBlock.LIFE_STAGE).generate(stage -> {
		String suffix = (stage == TriStateVineBlock.LifeStage.STALK ? "" : "_tip");
		if (stage == TriStateVineBlock.LifeStage.MATURE) return createModelVariant(block, suffix);
		return createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, suffix).createWithSuffix(block, suffix, ctx.modelOutput));
	}))));
	public static final DeferredBlock<Block> SWEET_PEA = register(simplePlant(blockWithItem("sweet_pea", () -> new FlammableFlowerBlock(MobEffects.NIGHT_VISION, 5, settings(MapColor.COLOR_MAGENTA, SoundType.GRASS, 0.0F).offsetType(BlockBehaviour.OffsetType.XZ).noCollission().noOcclusion().lightLevel(s -> 11).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> APRICOTTI = register(simplePlant(blockWithItem("apricotti", () -> new FlammableFlowerBlock(MobEffects.GLOWING, 5, settings(MapColor.COLOR_ORANGE, SoundType.GRASS, 0.0F).offsetType(BlockBehaviour.OffsetType.XZ).noCollission().noOcclusion().lightLevel(s -> 11).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> HUMMING_BELL = register(simplePlant(blockWithItem("humming_bell", () -> new FlammableFlowerBlock(SpectrumMobEffects.LIGHTWEIGHT, 5, settings(MapColor.COLOR_LIGHT_BLUE, SoundType.GRASS, 0.0F).offsetType(BlockBehaviour.OffsetType.XZ).noCollission().noOcclusion().lightLevel(s -> 9).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.LIME)));
	
	public static final DeferredBlock<Block> HUMMINGSTONE_GLASS = register(simple(blockWithItem("hummingstone_glass", () -> new TransparentBlock(settings(MapColor.SAND, SoundType.GLASS, 5.0F, 100.0F).noOcclusion().requiresCorrectToolForDrops()), InkColors.LIGHT_BLUE)));
	public static final DeferredBlock<Block> HUMMINGSTONE_GLASS_PANE = register(blockWithItem("hummingstone_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(HUMMINGSTONE_GLASS.get())), InkColors.LIGHT_BLUE));
	public static final DeferredBlock<HummingstoneBlock> HUMMINGSTONE = register(blockWithItem("hummingstone", () -> new HummingstoneBlock(BlockBehaviour.Properties.ofFullCopy(HUMMINGSTONE_GLASS.get()).lightLevel((state) -> 14)), InkColors.LIGHT_BLUE).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(HummingstoneBlock.HUMMING, SpectrumTexturedModelProviders.cubeAll(b -> b, "_humming").createWithSuffix(block, "_humming", ctx.modelOutput), TexturedModel.CUBE.create(block, ctx.modelOutput)))));
	public static final DeferredBlock<Block> WAXED_HUMMINGSTONE = register(parented(blockWithItem("waxed_hummingstone", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(HUMMINGSTONE.get())), InkColors.LIGHT_BLUE), b -> HUMMINGSTONE.get()));
	
	public static final DeferredBlock<MossBallBlock> MOSS_BALL = register(blockWithItem("moss_ball", () -> new MossBallBlock(settings(MapColor.PLANT, SoundType.WET_GRASS, 1F).noCollission().noOcclusion().offsetType(BlockBehaviour.OffsetType.XYZ)), InkColors.GREEN).withBlockModel((ctx, block) -> {
		List<Variant> variants = new ArrayList<>(SpectrumModelHelper.createHorizontalRotationVariantList(ModelLocationUtils.getModelLocation(block, "_tuft")));
		variants.add(createModelVariant(block, "").with(VariantProperties.WEIGHT, 4));
		return MultiVariantGenerator.multiVariant(block, variants.toArray(Variant[]::new));
	}));
	public static final DeferredBlock<GiantMossBallBlock> GIANT_MOSS_BALL = register(blockWithItem("giant_moss_ball", () -> new GiantMossBallBlock(settings(MapColor.PLANT, SoundType.WET_GRASS, 10F).noCollission().noOcclusion().offsetType(BlockBehaviour.OffsetType.XYZ)), InkColors.GREEN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelLocationUtils.getModelLocation(block))));
	
	public static final DeferredBlock<Block> RESPLENDENT_BLOCK = register(defaultUpFacing(blockWithItem("resplendent_block", () -> new CushionedFacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)), () -> IS.of(Rarity.UNCOMMON), InkColors.YELLOW), TexturedModel.CUBE_TOP_BOTTOM));
	public static final DeferredBlock<Block> RESPLENDENT_CUSHION = register(singleton(blockWithItem("resplendent_cushion", () -> new CushionBlock(BlockBehaviour.Properties.ofFullCopy(RESPLENDENT_BLOCK.get()).noOcclusion().isValidSpawn(SpectrumBlocks::never)), () -> IS.of(Rarity.UNCOMMON), InkColors.YELLOW), SpectrumTexturedModelProviders.CUSHION));
	public static final DeferredBlock<Block> RESPLENDENT_CARPET = register(singleton(blockWithItem("resplendent_carpet", () -> new CushionedCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CARPET)), () -> IS.of(Rarity.UNCOMMON), InkColors.YELLOW), TexturedModel.CARPET));
	public static final DeferredBlock<SpectrumBedBlock> RESPLENDENT_BED = register(blockWithItem("resplendent_bed", () -> new SpectrumBedBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_BED)), () -> IS.of(1, Rarity.UNCOMMON), InkColors.YELLOW).withPredefinedItemModel().withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createSouthDefaultHorizontalFacingVariantMap()).with(PropertyDispatch.property(BedBlock.PART).select(BedPart.HEAD, SpectrumModelHelper.createModelVariant(block, "_head")).select(BedPart.FOOT, SpectrumModelHelper.createModelVariant(block, "_foot")))));
	
	// JADE VINES
	public static BlockBehaviour.Properties jadeVine() {
		return settings(MapColor.GRASS, SoundType.WOOL, 0.1F).noCollission().noOcclusion();
	}
	
	public static final DeferredBlock<JadeVineRootsBlock> JADE_VINE_ROOTS = register(block("jade_vine_roots", () -> new JadeVineRootsBlock(jadeVine().randomTicks().lightLevel((state) -> state.getValue(JadeVineRootsBlock.DEAD) ? 0 : 4))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(JadeVineBulbBlock.DEAD, SpectrumModelTemplates.JADE_VINE_ROOTS.createWithSuffix(block, "_dead", SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelOutput), SpectrumModelTemplates.JADE_VINE_ROOTS.create(block, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelOutput)))));
	public static final DeferredBlock<JadeVineBulbBlock> JADE_VINE_BULB = register(block("jade_vine_bulb", () -> new JadeVineBulbBlock(jadeVine().lightLevel((state) -> state.getValue(JadeVineBulbBlock.DEAD) ? 0 : 5))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(JadeVineBulbBlock.DEAD, SpectrumModelTemplates.JADE_VINE_BULB.createWithSuffix(block, "_dead", SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE_BULB, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelOutput), SpectrumModelTemplates.JADE_VINE_BULB.create(block, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_BULB, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelOutput)))));
	public static final DeferredBlock<JadeVinePlantBlock> JADE_VINES = register(block("jade_vines", () -> new JadeVinePlantBlock(jadeVine().lightLevel((state) -> state.getValue(JadeVinePlantBlock.AGE) == 0 ? 0 : 5))).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.AGE_7, JadeVinePlantBlock.PART).generate((age, part) -> {
		ModelTemplate model = SpectrumModelTemplates.jadeVines(part);
		String suffix = "_" + part.getSerializedName() + (age == 0 ? "_dead" : age <= 2 ? "_leaves" : age <= 6 ? "_petals" : "_bloom");
		if (age == 0) return createModelVariant(model.createWithSuffix(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_RIPE, SpectrumTextures.JADE_VINE_PLANT_RIPE_BREAKING), ctx.modelOutput));
		if (age == 1) return createModelVariant(model.createWithSuffix(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelOutput));
		if (age == 3) return createModelVariant(model.createWithSuffix(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_PETALS, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelOutput));
		if (age == 7) return createModelVariant(model.createWithSuffix(block, suffix, SpectrumTextureMaps.flowerParticle(SpectrumTextures.JADE_VINE_PLANT_BLOOMING, SpectrumTextures.JADE_VINE_PLANT_BREAKING), ctx.modelOutput));
		return createModelVariant(block, suffix);
	}))));
	public static final DeferredBlock<Block> JADE_VINE_PETAL_BLOCK = register(simple(blockWithItem("jade_vine_petal_block", () -> new JadeVinePetalBlock(jadeVine().lightLevel(state -> 3)), InkColors.LIME)));
	public static final DeferredBlock<Block> JADE_VINE_PETAL_CARPET = register(singleton(blockWithItem("jade_vine_petal_carpet", () -> new FlammableCarpetBlock(jadeVine().lightLevel(state -> 3)), InkColors.LIME), SpectrumTexturedModelProviders.carpet(b -> JADE_VINE_PETAL_BLOCK.get(), "")));
	
	public static final DeferredBlock<NephriteBlossomStemBlock> NEPHRITE_BLOSSOM_STEM = register(blockWithItem("nephrite_blossom_stem", () -> new NephriteBlossomStemBlock(settings(MapColor.COLOR_PINK, SoundType.WOOL, 2.0F).noOcclusion().noCollission()), InkColors.PINK).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_bottom")).withBlockModel((ctx, block) -> {
		ResourceLocation bottom = SpectrumTexturedModelProviders.cross(b -> b, "_bottom").createWithSuffix(block, "_bottom", ctx.modelOutput);
		ResourceLocation top = SpectrumTexturedModelProviders.cross(b -> b, "_top").createWithSuffix(block, "_top", ctx.modelOutput);
		ResourceLocation fronds = ModelLocationUtils.getModelLocation(block, "_base");
		return MultiPartGenerator.multiPart(block)
				.with(Condition.condition().term(NephriteBlossomStemBlock.STEM_PART, StemComponent.STEM), createModelVariant(bottom))
				.with(Condition.condition().term(NephriteBlossomStemBlock.STEM_PART, StemComponent.STEMALT), createModelVariant(top))
				.with(Condition.condition().term(NephriteBlossomStemBlock.STEM_PART, StemComponent.BASE), createModelVariant(fronds))
				.with(Condition.condition().term(NephriteBlossomStemBlock.STEM_PART, StemComponent.BASE), createModelVariant(bottom));
	}));
	public static final DeferredBlock<NephriteBlossomLeavesBlock> NEPHRITE_BLOSSOM_LEAVES = register(blockWithItem("nephrite_blossom_leaves", () -> new NephriteBlossomLeavesBlock(settings(MapColor.COLOR_PINK, SoundType.GRASS, 0.2F).noOcclusion().randomTicks().lightLevel(state -> 13)), InkColors.PINK).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_2).generate(age -> {
		String suffix = age == 0 ? "" : age == 1 ? "_flowering" : "_fruiting";
		return createModelVariant(SpectrumTexturedModelProviders.leaves(b -> b, suffix).createWithSuffix(block, suffix, ctx.modelOutput));
	}))));
	public static final DeferredBlock<NephriteBlossomBulbBlock> NEPHRITE_BLOSSOM_BULB = register(cross(blockWithItem("nephrite_blossom_bulb", () -> new NephriteBlossomBulbBlock(BlockBehaviour.Properties.ofFullCopy(NEPHRITE_BLOSSOM_STEM.get())), InkColors.PINK)).withItemModel(SpectrumModelHelper::registerItemModel));
	
	public static BlockBehaviour.Properties jadeite() {
		return settings(MapColor.WOOL, SoundType.WOOL, 0.1F).noCollission().noOcclusion().lightLevel(state -> 12).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always);
	}
	
	public static final DeferredBlock<JadeiteLotusStemBlock> JADEITE_LOTUS_STEM = register(blockWithItem("jadeite_lotus_stem", () -> new JadeiteLotusStemBlock(settings(MapColor.COLOR_BLACK, SoundType.WOOL, 2.0F).noOcclusion().noCollission()), InkColors.LIME).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "_top")).withBlockModel((ctx, block) -> {
		ResourceLocation bottom = SpectrumTexturedModelProviders.cross(b -> b, "_bottom").createWithSuffix(block, "_bottom", ctx.modelOutput);
		ResourceLocation top = SpectrumTexturedModelProviders.cross(b -> b, "_top").createWithSuffix(block, "_top", ctx.modelOutput);
		ResourceLocation base = ModelLocationUtils.getModelLocation(block, "_base");
		return MultiPartGenerator.multiPart(block).with(Condition.condition().term(JadeiteLotusStemBlock.STEM_PART, StemComponent.STEM), createModelVariant(bottom)).with(Condition.condition().term(JadeiteLotusStemBlock.STEM_PART, StemComponent.STEMALT), createModelVariant(top)).with(Condition.condition().term(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE), createModelVariant(bottom)).with(Condition.condition().term(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE).term(BlockStateProperties.INVERTED, false), createModelVariant(base)).with(Condition.condition().term(JadeiteLotusStemBlock.STEM_PART, StemComponent.BASE).term(BlockStateProperties.INVERTED, true), createModelVariant(base).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
	}));
	public static final DeferredBlock<Block> JADEITE_LOTUS_FLOWER = register(defaultUpFacingGetter(blockWithItem("jadeite_lotus_flower", () -> new JadeiteLotusFlowerBlock(settings(MapColor.SNOW, SoundType.WOOL, 2.0F).lightLevel(state -> 14).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always)), InkColors.LIME), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<JadeiteLotusBulbBlock> JADEITE_LOTUS_BULB = register(cross(blockWithItem("jadeite_lotus_bulb", () -> new JadeiteLotusBulbBlock(BlockBehaviour.Properties.ofFullCopy(JADEITE_LOTUS_STEM.get()).noOcclusion()), InkColors.LIME)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final DeferredBlock<Block> JADEITE_PETAL_BLOCK = register(simple(blockWithItem("jadeite_petal_block", () -> new JadeVinePetalBlock(jadeite()), InkColors.LIME)));
	public static final DeferredBlock<Block> JADEITE_PETAL_CARPET = register(singleton(blockWithItem("jadeite_petal_carpet", () -> new FlammableCarpetBlock(jadeite()), InkColors.LIME), SpectrumTexturedModelProviders.carpet(b -> JADEITE_PETAL_BLOCK.get(), "")));
	
	private static BlockBehaviour.Properties ore() {
		return BlockBehaviour.Properties.ofFullCopy(IRON_ORE);
	}
	
	private static BlockBehaviour.Properties deepslateOre() {
		return BlockBehaviour.Properties.ofFullCopy(DEEPSLATE_IRON_ORE);
	}
	
	private static BlockBehaviour.Properties blackslagOre() {
		return BlockBehaviour.Properties.ofFullCopy(BLACKSLAG.get()).strength(BLACKSLAG_HARDNESS * 1.5F, BLACKSLAG_RESISTANCE * 2F).requiresCorrectToolForDrops();
	}
	
	private static BlockBehaviour.Properties netherrackOre() {
		return BlockBehaviour.Properties.ofFullCopy(NETHERRACK).strength(3.0F, 3.0F).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops();
	}
	
	private static BlockBehaviour.Properties endstoneOre() {
		return BlockBehaviour.Properties.ofFullCopy(END_STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
	}
	
	public static final DeferredBlock<ShimmerstoneOreBlock> SHIMMERSTONE_ORE = register(simple(blockWithItem("shimmerstone_ore", () -> new ShimmerstoneOreBlock(UniformInt.of(2, 4), ore().randomTicks(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, STONE.defaultBlockState()), InkColors.YELLOW)));
	public static final DeferredBlock<ShimmerstoneOreBlock> DEEPSLATE_SHIMMERSTONE_ORE = register(simple(blockWithItem("deepslate_shimmerstone_ore", () -> new ShimmerstoneOreBlock(UniformInt.of(2, 4), deepslateOre().randomTicks(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, DEEPSLATE.defaultBlockState()), InkColors.YELLOW)));
	public static final DeferredBlock<ShimmerstoneOreBlock> BLACKSLAG_SHIMMERSTONE_ORE = register(singleton(blockWithItem("blackslag_shimmerstone_ore", () -> new ShimmerstoneOreBlock(UniformInt.of(2, 4), blackslagOre().randomTicks(), SpectrumAdvancements.REVEAL_SHIMMERSTONE, BLACKSLAG.get().defaultBlockState()), InkColors.YELLOW), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<ShimmerstoneBlock> SHIMMERSTONE_BLOCK = register(simple(blockWithItem("shimmerstone_block", () -> new ShimmerstoneBlock(settings(MapColor.COLOR_YELLOW, SoundType.GLASS, 2.0F).lightLevel((state) -> 15)), InkColors.YELLOW)));
	
	public static final DeferredBlock<AzuriteOreBlock> AZURITE_ORE = register(simpleMirrored(blockWithItem("azurite_ore", () -> new AzuriteOreBlock(UniformInt.of(4, 7), ore().randomTicks(), SpectrumAdvancements.REVEAL_AZURITE, Blocks.STONE.defaultBlockState()), InkColors.BLUE)));
	public static final DeferredBlock<Block> RAW_AZURITE_BLOCK = register(simple(blockWithItem("raw_azurite_block", () -> new AzuriteBlock(ore().mapColor(MapColor.COLOR_BLUE)), InkColors.BLUE)));
	public static final DeferredBlock<AzuriteOreBlock> DEEPSLATE_AZURITE_ORE = register(simpleMirrored(blockWithItem("deepslate_azurite_ore", () -> new AzuriteOreBlock(UniformInt.of(4, 7), deepslateOre().randomTicks(), SpectrumAdvancements.REVEAL_AZURITE, Blocks.DEEPSLATE.defaultBlockState()), InkColors.BLUE)));
	public static final DeferredBlock<AzuriteOreBlock> BLACKSLAG_AZURITE_ORE = register(simpleMirrored(blockWithItem("blackslag_azurite_ore", () -> new AzuriteOreBlock(UniformInt.of(4, 7), blackslagOre().randomTicks(), SpectrumAdvancements.REVEAL_AZURITE, BLACKSLAG.get().defaultBlockState()), InkColors.BLUE)));
	public static final DeferredBlock<Block> AZURITE_BLOCK = register(defaultUpFacing(blockWithItem("azurite_block", () -> new SpectrumFacingBlock(BlockBehaviour.Properties.ofFullCopy(LAPIS_BLOCK).mapColor(MapColor.COLOR_BLUE)), InkColors.BLUE), TexturedModel.CUBE_TOP_BOTTOM));
	public static final DeferredBlock<SpectrumClusterBlock> AZURITE_CLUSTER = register(cluster(blockWithItem("azurite_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLUE, SpectrumSoundTypes.SMALL_ONYX_BUD, 2), SpectrumClusterBlock.GrowthStage.CLUSTER), () -> IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_AZURITE_BUD = register(cluster(blockWithItem("large_azurite_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLUE, SpectrumSoundTypes.LARGE_ONYX_BUD, 3), SpectrumClusterBlock.GrowthStage.LARGE), () -> IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_AZURITE_BUD = register(cluster(blockWithItem("small_azurite_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_BLUE, SpectrumSoundTypes.ONYX_CLUSTER, 5), SpectrumClusterBlock.GrowthStage.SMALL), () -> IS.of(Rarity.UNCOMMON), InkColors.BLUE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static final DeferredBlock<Block> MALACHITE_ORE = register(simple(blockWithItem("malachite_ore", () -> new CloakedOreBlock(UniformInt.of(7, 11), ore(), SpectrumAdvancements.REVEAL_MALACHITE, STONE.defaultBlockState()), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN)));
	public static final DeferredBlock<Block> RAW_MALACHITE_BLOCK = register(simple(blockWithItem("raw_malachite_block", () -> new Block(ore().mapColor(MapColor.EMERALD)), InkColors.GREEN)));
	public static final DeferredBlock<Block> DEEPSLATE_MALACHITE_ORE = register(simple(blockWithItem("deepslate_malachite_ore", () -> new CloakedOreBlock(UniformInt.of(7, 11), deepslateOre(), SpectrumAdvancements.REVEAL_MALACHITE, Blocks.DEEPSLATE.defaultBlockState()), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN)));
	public static final DeferredBlock<Block> BLACKSLAG_MALACHITE_ORE = register(singleton(blockWithItem("blackslag_malachite_ore", () -> new CloakedOreBlock(UniformInt.of(7, 11), blackslagOre(), SpectrumAdvancements.REVEAL_MALACHITE, BLACKSLAG.get().defaultBlockState()), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> MALACHITE_BLOCK = register(defaultUpFacing(blockWithItem("malachite_block", () -> new SpectrumFacingBlock(gemstoneBlock(MapColor.EMERALD, SoundType.CHAIN)), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<SpectrumClusterBlock> MALACHITE_CLUSTER = register(cluster(blockWithItem("malachite_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.EMERALD, SoundType.CHAIN, 9), SpectrumClusterBlock.GrowthStage.CLUSTER), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_MALACHITE_BUD = register(cluster(blockWithItem("large_malachite_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.EMERALD, SoundType.CHAIN, 7), SpectrumClusterBlock.GrowthStage.LARGE), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_MALACHITE_BUD = register(cluster(blockWithItem("small_malachite_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.EMERALD, SoundType.CHAIN, 5), SpectrumClusterBlock.GrowthStage.SMALL), () -> IS.of(Rarity.UNCOMMON), InkColors.GREEN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static final DeferredBlock<Block> RAW_BLOODSTONE_BLOCK = register(simple(blockWithItem("raw_bloodstone_block", () -> new Block(ore().mapColor(MapColor.COLOR_RED).sound(SpectrumSoundTypes.ONYX_CLUSTER)), InkColors.RED)));
	public static final DeferredBlock<Block> BLOODSTONE_BLOCK = register(defaultUpFacing(blockWithItem("bloodstone_block", () -> new SpectrumFacingBlock(gemstoneBlock(MapColor.COLOR_RED, SpectrumSoundTypes.ONYX_CLUSTER)), () -> IS.of(Rarity.UNCOMMON), InkColors.RED), TexturedModel.COLUMN));
	public static final DeferredBlock<SpectrumClusterBlock> BLOODSTONE_CLUSTER = register(cluster(blockWithItem("bloodstone_cluster", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_RED, SpectrumSoundTypes.SMALL_ONYX_BUD, 6), SpectrumClusterBlock.GrowthStage.CLUSTER), () -> IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_BLOODSTONE_BUD = register(cluster(blockWithItem("large_bloodstone_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_RED, SpectrumSoundTypes.SMALL_ONYX_BUD, 4), SpectrumClusterBlock.GrowthStage.LARGE), () -> IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_BLOODSTONE_BUD = register(cluster(blockWithItem("small_bloodstone_bud", () -> new SpectrumClusterBlock(gemstone(MapColor.COLOR_RED, SpectrumSoundTypes.ONYX_CLUSTER, 3), SpectrumClusterBlock.GrowthStage.SMALL), () -> IS.of(Rarity.UNCOMMON), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static final DeferredBlock<CloakedOreBlock> STRATINE_ORE = register(simple(blockWithItem("stratine_ore", () -> new CloakedOreBlock(UniformInt.of(3, 5), netherrackOre(), SpectrumAdvancements.REVEAL_STRATINE, NETHERRACK.defaultBlockState()), () -> IS.of().fireResistant().component(SpectrumDataComponentTypes.GRAVITABLE, -0.01F), InkColors.RED)));
	public static final DeferredBlock<CloakedOreBlock> PALTAERIA_ORE = register(simple(blockWithItem("paltaeria_ore", () -> new CloakedOreBlock(UniformInt.of(2, 4), endstoneOre(), SpectrumAdvancements.REVEAL_PALTAERIA, END_STONE.defaultBlockState()), () -> IS.of().component(SpectrumDataComponentTypes.GRAVITABLE, 0.01F), InkColors.CYAN)));
	
	private static BlockBehaviour.Properties gravityBlock(MapColor mapColor) {
		return settings(mapColor, SoundType.METAL, 4.0F, 6.0F).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops();
	}
	
	public static final DeferredBlock<FloatBlock> STRATINE_FLOATBLOCK = register(singleton(blockWithItem("stratine_floatblock", () -> new FloatBlock(gravityBlock(MapColor.NETHER), -0.2F), () -> IS.of().fireResistant().component(SpectrumDataComponentTypes.GRAVITABLE, -0.02F), InkColors.CYAN), SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "", b -> b, "_top", b -> b, "_bottom")));
	public static final DeferredBlock<FloatBlock> PALTAERIA_FLOATBLOCK = register(singleton(blockWithItem("paltaeria_floatblock", () -> new FloatBlock(gravityBlock(MapColor.COLOR_LIGHT_BLUE), 0.2F), () -> IS.of().component(SpectrumDataComponentTypes.GRAVITABLE, 0.02F), InkColors.RED), SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "", b -> b, "_top", b -> b, "_bottom")));
	public static final DeferredBlock<FloatBlock> HOVER_BLOCK = register(singleton(blockWithItem("hover_block", () -> new FloatBlock(gravityBlock(MapColor.DIAMOND), 0.0F), () -> IS.of().component(SpectrumDataComponentTypes.GRAVITABLE, 0.0F), InkColors.GREEN), TexturedModel.COLUMN));
	
	public static final DeferredBlock<Block> BLACKSLAG_COAL_ORE = register(singleton(blockWithItem("blackslag_coal_ore", () -> new DropExperienceBlock(UniformInt.of(0, 2), blackslagOre()), InkColors.BLACK), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_COPPER_ORE = register(singleton(blockWithItem("blackslag_copper_ore", () -> new DropExperienceBlock(ConstantInt.of(0), blackslagOre()), InkColors.BLACK), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_IRON_ORE = register(singleton(blockWithItem("blackslag_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), blackslagOre()), InkColors.BROWN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_GOLD_ORE = register(singleton(blockWithItem("blackslag_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), blackslagOre()), InkColors.YELLOW), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_LAPIS_ORE = register(singleton(blockWithItem("blackslag_lapis_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), blackslagOre()), InkColors.BLUE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_DIAMOND_ORE = register(singleton(blockWithItem("blackslag_diamond_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), blackslagOre()), InkColors.LIGHT_BLUE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_REDSTONE_ORE = register(singleton(blockWithItem("blackslag_redstone_ore", () -> new RedStoneOreBlock(blackslagOre().randomTicks().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 9 : 0)), InkColors.RED), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_EMERALD_ORE = register(singleton(blockWithItem("blackslag_emerald_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), blackslagOre()), InkColors.LIME), TexturedModel.COLUMN_ALT));
	
	// FUNCTIONAL BLOCKS
	public static final DeferredBlock<HeartboundChestBlock> HEARTBOUND_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("heartbound_chest", () -> new HeartboundChestBlock(settings(MapColor.TERRACOTTA_WHITE, SoundType.STONE, -1.0F, 3600000.0F).requiresCorrectToolForDrops().noOcclusion()), InkColors.BLUE), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<CompactingChestBlock> COMPACTING_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("compacting_chest", () -> new CompactingChestBlock(settings(MapColor.TERRACOTTA_WHITE, SoundType.STONE, 4.0F, 4.0F).requiresCorrectToolForDrops().noOcclusion()), InkColors.YELLOW), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<FabricationChestBlock> FABRICATION_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("fabrication_chest", () -> new FabricationChestBlock(settings(MapColor.COLOR_ORANGE, SoundType.STONE, 4.0F, 4.0F).requiresCorrectToolForDrops().noOcclusion()), InkColors.YELLOW), ModelLocationUtils::getModelLocation).withPredefinedItemModel());
	public static final DeferredBlock<BlackHoleChestBlock> BLACK_HOLE_CHEST = register(defaultNorthHorizontalFacing(blockWithItem("black_hole_chest", () -> new BlackHoleChestBlock(settings(MapColor.COLOR_BLACK, SoundType.STONE, 4.0F, 4.0F).requiresCorrectToolForDrops().noOcclusion()), InkColors.LIGHT_GRAY), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<ParticleSpawnerBlock> PARTICLE_SPAWNER = register(blockWithItem("particle_spawner", () -> new ParticleSpawnerBlock(settings(MapColor.TERRACOTTA_WHITE, SoundType.AMETHYST, 5.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()), InkColors.PINK).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_off")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.POWERED, SpectrumModelTemplates.PARTICLE_SPAWNER.create(block, SpectrumTextureMaps.top(block, "_top"), ctx.modelOutput), SpectrumModelTemplates.PARTICLE_SPAWNER.createWithSuffix(block, "_off", SpectrumTextureMaps.top(block, "_top_off"), ctx.modelOutput)))));
	public static final DeferredBlock<CreativeParticleSpawnerBlock> CREATIVE_PARTICLE_SPAWNER = register(singletonWithSoup(blockWithItem("creative_particle_spawner", () -> new CreativeParticleSpawnerBlock(BlockBehaviour.Properties.ofFullCopy(PARTICLE_SPAWNER.get()).strength(-1.0F, 3600000.8F).noLootTable()), block1 -> new BlockItem(block1, IS.of(Rarity.EPIC)), InkColors.PINK), (Function<Block, ResourceLocation>) b -> ModelLocationUtils.getModelLocation(PARTICLE_SPAWNER.get())).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, PARTICLE_SPAWNER.get(), "_off")));
	public static final DeferredBlock<BedrockAnvilBlock> BEDROCK_ANVIL = register(defaultSouthHorizontalFacing(blockWithItem("bedrock_anvil", () -> new BedrockAnvilBlock(BlockBehaviour.Properties.ofFullCopy(ANVIL).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.METAL)), InkColors.BLACK), ModelLocationUtils::getModelLocation));
	
	// SOLID LIQUID CRYSTAL
	public static final DeferredBlock<Block> FROSTBITE_CRYSTAL = register(simple(blockWithItem("frostbite_crystal", () -> new CloakedBlock(BlockBehaviour.Properties.ofFullCopy(GLOWSTONE).mapColor(MapColor.CLAY), SpectrumAdvancements.REVEAL_FROSTBITE_RESOURCES, Blocks.BLUE_ICE.defaultBlockState()), InkColors.LIGHT_BLUE)));
	// TODO: rename to incandescent crystal (including advancements)
	public static final DeferredBlock<Block> BLAZING_CRYSTAL = register(simple(blockWithItem("blazing_crystal", () -> new CloakedBlock(BlockBehaviour.Properties.ofFullCopy(GLOWSTONE).mapColor(MapColor.COLOR_ORANGE), SpectrumAdvancements.REVEAL_INCANDESCENT_RESOURCES, MAGMA_BLOCK.defaultBlockState()), () -> IS.of().fireResistant(), InkColors.ORANGE)));
	
	public static final DeferredBlock<QuitoxicReedsBlock> QUITOXIC_REEDS = register(cross(blockWithItem("quitoxic_reeds", () -> new QuitoxicReedsBlock(settings(MapColor.NONE, SoundType.GRASS, 0.0F).noCollission().offsetType(BlockBehaviour.OffsetType.XYZ).randomTicks().lightLevel(state -> state.getValue(QuitoxicReedsBlock.LOGGED).getLuminance())), InkColors.PURPLE)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final DeferredBlock<MermaidsBrushBlock> MERMAIDS_BRUSH = register(block("mermaids_brush", () -> new MermaidsBrushBlock(settings(MapColor.NONE, SoundType.WET_GRASS, 0.0F).noCollission().randomTicks().lightLevel(state -> state.getValue(MermaidsBrushBlock.LOGGED).getLuminance()))).withBlockModel((ctx, block) -> {
		ResourceLocation none = SpectrumTexturedModelProviders.cross(b -> b, "_none").createWithSuffix(block, "_none", ctx.modelOutput);
		ResourceLocation some = SpectrumTexturedModelProviders.cross(b -> b, "_some").createWithSuffix(block, "_some", ctx.modelOutput);
		ResourceLocation full = SpectrumTexturedModelProviders.cross(b -> b, "_full").createWithSuffix(block, "_full", ctx.modelOutput);
		return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_7).generate(age -> createModelVariant(age < 3 ? none : age < 6 ? some : full)));
	}));
	public static final DeferredBlock<RadiatingEnderBlock> RADIATING_ENDER = register(blockWithItem("radiating_ender", () -> new RadiatingEnderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).mapColor(MapColor.COLOR_PURPLE)), InkColors.PURPLE).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(TexturedModel.CUBE_TOP_BOTTOM.create(block, ctx.modelOutput))).with(SpectrumModelHelper.createUpDefaultFacingVariantMap())));
	public static final DeferredBlock<AmaranthCropBlock> AMARANTH = register(block("amaranth", () -> new AmaranthCropBlock(settings(MapColor.NONE, SoundType.CROP, 0.0F).noCollission().randomTicks())).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.AGE_7, TallCropBlock.HALF).generate((age, half) -> {
		String suffix;
		if (half == DoubleBlockHalf.LOWER) {
			suffix = "_stage" + ((age + 1) / 2) + "_lower";
			if (age > 0 && age % 2 == 0) return createModelVariant(block, suffix);
		} else {
			suffix = "_stage" + Math.max(2, ((age + 1) / 2)) + "_upper";
			if (age < 4 || age == 6) return createModelVariant(block, suffix);
		}
		return createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, suffix).createWithSuffix(block, suffix, ctx.modelOutput));
	}))));
	
	public static final DeferredBlock<MemoryBlock> MEMORY = register(singletonWithSoup(blockWithItem("memory", () -> new MemoryBlock(settings(MapColor.NONE, SoundType.AMETHYST, 0.0F).isViewBlocking(SpectrumBlocks::never).noOcclusion().randomTicks()), block -> new MemoryItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.LIGHT_GRAY), ModelLocationUtils::getModelLocation).withItemModel((ctx, item) -> SpectrumModelHelper.registerLayeredItemModel(ctx, item, ModelTemplates.THREE_LAYERED_ITEM, "_base", "_overlay", "_brighten")));
	public static final DeferredBlock<CrackedEndPortalFrameBlock> CRACKED_END_PORTAL_FRAME = register(blockWithItem("cracked_end_portal_frame", () -> new CrackedEndPortalFrameBlock(settings(MapColor.ICE, SoundType.GLASS, -1.0F, 3600000.0F).instrument(NoteBlockInstrument.BASEDRUM).lightLevel((state) -> 1)), () -> IS.of().fireResistant().component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_FIRE, DamageTypeTags.IS_EXPLOSION)), InkColors.PURPLE).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_none")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(CrackedEndPortalFrameBlock.FACING_VERTICAL).select(false, Variant.variant()).select(true, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))).with(PropertyDispatch.property(CrackedEndPortalFrameBlock.EYE_TYPE).generate(type -> SpectrumModelHelper.createModelVariant(ModelLocationUtils.getModelLocation(block, "_" + type.getSerializedName()))))));
	public static final DeferredBlock<Block> LAVA_SPONGE = register(simple(blockWithItem("lava_sponge", () -> new LavaSpongeBlock(BlockBehaviour.Properties.ofFullCopy(SPONGE).mapColor(MapColor.COLOR_ORANGE)), () -> IS.of().fireResistant(), InkColors.ORANGE)));
	public static final DeferredBlock<Block> WET_LAVA_SPONGE = register(simple(blockWithItem("wet_lava_sponge", () -> new WetLavaSpongeBlock(BlockBehaviour.Properties.ofFullCopy(WET_SPONGE).mapColor(MapColor.COLOR_ORANGE).lightLevel(s -> 9).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), block -> new WetLavaSpongeItem(block, IS.of(1).fireResistant().craftRemainder(LAVA_SPONGE.asItem())), InkColors.ORANGE)));
	
	public static final DeferredBlock<Block> LIGHT_LEVEL_DETECTOR = register(detector(blockWithItem("light_level_detector", () -> new BlockLightDetectorBlock(BlockBehaviour.Properties.ofFullCopy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final DeferredBlock<Block> WEATHER_DETECTOR = register(detector(blockWithItem("weather_detector", () -> new WeatherDetectorBlock(BlockBehaviour.Properties.ofFullCopy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final DeferredBlock<Block> ITEM_DETECTOR = register(detector(blockWithItem("item_detector", () -> new ItemDetectorBlock(BlockBehaviour.Properties.ofFullCopy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final DeferredBlock<Block> PLAYER_DETECTOR = register(detector(blockWithItem("player_detector", () -> new PlayerDetectorBlock(BlockBehaviour.Properties.ofFullCopy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final DeferredBlock<Block> CREATURE_DETECTOR = register(detector(blockWithItem("creature_detector", () -> new EntityDetectorBlock(BlockBehaviour.Properties.ofFullCopy(DAYLIGHT_DETECTOR)), InkColors.RED)));
	public static final DeferredBlock<RedstoneTimerBlock> REDSTONE_TIMER = register(blockWithItem("redstone_timer", () -> new RedstoneTimerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER)), InkColors.RED).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultiPartGenerator multipart = MultiPartGenerator.multiPart(block);
		ResourceLocation on = SpectrumModelTemplates.REDSTONE_TIMER.create(block, new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH)), ctx.modelOutput);
		ResourceLocation off = SpectrumModelTemplates.REDSTONE_TIMER.createWithSuffix(block, "_off", new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH, "_off")), ctx.modelOutput);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			VariantProperties.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(BlockStateProperties.POWERED, true), createModelVariant(on).with(VariantProperties.Y_ROT, rotation));
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(BlockStateProperties.POWERED, false), createModelVariant(off).with(VariantProperties.Y_ROT, rotation));
			for (RedstoneTimerBlock.TimingStep step : RedstoneTimerBlock.TimingStep.values()) {
				multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTimerBlock.ACTIVE_TIME, step), createModelVariant(block, "_left_" + step.ordinal()).with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, rotation));
				multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTimerBlock.INACTIVE_TIME, step), createModelVariant(block, "_right_" + step.ordinal()).with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, rotation));
			}
		}
		return multipart;
	}));
	public static final DeferredBlock<RedstoneCalculatorBlock> REDSTONE_CALCULATOR = register(blockWithItem("redstone_calculator", () -> new RedstoneCalculatorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER)), InkColors.RED).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultiPartGenerator multipart = MultiPartGenerator.multiPart(block);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			VariantProperties.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(BlockStateProperties.POWERED, true), createModelVariant(block, "_base").with(VariantProperties.Y_ROT, rotation));
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(BlockStateProperties.POWERED, false), createModelVariant(block, "_base_off").with(VariantProperties.Y_ROT, rotation));
			for (RedstoneCalculatorBlock.CalculationMode mode : RedstoneCalculatorBlock.CalculationMode.values()) {
				multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneCalculatorBlock.CALCULATION_MODE, mode), createModelVariant(block, "_" + mode.getSerializedName()).with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, rotation));
			}
		}
		return multipart;
	}));
	public static final DeferredBlock<RedstoneTransceiverBlock> REDSTONE_TRANSCEIVER = register(blockWithItem("redstone_transceiver", () -> new RedstoneTransceiverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER)), InkColors.RED).withPredefinedItemModel().withBlockModel((ctx, block) -> {
		MultiPartGenerator multipart = MultiPartGenerator.multiPart(block);
		ResourceLocation senderOn = SpectrumModelTemplates.REDSTONE_TRANSCEIVER_SENDER.createWithSuffix(block, "_sender", new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH)), ctx.modelOutput);
		ResourceLocation senderOff = SpectrumModelTemplates.REDSTONE_TRANSCEIVER_SENDER.createWithSuffix(block, "_sender_off", new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH, "_off")), ctx.modelOutput);
		ResourceLocation receiverOn = SpectrumModelTemplates.REDSTONE_TRANSCEIVER_RECEIVER.createWithSuffix(block, "_receiver", new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH)), ctx.modelOutput);
		ResourceLocation receiverOff = SpectrumModelTemplates.REDSTONE_TRANSCEIVER_RECEIVER.createWithSuffix(block, "_receiver_off", new TextureMapping().put(SpectrumTextureSlots.LIGHT, TextureMapping.getBlockTexture(REDSTONE_TORCH, "_off")), ctx.modelOutput);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			VariantProperties.Rotation rotation = SpectrumModelHelper.getSouthDefaultRotation(direction);
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTransceiverBlock.SENDER, true).term(BlockStateProperties.POWERED, true), createModelVariant(senderOn).with(VariantProperties.Y_ROT, rotation));
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTransceiverBlock.SENDER, true).term(BlockStateProperties.POWERED, false), createModelVariant(senderOff).with(VariantProperties.Y_ROT, rotation));
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTransceiverBlock.SENDER, false).term(BlockStateProperties.POWERED, true), createModelVariant(receiverOn).with(VariantProperties.Y_ROT, rotation));
			multipart.with(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).term(RedstoneTransceiverBlock.SENDER, false).term(BlockStateProperties.POWERED, false), createModelVariant(receiverOff).with(VariantProperties.Y_ROT, rotation));
		}
		for (DyeColor color : DyeColor.values()) {
			ResourceLocation channel = SpectrumModelTemplates.REDSTONE_TRANSCEIVER_CHANNEL.createWithSuffix(block, "_channel_" + color.getSerializedName(), SpectrumTextureMaps.all(SpectrumCommon.locate("block/" + color.getSerializedName() + "_block")), ctx.modelOutput);
			multipart.with(Condition.condition().term(RedstoneTransceiverBlock.CHANNEL, color), SpectrumModelHelper.createModelVariant(channel));
		}
		return multipart;
	}));
	public static final DeferredBlock<BlockPlacerBlock> BLOCK_PLACER = register(blockWithItem("block_placer", () -> new BlockPlacerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, SpectrumTexturedModelProviders.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_CALCITE.get(), "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side")).with(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap())));
	public static final DeferredBlock<BlockDetectorBlock> BLOCK_DETECTOR = register(blockWithItem("block_detector", () -> new BlockDetectorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap()).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.TRIGGERED, SpectrumTexturedModelProviders.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_BASALT.get(), "_top", b -> b, "_front", b -> b, "_back_active", b -> b, "_side").createWithSuffix(block, "_active", ctx.modelOutput), SpectrumTexturedModelProviders.complexOrientable(b -> b, "_side", b -> b, "_top", b -> NOTCHED_POLISHED_BASALT.get(), "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side").create(block, ctx.modelOutput)))));
	public static final DeferredBlock<BlockBreakerBlock> BLOCK_BREAKER = register(blockWithItem("block_breaker", () -> new BlockBreakerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER)), InkColors.CYAN).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, SpectrumTexturedModelProviders.complexOrientable(b -> b, "_side", b -> b, "_top", b -> POLISHED_BONE_ASH_PILLAR.get(), "_top", b -> b, "_front", b -> b, "_back", b -> b, "_side")).with(SpectrumModelHelper.createUpNorthDefaultOrientationVariantMap())));
	public static final DeferredBlock<EnderDropperBlock> ENDER_DROPPER = register(orientable(blockWithItem("ender_dropper", () -> new EnderDropperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DROPPER).mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(15F, 60.0F)), InkColors.PURPLE)));
	public static final DeferredBlock<EnderHopperBlock> ENDER_HOPPER = register(singletonWithSoup(blockWithItem("ender_hopper", () -> new EnderHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(15F, 60.0F)), InkColors.PURPLE), ModelLocationUtils::getModelLocation).withItemModel(SpectrumModelHelper::registerItemModel));
	
	public static final DeferredBlock<Block> OMINOUS_SAPLING = register(simplePlant(blockWithItem("ominous_sapling", () -> new OminousSaplingBlock(BlockBehaviour.Properties.ofFullCopy(OAK_SAPLING)), block -> new OminousSaplingBlockItem(block, IS.of()), InkColors.GREEN)));
	
	public static final DeferredBlock<Block> SPIRIT_SALLOW_LEAVES = register(singleton(blockWithItem("spirit_sallow_leaves", () -> new SpiritSallowLeavesBlock(BlockBehaviour.Properties.ofFullCopy(OAK_LEAVES).mapColor(MapColor.QUARTZ).lightLevel((state) -> 8)), InkColors.GREEN), TexturedModel.LEAVES));
	public static final DeferredBlock<Block> SPIRIT_SALLOW_LOG = register(log(blockWithItem("spirit_sallow_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(OAK_WOOD).mapColor(MapColor.COLOR_GRAY)), InkColors.GREEN)));
	public static final DeferredBlock<RotatedPillarBlock> SPIRIT_SALLOW_ROOTS = register(blockWithItem("spirit_sallow_roots", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(OAK_WOOD).mapColor(MapColor.COLOR_GRAY)), InkColors.GREEN).withBlockModel((ctx, block) -> {
		TextureMapping textureMap = SpectrumTextureMaps.sideEnd(block, "", block, "");
		ResourceLocation vertical = ModelTemplates.CUBE_COLUMN.create(block, textureMap, ctx.modelOutput);
		ResourceLocation horizontal = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, textureMap, ctx.modelOutput);
		return BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, vertical, horizontal);
	}));
	public static final DeferredBlock<Block> SPIRIT_SALLOW_HEART = register(singleton(blockWithItem("spirit_sallow_heart", () -> new Block(BlockBehaviour.Properties.ofFullCopy(OAK_WOOD).mapColor(MapColor.COLOR_GRAY).lightLevel(s -> 11)), InkColors.GREEN), SpectrumTexturedModelProviders.cubeColumn(b -> b, "", b -> SPIRIT_SALLOW_LOG.get(), "_top")));
	
	public static final DeferredBlock<ExtraTickFarmlandBlock> SACRED_SOIL = register(blockWithItem("sacred_soil", () -> new ExtraTickFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND).mapColor(MapColor.CLAY), Blocks.DIRT.defaultBlockState()), InkColors.LIME).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.MOISTURE).generate(moisture -> SpectrumModelHelper.createModelVariant(block, moisture == 7 ? "_moist" : "")))));
	
	private static BlockBehaviour.Properties spiritVines(MapColor mapColor) {
		return settings(mapColor, SoundType.CAVE_VINES, 0.0F).noCollission();
	}
	
	public static final DeferredBlock<SpiritVinesPlantBlock> CYAN_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("cyan_spirit_sallow_vines_body", () -> new SpiritVinesPlantBlock(spiritVines(MapColor.COLOR_CYAN), BuiltinGemstoneColor.CYAN))));
	public static final DeferredBlock<SpiritVinesPlantBlock> MAGENTA_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("magenta_spirit_sallow_vines_body", () -> new SpiritVinesPlantBlock(spiritVines(MapColor.COLOR_MAGENTA), BuiltinGemstoneColor.MAGENTA))));
	public static final DeferredBlock<SpiritVinesPlantBlock> YELLOW_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("yellow_spirit_sallow_vines_body", () -> new SpiritVinesPlantBlock(spiritVines(MapColor.COLOR_YELLOW), BuiltinGemstoneColor.YELLOW))));
	public static final DeferredBlock<SpiritVinesPlantBlock> BLACK_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("black_spirit_sallow_vines_body", () -> new SpiritVinesPlantBlock(spiritVines(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK))));
	public static final DeferredBlock<SpiritVinesPlantBlock> WHITE_SPIRIT_SALLOW_VINES_PLANT = register(spiritVines(block("white_spirit_sallow_vines_body", () -> new SpiritVinesPlantBlock(spiritVines(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE))));
	
	public static final DeferredBlock<SpiritVinesPlantStemBlock> CYAN_SPIRIT_SALLOW_VINES = register(spiritVines(block("cyan_spirit_sallow_vines_head", () -> new SpiritVinesPlantStemBlock(spiritVines(MapColor.COLOR_CYAN), BuiltinGemstoneColor.CYAN))));
	public static final DeferredBlock<SpiritVinesPlantStemBlock> MAGENTA_SPIRIT_SALLOW_VINES = register(spiritVines(block("magenta_spirit_sallow_vines_head", () -> new SpiritVinesPlantStemBlock(spiritVines(MapColor.COLOR_MAGENTA), BuiltinGemstoneColor.MAGENTA))));
	public static final DeferredBlock<SpiritVinesPlantStemBlock> YELLOW_SPIRIT_SALLOW_VINES = register(spiritVines(block("yellow_spirit_sallow_vines_head", () -> new SpiritVinesPlantStemBlock(spiritVines(MapColor.COLOR_YELLOW), BuiltinGemstoneColor.YELLOW))));
	public static final DeferredBlock<SpiritVinesPlantStemBlock> BLACK_SPIRIT_SALLOW_VINES = register(spiritVines(block("black_spirit_sallow_vines_head", () -> new SpiritVinesPlantStemBlock(spiritVines(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK))));
	public static final DeferredBlock<SpiritVinesPlantStemBlock> WHITE_SPIRIT_SALLOW_VINES = register(spiritVines(block("white_spirit_sallow_vines_head", () -> new SpiritVinesPlantStemBlock(spiritVines(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE))));
	
	public static final DeferredBlock<Block> STUCK_STORM_STONE = register(defaultWestHorizontalFacing(block("stuck_storm_stone", () -> new StuckStormStoneBlock(settings(MapColor.NONE, SoundType.SMALL_AMETHYST_BUD, 0.0F).noCollission().noOcclusion().isSuffocating(SpectrumBlocks::never).noTerrainParticles().isViewBlocking(SpectrumBlocks::never).replaceable())), ModelLocationUtils::getModelLocation));
	public static final DeferredBlock<DeeperDownPortalBlock> DEEPER_DOWN_PORTAL = register(block("deeper_down_portal", () -> new DeeperDownPortalBlock(settings(MapColor.COLOR_BLACK, SoundType.EMPTY, -1.0F, 3600000.0F).isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK).lightLevel(state -> 8).noLootTable())).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(DeeperDownPortalBlock.FACING_UP, ModelLocationUtils.getModelLocation(block, "_up"), ModelLocationUtils.getModelLocation(block)))));
	
	private static BlockBehaviour.Properties upgrade() {
		return BlockBehaviour.Properties.ofFullCopy(POLISHED_BASALT.get()).forceSolidOn();
	}
	
	public static final DeferredBlock<Block> UPGRADE_SPEED = register(parented(blockWithItem("upgrade_speed", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 1, InkColors.MAGENTA_COLOR, "upgrade_speed"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> b));
	public static final DeferredBlock<Block> UPGRADE_SPEED2 = register(parented(blockWithItem("upgrade_speed2", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 2, InkColors.MAGENTA_COLOR, "upgrade_speed2"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> UPGRADE_SPEED.get()));
	public static final DeferredBlock<Block> UPGRADE_SPEED3 = register(parented(blockWithItem("upgrade_speed3", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.SPEED, 8, InkColors.MAGENTA_COLOR, "upgrade_speed3"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> UPGRADE_SPEED.get()));
	public static final DeferredBlock<Block> UPGRADE_EFFICIENCY = register(parented(blockWithItem("upgrade_efficiency", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EFFICIENCY, 1, InkColors.YELLOW_COLOR, "upgrade_efficiency"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> b));
	public static final DeferredBlock<Block> UPGRADE_EFFICIENCY2 = register(parented(blockWithItem("upgrade_efficiency2", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EFFICIENCY, 4, InkColors.YELLOW_COLOR, "upgrade_efficiency2"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> UPGRADE_EFFICIENCY.get()));
	public static final DeferredBlock<Block> UPGRADE_YIELD = register(parented(blockWithItem("upgrade_yield", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.YIELD, 1, InkColors.CYAN_COLOR, "upgrade_yield"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> b));
	public static final DeferredBlock<Block> UPGRADE_YIELD2 = register(parented(blockWithItem("upgrade_yield2", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.YIELD, 4, InkColors.CYAN_COLOR, "upgrade_yield2"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> UPGRADE_YIELD.get()));
	public static final DeferredBlock<Block> UPGRADE_EXPERIENCE = register(parented(blockWithItem("upgrade_experience", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EXPERIENCE, 1, InkColors.PURPLE_COLOR, "upgrade_experience"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> b));
	public static final DeferredBlock<Block> UPGRADE_EXPERIENCE2 = register(parented(blockWithItem("upgrade_experience2", () -> new UpgradeBlock(upgrade(), Upgradeable.UpgradeType.EXPERIENCE, 4, InkColors.PURPLE_COLOR, "upgrade_experience2"), () -> IS.of(16, Rarity.UNCOMMON), InkColors.LIGHT_GRAY), b -> UPGRADE_EXPERIENCE.get()));
	
	public static final DeferredBlock<RedstoneSandBlock> REDSTONE_SAND = register(simple(blockWithItem("redstone_sand", () -> new RedstoneSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).mapColor(MapColor.FIRE)), InkColors.RED)));
	public static final DeferredBlock<EnderGlassBlock> ENDER_GLASS = register(blockWithItem("ender_glass", () -> new EnderGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_PURPLE).noOcclusion().isRedstoneConductor(SpectrumBlocks::never).isValidSpawn((state, world, pos, entityType) -> EnderGlassBlock.getTransparencyState(state) == EnderGlassBlock.TransparencyState.SOLID).isSuffocating((state, world, pos) -> EnderGlassBlock.getTransparencyState(state) == EnderGlassBlock.TransparencyState.SOLID).isViewBlocking((state, world, pos) -> EnderGlassBlock.getTransparencyState(state) == EnderGlassBlock.TransparencyState.SOLID)), InkColors.PURPLE).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_solid")).withBlockModel((ctx, block) ->
			MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(EnderGlassBlock.TRANSPARENCY_STATE)
					.generate(transparency -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_" + transparency.getSerializedName()).createWithSuffix(block, "_" + transparency.getSerializedName(), ctx.modelOutput))))));
	public static final DeferredBlock<CloverBlock> CLOVER = register(singletonWithSoup(blockWithItem("clover", () -> new CloverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)), InkColors.LIME), ModelLocationUtils::getModelLocation).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final DeferredBlock<FourLeafCloverBlock> FOUR_LEAF_CLOVER = register(singletonWithSoup(blockWithItem("four_leaf_clover", () -> new FourLeafCloverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)), block -> new FourLeafCloverItem(block, IS.of(), SpectrumAdvancements.REVEAL_FOUR_LEAF_CLOVER, CLOVER.asItem()), InkColors.LIME), ModelLocationUtils::getModelLocation).withItemModel(SpectrumModelHelper::registerItemModel));
	
	private static final UniformInt gemOreExperienceProvider = UniformInt.of(1, 4);
	public static final DeferredBlock<Block> TOPAZ_ORE = register(simple(blockWithItem("topaz_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, STONE.defaultBlockState()), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_ORE = register(simple(blockWithItem("amethyst_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, STONE.defaultBlockState()), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_ORE = register(simple(blockWithItem("citrine_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, STONE.defaultBlockState()), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_ORE = register(simple(blockWithItem("onyx_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, STONE.defaultBlockState()), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_ORE = register(simple(blockWithItem("moonstone_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, ore(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, STONE.defaultBlockState()), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> DEEPSLATE_TOPAZ_ORE = register(simple(blockWithItem("deepslate_topaz_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, DEEPSLATE.defaultBlockState()), InkColors.CYAN)));
	public static final DeferredBlock<Block> DEEPSLATE_AMETHYST_ORE = register(simple(blockWithItem("deepslate_amethyst_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, DEEPSLATE.defaultBlockState()), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> DEEPSLATE_CITRINE_ORE = register(simple(blockWithItem("deepslate_citrine_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, DEEPSLATE.defaultBlockState()), InkColors.YELLOW)));
	public static final DeferredBlock<Block> DEEPSLATE_ONYX_ORE = register(simple(blockWithItem("deepslate_onyx_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, DEEPSLATE.defaultBlockState()), InkColors.BLACK)));
	public static final DeferredBlock<Block> DEEPSLATE_MOONSTONE_ORE = register(simple(blockWithItem("deepslate_moonstone_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, deepslateOre(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, DEEPSLATE.defaultBlockState()), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> BLACKSLAG_TOPAZ_ORE = register(singleton(blockWithItem("blackslag_topaz_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.CYAN, SpectrumAdvancements.COLLECT_TOPAZ, BLACKSLAG.get().defaultBlockState()), InkColors.CYAN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_AMETHYST_ORE = register(singleton(blockWithItem("blackslag_amethyst_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.MAGENTA, SpectrumAdvancements.COLLECT_AMETHYST, BLACKSLAG.get().defaultBlockState()), InkColors.MAGENTA), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_CITRINE_ORE = register(singleton(blockWithItem("blackslag_citrine_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.YELLOW, SpectrumAdvancements.COLLECT_CITRINE, BLACKSLAG.get().defaultBlockState()), InkColors.YELLOW), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_ONYX_ORE = register(singleton(blockWithItem("blackslag_onyx_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.BLACK, SpectrumAdvancements.CREATE_ONYX, BLACKSLAG.get().defaultBlockState()), InkColors.BLACK), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLACKSLAG_MOONSTONE_ORE = register(singleton(blockWithItem("blackslag_moonstone_ore", () -> new GemstoneOreBlock(gemOreExperienceProvider, blackslagOre(), BuiltinGemstoneColor.WHITE, SpectrumAdvancements.COLLECT_MOONSTONE, BLACKSLAG.get().defaultBlockState()), InkColors.WHITE), TexturedModel.COLUMN_ALT));
	
	private static BlockBehaviour.Properties polishedGemBlock(MapColor mapColor, SoundType soundGroup) {
		return settings(mapColor, soundGroup, 5.0F, 6.0F);
	}
	
	public static final DeferredBlock<Block> POLISHED_TOPAZ = register(simple(blockWithItem("polished_topaz", () -> new Block(polishedGemBlock(MapColor.COLOR_CYAN, SpectrumSoundTypes.TOPAZ_BLOCK)), InkColors.CYAN)));
	public static final DeferredBlock<Block> POLISHED_AMETHYST = register(simple(blockWithItem("polished_amethyst", () -> new Block(polishedGemBlock(MapColor.COLOR_MAGENTA, SoundType.AMETHYST)), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> POLISHED_CITRINE = register(simple(blockWithItem("polished_citrine", () -> new Block(polishedGemBlock(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_BLOCK)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> POLISHED_ONYX = register(simple(blockWithItem("polished_onyx", () -> new Block(polishedGemBlock(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_BLOCK)), InkColors.BLACK)));
	public static final DeferredBlock<Block> POLISHED_MOONSTONE = register(simple(blockWithItem("polished_moonstone", () -> new Block(polishedGemBlock(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_BLOCK)), InkColors.WHITE)));
	
	public static final DeferredBlock<Block> TOPAZ_PILLAR = register(singleton(blockWithItem("topaz_pillar", () -> new Block(polishedGemBlock(MapColor.COLOR_CYAN, SpectrumSoundTypes.TOPAZ_BLOCK)), InkColors.CYAN), TexturedModel.TOP_BOTTOM_WITH_WALL));
	public static final DeferredBlock<Block> AMETHYST_PILLAR = register(singleton(blockWithItem("amethyst_pillar", () -> new Block(polishedGemBlock(MapColor.COLOR_MAGENTA, SoundType.AMETHYST)), InkColors.MAGENTA), TexturedModel.TOP_BOTTOM_WITH_WALL));
	public static final DeferredBlock<Block> CITRINE_PILLAR = register(singleton(blockWithItem("citrine_pillar", () -> new Block(polishedGemBlock(MapColor.COLOR_YELLOW, SpectrumSoundTypes.CITRINE_BLOCK)), InkColors.YELLOW), TexturedModel.TOP_BOTTOM_WITH_WALL));
	public static final DeferredBlock<Block> ONYX_PILLAR = register(singleton(blockWithItem("onyx_pillar", () -> new Block(polishedGemBlock(MapColor.COLOR_BLACK, SpectrumSoundTypes.ONYX_BLOCK)), InkColors.BLACK), TexturedModel.TOP_BOTTOM_WITH_WALL));
	public static final DeferredBlock<Block> MOONSTONE_PILLAR = register(singleton(blockWithItem("moonstone_pillar", () -> new Block(polishedGemBlock(MapColor.SNOW, SpectrumSoundTypes.MOONSTONE_BLOCK)), InkColors.WHITE), TexturedModel.TOP_BOTTOM_WITH_WALL));
	
	private static BlockBehaviour.Properties copyWithMapColor(Block baseBlock, MapColor color) {
		return BlockBehaviour.Properties.ofFullCopy(baseBlock).mapColor(color);
	}
	
	public static BlockBehaviour.Properties pottedPlant() {
		return BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
	}
	
	public static final DeferredBlock<AmaranthBushelBlock> AMARANTH_BUSHEL = register(cross(blockWithItem("amaranth_bushel", () -> new AmaranthBushelBlock(SpectrumMobEffects.NOURISHING, 8, settings(MapColor.NONE, SoundType.CROP, 0.0F).noCollission()), InkColors.RED)).withItemModel(SpectrumModelHelper::registerItemModel));
	public static final DeferredBlock<PottedAmaranthBushelBlock> POTTED_AMARANTH_BUSHEL = register(pottedPlant(block("potted_amaranth_bushel", () -> new PottedAmaranthBushelBlock(AMARANTH_BUSHEL.get(), pottedPlant())), false));
	
	public static final DeferredBlock<Block> RESONANT_LILY = register(simplePlant(blockWithItem("resonant_lily", () -> new ResonantLilyBlock(MobEffects.REGENERATION, 5, BlockBehaviour.Properties.ofFullCopy(POPPY).mapColor(MapColor.SNOW)), InkColors.GREEN)));
	public static final DeferredBlock<PottedResonantLilyBlock> POTTED_RESONANT_LILY = register(pottedPlant(block("potted_resonant_lily", () -> new PottedResonantLilyBlock(RESONANT_LILY.get(), pottedPlant())), false));
	
	public static final DeferredBlock<BloodOrchidBlock> BLOOD_ORCHID = register(blockWithItem("blood_orchid", () -> new BloodOrchidBlock(SpectrumMobEffects.FRENZY, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).offsetType(BlockBehaviour.OffsetType.NONE).randomTicks()), InkColors.RED).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerBlockTexturedItemModel(ctx, block, "5")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BloodOrchidBlock.AGE).generate(stage -> SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.cross(b -> b, stage.toString()).createWithSuffix(block, stage.toString(), ctx.modelOutput))))));
	public static final DeferredBlock<Block> POTTED_BLOOD_ORCHID = register(singleton(block("potted_blood_orchid", () -> new PottedBloodOrchidBlock(BLOOD_ORCHID.get(), pottedPlant())), SpectrumTexturedModelProviders.flowerPotCross(b -> BLOOD_ORCHID.get(), "5", false)));
	
	public static final DeferredBlock<FlowerPotBlock> POTTED_SWEET_PEA = register(pottedPlantWithCustomTexture(block("potted_sweet_pea", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, SWEET_PEA, pottedPlant().lightLevel(s -> 11).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always))), "_potted"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_APRICOTTI = register(pottedPlantWithCustomTexture(block("potted_apricotti", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, APRICOTTI, pottedPlant().lightLevel(s -> 11).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always))), "_potted"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_VARIA_SPROUT = register(pottedPlantWithCustomTexture(block("potted_varia_sprout", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, VARIA_SPROUT, pottedPlant().lightLevel(s -> 11).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always))), "_potted"));
	public static final DeferredBlock<FlowerPotBlock> POTTED_HUMMING_BELL = register(pottedPlant(block("potted_humming_bell", () -> new FlowerPotBlock(() -> (FlowerPotBlock) FLOWER_POT, HUMMING_BELL, pottedPlant().lightLevel(s -> 9).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always))), false));
	
	public static DeferredBlock<ColoredSaplingBlock> registerColoredSapling(String name, InkColor color, TreeGrower generator) {
		return register(simplePlant(blockWithItem(name, () -> new ColoredSaplingBlock(copyWithMapColor(OAK_SAPLING, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color, generator), color)));
	}
	public static final DeferredBlock<ColoredSaplingBlock> BLACK_SAPLING = registerColoredSapling("black_sapling", InkColors.BLACK, SpectrumTreeGrowers.BLACK_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> BLUE_SAPLING = registerColoredSapling("blue_sapling", InkColors.BLUE, SpectrumTreeGrowers.BLUE_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> BROWN_SAPLING = registerColoredSapling("brown_sapling", InkColors.BROWN, SpectrumTreeGrowers.BROWN_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> CYAN_SAPLING = registerColoredSapling("cyan_sapling", InkColors.CYAN, SpectrumTreeGrowers.CYAN_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> GRAY_SAPLING = registerColoredSapling("gray_sapling", InkColors.GRAY, SpectrumTreeGrowers.GRAY_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> GREEN_SAPLING = registerColoredSapling("green_sapling", InkColors.GREEN, SpectrumTreeGrowers.GREEN_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> LIGHT_BLUE_SAPLING = registerColoredSapling("light_blue_sapling", InkColors.LIGHT_BLUE, SpectrumTreeGrowers.LIGHT_BLUE_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> LIGHT_GRAY_SAPLING = registerColoredSapling("light_gray_sapling", InkColors.LIGHT_GRAY, SpectrumTreeGrowers.LIGHT_GRAY_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> LIME_SAPLING = registerColoredSapling("lime_sapling", InkColors.LIME, SpectrumTreeGrowers.LIME_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> MAGENTA_SAPLING = registerColoredSapling("magenta_sapling", InkColors.MAGENTA, SpectrumTreeGrowers.MAGENTA_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> ORANGE_SAPLING = registerColoredSapling("orange_sapling", InkColors.ORANGE, SpectrumTreeGrowers.ORANGE_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> PINK_SAPLING = registerColoredSapling("pink_sapling", InkColors.PINK, SpectrumTreeGrowers.PINK_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> PURPLE_SAPLING = registerColoredSapling("purple_sapling", InkColors.PURPLE, SpectrumTreeGrowers.PURPLE_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> RED_SAPLING = registerColoredSapling("red_sapling", InkColors.RED, SpectrumTreeGrowers.RED_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> WHITE_SAPLING = registerColoredSapling("white_sapling", InkColors.WHITE, SpectrumTreeGrowers.WHITE_COLORED_SAPLING_GENERATOR);
	public static final DeferredBlock<ColoredSaplingBlock> YELLOW_SAPLING = registerColoredSapling("yellow_sapling", InkColors.YELLOW, SpectrumTreeGrowers.YELLOW_COLORED_SAPLING_GENERATOR);
	
	public static DeferredBlock<PottedColoredSaplingBlock> registerPottedColoredSapling(String name, DeferredBlock<ColoredSaplingBlock> saplingBlock) {
		return register(pottedPlant(block(name, () -> new PottedColoredSaplingBlock(saplingBlock.get(), pottedPlant(), saplingBlock.get().getColor())), false));
	}
	
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_BLACK_SAPLING = registerPottedColoredSapling("potted_black_sapling", BLACK_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_BLUE_SAPLING = registerPottedColoredSapling("potted_blue_sapling", BLUE_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_BROWN_SAPLING = registerPottedColoredSapling("potted_brown_sapling", BROWN_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_CYAN_SAPLING = registerPottedColoredSapling("potted_cyan_sapling", CYAN_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_GRAY_SAPLING = registerPottedColoredSapling("potted_gray_sapling", GRAY_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_GREEN_SAPLING = registerPottedColoredSapling("potted_green_sapling", GREEN_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_LIGHT_BLUE_SAPLING = registerPottedColoredSapling("potted_light_blue_sapling", LIGHT_BLUE_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_LIGHT_GRAY_SAPLING = registerPottedColoredSapling("potted_light_gray_sapling", LIGHT_GRAY_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_LIME_SAPLING = registerPottedColoredSapling("potted_lime_sapling", LIME_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_MAGENTA_SAPLING = registerPottedColoredSapling("potted_magenta_sapling", MAGENTA_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_ORANGE_SAPLING = registerPottedColoredSapling("potted_orange_sapling", ORANGE_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_PINK_SAPLING = registerPottedColoredSapling("potted_pink_sapling", PINK_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_PURPLE_SAPLING = registerPottedColoredSapling("potted_purple_sapling", PURPLE_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_RED_SAPLING = registerPottedColoredSapling("potted_red_sapling", RED_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_WHITE_SAPLING = registerPottedColoredSapling("potted_white_sapling", WHITE_SAPLING);
	public static final DeferredBlock<PottedColoredSaplingBlock> POTTED_YELLOW_SAPLING = registerPottedColoredSapling("potted_yellow_sapling", YELLOW_SAPLING);
	
	public static DeferredBlock<ColoredStrippedSpectrumLogBlock> registerColoredStrippedLog(String name, InkColor color) {
		return register(log(blockWithItem(name, () -> new ColoredStrippedSpectrumLogBlock(copyWithMapColor(STRIPPED_OAK_LOG, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color)));
	}
	
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_BLACK_LOG = registerColoredStrippedLog("stripped_black_log", InkColors.BLACK);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_BLUE_LOG = registerColoredStrippedLog("stripped_blue_log", InkColors.BLUE);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_BROWN_LOG = registerColoredStrippedLog("stripped_brown_log", InkColors.BROWN);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_CYAN_LOG = registerColoredStrippedLog("stripped_cyan_log", InkColors.CYAN);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_GRAY_LOG = registerColoredStrippedLog("stripped_gray_log", InkColors.GRAY);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_GREEN_LOG = registerColoredStrippedLog("stripped_green_log", InkColors.GREEN);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_LIGHT_BLUE_LOG = registerColoredStrippedLog("stripped_light_blue_log", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_LIGHT_GRAY_LOG = registerColoredStrippedLog("stripped_light_gray_log", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_LIME_LOG = registerColoredStrippedLog("stripped_lime_log", InkColors.LIME);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_MAGENTA_LOG = registerColoredStrippedLog("stripped_magenta_log", InkColors.MAGENTA);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_ORANGE_LOG = registerColoredStrippedLog("stripped_orange_log", InkColors.ORANGE);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_PINK_LOG = registerColoredStrippedLog("stripped_pink_log", InkColors.PINK);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_PURPLE_LOG = registerColoredStrippedLog("stripped_purple_log", InkColors.PURPLE);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_RED_LOG = registerColoredStrippedLog("stripped_red_log", InkColors.RED);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_WHITE_LOG = registerColoredStrippedLog("stripped_white_log", InkColors.WHITE);
	public static final DeferredBlock<ColoredStrippedSpectrumLogBlock> STRIPPED_YELLOW_LOG = registerColoredStrippedLog("stripped_yellow_log", InkColors.YELLOW);
	
	public static DeferredBlock<ColoredStrippedWoodBlockSpectrum> registerColoredStrippedWood(String name, DeferredBlock<ColoredStrippedSpectrumLogBlock> logBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredStrippedWoodBlockSpectrum(copyWithMapColor(STRIPPED_OAK_WOOD, logBlock.get().defaultMapColor()), color), color));
	}
	
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_BLACK_WOOD = registerColoredStrippedWood("stripped_black_wood", STRIPPED_BLACK_LOG, InkColors.BLACK);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_BLUE_WOOD = registerColoredStrippedWood("stripped_blue_wood", STRIPPED_BLUE_LOG, InkColors.BLUE);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_BROWN_WOOD = registerColoredStrippedWood("stripped_brown_wood", STRIPPED_BROWN_LOG, InkColors.BROWN);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_CYAN_WOOD = registerColoredStrippedWood("stripped_cyan_wood", STRIPPED_CYAN_LOG, InkColors.CYAN);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_GRAY_WOOD = registerColoredStrippedWood("stripped_gray_wood", STRIPPED_GRAY_LOG, InkColors.GRAY);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_GREEN_WOOD = registerColoredStrippedWood("stripped_green_wood", STRIPPED_GREEN_LOG, InkColors.GREEN);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_LIGHT_BLUE_WOOD = registerColoredStrippedWood("stripped_light_blue_wood", STRIPPED_LIGHT_BLUE_LOG, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_LIGHT_GRAY_WOOD = registerColoredStrippedWood("stripped_light_gray_wood", STRIPPED_LIGHT_GRAY_LOG, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_LIME_WOOD = registerColoredStrippedWood("stripped_lime_wood", STRIPPED_LIME_LOG, InkColors.LIME);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_MAGENTA_WOOD = registerColoredStrippedWood("stripped_magenta_wood", STRIPPED_MAGENTA_LOG, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_ORANGE_WOOD = registerColoredStrippedWood("stripped_orange_wood", STRIPPED_ORANGE_LOG, InkColors.ORANGE);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_PINK_WOOD = registerColoredStrippedWood("stripped_pink_wood", STRIPPED_PINK_LOG, InkColors.PINK);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_PURPLE_WOOD = registerColoredStrippedWood("stripped_purple_wood", STRIPPED_PURPLE_LOG, InkColors.PURPLE);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_RED_WOOD = registerColoredStrippedWood("stripped_red_wood", STRIPPED_RED_LOG, InkColors.RED);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_WHITE_WOOD = registerColoredStrippedWood("stripped_white_wood", STRIPPED_WHITE_LOG, InkColors.WHITE);
	public static final DeferredBlock<ColoredStrippedWoodBlockSpectrum> STRIPPED_YELLOW_WOOD = registerColoredStrippedWood("stripped_yellow_wood", STRIPPED_YELLOW_LOG, InkColors.YELLOW);
	
	public static DeferredBlock<ColoredLogBlock> registerColoredLog(String name, Supplier<? extends ColoredStrippedSpectrumLogBlock> strippedBlock, InkColor color) {
		return register(log(blockWithItem(name, () -> new ColoredLogBlock(copyWithMapColor(OAK_LOG, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), strippedBlock, color), color)));
	}
	
	public static final DeferredBlock<ColoredLogBlock> BLACK_LOG = registerColoredLog("black_log", STRIPPED_BLACK_LOG, InkColors.BLACK);
	public static final DeferredBlock<ColoredLogBlock> BLUE_LOG = registerColoredLog("blue_log", STRIPPED_BLUE_LOG, InkColors.BLUE);
	public static final DeferredBlock<ColoredLogBlock> BROWN_LOG = registerColoredLog("brown_log", STRIPPED_BROWN_LOG, InkColors.BROWN);
	public static final DeferredBlock<ColoredLogBlock> CYAN_LOG = registerColoredLog("cyan_log", STRIPPED_CYAN_LOG, InkColors.CYAN);
	public static final DeferredBlock<ColoredLogBlock> GRAY_LOG = registerColoredLog("gray_log", STRIPPED_GRAY_LOG, InkColors.GRAY);
	public static final DeferredBlock<ColoredLogBlock> GREEN_LOG = registerColoredLog("green_log", STRIPPED_GREEN_LOG, InkColors.GREEN);
	public static final DeferredBlock<ColoredLogBlock> LIGHT_BLUE_LOG = registerColoredLog("light_blue_log", STRIPPED_LIGHT_BLUE_LOG, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredLogBlock> LIGHT_GRAY_LOG = registerColoredLog("light_gray_log", STRIPPED_LIGHT_GRAY_LOG, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredLogBlock> LIME_LOG = registerColoredLog("lime_log", STRIPPED_LIME_LOG, InkColors.LIME);
	public static final DeferredBlock<ColoredLogBlock> MAGENTA_LOG = registerColoredLog("magenta_log", STRIPPED_MAGENTA_LOG, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredLogBlock> ORANGE_LOG = registerColoredLog("orange_log", STRIPPED_ORANGE_LOG, InkColors.ORANGE);
	public static final DeferredBlock<ColoredLogBlock> PINK_LOG = registerColoredLog("pink_log", STRIPPED_PINK_LOG, InkColors.PINK);
	public static final DeferredBlock<ColoredLogBlock> PURPLE_LOG = registerColoredLog("purple_log", STRIPPED_PURPLE_LOG, InkColors.PURPLE);
	public static final DeferredBlock<ColoredLogBlock> RED_LOG = registerColoredLog("red_log", STRIPPED_RED_LOG, InkColors.RED);
	public static final DeferredBlock<ColoredLogBlock> WHITE_LOG = registerColoredLog("white_log", STRIPPED_WHITE_LOG, InkColors.WHITE);
	public static final DeferredBlock<ColoredLogBlock> YELLOW_LOG = registerColoredLog("yellow_log", STRIPPED_YELLOW_LOG, InkColors.YELLOW);
	
	public static DeferredBlock<ColoredWoodBlock> registerColoredWood(String name, Supplier<? extends ColoredStrippedWoodBlockSpectrum> strippedBlock, DeferredBlock<ColoredLogBlock> logBlock, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredWoodBlock(copyWithMapColor(OAK_WOOD, logBlock.get().defaultMapColor()), strippedBlock, color), color));
	}
	
	public static final DeferredBlock<ColoredWoodBlock> BLACK_WOOD = registerColoredWood("black_wood", STRIPPED_BLACK_WOOD, BLACK_LOG, InkColors.BLACK);
	public static final DeferredBlock<ColoredWoodBlock> BLUE_WOOD = registerColoredWood("blue_wood", STRIPPED_BLUE_WOOD, BLUE_LOG, InkColors.BLUE);
	public static final DeferredBlock<ColoredWoodBlock> BROWN_WOOD = registerColoredWood("brown_wood", STRIPPED_BROWN_WOOD, BROWN_LOG, InkColors.BROWN);
	public static final DeferredBlock<ColoredWoodBlock> CYAN_WOOD = registerColoredWood("cyan_wood", STRIPPED_CYAN_WOOD, CYAN_LOG, InkColors.CYAN);
	public static final DeferredBlock<ColoredWoodBlock> GRAY_WOOD = registerColoredWood("gray_wood", STRIPPED_GRAY_WOOD, GRAY_LOG, InkColors.GRAY);
	public static final DeferredBlock<ColoredWoodBlock> GREEN_WOOD = registerColoredWood("green_wood", STRIPPED_GREEN_WOOD, GREEN_LOG, InkColors.GREEN);
	public static final DeferredBlock<ColoredWoodBlock> LIGHT_BLUE_WOOD = registerColoredWood("light_blue_wood", STRIPPED_LIGHT_BLUE_WOOD, LIGHT_BLUE_LOG, InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredWoodBlock> LIGHT_GRAY_WOOD = registerColoredWood("light_gray_wood", STRIPPED_LIGHT_GRAY_WOOD, LIGHT_GRAY_LOG, InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredWoodBlock> LIME_WOOD = registerColoredWood("lime_wood", STRIPPED_LIME_WOOD, LIME_LOG, InkColors.LIME);
	public static final DeferredBlock<ColoredWoodBlock> MAGENTA_WOOD = registerColoredWood("magenta_wood", STRIPPED_MAGENTA_WOOD, MAGENTA_LOG, InkColors.MAGENTA);
	public static final DeferredBlock<ColoredWoodBlock> ORANGE_WOOD = registerColoredWood("orange_wood", STRIPPED_ORANGE_WOOD, ORANGE_LOG, InkColors.ORANGE);
	public static final DeferredBlock<ColoredWoodBlock> PINK_WOOD = registerColoredWood("pink_wood", STRIPPED_PINK_WOOD, PINK_LOG, InkColors.PINK);
	public static final DeferredBlock<ColoredWoodBlock> PURPLE_WOOD = registerColoredWood("purple_wood", STRIPPED_PURPLE_WOOD, PURPLE_LOG, InkColors.PURPLE);
	public static final DeferredBlock<ColoredWoodBlock> RED_WOOD = registerColoredWood("red_wood", STRIPPED_RED_WOOD, RED_LOG, InkColors.RED);
	public static final DeferredBlock<ColoredWoodBlock> WHITE_WOOD = registerColoredWood("white_wood", STRIPPED_WHITE_WOOD, WHITE_LOG, InkColors.WHITE);
	public static final DeferredBlock<ColoredWoodBlock> YELLOW_WOOD = registerColoredWood("yellow_wood", STRIPPED_YELLOW_WOOD, YELLOW_LOG, InkColors.YELLOW);
	
	public static DeferredBlock<ColoredLeavesBlock> registerColoredLeaves(String name, InkColor color) {
		return register(singleton(blockWithItem(name, () -> new ColoredLeavesBlock(copyWithMapColor(OAK_LEAVES, color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color), TexturedModel.LEAVES));
	}
	
	public static final DeferredBlock<ColoredLeavesBlock> BLACK_LEAVES = registerColoredLeaves("black_leaves", InkColors.BLACK);
	public static final DeferredBlock<ColoredLeavesBlock> BLUE_LEAVES = registerColoredLeaves("blue_leaves", InkColors.BLUE);
	public static final DeferredBlock<ColoredLeavesBlock> BROWN_LEAVES = registerColoredLeaves("brown_leaves", InkColors.BROWN);
	public static final DeferredBlock<ColoredLeavesBlock> CYAN_LEAVES = registerColoredLeaves("cyan_leaves", InkColors.CYAN);
	public static final DeferredBlock<ColoredLeavesBlock> GRAY_LEAVES = registerColoredLeaves("gray_leaves", InkColors.GRAY);
	public static final DeferredBlock<ColoredLeavesBlock> GREEN_LEAVES = registerColoredLeaves("green_leaves", InkColors.GREEN);
	public static final DeferredBlock<ColoredLeavesBlock> LIGHT_BLUE_LEAVES = registerColoredLeaves("light_blue_leaves", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredLeavesBlock> LIGHT_GRAY_LEAVES = registerColoredLeaves("light_gray_leaves", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredLeavesBlock> LIME_LEAVES = registerColoredLeaves("lime_leaves", InkColors.LIME);
	public static final DeferredBlock<ColoredLeavesBlock> MAGENTA_LEAVES = registerColoredLeaves("magenta_leaves", InkColors.MAGENTA);
	public static final DeferredBlock<ColoredLeavesBlock> ORANGE_LEAVES = registerColoredLeaves("orange_leaves", InkColors.ORANGE);
	public static final DeferredBlock<ColoredLeavesBlock> PINK_LEAVES = registerColoredLeaves("pink_leaves", InkColors.PINK);
	public static final DeferredBlock<ColoredLeavesBlock> PURPLE_LEAVES = registerColoredLeaves("purple_leaves", InkColors.PURPLE);
	public static final DeferredBlock<ColoredLeavesBlock> RED_LEAVES = registerColoredLeaves("red_leaves", InkColors.RED);
	public static final DeferredBlock<ColoredLeavesBlock> WHITE_LEAVES = registerColoredLeaves("white_leaves", InkColors.WHITE);
	public static final DeferredBlock<ColoredLeavesBlock> YELLOW_LEAVES = registerColoredLeaves("yellow_leaves", InkColors.YELLOW);
	
	public static DeferredBlock<GlowBlock> registerGlowBlock(String name, InkColor color) {
		return register(simple(blockWithItem(name, () -> new GlowBlock(settings(color.getDyeColor().orElse(DyeColor.LIME).getMapColor(), SoundType.BASALT, 2.5F).requiresCorrectToolForDrops().lightLevel(state -> 1).hasPostProcess(SpectrumBlocks::always).emissiveRendering(SpectrumBlocks::always), color), color)));
	}
	
	public static final DeferredBlock<GlowBlock> BLACK_GLOWBLOCK = registerGlowBlock("black_glowblock", InkColors.BLACK);
	public static final DeferredBlock<GlowBlock> BLUE_GLOWBLOCK = registerGlowBlock("blue_glowblock", InkColors.BLUE);
	public static final DeferredBlock<GlowBlock> BROWN_GLOWBLOCK = registerGlowBlock("brown_glowblock", InkColors.BROWN);
	public static final DeferredBlock<GlowBlock> CYAN_GLOWBLOCK = registerGlowBlock("cyan_glowblock", InkColors.CYAN);
	public static final DeferredBlock<GlowBlock> GRAY_GLOWBLOCK = registerGlowBlock("gray_glowblock", InkColors.GRAY);
	public static final DeferredBlock<GlowBlock> GREEN_GLOWBLOCK = registerGlowBlock("green_glowblock", InkColors.GREEN);
	public static final DeferredBlock<GlowBlock> LIGHT_BLUE_GLOWBLOCK = registerGlowBlock("light_blue_glowblock", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<GlowBlock> LIGHT_GRAY_GLOWBLOCK = registerGlowBlock("light_gray_glowblock", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<GlowBlock> LIME_GLOWBLOCK = registerGlowBlock("lime_glowblock", InkColors.LIME);
	public static final DeferredBlock<GlowBlock> MAGENTA_GLOWBLOCK = registerGlowBlock("magenta_glowblock", InkColors.MAGENTA);
	public static final DeferredBlock<GlowBlock> ORANGE_GLOWBLOCK = registerGlowBlock("orange_glowblock", InkColors.ORANGE);
	public static final DeferredBlock<GlowBlock> PINK_GLOWBLOCK = registerGlowBlock("pink_glowblock", InkColors.PINK);
	public static final DeferredBlock<GlowBlock> PURPLE_GLOWBLOCK = registerGlowBlock("purple_glowblock", InkColors.PURPLE);
	public static final DeferredBlock<GlowBlock> RED_GLOWBLOCK = registerGlowBlock("red_glowblock", InkColors.RED);
	public static final DeferredBlock<GlowBlock> WHITE_GLOWBLOCK = registerGlowBlock("white_glowblock", InkColors.WHITE);
	public static final DeferredBlock<GlowBlock> YELLOW_GLOWBLOCK = registerGlowBlock("yellow_glowblock", InkColors.YELLOW);
	
	public static DeferredBlock<ColoredLightBlock> registerColoredLightBlock(String name, InkColor color) {
		return register(blockWithItem(name, () -> new ColoredLightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP).mapColor(color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color), color).withBlockModel((ctx, block) -> {
			ResourceLocation off = TexturedModel.CUBE.create(block, ctx.modelOutput);
			ResourceLocation on = SpectrumModelTemplates.COLORED_LAMP_ON.createWithSuffix(block, "_on", SpectrumTextureMaps.innerOuter(block, "_on", block, "_outer"), ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.LIT, on, off));
		}));
	}
	
	public static final DeferredBlock<ColoredLightBlock> BLACK_LAMP = registerColoredLightBlock("black_lamp", InkColors.BLACK);
	public static final DeferredBlock<ColoredLightBlock> BLUE_LAMP = registerColoredLightBlock("blue_lamp", InkColors.BLUE);
	public static final DeferredBlock<ColoredLightBlock> BROWN_LAMP = registerColoredLightBlock("brown_lamp", InkColors.BROWN);
	public static final DeferredBlock<ColoredLightBlock> CYAN_LAMP = registerColoredLightBlock("cyan_lamp", InkColors.CYAN);
	public static final DeferredBlock<ColoredLightBlock> GRAY_LAMP = registerColoredLightBlock("gray_lamp", InkColors.GRAY);
	public static final DeferredBlock<ColoredLightBlock> GREEN_LAMP = registerColoredLightBlock("green_lamp", InkColors.GREEN);
	public static final DeferredBlock<ColoredLightBlock> LIGHT_BLUE_LAMP = registerColoredLightBlock("light_blue_lamp", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<ColoredLightBlock> LIGHT_GRAY_LAMP = registerColoredLightBlock("light_gray_lamp", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<ColoredLightBlock> LIME_LAMP = registerColoredLightBlock("lime_lamp", InkColors.LIME);
	public static final DeferredBlock<ColoredLightBlock> MAGENTA_LAMP = registerColoredLightBlock("magenta_lamp", InkColors.MAGENTA);
	public static final DeferredBlock<ColoredLightBlock> ORANGE_LAMP = registerColoredLightBlock("orange_lamp", InkColors.ORANGE);
	public static final DeferredBlock<ColoredLightBlock> PINK_LAMP = registerColoredLightBlock("pink_lamp", InkColors.PINK);
	public static final DeferredBlock<ColoredLightBlock> PURPLE_LAMP = registerColoredLightBlock("purple_lamp", InkColors.PURPLE);
	public static final DeferredBlock<ColoredLightBlock> RED_LAMP = registerColoredLightBlock("red_lamp", InkColors.RED);
	public static final DeferredBlock<ColoredLightBlock> WHITE_LAMP = registerColoredLightBlock("white_lamp", InkColors.WHITE);
	public static final DeferredBlock<ColoredLightBlock> YELLOW_LAMP = registerColoredLightBlock("yellow_lamp", InkColors.YELLOW);
	
	public static DeferredBlock<PigmentBlock> registerPigmentBlock(String name, InkColor color) {
		return register(simple(blockWithItem(name, () -> new PigmentBlock(settings(color.getDyeColor().orElse(DyeColor.LIME).getMapColor(), SoundType.WOOL, 1.0F), color), color)));
	}
	
	public static final DeferredBlock<PigmentBlock> BLACK_BLOCK = registerPigmentBlock("black_block", InkColors.BLACK);
	public static final DeferredBlock<PigmentBlock> BLUE_BLOCK = registerPigmentBlock("blue_block", InkColors.BLUE);
	public static final DeferredBlock<PigmentBlock> BROWN_BLOCK = registerPigmentBlock("brown_block", InkColors.BROWN);
	public static final DeferredBlock<PigmentBlock> CYAN_BLOCK = registerPigmentBlock("cyan_block", InkColors.CYAN);
	public static final DeferredBlock<PigmentBlock> GRAY_BLOCK = registerPigmentBlock("gray_block", InkColors.GRAY);
	public static final DeferredBlock<PigmentBlock> GREEN_BLOCK = registerPigmentBlock("green_block", InkColors.GREEN);
	public static final DeferredBlock<PigmentBlock> LIGHT_BLUE_BLOCK = registerPigmentBlock("light_blue_block", InkColors.LIGHT_BLUE);
	public static final DeferredBlock<PigmentBlock> LIGHT_GRAY_BLOCK = registerPigmentBlock("light_gray_block", InkColors.LIGHT_GRAY);
	public static final DeferredBlock<PigmentBlock> LIME_BLOCK = registerPigmentBlock("lime_block", InkColors.LIME);
	public static final DeferredBlock<PigmentBlock> MAGENTA_BLOCK = registerPigmentBlock("magenta_block", InkColors.MAGENTA);
	public static final DeferredBlock<PigmentBlock> ORANGE_BLOCK = registerPigmentBlock("orange_block", InkColors.ORANGE);
	public static final DeferredBlock<PigmentBlock> PINK_BLOCK = registerPigmentBlock("pink_block", InkColors.PINK);
	public static final DeferredBlock<PigmentBlock> PURPLE_BLOCK = registerPigmentBlock("purple_block", InkColors.PURPLE);
	public static final DeferredBlock<PigmentBlock> RED_BLOCK = registerPigmentBlock("red_block", InkColors.RED);
	public static final DeferredBlock<PigmentBlock> WHITE_BLOCK = registerPigmentBlock("white_block", InkColors.WHITE);
	public static final DeferredBlock<PigmentBlock> YELLOW_BLOCK = registerPigmentBlock("yellow_block", InkColors.YELLOW);
	
	public static DeferredBlock<ColoredSporeBlossomBlock> registerColoredSporeBlossomBlock(String name, InkColor color, ColoredFallingSporeBlossomParticleEffect falling, ColoredSporeBlossomAirParticleEffect air) {
		return register(singleton(blockWithItem(name, () -> new ColoredSporeBlossomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).mapColor(color.getDyeColor().orElse(DyeColor.LIME).getMapColor()), color, falling, air), color), TexturedModel.createDefault(b -> SpectrumTextureMaps.flowerParticle(b, "", b, ""), SpectrumModelTemplates.SPORE_BLOSSOM)));
	}
	
	public static final DeferredBlock<ColoredSporeBlossomBlock> BLACK_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("black_spore_blossom", InkColors.BLACK, ColoredFallingSporeBlossomParticleEffect.BLACK, ColoredSporeBlossomAirParticleEffect.BLACK);
	public static final DeferredBlock<ColoredSporeBlossomBlock> BLUE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("blue_spore_blossom", InkColors.BLUE, ColoredFallingSporeBlossomParticleEffect.BLUE, ColoredSporeBlossomAirParticleEffect.BLUE);
	public static final DeferredBlock<ColoredSporeBlossomBlock> BROWN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("brown_spore_blossom", InkColors.BROWN, ColoredFallingSporeBlossomParticleEffect.BROWN, ColoredSporeBlossomAirParticleEffect.BROWN);
	public static final DeferredBlock<ColoredSporeBlossomBlock> CYAN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("cyan_spore_blossom", InkColors.CYAN, ColoredFallingSporeBlossomParticleEffect.CYAN, ColoredSporeBlossomAirParticleEffect.CYAN);
	public static final DeferredBlock<ColoredSporeBlossomBlock> GRAY_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("gray_spore_blossom", InkColors.GRAY, ColoredFallingSporeBlossomParticleEffect.GRAY, ColoredSporeBlossomAirParticleEffect.GRAY);
	public static final DeferredBlock<ColoredSporeBlossomBlock> GREEN_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("green_spore_blossom", InkColors.GREEN, ColoredFallingSporeBlossomParticleEffect.GREEN, ColoredSporeBlossomAirParticleEffect.GREEN);
	public static final DeferredBlock<ColoredSporeBlossomBlock> LIGHT_BLUE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("light_blue_spore_blossom", InkColors.LIGHT_BLUE, ColoredFallingSporeBlossomParticleEffect.LIGHT_BLUE, ColoredSporeBlossomAirParticleEffect.LIGHT_BLUE);
	public static final DeferredBlock<ColoredSporeBlossomBlock> LIGHT_GRAY_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("light_gray_spore_blossom", InkColors.LIGHT_GRAY, ColoredFallingSporeBlossomParticleEffect.LIGHT_GRAY, ColoredSporeBlossomAirParticleEffect.LIGHT_GRAY);
	public static final DeferredBlock<ColoredSporeBlossomBlock> LIME_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("lime_spore_blossom", InkColors.LIME, ColoredFallingSporeBlossomParticleEffect.LIME, ColoredSporeBlossomAirParticleEffect.LIME);
	public static final DeferredBlock<ColoredSporeBlossomBlock> MAGENTA_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("magenta_spore_blossom", InkColors.MAGENTA, ColoredFallingSporeBlossomParticleEffect.MAGENTA, ColoredSporeBlossomAirParticleEffect.MAGENTA);
	public static final DeferredBlock<ColoredSporeBlossomBlock> ORANGE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("orange_spore_blossom", InkColors.ORANGE, ColoredFallingSporeBlossomParticleEffect.ORANGE, ColoredSporeBlossomAirParticleEffect.ORANGE);
	public static final DeferredBlock<ColoredSporeBlossomBlock> PINK_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("pink_spore_blossom", InkColors.PINK, ColoredFallingSporeBlossomParticleEffect.PINK, ColoredSporeBlossomAirParticleEffect.PINK);
	public static final DeferredBlock<ColoredSporeBlossomBlock> PURPLE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("purple_spore_blossom", InkColors.PURPLE, ColoredFallingSporeBlossomParticleEffect.PURPLE, ColoredSporeBlossomAirParticleEffect.PURPLE);
	public static final DeferredBlock<ColoredSporeBlossomBlock> RED_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("red_spore_blossom", InkColors.RED, ColoredFallingSporeBlossomParticleEffect.RED, ColoredSporeBlossomAirParticleEffect.RED);
	public static final DeferredBlock<ColoredSporeBlossomBlock> WHITE_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("white_spore_blossom", InkColors.WHITE, ColoredFallingSporeBlossomParticleEffect.WHITE, ColoredSporeBlossomAirParticleEffect.WHITE);
	public static final DeferredBlock<ColoredSporeBlossomBlock> YELLOW_SPORE_BLOSSOM = registerColoredSporeBlossomBlock("yellow_spore_blossom", InkColors.YELLOW, ColoredFallingSporeBlossomParticleEffect.YELLOW, ColoredSporeBlossomAirParticleEffect.YELLOW);
	
	public static DeferredBlock<ShimmerstoneLightBlock> registerShimmerstoneLight(String name, SoundType soundGroup, Supplier<ResourceLocation> outerSupplier) {
		return register(blockWithItem(name, () -> new ShimmerstoneLightBlock(settings(MapColor.NONE, soundGroup, 1.0F).noOcclusion().requiresCorrectToolForDrops().lightLevel(state -> 15).pushReaction(PushReaction.DESTROY)), InkColors.YELLOW).withBlockModel((ctx, block) -> {
			ResourceLocation outer = outerSupplier.get();
			ResourceLocation base = SpectrumModelTemplates.SHIMMERSTONE_LIGHT.create(block, SpectrumTextureMaps.innerOuterParticle(SpectrumTextures.SHIMMERSTONE_LIGHT, outer, outer), ctx.modelOutput);
			ResourceLocation mirrored = SpectrumModelTemplates.SHIMMERSTONE_LIGHT_MIRRORED.createWithSuffix(block, "_mirrored", SpectrumTextureMaps.innerOuterParticle(SpectrumTextures.SHIMMERSTONE_LIGHT, outer, outer), ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createNorthDefaultFacingVariantMap()).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.INVERTED, mirrored, base));
		}));
	}
	
	public static final DeferredBlock<ShimmerstoneLightBlock> STONE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("stone_shimmerstone_light", SoundType.STONE, () -> SpectrumTextures.STONE_FLAT_LIGHT);
	public static final DeferredBlock<ShimmerstoneLightBlock> BASALT_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("basalt_shimmerstone_light", SoundType.BASALT, () -> SpectrumTextures.BASALT_FLAT_LIGHT);
	public static final DeferredBlock<ShimmerstoneLightBlock> CALCITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("calcite_shimmerstone_light", SoundType.CALCITE, () -> SpectrumTextures.CALCITE_FLAT_LIGHT);
	public static final DeferredBlock<ShimmerstoneLightBlock> DEEPSLATE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("deepslate_shimmerstone_light", SoundType.DEEPSLATE, () -> SpectrumTextures.DEEPSLATE_FLAT_LIGHT);
	public static final DeferredBlock<ShimmerstoneLightBlock> BLACKSLAG_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("blackslag_shimmerstone_light", SoundType.DEEPSLATE, () -> SpectrumTextures.BLACKSLAG_FLAT_LIGHT);
	public static final DeferredBlock<ShimmerstoneLightBlock> GRANITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("granite_shimmerstone_light", SoundType.STONE, () -> ModelLocationUtils.getModelLocation(POLISHED_GRANITE));
	public static final DeferredBlock<ShimmerstoneLightBlock> DIORITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("diorite_shimmerstone_light", SoundType.STONE, () -> ModelLocationUtils.getModelLocation(POLISHED_DIORITE));
	public static final DeferredBlock<ShimmerstoneLightBlock> ANDESITE_SHIMMERSTONE_LIGHT = registerShimmerstoneLight("andesite_shimmerstone_light", SoundType.STONE, () -> ModelLocationUtils.getModelLocation(POLISHED_ANDESITE));
	
	// CRYSTALLARIEUM
	private static BlockBehaviour.Properties crystallarieumGrowable(Block baseBlock) {
		return BlockBehaviour.Properties.ofFullCopy(baseBlock).strength(1.5F).noOcclusion().forceSolidOn().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY);
	}
	
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_COAL_BUD = register(cluster(blockWithItem("small_coal_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(COAL_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_COAL_BUD = register(cluster(blockWithItem("large_coal_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_COAL_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> COAL_CLUSTER = register(cluster(blockWithItem("coal_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_COAL_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_IRON_BUD = register(cluster(blockWithItem("small_iron_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(IRON_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_IRON_BUD = register(cluster(blockWithItem("large_iron_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_IRON_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> IRON_CLUSTER = register(cluster(blockWithItem("iron_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_IRON_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_GOLD_BUD = register(cluster(blockWithItem("small_gold_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(GOLD_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_GOLD_BUD = register(cluster(blockWithItem("large_gold_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GOLD_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> GOLD_CLUSTER = register(cluster(blockWithItem("gold_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GOLD_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_DIAMOND_BUD = register(cluster(blockWithItem("small_diamond_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(DIAMOND_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_DIAMOND_BUD = register(cluster(blockWithItem("large_diamond_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_DIAMOND_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> DIAMOND_CLUSTER = register(cluster(blockWithItem("diamond_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_DIAMOND_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_EMERALD_BUD = register(cluster(blockWithItem("small_emerald_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(EMERALD_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_EMERALD_BUD = register(cluster(blockWithItem("large_emerald_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_EMERALD_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> EMERALD_CLUSTER = register(cluster(blockWithItem("emerald_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_EMERALD_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_REDSTONE_BUD = register(cluster(blockWithItem("small_redstone_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(REDSTONE_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_REDSTONE_BUD = register(cluster(blockWithItem("large_redstone_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_REDSTONE_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> REDSTONE_CLUSTER = register(cluster(blockWithItem("redstone_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_REDSTONE_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.RED), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_LAPIS_BUD = register(cluster(blockWithItem("small_lapis_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(LAPIS_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.PURPLE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_LAPIS_BUD = register(cluster(blockWithItem("large_lapis_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_LAPIS_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.PURPLE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LAPIS_CLUSTER = register(cluster(blockWithItem("lapis_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_LAPIS_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.PURPLE), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_COPPER_BUD = register(cluster(blockWithItem("small_copper_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(COPPER_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_COPPER_BUD = register(cluster(blockWithItem("large_copper_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> COPPER_CLUSTER = register(cluster(blockWithItem("copper_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_QUARTZ_BUD = register(cluster(blockWithItem("small_quartz_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(QUARTZ_BLOCK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_QUARTZ_BUD = register(cluster(blockWithItem("large_quartz_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> QUARTZ_CLUSTER = register(cluster(blockWithItem("quartz_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_QUARTZ_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_NETHERITE_SCRAP_BUD = register(cluster(blockWithItem("small_netherite_scrap_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(ANCIENT_DEBRIS), SpectrumClusterBlock.GrowthStage.SMALL), () -> IS.of().fireResistant(), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_NETHERITE_SCRAP_BUD = register(cluster(blockWithItem("large_netherite_scrap_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_NETHERITE_SCRAP_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), () -> IS.of().fireResistant(), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> NETHERITE_SCRAP_CLUSTER = register(cluster(blockWithItem("netherite_scrap_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_NETHERITE_SCRAP_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), () -> IS.of().fireResistant(), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_ECHO_BUD = register(cluster(blockWithItem("small_echo_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(SCULK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_ECHO_BUD = register(cluster(blockWithItem("large_echo_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ECHO_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> ECHO_CLUSTER = register(cluster(blockWithItem("echo_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ECHO_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_GLOWSTONE_BUD = register(cluster(blockWithItem("small_glowstone_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(GLOWSTONE).lightLevel(state -> 4), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.YELLOW), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_GLOWSTONE_BUD = register(cluster(blockWithItem("large_glowstone_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOWSTONE_BUD.get()).lightLevel(state -> 8), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.YELLOW), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> GLOWSTONE_CLUSTER = register(cluster(blockWithItem("glowstone_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_GLOWSTONE_BUD.get()).lightLevel(state -> 14), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.YELLOW), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> SMALL_PRISMARINE_BUD = register(cluster(blockWithItem("small_prismarine_bud", () -> new SpectrumClusterBlock(crystallarieumGrowable(SCULK), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> LARGE_PRISMARINE_BUD = register(cluster(blockWithItem("large_prismarine_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_PRISMARINE_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	public static final DeferredBlock<SpectrumClusterBlock> PRISMARINE_CLUSTER = register(cluster(blockWithItem("prismarine_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_PRISMARINE_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.CYAN), SpectrumModelTemplates.CRYSTALLARIEUM_FARMABLE));
	
	public static final DeferredBlock<Block> PURE_COAL_BLOCK = register(simple(blockWithItem("pure_coal_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(COAL_BLOCK)), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_IRON_BLOCK = register(simple(blockWithItem("pure_iron_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(IRON_BLOCK)), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_GOLD_BLOCK = register(simple(blockWithItem("pure_gold_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(GOLD_BLOCK)), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_DIAMOND_BLOCK = register(simple(blockWithItem("pure_diamond_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(DIAMOND_BLOCK)), InkColors.CYAN)));
	public static final DeferredBlock<Block> PURE_EMERALD_BLOCK = register(simple(blockWithItem("pure_emerald_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(EMERALD_BLOCK)), InkColors.CYAN)));
	public static final DeferredBlock<Block> PURE_REDSTONE_BLOCK = register(simple(blockWithItem("pure_redstone_block", () -> new PureRedstoneBlock(BlockBehaviour.Properties.ofFullCopy(REDSTONE_BLOCK)), InkColors.RED)));
	public static final DeferredBlock<Block> PURE_LAPIS_BLOCK = register(simple(blockWithItem("pure_lapis_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(LAPIS_BLOCK)), InkColors.PURPLE)));
	public static final DeferredBlock<Block> PURE_COPPER_BLOCK = register(simple(blockWithItem("pure_copper_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(COPPER_BLOCK)), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_QUARTZ_BLOCK = register(simple(blockWithItem("pure_quartz_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(QUARTZ_BLOCK)), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_GLOWSTONE_BLOCK = register(simple(blockWithItem("pure_glowstone_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(GLOWSTONE)), InkColors.YELLOW)));
	public static final DeferredBlock<Block> PURE_PRISMARINE_BLOCK = register(simple(blockWithItem("pure_prismarine_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)), InkColors.CYAN)));
	public static final DeferredBlock<Block> PURE_NETHERITE_SCRAP_BLOCK = register(simple(blockWithItem("pure_netherite_scrap_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ANCIENT_DEBRIS)), () -> IS.of().fireResistant(), InkColors.BROWN)));
	public static final DeferredBlock<Block> PURE_ECHO_BLOCK = register(simple(blockWithItem("pure_echo_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(DIAMOND_BLOCK)), InkColors.BROWN)));
	
	private static BlockBehaviour.Properties preservationBlock() {
		return settings(MapColor.CLAY, SoundType.STONE, -1.0F, 3600000.0F).instrument(NoteBlockInstrument.BASEDRUM).noLootTable().isValidSpawn(SpectrumBlocks::never).forceSolidOn();
	}
	
	public static final DeferredBlock<PreservationControllerBlock> PRESERVATION_CONTROLLER = register(singletonWithSoup(blockWithItem("preservation_controller", () -> new PreservationControllerBlock(preservationBlock().lightLevel(state -> 1).emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always)), InkColors.BLUE), ModelLocationUtils::getModelLocation).withPredefinedItemModel());
	public static final DeferredBlock<DikeGateBlock> DIKE_GATE = register(simple(blockWithItem("dike_gate", () -> new DikeGateBlock(preservationBlock().lightLevel(state -> 3).sound(SoundType.GLASS).noOcclusion().emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always).isRedstoneConductor(SpectrumBlocks::never).isSuffocating(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never)), InkColors.BLUE)));
	public static final DeferredBlock<DreamGateBlock> DREAM_GATE = register(simple(blockWithItem("dream_gate", () -> new DreamGateBlock(preservationBlock().lightLevel(state -> 3).sound(SoundType.GLASS).noOcclusion().emissiveRendering(SpectrumBlocks::always).hasPostProcess(SpectrumBlocks::always).isRedstoneConductor(SpectrumBlocks::never).isSuffocating(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never)), InkColors.BLUE)));
	public static final DeferredBlock<InvisibleWallBlock> INVISIBLE_WALL = register(singleton(blockWithItem("invisible_wall", () -> new InvisibleWallBlock(preservationBlock().lightLevel(state -> 3).sound(SoundType.GLASS).noOcclusion().isViewBlocking(SpectrumBlocks::never)), InkColors.BLUE), SpectrumTexturedModelProviders.particle(b -> Blocks.GLASS, "")).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, ETHEREAL_PLATFORM.get())));
	public static final DeferredBlock<PreservationChestBlock> PRESERVATION_CHEST = register(singletonWithSoup(blockWithItem("preservation_chest", () -> new PreservationChestBlock(preservationBlock()), InkColors.BLUE), ModelLocationUtils::getModelLocation));
	
	public static final DeferredBlock<Block> DOWNSTONE = register(simple(blockWithItem("downstone", () -> new Block(preservationBlock()), InkColors.BLUE))); // "raw" preservation stone, used in the Deeper Down bottom in place of bedrock
	
	public static final DeferredBlock<Block> PRESERVATION_STONE = register(blockWithItem("preservation_stone", () -> new Block(preservationBlock()), InkColors.BLUE).withBlockModel((ctx, block) -> {
		List<ResourceLocation> modelIds = new ArrayList<>();
		int[] tops = new int[]{0, 3, 1, 1, 2, 2, 0, 3, 1, 2, 3};
		modelIds.add(SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "", b -> b, "_top_" + tops[0], b -> b, "_bottom").create(block, ctx.modelOutput));
		for (int i = 1; i <= 10; i++) modelIds.add(SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "_" + i, b -> b, "_top_" + tops[i], b -> b, "_bottom").createWithSuffix(block, "_" + i, ctx.modelOutput));
		List<Variant> variants = new ArrayList<>();
		for (VariantProperties.Rotation rotation : VariantProperties.Rotation.values()) {
			variants.add(createModelVariant(modelIds.getFirst()).with(VariantProperties.WEIGHT, 10));
			if (rotation != VariantProperties.Rotation.R0) variants.getLast().with(VariantProperties.Y_ROT, rotation);
			for (int i = 1; i <= 10; i++) {
				variants.add(createModelVariant(modelIds.get(i)));
				if (rotation != VariantProperties.Rotation.R0) variants.getLast().with(VariantProperties.Y_ROT, rotation);
			}
		}
		return MultiVariantGenerator.multiVariant(block, variants.toArray(Variant[]::new));
	}));
	public static final DeferredBlock<Block> PRESERVATION_STAIRS = register(blockWithItem("preservation_stairs", () -> new StairBlock(PRESERVATION_STONE.get().defaultBlockState(), preservationBlock()), InkColors.BLUE));
	public static final DeferredBlock<Block> PRESERVATION_SLAB = register(blockWithItem("preservation_slab", () -> new SlabBlock(preservationBlock()), InkColors.BLUE));
	public static final DeferredBlock<Block> PRESERVATION_WALL = register(blockWithItem("preservation_wall", () -> new WallBlock(preservationBlock()), InkColors.BLUE));
	
	public static final DeferredBlock<Block> POWDER_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("powder_chiseled_preservation_stone", () -> new Block(preservationBlock().lightLevel(state -> 2)), InkColors.BLUE), SpectrumTexturedModelProviders.cubeColumn(b -> b, "", b -> PRESERVATION_STONE.get(), "_top_generic")));
	public static final DeferredBlock<Block> DIKE_CHISELED_PRESERVATION_STONE = register(simple(blockWithItem("dike_chiseled_preservation_stone", () -> new Block(preservationBlock().lightLevel(state -> 6)), InkColors.BLUE)));
	public static final DeferredBlock<Block> DREAM_CHISELED_PRESERVATION_STONE = register(simple(blockWithItem("dream_chiseled_preservation_stone", () -> new Block(preservationBlock().lightLevel(state -> 6)), InkColors.BLUE)));
	public static final DeferredBlock<Block> DEEP_LIGHT_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("deep_light_chiseled_preservation_stone", () -> new DeepLightBlock(preservationBlock().lightLevel(state -> 2)), InkColors.BLUE), SpectrumTexturedModelProviders.cubeColumn(b -> b, "", b -> PRESERVATION_STONE.get(), "_top_generic")));
	
	public static final DeferredBlock<Block> PRESERVATION_ITEM_BOWL = register(singleton(blockWithItem("preservation_item_bowl", () -> new PreservationItemBowlBlock(preservationBlock().noOcclusion().isRedstoneConductor(SpectrumBlocks::never).isSuffocating(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never)), InkColors.BLUE), TexturedModel.createDefault(b -> new TextureMapping().put(TextureSlot.TEXTURE, SpectrumCommon.locate("block/preservation_item_bowl")).put(TextureSlot.SIDE, SpectrumCommon.locate("block/preservation_bricks")).put(SpectrumTextureSlots.BASE, SpectrumCommon.locate("block/preservation_stone_top_0")), SpectrumModelTemplates.BOWL)));
	public static final DeferredBlock<Block> DIKE_GATE_FOUNTAIN = register(defaultUpFacing(blockWithItem("dike_gate_fountain", () -> new SpectrumFacingBlock(preservationBlock()), InkColors.BLUE), SpectrumTexturedModelProviders.cubeBottomTopParticle(b -> b, "_side", b -> b, "_top", b -> PRESERVATION_STONE.get(), "", b -> PRESERVATION_STONE.get(), "")));
	public static final DeferredBlock<Block> PRESERVATION_BRICKS = register(simple(blockWithItem("preservation_bricks", () -> new Block(preservationBlock()), InkColors.BLUE)));
	public static final DeferredBlock<Block> SHIMMERING_PRESERVATION_BRICKS = register(blockWithItem("shimmering_preservation_bricks", () -> new Block(preservationBlock().lightLevel(s -> 5)), InkColors.BLUE).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, createModelVariant(TexturedModel.CUBE.create(block, ctx.modelOutput)), createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_fast").createWithSuffix(block, "_fast", ctx.modelOutput)), createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_slow").createWithSuffix(block, "_slow", ctx.modelOutput)))));
	
	public static final DeferredBlock<StatueBlock> COURIER_STATUE = register(blockWithItem("courier_statue", () -> new StatueBlock(preservationBlock()), InkColors.BLUE).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_top")).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createNorthDefaultHorizontalFacingVariantMap()).with(PropertyDispatch.property(StatueBlock.HALF).select(DoubleBlockHalf.LOWER, SpectrumModelHelper.createModelVariant(block, "_bottom")).select(DoubleBlockHalf.UPPER, SpectrumModelHelper.createModelVariant(block, "_top")))));
	public static final DeferredBlock<ManxiBlock> MANXI = register(singletonWithSoup(block("manxi", () -> new ManxiBlock(preservationBlock().noOcclusion().noCollission().noLootTable())), b -> SpectrumModelTemplates.MOB_HEAD));
	
	public static final DeferredBlock<Block> BLACK_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("black_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.BLACK), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BLUE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("blue_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.BLUE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> BROWN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("brown_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.BROWN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> CYAN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("cyan_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.CYAN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> GRAY_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("gray_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.GRAY), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> GREEN_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("green_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.GREEN), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> LIGHT_BLUE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("light_blue_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.LIGHT_BLUE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> LIGHT_GRAY_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("light_gray_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.LIGHT_GRAY), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> LIME_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("lime_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.LIME), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> MAGENTA_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("magenta_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.MAGENTA), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> ORANGE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("orange_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.ORANGE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> PINK_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("pink_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.PINK), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> PURPLE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("purple_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.PURPLE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> RED_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("red_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.RED), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> WHITE_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("white_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.WHITE), TexturedModel.COLUMN_ALT));
	public static final DeferredBlock<Block> YELLOW_CHISELED_PRESERVATION_STONE = register(singleton(blockWithItem("yellow_chiseled_preservation_stone", () -> new Block(preservationBlock()), InkColors.YELLOW), TexturedModel.COLUMN_ALT));
	
	public static final DeferredBlock<Block> PRESERVATION_GLASS = register(simple(blockWithItem("preservation_glass", () -> new TransparentBlock(preservationBlock().sound(SoundType.GLASS).noOcclusion().isRedstoneConductor(SpectrumBlocks::never).isSuffocating(SpectrumBlocks::never).isViewBlocking(SpectrumBlocks::never)), InkColors.BLUE)));
	public static final DeferredBlock<Block> TINTED_PRESERVATION_GLASS = register(simple(blockWithItem("tinted_preservation_glass", () -> new TintedGlassBlock(BlockBehaviour.Properties.ofFullCopy(PRESERVATION_GLASS.get())), InkColors.BLUE)));
	public static final DeferredBlock<Block> PRESERVATION_ROUNDEL = register(singleton(blockWithItem("preservation_roundel", () -> new PreservationRoundelBlock(preservationBlock().noOcclusion().forceSolidOn()), InkColors.BLUE), SpectrumTexturedModelProviders.ROUNDEL));
	public static final DeferredBlock<PreservationBlockDetectorBlock> PRESERVATION_BLOCK_DETECTOR = register(blockWithItem("preservation_block_detector", () -> new PreservationBlockDetectorBlock(preservationBlock()), InkColors.BLUE).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block, SpectrumModelHelper.createModelVariant(SpectrumTexturedModelProviders.complexOrientable(b -> b, "_side", b -> b, "_top", b -> PRESERVATION_STONE.get(), "_top_generic", b -> b, "_front", b -> b, "_back", b -> b, "_side").create(block, ctx.modelOutput))).with(SpectrumModelHelper.createNorthDefaultFacingVariantMap())));
	
	private static BlockBehaviour.Properties shootingStar() {
		return BlockBehaviour.Properties.ofFullCopy(STONE).noOcclusion();
	}
	
	public static final DeferredBlock<ShootingStarBlock> GLISTERING_SHOOTING_STAR = register(singleton(blockWithItem("glistering_shooting_star", () -> new ShootingStarBlock(shootingStar(), ShootingStar.Variant.GLISTERING), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModelProviders.SHOOTING_STAR));
	public static final DeferredBlock<ShootingStarBlock> FIERY_SHOOTING_STAR = register(singleton(blockWithItem("fiery_shooting_star", () -> new ShootingStarBlock(shootingStar(), ShootingStar.Variant.FIERY), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModelProviders.SHOOTING_STAR));
	public static final DeferredBlock<ShootingStarBlock> COLORFUL_SHOOTING_STAR = register(singleton(blockWithItem("colorful_shooting_star", () -> new ShootingStarBlock(shootingStar(), ShootingStar.Variant.COLORFUL), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModelProviders.SHOOTING_STAR));
	public static final DeferredBlock<ShootingStarBlock> PRISTINE_SHOOTING_STAR = register(singleton(blockWithItem("pristine_shooting_star", () -> new ShootingStarBlock(shootingStar(), ShootingStar.Variant.PRISTINE), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModelProviders.SHOOTING_STAR));
	public static final DeferredBlock<ShootingStarBlock> GEMSTONE_SHOOTING_STAR = register(singleton(blockWithItem("gemstone_shooting_star", () -> new ShootingStarBlock(shootingStar(), ShootingStar.Variant.GEMSTONE), block -> new ShootingStarItem(block, IS.of(1, Rarity.UNCOMMON)), InkColors.PURPLE), SpectrumTexturedModelProviders.SHOOTING_STAR));
	public static final DeferredBlock<Block> STARDUST_BLOCK = register(blockWithItem("stardust_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.PURPLE.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).mapColor(MapColor.COLOR_PURPLE)), () -> IS.of(Rarity.UNCOMMON), InkColors.BLACK));
	
	public static final DeferredBlock<IncandescentAmalgamBlock> INCANDESCENT_AMALGAM = register(singletonWithSoup(blockWithItem("incandescent_amalgam", () -> new IncandescentAmalgamBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion()), block -> new IncandescentAmalgamItem(block, IS.of().food(SpectrumFoodComponents.INCANDESCENT_AMALGAM)), InkColors.RED), ModelLocationUtils::getModelLocation).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel));
	
	private static BlockBehaviour.Properties idol(SoundType soundGroup) {
		return settings(MapColor.TERRACOTTA_WHITE, soundGroup, 3.0F).requiresCorrectToolForDrops().noOcclusion();
	}
	
	public static final DeferredBlock<Block> AXOLOTL_IDOL = register(idol(blockWithItem("axolotl_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.AXOLOTL_IDOL), ParticleTypes.HEART, MobEffects.REGENERATION, 0, 100), InkColors.PINK))); // heals 2 hp / 1 hear
	public static final DeferredBlock<Block> BAT_IDOL = register(idol(blockWithItem("bat_idol", () -> new AoEStatusEffectIdolBlock(idol(SpectrumSoundTypes.BAT_IDOL), ParticleTypes.INSTANT_EFFECT, MobEffects.GLOWING, 0, 200, 8), InkColors.PINK)));
	public static final DeferredBlock<Block> BEE_IDOL = register(idol(blockWithItem("bee_idol", () -> new BonemealingIdolBlock(idol(SpectrumSoundTypes.BEE_IDOL), ParticleTypes.DRIPPING_HONEY), InkColors.PINK)));
	public static final DeferredBlock<Block> BLAZE_IDOL = register(idol(blockWithItem("blaze_idol", () -> new FirestarterIdolBlock(idol(SpectrumSoundTypes.BLAZE_IDOL), ParticleTypes.FLAME), InkColors.PINK)));
	public static final DeferredBlock<Block> CAT_IDOL = register(idol(blockWithItem("cat_idol", () -> new FallDamageNegatingIdolBlock(idol(SpectrumSoundTypes.CAT_IDOL), ParticleTypes.ENCHANTED_HIT), InkColors.PINK)));
	public static final DeferredBlock<Block> CHICKEN_IDOL = register(idol(blockWithItem("chicken_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.CHICKEN_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.SLOW_FALLING, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> COW_IDOL = register(idol(blockWithItem("cow_idol", () -> new MilkingIdolBlock(idol(SpectrumSoundTypes.COW_IDOL), ParticleTypes.ENCHANTED_HIT, 6), InkColors.PINK)));
	public static final DeferredBlock<Block> CREEPER_IDOL = register(idol(blockWithItem("creeper_idol", () -> new ExplosionIdolBlock(idol(SpectrumSoundTypes.CREEPER_IDOL), ParticleTypes.EXPLOSION, 3, false, Explosion.BlockInteraction.DESTROY), InkColors.PINK)));
	public static final DeferredBlock<Block> ENDER_DRAGON_IDOL = register(idol(blockWithItem("ender_dragon_idol", () -> new ProjectileIdolBlock(idol(SpectrumSoundTypes.ENDER_DRAGON_IDOL), ParticleTypes.DRAGON_BREATH, EntityType.DRAGON_FIREBALL, SoundEvents.ENDER_DRAGON_SHOOT, 6.0F, 1.1F) {
		@Override
		public Projectile createProjectile(ServerLevel world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER.get(), world);
			markerEntity.setPosRaw(position.x(), position.y(), position.z());
			
			Vec3 targetPosition = Vec3.atCenterOf(mobBlockPos.relative(side, 50));
			var velocity = targetPosition.subtract(markerEntity.position());
			
			DragonFireball entity = new DragonFireball(world, markerEntity, velocity);
			
			markerEntity.discard();
			return entity;
		}
	}, InkColors.PINK)));
	public static final DeferredBlock<Block> ENDERMAN_IDOL = register(idol(blockWithItem("enderman_idol", () -> new RandomTeleportingIdolBlock(idol(SpectrumSoundTypes.ENDERMAN_IDOL), ParticleTypes.REVERSE_PORTAL, 16, 16), InkColors.PINK)));
	public static final DeferredBlock<Block> ENDERMITE_IDOL = register(idol(blockWithItem("endermite_idol", () -> new LineTeleportingIdolBlock(idol(SpectrumSoundTypes.ENDERMITE_IDOL), ParticleTypes.REVERSE_PORTAL, 16), InkColors.PINK)));
	public static final DeferredBlock<Block> EVOKER_IDOL = register(idol(blockWithItem("evoker_idol", () -> new EntitySummoningIdolBlock(idol(SpectrumSoundTypes.EVOKER_IDOL), ParticleTypes.ANGRY_VILLAGER, EntityType.VEX) {
		@Override
		public void afterSummon(ServerLevel world, Entity entity) {
			((Vex) entity).setLimitedLife(20 * (30 + world.getRandom().nextInt(90)));
		}
	}, InkColors.PINK)));
	public static final DeferredBlock<Block> FISH_IDOL = register(idol(blockWithItem("fish_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.FISH_IDOL), ParticleTypes.SPLASH, MobEffects.WATER_BREATHING, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> FOX_IDOL = register(idol(blockWithItem("fox_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.FOX_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.DIG_SPEED, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> GHAST_IDOL = register(idol(blockWithItem("ghast_idol", () -> new ProjectileIdolBlock(idol(SpectrumSoundTypes.GHAST_IDOL), ParticleTypes.SMOKE, EntityType.FIREBALL, SoundEvents.GHAST_SHOOT, 6.0F, 1.1F) {
		@Override
		public Projectile createProjectile(ServerLevel world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER.get(), world);
			markerEntity.setPosRaw(position.x(), position.y(), position.z());
			
			Vec3 targetPosition = Vec3.atCenterOf(mobBlockPos.relative(side, 50));
			var velocity = targetPosition.subtract(markerEntity.position());
			
			LargeFireball entity = new LargeFireball(world, markerEntity, velocity, 1);
			
			markerEntity.discard();
			return entity;
		}
	}, InkColors.PINK)));
	public static final DeferredBlock<Block> GLOW_SQUID_IDOL = register(idol(blockWithItem("glow_squid_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.GLOW_SQUID_IDOL), ParticleTypes.GLOW_SQUID_INK, MobEffects.GLOWING, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> GOAT_IDOL = register(idol(blockWithItem("goat_idol", () -> new KnockbackIdolBlock(idol(SpectrumSoundTypes.GOAT_IDOL), ParticleTypes.ENCHANTED_HIT, 5.0F, 0.5F), InkColors.PINK))); // knocks mostly sideways
	public static final DeferredBlock<Block> GUARDIAN_IDOL = register(idol(blockWithItem("guardian_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.GUARDIAN_IDOL), ParticleTypes.BUBBLE, MobEffects.DIG_SLOWDOWN, 2, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> HORSE_IDOL = register(idol(blockWithItem("horse_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.HORSE_IDOL), ParticleTypes.INSTANT_EFFECT, MobEffects.DAMAGE_BOOST, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> ILLUSIONER_IDOL = register(idol(blockWithItem("illusioner_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.ILLUSIONER_IDOL), ParticleTypes.ANGRY_VILLAGER, MobEffects.INVISIBILITY, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> OCELOT_IDOL = register(idol(blockWithItem("ocelot_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.OCELOT_IDOL), ParticleTypes.INSTANT_EFFECT, MobEffects.NIGHT_VISION, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> PARROT_IDOL = register(idol(blockWithItem("parrot_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.PARROT_IDOL), ParticleTypes.INSTANT_EFFECT, MobEffects.ABSORPTION, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> PHANTOM_IDOL = register(idol(blockWithItem("phantom_idol", () -> new InsomniaIdolBlock(idol(SpectrumSoundTypes.PHANTOM_IDOL), ParticleTypes.POOF, 24000), InkColors.PINK))); // +1 ingame day without sleep
	public static final DeferredBlock<Block> PIG_IDOL = register(idol(blockWithItem("pig_idol", () -> new FeedingIdolBlock(idol(SpectrumSoundTypes.PIG_IDOL), ParticleTypes.INSTANT_EFFECT, 6), InkColors.PINK)));
	public static final DeferredBlock<Block> PIGLIN_IDOL = register(idol(blockWithItem("piglin_idol", () -> new PiglinTradeIdolBlock(idol(SpectrumSoundTypes.PIGLIN_IDOL), ParticleTypes.HEART), InkColors.PINK)));
	public static final DeferredBlock<Block> POLAR_BEAR_IDOL = register(idol(blockWithItem("polar_bear_idol", () -> new FreezingIdolBlock(idol(SpectrumSoundTypes.POLAR_BEAR_IDOL), ParticleTypes.SNOWFLAKE), InkColors.PINK)));
	public static final DeferredBlock<Block> PUFFERFISH_IDOL = register(idol(blockWithItem("pufferfish_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.PUFFERFISH_IDOL), ParticleTypes.SPLASH, MobEffects.CONFUSION, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> RABBIT_IDOL = register(idol(blockWithItem("rabbit_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.RABBIT_IDOL), ParticleTypes.INSTANT_EFFECT, MobEffects.JUMP, 3, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> SHEEP_IDOL = register(idol(blockWithItem("sheep_idol", () -> new ShearingIdolBlock(idol(SpectrumSoundTypes.SHEEP_IDOL), ParticleTypes.ENCHANTED_HIT, 6), InkColors.PINK)));
	public static final DeferredBlock<Block> SHULKER_IDOL = register(idol(blockWithItem("shulker_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.SHULKER_IDOL), ParticleTypes.END_ROD, MobEffects.LEVITATION, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> SILVERFISH_IDOL = register(idol(blockWithItem("silverfish_idol", () -> new SilverfishInsertingIdolBlock(idol(SpectrumSoundTypes.SILVERFISH_IDOL), ParticleTypes.EXPLOSION), InkColors.PINK)));
	public static final DeferredBlock<Block> SKELETON_IDOL = register(idol(blockWithItem("skeleton_idol", () -> new ProjectileIdolBlock(idol(SpectrumSoundTypes.SKELETON_IDOL), ParticleTypes.INSTANT_EFFECT, EntityType.ARROW, SoundEvents.ARROW_SHOOT, 6.0F, 1.1F) {
		@Override
		public Projectile createProjectile(ServerLevel world, BlockPos mobBlockPos, Position position, Direction side) {
			Arrow arrowEntity = new Arrow(world, position.x(), position.y(), position.z(), ItemStack.EMPTY, null);
			arrowEntity.pickup = AbstractArrow.Pickup.DISALLOWED;
			return arrowEntity;
		}
	}, InkColors.PINK)));
	public static final DeferredBlock<Block> SLIME_IDOL = register(idol(blockWithItem("slime_idol", () -> new SlimeSizingIdolBlock(idol(SpectrumSoundTypes.SLIME_IDOL), ParticleTypes.ITEM_SLIME, 6, 8), InkColors.PINK)));
	public static final DeferredBlock<Block> SNOW_GOLEM_IDOL = register(idol(blockWithItem("snow_golem_idol", () -> new ProjectileIdolBlock(idol(SpectrumSoundTypes.SNOW_GOLEM_IDOL), ParticleTypes.SNOWFLAKE, EntityType.SNOWBALL, SoundEvents.ARROW_SHOOT, 3.0F, 1.1F) {
		@Override
		public Projectile createProjectile(ServerLevel world, BlockPos mobBlockPos, Position position, Direction side) {
			world.playSound(null, mobBlockPos.getX(), mobBlockPos.getY(), mobBlockPos.getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.BLOCKS, 1.0F, 0.4F / world.getRandom().nextFloat() * 0.4F + 0.8F);
			return new Snowball(world, position.x(), position.y(), position.z());
		}
	}, InkColors.PINK)));
	public static final DeferredBlock<Block> SPIDER_IDOL = register(idol(blockWithItem("spider_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.SPIDER_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.POISON, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> SQUID_IDOL = register(idol(blockWithItem("squid_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.SQUID_IDOL), ParticleTypes.SQUID_INK, MobEffects.BLINDNESS, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> STRAY_IDOL = register(idol(blockWithItem("stray_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.STRAY_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.MOVEMENT_SLOWDOWN, 2, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> STRIDER_IDOL = register(idol(blockWithItem("strider_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.STRIDER_IDOL), ParticleTypes.DRIPPING_LAVA, MobEffects.FIRE_RESISTANCE, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> TURTLE_IDOL = register(idol(blockWithItem("turtle_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.TURTLE_IDOL), ParticleTypes.DRIPPING_WATER, MobEffects.DAMAGE_RESISTANCE, 1, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> WITCH_IDOL = register(idol(blockWithItem("witch_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.WITCH_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.WEAKNESS, 0, 200), InkColors.PINK)));
	public static final DeferredBlock<Block> WITHER_IDOL = register(idol(blockWithItem("wither_idol", () -> new ExplosionIdolBlock(idol(SpectrumSoundTypes.WITHER_IDOL), ParticleTypes.EXPLOSION, 7.0F, true, Explosion.BlockInteraction.DESTROY), InkColors.PINK)));
	public static final DeferredBlock<Block> WITHER_SKELETON_IDOL = register(idol(blockWithItem("wither_skeleton_idol", () -> new StatusEffectIdolBlock(idol(SpectrumSoundTypes.WITHER_SKELETON_IDOL), ParticleTypes.ENCHANTED_HIT, MobEffects.WITHER, 0, 100), InkColors.PINK)));
	public static final DeferredBlock<Block> ZOMBIE_IDOL = register(idol(blockWithItem("zombie_idol", () -> new VillagerConvertingIdolBlock(idol(SpectrumSoundTypes.ZOMBIE_IDOL), ParticleTypes.ENCHANTED_HIT), InkColors.PINK)));
	
	// FLUIDS
	private static BlockBehaviour.Properties fluid(MapColor mapColor) {
		return settings(mapColor, SoundType.EMPTY, 100.0F).replaceable().noCollission().pushReaction(PushReaction.DESTROY).noLootTable().liquid();
	}
	
	public static final DeferredBlock<Block> LIQUID_CRYSTAL = register(singleton(block("liquid_crystal", () -> new LiquidCrystalFluidBlock(SpectrumFluids.LIQUID_CRYSTAL.get(), BLAZING_CRYSTAL.get().defaultBlockState(), fluid(MapColor.CRIMSON_STEM).lightLevel((state) -> LiquidCrystalFluidBlock.LUMINANCE).replaceable())), SpectrumTexturedModelProviders.particle(b -> b, "_still")));
	public static final DeferredBlock<Block> SLUDGE = register(singleton(block("sludge", () -> new SludgeFluidBlock(SpectrumFluids.SLUDGE.get(), MUD.defaultBlockState(), fluid(MapColor.TERRACOTTA_BROWN).replaceable())), SpectrumTexturedModelProviders.particle(b -> b, "_still")));
	public static final DeferredBlock<Block> MIDNIGHT_SOLUTION = register(singleton(block("midnight_solution", () -> new MidnightSolutionFluidBlock(SpectrumFluids.MIDNIGHT_SOLUTION.get(), BLACK_MATERIA.get().defaultBlockState(), fluid(MapColor.WARPED_STEM).replaceable())), SpectrumTexturedModelProviders.particle(b -> b, "_still")));
	public static final DeferredBlock<Block> DRAGONROT = register(singleton(block("dragonrot", () -> new DragonrotFluidBlock(SpectrumFluids.DRAGONROT.get(), BLACKSTONE.defaultBlockState(), fluid(MapColor.ICE).lightLevel((state) -> 15).replaceable())), SpectrumTexturedModelProviders.particle(b -> b, "_still")));
	
	public static final DeferredBlock<Block> TOPAZ_BRICKS = register(simple(blockWithItem("topaz_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_TOPAZ.get())), InkColors.CYAN)));
	public static final DeferredBlock<Block> AMETHYST_BRICKS = register(simple(blockWithItem("amethyst_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_AMETHYST.get())), InkColors.MAGENTA)));
	public static final DeferredBlock<Block> CITRINE_BRICKS = register(simple(blockWithItem("citrine_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_CITRINE.get())), InkColors.YELLOW)));
	public static final DeferredBlock<Block> ONYX_BRICKS = register(simple(blockWithItem("onyx_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_ONYX.get())), InkColors.BLACK)));
	public static final DeferredBlock<Block> MOONSTONE_BRICKS = register(simple(blockWithItem("moonstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_MOONSTONE.get())), InkColors.WHITE)));
	public static final DeferredBlock<Block> MIXED_GEMSTONE_BRICKS = register(simple(blockWithItem("mixed_gemstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_AMETHYST.get())), InkColors.MAGENTA)));
	
	public static final DeferredBlock<Block> AZURITE_BRICKS = register(simple(blockWithItem("azurite_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(AZURITE_BLOCK.get())), InkColors.BLUE)));
	public static final DeferredBlock<Block> MALACHITE_BRICKS = register(simple(blockWithItem("malachite_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(MALACHITE_BLOCK.get())), InkColors.GREEN)));
	public static final DeferredBlock<Block> BLOODSTONE_BRICKS = register(simple(blockWithItem("bloodstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(BLOODSTONE_BLOCK.get())), InkColors.RED)));
	public static final DeferredBlock<Block> MIXED_REFINED_CRYSTAL_BRICKS = register(simple(blockWithItem("mixed_refined_crystal_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(AZURITE_BLOCK.get())), InkColors.BLUE)));
	
	static boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
		return false;
	}
	
	static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
	static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
		return false;
	}
	
	public static void register(IEventBus eventBus) {
		for (SpectrumSkullType type : SpectrumSkullType.values()) {
			BlockRegistrar<SpectrumSkullBlock> head = block(type.getSerializedName() + "_head", () -> new SpectrumSkullBlock(type, BlockBehaviour.Properties.ofFullCopy(SKELETON_SKULL).instrument(NoteBlockInstrument.CUSTOM_HEAD))).withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, SpectrumModelTemplates.SKULL_ITEM)).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumModelTemplates.MOB_HEAD));
			DeferredBlock<SpectrumWallSkullBlock> wallHead = register(block(type.getSerializedName() + "_wall_head", () -> new SpectrumWallSkullBlock(type, BlockBehaviour.Properties.ofFullCopy(SKELETON_SKULL).dropsLike(head.holder.get()))).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumModelTemplates.MOB_HEAD)));
			register(head.withItem(block -> new SpectrumSkullBlockItem(block, wallHead.get(), IS.of(), type)));
		}
		
		REGISTRAR.register(eventBus);
	}
	
	public static<T extends Block> DeferredBlock<T> register(BlockRegistrar<T> registrar) {
		if(registrar.hasItem) {
			SpectrumItems.REGISTRAR.register(registrar.id.getPath(), () -> registrar.callback.apply(registrar.holder.get()));
		}
		return Objects.requireNonNull(registrar.holder(), "Attempted to register a null block");
	}
	
	public static <T extends Block> BlockRegistrar<T> block(String name, Supplier<T> blockFactory) {
		return new BlockRegistrar<T>(name).withBlock(blockFactory);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, Supplier<T> block, InkColor color) {
		return blockWithItem(name, block, () -> IS.of(), color);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, Supplier<T> block, Supplier<Item.Properties> itemProperties, InkColor color) {
		return blockWithItem(name, block, b -> new BlockItem(b, itemProperties.get()), color);
	}
	
	public static <T extends Block> BlockRegistrar<T> blockWithItem(String name, Supplier<T> block, Function<T, Item> itemFactory, InkColor color) {
		return block(name, block).withItem(itemFactory);
	}
	
	public static <T extends Block> BlockRegistrar<T> simple(BlockRegistrar<T> registrar) {
		return singleton(registrar, TexturedModel.CUBE);
	}
	
	public static <T extends Block> BlockRegistrar<T> simpleMirrored(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createMirroredVariantsSupplier(block, TexturedModel.CUBE, TexturedModel.CUBE_MIRRORED, ctx.modelOutput));
	}
	
	public static <T extends Block> BlockRegistrar<T> singleton(BlockRegistrar<T> registrar, TexturedModel.Provider factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory));
	}
	
	public static <T extends Block> BlockRegistrar<T> singletonWithSoup(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdSupplier) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdSupplier.apply(block)));
	}
	
	public static <T extends Block> BlockRegistrar<T> parented(BlockRegistrar<T> registrar, UnaryOperator<Block> parent) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, parent.apply(block))).withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, ModelLocationUtils.getModelLocation(parent.apply(block))));
	}
	
	public static <T extends Block> BlockRegistrar<T> axisRotated(BlockRegistrar<T> registrar, TexturedModel.Provider factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory).with(SpectrumModelHelper.createAxisRotatedVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultUpFacing(BlockRegistrar<T> registrar, TexturedModel.Provider factory) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(ctx, block, factory).with(SpectrumModelHelper.createUpDefaultFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultUpFacingGetter(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).with(SpectrumModelHelper.createUpDefaultFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultNorthHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).with(SpectrumModelHelper.createNorthDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultSouthHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).with(SpectrumModelHelper.createSouthDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultWestHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).with(SpectrumModelHelper.createWestDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> defaultEastHorizontalFacing(BlockRegistrar<T> registrar, Function<Block, ResourceLocation> modelIdGetter) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, modelIdGetter.apply(block)).with(SpectrumModelHelper.createEastDefaultHorizontalFacingVariantMap()));
	}
	
	public static <T extends Block> BlockRegistrar<T> cross(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.createVariantsSupplier(block, SpectrumTexturedModelProviders.cross(b -> b, "").create(block, ctx.modelOutput)));
	}
	
	public static <T extends Block> BlockRegistrar<T> simplePlant(BlockRegistrar<T> registrar) {
		return cross(registrar).withBlockItemModel(SpectrumModelHelper::registerBlockTexturedItemModel);
	}
	
	public static <T extends FlowerPotBlock> BlockRegistrar<T> pottedPlant(BlockRegistrar<T> registrar, boolean tinted) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.pottedPlantBlockModel(ctx, (FlowerPotBlock) block, tinted));
	}
	
	public static <T extends FlowerPotBlock> BlockRegistrar<T> pottedPlantWithCustomTexture(BlockRegistrar<T> registrar, String suffix) {
		return registrar.withBlockModel((ctx, block) -> SpectrumModelHelper.pottedPlantWithCustomTextureBlockModel(ctx, (FlowerPotBlock) block, suffix));
	}
	
	public static <T extends Block> BlockRegistrar<T> log(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel(SpectrumModelHelper::logBlockModel);
	}
	
	
	public static <T extends Block> BlockRegistrar<T> snowy(BlockRegistrar<T> registrar, TexturedModel.Provider base, TexturedModel.Provider snowy) {
		return registrar.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.SNOWY).select(false, SpectrumModelHelper.createHorizontalRotationVariantList(base.create(block, ctx.modelOutput))).select(true, SpectrumModelHelper.createHorizontalRotationVariantList(snowy.createWithSuffix(block, "_snow", ctx.modelOutput)))));
	}
	
	public static <T extends Block> BlockRegistrar<T> redstoneLamp(BlockRegistrar<T> registrar) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_off")).withBlockModel((ctx, block) -> {
			ResourceLocation off = SpectrumTexturedModelProviders.cubeAll(b -> b, "_off").createWithSuffix(block, "_off", ctx.modelOutput);
			ResourceLocation on = SpectrumTexturedModelProviders.cubeAll(b -> b, "_on").createWithSuffix(block, "_on", ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.LIT, on, off));
		});
	}
	
	public static <T extends Block> BlockRegistrar<T> powered(BlockRegistrar<T> registrar) {
		return registrar
				.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block))
				.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(createPoweredMap(BlockStateProperties.POWER, ctx, block)));
	}
	
	
	public static PropertyDispatch createPoweredMap(IntegerProperty property, BlockModelGenerators ctx, Block block) {
		var v = PropertyDispatch.property(property);
		v.select(0, createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "").create(block, ctx.modelOutput)));
		for (int i = 1; i < 16; i++) {
			v.select(i, createModelVariant(SpectrumTexturedModelProviders.cubeAll(b -> b, "_" + i).createWithSuffix(block, "_" + i, ctx.modelOutput)));
		}
		return v;
	}
	
	public static <T extends Block> BlockRegistrar<T> barrellike(BlockRegistrar<T> registrar, UnaryOperator<Block> bottomBlock, String bottomSuffix) {
		return registrar.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createUpDefaultFacingVariantMap()).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.OPEN, SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "_side", b -> b, "_top_open", bottomBlock, bottomSuffix).createWithSuffix(block, "_open", ctx.modelOutput), SpectrumTexturedModelProviders.cubeBottomTop(b -> b, "_side", b -> b, "_top", bottomBlock, bottomSuffix).create(block, ctx.modelOutput))));
	}
	
	public static <T extends Block> BlockRegistrar<T> spiritVines(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(SpiritVine.CRYSTALS, SpectrumTexturedModelProviders.cross(b -> b, "_crystals").createWithSuffix(block, "_crystals", ctx.modelOutput), SpectrumTexturedModelProviders.cross(b -> b, "_none").createWithSuffix(block, "_none", ctx.modelOutput))));
	}
	
	public static <T extends Block> BlockRegistrar<T> idol(BlockRegistrar<T> registrar) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, SpectrumModelTemplates.MOB_BLOCK)).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(IdolBlock.COOLDOWN, SpectrumModelTemplates.MOB_BLOCK, SpectrumModelTemplates.MOB_BLOCK_COOLDOWN)));
	}
	
	public static <T extends Block> BlockRegistrar<T> pedestal(BlockRegistrar<T> registrar) {
		return singleton(registrar, TexturedModel.createDefault(b -> new TextureMapping().put(SpectrumTextureSlots.PEDESTAL, TextureMapping.getBlockTexture(b)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(b, "_breaking")), SpectrumModelTemplates.PEDESTAL));
	}
	
	public static <T extends Block> BlockRegistrar<T> sugarStick(BlockRegistrar<T> registrar, UnaryOperator<Block> sugarBlock) {
		return registrar.withItemModel(SpectrumModelHelper::registerItemModel).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_2).generate(age -> createModelVariant(SpectrumTexturedModelProviders.sugarStick(age, sugarBlock).createWithSuffix(block, age.toString(), ctx.modelOutput)))));
	}
	
	public static <T extends Block> BlockRegistrar<T> detector(BlockRegistrar<T> registrar) {
		return registrar; /* burnable(registrar, 300).withBlockModel((ctx, block) -> MultiVariantGenerator.multiVariant(block).with(SpectrumModelHelper.createBooleanModelMap(BlockStateProperties.INVERTED,
				SpectrumModels.SLAB_DETECTOR.createWithSuffix(block, "_inverted", SpectrumTextureMaps.sideTop(block, "_side", block, "_inverted_top"), ctx.modelOutput),
				SpectrumModels.SLAB_DETECTOR.create(block, SpectrumTextureMaps.sideTop(block, "_side", block, "_top"), ctx.modelOutput))));*/
	}
	
	public static <T extends Block> BlockRegistrar<T> orientable(BlockRegistrar<T> registrar) {
		return registrar.withBlockModel((ctx, block) -> {
			ResourceLocation horizontal = ModelTemplates.CUBE_ORIENTABLE.create(block, new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front")), ctx.modelOutput);
			ResourceLocation vertical = ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, new TextureMapping().put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_top")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_vertical")), ctx.modelOutput);
			return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.FACING).select(Direction.DOWN, createModelVariant(vertical).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(Direction.UP, createModelVariant(vertical)).select(Direction.NORTH, createModelVariant(horizontal)).select(Direction.EAST, createModelVariant(horizontal).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.SOUTH, createModelVariant(horizontal).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.WEST, createModelVariant(horizontal).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
		});
	}
	
	public static <T extends Block> BlockRegistrar<T> pylon(BlockRegistrar<T> registrar) {
		return registrar.withBlockItemModel((ctx, block) -> SpectrumModelHelper.registerParentedItemModel(ctx, block, block, "_head")).withBlockModel((ctx, block) -> {
			ResourceLocation head = ModelLocationUtils.getModelLocation(block, "_head");
			ResourceLocation body = ModelLocationUtils.getModelLocation(block, "_body");
			ResourceLocation waist = ModelLocationUtils.getModelLocation(block, "_waist");
			ResourceLocation foot = ModelLocationUtils.getModelLocation(block, "_foot");
			ResourceLocation end = ModelLocationUtils.getModelLocation(block, "_end");
			ResourceLocation pedestal = SpectrumModelTemplates.BALCITE_PYLON_PEDESTAL;
			SpectrumModelTemplates.BASE_PYLON_BODY.create(head, SpectrumTextureMaps.sideEnd(head, end), ctx.modelOutput);
			SpectrumModelTemplates.BASE_PYLON_BODY.create(body, SpectrumTextureMaps.sideEnd(body, end), ctx.modelOutput);
			SpectrumModelTemplates.BASE_PYLON_BODY.create(waist, SpectrumTextureMaps.sideEnd(waist, end), ctx.modelOutput);
			SpectrumModelTemplates.BASE_PYLON_BODY.create(foot, SpectrumTextureMaps.sideEnd(foot, end), ctx.modelOutput);
			return MultiPartGenerator.multiPart(block)
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.DOWN).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.DOWN).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.DOWN).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.DOWN).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.DOWN).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.UP).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.UP).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.UP).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.UP).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.UP).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.NORTH).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.NORTH).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.NORTH).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.NORTH).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.NORTH).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.SOUTH).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.SOUTH).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.SOUTH).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.SOUTH).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.SOUTH).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.WEST).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.WEST).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.WEST).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.WEST).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.WEST).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.EAST).term(PylonBlock.SECTION, PylonBlock.Section.HEAD), createModelVariant(head).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.EAST).term(PylonBlock.SECTION, PylonBlock.Section.BODY), createModelVariant(body).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.EAST).term(PylonBlock.SECTION, PylonBlock.Section.WAIST), createModelVariant(waist).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.EAST).term(PylonBlock.SECTION, PylonBlock.Section.FOOT), createModelVariant(foot).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.FACING, Direction.EAST).term(PylonBlock.PEDESTAL, true), createModelVariant(pedestal).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
		});
	}
	
	public static class BlockRegistrar<T extends Block> {
		
		public enum RenderLayerID {
			DEFAULT,
			CUTOUT,
			MIPPED_CUTOUT,
			TRANSLUCENT
		}
		
		private final ResourceLocation id;
		private boolean hasBlock = false;
		private boolean hasItem = false;
		@Nullable
		private DeferredBlock<T> holder = null;
		@Nullable
		private final Item item = null;
		private Function<T, Item> callback = null;
		private RenderLayerID renderLayer = RenderLayerID.DEFAULT;
		
		public BlockRegistrar(String name) {
			this.id = locate(name);
		}
		
		public BlockRegistrar<T> withBlock(Supplier<T> blockFactory) {
			if (hasBlock) throw new UnsupportedOperationException("Attempted to register two blocks with id " + id);
			hasBlock = true;
			holder = REGISTRAR.register(id.getPath(), blockFactory);
			return this;
		}
		
		public BlockRegistrar<T> withItem(Function<T, Item> callback) {
			if (hasItem) throw new UnsupportedOperationException("Attempted to register two items with id " + id);
			this.hasItem = true;
			this.callback = callback;
			return this;
		}
		
		public BlockRegistrar<T> withBlockModel(BiFunction<BlockModelGenerators, Block, BlockStateGenerator> callback) {
			/*SpectrumModelHelper.BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
				Objects.requireNonNull(holder);
				ctx.blockStateOutput.accept(callback.apply(ctx, holder.get()));
			});*/
			return this;
		}
		
		public BlockRegistrar<T> withBlockItemModel(BiConsumer<ItemModelGenerators, ? super T> callback) {
			/*SpectrumModelHelper.ITEM_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(holder);
					//callback.accept(ctx, holder.get());
				}
			});*/
			return this;
		}
		
		public BlockRegistrar<T> withItemModel(BiConsumer<ItemModelGenerators, Item> callback) {
			/*SpectrumModelHelper.ITEM_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(holder);
					callback.accept(ctx, holder.asItem());
				}
			});*/
			return this;
		}
		
		public BlockRegistrar<T> withPredefinedItemModel() {
			/*SpectrumModelHelper.BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
				if (hasItem) {
					Objects.requireNonNull(holder);
					ctx.skipAutoItemBlock(holder.get());
				}
			});*/
			return this;
		}

		public @Nullable DeferredBlock<T> holder() {
			return holder;
		}

		public @Nullable Item item() {
			return item;
		}
		
		public ResourceKey<Block> blockKey() {
			return ResourceKey.create(Registries.BLOCK, id);
		}
		
		public ResourceKey<Item> itemKey() {
			return ResourceKey.create(Registries.ITEM, id);
		}
		
	}
	
	public static void registerClient(FMLClientSetupEvent event) {
		// This will be obsolete starting MC 26.1
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(RADIANT_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(HUMMINGSTONE_GLASS_PANE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SMALL_COAL_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_COAL_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(COAL_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_IRON_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_IRON_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IRON_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_GOLD_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_GOLD_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GOLD_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_DIAMOND_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_DIAMOND_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(DIAMOND_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_EMERALD_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_EMERALD_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EMERALD_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_REDSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_REDSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(REDSTONE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_LAPIS_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_LAPIS_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LAPIS_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_COPPER_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_COPPER_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(COPPER_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_QUARTZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_QUARTZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(QUARTZ_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_NETHERITE_SCRAP_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_NETHERITE_SCRAP_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(NETHERITE_SCRAP_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_ECHO_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_ECHO_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECHO_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_GLOWSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_GLOWSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GLOWSTONE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_PRISMARINE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_PRISMARINE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRISMARINE_CLUSTER.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(BISMUTH_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_BISMUTH_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_BISMUTH_BUD.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(AZURITE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_AZURITE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_AZURITE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MALACHITE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_MALACHITE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_MALACHITE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLOODSTONE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_BLOODSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_BLOODSTONE_BUD.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_TOPAZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MEDIUM_TOPAZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_TOPAZ_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_CITRINE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MEDIUM_CITRINE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_CITRINE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ONYX_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_ONYX_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MEDIUM_ONYX_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_ONYX_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_CLUSTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LARGE_MOONSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MEDIUM_MOONSTONE_BUD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_MOONSTONE_BUD.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_BASIC_TOPAZ.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_BASIC_AMETHYST.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_BASIC_CITRINE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_ALL_BASIC.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_ONYX.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PEDESTAL_MOONSTONE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FUSION_SHRINE_BASALT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FUSION_SHRINE_CALCITE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ENCHANTER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ITEM_BOWL_BASALT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ITEM_BOWL_CALCITE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTION_WORKSHOP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SPIRIT_INSTILLER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CRYSTALLARIEUM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(COLOR_PICKER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(VEGETAL_BLOCK.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PYRITE_RIPPER.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SAG_LEAF.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SAG_BUBBLE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_SAG_BUBBLE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRIMORDIAL_FIRE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRIMORDIAL_WALL_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRIMORDIAL_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_BASALT_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_BASALT_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_BASALT_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_BASALT_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_BASALT_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_CALCITE_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_CALCITE_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_CALCITE_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_CALCITE_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_CALCITE_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(RADIANT_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ETHEREAL_PLATFORM.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(UNIVERSE_SPYHOLE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_CHIME.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_CHIME.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_CHIME.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_CHIME.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_CHIME.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TINTED_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(RADIANT_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TOPAZ_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(AMETHYST_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CITRINE_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ONYX_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOONSTONE_SEMI_PERMEABLE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ATTACHED_GLISTERING_MELON_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GLISTERING_MELON_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRESENT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BOTTOMLESS_BUNDLE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CONNECTION_NODE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PROVIDER_NODE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(STORAGE_NODE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SENDER_NODE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GATHER_NODE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(VARIA_SPROUT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SLATE_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EBONY_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IVORY_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CHESTNUT_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_SLATE_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_EBONY_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_IVORY_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_CHESTNUT_NOXSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SLATE_NOXWOOD_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SLATE_NOXWOOD_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SLATE_NOXWOOD_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EBONY_NOXWOOD_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EBONY_NOXWOOD_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EBONY_NOXWOOD_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IVORY_NOXWOOD_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IVORY_NOXWOOD_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IVORY_NOXWOOD_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CHESTNUT_NOXWOOD_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CHESTNUT_NOXWOOD_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CHESTNUT_NOXWOOD_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WEEPING_GALA_SPRIG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_WEEPING_GALA_SPRIG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WEEPING_GALA_FRONDS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WEEPING_GALA_FRONDS_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WEEPING_GALA_LANTERN.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WEEPING_GALA_LIGHT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(LONGING_CHIMERA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_RED_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_YELLOW_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_PINK_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_PURPLE_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SMALL_BLACK_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TALL_YELLOW_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TALL_RED_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TALL_PINK_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TALL_PURPLE_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TALL_BLACK_DRAGONJAG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ALOE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SAWBLADE_HOLLY_BUSH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BRISTLE_SPROUTS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(DOOMBLOOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SNAPPING_IVY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ABYSSAL_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(NIGHTDEW.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(SWEET_PEA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(APRICOTTI.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(HUMMING_BELL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(HUMMINGSTONE_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(HUMMINGSTONE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WAXED_HUMMINGSTONE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MOSS_BALL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(RESPLENDENT_BED.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADE_VINE_ROOTS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADE_VINE_BULB.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADE_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADE_VINE_PETAL_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADE_VINE_PETAL_CARPET.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(NEPHRITE_BLOSSOM_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(NEPHRITE_BLOSSOM_LEAVES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(NEPHRITE_BLOSSOM_BULB.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADEITE_LOTUS_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADEITE_LOTUS_FLOWER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADEITE_LOTUS_BULB.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADEITE_PETAL_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JADEITE_PETAL_CARPET.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FABRICATION_CHEST.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLACK_HOLE_CHEST.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PARTICLE_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CREATIVE_PARTICLE_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(QUITOXIC_REEDS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MERMAIDS_BRUSH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(AMARANTH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MEMORY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(REDSTONE_TIMER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(REDSTONE_CALCULATOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(REDSTONE_TRANSCEIVER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(OMINOUS_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CYAN_SPIRIT_SALLOW_VINES_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MAGENTA_SPIRIT_SALLOW_VINES_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(YELLOW_SPIRIT_SALLOW_VINES_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLACK_SPIRIT_SALLOW_VINES_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WHITE_SPIRIT_SALLOW_VINES_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CYAN_SPIRIT_SALLOW_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MAGENTA_SPIRIT_SALLOW_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(YELLOW_SPIRIT_SALLOW_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLACK_SPIRIT_SALLOW_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WHITE_SPIRIT_SALLOW_VINES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(STUCK_STORM_STONE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ENDER_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CLOVER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FOUR_LEAF_CLOVER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(AMARANTH_BUSHEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_AMARANTH_BUSHEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(RESONANT_LILY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_RESONANT_LILY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLOOD_ORCHID.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_BLOOD_ORCHID.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_SWEET_PEA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_APRICOTTI.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_VARIA_SPROUT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_HUMMING_BELL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLACK_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLUE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BROWN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CYAN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GRAY_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GREEN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_BLUE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_GRAY_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIME_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MAGENTA_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ORANGE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PINK_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PURPLE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(RED_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WHITE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(YELLOW_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_BLACK_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_BLUE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_BROWN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_CYAN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_GRAY_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_GREEN_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_LIGHT_BLUE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_LIGHT_GRAY_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_LIME_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_MAGENTA_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_ORANGE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_PINK_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_PURPLE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_RED_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_WHITE_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(POTTED_YELLOW_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLACK_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BLUE_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BROWN_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CYAN_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GRAY_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GREEN_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_BLUE_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_GRAY_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(LIME_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MAGENTA_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ORANGE_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PINK_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PURPLE_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(RED_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WHITE_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(YELLOW_LAMP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BLACK_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BLUE_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BROWN_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CYAN_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GRAY_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GREEN_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_BLUE_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIGHT_GRAY_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(LIME_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MAGENTA_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ORANGE_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PINK_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PURPLE_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(RED_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WHITE_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(YELLOW_SPORE_BLOSSOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRESERVATION_CONTROLLER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(DIKE_GATE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(DREAM_GATE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(INVISIBLE_WALL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PRESERVATION_ITEM_BOWL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(COURIER_STATUE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRESERVATION_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TINTED_PRESERVATION_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GLISTERING_SHOOTING_STAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FIERY_SHOOTING_STAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(COLORFUL_SHOOTING_STAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(PRISTINE_SHOOTING_STAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(GEMSTONE_SHOOTING_STAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(INCANDESCENT_AMALGAM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(AXOLOTL_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BAT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BEE_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BLAZE_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CAT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CHICKEN_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(COW_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CREEPER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ENDER_DRAGON_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ENDERMAN_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ENDERMITE_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(EVOKER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FISH_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FOX_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GHAST_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GLOW_SQUID_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GOAT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(GUARDIAN_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(HORSE_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ILLUSIONER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(OCELOT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PARROT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PHANTOM_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PIG_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PIGLIN_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(POLAR_BEAR_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(PUFFERFISH_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(RABBIT_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SHEEP_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SHULKER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SILVERFISH_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SKELETON_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SLIME_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SNOW_GOLEM_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SPIDER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SQUID_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(STRAY_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(STRIDER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TURTLE_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WITCH_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WITHER_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WITHER_SKELETON_IDOL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ZOMBIE_IDOL.get(), RenderType.translucent());
	}
	
}