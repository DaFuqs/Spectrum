package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.gravity.CloakedGravityItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.GemstoneArmorItem;
import de.dafuqs.spectrum.items.armor.GlowVisionHelmet;
import de.dafuqs.spectrum.items.armor.SpectrumArmorItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleGlowItemFrameItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleItemFrameItem;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import io.wispforest.owo.itemgroup.OwoItemSettings;
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

import static de.dafuqs.spectrum.registries.SpectrumFluids.LIQUID_CRYSTAL;
import static de.dafuqs.spectrum.registries.SpectrumFluids.MUD;

public class SpectrumItems {

	public static FabricItemSettings generalItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(64);
	public static FabricItemSettings generalItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1);
	public static FabricItemSettings generalUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(64).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalUncommonItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(1);
	public static FabricItemSettings generalUncommonItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(16);
	public static FabricItemSettings generalRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).maxCount(64);
	public static FabricItemSettings generalRareItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).maxCount(1);
	public static FabricItemSettings generalEpicItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.EPIC).maxCount(1);
	public static FabricItemSettings decayPlacerItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(16);
	public static FabricItemSettings spectrumLowNightVisionArmorItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GLOW_VISION.getDurability(EquipmentSlot.HEAD));

	// will be added to item group / tab programmatically in the item group itself with enchantments included
	public static FabricItemSettings spectrumBedrockToolItemSettings = new FabricItemSettings().rarity(Rarity.RARE).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability());
	public static FabricItemSettings spectrumBedrockArmorItemSettings = new FabricItemSettings().rarity(Rarity.RARE).fireproof().maxDamage(0);
	public static FabricItemSettings spectrumEmergencyArmorItemSettings = new FabricItemSettings().rarity(Rarity.RARE).maxDamage(SpectrumArmorMaterials.EMERGENCY.getDurability(EquipmentSlot.CHEST));
	public static FabricItemSettings spectrumLowHealthToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability());
	public static FabricItemSettings spectrumLowVoidingToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VOIDING.getDurability());
	public static FabricItemSettings spectrumMultiToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(ToolMaterials.IRON.getDurability());
	
	public static FabricItemSettings resourcesItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(64);
	public static FabricItemSettings resourcesItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(16);
	public static FabricItemSettings resourcesUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(64);
	public static FabricItemSettings resourcesRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.RARE).maxCount(64);
	
	
	// Main items
	public static final Item MANUAL = new ManualItem(generalItemSettingsSingle);
	public static final Item CRAFTING_TABLET = new CraftingTabletItem(generalItemSettingsSingle);

	public static final Item PEDESTAL_TIER_1_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_simple_structure_place"));
	public static final Item PEDESTAL_TIER_2_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_advanced_structure_place"));
	public static final Item PEDESTAL_TIER_3_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_complex_structure_place"));
	public static final Item FUSION_SHRINE_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine_structure"));
	public static final Item ENCHANTER_STRUCTURE_PLACER = new StructurePlacerItem(generalItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "enchanter_structure"));

	// Gem shards
	public static final Item TOPAZ_SHARD = new Item(resourcesItemSettings);
	public static final Item CITRINE_SHARD = new Item(resourcesItemSettings);
	public static final Item ONYX_SHARD = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "collect_all_basic_pigments_besides_brown"), Items.BLACK_DYE);
	public static final Item MOONSTONE_SHARD = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/break_decayed_bedrock"), Items.WHITE_DYE);
	public static final Item SPECTRAL_SHARD = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "lategame/build_complex_pedestal_structure"), Items.LIGHT_GRAY_DYE);
	
	private static final Identifier GEMSTONE_POWDER_CLOAK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "place_pedestal");
	public static final Item TOPAZ_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, GemstoneColor.CYAN);
	public static final Item AMETHYST_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, GemstoneColor.MAGENTA);
	public static final Item CITRINE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, GemstoneColor.YELLOW);
	public static final Item ONYX_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "create_onyx_shard"), GemstoneColor.BLACK);
	public static final Item MOONSTONE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/collect_moonstone_shard"), GemstoneColor.WHITE);

	// Pigment
	public static final Item BLACK_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.BLACK);
	public static final Item BLUE_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.BLUE);
	public static final Item BROWN_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.BROWN);
	public static final Item CYAN_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.CYAN);
	public static final Item GRAY_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.GRAY);
	public static final Item GREEN_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.GREEN);
	public static final Item LIGHT_BLUE_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.LIGHT_BLUE);
	public static final Item LIGHT_GRAY_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.LIGHT_GRAY);
	public static final Item LIME_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.LIME);
	public static final Item MAGENTA_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.MAGENTA);
	public static final Item ORANGE_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.ORANGE);
	public static final Item PINK_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.PINK);
	public static final Item PURPLE_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.PURPLE);
	public static final Item RED_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.RED);
	public static final Item WHITE_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.WHITE);
	public static final Item YELLOW_PIGMENT = new PigmentItem(resourcesItemSettings, DyeColor.YELLOW);

	// Preenchanted tools
	public static final Item MULTITOOL = new MultiToolItem(ToolMaterials.IRON, 2, -2.4F, spectrumMultiToolItemSettings);
	public static final Item SILKER_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, spectrumLowHealthToolItemSettings);
	public static final Item FORTUNE_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, spectrumLowHealthToolItemSettings);
	public static final Item LOOTING_FALCHION = new SwordItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 4, -2.2F, spectrumLowHealthToolItemSettings);
	public static final Item VOIDING_PICKAXE = new VoidingPickaxeItem(SpectrumToolMaterials.ToolMaterial.VOIDING, 1, -2.8F, spectrumLowVoidingToolItemSettings);
	public static final Item RESONANT_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, spectrumLowHealthToolItemSettings);

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

	// Armor
	public static final ArmorMaterial EMERGENCY_ARMOR_MATERIAL = SpectrumArmorMaterials.EMERGENCY;
	public static final Item EMERGENCY_HELMET = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumEmergencyArmorItemSettings);
	public static final Item EMERGENCY_CHESTPLATE = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumEmergencyArmorItemSettings);
	public static final Item EMERGENCY_LEGGINGS = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumEmergencyArmorItemSettings);
	public static final Item EMERGENCY_BOOTS = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumEmergencyArmorItemSettings);
	public static final Item GLOW_VISION_HELMET = new GlowVisionHelmet(SpectrumArmorMaterials.GLOW_VISION, EquipmentSlot.HEAD, spectrumLowNightVisionArmorItemSettings);
	
	// Decay drops
	public static final Item VEGETAL = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "craft_bottle_of_fading"), Items.GUNPOWDER);
	public static final Item CORRUPTED_OBSIDIAN_DUST = new CloakedItem(resourcesUncommonItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/craft_bottle_of_failing"), Items.GUNPOWDER);
	public static final Item BEDROCK_DUST = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/break_decayed_bedrock"), Items.GUNPOWDER);
	
	public static final Item MIDNIGHT_ABERRATION = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/create_midnight_aberration"), SpectrumItems.SPECTRAL_SHARD);

	// Fluid Buckets
	public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(LIQUID_CRYSTAL, generalItemSettingsSingle);
	public static final Item MUD_BUCKET = new BucketItem(MUD, generalItemSettingsSingle);

	// Decay bottles
	public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, decayPlacerItemSettings);
	public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, decayPlacerItemSettings);
	public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, decayPlacerItemSettings);
	public static final Item BOTTLE_OF_TERROR = new DecayPlacerItem(SpectrumBlocks.TERROR, decayPlacerItemSettings);
	public static final Item BOTTLE_OF_DECAY_AWAY = new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY, decayPlacerItemSettings);

	// Resources
	public static final CloakedItem SPARKLESTONE_GEM = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.SPARKLESTONE_ORE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE);
	public static final CloakedItem RAW_AZURITE = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedItem REFINED_AZURITE = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedGravityItem SCARLET_FRAGMENTS = new CloakedGravityItem(resourcesItemSettings, 1.003F, ((Cloakable) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedGravityItem SCARLET_GEM = new CloakedGravityItem(resourcesItemSettingsSixteen, 1.02F, ((Cloakable) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedGravityItem PALETUR_FRAGMENTS = new CloakedGravityItem(resourcesItemSettings, 0.997F, ((Cloakable) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	public static final CloakedGravityItem PALETUR_GEM = new CloakedGravityItem(resourcesItemSettingsSixteen, 0.98F, ((Cloakable) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);

	public static final CloakedItem QUITOXIC_POWDER = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.QUITOXIC_REEDS).getCloakAdvancementIdentifier(), Items.PURPLE_DYE);
	public static final CloakedItem LIGHTNING_STONE = new CloakedItem(resourcesItemSettingsSixteen, ((Cloakable) SpectrumBlocks.STUCK_LIGHTNING_STONE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE);
	public static final CloakedItem MERMAIDS_GEM = new CloakedItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "craft_using_pedestal"), Items.LIGHT_BLUE_DYE);
	public static final CloakedItem SHOOTING_STAR = new CloakedItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_shooting_stars"), Items.PURPLE_DYE);
	public static final CloakedItem STARDUST = new CloakedItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_shooting_stars"), Items.PURPLE_DYE);

	// Magical Tools
	public static final Item ENDER_BAG = new EnderBagItem(generalItemSettingsSingle);
	public static final Item LIGHT_STAFF = new LightStaffItem(generalUncommonItemSettingsSingle);
	public static final Item NATURES_STAFF = new NaturesStaffItem(generalUncommonItemSettingsSingle);
	public static final Item PLACEMENT_STAFF = new PlacementStaffItem(generalUncommonItemSettingsSingle);
	public static final Item EXCHANGE_STAFF = new ExchangeStaffItem(generalUncommonItemSettingsSingle);
	public static final Item BLOCK_FLOODER = new BlockFlooderItem(generalUncommonItemSettings);
	public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(generalUncommonItemSettingsSixteen);
	public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(generalRareItemSettings);

	// Catkin
	public static final Item VIBRANT_CYAN_CATKIN = new CatkinItem(GemstoneColor.CYAN, false, resourcesItemSettings);
	public static final Item VIBRANT_MAGENTA_CATKIN =  new CatkinItem(GemstoneColor.MAGENTA, false, resourcesItemSettings);
	public static final Item VIBRANT_YELLOW_CATKIN = new CatkinItem(GemstoneColor.YELLOW, false, resourcesItemSettings);
	public static final Item VIBRANT_BLACK_CATKIN = new CatkinItem(GemstoneColor.BLACK, false, resourcesItemSettings);
	public static final Item VIBRANT_WHITE_CATKIN = new CatkinItem(GemstoneColor.WHITE, false, resourcesItemSettings);
	
	public static final Item LUCID_CYAN_CATKIN =  new CatkinItem(GemstoneColor.CYAN, true, resourcesUncommonItemSettings);
	public static final Item LUCID_MAGENTA_CATKIN = new CatkinItem(GemstoneColor.MAGENTA, true, resourcesUncommonItemSettings);
	public static final Item LUCID_YELLOW_CATKIN = new CatkinItem(GemstoneColor.YELLOW, true, resourcesUncommonItemSettings);
	public static final Item LUCID_BLACK_CATKIN = new CatkinItem(GemstoneColor.BLACK, true, resourcesUncommonItemSettings);
	public static final Item LUCID_WHITE_CATKIN = new CatkinItem(GemstoneColor.WHITE, true, resourcesUncommonItemSettings);

	// Misc
	public static final Item MUSIC_DISC_SPECTRUM_THEME = new SpectrumMusicDiscItem(1, SpectrumSoundEvents.SPECTRUM_THEME, generalRareItemSettingsSingle);
	public static final Item MUSIC_DISC_DIMENSION_THEME = new SpectrumMusicDiscItem(2, SpectrumSoundEvents.BOSS_THEME, generalRareItemSettingsSingle);
	public static final Item SPAWNER = new SpawnerItem(Blocks.SPAWNER, generalEpicItemSettingsSingle);
	public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, generalItemSettings);
	public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, generalItemSettings);
	public static final Item INVISIBLE_GLOW_ITEM_FRAME = new InvisibleGlowItemFrameItem(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, generalItemSettings);

	public static final Item VOID_BUNDLE = new BottomlessBundleItem(generalItemSettingsSingle);
	public static final Item KNOWLEDGE_GEM = new KnowledgeGemItem(generalUncommonItemSettingsSingle, 10000);


	private static void register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), item);
	}

	public static void register() {
		register("manual", MANUAL);

		register("pedestal_tier_1_structure_placer", PEDESTAL_TIER_1_STRUCTURE_PLACER);
		register("pedestal_tier_2_structure_placer", PEDESTAL_TIER_2_STRUCTURE_PLACER);
		register("pedestal_tier_3_structure_placer", PEDESTAL_TIER_3_STRUCTURE_PLACER);
		register("fusion_shrine_structure_placer", FUSION_SHRINE_STRUCTURE_PLACER);
		register("enchanter_structure_placer", ENCHANTER_STRUCTURE_PLACER);

		registerGemstoneItems();
		registerPigments();
		registerCatkin();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerMagicalTools();
		registerFluidBuckets();
		
		register("crafting_tablet", CRAFTING_TABLET);
		register("void_bundle", VOID_BUNDLE);
		register("music_disc_spectrum_theme", MUSIC_DISC_SPECTRUM_THEME);
		register("music_disc_dimension_theme", MUSIC_DISC_DIMENSION_THEME);
		register("spawner", SPAWNER);
		register("glistering_melon_seeds", GLISTERING_MELON_SEEDS);
		register("invisible_item_frame", INVISIBLE_ITEM_FRAME);
		register("invisible_glow_item_frame", INVISIBLE_GLOW_ITEM_FRAME);
		register("knowledge_gem", KNOWLEDGE_GEM);
	}
		
	public static void registerGemstoneItems() {
		register("topaz_shard", TOPAZ_SHARD);
		register("citrine_shard", CITRINE_SHARD);
		register("onyx_shard", ONYX_SHARD);
		register("moonstone_shard", MOONSTONE_SHARD);
		register("spectral_shard", SPECTRAL_SHARD);
		
		register("topaz_powder", TOPAZ_POWDER);
		register("amethyst_powder", AMETHYST_POWDER);
		register("citrine_powder", CITRINE_POWDER);
		register("onyx_powder", ONYX_POWDER);
		register("moonstone_powder", MOONSTONE_POWDER);
	}
	
	public static void registerPigments() {
		register("white_pigment", WHITE_PIGMENT);
		register("orange_pigment", ORANGE_PIGMENT);
		register("magenta_pigment", MAGENTA_PIGMENT);
		register("light_blue_pigment", LIGHT_BLUE_PIGMENT);
		register("yellow_pigment", YELLOW_PIGMENT);
		register("lime_pigment", LIME_PIGMENT);
		register("pink_pigment", PINK_PIGMENT);
		register("gray_pigment", GRAY_PIGMENT);
		register("light_gray_pigment", LIGHT_GRAY_PIGMENT);
		register("cyan_pigment", CYAN_PIGMENT);
		register("purple_pigment", PURPLE_PIGMENT);
		register("blue_pigment", BLUE_PIGMENT);
		register("brown_pigment", BROWN_PIGMENT);
		register("green_pigment", GREEN_PIGMENT);
		register("red_pigment", RED_PIGMENT);
		register("black_pigment", BLACK_PIGMENT);
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
		register("refined_azurite", REFINED_AZURITE);
		register("paletur_fragments", PALETUR_FRAGMENTS);
		register("paletur_gem", PALETUR_GEM);
		register("scarlet_fragments", SCARLET_FRAGMENTS);
		register("scarlet_gem", SCARLET_GEM);

		register("quitoxic_powder", QUITOXIC_POWDER);
		register("mermaids_gem", MERMAIDS_GEM);
		register("lightning_stone", LIGHTNING_STONE);
		register("shooting_star", SHOOTING_STAR);
		register("stardust", STARDUST);

		register("vegetal", VEGETAL);
		register("neolith", CORRUPTED_OBSIDIAN_DUST);
		register("bedrock_dust", BEDROCK_DUST);
		register("midnight_aberration", MIDNIGHT_ABERRATION);
	}
	
	public static void registerDecayBottles() {
		register("bottle_of_fading", BOTTLE_OF_FADING);
		register("bottle_of_failing", BOTTLE_OF_FAILING);
		register("bottle_of_ruin", BOTTLE_OF_RUIN);
		register("bottle_of_terror", BOTTLE_OF_TERROR);
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

		register("glow_vision_helmet", GLOW_VISION_HELMET);
		
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
		register("ender_bag", ENDER_BAG);
		register("light_staff", LIGHT_STAFF);
		register("natures_staff", NATURES_STAFF);
		register("placement_staff", PLACEMENT_STAFF);
		register("exchange_staff", EXCHANGE_STAFF);
		register("block_flooder", BLOCK_FLOODER);
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
