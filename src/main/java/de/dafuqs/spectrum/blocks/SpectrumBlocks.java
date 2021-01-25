package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.altar.AltarBlock;
import de.dafuqs.spectrum.blocks.base.DirectionalBlock;
import de.dafuqs.spectrum.blocks.base.SpectrumStairsBlock;
import de.dafuqs.spectrum.blocks.buddingblocks.BuddingCitrineBlock;
import de.dafuqs.spectrum.blocks.buddingblocks.BuddingMoonstoneBlock;
import de.dafuqs.spectrum.blocks.buddingblocks.BuddingOnyxBlock;
import de.dafuqs.spectrum.blocks.buddingblocks.BuddingTopazBlock;
import de.dafuqs.spectrum.blocks.decay.DecayBlock1;
import de.dafuqs.spectrum.blocks.decay.DecayBlock2;
import de.dafuqs.spectrum.blocks.decay.DecayBlock3;
import de.dafuqs.spectrum.blocks.melon.AttachedGlisteringStemBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringMelonBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringStemBlock;
import de.dafuqs.spectrum.blocks.tree.OminousSaplingBlock;
import de.dafuqs.spectrum.blocks.tree.OminousSaplingBlockItem;
import de.dafuqs.spectrum.fluid.LiquidCrystalBlock;
import de.dafuqs.spectrum.fluid.SpectrumFluids;
import de.dafuqs.spectrum.items.SpectrumItemGroups;
import de.dafuqs.spectrum.items.SpectrumItems;
import de.dafuqs.spectrum.misc.SpectrumMaterial;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.function.ToIntFunction;

public class SpectrumBlocks {

