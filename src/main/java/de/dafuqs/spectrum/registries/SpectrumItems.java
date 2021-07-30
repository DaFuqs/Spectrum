package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.gravity.GravityItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.enums.SpectrumColor;
import de.dafuqs.spectrum.items.item_frame.InvisibleGlowItemFrameItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleItemFrameItem;
import de.dafuqs.spectrum.items.materials.*;
import de.dafuqs.spectrum.items.misc.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static de.dafuqs.spectrum.registries.SpectrumFluids.STILL_LIQUID_CRYSTAL;
import static de.dafuqs.spectrum.registries.SpectrumFluids.STILL_MUD;

public class SpectrumItems {

    public static FabricItemSettings generalItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(64);
    public static FabricItemSettings generalItemSettingsSingle = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(1);
    public static FabricItemSettings generalUncommonItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(64).rarity(Rarity.UNCOMMON);
    public static FabricItemSettings spectrumWorldgenItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_WORLDGEN).maxCount(64);
    public static FabricItemSettings spectrumUncommonItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.UNCOMMON).maxCount(64);
    public static FabricItemSettings spectrumRareItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.RARE).maxCount(64);
    public static FabricItemSettings decayPlacerItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(16);
    public static FabricItemSettings musicDiscItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).maxCount(1).rarity(Rarity.RARE);

    public static FabricItemSettings preEnchantedItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS);
    public static FabricItemSettings spectrumBedrockItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.RARE).fireproof();

	// Main items
    public static final Item MANUAL = new ManualItem(generalItemSettingsSingle);
    public static final Item CRAFTING_TABLET = new CraftingTabletItem(generalItemSettingsSingle);

    // Gem shards
    public static final Item TOPAZ_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item CITRINE_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item ONYX_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item MOONSTONE_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item AMMOLITE_ITEM = new Item(spectrumWorldgenItemSettings);
	
    public static final Item TOPAZ_POWDER = new Item(generalItemSettings);
    public static final Item AMETHYST_POWDER = new Item(generalItemSettings);
    public static final Item CITRINE_POWDER = new Item(generalItemSettings);
    public static final Item ONYX_POWDER = new Item(generalItemSettings);
    public static final Item MOONSTONE_POWDER = new Item(generalItemSettings);

    // Spectrum
    public static final Item BLACK_SPECTRUM = new Item(generalItemSettings);
    public static final Item BLUE_SPECTRUM = new Item(generalItemSettings);
    public static final Item BROWN_SPECTRUM = new Item(generalItemSettings);
    public static final Item CYAN_SPECTRUM = new Item(generalItemSettings);
    public static final Item GRAY_SPECTRUM = new Item(generalItemSettings);
    public static final Item GREEN_SPECTRUM = new Item(generalItemSettings);
    public static final Item LIGHT_BLUE_SPECTRUM = new Item(generalItemSettings);
    public static final Item LIGHT_GRAY_SPECTRUM = new Item(generalItemSettings);
    public static final Item LIME_SPECTRUM = new Item(generalItemSettings);
    public static final Item MAGENTA_SPECTRUM = new Item(generalItemSettings);
    public static final Item ORANGE_SPECTRUM = new Item(generalItemSettings);
    public static final Item PINK_SPECTRUM = new Item(generalItemSettings);
    public static final Item PURPLE_SPECTRUM = new Item(generalItemSettings);
    public static final Item RED_SPECTRUM = new Item(generalItemSettings);
    public static final Item WHITE_SPECTRUM = new Item(generalItemSettings);
    public static final Item YELLOW_SPECTRUM = new Item(generalItemSettings);

    // Bedrock Tools
    public static final ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BedrockToolMaterial.INSTANCE, 1, -2.8F, spectrumBedrockItemSettings);
    public static final ToolItem BEDROCK_AXE = new BedrockAxeItem(BedrockToolMaterial.INSTANCE, 5, -3.0F, spectrumBedrockItemSettings);
    public static final ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BedrockToolMaterial.INSTANCE, 1, -3.0F, spectrumBedrockItemSettings);
    public static final ToolItem BEDROCK_SWORD = new BedrockSwordItem(BedrockToolMaterial.INSTANCE, 4, -2.4F, spectrumBedrockItemSettings);
    public static final ToolItem BEDROCK_HOE = new BedrockHoeItem(BedrockToolMaterial.INSTANCE, -2, -0.0F, spectrumBedrockItemSettings);
    public static final BedrockBowItem BEDROCK_BOW = new BedrockBowItem(spectrumBedrockItemSettings);
    public static final BedrockCrossbowItem BEDROCK_CROSSBOW = new BedrockCrossbowItem(spectrumBedrockItemSettings);
    public static final BedrockShearsItem BEDROCK_SHEARS = new BedrockShearsItem(spectrumBedrockItemSettings); // TODO: wait for fabric pull request to get shears drop: https://github.com/FabricMC/fabric/pull/1287
    public static final FishingRodItem BEDROCK_FISHING_ROD = new BedrockFishingRodItem(spectrumBedrockItemSettings);

    // Bedrock Armor
    public static final BedrockArmorMaterial BEDROCK_ARMOR_MATERIAL = new BedrockArmorMaterial();
    public static final Item BEDROCK_HELMET = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumBedrockItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumBedrockItemSettings);
    public static final Item BEDROCK_LEGGINGS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumBedrockItemSettings);
    public static final Item BEDROCK_BOOTS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumBedrockItemSettings);

    // Decay drops
    public static final Item VEGETAL = new Item(generalItemSettings);
    public static final Item CORRUPTED_OBSIDIAN_DUST = new Item(spectrumUncommonItemSettings);
    public static final Item CORRUPTED_BEDROCK_DUST = new Item(spectrumRareItemSettings);

    // Fluid Buckets
    public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(STILL_LIQUID_CRYSTAL, generalItemSettingsSingle);
    public static final Item MUD_BUCKET = new BucketItem(STILL_MUD, generalItemSettingsSingle);

    // Decay bottles
    public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, decayPlacerItemSettings);
    public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, decayPlacerItemSettings);
    public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, decayPlacerItemSettings);

    // Resources
    public static final Item SPARKLESTONE_GEM = new Item(spectrumWorldgenItemSettings);
    public static final Item RAW_AZURITE = new Item(spectrumWorldgenItemSettings);
    public static final Item SHAPED_AZURITE = new Item(spectrumWorldgenItemSettings);
    public static final Item SHATTERED_SCARLET_FRAGMENTS = new GravityItem(generalItemSettings, 1.003F);
    public static final Item SCARLET_GEM = new GravityItem(generalItemSettings, 1.02F);
    public static final Item SHATTERED_PALETUR_FRAGMENTS = new GravityItem(generalItemSettings, 0.997F);
    public static final Item PALETUR_GEM = new GravityItem(generalItemSettings, 0.98F);

    public static final Item QUITOXIC_POWDER = new Item(generalItemSettings);
    public static final Item MERMAIDS_GEM = new Item(generalItemSettings);
    public static final Item SHOOTING_STAR = new Item(generalItemSettings);

    // Preenchanted tools
    public static final LowHealthMaterial LOW_HEALTH_MATERIAL = new LowHealthMaterial();
    public static final VoidingMaterial VOIDING_MATERIAL = new VoidingMaterial();
    public static final Item MULTITOOL = new MultiToolItem(ToolMaterials.IRON, 2, -2.4F, preEnchantedItemSettings);
    public static final Item SILKER_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, preEnchantedItemSettings);
    public static final Item FORTUNE_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, preEnchantedItemSettings);
    public static final Item LOOTING_FALCHION = new SwordItem(LOW_HEALTH_MATERIAL, 4, -2.2F, preEnchantedItemSettings);
    public static final Item VOIDING_PICKAXE = new VoidingPickaxeItem(VOIDING_MATERIAL, 1, -2.8F, preEnchantedItemSettings);
    public static final Item RESONANT_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, preEnchantedItemSettings);

	// Magical Tools
    public static final Item GLOW_VISION_HELMET = new GlowVisionHelmet(GlowVisionMaterial.INSTANCE, EquipmentSlot.HEAD, preEnchantedItemSettings);
    public static final Item NATURES_STAFF = new AnimatedWandItem(preEnchantedItemSettings);
    public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(generalItemSettings);
    public static final Item ENDER_BAG = new EnderBagItem(generalItemSettings);
    public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(generalItemSettings);

    // Catkin
    public static final Item VIBRANT_CYAN_CATKIN = new CatkinItem(SpectrumColor.CYAN, false, generalItemSettings);
    public static final Item VIBRANT_MAGENTA_CATKIN =  new CatkinItem(SpectrumColor.MAGENTA, false, generalItemSettings);
    public static final Item VIBRANT_YELLOW_CATKIN = new CatkinItem(SpectrumColor.YELLOW, false, generalItemSettings);
    public static final Item VIBRANT_BLACK_CATKIN = new CatkinItem(SpectrumColor.BLACK, false, generalItemSettings);
    public static final Item VIBRANT_WHITE_CATKIN = new CatkinItem(SpectrumColor.WHITE, false, generalItemSettings);
	
    public static final Item LUCID_CYAN_CATKIN =  new CatkinItem(SpectrumColor.CYAN, true, generalUncommonItemSettings);
    public static final Item LUCID_MAGENTA_CATKIN = new CatkinItem(SpectrumColor.MAGENTA, true, generalUncommonItemSettings);
    public static final Item LUCID_YELLOW_CATKIN = new CatkinItem(SpectrumColor.YELLOW, true, generalUncommonItemSettings);
    public static final Item LUCID_BLACK_CATKIN = new CatkinItem(SpectrumColor.BLACK, true, generalUncommonItemSettings);
    public static final Item LUCID_WHITE_CATKIN = new CatkinItem(SpectrumColor.WHITE, true, generalUncommonItemSettings);

    // Misc
	public static final Item MUSIC_DISC_SPECTRUM_THEME = new SpectrumMusicDiscItem(1, SpectrumSoundEvents.SPECTRUM_THEME, musicDiscItemSettings);
    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, spectrumUncommonItemSettings);
    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, spectrumUncommonItemSettings);
    public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, generalItemSettings);
    public static final Item INVISIBLE_GLOW_ITEM_FRAME = new InvisibleGlowItemFrameItem(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, generalItemSettings);

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), item);
    }

    public static void register() {
        register("manual", MANUAL);
        register("crafting_tablet", CRAFTING_TABLET);
		
		registerGemstoneItems();
		registerSpectrums();
		registerCatkin();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerMagicalTools();
		registerFluidBuckets();

        register("music_disc_spectrum_theme", MUSIC_DISC_SPECTRUM_THEME);
        register("spawner", SPAWNER);
        register("glistering_melon_seeds", GLISTERING_MELON_SEEDS);
        register("invisible_item_frame", INVISIBLE_ITEM_FRAME);
        register("invisible_glow_item_frame", INVISIBLE_GLOW_ITEM_FRAME);
    }
		
	public static void registerGemstoneItems() {
        register("topaz_shard", TOPAZ_SHARD_ITEM);
        register("citrine_shard", CITRINE_SHARD_ITEM);
        register("onyx_shard", ONYX_SHARD_ITEM);
        register("moonstone_shard", MOONSTONE_SHARD_ITEM);
        register("ammolite_shard", AMMOLITE_ITEM);
		
        register("topaz_powder", TOPAZ_POWDER);
		register("amethyst_powder", AMETHYST_POWDER);
        register("citrine_powder", CITRINE_POWDER);
        register("onyx_powder", ONYX_POWDER);
        register("moonstone_powder", MOONSTONE_POWDER);
	}
	
	public static void registerSpectrums() {
        register("black_spectrum", BLACK_SPECTRUM);
        register("blue_spectrum", BLUE_SPECTRUM);
        register("brown_spectrum", BROWN_SPECTRUM);
        register("cyan_spectrum", CYAN_SPECTRUM);
        register("gray_spectrum", GRAY_SPECTRUM);
        register("green_spectrum", GREEN_SPECTRUM);
        register("light_blue_spectrum", LIGHT_BLUE_SPECTRUM);
        register("light_gray_spectrum", LIGHT_GRAY_SPECTRUM);
        register("lime_spectrum", LIME_SPECTRUM);
        register("magenta_spectrum", MAGENTA_SPECTRUM);
        register("orange_spectrum", ORANGE_SPECTRUM);
        register("pink_spectrum", PINK_SPECTRUM);
        register("purple_spectrum", PURPLE_SPECTRUM);
        register("red_spectrum", RED_SPECTRUM);
        register("white_spectrum", WHITE_SPECTRUM);
        register("yellow_spectrum", YELLOW_SPECTRUM);
	}
	
	public static void registerCatkin() {
		register("vibrant_cyan_catkin", VIBRANT_CYAN_CATKIN);
		register("vibrant_magenta_catkin", VIBRANT_MAGENTA_CATKIN);
		register("vibrant_yellow_catkin", VIBRANT_YELLOW_CATKIN);
		register("vibrant_black_catkin", VIBRANT_BLACK_CATKIN);
		register("vibrant_white_catkin", VIBRANT_WHITE_CATKIN);
		
		register("lucid_cyan_catkin", LUCID_CYAN_CATKIN);
		register("lucid_magenta_catkin", LUCID_MAGENTA_CATKIN);
		register("lucid_yellow_catkin", LUCID_YELLOW_CATKIN);
		register("lucid_black_catkin", LUCID_BLACK_CATKIN);
		register("lucid_white_catkin", LUCID_WHITE_CATKIN);
	}
	
	public static void registerResources() {
        register("sparklestone_gem", SPARKLESTONE_GEM);
        register("raw_azurite", RAW_AZURITE);
        register("shaped_azurite", SHAPED_AZURITE);
        register("shattered_paletur_fragments", SHATTERED_PALETUR_FRAGMENTS);
        register("paletur_gem", PALETUR_GEM);
        register("shattered_scarlet_fragments", SHATTERED_SCARLET_FRAGMENTS);
        register("scarlet_gem", SCARLET_GEM);

        register("quitoxic_powder", QUITOXIC_POWDER);
        register("mermaids_gem", MERMAIDS_GEM);
        register("shooting_star", SHOOTING_STAR);
		
		register("vegetal", VEGETAL);
        register("corrupted_obsidian_dust", CORRUPTED_OBSIDIAN_DUST);
        register("corrupted_bedrock_dust", CORRUPTED_BEDROCK_DUST);
	}
	
	public static void registerDecayBottles() {
        register("bottle_of_fading", BOTTLE_OF_FADING);
        register("bottle_of_failing", BOTTLE_OF_FAILING);
        register("bottle_of_ruin", BOTTLE_OF_RUIN);
	}
	
	public static void registerPreEnchantedTools() {
		register("multitool", MULTITOOL);
        register("silker_pickaxe", SILKER_PICKAXE);
        register("fortune_pickaxe", FORTUNE_PICKAXE);
        register("looting_falchion", LOOTING_FALCHION);
        register("voiding_pickaxe", VOIDING_PICKAXE);
        register("resonant_pickaxe", RESONANT_PICKAXE);
		
        register("bedrock_pickaxe", BEDROCK_PICKAXE);
        register("bedrock_axe", BEDROCK_AXE);
        register("bedrock_shovel", BEDROCK_SHOVEL);
        register("bedrock_sword", BEDROCK_SWORD);
        register("bedrock_hoe", BEDROCK_HOE);
        register("bedrock_bow", BEDROCK_BOW);
        register("bedrock_crossbow", BEDROCK_CROSSBOW);
        register("bedrock_shears", BEDROCK_SHEARS);
        register("bedrock_fishing_rod", BEDROCK_FISHING_ROD);
        register("bedrock_helmet", BEDROCK_HELMET);
        register("bedrock_chestplate", BEDROCK_CHESTPLATE);
        register("bedrock_leggings", BEDROCK_LEGGINGS);
        register("bedrock_boots", BEDROCK_BOOTS);
	}
	
	public static void registerMagicalTools() {
        register("glow_vision_helmet", GLOW_VISION_HELMET);
        register("natures_staff", NATURES_STAFF);
		register("ender_bag", ENDER_BAG);
        register("ender_splice", ENDER_SPLICE);
        register("end_portal_cracker", END_PORTAL_CRACKER);
	}
	
	public static void registerFluidBuckets() {
		register("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET);
        register("mud_bucket", MUD_BUCKET);
	}
	
    public static void registerFuelRegistry() {
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), 12800);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_LEVEL_DETECTOR.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WEATHER_DETECTOR.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ITEM_DETECTOR.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PLAYER_DETECTOR.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ENTITY_DETECTOR.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_LOG.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_SAPLING.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANKS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_STAIRS.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_PRESSURE_PLATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_FENCE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_FENCE_GATE.asItem(), 300);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_BUTTON.asItem(), 100);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLACK_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BLUE_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.BROWN_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.CYAN_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GRAY_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.GREEN_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_BLUE_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_GRAY_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.LIME_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.MAGENTA_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.ORANGE_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PINK_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.PURPLE_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.RED_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.WHITE_PLANK_SLAB.asItem(), 150);
        FuelRegistry.INSTANCE.add(SpectrumBlocks.YELLOW_PLANK_SLAB.asItem(), 150);
    }

}
