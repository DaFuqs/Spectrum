package de.dafuqs.spectrum.registries;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlock;
import de.dafuqs.spectrum.blocks.bottomless_bundle.BottomlessBundleBlock;
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
import de.dafuqs.spectrum.blocks.energy.ColorPickerBlock;
import de.dafuqs.spectrum.blocks.energy.CrystalApothecaryBlock;
import de.dafuqs.spectrum.blocks.energy.InkDuctBlock;
import de.dafuqs.spectrum.blocks.energy.InkwellBlock;
import de.dafuqs.spectrum.blocks.fluid.LiquidCrystalFluidBlock;
import de.dafuqs.spectrum.blocks.fluid.MidnightSolutionFluidBlock;
import de.dafuqs.spectrum.blocks.fluid.MudFluidBlock;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlock;
import de.dafuqs.spectrum.blocks.gemstone.SpectrumBuddingBlock;
import de.dafuqs.spectrum.blocks.gemstone.SpectrumGemstoneBlock;
import de.dafuqs.spectrum.blocks.gravity.FloatBlock;
import de.dafuqs.spectrum.blocks.gravity.FloatBlockItem;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineBulbBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVinePetalBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVinePlantBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.LavaSpongeBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.WetLavaSpongeBlock;
import de.dafuqs.spectrum.blocks.lava_sponge.WetLavaSpongeItem;
import de.dafuqs.spectrum.blocks.melon.AttachedGlisteringStemBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringMelonBlock;
import de.dafuqs.spectrum.blocks.melon.GlisteringStemBlock;
import de.dafuqs.spectrum.blocks.memory.MemoryBlock;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.blocks.mob_blocks.*;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockItem;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumWallSkullBlock;
import de.dafuqs.spectrum.blocks.particle_spawner.CreativeParticleSpawnerBlock;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlock;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.PastelNetworkNodeBlock;
import de.dafuqs.spectrum.blocks.pedestal.BuiltinPedestalVariant;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockItem;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlock;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarItem;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlock;
import de.dafuqs.spectrum.blocks.spirit_sallow.*;
import de.dafuqs.spectrum.blocks.structure.DikeGateBlock;
import de.dafuqs.spectrum.blocks.structure.PreservationControllerBlock;
import de.dafuqs.spectrum.blocks.structure.StatueBlock;
import de.dafuqs.spectrum.blocks.structure.TreasureChestBlock;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlock;
import de.dafuqs.spectrum.blocks.upgrade.UpgradeBlockItem;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.entity.LivingMarkerEntity;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.items.FourLeafCloverItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.color.ItemColors;
import de.dafuqs.spectrum.worldgen.ColoredSaplingGenerator;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;

import static de.dafuqs.spectrum.registries.SpectrumItems.*;

public class SpectrumBlocks {
	