    private static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel) {
        return (blockState) -> {
            return (Boolean)blockState.get(Properties.LIT) ? litLevel : 0;
        };
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }



    public static FabricItemSettings blockItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BUILDING);

    // TODO: hardness
    public static final Block CITRINE_BLOCK = new AmethystBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block BUDDING_CITRINE = new BuddingCitrineBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.AMETHYST_CLUSTER).luminance(createLightLevelFromBlockState(7)));
    public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance(createLightLevelFromBlockState(5)));
    public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance(createLightLevelFromBlockState(3)));
    public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance(createLightLevelFromBlockState(2)));

    public static final Block TOPAZ_BLOCK = new AmethystBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block BUDDING_TOPAZ = new BuddingTopazBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.AMETHYST_CLUSTER).luminance(createLightLevelFromBlockState(6)));
    public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance(createLightLevelFromBlockState(4)));
    public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance(createLightLevelFromBlockState(2)));
    public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance(createLightLevelFromBlockState(1)));

    public static final Block ONYX_BLOCK = new AmethystBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block BUDDING_ONYX = new BuddingOnyxBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.AMETHYST_CLUSTER).luminance(createLightLevelFromBlockState(3)));
    public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance(createLightLevelFromBlockState(2)));
    public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance(createLightLevelFromBlockState(1)));
    public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance(createLightLevelFromBlockState(0)));

    public static final Block MOONSTONE_BLOCK = new AmethystBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block BUDDING_MOONSTONE = new BuddingMoonstoneBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
    public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.AMETHYST_CLUSTER).luminance(createLightLevelFromBlockState(14)));
    public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance(createLightLevelFromBlockState(10)));
    public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance(createLightLevelFromBlockState(7)));
    public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance(createLightLevelFromBlockState(4)));

    public static final Block RAINBOW_MOONSTONE_BLOCK = new AmethystBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));

    public static final Block TUFF_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_STAIRS = new SpectrumStairsBlock(Blocks.TUFF.getDefaultState(), AbstractBlock.Settings.copy(Blocks.TUFF));

    public static final Block POLISHED_TUFF = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_TUFF_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_TUFF_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_TUFF_STAIRS = new SpectrumStairsBlock(POLISHED_TUFF.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_TUFF));

    public static final Block TUFF_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_BRICK_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_BRICK_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_BRICK_STAIRS = new SpectrumStairsBlock(TUFF_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(TUFF_BRICKS));

    public static final Block AMETHYST_CHISELED_TUFF = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(5));
    public static final Block TOPAZ_CHISELED_TUFF = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(6));
    public static final Block CITRINE_CHISELED_TUFF = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(7));
    public static final Block ONYX_CHISELED_TUFF = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(3));
    public static final Block MOONSTONE_CHISELED_TUFF = new DirectionalBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(12));

    public static final Block CALCITE_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_STAIRS = new SpectrumStairsBlock(Blocks.CALCITE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_STAIRS = new SpectrumStairsBlock(POLISHED_CALCITE.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_CALCITE));

    public static final Block CALCITE_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_STAIRS = new SpectrumStairsBlock(CALCITE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(CALCITE_BRICKS));

    public static final Block AMETHYST_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(5));
    public static final Block TOPAZ_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(6));
    public static final Block CITRINE_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(7));
    public static final Block ONYX_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(3));
    public static final Block MOONSTONE_CHISELED_CALCITE = new DirectionalBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(12));

    // LAMPS
    public static final Block AMETHYST_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block TOPAZ_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block CITRINE_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block ONYX_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block MOONSTONE_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block AMETHYST_TUFF_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block TOPAZ_TUFF_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block CITRINE_TUFF_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block ONYX_TUFF_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block MOONSTONE_TUFF_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());

    // GLASS
    public static final Block AMETHYST_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block TOPAZ_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block CITRINE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block ONYX_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block MOONSTONE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));

    // PLAYER GLASS
    public static final Block VANILLA_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
    public static final Block AMETHYST_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
    public static final Block TOPAZ_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
    public static final Block CITRINE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
    public static final Block ONYX_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
    public static final Block MOONSTONE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(SpectrumBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));

    // MELON
    public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.of(Material.GOURD, MapColor.LIME).strength(1.0F).sounds(BlockSoundGroup.WOOD));
    public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.of(Material.PLANT).noCollision().nonOpaque().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.STEM));
    public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.of(Material.PLANT).nonOpaque().noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD));

    // SAPLING
    private static final FabricBlockSettings saplingSettings = FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);
    public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(saplingSettings);

    // DECAY
    private static final FabricBlockSettings decaySettings = FabricBlockSettings.of(SpectrumMaterial.DECAY, MapColor.BLACK)
            .ticksRandomly().requiresTool().breakByTool(FabricToolTags.PICKAXES);
    public static final Block DECAY1 = new DecayBlock1(decaySettings.hardness(0.5F).resistance(0.5F), BlockTags.LEAVES, null,1,  1F);
    public static final Block DECAY2 = new DecayBlock2(decaySettings.hardness(20.0F).resistance(50.0F), null, SpectrumBlockTags.DECAY2_SAFE, 2,  2.5F);
    public static final Block DECAY3 = new DecayBlock3(decaySettings.hardness(100.0F).resistance(3600000.0F), null, SpectrumBlockTags.DECAY3_SAFE, 3, 5F);

    // ALTAR
    private static final FabricBlockSettings altarSettings = FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool().hardness(5.0F).resistance(20.0F);
    public static final Block ALTAR = new AltarBlock(altarSettings);

    // FLUIDS
    public static final Block LIQUID_CRYSTAL = new LiquidCrystalBlock(SpectrumFluids.STILL_LIQUID_CRYSTAL, FabricBlockSettings.copyOf(Blocks.WATER).luminance((state) -> 8));

    // COLORED TREES
    public static final Block BLACK_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block BLUE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block BROWN_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block CYAN_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block GRAY_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block GREEN_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block LIGHT_BLUE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block LIGHT_GRAY_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block LIME_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block MAGENTA_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block ORANGE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block PINK_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block PURPLE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block RED_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block WHITE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block YELLOW_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));

    public static final Block BLACK_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block BLUE_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block BROWN_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block CYAN_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block GRAY_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block GREEN_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block LIGHT_BLUE_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block LIGHT_GRAY_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block LIME_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block MAGENTA_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block ORANGE_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block PINK_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block PURPLE_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block RED_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block WHITE_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));
    public static final Block YELLOW_LEAVES = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));

    public static final Block BLACK_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block BLUE_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block BROWN_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block CYAN_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block GRAY_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block GREEN_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block LIGHT_BLUE_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block LIGHT_GRAY_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block LIME_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block MAGENTA_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block ORANGE_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block PINK_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block PURPLE_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block RED_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block WHITE_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    public static final Block YELLOW_SAPLING = new Block(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));

    private static void registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, BlockItem blockItem) {
        Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
    }

    public static void register() {
        registerBlock("small_citrine_bud", SMALL_CITRINE_BUD);
        registerBlockItem("small_citrine_bud", new BlockItem(SMALL_CITRINE_BUD, blockItemSettings));
        registerBlock("medium_citrine_bud", MEDIUM_CITRINE_BUD);
        registerBlockItem("medium_citrine_bud", new BlockItem(MEDIUM_CITRINE_BUD, blockItemSettings));
        registerBlock("large_citrine_bud", LARGE_CITRINE_BUD);
        registerBlockItem("large_citrine_bud", new BlockItem(LARGE_CITRINE_BUD, blockItemSettings));
        registerBlock("citrine_cluster", CITRINE_CLUSTER);
        registerBlockItem("citrine_cluster", new BlockItem(CITRINE_CLUSTER, blockItemSettings));
        registerBlock("citrine_block", CITRINE_BLOCK);
        registerBlockItem("citrine_block", new BlockItem(CITRINE_BLOCK, blockItemSettings));
        registerBlock("budding_citrine", BUDDING_CITRINE);
        registerBlockItem("budding_citrine", new BlockItem(BUDDING_CITRINE, blockItemSettings));
        registerBlock("small_topaz_bud", SMALL_TOPAZ_BUD);
        registerBlockItem("small_topaz_bud", new BlockItem(SMALL_TOPAZ_BUD, blockItemSettings));
        registerBlock("medium_topaz_bud", MEDIUM_TOPAZ_BUD);
        registerBlockItem("medium_topaz_bud", new BlockItem(MEDIUM_TOPAZ_BUD, blockItemSettings));
        registerBlock("large_topaz_bud", LARGE_TOPAZ_BUD);
        registerBlockItem("large_topaz_bud", new BlockItem(LARGE_TOPAZ_BUD, blockItemSettings));
        registerBlock("topaz_cluster", TOPAZ_CLUSTER);
        registerBlockItem("topaz_cluster", new BlockItem(TOPAZ_CLUSTER, blockItemSettings));
        registerBlock("topaz_block", TOPAZ_BLOCK);
        registerBlockItem("topaz_block", new BlockItem(TOPAZ_BLOCK, blockItemSettings));
        registerBlock("budding_topaz", BUDDING_TOPAZ);
        registerBlockItem("budding_topaz", new BlockItem(BUDDING_TOPAZ, blockItemSettings));
        registerBlock("small_onyx_bud", SMALL_ONYX_BUD);
        registerBlockItem("small_onyx_bud", new BlockItem(SMALL_ONYX_BUD, blockItemSettings));
        registerBlock("medium_onyx_bud", MEDIUM_ONYX_BUD);
        registerBlockItem("medium_onyx_bud", new BlockItem(MEDIUM_ONYX_BUD, blockItemSettings));
        registerBlock("large_onyx_bud", LARGE_ONYX_BUD);
        registerBlockItem("large_onyx_bud", new BlockItem(LARGE_ONYX_BUD, blockItemSettings));
        registerBlock("onyx_cluster", ONYX_CLUSTER);
        registerBlockItem("onyx_cluster", new BlockItem(ONYX_CLUSTER, blockItemSettings));
        registerBlock("onyx_block", ONYX_BLOCK);
        registerBlockItem("onyx_block", new BlockItem(ONYX_BLOCK, blockItemSettings));
        registerBlock("budding_onyx", BUDDING_ONYX);
        registerBlockItem("budding_onyx", new BlockItem(BUDDING_ONYX, blockItemSettings));
        registerBlock("small_moonstone_bud", SMALL_MOONSTONE_BUD);
        registerBlockItem("small_moonstone_bud", new BlockItem(SMALL_MOONSTONE_BUD, blockItemSettings));
        registerBlock("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD);
        registerBlockItem("medium_moonstone_bud", new BlockItem(MEDIUM_MOONSTONE_BUD, blockItemSettings));
        registerBlock("large_moonstone_bud", LARGE_MOONSTONE_BUD);
        registerBlockItem("large_moonstone_bud", new BlockItem(LARGE_MOONSTONE_BUD, blockItemSettings));
        registerBlock("moonstone_cluster", MOONSTONE_CLUSTER);
        registerBlockItem("moonstone_cluster", new BlockItem(MOONSTONE_CLUSTER, blockItemSettings));
        registerBlock("moonstone_block", MOONSTONE_BLOCK);
        registerBlockItem("moonstone_block", new BlockItem(MOONSTONE_BLOCK, blockItemSettings));
        registerBlock("budding_moonstone", BUDDING_MOONSTONE);
        registerBlockItem("budding_moonstone", new BlockItem(BUDDING_MOONSTONE, blockItemSettings));
        registerBlock("tuff_slab", TUFF_SLAB);
        registerBlockItem("tuff_slab", new BlockItem(TUFF_SLAB, blockItemSettings));
        registerBlock("tuff_wall", TUFF_WALL);
        registerBlockItem("tuff_wall", new BlockItem(TUFF_WALL, blockItemSettings));
        registerBlock("tuff_stairs", TUFF_STAIRS);
        registerBlockItem("tuff_stairs", new BlockItem(TUFF_STAIRS, blockItemSettings));
        registerBlock("polished_tuff", POLISHED_TUFF);
        registerBlockItem("polished_tuff", new BlockItem(POLISHED_TUFF, blockItemSettings));
        registerBlock("polished_tuff_slab", POLISHED_TUFF_SLAB);
        registerBlockItem("polished_tuff_slab", new BlockItem(POLISHED_TUFF_SLAB, blockItemSettings));
        registerBlock("polished_tuff_wall", POLISHED_TUFF_WALL);
        registerBlockItem("polished_tuff_wall", new BlockItem(POLISHED_TUFF_WALL, blockItemSettings));
        registerBlock("polished_tuff_stairs", POLISHED_TUFF_STAIRS);
        registerBlockItem("polished_tuff_stairs", new BlockItem(POLISHED_TUFF_STAIRS, blockItemSettings));
        registerBlock("tuff_bricks", TUFF_BRICKS);
        registerBlockItem("tuff_bricks", new BlockItem(TUFF_BRICKS, blockItemSettings));
        registerBlock("tuff_brick_slab", TUFF_BRICK_SLAB);
        registerBlockItem("tuff_brick_slab", new BlockItem(TUFF_BRICK_SLAB, blockItemSettings));
        registerBlock("tuff_brick_wall", TUFF_BRICK_WALL);
        registerBlockItem("tuff_brick_wall", new BlockItem(TUFF_BRICK_WALL, blockItemSettings));
        registerBlock("tuff_brick_stairs", TUFF_BRICK_STAIRS);
        registerBlockItem("tuff_brick_stairs", new BlockItem(TUFF_BRICK_STAIRS, blockItemSettings));
        registerBlock("amethyst_chiseled_tuff", AMETHYST_CHISELED_TUFF);
        registerBlockItem("amethyst_chiseled_tuff", new BlockItem(AMETHYST_CHISELED_TUFF, blockItemSettings));
        registerBlock("topaz_chiseled_tuff", TOPAZ_CHISELED_TUFF);
        registerBlockItem("topaz_chiseled_tuff", new BlockItem(TOPAZ_CHISELED_TUFF, blockItemSettings));
        registerBlock("citrine_chiseled_tuff", CITRINE_CHISELED_TUFF);
        registerBlockItem("citrine_chiseled_tuff", new BlockItem(CITRINE_CHISELED_TUFF, blockItemSettings));
        registerBlock("onyx_chiseled_tuff", ONYX_CHISELED_TUFF);
        registerBlockItem("onyx_chiseled_tuff", new BlockItem(ONYX_CHISELED_TUFF, blockItemSettings));
        registerBlock("moonstone_chiseled_tuff", MOONSTONE_CHISELED_TUFF);
        registerBlockItem("moonstone_chiseled_tuff", new BlockItem(MOONSTONE_CHISELED_TUFF, blockItemSettings));
        registerBlock("calcite_slab", CALCITE_SLAB);
        registerBlockItem("calcite_slab", new BlockItem(CALCITE_SLAB, blockItemSettings));
        registerBlock("calcite_wall", CALCITE_WALL);
        registerBlockItem("calcite_wall", new BlockItem(CALCITE_WALL, blockItemSettings));
        registerBlock("calcite_stairs", CALCITE_STAIRS);
        registerBlockItem("calcite_stairs", new BlockItem(CALCITE_STAIRS, blockItemSettings));
        registerBlock("polished_calcite", POLISHED_CALCITE);
        registerBlockItem("polished_calcite", new BlockItem(POLISHED_CALCITE, blockItemSettings));
        registerBlock("polished_calcite_slab", POLISHED_CALCITE_SLAB);
        registerBlockItem("polished_calcite_slab", new BlockItem(POLISHED_CALCITE_SLAB, blockItemSettings));
        registerBlock("polished_calcite_wall", POLISHED_CALCITE_WALL);
        registerBlockItem("polished_calcite_wall", new BlockItem(POLISHED_CALCITE_WALL, blockItemSettings));
        registerBlock("polished_calcite_stairs", POLISHED_CALCITE_STAIRS);
        registerBlockItem("polished_calcite_stairs", new BlockItem(POLISHED_CALCITE_STAIRS, blockItemSettings));
        registerBlock("calcite_bricks", CALCITE_BRICKS);
        registerBlockItem("calcite_bricks", new BlockItem(CALCITE_BRICKS, blockItemSettings));
        registerBlock("calcite_brick_slab", CALCITE_BRICK_SLAB);
        registerBlockItem("calcite_brick_slab", new BlockItem(CALCITE_BRICK_SLAB, blockItemSettings));
        registerBlock("calcite_brick_wall", CALCITE_BRICK_WALL);
        registerBlockItem("calcite_brick_wall", new BlockItem(CALCITE_BRICK_WALL, blockItemSettings));
        registerBlock("calcite_brick_stairs", CALCITE_BRICK_STAIRS);
        registerBlockItem("calcite_brick_stairs", new BlockItem(CALCITE_BRICK_STAIRS, blockItemSettings));
        registerBlock("amethyst_chiseled_calcite", AMETHYST_CHISELED_CALCITE);
        registerBlockItem("amethyst_chiseled_calcite", new BlockItem(AMETHYST_CHISELED_CALCITE, blockItemSettings));
        registerBlock("topaz_chiseled_calcite", TOPAZ_CHISELED_CALCITE);
        registerBlockItem("topaz_chiseled_calcite", new BlockItem(TOPAZ_CHISELED_CALCITE, blockItemSettings));
        registerBlock("citrine_chiseled_calcite", CITRINE_CHISELED_CALCITE);
        registerBlockItem("citrine_chiseled_calcite", new BlockItem(CITRINE_CHISELED_CALCITE, blockItemSettings));
        registerBlock("onyx_chiseled_calcite", ONYX_CHISELED_CALCITE);
        registerBlockItem("onyx_chiseled_calcite", new BlockItem(ONYX_CHISELED_CALCITE, blockItemSettings));
        registerBlock("moonstone_chiseled_calcite", MOONSTONE_CHISELED_CALCITE);
        registerBlockItem("moonstone_chiseled_calcite", new BlockItem(MOONSTONE_CHISELED_CALCITE, blockItemSettings));
        registerBlock("rainbow_moonstone_block", RAINBOW_MOONSTONE_BLOCK);
        registerBlockItem("rainbow_moonstone_block", new BlockItem(RAINBOW_MOONSTONE_BLOCK, blockItemSettings));

        // LAMPS
        registerBlock("amethyst_calcite_lamp", AMETHYST_CALCITE_LAMP);
        registerBlockItem("amethyst_calcite_lamp", new BlockItem(AMETHYST_CALCITE_LAMP, blockItemSettings));
        registerBlock("topaz_calcite_lamp", TOPAZ_CALCITE_LAMP);
        registerBlockItem("topaz_calcite_lamp", new BlockItem(TOPAZ_CALCITE_LAMP, blockItemSettings));
        registerBlock("citrine_calcite_lamp", CITRINE_CALCITE_LAMP);
        registerBlockItem("citrine_calcite_lamp", new BlockItem(CITRINE_CALCITE_LAMP, blockItemSettings));
        registerBlock("onyx_calcite_lamp", ONYX_CALCITE_LAMP);
        registerBlockItem("onyx_calcite_lamp", new BlockItem(ONYX_CALCITE_LAMP, blockItemSettings));
        registerBlock("moonstone_calcite_lamp", MOONSTONE_CALCITE_LAMP);
        registerBlockItem("moonstone_calcite_lamp", new BlockItem(MOONSTONE_CALCITE_LAMP, blockItemSettings));
        registerBlock("amethyst_tuff_lamp", AMETHYST_TUFF_LAMP);
        registerBlockItem("amethyst_tuff_lamp", new BlockItem(AMETHYST_TUFF_LAMP, blockItemSettings));
        registerBlock("topaz_tuff_lamp", TOPAZ_TUFF_LAMP);
        registerBlockItem("topaz_tuff_lamp", new BlockItem(TOPAZ_TUFF_LAMP, blockItemSettings));
        registerBlock("citrine_tuff_lamp", CITRINE_TUFF_LAMP);
        registerBlockItem("citrine_tuff_lamp", new BlockItem(CITRINE_TUFF_LAMP, blockItemSettings));
        registerBlock("onyx_tuff_lamp", ONYX_TUFF_LAMP);
        registerBlockItem("onyx_tuff_lamp", new BlockItem(ONYX_TUFF_LAMP, blockItemSettings));
        registerBlock("moonstone_tuff_lamp", MOONSTONE_TUFF_LAMP);
        registerBlockItem("moonstone_tuff_lamp", new BlockItem(MOONSTONE_TUFF_LAMP, blockItemSettings));

        // GLASS
        registerBlock("amethyst_glass", AMETHYST_GLASS);
        registerBlockItem("amethyst_glass", new BlockItem(AMETHYST_GLASS, blockItemSettings));
        registerBlock("topaz_glass", TOPAZ_GLASS);
        registerBlockItem("topaz_glass", new BlockItem(TOPAZ_GLASS, blockItemSettings));
        registerBlock("citrine_glass", CITRINE_GLASS);
        registerBlockItem("citrine_glass", new BlockItem(CITRINE_GLASS, blockItemSettings));
        registerBlock("onyx_glass", ONYX_GLASS);
        registerBlockItem("onyx_glass", new BlockItem(ONYX_GLASS, blockItemSettings));
        registerBlock("moonstone_glass", MOONSTONE_GLASS);
        registerBlockItem("moonstone_glass", new BlockItem(MOONSTONE_GLASS, blockItemSettings));

        // PLAYER ONLY GLASS
        registerBlock("vanilla_player_only_glass", VANILLA_PLAYER_ONLY_GLASS);
        registerBlockItem("vanilla_player_only_glass", new BlockItem(VANILLA_PLAYER_ONLY_GLASS, blockItemSettings));
        registerBlock("amethyst_player_only_glass", AMETHYST_PLAYER_ONLY_GLASS);
        registerBlockItem("amethyst_player_only_glass", new BlockItem(AMETHYST_PLAYER_ONLY_GLASS, blockItemSettings));
        registerBlock("topaz_player_only_glass", TOPAZ_PLAYER_ONLY_GLASS);
        registerBlockItem("topaz_player_only_glass", new BlockItem(TOPAZ_PLAYER_ONLY_GLASS, blockItemSettings));
        registerBlock("citrine_player_only_glass", CITRINE_PLAYER_ONLY_GLASS);
        registerBlockItem("citrine_player_only_glass", new BlockItem(CITRINE_PLAYER_ONLY_GLASS, blockItemSettings));
        registerBlock("onyx_player_only_glass", ONYX_PLAYER_ONLY_GLASS);
        registerBlockItem("onyx_player_only_glass", new BlockItem(ONYX_PLAYER_ONLY_GLASS, blockItemSettings));
        registerBlock("moonstone_player_only_glass", MOONSTONE_PLAYER_ONLY_GLASS);
        registerBlockItem("moonstone_player_only_glass", new BlockItem(MOONSTONE_PLAYER_ONLY_GLASS, blockItemSettings));

        // GLISTERING MELON
        registerBlock("glistering_melon", GLISTERING_MELON);
        registerBlockItem("glistering_melon", new BlockItem(GLISTERING_MELON, blockItemSettings));
        registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
        registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);

        // SAPLING
        registerBlock("ominous_sapling", OMINOUS_SAPLING);
        registerBlockItem("ominous_sapling", new OminousSaplingBlockItem(OMINOUS_SAPLING, blockItemSettings));

        // DECAY
        registerBlock("decay1", DECAY1);
        registerBlockItem("decay1", new BlockItem(DECAY1, blockItemSettings));
        registerBlock("decay2", DECAY2);
        registerBlockItem("decay2", new BlockItem(DECAY2, blockItemSettings));
        registerBlock("decay3", DECAY3);
        registerBlockItem("decay3", new BlockItem(DECAY3, blockItemSettings));

        // ALTAR
        registerBlock("altar", ALTAR);
        registerBlockItem("altar", new BlockItem(ALTAR, blockItemSettings));

        // FLUIDS
        registerBlock("liquid_crystal.json", LIQUID_CRYSTAL);

        // COLORED TREES
        registerBlock("black_log", BLACK_LOG);
        registerBlockItem("black_log", new BlockItem(BLACK_LOG, blockItemSettings));
        registerBlock("blue_log", BLUE_LOG);
        registerBlockItem("blue_log", new BlockItem(BLUE_LOG, blockItemSettings));
        registerBlock("brown_log", BROWN_LOG);
        registerBlockItem("brown_log", new BlockItem(BROWN_LOG, blockItemSettings));
        registerBlock("cyan_log", CYAN_LOG);
        registerBlockItem("cyan_log", new BlockItem(CYAN_LOG, blockItemSettings));
        registerBlock("gray_log", GRAY_LOG);
        registerBlockItem("gray_log", new BlockItem(GRAY_LOG, blockItemSettings));
        registerBlock("green_log", GREEN_LOG);
        registerBlockItem("green_log", new BlockItem(GREEN_LOG, blockItemSettings));
        registerBlock("light_blue_log", LIGHT_BLUE_LOG);
        registerBlockItem("light_blue_log", new BlockItem(LIGHT_BLUE_LOG, blockItemSettings));
        registerBlock("light_gray_log", LIGHT_GRAY_LOG);
        registerBlockItem("light_gray_log", new BlockItem(LIGHT_GRAY_LOG, blockItemSettings));
        registerBlock("lime_log", LIME_LOG);
        registerBlockItem("lime_log", new BlockItem(LIME_LOG, blockItemSettings));
        registerBlock("magenta_log", MAGENTA_LOG);
        registerBlockItem("magenta_log", new BlockItem(MAGENTA_LOG, blockItemSettings));
        registerBlock("orange_log", ORANGE_LOG);
        registerBlockItem("orange_log", new BlockItem(ORANGE_LOG, blockItemSettings));
        registerBlock("pink_log", PINK_LOG);
        registerBlockItem("pink_log", new BlockItem(PINK_LOG, blockItemSettings));
        registerBlock("purple_log", PURPLE_LOG);
        registerBlockItem("purple_log", new BlockItem(PURPLE_LOG, blockItemSettings));
        registerBlock("red_log", RED_LOG);
        registerBlockItem("red_log", new BlockItem(RED_LOG, blockItemSettings));
        registerBlock("white_log", WHITE_LOG);
        registerBlockItem("white_log", new BlockItem(WHITE_LOG, blockItemSettings));
        registerBlock("yellow_log", YELLOW_LOG);
        registerBlockItem("yellow_log", new BlockItem(YELLOW_LOG, blockItemSettings));

        registerBlock("black_leaves", BLACK_LEAVES);
        registerBlockItem("black_leaves", new BlockItem(BLACK_LEAVES, blockItemSettings));
        registerBlock("blue_leaves", BLUE_LEAVES);
        registerBlockItem("blue_leaves", new BlockItem(BLUE_LEAVES, blockItemSettings));
        registerBlock("brown_leaves", BROWN_LEAVES);
        registerBlockItem("brown_leaves", new BlockItem(BROWN_LEAVES, blockItemSettings));
        registerBlock("cyan_leaves", CYAN_LEAVES);
        registerBlockItem("cyan_leaves", new BlockItem(CYAN_LEAVES, blockItemSettings));
        registerBlock("gray_leaves", GRAY_LEAVES);
        registerBlockItem("gray_leaves", new BlockItem(GRAY_LEAVES, blockItemSettings));
        registerBlock("green_leaves", GREEN_LEAVES);
        registerBlockItem("green_leaves", new BlockItem(GREEN_LEAVES, blockItemSettings));
        registerBlock("light_blue_leaves", LIGHT_BLUE_LEAVES);
        registerBlockItem("light_blue_leaves", new BlockItem(LIGHT_BLUE_LEAVES, blockItemSettings));
        registerBlock("light_gray_leaves", LIGHT_GRAY_LEAVES);
        registerBlockItem("light_gray_leaves", new BlockItem(LIGHT_GRAY_LEAVES, blockItemSettings));
        registerBlock("lime_leaves", LIME_LEAVES);
        registerBlockItem("lime_leaves", new BlockItem(LIME_LEAVES, blockItemSettings));
        registerBlock("magenta_leaves", MAGENTA_LEAVES);
        registerBlockItem("magenta_leaves", new BlockItem(MAGENTA_LEAVES, blockItemSettings));
        registerBlock("orange_leaves", ORANGE_LEAVES);
        registerBlockItem("orange_leaves", new BlockItem(ORANGE_LEAVES, blockItemSettings));
        registerBlock("pink_leaves", PINK_LEAVES);
        registerBlockItem("pink_leaves", new BlockItem(PINK_LEAVES, blockItemSettings));
        registerBlock("purple_leaves", PURPLE_LEAVES);
        registerBlockItem("purple_leaves", new BlockItem(PURPLE_LEAVES, blockItemSettings));
        registerBlock("red_leaves", RED_LEAVES);
        registerBlockItem("red_leaves", new BlockItem(RED_LEAVES, blockItemSettings));
        registerBlock("white_leaves", WHITE_LEAVES);
        registerBlockItem("white_leaves", new BlockItem(WHITE_LEAVES, blockItemSettings));
        registerBlock("yellow_leaves", YELLOW_LEAVES);
        registerBlockItem("yellow_leaves", new BlockItem(YELLOW_LEAVES, blockItemSettings));


        registerBlock("black_sapling", BLACK_SAPLING);
        registerBlockItem("black_sapling", new BlockItem(BLACK_SAPLING, blockItemSettings));
        registerBlock("blue_sapling", BLUE_SAPLING);
        registerBlockItem("blue_sapling", new BlockItem(BLUE_SAPLING, blockItemSettings));
        registerBlock("brown_sapling", BROWN_SAPLING);
        registerBlockItem("brown_sapling", new BlockItem(BROWN_SAPLING, blockItemSettings));
        registerBlock("cyan_sapling", CYAN_SAPLING);
        registerBlockItem("cyan_sapling", new BlockItem(CYAN_SAPLING, blockItemSettings));
        registerBlock("gray_sapling", GRAY_SAPLING);
        registerBlockItem("gray_sapling", new BlockItem(GRAY_SAPLING, blockItemSettings));
        registerBlock("green_sapling", GREEN_SAPLING);
        registerBlockItem("green_sapling", new BlockItem(GREEN_SAPLING, blockItemSettings));
        registerBlock("light_blue_sapling", LIGHT_BLUE_SAPLING);
        registerBlockItem("light_blue_sapling", new BlockItem(LIGHT_BLUE_SAPLING, blockItemSettings));
        registerBlock("light_gray_sapling", LIGHT_GRAY_SAPLING);
        registerBlockItem("light_gray_sapling", new BlockItem(LIGHT_GRAY_SAPLING, blockItemSettings));
        registerBlock("lime_sapling", LIME_SAPLING);
        registerBlockItem("lime_sapling", new BlockItem(LIME_SAPLING, blockItemSettings));
        registerBlock("magenta_sapling", MAGENTA_SAPLING);
        registerBlockItem("magenta_sapling", new BlockItem(MAGENTA_SAPLING, blockItemSettings));
        registerBlock("orange_sapling", ORANGE_SAPLING);
        registerBlockItem("orange_sapling", new BlockItem(ORANGE_SAPLING, blockItemSettings));
        registerBlock("pink_sapling", PINK_SAPLING);
        registerBlockItem("pink_sapling", new BlockItem(PINK_SAPLING, blockItemSettings));
        registerBlock("purple_sapling", PURPLE_SAPLING);
        registerBlockItem("purple_sapling", new BlockItem(PURPLE_SAPLING, blockItemSettings));
        registerBlock("red_sapling", RED_SAPLING);
        registerBlockItem("red_sapling", new BlockItem(RED_SAPLING, blockItemSettings));
        registerBlock("white_sapling", WHITE_SAPLING);
        registerBlockItem("white_sapling", new BlockItem(WHITE_SAPLING, blockItemSettings));
        registerBlock("yellow_sapling", YELLOW_SAPLING);
        registerBlockItem("yellow_sapling", new BlockItem(YELLOW_SAPLING, blockItemSettings));
    }

    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_CITRINE_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_ONYX_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_TOPAZ_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.SMALL_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEDIUM_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.LARGE_MOONSTONE_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.VANILLA_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());


        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_CALCITE_LAMP, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_TUFF_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_TUFF_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_TUFF_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_TUFF_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_TUFF_LAMP, RenderLayer.getTranslucent());

        // SAPLINGS
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
    }
}
