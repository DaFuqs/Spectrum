package de.dafuqs.spectrum.registries;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlock;
import de.dafuqs.spectrum.blocks.chests.CompactingChestBlock;
import de.dafuqs.spectrum.blocks.chests.PrivateChestBlock;
import de.dafuqs.spectrum.blocks.chests.RestockingChestBlock;
import de.dafuqs.spectrum.blocks.chests.SuckingChestBlock;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.decay.DecayAwayBlock;
import de.dafuqs.spectrum.blocks.decay.FadingBlock;
import de.dafuqs.spectrum.blocks.decay.FailingBlock;
import de.dafuqs.spectrum.blocks.decay.TerrorBlock;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.deeper_down_portal.DeeperDownPortalBlock;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlock;
import de.dafuqs.spectrum.blocks.ender.EnderDropperBlock;
import de.dafuqs.spectrum.blocks.ender.EnderHopperBlock;
import de.dafuqs.spectrum.blocks.fluid.LiquidCrystalFluidBlock;
import de.dafuqs.spectrum.blocks.fluid.MudFluidBlock;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlock;
import de.dafuqs.spectrum.blocks.gemstone.SpectrumBuddingBlock;
import de.dafuqs.spectrum.blocks.gemstone.SpectrumGemstoneBlock;
import de.dafuqs.spectrum.blocks.gravity.GravitableBlock;
import de.dafuqs.spectrum.blocks.gravity.GravityBlockItem;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.LavaSpongeBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.WetLavaSpongeBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.WetLavaSpongeItem;
import de.dafuqs.spectrum.blocks.melon.AttachedGlisteringStemBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringMelonBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringStemBlock;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockItem;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumWallSkullBlock;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlock;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.PastelNetworkNodeBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockItem;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.blocks.spirit_sallow.*;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlock;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockItem;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.sound.SpectrumBlockSoundGroups;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import de.dafuqs.spectrum.worldgen.ColoredSaplingGenerator;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;

public class SpectrumBlocks {

