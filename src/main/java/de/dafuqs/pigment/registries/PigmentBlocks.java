package de.dafuqs.pigment.registries;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.*;
import de.dafuqs.pigment.blocks.altar.AltarBlock;
import de.dafuqs.pigment.blocks.chests.CompactingChestBlock;
import de.dafuqs.pigment.blocks.chests.PrivateChestBlock;
import de.dafuqs.pigment.blocks.chests.RestockingChestBlock;
import de.dafuqs.pigment.blocks.conditional.*;
import de.dafuqs.pigment.blocks.conditional.GemstoneOreBlock;
import de.dafuqs.pigment.blocks.decay.FadingBlock;
import de.dafuqs.pigment.blocks.decay.FailingBlock;
import de.dafuqs.pigment.blocks.decay.RuinBlock;
import de.dafuqs.pigment.blocks.deeper_down_portal.DeeperDownPortalBlock;
import de.dafuqs.pigment.blocks.detector.*;
import de.dafuqs.pigment.blocks.ender.EnderDropperBlock;
import de.dafuqs.pigment.blocks.ender.EnderHopperBlock;
import de.dafuqs.pigment.blocks.fluid.LiquidCrystalFluidBlock;
import de.dafuqs.pigment.blocks.fluid.MudFluidBlock;
import de.dafuqs.pigment.blocks.gemstone.PigmentBuddingBlock;
import de.dafuqs.pigment.blocks.gemstone.PigmentGemstoneBlock;
import de.dafuqs.pigment.blocks.gravity.GravitableBlock;
import de.dafuqs.pigment.blocks.gravity.GravityBlockItem;
import de.dafuqs.pigment.blocks.lava_sponge.LavaSpongeBlock;
import de.dafuqs.pigment.blocks.lava_sponge.WetLavaSpongeBlock;
import de.dafuqs.pigment.blocks.lava_sponge.WetLavaSpongeItem;
import de.dafuqs.pigment.blocks.melon.AttachedGlisteringStemBlock;
import de.dafuqs.pigment.blocks.melon.GlisteringMelonBlock;
import de.dafuqs.pigment.blocks.melon.GlisteringStemBlock;
import de.dafuqs.pigment.blocks.mob_head.PigmentSkullBlock;
import de.dafuqs.pigment.blocks.mob_head.PigmentSkullBlockItem;
import de.dafuqs.pigment.blocks.mob_head.PigmentWallSkullBlock;
import de.dafuqs.pigment.blocks.redstone.RedstoneGravityBlock;
import de.dafuqs.pigment.blocks.redstone.RedstoneTransparencyBlock;
import de.dafuqs.pigment.blocks.spirit_tree.OminousSaplingBlock;
import de.dafuqs.pigment.blocks.spirit_tree.OminousSaplingBlockItem;
import de.dafuqs.pigment.blocks.spirit_vines.SpiritVinesBodyBlock;
import de.dafuqs.pigment.blocks.spirit_vines.SpiritVinesHeadBlock;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.misc.PigmentMaterial;
import de.dafuqs.pigment.sound.PigmentBlockSoundGroups;
import de.dafuqs.pigment.sound.PigmentSoundEvents;
import de.dafuqs.pigment.worldgen.ColoredSaplingGenerator;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
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

public class PigmentBlocks {

