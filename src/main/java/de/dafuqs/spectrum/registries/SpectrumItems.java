package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.gravity.CloakedGravityItem;
import de.dafuqs.spectrum.blocks.jade_vines.GerminatedJadeVineSeedsItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.GemstoneArmorItem;
import de.dafuqs.spectrum.items.armor.SpectrumArmorItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleGlowItemFrameItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleItemFrameItem;
import de.dafuqs.spectrum.items.magic.ArtistsPaletteItem;
import de.dafuqs.spectrum.items.magic.InkAssortmentItem;
import de.dafuqs.spectrum.items.magic.InkFlaskItem;
import de.dafuqs.spectrum.items.magic.PigmentPaletteItem;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
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

import static de.dafuqs.spectrum.registries.SpectrumFluids.*;

public class SpectrumItems {
	
	// FIRST ITEM GROUP
	public static FabricItemSettings generalItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0);
	public static FabricItemSettings generalItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1);
	public static FabricItemSettings generalItemSettingsSingleUncommon = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(8);
	public static FabricItemSettings generalItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(16);
	public static FabricItemSettings generalUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalUncommonItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON).maxCount(8);
	public static FabricItemSettings generalRareItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(8);
	public static FabricItemSettings generalRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(64);
	public static FabricItemSettings generalRareItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(1);
	public static FabricItemSettings generalEpicItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.EPIC).maxCount(1);
	
	public static FabricItemSettings toolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1);
	public static FabricItemSettings toolItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1);
	public static FabricItemSettings toolItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(16);
	public static FabricItemSettings toolUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(64).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings toolUncommonItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(1);
	public static FabricItemSettings toolUncommonItemSettingsSingleFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(1).fireproof();
	public static FabricItemSettings toolUncommonItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(16);
	public static FabricItemSettings toolRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE);
	public static FabricItemSettings toolRareItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).maxCount(1);
	
	public static FabricItemSettings worldgenItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2);
	public static FabricItemSettings worldgenItemSettingsFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).fireproof();
	public static FabricItemSettings worldgenItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).rarity(Rarity.RARE);
	
	public static FabricItemSettings resourcesItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3);
	public static FabricItemSettings resourcesItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(16);
	public static FabricItemSettings resourcesItemSettingUncommonSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(1);
	public static FabricItemSettings resourcesUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(64);
	public static FabricItemSettings resourcesRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.RARE).maxCount(64);
	public static FabricItemSettings resourcesItemSettingsFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(64).fireproof();
	public static FabricItemSettings resourcesItemSettingsSixteenFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(16).fireproof();
	
	// SECOND ITEM GROUP
	public static FabricItemSettings decorationItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0);
	public static FabricItemSettings decorationItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).rarity(Rarity.RARE);
	public static FabricItemSettings decorationItemSettingsFireProof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).fireproof();
	
	public static FabricItemSettings coloredWoodItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(1);
	
	public static FabricItemSettings mobHeadItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(2).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings mobBlockItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(2).rarity(Rarity.UNCOMMON);
	
	// added to item group / tab programmatically in the item group itself with enchantments included
	public static FabricItemSettings spectrumBedrockToolItemSettings = new FabricItemSettings().rarity(Rarity.RARE).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability());
	public static FabricItemSettings spectrumBedrockArmorItemSettings = new FabricItemSettings().rarity(Rarity.RARE).fireproof().maxDamage(0);
	public static FabricItemSettings spectrumEmergencyArmorItemSettings = new FabricItemSettings().rarity(Rarity.RARE).maxDamage(SpectrumArmorMaterials.EMERGENCY.getDurability(EquipmentSlot.CHEST));
	public static FabricItemSettings spectrumLowHealthToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability());
	public static FabricItemSettings spectrumLowVoidingToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VOIDING.getDurability());
	public static FabricItemSettings spectrumMultiToolItemSettings = new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(ToolMaterials.IRON.getDurability());
	
	// Main items
	public static final Item MANUAL = new ManualItem(toolItemSettingsSingle);
	public static final Item CRAFTING_TABLET = new CraftingTabletItem(toolItemSettingsSingle);

	public static final Item PEDESTAL_TIER_1_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_simple_structure_place"));
	public static final Item PEDESTAL_TIER_2_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_advanced_structure_place"));
	public static final Item PEDESTAL_TIER_3_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "pedestal_complex_structure_place"));
	public static final Item FUSION_SHRINE_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine_structure"));
	public static final Item ENCHANTER_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "enchanter_structure"));
	public static final Item SPIRIT_INSTILLER_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, new Identifier(SpectrumCommon.MOD_ID, "spirit_instiller_structure"));

	// Gem shards
	public static final Item TOPAZ_SHARD = new Item(resourcesItemSettings);
	public static final Item CITRINE_SHARD = new Item(resourcesItemSettings);
	public static final Item ONYX_SHARD = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "collect_all_basic_pigments_besides_brown"), Items.BLACK_DYE);
	public static final Item MOONSTONE_SHARD = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/break_decayed_bedrock"), Items.WHITE_DYE);
	public static final Item SPECTRAL_SHARD = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "lategame/build_complex_pedestal_structure"), Items.LIGHT_GRAY_DYE);
	
	private static final Identifier GEMSTONE_POWDER_CLOAK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "place_pedestal");
	public static final Item TOPAZ_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.CYAN);
	public static final Item AMETHYST_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.MAGENTA);
	public static final Item CITRINE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.YELLOW);
	public static final Item ONYX_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "create_onyx_shard"), BuiltinGemstoneColor.BLACK);
	public static final Item MOONSTONE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/collect_moonstone_shard"), BuiltinGemstoneColor.WHITE);

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
	public static final Item LOOTING_FALCHION = new SpectrumSwordItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 4, -2.2F, spectrumLowHealthToolItemSettings);
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
	
	// Decay drops
	public static final Item VEGETAL = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "craft_bottle_of_fading"), Items.GUNPOWDER);
	public static final Item NEOLITH = new CloakedItem(resourcesUncommonItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/craft_bottle_of_failing"), Items.GUNPOWDER);
	public static final Item BEDROCK_DUST = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/break_decayed_bedrock"), Items.GUNPOWDER);
	
	public static final Item MIDNIGHT_ABERRATION = new MidnightAberrationItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/create_midnight_aberration"), SpectrumItems.SPECTRAL_SHARD);
	public static final Item MIDNIGHT_CHIP = new CloakedItem(resourcesRareItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/create_midnight_aberration"), SpectrumItems.SPECTRAL_SHARD);

	// Fluid Buckets
	public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(LIQUID_CRYSTAL, toolItemSettingsSingle);
	public static final Item MUD_BUCKET = new BucketItem(MUD, toolItemSettingsSingle);
	public static final Item MIDNIGHT_SOLUTION_BUCKET = new BucketItem(MIDNIGHT_SOLUTION, toolItemSettingsSingle);

	// Decay bottles
	public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_TERROR = new DecayPlacerItem(SpectrumBlocks.TERROR, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_DECAY_AWAY = new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY, toolItemSettingsSixteen);

	// Resources
	public static final CloakedItem SPARKLESTONE_GEM = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.SPARKLESTONE_ORE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE);
	public static final CloakedItem RAW_AZURITE = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedItem REFINED_AZURITE = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedGravityItem SCARLET_FRAGMENTS = new CloakedGravityItem(resourcesItemSettingsFireproof, 1.003F, ((Cloakable) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedGravityItem SCARLET_GEM = new CloakedGravityItem(resourcesItemSettingsSixteenFireproof, 1.02F, ((Cloakable) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedGravityItem PALETUR_FRAGMENTS = new CloakedGravityItem(resourcesItemSettings, 0.997F, ((Cloakable) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	public static final CloakedGravityItem PALETUR_GEM = new CloakedGravityItem(resourcesItemSettingsSixteen, 0.98F, ((Cloakable) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);

	public static final CloakedItem QUITOXIC_POWDER = new CloakedItem(resourcesItemSettings, ((Cloakable) SpectrumBlocks.QUITOXIC_REEDS).getCloakAdvancementIdentifier(), Items.PURPLE_DYE);
	public static final CloakedItem LIGHTNING_STONE = new CloakedItem(resourcesItemSettingsSixteen, ((Cloakable) SpectrumBlocks.STUCK_LIGHTNING_STONE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE);
	public static final CloakedItem MERMAIDS_GEM = new CloakedItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "craft_using_pedestal"), Items.LIGHT_BLUE_DYE);
	public static final CloakedItem SHOOTING_STAR = new CloakedItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_shooting_stars"), Items.PURPLE_DYE);
	public static final CloakedItem STARDUST = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_shooting_stars"), Items.PURPLE_DYE);
	
	public static final Item ANCIENT_JADE_VINE_SEEDS = new Item(resourcesItemSettingsSixteen);
	public static final CloakedItem GERMINATED_JADE_VINE_SEEDS = new GerminatedJadeVineSeedsItem(resourcesItemSettingsSixteen, new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure"), Items.LIME_DYE);
	public static final CloakedItem JADE_VINE_PETALS = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure"), Items.LIME_DYE);
	public static final CloakedItem MOONSTRUCK_NECTAR = new CloakedItem(resourcesItemSettings, new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure"), Items.LIME_DYE);
	public static final Item RESTORATION_TEA = new RestorationTeaItem(toolUncommonItemSettingsSixteen);

	// Magical Tools
	public static final Item ENDER_BAG = new EnderBagItem(toolItemSettingsSingle);
	public static final Item RADIANCE_STAFF = new RadianceStaffItem(toolUncommonItemSettingsSingle);
	public static final Item NATURES_STAFF = new NaturesStaffItem(toolUncommonItemSettingsSingle);
	public static final Item PLACEMENT_STAFF = new PlacementStaffItem(toolUncommonItemSettingsSingle);
	public static final Item EXCHANGE_STAFF = new ExchangeStaffItem(toolUncommonItemSettingsSingle);
	public static final Item BLOCK_FLOODER = new BlockFlooderItem(toolUncommonItemSettings);
	public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(toolUncommonItemSettingsSixteen);
	public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(toolRareItemSettings);
	
	// Catkin
	public static final Item VIBRANT_CYAN_CATKIN = new CatkinItem(BuiltinGemstoneColor.CYAN, false, resourcesItemSettings);
	public static final Item VIBRANT_MAGENTA_CATKIN =  new CatkinItem(BuiltinGemstoneColor.MAGENTA, false, resourcesItemSettings);
	public static final Item VIBRANT_YELLOW_CATKIN = new CatkinItem(BuiltinGemstoneColor.YELLOW, false, resourcesItemSettings);
	public static final Item VIBRANT_BLACK_CATKIN = new CatkinItem(BuiltinGemstoneColor.BLACK, false, resourcesItemSettings);
	public static final Item VIBRANT_WHITE_CATKIN = new CatkinItem(BuiltinGemstoneColor.WHITE, false, resourcesItemSettings);
	
	public static final Item LUCID_CYAN_CATKIN =  new CatkinItem(BuiltinGemstoneColor.CYAN, true, resourcesUncommonItemSettings);
	public static final Item LUCID_MAGENTA_CATKIN = new CatkinItem(BuiltinGemstoneColor.MAGENTA, true, resourcesUncommonItemSettings);
	public static final Item LUCID_YELLOW_CATKIN = new CatkinItem(BuiltinGemstoneColor.YELLOW, true, resourcesUncommonItemSettings);
	public static final Item LUCID_BLACK_CATKIN = new CatkinItem(BuiltinGemstoneColor.BLACK, true, resourcesUncommonItemSettings);
	public static final Item LUCID_WHITE_CATKIN = new CatkinItem(BuiltinGemstoneColor.WHITE, true, resourcesUncommonItemSettings);

	// Misc
	public static final Item MUSIC_DISC_SPECTRUM_THEME = new SpectrumMusicDiscItem(1, SpectrumSoundEvents.SPECTRUM_THEME, toolRareItemSettingsSingle);
	public static final Item MUSIC_DISC_DIMENSION_THEME = new SpectrumMusicDiscItem(2, SpectrumSoundEvents.BOSS_THEME, toolRareItemSettingsSingle);
	public static final Item SPAWNER = new SpectrumMobSpawnerItem(Blocks.SPAWNER, generalEpicItemSettingsSingle);
	public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, toolItemSettings);
	public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, toolItemSettings);
	public static final Item INVISIBLE_GLOW_ITEM_FRAME = new InvisibleGlowItemFrameItem(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, toolItemSettings);

	public static final Item BOTTOMLESS_BUNDLE = new BottomlessBundleItem(toolItemSettingsSingle);
	public static final Item KNOWLEDGE_GEM = new KnowledgeGemItem(toolUncommonItemSettingsSingle, 10000);
	public static final Item CELESTIAL_POCKETWATCH = new CelestialPocketWatchItem(toolUncommonItemSettingsSingle);
	public static final Item GILDED_BOOK = new GildedBookItem(toolUncommonItemSettingsSingle);
	
	// Trinkets
	public static final Item FANCIFUL_BELT = new Item(toolUncommonItemSettingsSixteen);
	public static final Item FANCIFUL_PENDANT = new Item(toolUncommonItemSettingsSixteen);
	public static final Item FANCIFUL_STONE_RING = new Item(toolUncommonItemSettingsSixteen);
	public static final Item FANCIFUL_CIRCLET = new Item(toolUncommonItemSettingsSixteen);
	
	public static final Item GLOW_VISION_GOGGLES = new GlowVisionGogglesItem(toolUncommonItemSettingsSingle);
	public static final Item JEOPARDANT = new AttackRingItem(toolUncommonItemSettingsSingle);
	public static final Item SEVEN_LEAGUE_BOOTS = new SevenLeagueBootsItem(toolUncommonItemSettingsSingle);
	public static final Item RADIANCE_PIN = new RadiancePinItem(toolUncommonItemSettingsSingle);
	public static final Item TOTEM_PENDANT = new TotemPendantItem(toolUncommonItemSettingsSingle);
	public static final Item TAKE_OFF_BELT = new TakeOffBeltItem(toolUncommonItemSettingsSingle);
	public static final Item AZURE_DIKE_BELT = new AzureDikeBeltItem(toolUncommonItemSettingsSingle);
	public static final Item AZURE_DIKE_RING = new AzureDikeRingItem(toolUncommonItemSettingsSingle);
	
	public static final InkFlaskItem INK_FLASK = new InkFlaskItem(toolItemSettingsSingle, 64 * 64 * 100); // 64 stacks of pigments (1 pigment => 100 energy)
	public static final InkAssortmentItem INK_ASSORTMENT = new InkAssortmentItem(toolItemSettingsSingle, 64 * 100);
	public static final PigmentPaletteItem PIGMENT_PALETTE = new PigmentPaletteItem(toolUncommonItemSettingsSingle, 64 * 64 * 64 * 100);
	public static final ArtistsPaletteItem ARTISTS_PALETTE = new ArtistsPaletteItem(toolUncommonItemSettingsSingle, 64 * 64 * 64 * 64 * 100);
	
	public static final Item GLEAMING_PIN = new GleamingPinItem(toolUncommonItemSettingsSingle);
	public static final Item LESSER_POTION_PENDANT = new PotionPendantItem(toolUncommonItemSettingsSingle, 1, 2, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_lesser_potion_pendant"));
	public static final Item GREATER_POTION_PENDANT = new PotionPendantItem(toolUncommonItemSettingsSingle, 3, 0,  new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_greater_potion_pendant"));
	public static final Item ASHEN_CIRCLET = new AshenCircletItem(toolUncommonItemSettingsSingleFireproof);
	public static final Item TIDAL_CIRCLET = new TidalCircletItem(toolUncommonItemSettingsSingle);
	public static final Item PUFF_CIRCLET = new PuffCircletItem(toolUncommonItemSettingsSingle);
	public static final Item WHISPY_CIRCLET = new WhispyCircletItem(toolUncommonItemSettingsSingle);
	public static final Item NEAT_RING = new NeatRingItem(toolRareItemSettingsSingle);

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
		register("spirit_instiller_structure_placer", SPIRIT_INSTILLER_STRUCTURE_PLACER);

		registerGemstoneItems();
		registerPigments();
		registerCatkin();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerMagicalTools();
		registerTrinkets();
		registerFluidBuckets();
		
		register("crafting_tablet", CRAFTING_TABLET);
		register("void_bundle", BOTTOMLESS_BUNDLE);
		register("music_disc_spectrum_theme", MUSIC_DISC_SPECTRUM_THEME);
		register("music_disc_dimension_theme", MUSIC_DISC_DIMENSION_THEME);
		register("spawner", SPAWNER);
		register("glistering_melon_seeds", GLISTERING_MELON_SEEDS);
		register("invisible_item_frame", INVISIBLE_ITEM_FRAME);
		register("invisible_glow_item_frame", INVISIBLE_GLOW_ITEM_FRAME);
		register("knowledge_gem", KNOWLEDGE_GEM);
		register("celestial_pocketwatch", CELESTIAL_POCKETWATCH);
		register("gilded_book", GILDED_BOOK);
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
		
		register("ancient_jade_vine_seeds", ANCIENT_JADE_VINE_SEEDS);
		register("germinated_jade_vine_seeds", GERMINATED_JADE_VINE_SEEDS);
		register("jade_vine_petals", JADE_VINE_PETALS);
		register("moonstruck_nectar", MOONSTRUCK_NECTAR);

		register("vegetal", VEGETAL);
		register("neolith", NEOLITH);
		register("bedrock_dust", BEDROCK_DUST);
		register("midnight_aberration", MIDNIGHT_ABERRATION);
		register("midnight_chip", MIDNIGHT_CHIP);
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
		register("light_staff", RADIANCE_STAFF);
		register("natures_staff", NATURES_STAFF);
		register("placement_staff", PLACEMENT_STAFF);
		register("exchange_staff", EXCHANGE_STAFF);
		register("block_flooder", BLOCK_FLOODER);
		register("ender_splice", ENDER_SPLICE);
		register("end_portal_cracker", END_PORTAL_CRACKER);
		register("restoration_tea", RESTORATION_TEA);
	}
	
	public static void registerTrinkets() {
		register("fanciful_stone_ring", FANCIFUL_STONE_RING);
		register("fanciful_belt", FANCIFUL_BELT);
		register("fanciful_pendant", FANCIFUL_PENDANT);
		register("fanciful_circlet", FANCIFUL_CIRCLET);
		
		register("glow_vision_helmet", GLOW_VISION_GOGGLES);
		register("jeopardant", JEOPARDANT);
		register("seven_league_boots", SEVEN_LEAGUE_BOOTS);
		register("radiance_pin", RADIANCE_PIN);
		register("totem_pendant", TOTEM_PENDANT);
		register("take_off_belt", TAKE_OFF_BELT);
		register("azure_dike_belt", AZURE_DIKE_BELT);
		register("azure_dike_ring", AZURE_DIKE_RING);
		register("gleaming_pin", GLEAMING_PIN);
		register("lesser_potion_pendant", LESSER_POTION_PENDANT);
		register("greater_potion_pendant", GREATER_POTION_PENDANT);
		register("ashen_circlet", ASHEN_CIRCLET);
		register("tidal_circlet", TIDAL_CIRCLET);
		register("puff_circlet", PUFF_CIRCLET);
		register("whispy_circlet", WHISPY_CIRCLET);
		register("neat_ring", NEAT_RING);
		
		register("ink_flask", INK_FLASK);
		register("ink_assortment", INK_ASSORTMENT);
		register("pigment_palette", PIGMENT_PALETTE);
		register("artists_palette", ARTISTS_PALETTE);
	}
	
	public static void registerFluidBuckets() {
		register("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET);
		register("mud_bucket", MUD_BUCKET);
		register("midnight_solution_bucket", MIDNIGHT_SOLUTION_BUCKET);
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