	// PEDESTALS
	public static final Block PEDESTAL_BASIC_TOPAZ = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.BASIC_TOPAZ);
	public static final Block PEDESTAL_BASIC_AMETHYST = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.BASIC_AMETHYST);
	public static final Block PEDESTAL_BASIC_CITRINE = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.BASIC_CITRINE);
	public static final Block PEDESTAL_ALL_BASIC = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.CMY);
	public static final Block PEDESTAL_ONYX = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.ONYX);
	public static final Block PEDESTAL_MOONSTONE = new PedestalBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F), BuiltinPedestalVariant.MOONSTONE);
	public static final Block ENCHANTER = new EnchanterBlock(FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F).nonOpaque());
	public static final Block ITEM_BOWL_BASALT = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block ITEM_BOWL_CALCITE = new ItemBowlBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block POTION_WORKSHOP = new PotionWorkshopBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).nonOpaque());
	public static final Block SPIRIT_INSTILLER = new SpiritInstillerBlock(FabricBlockSettings.of(Material.STONE).strength(2.0F, 5.0F).nonOpaque());
	public static final Block MEMORY = new MemoryBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.0f).nonOpaque().ticksRandomly());
	// GEMS
	public static final Block TOPAZ_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER).luminance(6));
	public static final Block LARGE_TOPAZ_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_TOPAZ_BUD).luminance(6));
	public static final Block MEDIUM_TOPAZ_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_TOPAZ_BUD).luminance(4));
	public static final Block SMALL_TOPAZ_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(TOPAZ_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_TOPAZ_BUD).luminance(2));
	public static final Block BUDDING_TOPAZ = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
	public static final Block TOPAZ_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.TOPAZ_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME);
	public static final Block CITRINE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER).luminance(7));
	public static final Block LARGE_CITRINE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_CITRINE_BUD).luminance(7));
	public static final Block MEDIUM_CITRINE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_CITRINE_BUD).luminance(5));
	public static final Block SMALL_CITRINE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(CITRINE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_CITRINE_BUD).luminance(3));
	public static final Block BUDDING_CITRINE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
	public static final Block CITRINE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).hardness(1.5f).sounds(SpectrumBlockSoundGroups.CITRINE_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME);
	public static final Block ONYX_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER).luminance(3));
	public static final Block LARGE_ONYX_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_ONYX_BUD).luminance(5));
	public static final Block MEDIUM_ONYX_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_ONYX_BUD).luminance(3));
	public static final Block SMALL_ONYX_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(ONYX_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_ONYX_BUD).luminance(1));
	public static final Block BUDDING_ONYX = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SMALL_ONYX_BUD, MEDIUM_ONYX_BUD, LARGE_ONYX_BUD, ONYX_CLUSTER, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	public static final Block ONYX_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.BLACK).hardness(1.5F).sounds(SpectrumBlockSoundGroups.ONYX_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME);
	public static final Block MOONSTONE_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER).luminance(15));
	public static final Block LARGE_MOONSTONE_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.LARGE_MOONSTONE_BUD).luminance(10));
	public static final Block MEDIUM_MOONSTONE_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.MEDIUM_MOONSTONE_BUD).luminance(8));
	public static final Block SMALL_MOONSTONE_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copyOf(MOONSTONE_CLUSTER).sounds(SpectrumBlockSoundGroups.SMALL_MOONSTONE_BUD).luminance(6));
	public static final Block BUDDING_MOONSTONE = new SpectrumBuddingBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).ticksRandomly().sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool(), SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
	public static final Block MOONSTONE_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.MOONSTONE_BLOCK).requiresTool(), SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
	public static final Block SPECTRAL_SHARD_BLOCK = new SpectrumGemstoneBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DIAMOND_BLUE).hardness(1.5F).sounds(SpectrumBlockSoundGroups.SPECTRAL_BLOCK).requiresTool(), SpectrumSoundEvents.SPECTRAL_BLOCK_HIT, SpectrumSoundEvents.SPECTRAL_BLOCK_CHIME);
	public static final Block BEDROCK_STORAGE_BLOCK = new BlockWithTooltip(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(100.0F, 3600.0F), new TranslatableText("spectrum.tooltip.dragon_and_wither_immune"));
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
	public static final Block VANILLA_PLAYER_ONLY_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), Blocks.GLASS, false);
	public static final Block TINTED_PLAYER_ONLY_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(Blocks.TINTED_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), Blocks.TINTED_GLASS, true);
	public static final Block RADIANT_PLAYER_ONLY_GLASS = new AlternatePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.RADIANT_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never).luminance((state) -> 15), SpectrumBlocks.RADIANT_GLASS, false);
	public static final Block TOPAZ_PLAYER_ONLY_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.TOPAZ_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.CYAN);
	public static final Block AMETHYST_PLAYER_ONLY_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.MAGENTA);
	public static final Block CITRINE_PLAYER_ONLY_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.CITRINE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.YELLOW);
	public static final Block ONYX_PLAYER_ONLY_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.ONYX_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.BLACK);
	public static final Block MOONSTONE_PLAYER_ONLY_GLASS = new GemstonePlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.MOONSTONE_GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never), BuiltinGemstoneColor.WHITE);
	// MELON
	public static final Block GLISTERING_MELON = new GlisteringMelonBlock(FabricBlockSettings.copyOf(Blocks.MELON));
	public static final Block GLISTERING_MELON_STEM = new GlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.MELON_STEM));
	public static final Block ATTACHED_GLISTERING_MELON_STEM = new AttachedGlisteringStemBlock((GourdBlock) GLISTERING_MELON, () -> SpectrumItems.GLISTERING_MELON_SEEDS, FabricBlockSettings.copyOf(Blocks.ATTACHED_MELON_STEM));
	// SAPLING
	public static final Block OMINOUS_SAPLING = new OminousSaplingBlock(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).ticksRandomly());
	// TECHNICAL WITHOUT CORRESPONDING ITEMS
	public static final Block BLOCK_FLOODER = new BlockFlooderBlock(FabricBlockSettings.of(Material.STONE));
	public static final Block BOTTOMLESS_BUNDLE = new BottomlessBundleBlock(FabricBlockSettings.of(Material.WOOL).hardness(2.0F).mapColor(MapColor.PALE_PURPLE).nonOpaque());
	public static final Block WAND_LIGHT_BLOCK = new WandLightBlock(FabricBlockSettings.copyOf(Blocks.LIGHT).sounds(SpectrumBlockSoundGroups.WAND_LIGHT).breakInstantly());
	public static final Block DECAYING_LIGHT_BLOCK = new DecayingLightBlock(FabricBlockSettings.copyOf(WAND_LIGHT_BLOCK).ticksRandomly());
	// DECAY
	public static final Block FADING = new FadingBlock(FabricBlockSettings.of(SpectrumBlockMaterials.DECAY, MapColor.BLACK).ticksRandomly().strength(0.5F, 0.5F), SpectrumBlockTags.FADING_CONVERSIONS, null, 1, 1F);
	public static final Block FAILING = new FailingBlock(FabricBlockSettings.copyOf(FADING).strength(20.0F, 50.0F), null, SpectrumBlockTags.FAILING_SAFE, 2, 2.5F);
	public static final Block RUIN = new TerrorBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F), null, SpectrumBlockTags.RUIN_SAFE, 3, 5F);
	public static final Block TERROR = new TerrorBlock(FabricBlockSettings.copyOf(FADING).strength(100.0F, 3600000.0F), null, SpectrumBlockTags.TERROR_SAFE, 4, 7.5F);
	public static final Block DECAY_AWAY = new DecayAwayBlock(FabricBlockSettings.copyOf(Blocks.DIRT));
	public static final Block BLACK_MATERIA = new BlackMateriaBlock(FabricBlockSettings.copyOf(Blocks.SAND).ticksRandomly().breakInstantly());
	// FLUIDS
	public static final Material LIQUID_CRYSTAL_MATERIAL = (new FabricMaterialBuilder(MapColor.LIGHT_GRAY)).allowsMovement().lightPassesThrough().notSolid().destroyedByPiston().replaceable().liquid().build();
	public static final Material MUD_MATERIAL = (new FabricMaterialBuilder(MapColor.TERRACOTTA_BROWN)).allowsMovement().lightPassesThrough().notSolid().destroyedByPiston().replaceable().liquid().build();
	public static final Material MIDNIGHT_SOLUTION_MATERIAL = (new FabricMaterialBuilder(MapColor.DARK_AQUA)).allowsMovement().lightPassesThrough().notSolid().destroyedByPiston().replaceable().liquid().build();
	public static final Block LIQUID_CRYSTAL = new LiquidCrystalFluidBlock(SpectrumFluids.LIQUID_CRYSTAL, AbstractBlock.Settings.of(LIQUID_CRYSTAL_MATERIAL).noCollision().strength(100.0F).dropsNothing().luminance((state) -> 8));
	public static final Block MUD = new MudFluidBlock(SpectrumFluids.MUD, AbstractBlock.Settings.of(MUD_MATERIAL).noCollision().strength(100.0F).dropsNothing());
	public static final Block MIDNIGHT_SOLUTION = new MidnightSolutionFluidBlock(SpectrumFluids.MIDNIGHT_SOLUTION, AbstractBlock.Settings.of(MIDNIGHT_SOLUTION_MATERIAL).noCollision().strength(100.0F).dropsNothing());
	// PASTEL NETWORK
	public static final Block CONNECTION_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), "block.spectrum.connection_node.tooltip");
	public static final Block PROVIDER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(BlockSoundGroup.AMETHYST_CLUSTER), "block.spectrum.provider_node.tooltip");
	public static final Block STORAGE_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.TOPAZ_CLUSTER), "block.spectrum.storage_node.tooltip");
	public static final Block PUSHER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.CITRINE_CLUSTER), "block.spectrum.pusher_node.tooltip");
	public static final Block PULLER_NODE = new PastelNetworkNodeBlock(FabricBlockSettings.of(Material.AMETHYST).hardness(1.5F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.ONYX_CLUSTER), "block.spectrum.puller_node.tooltip");
	public static final Block INTERACTION_NODE = new Block(FabricBlockSettings.of(Material.AMETHYST).hardness(5.0F).nonOpaque().requiresTool().sounds(SpectrumBlockSoundGroups.MOONSTONE_CLUSTER));
	// ENERGY
	public static final Block COLOR_PICKER = new ColorPickerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F).nonOpaque());
	public static final Block INKWELL = new InkwellBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F));
	public static final Block INK_DUCT = new InkDuctBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F).nonOpaque());
	public static final Block CRYSTAL_APOTHECARY = new CrystalApothecaryBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0F));
	public static final Block BLACK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BLACK_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BLACK_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BLACK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BLACK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BLACK_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BLACK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BLUE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BLUE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BLUE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block BROWN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block BROWN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block BROWN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block BROWN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block BROWN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block BROWN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block BROWN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block CYAN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block CYAN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block CYAN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block CYAN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block CYAN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block CYAN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block CYAN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block GRAY_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block GRAY_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block GRAY_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block GREEN_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block GREEN_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block GREEN_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block GREEN_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block GREEN_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block GREEN_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block GREEN_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block LIGHT_BLUE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIGHT_BLUE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIGHT_BLUE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIGHT_BLUE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIGHT_BLUE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIGHT_BLUE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIGHT_BLUE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block LIGHT_GRAY_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIGHT_GRAY_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIGHT_GRAY_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIGHT_GRAY_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIGHT_GRAY_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIGHT_GRAY_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIGHT_GRAY_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block LIME_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block LIME_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block LIME_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block LIME_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block LIME_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block LIME_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block LIME_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block MAGENTA_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block MAGENTA_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block MAGENTA_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block MAGENTA_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block MAGENTA_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block MAGENTA_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block MAGENTA_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block ORANGE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block ORANGE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block ORANGE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block ORANGE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block ORANGE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block ORANGE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block ORANGE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block PINK_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block PINK_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block PINK_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block PINK_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block PINK_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block PINK_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block PINK_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block PURPLE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block PURPLE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block PURPLE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block PURPLE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block PURPLE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block PURPLE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block PURPLE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block RED_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block RED_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block RED_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block RED_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block RED_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block RED_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block RED_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block WHITE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block WHITE_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block WHITE_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block WHITE_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block WHITE_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block WHITE_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block WHITE_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block YELLOW_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	public static final Block YELLOW_PLANK_STAIRS = new SpectrumStairsBlock(BLACK_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
	public static final Block YELLOW_PLANK_PRESSURE_PLATE = new SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE));
	public static final Block YELLOW_PLANK_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
	public static final Block YELLOW_PLANK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
	public static final Block YELLOW_PLANK_BUTTON = new SpectrumWoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
	public static final Block YELLOW_PLANK_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
	public static final Block PRESERVATION_CONTROLLER = new PreservationControllerBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F).dropsNothing().luminance(1).emissiveLighting(SpectrumBlocks::always).postProcess(SpectrumBlocks::always));
	public static final Block DIKE_GATE = new DikeGateBlock(FabricBlockSettings.of(Material.GLASS).strength(-1.0F).dropsNothing().luminance(3).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never));
	public static final Block TREASURE_CHEST = new TreasureChestBlock(FabricBlockSettings.copyOf(Blocks.CHEST).hardness(-1.0F));
	// JADE VINES
	public static final Block JADE_VINE_ROOTS = new JadeVineRootsBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).sounds(BlockSoundGroup.WOOL).ticksRandomly().luminance((state) -> state.get(JadeVineRootsBlock.DEAD) ? 0 : 4).nonOpaque());
	public static final Block JADE_VINE_BULB = new JadeVineBulbBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).noCollision().sounds(BlockSoundGroup.WOOL).luminance((state) -> state.get(JadeVineBulbBlock.DEAD) ? 0 : 5).nonOpaque());
	public static final Block JADE_VINES = new JadeVinePlantBlock(FabricBlockSettings.of(Material.PLANT, MapColor.PALE_GREEN).strength(0.1F).noCollision().sounds(BlockSoundGroup.WOOL).luminance((state) -> state.get(JadeVinePlantBlock.AGE) == 0 ? 0 : 5).nonOpaque());
	public static final Block JADE_VINE_PETAL_BLOCK = new JadeVinePetalBlock(FabricBlockSettings.of(Material.LEAVES, MapColor.PALE_GREEN).strength(0.1F).nonOpaque().sounds(BlockSoundGroup.WOOL).luminance(3));
	public static final Block JADE_VINE_PETAL_CARPET = new CarpetBlock(FabricBlockSettings.of(Material.CARPET, MapColor.PALE_GREEN).strength(0.1F).nonOpaque().sounds(BlockSoundGroup.WOOL).luminance(3));
	// ORES
	public static final Block SPARKLESTONE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_sparklestone"), Blocks.STONE.getDefaultState());
	public static final Block DEEPSLATE_SPARKLESTONE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE).requiresTool(), UniformIntProvider.create(2, 4), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_sparklestone"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block AZURITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_azurite"), Blocks.STONE.getDefaultState());
	public static final Block DEEPSLATE_AZURITE_ORE = new CloakedOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).requiresTool(), UniformIntProvider.create(4, 7), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_azurite"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block PALETUR_ORE = new CloakedOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.DARK_RED).requiresTool().requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE), UniformIntProvider.create(2, 4), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_paletur"), Blocks.END_STONE.getDefaultState());
	public static final Block SCARLET_ORE = new CloakedOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3.0F, 9.0F).requiresTool(), UniformIntProvider.create(3, 5), new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_scarlet"), Blocks.NETHERRACK.getDefaultState());
	public static final Block SPARKLESTONE_BLOCK = new SparklestoneBlock(FabricBlockSettings.of(Material.GLASS, MapColor.YELLOW).strength(2.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 15));
	public static final Block AZURITE_BLOCK = new SpectrumFacingBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK));
	public static final FloatBlock PALETUR_FRAGMENT_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.2F);
	public static final FloatBlock SCARLET_FRAGMENT_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.DARK_RED).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), -0.2F);
	public static final FloatBlock HOVER_BLOCK = new FloatBlock(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL), 0.0F);
	// FUNCTIONAL BLOCKS
	public static final Block PRIVATE_CHEST = new PrivateChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 3600000.0F).sounds(BlockSoundGroup.STONE));
	public static final Block COMPACTING_CHEST = new CompactingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block RESTOCKING_CHEST = new RestockingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block SUCKING_CHEST = new SuckingChestBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.0F, 4.0F).sounds(BlockSoundGroup.STONE));
	public static final Block PARTICLE_SPAWNER = new ParticleSpawnerBlock(FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque());
	public static final Block CREATIVE_PARTICLE_SPAWNER = new CreativeParticleSpawnerBlock(FabricBlockSettings.copyOf(SpectrumBlocks.PARTICLE_SPAWNER).strength(-1.0F, 3600000.8F).dropsNothing().allowsSpawning(SpectrumBlocks::never));
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
	public static final Block WEATHER_DETECTOR = new WeatherDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ITEM_DETECTOR = new ItemDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block PLAYER_DETECTOR = new PlayerDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block ENTITY_DETECTOR = new EntityDetectorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR));
	public static final Block REDSTONE_CALCULATOR = new RedstoneCalculatorBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_TIMER = new RedstoneTimerBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block REDSTONE_WIRELESS = new RedstoneWirelessBlock(FabricBlockSettings.copyOf(Blocks.REPEATER));
	public static final Block BLOCK_PLACER = new BlockPlacerBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
	public static final Block ENDER_DROPPER = new EnderDropperBlock(FabricBlockSettings.copyOf(Blocks.DROPPER).requiresTool().strength(15F, 60.0F));
	public static final Block ENDER_HOPPER = new EnderHopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER).requiresTool().strength(15F, 60.0F));
	public static final Block SPIRIT_SALLOW_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_ROOTS = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
	public static final Block SPIRIT_SALLOW_HEART = new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).luminance(11));
	public static final Block SACRED_SOIL = new ExtraTickFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND));
	public static final Block STUCK_LIGHTNING_STONE = new LightningStoneBlock(FabricBlockSettings.copyOf(Blocks.DIRT));
	public static final Block DEEPER_DOWN_PORTAL = new DeeperDownPortalBlock(FabricBlockSettings.copyOf(Blocks.END_PORTAL));
	public static final Block UPGRADE_SPEED = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 0.5);
	public static final Block UPGRADE_SPEED2 = new UpgradeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.POLISHED_BASALT), Upgradeable.UpgradeType.SPEED, 2.0);
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
	private static final FabricBlockSettings FUSION_SHINE_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(5.0F, 20.0F).requiresTool().nonOpaque().luminance(value -> value.get(FusionShrineBlock.LIGHT_LEVEL));
	public static final Block FUSION_SHRINE_BASALT = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	public static final Block FUSION_SHRINE_CALCITE = new FusionShrineBlock(FUSION_SHINE_BLOCK_SETTINGS);
	private static final FabricBlockSettings gemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool();
	private static final UniformIntProvider gemOreExperienceProvider = UniformIntProvider.create(1, 4);
	public static final Block TOPAZ_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.CYAN, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_topaz_shard"), Blocks.STONE.getDefaultState());
	public static final Block AMETHYST_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.MAGENTA, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_amethyst_shard"), Blocks.STONE.getDefaultState());
	public static final Block CITRINE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.YELLOW, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_citrine_shard"), Blocks.STONE.getDefaultState());
	public static final Block ONYX_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.BLACK, new Identifier(SpectrumCommon.MOD_ID, "create_onyx_shard"), Blocks.STONE.getDefaultState());
	public static final Block MOONSTONE_ORE = new GemstoneOreBlock(gemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.WHITE, new Identifier(SpectrumCommon.MOD_ID, "midgame/collect_moonstone_shard"), Blocks.STONE.getDefaultState());
	private static final FabricBlockSettings deepslateGemOreBlockSettings = FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE);
	public static final Block DEEPSLATE_TOPAZ_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.CYAN, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_topaz_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_AMETHYST_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.MAGENTA, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_amethyst_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_CITRINE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.YELLOW, new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_citrine_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_ONYX_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.BLACK, new Identifier(SpectrumCommon.MOD_ID, "create_onyx_shard"), Blocks.DEEPSLATE.getDefaultState());
	public static final Block DEEPSLATE_MOONSTONE_ORE = new GemstoneOreBlock(deepslateGemOreBlockSettings, gemOreExperienceProvider, BuiltinGemstoneColor.WHITE, new Identifier(SpectrumCommon.MOD_ID, "midgame/collect_moonstone_shard"), Blocks.DEEPSLATE.getDefaultState());
	private static final FabricBlockSettings gemstoneStorageBlockSettings = FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(5.0F, 6.0F);
	public static final Block TOPAZ_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block AMETHYST_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block CITRINE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block ONYX_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block MOONSTONE_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	public static final Block SPECTRAL_SHARD_STORAGE_BLOCK = new Block(gemstoneStorageBlockSettings);
	// COLORED TREES
	private static final FabricBlockSettings coloredSaplingBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_SAPLING);
	public static final Block BLACK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLACK), coloredSaplingBlockSettings);
	public static final Block BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BLUE), coloredSaplingBlockSettings);
	public static final Block BROWN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.BROWN), coloredSaplingBlockSettings);
	public static final Block CYAN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.CYAN), coloredSaplingBlockSettings);
	public static final Block GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GRAY), coloredSaplingBlockSettings);
	public static final Block GREEN_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.GREEN), coloredSaplingBlockSettings);
	public static final Block LIGHT_BLUE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_BLUE), coloredSaplingBlockSettings);
	public static final Block LIGHT_GRAY_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIGHT_GRAY), coloredSaplingBlockSettings);
	public static final Block LIME_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.LIME), coloredSaplingBlockSettings);
	public static final Block MAGENTA_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.MAGENTA), coloredSaplingBlockSettings);
	public static final Block ORANGE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.ORANGE), coloredSaplingBlockSettings);
	public static final Block PINK_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PINK), coloredSaplingBlockSettings);
	public static final Block PURPLE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.PURPLE), coloredSaplingBlockSettings);
	public static final Block RED_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.RED), coloredSaplingBlockSettings);
	public static final Block WHITE_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.WHITE), coloredSaplingBlockSettings);
	public static final Block YELLOW_SAPLING = new ColoredSaplingBlock(new ColoredSaplingGenerator(DyeColor.YELLOW), coloredSaplingBlockSettings);
	private static final FabricBlockSettings coloredLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 2);
	public static final Block BLACK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block BROWN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block CYAN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block GREEN_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIGHT_BLUE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIGHT_GRAY_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block LIME_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block MAGENTA_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block ORANGE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block PINK_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block PURPLE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block RED_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block WHITE_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	public static final Block YELLOW_LEAVES = new ColoredLeavesBlock(coloredLeavesBlockSettings);
	private static final FabricBlockSettings spiritSallowLeavesBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).luminance((state) -> 8);
	public static final Block SPIRIT_SALLOW_LEAVES = new SpiritSallowLeavesBlock(spiritSallowLeavesBlockSettings);
	private static final FabricBlockSettings coloredLogBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_LOG).luminance((state) -> 5);
	public static final Block BLACK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block BROWN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block CYAN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block GREEN_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIGHT_BLUE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIGHT_GRAY_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block LIME_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block MAGENTA_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block ORANGE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block PINK_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block PURPLE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block RED_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block WHITE_LOG = new ColoredLogBlock(coloredLogBlockSettings);
	public static final Block YELLOW_LOG = new ColoredLogBlock(coloredLogBlockSettings);
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
	// STRUCTURE BLOCKS
	private static final FabricBlockSettings preservationBlockSettings = FabricBlockSettings.of(Material.STONE).strength(-1.0F).dropsNothing();
	public static final Block PRESERVATION_STONE = new Block(preservationBlockSettings);
	public static final Block DIKE_CHISELED_PRESERVATION_STONE = new Block(FabricBlockSettings.copyOf(PRESERVATION_STONE).luminance(6));
	public static final Block DIKE_GATE_FOUNTAIN = new SpectrumFacingBlock(preservationBlockSettings);
	public static final Block PRESERVATION_BRICKS = new Block(preservationBlockSettings);
	public static final Block SHIMMERING_PRESERVATION_BRICKS = new Block(FabricBlockSettings.copyOf(preservationBlockSettings).luminance(5));
	public static final Block COURIER_STATUE = new StatueBlock(preservationBlockSettings);
	private static final FabricBlockSettings preservationGlassBlockSettings = FabricBlockSettings.of(Material.GLASS).strength(-1.0F).dropsNothing().sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(SpectrumBlocks::never).solidBlock(SpectrumBlocks::never).suffocates(SpectrumBlocks::never).blockVision(SpectrumBlocks::never);
	public static final Block PRESERVATION_GLASS = new GlassBlock(preservationGlassBlockSettings);
	public static final Block TINTED_PRESERVATION_GLASS = new GlassBlock(FabricBlockSettings.copyOf(PRESERVATION_GLASS).luminance(12).hardness(Float.MAX_VALUE));
	private static final BiMap<SpectrumSkullBlock.SpectrumSkullBlockType, Block> MOB_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.SpectrumSkullBlockType.class);
	private static final BiMap<SpectrumSkullBlock.SpectrumSkullBlockType, Block> MOB_WALL_HEADS = EnumHashBiMap.create(SpectrumSkullBlock.SpectrumSkullBlockType.class);
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
	private static final FabricBlockSettings shootingStartBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque();
	public static final ShootingStarBlock GLISTERING_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GLISTERING);
	public static final ShootingStarBlock FIERY_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.FIERY);
	public static final ShootingStarBlock COLORFUL_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.COLORFUL);
	public static final ShootingStarBlock PRISTINE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.PRISTINE);
	public static final ShootingStarBlock GEMSTONE_SHOOTING_STAR = new ShootingStarBlock(shootingStartBlockSettings, ShootingStarBlock.Type.GEMSTONE);
	public static FabricBlockSettings mobBlockSettings = FabricBlockSettings.of(new Material.Builder(MapColor.BLUE).build()).sounds(BlockSoundGroup.BONE).strength(3.0F).nonOpaque();
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
	public static final Block ZOMBIE_MOB_BLOCK = new StatusEffectMobBlock(FabricBlockSettings.copyOf(mobBlockSettings).sounds(SpectrumBlockSoundGroups.ZOMBIE_MOB_BLOCK), ParticleTypes.ENCHANTED_HIT, StatusEffects.HUNGER, 7, 200);

	private static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}

	private static boolean always(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	private static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}
	
	private static void registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
	}
	
	private static void registerBlockItem(String name, BlockItem blockItem, DyeColor dyeColor) {
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	private static void registerBlockWithItem(String name, Block block, FabricItemSettings itemSettings, DyeColor dyeColor) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	private static void registerBlockWithItem(String name, Block block, BlockItem blockItem, DyeColor dyeColor) {
		Registry.register(Registry.BLOCK, new Identifier(SpectrumCommon.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), blockItem);
		ItemColors.ITEM_COLORS.registerColorMapping(blockItem, dyeColor);
	}
	
	public static void register() {
		registerBlockWithItem("pedestal_basic_topaz", PEDESTAL_BASIC_TOPAZ, new PedestalBlockItem(PEDESTAL_BASIC_TOPAZ, generalItemSettingsSingle, BuiltinPedestalVariant.BASIC_TOPAZ, "item.spectrum.pedestal.tooltip.basic_topaz"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_basic_amethyst", PEDESTAL_BASIC_AMETHYST, new PedestalBlockItem(PEDESTAL_BASIC_AMETHYST, generalItemSettingsSingle, BuiltinPedestalVariant.BASIC_AMETHYST, "item.spectrum.pedestal.tooltip.basic_amethyst"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_basic_citrine", PEDESTAL_BASIC_CITRINE, new PedestalBlockItem(PEDESTAL_BASIC_CITRINE, generalItemSettingsSingle, BuiltinPedestalVariant.BASIC_CITRINE, "item.spectrum.pedestal.tooltip.basic_citrine"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_all_basic", PEDESTAL_ALL_BASIC, new PedestalBlockItem(PEDESTAL_ALL_BASIC, generalItemSettingsSingle, BuiltinPedestalVariant.CMY, "item.spectrum.pedestal.tooltip.all_basic"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_onyx", PEDESTAL_ONYX, new PedestalBlockItem(PEDESTAL_ONYX, generalItemSettingsSingle, BuiltinPedestalVariant.ONYX, "item.spectrum.pedestal.tooltip.onyx"), DyeColor.WHITE);
		registerBlockWithItem("pedestal_moonstone", PEDESTAL_MOONSTONE, new PedestalBlockItem(PEDESTAL_MOONSTONE, generalItemSettingsSingle, BuiltinPedestalVariant.MOONSTONE, "item.spectrum.pedestal.tooltip.moonstone"), DyeColor.WHITE);
		registerBlockWithItem("fusion_shrine_basalt", FUSION_SHRINE_BASALT, generalItemSettingsSingle, DyeColor.GRAY);
		registerBlockWithItem("fusion_shrine_calcite", FUSION_SHRINE_CALCITE, generalItemSettingsSingle, DyeColor.GRAY);
		registerBlockWithItem("enchanter", ENCHANTER, generalItemSettingsSingle, DyeColor.PURPLE);
		registerBlockWithItem("item_bowl_basalt", ITEM_BOWL_BASALT, generalItemSettingsSixteen, DyeColor.PINK);
		registerBlockWithItem("item_bowl_calcite", ITEM_BOWL_CALCITE, generalItemSettingsSixteen, DyeColor.PINK);
		registerBlockWithItem("potion_workshop", POTION_WORKSHOP, generalItemSettingsSingle, DyeColor.PURPLE);
		
		registerBlockWithItem("spirit_instiller", SPIRIT_INSTILLER, generalItemSettingsSingle, DyeColor.WHITE);
		registerBlockWithItem("memory", MEMORY, new MemoryItem(MEMORY, generalItemSettingsSingleUncommon), DyeColor.LIGHT_GRAY);
		
		registerBlockWithItem("upgrade_speed", UPGRADE_SPEED, new UpgradeBlockItem(UPGRADE_SPEED, generalItemSettingsEight, "upgrade_speed"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_speed2", UPGRADE_SPEED2, new UpgradeBlockItem(UPGRADE_SPEED2, generalUncommonItemSettingsEight, "upgrade_speed2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_speed3", UPGRADE_SPEED3, new UpgradeBlockItem(UPGRADE_SPEED3, generalRareItemSettingsEight, "upgrade_speed3"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_efficiency", UPGRADE_EFFICIENCY, new UpgradeBlockItem(UPGRADE_EFFICIENCY, generalUncommonItemSettingsEight, "upgrade_efficiency"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_efficiency2", UPGRADE_EFFICIENCY2, new UpgradeBlockItem(UPGRADE_EFFICIENCY2, generalRareItemSettingsEight, "upgrade_efficiency2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_yield", UPGRADE_YIELD, new UpgradeBlockItem(UPGRADE_YIELD, generalUncommonItemSettingsEight, "upgrade_yield"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_yield2", UPGRADE_YIELD2, new UpgradeBlockItem(UPGRADE_YIELD2, generalRareItemSettingsEight, "upgrade_yield2"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_experience", UPGRADE_EXPERIENCE, new UpgradeBlockItem(UPGRADE_EXPERIENCE, generalItemSettingsEight, "upgrade_experience"), DyeColor.LIGHT_GRAY);
		registerBlockWithItem("upgrade_experience2", UPGRADE_EXPERIENCE2, new UpgradeBlockItem(UPGRADE_EXPERIENCE2, generalUncommonItemSettingsEight, "upgrade_experience2"), DyeColor.LIGHT_GRAY);
		
		registerStructureBlocks(generalItemSettings);
		registerJadeVineBlocks(generalItemSettings);
		
		registerMachines(generalItemSettingsEight);
		registerPastelNetworkNodes(generalItemSettingsSixteen);
		registerStoneBlocks(decorationItemSettings);
		registerGemBlocks(worldgenItemSettings);
		registerBlockWithItem("spectral_shard_block", SPECTRAL_SHARD_BLOCK, worldgenItemSettingsRare, DyeColor.WHITE);
		registerBlockWithItem("bedrock_storage_block", BEDROCK_STORAGE_BLOCK, decorationItemSettingsRare, DyeColor.BLACK);
		registerShootingStarBlocks(resourcesItemSettingUncommonSingle);
		
		registerGemOreBlocks(worldgenItemSettings);
		registerOreBlocks(worldgenItemSettings, worldgenItemSettingsFireproof);
		registerOreStorageBlocks(decorationItemSettings, decorationItemSettingsFireProof);
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
		registerMobBlocks(mobBlockItemSettings);
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
		registerBlock("midnight_solution", MIDNIGHT_SOLUTION);
		
		registerBlockWithItem("black_materia", BLACK_MATERIA, generalItemSettings, DyeColor.GRAY);
		registerBlockWithItem("frostbite_crystal", FROSTBITE_CRYSTAL, generalItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("blazing_crystal", BLAZING_CRYSTAL, generalItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("resonant_lily", RESONANT_LILY, generalItemSettings, DyeColor.GREEN);
		registerBlockWithItem("clover", CLOVER, worldgenItemSettings, DyeColor.LIME);
		registerBlockWithItem("four_leaf_clover", FOUR_LEAF_CLOVER, new FourLeafCloverItem(FOUR_LEAF_CLOVER, worldgenItemSettings, new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_four_leaf_clover"), CLOVER.asItem()), DyeColor.LIME);
		
		// Worldgen
		registerBlockWithItem("quitoxic_reeds", QUITOXIC_REEDS, worldgenItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("mermaids_brush", MERMAIDS_BRUSH, worldgenItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("ender_treasure", ENDER_TREASURE, worldgenItemSettings, DyeColor.PURPLE);
		
		registerBlockWithItem("bedrock_anvil", BEDROCK_ANVIL, generalItemSettings, DyeColor.BLACK);
		registerBlockWithItem("cracked_end_portal_frame", CRACKED_END_PORTAL_FRAME, generalItemSettings, DyeColor.PURPLE);
		
		// Technical Blocks without items
		registerBlock("deeper_down_portal", DEEPER_DOWN_PORTAL);
		registerBlock("glistering_melon_stem", GLISTERING_MELON_STEM);
		registerBlock("attached_glistering_melon_stem", ATTACHED_GLISTERING_MELON_STEM);
		registerBlock("stuck_lightning_stone", STUCK_LIGHTNING_STONE);
		registerBlock("wand_light", WAND_LIGHT_BLOCK);
		registerBlock("decaying_light", DECAYING_LIGHT_BLOCK);
		registerBlock("block_flooder", BLOCK_FLOODER);
		registerBlock("bottomless_bundle", BOTTOMLESS_BUNDLE);
	}
	
	private static void registerRedstone(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("light_level_detector", LIGHT_LEVEL_DETECTOR, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("weather_detector", WEATHER_DETECTOR, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("item_detector", ITEM_DETECTOR, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("player_detector", PLAYER_DETECTOR, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("entity_detector", ENTITY_DETECTOR, fabricItemSettings, DyeColor.RED);
		
		registerBlockWithItem("redstone_timer", REDSTONE_TIMER, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("redstone_calculator", REDSTONE_CALCULATOR, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("redstone_wireless", REDSTONE_WIRELESS, fabricItemSettings, DyeColor.RED);
		
		registerBlockWithItem("redstone_sand", REDSTONE_SAND, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("ender_glass", ENDER_GLASS, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("block_placer", BLOCK_PLACER, fabricItemSettings, DyeColor.CYAN);
	}
	
	private static void registerMagicalBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("private_chest", PRIVATE_CHEST, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("compacting_chest", COMPACTING_CHEST, generalItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("restocking_chest", RESTOCKING_CHEST, generalItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("sucking_chest", SUCKING_CHEST, generalItemSettings, DyeColor.LIGHT_GRAY);
		
		registerBlockWithItem("ender_hopper", ENDER_HOPPER, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("ender_dropper", ENDER_DROPPER, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("particle_spawner", PARTICLE_SPAWNER, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("creative_particle_spawner", CREATIVE_PARTICLE_SPAWNER, new BlockItem(CREATIVE_PARTICLE_SPAWNER, new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.EPIC)), DyeColor.PINK);
		
		registerBlockWithItem("glistering_melon", GLISTERING_MELON, generalItemSettings, DyeColor.LIME);
		
		registerBlockWithItem("lava_sponge", LAVA_SPONGE, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("wet_lava_sponge", WET_LAVA_SPONGE, new WetLavaSpongeItem(WET_LAVA_SPONGE, new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(1).recipeRemainder(LAVA_SPONGE.asItem())), DyeColor.ORANGE);
		
		registerBlockWithItem("ethereal_platform", ETHEREAL_PLATFORM, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("universe_spyhole", UNIVERSE_SPYHOLE, fabricItemSettings, DyeColor.LIGHT_GRAY);
	}
	
	private static void registerPigmentStorageBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_block", WHITE_BLOCK, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_block", ORANGE_BLOCK, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_block", MAGENTA_BLOCK, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_block", LIGHT_BLUE_BLOCK, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_block", YELLOW_BLOCK, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_block", LIME_BLOCK, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_block", PINK_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_block", GRAY_BLOCK, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_block", LIGHT_GRAY_BLOCK, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_block", CYAN_BLOCK, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_block", PURPLE_BLOCK, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_block", BLUE_BLOCK, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_block", BROWN_BLOCK, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_block", GREEN_BLOCK, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_block", RED_BLOCK, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_block", BLACK_BLOCK, fabricItemSettings, DyeColor.BLACK);
	}
	
	private static void registerSpiritTree(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("ominous_sapling", OMINOUS_SAPLING, new OminousSaplingBlockItem(OMINOUS_SAPLING, fabricItemSettings), DyeColor.GREEN);
		
		registerBlockWithItem("spirit_sallow_roots", SPIRIT_SALLOW_ROOTS, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_log", SPIRIT_SALLOW_LOG, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_leaves", SPIRIT_SALLOW_LEAVES, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("spirit_sallow_heart", SPIRIT_SALLOW_HEART, fabricItemSettings, DyeColor.GREEN);
		
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
		
		registerBlockWithItem("sacred_soil", SACRED_SOIL, fabricItemSettings, DyeColor.LIME);
	}
	
	private static void registerOreBlocks(FabricItemSettings fabricItemSettings, FabricItemSettings fabricItemSettingsFireProof) {
		registerBlockWithItem("sparklestone_ore", SPARKLESTONE_ORE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_sparklestone_ore", DEEPSLATE_SPARKLESTONE_ORE, fabricItemSettings, DyeColor.YELLOW);
		
		registerBlockWithItem("azurite_ore", AZURITE_ORE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("deepslate_azurite_ore", DEEPSLATE_AZURITE_ORE, fabricItemSettings, DyeColor.BLUE);
		
		registerBlockWithItem("scarlet_ore", SCARLET_ORE, new FloatBlockItem(SCARLET_ORE, fabricItemSettingsFireProof, 1.01F), DyeColor.RED);
		registerBlockWithItem("paletur_ore", PALETUR_ORE, new FloatBlockItem(PALETUR_ORE, fabricItemSettings, 0.99F), DyeColor.CYAN);
	}
	
	private static void registerOreStorageBlocks(FabricItemSettings fabricItemSettings, FabricItemSettings fabricItemSettingsFireProof) {
		registerBlockWithItem("topaz_storage_block", TOPAZ_STORAGE_BLOCK, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_storage_block", AMETHYST_STORAGE_BLOCK, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_storage_block", CITRINE_STORAGE_BLOCK, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_storage_block", ONYX_STORAGE_BLOCK, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_storage_block", MOONSTONE_STORAGE_BLOCK, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("spectral_shard_storage_block", SPECTRAL_SHARD_STORAGE_BLOCK, fabricItemSettings, DyeColor.WHITE);
		
		registerBlockWithItem("azurite_block", AZURITE_BLOCK, decorationItemSettings, DyeColor.BLUE);
		registerBlockWithItem("sparklestone_block", SPARKLESTONE_BLOCK, decorationItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("scarlet_fragment_block", SCARLET_FRAGMENT_BLOCK, new FloatBlockItem(SCARLET_FRAGMENT_BLOCK, fabricItemSettingsFireProof, 1.02F), DyeColor.RED);
		registerBlockWithItem("paletur_fragment_block", PALETUR_FRAGMENT_BLOCK, new FloatBlockItem(PALETUR_FRAGMENT_BLOCK, fabricItemSettings, 0.98F), DyeColor.CYAN);
		registerBlockWithItem("hover_block", HOVER_BLOCK, new FloatBlockItem(HOVER_BLOCK, fabricItemSettings, 0.996F), DyeColor.GREEN);
	}
	
	private static void registerColoredLamps(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_lamp", WHITE_LAMP, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_lamp", ORANGE_LAMP, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_lamp", MAGENTA_LAMP, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_lamp", LIGHT_BLUE_LAMP, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_lamp", YELLOW_LAMP, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_lamp", LIME_LAMP, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_lamp", PINK_LAMP, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_lamp", GRAY_LAMP, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_lamp", LIGHT_GRAY_LAMP, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_lamp", CYAN_LAMP, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_lamp", PURPLE_LAMP, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_lamp", BLUE_LAMP, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_lamp", BROWN_LAMP, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_lamp", GREEN_LAMP, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_lamp", RED_LAMP, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_lamp", BLACK_LAMP, fabricItemSettings, DyeColor.BLACK);
	}
	
	private static void registerGemstoneGlass(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_glass", TOPAZ_GLASS, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_glass", AMETHYST_GLASS, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_glass", CITRINE_GLASS, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_glass", ONYX_GLASS, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_glass", MOONSTONE_GLASS, fabricItemSettings, DyeColor.WHITE);
		
		registerBlockWithItem("glowing_glass", RADIANT_GLASS, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerPlayerOnlyGlass(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("vanilla_player_only_glass", VANILLA_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("tinted_player_only_glass", TINTED_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("glowing_player_only_glass", RADIANT_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.YELLOW);
		
		registerBlockWithItem("topaz_player_only_glass", TOPAZ_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_player_only_glass", AMETHYST_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_player_only_glass", CITRINE_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_player_only_glass", ONYX_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_player_only_glass", MOONSTONE_PLAYER_ONLY_GLASS, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerGemstoneChimes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_chime", TOPAZ_CHIME, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chime", AMETHYST_CHIME, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chime", CITRINE_CHIME, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chime", ONYX_CHIME, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chime", MOONSTONE_CHIME, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerDecoStones(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("amethyst_decostone", AMETHYST_DECOSTONE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("topaz_decostone", TOPAZ_DECOSTONE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("citrine_decostone", CITRINE_DECOSTONE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_decostone", ONYX_DECOSTONE, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_decostone", MOONSTONE_DECOSTONE, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerStoneBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("smooth_basalt_slab", SMOOTH_BASALT_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("smooth_basalt_wall", SMOOTH_BASALT_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("smooth_basalt_stairs", SMOOTH_BASALT_STAIRS, fabricItemSettings, DyeColor.BROWN);
		
		registerBlockWithItem("calcite_slab", CALCITE_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_wall", CALCITE_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_stairs", CALCITE_STAIRS, fabricItemSettings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_basalt", POLISHED_BASALT, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_pillar", POLISHED_BASALT_PILLAR, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_crest", POLISHED_BASALT_CREST, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("chiseled_polished_basalt", CHISELED_POLISHED_BASALT, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("notched_polished_basalt", NOTCHED_POLISHED_BASALT, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_slab", POLISHED_BASALT_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_wall", POLISHED_BASALT_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_basalt_stairs", POLISHED_BASALT_STAIRS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("basalt_bricks", BASALT_BRICKS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_slab", BASALT_BRICK_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_wall", BASALT_BRICK_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("basalt_brick_stairs", BASALT_BRICK_STAIRS, fabricItemSettings, DyeColor.BROWN);
		
		registerBlockWithItem("polished_calcite", POLISHED_CALCITE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_pillar", POLISHED_CALCITE_PILLAR, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_crest", POLISHED_CALCITE_CREST, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("chiseled_polished_calcite", CHISELED_POLISHED_CALCITE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("notched_polished_calcite", NOTCHED_POLISHED_CALCITE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_slab", POLISHED_CALCITE_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_wall", POLISHED_CALCITE_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("polished_calcite_stairs", POLISHED_CALCITE_STAIRS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_bricks", CALCITE_BRICKS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_slab", CALCITE_BRICK_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_wall", CALCITE_BRICK_WALL, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("calcite_brick_stairs", CALCITE_BRICK_STAIRS, fabricItemSettings, DyeColor.BROWN);
	}
	
	private static void registerRunes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_chiseled_basalt", TOPAZ_CHISELED_BASALT, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chiseled_basalt", AMETHYST_CHISELED_BASALT, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chiseled_basalt", CITRINE_CHISELED_BASALT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chiseled_basalt", ONYX_CHISELED_BASALT, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chiseled_basalt", MOONSTONE_CHISELED_BASALT, fabricItemSettings, DyeColor.WHITE);
		
		registerBlockWithItem("topaz_chiseled_calcite", TOPAZ_CHISELED_CALCITE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_chiseled_calcite", AMETHYST_CHISELED_CALCITE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_chiseled_calcite", CITRINE_CHISELED_CALCITE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_chiseled_calcite", ONYX_CHISELED_CALCITE, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_chiseled_calcite", MOONSTONE_CHISELED_CALCITE, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerGemstoneLamps(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_calcite_lamp", TOPAZ_CALCITE_LAMP, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_calcite_lamp", AMETHYST_CALCITE_LAMP, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_calcite_lamp", CITRINE_CALCITE_LAMP, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_calcite_lamp", ONYX_CALCITE_LAMP, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_calcite_lamp", MOONSTONE_CALCITE_LAMP, fabricItemSettings, DyeColor.WHITE);
		
		registerBlockWithItem("topaz_basalt_lamp", TOPAZ_BASALT_LAMP, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_basalt_lamp", AMETHYST_BASALT_LAMP, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_basalt_lamp", CITRINE_BASALT_LAMP, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_basalt_lamp", ONYX_BASALT_LAMP, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_basalt_lamp", MOONSTONE_BASALT_LAMP, fabricItemSettings, DyeColor.WHITE);
	}
	
	private static void registerColoredWood(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_log", WHITE_LOG, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_log", ORANGE_LOG, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_log", MAGENTA_LOG, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_log", LIGHT_BLUE_LOG, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_log", YELLOW_LOG, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_log", LIME_LOG, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_log", PINK_LOG, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_log", GRAY_LOG, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_log", LIGHT_GRAY_LOG, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_log", CYAN_LOG, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_log", PURPLE_LOG, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_log", BLUE_LOG, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_log", BROWN_LOG, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_log", GREEN_LOG, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_log", RED_LOG, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_log", BLACK_LOG, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_leaves", WHITE_LEAVES, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_leaves", ORANGE_LEAVES, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_leaves", MAGENTA_LEAVES, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_leaves", LIGHT_BLUE_LEAVES, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_leaves", YELLOW_LEAVES, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_leaves", LIME_LEAVES, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_leaves", PINK_LEAVES, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_leaves", GRAY_LEAVES, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_leaves", LIGHT_GRAY_LEAVES, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_leaves", CYAN_LEAVES, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_leaves", PURPLE_LEAVES, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_leaves", BLUE_LEAVES, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_leaves", BROWN_LEAVES, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_leaves", GREEN_LEAVES, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_leaves", RED_LEAVES, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_leaves", BLACK_LEAVES, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_sapling", WHITE_SAPLING, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_sapling", ORANGE_SAPLING, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_sapling", MAGENTA_SAPLING, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_sapling", LIGHT_BLUE_SAPLING, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_sapling", YELLOW_SAPLING, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_sapling", LIME_SAPLING, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_sapling", PINK_SAPLING, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_sapling", GRAY_SAPLING, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_sapling", LIGHT_GRAY_SAPLING, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_sapling", CYAN_SAPLING, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_sapling", PURPLE_SAPLING, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_sapling", BLUE_SAPLING, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_sapling", BROWN_SAPLING, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_sapling", GREEN_SAPLING, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_sapling", RED_SAPLING, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_sapling", BLACK_SAPLING, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_planks", WHITE_PLANKS, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_planks", ORANGE_PLANKS, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_planks", MAGENTA_PLANKS, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_planks", LIGHT_BLUE_PLANKS, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_planks", YELLOW_PLANKS, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_planks", LIME_PLANKS, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_planks", PINK_PLANKS, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_planks", GRAY_PLANKS, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_planks", LIGHT_GRAY_PLANKS, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_planks", CYAN_PLANKS, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_planks", PURPLE_PLANKS, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_planks", BLUE_PLANKS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_planks", BROWN_PLANKS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_planks", GREEN_PLANKS, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_planks", RED_PLANKS, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_planks", BLACK_PLANKS, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_stairs", WHITE_PLANK_STAIRS, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_stairs", ORANGE_PLANK_STAIRS, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_stairs", MAGENTA_PLANK_STAIRS, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_stairs", LIGHT_BLUE_PLANK_STAIRS, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_stairs", YELLOW_PLANK_STAIRS, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_stairs", LIME_PLANK_STAIRS, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_stairs", PINK_PLANK_STAIRS, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_stairs", GRAY_PLANK_STAIRS, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_stairs", LIGHT_GRAY_PLANK_STAIRS, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_stairs", CYAN_PLANK_STAIRS, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_stairs", PURPLE_PLANK_STAIRS, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_stairs", BLUE_PLANK_STAIRS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_stairs", BROWN_PLANK_STAIRS, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_stairs", GREEN_PLANK_STAIRS, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_stairs", RED_PLANK_STAIRS, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_stairs", BLACK_PLANK_STAIRS, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_pressure_plate", WHITE_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_pressure_plate", ORANGE_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_pressure_plate", MAGENTA_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_pressure_plate", LIGHT_BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_pressure_plate", YELLOW_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_pressure_plate", LIME_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_pressure_plate", PINK_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_pressure_plate", GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_pressure_plate", LIGHT_GRAY_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_pressure_plate", CYAN_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_pressure_plate", PURPLE_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_pressure_plate", BLUE_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_pressure_plate", BROWN_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_pressure_plate", GREEN_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_pressure_plate", RED_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_pressure_plate", BLACK_PLANK_PRESSURE_PLATE, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_fence", WHITE_PLANK_FENCE, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_fence", ORANGE_PLANK_FENCE, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_fence", MAGENTA_PLANK_FENCE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_fence", LIGHT_BLUE_PLANK_FENCE, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_fence", YELLOW_PLANK_FENCE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_fence", LIME_PLANK_FENCE, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_fence", PINK_PLANK_FENCE, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_fence", GRAY_PLANK_FENCE, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_fence", LIGHT_GRAY_PLANK_FENCE, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_fence", CYAN_PLANK_FENCE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_fence", PURPLE_PLANK_FENCE, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_fence", BLUE_PLANK_FENCE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_fence", BROWN_PLANK_FENCE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_fence", GREEN_PLANK_FENCE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_fence", RED_PLANK_FENCE, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_fence", BLACK_PLANK_FENCE, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_fence_gate", WHITE_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_fence_gate", ORANGE_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_fence_gate", MAGENTA_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_fence_gate", LIGHT_BLUE_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_fence_gate", YELLOW_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_fence_gate", LIME_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_fence_gate", PINK_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_fence_gate", GRAY_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_fence_gate", LIGHT_GRAY_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_fence_gate", CYAN_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_fence_gate", PURPLE_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_fence_gate", BLUE_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_fence_gate", BROWN_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_fence_gate", GREEN_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_fence_gate", RED_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_fence_gate", BLACK_PLANK_FENCE_GATE, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_button", WHITE_PLANK_BUTTON, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_button", ORANGE_PLANK_BUTTON, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_button", MAGENTA_PLANK_BUTTON, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_button", LIGHT_BLUE_PLANK_BUTTON, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_button", YELLOW_PLANK_BUTTON, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_button", LIME_PLANK_BUTTON, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_button", PINK_PLANK_BUTTON, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_button", GRAY_PLANK_BUTTON, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_button", LIGHT_GRAY_PLANK_BUTTON, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_button", CYAN_PLANK_BUTTON, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_button", PURPLE_PLANK_BUTTON, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_button", BLUE_PLANK_BUTTON, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_button", BROWN_PLANK_BUTTON, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_button", GREEN_PLANK_BUTTON, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_button", RED_PLANK_BUTTON, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_button", BLACK_PLANK_BUTTON, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("white_plank_slab", WHITE_PLANK_SLAB, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_plank_slab", ORANGE_PLANK_SLAB, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_plank_slab", MAGENTA_PLANK_SLAB, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_plank_slab", LIGHT_BLUE_PLANK_SLAB, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_plank_slab", YELLOW_PLANK_SLAB, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_plank_slab", LIME_PLANK_SLAB, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_plank_slab", PINK_PLANK_SLAB, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_plank_slab", GRAY_PLANK_SLAB, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_plank_slab", LIGHT_GRAY_PLANK_SLAB, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_plank_slab", CYAN_PLANK_SLAB, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_plank_slab", PURPLE_PLANK_SLAB, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_plank_slab", BLUE_PLANK_SLAB, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_plank_slab", BROWN_PLANK_SLAB, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_plank_slab", GREEN_PLANK_SLAB, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_plank_slab", RED_PLANK_SLAB, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_plank_slab", BLACK_PLANK_SLAB, fabricItemSettings, DyeColor.BLACK);
	}
	
	private static void registerGlowBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_glowblock", WHITE_GLOWBLOCK, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_glowblock", ORANGE_GLOWBLOCK, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_glowblock", MAGENTA_GLOWBLOCK, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_glowblock", LIGHT_BLUE_GLOWBLOCK, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_glowblock", YELLOW_GLOWBLOCK, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_glowblock", LIME_GLOWBLOCK, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_glowblock", PINK_GLOWBLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_glowblock", GRAY_GLOWBLOCK, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_glowblock", LIGHT_GRAY_GLOWBLOCK, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_glowblock", CYAN_GLOWBLOCK, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_glowblock", PURPLE_GLOWBLOCK, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_glowblock", BLUE_GLOWBLOCK, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_glowblock", BROWN_GLOWBLOCK, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_glowblock", GREEN_GLOWBLOCK, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_glowblock", RED_GLOWBLOCK, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_glowblock", BLACK_GLOWBLOCK, fabricItemSettings, DyeColor.BLACK);
	}
	
	public static void registerSparklestoneLights(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("basalt_sparklestone_light", BASALT_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("calcite_sparklestone_light", CALCITE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("stone_sparklestone_light", STONE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("granite_sparklestone_light", GRANITE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("diorite_sparklestone_light", DIORITE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("andesite_sparklestone_light", ANDESITE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_sparklestone_light", DEEPSLATE_SPARKLESTONE_LIGHT, fabricItemSettings, DyeColor.YELLOW);
	}
	
	public static void registerShootingStarBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("shooting_star_glistering", GLISTERING_SHOOTING_STAR, new ShootingStarItem(GLISTERING_SHOOTING_STAR, fabricItemSettings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_fiery", FIERY_SHOOTING_STAR, new ShootingStarItem(FIERY_SHOOTING_STAR, fabricItemSettings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_colorful", COLORFUL_SHOOTING_STAR, new ShootingStarItem(COLORFUL_SHOOTING_STAR, fabricItemSettings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_pristine", PRISTINE_SHOOTING_STAR, new ShootingStarItem(PRISTINE_SHOOTING_STAR, fabricItemSettings), DyeColor.PURPLE);
		registerBlockWithItem("shooting_star_gemstone", GEMSTONE_SHOOTING_STAR, new ShootingStarItem(GEMSTONE_SHOOTING_STAR, fabricItemSettings), DyeColor.PURPLE);
	}
	
	public static void registerPastelNetworkNodes(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("connection_node", CONNECTION_NODE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("provider_node", PROVIDER_NODE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("storage_node", STORAGE_NODE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("pusher_node", PUSHER_NODE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("puller_node", PULLER_NODE, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("interaction_node", INTERACTION_NODE, fabricItemSettings, DyeColor.GREEN);
	}
	
	public static void registerMachines(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("crystal_apothecary", CRYSTAL_APOTHECARY, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("color_picker", COLOR_PICKER, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("inkwell", INKWELL, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("ink_duct", INK_DUCT, fabricItemSettings, DyeColor.GREEN);
	}
	
	public static void registerSporeBlossoms(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("white_spore_blossom", WHITE_SPORE_BLOSSOM, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("orange_spore_blossom", ORANGE_SPORE_BLOSSOM, fabricItemSettings, DyeColor.ORANGE);
		registerBlockWithItem("magenta_spore_blossom", MAGENTA_SPORE_BLOSSOM, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("light_blue_spore_blossom", LIGHT_BLUE_SPORE_BLOSSOM, fabricItemSettings, DyeColor.LIGHT_BLUE);
		registerBlockWithItem("yellow_spore_blossom", YELLOW_SPORE_BLOSSOM, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("lime_spore_blossom", LIME_SPORE_BLOSSOM, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("pink_spore_blossom", PINK_SPORE_BLOSSOM, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("gray_spore_blossom", GRAY_SPORE_BLOSSOM, fabricItemSettings, DyeColor.GRAY);
		registerBlockWithItem("light_gray_spore_blossom", LIGHT_GRAY_SPORE_BLOSSOM, fabricItemSettings, DyeColor.LIGHT_GRAY);
		registerBlockWithItem("cyan_spore_blossom", CYAN_SPORE_BLOSSOM, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("purple_spore_blossom", PURPLE_SPORE_BLOSSOM, fabricItemSettings, DyeColor.PURPLE);
		registerBlockWithItem("blue_spore_blossom", BLUE_SPORE_BLOSSOM, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("brown_spore_blossom", BROWN_SPORE_BLOSSOM, fabricItemSettings, DyeColor.BROWN);
		registerBlockWithItem("green_spore_blossom", GREEN_SPORE_BLOSSOM, fabricItemSettings, DyeColor.GREEN);
		registerBlockWithItem("red_spore_blossom", RED_SPORE_BLOSSOM, fabricItemSettings, DyeColor.RED);
		registerBlockWithItem("black_spore_blossom", BLACK_SPORE_BLOSSOM, fabricItemSettings, DyeColor.BLACK);
	}
	
	private static void registerGemBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("topaz_block", TOPAZ_BLOCK, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("budding_topaz", BUDDING_TOPAZ, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("small_topaz_bud", SMALL_TOPAZ_BUD, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("medium_topaz_bud", MEDIUM_TOPAZ_BUD, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("large_topaz_bud", LARGE_TOPAZ_BUD, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("topaz_cluster", TOPAZ_CLUSTER, fabricItemSettings, DyeColor.CYAN);
		
		registerBlockWithItem("citrine_block", CITRINE_BLOCK, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("budding_citrine", BUDDING_CITRINE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("small_citrine_bud", SMALL_CITRINE_BUD, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("medium_citrine_bud", MEDIUM_CITRINE_BUD, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("large_citrine_bud", LARGE_CITRINE_BUD, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("citrine_cluster", CITRINE_CLUSTER, fabricItemSettings, DyeColor.YELLOW);
		
		registerBlockWithItem("onyx_block", ONYX_BLOCK, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("budding_onyx", BUDDING_ONYX, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("small_onyx_bud", SMALL_ONYX_BUD, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("medium_onyx_bud", MEDIUM_ONYX_BUD, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("large_onyx_bud", LARGE_ONYX_BUD, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("onyx_cluster", ONYX_CLUSTER, fabricItemSettings, DyeColor.BLACK);
		
		registerBlockWithItem("moonstone_block", MOONSTONE_BLOCK, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("budding_moonstone", BUDDING_MOONSTONE, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("small_moonstone_bud", SMALL_MOONSTONE_BUD, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("medium_moonstone_bud", MEDIUM_MOONSTONE_BUD, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("large_moonstone_bud", LARGE_MOONSTONE_BUD, fabricItemSettings, DyeColor.WHITE);
		registerBlockWithItem("moonstone_cluster", MOONSTONE_CLUSTER, fabricItemSettings, DyeColor.WHITE);
		
	}
	
	private static void registerGemOreBlocks(FabricItemSettings fabricItemSettings) {
		// stone ores
		registerBlockWithItem("topaz_ore", TOPAZ_ORE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("amethyst_ore", AMETHYST_ORE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("citrine_ore", CITRINE_ORE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("onyx_ore", ONYX_ORE, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("moonstone_ore", MOONSTONE_ORE, fabricItemSettings, DyeColor.WHITE);
		
		// deepslate ores
		registerBlockWithItem("deepslate_topaz_ore", DEEPSLATE_TOPAZ_ORE, fabricItemSettings, DyeColor.CYAN);
		registerBlockWithItem("deepslate_amethyst_ore", DEEPSLATE_AMETHYST_ORE, fabricItemSettings, DyeColor.MAGENTA);
		registerBlockWithItem("deepslate_citrine_ore", DEEPSLATE_CITRINE_ORE, fabricItemSettings, DyeColor.YELLOW);
		registerBlockWithItem("deepslate_onyx_ore", DEEPSLATE_ONYX_ORE, fabricItemSettings, DyeColor.BLACK);
		registerBlockWithItem("deepslate_moonstone_ore", DEEPSLATE_MOONSTONE_ORE, fabricItemSettings, DyeColor.WHITE);
		
	}
	
	private static void registerStructureBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("preservation_controller", PRESERVATION_CONTROLLER, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("preservation_stone", PRESERVATION_STONE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("preservation_bricks", PRESERVATION_BRICKS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("shimmering_preservation_bricks", SHIMMERING_PRESERVATION_BRICKS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("dike_chiseled_preservation_stone", DIKE_CHISELED_PRESERVATION_STONE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("preservation_glass", PRESERVATION_GLASS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("tinted_preservation_glass", TINTED_PRESERVATION_GLASS, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("dike_gate_fountain", DIKE_GATE_FOUNTAIN, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("dike_gate", DIKE_GATE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("courier_statue", COURIER_STATUE, fabricItemSettings, DyeColor.BLUE);
		registerBlockWithItem("treasure_chest", TREASURE_CHEST, fabricItemSettings, DyeColor.BLUE);
	}
	
	private static void registerJadeVineBlocks(FabricItemSettings fabricItemSettings) {
		registerBlock("jade_vine_roots", JADE_VINE_ROOTS);
		registerBlock("jade_vine_bulb", JADE_VINE_BULB);
		registerBlock("jade_vines", JADE_VINES);
		
		registerBlockWithItem("jade_vine_petal_block", JADE_VINE_PETAL_BLOCK, fabricItemSettings, DyeColor.LIME);
		registerBlockWithItem("jade_vine_petal_carpet", JADE_VINE_PETAL_CARPET, fabricItemSettings, DyeColor.LIME);
	}
	
	private static void registerMobBlocks(FabricItemSettings fabricItemSettings) {
		registerBlockWithItem("axolotl_mob_block", AXOLOTL_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("bat_mob_block", BAT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("bee_mob_block", BEE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("blaze_mob_block", BLAZE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("cat_mob_block", CAT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("chicken_mob_block", CHICKEN_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("cow_mob_block", COW_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("creeper_mob_block", CREEPER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("ender_dragon_mob_block", ENDER_DRAGON_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("enderman_mob_block", ENDERMAN_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("endermite_mob_block", ENDERMITE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("evoker_mob_block", EVOKER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("fish_mob_block", FISH_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("fox_mob_block", FOX_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("ghast_mob_block", GHAST_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("glow_squid_mob_block", GLOW_SQUID_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("goat_mob_block", GOAT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("guardian_mob_block", GUARDIAN_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("horse_mob_block", HORSE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("illusioner_mob_block", ILLUSIONER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("ocelot_mob_block", OCELOT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("parrot_mob_block", PARROT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("phantom_mob_block", PHANTOM_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("pig_mob_block", PIG_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("piglin_mob_block", PIGLIN_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("polar_bear_mob_block", POLAR_BEAR_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("pufferfish_mob_block", PUFFERFISH_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("rabbit_mob_block", RABBIT_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("sheep_mob_block", SHEEP_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("shulker_mob_block", SHULKER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("silverfish_mob_block", SILVERFISH_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("skeleton_mob_block", SKELETON_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("slime_mob_block", SLIME_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("snow_golem_mob_block", SNOW_GOLEM_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("spider_mob_block", SPIDER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("squid_mob_block", SQUID_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("stray_mob_block", STRAY_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("strider_mob_block", STRIDER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("turtle_mob_block", TURTLE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("witch_mob_block", WITCH_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("wither_mob_block", WITHER_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("wither_skeleton_mob_block", WITHER_SKELETON_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
		registerBlockWithItem("zombie_mob_block", ZOMBIE_MOB_BLOCK, fabricItemSettings, DyeColor.PINK);
	}
	
	// Most mob heads vanilla is missing (vanilla only has: skeleton, wither skeleton, zombie, player, creeper, ender dragon)
	private static void registerMobHeads(FabricItemSettings fabricItemSettings) {
		for (SpectrumSkullBlock.SpectrumSkullBlockType type : SpectrumSkullBlock.SpectrumSkullBlockType.values()) {
			Block head = new SpectrumSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_head", head);
			Block wallHead = new SpectrumWallSkullBlock(type, FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL).dropsLike(head));
			registerBlock(type.name().toLowerCase(Locale.ROOT) + "_wall_head", wallHead);
			BlockItem headItem = new SpectrumSkullBlockItem(head, wallHead, (fabricItemSettings), type.entityType);
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
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RADIANT_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.RADIANT_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TINTED_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.VANILLA_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.TOPAZ_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.AMETHYST_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CITRINE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MOONSTONE_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ONYX_PLAYER_ONLY_GLASS, RenderLayer.getTranslucent());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ENDER_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.PARTICLE_SPAWNER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CREATIVE_PARTICLE_SPAWNER, RenderLayer.getCutout());
		
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
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.OMINOUS_SAPLING, RenderLayer.getCutout());
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_BASALT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ITEM_BOWL_CALCITE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.MEMORY, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_ROOTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_BULB, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINES, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_PETAL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.JADE_VINE_PETAL_CARPET, RenderLayer.getCutout());
		
		
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.DIKE_GATE, RenderLayer.getTranslucent());
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
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.STUCK_LIGHTNING_STONE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.CLOVER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.FOUR_LEAF_CLOVER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.ETHEREAL_PLATFORM, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.UNIVERSE_SPYHOLE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(SpectrumBlocks.BOTTOMLESS_BUNDLE, RenderLayer.getCutout());
		
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

	/*public static Block getColoredSaplingBlock(DyeColor dyeColor) {
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

	public static Block getColoredBlock(DyeColor dyeColor) {
		return switch (dyeColor) {
			case RED -> RED_BLOCK;
			case BROWN -> BROWN_BLOCK;
			case CYAN -> CYAN_BLOCK;
			case GRAY -> GRAY_BLOCK;
			case GREEN -> GREEN_BLOCK;
			case LIGHT_BLUE -> LIGHT_BLUE_BLOCK;
			case LIGHT_GRAY -> LIGHT_GRAY_BLOCK;
			case BLUE -> BLUE_BLOCK;
			case LIME -> LIME_BLOCK;
			case ORANGE -> ORANGE_BLOCK;
			case PINK -> PINK_BLOCK;
			case PURPLE -> PURPLE_BLOCK;
			case WHITE -> WHITE_BLOCK;
			case YELLOW -> YELLOW_BLOCK;
			case BLACK -> BLACK_BLOCK;
			default -> MAGENTA_BLOCK;
		};
	}*/
	
}