    private static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
    private static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public static FabricItemSettings generalItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL);
    public static FabricItemSettings worldgenItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_WORLDGEN);
    public static FabricItemSettings decorationItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_DECORATION);
    public static FabricItemSettings coloredWoodItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_COLORED_WOOD);
    public static FabricItemSettings mobHeadItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_MOB_HEADS).rarity(Rarity.UNCOMMON);

    public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.TOPAZ_CLUSTER).luminance((state) -> 6));
    public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_TOPAZ_BUD).luminance((state) -> 6));
    public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_TOPAZ_BUD).luminance((state) -> 4));
    public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_TOPAZ_BUD).luminance((state) -> 2));
    public static final Block TOPAZ_BLOCK = new PigmentGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(PigmentBlockSoundGroups.TOPAZ_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
    public static final Block BUDDING_TOPAZ = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.TOPAZ_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);

    public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.CITRINE_CLUSTER).luminance((state) -> 7));
    public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_CITRINE_BUD).luminance((state) -> 7));
    public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_CITRINE_BUD).luminance((state) -> 5));
    public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_CITRINE_BUD).luminance((state) -> 3));
    public static final Block CITRINE_BLOCK = new PigmentGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(PigmentBlockSoundGroups.CITRINE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_CITRINE_BLOCK_HIT, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
    public static final Block BUDDING_CITRINE = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.CITRINE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_HIT, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);

    public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.ONYX_CLUSTER).luminance((state) -> 3));
    public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_ONYX_BUD).luminance((state) -> 5));
    public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_ONYX_BUD).luminance((state) -> 3));
    public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_ONYX_BUD).luminance((state) -> 1));
    public static final Block ONYX_BLOCK = new PigmentGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(PigmentBlockSoundGroups.ONYX_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_ONYX_BLOCK_HIT, PigmentSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
    public static final Block BUDDING_ONYX = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.ONYX_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, PigmentSoundEvents.BLOCK_ONYX_BLOCK_HIT, PigmentSoundEvents.BLOCK_ONYX_BLOCK_CHIME);

    public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.MOONSTONE_CLUSTER).luminance((state) -> 14));
    public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_MOONSTONE_BUD).luminance((state) -> 12));
    public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_MOONSTONE_BUD).luminance((state) -> 7));
    public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_MOONSTONE_BUD).luminance((state) -> 4));
    public static final Block MOONSTONE_BLOCK = new PigmentGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(PigmentBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
    public static final Block BUDDING_MOONSTONE = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);

    public static final Block AMMOLITE_BLOCK = new PigmentGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(PigmentBlockSoundGroups.AMMOLITE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_AMMOLITE_BLOCK_HIT, PigmentSoundEvents.BLOCK_AMMOLITE_BLOCK_CHIME);

    private static final FabricBlockSettings gemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.IRON_ORE).breakByTool(FabricToolTags.PICKAXES, 3);
    private static final UniformIntProvider gemOreExperienceProvider = UniformIntProvider.create(1, 4);
    public static final Block TOPAZ_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, PigmentColor.CYAN, false);
    public static final Block AMETHYST_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, PigmentColor.MAGENTA, false);
    public static final Block CITRINE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, PigmentColor.YELLOW, false);
    public static final Block ONYX_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, PigmentColor.BLACK, false);
    public static final Block MOONSTONE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, PigmentColor.WHITE, false);

    private static final FabricBlockSettings deepslateGemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE);
    public static final Block DEEPSLATE_TOPAZ_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, PigmentColor.CYAN, true);
    public static final Block DEEPSLATE_AMETHYST_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, PigmentColor.MAGENTA, true);
    public static final Block DEEPSLATE_CITRINE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, PigmentColor.YELLOW, true);
    public static final Block DEEPSLATE_ONYX_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, PigmentColor.BLACK, false);
    public static final Block DEEPSLATE_MOONSTONE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, PigmentColor.WHITE, true);

    private static final FabricBlockSettings gemstoneStorageBlockSettings = FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F);
    public static final Block TOPAZ_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
    public static final Block AMETHYST_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
    public static final Block CITRINE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
    public static final Block ONYX_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
    public static final Block MOONSTONE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);

    public static final Block TUFF_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
    public static final Block TUFF_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.TUFF));
    public static final Block TUFF_STAIRS = new PigmentStairsBlock(Blocks.TUFF.getDefaultState(), FabricBlockSettings.copyOf(Blocks.TUFF));

    public static final Block POLISHED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).strength(2.0F, 5.0F));
    public static final Block POLISHED_BASALT_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block CHISELED_POLISHED_BASALT = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_BASALT_SLAB = new SlabBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_BASALT_WALL = new WallBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_BASALT_STAIRS = new PigmentStairsBlock(POLISHED_BASALT.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_BASALT));

    public static final Block BASALT_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).strength(2.5F, 6.0F));
    public static final Block BASALT_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
    public static final Block BASALT_BRICK_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
    public static final Block BASALT_BRICK_STAIRS = new PigmentStairsBlock(BASALT_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(BASALT_BRICKS));

    public static final Block TOPAZ_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(6));
    public static final Block AMETHYST_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(5));
    public static final Block CITRINE_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(7));
    public static final Block ONYX_CHISELED_BASALT = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(3));
    public static final Block MOONSTONE_CHISELED_BASALT = new PillarBlock(FabricBlockSettings.copyOf(BASALT_BRICKS).luminance(12));

    public static final Block CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_STAIRS = new PigmentStairsBlock(Blocks.CALCITE.getDefaultState(), FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_CALCITE_PILLAR = new PillarBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block CHISELED_POLISHED_CALCITE = new Block(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_CALCITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_CALCITE_WALL = new WallBlock(FabricBlockSettings.copyOf(POLISHED_BASALT));
    public static final Block POLISHED_CALCITE_STAIRS = new PigmentStairsBlock(POLISHED_CALCITE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_BASALT));

    public static final Block CALCITE_BRICKS = new Block(FabricBlockSettings.copyOf(BASALT_BRICKS));
    public static final Block CALCITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
    public static final Block CALCITE_BRICK_WALL = new WallBlock(FabricBlockSettings.copyOf(BASALT_BRICKS));
    public static final Block CALCITE_BRICK_STAIRS = new PigmentStairsBlock(CALCITE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(BASALT_BRICKS));

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
    public static final Block GLOWING_GLASS = new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS).luminance(value -> 12));

    // ALTAR
    private static final FabricBlockSettings altarSettings = FabricBlockSettings.of(Material.STONE).requiresTool().strength(5.0F, 20.0F);
    public static final Block ALTAR = new AltarBlock(altarSettings);
    public static final Block ALTAR2 = new AltarBlock(altarSettings);
    public static final Block ALTAR3 = new AltarBlock(altarSettings);

    // PLAYER GLASS
    public static final Block VANILLA_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);
    public static final Block TINTED_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.TINTED_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), true);
    public static final Block GLOWING_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.GLOWING_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never).luminance((state) -> 15), false);

    public static final Block TOPAZ_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);
    public static final Block AMETHYST_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);
    public static final Block CITRINE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);
    public static final Block ONYX_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);
    public static final Block MOONSTONE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never), false);

    // MELON
    public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.of(Material.GOURD, MapColor.LIME));
    public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> PigmentItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.MELON_STEM));
    public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> PigmentItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.MELON_STEM));

    // SAPLING
    public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).ticksRandomly());

    // DECAY
    public static final Block FADING = new FadingBlock(FabricBlockSettings.of(PigmentMaterial.DECAY, MapColor.BLACK).ticksRandomly().requiresTool().strength(0.5F, 0.5F), BlockTags.LEAVES, null,1,  1F);
    public static final Block FAILING = new FailingBlock(FabricBlockSettings.copyOf(FADING).strength(20.0F, 50.0F), null, PigmentBlockTags.FAILING_SAFE, 2,  2.5F);
    public static final Block RUIN = new RuinBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F), null, PigmentBlockTags.RUIN_SAFE, 3, 5F);

    // FLUIDS
    public static final Block LIQUID_CRYSTAL = new LiquidCrystalFluidBlock(PigmentFluids.STILL_LIQUID_CRYSTAL, FabricBlockSettings.copyOf(Blocks.WATER).luminance((state) -> 8));
    public static final Block MUD = new MudFluidBlock(PigmentFluids.STILL_MUD, FabricBlockSettings.copyOf(Blocks.WATER).suffocates(PigmentBlocks::always));

    // COLORED TREES
    private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
    private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 2);
    private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG).luminance((state) -> 5);

    public static final Block BLACK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BLACK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BLACK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLACK), coloredSaplingBlockSettings);
    public static final Block BLACK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BLACK_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BLACK_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BLACK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BLACK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BLACK_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BLACK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLUE), coloredSaplingBlockSettings);
    public static final Block BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BLUE_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BLUE_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BLUE_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block BROWN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BROWN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BROWN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BROWN), coloredSaplingBlockSettings);
    public static final Block BROWN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BROWN_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BROWN_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BROWN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BROWN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BROWN_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BROWN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block CYAN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block CYAN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block CYAN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.CYAN), coloredSaplingBlockSettings);
    public static final Block CYAN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block CYAN_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block CYAN_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block CYAN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block CYAN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block CYAN_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block CYAN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GRAY), coloredSaplingBlockSettings);
    public static final Block GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block GRAY_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block GRAY_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block GRAY_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block GREEN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block GREEN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block GREEN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GREEN), coloredSaplingBlockSettings);
    public static final Block GREEN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block GREEN_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block GREEN_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block GREEN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block GREEN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block GREEN_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block GREEN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIGHT_BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIGHT_BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIGHT_BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_BLUE), coloredSaplingBlockSettings);
    public static final Block LIGHT_BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIGHT_BLUE_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIGHT_BLUE_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIGHT_BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIGHT_BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIGHT_BLUE_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIGHT_BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIGHT_GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIGHT_GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIGHT_GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_GRAY), coloredSaplingBlockSettings);
    public static final Block LIGHT_GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIGHT_GRAY_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIGHT_GRAY_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIGHT_GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIGHT_GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIGHT_GRAY_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIGHT_GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIME_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIME_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIME_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIME), coloredSaplingBlockSettings);
    public static final Block LIME_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIME_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIME_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIME_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIME_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIME_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIME_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block MAGENTA_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block MAGENTA_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block MAGENTA_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.MAGENTA), coloredSaplingBlockSettings);
    public static final Block MAGENTA_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block MAGENTA_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block MAGENTA_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block MAGENTA_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block MAGENTA_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block MAGENTA_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block MAGENTA_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block ORANGE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block ORANGE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block ORANGE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.ORANGE), coloredSaplingBlockSettings);
    public static final Block ORANGE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block ORANGE_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block ORANGE_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block ORANGE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block ORANGE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block ORANGE_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block ORANGE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block PINK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block PINK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block PINK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PINK), coloredSaplingBlockSettings);
    public static final Block PINK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block PINK_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block PINK_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block PINK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block PINK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block PINK_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block PINK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block PURPLE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block PURPLE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block PURPLE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PURPLE), coloredSaplingBlockSettings);
    public static final Block PURPLE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block PURPLE_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block PURPLE_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block PURPLE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block PURPLE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block PURPLE_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block PURPLE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block RED_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block RED_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block RED_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.RED), coloredSaplingBlockSettings);
    public static final Block RED_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block RED_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block RED_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block RED_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block RED_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block RED_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block RED_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block WHITE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block WHITE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block WHITE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.WHITE), coloredSaplingBlockSettings);
    public static final Block WHITE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block WHITE_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block WHITE_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block WHITE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block WHITE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block WHITE_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block WHITE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block YELLOW_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block YELLOW_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block YELLOW_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.YELLOW), coloredSaplingBlockSettings);
    public static final Block YELLOW_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block YELLOW_PLANK_STAIRS = new PigmentStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block YELLOW_PLANK_PRESSURE_PLATE = new PigmentPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block YELLOW_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block YELLOW_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block YELLOW_PLANK_BUTTON = new PigmentWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block YELLOW_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    // FLAT COLORED BLOCKS
    private static final FabricBlockSettings flatColoredBlockBlockSettings = FabricBlockSettings.of(Material.STONE).hardness(2.5F).requiresTool().luminance(1).postProcess(PigmentBlocks::always).emissiveLighting(PigmentBlocks::always);
    public static final Block BLACK_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block BLUE_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block BROWN_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block CYAN_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block GRAY_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block GREEN_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block LIGHT_BLUE_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block LIGHT_GRAY_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block LIME_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block MAGENTA_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block ORANGE_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block PINK_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block PURPLE_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block RED_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block WHITE_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);
    public static final Block YELLOW_FLAT_COLORED_BLOCK = new Block(flatColoredBlockBlockSettings);

    // COLORED LAMPS // TODO: Culling when side is obstructed
    private static final FabricBlockSettings coloredLampBlockBlockSettings = FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP);
    public static final Block BLACK_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block BLUE_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block BROWN_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block CYAN_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block GRAY_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block GREEN_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block LIGHT_BLUE_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block LIGHT_GRAY_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block LIME_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block MAGENTA_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block ORANGE_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block PINK_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block PURPLE_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block RED_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block WHITE_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);
    public static final Block YELLOW_LAMP = new RedstoneLampBlock(coloredLampBlockBlockSettings);

    // ORES
    public static final Block SPARKLESTONE_ORE = new SparklestoneOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).breakByTool(FabricToolTags.PICKAXES, 2), UniformIntProvider.create(2, 4)); // drops sparklestone gems
    public static final Block DEEPSLATE_SPARKLESTONE_ORE = new SparklestoneOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE).breakByTool(FabricToolTags.PICKAXES, 2), UniformIntProvider.create(2, 4)); // drops sparklestone gems
    public static final Block AZURITE_ORE = new AzuriteOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).breakByTool(FabricToolTags.PICKAXES, 3), UniformIntProvider.create(4, 7));
    public static final Block DEEPSLATE_AZURITE_ORE = new AzuriteOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).breakByTool(FabricToolTags.PICKAXES, 3), UniformIntProvider.create(4, 7));
    public static final Block PALETUR_ORE = new PaleturOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.DARK_RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE), UniformIntProvider.create(2, 4));
    public static final Block SCARLET_ORE = new ScarletOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(3.0F, 9.0F), UniformIntProvider.create(3, 5));

    public static final Block SPARKLESTONE_BLOCK = new SparklestoneBlock(FabricBlockSettings.of(Material.GLASS, MapColor.YELLOW).strength(2.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 15));
    public static final Block AZURITE_BLOCK = new PigmentFacingBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));
    public static final Block PALETUR_FRAGMENT_BLOCK = new GravitableBlock(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.02F);
    public static final Block SCARLET_FRAGMENT_BLOCK = new GravitableBlock(FabricBlockSettings.of(Material.METAL, MapColor.DARK_RED).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), -0.2F);

    // FUNCTIONAL BLOCKS
    public static final Block PRIVATE_CHEST = new PrivateChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 3600000.0F).sounds(BlockSoundGroup.STONE));
    public static final Block COMPACTING_CHEST = new CompactingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
    public static final Block RESTOCKING_CHEST = new RestockingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
    public static final Block PARTICLE_EMITTER = new ParticleEmitterBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL));
    public static final Block BEDROCK_ANVIL = new BedrockAnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).requiresTool().strength(8.0F, 8.0F).sounds(BlockSoundGroup.METAL));

    // SOLID LIQUID CRYSTAL
    public static final Block FROSTBITE_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
    public static final Block BLAZING_CRYSTAL = new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE));
    public static final Block RESONANT_LILY = new Block(FabricBlockSettings.copyOf(Blocks.POPPY));

    public static final Block QUITOXIC_REEDS = new QuitoxicReedsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).ticksRandomly());
    public static final Block MERMAIDS_BRUSH = new MermaidsBrushBlock(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).ticksRandomly().luminance(value -> 2));

    public static final Block ENDER_TREASURE = new EnderTreasureBlock(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK));
    public static final Block CRACKED_END_PORTAL_FRAME = new CrackedEndPortalFrameBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL_FRAME));

    public static final Block LAVA_SPONGE = new LavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.SPONGE));
    public static final Block WET_LAVA_SPONGE = new WetLavaSpongeBlock(FabricBlockSettings.copyOf(Blocks.WET_SPONGE).luminance(9).emissiveLighting(PigmentBlocks::always).postProcess(PigmentBlocks::always));

    public static final Block LIGHT_LEVEL_DETECTOR = new BlockLightDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
    public static final Block WEATHER_DETECTOR =  new WeatherDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
    public static final Block ITEM_DETECTOR = new ItemDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
    public static final Block PLAYER_DETECTOR = new PlayerDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
    public static final Block ENTITY_DETECTOR = new EntityDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));

    public static final Block ENDER_DROPPER = new EnderDropperBlock(FabricBlockSettings.copyOf(Blocks.DROPPER).requiresTool().strength(15F, 60.0F));
    public static final Block ENDER_HOPPER = new EnderHopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER).requiresTool().strength(15F, 60.0F));

    private static final BiMap<PigmentSkullBlock.Type, Block> MOB_HEADS = EnumHashBiMap.create(PigmentSkullBlock.Type.class);
    private static final BiMap<PigmentSkullBlock.Type, Block> MOB_WALL_HEADS = EnumHashBiMap.create(PigmentSkullBlock.Type.class);

    public static final Block SPIRIT_SALLOW_ROOTS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block SPIRIT_SALLOW_LOG = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block SPIRIT_SALLOW_BARK = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block SPIRIT_SALLOW_CORE = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block SPIRIT_SALLOW_LEAVES = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block SPIRIT_SALLOW_HEART = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));

    private static final FabricBlockSettings spiritVinesBlockSettings = FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.CAVE_VINES);
    public static final Block CYAN_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), PigmentColor.CYAN);
    public static final Block CYAN_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.CYAN), PigmentColor.CYAN);
    public static final Block MAGENTA_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), PigmentColor.MAGENTA);
    public static final Block MAGENTA_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.MAGENTA), PigmentColor.MAGENTA);
    public static final Block YELLOW_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), PigmentColor.YELLOW);
    public static final Block YELLOW_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.YELLOW), PigmentColor.YELLOW);
    public static final Block BLACK_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), PigmentColor.BLACK);
    public static final Block BLACK_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_BLACK), PigmentColor.BLACK);
    public static final Block WHITE_SPIRIT_SALLOW_VINES_BODY = new SpiritVinesBodyBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), PigmentColor.WHITE);
    public static final Block WHITE_SPIRIT_SALLOW_VINES_HEAD = new SpiritVinesHeadBlock(FabricBlockSettings.copyOf(spiritVinesBlockSettings).mapColor(MapColor.TERRACOTTA_WHITE), PigmentColor.WHITE);

    public static final Block SACRED_SOIL = new Block(FabricBlockSettings.copyOf(Blocks.GRASS));

    public static final Block DEEPER_DOWN_PORTAL = new DeeperDownPortalBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL));

    public static final Block REDSTONE_SAND = new RedstoneGravityBlock(FabricBlockSettings.copyOf(Blocks.SAND));
    public static final Block ENDER_GLASS = new RedstoneTransparencyBlock(FabricBlockSettings.copyOf(Blocks.GLASS).nonOpaque()
            .allowsSpawning((state, world, pos, entityType) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
            .solidBlock(PigmentBlocks::never).suffocates((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID)
            .blockVision((state, world, pos) -> state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) == RedstoneTransparencyBlock.TransparencyState.SOLID));


    private static void registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(PigmentCommon.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, BlockItem blockItem) {
        Registry.register(Registry.ITEM, new Identifier(PigmentCommon.MOD_ID, name), blockItem);
    }

    private static void registerBlockWithItem(String name, Block block, FabricItemSettings itemSettings) {
        Registry.register(Registry.BLOCK, new Identifier(PigmentCommon.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(PigmentCommon.MOD_ID, name), new BlockItem(block, itemSettings));
    }

    private static void registerBlockWithItem(String name, Block block, BlockItem blockItem) {
        Registry.register(Registry.BLOCK, new Identifier(PigmentCommon.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(PigmentCommon.MOD_ID, name), blockItem);
    }

    public static void register() {
        registerBlockWithItem("altar", ALTAR, generalItemSettings);
        registerBlockWithItem("altar2", ALTAR2, generalItemSettings);
        registerBlockWithItem("altar3", ALTAR3, generalItemSettings);

        registerStoneBlocks(decorationItemSettings);
        registerGemBlocks(worldgenItemSettings);
        registerGemOreBlocks(worldgenItemSettings);
        registerOreBlocks();

        registerStoneLamps(decorationItemSettings);
        registerRunes(decorationItemSettings);
        registerGemGlass(decorationItemSettings);
        registerPlayerOnlyGlass(generalItemSettings);

        registerColoredLamps(decorationItemSettings);
        registerFlatColoredBlocks(decorationItemSettings);

        registerColoredWood(coloredWoodItemSettings);

        registerRedstone(generalItemSettings);
        registerMagicalBlocks(generalItemSettings);

        registerMobHeads(mobHeadItemSettings);
        registerSpiritTree(generalItemSettings);

        // Decay
        registerBlock("fading", FADING);
        registerBlock("failing", FAILING);
        registerBlock("ruin", RUIN);

        // Fluids + Products
        registerBlock("mud", MUD);
        registerBlock("liquid_crystal", LIQUID_CRYSTAL);
        registerBlockWithItem("frostbite_crystal", FROSTBITE_CRYSTAL, generalItemSettings);
        registerBlockWithItem("blazing_crystal", BLAZING_CRYSTAL, generalItemSettings);
        registerBlockWithItem("resonant_lily", RESONANT_LILY, generalItemSettings);

        // Worldgen
        registerBlockWithItem("quitoxic_reeds", QUITOXIC_REEDS, worldgenItemSettings);
        registerBlockWithItem("mermaids_brush", MERMAIDS_BRUSH, worldgenItemSettings);
        registerBlockWithItem("ender_treasure", ENDER_TREASURE, worldgenItemSettings);

        // Plants
        registerBlockWithItem("glistering_melon", GLISTERING_MELON, generalItemSettings);
        registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
        registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);

        // Misc
        registerBlock("deeper_down_portal", DEEPER_DOWN_PORTAL);
        registerBlockWithItem("bedrock_anvil", BEDROCK_ANVIL, generalItemSettings);
        registerBlockWithItem("cracked_end_portal_frame", CRACKED_END_PORTAL_FRAME, generalItemSettings);
    }

    private static void registerRedstone(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("light_level_detector", LIGHT_LEVEL_DETECTOR, fabricItemSettings);
        registerBlockWithItem("weather_detector", WEATHER_DETECTOR, fabricItemSettings);
        registerBlockWithItem("item_detector", ITEM_DETECTOR, fabricItemSettings);
        registerBlockWithItem("player_detector", PLAYER_DETECTOR, fabricItemSettings);
        registerBlockWithItem("entity_detector", ENTITY_DETECTOR, fabricItemSettings);

        registerBlockWithItem("redstone_sand", REDSTONE_SAND, fabricItemSettings);
        registerBlockWithItem("ender_glass", ENDER_GLASS, fabricItemSettings);
    }

    private static void registerMagicalBlocks(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("private_chest", PRIVATE_CHEST, fabricItemSettings);
        registerBlockWithItem("compacting_chest", COMPACTING_CHEST, generalItemSettings);
        registerBlockWithItem("restocking_chest", RESTOCKING_CHEST, generalItemSettings);
        registerBlockWithItem("ender_hopper", ENDER_HOPPER, fabricItemSettings);
        registerBlockWithItem("ender_dropper", ENDER_DROPPER, fabricItemSettings);
        registerBlockWithItem("particle_emitter", PARTICLE_EMITTER, fabricItemSettings);

        registerBlockWithItem("lava_sponge", LAVA_SPONGE, fabricItemSettings);
        registerBlockWithItem("wet_lava_sponge", WET_LAVA_SPONGE, new WetLavaSpongeItem(WET_LAVA_SPONGE, new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).maxCount(1).recipeRemainder(LAVA_SPONGE.asItem())));
    }


    private static void registerSpiritTree(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("ominous_sapling", OMINOUS_SAPLING, new OminousSaplingBlockItem(OMINOUS_SAPLING, fabricItemSettings));

        registerBlockWithItem("spirit_sallow_roots", SPIRIT_SALLOW_ROOTS, fabricItemSettings);
        registerBlockWithItem("spirit_sallow_log", SPIRIT_SALLOW_LOG, fabricItemSettings);
        registerBlockWithItem("spirit_sallow_bark", SPIRIT_SALLOW_BARK, fabricItemSettings);
        registerBlockWithItem("spirit_sallow_core", SPIRIT_SALLOW_CORE, fabricItemSettings);
        registerBlockWithItem("spirit_sallow_leaves", SPIRIT_SALLOW_LEAVES, fabricItemSettings);
        registerBlockWithItem("spirit_sallow_heart", SPIRIT_SALLOW_HEART, fabricItemSettings);

        registerBlockWithItem("cyan_spirit_sallow_vines_head", CYAN_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings);
        registerBlockWithItem("magenta_spirit_sallow_vines_head", MAGENTA_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings);
        registerBlockWithItem("yellow_spirit_sallow_vines_head", YELLOW_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings);
        registerBlockWithItem("black_spirit_sallow_vines_head", BLACK_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings);
        registerBlockWithItem("white_spirit_sallow_vines_head", WHITE_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings);

        registerBlockWithItem("cyan_spirit_sallow_vines_body", CYAN_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings);
        registerBlockWithItem("magenta_spirit_sallow_vines_body", MAGENTA_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings);
        registerBlockWithItem("yellow_spirit_sallow_vines_body", YELLOW_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings);
        registerBlockWithItem("black_spirit_sallow_vines_body", BLACK_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings);
        registerBlockWithItem("white_spirit_sallow_vines_body", WHITE_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings);

        registerBlockWithItem("sacred_soil", SACRED_SOIL, fabricItemSettings);
    }

    private static void registerOreBlocks() {
        registerBlockWithItem("sparklestone_ore", SPARKLESTONE_ORE, worldgenItemSettings);
        registerBlockWithItem("deepslate_sparklestone_ore", DEEPSLATE_SPARKLESTONE_ORE, worldgenItemSettings);
        registerBlockWithItem("sparklestone_block", SPARKLESTONE_BLOCK, decorationItemSettings);

        registerBlockWithItem("azurite_ore", AZURITE_ORE, worldgenItemSettings);
        registerBlockWithItem("deepslate_azurite_ore", DEEPSLATE_AZURITE_ORE, worldgenItemSettings);
        registerBlockWithItem("azurite_block", AZURITE_BLOCK, decorationItemSettings);

        registerBlockWithItem("scarlet_ore", SCARLET_ORE, new GravityBlockItem(SCARLET_ORE, worldgenItemSettings, 1.01F));
        registerBlockWithItem("scarlet_fragment_block", SCARLET_FRAGMENT_BLOCK, new GravityBlockItem(SCARLET_FRAGMENT_BLOCK, generalItemSettings, 1.02F));
        registerBlockWithItem("paletur_ore", PALETUR_ORE, new GravityBlockItem(PALETUR_ORE, worldgenItemSettings, 0.99F));
        registerBlockWithItem("paletur_fragment_block", PALETUR_FRAGMENT_BLOCK, new GravityBlockItem(PALETUR_FRAGMENT_BLOCK, generalItemSettings, 0.98F));
    }

    private static void registerColoredLamps(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("black_lamp", BLACK_LAMP, fabricItemSettings);
        registerBlockWithItem("blue_lamp", BLUE_LAMP, fabricItemSettings);
        registerBlockWithItem("brown_lamp", BROWN_LAMP, fabricItemSettings);
        registerBlockWithItem("cyan_lamp", CYAN_LAMP, fabricItemSettings);
        registerBlockWithItem("gray_lamp", GRAY_LAMP, fabricItemSettings);
        registerBlockWithItem("green_lamp", GREEN_LAMP, fabricItemSettings);
        registerBlockWithItem("light_blue_lamp", LIGHT_BLUE_LAMP, fabricItemSettings);
        registerBlockWithItem("light_gray_lamp", LIGHT_GRAY_LAMP, fabricItemSettings);
        registerBlockWithItem("lime_lamp", LIME_LAMP, fabricItemSettings);
        registerBlockWithItem("magenta_lamp", MAGENTA_LAMP, fabricItemSettings);
        registerBlockWithItem("orange_lamp", ORANGE_LAMP, fabricItemSettings);
        registerBlockWithItem("pink_lamp", PINK_LAMP, fabricItemSettings);
        registerBlockWithItem("purple_lamp", PURPLE_LAMP, fabricItemSettings);
        registerBlockWithItem("red_lamp", RED_LAMP, fabricItemSettings);
        registerBlockWithItem("white_lamp", WHITE_LAMP, fabricItemSettings);
        registerBlockWithItem("yellow_lamp", YELLOW_LAMP, fabricItemSettings);
    }

    private static void registerGemGlass(FabricItemSettings fabricItemSettings) {
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

    private static void registerStoneBlocks(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("tuff_slab", TUFF_SLAB, fabricItemSettings);
        registerBlockWithItem("tuff_wall", TUFF_WALL, fabricItemSettings);
        registerBlockWithItem("tuff_stairs", TUFF_STAIRS, fabricItemSettings);

        registerBlockWithItem("calcite_slab", CALCITE_SLAB, fabricItemSettings);
        registerBlockWithItem("calcite_wall", CALCITE_WALL, fabricItemSettings);
        registerBlockWithItem("calcite_stairs", CALCITE_STAIRS, fabricItemSettings);

        registerBlockWithItem("polished_basalt", POLISHED_BASALT, fabricItemSettings);
        registerBlockWithItem("polished_basalt_pillar", POLISHED_BASALT_PILLAR, fabricItemSettings);
        registerBlockWithItem("chiseled_polished_basalt", CHISELED_POLISHED_BASALT, fabricItemSettings);
        registerBlockWithItem("polished_basalt_slab", POLISHED_BASALT_SLAB, fabricItemSettings);
        registerBlockWithItem("polished_basalt_wall", POLISHED_BASALT_WALL, fabricItemSettings);
        registerBlockWithItem("polished_basalt_stairs", POLISHED_BASALT_STAIRS, fabricItemSettings);
        registerBlockWithItem("basalt_bricks", BASALT_BRICKS, fabricItemSettings);
        registerBlockWithItem("basalt_brick_slab", BASALT_BRICK_SLAB, fabricItemSettings);
        registerBlockWithItem("basalt_brick_wall", BASALT_BRICK_WALL, fabricItemSettings);
        registerBlockWithItem("basalt_brick_stairs", BASALT_BRICK_STAIRS, fabricItemSettings);

        registerBlockWithItem("polished_calcite", POLISHED_CALCITE, fabricItemSettings);
        registerBlockWithItem("polished_calcite_pillar", POLISHED_CALCITE_PILLAR, fabricItemSettings);
        registerBlockWithItem("chiseled_polished_calcite", CHISELED_POLISHED_CALCITE, fabricItemSettings);
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

    private static void registerStoneLamps(FabricItemSettings fabricItemSettings) {
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
        registerBlockWithItem("black_log", BLACK_LOG, fabricItemSettings);
        registerBlockWithItem("blue_log", BLUE_LOG, fabricItemSettings);
        registerBlockWithItem("brown_log", BROWN_LOG, fabricItemSettings);
        registerBlockWithItem("cyan_log", CYAN_LOG, fabricItemSettings);
        registerBlockWithItem("gray_log", GRAY_LOG, fabricItemSettings);
        registerBlockWithItem("green_log", GREEN_LOG, fabricItemSettings);
        registerBlockWithItem("light_blue_log", LIGHT_BLUE_LOG, fabricItemSettings);
        registerBlockWithItem("light_gray_log", LIGHT_GRAY_LOG, fabricItemSettings);
        registerBlockWithItem("lime_log", LIME_LOG, fabricItemSettings);
        registerBlockWithItem("magenta_log", MAGENTA_LOG, fabricItemSettings);
        registerBlockWithItem("orange_log", ORANGE_LOG, fabricItemSettings);
        registerBlockWithItem("pink_log", PINK_LOG, fabricItemSettings);
        registerBlockWithItem("purple_log", PURPLE_LOG, fabricItemSettings);
        registerBlockWithItem("red_log", RED_LOG, fabricItemSettings);
        registerBlockWithItem("white_log", WHITE_LOG, fabricItemSettings);
        registerBlockWithItem("yellow_log", YELLOW_LOG, fabricItemSettings);
        registerBlockWithItem("black_leaves", BLACK_LEAVES, fabricItemSettings);
        registerBlockWithItem("blue_leaves", BLUE_LEAVES, fabricItemSettings);
        registerBlockWithItem("brown_leaves", BROWN_LEAVES, fabricItemSettings);
        registerBlockWithItem("cyan_leaves", CYAN_LEAVES, fabricItemSettings);
        registerBlockWithItem("gray_leaves", GRAY_LEAVES, fabricItemSettings);
        registerBlockWithItem("green_leaves", GREEN_LEAVES, fabricItemSettings);
        registerBlockWithItem("light_blue_leaves", LIGHT_BLUE_LEAVES, fabricItemSettings);
        registerBlockWithItem("light_gray_leaves", LIGHT_GRAY_LEAVES, fabricItemSettings);
        registerBlockWithItem("lime_leaves", LIME_LEAVES, fabricItemSettings);
        registerBlockWithItem("magenta_leaves", MAGENTA_LEAVES, fabricItemSettings);
        registerBlockWithItem("orange_leaves", ORANGE_LEAVES, fabricItemSettings);
        registerBlockWithItem("pink_leaves", PINK_LEAVES, fabricItemSettings);
        registerBlockWithItem("purple_leaves", PURPLE_LEAVES, fabricItemSettings);
        registerBlockWithItem("red_leaves", RED_LEAVES, fabricItemSettings);
        registerBlockWithItem("white_leaves", WHITE_LEAVES, fabricItemSettings);
        registerBlockWithItem("yellow_leaves", YELLOW_LEAVES, fabricItemSettings);
        registerBlockWithItem("black_sapling", BLACK_SAPLING, fabricItemSettings);
        registerBlockWithItem("blue_sapling", BLUE_SAPLING, fabricItemSettings);
        registerBlockWithItem("brown_sapling", BROWN_SAPLING, fabricItemSettings);
        registerBlockWithItem("cyan_sapling", CYAN_SAPLING, fabricItemSettings);
        registerBlockWithItem("gray_sapling", GRAY_SAPLING, fabricItemSettings);
        registerBlockWithItem("green_sapling", GREEN_SAPLING, fabricItemSettings);
        registerBlockWithItem("light_blue_sapling", LIGHT_BLUE_SAPLING, fabricItemSettings);
        registerBlockWithItem("light_gray_sapling", LIGHT_GRAY_SAPLING, fabricItemSettings);
        registerBlockWithItem("lime_sapling", LIME_SAPLING, fabricItemSettings);
        registerBlockWithItem("magenta_sapling", MAGENTA_SAPLING, fabricItemSettings);
        registerBlockWithItem("orange_sapling", ORANGE_SAPLING, fabricItemSettings);
        registerBlockWithItem("pink_sapling", PINK_SAPLING, fabricItemSettings);
        registerBlockWithItem("purple_sapling", PURPLE_SAPLING, fabricItemSettings);
        registerBlockWithItem("red_sapling", RED_SAPLING, fabricItemSettings);
        registerBlockWithItem("white_sapling", WHITE_SAPLING, fabricItemSettings);
        registerBlockWithItem("yellow_sapling", YELLOW_SAPLING, fabricItemSettings);
        registerBlockWithItem("black_planks", BLACK_PLANKS, fabricItemSettings);
        registerBlockWithItem("blue_planks", BLUE_PLANKS, fabricItemSettings);
        registerBlockWithItem("brown_planks", BROWN_PLANKS, fabricItemSettings);
        registerBlockWithItem("cyan_planks", CYAN_PLANKS, fabricItemSettings);
        registerBlockWithItem("gray_planks", GRAY_PLANKS, fabricItemSettings);
        registerBlockWithItem("green_planks", GREEN_PLANKS, fabricItemSettings);
        registerBlockWithItem("light_blue_planks", LIGHT_BLUE_PLANKS, fabricItemSettings);
        registerBlockWithItem("light_gray_planks", LIGHT_GRAY_PLANKS, fabricItemSettings);
        registerBlockWithItem("lime_planks", LIME_PLANKS, fabricItemSettings);
        registerBlockWithItem("magenta_planks", MAGENTA_PLANKS, fabricItemSettings);
        registerBlockWithItem("orange_planks", ORANGE_PLANKS, fabricItemSettings);
        registerBlockWithItem("pink_planks", PINK_PLANKS, fabricItemSettings);
        registerBlockWithItem("purple_planks", PURPLE_PLANKS, fabricItemSettings);
        registerBlockWithItem("red_planks", RED_PLANKS, fabricItemSettings);
        registerBlockWithItem("white_planks", WHITE_PLANKS, fabricItemSettings);
        registerBlockWithItem("yellow_planks", YELLOW_PLANKS, fabricItemSettings);
        registerBlockWithItem("black_plank_stairs", BLACK_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("blue_plank_stairs", BLUE_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("brown_plank_stairs", BROWN_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("cyan_plank_stairs", CYAN_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("gray_plank_stairs", GRAY_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("green_plank_stairs", GREEN_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_stairs", LIGHT_BLUE_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_stairs", LIGHT_GRAY_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("lime_plank_stairs", LIME_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("magenta_plank_stairs", MAGENTA_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("orange_plank_stairs", ORANGE_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("pink_plank_stairs", PINK_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("purple_plank_stairs", PURPLE_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("red_plank_stairs", RED_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("white_plank_stairs", WHITE_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("yellow_plank_stairs", YELLOW_PLANK_STAIRS, fabricItemSettings);
        registerBlockWithItem("black_plank_pressure_plate", BLACK_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("blue_plank_pressure_plate", BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("brown_plank_pressure_plate", BROWN_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("cyan_plank_pressure_plate", CYAN_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("gray_plank_pressure_plate", GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("green_plank_pressure_plate", GREEN_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_pressure_plate", LIGHT_BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_pressure_plate", LIGHT_GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("lime_plank_pressure_plate", LIME_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("magenta_plank_pressure_plate", MAGENTA_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("orange_plank_pressure_plate", ORANGE_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("pink_plank_pressure_plate", PINK_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("purple_plank_pressure_plate", PURPLE_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("red_plank_pressure_plate", RED_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("white_plank_pressure_plate", WHITE_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("yellow_plank_pressure_plate", YELLOW_PLANK_PRESSURE_PLATE, fabricItemSettings);
        registerBlockWithItem("black_plank_fence", BLACK_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("blue_plank_fence", BLUE_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("brown_plank_fence", BROWN_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("cyan_plank_fence", CYAN_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("gray_plank_fence", GRAY_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("green_plank_fence", GREEN_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_fence", LIGHT_BLUE_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_fence", LIGHT_GRAY_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("lime_plank_fence", LIME_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("magenta_plank_fence", MAGENTA_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("orange_plank_fence", ORANGE_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("pink_plank_fence", PINK_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("purple_plank_fence", PURPLE_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("red_plank_fence", RED_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("white_plank_fence", WHITE_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("yellow_plank_fence", YELLOW_PLANK_FENCE, fabricItemSettings);
        registerBlockWithItem("black_plank_fence_gate", BLACK_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("blue_plank_fence_gate", BLUE_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("brown_plank_fence_gate", BROWN_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("cyan_plank_fence_gate", CYAN_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("gray_plank_fence_gate", GRAY_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("green_plank_fence_gate", GREEN_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_fence_gate", LIGHT_BLUE_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_fence_gate", LIGHT_GRAY_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("lime_plank_fence_gate", LIME_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("magenta_plank_fence_gate", MAGENTA_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("orange_plank_fence_gate", ORANGE_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("pink_plank_fence_gate", PINK_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("purple_plank_fence_gate", PURPLE_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("red_plank_fence_gate", RED_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("white_plank_fence_gate", WHITE_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("yellow_plank_fence_gate", YELLOW_PLANK_FENCE_GATE, fabricItemSettings);
        registerBlockWithItem("black_plank_button", BLACK_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("blue_plank_button", BLUE_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("brown_plank_button", BROWN_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("cyan_plank_button", CYAN_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("gray_plank_button", GRAY_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("green_plank_button", GREEN_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_button", LIGHT_BLUE_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_button", LIGHT_GRAY_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("lime_plank_button", LIME_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("magenta_plank_button", MAGENTA_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("orange_plank_button", ORANGE_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("pink_plank_button", PINK_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("purple_plank_button", PURPLE_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("red_plank_button", RED_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("white_plank_button", WHITE_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("yellow_plank_button", YELLOW_PLANK_BUTTON, fabricItemSettings);
        registerBlockWithItem("black_plank_slab", BLACK_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("blue_plank_slab", BLUE_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("brown_plank_slab", BROWN_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("cyan_plank_slab", CYAN_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("gray_plank_slab", GRAY_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("green_plank_slab", GREEN_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("light_blue_plank_slab", LIGHT_BLUE_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("light_gray_plank_slab", LIGHT_GRAY_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("lime_plank_slab", LIME_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("magenta_plank_slab", MAGENTA_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("orange_plank_slab", ORANGE_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("pink_plank_slab", PINK_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("purple_plank_slab", PURPLE_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("red_plank_slab", RED_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("white_plank_slab", WHITE_PLANK_SLAB, fabricItemSettings);
        registerBlockWithItem("yellow_plank_slab", YELLOW_PLANK_SLAB, fabricItemSettings);
    }

    private static void registerFlatColoredBlocks(FabricItemSettings fabricItemSettings) {
        registerBlockWithItem("black_flat_colored_block", BLACK_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("blue_flat_colored_block", BLUE_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("brown_flat_colored_block", BROWN_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("cyan_flat_colored_block", CYAN_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("gray_flat_colored_block", GRAY_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("green_flat_colored_block", GREEN_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("light_blue_flat_colored_block", LIGHT_BLUE_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("light_gray_flat_colored_block", LIGHT_GRAY_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("lime_flat_colored_block", LIME_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("magenta_flat_colored_block", MAGENTA_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("orange_flat_colored_block", ORANGE_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("pink_flat_colored_block", PINK_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("purple_flat_colored_block", PURPLE_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("red_flat_colored_block", RED_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("white_flat_colored_block", WHITE_FLAT_COLORED_BLOCK, fabricItemSettings);
        registerBlockWithItem("yellow_flat_colored_block", YELLOW_FLAT_COLORED_BLOCK, fabricItemSettings);
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

        registerBlockWithItem("ammolite_block", AMMOLITE_BLOCK, fabricItemSettings);

        // storage blocks
        registerBlockWithItem("topaz_storage_block", TOPAZ_STORAGE_BLOCK, fabricItemSettings);
        registerBlockWithItem("amethyst_storage_block", AMETHYST_STORAGE_BLOCK, fabricItemSettings);
        registerBlockWithItem("citrine_storage_block", CITRINE_STORAGE_BLOCK, fabricItemSettings);
        registerBlockWithItem("onyx_storage_block", ONYX_STORAGE_BLOCK, fabricItemSettings);
        registerBlockWithItem("moonstone_storage_block", MOONSTONE_STORAGE_BLOCK, fabricItemSettings);
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
        FabricBlockSettings mobHeadBlockSettings = FabricBlockSettings.of(Material.DECORATION).strength(1.0F);

        for(PigmentSkullBlock.Type type : PigmentSkullBlock.Type.values()) {
            Block head = new PigmentSkullBlock(type, mobHeadBlockSettings);
            Block wallHead = new PigmentWallSkullBlock(type, FabricBlockSettings.of(Material.DECORATION).strength(1.0F).dropsLike(head));
            BlockItem headItem = new PigmentSkullBlockItem(head, wallHead, (fabricItemSettings));

            MOB_HEADS.put(type, head);
            MOB_WALL_HEADS.put(type, wallHead);

            registerBlock(type.name().toLowerCase(Locale.ROOT) + "_head", head);
            registerBlock(type.name().toLowerCase(Locale.ROOT) + "_wall_head", wallHead);
            registerBlockItem(type.name().toLowerCase(Locale.ROOT) + "_head", headItem);
        }
    }

    public static Block getMobHead(PigmentSkullBlock.Type skullType) {
        return MOB_HEADS.get(skullType);
    }

    public static PigmentSkullBlock.Type getSkullType(Block block) {
        if(block instanceof PigmentWallSkullBlock) {
            return MOB_WALL_HEADS.inverse().get(block);
        } else {
            return MOB_HEADS.inverse().get(block);
        }
    }

    public static Block getMobWallHead(PigmentSkullBlock.Type skullType) {
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
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_TOPAZ_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_CITRINE_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_ONYX_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_MOONSTONE_BUD, RenderLayer.getCutout());

        // Glass
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GLOWING_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GLOWING_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TINTED_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.VANILLA_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ENDER_GLASS, RenderLayer.getTranslucent());

        // Gem lamps
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_CALCITE_LAMP, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_BASALT_LAMP, RenderLayer.getTranslucent());

        // Saplings
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BLACK_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BLUE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BROWN_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CYAN_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GRAY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GREEN_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIGHT_BLUE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIGHT_GRAY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIME_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MAGENTA_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ORANGE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.PINK_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.PURPLE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.RED_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.WHITE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.YELLOW_SAPLING, RenderLayer.getCutout());

        // Colored lamps
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BLACK_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BLUE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.BROWN_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CYAN_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GRAY_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GREEN_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIGHT_BLUE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIGHT_GRAY_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LIME_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MAGENTA_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ORANGE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.PINK_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.PURPLE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.RED_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.WHITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.YELLOW_LAMP, RenderLayer.getTranslucent());

        // Others
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.QUITOXIC_REEDS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MERMAIDS_BRUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.RESONANT_LILY, RenderLayer.getCutout());
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
    public static Item getColoredLogItem(DyeColor dyeColor) {
        return getColoredLogBlock(dyeColor).asItem();
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

    public static Item getColoredSaplingItem(DyeColor dyeColor) {
        return getColoredSaplingBlock(dyeColor).asItem();
    }
}
