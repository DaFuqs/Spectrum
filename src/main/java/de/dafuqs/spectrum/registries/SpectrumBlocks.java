package de.dafuqs.spectrum.registries;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.amphora.*;
import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.cinderhearth.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.conditional.amaranth.*;
import de.dafuqs.spectrum.blocks.conditional.blood_orchid.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.blocks.dd_deco.*;
import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.blocks.ender.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.furniture.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.gemstone.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.blocks.lava_sponge.*;
import de.dafuqs.spectrum.blocks.melon.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.mob_blocks.*;
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
import de.dafuqs.spectrum.blocks.structure.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.blocks.weathering.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.color.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;
import static de.dafuqs.spectrum.registries.SpectrumItems.*;

public class SpectrumBlocks {
	
	// PEDESTALS
	private static final FabricBlockSettings PEDESTAL_SETTINGS = FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.AMETHYST_BLOCK).strength(5.0F, 20.0F).solidBlock(SpectrumBlocks::never).blockVision(SpectrumBlocks::never).nonOpaque();
	public static final Block PEDESTAL_BASIC_TOPAZ = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.BASIC_TOPAZ);
	public static final Block PEDESTAL_BASIC_AMETHYST = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.BASIC_AMETHYST);
	public static final Block PEDESTAL_BASIC_CITRINE = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.BASIC_CITRINE);
	public static final Block PEDESTAL_ALL_BASIC = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.CMY);
	public static final Block PEDESTAL_ONYX = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.ONYX);
	public static final Block PEDESTAL_MOONSTONE = new PedestalBlock(PEDESTAL_SETTINGS, BuiltinPedestalVariant.MOONSTONE);
	
	private static final FabricBlockSettings FUSION_SHINE_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F).requiresTool().nonOpaque().luminance(value -> value.get(FusionShrineBlock.LIGHT_LEVEL));
	public static final Block FUSION_SHRINE_BASALT = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	public static final Block FUSION_SHRINE_CALCITE = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	
	public static final Block ENCHANTER = new EnchanterBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 8.0F).nonOpaque());
	public static final Block ITEM_BOWL_BASALT = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block ITEM_BOWL_CALCITE = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block ITEM_ROUNDEL = new ItemRoundelBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block POTION_WORKSHOP = new PotionWorkshopBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F).nonOpaque());
	public static final Block SPIRIT_INSTILLER = new SpiritInstillerBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 8.0F).nonOpaque());
	public static final Block CRYSTALLARIEUM = new CrystallarieumBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 8.0F).nonOpaque());
	public static final Block CINDERHEARTH = new CinderhearthBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 8.0F).nonOpaque());
	public static final Block MEMORY = new MemoryBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0F).nonOpaque().ticksRandomly());
	
	// GEMS
	public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER).luminance(6));
	public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_TOPAZ_BUD).luminance(6));
	public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_TOPAZ_BUD).luminance(4));
	public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_TOPAZ_BUD).luminance(2));
	public static final Block BUDDING_TOPAZ = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
	public static final Block TOPAZ_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
	public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER).luminance(7));
	public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_CITRINE_BUD).luminance(7));
	public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_CITRINE_BUD).luminance(5));
	public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_CITRINE_BUD).luminance(3));
	public static final Block BUDDING_CITRINE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
	public static final Block CITRINE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
	public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER).luminance(3));
	public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_ONYX_BUD).luminance(5));
	public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_ONYX_BUD).luminance(3));
	public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD).luminance(1));
	public static final Block BUDDING_ONYX = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	public static final Block ONYX_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER).luminance(15));
	public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_MOONSTONE_BUD).luminance(10));
	public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_MOONSTONE_BUD).luminance(8));
	public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_MOONSTONE_BUD).luminance(6));
	public static final Block BUDDING_MOONSTONE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool(), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
	public static final Block MOONSTONE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
	public static final Block SPECTRAL_SHARD_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.SPECTRAL_BLOCK).requiresTool(), SpectrumSoundEvents.SPECTRAL_BLOCK_HIT, SpectrumSoundEvents.SPECTRAL_BLOCK_CHIME);
	
	public static final Block BEDROCK_STORAGE_BLOCK = new BlockWithTooltip(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(100.0F, 3600.0F), Text.translatable("spectrum.tooltip.dragon_and_wither_immune"));
	
	public static final Block BISMUTH_CLUSTER = new BismuthClusterBlock(9, 3, null, FabricBlockSettings.of(Material.AMETHYST).mapColor(MapColor.DARK_AQUA).hardness(1.5F).nonOpaque().sounds(BlockSoundGroup.CHAIN));
	public static final Block LARGE_BISMUTH_BUD = new BismuthClusterBlock(5, 3, BISMUTH_CLUSTER.getDefaultState(), FabricBlockSettings.copyOf(TOPAZ_CLUSTER).mapColor(MapColor.DARK_AQUA).sounds(BlockSoundGroup.CHAIN));
	public static final Block SMALL_BISMUTH_BUD = new BismuthClusterBlock(3, 4, LARGE_BISMUTH_BUD.getDefaultState(), FabricBlockSettings.copyOf(TOPAZ_CLUSTER).mapColor(MapColor.DARK_AQUA).sounds(BlockSoundGroup.CHAIN));
	public static final Block BISMUTH_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).strength(20.0F).sounds(BlockSoundGroup.CHAIN));
	
	public static final Block MALACHITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool(), UniformIntProvider.create(7, 11), locate("milestones/reveal_malachite"), Blocks.STONE.getDefaultState());
	public static final Block DEEPSLATE_MALACHITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool(), UniformIntProvider.create(7, 11), locate("milestones/reveal_malachite"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block MALACHITE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(BlockSoundGroup.CHAIN));
	public static final Block LARGE_MALACHITE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(BlockSoundGroup.CHAIN));
	public static final Block SMALL_MALACHITE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(BlockSoundGroup.CHAIN));
	public static final Block MALACHITE_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).sounds(BlockSoundGroup.CHAIN));
	
	public static final Block BLOODSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD));
	public static final Block LARGE_BLOODSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD));
	public static final Block SMALL_BLOODSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER));
	public static final Block BLOODSTONE_BLOCK = new PillarBlock(AbstractBlock.Settings.copy(MALACHITE_BLOCK).sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER));

	public static final Block EFFULGENT_BLOCK = new CushionedFacingBlock(AbstractBlock.Settings.copy(Blocks.RED_WOOL).strength(5F));
	public static final Block EFFULGENT_CUSHION = new CushionBlock(AbstractBlock.Settings.copy(EFFULGENT_BLOCK));
	public static final Block EFFULGENT_CARPET = new CushionedCarpetBlock(AbstractBlock.Settings.copy(EFFULGENT_BLOCK));

	// DD BLOCKS
	private static final float BLACKSLAG_HARDNESS = 5.0F;
	private static final float BLACKSLAG_BLAST_RESISTANCE = 7.0F;
	public static final Block BLACKSLAG = new BlackslagBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(BLACKSLAG_HARDNESS, BLACKSLAG_BLAST_RESISTANCE));
	public static final Block INFESTED_BLACKSLAG = new InfestedBlock(BLACKSLAG, AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT));
	public static final Block COBBLED_BLACKSLAG = new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(BLACKSLAG_HARDNESS, BLACKSLAG_BLAST_RESISTANCE));
	public static final Block COBBLED_BLACKSLAG_STAIRS = new StairsBlock(COBBLED_BLACKSLAG.getDefaultState(), AbstractBlock.Settings.copy(COBBLED_BLACKSLAG));
	public static final Block COBBLED_BLACKSLAG_SLAB = new SlabBlock(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG));
	public static final Block COBBLED_BLACKSLAG_WALL = new WallBlock(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG));
	public static final Block POLISHED_BLACKSLAG = new Block(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.POLISHED_DEEPSLATE));
	public static final Block POLISHED_BLACKSLAG_STAIRS = new StairsBlock(POLISHED_BLACKSLAG.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_BLACKSLAG));
	public static final Block POLISHED_BLACKSLAG_SLAB = new SlabBlock(AbstractBlock.Settings.copy(POLISHED_BLACKSLAG));
	public static final Block POLISHED_BLACKSLAG_WALL = new WallBlock(AbstractBlock.Settings.copy(POLISHED_BLACKSLAG));
	public static final Block BLACKSLAG_TILES = new Block(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.DEEPSLATE_TILES));
	public static final Block BLACKSLAG_TILE_STAIRS = new StairsBlock(BLACKSLAG_TILES.getDefaultState(), AbstractBlock.Settings.copy(BLACKSLAG_TILES));
	public static final Block BLACKSLAG_TILE_SLAB = new SlabBlock(AbstractBlock.Settings.copy(BLACKSLAG_TILES));
	public static final Block BLACKSLAG_TILE_WALL = new WallBlock(AbstractBlock.Settings.copy(BLACKSLAG_TILES));
	public static final Block BLACKSLAG_BRICKS = new Block(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.DEEPSLATE_BRICKS));
	public static final Block BLACKSLAG_BRICK_STAIRS = new StairsBlock(BLACKSLAG_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(BLACKSLAG_BRICKS));
	public static final Block BLACKSLAG_BRICK_SLAB = new SlabBlock(AbstractBlock.Settings.copy(BLACKSLAG_BRICKS));
	public static final Block BLACKSLAG_BRICK_WALL = new WallBlock(AbstractBlock.Settings.copy(BLACKSLAG_BRICKS));
	public static final Block POLISHED_BLACKSLAG_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.DEEPSLATE_BRICKS));
	
	public static final Block CHISELED_POLISHED_BLACKSLAG = new Block(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.DEEPSLATE_BRICKS));
	public static final Block ANCIENT_CHISELED_POLISHED_BLACKSLAG = new Block(AbstractBlock.Settings.copy(COBBLED_BLACKSLAG).sounds(BlockSoundGroup.DEEPSLATE_BRICKS));
	public static final Block CRACKED_BLACKSLAG_BRICKS = new Block(AbstractBlock.Settings.copy(BLACKSLAG_BRICKS));
	public static final Block CRACKED_BLACKSLAG_TILES = new Block(AbstractBlock.Settings.copy(BLACKSLAG_TILES));
	public static final Block POLISHED_BLACKSLAG_BUTTON = new StoneButtonBlock(AbstractBlock.Settings.of(Material.DECORATION).noCollision().strength(0.5F));
	public static final Block POLISHED_BLACKSLAG_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().noCollision().strength(0.5F));
	
	
	public static final Block SHALE_CLAY = new PillarBlock(AbstractBlock.Settings.copy(BLACKSLAG).sounds(BlockSoundGroup.MUD_BRICKS));
	public static final Block TILLED_SHALE_CLAY = new TilledShaleClayBlock(AbstractBlock.Settings.copy(SHALE_CLAY), SHALE_CLAY.getDefaultState());
	
	public static final Block POLISHED_SHALE_CLAY = new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_POLISHED_SHALE_CLAY = new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_POLISHED_SHALE_CLAY = new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block POLISHED_SHALE_CLAY_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, POLISHED_SHALE_CLAY.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block POLISHED_SHALE_CLAY_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_POLISHED_SHALE_CLAY_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_POLISHED_SHALE_CLAY.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_POLISHED_SHALE_CLAY_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_POLISHED_SHALE_CLAY_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_POLISHED_SHALE_CLAY.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_POLISHED_SHALE_CLAY_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	
	public static final Block SHALE_CLAY_BRICKS = new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_BRICKS = new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_BRICKS = new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block SHALE_CLAY_BRICK_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block SHALE_CLAY_BRICK_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_BRICK_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_BRICK_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_BRICK_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_BRICK_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	
	public static final Block SHALE_CLAY_TILES = new WeatheringBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_TILES = new WeatheringBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_TILES = new WeatheringBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block SHALE_CLAY_TILE_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.UNAFFECTED, SHALE_CLAY_TILES.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block SHALE_CLAY_TILE_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.UNAFFECTED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_TILE_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.EXPOSED, EXPOSED_SHALE_CLAY_TILES.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block EXPOSED_SHALE_CLAY_TILE_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.EXPOSED, AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_TILE_STAIRS = new WeatheringStairsBlock(Weathering.WeatheringLevel.WEATHERED, WEATHERED_SHALE_CLAY_TILES.getDefaultState(), AbstractBlock.Settings.copy(SHALE_CLAY));
	public static final Block WEATHERED_SHALE_CLAY_TILE_SLAB = new WeatheringSlabBlock(Weathering.WeatheringLevel.WEATHERED, AbstractBlock.Settings.copy(SHALE_CLAY));
	
	public static final Block DRAGONBONE = new DragonboneBlock(AbstractBlock.Settings.of(Material.STONE).strength(-1.0F, 25.0F).sounds(BlockSoundGroup.BONE));
	public static final Block CRACKED_DRAGONBONE = new CrackedDragonboneBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.STONE_GRAY).strength(100.0F, 1200.0F).sounds(BlockSoundGroup.BONE).requiresTool());

	public static final Block POLISHED_BONE_ASH = new Block(FabricBlockSettings.copyOf(CRACKED_DRAGONBONE).sounds(BlockSoundGroup.BONE).hardness(1500F).mapColor(DyeColor.WHITE));
	public static final Block POLISHED_BONE_ASH_STAIRS = new StairsBlock(POLISHED_BONE_ASH.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_BONE_ASH));
	public static final Block POLISHED_BONE_ASH_SLAB = new SlabBlock(AbstractBlock.Settings.copy(POLISHED_BONE_ASH));
	public static final Block POLISHED_BONE_ASH_WALL = new WallBlock(AbstractBlock.Settings.copy(POLISHED_BONE_ASH));
	
	public static final Block BONE_ASH_BRICKS = new Block(FabricBlockSettings.copyOf(CRACKED_DRAGONBONE).sounds(BlockSoundGroup.BONE).hardness(1500F).mapColor(DyeColor.WHITE));
	public static final Block BONE_ASH_BRICK_STAIRS = new StairsBlock(BONE_ASH_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(BONE_ASH_BRICKS));
	public static final Block BONE_ASH_BRICK_SLAB = new SlabBlock(AbstractBlock.Settings.copy(BONE_ASH_BRICKS));
	public static final Block BONE_ASH_BRICK_WALL = new WallBlock(AbstractBlock.Settings.copy(BONE_ASH_BRICKS));
	
	public static final Block BONE_ASH_TILES = new Block(FabricBlockSettings.copyOf(CRACKED_DRAGONBONE));
	public static final Block BONE_ASH_TILE_STAIRS = new StairsBlock(BONE_ASH_TILES.getDefaultState(), AbstractBlock.Settings.copy(BONE_ASH_TILES));
	public static final Block BONE_ASH_TILE_SLAB = new SlabBlock(AbstractBlock.Settings.copy(BONE_ASH_TILES));
	public static final Block BONE_ASH_TILE_WALL = new WallBlock(AbstractBlock.Settings.copy(BONE_ASH_TILES));
	
	public static final Block POLISHED_BONE_ASH_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BONE_ASH));
	public static final Block BONE_ASH_SHINGLES = new ShinglesBlock(FabricBlockSettings.copyOf(POLISHED_BONE_ASH).nonOpaque());
	
	public static final Block SAWTOOTH = new SawtoothBlock(AbstractBlock.Settings.copy(BLACKSLAG));
	public static final Block SLUSH = new PillarBlock(AbstractBlock.Settings.copy(BLACKSLAG).sounds(BlockSoundGroup.MUDDY_MANGROVE_ROOTS));
	public static final Block TILLED_SLUSH = new TilledSlushBlock(AbstractBlock.Settings.copy(SLUSH), SLUSH.getDefaultState());

	public static final Block BLACK_MATERIA = new BlackMateriaBlock(FabricBlockSettings.copyOf(Blocks.SAND).ticksRandomly().breakInstantly());
	public static final Block BLACK_SLUDGE = new Block(FabricBlockSettings.copyOf(Blocks.SAND).ticksRandomly());
	public static final Block SAG_LEAF = new BlackSludgePlantBlock(FabricBlockSettings.copyOf(Blocks.GRASS).mapColor(MapColor.TERRACOTTA_BLACK));
	public static final Block SAG_BUBBLE = new BlackSludgePlantBlock(FabricBlockSettings.copyOf(Blocks.GRASS).mapColor(MapColor.TERRACOTTA_BLACK));
	public static final Block SMALL_SAG_BUBBLE = new BlackSludgePlantBlock(FabricBlockSettings.copyOf(Blocks.GRASS).mapColor(MapColor.TERRACOTTA_BLACK));
	
	public static final PrimordialFireBlock PRIMORDIAL_FIRE = new PrimordialFireBlock(AbstractBlock.Settings.of(Material.FIRE, MapColor.PURPLE).noCollision().breakInstantly().luminance((state) -> 10).sounds(BlockSoundGroup.WOOL));
	
	public static final Block SMOOTH_BASALT_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
	public static final Block SMOOTH_BASALT_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
	public static final Block SMOOTH_BASALT_STAIRS = new SpectrumStairsBlock(Blocks.TUFF.getDefaultState(), FabricBlockSettings.copyOf(Blocks.TUFF));
	
	public static final Block POLISHED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).strength(2.0F, 5.0F).requiresTool());
	public static final Block POLISHED_BASALT_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_BASALT_CREST = new CardinalFacingBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block CHISELED_POLISHED_BASALT = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block NOTCHED_POLISHED_BASALT = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_BASALT_SLAB = new SlabBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_BASALT_WALL = new WallBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_BASALT_STAIRS = new SpectrumStairsBlock(POLISHED_BASALT.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block BASALT_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).strength(2.5F, 6.0F));
	public static final Block BASALT_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block BASALT_BRICK_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block BASALT_BRICK_STAIRS = new SpectrumStairsBlock(BASALT_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block TOPAZ_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(6));
	public static final Block AMETHYST_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5));
	public static final Block CITRINE_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(7));
	public static final Block ONYX_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(3));
	public static final Block MOONSTONE_CHISELED_BASALT = new SpectrumLineFacingBlock(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(12));
	
	public static final Block BASALT_TILES = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block CRACKED_BASALT_TILES = new Block(FabricBlockSettings.copyOf(BASALT_TILES));
	public static final Block BASALT_TILE_STAIRS = new SpectrumStairsBlock(BASALT_TILES.getDefaultState(), FabricBlockSettings.copyOf(BASALT_TILES));
	public static final Block BASALT_TILE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_TILES));
	public static final Block BASALT_TILE_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_TILES));
	public static final Block CRACKED_BASALT_BRICKS = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block POLISHED_BASALT_BUTTON = new StoneButtonBlock(AbstractBlock.Settings.of(Material.DECORATION).noCollision().strength(0.5F));
	public static final Block POLISHED_BASALT_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().noCollision().strength(0.5F));
	
	public static final Block CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
	public static final Block CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
	public static final Block CALCITE_STAIRS = new SpectrumStairsBlock(Blocks.CALCITE.getDefaultState(), FabricBlockSettings.copyOf(Blocks.CALCITE));
	public static final Block POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_STAIRS = new SpectrumStairsBlock(POLISHED_CALCITE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_CREST = new CardinalFacingBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block CHISELED_POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block NOTCHED_POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block CALCITE_BRICKS = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_STAIRS = new SpectrumStairsBlock(CALCITE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block TOPAZ_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(6));
	public static final Block AMETHYST_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(5));
	public static final Block CITRINE_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(7));
	public static final Block ONYX_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(3));
	public static final Block MOONSTONE_CHISELED_CALCITE = new SpectrumLineFacingBlock(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(12));
	
	public static final Block CALCITE_TILES = new Block(FabricBlockSettings.copyOf(POLISHED_CALCITE));
	public static final Block CALCITE_TILE_STAIRS = new SpectrumStairsBlock(CALCITE_TILES.getDefaultState(), FabricBlockSettings.copyOf(CALCITE_TILES));
	public static final Block CALCITE_TILE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(CALCITE_TILES));
	public static final Block CALCITE_TILE_WALL = new WallBlock(FabricBlockSettings.copyOf(CALCITE_TILES));
	public static final Block CRACKED_CALCITE_TILES = new Block(FabricBlockSettings.copyOf(CALCITE_TILES));
	public static final Block CRACKED_CALCITE_BRICKS = new Block(FabricBlockSettings.copyOf(CALCITE_BRICKS));
	public static final Block POLISHED_CALCITE_BUTTON = new StoneButtonBlock(AbstractBlock.Settings.of(Material.DECORATION).noCollision().strength(0.5F));
	public static final Block POLISHED_CALCITE_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().noCollision().strength(0.5F));
	
	// GEMSTONE LAMPS
	public static final Block TOPAZ_CALCITE_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block AMETHYST_CALCITE_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block CITRINE_CALCITE_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block ONYX_CALCITE_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block MOONSTONE_CALCITE_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block TOPAZ_BASALT_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block AMETHYST_BASALT_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block CITRINE_BASALT_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block ONYX_BASALT_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	public static final Block MOONSTONE_BASALT_LAMP = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT).luminance(15).nonOpaque());
	
	// GLASS
	public static final Block TOPAZ_GLASS = new GemstoneGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS), BuiltinGemstoneColor.CYAN);
	public static final Block AMETHYST_GLASS = new GemstoneGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS), BuiltinGemstoneColor.MAGENTA);
	public static final Block CITRINE_GLASS = new GemstoneGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS), BuiltinGemstoneColor.YELLOW);
	public static final Block ONYX_GLASS = new GemstoneGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS), BuiltinGemstoneColor.BLACK);
	public static final Block MOONSTONE_GLASS = new GemstoneGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS), BuiltinGemstoneColor.WHITE);
	public static final Block RADIANT_GLASS = new RadiantGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS).luminance(value -> 12));
	
	public static final Block ETHEREAL_PLATFORM = new EtherealGlassBlock(FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD).sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block UNIVERSE_SPYHOLE = new GlassBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).blockVision(SpectrumBlocks::never).hardness(1.0F));
	
	public static final Block TOPAZ_CHIME = new GemstoneChimeBlock(FabricBlockSettings.copyOf(TOPAZ_CLUSTER).hardness(1.0F).sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER).nonOpaque(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME, SpectrumParticleTypes.CYAN_SPARKLE_RISING);
	public static final Block AMETHYST_CHIME = new GemstoneChimeBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER).hardness(1.0F).sounds(BlockSoundGroup.AMETHYST_CLUSTER).nonOpaque(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SpectrumParticleTypes.MAGENTA_SPARKLE_RISING);
	public static final Block CITRINE_CHIME = new GemstoneChimeBlock(FabricBlockSettings.copyOf(CITRINE_CLUSTER).hardness(1.0F).sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER).nonOpaque(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, SpectrumParticleTypes.YELLOW_SPARKLE_RISING);
	public static final Block ONYX_CHIME = new GemstoneChimeBlock(FabricBlockSettings.copyOf(ONYX_CLUSTER).hardness(1.0F).sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER).nonOpaque(), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME, SpectrumParticleTypes.BLACK_SPARKLE_RISING);
	public static final Block MOONSTONE_CHIME = new GemstoneChimeBlock(FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).hardness(1.0F).sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER).nonOpaque(), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME, SpectrumParticleTypes.WHITE_SPARKLE_RISING);
	
	public static final Block TOPAZ_DECOSTONE = new DecoStoneBlock(FabricBlockSettings.copyOf(TOPAZ_BLOCK).hardness(3.0F).sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER).nonOpaque());
	public static final Block AMETHYST_DECOSTONE = new DecoStoneBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).hardness(3.0F).sounds(BlockSoundGroup.AMETHYST_CLUSTER).nonOpaque());
	public static final Block CITRINE_DECOSTONE = new DecoStoneBlock(FabricBlockSettings.copyOf(CITRINE_BLOCK).hardness(3.0F).sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER).nonOpaque());
	public static final Block ONYX_DECOSTONE = new DecoStoneBlock(FabricBlockSettings.copyOf(ONYX_BLOCK).hardness(3.0F).sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER).nonOpaque());
	public static final Block MOONSTONE_DECOSTONE = new DecoStoneBlock(FabricBlockSettings.copyOf(MOONSTONE_BLOCK).hardness(3.0F).sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER).nonOpaque());
	
	// PLAYER GLASS
	public static final Block VANILLA_SEMI_PERMEABLE_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), Blocks.GLASS, false);
	public static final Block TINTED_SEMI_PERMEABLE_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(Blocks.TINTED_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), Blocks.TINTED_GLASS, true);
	public static final Block RADIANT_SEMI_PERMEABLE_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.RADIANT_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never).luminance((state) -> 12), SpectrumBlocks.RADIANT_GLASS, false);
	public static final Block TOPAZ_SEMI_PERMEABLE_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.CYAN);
	public static final Block AMETHYST_SEMI_PERMEABLE_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.MAGENTA);
	public static final Block CITRINE_SEMI_PERMEABLE_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.YELLOW);
	public static final Block ONYX_SEMI_PERMEABLE_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.BLACK);
	public static final Block MOONSTONE_SEMI_PERMEABLE_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.WHITE);
	
	// MELON
	public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.copyOf(Blocks.MELON));
	public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.MELON_STEM));
	public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.ATTACHED_MELON_STEM));
	
	public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).ticksRandomly());
	public static final Block PRESENT = new PresentBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block TITRATION_BARREL = new TitrationBarrelBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	
	// TECHNICAL WITHOUT CORRESPONDING ITEMS
	public static final Block BLOCK_FLOODER = new BlockFlooderBlock(FabricBlockSettings.of(Material.STONE));
	public static final Block BOTTOMLESS_BUNDLE = new BottomlessBundleBlock(FabricBlockSettings.of(Material.WOOL, MapColor.PALE_PURPLE).hardness(1.0F).nonOpaque());
	public static final Block WAND_LIGHT_BLOCK = new WandLightBlock(FabricBlockSettings.copyOf(Blocks.LIGHT).sounds(SpectrumBlockSoundGroups.WAND_LIGHT).breakInstantly());
	public static final Block DECAYING_LIGHT_BLOCK = new DecayingLightBlock(FabricBlockSettings.copyOf(WAND_LIGHT_BLOCK).ticksRandomly());
	
	// DECAY
	public static final Block FADING = new FadingBlock(FabricBlockSettings.of(SpectrumBlockMaterials.DECAY, MapColor.BLACK).ticksRandomly().strength(0.5F, 0.5F).allowsSpawning((state, world, pos, type) -> false));
	public static final Block FAILING = new FailingBlock(FabricBlockSettings.copyOf(FADING).strength(20.0F, 50.0F));
	public static final Block RUIN = new RuinBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F));
	public static final Block FORFEITURE = new ForfeitureBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F));
	public static final Block DECAY_AWAY = new DecayAwayBlock(FabricBlockSettings.copyOf(Blocks.DIRT));
	
	
	private static AbstractBlock.Settings fluid(Material material) {
		return AbstractBlock.Settings.of(material).noCollision().strength(100.0F).dropsNothing();
	}
	
	// FLUIDS
	public static final Block LIQUID_CRYSTAL = new LiquidCrystalFluidBlock(SpectrumFluids.LIQUID_CRYSTAL, fluid(SpectrumBlockMaterials.LIQUID_CRYSTAL).luminance((state) -> LiquidCrystalFluidBlock.LUMINANCE));
	public static final Block MUD = new MudFluidBlock(SpectrumFluids.MUD, fluid(SpectrumBlockMaterials.MUD));
	public static final Block MIDNIGHT_SOLUTION = new MidnightSolutionFluidBlock(SpectrumFluids.MIDNIGHT_SOLUTION, fluid(SpectrumBlockMaterials.MIDNIGHT_SOLUTION));
	public static final Block DRAGONROT = new DragonrotFluidBlock(SpectrumFluids.DRAGONROT, fluid(SpectrumBlockMaterials.DRAGONROT).luminance((state) -> 15));
	
	// ROCK CANDY
	private static final ToIntFunction<BlockState> ROCK_CANDY_LUMINANCE = state -> Math.max(15, state.get(Properties.AGE_2) * 3 + (state.get(SugarStickBlock.LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? LiquidCrystalFluidBlock.LUMINANCE : 8));
	public static final Block SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.SUGAR);
	public static final Block TOPAZ_SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(SpectrumBlocks.SMALL_TOPAZ_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.TOPAZ);
	public static final Block AMETHYST_SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.AMETHYST);
	public static final Block CITRINE_SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(SpectrumBlocks.SMALL_CITRINE_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.CITRINE);
	public static final Block ONYX_SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(SpectrumBlocks.SMALL_ONYX_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.ONYX);
	public static final Block MOONSTONE_SUGAR_STICK = new SugarStickBlock(FabricBlockSettings.copyOf(SpectrumBlocks.SMALL_MOONSTONE_BUD).hardness(0.5F).luminance(ROCK_CANDY_LUMINANCE).ticksRandomly(), RockCandy.RockCandyVariant.MOONSTONE);
	
	// PASTEL NETWORK
	public static final Block CONNECTION_NODE = new PastelNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), PastelNodeType.CONNECTION);
	public static final Block PROVIDER_NODE = new PastelNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), PastelNodeType.PROVIDER);
	public static final Block STORAGE_NODE = new PastelNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER), PastelNodeType.STORAGE);
	public static final Block SENDER_NODE = new PastelNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER), PastelNodeType.SENDER);
	public static final Block GATHER_NODE = new PastelNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER), PastelNodeType.GATHER);
	
	// ENERGY
	public static final Block COLOR_PICKER = new ColorPickerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F).nonOpaque());
	public static final Block INKWELL = new InkwellBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F));
	public static final Block INK_DUCT = new InkDuctBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F).nonOpaque());
	public static final Block CRYSTAL_APOTHECARY = new CrystalApothecaryBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F));
	
	public static final Block BLACK_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.BLACK);
	public static final Block BLACK_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.BLACK);
	public static final Block BLACK_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.BLACK);
	public static final Block BLACK_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.BLACK);
	public static final Block BLACK_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.BLACK);
	public static final Block BLACK_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.BLACK);
	public static final Block BLACK_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.BLACK);
	public static final Block BLUE_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.BLUE);
	public static final Block BLUE_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.BLUE);
	public static final Block BLUE_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.BLUE);
	public static final Block BLUE_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.BLUE);
	public static final Block BLUE_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.BLUE);
	public static final Block BLUE_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.BLUE);
	public static final Block BLUE_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.BLUE);
	public static final Block BROWN_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.BROWN);
	public static final Block BROWN_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.BROWN);
	public static final Block BROWN_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.BROWN);
	public static final Block BROWN_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.BROWN);
	public static final Block BROWN_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.BROWN);
	public static final Block BROWN_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.BROWN);
	public static final Block BROWN_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.BROWN);
	public static final Block CYAN_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.CYAN);
	public static final Block CYAN_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.CYAN);
	public static final Block CYAN_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.CYAN);
	public static final Block CYAN_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.CYAN);
	public static final Block CYAN_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.CYAN);
	public static final Block CYAN_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.CYAN);
	public static final Block CYAN_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.CYAN);
	public static final Block GRAY_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.GRAY);
	public static final Block GRAY_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.GRAY);
	public static final Block GRAY_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.GRAY);
	public static final Block GRAY_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.GRAY);
	public static final Block GRAY_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.GRAY);
	public static final Block GRAY_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.GRAY);
	public static final Block GRAY_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.GRAY);
	public static final Block GREEN_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.GREEN);
	public static final Block GREEN_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.GREEN);
	public static final Block GREEN_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.GREEN);
	public static final Block GREEN_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.GREEN);
	public static final Block GREEN_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.GREEN);
	public static final Block GREEN_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.GREEN);
	public static final Block GREEN_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.GREEN);
	public static final Block LIGHT_BLUE_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_BLUE_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.LIGHT_GRAY);
	public static final Block LIGHT_GRAY_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.LIGHT_GRAY);
	public static final Block LIME_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.LIME);
	public static final Block LIME_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.LIME);
	public static final Block LIME_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.LIME);
	public static final Block LIME_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.LIME);
	public static final Block LIME_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.LIME);
	public static final Block LIME_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.LIME);
	public static final Block LIME_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.LIME);
	public static final Block MAGENTA_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.MAGENTA);
	public static final Block MAGENTA_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.MAGENTA);
	public static final Block MAGENTA_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.MAGENTA);
	public static final Block MAGENTA_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.MAGENTA);
	public static final Block MAGENTA_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.MAGENTA);
	public static final Block MAGENTA_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.MAGENTA);
	public static final Block MAGENTA_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.MAGENTA);
	public static final Block ORANGE_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.ORANGE);
	public static final Block ORANGE_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.ORANGE);
	public static final Block ORANGE_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.ORANGE);
	public static final Block ORANGE_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.ORANGE);
	public static final Block ORANGE_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.ORANGE);
	public static final Block ORANGE_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.ORANGE);
	public static final Block ORANGE_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.ORANGE);
	public static final Block PINK_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.PINK);
	public static final Block PINK_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.PINK);
	public static final Block PINK_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.PINK);
	public static final Block PINK_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.PINK);
	public static final Block PINK_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.PINK);
	public static final Block PINK_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.PINK);
	public static final Block PINK_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.PINK);
	public static final Block PURPLE_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.PURPLE);
	public static final Block PURPLE_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.PURPLE);
	public static final Block PURPLE_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.PURPLE);
	public static final Block PURPLE_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.PURPLE);
	public static final Block PURPLE_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.PURPLE);
	public static final Block PURPLE_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.PURPLE);
	public static final Block PURPLE_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.PURPLE);
	public static final Block RED_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.RED);
	public static final Block RED_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.RED);
	public static final Block RED_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.RED);
	public static final Block RED_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.RED);
	public static final Block RED_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.RED);
	public static final Block RED_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.RED);
	public static final Block RED_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.RED);
	public static final Block WHITE_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.WHITE);
	public static final Block WHITE_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.WHITE);
	public static final Block WHITE_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.WHITE);
	public static final Block WHITE_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.WHITE);
	public static final Block WHITE_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.WHITE);
	public static final Block WHITE_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.WHITE);
	public static final Block WHITE_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.WHITE);
	public static final Block YELLOW_PLANKS = new ColoredPlankBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), DyeColor.YELLOW);
	public static final Block YELLOW_STAIRS = new ColoredStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS), DyeColor.YELLOW);
	public static final Block YELLOW_PRESSURE_PLATE = new ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), DyeColor.YELLOW);
	public static final Block YELLOW_FENCE = new ColoredFenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE), DyeColor.YELLOW);
	public static final Block YELLOW_FENCE_GATE = new ColoredFenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), DyeColor.YELLOW);
	public static final Block YELLOW_BUTTON = new ColoredWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), DyeColor.YELLOW);
	public static final Block YELLOW_SLAB = new ColoredSlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB), DyeColor.YELLOW);
	
	
	public static FabricBlockSettings noxcap(MapColor color) {
		return FabricBlockSettings.copyOf(Blocks.CRIMSON_STEM).mapColor(color).strength(4.0F);
	}
	
	public static AbstractBlock.Settings fungus(MapColor color) {
		return AbstractBlock.Settings.of(Material.PLANT, color).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS);
	}
	
	//DD FLORA
	public static final Block SAWBLADE_GRASS = new BlackslagVegetationBlock(AbstractBlock.Settings.copy(Blocks.PODZOL).strength(BLACKSLAG_HARDNESS, BLACKSLAG_BLAST_RESISTANCE).sounds(BlockSoundGroup.AZALEA_LEAVES).ticksRandomly());
	public static final Block SHIMMEL = new BlackslagVegetationBlock(AbstractBlock.Settings.copy(Blocks.MYCELIUM).strength(BLACKSLAG_HARDNESS, BLACKSLAG_BLAST_RESISTANCE).sounds(BlockSoundGroup.WART_BLOCK).ticksRandomly());
	public static final Block OVERGROWN_BLACKSLAG = new BlackslagVegetationBlock(AbstractBlock.Settings.copy(Blocks.PODZOL).strength(BLACKSLAG_HARDNESS, BLACKSLAG_BLAST_RESISTANCE).sounds(BlockSoundGroup.VINE).velocityMultiplier(0.925F).ticksRandomly());
	public static final Block ROTTEN_GROUND = new RottenGroundBlock(AbstractBlock.Settings.copy(Blocks.MUD).sounds(BlockSoundGroup.HONEY).velocityMultiplier(0.775F).jumpVelocityMultiplier(0.9F));
	
	public static final Block SLATE_NOXSHROOM = new GilledFungusBlock(fungus(MapColor.GRAY), SpectrumCommon.locate("noxfungi/slate"));
	public static final Block EBONY_NOXSHROOM = new GilledFungusBlock(fungus(MapColor.TERRACOTTA_BLACK), SpectrumCommon.locate("noxfungi/ebony"));
	public static final Block IVORY_NOXSHROOM = new GilledFungusBlock(fungus(MapColor.OFF_WHITE), SpectrumCommon.locate("noxfungi/ivory"));
	public static final Block CHESTNUT_NOXSHROOM = new GilledFungusBlock(fungus(MapColor.DULL_RED), SpectrumCommon.locate("noxfungi/chestnut"));
	
	public static final ToIntFunction<BlockState> LIT_PROVIDER = (state -> state.get(RedstoneLampBlock.LIT) ? 15 : 0);
	
	public static final PillarBlock STRIPPED_SLATE_NOXCAP_STEM = new PillarBlock(noxcap(MapColor.GRAY));
	public static final PillarBlock SLATE_NOXCAP_STEM = new StrippingLootPillarBlock(noxcap(MapColor.GRAY), STRIPPED_SLATE_NOXCAP_STEM, SpectrumCommon.locate("gameplay/stripping/slate_noxcap_stripping"));
	public static final Block SLATE_NOXCAP_CAP = new Block(noxcap(MapColor.GRAY));
	public static final PillarBlock SLATE_NOXCAP_GILLS = new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(12).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block SLATE_NOXWOOD_PLANKS = new Block(noxcap(MapColor.GRAY));
	public static final StairsBlock SLATE_NOXWOOD_STAIRS = new StairsBlock(SLATE_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.GRAY));
	public static final SlabBlock SLATE_NOXWOOD_SLAB = new SlabBlock(noxcap(MapColor.GRAY));
	public static final FenceBlock SLATE_NOXWOOD_FENCE = new FenceBlock(noxcap(MapColor.GRAY));
	public static final FenceGateBlock SLATE_NOXWOOD_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SLATE_NOXWOOD_DOOR = new DoorBlock(noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_TRAPDOOR = new TrapdoorBlock(noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_BUTTON = new WoodenButtonBlock(noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_BEAM = new PillarBlock(noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_AMPHORA = new AmphoraBlock(noxcap(MapColor.GRAY));
	public static final Block SLATE_NOXWOOD_LANTERN = new RedstoneLampBlock(noxcap(MapColor.GRAY).luminance(LIT_PROVIDER));
	public static final Block SLATE_NOXWOOD_LIGHT = new PillarBlock(noxcap(MapColor.GRAY).luminance(15));
	public static final Block SLATE_NOXWOOD_LAMP = new FlexLanternBlock(noxcap(MapColor.GRAY).luminance(13));
	
	public static final PillarBlock STRIPPED_EBONY_NOXCAP_STEM = new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final PillarBlock EBONY_NOXCAP_STEM = new StrippingLootPillarBlock(noxcap(MapColor.TERRACOTTA_BLACK), STRIPPED_EBONY_NOXCAP_STEM, SpectrumCommon.locate("gameplay/stripping/ebony_noxcap_stripping"));
	public static final Block EBONY_NOXCAP_CAP = new Block(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final PillarBlock EBONY_NOXCAP_GILLS = new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(12).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block EBONY_NOXWOOD_PLANKS = new Block(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final StairsBlock EBONY_NOXWOOD_STAIRS = new StairsBlock(EBONY_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.TERRACOTTA_BLACK));
	public static final SlabBlock EBONY_NOXWOOD_SLAB = new SlabBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final FenceBlock EBONY_NOXWOOD_FENCE = new FenceBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final FenceGateBlock EBONY_NOXWOOD_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block EBONY_NOXWOOD_DOOR = new DoorBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_TRAPDOOR = new TrapdoorBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_BUTTON = new WoodenButtonBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_BEAM = new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_AMPHORA = new AmphoraBlock(noxcap(MapColor.TERRACOTTA_BLACK));
	public static final Block EBONY_NOXWOOD_LANTERN = new RedstoneLampBlock(noxcap(MapColor.TERRACOTTA_BLACK).luminance(LIT_PROVIDER));
	public static final Block EBONY_NOXWOOD_LIGHT = new PillarBlock(noxcap(MapColor.TERRACOTTA_BLACK).luminance(15));
	public static final Block EBONY_NOXWOOD_LAMP = new FlexLanternBlock(noxcap(MapColor.TERRACOTTA_BLACK).luminance(13));
	
	public static final PillarBlock STRIPPED_IVORY_NOXCAP_STEM = new PillarBlock(noxcap(MapColor.OFF_WHITE));
	public static final PillarBlock IVORY_NOXCAP_STEM = new StrippingLootPillarBlock(noxcap(MapColor.OFF_WHITE), STRIPPED_IVORY_NOXCAP_STEM, SpectrumCommon.locate("gameplay/stripping/ivory_noxcap_stripping"));
	public static final Block IVORY_NOXCAP_CAP = new Block(noxcap(MapColor.OFF_WHITE));
	public static final PillarBlock IVORY_NOXCAP_GILLS = new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(12).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block IVORY_NOXWOOD_PLANKS = new Block(noxcap(MapColor.OFF_WHITE));
	public static final StairsBlock IVORY_NOXWOOD_STAIRS = new StairsBlock(IVORY_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.OFF_WHITE));
	public static final SlabBlock IVORY_NOXWOOD_SLAB = new SlabBlock(noxcap(MapColor.OFF_WHITE));
	public static final FenceBlock IVORY_NOXWOOD_FENCE = new FenceBlock(noxcap(MapColor.OFF_WHITE));
	public static final FenceGateBlock IVORY_NOXWOOD_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block IVORY_NOXWOOD_DOOR = new DoorBlock(noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_TRAPDOOR = new TrapdoorBlock(noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_BUTTON = new WoodenButtonBlock(noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_BEAM = new PillarBlock(noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_AMPHORA = new AmphoraBlock(noxcap(MapColor.OFF_WHITE));
	public static final Block IVORY_NOXWOOD_LANTERN = new RedstoneLampBlock(noxcap(MapColor.OFF_WHITE).luminance(LIT_PROVIDER));
	public static final Block IVORY_NOXWOOD_LIGHT = new PillarBlock(noxcap(MapColor.OFF_WHITE).luminance(15));
	public static final Block IVORY_NOXWOOD_LAMP = new FlexLanternBlock(noxcap(MapColor.OFF_WHITE).luminance(13));
	
	public static final PillarBlock STRIPPED_CHESTNUT_NOXCAP_STEM = new PillarBlock(noxcap(MapColor.DULL_RED));
	public static final PillarBlock CHESTNUT_NOXCAP_STEM = new StrippingLootPillarBlock(noxcap(MapColor.DULL_RED), STRIPPED_CHESTNUT_NOXCAP_STEM, SpectrumCommon.locate("gameplay/stripping/chestnut_noxcap_stripping"));
	public static final Block CHESTNUT_NOXCAP_CAP = new Block(noxcap(MapColor.DULL_RED));
	public static final PillarBlock CHESTNUT_NOXCAP_GILLS = new PillarBlock(noxcap(MapColor.DIAMOND_BLUE).luminance(12).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block CHESTNUT_NOXWOOD_PLANKS = new Block(noxcap(MapColor.DULL_RED));
	public static final StairsBlock CHESTNUT_NOXWOOD_STAIRS = new StairsBlock(CHESTNUT_NOXWOOD_PLANKS.getDefaultState(), noxcap(MapColor.DULL_RED));
	public static final SlabBlock CHESTNUT_NOXWOOD_SLAB = new SlabBlock(noxcap(MapColor.DULL_RED));
	public static final FenceBlock CHESTNUT_NOXWOOD_FENCE = new FenceBlock(noxcap(MapColor.DULL_RED));
	public static final FenceGateBlock CHESTNUT_NOXWOOD_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block CHESTNUT_NOXWOOD_DOOR = new DoorBlock(noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_TRAPDOOR = new TrapdoorBlock(noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_BUTTON = new WoodenButtonBlock(noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_BEAM = new PillarBlock(noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_AMPHORA = new AmphoraBlock(noxcap(MapColor.DULL_RED));
	public static final Block CHESTNUT_NOXWOOD_LANTERN = new RedstoneLampBlock(noxcap(MapColor.DULL_RED).luminance(LIT_PROVIDER));
	public static final Block CHESTNUT_NOXWOOD_LIGHT = new PillarBlock(noxcap(MapColor.DULL_RED).luminance(15));
	public static final Block CHESTNUT_NOXWOOD_LAMP = new FlexLanternBlock(noxcap(MapColor.DULL_RED).luminance(13));
	
	
	public static AbstractBlock.Settings dragonjag(MapColor color) {
		return AbstractBlock.Settings.of(Material.PLANT, color).hardness(1.0F).sounds(BlockSoundGroup.GRASS);
	}
	
	public static final Block SMALL_RED_DRAGONJAG = new SmallDragonjagBlock(dragonjag(MapColor.DARK_RED), Dragonjag.Variant.RED);
	public static final Block SMALL_YELLOW_DRAGONJAG = new SmallDragonjagBlock(dragonjag(MapColor.PALE_YELLOW), Dragonjag.Variant.YELLOW);
	public static final Block SMALL_PINK_DRAGONJAG = new SmallDragonjagBlock(dragonjag(MapColor.DARK_DULL_PINK), Dragonjag.Variant.PINK);
	public static final Block SMALL_PURPLE_DRAGONJAG = new SmallDragonjagBlock(dragonjag(MapColor.PURPLE), Dragonjag.Variant.PURPLE);
	public static final Block SMALL_BLACK_DRAGONJAG = new SmallDragonjagBlock(dragonjag(MapColor.TERRACOTTA_BLACK), Dragonjag.Variant.BLACK);
	
	public static final Block TALL_RED_DRAGONJAG = new TallDragonjagBlock(dragonjag(MapColor.DARK_RED), Dragonjag.Variant.RED);
	public static final Block TALL_YELLOW_DRAGONJAG = new TallDragonjagBlock(dragonjag(MapColor.PALE_YELLOW), Dragonjag.Variant.YELLOW);
	public static final Block TALL_PINK_DRAGONJAG = new TallDragonjagBlock(dragonjag(MapColor.DARK_DULL_PINK), Dragonjag.Variant.PINK);
	public static final Block TALL_PURPLE_DRAGONJAG = new TallDragonjagBlock(dragonjag(MapColor.PURPLE), Dragonjag.Variant.PURPLE);
	public static final Block TALL_BLACK_DRAGONJAG = new TallDragonjagBlock(dragonjag(MapColor.TERRACOTTA_BLACK), Dragonjag.Variant.BLACK);
	
	public static final Block ALOE = new AloeBlock(AbstractBlock.Settings.of(Material.PLANT, MapColor.DARK_GREEN).hardness(1.0F).sounds(BlockSoundGroup.GRASS).noCollision().ticksRandomly().nonOpaque());
	public static final Block SAWBLADE_HOLLY_BUSH = new SawbladeHollyBushBlock(AbstractBlock.Settings.of(Material.PLANT, MapColor.TERRACOTTA_GREEN).sounds(BlockSoundGroup.GRASS).noCollision().ticksRandomly().breakInstantly().nonOpaque());
	public static final Block BRISTLE_SPROUTS = new BristleSproutsBlock(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.PALE_GREEN).sounds(BlockSoundGroup.GRASS).noCollision().breakInstantly().nonOpaque().offsetType(OffsetType.XZ));
	public static final Block DOOMBLOOM = new DoomBloomBlock(AbstractBlock.Settings.of(Material.PLANT, MapColor.PALE_GREEN).sounds(BlockSoundGroup.GRASS).noCollision().breakInstantly().luminance((state) -> state.get(DoomBloomBlock.AGE) * 2).nonOpaque());
	public static final Block SNAPPING_IVY = new SnappingIvyBlock(AbstractBlock.Settings.of(Material.PLANT, MapColor.PALE_GREEN).hardness(3.0F).noCollision().sounds(BlockSoundGroup.GRASS).nonOpaque());
	
	public static final Block HUMMINGSTONE_GLASS = new GlassBlock(AbstractBlock.Settings.of(Material.GLASS, MapColor.PALE_YELLOW).strength(5.0F, 100.0F).nonOpaque().sounds(BlockSoundGroup.GLASS).luminance((state) -> 12).requiresTool());
	public static final Block HUMMINGSTONE = new HummingstoneBlock(AbstractBlock.Settings.copy(HUMMINGSTONE_GLASS).ticksRandomly());
	
	// JADE VINES
	public static final Block JADE_VINE_ROOTS = new JadeVineRootsBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).sounds(BlockSoundGroup.WOOL).ticksRandomly().luminance((state) -> state.get(JadeVineRootsBlock.DEAD) ? 0 : 4).nonOpaque());
	public static final Block JADE_VINE_BULB = new JadeVineBulbBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).noCollision().sounds(BlockSoundGroup.WOOL).luminance((state) -> state.get(JadeVineBulbBlock.DEAD) ? 0 : 5).nonOpaque());
	public static final Block JADE_VINES = new JadeVinePlantBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).noCollision().sounds(BlockSoundGroup.WOOL).luminance((state) -> state.get(JadeVinePlantBlock.AGE) == 0 ? 0 : 5).nonOpaque());
	public static final Block JADE_VINE_PETAL_BLOCK = new JadeVinePetalBlock(FabricBlockSettings.of(Material.LEAVES, MapColor.PALE_GREEN).strength(0.1F).nonOpaque().sounds(BlockSoundGroup.WOOL).luminance(3));
	public static final Block JADE_VINE_PETAL_CARPET = new CarpetBlock(FabricBlockSettings.of(Material.CARPET, MapColor.PALE_GREEN).strength(0.1F).nonOpaque().sounds(BlockSoundGroup.WOOL).luminance(3));

	public static final Block NEPHRITE_BLOSSOM_STEM = new NephriteBlossomStemBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PINK).strength(2F).sounds(BlockSoundGroup.WOOL).nonOpaque().noCollision());
	public static final Block NEPHRITE_BLOSSOM_LEAVES = new NephriteBlossomLeavesBlock((FabricBlockSettings.copyOf(NEPHRITE_BLOSSOM_STEM).ticksRandomly().collidable(true).strength(0.2F).sounds(BlockSoundGroup.GRASS).luminance(13)));
	public static final Block NEPHRITE_BLOSSOM_BULB = new NephriteBlossomBulbBlock(FabricBlockSettings.copyOf(NEPHRITE_BLOSSOM_STEM));

	public static final Block JADEITE_LOTUS_STEM = new JadeiteLotusStemBlock(FabricBlockSettings.of(Material.PLANT, MapColor.BLACK).strength(2F).sounds(BlockSoundGroup.WOOL).nonOpaque().noCollision());
	public static final Block JADEITE_LOTUS_FLOWER = new JadeiteFlowerBlock(FabricBlockSettings.copyOf(JADEITE_LOTUS_STEM).mapColor(MapColor.WHITE).luminance(14).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always));
	public static final Block JADEITE_LOTUS_BULB = new JadeiteLotusBulbBlock(FabricBlockSettings.copyOf(JADEITE_LOTUS_STEM));
	
	// ORES
	public static final Block SHIMMERSTONE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), locate("milestones/reveal_shimmerstone"), Blocks.STONE.getDefaultState());
	public static final Block DEEPSLATE_SHIMMERSTONE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), locate("milestones/reveal_shimmerstone"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block AZURITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), locate("milestones/reveal_azurite"), Blocks.STONE.getDefaultState());
	public static final Block DEEPSLATE_AZURITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), locate("milestones/reveal_azurite"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block BLACKSLAG_AZURITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), locate("milestones/reveal_azurite"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	
	public static final Block AZURITE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD));
	public static final Block LARGE_AZURITE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD));
	public static final Block SMALL_AZURITE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER));
	
	public static final Block PALTAERIA_ORE = new CloakedOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.DARK_RED).requiresTool().requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE), UniformIntProvider.create(2, 4), locate("milestones/reveal_paltaeria"), Blocks.END_STONE.getDefaultState());
	public static final Block STRATINE_ORE = new CloakedOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3.0F, 9.0F).requiresTool(), UniformIntProvider.create(3, 5), locate("milestones/reveal_stratine"), Blocks.NETHERRACK.getDefaultState());
	public static final Block SHIMMERSTONE_BLOCK = new SparklestoneBlock(FabricBlockSettings.of(Material.GLASS, MapColor.YELLOW).strength(2.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 15));
	public static final Block AZURITE_BLOCK = new SpectrumFacingBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));
	public static final FloatBlock PALTAERIA_FRAGMENT_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.2F);
	public static final FloatBlock STRATINE_FRAGMENT_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.DARK_RED).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), -0.2F);
	public static final FloatBlock HOVER_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.0F);
	
	public static final Block BLACKSLAG_COAL_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_COAL_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE), UniformIntProvider.create(0, 2));
	public static final Block BLACKSLAG_COPPER_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_COPPER_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE));
	public static final Block BLACKSLAG_IRON_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE));
	public static final Block BLACKSLAG_GOLD_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_GOLD_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE));
	public static final Block BLACKSLAG_LAPIS_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE), UniformIntProvider.create(2, 5));
	public static final Block BLACKSLAG_DIAMOND_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_DIAMOND_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE), UniformIntProvider.create(3, 7));
	public static final Block BLACKSLAG_REDSTONE_ORE = new RedstoneOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_REDSTONE_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE));
	public static final Block BLACKSLAG_EMERALD_ORE = new OreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_EMERALD_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE));
	public static final Block BLACKSLAG_SHIMMERSTONE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(DEEPSLATE_SHIMMERSTONE_ORE).strength(6.0F, 5.0F).sounds(BlockSoundGroup.DEEPSLATE), UniformIntProvider.create(2, 4), locate("milestones/reveal_shimmerstone"), BLACKSLAG.getDefaultState());
	public static final Block BLACKSLAG_MALACHITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(DEEPSLATE_MALACHITE_ORE), UniformIntProvider.create(7, 11), locate("milestones/reveal_malachite"), BLACKSLAG.getDefaultState());
	
	// FUNCTIONAL BLOCKS
	public static final Block PRIVATE_CHEST = new PrivateChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE));
	public static final Block COMPACTING_CHEST = new CompactingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block RESTOCKING_CHEST = new RestockingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block BLACK_HOLE_CHEST = new BlackHoleChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block PARTICLE_SPAWNER = new ParticleSpawnerBlock(FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque());
	public static final Block CREATIVE_PARTICLE_SPAWNER = new CreativeParticleSpawnerBlock(FabricBlockSettings.copyOf(SpectrumBlocks.PARTICLE_SPAWNER).strength(-1.0F, 3600000.8F).dropsNothing());
	public static final Block BEDROCK_ANVIL = new BedrockAnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).requiresTool().strength(8.0F, 8.0F).sounds(BlockSoundGroup.METAL));
	
	// SOLID LIQUID CRYSTAL
	public static final Block FROSTBITE_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
	public static final Block BLAZING_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
	
	public static final Block RESONANT_LILY = new FlowerBlock(StatusEffects.INSTANT_HEALTH, 5, FabricBlockSettings.copyOf(Blocks.POPPY));
	public static final Block QUITOXIC_REEDS = new QuitoxicReedsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XYZ).ticksRandomly().luminance(state -> state.get(QuitoxicReedsBlock.LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? LiquidCrystalFluidBlock.LUMINANCE : 0));
	public static final Block MERMAIDS_BRUSH = new MermaidsBrushBlock(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).ticksRandomly().luminance(state -> state.get(MermaidsBrushBlock.LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? LiquidCrystalFluidBlock.LUMINANCE : 0));
	public static final Block RADIATING_ENDER = new RadiatingEnderBlock(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK));
	public static final Block AMARANTH = new AmaranthCropBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final Block AMARANTH_BUSHEL = new AmaranthBushelBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.CROP));
	
	public static final Block CRACKED_END_PORTAL_FRAME = new CrackedEndPortalFrameBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL_FRAME));
	public static final Block LAVA_SPONGE = new LavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.SPONGE));
	public static final Block WET_LAVA_SPONGE = new WetLavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.WET_SPONGE).luminance(9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block LIGHT_LEVEL_DETECTOR = new BlockLightDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block WEATHER_DETECTOR = new WeatherDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ITEM_DETECTOR = new ItemDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block PLAYER_DETECTOR = new PlayerDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ENTITY_DETECTOR = new EntityDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block REDSTONE_CALCULATOR = new RedstoneCalculatorBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_TIMER = new RedstoneTimerBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_WIRELESS = new RedstoneWirelessBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block BLOCK_PLACER = new BlockPlacerBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
	public static final Block BLOCK_DETECTOR = new BlockDetectorBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
	public static final EnderDropperBlock ENDER_DROPPER = new EnderDropperBlock(FabricBlockSettings.copyOf(Blocks.DROPPER).requiresTool().strength(15F, 60.0F));
	public static final Block ENDER_HOPPER = new EnderHopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER).requiresTool().strength(15F, 60.0F));
	
	public static final Block SPIRIT_SALLOW_LEAVES = new SpiritSallowLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 8));
	public static final Block SPIRIT_SALLOW_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_ROOTS = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_HEART = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).luminance(11));
	public static final Block SACRED_SOIL = new ExtraTickFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND));
	
	private static final FabricBlockSettings spiritVinesBlockSettings = FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.CAVE_VINES);
	public static final Block CYAN_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), BuiltinGemstoneColor.CYAN);
	public static final Block CYAN_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), BuiltinGemstoneColor.CYAN);
	public static final Block MAGENTA_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), BuiltinGemstoneColor.MAGENTA);
	public static final Block MAGENTA_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), BuiltinGemstoneColor.MAGENTA);
	public static final Block YELLOW_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), BuiltinGemstoneColor.YELLOW);
	public static final Block YELLOW_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), BuiltinGemstoneColor.YELLOW);
	public static final Block BLACK_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK);
	public static final Block BLACK_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), BuiltinGemstoneColor.BLACK);
	public static final Block WHITE_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE);
	public static final Block WHITE_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), BuiltinGemstoneColor.WHITE);
	
	public static final Block STUCK_STORM_STONE = new StormStoneBlock(FabricBlockSettings.of(Material.SNOW_LAYER, MapColor.PALE_YELLOW).noCollision().breakInstantly().sounds(BlockSoundGroup.SMALL_AMETHYST_BUD));
	public static final Block DEEPER_DOWN_PORTAL = new DeeperDownPortalBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL));
	
	public static final Block UPGRADE_SPEED = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 1, DyeColor.MAGENTA);
	public static final Block UPGRADE_SPEED2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 2, DyeColor.MAGENTA);
	public static final Block UPGRADE_SPEED3 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 8, DyeColor.MAGENTA);
	public static final Block UPGRADE_EFFICIENCY = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EFFICIENCY, 1, DyeColor.YELLOW);
	public static final Block UPGRADE_EFFICIENCY2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EFFICIENCY, 4, DyeColor.YELLOW);
	public static final Block UPGRADE_YIELD = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.YIELD, 1, DyeColor.CYAN);
	public static final Block UPGRADE_YIELD2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.YIELD, 4, DyeColor.CYAN);
	public static final Block UPGRADE_EXPERIENCE = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EXPERIENCE, 1, DyeColor.PURPLE);
	public static final Block UPGRADE_EXPERIENCE2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EXPERIENCE, 4, DyeColor.PURPLE);
	
	public static final Block REDSTONE_SAND = new RedstoneGravityBlock(FabricBlockSettings.copyOf(Blocks.SAND));
	public static final Block ENDER_GLASS = new RedstoneTransparencyBlock(FabricBlockSettings.copyOf(Blocks.GLASS).nonOpaque()
			.allowsSpawning((state, world, pos, entityType) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
			.solidBlock(SpectrumBlocks::never).suffocates((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
			.blockVision((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID));
	public static final Block CLOVER = new CloverBlock(FabricBlockSettings.copyOf(Blocks.GRASS).offsetType(AbstractBlock.OffsetType.XZ));
	public static final Block FOUR_LEAF_CLOVER = new FourLeafCloverBlock(FabricBlockSettings.copyOf(Blocks.GRASS).offsetType(AbstractBlock.OffsetType.XZ));
	public static final Block BLOOD_ORCHID = new BloodOrchidBlock(StatusEffects.BAD_OMEN, 10, FabricBlockSettings.copyOf(Blocks.POPPY).offsetType(AbstractBlock.OffsetType.NONE).ticksRandomly());
	
	private static final FabricBlockSettings gemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool();
	private static final UniformIntProvider gemOreExperienceProvider = UniformIntProvider.create(1, 4);
	public static final Block TOPAZ_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.CYAN, locate("hidden/collect_shards/collect_topaz_shard"), Blocks.STONE.getDefaultState());
	public static final Block AMETHYST_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.MAGENTA, locate("hidden/collect_shards/collect_amethyst_shard"), Blocks.STONE.getDefaultState());
	public static final Block CITRINE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.YELLOW, locate("hidden/collect_shards/collect_citrine_shard"), Blocks.STONE.getDefaultState());
	public static final Block ONYX_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.BLACK, locate("create_onyx_shard"), Blocks.STONE.getDefaultState());
	public static final Block MOONSTONE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.WHITE, locate("lategame/collect_moonstone_shard"), Blocks.STONE.getDefaultState());
	
	private static final FabricBlockSettings deepslateGemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE);
	public static final Block DEEPSLATE_TOPAZ_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.CYAN, locate("hidden/collect_shards/collect_topaz_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_AMETHYST_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.MAGENTA, locate("hidden/collect_shards/collect_amethyst_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_CITRINE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.YELLOW, locate("hidden/collect_shards/collect_citrine_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_ONYX_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.BLACK, locate("create_onyx_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_MOONSTONE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.WHITE, locate("lategame/collect_moonstone_shard"), Blocks.DEEPSLATE.getDefaultState());
	
	private static final FabricBlockSettings blackslagGemOreBlockSettings = FabricBlockSettings.copyOf(SpectrumBlocks.BLACKSLAG);
	public static final Block BLACKSLAG_TOPAZ_ORE = new GemstoneOreBlock(blackslagGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.CYAN, locate("hidden/collect_shards/collect_topaz_shard"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	public static final Block BLACKSLAG_AMETHYST_ORE = new GemstoneOreBlock(blackslagGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.MAGENTA, locate("hidden/collect_shards/collect_amethyst_shard"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	public static final Block BLACKSLAG_CITRINE_ORE = new GemstoneOreBlock(blackslagGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.YELLOW, locate("hidden/collect_shards/collect_citrine_shard"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	public static final Block BLACKSLAG_ONYX_ORE = new GemstoneOreBlock(blackslagGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.BLACK, locate("create_onyx_shard"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	public static final Block BLACKSLAG_MOONSTONE_ORE = new GemstoneOreBlock(blackslagGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.WHITE, locate("lategame/collect_moonstone_shard"), SpectrumBlocks.BLACKSLAG.getDefaultState());
	
	private static final FabricBlockSettings gemstoneStorageBlockSettings = FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F);
	public static final Block TOPAZ_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block AMETHYST_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block CITRINE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block ONYX_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block MOONSTONE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block SPECTRAL_SHARD_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	
	// COLORED TREES
	private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
	public static final Block BLACK_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.PURPLE);
	public static final Block RED_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.RED);
	public static final Block WHITE_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_SAPLING = new ColoredSaplingBlock(coloredSaplingBlockSettings, DyeColor.YELLOW);
	
	private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES);
	public static final Block BLACK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.PURPLE);
	public static final Block RED_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.RED);
	public static final Block WHITE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings, DyeColor.YELLOW);
	
	private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG);
	public static final Block BLACK_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.PURPLE);
	public static final Block RED_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.RED);
	public static final Block WHITE_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_LOG = new ColoredLogBlock(coloredLogBlockSettings, DyeColor.YELLOW);
	
	// POTTED PLANTS
	private static final Settings POTTED_PLANT_SETTINGS = AbstractBlock.Settings.of(Material.DECORATION).breakInstantly().nonOpaque();
	public static final Block POTTED_AMARANTH_BUSHEL = new PottedAmaranthBushelBlock(AMARANTH_BUSHEL, POTTED_PLANT_SETTINGS);
	public static final Block POTTED_BLOOD_ORCHID = new PottedBloodOrchidBlock(BLOOD_ORCHID, POTTED_PLANT_SETTINGS);
	public static final Block POTTED_BLACK_SAPLING = new PottedColoredSaplingBlock(BLACK_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.BLACK);
	public static final Block POTTED_BLUE_SAPLING = new PottedColoredSaplingBlock(BLUE_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.BLUE);
	public static final Block POTTED_BROWN_SAPLING = new PottedColoredSaplingBlock(BROWN_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.BROWN);
	public static final Block POTTED_CYAN_SAPLING = new PottedColoredSaplingBlock(CYAN_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.CYAN);
	public static final Block POTTED_GRAY_SAPLING = new PottedColoredSaplingBlock(GRAY_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.GRAY);
	public static final Block POTTED_GREEN_SAPLING = new PottedColoredSaplingBlock(GREEN_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.GREEN);
	public static final Block POTTED_LIGHT_BLUE_SAPLING = new PottedColoredSaplingBlock(LIGHT_BLUE_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.LIGHT_BLUE);
	public static final Block POTTED_LIGHT_GRAY_SAPLING = new PottedColoredSaplingBlock(LIGHT_GRAY_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.LIGHT_GRAY);
	public static final Block POTTED_LIME_SAPLING = new PottedColoredSaplingBlock(LIME_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.LIME);
	public static final Block POTTED_MAGENTA_SAPLING = new PottedColoredSaplingBlock(MAGENTA_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.MAGENTA);
	public static final Block POTTED_ORANGE_SAPLING = new PottedColoredSaplingBlock(ORANGE_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.ORANGE);
	public static final Block POTTED_PINK_SAPLING = new PottedColoredSaplingBlock(PINK_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.PINK);
	public static final Block POTTED_PURPLE_SAPLING = new PottedColoredSaplingBlock(PURPLE_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.PURPLE);
	public static final Block POTTED_RED_SAPLING = new PottedColoredSaplingBlock(RED_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.RED);
	public static final Block POTTED_WHITE_SAPLING = new PottedColoredSaplingBlock(WHITE_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.WHITE);
	public static final Block POTTED_YELLOW_SAPLING = new PottedColoredSaplingBlock(YELLOW_SAPLING, POTTED_PLANT_SETTINGS, DyeColor.YELLOW);
	
	// FLAT COLORED BLOCKS
	private static final FabricBlockSettings flatColoredBlockBlockSettings = FabricBlockSettings.of(Material.STONE).hardness(2.5F).requiresTool().luminance(1).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always);
	public static final Block BLACK_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.PURPLE);
	public static final Block RED_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.RED);
	public static final Block WHITE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings, DyeColor.YELLOW);
	
	// COLORED LAMPS
	private static final FabricBlockSettings coloredLampBlockBlockSettings = FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP);
	public static final Block BLACK_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.PURPLE);
	public static final Block RED_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.RED);
	public static final Block WHITE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings, DyeColor.YELLOW);
	
	// PIGMENT STORAGE BLOCKS
	private static final FabricBlockSettings pigmentStorageBlockBlockSettings = FabricBlockSettings.of(Material.WOOL).strength(1.0F).sounds(BlockSoundGroup.WOOL);
	public static final Block BLACK_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.PURPLE);
	public static final Block RED_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.RED);
	public static final Block WHITE_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_BLOCK = new PigmentBlock(pigmentStorageBlockBlockSettings, DyeColor.YELLOW);
	
	// SPORE BLOSSOMS
	private static final FabricBlockSettings sporeBlossomBlockSettings = FabricBlockSettings.copyOf(Blocks.SPORE_BLOSSOM);
	public static final Block BLACK_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.BLACK);
	public static final Block BLUE_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.BLUE);
	public static final Block BROWN_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.BROWN);
	public static final Block CYAN_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.CYAN);
	public static final Block GRAY_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.GRAY);
	public static final Block GREEN_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.GREEN);
	public static final Block LIGHT_BLUE_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.LIGHT_GRAY);
	public static final Block LIME_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.LIME);
	public static final Block MAGENTA_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.MAGENTA);
	public static final Block ORANGE_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.ORANGE);
	public static final Block PINK_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.PINK);
	public static final Block PURPLE_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.PURPLE);
	public static final Block RED_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.RED);
	public static final Block WHITE_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.WHITE);
	public static final Block YELLOW_SPORE_BLOSSOM = new ColoredSporeBlossomBlock(sporeBlossomBlockSettings, DyeColor.YELLOW);
	
	// SHIMMERSTONE LIGHTS
	private static final FabricBlockSettings shimmerstoneLightBlockSettings = FabricBlockSettings.of(Material.STONE).strength(1.0F, 1.0F).nonOpaque().luminance(15);
	public static final Block BASALT_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block CALCITE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block STONE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block GRANITE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block DIORITE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block ANDESITE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block DEEPSLATE_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	public static final Block BLACKSLAG_SHIMMERSTONE_LIGHT = new ShimmerstoneLightBlock(shimmerstoneLightBlockSettings);
	
	// CRYSTALLARIEUM
	public static final Block SMALL_COAL_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.COAL_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_COAL_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_COAL_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block COAL_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_COAL_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_COPPER_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.COPPER_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_COPPER_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_COPPER_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block COPPER_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_COPPER_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_DIAMOND_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.DIAMOND_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_DIAMOND_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_DIAMOND_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block DIAMOND_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_DIAMOND_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_EMERALD_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.EMERALD_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_EMERALD_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_EMERALD_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block EMERALD_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_EMERALD_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_GLOWSTONE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.GLOWSTONE.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_GLOWSTONE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOWSTONE_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block GLOWSTONE_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GLOWSTONE_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_GOLD_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.GOLD_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_GOLD_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GOLD_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block GOLD_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_GOLD_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_IRON_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.IRON_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_IRON_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_IRON_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block IRON_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_IRON_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_LAPIS_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.LAPIS_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_LAPIS_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_LAPIS_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block LAPIS_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_LAPIS_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_NETHERITE_SCRAP_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.ANCIENT_DEBRIS.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_NETHERITE_SCRAP_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_NETHERITE_SCRAP_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block NETHERITE_SCRAP_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_NETHERITE_SCRAP_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_ECHO_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.SCULK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_ECHO_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_ECHO_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block ECHO_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_ECHO_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_PRISMARINE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.SCULK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_PRISMARINE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_PRISMARINE_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block PRISMARINE_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_PRISMARINE_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_QUARTZ_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.QUARTZ_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_QUARTZ_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_QUARTZ_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block QUARTZ_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_QUARTZ_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	public static final Block SMALL_REDSTONE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).mapColor(Blocks.REDSTONE_BLOCK.getDefaultMapColor()).requiresTool().nonOpaque(), CrystallarieumGrowableBlock.GrowthStage.SMALL);
	public static final Block LARGE_REDSTONE_BUD = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_REDSTONE_BUD), CrystallarieumGrowableBlock.GrowthStage.LARGE);
	public static final Block REDSTONE_CLUSTER = new CrystallarieumGrowableBlock(FabricBlockSettings.copyOf(SMALL_REDSTONE_BUD), CrystallarieumGrowableBlock.GrowthStage.CLUSTER);
	
	public static final Block PURE_COAL_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.COAL_BLOCK));
	public static final Block PURE_IRON_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
	public static final Block PURE_GOLD_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK));
	public static final Block PURE_DIAMOND_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK));
	public static final Block PURE_EMERALD_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK));
	public static final Block PURE_REDSTONE_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final Block PURE_LAPIS_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));
	public static final Block PURE_COPPER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK));
	public static final Block PURE_QUARTZ_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK));
	public static final Block PURE_NETHERITE_SCRAP_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.ANCIENT_DEBRIS));
	public static final Block PURE_ECHO_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.SCULK));
	public static final Block PURE_GLOWSTONE_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
	public static final Block PURE_PRISMARINE_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.PRISMARINE));
	
	// STRUCTURE BLOCKS
	private static final AbstractBlock.Settings PRESERVATION_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(SpectrumBlocks::never);
	
	public static final Block PRESERVATION_CONTROLLER = new PreservationControllerBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().luminance(value -> 1).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block DIKE_GATE = new DikeGateBlock(FabricBlockSettings.of(Material.GLASS).strength(-1.0F, 3600000.0F).dropsNothing().luminance(value -> 3).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
	public static final Block INVISIBLE_WALL = new InvisibleWallBlock(FabricBlockSettings.of(Material.GLASS).strength(-1.0F, 3600000.0F).dropsNothing().luminance(value -> 3).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
	public static final Block TREASURE_CHEST = new TreasureChestBlock(FabricBlockSettings.copyOf(Blocks.CHEST).strength(-1.0F, 3600000.0F));
	
	public static final Block DOWNSTONE = new Block(PRESERVATION_BLOCK_SETTINGS); // "raw" preservation stone, used in the Deeper Down bottom in place of bedrock
	public static final Block PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block POWDER_CHISELED_PRESERVATION_STONE = new Block(FabricBlockSettings.copyOf(PRESERVATION_STONE).luminance(2));
	public static final Block DIKE_CHISELED_PRESERVATION_STONE = new Block(FabricBlockSettings.copyOf(PRESERVATION_STONE).luminance(6));
	public static final Block DIKE_GATE_FOUNTAIN = new SpectrumFacingBlock(PRESERVATION_BLOCK_SETTINGS);
	public static final Block PRESERVATION_BRICKS = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block SHIMMERING_PRESERVATION_BRICKS = new Block(FabricBlockSettings.copyOf(PRESERVATION_BLOCK_SETTINGS).luminance(5));
	public static final Block COURIER_STATUE = new StatueBlock(PRESERVATION_BLOCK_SETTINGS);
	
	public static final Block BLACK_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block BLUE_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block BROWN_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block CYAN_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block GRAY_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block GREEN_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block LIGHT_BLUE_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block LIGHT_GRAY_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block LIME_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block MAGENTA_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block ORANGE_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block PINK_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block PURPLE_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block RED_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block WHITE_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	public static final Block YELLOW_CHISELED_PRESERVATION_STONE = new Block(PRESERVATION_BLOCK_SETTINGS);
	
	public static final Block PRESERVATION_GLASS = new GlassBlock(FabricBlockSettings.of(Material.GLASS).strength(-1.0F, 3600000.0F).dropsNothing().sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
	public static final Block TINTED_PRESERVATION_GLASS = new GlassBlock(FabricBlockSettings.copyOf(PRESERVATION_GLASS).luminance(12).strength(Float.MAX_VALUE, 3600000.0F));
	public static final Block PRESERVATION_ROUNDEL = new PreservationRoundelBlock(FabricBlockSettings.copyOf(PRESERVATION_STONE).nonOpaque());
	public static final Block PRESERVATION_BLOCK_DETECTOR = new PreservationBlockDetectorBlock(FabricBlockSettings.copyOf(PRESERVATION_STONE));
	
	public static final BiMap<SpectrumSkullBlock.SpectrumSkullBlockType, Block> MOB_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.SpectrumSkullBlockType.class);
	public static final BiMap<SpectrumSkullBlock.SpectrumSkullBlockType, Block> MOB_WALL_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.SpectrumSkullBlockType.class);
	
	private static final FabricBlockSettings shootingStartBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque();
	public static final ShootingStarBlock GLISTERING_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GLISTERING);
	public static final ShootingStarBlock FIERY_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.FIERY);
	public static final ShootingStarBlock COLORFUL_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.COLORFUL);
	public static final ShootingStarBlock PRISTINE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.PRISTINE);
	public static final ShootingStarBlock GEMSTONE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GEMSTONE);
	
	public static final Block INCANDESCENT_AMALGAM = new IncandescentAmalgamBlock(FabricBlockSettings.of(Material.GLASS).breakInstantly().nonOpaque());
	
	public static final FabricBlockSettings mobBlockSettings = FabricBlockSettings.of(new Material.Builder(MapColor.BLUE).build()).sounds(BlockSoundGroup.BONE).strength(3.0F).nonOpaque();
	public static final Block AXOLOTL_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.AXOLOTL_MOB_BLOCK), ParticleTypes.HEART, StatusEffects.REGENERATION, 0, 100); // heals 2 hp / 1 heart
	public static final Block BAT_MOB_BLOCK = new AoEStatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.BAT_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, StatusEffects.GLOWING, 0, 200, 8);
	public static final Block BEE_MOB_BLOCK = new BonemealingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.BEE_MOB_BLOCK), ParticleTypes.DRIPPING_HONEY);
	public static final Block BLAZE_MOB_BLOCK = new FirestarterMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.BLAZE_MOB_BLOCK), ParticleTypes.FLAME);
	public static final Block CAT_MOB_BLOCK = new FallDamageNegatingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.CAT_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT);
	public static final Block CHICKEN_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.CHICKEN_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.SLOW_FALLING, 0, 100);
	public static final Block COW_MOB_BLOCK = new MilkingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.COW_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, 6);
	public static final Block CREEPER_MOB_BLOCK = new ExplosionMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.CREEPER_MOB_BLOCK), ParticleTypes.EXPLOSION, 3, false, Explosion.DestructionType.BREAK);
	public static final Block ENDER_DRAGON_MOB_BLOCK = new ProjectileMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ENDER_DRAGON_MOB_BLOCK), ParticleTypes.DRAGON_BREATH, EntityType.DRAGON_FIREBALL, SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER, world);
			markerEntity.setPos(position.getX(), position.getY(), position.getZ());
			
			Vec3d targetPosition = Vec3d.ofCenter(mobBlockPos.offset(side, 50));
			double f = targetPosition.getX() - markerEntity.getX();
			double g = targetPosition.getY() - markerEntity.getY();
			double h = targetPosition.getZ() - markerEntity.getZ();
			
			DragonFireballEntity entity = new DragonFireballEntity(world, markerEntity, f, g, h);
			
			markerEntity.discard();
			return entity;
		}
	};
	public static final Block ENDERMAN_MOB_BLOCK = new RandomTeleportingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ENDERMAN_MOB_BLOCK), ParticleTypes.REVERSE_PORTAL, 16, 16);
	public static final Block ENDERMITE_MOB_BLOCK = new LineTeleportingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ENDERMITE_MOB_BLOCK), ParticleTypes.REVERSE_PORTAL, 16);
	public static final Block EVOKER_MOB_BLOCK = new EntitySummoningMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.EVOKER_MOB_BLOCK), ParticleTypes.ANGRY_VILLAGER, EntityType.VEX) {
		@Override
		public void afterSummon(ServerWorld world, Entity entity) {
			((VexEntity) entity).setLifeTicks(20 * (30 + world.random.nextInt(90)));
		}
	};
	public static final Block FISH_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.FISH_MOB_BLOCK), ParticleTypes.SPLASH, StatusEffects.WATER_BREATHING, 0, 200);
	public static final Block FOX_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.FOX_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.HASTE, 0, 200);
	public static final Block GHAST_MOB_BLOCK = new ProjectileMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.GHAST_MOB_BLOCK), ParticleTypes.SMOKE, EntityType.FIREBALL, SoundEvents.ENTITY_GHAST_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			LivingMarkerEntity markerEntity = new LivingMarkerEntity(SpectrumEntityTypes.LIVING_MARKER, world);
			markerEntity.setPos(position.getX(), position.getY(), position.getZ());
			
			Vec3d targetPosition = Vec3d.ofCenter(mobBlockPos.offset(side, 50));
			double f = targetPosition.getX() - markerEntity.getX();
			double g = targetPosition.getY() - markerEntity.getY();
			double h = targetPosition.getZ() - markerEntity.getZ();
			
			FireballEntity entity = new FireballEntity(world, markerEntity, f, g, h, 1);
			
			markerEntity.discard();
			return entity;
		}
	};
	public static final Block GLOW_SQUID_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.GLOW_SQUID_MOB_BLOCK), ParticleTypes.GLOW_SQUID_INK, StatusEffects.GLOWING, 0, 200);
	public static final Block GOAT_MOB_BLOCK = new KnockbackMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.GOAT_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, 5.0F, 0.5F); // knocks mostly sideways
	public static final Block GUARDIAN_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.GUARDIAN_MOB_BLOCK), ParticleTypes.BUBBLE, StatusEffects.MINING_FATIGUE, 2, 200);
	public static final Block HORSE_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.HORSE_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, StatusEffects.STRENGTH, 0, 100);
	public static final Block ILLUSIONER_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ILLUSIONER_MOB_BLOCK), ParticleTypes.ANGRY_VILLAGER, StatusEffects.INVISIBILITY, 0, 100);
	public static final Block OCELOT_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.OCELOT_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, StatusEffects.NIGHT_VISION, 0, 100);
	public static final Block PARROT_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.PARROT_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, StatusEffects.ABSORPTION, 0, 100);
	public static final Block PHANTOM_MOB_BLOCK = new InsomniaMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.PHANTOM_MOB_BLOCK), ParticleTypes.POOF, 24000); // +1 ingame day without sleep
	public static final Block PIG_MOB_BLOCK = new FeedingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.PIG_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, 6);
	public static final Block PIGLIN_MOB_BLOCK = new PiglinTradeMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.PIGLIN_MOB_BLOCK), ParticleTypes.HEART);
	public static final Block POLAR_BEAR_MOB_BLOCK = new FreezingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.POLAR_BEAR_MOB_BLOCK), ParticleTypes.SNOWFLAKE);
	public static final Block PUFFERFISH_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.PUFFERFISH_MOB_BLOCK), ParticleTypes.SPLASH, StatusEffects.NAUSEA, 0, 200);
	public static final Block RABBIT_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.RABBIT_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, StatusEffects.JUMP_BOOST, 3, 100);
	public static final Block SHEEP_MOB_BLOCK = new ShearingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SHEEP_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, 6);
	public static final Block SHULKER_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SHULKER_MOB_BLOCK), ParticleTypes.END_ROD, StatusEffects.LEVITATION, 0, 100);
	public static final Block SILVERFISH_MOB_BLOCK = new SilverfishInsertingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SILVERFISH_MOB_BLOCK), ParticleTypes.EXPLOSION);
	public static final Block SKELETON_MOB_BLOCK = new ProjectileMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SKELETON_MOB_BLOCK), ParticleTypes.INSTANT_EFFECT, EntityType.ARROW, SoundEvents.ENTITY_ARROW_SHOOT, 6.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			ArrowEntity arrowEntity = new ArrowEntity(world, position.getX(), position.getY(), position.getZ());
			arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			return arrowEntity;
		}
	};
	public static final Block SLIME_MOB_BLOCK = new SlimeSizingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SLIME_MOB_BLOCK), ParticleTypes.ITEM_SLIME, 6, 8);
	public static final Block SNOW_GOLEM_MOB_BLOCK = new ProjectileMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SNOW_GOLEM_MOB_BLOCK), ParticleTypes.SNOWFLAKE, EntityType.SNOWBALL, SoundEvents.ENTITY_ARROW_SHOOT, 3.0F, 1.1F) {
		@Override
		public ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position position, Direction side) {
			world.playSound(null, mobBlockPos.getX(), mobBlockPos.getY(), mobBlockPos.getZ(), SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, SoundCategory.BLOCKS, 1.0F, 0.4F / world.random.nextFloat() * 0.4F + 0.8F);
			return new SnowballEntity(world, position.getX(), position.getY(), position.getZ());
		}
	};
	public static final Block SPIDER_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SPIDER_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.POISON, 0, 100);
	public static final Block SQUID_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.SQUID_MOB_BLOCK), ParticleTypes.SQUID_INK, StatusEffects.BLINDNESS, 0, 200);
	public static final Block STRAY_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.STRAY_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.SLOWNESS, 2, 100);
	public static final Block STRIDER_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.STRIDER_MOB_BLOCK), ParticleTypes.DRIPPING_LAVA, StatusEffects.FIRE_RESISTANCE, 0, 200);
	public static final Block TURTLE_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.TURTLE_MOB_BLOCK), ParticleTypes.DRIPPING_WATER, StatusEffects.RESISTANCE, 1, 200);
	public static final Block WITCH_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.WITCH_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.WEAKNESS, 0, 200);
	public static final Block WITHER_MOB_BLOCK = new ExplosionMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.WITHER_MOB_BLOCK), ParticleTypes.EXPLOSION, 7.0F, true, Explosion.DestructionType.BREAK);
	public static final Block WITHER_SKELETON_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.WITHER_SKELETON_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.WITHER, 0, 100);
	public static final Block ZOMBIE_MOB_BLOCK = new VillagerConvertingMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ZOMBIE_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT);
	
	static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}
	
	static boolean always(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
	static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}
	
	static void registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, locate(name), block);
	}
	
	static void registerBlockItem(String name, BlockItem blockItem, DyeColor dyeColor) {
		Registry.register(Registry.ITEM, locate(name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	static void registerBlockWithItem(String name, Block block, FabricItemSettings itemSettings, DyeColor dyeColor) {
		Registry.register(Registry.BLOCK, locate(name), block);
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registry.ITEM, locate(name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	static void registerBlockWithItem(String name, Block block, BlockItem blockItem, DyeColor dyeColor) {
		Registry.register(Registry.BLOCK, locate(name), block);
		Registry.register(Registry.ITEM, locate(name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	public static void register() {
		registerBlockWithItem("pedestal_basic_topaz", PEDESTAL_BASIC_TOPAZ, new PedestalBlockItem(PEDESTAL_BASIC_TOPAZ, Tab.GENERAL.settings(1), BuiltinPedestalVariant.BASIC_TOPAZ, "item.spectrum.pedestal.tooltip.basic_topaz"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_basic_amethyst", PEDESTAL_BASIC_AMETHYST, new PedestalBlockItem(PEDESTAL_BASIC_AMETHYST, Tab.GENERAL.settings(1), BuiltinPedestalVariant.BASIC_AMETHYST, "item.spectrum.pedestal.tooltip.basic_amethyst"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_basic_citrine", PEDESTAL_BASIC_CITRINE, new PedestalBlockItem(PEDESTAL_BASIC_CITRINE, Tab.GENERAL.settings(1), BuiltinPedestalVariant.BASIC_CITRINE, "item.spectrum.pedestal.tooltip.basic_citrine"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_all_basic", PEDESTAL_ALL_BASIC, new PedestalBlockItem(PEDESTAL_ALL_BASIC, Tab.GENERAL.settings(1), BuiltinPedestalVariant.CMY, "item.spectrum.pedestal.tooltip.all_basic"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_onyx", PEDESTAL_ONYX, new PedestalBlockItem(PEDESTAL_ONYX, Tab.GENERAL.settings(1), BuiltinPedestalVariant.ONYX, "item.spectrum.pedestal.tooltip.onyx"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_moonstone", PEDESTAL_MOONSTONE, new PedestalBlockItem(PEDESTAL_MOONSTONE, Tab.GENERAL.settings(1), BuiltinPedestalVariant.MOONSTONE, "item.spectrum.pedestal.tooltip.moonstone"), DyeColor.WHITE);
		registerBlockWithItem("fusion_shrine_basalt", FUSION_SHRINE_BASALT, Tab.GENERAL.settings(1), DyeColor.GRAY);
		registerBlockWithItem("fusion_shrine_calcite", FUSION_SHRINE_CALCITE, Tab.GENERAL.settings(1), DyeColor.GRAY);
		registerBlockWithItem("enchanter", ENCHANTER, Tab.GENERAL.settings(1), DyeColor.PURPLE);
		registerBlockWithItem("item_bowl_basalt", ITEM_BOWL_BASALT, Tab.GENERAL.settings(16), DyeColor.PINK);
		registerBlockWithItem("item_bowl_calcite", ITEM_BOWL_CALCITE, Tab.GENERAL.settings(16), DyeColor.PINK);
		registerBlockWithItem("item_roundel", ITEM_ROUNDEL, Tab.GENERAL.settings(16), DyeColor.PINK);
		registerBlockWithItem("potion_workshop", POTION_WORKSHOP, Tab.GENERAL.settings(1), DyeColor.PURPLE);
		registerBlockWithItem("spirit_instiller", SPIRIT_INSTILLER, Tab.GENERAL.settings(1), DyeColor.WHITE);
		registerBlockWithItem("crystallarieum", CRYSTALLARIEUM, Tab.GENERAL.settings(1), DyeColor.BROWN);
		registerBlockWithItem("cinderhearth", CINDERHEARTH, Tab.GENERAL.settings(1).fireproof(), DyeColor.ORANGE);
		registerBlockWithItem("crystal_apothecary", CRYSTAL_APOTHECARY, Tab.GENERAL.settings(8), DyeColor.GREEN);
		registerBlockWithItem("color_picker", COLOR_PICKER, Tab.GENERAL.settings(8), DyeColor.GREEN);
		registerBlockWithItem("inkwell", INKWELL, Tab.GENERAL.settings(8), DyeColor.GREEN);
		registerBlockWithItem("ink_duct", INK_DUCT, Tab.GENERAL.settings(8), DyeColor.GREEN);
		
		registerBlockWithItem("upgrade_speed", UPGRADE_SPEED, new UpgradeBlockItem(UPGRADE_SPEED, Tab.GENERAL.settings(8), "upgrade_speed"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_speed2", UPGRADE_SPEED2, new UpgradeBlockItem(UPGRADE_SPEED2, Tab.GENERAL.settings(8, Rarity.UNCOMMON), "upgrade_speed2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_speed3", UPGRADE_SPEED3, new UpgradeBlockItem(UPGRADE_SPEED3, Tab.GENERAL.settings(8, Rarity.RARE), "upgrade_speed3"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_efficiency", UPGRADE_EFFICIENCY, new UpgradeBlockItem(UPGRADE_EFFICIENCY, Tab.GENERAL.settings(8, Rarity.UNCOMMON), "upgrade_efficiency"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_efficiency2", UPGRADE_EFFICIENCY2, new UpgradeBlockItem(UPGRADE_EFFICIENCY2, Tab.GENERAL.settings(8, Rarity.RARE), "upgrade_efficiency2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_yield", UPGRADE_YIELD, new UpgradeBlockItem(UPGRADE_YIELD, Tab.GENERAL.settings(8, Rarity.UNCOMMON), "upgrade_yield"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_yield2", UPGRADE_YIELD2, new UpgradeBlockItem(UPGRADE_YIELD2, Tab.GENERAL.settings(8, Rarity.RARE), "upgrade_yield2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_experience", UPGRADE_EXPERIENCE, new UpgradeBlockItem(UPGRADE_EXPERIENCE, Tab.GENERAL.settings(8), "upgrade_experience"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_experience2", UPGRADE_EXPERIENCE2, new UpgradeBlockItem(UPGRADE_EXPERIENCE2, Tab.GENERAL.settings(8, Rarity.UNCOMMON), "upgrade_experience2"), DyeColor.LIGHT_GRAY);
		
		registerBlockWithItem("memory", MEMORY, new MemoryItem(MEMORY, Tab.GENERAL.settings(Rarity.UNCOMMON)), DyeColor.LIGHT_GRAY);
		
		
		registerPastelNetworkNodes(Tab.GENERAL.settings(16));
		registerStoneBlocks(Tab.DECORATION.settings());
		registerGemBlocks(Tab.RESOURCES.settings());
		
		registerShootingStarBlocks(Tab.RESOURCES.settings(1, Rarity.UNCOMMON));
		
		registerGemOreBlocks(Tab.RESOURCES.settings());
		registerOreBlocks(Tab.RESOURCES.settings(), Tab.RESOURCES.settings().fireproof());
		registerOreStorageBlocks(Tab.DECORATION.settings(), Tab.DECORATION.settings().fireproof());
		registerGemstoneLamps(Tab.DECORATION.settings());
		registerSparklestoneLights(Tab.DECORATION.settings());
		registerRunes(Tab.DECORATION.settings());
		registerGemstoneGlass(Tab.DECORATION.settings());
		registerPlayerOnlyGlass(Tab.GENERAL.settings());
		registerGemstoneChimes(Tab.DECORATION.settings());
		registerDecoStones(Tab.DECORATION.settings());
		registerPigmentStorageBlocks(Tab.DECORATION.settings());
		registerColoredLamps(Tab.DECORATION.settings());
		registerGlowBlocks(Tab.DECORATION.settings());
		registerSporeBlossoms(Tab.DECORATION.settings());
		registerColoredWood(Tab.COLORED_WOOD.settings());
		registerDDFlora(Tab.DECORATION.settings());
		registerRedstone(Tab.GENERAL.settings());
		registerMagicalBlocks(Tab.GENERAL.settings());
		registerMobBlocks(Tab.MOB_HEADS.settings());
		registerMobHeads(Tab.MOB_HEADS.settings());
		registerCrystallarieumGrowingBlocks(Tab.RESOURCES.settings());
		registerPureOreBlocks(Tab.RESOURCES.settings());
		registerJadeVineBlocks(Tab.DECORATION.settings());
		registerStructureBlocks(Tab.DECORATION.settings());
		registerSugarSticks(Tab.CONSUMABLES.settings());
		registerSpiritTree(Tab.GENERAL.settings());
		
		// Decay
		registerBlock("fading", FADING);
		registerBlock("failing", FAILING);
		registerBlock("ruin", RUIN);
		registerBlock("forfeiture", FORFEITURE);
		registerBlock("decay_away", DECAY_AWAY);
		
		// Fluids + Products
		registerBlock("mud", MUD);
		registerBlock("liquid_crystal", LIQUID_CRYSTAL);
		registerBlock("midnight_solution", MIDNIGHT_SOLUTION);
		registerBlock("dragonrot", DRAGONROT);
		
		registerBlockWithItem("black_materia", BLACK_MATERIA, Tab.GENERAL.settings(), DyeColor.GRAY);
		registerBlockWithItem("frostbite_crystal", FROSTBITE_CRYSTAL, Tab.GENERAL.settings(), DyeColor.LIGHT_BLUE);
		registerBlockWithItem("blazing_crystal", BLAZING_CRYSTAL, Tab.GENERAL.settings(), DyeColor.ORANGE);
		registerBlockWithItem("resonant_lily", RESONANT_LILY, Tab.GENERAL.settings(), DyeColor.GREEN);
		registerBlockWithItem("clover", CLOVER, Tab.RESOURCES.settings(), DyeColor.LIME);
		registerBlockWithItem("four_leaf_clover", FOUR_LEAF_CLOVER, new FourLeafCloverItem(FOUR_LEAF_CLOVER, Tab.RESOURCES.settings(), locate("milestones/reveal_four_leaf_clover"), CLOVER.asItem()), DyeColor.LIME);
		registerBlockWithItem("incandescent_amalgam", INCANDESCENT_AMALGAM, new IncandescentAmalgamItem(INCANDESCENT_AMALGAM, Tab.EQUIPMENT.settings(16).food(SpectrumFoodComponents.INCANDESCENT_AMALGAM)), DyeColor.RED);
		
		registerBlockWithItem("blood_orchid", BLOOD_ORCHID, Tab.RESOURCES.settings(), DyeColor.RED);
		registerBlock("potted_blood_orchid", POTTED_BLOOD_ORCHID);
		
		// Worldgen
		registerBlockWithItem("quitoxic_reeds", QUITOXIC_REEDS, Tab.RESOURCES.settings(), DyeColor.PURPLE);
		registerBlockWithItem("radiating_ender", RADIATING_ENDER, Tab.RESOURCES.settings(), DyeColor.PURPLE);
		
		registerBlock("amaranth", AMARANTH);
		registerBlockWithItem("amaranth_bushel", AMARANTH_BUSHEL, Tab.RESOURCES.settings(), DyeColor.RED);
		registerBlock("potted_amaranth_bushel", POTTED_AMARANTH_BUSHEL);
		
		registerBlockWithItem("bedrock_anvil", BEDROCK_ANVIL, Tab.GENERAL.settings(), DyeColor.BLACK);
		registerBlockWithItem("cracked_end_portal_frame", CRACKED_END_PORTAL_FRAME, Tab.GENERAL.settings(), DyeColor.PURPLE);
		
		registerBlockWithItem("black_sludge", BLACK_SLUDGE, Tab.DECORATION.settings(), DyeColor.GRAY);
		
		// Technical Blocks without items
		registerBlock("mermaids_brush", MERMAIDS_BRUSH);
		registerBlock("sag_leaf", SAG_LEAF);
		registerBlock("sag_bubble", SAG_BUBBLE);
		registerBlock("small_sag_bubble", SMALL_SAG_BUBBLE);
		
		registerBlock("primordial_fire", PRIMORDIAL_FIRE);
		registerBlock("deeper_down_portal", DEEPER_DOWN_PORTAL);
		registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
		registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);
		registerBlock("stuck_storm_stone", STUCK_STORM_STONE);
		registerBlock("wand_light", WAND_LIGHT_BLOCK);
		registerBlock("decaying_light", DECAYING_LIGHT_BLOCK);
		registerBlock("block_flooder", BLOCK_FLOODER);
		registerBlock("bottomless_bundle", BOTTOMLESS_BUNDLE);
	}
	
	private static void registerDDFlora(FabricItemSettings settings) {
		registerBlockWithItem("sawblade_grass", SAWBLADE_GRASS, settings, DyeColor.LIME);
		registerBlockWithItem("overgrown_blackslag", OVERGROWN_BLACKSLAG, settings, DyeColor.LIME);
		registerBlockWithItem("shimmel", SHIMMEL, settings, DyeColor.LIME);
		registerBlockWithItem("rotten_ground", ROTTEN_GROUND, settings, DyeColor.LIME);
		
		registerBlockWithItem("slate_noxshroom", SLATE_NOXSHROOM, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxcap_cap", SLATE_NOXCAP_CAP, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxcap_stem", SLATE_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("stripped_slate_noxcap_stem", STRIPPED_SLATE_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxcap_gills", SLATE_NOXCAP_GILLS, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_planks", SLATE_NOXWOOD_PLANKS, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_stairs", SLATE_NOXWOOD_STAIRS, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_slab", SLATE_NOXWOOD_SLAB, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_fence", SLATE_NOXWOOD_FENCE, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_fence_gate", SLATE_NOXWOOD_FENCE_GATE, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_door", SLATE_NOXWOOD_DOOR, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_trapdoor", SLATE_NOXWOOD_TRAPDOOR, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_pressure_plate", SLATE_NOXWOOD_PRESSURE_PLATE, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_button", SLATE_NOXWOOD_BUTTON, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_beam", SLATE_NOXWOOD_BEAM, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_amphora", SLATE_NOXWOOD_AMPHORA, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_lantern", SLATE_NOXWOOD_LANTERN, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_light", SLATE_NOXWOOD_LIGHT, settings, DyeColor.LIME);
		registerBlockWithItem("slate_noxwood_lamp", SLATE_NOXWOOD_LAMP, settings, DyeColor.LIME);
		
		registerBlockWithItem("ebony_noxshroom", EBONY_NOXSHROOM, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxcap_cap", EBONY_NOXCAP_CAP, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxcap_stem", EBONY_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("stripped_ebony_noxcap_stem", STRIPPED_EBONY_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxcap_gills", EBONY_NOXCAP_GILLS, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_planks", EBONY_NOXWOOD_PLANKS, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_stairs", EBONY_NOXWOOD_STAIRS, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_slab", EBONY_NOXWOOD_SLAB, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_fence", EBONY_NOXWOOD_FENCE, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_fence_gate", EBONY_NOXWOOD_FENCE_GATE, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_door", EBONY_NOXWOOD_DOOR, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_trapdoor", EBONY_NOXWOOD_TRAPDOOR, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_pressure_plate", EBONY_NOXWOOD_PRESSURE_PLATE, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_button", EBONY_NOXWOOD_BUTTON, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_beam", EBONY_NOXWOOD_BEAM, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_amphora", EBONY_NOXWOOD_AMPHORA, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_lantern", EBONY_NOXWOOD_LANTERN, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_light", EBONY_NOXWOOD_LIGHT, settings, DyeColor.LIME);
		registerBlockWithItem("ebony_noxwood_lamp", EBONY_NOXWOOD_LAMP, settings, DyeColor.LIME);
		
		registerBlockWithItem("ivory_noxshroom", IVORY_NOXSHROOM, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxcap_cap", IVORY_NOXCAP_CAP, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxcap_stem", IVORY_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("stripped_ivory_noxcap_stem", STRIPPED_IVORY_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxcap_gills", IVORY_NOXCAP_GILLS, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_planks", IVORY_NOXWOOD_PLANKS, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_stairs", IVORY_NOXWOOD_STAIRS, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_slab", IVORY_NOXWOOD_SLAB, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_fence", IVORY_NOXWOOD_FENCE, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_fence_gate", IVORY_NOXWOOD_FENCE_GATE, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_door", IVORY_NOXWOOD_DOOR, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_trapdoor", IVORY_NOXWOOD_TRAPDOOR, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_pressure_plate", IVORY_NOXWOOD_PRESSURE_PLATE, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_button", IVORY_NOXWOOD_BUTTON, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_beam", IVORY_NOXWOOD_BEAM, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_amphora", IVORY_NOXWOOD_AMPHORA, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_lantern", IVORY_NOXWOOD_LANTERN, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_light", IVORY_NOXWOOD_LIGHT, settings, DyeColor.LIME);
		registerBlockWithItem("ivory_noxwood_lamp", IVORY_NOXWOOD_LAMP, settings, DyeColor.LIME);
		
		registerBlockWithItem("chestnut_noxshroom", CHESTNUT_NOXSHROOM, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxcap_cap", CHESTNUT_NOXCAP_CAP, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxcap_stem", CHESTNUT_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("stripped_chestnut_noxcap_stem", STRIPPED_CHESTNUT_NOXCAP_STEM, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxcap_gills", CHESTNUT_NOXCAP_GILLS, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_planks", CHESTNUT_NOXWOOD_PLANKS, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_stairs", CHESTNUT_NOXWOOD_STAIRS, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_slab", CHESTNUT_NOXWOOD_SLAB, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_fence", CHESTNUT_NOXWOOD_FENCE, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_fence_gate", CHESTNUT_NOXWOOD_FENCE_GATE, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_door", CHESTNUT_NOXWOOD_DOOR, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_trapdoor", CHESTNUT_NOXWOOD_TRAPDOOR, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_pressure_plate", CHESTNUT_NOXWOOD_PRESSURE_PLATE, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_button", CHESTNUT_NOXWOOD_BUTTON, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_beam", CHESTNUT_NOXWOOD_BEAM, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_amphora", CHESTNUT_NOXWOOD_AMPHORA, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_lantern", CHESTNUT_NOXWOOD_LANTERN, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_light", CHESTNUT_NOXWOOD_LIGHT, settings, DyeColor.LIME);
		registerBlockWithItem("chestnut_noxwood_lamp", CHESTNUT_NOXWOOD_LAMP, settings, DyeColor.LIME);
		
		registerBlockWithItem("small_red_dragonjag", SMALL_RED_DRAGONJAG, settings, DyeColor.LIME);
		registerBlockWithItem("small_yellow_dragonjag", SMALL_YELLOW_DRAGONJAG, settings, DyeColor.LIME);
		registerBlockWithItem("small_pink_dragonjag", SMALL_PINK_DRAGONJAG, settings, DyeColor.LIME);
		registerBlockWithItem("small_purple_dragonjag", SMALL_PURPLE_DRAGONJAG, settings, DyeColor.LIME);
		registerBlockWithItem("small_black_dragonjag", SMALL_BLACK_DRAGONJAG, settings, DyeColor.LIME);
		registerBlock("tall_red_dragonjag", TALL_RED_DRAGONJAG);
		registerBlock("tall_yellow_dragonjag", TALL_YELLOW_DRAGONJAG);
		registerBlock("tall_pink_dragonjag", TALL_PINK_DRAGONJAG);
		registerBlock("tall_purple_dragonjag", TALL_PURPLE_DRAGONJAG);
		registerBlock("tall_black_dragonjag", TALL_BLACK_DRAGONJAG);
		
		registerBlock("aloe", ALOE);
		registerBlock("sawblade_holly_bush", SAWBLADE_HOLLY_BUSH);
		registerBlockWithItem("bristle_sprouts", BRISTLE_SPROUTS, settings, DyeColor.LIME);
		registerBlockWithItem("doombloom", DOOMBLOOM, settings, DyeColor.BLACK);
		registerBlockWithItem("snapping_ivy", SNAPPING_IVY, settings, DyeColor.RED);
		
		registerBlockWithItem("hummingstone", HUMMINGSTONE, settings, DyeColor.LIME);
		registerBlockWithItem("hummingstone_glass", HUMMINGSTONE_GLASS, settings, DyeColor.LIME);
	}
	
	private static void registerCrystallarieumGrowingBlocks(FabricItemSettings settings) {
		// vanilla
		registerBlockWithItem("small_coal_bud", SMALL_COAL_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_coal_bud", LARGE_COAL_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("coal_cluster", COAL_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_iron_bud", SMALL_IRON_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_iron_bud", LARGE_IRON_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("iron_cluster", IRON_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_gold_bud", SMALL_GOLD_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_gold_bud", LARGE_GOLD_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("gold_cluster", GOLD_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_diamond_bud", SMALL_DIAMOND_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("large_diamond_bud", LARGE_DIAMOND_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("diamond_cluster", DIAMOND_CLUSTER, settings, DyeColor.CYAN);
		
		registerBlockWithItem("small_emerald_bud", SMALL_EMERALD_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("large_emerald_bud", LARGE_EMERALD_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("emerald_cluster", EMERALD_CLUSTER, settings, DyeColor.CYAN);
		
		registerBlockWithItem("small_redstone_bud", SMALL_REDSTONE_BUD, settings, DyeColor.RED);
		registerBlockWithItem("large_redstone_bud", LARGE_REDSTONE_BUD, settings, DyeColor.RED);
		registerBlockWithItem("redstone_cluster", REDSTONE_CLUSTER, settings, DyeColor.RED);
		
		registerBlockWithItem("small_lapis_bud", SMALL_LAPIS_BUD, settings, DyeColor.PURPLE);
		registerBlockWithItem("large_lapis_bud", LARGE_LAPIS_BUD, settings, DyeColor.PURPLE);
		registerBlockWithItem("lapis_cluster", LAPIS_CLUSTER, settings, DyeColor.PURPLE);
		
		registerBlockWithItem("small_copper_bud", SMALL_COPPER_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_copper_bud", LARGE_COPPER_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("copper_cluster", COPPER_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_quartz_bud", SMALL_QUARTZ_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_quartz_bud", LARGE_QUARTZ_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("quartz_cluster", QUARTZ_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_netherite_scrap_bud", SMALL_NETHERITE_SCRAP_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_netherite_scrap_bud", LARGE_NETHERITE_SCRAP_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("netherite_scrap_cluster", NETHERITE_SCRAP_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_echo_bud", SMALL_ECHO_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("large_echo_bud", LARGE_ECHO_BUD, settings, DyeColor.BROWN);
		registerBlockWithItem("echo_cluster", ECHO_CLUSTER, settings, DyeColor.BROWN);
		
		registerBlockWithItem("small_glowstone_bud", SMALL_GLOWSTONE_BUD, settings, DyeColor.YELLOW);
		registerBlockWithItem("large_glowstone_bud", LARGE_GLOWSTONE_BUD, settings, DyeColor.YELLOW);
		registerBlockWithItem("glowstone_cluster", GLOWSTONE_CLUSTER, settings, DyeColor.YELLOW);
		
		registerBlockWithItem("small_prismarine_bud", SMALL_PRISMARINE_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("large_prismarine_bud", LARGE_PRISMARINE_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("prismarine_cluster", PRISMARINE_CLUSTER, settings, DyeColor.CYAN);
	}
	
	private static void registerRedstone(FabricItemSettings settings) {
		registerBlockWithItem("light_level_detector", LIGHT_LEVEL_DETECTOR, settings, DyeColor.RED);
		registerBlockWithItem("weather_detector", WEATHER_DETECTOR, settings, DyeColor.RED);
		registerBlockWithItem("item_detector", ITEM_DETECTOR, settings, DyeColor.RED);
		registerBlockWithItem("player_detector", PLAYER_DETECTOR, settings, DyeColor.RED);
		registerBlockWithItem("entity_detector", ENTITY_DETECTOR, settings, DyeColor.RED);
		
		registerBlockWithItem("redstone_timer", REDSTONE_TIMER, settings, DyeColor.RED);
		registerBlockWithItem("redstone_calculator", REDSTONE_CALCULATOR, settings, DyeColor.RED);
		registerBlockWithItem("redstone_wireless", REDSTONE_WIRELESS, settings, DyeColor.RED);
		
		registerBlockWithItem("redstone_sand", REDSTONE_SAND, settings, DyeColor.RED);
		registerBlockWithItem("ender_glass", ENDER_GLASS, settings, DyeColor.PURPLE);
		registerBlockWithItem("block_placer", BLOCK_PLACER, settings, DyeColor.CYAN);
		registerBlockWithItem("block_detector", BLOCK_DETECTOR, settings, DyeColor.CYAN);
	}
	
	private static void registerMagicalBlocks(FabricItemSettings settings) {
		registerBlockWithItem("private_chest", PRIVATE_CHEST, settings, DyeColor.BLUE);
		registerBlockWithItem("compacting_chest", COMPACTING_CHEST, settings, DyeColor.YELLOW);
		registerBlockWithItem("restocking_chest", RESTOCKING_CHEST, settings, DyeColor.YELLOW);
		registerBlockWithItem("black_hole_chest", BLACK_HOLE_CHEST, settings, DyeColor.LIGHT_GRAY);
		
		registerBlockWithItem("ender_hopper", ENDER_HOPPER, settings, DyeColor.PURPLE);
		registerBlockWithItem("ender_dropper", ENDER_DROPPER, settings, DyeColor.PURPLE);
		registerBlockWithItem("particle_spawner", PARTICLE_SPAWNER, settings, DyeColor.PINK);
		registerBlockWithItem("creative_particle_spawner", CREATIVE_PARTICLE_SPAWNER, new BlockItem(CREATIVE_PARTICLE_SPAWNER, Tab.GENERAL.settings(Rarity.EPIC)), DyeColor.PINK);
		
		registerBlockWithItem("glistering_melon", GLISTERING_MELON, settings, DyeColor.LIME);
		
		registerBlockWithItem("lava_sponge", LAVA_SPONGE, settings, DyeColor.ORANGE);
		registerBlockWithItem("wet_lava_sponge", WET_LAVA_SPONGE, new WetLavaSpongeItem(WET_LAVA_SPONGE, Tab.GENERAL.settings(1).recipeRemainder(LAVA_SPONGE.asItem())), DyeColor.ORANGE);
		
		registerBlockWithItem("ethereal_platform", ETHEREAL_PLATFORM, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("universe_spyhole", UNIVERSE_SPYHOLE, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("present", PRESENT, new PresentItem(PRESENT, Tab.GENERAL.settings(1)), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("titration_barrel", TITRATION_BARREL, Tab.GENERAL.settings(), DyeColor.MAGENTA);
	}
	
	private static void registerPigmentStorageBlocks(FabricItemSettings settings) {
		registerBlockWithItem("white_block", WHITE_BLOCK, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_block", ORANGE_BLOCK, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_block", MAGENTA_BLOCK, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_block", LIGHT_BLUE_BLOCK, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_block", YELLOW_BLOCK, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_block", LIME_BLOCK, settings, DyeColor.LIME);
		registerBlockWithItem("pink_block", PINK_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("gray_block", GRAY_BLOCK, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_block", LIGHT_GRAY_BLOCK, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_block", CYAN_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_block", PURPLE_BLOCK, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_block", BLUE_BLOCK, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_block", BROWN_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("green_block", GREEN_BLOCK, settings, DyeColor.GREEN);
		registerBlockWithItem("red_block", RED_BLOCK, settings, DyeColor.RED);
		registerBlockWithItem("black_block", BLACK_BLOCK, settings, DyeColor.BLACK);
	}
	
	private static void registerSpiritTree(FabricItemSettings settings) {
		registerBlockWithItem("ominous_sapling", OMINOUS_SAPLING, new OminousSaplingBlockItem(OMINOUS_SAPLING, settings), DyeColor.GREEN);
		
		registerBlockWithItem("spirit_sallow_roots", SPIRIT_SALLOW_ROOTS, settings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_log", SPIRIT_SALLOW_LOG, settings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_leaves", SPIRIT_SALLOW_LEAVES, settings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_heart", SPIRIT_SALLOW_HEART, settings, DyeColor.GREEN);
		
		registerBlock("cyan_spirit_sallow_vines_head", CYAN_SPIRIT_SALLOW_VINES_HEAD);
		registerBlock("magenta_spirit_sallow_vines_head", MAGENTA_SPIRIT_SALLOW_VINES_HEAD);
		registerBlock("yellow_spirit_sallow_vines_head", YELLOW_SPIRIT_SALLOW_VINES_HEAD);
		registerBlock("black_spirit_sallow_vines_head", BLACK_SPIRIT_SALLOW_VINES_HEAD);
		registerBlock("white_spirit_sallow_vines_head", WHITE_SPIRIT_SALLOW_VINES_HEAD);
		
		registerBlock("cyan_spirit_sallow_vines_body", CYAN_SPIRIT_SALLOW_VINES_BODY);
		registerBlock("magenta_spirit_sallow_vines_body", MAGENTA_SPIRIT_SALLOW_VINES_BODY);
		registerBlock("yellow_spirit_sallow_vines_body", YELLOW_SPIRIT_SALLOW_VINES_BODY);
		registerBlock("black_spirit_sallow_vines_body", BLACK_SPIRIT_SALLOW_VINES_BODY);
		registerBlock("white_spirit_sallow_vines_body", WHITE_SPIRIT_SALLOW_VINES_BODY);
		
		registerBlockWithItem("sacred_soil", SACRED_SOIL, settings, DyeColor.LIME);
	}
	
	private static void registerOreBlocks(FabricItemSettings settings, FabricItemSettings settingsFireproof) {
		registerBlockWithItem("shimmerstone_ore", SHIMMERSTONE_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_shimmerstone_ore", DEEPSLATE_SHIMMERSTONE_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("blackslag_shimmerstone_ore", BLACKSLAG_SHIMMERSTONE_ORE, settings, DyeColor.YELLOW);
		
		registerBlockWithItem("azurite_ore", AZURITE_ORE, settings, DyeColor.BLUE);
		registerBlockWithItem("deepslate_azurite_ore", DEEPSLATE_AZURITE_ORE, settings, DyeColor.BLUE);
		registerBlockWithItem("blackslag_azurite_ore", BLACKSLAG_AZURITE_ORE, settings, DyeColor.BLUE);
		
		registerBlockWithItem("stratine_ore", STRATINE_ORE, new FloatBlockItem(STRATINE_ORE, settingsFireproof, 1.01F), DyeColor.RED);
		registerBlockWithItem("paltaeria_ore", PALTAERIA_ORE, new FloatBlockItem(PALTAERIA_ORE, settings, 0.99F), DyeColor.CYAN);
		
		registerBlockWithItem("small_bismuth_bud", SMALL_BISMUTH_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.CYAN);
		registerBlockWithItem("large_bismuth_bud", LARGE_BISMUTH_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.CYAN);
		registerBlockWithItem("bismuth_cluster", BISMUTH_CLUSTER, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.CYAN);
		registerBlockWithItem("bismuth_block", BISMUTH_BLOCK, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.CYAN);
		
		registerBlockWithItem("malachite_ore", MALACHITE_ORE, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("deepslate_malachite_ore", DEEPSLATE_MALACHITE_ORE, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("blackslag_malachite_ore", BLACKSLAG_MALACHITE_ORE, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("small_malachite_bud", SMALL_MALACHITE_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("large_malachite_bud", LARGE_MALACHITE_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("malachite_cluster", MALACHITE_CLUSTER, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("malachite_block", MALACHITE_BLOCK, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.GREEN);
		
		registerBlockWithItem("azurite_cluster", AZURITE_CLUSTER, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.BLUE);
		registerBlockWithItem("large_azurite_bud", LARGE_AZURITE_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.BLUE);
		registerBlockWithItem("small_azurite_bud", SMALL_AZURITE_BUD, Tab.RESOURCES.settings(Rarity.UNCOMMON), DyeColor.BLUE);
		
		registerBlockWithItem("blackslag_coal_ore", BLACKSLAG_COAL_ORE, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_copper_ore", BLACKSLAG_COPPER_ORE, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_iron_ore", BLACKSLAG_IRON_ORE, settings, DyeColor.BROWN);
		registerBlockWithItem("blackslag_gold_ore", BLACKSLAG_GOLD_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("blackslag_diamond_ore", BLACKSLAG_DIAMOND_ORE, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("blackslag_redstone_ore", BLACKSLAG_REDSTONE_ORE, settings, DyeColor.RED);
		registerBlockWithItem("blackslag_lapis_ore", BLACKSLAG_LAPIS_ORE, settings, DyeColor.BLUE);
		registerBlockWithItem("blackslag_emerald_ore", BLACKSLAG_EMERALD_ORE, settings, DyeColor.LIME);
	}
	
	private static void registerOreStorageBlocks(FabricItemSettings settings, FabricItemSettings settingsFireproof) {
		registerBlockWithItem("topaz_storage_block", TOPAZ_STORAGE_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_storage_block", AMETHYST_STORAGE_BLOCK, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_storage_block", CITRINE_STORAGE_BLOCK, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_storage_block", ONYX_STORAGE_BLOCK, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_storage_block", MOONSTONE_STORAGE_BLOCK, settings, DyeColor.WHITE);
		registerBlockWithItem("spectral_shard_storage_block", SPECTRAL_SHARD_STORAGE_BLOCK, Tab.DECORATION.settings(Rarity.RARE), DyeColor.WHITE);
		
		registerBlockWithItem("bedrock_storage_block", BEDROCK_STORAGE_BLOCK, Tab.DECORATION.settings(Rarity.UNCOMMON), DyeColor.BLACK);
		registerBlockWithItem("spectral_shard_block", SPECTRAL_SHARD_BLOCK, Tab.DECORATION.settings(Rarity.RARE), DyeColor.WHITE);
		
		registerBlockWithItem("azurite_block", AZURITE_BLOCK, Tab.DECORATION.settings(), DyeColor.BLUE);
		registerBlockWithItem("shimmerstone_block", SHIMMERSTONE_BLOCK, Tab.DECORATION.settings(), DyeColor.YELLOW);
		registerBlockWithItem("stratine_fragment_block", STRATINE_FRAGMENT_BLOCK, new FloatBlockItem(STRATINE_FRAGMENT_BLOCK, settingsFireproof, 1.02F), DyeColor.RED);
		registerBlockWithItem("paltaeria_fragment_block", PALTAERIA_FRAGMENT_BLOCK, new FloatBlockItem(PALTAERIA_FRAGMENT_BLOCK, settings, 0.98F), DyeColor.CYAN);
		registerBlockWithItem("hover_block", HOVER_BLOCK, new FloatBlockItem(HOVER_BLOCK, settings, 0.996F), DyeColor.GREEN);
	}
	
	private static void registerColoredLamps(FabricItemSettings settings) {
		registerBlockWithItem("white_lamp", WHITE_LAMP, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_lamp", ORANGE_LAMP, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_lamp", MAGENTA_LAMP, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_lamp", LIGHT_BLUE_LAMP, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_lamp", YELLOW_LAMP, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_lamp", LIME_LAMP, settings, DyeColor.LIME);
		registerBlockWithItem("pink_lamp", PINK_LAMP, settings, DyeColor.PINK);
		registerBlockWithItem("gray_lamp", GRAY_LAMP, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_lamp", LIGHT_GRAY_LAMP, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_lamp", CYAN_LAMP, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_lamp", PURPLE_LAMP, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_lamp", BLUE_LAMP, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_lamp", BROWN_LAMP, settings, DyeColor.BROWN);
		registerBlockWithItem("green_lamp", GREEN_LAMP, settings, DyeColor.GREEN);
		registerBlockWithItem("red_lamp", RED_LAMP, settings, DyeColor.RED);
		registerBlockWithItem("black_lamp", BLACK_LAMP, settings, DyeColor.BLACK);
	}
	
	private static void registerGemstoneGlass(FabricItemSettings settings) {
		registerBlockWithItem("topaz_glass", TOPAZ_GLASS, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_glass", AMETHYST_GLASS, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_glass", CITRINE_GLASS, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_glass", ONYX_GLASS, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_glass", MOONSTONE_GLASS, settings, DyeColor.WHITE);
		
		registerBlockWithItem("glowing_glass", RADIANT_GLASS, settings, DyeColor.WHITE);
	}
	
	private static void registerPlayerOnlyGlass(FabricItemSettings settings) {
		registerBlockWithItem("vanilla_semi_permeable_glass", VANILLA_SEMI_PERMEABLE_GLASS, settings, DyeColor.WHITE);
		registerBlockWithItem("tinted_semi_permeable_glass", TINTED_SEMI_PERMEABLE_GLASS, settings, DyeColor.BLACK);
		registerBlockWithItem("glowing_semi_permeable_glass", RADIANT_SEMI_PERMEABLE_GLASS, settings, DyeColor.YELLOW);
		
		registerBlockWithItem("topaz_semi_permeable_glass", TOPAZ_SEMI_PERMEABLE_GLASS, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_semi_permeable_glass", AMETHYST_SEMI_PERMEABLE_GLASS, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_semi_permeable_glass", CITRINE_SEMI_PERMEABLE_GLASS, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_semi_permeable_glass", ONYX_SEMI_PERMEABLE_GLASS, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_semi_permeable_glass", MOONSTONE_SEMI_PERMEABLE_GLASS, settings, DyeColor.WHITE);
	}
	
	private static void registerGemstoneChimes(FabricItemSettings settings) {
		registerBlockWithItem("topaz_chime", TOPAZ_CHIME, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chime", AMETHYST_CHIME, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chime", CITRINE_CHIME, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chime", ONYX_CHIME, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chime", MOONSTONE_CHIME, settings, DyeColor.WHITE);
	}
	
	private static void registerDecoStones(FabricItemSettings settings) {
		registerBlockWithItem("amethyst_decostone", AMETHYST_DECOSTONE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("topaz_decostone", TOPAZ_DECOSTONE, settings, DyeColor.CYAN);
		registerBlockWithItem("citrine_decostone", CITRINE_DECOSTONE, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_decostone", ONYX_DECOSTONE, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_decostone", MOONSTONE_DECOSTONE, settings, DyeColor.WHITE);
	}
	
	private static void registerStoneBlocks(FabricItemSettings settings) {
		registerBlockWithItem("smooth_basalt_slab", SMOOTH_BASALT_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("smooth_basalt_wall", SMOOTH_BASALT_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("smooth_basalt_stairs", SMOOTH_BASALT_STAIRS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_basalt", POLISHED_BASALT, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_pillar", POLISHED_BASALT_PILLAR, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_crest", POLISHED_BASALT_CREST, settings, DyeColor.BROWN);
		registerBlockWithItem("chiseled_polished_basalt", CHISELED_POLISHED_BASALT, settings, DyeColor.BROWN);
		registerBlockWithItem("notched_polished_basalt", NOTCHED_POLISHED_BASALT, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_slab", POLISHED_BASALT_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_wall", POLISHED_BASALT_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_stairs", POLISHED_BASALT_STAIRS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("basalt_bricks", BASALT_BRICKS, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_slab", BASALT_BRICK_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_wall", BASALT_BRICK_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_stairs", BASALT_BRICK_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("cracked_basalt_bricks", CRACKED_BASALT_BRICKS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("basalt_tiles", BASALT_TILES, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_tile_stairs", BASALT_TILE_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_tile_slab", BASALT_TILE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("basalt_tile_wall", BASALT_TILE_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("cracked_basalt_tiles", CRACKED_BASALT_TILES, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_basalt_button", POLISHED_BASALT_BUTTON, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_pressure_plate", POLISHED_BASALT_PRESSURE_PLATE, settings, DyeColor.BROWN);
		
		registerBlockWithItem("calcite_slab", CALCITE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_wall", CALCITE_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_stairs", CALCITE_STAIRS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_calcite", POLISHED_CALCITE, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_pillar", POLISHED_CALCITE_PILLAR, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_crest", POLISHED_CALCITE_CREST, settings, DyeColor.BROWN);
		registerBlockWithItem("chiseled_polished_calcite", CHISELED_POLISHED_CALCITE, settings, DyeColor.BROWN);
		registerBlockWithItem("notched_polished_calcite", NOTCHED_POLISHED_CALCITE, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_slab", POLISHED_CALCITE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_wall", POLISHED_CALCITE_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_stairs", POLISHED_CALCITE_STAIRS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("calcite_bricks", CALCITE_BRICKS, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_slab", CALCITE_BRICK_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_wall", CALCITE_BRICK_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_stairs", CALCITE_BRICK_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("cracked_calcite_bricks", CRACKED_CALCITE_BRICKS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("calcite_tiles", CALCITE_TILES, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_tile_stairs", CALCITE_TILE_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_tile_slab", CALCITE_TILE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("calcite_tile_wall", CALCITE_TILE_WALL, settings, DyeColor.BROWN);
		registerBlockWithItem("cracked_calcite_tiles", CRACKED_CALCITE_TILES, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_calcite_button", POLISHED_CALCITE_BUTTON, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_pressure_plate", POLISHED_CALCITE_PRESSURE_PLATE, settings, DyeColor.BROWN);
		
		registerBlockWithItem("blackslag", BLACKSLAG, settings, DyeColor.BLACK);
		registerBlockWithItem("cobbled_blackslag", COBBLED_BLACKSLAG, settings, DyeColor.BLACK);
		registerBlockWithItem("cobbled_blackslag_stairs", COBBLED_BLACKSLAG_STAIRS, settings, DyeColor.BLACK);
		registerBlockWithItem("cobbled_blackslag_slab", COBBLED_BLACKSLAG_SLAB, settings, DyeColor.BLACK);
		registerBlockWithItem("cobbled_blackslag_wall", COBBLED_BLACKSLAG_WALL, settings, DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag", POLISHED_BLACKSLAG, settings, DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag_stairs", POLISHED_BLACKSLAG_STAIRS, settings, DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag_slab", POLISHED_BLACKSLAG_SLAB, settings, DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag_wall", POLISHED_BLACKSLAG_WALL, settings, DyeColor.BLACK);
		
		registerBlockWithItem("blackslag_tiles", BLACKSLAG_TILES, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_tile_stairs", BLACKSLAG_TILE_STAIRS, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_tile_slab", BLACKSLAG_TILE_SLAB, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_tile_wall", BLACKSLAG_TILE_WALL, settings, DyeColor.BLACK);
		registerBlockWithItem("cracked_blackslag_tiles", CRACKED_BLACKSLAG_TILES, settings, DyeColor.BLACK);
		
		registerBlockWithItem("blackslag_bricks", BLACKSLAG_BRICKS, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_brick_stairs", BLACKSLAG_BRICK_STAIRS, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_brick_slab", BLACKSLAG_BRICK_SLAB, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_brick_wall", BLACKSLAG_BRICK_WALL, settings, DyeColor.BLACK);
		registerBlockWithItem("cracked_blackslag_bricks", CRACKED_BLACKSLAG_BRICKS, settings, DyeColor.BLACK);
		
		registerBlockWithItem("polished_blackslag_pillar", POLISHED_BLACKSLAG_PILLAR, settings, DyeColor.BLACK);
		registerBlockWithItem("chiseled_polished_blackslag", CHISELED_POLISHED_BLACKSLAG, settings, DyeColor.BLACK);
		registerBlockWithItem("ancient_chiseled_polished_blackslag", ANCIENT_CHISELED_POLISHED_BLACKSLAG, Tab.DECORATION.settings(Rarity.UNCOMMON), DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag_button", POLISHED_BLACKSLAG_BUTTON, settings, DyeColor.BLACK);
		registerBlockWithItem("polished_blackslag_pressure_plate", POLISHED_BLACKSLAG_PRESSURE_PLATE, settings, DyeColor.BLACK);
		registerBlockWithItem("infested_blackslag", INFESTED_BLACKSLAG, settings, DyeColor.BLACK);


		registerBlockWithItem("shale_clay", SHALE_CLAY, settings, DyeColor.BROWN);
		registerBlockWithItem("tilled_shale_clay", TILLED_SHALE_CLAY, settings, DyeColor.BROWN);

		registerBlockWithItem("polished_shale_clay", POLISHED_SHALE_CLAY, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_polished_shale_clay", EXPOSED_POLISHED_SHALE_CLAY, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_polished_shale_clay", WEATHERED_POLISHED_SHALE_CLAY, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_shale_clay_stairs", POLISHED_SHALE_CLAY_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("polished_shale_clay_slab", POLISHED_SHALE_CLAY_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_polished_shale_clay_stairs", EXPOSED_POLISHED_SHALE_CLAY_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_polished_shale_clay_slab", EXPOSED_POLISHED_SHALE_CLAY_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_polished_shale_clay_stairs", WEATHERED_POLISHED_SHALE_CLAY_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_polished_shale_clay_slab", WEATHERED_POLISHED_SHALE_CLAY_SLAB, settings, DyeColor.BROWN);
		
		registerBlockWithItem("shale_clay_bricks", SHALE_CLAY_BRICKS, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_bricks", EXPOSED_SHALE_CLAY_BRICKS, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_bricks", WEATHERED_SHALE_CLAY_BRICKS, settings, DyeColor.BROWN);
		
		registerBlockWithItem("shale_clay_brick_stairs", SHALE_CLAY_BRICK_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("shale_clay_brick_slab", SHALE_CLAY_BRICK_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_brick_stairs", EXPOSED_SHALE_CLAY_BRICK_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_brick_slab", EXPOSED_SHALE_CLAY_BRICK_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_brick_stairs", WEATHERED_SHALE_CLAY_BRICK_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_brick_slab", WEATHERED_SHALE_CLAY_BRICK_SLAB, settings, DyeColor.BROWN);
		
		registerBlockWithItem("shale_clay_tiles", SHALE_CLAY_TILES, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_tiles", EXPOSED_SHALE_CLAY_TILES, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_tiles", WEATHERED_SHALE_CLAY_TILES, settings, DyeColor.BROWN);
		
		registerBlockWithItem("shale_clay_tile_stairs", SHALE_CLAY_TILE_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("shale_clay_tile_slab", SHALE_CLAY_TILE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_tile_stairs", EXPOSED_SHALE_CLAY_TILE_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("exposed_shale_clay_tile_slab", EXPOSED_SHALE_CLAY_TILE_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_tile_stairs", WEATHERED_SHALE_CLAY_TILE_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("weathered_shale_clay_tile_slab", WEATHERED_SHALE_CLAY_TILE_SLAB, settings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_bone_ash", POLISHED_BONE_ASH, settings, DyeColor.CYAN);
		registerBlockWithItem("polished_bone_ash_slab", POLISHED_BONE_ASH_SLAB, settings, DyeColor.CYAN);
		registerBlockWithItem("polished_bone_ash_stairs", POLISHED_BONE_ASH_STAIRS, settings, DyeColor.CYAN);
		registerBlockWithItem("polished_bone_ash_wall", POLISHED_BONE_ASH_WALL, settings, DyeColor.CYAN);
		
		registerBlockWithItem("bone_ash_bricks", BONE_ASH_BRICKS, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_brick_slab", BONE_ASH_BRICK_SLAB, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_brick_stairs", BONE_ASH_BRICK_STAIRS, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_brick_wall", BONE_ASH_BRICK_WALL, settings, DyeColor.CYAN);
		
		registerBlockWithItem("bone_ash_tiles", BONE_ASH_TILES, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_tile_slab", BONE_ASH_TILE_SLAB, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_tile_stairs", BONE_ASH_TILE_STAIRS, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_tile_wall", BONE_ASH_TILE_WALL, settings, DyeColor.CYAN);
		
		registerBlockWithItem("polished_bone_ash_pillar", POLISHED_BONE_ASH_PILLAR, settings, DyeColor.CYAN);
		registerBlockWithItem("bone_ash_shingles", BONE_ASH_SHINGLES, settings, DyeColor.CYAN);
		
		registerBlockWithItem("sawtooth", SAWTOOTH, settings, DyeColor.RED);
		registerBlockWithItem("slush", SLUSH, settings, DyeColor.BROWN);
		registerBlockWithItem("tilled_slush", TILLED_SLUSH, settings, DyeColor.BROWN);

		registerBlockWithItem("dragonbone", DRAGONBONE, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("cracked_dragonbone", CRACKED_DRAGONBONE, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.GREEN);
		registerBlockWithItem("small_bloodstone_bud", SMALL_BLOODSTONE_BUD, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.RED);
		registerBlockWithItem("large_bloodstone_bud", LARGE_BLOODSTONE_BUD, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.RED);
		registerBlockWithItem("bloodstone_cluster", BLOODSTONE_CLUSTER, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.RED);
		registerBlockWithItem("bloodstone_block", BLOODSTONE_BLOCK, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.RED);
		registerBlockWithItem("effulgent_block", EFFULGENT_BLOCK, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.YELLOW);
		registerBlockWithItem("effulgent_cushion", EFFULGENT_CUSHION, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.YELLOW);
		registerBlockWithItem("effulgent_carpet", EFFULGENT_CARPET, Tab.DECORATION.settings().rarity(Rarity.UNCOMMON), DyeColor.YELLOW);

	}
	
	private static void registerRunes(FabricItemSettings settings) {
		registerBlockWithItem("topaz_chiseled_basalt", TOPAZ_CHISELED_BASALT, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chiseled_basalt", AMETHYST_CHISELED_BASALT, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chiseled_basalt", CITRINE_CHISELED_BASALT, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chiseled_basalt", ONYX_CHISELED_BASALT, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chiseled_basalt", MOONSTONE_CHISELED_BASALT, settings, DyeColor.WHITE);
		
		registerBlockWithItem("topaz_chiseled_calcite", TOPAZ_CHISELED_CALCITE, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chiseled_calcite", AMETHYST_CHISELED_CALCITE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chiseled_calcite", CITRINE_CHISELED_CALCITE, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chiseled_calcite", ONYX_CHISELED_CALCITE, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chiseled_calcite", MOONSTONE_CHISELED_CALCITE, settings, DyeColor.WHITE);
	}
	
	private static void registerGemstoneLamps(FabricItemSettings settings) {
		registerBlockWithItem("topaz_calcite_lamp", TOPAZ_CALCITE_LAMP, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_calcite_lamp", AMETHYST_CALCITE_LAMP, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_calcite_lamp", CITRINE_CALCITE_LAMP, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_calcite_lamp", ONYX_CALCITE_LAMP, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_calcite_lamp", MOONSTONE_CALCITE_LAMP, settings, DyeColor.WHITE);
		
		registerBlockWithItem("topaz_basalt_lamp", TOPAZ_BASALT_LAMP, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_basalt_lamp", AMETHYST_BASALT_LAMP, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_basalt_lamp", CITRINE_BASALT_LAMP, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_basalt_lamp", ONYX_BASALT_LAMP, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_basalt_lamp", MOONSTONE_BASALT_LAMP, settings, DyeColor.WHITE);
	}
	
	private static void registerColoredWood(FabricItemSettings settings) {
		registerBlockWithItem("white_log", WHITE_LOG, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_log", ORANGE_LOG, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_log", MAGENTA_LOG, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_log", LIGHT_BLUE_LOG, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_log", YELLOW_LOG, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_log", LIME_LOG, settings, DyeColor.LIME);
		registerBlockWithItem("pink_log", PINK_LOG, settings, DyeColor.PINK);
		registerBlockWithItem("gray_log", GRAY_LOG, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_log", LIGHT_GRAY_LOG, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_log", CYAN_LOG, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_log", PURPLE_LOG, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_log", BLUE_LOG, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_log", BROWN_LOG, settings, DyeColor.BROWN);
		registerBlockWithItem("green_log", GREEN_LOG, settings, DyeColor.GREEN);
		registerBlockWithItem("red_log", RED_LOG, settings, DyeColor.RED);
		registerBlockWithItem("black_log", BLACK_LOG, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_leaves", WHITE_LEAVES, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_leaves", ORANGE_LEAVES, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_leaves", MAGENTA_LEAVES, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_leaves", LIGHT_BLUE_LEAVES, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_leaves", YELLOW_LEAVES, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_leaves", LIME_LEAVES, settings, DyeColor.LIME);
		registerBlockWithItem("pink_leaves", PINK_LEAVES, settings, DyeColor.PINK);
		registerBlockWithItem("gray_leaves", GRAY_LEAVES, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_leaves", LIGHT_GRAY_LEAVES, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_leaves", CYAN_LEAVES, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_leaves", PURPLE_LEAVES, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_leaves", BLUE_LEAVES, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_leaves", BROWN_LEAVES, settings, DyeColor.BROWN);
		registerBlockWithItem("green_leaves", GREEN_LEAVES, settings, DyeColor.GREEN);
		registerBlockWithItem("red_leaves", RED_LEAVES, settings, DyeColor.RED);
		registerBlockWithItem("black_leaves", BLACK_LEAVES, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_sapling", WHITE_SAPLING, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_sapling", ORANGE_SAPLING, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_sapling", MAGENTA_SAPLING, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_sapling", LIGHT_BLUE_SAPLING, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_sapling", YELLOW_SAPLING, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_sapling", LIME_SAPLING, settings, DyeColor.LIME);
		registerBlockWithItem("pink_sapling", PINK_SAPLING, settings, DyeColor.PINK);
		registerBlockWithItem("gray_sapling", GRAY_SAPLING, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_sapling", LIGHT_GRAY_SAPLING, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_sapling", CYAN_SAPLING, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_sapling", PURPLE_SAPLING, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_sapling", BLUE_SAPLING, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_sapling", BROWN_SAPLING, settings, DyeColor.BROWN);
		registerBlockWithItem("green_sapling", GREEN_SAPLING, settings, DyeColor.GREEN);
		registerBlockWithItem("red_sapling", RED_SAPLING, settings, DyeColor.RED);
		registerBlockWithItem("black_sapling", BLACK_SAPLING, settings, DyeColor.BLACK);
		
		registerBlock("potted_white_sapling", POTTED_WHITE_SAPLING);
		registerBlock("potted_orange_sapling", POTTED_ORANGE_SAPLING);
		registerBlock("potted_magenta_sapling", POTTED_MAGENTA_SAPLING);
		registerBlock("potted_light_blue_sapling", POTTED_LIGHT_BLUE_SAPLING);
		registerBlock("potted_yellow_sapling", POTTED_YELLOW_SAPLING);
		registerBlock("potted_lime_sapling", POTTED_LIME_SAPLING);
		registerBlock("potted_pink_sapling", POTTED_PINK_SAPLING);
		registerBlock("potted_gray_sapling", POTTED_GRAY_SAPLING);
		registerBlock("potted_light_gray_sapling", POTTED_LIGHT_GRAY_SAPLING);
		registerBlock("potted_cyan_sapling", POTTED_CYAN_SAPLING);
		registerBlock("potted_purple_sapling", POTTED_PURPLE_SAPLING);
		registerBlock("potted_blue_sapling", POTTED_BLUE_SAPLING);
		registerBlock("potted_brown_sapling", POTTED_BROWN_SAPLING);
		registerBlock("potted_green_sapling", POTTED_GREEN_SAPLING);
		registerBlock("potted_red_sapling", POTTED_RED_SAPLING);
		registerBlock("potted_black_sapling", POTTED_BLACK_SAPLING);
		
		registerBlockWithItem("white_planks", WHITE_PLANKS, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_planks", ORANGE_PLANKS, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_planks", MAGENTA_PLANKS, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_planks", LIGHT_BLUE_PLANKS, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_planks", YELLOW_PLANKS, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_planks", LIME_PLANKS, settings, DyeColor.LIME);
		registerBlockWithItem("pink_planks", PINK_PLANKS, settings, DyeColor.PINK);
		registerBlockWithItem("gray_planks", GRAY_PLANKS, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_planks", LIGHT_GRAY_PLANKS, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_planks", CYAN_PLANKS, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_planks", PURPLE_PLANKS, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_planks", BLUE_PLANKS, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_planks", BROWN_PLANKS, settings, DyeColor.BROWN);
		registerBlockWithItem("green_planks", GREEN_PLANKS, settings, DyeColor.GREEN);
		registerBlockWithItem("red_planks", RED_PLANKS, settings, DyeColor.RED);
		registerBlockWithItem("black_planks", BLACK_PLANKS, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_stairs", WHITE_STAIRS, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_stairs", ORANGE_STAIRS, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_stairs", MAGENTA_STAIRS, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_stairs", LIGHT_BLUE_STAIRS, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_stairs", YELLOW_STAIRS, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_stairs", LIME_STAIRS, settings, DyeColor.LIME);
		registerBlockWithItem("pink_stairs", PINK_STAIRS, settings, DyeColor.PINK);
		registerBlockWithItem("gray_stairs", GRAY_STAIRS, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_stairs", LIGHT_GRAY_STAIRS, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_stairs", CYAN_STAIRS, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_stairs", PURPLE_STAIRS, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_stairs", BLUE_STAIRS, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_stairs", BROWN_STAIRS, settings, DyeColor.BROWN);
		registerBlockWithItem("green_stairs", GREEN_STAIRS, settings, DyeColor.GREEN);
		registerBlockWithItem("red_stairs", RED_STAIRS, settings, DyeColor.RED);
		registerBlockWithItem("black_stairs", BLACK_STAIRS, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_pressure_plate", WHITE_PRESSURE_PLATE, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_pressure_plate", ORANGE_PRESSURE_PLATE, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_pressure_plate", MAGENTA_PRESSURE_PLATE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_pressure_plate", LIGHT_BLUE_PRESSURE_PLATE, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_pressure_plate", YELLOW_PRESSURE_PLATE, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_pressure_plate", LIME_PRESSURE_PLATE, settings, DyeColor.LIME);
		registerBlockWithItem("pink_pressure_plate", PINK_PRESSURE_PLATE, settings, DyeColor.PINK);
		registerBlockWithItem("gray_pressure_plate", GRAY_PRESSURE_PLATE, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_pressure_plate", LIGHT_GRAY_PRESSURE_PLATE, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_pressure_plate", CYAN_PRESSURE_PLATE, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_pressure_plate", PURPLE_PRESSURE_PLATE, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_pressure_plate", BLUE_PRESSURE_PLATE, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_pressure_plate", BROWN_PRESSURE_PLATE, settings, DyeColor.BROWN);
		registerBlockWithItem("green_pressure_plate", GREEN_PRESSURE_PLATE, settings, DyeColor.GREEN);
		registerBlockWithItem("red_pressure_plate", RED_PRESSURE_PLATE, settings, DyeColor.RED);
		registerBlockWithItem("black_pressure_plate", BLACK_PRESSURE_PLATE, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_fence", WHITE_FENCE, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_fence", ORANGE_FENCE, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_fence", MAGENTA_FENCE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_fence", LIGHT_BLUE_FENCE, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_fence", YELLOW_FENCE, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_fence", LIME_FENCE, settings, DyeColor.LIME);
		registerBlockWithItem("pink_fence", PINK_FENCE, settings, DyeColor.PINK);
		registerBlockWithItem("gray_fence", GRAY_FENCE, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_fence", LIGHT_GRAY_FENCE, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_fence", CYAN_FENCE, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_fence", PURPLE_FENCE, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_fence", BLUE_FENCE, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_fence", BROWN_FENCE, settings, DyeColor.BROWN);
		registerBlockWithItem("green_fence", GREEN_FENCE, settings, DyeColor.GREEN);
		registerBlockWithItem("red_fence", RED_FENCE, settings, DyeColor.RED);
		registerBlockWithItem("black_fence", BLACK_FENCE, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_fence_gate", WHITE_FENCE_GATE, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_fence_gate", ORANGE_FENCE_GATE, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_fence_gate", MAGENTA_FENCE_GATE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_fence_gate", LIGHT_BLUE_FENCE_GATE, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_fence_gate", YELLOW_FENCE_GATE, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_fence_gate", LIME_FENCE_GATE, settings, DyeColor.LIME);
		registerBlockWithItem("pink_fence_gate", PINK_FENCE_GATE, settings, DyeColor.PINK);
		registerBlockWithItem("gray_fence_gate", GRAY_FENCE_GATE, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_fence_gate", LIGHT_GRAY_FENCE_GATE, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_fence_gate", CYAN_FENCE_GATE, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_fence_gate", PURPLE_FENCE_GATE, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_fence_gate", BLUE_FENCE_GATE, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_fence_gate", BROWN_FENCE_GATE, settings, DyeColor.BROWN);
		registerBlockWithItem("green_fence_gate", GREEN_FENCE_GATE, settings, DyeColor.GREEN);
		registerBlockWithItem("red_fence_gate", RED_FENCE_GATE, settings, DyeColor.RED);
		registerBlockWithItem("black_fence_gate", BLACK_FENCE_GATE, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_button", WHITE_BUTTON, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_button", ORANGE_BUTTON, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_button", MAGENTA_BUTTON, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_button", LIGHT_BLUE_BUTTON, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_button", YELLOW_BUTTON, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_button", LIME_BUTTON, settings, DyeColor.LIME);
		registerBlockWithItem("pink_button", PINK_BUTTON, settings, DyeColor.PINK);
		registerBlockWithItem("gray_button", GRAY_BUTTON, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_button", LIGHT_GRAY_BUTTON, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_button", CYAN_BUTTON, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_button", PURPLE_BUTTON, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_button", BLUE_BUTTON, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_button", BROWN_BUTTON, settings, DyeColor.BROWN);
		registerBlockWithItem("green_button", GREEN_BUTTON, settings, DyeColor.GREEN);
		registerBlockWithItem("red_button", RED_BUTTON, settings, DyeColor.RED);
		registerBlockWithItem("black_button", BLACK_BUTTON, settings, DyeColor.BLACK);
		
		registerBlockWithItem("white_slab", WHITE_SLAB, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_slab", ORANGE_SLAB, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_slab", MAGENTA_SLAB, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_slab", LIGHT_BLUE_SLAB, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_slab", YELLOW_SLAB, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_slab", LIME_SLAB, settings, DyeColor.LIME);
		registerBlockWithItem("pink_slab", PINK_SLAB, settings, DyeColor.PINK);
		registerBlockWithItem("gray_slab", GRAY_SLAB, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_slab", LIGHT_GRAY_SLAB, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_slab", CYAN_SLAB, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_slab", PURPLE_SLAB, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_slab", BLUE_SLAB, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_slab", BROWN_SLAB, settings, DyeColor.BROWN);
		registerBlockWithItem("green_slab", GREEN_SLAB, settings, DyeColor.GREEN);
		registerBlockWithItem("red_slab", RED_SLAB, settings, DyeColor.RED);
		registerBlockWithItem("black_slab", BLACK_SLAB, settings, DyeColor.BLACK);
	}
	
	private static void registerGlowBlocks(FabricItemSettings settings) {
		registerBlockWithItem("white_glowblock", WHITE_GLOWBLOCK, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_glowblock", ORANGE_GLOWBLOCK, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_glowblock", MAGENTA_GLOWBLOCK, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_glowblock", LIGHT_BLUE_GLOWBLOCK, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_glowblock", YELLOW_GLOWBLOCK, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_glowblock", LIME_GLOWBLOCK, settings, DyeColor.LIME);
		registerBlockWithItem("pink_glowblock", PINK_GLOWBLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("gray_glowblock", GRAY_GLOWBLOCK, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_glowblock", LIGHT_GRAY_GLOWBLOCK, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_glowblock", CYAN_GLOWBLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_glowblock", PURPLE_GLOWBLOCK, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_glowblock", BLUE_GLOWBLOCK, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_glowblock", BROWN_GLOWBLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("green_glowblock", GREEN_GLOWBLOCK, settings, DyeColor.GREEN);
		registerBlockWithItem("red_glowblock", RED_GLOWBLOCK, settings, DyeColor.RED);
		registerBlockWithItem("black_glowblock", BLACK_GLOWBLOCK, settings, DyeColor.BLACK);
	}
	
	public static void registerSparklestoneLights(FabricItemSettings settings) {
		registerBlockWithItem("basalt_shimmerstone_light", BASALT_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("calcite_shimmerstone_light", CALCITE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("stone_shimmerstone_light", STONE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("granite_shimmerstone_light", GRANITE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("diorite_shimmerstone_light", DIORITE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("andesite_shimmerstone_light", ANDESITE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_shimmerstone_light", DEEPSLATE_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
		registerBlockWithItem("blackslag_shimmerstone_light", BLACKSLAG_SHIMMERSTONE_LIGHT, settings, DyeColor.YELLOW);
	}
	
	public static void registerShootingStarBlocks(FabricItemSettings settings) {
		registerBlockWithItem("shooting_star_glistering", GLISTERING_SHOOTING_STAR, new ShootingStarItem(GLISTERING_SHOOTING_STAR, settings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_fiery", FIERY_SHOOTING_STAR, new ShootingStarItem(FIERY_SHOOTING_STAR, settings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_colorful", COLORFUL_SHOOTING_STAR, new ShootingStarItem(COLORFUL_SHOOTING_STAR, settings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_pristine", PRISTINE_SHOOTING_STAR, new ShootingStarItem(PRISTINE_SHOOTING_STAR, settings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_gemstone", GEMSTONE_SHOOTING_STAR, new ShootingStarItem(GEMSTONE_SHOOTING_STAR, settings), DyeColor.PURPLE);
	}
	
	public static void registerPastelNetworkNodes(FabricItemSettings settings) {
		registerBlockWithItem("connection_node", CONNECTION_NODE, settings, DyeColor.GREEN);
		registerBlockWithItem("provider_node", PROVIDER_NODE, settings, DyeColor.GREEN);
		registerBlockWithItem("storage_node", STORAGE_NODE, settings, DyeColor.GREEN);
		registerBlockWithItem("sender_node", SENDER_NODE, settings, DyeColor.GREEN);
		registerBlockWithItem("gather_node", GATHER_NODE, settings, DyeColor.GREEN);
	}
	
	public static void registerSporeBlossoms(FabricItemSettings settings) {
		registerBlockWithItem("white_spore_blossom", WHITE_SPORE_BLOSSOM, settings, DyeColor.WHITE);
		registerBlockWithItem("orange_spore_blossom", ORANGE_SPORE_BLOSSOM, settings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_spore_blossom", MAGENTA_SPORE_BLOSSOM, settings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_spore_blossom", LIGHT_BLUE_SPORE_BLOSSOM, settings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_spore_blossom", YELLOW_SPORE_BLOSSOM, settings, DyeColor.YELLOW);
		registerBlockWithItem("lime_spore_blossom", LIME_SPORE_BLOSSOM, settings, DyeColor.LIME);
		registerBlockWithItem("pink_spore_blossom", PINK_SPORE_BLOSSOM, settings, DyeColor.PINK);
		registerBlockWithItem("gray_spore_blossom", GRAY_SPORE_BLOSSOM, settings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_spore_blossom", LIGHT_GRAY_SPORE_BLOSSOM, settings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_spore_blossom", CYAN_SPORE_BLOSSOM, settings, DyeColor.CYAN);
		registerBlockWithItem("purple_spore_blossom", PURPLE_SPORE_BLOSSOM, settings, DyeColor.PURPLE);
		registerBlockWithItem("blue_spore_blossom", BLUE_SPORE_BLOSSOM, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_spore_blossom", BROWN_SPORE_BLOSSOM, settings, DyeColor.BROWN);
		registerBlockWithItem("green_spore_blossom", GREEN_SPORE_BLOSSOM, settings, DyeColor.GREEN);
		registerBlockWithItem("red_spore_blossom", RED_SPORE_BLOSSOM, settings, DyeColor.RED);
		registerBlockWithItem("black_spore_blossom", BLACK_SPORE_BLOSSOM, settings, DyeColor.BLACK);
	}
	
	private static void registerGemBlocks(FabricItemSettings settings) {
		registerBlockWithItem("topaz_block", TOPAZ_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("budding_topaz", BUDDING_TOPAZ, settings, DyeColor.CYAN);
		registerBlockWithItem("small_topaz_bud", SMALL_TOPAZ_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("medium_topaz_bud", MEDIUM_TOPAZ_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("large_topaz_bud", LARGE_TOPAZ_BUD, settings, DyeColor.CYAN);
		registerBlockWithItem("topaz_cluster", TOPAZ_CLUSTER, settings, DyeColor.CYAN);
		
		registerBlockWithItem("citrine_block", CITRINE_BLOCK, settings, DyeColor.YELLOW);
		registerBlockWithItem("budding_citrine", BUDDING_CITRINE, settings, DyeColor.YELLOW);
		registerBlockWithItem("small_citrine_bud", SMALL_CITRINE_BUD, settings, DyeColor.YELLOW);
		registerBlockWithItem("medium_citrine_bud", MEDIUM_CITRINE_BUD, settings, DyeColor.YELLOW);
		registerBlockWithItem("large_citrine_bud", LARGE_CITRINE_BUD, settings, DyeColor.YELLOW);
		registerBlockWithItem("citrine_cluster", CITRINE_CLUSTER, settings, DyeColor.YELLOW);
		
		registerBlockWithItem("onyx_block", ONYX_BLOCK, settings, DyeColor.BLACK);
		registerBlockWithItem("budding_onyx", BUDDING_ONYX, settings, DyeColor.BLACK);
		registerBlockWithItem("small_onyx_bud", SMALL_ONYX_BUD, settings, DyeColor.BLACK);
		registerBlockWithItem("medium_onyx_bud", MEDIUM_ONYX_BUD, settings, DyeColor.BLACK);
		registerBlockWithItem("large_onyx_bud", LARGE_ONYX_BUD, settings, DyeColor.BLACK);
		registerBlockWithItem("onyx_cluster", ONYX_CLUSTER, settings, DyeColor.BLACK);
		
		registerBlockWithItem("moonstone_block", MOONSTONE_BLOCK, settings, DyeColor.WHITE);
		registerBlockWithItem("budding_moonstone", BUDDING_MOONSTONE, settings, DyeColor.WHITE);
		registerBlockWithItem("small_moonstone_bud", SMALL_MOONSTONE_BUD, settings, DyeColor.WHITE);
		registerBlockWithItem("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD, settings, DyeColor.WHITE);
		registerBlockWithItem("large_moonstone_bud", LARGE_MOONSTONE_BUD, settings, DyeColor.WHITE);
		registerBlockWithItem("moonstone_cluster", MOONSTONE_CLUSTER, settings, DyeColor.WHITE);
	}
	
	private static void registerGemOreBlocks(FabricItemSettings settings) {
		// stone ores
		registerBlockWithItem("topaz_ore", TOPAZ_ORE, settings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_ore", AMETHYST_ORE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_ore", CITRINE_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_ore", ONYX_ORE, settings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_ore", MOONSTONE_ORE, settings, DyeColor.WHITE);
		
		// deepslate ores
		registerBlockWithItem("deepslate_topaz_ore", DEEPSLATE_TOPAZ_ORE, settings, DyeColor.CYAN);
		registerBlockWithItem("deepslate_amethyst_ore", DEEPSLATE_AMETHYST_ORE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("deepslate_citrine_ore", DEEPSLATE_CITRINE_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_onyx_ore", DEEPSLATE_ONYX_ORE, settings, DyeColor.BLACK);
		registerBlockWithItem("deepslate_moonstone_ore", DEEPSLATE_MOONSTONE_ORE, settings, DyeColor.WHITE);
		
		// blackslag ores
		registerBlockWithItem("blackslag_topaz_ore", BLACKSLAG_TOPAZ_ORE, settings, DyeColor.CYAN);
		registerBlockWithItem("blackslag_amethyst_ore", BLACKSLAG_AMETHYST_ORE, settings, DyeColor.MAGENTA);
		registerBlockWithItem("blackslag_citrine_ore", BLACKSLAG_CITRINE_ORE, settings, DyeColor.YELLOW);
		registerBlockWithItem("blackslag_onyx_ore", BLACKSLAG_ONYX_ORE, settings, DyeColor.BLACK);
		registerBlockWithItem("blackslag_moonstone_ore", BLACKSLAG_MOONSTONE_ORE, settings, DyeColor.WHITE);
	}
	
	private static void registerStructureBlocks(FabricItemSettings settings) {
		registerBlockWithItem("downstone", DOWNSTONE, settings, DyeColor.BLUE);
		
		registerBlockWithItem("preservation_stone", PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("preservation_bricks", PRESERVATION_BRICKS, settings, DyeColor.BLUE);
		registerBlockWithItem("shimmering_preservation_bricks", SHIMMERING_PRESERVATION_BRICKS, settings, DyeColor.BLUE);
		registerBlockWithItem("powder_chiseled_preservation_stone", POWDER_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("dike_chiseled_preservation_stone", DIKE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("preservation_glass", PRESERVATION_GLASS, settings, DyeColor.BLUE);
		registerBlockWithItem("tinted_preservation_glass", TINTED_PRESERVATION_GLASS, settings, DyeColor.BLUE);
		registerBlockWithItem("preservation_roundel", PRESERVATION_ROUNDEL, settings, DyeColor.BLUE);
		registerBlockWithItem("preservation_block_detector", PRESERVATION_BLOCK_DETECTOR, settings, DyeColor.BLUE);
		registerBlockWithItem("dike_gate_fountain", DIKE_GATE_FOUNTAIN, settings, DyeColor.BLUE);
		registerBlockWithItem("dike_gate", DIKE_GATE, settings, DyeColor.BLUE);
		registerBlockWithItem("preservation_controller", PRESERVATION_CONTROLLER, settings, DyeColor.BLUE);
		
		registerBlockWithItem("black_chiseled_preservation_stone", BLACK_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("blue_chiseled_preservation_stone", BLUE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("brown_chiseled_preservation_stone", BROWN_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("cyan_chiseled_preservation_stone", CYAN_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("gray_chiseled_preservation_stone", GRAY_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("green_chiseled_preservation_stone", GREEN_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("light_blue_chiseled_preservation_stone", LIGHT_BLUE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("light_gray_chiseled_preservation_stone", LIGHT_GRAY_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("lime_chiseled_preservation_stone", LIME_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("magenta_chiseled_preservation_stone", MAGENTA_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("orange_chiseled_preservation_stone", ORANGE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("pink_chiseled_preservation_stone", PINK_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("purple_chiseled_preservation_stone", PURPLE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("red_chiseled_preservation_stone", RED_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("white_chiseled_preservation_stone", WHITE_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		registerBlockWithItem("yellow_chiseled_preservation_stone", YELLOW_CHISELED_PRESERVATION_STONE, settings, DyeColor.BLUE);
		
		registerBlockWithItem("invisible_wall", INVISIBLE_WALL, settings, DyeColor.BLUE);
		registerBlockWithItem("courier_statue", COURIER_STATUE, settings, DyeColor.BLUE);
		registerBlockWithItem("treasure_chest", TREASURE_CHEST, settings, DyeColor.BLUE);
	}
	
	private static void registerJadeVineBlocks(FabricItemSettings settings) {
		registerBlock("jade_vine_roots", JADE_VINE_ROOTS);
		registerBlock("jade_vine_bulb", JADE_VINE_BULB);
		registerBlock("jade_vines", JADE_VINES);

		registerBlockWithItem("nephrite_blossom_stem", NEPHRITE_BLOSSOM_STEM, settings, DyeColor.PINK);
		registerBlockWithItem("nephrite_blossom_leaves", NEPHRITE_BLOSSOM_LEAVES, settings, DyeColor.PINK);
		registerBlock("nephrite_blossom_bulb", NEPHRITE_BLOSSOM_BULB);

		registerBlockWithItem("jadeite_lotus_stem", JADEITE_LOTUS_STEM, settings, DyeColor.BROWN);
		registerBlockWithItem("jadeite_lotus_flower", JADEITE_LOTUS_FLOWER, Tab.DECORATION.settings().maxCount(8), DyeColor.BROWN);
		registerBlock("jadeite_lotus_bulb", JADEITE_LOTUS_BULB);


		registerBlockWithItem("jade_vine_petal_block", JADE_VINE_PETAL_BLOCK, settings, DyeColor.LIME);
		registerBlockWithItem("jade_vine_petal_carpet", JADE_VINE_PETAL_CARPET, settings, DyeColor.LIME);
	}
	
	private static void registerSugarSticks(FabricItemSettings settings) {
		registerBlockWithItem("sugar_stick", SUGAR_STICK, settings, DyeColor.PINK);
		registerBlockWithItem("topaz_sugar_stick", TOPAZ_SUGAR_STICK, settings, DyeColor.PINK);
		registerBlockWithItem("amethyst_sugar_stick", AMETHYST_SUGAR_STICK, settings, DyeColor.PINK);
		registerBlockWithItem("citrine_sugar_stick", CITRINE_SUGAR_STICK, settings, DyeColor.PINK);
		registerBlockWithItem("onyx_sugar_stick", ONYX_SUGAR_STICK, settings, DyeColor.PINK);
		registerBlockWithItem("moonstone_sugar_stick", MOONSTONE_SUGAR_STICK, settings, DyeColor.PINK);
	}
	
	private static void registerPureOreBlocks(FabricItemSettings settings) {
		registerBlockWithItem("pure_coal_block", PURE_COAL_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_iron_block", PURE_IRON_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_gold_block", PURE_GOLD_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_diamond_block", PURE_DIAMOND_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("pure_emerald_block", PURE_EMERALD_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("pure_redstone_block", PURE_REDSTONE_BLOCK, settings, DyeColor.RED);
		registerBlockWithItem("pure_lapis_block", PURE_LAPIS_BLOCK, settings, DyeColor.PURPLE);
		registerBlockWithItem("pure_copper_block", PURE_COPPER_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_quartz_block", PURE_QUARTZ_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_glowstone_block", PURE_GLOWSTONE_BLOCK, settings, DyeColor.YELLOW);
		registerBlockWithItem("pure_prismarine_block", PURE_PRISMARINE_BLOCK, settings, DyeColor.CYAN);
		registerBlockWithItem("pure_netherite_scrap_block", PURE_NETHERITE_SCRAP_BLOCK, settings, DyeColor.BROWN);
		registerBlockWithItem("pure_echo_block", PURE_ECHO_BLOCK, settings, DyeColor.BROWN);
	}
	
	private static void registerMobBlocks(FabricItemSettings settings) {
		registerBlockWithItem("axolotl_mob_block", AXOLOTL_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("bat_mob_block", BAT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("bee_mob_block", BEE_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("blaze_mob_block", BLAZE_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("cat_mob_block", CAT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("chicken_mob_block", CHICKEN_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("cow_mob_block", COW_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("creeper_mob_block", CREEPER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("ender_dragon_mob_block", ENDER_DRAGON_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("enderman_mob_block", ENDERMAN_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("endermite_mob_block", ENDERMITE_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("evoker_mob_block", EVOKER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("fish_mob_block", FISH_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("fox_mob_block", FOX_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("ghast_mob_block", GHAST_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("glow_squid_mob_block", GLOW_SQUID_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("goat_mob_block", GOAT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("guardian_mob_block", GUARDIAN_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("horse_mob_block", HORSE_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("illusioner_mob_block", ILLUSIONER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("ocelot_mob_block", OCELOT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("parrot_mob_block", PARROT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("phantom_mob_block", PHANTOM_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("pig_mob_block", PIG_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("piglin_mob_block", PIGLIN_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("polar_bear_mob_block", POLAR_BEAR_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("pufferfish_mob_block", PUFFERFISH_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("rabbit_mob_block", RABBIT_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("sheep_mob_block", SHEEP_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("shulker_mob_block", SHULKER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("silverfish_mob_block", SILVERFISH_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("skeleton_mob_block", SKELETON_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("slime_mob_block", SLIME_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("snow_golem_mob_block", SNOW_GOLEM_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("spider_mob_block", SPIDER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("squid_mob_block", SQUID_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("stray_mob_block", STRAY_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("strider_mob_block", STRIDER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("turtle_mob_block", TURTLE_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("witch_mob_block", WITCH_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("wither_mob_block", WITHER_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("wither_skeleton_mob_block", WITHER_SKELETON_MOB_BLOCK, settings, DyeColor.PINK);
		registerBlockWithItem("zombie_mob_block", ZOMBIE_MOB_BLOCK, settings, DyeColor.PINK);
	}
	
	// Most mob heads vanilla is missing (vanilla only has: skeleton, wither skeleton, zombie, player, creeper, ender dragon)
	private static void registerMobHeads(FabricItemSettings settings) {
		for (SpectrumSkullBlock.SpectrumSkullBlockType type : SpectrumSkullBlock.SpectrumSkullBlockType.values()) {
			Block head = new SpectrumSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_head", head);
			Block wallHead = new SpectrumWallSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL).dropsLike(head));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_wall_head", wallHead);
			BlockItem headItem = new SpectrumSkullBlockItem(head, wallHead, (settings), type.entityType);
			registerBlockItem(type.name().toLowerCase(Locale.ROOT) + "_head", headItem, DyeColor.GRAY);
			
			MOB_HEADS.put(type, head);
			MOB_WALL_HEADS.put(type, wallHead);
		}
	}
	
	public static Block getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType skullType) {
		return MOB_HEADS.get(skullType);
	}
	
	public static SpectrumSkullBlock.SpectrumSkullBlockType getSkullType(Block block) {
		if (block instanceof SpectrumWallSkullBlock) {
			return MOB_WALL_HEADS.inverse().get(block);
		} else {
			return MOB_HEADS.inverse().get(block);
		}
	}
	
	public static Block getMobWallHead(SpectrumSkullBlock.SpectrumSkullBlockType skullType) {
		return MOB_WALL_HEADS.get(skullType);
	}
	
	@Contract(pure = true)
	public static @NotNull Collection<Block> getMobHeads() {
		return MOB_HEADS.values();
	}
	
	@Contract(pure = true)
	public static @NotNull Collection<Block> getMobWallHeads() {
		return MOB_WALL_HEADS.values();
	}
	
	public static void registerClient() {
		
		// Crafting Stations
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), PEDESTAL_BASIC_AMETHYST, PEDESTAL_BASIC_CITRINE, PEDESTAL_BASIC_TOPAZ, PEDESTAL_ALL_BASIC, PEDESTAL_ONYX, PEDESTAL_MOONSTONE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), FUSION_SHRINE_BASALT, FUSION_SHRINE_CALCITE);
		BlockRenderLayerMap.INSTANCE.putBlock(ENCHANTER, RenderLayer.getCutout());
		
		// Gemstones
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_TOPAZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_TOPAZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_TOPAZ_BUD, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_CITRINE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_CITRINE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_CITRINE_BUD, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_ONYX_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_ONYX_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_ONYX_BUD, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_MOONSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_MOONSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_MOONSTONE_BUD, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BISMUTH_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_BISMUTH_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_BISMUTH_BUD, RenderLayer.getCutout());
		
		// Glass
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_GLASS, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RADIANT_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RADIANT_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TINTED_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.VANILLA_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_SEMI_PERMEABLE_GLASS, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDER_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PARTICLE_SPAWNER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CREATIVE_PARTICLE_SPAWNER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CRYSTALLARIEUM, RenderLayer.getTranslucent());
		
		// Gemstone Lights
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CALCITE_LAMP, RenderLayer.getTranslucent());
		
		// Gemstone Lamps
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_BASALT_LAMP, RenderLayer.getTranslucent());
		
		// Noxwood
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IVORY_NOXWOOD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IVORY_NOXWOOD_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EBONY_NOXWOOD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EBONY_NOXWOOD_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLATE_NOXWOOD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLATE_NOXWOOD_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHESTNUT_NOXWOOD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHESTNUT_NOXWOOD_TRAPDOOR, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLATE_NOXWOOD_LAMP, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EBONY_NOXWOOD_LAMP, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IVORY_NOXWOOD_LAMP, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHESTNUT_NOXWOOD_LAMP, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLATE_NOXWOOD_LIGHT, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EBONY_NOXWOOD_LIGHT, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IVORY_NOXWOOD_LIGHT, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHESTNUT_NOXWOOD_LIGHT, RenderLayer.getTranslucent());
		
		// Saplings
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLACK_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLUE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BROWN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CYAN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GRAY_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GREEN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_BLUE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_GRAY_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIME_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MAGENTA_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ORANGE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PINK_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PURPLE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RED_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WHITE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.YELLOW_SAPLING, RenderLayer.getCutout());
		
		// Potted Saplings
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_BLACK_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_BLUE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_BROWN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_CYAN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_GRAY_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_GREEN_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_LIGHT_BLUE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_LIGHT_GRAY_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_LIME_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_MAGENTA_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_ORANGE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_PINK_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_PURPLE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_RED_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_WHITE_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_YELLOW_SAPLING, RenderLayer.getCutout());
		
		// Spore Blossoms
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLACK_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLUE_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BROWN_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CYAN_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GRAY_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GREEN_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_BLUE_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_GRAY_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIME_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MAGENTA_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ORANGE_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PINK_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PURPLE_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RED_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WHITE_SPORE_BLOSSOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.YELLOW_SPORE_BLOSSOM, RenderLayer.getCutout());
		
		// Colored lamps
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLACK_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLUE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BROWN_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CYAN_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GRAY_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GREEN_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_BLUE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIGHT_GRAY_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LIME_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MAGENTA_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ORANGE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PINK_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PURPLE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RED_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WHITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.YELLOW_LAMP, RenderLayer.getTranslucent());
		
		// Decostones
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_DECOSTONE, RenderLayer.getTranslucent());
		
		// Chimes
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CHIME, RenderLayer.getTranslucent());
		
		// Others
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PRESENT, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_BASALT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_CALCITE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_ROUNDEL, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEMORY, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_ROOTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_BULB, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINES, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_PETAL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_PETAL_CARPET, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), NEPHRITE_BLOSSOM_LEAVES, NEPHRITE_BLOSSOM_BULB, NEPHRITE_BLOSSOM_STEM);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), JADEITE_LOTUS_FLOWER, JADEITE_LOTUS_BULB, JADEITE_LOTUS_STEM);

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMARANTH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMARANTH_BUSHEL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_AMARANTH_BUSHEL, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLOOD_ORCHID, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POTTED_BLOOD_ORCHID, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.DIKE_GATE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.INVISIBLE_WALL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PRESERVATION_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TINTED_PRESERVATION_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COURIER_STATUE, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COLOR_PICKER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.INK_DUCT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.INKWELL, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_TIMER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_WIRELESS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_CALCULATOR, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.QUITOXIC_REEDS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MERMAIDS_BRUSH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RESONANT_LILY, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.STUCK_STORM_STONE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CLOVER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FOUR_LEAF_CLOVER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ETHEREAL_PLATFORM, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.UNIVERSE_SPYHOLE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BOTTOMLESS_BUNDLE, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SAG_LEAF, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SAG_BUBBLE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_SAG_BUBBLE, RenderLayer.getCutout());
		
		// Mob Blocks
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AXOLOTL_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BAT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BEE_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLAZE_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CAT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHICKEN_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COW_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CREEPER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDER_DRAGON_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDERMAN_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDERMITE_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EVOKER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FISH_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FOX_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GHAST_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLOW_SQUID_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GOAT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GUARDIAN_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.HORSE_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ILLUSIONER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.OCELOT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PARROT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PHANTOM_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PIG_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PIGLIN_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PUFFERFISH_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RABBIT_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SHEEP_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SHULKER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SILVERFISH_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SKELETON_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLIME_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SNOW_GOLEM_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SPIDER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SQUID_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.STRAY_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.STRIDER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TURTLE_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WITCH_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WITHER_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.WITHER_SKELETON_MOB_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ZOMBIE_MOB_BLOCK, RenderLayer.getTranslucent());
		
		// Shooting stars
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COLORFUL_SHOOTING_STAR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FIERY_SHOOTING_STAR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GEMSTONE_SHOOTING_STAR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLISTERING_SHOOTING_STAR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PRISTINE_SHOOTING_STAR, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.INCANDESCENT_AMALGAM, RenderLayer.getCutout());
		
		// CRYSTALLARIEUM GROWABLES
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_COAL_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_COAL_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COAL_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_COPPER_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_COPPER_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.COPPER_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_DIAMOND_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_DIAMOND_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.DIAMOND_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_EMERALD_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_EMERALD_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EMERALD_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_GLOWSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_GLOWSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLOWSTONE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_GOLD_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_GOLD_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GOLD_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_IRON_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_IRON_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IRON_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_LAPIS_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_LAPIS_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LAPIS_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_PRISMARINE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_PRISMARINE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PRISMARINE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_QUARTZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_QUARTZ_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.QUARTZ_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_REDSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_REDSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_ECHO_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_ECHO_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ECHO_CLUSTER, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_AZURITE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_AZURITE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AZURITE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_MALACHITE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_MALACHITE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MALACHITE_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_BLOODSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_BLOODSTONE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BLOODSTONE_CLUSTER, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_YELLOW_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_RED_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_PINK_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_PURPLE_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_BLACK_DRAGONJAG, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TALL_YELLOW_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TALL_RED_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TALL_PINK_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TALL_PURPLE_DRAGONJAG, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TALL_BLACK_DRAGONJAG, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ALOE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SAWBLADE_HOLLY_BUSH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BRISTLE_SPROUTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.DOOMBLOOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SNAPPING_IVY, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SLATE_NOXSHROOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.IVORY_NOXSHROOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.EBONY_NOXSHROOM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CHESTNUT_NOXSHROOM, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.HUMMINGSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.HUMMINGSTONE_GLASS, RenderLayer.getTranslucent());
	}
	
}