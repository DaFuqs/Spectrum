package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fluid.LiquidCrystalBlock;
import de.dafuqs.spectrum.blocks.fluid.SpectrumFluids;
import de.dafuqs.spectrum.blocks.types.*;
import de.dafuqs.spectrum.blocks.types.altar.AltarBlock;
import de.dafuqs.spectrum.blocks.types.budding.BuddingCitrineBlock;
import de.dafuqs.spectrum.blocks.types.budding.BuddingMoonstoneBlock;
import de.dafuqs.spectrum.blocks.types.budding.BuddingOnyxBlock;
import de.dafuqs.spectrum.blocks.types.budding.BuddingTopazBlock;
import de.dafuqs.spectrum.blocks.types.conditional.ColoredLeavesBlock;
import de.dafuqs.spectrum.blocks.types.conditional.ColoredLogBlock;
import de.dafuqs.spectrum.blocks.types.conditional.ConditionallyVisibleBlock;
import de.dafuqs.spectrum.blocks.types.decay.DecayBlock1;
import de.dafuqs.spectrum.blocks.types.decay.DecayBlock2;
import de.dafuqs.spectrum.blocks.types.decay.DecayBlock3;
import de.dafuqs.spectrum.blocks.types.melon.AttachedGlisteringStemBlock;
import de.dafuqs.spectrum.blocks.types.melon.GlisteringMelonBlock;
import de.dafuqs.spectrum.blocks.types.melon.GlisteringStemBlock;
import de.dafuqs.spectrum.blocks.types.tree.OminousSaplingBlock;
import de.dafuqs.spectrum.blocks.types.tree.OminousSaplingBlockItem;
import de.dafuqs.spectrum.items.SpectrumItemGroups;
import de.dafuqs.spectrum.items.SpectrumItems;
import de.dafuqs.spectrum.misc.SpectrumMaterial;
import de.dafuqs.spectrum.worldgen.ColoredSaplingGenerator;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class SpectrumBlocks {

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
    private static Boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public static FabricItemSettings functionalItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_FUNCTIONAL);
    public static FabricItemSettings decorationItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_DECORATION);
    public static FabricItemSettings coloredWoodItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_COLORED_WOOD);
    public static FabricItemSettings gemItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GEMS);

    private static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel) {
        return (blockState) -> (Boolean)blockState.get(Properties.LIT) ? litLevel : 0;
    }

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
    private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
    private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> { return 5; });
    private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG).luminance((state) -> {
        return 10;
    }); //.postProcess(SpectrumBlocks::always).emissiveLighting(SpectrumBlocks::always);

    public static final Block BLACK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BLACK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BLACK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLACK), coloredSaplingBlockSettings);
    public static final Block BLACK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BLACK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BLACK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BLACK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BLACK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BLACK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BLACK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLUE), coloredSaplingBlockSettings);
    public static final Block BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BLUE_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BLUE_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BLUE_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BLUE_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BLUE_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BLUE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block BROWN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block BROWN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block BROWN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BROWN), coloredSaplingBlockSettings);
    public static final Block BROWN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block BROWN_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block BROWN_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BROWN_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block BROWN_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block BROWN_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block BROWN_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block CYAN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block CYAN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block CYAN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.CYAN), coloredSaplingBlockSettings);
    public static final Block CYAN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block CYAN_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block CYAN_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block CYAN_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block CYAN_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block CYAN_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block CYAN_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GRAY), coloredSaplingBlockSettings);
    public static final Block GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block GRAY_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block GRAY_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block GRAY_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block GRAY_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block GRAY_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block GRAY_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block GREEN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block GREEN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block GREEN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GREEN), coloredSaplingBlockSettings);
    public static final Block GREEN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block GREEN_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block GREEN_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block GREEN_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block GREEN_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block GREEN_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block GREEN_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIGHT_BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIGHT_BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIGHT_BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_BLUE), coloredSaplingBlockSettings);
    public static final Block LIGHT_BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIGHT_BLUE_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIGHT_BLUE_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIGHT_BLUE_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIGHT_BLUE_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIGHT_BLUE_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIGHT_BLUE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIGHT_GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIGHT_GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIGHT_GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_GRAY), coloredSaplingBlockSettings);
    public static final Block LIGHT_GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIGHT_GRAY_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIGHT_GRAY_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIGHT_GRAY_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIGHT_GRAY_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIGHT_GRAY_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIGHT_GRAY_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block LIME_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block LIME_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block LIME_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIME), coloredSaplingBlockSettings);
    public static final Block LIME_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block LIME_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block LIME_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block LIME_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block LIME_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block LIME_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block LIME_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block MAGENTA_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block MAGENTA_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block MAGENTA_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.MAGENTA), coloredSaplingBlockSettings);
    public static final Block MAGENTA_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block MAGENTA_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block MAGENTA_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block MAGENTA_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block MAGENTA_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block MAGENTA_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block MAGENTA_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block ORANGE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block ORANGE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block ORANGE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.ORANGE), coloredSaplingBlockSettings);
    public static final Block ORANGE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block ORANGE_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block ORANGE_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block ORANGE_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block ORANGE_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block ORANGE_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block ORANGE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block PINK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block PINK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block PINK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PINK), coloredSaplingBlockSettings);
    public static final Block PINK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block PINK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block PINK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block PINK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block PINK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block PINK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block PINK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block PURPLE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block PURPLE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block PURPLE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PURPLE), coloredSaplingBlockSettings);
    public static final Block PURPLE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block PURPLE_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block PURPLE_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block PURPLE_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block PURPLE_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block PURPLE_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block PURPLE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block RED_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block RED_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block RED_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.RED), coloredSaplingBlockSettings);
    public static final Block RED_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block RED_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block RED_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block RED_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block RED_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block RED_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block RED_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block WHITE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block WHITE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block WHITE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.WHITE), coloredSaplingBlockSettings);
    public static final Block WHITE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block WHITE_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block WHITE_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block WHITE_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block WHITE_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block WHITE_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block WHITE_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    public static final Block YELLOW_LOG = new ColoredLogBlock(coloredLogBlockSettings);
    public static final Block YELLOW_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
    public static final Block YELLOW_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.YELLOW), coloredSaplingBlockSettings);
    public static final Block YELLOW_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block YELLOW_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block YELLOW_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
    public static final Block YELLOW_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block YELLOW_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    public static final Block YELLOW_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    public static final Block YELLOW_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));

    // FLAT COLORED BLOCKS
    private static final FabricBlockSettings flatColoredBlockBlockSettings = FabricBlockSettings.of(Material.STONE)
            .hardness(2.5F)
            .breakByTool(FabricToolTags.PICKAXES)
            .requiresTool()
            .luminance(1)
            .postProcess(SpectrumBlocks::always)
            .emissiveLighting(SpectrumBlocks::always);

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
    public static final Block BLACK_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block BLUE_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block BROWN_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block CYAN_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block GRAY_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block GREEN_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block LIGHT_BLUE_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block LIGHT_GRAY_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block LIME_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block MAGENTA_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block ORANGE_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block PINK_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block PURPLE_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block RED_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block WHITE_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));
    public static final Block YELLOW_LAMP = new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP));

    public static final Block ENDER_BLOCK = new ConditionallyVisibleBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)) { // TODO

        @Override
        protected boolean isVisible(ClientPlayerEntity clientPlayerEntity, BlockState state) {
            return clientPlayerEntity.getArmor() > 0;
        }

        @Override
        public boolean isVisibleOnServer() {
            return false;
        }

        @Override
        public VoxelShape getVisibleCollisionShape() {
            return VoxelShapes.fullCube();
        }

        @Override
        public VoxelShape getVisibleOutlineShape() {
            return VoxelShapes.fullCube();
        }
    };


    private static void registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, BlockItem blockItem) {
        Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
    }

    public static void register() {
        registerColoredWood(coloredWoodItemSettings);
        registerGemBlocks(gemItemSettings);
        registerStoneBlocks(decorationItemSettings);
        registerRunes(decorationItemSettings);
        registerStoneLamps(decorationItemSettings);
        registerColoredLamps(decorationItemSettings);
        registerGemGlass(decorationItemSettings);
        registerPlayerOnlyGlass(functionalItemSettings);
        registerFlatColoredBlocks(decorationItemSettings);

        // TEST
        registerBlock("altar", ALTAR);
        registerBlockItem("altar", new BlockItem(ALTAR, functionalItemSettings));

        // GLISTERING MELON
        registerBlock("glistering_melon", GLISTERING_MELON);
        registerBlockItem("glistering_melon", new BlockItem(GLISTERING_MELON, functionalItemSettings));
        registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
        registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);

        // SAPLING
        registerBlock("ominous_sapling", OMINOUS_SAPLING);
        registerBlockItem("ominous_sapling", new OminousSaplingBlockItem(OMINOUS_SAPLING, functionalItemSettings));

        // DECAY
        registerBlock("decay1", DECAY1);
        registerBlockItem("decay1", new BlockItem(DECAY1, functionalItemSettings));
        registerBlock("decay2", DECAY2);
        registerBlockItem("decay2", new BlockItem(DECAY2, functionalItemSettings));
        registerBlock("decay3", DECAY3);
        registerBlockItem("decay3", new BlockItem(DECAY3, functionalItemSettings));

        // FLUIDS
        registerBlock("liquid_crystal", LIQUID_CRYSTAL);
    }

    private static void registerColoredLamps(FabricItemSettings fabricItemSettings) {
        registerBlock("black_lamp", BLACK_LAMP);
        registerBlockItem("black_lamp", new BlockItem(BLACK_LAMP, fabricItemSettings));
        registerBlock("blue_lamp", BLUE_LAMP);
        registerBlockItem("blue_lamp", new BlockItem(BLUE_LAMP, fabricItemSettings));
        registerBlock("brown_lamp", BROWN_LAMP);
        registerBlockItem("brown_lamp", new BlockItem(BROWN_LAMP, fabricItemSettings));
        registerBlock("cyan_lamp", CYAN_LAMP);
        registerBlockItem("cyan_lamp", new BlockItem(CYAN_LAMP, fabricItemSettings));
        registerBlock("gray_lamp", GRAY_LAMP);
        registerBlockItem("gray_lamp", new BlockItem(GRAY_LAMP, fabricItemSettings));
        registerBlock("green_lamp", GREEN_LAMP);
        registerBlockItem("green_lamp", new BlockItem(GREEN_LAMP, fabricItemSettings));
        registerBlock("light_blue_lamp", LIGHT_BLUE_LAMP);
        registerBlockItem("light_blue_lamp", new BlockItem(LIGHT_BLUE_LAMP, fabricItemSettings));
        registerBlock("light_gray_lamp", LIGHT_GRAY_LAMP);
        registerBlockItem("light_gray_lamp", new BlockItem(LIGHT_GRAY_LAMP, fabricItemSettings));
        registerBlock("lime_lamp", LIME_LAMP);
        registerBlockItem("lime_lamp", new BlockItem(LIME_LAMP, fabricItemSettings));
        registerBlock("magenta_lamp", MAGENTA_LAMP);
        registerBlockItem("magenta_lamp", new BlockItem(MAGENTA_LAMP, fabricItemSettings));
        registerBlock("orange_lamp", ORANGE_LAMP);
        registerBlockItem("orange_lamp", new BlockItem(ORANGE_LAMP, fabricItemSettings));
        registerBlock("pink_lamp", PINK_LAMP);
        registerBlockItem("pink_lamp", new BlockItem(PINK_LAMP, fabricItemSettings));
        registerBlock("purple_lamp", PURPLE_LAMP);
        registerBlockItem("purple_lamp", new BlockItem(PURPLE_LAMP, fabricItemSettings));
        registerBlock("red_lamp", RED_LAMP);
        registerBlockItem("red_lamp", new BlockItem(RED_LAMP, fabricItemSettings));
        registerBlock("white_lamp", WHITE_LAMP);
        registerBlockItem("white_lamp", new BlockItem(WHITE_LAMP, fabricItemSettings));
        registerBlock("yellow_lamp", YELLOW_LAMP);
        registerBlockItem("yellow_lamp", new BlockItem(YELLOW_LAMP, fabricItemSettings));
    }

    private static void registerGemGlass(FabricItemSettings fabricItemSettings) {
        registerBlock("amethyst_glass", AMETHYST_GLASS);
        registerBlockItem("amethyst_glass", new BlockItem(AMETHYST_GLASS, fabricItemSettings));
        registerBlock("topaz_glass", TOPAZ_GLASS);
        registerBlockItem("topaz_glass", new BlockItem(TOPAZ_GLASS, fabricItemSettings));
        registerBlock("citrine_glass", CITRINE_GLASS);
        registerBlockItem("citrine_glass", new BlockItem(CITRINE_GLASS, fabricItemSettings));
        registerBlock("onyx_glass", ONYX_GLASS);
        registerBlockItem("onyx_glass", new BlockItem(ONYX_GLASS, fabricItemSettings));
        registerBlock("moonstone_glass", MOONSTONE_GLASS);
        registerBlockItem("moonstone_glass", new BlockItem(MOONSTONE_GLASS, fabricItemSettings));
    }

    private static void registerPlayerOnlyGlass(FabricItemSettings fabricItemSettings) {
        registerBlock("vanilla_player_only_glass", VANILLA_PLAYER_ONLY_GLASS);
        registerBlockItem("vanilla_player_only_glass", new BlockItem(VANILLA_PLAYER_ONLY_GLASS, fabricItemSettings));
        registerBlock("amethyst_player_only_glass", AMETHYST_PLAYER_ONLY_GLASS);
        registerBlockItem("amethyst_player_only_glass", new BlockItem(AMETHYST_PLAYER_ONLY_GLASS, fabricItemSettings));
        registerBlock("topaz_player_only_glass", TOPAZ_PLAYER_ONLY_GLASS);
        registerBlockItem("topaz_player_only_glass", new BlockItem(TOPAZ_PLAYER_ONLY_GLASS, fabricItemSettings));
        registerBlock("citrine_player_only_glass", CITRINE_PLAYER_ONLY_GLASS);
        registerBlockItem("citrine_player_only_glass", new BlockItem(CITRINE_PLAYER_ONLY_GLASS, fabricItemSettings));
        registerBlock("onyx_player_only_glass", ONYX_PLAYER_ONLY_GLASS);
        registerBlockItem("onyx_player_only_glass", new BlockItem(ONYX_PLAYER_ONLY_GLASS, fabricItemSettings));
        registerBlock("moonstone_player_only_glass", MOONSTONE_PLAYER_ONLY_GLASS);
        registerBlockItem("moonstone_player_only_glass", new BlockItem(MOONSTONE_PLAYER_ONLY_GLASS, fabricItemSettings));
    }

    private static void registerStoneBlocks(FabricItemSettings fabricItemSettings) {
        registerBlock("tuff_slab", TUFF_SLAB);
        registerBlockItem("tuff_slab", new BlockItem(TUFF_SLAB, fabricItemSettings));
        registerBlock("tuff_wall", TUFF_WALL);
        registerBlockItem("tuff_wall", new BlockItem(TUFF_WALL, fabricItemSettings));
        registerBlock("tuff_stairs", TUFF_STAIRS);
        registerBlockItem("tuff_stairs", new BlockItem(TUFF_STAIRS, fabricItemSettings));

        registerBlock("calcite_slab", CALCITE_SLAB);
        registerBlockItem("calcite_slab", new BlockItem(CALCITE_SLAB, fabricItemSettings));
        registerBlock("calcite_wall", CALCITE_WALL);
        registerBlockItem("calcite_wall", new BlockItem(CALCITE_WALL, fabricItemSettings));
        registerBlock("calcite_stairs", CALCITE_STAIRS);
        registerBlockItem("calcite_stairs", new BlockItem(CALCITE_STAIRS, fabricItemSettings));

        registerBlock("polished_tuff", POLISHED_TUFF);
        registerBlockItem("polished_tuff", new BlockItem(POLISHED_TUFF, fabricItemSettings));
        registerBlock("polished_tuff_slab", POLISHED_TUFF_SLAB);
        registerBlockItem("polished_tuff_slab", new BlockItem(POLISHED_TUFF_SLAB, fabricItemSettings));
        registerBlock("polished_tuff_wall", POLISHED_TUFF_WALL);
        registerBlockItem("polished_tuff_wall", new BlockItem(POLISHED_TUFF_WALL, fabricItemSettings));
        registerBlock("polished_tuff_stairs", POLISHED_TUFF_STAIRS);
        registerBlockItem("polished_tuff_stairs", new BlockItem(POLISHED_TUFF_STAIRS, fabricItemSettings));
        registerBlock("tuff_bricks", TUFF_BRICKS);
        registerBlockItem("tuff_bricks", new BlockItem(TUFF_BRICKS, fabricItemSettings));
        registerBlock("tuff_brick_slab", TUFF_BRICK_SLAB);
        registerBlockItem("tuff_brick_slab", new BlockItem(TUFF_BRICK_SLAB, fabricItemSettings));
        registerBlock("tuff_brick_wall", TUFF_BRICK_WALL);
        registerBlockItem("tuff_brick_wall", new BlockItem(TUFF_BRICK_WALL, fabricItemSettings));
        registerBlock("tuff_brick_stairs", TUFF_BRICK_STAIRS);
        registerBlockItem("tuff_brick_stairs", new BlockItem(TUFF_BRICK_STAIRS, fabricItemSettings));

        registerBlock("polished_calcite", POLISHED_CALCITE);
        registerBlockItem("polished_calcite", new BlockItem(POLISHED_CALCITE, fabricItemSettings));
        registerBlock("polished_calcite_slab", POLISHED_CALCITE_SLAB);
        registerBlockItem("polished_calcite_slab", new BlockItem(POLISHED_CALCITE_SLAB, fabricItemSettings));
        registerBlock("polished_calcite_wall", POLISHED_CALCITE_WALL);
        registerBlockItem("polished_calcite_wall", new BlockItem(POLISHED_CALCITE_WALL, fabricItemSettings));
        registerBlock("polished_calcite_stairs", POLISHED_CALCITE_STAIRS);
        registerBlockItem("polished_calcite_stairs", new BlockItem(POLISHED_CALCITE_STAIRS, fabricItemSettings));
        registerBlock("calcite_bricks", CALCITE_BRICKS);
        registerBlockItem("calcite_bricks", new BlockItem(CALCITE_BRICKS, fabricItemSettings));
        registerBlock("calcite_brick_slab", CALCITE_BRICK_SLAB);
        registerBlockItem("calcite_brick_slab", new BlockItem(CALCITE_BRICK_SLAB, fabricItemSettings));
        registerBlock("calcite_brick_wall", CALCITE_BRICK_WALL);
        registerBlockItem("calcite_brick_wall", new BlockItem(CALCITE_BRICK_WALL, fabricItemSettings));
        registerBlock("calcite_brick_stairs", CALCITE_BRICK_STAIRS);
        registerBlockItem("calcite_brick_stairs", new BlockItem(CALCITE_BRICK_STAIRS, fabricItemSettings));
    }

    private static void registerRunes(FabricItemSettings fabricItemSettings) {
        registerBlock("amethyst_chiseled_tuff", AMETHYST_CHISELED_TUFF);
        registerBlockItem("amethyst_chiseled_tuff", new BlockItem(AMETHYST_CHISELED_TUFF, fabricItemSettings));
        registerBlock("topaz_chiseled_tuff", TOPAZ_CHISELED_TUFF);
        registerBlockItem("topaz_chiseled_tuff", new BlockItem(TOPAZ_CHISELED_TUFF, fabricItemSettings));
        registerBlock("citrine_chiseled_tuff", CITRINE_CHISELED_TUFF);
        registerBlockItem("citrine_chiseled_tuff", new BlockItem(CITRINE_CHISELED_TUFF, fabricItemSettings));
        registerBlock("onyx_chiseled_tuff", ONYX_CHISELED_TUFF);
        registerBlockItem("onyx_chiseled_tuff", new BlockItem(ONYX_CHISELED_TUFF, fabricItemSettings));
        registerBlock("moonstone_chiseled_tuff", MOONSTONE_CHISELED_TUFF);
        registerBlockItem("moonstone_chiseled_tuff", new BlockItem(MOONSTONE_CHISELED_TUFF, fabricItemSettings));

        registerBlock("amethyst_chiseled_calcite", AMETHYST_CHISELED_CALCITE);
        registerBlockItem("amethyst_chiseled_calcite", new BlockItem(AMETHYST_CHISELED_CALCITE, fabricItemSettings));
        registerBlock("topaz_chiseled_calcite", TOPAZ_CHISELED_CALCITE);
        registerBlockItem("topaz_chiseled_calcite", new BlockItem(TOPAZ_CHISELED_CALCITE, fabricItemSettings));
        registerBlock("citrine_chiseled_calcite", CITRINE_CHISELED_CALCITE);
        registerBlockItem("citrine_chiseled_calcite", new BlockItem(CITRINE_CHISELED_CALCITE, fabricItemSettings));
        registerBlock("onyx_chiseled_calcite", ONYX_CHISELED_CALCITE);
        registerBlockItem("onyx_chiseled_calcite", new BlockItem(ONYX_CHISELED_CALCITE, fabricItemSettings));
        registerBlock("moonstone_chiseled_calcite", MOONSTONE_CHISELED_CALCITE);
        registerBlockItem("moonstone_chiseled_calcite", new BlockItem(MOONSTONE_CHISELED_CALCITE, fabricItemSettings));
    }

    private static void registerStoneLamps(FabricItemSettings fabricItemSettings) {
        registerBlock("amethyst_calcite_lamp", AMETHYST_CALCITE_LAMP);
        registerBlockItem("amethyst_calcite_lamp", new BlockItem(AMETHYST_CALCITE_LAMP, fabricItemSettings));
        registerBlock("topaz_calcite_lamp", TOPAZ_CALCITE_LAMP);
        registerBlockItem("topaz_calcite_lamp", new BlockItem(TOPAZ_CALCITE_LAMP, fabricItemSettings));
        registerBlock("citrine_calcite_lamp", CITRINE_CALCITE_LAMP);
        registerBlockItem("citrine_calcite_lamp", new BlockItem(CITRINE_CALCITE_LAMP, fabricItemSettings));
        registerBlock("onyx_calcite_lamp", ONYX_CALCITE_LAMP);
        registerBlockItem("onyx_calcite_lamp", new BlockItem(ONYX_CALCITE_LAMP, fabricItemSettings));
        registerBlock("moonstone_calcite_lamp", MOONSTONE_CALCITE_LAMP);
        registerBlockItem("moonstone_calcite_lamp", new BlockItem(MOONSTONE_CALCITE_LAMP, fabricItemSettings));
        registerBlock("amethyst_tuff_lamp", AMETHYST_TUFF_LAMP);
        registerBlockItem("amethyst_tuff_lamp", new BlockItem(AMETHYST_TUFF_LAMP, fabricItemSettings));
        registerBlock("topaz_tuff_lamp", TOPAZ_TUFF_LAMP);
        registerBlockItem("topaz_tuff_lamp", new BlockItem(TOPAZ_TUFF_LAMP, fabricItemSettings));
        registerBlock("citrine_tuff_lamp", CITRINE_TUFF_LAMP);
        registerBlockItem("citrine_tuff_lamp", new BlockItem(CITRINE_TUFF_LAMP, fabricItemSettings));
        registerBlock("onyx_tuff_lamp", ONYX_TUFF_LAMP);
        registerBlockItem("onyx_tuff_lamp", new BlockItem(ONYX_TUFF_LAMP, fabricItemSettings));
        registerBlock("moonstone_tuff_lamp", MOONSTONE_TUFF_LAMP);
        registerBlockItem("moonstone_tuff_lamp", new BlockItem(MOONSTONE_TUFF_LAMP, fabricItemSettings));
    }

    private static void registerColoredWood(FabricItemSettings fabricItemSettings) {
        registerBlock("black_log", BLACK_LOG);
        registerBlockItem("black_log", new BlockItem(BLACK_LOG, fabricItemSettings));
        registerBlock("blue_log", BLUE_LOG);
        registerBlockItem("blue_log", new BlockItem(BLUE_LOG, fabricItemSettings));
        registerBlock("brown_log", BROWN_LOG);
        registerBlockItem("brown_log", new BlockItem(BROWN_LOG, fabricItemSettings));
        registerBlock("cyan_log", CYAN_LOG);
        registerBlockItem("cyan_log", new BlockItem(CYAN_LOG, fabricItemSettings));
        registerBlock("gray_log", GRAY_LOG);
        registerBlockItem("gray_log", new BlockItem(GRAY_LOG, fabricItemSettings));
        registerBlock("green_log", GREEN_LOG);
        registerBlockItem("green_log", new BlockItem(GREEN_LOG, fabricItemSettings));
        registerBlock("light_blue_log", LIGHT_BLUE_LOG);
        registerBlockItem("light_blue_log", new BlockItem(LIGHT_BLUE_LOG, fabricItemSettings));
        registerBlock("light_gray_log", LIGHT_GRAY_LOG);
        registerBlockItem("light_gray_log", new BlockItem(LIGHT_GRAY_LOG, fabricItemSettings));
        registerBlock("lime_log", LIME_LOG);
        registerBlockItem("lime_log", new BlockItem(LIME_LOG, fabricItemSettings));
        registerBlock("magenta_log", MAGENTA_LOG);
        registerBlockItem("magenta_log", new BlockItem(MAGENTA_LOG, fabricItemSettings));
        registerBlock("orange_log", ORANGE_LOG);
        registerBlockItem("orange_log", new BlockItem(ORANGE_LOG, fabricItemSettings));
        registerBlock("pink_log", PINK_LOG);
        registerBlockItem("pink_log", new BlockItem(PINK_LOG, fabricItemSettings));
        registerBlock("purple_log", PURPLE_LOG);
        registerBlockItem("purple_log", new BlockItem(PURPLE_LOG, fabricItemSettings));
        registerBlock("red_log", RED_LOG);
        registerBlockItem("red_log", new BlockItem(RED_LOG, fabricItemSettings));
        registerBlock("white_log", WHITE_LOG);
        registerBlockItem("white_log", new BlockItem(WHITE_LOG, fabricItemSettings));
        registerBlock("yellow_log", YELLOW_LOG);
        registerBlockItem("yellow_log", new BlockItem(YELLOW_LOG, fabricItemSettings));
        registerBlock("black_leaves", BLACK_LEAVES);
        registerBlockItem("black_leaves", new BlockItem(BLACK_LEAVES, fabricItemSettings));
        registerBlock("blue_leaves", BLUE_LEAVES);
        registerBlockItem("blue_leaves", new BlockItem(BLUE_LEAVES, fabricItemSettings));
        registerBlock("brown_leaves", BROWN_LEAVES);
        registerBlockItem("brown_leaves", new BlockItem(BROWN_LEAVES, fabricItemSettings));
        registerBlock("cyan_leaves", CYAN_LEAVES);
        registerBlockItem("cyan_leaves", new BlockItem(CYAN_LEAVES, fabricItemSettings));
        registerBlock("gray_leaves", GRAY_LEAVES);
        registerBlockItem("gray_leaves", new BlockItem(GRAY_LEAVES, fabricItemSettings));
        registerBlock("green_leaves", GREEN_LEAVES);
        registerBlockItem("green_leaves", new BlockItem(GREEN_LEAVES, fabricItemSettings));
        registerBlock("light_blue_leaves", LIGHT_BLUE_LEAVES);
        registerBlockItem("light_blue_leaves", new BlockItem(LIGHT_BLUE_LEAVES, fabricItemSettings));
        registerBlock("light_gray_leaves", LIGHT_GRAY_LEAVES);
        registerBlockItem("light_gray_leaves", new BlockItem(LIGHT_GRAY_LEAVES, fabricItemSettings));
        registerBlock("lime_leaves", LIME_LEAVES);
        registerBlockItem("lime_leaves", new BlockItem(LIME_LEAVES, fabricItemSettings));
        registerBlock("magenta_leaves", MAGENTA_LEAVES);
        registerBlockItem("magenta_leaves", new BlockItem(MAGENTA_LEAVES, fabricItemSettings));
        registerBlock("orange_leaves", ORANGE_LEAVES);
        registerBlockItem("orange_leaves", new BlockItem(ORANGE_LEAVES, fabricItemSettings));
        registerBlock("pink_leaves", PINK_LEAVES);
        registerBlockItem("pink_leaves", new BlockItem(PINK_LEAVES, fabricItemSettings));
        registerBlock("purple_leaves", PURPLE_LEAVES);
        registerBlockItem("purple_leaves", new BlockItem(PURPLE_LEAVES, fabricItemSettings));
        registerBlock("red_leaves", RED_LEAVES);
        registerBlockItem("red_leaves", new BlockItem(RED_LEAVES, fabricItemSettings));
        registerBlock("white_leaves", WHITE_LEAVES);
        registerBlockItem("white_leaves", new BlockItem(WHITE_LEAVES, fabricItemSettings));
        registerBlock("yellow_leaves", YELLOW_LEAVES);
        registerBlockItem("yellow_leaves", new BlockItem(YELLOW_LEAVES, fabricItemSettings));
        registerBlock("black_sapling", BLACK_SAPLING);
        registerBlockItem("black_sapling", new BlockItem(BLACK_SAPLING, fabricItemSettings));
        registerBlock("blue_sapling", BLUE_SAPLING);
        registerBlockItem("blue_sapling", new BlockItem(BLUE_SAPLING, fabricItemSettings));
        registerBlock("brown_sapling", BROWN_SAPLING);
        registerBlockItem("brown_sapling", new BlockItem(BROWN_SAPLING, fabricItemSettings));
        registerBlock("cyan_sapling", CYAN_SAPLING);
        registerBlockItem("cyan_sapling", new BlockItem(CYAN_SAPLING, fabricItemSettings));
        registerBlock("gray_sapling", GRAY_SAPLING);
        registerBlockItem("gray_sapling", new BlockItem(GRAY_SAPLING, fabricItemSettings));
        registerBlock("green_sapling", GREEN_SAPLING);
        registerBlockItem("green_sapling", new BlockItem(GREEN_SAPLING, fabricItemSettings));
        registerBlock("light_blue_sapling", LIGHT_BLUE_SAPLING);
        registerBlockItem("light_blue_sapling", new BlockItem(LIGHT_BLUE_SAPLING, fabricItemSettings));
        registerBlock("light_gray_sapling", LIGHT_GRAY_SAPLING);
        registerBlockItem("light_gray_sapling", new BlockItem(LIGHT_GRAY_SAPLING, fabricItemSettings));
        registerBlock("lime_sapling", LIME_SAPLING);
        registerBlockItem("lime_sapling", new BlockItem(LIME_SAPLING, fabricItemSettings));
        registerBlock("magenta_sapling", MAGENTA_SAPLING);
        registerBlockItem("magenta_sapling", new BlockItem(MAGENTA_SAPLING, fabricItemSettings));
        registerBlock("orange_sapling", ORANGE_SAPLING);
        registerBlockItem("orange_sapling", new BlockItem(ORANGE_SAPLING, fabricItemSettings));
        registerBlock("pink_sapling", PINK_SAPLING);
        registerBlockItem("pink_sapling", new BlockItem(PINK_SAPLING, fabricItemSettings));
        registerBlock("purple_sapling", PURPLE_SAPLING);
        registerBlockItem("purple_sapling", new BlockItem(PURPLE_SAPLING, fabricItemSettings));
        registerBlock("red_sapling", RED_SAPLING);
        registerBlockItem("red_sapling", new BlockItem(RED_SAPLING, fabricItemSettings));
        registerBlock("white_sapling", WHITE_SAPLING);
        registerBlockItem("white_sapling", new BlockItem(WHITE_SAPLING, fabricItemSettings));
        registerBlock("yellow_sapling", YELLOW_SAPLING);
        registerBlockItem("yellow_sapling", new BlockItem(YELLOW_SAPLING, fabricItemSettings));
        registerBlock("black_planks", BLACK_PLANKS);
        registerBlockItem("black_planks", new BlockItem(BLACK_PLANKS, fabricItemSettings));
        registerBlock("blue_planks", BLUE_PLANKS);
        registerBlockItem("blue_planks", new BlockItem(BLUE_PLANKS, fabricItemSettings));
        registerBlock("brown_planks", BROWN_PLANKS);
        registerBlockItem("brown_planks", new BlockItem(BROWN_PLANKS, fabricItemSettings));
        registerBlock("cyan_planks", CYAN_PLANKS);
        registerBlockItem("cyan_planks", new BlockItem(CYAN_PLANKS, fabricItemSettings));
        registerBlock("gray_planks", GRAY_PLANKS);
        registerBlockItem("gray_planks", new BlockItem(GRAY_PLANKS, fabricItemSettings));
        registerBlock("green_planks", GREEN_PLANKS);
        registerBlockItem("green_planks", new BlockItem(GREEN_PLANKS, fabricItemSettings));
        registerBlock("light_blue_planks", LIGHT_BLUE_PLANKS);
        registerBlockItem("light_blue_planks", new BlockItem(LIGHT_BLUE_PLANKS, fabricItemSettings));
        registerBlock("light_gray_planks", LIGHT_GRAY_PLANKS);
        registerBlockItem("light_gray_planks", new BlockItem(LIGHT_GRAY_PLANKS, fabricItemSettings));
        registerBlock("lime_planks", LIME_PLANKS);
        registerBlockItem("lime_planks", new BlockItem(LIME_PLANKS, fabricItemSettings));
        registerBlock("magenta_planks", MAGENTA_PLANKS);
        registerBlockItem("magenta_planks", new BlockItem(MAGENTA_PLANKS, fabricItemSettings));
        registerBlock("orange_planks", ORANGE_PLANKS);
        registerBlockItem("orange_planks", new BlockItem(ORANGE_PLANKS, fabricItemSettings));
        registerBlock("pink_planks", PINK_PLANKS);
        registerBlockItem("pink_planks", new BlockItem(PINK_PLANKS, fabricItemSettings));
        registerBlock("purple_planks", PURPLE_PLANKS);
        registerBlockItem("purple_planks", new BlockItem(PURPLE_PLANKS, fabricItemSettings));
        registerBlock("red_planks", RED_PLANKS);
        registerBlockItem("red_planks", new BlockItem(RED_PLANKS, fabricItemSettings));
        registerBlock("white_planks", WHITE_PLANKS);
        registerBlockItem("white_planks", new BlockItem(WHITE_PLANKS, fabricItemSettings));
        registerBlock("yellow_planks", YELLOW_PLANKS);
        registerBlockItem("yellow_planks", new BlockItem(YELLOW_PLANKS, fabricItemSettings));
        registerBlock("black_plank_stairs", BLACK_STAIRS);
        registerBlockItem("black_plank_stairs", new BlockItem(BLACK_STAIRS, fabricItemSettings));
        registerBlock("blue_plank_stairs", BLUE_STAIRS);
        registerBlockItem("blue_plank_stairs", new BlockItem(BLUE_STAIRS, fabricItemSettings));
        registerBlock("brown_plank_stairs", BROWN_STAIRS);
        registerBlockItem("brown_plank_stairs", new BlockItem(BROWN_STAIRS, fabricItemSettings));
        registerBlock("cyan_plank_stairs", CYAN_STAIRS);
        registerBlockItem("cyan_plank_stairs", new BlockItem(CYAN_STAIRS, fabricItemSettings));
        registerBlock("gray_plank_stairs", GRAY_STAIRS);
        registerBlockItem("gray_plank_stairs", new BlockItem(GRAY_STAIRS, fabricItemSettings));
        registerBlock("green_plank_stairs", GREEN_STAIRS);
        registerBlockItem("green_plank_stairs", new BlockItem(GREEN_STAIRS, fabricItemSettings));
        registerBlock("light_blue_plank_stairs", LIGHT_BLUE_STAIRS);
        registerBlockItem("light_blue_plank_stairs", new BlockItem(LIGHT_BLUE_STAIRS, fabricItemSettings));
        registerBlock("light_gray_plank_stairs", LIGHT_GRAY_STAIRS);
        registerBlockItem("light_gray_plank_stairs", new BlockItem(LIGHT_GRAY_STAIRS, fabricItemSettings));
        registerBlock("lime_plank_stairs", LIME_STAIRS);
        registerBlockItem("lime_plank_stairs", new BlockItem(LIME_STAIRS, fabricItemSettings));
        registerBlock("magenta_plank_stairs", MAGENTA_STAIRS);
        registerBlockItem("magenta_plank_stairs", new BlockItem(MAGENTA_STAIRS, fabricItemSettings));
        registerBlock("orange_plank_stairs", ORANGE_STAIRS);
        registerBlockItem("orange_plank_stairs", new BlockItem(ORANGE_STAIRS, fabricItemSettings));
        registerBlock("pink_plank_stairs", PINK_STAIRS);
        registerBlockItem("pink_plank_stairs", new BlockItem(PINK_STAIRS, fabricItemSettings));
        registerBlock("purple_plank_stairs", PURPLE_STAIRS);
        registerBlockItem("purple_plank_stairs", new BlockItem(PURPLE_STAIRS, fabricItemSettings));
        registerBlock("red_plank_stairs", RED_STAIRS);
        registerBlockItem("red_plank_stairs", new BlockItem(RED_STAIRS, fabricItemSettings));
        registerBlock("white_plank_stairs", WHITE_STAIRS);
        registerBlockItem("white_plank_stairs", new BlockItem(WHITE_STAIRS, fabricItemSettings));
        registerBlock("yellow_plank_stairs", YELLOW_STAIRS);
        registerBlockItem("yellow_plank_stairs", new BlockItem(YELLOW_STAIRS, fabricItemSettings));
        registerBlock("black_plank_pressure_plate", BLACK_PRESSURE_PLATE);
        registerBlockItem("black_plank_pressure_plate", new BlockItem(BLACK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("blue_plank_pressure_plate", BLUE_PRESSURE_PLATE);
        registerBlockItem("blue_plank_pressure_plate", new BlockItem(BLUE_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("brown_plank_pressure_plate", BROWN_PRESSURE_PLATE);
        registerBlockItem("brown_plank_pressure_plate", new BlockItem(BROWN_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("cyan_plank_pressure_plate", CYAN_PRESSURE_PLATE);
        registerBlockItem("cyan_plank_pressure_plate", new BlockItem(CYAN_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("gray_plank_pressure_plate", GRAY_PRESSURE_PLATE);
        registerBlockItem("gray_plank_pressure_plate", new BlockItem(GRAY_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("green_plank_pressure_plate", GREEN_PRESSURE_PLATE);
        registerBlockItem("green_plank_pressure_plate", new BlockItem(GREEN_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("light_blue_plank_pressure_plate", LIGHT_BLUE_PRESSURE_PLATE);
        registerBlockItem("light_blue_plank_pressure_plate", new BlockItem(LIGHT_BLUE_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("light_gray_plank_pressure_plate", LIGHT_GRAY_PRESSURE_PLATE);
        registerBlockItem("light_gray_plank_pressure_plate", new BlockItem(LIGHT_GRAY_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("lime_plank_pressure_plate", LIME_PRESSURE_PLATE);
        registerBlockItem("lime_plank_pressure_plate", new BlockItem(LIME_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("magenta_plank_pressure_plate", MAGENTA_PRESSURE_PLATE);
        registerBlockItem("magenta_plank_pressure_plate", new BlockItem(MAGENTA_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("orange_plank_pressure_plate", ORANGE_PRESSURE_PLATE);
        registerBlockItem("orange_plank_pressure_plate", new BlockItem(ORANGE_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("pink_plank_pressure_plate", PINK_PRESSURE_PLATE);
        registerBlockItem("pink_plank_pressure_plate", new BlockItem(PINK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("purple_plank_pressure_plate", PURPLE_PRESSURE_PLATE);
        registerBlockItem("purple_plank_pressure_plate", new BlockItem(PURPLE_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("red_plank_pressure_plate", RED_PRESSURE_PLATE);
        registerBlockItem("red_plank_pressure_plate", new BlockItem(RED_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("white_plank_pressure_plate", WHITE_PRESSURE_PLATE);
        registerBlockItem("white_plank_pressure_plate", new BlockItem(WHITE_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("yellow_plank_pressure_plate", YELLOW_PRESSURE_PLATE);
        registerBlockItem("yellow_plank_pressure_plate", new BlockItem(YELLOW_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("black_plank_fence", BLACK_FENCE);
        registerBlockItem("black_plank_fence", new BlockItem(BLACK_FENCE, fabricItemSettings));
        registerBlock("blue_plank_fence", BLUE_FENCE);
        registerBlockItem("blue_plank_fence", new BlockItem(BLUE_FENCE, fabricItemSettings));
        registerBlock("brown_plank_fence", BROWN_FENCE);
        registerBlockItem("brown_plank_fence", new BlockItem(BROWN_FENCE, fabricItemSettings));
        registerBlock("cyan_plank_fence", CYAN_FENCE);
        registerBlockItem("cyan_plank_fence", new BlockItem(CYAN_FENCE, fabricItemSettings));
        registerBlock("gray_plank_fence", GRAY_FENCE);
        registerBlockItem("gray_plank_fence", new BlockItem(GRAY_FENCE, fabricItemSettings));
        registerBlock("green_plank_fence", GREEN_FENCE);
        registerBlockItem("green_plank_fence", new BlockItem(GREEN_FENCE, fabricItemSettings));
        registerBlock("light_blue_plank_fence", LIGHT_BLUE_FENCE);
        registerBlockItem("light_blue_plank_fence", new BlockItem(LIGHT_BLUE_FENCE, fabricItemSettings));
        registerBlock("light_gray_plank_fence", LIGHT_GRAY_FENCE);
        registerBlockItem("light_gray_plank_fence", new BlockItem(LIGHT_GRAY_FENCE, fabricItemSettings));
        registerBlock("lime_plank_fence", LIME_FENCE);
        registerBlockItem("lime_plank_fence", new BlockItem(LIME_FENCE, fabricItemSettings));
        registerBlock("magenta_plank_fence", MAGENTA_FENCE);
        registerBlockItem("magenta_plank_fence", new BlockItem(MAGENTA_FENCE, fabricItemSettings));
        registerBlock("orange_plank_fence", ORANGE_FENCE);
        registerBlockItem("orange_plank_fence", new BlockItem(ORANGE_FENCE, fabricItemSettings));
        registerBlock("pink_plank_fence", PINK_FENCE);
        registerBlockItem("pink_plank_fence", new BlockItem(PINK_FENCE, fabricItemSettings));
        registerBlock("purple_plank_fence", PURPLE_FENCE);
        registerBlockItem("purple_plank_fence", new BlockItem(PURPLE_FENCE, fabricItemSettings));
        registerBlock("red_plank_fence", RED_FENCE);
        registerBlockItem("red_plank_fence", new BlockItem(RED_FENCE, fabricItemSettings));
        registerBlock("white_plank_fence", WHITE_FENCE);
        registerBlockItem("white_plank_fence", new BlockItem(WHITE_FENCE, fabricItemSettings));
        registerBlock("yellow_plank_fence", YELLOW_FENCE);
        registerBlockItem("yellow_plank_fence", new BlockItem(YELLOW_FENCE, fabricItemSettings));
        registerBlock("black_plank_fence_gate", BLACK_FENCE_GATE);
        registerBlockItem("black_plank_fence_gate", new BlockItem(BLACK_FENCE_GATE, fabricItemSettings));
        registerBlock("blue_plank_fence_gate", BLUE_FENCE_GATE);
        registerBlockItem("blue_plank_fence_gate", new BlockItem(BLUE_FENCE_GATE, fabricItemSettings));
        registerBlock("brown_plank_fence_gate", BROWN_FENCE_GATE);
        registerBlockItem("brown_plank_fence_gate", new BlockItem(BROWN_FENCE_GATE, fabricItemSettings));
        registerBlock("cyan_plank_fence_gate", CYAN_FENCE_GATE);
        registerBlockItem("cyan_plank_fence_gate", new BlockItem(CYAN_FENCE_GATE, fabricItemSettings));
        registerBlock("gray_plank_fence_gate", GRAY_FENCE_GATE);
        registerBlockItem("gray_plank_fence_gate", new BlockItem(GRAY_FENCE_GATE, fabricItemSettings));
        registerBlock("green_plank_fence_gate", GREEN_FENCE_GATE);
        registerBlockItem("green_plank_fence_gate", new BlockItem(GREEN_FENCE_GATE, fabricItemSettings));
        registerBlock("light_blue_plank_fence_gate", LIGHT_BLUE_FENCE_GATE);
        registerBlockItem("light_blue_plank_fence_gate", new BlockItem(LIGHT_BLUE_FENCE_GATE, fabricItemSettings));
        registerBlock("light_gray_plank_fence_gate", LIGHT_GRAY_FENCE_GATE);
        registerBlockItem("light_gray_plank_fence_gate", new BlockItem(LIGHT_GRAY_FENCE_GATE, fabricItemSettings));
        registerBlock("lime_plank_fence_gate", LIME_FENCE_GATE);
        registerBlockItem("lime_plank_fence_gate", new BlockItem(LIME_FENCE_GATE, fabricItemSettings));
        registerBlock("magenta_plank_fence_gate", MAGENTA_FENCE_GATE);
        registerBlockItem("magenta_plank_fence_gate", new BlockItem(MAGENTA_FENCE_GATE, fabricItemSettings));
        registerBlock("orange_plank_fence_gate", ORANGE_FENCE_GATE);
        registerBlockItem("orange_plank_fence_gate", new BlockItem(ORANGE_FENCE_GATE, fabricItemSettings));
        registerBlock("pink_plank_fence_gate", PINK_FENCE_GATE);
        registerBlockItem("pink_plank_fence_gate", new BlockItem(PINK_FENCE_GATE, fabricItemSettings));
        registerBlock("purple_plank_fence_gate", PURPLE_FENCE_GATE);
        registerBlockItem("purple_plank_fence_gate", new BlockItem(PURPLE_FENCE_GATE, fabricItemSettings));
        registerBlock("red_plank_fence_gate", RED_FENCE_GATE);
        registerBlockItem("red_plank_fence_gate", new BlockItem(RED_FENCE_GATE, fabricItemSettings));
        registerBlock("white_plank_fence_gate", WHITE_FENCE_GATE);
        registerBlockItem("white_plank_fence_gate", new BlockItem(WHITE_FENCE_GATE, fabricItemSettings));
        registerBlock("yellow_plank_fence_gate", YELLOW_FENCE_GATE);
        registerBlockItem("yellow_plank_fence_gate", new BlockItem(YELLOW_FENCE_GATE, fabricItemSettings));
        registerBlock("black_plank_button", BLACK_BUTTON);
        registerBlockItem("black_plank_button", new BlockItem(BLACK_BUTTON, fabricItemSettings));
        registerBlock("blue_plank_button", BLUE_BUTTON);
        registerBlockItem("blue_plank_button", new BlockItem(BLUE_BUTTON, fabricItemSettings));
        registerBlock("brown_plank_button", BROWN_BUTTON);
        registerBlockItem("brown_plank_button", new BlockItem(BROWN_BUTTON, fabricItemSettings));
        registerBlock("cyan_plank_button", CYAN_BUTTON);
        registerBlockItem("cyan_plank_button", new BlockItem(CYAN_BUTTON, fabricItemSettings));
        registerBlock("gray_plank_button", GRAY_BUTTON);
        registerBlockItem("gray_plank_button", new BlockItem(GRAY_BUTTON, fabricItemSettings));
        registerBlock("green_plank_button", GREEN_BUTTON);
        registerBlockItem("green_plank_button", new BlockItem(GREEN_BUTTON, fabricItemSettings));
        registerBlock("light_blue_plank_button", LIGHT_BLUE_BUTTON);
        registerBlockItem("light_blue_plank_button", new BlockItem(LIGHT_BLUE_BUTTON, fabricItemSettings));
        registerBlock("light_gray_plank_button", LIGHT_GRAY_BUTTON);
        registerBlockItem("light_gray_plank_button", new BlockItem(LIGHT_GRAY_BUTTON, fabricItemSettings));
        registerBlock("lime_plank_button", LIME_BUTTON);
        registerBlockItem("lime_plank_button", new BlockItem(LIME_BUTTON, fabricItemSettings));
        registerBlock("magenta_plank_button", MAGENTA_BUTTON);
        registerBlockItem("magenta_plank_button", new BlockItem(MAGENTA_BUTTON, fabricItemSettings));
        registerBlock("orange_plank_button", ORANGE_BUTTON);
        registerBlockItem("orange_plank_button", new BlockItem(ORANGE_BUTTON, fabricItemSettings));
        registerBlock("pink_plank_button", PINK_BUTTON);
        registerBlockItem("pink_plank_button", new BlockItem(PINK_BUTTON, fabricItemSettings));
        registerBlock("purple_plank_button", PURPLE_BUTTON);
        registerBlockItem("purple_plank_button", new BlockItem(PURPLE_BUTTON, fabricItemSettings));
        registerBlock("red_plank_button", RED_BUTTON);
        registerBlockItem("red_plank_button", new BlockItem(RED_BUTTON, fabricItemSettings));
        registerBlock("white_plank_button", WHITE_BUTTON);
        registerBlockItem("white_plank_button", new BlockItem(WHITE_BUTTON, fabricItemSettings));
        registerBlock("yellow_plank_button", YELLOW_BUTTON);
        registerBlockItem("yellow_plank_button", new BlockItem(YELLOW_BUTTON, fabricItemSettings));
        registerBlock("black_plank_slab", BLACK_SLAB);
        registerBlockItem("black_plank_slab", new BlockItem(BLACK_SLAB, fabricItemSettings));
        registerBlock("blue_plank_slab", BLUE_SLAB);
        registerBlockItem("blue_plank_slab", new BlockItem(BLUE_SLAB, fabricItemSettings));
        registerBlock("brown_plank_slab", BROWN_SLAB);
        registerBlockItem("brown_plank_slab", new BlockItem(BROWN_SLAB, fabricItemSettings));
        registerBlock("cyan_plank_slab", CYAN_SLAB);
        registerBlockItem("cyan_plank_slab", new BlockItem(CYAN_SLAB, fabricItemSettings));
        registerBlock("gray_plank_slab", GRAY_SLAB);
        registerBlockItem("gray_plank_slab", new BlockItem(GRAY_SLAB, fabricItemSettings));
        registerBlock("green_plank_slab", GREEN_SLAB);
        registerBlockItem("green_plank_slab", new BlockItem(GREEN_SLAB, fabricItemSettings));
        registerBlock("light_blue_plank_slab", LIGHT_BLUE_SLAB);
        registerBlockItem("light_blue_plank_slab", new BlockItem(LIGHT_BLUE_SLAB, fabricItemSettings));
        registerBlock("light_gray_plank_slab", LIGHT_GRAY_SLAB);
        registerBlockItem("light_gray_plank_slab", new BlockItem(LIGHT_GRAY_SLAB, fabricItemSettings));
        registerBlock("lime_plank_slab", LIME_SLAB);
        registerBlockItem("lime_plank_slab", new BlockItem(LIME_SLAB, fabricItemSettings));
        registerBlock("magenta_plank_slab", MAGENTA_SLAB);
        registerBlockItem("magenta_plank_slab", new BlockItem(MAGENTA_SLAB, fabricItemSettings));
        registerBlock("orange_plank_slab", ORANGE_SLAB);
        registerBlockItem("orange_plank_slab", new BlockItem(ORANGE_SLAB, fabricItemSettings));
        registerBlock("pink_plank_slab", PINK_SLAB);
        registerBlockItem("pink_plank_slab", new BlockItem(PINK_SLAB, fabricItemSettings));
        registerBlock("purple_plank_slab", PURPLE_SLAB);
        registerBlockItem("purple_plank_slab", new BlockItem(PURPLE_SLAB, fabricItemSettings));
        registerBlock("red_plank_slab", RED_SLAB);
        registerBlockItem("red_plank_slab", new BlockItem(RED_SLAB, fabricItemSettings));
        registerBlock("white_plank_slab", WHITE_SLAB);
        registerBlockItem("white_plank_slab", new BlockItem(WHITE_SLAB, fabricItemSettings));
        registerBlock("yellow_plank_slab", YELLOW_SLAB);
        registerBlockItem("yellow_plank_slab", new BlockItem(YELLOW_SLAB, fabricItemSettings));
    }

    private static void registerFlatColoredBlocks(FabricItemSettings fabricItemSettings) {
        registerBlock("black_flat_colored_block", BLACK_FLAT_COLORED_BLOCK);
        registerBlockItem("black_flat_colored_block", new BlockItem(BLACK_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("blue_flat_colored_block", BLUE_FLAT_COLORED_BLOCK);
        registerBlockItem("blue_flat_colored_block", new BlockItem(BLUE_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("brown_flat_colored_block", BROWN_FLAT_COLORED_BLOCK);
        registerBlockItem("brown_flat_colored_block", new BlockItem(BROWN_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("cyan_flat_colored_block", CYAN_FLAT_COLORED_BLOCK);
        registerBlockItem("cyan_flat_colored_block", new BlockItem(CYAN_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("gray_flat_colored_block", GRAY_FLAT_COLORED_BLOCK);
        registerBlockItem("gray_flat_colored_block", new BlockItem(GRAY_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("green_flat_colored_block", GREEN_FLAT_COLORED_BLOCK);
        registerBlockItem("green_flat_colored_block", new BlockItem(GREEN_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("light_blue_flat_colored_block", LIGHT_BLUE_FLAT_COLORED_BLOCK);
        registerBlockItem("light_blue_flat_colored_block", new BlockItem(LIGHT_BLUE_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("light_gray_flat_colored_block", LIGHT_GRAY_FLAT_COLORED_BLOCK);
        registerBlockItem("light_gray_flat_colored_block", new BlockItem(LIGHT_GRAY_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("lime_flat_colored_block", LIME_FLAT_COLORED_BLOCK);
        registerBlockItem("lime_flat_colored_block", new BlockItem(LIME_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("magenta_flat_colored_block", MAGENTA_FLAT_COLORED_BLOCK);
        registerBlockItem("magenta_flat_colored_block", new BlockItem(MAGENTA_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("orange_flat_colored_block", ORANGE_FLAT_COLORED_BLOCK);
        registerBlockItem("orange_flat_colored_block", new BlockItem(ORANGE_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("pink_flat_colored_block", PINK_FLAT_COLORED_BLOCK);
        registerBlockItem("pink_flat_colored_block", new BlockItem(PINK_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("purple_flat_colored_block", PURPLE_FLAT_COLORED_BLOCK);
        registerBlockItem("purple_flat_colored_block", new BlockItem(PURPLE_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("red_flat_colored_block", RED_FLAT_COLORED_BLOCK);
        registerBlockItem("red_flat_colored_block", new BlockItem(RED_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("white_flat_colored_block", WHITE_FLAT_COLORED_BLOCK);
        registerBlockItem("white_flat_colored_block", new BlockItem(WHITE_FLAT_COLORED_BLOCK, fabricItemSettings));
        registerBlock("yellow_flat_colored_block", YELLOW_FLAT_COLORED_BLOCK);
        registerBlockItem("yellow_flat_colored_block", new BlockItem(YELLOW_FLAT_COLORED_BLOCK, fabricItemSettings));
    }

    private static void registerGemBlocks(FabricItemSettings fabricItemSettings) {
        registerBlock("small_citrine_bud", SMALL_CITRINE_BUD);
        registerBlockItem("small_citrine_bud", new BlockItem(SMALL_CITRINE_BUD, fabricItemSettings));
        registerBlock("medium_citrine_bud", MEDIUM_CITRINE_BUD);
        registerBlockItem("medium_citrine_bud", new BlockItem(MEDIUM_CITRINE_BUD, fabricItemSettings));
        registerBlock("large_citrine_bud", LARGE_CITRINE_BUD);
        registerBlockItem("large_citrine_bud", new BlockItem(LARGE_CITRINE_BUD, fabricItemSettings));
        registerBlock("citrine_cluster", CITRINE_CLUSTER);
        registerBlockItem("citrine_cluster", new BlockItem(CITRINE_CLUSTER, fabricItemSettings));
        registerBlock("citrine_block", CITRINE_BLOCK);
        registerBlockItem("citrine_block", new BlockItem(CITRINE_BLOCK, fabricItemSettings));
        registerBlock("budding_citrine", BUDDING_CITRINE);
        registerBlockItem("budding_citrine", new BlockItem(BUDDING_CITRINE, fabricItemSettings));
        
        registerBlock("small_topaz_bud", SMALL_TOPAZ_BUD);
        registerBlockItem("small_topaz_bud", new BlockItem(SMALL_TOPAZ_BUD, fabricItemSettings));
        registerBlock("medium_topaz_bud", MEDIUM_TOPAZ_BUD);
        registerBlockItem("medium_topaz_bud", new BlockItem(MEDIUM_TOPAZ_BUD, fabricItemSettings));
        registerBlock("large_topaz_bud", LARGE_TOPAZ_BUD);
        registerBlockItem("large_topaz_bud", new BlockItem(LARGE_TOPAZ_BUD, fabricItemSettings));
        registerBlock("topaz_cluster", TOPAZ_CLUSTER);
        registerBlockItem("topaz_cluster", new BlockItem(TOPAZ_CLUSTER, fabricItemSettings));
        registerBlock("topaz_block", TOPAZ_BLOCK);
        registerBlockItem("topaz_block", new BlockItem(TOPAZ_BLOCK, fabricItemSettings));
        registerBlock("budding_topaz", BUDDING_TOPAZ);
        registerBlockItem("budding_topaz", new BlockItem(BUDDING_TOPAZ, fabricItemSettings));

        registerBlock("small_onyx_bud", SMALL_ONYX_BUD);
        registerBlockItem("small_onyx_bud", new BlockItem(SMALL_ONYX_BUD, fabricItemSettings));
        registerBlock("medium_onyx_bud", MEDIUM_ONYX_BUD);
        registerBlockItem("medium_onyx_bud", new BlockItem(MEDIUM_ONYX_BUD, fabricItemSettings));
        registerBlock("large_onyx_bud", LARGE_ONYX_BUD);
        registerBlockItem("large_onyx_bud", new BlockItem(LARGE_ONYX_BUD, fabricItemSettings));
        registerBlock("onyx_cluster", ONYX_CLUSTER);
        registerBlockItem("onyx_cluster", new BlockItem(ONYX_CLUSTER, fabricItemSettings));
        registerBlock("onyx_block", ONYX_BLOCK);
        registerBlockItem("onyx_block", new BlockItem(ONYX_BLOCK, fabricItemSettings));
        registerBlock("budding_onyx", BUDDING_ONYX);
        registerBlockItem("budding_onyx", new BlockItem(BUDDING_ONYX, fabricItemSettings));

        registerBlock("small_moonstone_bud", SMALL_MOONSTONE_BUD);
        registerBlockItem("small_moonstone_bud", new BlockItem(SMALL_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD);
        registerBlockItem("medium_moonstone_bud", new BlockItem(MEDIUM_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("large_moonstone_bud", LARGE_MOONSTONE_BUD);
        registerBlockItem("large_moonstone_bud", new BlockItem(LARGE_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("moonstone_cluster", MOONSTONE_CLUSTER);
        registerBlockItem("moonstone_cluster", new BlockItem(MOONSTONE_CLUSTER, fabricItemSettings));
        registerBlock("moonstone_block", MOONSTONE_BLOCK);
        registerBlockItem("moonstone_block", new BlockItem(MOONSTONE_BLOCK, fabricItemSettings));
        registerBlock("budding_moonstone", BUDDING_MOONSTONE);
        registerBlockItem("budding_moonstone", new BlockItem(BUDDING_MOONSTONE, fabricItemSettings));

        registerBlock("rainbow_moonstone_block", RAINBOW_MOONSTONE_BLOCK);
        registerBlockItem("rainbow_moonstone_block", new BlockItem(RAINBOW_MOONSTONE_BLOCK, fabricItemSettings));
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
    }

    @NotNull
    public static Block getColoredLog(DyeColor dyeColor) {
        switch (dyeColor) {
            case RED:
                return RED_LOG;
            case BROWN:
                return BROWN_LOG;
            case CYAN:
                return CYAN_LOG;
            case GRAY:
                return GRAY_LOG;
            case GREEN:
                return GREEN_LOG;
            case LIGHT_BLUE:
                return LIGHT_BLUE_LOG;
            case LIGHT_GRAY:
                return LIGHT_GRAY_LOG;
            case BLUE:
                return BLUE_LOG;
            case LIME:
                return LIME_LOG;
            case ORANGE:
                return ORANGE_LOG;
            case PINK:
                return PINK_LOG;
            case PURPLE:
                return PURPLE_LOG;
            case WHITE:
                return WHITE_LOG;
            case YELLOW:
                return YELLOW_LOG;
            case BLACK:
                return BLACK_LOG;
            default:
                return MAGENTA_LOG;
        }
    }

    @NotNull
    public static Block getColoredLeaves(DyeColor dyeColor) {
        switch (dyeColor) {
            case RED:
                return RED_LEAVES;
            case BROWN:
                return BROWN_LEAVES;
            case CYAN:
                return CYAN_LEAVES;
            case GRAY:
                return GRAY_LEAVES;
            case GREEN:
                return GREEN_LEAVES;
            case LIGHT_BLUE:
                return LIGHT_BLUE_LEAVES;
            case LIGHT_GRAY:
                return LIGHT_GRAY_LEAVES;
            case BLUE:
                return BLUE_LEAVES;
            case LIME:
                return LIME_LEAVES;
            case ORANGE:
                return ORANGE_LEAVES;
            case PINK:
                return PINK_LEAVES;
            case PURPLE:
                return PURPLE_LEAVES;
            case WHITE:
                return WHITE_LEAVES;
            case YELLOW:
                return YELLOW_LEAVES;
            case BLACK:
                return BLACK_LEAVES;
            default:
                return MAGENTA_LEAVES;
        }
    }
}