	private static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}
	private static boolean always(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	private static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public static FabricItemSettings generalItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0);
	public static FabricItemSettings generalItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1);
	public static FabricItemSettings generalItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(8);
	public static FabricItemSettings generalItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(16);
	public static FabricItemSettings generalItemSettingsUncommon = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalItemSettingsUncommonEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON).maxCount(8);
	public static FabricItemSettings generalItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE);
	public static FabricItemSettings generalItemSettingsRareEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(8);
	
	public static FabricItemSettings resourcesItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2);
	public static FabricItemSettings resourcesItemSettingsUncommon = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings resourcesItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).rarity(Rarity.RARE);
	public static FabricItemSettings decorationItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0);
	public static FabricItemSettings decorationItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).rarity(Rarity.RARE);
	public static FabricItemSettings coloredWoodItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(1);
	public static FabricItemSettings mobHeadItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(2).rarity(Rarity.UNCOMMON);

	// PEDESTALS
	public static final Block PEDESTAL_BASIC_TOPAZ = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.BASIC_TOPAZ);
	public static final Block PEDESTAL_BASIC_AMETHYST = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.BASIC_AMETHYST);
	public static final Block PEDESTAL_BASIC_CITRINE = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.BASIC_CITRINE);
	public static final Block PEDESTAL_ALL_BASIC = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.CMY);
	public static final Block PEDESTAL_ONYX = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.ONYX);
	public static final Block PEDESTAL_MOONSTONE = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), PedestalBlock.PedestalVariant.MOONSTONE);

	private static final FabricBlockSettings FUSION_SHINE_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F).requiresTool().nonOpaque().luminance(value -> value.get(FusionShrineBlock.LIGHT_LEVEL));
	public static final Block FUSION_SHRINE_BASALT = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	public static final Block FUSION_SHRINE_CALCITE = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	public static final Block ENCHANTER = new EnchanterBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F).nonOpaque());
	public static final Block ITEM_BOWL_BASALT = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f));
	public static final Block ITEM_BOWL_CALCITE = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f));

	// GEMS
	public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER).luminance((state) -> 6));
	public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_TOPAZ_BUD).luminance((state) -> 6));
	public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_TOPAZ_BUD).luminance((state) -> 4));
	public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_TOPAZ_BUD).luminance((state) -> 2));
	public static final Block TOPAZ_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
	public static final Block BUDDING_TOPAZ = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);

	public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER).luminance((state) -> 7));
	public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_CITRINE_BUD).luminance((state) -> 7));
	public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_CITRINE_BUD).luminance((state) -> 5));
	public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_CITRINE_BUD).luminance((state) -> 3));
	public static final Block CITRINE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
	public static final Block BUDDING_CITRINE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);

	public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER).luminance((state) -> 3));
	public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_ONYX_BUD).luminance((state) -> 5));
	public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_ONYX_BUD).luminance((state) -> 3));
	public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD).luminance((state) -> 1));
	public static final Block ONYX_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	public static final Block BUDDING_ONYX = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	
	public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER).luminance((state) -> 15));
	public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_MOONSTONE_BUD).luminance((state) -> 8));
	public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_MOONSTONE_BUD).luminance((state) -> 5));
	public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_MOONSTONE_BUD).luminance((state) -> 4));
	public static final Block MOONSTONE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().luminance((state) -> 3), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
	public static final Block BUDDING_MOONSTONE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().luminance((state) -> 3), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);

	public static final Block SPECTRAL_SHARD_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.SPECTRAL_BLOCK).requiresTool(), SpectrumSoundEvents.SPECTRAL_BLOCK_HIT, SpectrumSoundEvents.SPECTRAL_BLOCK_CHIME);
	public static final Block BEDROCK_STORAGE_BLOCK = new BlockWithTooltip(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(100.0F, 3600.0F), new TranslatableText("spectrum.tooltip.dragon_and_wither_immune"));

	private static final FabricBlockSettings gemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool();
	private static final UniformIntProvider gemOreExperienceProvider = UniformIntProvider.create(1, 4);
	public static final Block TOPAZ_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.CYAN, false);
	public static final Block AMETHYST_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.MAGENTA, false);
	public static final Block CITRINE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.YELLOW, false);
	public static final Block ONYX_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.BLACK, false);
	public static final Block MOONSTONE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.WHITE, false);

	private static final FabricBlockSettings deepslateGemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE);
	public static final Block DEEPSLATE_TOPAZ_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.CYAN, true);
	public static final Block DEEPSLATE_AMETHYST_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.MAGENTA, true);
	public static final Block DEEPSLATE_CITRINE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.YELLOW, true);
	public static final Block DEEPSLATE_ONYX_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.BLACK, true);
	public static final Block DEEPSLATE_MOONSTONE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, GemstoneColor.WHITE, true);

	private static final FabricBlockSettings gemstoneStorageBlockSettings = FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F);
	public static final Block TOPAZ_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block AMETHYST_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block CITRINE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block ONYX_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block MOONSTONE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block SPECTRAL_SHARD_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);

	public static final Block SMOOTH_BASALT_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
	public static final Block SMOOTH_BASALT_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
	public static final Block SMOOTH_BASALT_STAIRS = new SpectrumStairsBlock(Blocks.TUFF.getDefaultState(), FabricBlockSettings.copyOf(Blocks.TUFF));

	public static final Block POLISHED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).strength(2.0F, 5.0F));
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
	public static final Block MOONSTONE_CHISELED_BASALT = new PillarBlock(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(12));

	public static final Block CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
	public static final Block CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
	public static final Block CALCITE_STAIRS = new SpectrumStairsBlock(Blocks.CALCITE.getDefaultState(), FabricBlockSettings.copyOf(Blocks.CALCITE));

	public static final Block POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_CREST = new CardinalFacingBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block CHISELED_POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block NOTCHED_POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
	public static final Block POLISHED_CALCITE_STAIRS = new SpectrumStairsBlock(POLISHED_CALCITE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_BASALT));

	public static final Block CALCITE_BRICKS = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
	public static final Block CALCITE_BRICK_STAIRS = new SpectrumStairsBlock(CALCITE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(BASALT_BRICKS));

	public static final Block TOPAZ_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(6));
	public static final Block AMETHYST_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(5));
	public static final Block CITRINE_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(7));
	public static final Block ONYX_CHISELED_CALCITE = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(3));
	public static final Block MOONSTONE_CHISELED_CALCITE = new PillarBlock(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5).luminance(12));

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
	public static final Block TOPAZ_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
	public static final Block AMETHYST_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
	public static final Block CITRINE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
	public static final Block ONYX_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
	public static final Block MOONSTONE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
	public static final Block GLOWING_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS).luminance(value -> 12));

	public static final Block ETHEREAL_PLATFORM = new EtherealGlassBlock(FabricBlockSettings.copy(Blocks.SMALL_AMETHYST_BUD).sounds(BlockSoundGroup.AMETHYST_BLOCK));
	
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
	public static final Block VANILLA_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);
	public static final Block TINTED_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.TINTED_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), true);
	public static final Block GLOWING_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.GLOWING_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never).luminance((state) -> 15), false);

	public static final Block TOPAZ_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);
	public static final Block AMETHYST_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);
	public static final Block CITRINE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);
	public static final Block ONYX_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);
	public static final Block MOONSTONE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), false);

	// MELON
	public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.copyOf(Blocks.MELON));
	public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.MELON_STEM));
	public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.ATTACHED_MELON_STEM));

	// SAPLING
	public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).ticksRandomly());

	// TECHNICAL WITHOUT CORRESPONDING ITEMS
	public static final Block BLOCK_FLOODER = new BlockFlooderBlock(FabricBlockSettings.of(Material.STONE));
	public static final Block WAND_LIGHT_BLOCK = new WandLightBlock(FabricBlockSettings.copyOf(Blocks.LIGHT).sounds(SpectrumBlockSoundGroups.WAND_LIGHT).breakInstantly().breakByHand(true));

	// DECAY
	public static final Block FADING = new FadingBlock(FabricBlockSettings.of(SpectrumBlockMaterials.DECAY, MapColor.BLACK).ticksRandomly().requiresTool().strength(0.5F, 0.5F), SpectrumBlockTags.FADING_CONVERSIONS, null,1,  1F);
	public static final Block FAILING = new FailingBlock(FabricBlockSettings.copyOf(FADING).strength(20.0F, 50.0F), null, SpectrumBlockTags.FAILING_SAFE, 2,  2.5F);
	public static final Block RUIN = new TerrorBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F), null, SpectrumBlockTags.RUIN_SAFE, 3, 5F);
	public static final Block TERROR = new TerrorBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F), null, SpectrumBlockTags.TERROR_SAFE, 4, 7.5F);
	public static final Block DECAY_AWAY = new DecayAwayBlock(FabricBlockSettings.copyOf(Blocks.DIRT));

	// FLUIDS
	public static final Block LIQUID_CRYSTAL = new LiquidCrystalFluidBlock(SpectrumFluids.LIQUID_CRYSTAL, FabricBlockSettings.copyOf(Blocks.WATER).luminance((state) -> 8));
	public static final Block MUD = new MudFluidBlock(SpectrumFluids.MUD, FabricBlockSettings.copyOf(Blocks.WATER).suffocates(SpectrumBlocks::always));

	// PASTEL NETWORK
	public static final Block CONNECTION_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), "block.spectrum.connection_node.tooltip");
	public static final Block PROVIDER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), "block.spectrum.provider_node.tooltip");
	public static final Block STORAGE_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER), "block.spectrum.storage_node.tooltip");
	public static final Block PUSHER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER), "block.spectrum.pusher_node.tooltip");
	public static final Block PULLER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER), "block.spectrum.puller_node.tooltip");
	public static final Block INTERACTION_NODE = new Block(FabricBlockSettings.of(Material.AMETHYST).hardness(5.0F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER));
	
	// COLORED TREES
	private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
	private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 2);
	private static final FabricBlockSettings spiritSallowLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 8);
	private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG).luminance((state) -> 5);

	public static final Block BLACK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block BLACK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block BLACK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLACK), coloredSaplingBlockSettings);
	public static final Block BLACK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BLACK_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BLACK_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BLACK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BLACK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BLACK_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BLACK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLUE), coloredSaplingBlockSettings);
	public static final Block BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BLUE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BLUE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BLUE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block BROWN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block BROWN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block BROWN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BROWN), coloredSaplingBlockSettings);
	public static final Block BROWN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BROWN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BROWN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BROWN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BROWN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BROWN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BROWN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block CYAN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block CYAN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block CYAN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.CYAN), coloredSaplingBlockSettings);
	public static final Block CYAN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block CYAN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block CYAN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block CYAN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block CYAN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block CYAN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block CYAN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GRAY), coloredSaplingBlockSettings);
	public static final Block GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block GRAY_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block GRAY_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block GRAY_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block GREEN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block GREEN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block GREEN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GREEN), coloredSaplingBlockSettings);
	public static final Block GREEN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block GREEN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block GREEN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block GREEN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block GREEN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block GREEN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block GREEN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block LIGHT_BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIGHT_BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIGHT_BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_BLUE), coloredSaplingBlockSettings);
	public static final Block LIGHT_BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIGHT_BLUE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIGHT_BLUE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIGHT_BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIGHT_BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIGHT_BLUE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIGHT_BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block LIGHT_GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIGHT_GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIGHT_GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_GRAY), coloredSaplingBlockSettings);
	public static final Block LIGHT_GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIGHT_GRAY_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIGHT_GRAY_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIGHT_GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIGHT_GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIGHT_GRAY_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIGHT_GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block LIME_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIME_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIME_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIME), coloredSaplingBlockSettings);
	public static final Block LIME_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIME_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIME_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIME_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIME_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIME_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIME_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block MAGENTA_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block MAGENTA_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block MAGENTA_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.MAGENTA), coloredSaplingBlockSettings);
	public static final Block MAGENTA_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block MAGENTA_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block MAGENTA_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block MAGENTA_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block MAGENTA_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block MAGENTA_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block MAGENTA_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block ORANGE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block ORANGE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block ORANGE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.ORANGE), coloredSaplingBlockSettings);
	public static final Block ORANGE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block ORANGE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block ORANGE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block ORANGE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block ORANGE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block ORANGE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block ORANGE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block PINK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block PINK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block PINK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PINK), coloredSaplingBlockSettings);
	public static final Block PINK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block PINK_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block PINK_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block PINK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block PINK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block PINK_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block PINK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block PURPLE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block PURPLE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block PURPLE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PURPLE), coloredSaplingBlockSettings);
	public static final Block PURPLE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block PURPLE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block PURPLE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block PURPLE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block PURPLE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block PURPLE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block PURPLE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block RED_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block RED_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block RED_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.RED), coloredSaplingBlockSettings);
	public static final Block RED_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block RED_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block RED_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block RED_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block RED_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block RED_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block RED_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block WHITE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block WHITE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block WHITE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.WHITE), coloredSaplingBlockSettings);
	public static final Block WHITE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block WHITE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block WHITE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block WHITE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block WHITE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block WHITE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block WHITE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	public static final Block YELLOW_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block YELLOW_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block YELLOW_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.YELLOW), coloredSaplingBlockSettings);
	public static final Block YELLOW_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block YELLOW_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block YELLOW_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block YELLOW_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block YELLOW_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block YELLOW_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block YELLOW_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

	// FLAT COLORED BLOCKS
	private static final FabricBlockSettings flatColoredBlockBlockSettings = FabricBlockSettings.of(Material.STONE).hardness(2.5F).requiresTool().luminance(1).postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always);
	public static final Block BLACK_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block BLUE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block BROWN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block CYAN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block GRAY_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block GREEN_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block LIGHT_BLUE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block LIGHT_GRAY_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block LIME_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block MAGENTA_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block ORANGE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block PINK_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block PURPLE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block RED_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block WHITE_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);
	public static final Block YELLOW_GLOWBLOCK = new GlowBlock(flatColoredBlockBlockSettings);

	// COLORED LAMPS
	private static final FabricBlockSettings coloredLampBlockBlockSettings = FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP);
	public static final Block BLACK_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block BLUE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block BROWN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block CYAN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block GRAY_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block GREEN_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block LIGHT_BLUE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block LIGHT_GRAY_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block LIME_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block MAGENTA_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block ORANGE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block PINK_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block PURPLE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block RED_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block WHITE_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);
	public static final Block YELLOW_LAMP = new ColoredLightBlock(coloredLampBlockBlockSettings);

	// PIGMENT STORAGE BLOCKS
	private static final FabricBlockSettings pigmentStorageBlockBlockSettings = FabricBlockSettings.of(Material.WOOL).strength(1.0F).sounds(BlockSoundGroup.WOOL);
	public static final Block BLACK_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block BLUE_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block BROWN_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block CYAN_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block GRAY_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block GREEN_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block LIGHT_BLUE_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block LIGHT_GRAY_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block LIME_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block MAGENTA_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block ORANGE_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block PINK_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block PURPLE_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block RED_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block WHITE_BLOCK = new Block(pigmentStorageBlockBlockSettings);
	public static final Block YELLOW_BLOCK = new Block(pigmentStorageBlockBlockSettings);

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

	// SPARKLESTONE LIGHTS
	private static final FabricBlockSettings sparklestoneLightBlockSettings = FabricBlockSettings.of(Material.STONE).strength(1.0F, 1.0F).nonOpaque().luminance(15);
	public static final Block BASALT_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block CALCITE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block STONE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block GRANITE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block DIORITE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block ANDESITE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);
	public static final Block DEEPSLATE_SPARKLESTONE_LIGHT = new SparklestoneLightBlock(sparklestoneLightBlockSettings);

	// ORES
	public static final Block SPARKLESTONE_ORE = new SparklestoneOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), false);
	public static final Block DEEPSLATE_SPARKLESTONE_ORE = new SparklestoneOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), true);
	public static final Block AZURITE_ORE = new AzuriteOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), false);
	public static final Block DEEPSLATE_AZURITE_ORE = new AzuriteOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), true);
	public static final Block PALETUR_ORE = new PaleturOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.DARK_RED).requiresTool().requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE), UniformIntProvider.create(2, 4));
	public static final Block SCARLET_ORE = new ScarletOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3.0F, 9.0F).requiresTool(), UniformIntProvider.create(3, 5));

	public static final Block SPARKLESTONE_BLOCK = new SparklestoneBlock(FabricBlockSettings.of(Material.GLASS, MapColor.YELLOW).strength(2.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 15));
	public static final Block AZURITE_BLOCK = new SpectrumFacingBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));
	public static final Block PALETUR_FRAGMENT_BLOCK = new GravitableBlock(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.02F);
	public static final Block SCARLET_FRAGMENT_BLOCK = new GravitableBlock(FabricBlockSettings.of(Material.METAL, MapColor.DARK_RED).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), -0.2F);

	// FUNCTIONAL BLOCKS
	public static final Block PRIVATE_CHEST = new PrivateChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 3600000.0F).sounds(BlockSoundGroup.STONE));
	public static final Block COMPACTING_CHEST = new CompactingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block RESTOCKING_CHEST = new RestockingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block SUCKING_CHEST = new SuckingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));

	public static final Block PARTICLE_SPAWNER = new ParticleSpawnerBlock(FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque());
	public static final Block BEDROCK_ANVIL = new BedrockAnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).requiresTool().strength(8.0F, 8.0F).sounds(BlockSoundGroup.METAL));

	// SOLID LIQUID CRYSTAL
	public static final Block FROSTBITE_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
	public static final Block BLAZING_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
	public static final Block RESONANT_LILY = new FlowerBlock(StatusEffects.INSTANT_HEALTH, 5, FabricBlockSettings.copyOf(Blocks.POPPY));
	
	public static final Block QUITOXIC_REEDS = new QuitoxicReedsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).ticksRandomly());
	public static final Block MERMAIDS_BRUSH = new MermaidsBrushBlock(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).ticksRandomly().luminance(value -> 3));

	public static final Block ENDER_TREASURE = new EnderTreasureBlock(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK));
	public static final Block CRACKED_END_PORTAL_FRAME = new CrackedEndPortalFrameBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL_FRAME));

	public static final Block LAVA_SPONGE = new LavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.SPONGE));
	public static final Block WET_LAVA_SPONGE = new WetLavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.WET_SPONGE).luminance(9).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));

	public static final Block LIGHT_LEVEL_DETECTOR = new BlockLightDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block WEATHER_DETECTOR =  new WeatherDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ITEM_DETECTOR = new ItemDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block PLAYER_DETECTOR = new PlayerDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ENTITY_DETECTOR = new EntityDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));

	public static final Block REDSTONE_CALCULATOR = new RedstoneCalculatorBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_TIMER = new RedstoneTimerBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_WIRELESS = new RedstoneWirelessBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block BLOCK_PLACER = new BlockPlacerBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));

	public static final Block ENDER_DROPPER = new EnderDropperBlock(FabricBlockSettings.copyOf(Blocks.DROPPER).requiresTool().strength(15F, 60.0F));
	public static final Block ENDER_HOPPER = new EnderHopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER).requiresTool().strength(15F, 60.0F));

	private static final BiMap<SpectrumSkullBlock.Type, Block> MOB_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.Type.class);
	private static final BiMap<SpectrumSkullBlock.Type, Block> MOB_WALL_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.Type.class);

	public static final Block SPIRIT_SALLOW_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_LEAVES = new SpiritSallowLeavesBlock(spiritSallowLeavesBlockSettings);
	public static final Block SPIRIT_SALLOW_ROOTS = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_HEART = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).luminance(11));

	private static final FabricBlockSettings spiritVinesBlockSettings = FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.CAVE_VINES);
	public static final Block CYAN_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), GemstoneColor.CYAN);
	public static final Block CYAN_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), GemstoneColor.CYAN);
	public static final Block MAGENTA_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), GemstoneColor.MAGENTA);
	public static final Block MAGENTA_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), GemstoneColor.MAGENTA);
	public static final Block YELLOW_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), GemstoneColor.YELLOW);
	public static final Block YELLOW_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), GemstoneColor.YELLOW);
	public static final Block BLACK_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), GemstoneColor.BLACK);
	public static final Block BLACK_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), GemstoneColor.BLACK);
	public static final Block WHITE_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), GemstoneColor.WHITE);
	public static final Block WHITE_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), GemstoneColor.WHITE);

	public static final Block SACRED_SOIL = new ExtraTickFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND));
	public static final Block STUCK_LIGHTNING_STONE = new LightningStoneBlock(FabricBlockSettings.copyOf(Blocks.DIRT));
	
	private static final FabricBlockSettings shootingStartBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE);
	public static final Block GLISTERING_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GLISTERING);
	public static final Block FIERY_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.FIERY);
	public static final Block COLORFUL_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.COLORFUL);
	public static final Block PRISTINE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.PRISTINE);
	public static final Block GEMSTONE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GEMSTONE);

	public static final Block DEEPER_DOWN_PORTAL = new DeeperDownPortalBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL));
	
	public static final Block UPGRADE_SPEED = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 0.25);
	public static final Block UPGRADE_SPEED2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 1.0);
	public static final Block UPGRADE_SPEED3 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 8.0);
	public static final Block UPGRADE_EFFICIENCY = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EFFICIENCY, 0.05);
	public static final Block UPGRADE_EFFICIENCY2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EFFICIENCY, 0.25);
	public static final Block UPGRADE_YIELD = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.YIELD, 0.05);
	public static final Block UPGRADE_YIELD2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.YIELD, 0.25);
	public static final Block UPGRADE_EXPERIENCE = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EXPERIENCE, 1.0);
	public static final Block UPGRADE_EXPERIENCE2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.EXPERIENCE, 4.0);

	public static final Block REDSTONE_SAND = new RedstoneGravityBlock(FabricBlockSettings.copyOf(Blocks.SAND));
	public static final Block ENDER_GLASS = new RedstoneTransparencyBlock(FabricBlockSettings.copyOf(Blocks.GLASS).nonOpaque()
			.allowsSpawning((state, world, pos, entityType) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
			.solidBlock(SpectrumBlocks::never).suffocates((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
			.blockVision((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID));

	public static final Block CLOVER = new CloverBlock(FabricBlockSettings.copyOf(Blocks.GRASS));
	public static final Block FOUR_LEAF_CLOVER = new FourLeafCloverBlock(FabricBlockSettings.copyOf(Blocks.GRASS));

	private static void registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
	}

	private static void registerBlockItem(String name, BlockItem blockItem) {
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
	}

	private static void registerBlockWithItem(String name, Block block, FabricItemSettings itemSettings) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), new BlockItem(block, itemSettings));
	}

	private static void registerBlockWithItem(String name, Block block, BlockItem blockItem) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
	}

	public static void register() {
		registerBlockWithItem("pedestal_basic_topaz", PEDESTAL_BASIC_TOPAZ, new PedestalBlockItem(PEDESTAL_BASIC_TOPAZ, generalItemSettingsSingle, PedestalBlock.PedestalVariant.BASIC_TOPAZ));
		registerBlockWithItem("pedestal_basic_amethyst", PEDESTAL_BASIC_AMETHYST, new PedestalBlockItem(PEDESTAL_BASIC_AMETHYST, generalItemSettingsSingle, PedestalBlock.PedestalVariant.BASIC_AMETHYST));
		registerBlockWithItem("pedestal_basic_citrine", PEDESTAL_BASIC_CITRINE, new PedestalBlockItem(PEDESTAL_BASIC_CITRINE, generalItemSettingsSingle, PedestalBlock.PedestalVariant.BASIC_CITRINE));
		registerBlockWithItem("pedestal_all_basic", PEDESTAL_ALL_BASIC, new PedestalBlockItem(PEDESTAL_ALL_BASIC, generalItemSettingsSingle, PedestalBlock.PedestalVariant.CMY));
		registerBlockWithItem("pedestal_onyx", PEDESTAL_ONYX, new PedestalBlockItem(PEDESTAL_ONYX, generalItemSettingsSingle, PedestalBlock.PedestalVariant.ONYX));
		registerBlockWithItem("pedestal_moonstone", PEDESTAL_MOONSTONE, new PedestalBlockItem(PEDESTAL_MOONSTONE, generalItemSettingsSingle, PedestalBlock.PedestalVariant.MOONSTONE));
		registerBlockWithItem("fusion_shrine_basalt", FUSION_SHRINE_BASALT, generalItemSettingsSingle);
		registerBlockWithItem("fusion_shrine_calcite", FUSION_SHRINE_CALCITE, generalItemSettingsSingle);
		registerBlockWithItem("enchanter", ENCHANTER, generalItemSettingsSingle);
		registerBlockWithItem("item_bowl_basalt", ITEM_BOWL_BASALT, generalItemSettingsSixteen);
		registerBlockWithItem("item_bowl_calcite", ITEM_BOWL_CALCITE, generalItemSettingsSixteen);
		
		registerBlockWithItem("upgrade_speed", UPGRADE_SPEED, new UpgradeBlockItem(UPGRADE_SPEED, generalItemSettingsEight, "upgrade_speed"));
		registerBlockWithItem("upgrade_speed2", UPGRADE_SPEED2, new UpgradeBlockItem(UPGRADE_SPEED2, generalItemSettingsUncommonEight, "upgrade_speed2"));
		registerBlockWithItem("upgrade_speed3", UPGRADE_SPEED3, new UpgradeBlockItem(UPGRADE_SPEED3, generalItemSettingsRareEight, "upgrade_speed3"));
		registerBlockWithItem("upgrade_efficiency", UPGRADE_EFFICIENCY, new UpgradeBlockItem(UPGRADE_EFFICIENCY, generalItemSettingsUncommonEight, "upgrade_efficiency"));
		registerBlockWithItem("upgrade_efficiency2", UPGRADE_EFFICIENCY2, new UpgradeBlockItem(UPGRADE_EFFICIENCY2, generalItemSettingsRareEight, "upgrade_efficiency2"));
		registerBlockWithItem("upgrade_yield", UPGRADE_YIELD, new UpgradeBlockItem(UPGRADE_YIELD, generalItemSettingsUncommonEight, "upgrade_yield"));
		registerBlockWithItem("upgrade_yield2", UPGRADE_YIELD2, new UpgradeBlockItem(UPGRADE_YIELD2, generalItemSettingsRareEight, "upgrade_yield2"));
		registerBlockWithItem("upgrade_experience", UPGRADE_EXPERIENCE, new UpgradeBlockItem(UPGRADE_EXPERIENCE, generalItemSettingsEight, "upgrade_experience"));
		registerBlockWithItem("upgrade_experience2", UPGRADE_EXPERIENCE2, new UpgradeBlockItem(UPGRADE_EXPERIENCE2, generalItemSettingsUncommonEight, "upgrade_experience2"));
		
		registerPastelNetworkNodes(generalItemSettingsSixteen);
		registerStoneBlocks(decorationItemSettings);
		registerGemBlocks(resourcesItemSettings);
		registerBlockWithItem("spectral_shard_block", SPECTRAL_SHARD_BLOCK, resourcesItemSettingsRare);
		registerBlockWithItem("bedrock_storage_block", BEDROCK_STORAGE_BLOCK, decorationItemSettingsRare);
		registerShootingStarBlocks(resourcesItemSettingsUncommon);
		
		registerGemOreBlocks(resourcesItemSettings);
		registerOreBlocks(resourcesItemSettings);
		registerOreStorageBlocks(decorationItemSettings);
		registerGemstoneLamps(decorationItemSettings);
		registerSparklestoneLights(decorationItemSettings);
		registerRunes(decorationItemSettings);
		registerGemstoneGlass(decorationItemSettings);
		registerPlayerOnlyGlass(generalItemSettings);
		registerGemstoneChimes(decorationItemSettings);
		registerDecoStones(decorationItemSettings);
		registerPigmentStorageBlocks(decorationItemSettings);
		registerColoredLamps(decorationItemSettings);
		registerGlowBlocks(decorationItemSettings);
		registerSporeBlossoms(decorationItemSettings);
		registerColoredWood(coloredWoodItemSettings);
		registerRedstone(generalItemSettings);
		registerMagicalBlocks(generalItemSettings);
		registerSpiritTree(generalItemSettings);
		registerMobHeads(mobHeadItemSettings);

		// Decay
		registerBlock("fading", FADING);
		registerBlock("failing", FAILING);
		registerBlock("ruin", RUIN);
		registerBlock("terror", TERROR);
		registerBlock("decay_away", DECAY_AWAY);

		// Fluids + Products
		registerBlock("mud", MUD);
		registerBlock("liquid_crystal", LIQUID_CRYSTAL);
		registerBlockWithItem("frostbite_crystal", FROSTBITE_CRYSTAL, generalItemSettings);
		registerBlockWithItem("blazing_crystal", BLAZING_CRYSTAL, generalItemSettings);
		registerBlockWithItem("resonant_lily", RESONANT_LILY, generalItemSettings);
		registerBlockWithItem("clover", CLOVER, resourcesItemSettings);
		registerBlockWithItem("four_leaf_clover", FOUR_LEAF_CLOVER, resourcesItemSettings);

		// Worldgen
		registerBlockWithItem("quitoxic_reeds", QUITOXIC_REEDS, resourcesItemSettings);
		registerBlockWithItem("mermaids_brush", MERMAIDS_BRUSH, resourcesItemSettings);
		registerBlockWithItem("ender_treasure", ENDER_TREASURE, resourcesItemSettings);

		registerBlockWithItem("bedrock_anvil", BEDROCK_ANVIL, generalItemSettings);
		registerBlockWithItem("cracked_end_portal_frame", CRACKED_END_PORTAL_FRAME, generalItemSettings);

		// Technical Blocks without items
		registerBlock("deeper_down_portal", DEEPER_DOWN_PORTAL);
		registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
		registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);
		registerBlock("stuck_lightning_stone", STUCK_LIGHTNING_STONE);
		registerBlock("wand_light", WAND_LIGHT_BLOCK);
		registerBlock("block_flooder", BLOCK_FLOODER);
	}

	private static void registerRedstone(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("light_level_detector", LIGHT_LEVEL_DETECTOR, fabricItemSettings);
		registerBlockWithItem("weather_detector", WEATHER_DETECTOR, fabricItemSettings);
		registerBlockWithItem("item_detector", ITEM_DETECTOR, fabricItemSettings);
		registerBlockWithItem("player_detector", PLAYER_DETECTOR, fabricItemSettings);
		registerBlockWithItem("entity_detector", ENTITY_DETECTOR, fabricItemSettings);

		registerBlockWithItem("redstone_timer", REDSTONE_TIMER, fabricItemSettings);
		registerBlockWithItem("redstone_calculator", REDSTONE_CALCULATOR, fabricItemSettings);
		registerBlockWithItem("redstone_wireless", REDSTONE_WIRELESS, fabricItemSettings);
		
		registerBlockWithItem("redstone_sand", REDSTONE_SAND, fabricItemSettings);
		registerBlockWithItem("ender_glass", ENDER_GLASS, fabricItemSettings);
		registerBlockWithItem("block_placer", BLOCK_PLACER, fabricItemSettings);
	}

	private static void registerMagicalBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("private_chest", PRIVATE_CHEST, fabricItemSettings);
		registerBlockWithItem("compacting_chest", COMPACTING_CHEST, generalItemSettings);
		registerBlockWithItem("restocking_chest", RESTOCKING_CHEST, generalItemSettings);
		registerBlockWithItem("sucking_chest", SUCKING_CHEST, generalItemSettings);

		registerBlockWithItem("ender_hopper", ENDER_HOPPER, fabricItemSettings);
		registerBlockWithItem("ender_dropper", ENDER_DROPPER, fabricItemSettings);
		registerBlockWithItem("particle_spawner", PARTICLE_SPAWNER, fabricItemSettings);

		registerBlockWithItem("glistering_melon", GLISTERING_MELON, generalItemSettings);

		registerBlockWithItem("lava_sponge", LAVA_SPONGE, fabricItemSettings);
		registerBlockWithItem("wet_lava_sponge", WET_LAVA_SPONGE, new WetLavaSpongeItem(WET_LAVA_SPONGE, new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(1).recipeRemainder(LAVA_SPONGE.asItem())));
		
		registerBlockWithItem("ethereal_platform", ETHEREAL_PLATFORM, fabricItemSettings);
	}

	private static void registerPigmentStorageBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_block", WHITE_BLOCK, fabricItemSettings);
		registerBlockWithItem("orange_block", ORANGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("magenta_block", MAGENTA_BLOCK, fabricItemSettings);
		registerBlockWithItem("light_blue_block", LIGHT_BLUE_BLOCK, fabricItemSettings);
		registerBlockWithItem("yellow_block", YELLOW_BLOCK, fabricItemSettings);
		registerBlockWithItem("lime_block", LIME_BLOCK, fabricItemSettings);
		registerBlockWithItem("pink_block", PINK_BLOCK, fabricItemSettings);
		registerBlockWithItem("gray_block", GRAY_BLOCK, fabricItemSettings);
		registerBlockWithItem("light_gray_block", LIGHT_GRAY_BLOCK, fabricItemSettings);
		registerBlockWithItem("cyan_block", CYAN_BLOCK, fabricItemSettings);
		registerBlockWithItem("purple_block", PURPLE_BLOCK, fabricItemSettings);
		registerBlockWithItem("blue_block", BLUE_BLOCK, fabricItemSettings);
		registerBlockWithItem("brown_block", BROWN_BLOCK, fabricItemSettings);
		registerBlockWithItem("green_block", GREEN_BLOCK, fabricItemSettings);
		registerBlockWithItem("red_block", RED_BLOCK, fabricItemSettings);
		registerBlockWithItem("black_block", BLACK_BLOCK, fabricItemSettings);
	}

	private static void registerSpiritTree(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("ominous_sapling", OMINOUS_SAPLING, new OminousSaplingBlockItem(OMINOUS_SAPLING, fabricItemSettings));

		registerBlockWithItem("spirit_sallow_roots", SPIRIT_SALLOW_ROOTS, fabricItemSettings);
		registerBlockWithItem("spirit_sallow_log", SPIRIT_SALLOW_LOG, fabricItemSettings);
		registerBlockWithItem("spirit_sallow_leaves", SPIRIT_SALLOW_LEAVES, fabricItemSettings);
		registerBlockWithItem("spirit_sallow_heart", SPIRIT_SALLOW_HEART, fabricItemSettings);

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

		registerBlockWithItem("sacred_soil", SACRED_SOIL, fabricItemSettings);
	}

	private static void registerOreBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("sparklestone_ore", SPARKLESTONE_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_sparklestone_ore", DEEPSLATE_SPARKLESTONE_ORE, fabricItemSettings);

		registerBlockWithItem("azurite_ore", AZURITE_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_azurite_ore", DEEPSLATE_AZURITE_ORE, fabricItemSettings);

		registerBlockWithItem("scarlet_ore", SCARLET_ORE, new GravityBlockItem(SCARLET_ORE, fabricItemSettings, 1.01F));
		registerBlockWithItem("paletur_ore", PALETUR_ORE, new GravityBlockItem(PALETUR_ORE, fabricItemSettings, 0.99F));
	}

	private static void registerOreStorageBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_storage_block", TOPAZ_STORAGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("amethyst_storage_block", AMETHYST_STORAGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("citrine_storage_block", CITRINE_STORAGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("onyx_storage_block", ONYX_STORAGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("moonstone_storage_block", MOONSTONE_STORAGE_BLOCK, fabricItemSettings);
		registerBlockWithItem("spectral_shard_storage_block", SPECTRAL_SHARD_STORAGE_BLOCK, fabricItemSettings);
		
		registerBlockWithItem("azurite_block", AZURITE_BLOCK, decorationItemSettings);
		registerBlockWithItem("sparklestone_block", SPARKLESTONE_BLOCK, decorationItemSettings);
		registerBlockWithItem("scarlet_fragment_block", SCARLET_FRAGMENT_BLOCK, new GravityBlockItem(SCARLET_FRAGMENT_BLOCK, fabricItemSettings, 1.02F));
		registerBlockWithItem("paletur_fragment_block", PALETUR_FRAGMENT_BLOCK, new GravityBlockItem(PALETUR_FRAGMENT_BLOCK, fabricItemSettings, 0.98F));
	}

	private static void registerColoredLamps(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_lamp", WHITE_LAMP, fabricItemSettings);
		registerBlockWithItem("orange_lamp", ORANGE_LAMP, fabricItemSettings);
		registerBlockWithItem("magenta_lamp", MAGENTA_LAMP, fabricItemSettings);
		registerBlockWithItem("light_blue_lamp", LIGHT_BLUE_LAMP, fabricItemSettings);
		registerBlockWithItem("yellow_lamp", YELLOW_LAMP, fabricItemSettings);
		registerBlockWithItem("lime_lamp", LIME_LAMP, fabricItemSettings);
		registerBlockWithItem("pink_lamp", PINK_LAMP, fabricItemSettings);
		registerBlockWithItem("gray_lamp", GRAY_LAMP, fabricItemSettings);
		registerBlockWithItem("light_gray_lamp", LIGHT_GRAY_LAMP, fabricItemSettings);
		registerBlockWithItem("cyan_lamp", CYAN_LAMP, fabricItemSettings);
		registerBlockWithItem("purple_lamp", PURPLE_LAMP, fabricItemSettings);
		registerBlockWithItem("blue_lamp", BLUE_LAMP, fabricItemSettings);
		registerBlockWithItem("brown_lamp", BROWN_LAMP, fabricItemSettings);
		registerBlockWithItem("green_lamp", GREEN_LAMP, fabricItemSettings);
		registerBlockWithItem("red_lamp", RED_LAMP, fabricItemSettings);
		registerBlockWithItem("black_lamp", BLACK_LAMP, fabricItemSettings);
	}

	private static void registerGemstoneGlass(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_glass", TOPAZ_GLASS, fabricItemSettings);
		registerBlockWithItem("amethyst_glass", AMETHYST_GLASS, fabricItemSettings);
		registerBlockWithItem("citrine_glass", CITRINE_GLASS, fabricItemSettings);
		registerBlockWithItem("onyx_glass", ONYX_GLASS, fabricItemSettings);
		registerBlockWithItem("moonstone_glass", MOONSTONE_GLASS, fabricItemSettings);

		registerBlockWithItem("glowing_glass", GLOWING_GLASS, fabricItemSettings);
	}

	private static void registerPlayerOnlyGlass(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("vanilla_player_only_glass", VANILLA_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("tinted_player_only_glass", TINTED_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("glowing_player_only_glass", GLOWING_PLAYER_ONLY_GLASS, fabricItemSettings);

		registerBlockWithItem("topaz_player_only_glass", TOPAZ_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("amethyst_player_only_glass", AMETHYST_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("citrine_player_only_glass", CITRINE_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("onyx_player_only_glass", ONYX_PLAYER_ONLY_GLASS, fabricItemSettings);
		registerBlockWithItem("moonstone_player_only_glass", MOONSTONE_PLAYER_ONLY_GLASS, fabricItemSettings);
	}
	
	private static void registerGemstoneChimes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("amethyst_chime", AMETHYST_CHIME, fabricItemSettings);
		registerBlockWithItem("topaz_chime", TOPAZ_CHIME, fabricItemSettings);
		registerBlockWithItem("citrine_chime", CITRINE_CHIME, fabricItemSettings);
		registerBlockWithItem("moonstone_chime", MOONSTONE_CHIME, fabricItemSettings);
		registerBlockWithItem("onyx_chime", ONYX_CHIME, fabricItemSettings);
	}
	
	private static void registerDecoStones(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("amethyst_decostone", AMETHYST_DECOSTONE, fabricItemSettings);
		registerBlockWithItem("topaz_decostone", TOPAZ_DECOSTONE, fabricItemSettings);
		registerBlockWithItem("citrine_decostone", CITRINE_DECOSTONE, fabricItemSettings);
		registerBlockWithItem("moonstone_decostone", MOONSTONE_DECOSTONE, fabricItemSettings);
		registerBlockWithItem("onyx_decostone", ONYX_DECOSTONE, fabricItemSettings);
	}

	private static void registerStoneBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("smooth_basalt_slab", SMOOTH_BASALT_SLAB, fabricItemSettings);
		registerBlockWithItem("smooth_basalt_wall", SMOOTH_BASALT_WALL, fabricItemSettings);
		registerBlockWithItem("smooth_basalt_stairs", SMOOTH_BASALT_STAIRS, fabricItemSettings);

		registerBlockWithItem("calcite_slab", CALCITE_SLAB, fabricItemSettings);
		registerBlockWithItem("calcite_wall", CALCITE_WALL, fabricItemSettings);
		registerBlockWithItem("calcite_stairs", CALCITE_STAIRS, fabricItemSettings);

		registerBlockWithItem("polished_basalt", POLISHED_BASALT, fabricItemSettings);
		registerBlockWithItem("polished_basalt_pillar", POLISHED_BASALT_PILLAR, fabricItemSettings);
		registerBlockWithItem("polished_basalt_crest", POLISHED_BASALT_CREST, fabricItemSettings);
		registerBlockWithItem("chiseled_polished_basalt", CHISELED_POLISHED_BASALT, fabricItemSettings);
		registerBlockWithItem("notched_polished_basalt", NOTCHED_POLISHED_BASALT, fabricItemSettings);
		registerBlockWithItem("polished_basalt_slab", POLISHED_BASALT_SLAB, fabricItemSettings);
		registerBlockWithItem("polished_basalt_wall", POLISHED_BASALT_WALL, fabricItemSettings);
		registerBlockWithItem("polished_basalt_stairs", POLISHED_BASALT_STAIRS, fabricItemSettings);
		registerBlockWithItem("basalt_bricks", BASALT_BRICKS, fabricItemSettings);
		registerBlockWithItem("basalt_brick_slab", BASALT_BRICK_SLAB, fabricItemSettings);
		registerBlockWithItem("basalt_brick_wall", BASALT_BRICK_WALL, fabricItemSettings);
		registerBlockWithItem("basalt_brick_stairs", BASALT_BRICK_STAIRS, fabricItemSettings);

		registerBlockWithItem("polished_calcite", POLISHED_CALCITE, fabricItemSettings);
		registerBlockWithItem("polished_calcite_pillar", POLISHED_CALCITE_PILLAR, fabricItemSettings);
		registerBlockWithItem("polished_calcite_crest", POLISHED_CALCITE_CREST, fabricItemSettings);
		registerBlockWithItem("chiseled_polished_calcite", CHISELED_POLISHED_CALCITE, fabricItemSettings);
		registerBlockWithItem("notched_polished_calcite", NOTCHED_POLISHED_CALCITE, fabricItemSettings);
		registerBlockWithItem("polished_calcite_slab", POLISHED_CALCITE_SLAB, fabricItemSettings);
		registerBlockWithItem("polished_calcite_wall", POLISHED_CALCITE_WALL, fabricItemSettings);
		registerBlockWithItem("polished_calcite_stairs", POLISHED_CALCITE_STAIRS, fabricItemSettings);
		registerBlockWithItem("calcite_bricks", CALCITE_BRICKS, fabricItemSettings);
		registerBlockWithItem("calcite_brick_slab", CALCITE_BRICK_SLAB, fabricItemSettings);
		registerBlockWithItem("calcite_brick_wall", CALCITE_BRICK_WALL, fabricItemSettings);
		registerBlockWithItem("calcite_brick_stairs", CALCITE_BRICK_STAIRS, fabricItemSettings);
	}

	private static void registerRunes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_chiseled_basalt", TOPAZ_CHISELED_BASALT, fabricItemSettings);
		registerBlockWithItem("amethyst_chiseled_basalt", AMETHYST_CHISELED_BASALT, fabricItemSettings);
		registerBlockWithItem("citrine_chiseled_basalt", CITRINE_CHISELED_BASALT, fabricItemSettings);
		registerBlockWithItem("onyx_chiseled_basalt", ONYX_CHISELED_BASALT, fabricItemSettings);
		registerBlockWithItem("moonstone_chiseled_basalt", MOONSTONE_CHISELED_BASALT, fabricItemSettings);

		registerBlockWithItem("topaz_chiseled_calcite", TOPAZ_CHISELED_CALCITE, fabricItemSettings);
		registerBlockWithItem("amethyst_chiseled_calcite", AMETHYST_CHISELED_CALCITE, fabricItemSettings);
		registerBlockWithItem("citrine_chiseled_calcite", CITRINE_CHISELED_CALCITE, fabricItemSettings);
		registerBlockWithItem("onyx_chiseled_calcite", ONYX_CHISELED_CALCITE, fabricItemSettings);
		registerBlockWithItem("moonstone_chiseled_calcite", MOONSTONE_CHISELED_CALCITE, fabricItemSettings);
	}

	private static void registerGemstoneLamps(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_calcite_lamp", TOPAZ_CALCITE_LAMP, fabricItemSettings);
		registerBlockWithItem("amethyst_calcite_lamp", AMETHYST_CALCITE_LAMP, fabricItemSettings);
		registerBlockWithItem("citrine_calcite_lamp", CITRINE_CALCITE_LAMP, fabricItemSettings);
		registerBlockWithItem("onyx_calcite_lamp", ONYX_CALCITE_LAMP, fabricItemSettings);
		registerBlockWithItem("moonstone_calcite_lamp", MOONSTONE_CALCITE_LAMP, fabricItemSettings);
		registerBlockWithItem("topaz_basalt_lamp", TOPAZ_BASALT_LAMP, fabricItemSettings);
		registerBlockWithItem("amethyst_basalt_lamp", AMETHYST_BASALT_LAMP, fabricItemSettings);
		registerBlockWithItem("citrine_basalt_lamp", CITRINE_BASALT_LAMP, fabricItemSettings);
		registerBlockWithItem("onyx_basalt_lamp", ONYX_BASALT_LAMP, fabricItemSettings);
		registerBlockWithItem("moonstone_basalt_lamp", MOONSTONE_BASALT_LAMP, fabricItemSettings);
	}

	private static void registerColoredWood(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_log", WHITE_LOG, fabricItemSettings);
		registerBlockWithItem("orange_log", ORANGE_LOG, fabricItemSettings);
		registerBlockWithItem("magenta_log", MAGENTA_LOG, fabricItemSettings);
		registerBlockWithItem("light_blue_log", LIGHT_BLUE_LOG, fabricItemSettings);
		registerBlockWithItem("yellow_log", YELLOW_LOG, fabricItemSettings);
		registerBlockWithItem("lime_log", LIME_LOG, fabricItemSettings);
		registerBlockWithItem("pink_log", PINK_LOG, fabricItemSettings);
		registerBlockWithItem("gray_log", GRAY_LOG, fabricItemSettings);
		registerBlockWithItem("light_gray_log", LIGHT_GRAY_LOG, fabricItemSettings);
		registerBlockWithItem("cyan_log", CYAN_LOG, fabricItemSettings);
		registerBlockWithItem("purple_log", PURPLE_LOG, fabricItemSettings);
		registerBlockWithItem("blue_log", BLUE_LOG, fabricItemSettings);
		registerBlockWithItem("brown_log", BROWN_LOG, fabricItemSettings);
		registerBlockWithItem("green_log", GREEN_LOG, fabricItemSettings);
		registerBlockWithItem("red_log", RED_LOG, fabricItemSettings);
		registerBlockWithItem("black_log", BLACK_LOG, fabricItemSettings);
		
		registerBlockWithItem("white_leaves", WHITE_LEAVES, fabricItemSettings);
		registerBlockWithItem("orange_leaves", ORANGE_LEAVES, fabricItemSettings);
		registerBlockWithItem("magenta_leaves", MAGENTA_LEAVES, fabricItemSettings);
		registerBlockWithItem("light_blue_leaves", LIGHT_BLUE_LEAVES, fabricItemSettings);
		registerBlockWithItem("yellow_leaves", YELLOW_LEAVES, fabricItemSettings);
		registerBlockWithItem("lime_leaves", LIME_LEAVES, fabricItemSettings);
		registerBlockWithItem("pink_leaves", PINK_LEAVES, fabricItemSettings);
		registerBlockWithItem("gray_leaves", GRAY_LEAVES, fabricItemSettings);
		registerBlockWithItem("light_gray_leaves", LIGHT_GRAY_LEAVES, fabricItemSettings);
		registerBlockWithItem("cyan_leaves", CYAN_LEAVES, fabricItemSettings);
		registerBlockWithItem("purple_leaves", PURPLE_LEAVES, fabricItemSettings);
		registerBlockWithItem("blue_leaves", BLUE_LEAVES, fabricItemSettings);
		registerBlockWithItem("brown_leaves", BROWN_LEAVES, fabricItemSettings);
		registerBlockWithItem("green_leaves", GREEN_LEAVES, fabricItemSettings);
		registerBlockWithItem("red_leaves", RED_LEAVES, fabricItemSettings);
		registerBlockWithItem("black_leaves", BLACK_LEAVES, fabricItemSettings);
		
		registerBlockWithItem("white_sapling", WHITE_SAPLING, fabricItemSettings);
		registerBlockWithItem("orange_sapling", ORANGE_SAPLING, fabricItemSettings);
		registerBlockWithItem("magenta_sapling", MAGENTA_SAPLING, fabricItemSettings);
		registerBlockWithItem("light_blue_sapling", LIGHT_BLUE_SAPLING, fabricItemSettings);
		registerBlockWithItem("yellow_sapling", YELLOW_SAPLING, fabricItemSettings);
		registerBlockWithItem("lime_sapling", LIME_SAPLING, fabricItemSettings);
		registerBlockWithItem("pink_sapling", PINK_SAPLING, fabricItemSettings);
		registerBlockWithItem("gray_sapling", GRAY_SAPLING, fabricItemSettings);
		registerBlockWithItem("light_gray_sapling", LIGHT_GRAY_SAPLING, fabricItemSettings);
		registerBlockWithItem("cyan_sapling", CYAN_SAPLING, fabricItemSettings);
		registerBlockWithItem("purple_sapling", PURPLE_SAPLING, fabricItemSettings);
		registerBlockWithItem("blue_sapling", BLUE_SAPLING, fabricItemSettings);
		registerBlockWithItem("brown_sapling", BROWN_SAPLING, fabricItemSettings);
		registerBlockWithItem("green_sapling", GREEN_SAPLING, fabricItemSettings);
		registerBlockWithItem("red_sapling", RED_SAPLING, fabricItemSettings);
		registerBlockWithItem("black_sapling", BLACK_SAPLING, fabricItemSettings);
		
		registerBlockWithItem("white_planks", WHITE_PLANKS, fabricItemSettings);
		registerBlockWithItem("orange_planks", ORANGE_PLANKS, fabricItemSettings);
		registerBlockWithItem("magenta_planks", MAGENTA_PLANKS, fabricItemSettings);
		registerBlockWithItem("light_blue_planks", LIGHT_BLUE_PLANKS, fabricItemSettings);
		registerBlockWithItem("yellow_planks", YELLOW_PLANKS, fabricItemSettings);
		registerBlockWithItem("lime_planks", LIME_PLANKS, fabricItemSettings);
		registerBlockWithItem("pink_planks", PINK_PLANKS, fabricItemSettings);
		registerBlockWithItem("gray_planks", GRAY_PLANKS, fabricItemSettings);
		registerBlockWithItem("light_gray_planks", LIGHT_GRAY_PLANKS, fabricItemSettings);
		registerBlockWithItem("cyan_planks", CYAN_PLANKS, fabricItemSettings);
		registerBlockWithItem("purple_planks", PURPLE_PLANKS, fabricItemSettings);
		registerBlockWithItem("blue_planks", BLUE_PLANKS, fabricItemSettings);
		registerBlockWithItem("brown_planks", BROWN_PLANKS, fabricItemSettings);
		registerBlockWithItem("green_planks", GREEN_PLANKS, fabricItemSettings);
		registerBlockWithItem("red_planks", RED_PLANKS, fabricItemSettings);
		registerBlockWithItem("black_planks", BLACK_PLANKS, fabricItemSettings);
		
		registerBlockWithItem("white_plank_stairs", WHITE_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("orange_plank_stairs", ORANGE_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("magenta_plank_stairs", MAGENTA_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_stairs", LIGHT_BLUE_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("yellow_plank_stairs", YELLOW_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("lime_plank_stairs", LIME_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("pink_plank_stairs", PINK_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("gray_plank_stairs", GRAY_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_stairs", LIGHT_GRAY_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("cyan_plank_stairs", CYAN_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("purple_plank_stairs", PURPLE_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("blue_plank_stairs", BLUE_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("brown_plank_stairs", BROWN_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("green_plank_stairs", GREEN_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("red_plank_stairs", RED_PLANK_STAIRS, fabricItemSettings);
		registerBlockWithItem("black_plank_stairs", BLACK_PLANK_STAIRS, fabricItemSettings);
		
		registerBlockWithItem("white_plank_pressure_plate", WHITE_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("orange_plank_pressure_plate", ORANGE_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("magenta_plank_pressure_plate", MAGENTA_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_pressure_plate", LIGHT_BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("yellow_plank_pressure_plate", YELLOW_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("lime_plank_pressure_plate", LIME_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("pink_plank_pressure_plate", PINK_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("gray_plank_pressure_plate", GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_pressure_plate", LIGHT_GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("cyan_plank_pressure_plate", CYAN_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("purple_plank_pressure_plate", PURPLE_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("blue_plank_pressure_plate", BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("brown_plank_pressure_plate", BROWN_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("green_plank_pressure_plate", GREEN_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("red_plank_pressure_plate", RED_PLANK_PRESSURE_PLATE, fabricItemSettings);
		registerBlockWithItem("black_plank_pressure_plate", BLACK_PLANK_PRESSURE_PLATE, fabricItemSettings);
		
		registerBlockWithItem("white_plank_fence", WHITE_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("orange_plank_fence", ORANGE_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("magenta_plank_fence", MAGENTA_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_fence", LIGHT_BLUE_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("yellow_plank_fence", YELLOW_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("lime_plank_fence", LIME_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("pink_plank_fence", PINK_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("gray_plank_fence", GRAY_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_fence", LIGHT_GRAY_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("cyan_plank_fence", CYAN_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("purple_plank_fence", PURPLE_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("blue_plank_fence", BLUE_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("brown_plank_fence", BROWN_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("green_plank_fence", GREEN_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("red_plank_fence", RED_PLANK_FENCE, fabricItemSettings);
		registerBlockWithItem("black_plank_fence", BLACK_PLANK_FENCE, fabricItemSettings);
		
		registerBlockWithItem("white_plank_fence_gate", WHITE_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("orange_plank_fence_gate", ORANGE_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("magenta_plank_fence_gate", MAGENTA_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_fence_gate", LIGHT_BLUE_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("yellow_plank_fence_gate", YELLOW_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("lime_plank_fence_gate", LIME_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("pink_plank_fence_gate", PINK_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("gray_plank_fence_gate", GRAY_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_fence_gate", LIGHT_GRAY_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("cyan_plank_fence_gate", CYAN_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("purple_plank_fence_gate", PURPLE_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("blue_plank_fence_gate", BLUE_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("brown_plank_fence_gate", BROWN_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("green_plank_fence_gate", GREEN_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("red_plank_fence_gate", RED_PLANK_FENCE_GATE, fabricItemSettings);
		registerBlockWithItem("black_plank_fence_gate", BLACK_PLANK_FENCE_GATE, fabricItemSettings);
		
		registerBlockWithItem("white_plank_button", WHITE_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("orange_plank_button", ORANGE_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("magenta_plank_button", MAGENTA_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_button", LIGHT_BLUE_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("yellow_plank_button", YELLOW_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("lime_plank_button", LIME_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("pink_plank_button", PINK_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("gray_plank_button", GRAY_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_button", LIGHT_GRAY_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("cyan_plank_button", CYAN_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("purple_plank_button", PURPLE_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("blue_plank_button", BLUE_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("brown_plank_button", BROWN_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("green_plank_button", GREEN_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("red_plank_button", RED_PLANK_BUTTON, fabricItemSettings);
		registerBlockWithItem("black_plank_button", BLACK_PLANK_BUTTON, fabricItemSettings);
		
		registerBlockWithItem("white_plank_slab", WHITE_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("orange_plank_slab", ORANGE_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("magenta_plank_slab", MAGENTA_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("light_blue_plank_slab", LIGHT_BLUE_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("yellow_plank_slab", YELLOW_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("lime_plank_slab", LIME_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("pink_plank_slab", PINK_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("gray_plank_slab", GRAY_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("light_gray_plank_slab", LIGHT_GRAY_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("cyan_plank_slab", CYAN_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("purple_plank_slab", PURPLE_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("blue_plank_slab", BLUE_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("brown_plank_slab", BROWN_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("green_plank_slab", GREEN_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("red_plank_slab", RED_PLANK_SLAB, fabricItemSettings);
		registerBlockWithItem("black_plank_slab", BLACK_PLANK_SLAB, fabricItemSettings);
	}

	private static void registerGlowBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_glowblock", WHITE_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("orange_glowblock", ORANGE_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("magenta_glowblock", MAGENTA_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("light_blue_glowblock", LIGHT_BLUE_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("yellow_glowblock", YELLOW_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("lime_glowblock", LIME_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("pink_glowblock", PINK_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("gray_glowblock", GRAY_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("light_gray_glowblock", LIGHT_GRAY_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("cyan_glowblock", CYAN_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("purple_glowblock", PURPLE_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("blue_glowblock", BLUE_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("brown_glowblock", BROWN_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("green_glowblock", GREEN_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("red_glowblock", RED_GLOWBLOCK, fabricItemSettings);
		registerBlockWithItem("black_glowblock", BLACK_GLOWBLOCK, fabricItemSettings);
	}

	public static void registerSparklestoneLights(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("basalt_sparklestone_light", BASALT_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("calcite_sparklestone_light", CALCITE_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("stone_sparklestone_light", STONE_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("granite_sparklestone_light", GRANITE_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("diorite_sparklestone_light", DIORITE_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("andesite_sparklestone_light", ANDESITE_SPARKLESTONE_LIGHT, fabricItemSettings);
		registerBlockWithItem("deepslate_sparklestone_light", DEEPSLATE_SPARKLESTONE_LIGHT, fabricItemSettings);
	}
	
	public static void registerShootingStarBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("shooting_star_glistering", GLISTERING_SHOOTING_STAR, fabricItemSettings);
		registerBlockWithItem("shooting_star_fiery", FIERY_SHOOTING_STAR, fabricItemSettings);
		registerBlockWithItem("shooting_star_colorful", COLORFUL_SHOOTING_STAR, fabricItemSettings);
		registerBlockWithItem("shooting_star_pristine", PRISTINE_SHOOTING_STAR, fabricItemSettings);
		registerBlockWithItem("shooting_star_gemstone", GEMSTONE_SHOOTING_STAR, fabricItemSettings);
	}
	
	public static void registerPastelNetworkNodes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("connection_node", CONNECTION_NODE, fabricItemSettings);
		registerBlockWithItem("provider_node", PROVIDER_NODE, fabricItemSettings);
		registerBlockWithItem("storage_node", STORAGE_NODE, fabricItemSettings);
		registerBlockWithItem("pusher_node", PUSHER_NODE, fabricItemSettings);
		registerBlockWithItem("puller_node", PULLER_NODE, fabricItemSettings);
		registerBlockWithItem("interaction_node", INTERACTION_NODE, fabricItemSettings);
	}
	
	public static void registerSporeBlossoms(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_spore_blossom", WHITE_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("orange_spore_blossom", ORANGE_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("magenta_spore_blossom", MAGENTA_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("light_blue_spore_blossom", LIGHT_BLUE_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("yellow_spore_blossom", YELLOW_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("lime_spore_blossom", LIME_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("pink_spore_blossom", PINK_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("gray_spore_blossom", GRAY_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("light_gray_spore_blossom", LIGHT_GRAY_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("cyan_spore_blossom", CYAN_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("purple_spore_blossom", PURPLE_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("blue_spore_blossom", BLUE_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("brown_spore_blossom", BROWN_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("green_spore_blossom", GREEN_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("red_spore_blossom", RED_SPORE_BLOSSOM, fabricItemSettings);
		registerBlockWithItem("black_spore_blossom", BLACK_SPORE_BLOSSOM, fabricItemSettings);
	}

	private static void registerGemBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_block", TOPAZ_BLOCK, fabricItemSettings);
		registerBlockWithItem("budding_topaz", BUDDING_TOPAZ, fabricItemSettings);
		registerBlockWithItem("small_topaz_bud", SMALL_TOPAZ_BUD, fabricItemSettings);
		registerBlockWithItem("medium_topaz_bud", MEDIUM_TOPAZ_BUD, fabricItemSettings);
		registerBlockWithItem("large_topaz_bud", LARGE_TOPAZ_BUD, fabricItemSettings);
		registerBlockWithItem("topaz_cluster", TOPAZ_CLUSTER, fabricItemSettings);

		registerBlockWithItem("citrine_block", CITRINE_BLOCK, fabricItemSettings);
		registerBlockWithItem("budding_citrine", BUDDING_CITRINE, fabricItemSettings);
		registerBlockWithItem("small_citrine_bud", SMALL_CITRINE_BUD, fabricItemSettings);
		registerBlockWithItem("medium_citrine_bud", MEDIUM_CITRINE_BUD, fabricItemSettings);
		registerBlockWithItem("large_citrine_bud", LARGE_CITRINE_BUD, fabricItemSettings);
		registerBlockWithItem("citrine_cluster", CITRINE_CLUSTER, fabricItemSettings);

		registerBlockWithItem("onyx_block", ONYX_BLOCK, fabricItemSettings);
		registerBlockWithItem("budding_onyx", BUDDING_ONYX, fabricItemSettings);
		registerBlockWithItem("small_onyx_bud", SMALL_ONYX_BUD, fabricItemSettings);
		registerBlockWithItem("medium_onyx_bud", MEDIUM_ONYX_BUD, fabricItemSettings);
		registerBlockWithItem("large_onyx_bud", LARGE_ONYX_BUD, fabricItemSettings);
		registerBlockWithItem("onyx_cluster", ONYX_CLUSTER, fabricItemSettings);

		registerBlockWithItem("moonstone_block", MOONSTONE_BLOCK, fabricItemSettings);
		registerBlockWithItem("budding_moonstone", BUDDING_MOONSTONE, fabricItemSettings);
		registerBlockWithItem("small_moonstone_bud", SMALL_MOONSTONE_BUD, fabricItemSettings);
		registerBlockWithItem("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD, fabricItemSettings);
		registerBlockWithItem("large_moonstone_bud", LARGE_MOONSTONE_BUD, fabricItemSettings);
		registerBlockWithItem("moonstone_cluster", MOONSTONE_CLUSTER, fabricItemSettings);
	}

	private static void registerGemOreBlocks(FabricItemSettings fabricItemSettings) {
		// stone ores
		registerBlockWithItem("topaz_ore", TOPAZ_ORE, fabricItemSettings);
		registerBlockWithItem("amethyst_ore", AMETHYST_ORE, fabricItemSettings);
		registerBlockWithItem("citrine_ore", CITRINE_ORE, fabricItemSettings);
		registerBlockWithItem("onyx_ore", ONYX_ORE, fabricItemSettings);
		registerBlockWithItem("moonstone_ore", MOONSTONE_ORE, fabricItemSettings);

		// deepslate ores
		registerBlockWithItem("deepslate_topaz_ore", DEEPSLATE_TOPAZ_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_amethyst_ore", DEEPSLATE_AMETHYST_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_citrine_ore", DEEPSLATE_CITRINE_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_onyx_ore", DEEPSLATE_ONYX_ORE, fabricItemSettings);
		registerBlockWithItem("deepslate_moonstone_ore", DEEPSLATE_MOONSTONE_ORE, fabricItemSettings);
	}

	// Most mob heads vanilla is missing (vanilla only has: skeleton, wither skeleton, zombie, player, creeper, ender dragon)
	private static void registerMobHeads(FabricItemSettings fabricItemSettings) {
		for(SpectrumSkullBlock.Type type : SpectrumSkullBlock.Type.values()) {
			Block head = new SpectrumSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_head", head);
			Block wallHead = new SpectrumWallSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL).dropsLike(head));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_wall_head", wallHead);
			BlockItem headItem = new SpectrumSkullBlockItem(head, wallHead, (fabricItemSettings));
			registerBlockItem(type.name().toLowerCase(Locale.ROOT) + "_head", headItem);

			MOB_HEADS.put(type, head);
			MOB_WALL_HEADS.put(type, wallHead);
		}
	}

	public static Block getMobHead(SpectrumSkullBlock.Type skullType) {
		return MOB_HEADS.get(skullType);
	}

	public static SpectrumSkullBlock.Type getSkullType(Block block) {
		if(block instanceof SpectrumWallSkullBlock) {
			return MOB_WALL_HEADS.inverse().get(block);
		} else {
			return MOB_HEADS.inverse().get(block);
		}
	}

	public static Block getMobWallHead(SpectrumSkullBlock.Type skullType) {
		return MOB_WALL_HEADS.get(skullType);
	}

	public static Collection<Block> getMobHeads() {
		return MOB_HEADS.values();
	}

	public static Collection<Block> getMobWallHeads() {
		return MOB_WALL_HEADS.values();
	}

	public static void registerClient() {
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

		// Glass
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_GLASS, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLOWING_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLOWING_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TINTED_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.VANILLA_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDER_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PARTICLE_SPAWNER, RenderLayer.getCutout());

		// Gem lamps
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CALCITE_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CALCITE_LAMP, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_BASALT_LAMP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_BASALT_LAMP, RenderLayer.getTranslucent());

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

		// DECOSTONES
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_DECOSTONE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_DECOSTONE, RenderLayer.getTranslucent());
		
		// CHIMES
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CHIME, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CHIME, RenderLayer.getTranslucent());
		
		// Others
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_BASALT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_CALCITE, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_TIMER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_WIRELESS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.REDSTONE_CALCULATOR, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.QUITOXIC_REEDS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MERMAIDS_BRUSH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RESONANT_LILY, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.STUCK_LIGHTNING_STONE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CLOVER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FOUR_LEAF_CLOVER, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ETHEREAL_PLATFORM, RenderLayer.getTranslucent());
	}

	@NotNull
	public static Block getColoredLogBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_LOG;
			case BROWN -> BROWN_LOG;
			case CYAN -> CYAN_LOG;
			case GRAY -> GRAY_LOG;
			case GREEN -> GREEN_LOG;
			case LIGHT_BLUE -> LIGHT_BLUE_LOG;
			case LIGHT_GRAY -> LIGHT_GRAY_LOG;
			case BLUE -> BLUE_LOG;
			case LIME -> LIME_LOG;
			case ORANGE -> ORANGE_LOG;
			case PINK -> PINK_LOG;
			case PURPLE -> PURPLE_LOG;
			case WHITE -> WHITE_LOG;
			case YELLOW -> YELLOW_LOG;
			case BLACK -> BLACK_LOG;
			default -> MAGENTA_LOG;
		};
	}

	@NotNull
	public static Block getColoredLeavesBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_LEAVES;
			case BROWN -> BROWN_LEAVES;
			case CYAN -> CYAN_LEAVES;
			case GRAY -> GRAY_LEAVES;
			case GREEN -> GREEN_LEAVES;
			case LIGHT_BLUE -> LIGHT_BLUE_LEAVES;
			case LIGHT_GRAY -> LIGHT_GRAY_LEAVES;
			case BLUE -> BLUE_LEAVES;
			case LIME -> LIME_LEAVES;
			case ORANGE -> ORANGE_LEAVES;
			case PINK -> PINK_LEAVES;
			case PURPLE -> PURPLE_LEAVES;
			case WHITE -> WHITE_LEAVES;
			case YELLOW -> YELLOW_LEAVES;
			case BLACK -> BLACK_LEAVES;
			default -> MAGENTA_LEAVES;
		};
	}

	@NotNull
	public static Block getColoredPlanksBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_PLANKS;
			case BROWN -> BROWN_PLANKS;
			case CYAN -> CYAN_PLANKS;
			case GRAY -> GRAY_PLANKS;
			case GREEN -> GREEN_PLANKS;
			case LIGHT_BLUE -> LIGHT_BLUE_PLANKS;
			case LIGHT_GRAY -> LIGHT_GRAY_PLANKS;
			case BLUE -> BLUE_PLANKS;
			case LIME -> LIME_PLANKS;
			case ORANGE -> ORANGE_PLANKS;
			case PINK -> PINK_PLANKS;
			case PURPLE -> PURPLE_PLANKS;
			case WHITE -> WHITE_PLANKS;
			case YELLOW -> YELLOW_PLANKS;
			case BLACK -> BLACK_PLANKS;
			default -> MAGENTA_PLANKS;
		};
	}

	@NotNull
	public static Block getColoredSlabsBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_PLANK_SLAB;
			case BROWN -> BROWN_PLANK_SLAB;
			case CYAN -> CYAN_PLANK_SLAB;
			case GRAY -> GRAY_PLANK_SLAB;
			case GREEN -> GREEN_PLANK_SLAB;
			case LIGHT_BLUE -> LIGHT_BLUE_PLANK_SLAB;
			case LIGHT_GRAY -> LIGHT_GRAY_PLANK_SLAB;
			case BLUE -> BLUE_PLANK_SLAB;
			case LIME -> LIME_PLANK_SLAB;
			case ORANGE -> ORANGE_PLANK_SLAB;
			case PINK -> PINK_PLANK_SLAB;
			case PURPLE -> PURPLE_PLANK_SLAB;
			case WHITE -> WHITE_PLANK_SLAB;
			case YELLOW -> YELLOW_PLANK_SLAB;
			case BLACK -> BLACK_PLANK_SLAB;
			default -> MAGENTA_PLANK_SLAB;
		};
	}

	@NotNull
	public static Block getColoredFenceBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_PLANK_FENCE;
			case BROWN -> BROWN_PLANK_FENCE;
			case CYAN -> CYAN_PLANK_FENCE;
			case GRAY -> GRAY_PLANK_FENCE;
			case GREEN -> GREEN_PLANK_FENCE;
			case LIGHT_BLUE -> LIGHT_BLUE_PLANK_FENCE;
			case LIGHT_GRAY -> LIGHT_GRAY_PLANK_FENCE;
			case BLUE -> BLUE_PLANK_FENCE;
			case LIME -> LIME_PLANK_FENCE;
			case ORANGE -> ORANGE_PLANK_FENCE;
			case PINK -> PINK_PLANK_FENCE;
			case PURPLE -> PURPLE_PLANK_FENCE;
			case WHITE -> WHITE_PLANK_FENCE;
			case YELLOW -> YELLOW_PLANK_FENCE;
			case BLACK -> BLACK_PLANK_FENCE;
			default -> MAGENTA_PLANK_FENCE;
		};
	}

	@NotNull
	public static Block getColoredFenceGateBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_PLANK_FENCE_GATE;
			case BROWN -> BROWN_PLANK_FENCE_GATE;
			case CYAN -> CYAN_PLANK_FENCE_GATE;
			case GRAY -> GRAY_PLANK_FENCE_GATE;
			case GREEN -> GREEN_PLANK_FENCE_GATE;
			case LIGHT_BLUE -> LIGHT_BLUE_PLANK_FENCE_GATE;
			case LIGHT_GRAY -> LIGHT_GRAY_PLANK_FENCE_GATE;
			case BLUE -> BLUE_PLANK_FENCE_GATE;
			case LIME -> LIME_PLANK_FENCE_GATE;
			case ORANGE -> ORANGE_PLANK_FENCE_GATE;
			case PINK -> PINK_PLANK_FENCE_GATE;
			case PURPLE -> PURPLE_PLANK_FENCE_GATE;
			case WHITE -> WHITE_PLANK_FENCE_GATE;
			case YELLOW -> YELLOW_PLANK_FENCE_GATE;
			case BLACK -> BLACK_PLANK_FENCE_GATE;
			default -> MAGENTA_PLANK_FENCE_GATE;
		};
	}

	@NotNull
	public static Block getColoredStairsBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_PLANK_STAIRS;
			case BROWN -> BROWN_PLANK_STAIRS;
			case CYAN -> CYAN_PLANK_STAIRS;
			case GRAY -> GRAY_PLANK_STAIRS;
			case GREEN -> GREEN_PLANK_STAIRS;
			case LIGHT_BLUE -> LIGHT_BLUE_PLANK_STAIRS;
			case LIGHT_GRAY -> LIGHT_GRAY_PLANK_STAIRS;
			case BLUE -> BLUE_PLANK_STAIRS;
			case LIME -> LIME_PLANK_STAIRS;
			case ORANGE -> ORANGE_PLANK_STAIRS;
			case PINK -> PINK_PLANK_STAIRS;
			case PURPLE -> PURPLE_PLANK_STAIRS;
			case WHITE -> WHITE_PLANK_STAIRS;
			case YELLOW -> YELLOW_PLANK_STAIRS;
			case BLACK -> BLACK_PLANK_STAIRS;
			default -> MAGENTA_PLANK_STAIRS;
		};
	}

	@NotNull
	public static Item getColoredLeavesItem(DyeColor dyeColor) {
		return getColoredLeavesBlock(dyeColor).asItem();
	}

	public static Block getColoredSaplingBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_SAPLING;
			case BROWN -> BROWN_SAPLING;
			case CYAN -> CYAN_SAPLING;
			case GRAY -> GRAY_SAPLING;
			case GREEN -> GREEN_SAPLING;
			case LIGHT_BLUE -> LIGHT_BLUE_SAPLING;
			case LIGHT_GRAY -> LIGHT_GRAY_SAPLING;
			case BLUE -> BLUE_SAPLING;
			case LIME -> LIME_SAPLING;
			case ORANGE -> ORANGE_SAPLING;
			case PINK -> PINK_SAPLING;
			case PURPLE -> PURPLE_SAPLING;
			case WHITE -> WHITE_SAPLING;
			case YELLOW -> YELLOW_SAPLING;
			case BLACK -> BLACK_SAPLING;
			default -> MAGENTA_SAPLING;
		};
	}

}
