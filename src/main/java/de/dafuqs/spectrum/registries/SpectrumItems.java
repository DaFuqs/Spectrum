package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.gravity.GravityItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.items.PigmentItem;
import de.dafuqs.spectrum.items.StructurePlacerItem;
import de.dafuqs.spectrum.items.VoidBundleItem;
import de.dafuqs.spectrum.items.armor.EmergencyArmorItem;
import de.dafuqs.spectrum.items.armor.GlowVisionHelmet;
import de.dafuqs.spectrum.items.armor.SpectrumArmorItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleGlowItemFrameItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleItemFrameItem;
import de.dafuqs.spectrum.items.misc.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.materials.SpectrumArmorMaterials;
import de.dafuqs.spectrum.registries.materials.SpectrumToolMaterials;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    public static FabricItemSettings spectrumBedrockToolItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.RARE).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability());
    public static FabricItemSettings spectrumBedrockArmorItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.RARE).fireproof().maxDamage(0);
    public static FabricItemSettings spectrumEmergencyArmorItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.RARE).maxDamage(SpectrumArmorMaterials.EMERGENCY.getDurability(EquipmentSlot.CHEST));
    public static FabricItemSettings spectrumLowHealthToolItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability());
    public static FabricItemSettings spectrumLowVoidingToolItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VOIDING.getDurability());
    public static FabricItemSettings spectrumLowNightVisionArmorItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GLOW_VISION.getDurability(EquipmentSlot.HEAD));
    public static FabricItemSettings spectrumMultiToolItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.UNCOMMON).maxDamage(ToolMaterials.IRON.getDurability());

	// Main items
    public static final Item MANUAL = new ManualItem(generalItemSettingsSingle);
    public static final Item CRAFTING_TABLET = new CraftingTabletItem(generalItemSettingsSingle);

    public static final Item PEDESTAL_TIER_1_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_simple_structure"));
    public static final Item PEDESTAL_TIER_2_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_advanced_structure_display"));
    public static final Item PEDESTAL_TIER_3_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_complex_structure_display"));

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

    // Pigment
    public static final Item BLACK_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.BLACK);
    public static final Item BLUE_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.BLUE);
    public static final Item BROWN_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.BROWN);
    public static final Item CYAN_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.CYAN);
    public static final Item GRAY_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.GRAY);
    public static final Item GREEN_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.GREEN);
    public static final Item LIGHT_BLUE_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.LIGHT_BLUE);
    public static final Item LIGHT_GRAY_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.LIGHT_GRAY);
    public static final Item LIME_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.LIME);
    public static final Item MAGENTA_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.MAGENTA);
    public static final Item ORANGE_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.ORANGE);
    public static final Item PINK_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.PINK);
    public static final Item PURPLE_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.PURPLE);
    public static final Item RED_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.RED);
    public static final Item WHITE_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.WHITE);
    public static final Item YELLOW_PIGMENT = new PigmentItem(generalItemSettings, DyeColor.YELLOW);

    // Preenchanted tools
    public static final SpectrumToolMaterials.ToolMaterial LOW_HEALTH_MATERIAL = SpectrumToolMaterials.ToolMaterial.LOW_HEALTH;
    public static final SpectrumToolMaterials.ToolMaterial VOIDING_MATERIAL = SpectrumToolMaterials.ToolMaterial.VOIDING;
    public static final Item MULTITOOL = new MultiToolItem(ToolMaterials.IRON, 2, -2.4F, spectrumMultiToolItemSettings);
    public static final Item SILKER_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, spectrumLowHealthToolItemSettings);
    public static final Item FORTUNE_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, spectrumLowHealthToolItemSettings);
    public static final Item LOOTING_FALCHION = new SwordItem(LOW_HEALTH_MATERIAL, 4, -2.2F, spectrumLowHealthToolItemSettings);
    public static final Item VOIDING_PICKAXE = new VoidingPickaxeItem(VOIDING_MATERIAL, 1, -2.8F, spectrumLowVoidingToolItemSettings);
    public static final Item RESONANT_PICKAXE = new SpectrumPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, spectrumLowHealthToolItemSettings);

    // Bedrock Tools
    public static final SpectrumToolMaterials.ToolMaterial BEDROCK_MATERIAL = SpectrumToolMaterials.ToolMaterial.BEDROCK;
    public static final ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BEDROCK_MATERIAL, 1, -2.8F, spectrumBedrockToolItemSettings);
    public static final ToolItem BEDROCK_AXE = new BedrockAxeItem(BEDROCK_MATERIAL, 5, -3.0F, spectrumBedrockToolItemSettings);
    public static final ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BEDROCK_MATERIAL, 1, -3.0F, spectrumBedrockToolItemSettings);
    public static final ToolItem BEDROCK_SWORD = new BedrockSwordItem(BEDROCK_MATERIAL, 4, -2.4F, spectrumBedrockToolItemSettings);
    public static final ToolItem BEDROCK_HOE = new BedrockHoeItem(BEDROCK_MATERIAL, -2, -0.0F, spectrumBedrockToolItemSettings);
    public static final BedrockBowItem BEDROCK_BOW = new BedrockBowItem(spectrumBedrockToolItemSettings);
    public static final BedrockCrossbowItem BEDROCK_CROSSBOW = new BedrockCrossbowItem(spectrumBedrockToolItemSettings);
    public static final BedrockShearsItem BEDROCK_SHEARS = new BedrockShearsItem(spectrumBedrockToolItemSettings); // TODO: wait for fabric pull request to get shears drop: https://github.com/FabricMC/fabric/pull/1287
    public static final FishingRodItem BEDROCK_FISHING_ROD = new BedrockFishingRodItem(spectrumBedrockToolItemSettings);    
    
    // Bedrock Armor
    public static final ArmorMaterial BEDROCK_ARMOR_MATERIAL = SpectrumArmorMaterials.BEDROCK;
    public static final Item BEDROCK_HELMET = new SpectrumArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumBedrockArmorItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new SpectrumArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumBedrockArmorItemSettings);
    public static final Item BEDROCK_LEGGINGS = new SpectrumArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumBedrockArmorItemSettings);
    public static final Item BEDROCK_BOOTS = new SpectrumArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumBedrockArmorItemSettings);

    // Emergency Armor
    public static final ArmorMaterial EMERGENCY_ARMOR_MATERIAL = SpectrumArmorMaterials.EMERGENCY;
    public static final Item EMERGENCY_HELMET = new EmergencyArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumEmergencyArmorItemSettings);
    public static final Item EMERGENCY_CHESTPLATE = new EmergencyArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumEmergencyArmorItemSettings);
    public static final Item EMERGENCY_LEGGINGS = new EmergencyArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumEmergencyArmorItemSettings);
    public static final Item EMERGENCY_BOOTS = new EmergencyArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumEmergencyArmorItemSettings);
    
    // Decay drops
    public static final Item VEGETAL = new Item(generalItemSettings);
    public static final Item CORRUPTED_OBSIDIAN_DUST = new Item(spectrumUncommonItemSettings);
    public static final Item BEDROCK_DUST = new Item(spectrumRareItemSettings);

    // Fluid Buckets
    public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(STILL_LIQUID_CRYSTAL, generalItemSettingsSingle);
    public static final Item MUD_BUCKET = new BucketItem(STILL_MUD, generalItemSettingsSingle);

    // Decay bottles
    public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, decayPlacerItemSettings);
    public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, decayPlacerItemSettings);
    public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, decayPlacerItemSettings);
    public static final Item BOTTLE_OF_DECAY_AWAY = new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY, decayPlacerItemSettings);

    // Resources
    public static final Item SPARKLESTONE_GEM = new Item(spectrumWorldgenItemSettings);
    public static final Item RAW_AZURITE = new Item(spectrumWorldgenItemSettings);
    public static final Item SHAPED_AZURITE = new Item(spectrumWorldgenItemSettings);
    public static final Item SCARLET_FRAGMENTS = new GravityItem(generalItemSettings, 1.003F);
    public static final Item SCARLET_GEM = new GravityItem(generalItemSettings, 1.02F);
    public static final Item PALETUR_FRAGMENTS = new GravityItem(generalItemSettings, 0.997F);
    public static final Item PALETUR_GEM = new GravityItem(generalItemSettings, 0.98F);

    public static final Item QUITOXIC_POWDER = new Item(generalItemSettings);
    public static final Item MERMAIDS_GEM = new Item(generalItemSettings);
    public static final Item SHOOTING_STAR = new Item(generalItemSettings);

	// Magical Tools
    public static final Item GLOW_VISION_HELMET = new GlowVisionHelmet(SpectrumArmorMaterials.GLOW_VISION, EquipmentSlot.HEAD, spectrumLowNightVisionArmorItemSettings);
    public static final Item NATURES_STAFF = new AnimatedWandItem(generalItemSettings);
    public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(generalItemSettings);
    public static final Item ENDER_BAG = new EnderBagItem(generalItemSettings);
    public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(generalItemSettings);

    // Catkin
    public static final Item VIBRANT_CYAN_CATKIN = new CatkinItem(GemstoneColor.CYAN, false, generalItemSettings);
    public static final Item VIBRANT_MAGENTA_CATKIN =  new CatkinItem(GemstoneColor.MAGENTA, false, generalItemSettings);
    public static final Item VIBRANT_YELLOW_CATKIN = new CatkinItem(GemstoneColor.YELLOW, false, generalItemSettings);
    public static final Item VIBRANT_BLACK_CATKIN = new CatkinItem(GemstoneColor.BLACK, false, generalItemSettings);
    public static final Item VIBRANT_WHITE_CATKIN = new CatkinItem(GemstoneColor.WHITE, false, generalItemSettings);
	
    public static final Item LUCID_CYAN_CATKIN =  new CatkinItem(GemstoneColor.CYAN, true, generalUncommonItemSettings);
    public static final Item LUCID_MAGENTA_CATKIN = new CatkinItem(GemstoneColor.MAGENTA, true, generalUncommonItemSettings);
    public static final Item LUCID_YELLOW_CATKIN = new CatkinItem(GemstoneColor.YELLOW, true, generalUncommonItemSettings);
    public static final Item LUCID_BLACK_CATKIN = new CatkinItem(GemstoneColor.BLACK, true, generalUncommonItemSettings);
    public static final Item LUCID_WHITE_CATKIN = new CatkinItem(GemstoneColor.WHITE, true, generalUncommonItemSettings);

    // Misc
	public static final Item MUSIC_DISC_SPECTRUM_THEME = new SpectrumMusicDiscItem(1, SpectrumSoundEvents.SPECTRUM_THEME, musicDiscItemSettings);
    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, spectrumUncommonItemSettings);
    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, spectrumUncommonItemSettings);
    public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, generalItemSettings);
    public static final Item INVISIBLE_GLOW_ITEM_FRAME = new InvisibleGlowItemFrameItem(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, generalItemSettings);

    public static final Item VOID_BUNDLE = new VoidBundleItem(generalItemSettingsSingle);
    public static final Item LIGHTNING_STONE = new Item(generalItemSettings);

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), item);
    }

    public static void register() {
        register("manual", MANUAL);
        register("crafting_tablet", CRAFTING_TABLET);

        register("pedestal_tier_1_structure_placer", PEDESTAL_TIER_1_STRUCTURE_PLACER);
        register("pedestal_tier_2_structure_placer", PEDESTAL_TIER_2_STRUCTURE_PLACER);
        register("pedestal_tier_3_structure_placer", PEDESTAL_TIER_3_STRUCTURE_PLACER);

        registerGemstoneItems();
		registerPigments();
		registerCatkin();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerMagicalTools();
		registerFluidBuckets();

        register("void_bundle", VOID_BUNDLE);
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
	
	public static void registerPigments() {
        register("black_pigment", BLACK_PIGMENT);
        register("blue_pigment", BLUE_PIGMENT);
        register("brown_pigment", BROWN_PIGMENT);
        register("cyan_pigment", CYAN_PIGMENT);
        register("gray_pigment", GRAY_PIGMENT);
        register("green_pigment", GREEN_PIGMENT);
        register("light_blue_pigment", LIGHT_BLUE_PIGMENT);
        register("light_gray_pigment", LIGHT_GRAY_PIGMENT);
        register("lime_pigment", LIME_PIGMENT);
        register("magenta_pigment", MAGENTA_PIGMENT);
        register("orange_pigment", ORANGE_PIGMENT);
        register("pink_pigment", PINK_PIGMENT);
        register("purple_pigment", PURPLE_PIGMENT);
        register("red_pigment", RED_PIGMENT);
        register("white_pigment", WHITE_PIGMENT);
        register("yellow_pigment", YELLOW_PIGMENT);
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
        register("paletur_fragments", PALETUR_FRAGMENTS);
        register("paletur_gem", PALETUR_GEM);
        register("scarlet_fragments", SCARLET_FRAGMENTS);
        register("scarlet_gem", SCARLET_GEM);

        register("quitoxic_powder", QUITOXIC_POWDER);
        register("mermaids_gem", MERMAIDS_GEM);
        register("lightning_stone", LIGHTNING_STONE);
        register("shooting_star", SHOOTING_STAR);

		register("vegetal", VEGETAL);
        register("corrupted_obsidian_dust", CORRUPTED_OBSIDIAN_DUST);
        register("bedrock_dust", BEDROCK_DUST);
	}
	
	public static void registerDecayBottles() {
        register("bottle_of_fading", BOTTLE_OF_FADING);
        register("bottle_of_failing", BOTTLE_OF_FAILING);
        register("bottle_of_ruin", BOTTLE_OF_RUIN);
        register("bottle_of_decay_away", BOTTLE_OF_DECAY_AWAY);
	}
	
	public static void registerPreEnchantedTools() {
		register("multitool", MULTITOOL);
        register("silker_pickaxe", SILKER_PICKAXE);
        register("fortune_pickaxe", FORTUNE_PICKAXE);
        register("looting_falchion", LOOTING_FALCHION);
        register("voiding_pickaxe", VOIDING_PICKAXE);
        register("resonant_pickaxe", RESONANT_PICKAXE);

        register("emergency_helmet", EMERGENCY_HELMET);
        register("emergency_chestplate", EMERGENCY_CHESTPLATE);
        register("emergency_leggings", EMERGENCY_LEGGINGS);
        register("emergency_boots", EMERGENCY_BOOTS);
		
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

    @Contract(pure = true)
    public static Item getGemstoneShard(@NotNull GemstoneColor gemstoneColor) {
        switch (gemstoneColor) {
            case CYAN -> {
                return TOPAZ_POWDER;
            }
            case MAGENTA -> {
                return AMETHYST_POWDER;
            }
            case YELLOW -> {
                return CITRINE_POWDER;
            }
            case BLACK -> {
                return ONYX_POWDER;
            }
            default -> {
                return MOONSTONE_POWDER;
            }
        }
    }

}
