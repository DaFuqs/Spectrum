package de.dafuqs.pigment.registries;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.*;
import de.dafuqs.pigment.blocks.altar.AltarBlock;
import de.dafuqs.pigment.blocks.detector.*;
import de.dafuqs.pigment.blocks.minerals.PigmentBuddingBlock;
import de.dafuqs.pigment.blocks.minerals.PigmentMineralBlock;
import de.dafuqs.pigment.blocks.spirit_tree.OminousSaplingBlock;
import de.dafuqs.pigment.blocks.spirit_tree.OminousSaplingBlockItem;
import de.dafuqs.pigment.blocks.compactor.CompactorBlock;
import de.dafuqs.pigment.blocks.conditional.*;
import de.dafuqs.pigment.blocks.decay.DecayBlock1;
import de.dafuqs.pigment.blocks.decay.DecayBlock2;
import de.dafuqs.pigment.blocks.decay.DecayBlock3;
import de.dafuqs.pigment.blocks.deeper_down_portal.DeeperDownPortalBlock;
import de.dafuqs.pigment.blocks.ender_dropper.EnderDropperBlock;
import de.dafuqs.pigment.blocks.fluid.LiquidCrystalFluidBlock;
import de.dafuqs.pigment.blocks.fluid.MudFluidBlock;
import de.dafuqs.pigment.blocks.mob_head.PigmentSkullBlock;
import de.dafuqs.pigment.blocks.mob_head.PigmentSkullBlockItem;
import de.dafuqs.pigment.blocks.mob_head.PigmentWallSkullBlock;
import de.dafuqs.pigment.blocks.lava_sponge.LavaSpongeBlock;
import de.dafuqs.pigment.blocks.lava_sponge.WetLavaSpongeBlock;
import de.dafuqs.pigment.blocks.lava_sponge.WetLavaSpongeItem;
import de.dafuqs.pigment.blocks.melon.AttachedGlisteringStemBlock;
import de.dafuqs.pigment.blocks.melon.GlisteringMelonBlock;
import de.dafuqs.pigment.blocks.melon.GlisteringStemBlock;
import de.dafuqs.pigment.blocks.ParticleEmitterBlock;
import de.dafuqs.pigment.blocks.gravity.GravitableBlock;
import de.dafuqs.pigment.blocks.gravity.GravityBlockItem;
import de.dafuqs.pigment.blocks.private_chest.PrivateChestBlock;
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
import net.minecraft.item.*;
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

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
    private static Boolean always(BlockState state, BlockView world, BlockPos pos) {
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

    // TODO: hardness
    public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.CITRINE_CLUSTER).luminance((state) -> 7));
    public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_CITRINE_BUD).luminance((state) -> 5));
    public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_CITRINE_BUD).luminance((state) -> 3));
    public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_CITRINE_BUD).luminance((state) -> 2));
    public static final Block CITRINE_BLOCK = new PigmentMineralBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(PigmentBlockSoundGroups.CITRINE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_CITRINE_BLOCK_HIT, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
    public static final Block BUDDING_CITRINE = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.CITRINE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_HIT, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);

    public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.TOPAZ_CLUSTER).luminance((state) -> 6));
    public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_TOPAZ_BUD).luminance((state) -> 4));
    public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_TOPAZ_BUD).luminance((state) -> 2));
    public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_TOPAZ_BUD).luminance((state) -> 1));
    public static final Block TOPAZ_BLOCK = new PigmentMineralBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(PigmentBlockSoundGroups.TOPAZ_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
    public static final Block BUDDING_TOPAZ = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.TOPAZ_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);

    public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.ONYX_CLUSTER).luminance((state) -> 3));
    public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_ONYX_BUD).luminance((state) -> 2));
    public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_ONYX_BUD).luminance((state) -> 1));
    public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_ONYX_BUD).luminance((state) -> 0));
    public static final Block ONYX_BLOCK = new PigmentMineralBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(PigmentBlockSoundGroups.ONYX_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_ONYX_BLOCK_HIT, PigmentSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
    public static final Block BUDDING_ONYX = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.ONYX_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, PigmentSoundEvents.BLOCK_ONYX_BLOCK_HIT, PigmentSoundEvents.BLOCK_ONYX_BLOCK_CHIME);

    public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(PigmentBlockSoundGroups.MOONSTONE_CLUSTER).luminance((state) -> 14));
    public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.LARGE_MOONSTONE_BUD).luminance((state) -> 10));
    public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.MEDIUM_MOONSTONE_BUD).luminance((state) -> 7));
    public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(PigmentBlockSoundGroups.SMALL_MOONSTONE_BUD).luminance((state) -> 4));
    public static final Block MOONSTONE_BLOCK = new PigmentMineralBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(PigmentBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
    public static final Block BUDDING_MOONSTONE = new PigmentBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(PigmentBlockSoundGroups.MOONSTONE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);

    public static final Block RAINBOW_MOONSTONE_BLOCK = new PigmentMineralBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(PigmentBlockSoundGroups.RAINBOW_MOONSTONE_BLOCK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2), PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_CHIME);

    public static final Block TUFF_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block TUFF_STAIRS = new PigmentStairsBlock(Blocks.TUFF.getDefaultState(), AbstractBlock.Settings.copy(Blocks.TUFF));

    public static final Block POLISHED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CHISELED_POLISHED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_BASALT_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_BASALT_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_BASALT_STAIRS = new PigmentStairsBlock(POLISHED_BASALT.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_BASALT));

    public static final Block BASALT_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block BASALT_BRICK_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block BASALT_BRICK_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block BASALT_BRICK_STAIRS = new PigmentStairsBlock(BASALT_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(BASALT_BRICKS));

    public static final Block AMETHYST_CHISELED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(5));
    public static final Block TOPAZ_CHISELED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(6));
    public static final Block CITRINE_CHISELED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(7));
    public static final Block ONYX_CHISELED_BASALT = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(3));
    public static final Block MOONSTONE_CHISELED_BASALT = new PigmentPillarBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(12));

    public static final Block CALCITE_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_STAIRS = new PigmentStairsBlock(Blocks.CALCITE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CHISELED_POLISHED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block POLISHED_CALCITE_STAIRS = new PigmentStairsBlock(POLISHED_CALCITE.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_CALCITE));

    public static final Block CALCITE_BRICKS = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
    public static final Block CALCITE_BRICK_STAIRS = new PigmentStairsBlock(CALCITE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(CALCITE_BRICKS));

    public static final Block AMETHYST_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(5));
    public static final Block TOPAZ_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(6));
    public static final Block CITRINE_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(7));
    public static final Block ONYX_CHISELED_CALCITE = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(3));
    public static final Block MOONSTONE_CHISELED_CALCITE = new PigmentPillarBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(12));

    // LAMPS
    public static final Block AMETHYST_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block TOPAZ_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block CITRINE_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block ONYX_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block MOONSTONE_CALCITE_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block AMETHYST_BASALT_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block TOPAZ_BASALT_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block CITRINE_BASALT_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block ONYX_BASALT_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());
    public static final Block MOONSTONE_BASALT_LAMP = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f).luminance(15).nonOpaque());

    // GLASS
    public static final Block AMETHYST_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block TOPAZ_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block CITRINE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block ONYX_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block MOONSTONE_GLASS = new GemGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));

    // ALTAR
    private static final FabricBlockSettings altarSettings = FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool().hardness(5.0F).resistance(20.0F);
    public static final Block ALTAR = new AltarBlock(altarSettings);
    public static final Block ALTAR2 = new AltarBlock(altarSettings);
    public static final Block ALTAR3 = new AltarBlock(altarSettings);

    // PLAYER GLASS
    public static final Block VANILLA_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));
    public static final Block AMETHYST_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));
    public static final Block TOPAZ_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));
    public static final Block CITRINE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));
    public static final Block ONYX_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));
    public static final Block MOONSTONE_PLAYER_ONLY_GLASS = new PlayerOnlyGlassBlock(FabricBlockSettings.copy(PigmentBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(PigmentBlocks::never).solidBlock(PigmentBlocks::never).suffocates(PigmentBlocks::never).blockVision(PigmentBlocks::never));

    // MELON
    public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.of(Material.GOURD, MapColor.LIME).strength(1.0F).sounds(BlockSoundGroup.WOOD));
    public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> PigmentItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.of(Material.PLANT).noCollision().nonOpaque().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.STEM));
    public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> PigmentItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.of(Material.PLANT).nonOpaque().noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD));

    // SAPLING
    private static final FabricBlockSettings saplingSettings = FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);
    public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(saplingSettings);

    // DECAY
    private static final FabricBlockSettings decaySettings = FabricBlockSettings.of(PigmentMaterial.DECAY, MapColor.BLACK)
            .ticksRandomly().requiresTool().breakByTool(FabricToolTags.PICKAXES);
    public static final Block DECAY1 = new DecayBlock1(decaySettings.hardness(0.5F).resistance(0.5F), BlockTags.LEAVES, null,1,  1F);
    public static final Block DECAY2 = new DecayBlock2(decaySettings.hardness(20.0F).resistance(50.0F), null, PigmentBlockTags.DECAY2_SAFE, 2,  2.5F);
    public static final Block DECAY3 = new DecayBlock3(decaySettings.hardness(100.0F).resistance(3600000.0F), null, PigmentBlockTags.DECAY3_SAFE, 3, 5F);

    // FLUIDS
    public static final Block LIQUID_CRYSTAL = new LiquidCrystalFluidBlock(PigmentFluids.STILL_LIQUID_CRYSTAL, FabricBlockSettings.copyOf(Blocks.WATER).luminance((state) -> 8));
    public static final Block MUD = new MudFluidBlock(PigmentFluids.STILL_MUD, FabricBlockSettings.of(Material.LAVA).strength(100.0F).suffocates(PigmentBlocks::always).dropsNothing());

    // COLORED TREES
    private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
    private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> { return 5; });
    private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG).luminance((state) -> {
        return 10;
    });

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
    private static final FabricBlockSettings flatColoredBlockBlockSettings = FabricBlockSettings.of(Material.STONE)
            .hardness(2.5F)
            .breakByTool(FabricToolTags.PICKAXES)
            .requiresTool()
            .luminance(1)
            .postProcess(PigmentBlocks::always)
            .emissiveLighting(PigmentBlocks::always);

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

    public static final Block SPARKLESTONE_ORE = new SparklestoneOreBlock(FabricBlockSettings.copyOf(Blocks.STONE), UniformIntProvider.create(2, 4)); // drops sparklestone gems
    public static final Block SPARKLESTONE_BLOCK = new Block(FabricBlockSettings.of(Material.GLASS, MapColor.YELLOW).strength(2.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 15));

    // ORES
    public static final Block AZURITE_ORE = new AzuriteOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE), UniformIntProvider.create(4, 7));
    public static final Block AZURITE_BLOCK = new PigmentFacingBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));

    public static final Block PALETUR_ORE = new PaleturOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(2, 4));
    public static final Block PALETUR_FRAGMENT_BLOCK = new GravitableBlock( FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.02F);
    public static final Block SCARLET_ORE = new ScarletOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(3, 5));
    public static final Block SCARLET_FRAGMENT_BLOCK = new GravitableBlock(FabricBlockSettings.of(Material.METAL, MapColor.DARK_RED).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), -0.2F);

    // FUNCTIONAL BLOCKS
    public static final Block PARTICLE_EMITTER = new ParticleEmitterBlock(FabricBlockSettings.of(Material.METAL));
    public static final Block COMPACTOR = new CompactorBlock(FabricBlockSettings.of(Material.METAL));
    public static final Block BEDROCK_ANVIL = new BedrockAnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).hardness(8));

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
    public static final Block PRIVATE_CHEST = new PrivateChestBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool().hardness(4.0F).resistance(3600000.0F));

    public static final Block LIGHT_LEVEL_DETECTOR = new BlockLightDetectorBlock(FabricBlockSettings.of(Material.STONE).strength(0.2F).sounds(BlockSoundGroup.STONE));
    public static final Block WEATHER_DETECTOR =  new WeatherDetectorBlock(FabricBlockSettings.of(Material.STONE).strength(0.2F).sounds(BlockSoundGroup.STONE));
    public static final Block ITEM_DETECTOR = new ItemDetectorBlock(FabricBlockSettings.of(Material.STONE).strength(0.2F).sounds(BlockSoundGroup.STONE));
    public static final Block PLAYER_DETECTOR = new PlayerDetectorBlock(FabricBlockSettings.of(Material.STONE).strength(0.2F).sounds(BlockSoundGroup.STONE));
    public static final Block ENTITY_DETECTOR = new EntityDetectorBlock(FabricBlockSettings.of(Material.STONE).strength(0.2F).sounds(BlockSoundGroup.STONE));

    public static final Block ENDER_DROPPER = new EnderDropperBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15F, 60.0F));

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


    private static void registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(PigmentCommon.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, BlockItem blockItem) {
        Registry.register(Registry.ITEM, new Identifier(PigmentCommon.MOD_ID, name), blockItem);
    }

    public static void register() {
        registerBlock("altar", ALTAR);
        registerBlockItem("altar", new BlockItem(ALTAR, generalItemSettings));
        registerBlock("altar2", ALTAR2);
        registerBlockItem("altar2", new BlockItem(ALTAR2, generalItemSettings));
        registerBlock("altar3", ALTAR3);
        registerBlockItem("altar3", new BlockItem(ALTAR3, generalItemSettings));

        registerSpiritTree(generalItemSettings);

        registerBlock("deeper_down_portal", DEEPER_DOWN_PORTAL);
        //registerBlockItem("deeper_down_portal", new BlockItem(DEEPER_DOWN_PORTAL, generalItemSettings));

        registerBlock("private_chest", PRIVATE_CHEST);
        registerBlockItem("private_chest", new BlockItem(PRIVATE_CHEST, generalItemSettings));

        registerBlock("light_level_detector", LIGHT_LEVEL_DETECTOR);
        registerBlockItem("light_level_detector", new BlockItem(LIGHT_LEVEL_DETECTOR, generalItemSettings));
        registerBlock("weather_detector", WEATHER_DETECTOR);
        registerBlockItem("weather_detector", new BlockItem(WEATHER_DETECTOR, generalItemSettings));
        registerBlock("item_detector", ITEM_DETECTOR);
        registerBlockItem("item_detector", new BlockItem(ITEM_DETECTOR, generalItemSettings));
        registerBlock("player_detector", PLAYER_DETECTOR);
        registerBlockItem("player_detector", new BlockItem(PLAYER_DETECTOR, generalItemSettings));
        registerBlock("entity_detector", ENTITY_DETECTOR);
        registerBlockItem("entity_detector", new BlockItem(ENTITY_DETECTOR, generalItemSettings));

        registerColoredWood(coloredWoodItemSettings);
        registerGemBlocks(worldgenItemSettings);
        registerOreBlocks();
        registerStoneBlocks(decorationItemSettings);
        registerRunes(decorationItemSettings);
        registerStoneLamps(decorationItemSettings);
        registerColoredLamps(decorationItemSettings);
        registerGemGlass(decorationItemSettings);
        registerPlayerOnlyGlass(generalItemSettings);
        registerFlatColoredBlocks(decorationItemSettings);
        registerMobHeads(mobHeadItemSettings);

        // GLISTERING MELON
        registerBlock("glistering_melon", GLISTERING_MELON);
        registerBlockItem("glistering_melon", new BlockItem(GLISTERING_MELON, generalItemSettings));
        registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
        registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);

        // SAPLING
        registerBlock("ominous_sapling", OMINOUS_SAPLING);
        registerBlockItem("ominous_sapling", new OminousSaplingBlockItem(OMINOUS_SAPLING, generalItemSettings));

        // DECAY
        registerBlock("decay1", DECAY1);
        registerBlock("decay2", DECAY2);
        registerBlock("decay3", DECAY3);

        // FLUIDS + PRODUCTS
        registerBlock("mud", MUD);
        registerBlock("liquid_crystal", LIQUID_CRYSTAL);
        registerBlock("frostbite_crystal", FROSTBITE_CRYSTAL);
        registerBlockItem("frostbite_crystal", new BlockItem(FROSTBITE_CRYSTAL, generalItemSettings));
        registerBlock("blazing_crystal", BLAZING_CRYSTAL);
        registerBlockItem("blazing_crystal", new BlockItem(BLAZING_CRYSTAL, generalItemSettings));
        registerBlock("resonant_lily", RESONANT_LILY);
        registerBlockItem("resonant_lily", new BlockItem(RESONANT_LILY, generalItemSettings));
        registerBlock("quitoxic_reeds", QUITOXIC_REEDS);
        registerBlockItem("quitoxic_reeds", new BlockItem(QUITOXIC_REEDS, worldgenItemSettings));
        registerBlock("mermaids_brush", MERMAIDS_BRUSH);
        registerBlockItem("mermaids_brush", new BlockItem(MERMAIDS_BRUSH, worldgenItemSettings));

        registerBlock("particle_emitter", PARTICLE_EMITTER);
        registerBlockItem("particle_emitter", new BlockItem(PARTICLE_EMITTER, generalItemSettings));

        registerBlock("compactor", COMPACTOR);
        registerBlockItem("compactor", new BlockItem(COMPACTOR, generalItemSettings));
        registerBlock("ender_dropper", ENDER_DROPPER);
        registerBlockItem("ender_dropper", new BlockItem(ENDER_DROPPER, generalItemSettings));
        registerBlock("bedrock_anvil", BEDROCK_ANVIL);
        registerBlockItem("bedrock_anvil", new BlockItem(BEDROCK_ANVIL, generalItemSettings));

        registerBlock("cracked_end_portal_frame", CRACKED_END_PORTAL_FRAME);
        registerBlockItem("cracked_end_portal_frame", new BlockItem(CRACKED_END_PORTAL_FRAME, generalItemSettings));

        registerBlock("ender_treasure", ENDER_TREASURE);
        registerBlockItem("ender_treasure", new BlockItem(ENDER_TREASURE, generalItemSettings));

        registerBlock("lava_sponge", LAVA_SPONGE);
        registerBlockItem("lava_sponge", new BlockItem(LAVA_SPONGE, generalItemSettings));
        registerBlock("wet_lava_sponge", WET_LAVA_SPONGE);
        registerBlockItem("wet_lava_sponge", new WetLavaSpongeItem(WET_LAVA_SPONGE, generalItemSettings));
    }

    private static void registerSpiritTree(FabricItemSettings fabricItemSettings) {
        registerBlock("spirit_sallow_roots", SPIRIT_SALLOW_ROOTS);
        registerBlockItem("spirit_sallow_roots", new BlockItem(SPIRIT_SALLOW_ROOTS, fabricItemSettings));
        registerBlock("spirit_sallow_log", SPIRIT_SALLOW_LOG);
        registerBlockItem("spirit_sallow_log", new BlockItem(SPIRIT_SALLOW_LOG, fabricItemSettings));
        registerBlock("spirit_sallow_bark", SPIRIT_SALLOW_BARK);
        registerBlockItem("spirit_sallow_bark", new BlockItem(SPIRIT_SALLOW_BARK, fabricItemSettings));
        registerBlock("spirit_sallow_core", SPIRIT_SALLOW_CORE);
        registerBlockItem("spirit_sallow_core", new BlockItem(SPIRIT_SALLOW_CORE, fabricItemSettings));
        registerBlock("spirit_sallow_leaves", SPIRIT_SALLOW_LEAVES);
        registerBlockItem("spirit_sallow_leaves", new BlockItem(SPIRIT_SALLOW_LEAVES, fabricItemSettings));
        registerBlock("spirit_sallow_heart", SPIRIT_SALLOW_HEART);
        registerBlockItem("spirit_sallow_heart", new BlockItem(SPIRIT_SALLOW_HEART, fabricItemSettings));

        registerBlock("cyan_spirit_sallow_vines_head", CYAN_SPIRIT_SALLOW_VINES_HEAD);
        registerBlockItem("cyan_spirit_sallow_vines_head", new BlockItem(CYAN_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings));
        registerBlock("magenta_spirit_sallow_vines_head", MAGENTA_SPIRIT_SALLOW_VINES_HEAD);
        registerBlockItem("magenta_spirit_sallow_vines_head", new BlockItem(MAGENTA_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings));
        registerBlock("yellow_spirit_sallow_vines_head", YELLOW_SPIRIT_SALLOW_VINES_HEAD);
        registerBlockItem("yellow_spirit_sallow_vines_head", new BlockItem(YELLOW_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings));
        registerBlock("black_spirit_sallow_vines_head", BLACK_SPIRIT_SALLOW_VINES_HEAD);
        registerBlockItem("black_spirit_sallow_vines_head", new BlockItem(BLACK_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings));
        registerBlock("white_spirit_sallow_vines_head", WHITE_SPIRIT_SALLOW_VINES_HEAD);
        registerBlockItem("white_spirit_sallow_vines_head", new BlockItem(WHITE_SPIRIT_SALLOW_VINES_HEAD, fabricItemSettings));

        registerBlock("cyan_spirit_sallow_vines_body", CYAN_SPIRIT_SALLOW_VINES_BODY);
        registerBlockItem("cyan_spirit_sallow_vines_body", new BlockItem(CYAN_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings));
        registerBlock("magenta_spirit_sallow_vines_body", MAGENTA_SPIRIT_SALLOW_VINES_BODY);
        registerBlockItem("magenta_spirit_sallow_vines_body", new BlockItem(MAGENTA_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings));
        registerBlock("yellow_spirit_sallow_vines_body", YELLOW_SPIRIT_SALLOW_VINES_BODY);
        registerBlockItem("yellow_spirit_sallow_vines_body", new BlockItem(YELLOW_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings));
        registerBlock("black_spirit_sallow_vines_body", BLACK_SPIRIT_SALLOW_VINES_BODY);
        registerBlockItem("black_spirit_sallow_vines_body", new BlockItem(BLACK_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings));
        registerBlock("white_spirit_sallow_vines_body", WHITE_SPIRIT_SALLOW_VINES_BODY);
        registerBlockItem("white_spirit_sallow_vines_body", new BlockItem(WHITE_SPIRIT_SALLOW_VINES_BODY, fabricItemSettings));

        registerBlock("sacred_soil", SACRED_SOIL);
        registerBlockItem("sacred_soil", new BlockItem(SACRED_SOIL, fabricItemSettings));
    }

    private static void registerOreBlocks() {
        registerBlock("sparklestone_ore", SPARKLESTONE_ORE);
        registerBlockItem("sparklestone_ore", new BlockItem(SPARKLESTONE_ORE, worldgenItemSettings));
        registerBlock("sparklestone_block", SPARKLESTONE_BLOCK);
        registerBlockItem("sparklestone_block", new BlockItem(SPARKLESTONE_BLOCK, decorationItemSettings));

        registerBlock("azurite_ore", AZURITE_ORE);
        registerBlockItem("azurite_ore", new BlockItem(AZURITE_ORE, worldgenItemSettings));
        registerBlock("azurite_block", AZURITE_BLOCK);
        registerBlockItem("azurite_block", new BlockItem(AZURITE_BLOCK, decorationItemSettings));

        registerBlock("scarlet_ore", SCARLET_ORE);
        registerBlockItem("scarlet_ore", new BlockItem(SCARLET_ORE, worldgenItemSettings));
        registerBlock("scarlet_fragment_block", SCARLET_FRAGMENT_BLOCK);
        registerBlockItem("scarlet_fragment_block", new GravityBlockItem(SCARLET_FRAGMENT_BLOCK, generalItemSettings, -0.2F));
        registerBlock("paletur_ore", PALETUR_ORE);
        registerBlockItem("paletur_ore", new BlockItem(PALETUR_ORE, worldgenItemSettings));
        registerBlock("paletur_fragment_block", PALETUR_FRAGMENT_BLOCK);
        registerBlockItem("paletur_fragment_block", new GravityBlockItem(PALETUR_FRAGMENT_BLOCK, generalItemSettings, 0.02F));
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

        registerBlock("polished_basalt", POLISHED_BASALT);
        registerBlockItem("polished_basalt", new BlockItem(POLISHED_BASALT, fabricItemSettings));
        registerBlock("chiseled_polished_basalt", CHISELED_POLISHED_BASALT);
        registerBlockItem("chiseled_polished_basalt", new BlockItem(CHISELED_POLISHED_BASALT, fabricItemSettings));
        registerBlock("polished_basalt_slab", POLISHED_BASALT_SLAB);
        registerBlockItem("polished_basalt_slab", new BlockItem(POLISHED_BASALT_SLAB, fabricItemSettings));
        registerBlock("polished_basalt_wall", POLISHED_BASALT_WALL);
        registerBlockItem("polished_basalt_wall", new BlockItem(POLISHED_BASALT_WALL, fabricItemSettings));
        registerBlock("polished_basalt_stairs", POLISHED_BASALT_STAIRS);
        registerBlockItem("polished_basalt_stairs", new BlockItem(POLISHED_BASALT_STAIRS, fabricItemSettings));
        registerBlock("basalt_bricks", BASALT_BRICKS);
        registerBlockItem("basalt_bricks", new BlockItem(BASALT_BRICKS, fabricItemSettings));
        registerBlock("basalt_brick_slab", BASALT_BRICK_SLAB);
        registerBlockItem("basalt_brick_slab", new BlockItem(BASALT_BRICK_SLAB, fabricItemSettings));
        registerBlock("basalt_brick_wall", BASALT_BRICK_WALL);
        registerBlockItem("basalt_brick_wall", new BlockItem(BASALT_BRICK_WALL, fabricItemSettings));
        registerBlock("basalt_brick_stairs", BASALT_BRICK_STAIRS);
        registerBlockItem("basalt_brick_stairs", new BlockItem(BASALT_BRICK_STAIRS, fabricItemSettings));

        registerBlock("polished_calcite", POLISHED_CALCITE);
        registerBlockItem("polished_calcite", new BlockItem(POLISHED_CALCITE, fabricItemSettings));
        registerBlock("chiseled_polished_calcite", CHISELED_POLISHED_CALCITE);
        registerBlockItem("chiseled_polished_calcite", new BlockItem(CHISELED_POLISHED_CALCITE, fabricItemSettings));
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
        registerBlock("amethyst_chiseled_basalt", AMETHYST_CHISELED_BASALT);
        registerBlockItem("amethyst_chiseled_basalt", new BlockItem(AMETHYST_CHISELED_BASALT, fabricItemSettings));
        registerBlock("topaz_chiseled_basalt", TOPAZ_CHISELED_BASALT);
        registerBlockItem("topaz_chiseled_basalt", new BlockItem(TOPAZ_CHISELED_BASALT, fabricItemSettings));
        registerBlock("citrine_chiseled_basalt", CITRINE_CHISELED_BASALT);
        registerBlockItem("citrine_chiseled_basalt", new BlockItem(CITRINE_CHISELED_BASALT, fabricItemSettings));
        registerBlock("onyx_chiseled_basalt", ONYX_CHISELED_BASALT);
        registerBlockItem("onyx_chiseled_basalt", new BlockItem(ONYX_CHISELED_BASALT, fabricItemSettings));
        registerBlock("moonstone_chiseled_basalt", MOONSTONE_CHISELED_BASALT);
        registerBlockItem("moonstone_chiseled_basalt", new BlockItem(MOONSTONE_CHISELED_BASALT, fabricItemSettings));

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
        registerBlock("amethyst_basalt_lamp", AMETHYST_BASALT_LAMP);
        registerBlockItem("amethyst_basalt_lamp", new BlockItem(AMETHYST_BASALT_LAMP, fabricItemSettings));
        registerBlock("topaz_basalt_lamp", TOPAZ_BASALT_LAMP);
        registerBlockItem("topaz_basalt_lamp", new BlockItem(TOPAZ_BASALT_LAMP, fabricItemSettings));
        registerBlock("citrine_basalt_lamp", CITRINE_BASALT_LAMP);
        registerBlockItem("citrine_basalt_lamp", new BlockItem(CITRINE_BASALT_LAMP, fabricItemSettings));
        registerBlock("onyx_basalt_lamp", ONYX_BASALT_LAMP);
        registerBlockItem("onyx_basalt_lamp", new BlockItem(ONYX_BASALT_LAMP, fabricItemSettings));
        registerBlock("moonstone_basalt_lamp", MOONSTONE_BASALT_LAMP);
        registerBlockItem("moonstone_basalt_lamp", new BlockItem(MOONSTONE_BASALT_LAMP, fabricItemSettings));
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
        registerBlock("black_plank_stairs", BLACK_PLANK_STAIRS);
        registerBlockItem("black_plank_stairs", new BlockItem(BLACK_PLANK_STAIRS, fabricItemSettings));
        registerBlock("blue_plank_stairs", BLUE_PLANK_STAIRS);
        registerBlockItem("blue_plank_stairs", new BlockItem(BLUE_PLANK_STAIRS, fabricItemSettings));
        registerBlock("brown_plank_stairs", BROWN_PLANK_STAIRS);
        registerBlockItem("brown_plank_stairs", new BlockItem(BROWN_PLANK_STAIRS, fabricItemSettings));
        registerBlock("cyan_plank_stairs", CYAN_PLANK_STAIRS);
        registerBlockItem("cyan_plank_stairs", new BlockItem(CYAN_PLANK_STAIRS, fabricItemSettings));
        registerBlock("gray_plank_stairs", GRAY_PLANK_STAIRS);
        registerBlockItem("gray_plank_stairs", new BlockItem(GRAY_PLANK_STAIRS, fabricItemSettings));
        registerBlock("green_plank_stairs", GREEN_PLANK_STAIRS);
        registerBlockItem("green_plank_stairs", new BlockItem(GREEN_PLANK_STAIRS, fabricItemSettings));
        registerBlock("light_blue_plank_stairs", LIGHT_BLUE_PLANK_STAIRS);
        registerBlockItem("light_blue_plank_stairs", new BlockItem(LIGHT_BLUE_PLANK_STAIRS, fabricItemSettings));
        registerBlock("light_gray_plank_stairs", LIGHT_GRAY_PLANK_STAIRS);
        registerBlockItem("light_gray_plank_stairs", new BlockItem(LIGHT_GRAY_PLANK_STAIRS, fabricItemSettings));
        registerBlock("lime_plank_stairs", LIME_PLANK_STAIRS);
        registerBlockItem("lime_plank_stairs", new BlockItem(LIME_PLANK_STAIRS, fabricItemSettings));
        registerBlock("magenta_plank_stairs", MAGENTA_PLANK_STAIRS);
        registerBlockItem("magenta_plank_stairs", new BlockItem(MAGENTA_PLANK_STAIRS, fabricItemSettings));
        registerBlock("orange_plank_stairs", ORANGE_PLANK_STAIRS);
        registerBlockItem("orange_plank_stairs", new BlockItem(ORANGE_PLANK_STAIRS, fabricItemSettings));
        registerBlock("pink_plank_stairs", PINK_PLANK_STAIRS);
        registerBlockItem("pink_plank_stairs", new BlockItem(PINK_PLANK_STAIRS, fabricItemSettings));
        registerBlock("purple_plank_stairs", PURPLE_PLANK_STAIRS);
        registerBlockItem("purple_plank_stairs", new BlockItem(PURPLE_PLANK_STAIRS, fabricItemSettings));
        registerBlock("red_plank_stairs", RED_PLANK_STAIRS);
        registerBlockItem("red_plank_stairs", new BlockItem(RED_PLANK_STAIRS, fabricItemSettings));
        registerBlock("white_plank_stairs", WHITE_PLANK_STAIRS);
        registerBlockItem("white_plank_stairs", new BlockItem(WHITE_PLANK_STAIRS, fabricItemSettings));
        registerBlock("yellow_plank_stairs", YELLOW_PLANK_STAIRS);
        registerBlockItem("yellow_plank_stairs", new BlockItem(YELLOW_PLANK_STAIRS, fabricItemSettings));
        registerBlock("black_plank_pressure_plate", BLACK_PLANK_PRESSURE_PLATE);
        registerBlockItem("black_plank_pressure_plate", new BlockItem(BLACK_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("blue_plank_pressure_plate", BLUE_PLANK_PRESSURE_PLATE);
        registerBlockItem("blue_plank_pressure_plate", new BlockItem(BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("brown_plank_pressure_plate", BROWN_PLANK_PRESSURE_PLATE);
        registerBlockItem("brown_plank_pressure_plate", new BlockItem(BROWN_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("cyan_plank_pressure_plate", CYAN_PLANK_PRESSURE_PLATE);
        registerBlockItem("cyan_plank_pressure_plate", new BlockItem(CYAN_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("gray_plank_pressure_plate", GRAY_PLANK_PRESSURE_PLATE);
        registerBlockItem("gray_plank_pressure_plate", new BlockItem(GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("green_plank_pressure_plate", GREEN_PLANK_PRESSURE_PLATE);
        registerBlockItem("green_plank_pressure_plate", new BlockItem(GREEN_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("light_blue_plank_pressure_plate", LIGHT_BLUE_PLANK_PRESSURE_PLATE);
        registerBlockItem("light_blue_plank_pressure_plate", new BlockItem(LIGHT_BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("light_gray_plank_pressure_plate", LIGHT_GRAY_PLANK_PRESSURE_PLATE);
        registerBlockItem("light_gray_plank_pressure_plate", new BlockItem(LIGHT_GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("lime_plank_pressure_plate", LIME_PLANK_PRESSURE_PLATE);
        registerBlockItem("lime_plank_pressure_plate", new BlockItem(LIME_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("magenta_plank_pressure_plate", MAGENTA_PLANK_PRESSURE_PLATE);
        registerBlockItem("magenta_plank_pressure_plate", new BlockItem(MAGENTA_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("orange_plank_pressure_plate", ORANGE_PLANK_PRESSURE_PLATE);
        registerBlockItem("orange_plank_pressure_plate", new BlockItem(ORANGE_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("pink_plank_pressure_plate", PINK_PLANK_PRESSURE_PLATE);
        registerBlockItem("pink_plank_pressure_plate", new BlockItem(PINK_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("purple_plank_pressure_plate", PURPLE_PLANK_PRESSURE_PLATE);
        registerBlockItem("purple_plank_pressure_plate", new BlockItem(PURPLE_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("red_plank_pressure_plate", RED_PLANK_PRESSURE_PLATE);
        registerBlockItem("red_plank_pressure_plate", new BlockItem(RED_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("white_plank_pressure_plate", WHITE_PLANK_PRESSURE_PLATE);
        registerBlockItem("white_plank_pressure_plate", new BlockItem(WHITE_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("yellow_plank_pressure_plate", YELLOW_PLANK_PRESSURE_PLATE);
        registerBlockItem("yellow_plank_pressure_plate", new BlockItem(YELLOW_PLANK_PRESSURE_PLATE, fabricItemSettings));
        registerBlock("black_plank_fence", BLACK_PLANK_FENCE);
        registerBlockItem("black_plank_fence", new BlockItem(BLACK_PLANK_FENCE, fabricItemSettings));
        registerBlock("blue_plank_fence", BLUE_PLANK_FENCE);
        registerBlockItem("blue_plank_fence", new BlockItem(BLUE_PLANK_FENCE, fabricItemSettings));
        registerBlock("brown_plank_fence", BROWN_PLANK_FENCE);
        registerBlockItem("brown_plank_fence", new BlockItem(BROWN_PLANK_FENCE, fabricItemSettings));
        registerBlock("cyan_plank_fence", CYAN_PLANK_FENCE);
        registerBlockItem("cyan_plank_fence", new BlockItem(CYAN_PLANK_FENCE, fabricItemSettings));
        registerBlock("gray_plank_fence", GRAY_PLANK_FENCE);
        registerBlockItem("gray_plank_fence", new BlockItem(GRAY_PLANK_FENCE, fabricItemSettings));
        registerBlock("green_plank_fence", GREEN_PLANK_FENCE);
        registerBlockItem("green_plank_fence", new BlockItem(GREEN_PLANK_FENCE, fabricItemSettings));
        registerBlock("light_blue_plank_fence", LIGHT_BLUE_PLANK_FENCE);
        registerBlockItem("light_blue_plank_fence", new BlockItem(LIGHT_BLUE_PLANK_FENCE, fabricItemSettings));
        registerBlock("light_gray_plank_fence", LIGHT_GRAY_PLANK_FENCE);
        registerBlockItem("light_gray_plank_fence", new BlockItem(LIGHT_GRAY_PLANK_FENCE, fabricItemSettings));
        registerBlock("lime_plank_fence", LIME_PLANK_FENCE);
        registerBlockItem("lime_plank_fence", new BlockItem(LIME_PLANK_FENCE, fabricItemSettings));
        registerBlock("magenta_plank_fence", MAGENTA_PLANK_FENCE);
        registerBlockItem("magenta_plank_fence", new BlockItem(MAGENTA_PLANK_FENCE, fabricItemSettings));
        registerBlock("orange_plank_fence", ORANGE_PLANK_FENCE);
        registerBlockItem("orange_plank_fence", new BlockItem(ORANGE_PLANK_FENCE, fabricItemSettings));
        registerBlock("pink_plank_fence", PINK_PLANK_FENCE);
        registerBlockItem("pink_plank_fence", new BlockItem(PINK_PLANK_FENCE, fabricItemSettings));
        registerBlock("purple_plank_fence", PURPLE_PLANK_FENCE);
        registerBlockItem("purple_plank_fence", new BlockItem(PURPLE_PLANK_FENCE, fabricItemSettings));
        registerBlock("red_plank_fence", RED_PLANK_FENCE);
        registerBlockItem("red_plank_fence", new BlockItem(RED_PLANK_FENCE, fabricItemSettings));
        registerBlock("white_plank_fence", WHITE_PLANK_FENCE);
        registerBlockItem("white_plank_fence", new BlockItem(WHITE_PLANK_FENCE, fabricItemSettings));
        registerBlock("yellow_plank_fence", YELLOW_PLANK_FENCE);
        registerBlockItem("yellow_plank_fence", new BlockItem(YELLOW_PLANK_FENCE, fabricItemSettings));
        registerBlock("black_plank_fence_gate", BLACK_PLANK_FENCE_GATE);
        registerBlockItem("black_plank_fence_gate", new BlockItem(BLACK_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("blue_plank_fence_gate", BLUE_PLANK_FENCE_GATE);
        registerBlockItem("blue_plank_fence_gate", new BlockItem(BLUE_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("brown_plank_fence_gate", BROWN_PLANK_FENCE_GATE);
        registerBlockItem("brown_plank_fence_gate", new BlockItem(BROWN_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("cyan_plank_fence_gate", CYAN_PLANK_FENCE_GATE);
        registerBlockItem("cyan_plank_fence_gate", new BlockItem(CYAN_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("gray_plank_fence_gate", GRAY_PLANK_FENCE_GATE);
        registerBlockItem("gray_plank_fence_gate", new BlockItem(GRAY_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("green_plank_fence_gate", GREEN_PLANK_FENCE_GATE);
        registerBlockItem("green_plank_fence_gate", new BlockItem(GREEN_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("light_blue_plank_fence_gate", LIGHT_BLUE_PLANK_FENCE_GATE);
        registerBlockItem("light_blue_plank_fence_gate", new BlockItem(LIGHT_BLUE_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("light_gray_plank_fence_gate", LIGHT_GRAY_PLANK_FENCE_GATE);
        registerBlockItem("light_gray_plank_fence_gate", new BlockItem(LIGHT_GRAY_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("lime_plank_fence_gate", LIME_PLANK_FENCE_GATE);
        registerBlockItem("lime_plank_fence_gate", new BlockItem(LIME_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("magenta_plank_fence_gate", MAGENTA_PLANK_FENCE_GATE);
        registerBlockItem("magenta_plank_fence_gate", new BlockItem(MAGENTA_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("orange_plank_fence_gate", ORANGE_PLANK_FENCE_GATE);
        registerBlockItem("orange_plank_fence_gate", new BlockItem(ORANGE_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("pink_plank_fence_gate", PINK_PLANK_FENCE_GATE);
        registerBlockItem("pink_plank_fence_gate", new BlockItem(PINK_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("purple_plank_fence_gate", PURPLE_PLANK_FENCE_GATE);
        registerBlockItem("purple_plank_fence_gate", new BlockItem(PURPLE_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("red_plank_fence_gate", RED_PLANK_FENCE_GATE);
        registerBlockItem("red_plank_fence_gate", new BlockItem(RED_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("white_plank_fence_gate", WHITE_PLANK_FENCE_GATE);
        registerBlockItem("white_plank_fence_gate", new BlockItem(WHITE_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("yellow_plank_fence_gate", YELLOW_PLANK_FENCE_GATE);
        registerBlockItem("yellow_plank_fence_gate", new BlockItem(YELLOW_PLANK_FENCE_GATE, fabricItemSettings));
        registerBlock("black_plank_button", BLACK_PLANK_BUTTON);
        registerBlockItem("black_plank_button", new BlockItem(BLACK_PLANK_BUTTON, fabricItemSettings));
        registerBlock("blue_plank_button", BLUE_PLANK_BUTTON);
        registerBlockItem("blue_plank_button", new BlockItem(BLUE_PLANK_BUTTON, fabricItemSettings));
        registerBlock("brown_plank_button", BROWN_PLANK_BUTTON);
        registerBlockItem("brown_plank_button", new BlockItem(BROWN_PLANK_BUTTON, fabricItemSettings));
        registerBlock("cyan_plank_button", CYAN_PLANK_BUTTON);
        registerBlockItem("cyan_plank_button", new BlockItem(CYAN_PLANK_BUTTON, fabricItemSettings));
        registerBlock("gray_plank_button", GRAY_PLANK_BUTTON);
        registerBlockItem("gray_plank_button", new BlockItem(GRAY_PLANK_BUTTON, fabricItemSettings));
        registerBlock("green_plank_button", GREEN_PLANK_BUTTON);
        registerBlockItem("green_plank_button", new BlockItem(GREEN_PLANK_BUTTON, fabricItemSettings));
        registerBlock("light_blue_plank_button", LIGHT_BLUE_PLANK_BUTTON);
        registerBlockItem("light_blue_plank_button", new BlockItem(LIGHT_BLUE_PLANK_BUTTON, fabricItemSettings));
        registerBlock("light_gray_plank_button", LIGHT_GRAY_PLANK_BUTTON);
        registerBlockItem("light_gray_plank_button", new BlockItem(LIGHT_GRAY_PLANK_BUTTON, fabricItemSettings));
        registerBlock("lime_plank_button", LIME_PLANK_BUTTON);
        registerBlockItem("lime_plank_button", new BlockItem(LIME_PLANK_BUTTON, fabricItemSettings));
        registerBlock("magenta_plank_button", MAGENTA_PLANK_BUTTON);
        registerBlockItem("magenta_plank_button", new BlockItem(MAGENTA_PLANK_BUTTON, fabricItemSettings));
        registerBlock("orange_plank_button", ORANGE_PLANK_BUTTON);
        registerBlockItem("orange_plank_button", new BlockItem(ORANGE_PLANK_BUTTON, fabricItemSettings));
        registerBlock("pink_plank_button", PINK_PLANK_BUTTON);
        registerBlockItem("pink_plank_button", new BlockItem(PINK_PLANK_BUTTON, fabricItemSettings));
        registerBlock("purple_plank_button", PURPLE_PLANK_BUTTON);
        registerBlockItem("purple_plank_button", new BlockItem(PURPLE_PLANK_BUTTON, fabricItemSettings));
        registerBlock("red_plank_button", RED_PLANK_BUTTON);
        registerBlockItem("red_plank_button", new BlockItem(RED_PLANK_BUTTON, fabricItemSettings));
        registerBlock("white_plank_button", WHITE_PLANK_BUTTON);
        registerBlockItem("white_plank_button", new BlockItem(WHITE_PLANK_BUTTON, fabricItemSettings));
        registerBlock("yellow_plank_button", YELLOW_PLANK_BUTTON);
        registerBlockItem("yellow_plank_button", new BlockItem(YELLOW_PLANK_BUTTON, fabricItemSettings));
        registerBlock("black_plank_slab", BLACK_PLANK_SLAB);
        registerBlockItem("black_plank_slab", new BlockItem(BLACK_PLANK_SLAB, fabricItemSettings));
        registerBlock("blue_plank_slab", BLUE_PLANK_SLAB);
        registerBlockItem("blue_plank_slab", new BlockItem(BLUE_PLANK_SLAB, fabricItemSettings));
        registerBlock("brown_plank_slab", BROWN_PLANK_SLAB);
        registerBlockItem("brown_plank_slab", new BlockItem(BROWN_PLANK_SLAB, fabricItemSettings));
        registerBlock("cyan_plank_slab", CYAN_PLANK_SLAB);
        registerBlockItem("cyan_plank_slab", new BlockItem(CYAN_PLANK_SLAB, fabricItemSettings));
        registerBlock("gray_plank_slab", GRAY_PLANK_SLAB);
        registerBlockItem("gray_plank_slab", new BlockItem(GRAY_PLANK_SLAB, fabricItemSettings));
        registerBlock("green_plank_slab", GREEN_PLANK_SLAB);
        registerBlockItem("green_plank_slab", new BlockItem(GREEN_PLANK_SLAB, fabricItemSettings));
        registerBlock("light_blue_plank_slab", LIGHT_BLUE_PLANK_SLAB);
        registerBlockItem("light_blue_plank_slab", new BlockItem(LIGHT_BLUE_PLANK_SLAB, fabricItemSettings));
        registerBlock("light_gray_plank_slab", LIGHT_GRAY_PLANK_SLAB);
        registerBlockItem("light_gray_plank_slab", new BlockItem(LIGHT_GRAY_PLANK_SLAB, fabricItemSettings));
        registerBlock("lime_plank_slab", LIME_PLANK_SLAB);
        registerBlockItem("lime_plank_slab", new BlockItem(LIME_PLANK_SLAB, fabricItemSettings));
        registerBlock("magenta_plank_slab", MAGENTA_PLANK_SLAB);
        registerBlockItem("magenta_plank_slab", new BlockItem(MAGENTA_PLANK_SLAB, fabricItemSettings));
        registerBlock("orange_plank_slab", ORANGE_PLANK_SLAB);
        registerBlockItem("orange_plank_slab", new BlockItem(ORANGE_PLANK_SLAB, fabricItemSettings));
        registerBlock("pink_plank_slab", PINK_PLANK_SLAB);
        registerBlockItem("pink_plank_slab", new BlockItem(PINK_PLANK_SLAB, fabricItemSettings));
        registerBlock("purple_plank_slab", PURPLE_PLANK_SLAB);
        registerBlockItem("purple_plank_slab", new BlockItem(PURPLE_PLANK_SLAB, fabricItemSettings));
        registerBlock("red_plank_slab", RED_PLANK_SLAB);
        registerBlockItem("red_plank_slab", new BlockItem(RED_PLANK_SLAB, fabricItemSettings));
        registerBlock("white_plank_slab", WHITE_PLANK_SLAB);
        registerBlockItem("white_plank_slab", new BlockItem(WHITE_PLANK_SLAB, fabricItemSettings));
        registerBlock("yellow_plank_slab", YELLOW_PLANK_SLAB);
        registerBlockItem("yellow_plank_slab", new BlockItem(YELLOW_PLANK_SLAB, fabricItemSettings));
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
        registerBlock("citrine_block", CITRINE_BLOCK);
        registerBlockItem("citrine_block", new BlockItem(CITRINE_BLOCK, fabricItemSettings));
        registerBlock("budding_citrine", BUDDING_CITRINE);
        registerBlockItem("budding_citrine", new BlockItem(BUDDING_CITRINE, fabricItemSettings));
        registerBlock("small_citrine_bud", SMALL_CITRINE_BUD);
        registerBlockItem("small_citrine_bud", new BlockItem(SMALL_CITRINE_BUD, fabricItemSettings));
        registerBlock("medium_citrine_bud", MEDIUM_CITRINE_BUD);
        registerBlockItem("medium_citrine_bud", new BlockItem(MEDIUM_CITRINE_BUD, fabricItemSettings));
        registerBlock("large_citrine_bud", LARGE_CITRINE_BUD);
        registerBlockItem("large_citrine_bud", new BlockItem(LARGE_CITRINE_BUD, fabricItemSettings));
        registerBlock("citrine_cluster", CITRINE_CLUSTER);
        registerBlockItem("citrine_cluster", new BlockItem(CITRINE_CLUSTER, fabricItemSettings));

        registerBlock("topaz_block", TOPAZ_BLOCK);
        registerBlockItem("topaz_block", new BlockItem(TOPAZ_BLOCK, fabricItemSettings));
        registerBlock("budding_topaz", BUDDING_TOPAZ);
        registerBlockItem("budding_topaz", new BlockItem(BUDDING_TOPAZ, fabricItemSettings));
        registerBlock("small_topaz_bud", SMALL_TOPAZ_BUD);
        registerBlockItem("small_topaz_bud", new BlockItem(SMALL_TOPAZ_BUD, fabricItemSettings));
        registerBlock("medium_topaz_bud", MEDIUM_TOPAZ_BUD);
        registerBlockItem("medium_topaz_bud", new BlockItem(MEDIUM_TOPAZ_BUD, fabricItemSettings));
        registerBlock("large_topaz_bud", LARGE_TOPAZ_BUD);
        registerBlockItem("large_topaz_bud", new BlockItem(LARGE_TOPAZ_BUD, fabricItemSettings));
        registerBlock("topaz_cluster", TOPAZ_CLUSTER);
        registerBlockItem("topaz_cluster", new BlockItem(TOPAZ_CLUSTER, fabricItemSettings));

        registerBlock("onyx_block", ONYX_BLOCK);
        registerBlockItem("onyx_block", new BlockItem(ONYX_BLOCK, fabricItemSettings));
        registerBlock("budding_onyx", BUDDING_ONYX);
        registerBlockItem("budding_onyx", new BlockItem(BUDDING_ONYX, fabricItemSettings));
        registerBlock("small_onyx_bud", SMALL_ONYX_BUD);
        registerBlockItem("small_onyx_bud", new BlockItem(SMALL_ONYX_BUD, fabricItemSettings));
        registerBlock("medium_onyx_bud", MEDIUM_ONYX_BUD);
        registerBlockItem("medium_onyx_bud", new BlockItem(MEDIUM_ONYX_BUD, fabricItemSettings));
        registerBlock("large_onyx_bud", LARGE_ONYX_BUD);
        registerBlockItem("large_onyx_bud", new BlockItem(LARGE_ONYX_BUD, fabricItemSettings));
        registerBlock("onyx_cluster", ONYX_CLUSTER);
        registerBlockItem("onyx_cluster", new BlockItem(ONYX_CLUSTER, fabricItemSettings));

        registerBlock("moonstone_block", MOONSTONE_BLOCK);
        registerBlockItem("moonstone_block", new BlockItem(MOONSTONE_BLOCK, fabricItemSettings));
        registerBlock("budding_moonstone", BUDDING_MOONSTONE);
        registerBlockItem("budding_moonstone", new BlockItem(BUDDING_MOONSTONE, fabricItemSettings));
        registerBlock("small_moonstone_bud", SMALL_MOONSTONE_BUD);
        registerBlockItem("small_moonstone_bud", new BlockItem(SMALL_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD);
        registerBlockItem("medium_moonstone_bud", new BlockItem(MEDIUM_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("large_moonstone_bud", LARGE_MOONSTONE_BUD);
        registerBlockItem("large_moonstone_bud", new BlockItem(LARGE_MOONSTONE_BUD, fabricItemSettings));
        registerBlock("moonstone_cluster", MOONSTONE_CLUSTER);
        registerBlockItem("moonstone_cluster", new BlockItem(MOONSTONE_CLUSTER, fabricItemSettings));

        registerBlock("rainbow_moonstone_block", RAINBOW_MOONSTONE_BLOCK);
        registerBlockItem("rainbow_moonstone_block", new BlockItem(RAINBOW_MOONSTONE_BLOCK, fabricItemSettings));
    }

    // Most mob heads vanilla is missing (vanilla only has: skeleton, wither skeleton, zombie, player, creeper, ender dragon)
    private static void registerMobHeads(FabricItemSettings fabricItemSettings) {
        FabricBlockSettings mobHeadBlockSettings = FabricBlockSettings.of(Material.DECORATION).strength(1.0F);

        for(PigmentSkullBlock.Type type : PigmentSkullBlock.Type.values()) {
            Block head = new PigmentSkullBlock(type, mobHeadBlockSettings);
            Block wallHead = new PigmentWallSkullBlock(type, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0F).dropsLike(head));
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
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_CITRINE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_CITRINE_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_ONYX_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_ONYX_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_TOPAZ_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_TOPAZ_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.SMALL_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MEDIUM_MOONSTONE_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.LARGE_MOONSTONE_BUD, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.VANILLA_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_CALCITE_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_CALCITE_LAMP, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.AMETHYST_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.CITRINE_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.TOPAZ_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.MOONSTONE_BASALT_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PigmentBlocks.ONYX_BASALT_LAMP, RenderLayer.getTranslucent());

        // SAPLINGS
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
