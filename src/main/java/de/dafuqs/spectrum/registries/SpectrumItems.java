package de.dafuqs.spectrum.registries;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.gravity.CloakedFloatItem;
import de.dafuqs.spectrum.blocks.jade_vines.GerminatedJadeVineSeedsItem;
import de.dafuqs.spectrum.blocks.jade_vines.JadeJellyItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.graces.crystal.ColorPool;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.BedrockArmorItem;
import de.dafuqs.spectrum.items.armor.GemstoneArmorItem;
import de.dafuqs.spectrum.items.conditional.CloakedGemstoneColorItem;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.items.item_frame.InvisibleGlowItemFrameItem;
import de.dafuqs.spectrum.items.item_frame.InvisibleItemFrameItem;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.color.ItemColors;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.Map;

import static de.dafuqs.spectrum.registries.SpectrumFluids.*;

public class SpectrumItems {
	
	// FIRST ITEM GROUP
	// GENERAL TAB
	public static FabricItemSettings generalItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0);
	public static FabricItemSettings generalItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1);
	public static FabricItemSettings generalItemSettingsSingleFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1).fireproof();
	public static FabricItemSettings generalUncommonItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(1).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(8);
	public static FabricItemSettings generalItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).maxCount(16);
	public static FabricItemSettings generalUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings generalUncommonItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.UNCOMMON).maxCount(8);
	public static FabricItemSettings generalRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(64);
	public static FabricItemSettings generalRareItemSettingsEight = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(8);
	public static FabricItemSettings generalRareItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.RARE).maxCount(1);
	public static FabricItemSettings generalEpicItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(0).rarity(Rarity.EPIC).maxCount(1);
	
	// TOOL TAB
	public static FabricItemSettings toolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1);
	public static FabricItemSettings toolItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1);
	public static FabricItemSettings toolItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(16);
	public static FabricItemSettings toolUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(64).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings toolUncommonItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(1);
	public static FabricItemSettings toolUncommonItemSettingsSingleFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(1).fireproof();
	public static FabricItemSettings toolUncommonItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(16);
	public static FabricItemSettings toolRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE);
	public static FabricItemSettings toolRareItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).maxCount(1);
	public static FabricItemSettings toolEpicItemSettingsSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.EPIC).maxCount(1);
	
	public static FabricItemSettings bedrockToolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability());
	public static FabricItemSettings bedrockArmorItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).fireproof().maxDamage(-1);
	public static FabricItemSettings gemstoneArmorItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.RARE).maxDamage(SpectrumArmorMaterials.EMERGENCY.getDurability(EquipmentSlot.CHEST));
	public static FabricItemSettings lowHealthToolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability());
	public static FabricItemSettings voidingToolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VOIDING.getDurability());
	public static FabricItemSettings multiToolItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxDamage(ToolMaterials.IRON.getDurability());
	
	// WORLDGEN TAB
	public static FabricItemSettings worldgenItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2);
	public static FabricItemSettings worldgenItemSettingsFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).fireproof();
	public static FabricItemSettings worldgenItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(2).rarity(Rarity.RARE);
	
	// RESOURCES TAB
	public static FabricItemSettings resourcesItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3);
	public static FabricItemSettings resourcesItemSettingsSixteen = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(16);
	public static FabricItemSettings resourcesItemSettingUncommonSingle = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(1);
	public static FabricItemSettings resourcesUncommonItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(64);
	public static FabricItemSettings resourcesRareItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.RARE).maxCount(64);
	public static FabricItemSettings resourcesItemSettingsFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(64).fireproof();
	public static FabricItemSettings resourcesItemSettingsSixteenFireproof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).maxCount(16).fireproof();
	
	// SECOND ITEM GROUP
	// DECORATION TAB
	public static FabricItemSettings decorationItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0);
	public static FabricItemSettings decorationItemSettingsUncommon = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings decorationItemSettingsRare = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).rarity(Rarity.RARE);
	public static FabricItemSettings decorationItemSettingsFireProof = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(0).fireproof();
	
	// COLORED WOOD TAB
	public static FabricItemSettings coloredWoodItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(1);
	
	// MOB HEADS+BLOCKS TAB
	public static FabricItemSettings mobHeadItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(2).rarity(Rarity.UNCOMMON);
	public static FabricItemSettings mobBlockItemSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BLOCKS).tab(2).rarity(Rarity.UNCOMMON);
	
	
	// Main items
	public static final Item MANUAL = new GuidebookItem(toolItemSettingsSingle);
	public static final Item CRAFTING_TABLET = new CraftingTabletItem(toolItemSettingsSingle);
	
	public static final Item PEDESTAL_TIER_1_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("pedestal_simple_structure_place"));
	public static final Item PEDESTAL_TIER_2_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("pedestal_advanced_structure_place"));
	public static final Item PEDESTAL_TIER_3_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("pedestal_complex_structure_place"));
	public static final Item FUSION_SHRINE_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("fusion_shrine_structure"));
	public static final Item ENCHANTER_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("enchanter_structure"));
	public static final Item SPIRIT_INSTILLER_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("spirit_instiller_structure"));
	public static final Item cinderhearth_STRUCTURE_PLACER = new StructurePlacerItem(toolItemSettingsSingle, SpectrumCommon.locate("cinderhearth_structure"));
	
	// Gem shards
	public static final Item TOPAZ_SHARD = new Item(resourcesItemSettings);
	public static final Item CITRINE_SHARD = new Item(resourcesItemSettings);
	public static final Item ONYX_SHARD = new CloakedItem(resourcesItemSettings, SpectrumCommon.locate("collect_all_basic_pigments_besides_brown"), Items.BLACK_DYE);
	public static final Item MOONSTONE_SHARD = new CloakedItem(resourcesItemSettings, SpectrumCommon.locate("midgame/break_decayed_bedrock"), Items.WHITE_DYE);
	public static final Item SPECTRAL_SHARD = new CloakedItem(resourcesRareItemSettings, SpectrumCommon.locate("lategame/build_complex_pedestal_structure"), Items.LIGHT_GRAY_DYE);
	
	private static final Identifier GEMSTONE_POWDER_CLOAK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	public static final Item TOPAZ_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.CYAN);
	public static final Item AMETHYST_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.MAGENTA);
	public static final Item CITRINE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, GEMSTONE_POWDER_CLOAK_IDENTIFIER, BuiltinGemstoneColor.YELLOW);
	public static final Item ONYX_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, SpectrumCommon.locate("create_onyx_shard"), BuiltinGemstoneColor.BLACK);
	public static final Item MOONSTONE_POWDER = new CloakedGemstoneColorItem(resourcesItemSettings, SpectrumCommon.locate("midgame/collect_moonstone_shard"), BuiltinGemstoneColor.WHITE);
	
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
	public static final Item MULTITOOL = new MultiToolItem(ToolMaterials.IRON, 2, -2.4F, multiToolItemSettings);
	public static final Item SILKER_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, lowHealthToolItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.SILK_TOUCH, 1);
		}
	};
	public static final Item FORTUNE_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, lowHealthToolItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FORTUNE, 3);
		}
	};
	public static final Item LOOTING_FALCHION = new LootingFalchionItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 4, -2.2F, lowHealthToolItemSettings);
	public static final Item VOIDING_PICKAXE = new VoidingPickaxeItem(SpectrumToolMaterials.ToolMaterial.VOIDING, 1, -2.8F, voidingToolItemSettings);
	public static final Item RESONANT_PICKAXE = new SpectrumPickaxeItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, lowHealthToolItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(SpectrumEnchantments.RESONANCE, 1);
		}
	};
	
	// Bedrock Tools
	public static final SpectrumToolMaterials.ToolMaterial BEDROCK_MATERIAL = SpectrumToolMaterials.ToolMaterial.BEDROCK;
	public static final ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BEDROCK_MATERIAL, 1, -2.8F, bedrockToolItemSettings);
	public static final ToolItem BEDROCK_AXE = new BedrockAxeItem(BEDROCK_MATERIAL, 5, -3.0F, bedrockToolItemSettings);
	public static final ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BEDROCK_MATERIAL, 1, -3.0F, bedrockToolItemSettings);
	public static final ToolItem BEDROCK_SWORD = new BedrockSwordItem(BEDROCK_MATERIAL, 4, -2.4F, bedrockToolItemSettings);
	public static final ToolItem BEDROCK_HOE = new BedrockHoeItem(BEDROCK_MATERIAL, -2, -0.0F, bedrockToolItemSettings);
	public static final BedrockBowItem BEDROCK_BOW = new BedrockBowItem(bedrockToolItemSettings);
	public static final BedrockCrossbowItem BEDROCK_CROSSBOW = new BedrockCrossbowItem(bedrockToolItemSettings);
	public static final BedrockShearsItem BEDROCK_SHEARS = new BedrockShearsItem(bedrockToolItemSettings);
	public static final FishingRodItem BEDROCK_FISHING_ROD = new BedrockFishingRodItem(bedrockToolItemSettings);
	
	public static final SwordItem DREAMFLAYER = new DreamflayerItem(SpectrumToolMaterials.ToolMaterial.DREAMFLAYER, 3, -1.8F, toolUncommonItemSettingsSingle);

	// Graces
	public static OwoItemSettings graceSettings = new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.RARE).maxCount(1).fireproof();

	public static final CrystalGraceItem WHITE_CRYSTAL_GRACE = new CrystalGraceItem(ColorPool.WHITE, graceSettings, "ooooo go stick my dick in Azzy's ass oooo");


	// Bedrock Armor
	public static final Item BEDROCK_HELMET = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, EquipmentSlot.HEAD, bedrockArmorItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROJECTILE_PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_CHESTPLATE = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, EquipmentSlot.CHEST, bedrockArmorItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_LEGGINGS = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, EquipmentSlot.LEGS, bedrockArmorItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.BLAST_PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_BOOTS = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, EquipmentSlot.FEET, bedrockArmorItemSettings) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FIRE_PROTECTION, 5);
		}
	};
	
	// Armor
	public static final ArmorMaterial EMERGENCY_ARMOR_MATERIAL = SpectrumArmorMaterials.EMERGENCY;
	public static final Item EMERGENCY_HELMET = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.HEAD, gemstoneArmorItemSettings);
	public static final Item EMERGENCY_CHESTPLATE = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.CHEST, gemstoneArmorItemSettings);
	public static final Item EMERGENCY_LEGGINGS = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.LEGS, gemstoneArmorItemSettings);
	public static final Item EMERGENCY_BOOTS = new GemstoneArmorItem(EMERGENCY_ARMOR_MATERIAL, EquipmentSlot.FEET, gemstoneArmorItemSettings);
	
	// Decay drops
	public static final Item VEGETAL = new CloakedItemWithLoomPattern(resourcesItemSettings, SpectrumCommon.locate("craft_bottle_of_fading"), Items.GUNPOWDER, SpectrumBannerPatterns.VEGETAL);
	public static final Item NEOLITH = new CloakedItemWithLoomPattern(resourcesUncommonItemSettings, SpectrumCommon.locate("midgame/craft_bottle_of_failing"), Items.GUNPOWDER, SpectrumBannerPatterns.NEOLITH);
	public static final Item BEDROCK_DUST = new CloakedItemWithLoomPattern(resourcesRareItemSettings, SpectrumCommon.locate("midgame/break_decayed_bedrock"), Items.GUNPOWDER, SpectrumBannerPatterns.BEDROCK_DUST);
	
	public static final MidnightAberrationItem MIDNIGHT_ABERRATION = new MidnightAberrationItem(resourcesRareItemSettings, SpectrumCommon.locate("midgame/create_midnight_aberration"), SpectrumItems.SPECTRAL_SHARD);
	public static final Item MIDNIGHT_CHIP = new CloakedItem(resourcesRareItemSettings, SpectrumCommon.locate("midgame/create_midnight_aberration"), Items.GRAY_DYE);
	public static final Item BISMUTH_CRYSTAL = new CloakedItem(resourcesRareItemSettings, SpectrumCommon.locate("midgame/enter_dimension"), Items.CYAN_DYE);
	
	public static final Item RAW_MALACHITE = new CloakedItem(resourcesRareItemSettings, SpectrumCommon.locate("milestones/reveal_malachite"), Items.GREEN_DYE);
	public static final Item MALACHITE_CRYSTAL = new CloakedItem(resourcesRareItemSettings, SpectrumCommon.locate("milestones/reveal_malachite"), Items.GREEN_DYE);
	
	// Fluid Buckets
	public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(LIQUID_CRYSTAL, new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1).recipeRemainder(Items.BUCKET));
	public static final Item MUD_BUCKET = new BucketItem(MUD, new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1).recipeRemainder(Items.BUCKET));
	public static final Item MIDNIGHT_SOLUTION_BUCKET = new BucketItem(MIDNIGHT_SOLUTION, new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(1).recipeRemainder(Items.BUCKET));
	
	// Decay bottles
	public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_TERROR = new DecayPlacerItem(SpectrumBlocks.TERROR, toolItemSettingsSixteen);
	public static final Item BOTTLE_OF_DECAY_AWAY = new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY, toolItemSettingsSixteen);
	
	// Resources
	public static final CloakedItem SPARKLESTONE_GEM = new CloakedItemWithLoomPattern(resourcesItemSettings, ((RevelationAware) SpectrumBlocks.SPARKLESTONE_ORE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE, SpectrumBannerPatterns.SHIMMERSTONE);
	public static final CloakedItem RAW_AZURITE = new CloakedItemWithLoomPattern(resourcesItemSettings, ((RevelationAware) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE, SpectrumBannerPatterns.RAW_AZURITE);
	public static final CloakedItem REFINED_AZURITE = new CloakedItem(resourcesItemSettings, ((RevelationAware) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedFloatItem SCARLET_FRAGMENTS = new CloakedFloatItem(resourcesItemSettingsFireproof, 1.003F, ((RevelationAware) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedFloatItem SCARLET_GEM = new CloakedFloatItem(resourcesItemSettingsSixteenFireproof, 1.02F, ((RevelationAware) SpectrumBlocks.SCARLET_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedFloatItem PALETUR_FRAGMENTS = new CloakedFloatItem(resourcesItemSettings, 0.997F, ((RevelationAware) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	public static final CloakedFloatItem PALETUR_GEM = new CloakedFloatItem(resourcesItemSettingsSixteen, 0.98F, ((RevelationAware) SpectrumBlocks.PALETUR_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	
	public static final CloakedItem QUITOXIC_POWDER = new CloakedItem(resourcesItemSettings, ((RevelationAware) SpectrumBlocks.QUITOXIC_REEDS).getCloakAdvancementIdentifier(), Items.PURPLE_DYE);
	public static final CloakedItem LIGHTNING_STONE = new CloakedItem(resourcesItemSettingsSixteen, ((RevelationAware) SpectrumBlocks.STUCK_LIGHTNING_STONE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE);
	public static final CloakedItem MERMAIDS_GEM = new CloakedItem(resourcesItemSettingsSixteen, SpectrumCommon.locate("craft_using_pedestal"), Items.LIGHT_BLUE_DYE);
	public static final CloakedItem SHOOTING_STAR = new CloakedItem(resourcesItemSettingsSixteen, SpectrumCommon.locate("milestones/unlock_shooting_stars"), Items.PURPLE_DYE);
	public static final CloakedItem STARDUST = new CloakedItemWithLoomPattern(resourcesItemSettings, SpectrumCommon.locate("milestones/unlock_shooting_stars"), Items.PURPLE_DYE, SpectrumBannerPatterns.SHIMMER);
	
	public static final Item HIBERNATING_JADE_VINE_SEEDS = new ItemWithTooltip(resourcesItemSettingsSixteen, "item.spectrum.hibernating_jade_vine_seeds.tooltip");
	public static final CloakedItem GERMINATED_JADE_VINE_SEEDS = new GerminatedJadeVineSeedsItem(resourcesItemSettingsSixteen, SpectrumCommon.locate("hidden/collect_hibernating_jade_vine_seeds"), Items.LIME_DYE);
	public static final CloakedItem JADE_VINE_PETALS = new CloakedItemWithLoomPattern(resourcesItemSettings, SpectrumCommon.locate("midgame/build_spirit_instiller_structure"), Items.LIME_DYE, SpectrumBannerPatterns.JADE_VINE);
	public static final CloakedItem MOONSTRUCK_NECTAR = new MoonstruckNectarItem(new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(3).rarity(Rarity.UNCOMMON).maxCount(16).food(MoonstruckNectarItem.FOOD_COMPONENT).recipeRemainder(Items.GLASS_BOTTLE), SpectrumCommon.locate("midgame/build_spirit_instiller_structure"), Items.LIME_DYE);
	public static final Item JADE_JELLY = new JadeJellyItem(new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).maxCount(64).rarity(Rarity.UNCOMMON).food(JadeJellyItem.FOOD_COMPONENT));
	public static final Item RESTORATION_TEA = new RestorationTeaItem(new OwoItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).tab(1).rarity(Rarity.UNCOMMON).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE));
	
	// Banner Patterns
	public static final Item LOGO_BANNER_PATTERN = new SpectrumBannerPatternItem(toolRareItemSettingsSingle, SpectrumBannerPatterns.SPECTRUM_LOGO, "item.spectrum.logo_banner_pattern.desc");
	public static final Item AMETHYST_SHARD_BANNER_PATTERN = new SpectrumBannerPatternItem(toolItemSettingsSingle, SpectrumBannerPatterns.AMETHYST_SHARD, "item.minecraft.amethyst_shard");
	public static final Item AMETHYST_CLUSTER_BANNER_PATTERN = new SpectrumBannerPatternItem(toolItemSettingsSingle, SpectrumBannerPatterns.AMETHYST_CLUSTER, "block.minecraft.amethyst_cluster");
	
	// Magical Tools
	public static final Item ENDER_BAG = new EnderBagItem(toolItemSettingsSingle);
	public static final Item RADIANCE_STAFF = new RadianceStaffItem(toolUncommonItemSettingsSingle);
	public static final Item NATURES_STAFF = new NaturesStaffItem(toolUncommonItemSettingsSingle);
	public static final Item PLACEMENT_STAFF = new PlacementStaffItem(toolUncommonItemSettingsSingle);
	public static final Item EXCHANGE_STAFF = new ExchangeStaffItem(toolUncommonItemSettingsSingle);
	public static final Item BLOCK_FLOODER = new BlockFlooderItem(toolUncommonItemSettings);
	public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(toolUncommonItemSettingsSixteen);
	public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(toolRareItemSettings);
	public static final Item CRESCENT_CLOCK = new Item(toolItemSettingsSingle);
	
	// Elemental Powder
	public static final Item FIERY_POWDER = new Item(resourcesItemSettings);
	public static final Item BLIZZARD_POWDER = new Item(resourcesItemSettings);
	
	// Catkin
	public static final Item VIBRANT_CYAN_CATKIN = new CatkinItem(BuiltinGemstoneColor.CYAN, false, resourcesItemSettings);
	public static final Item VIBRANT_MAGENTA_CATKIN = new CatkinItem(BuiltinGemstoneColor.MAGENTA, false, resourcesItemSettings);
	public static final Item VIBRANT_YELLOW_CATKIN = new CatkinItem(BuiltinGemstoneColor.YELLOW, false, resourcesItemSettings);
	public static final Item VIBRANT_BLACK_CATKIN = new CatkinItem(BuiltinGemstoneColor.BLACK, false, resourcesItemSettings);
	public static final Item VIBRANT_WHITE_CATKIN = new CatkinItem(BuiltinGemstoneColor.WHITE, false, resourcesItemSettings);
	
	public static final Item LUCID_CYAN_CATKIN = new CatkinItem(BuiltinGemstoneColor.CYAN, true, resourcesUncommonItemSettings);
	public static final Item LUCID_MAGENTA_CATKIN = new CatkinItem(BuiltinGemstoneColor.MAGENTA, true, resourcesUncommonItemSettings);
	public static final Item LUCID_YELLOW_CATKIN = new CatkinItem(BuiltinGemstoneColor.YELLOW, true, resourcesUncommonItemSettings);
	public static final Item LUCID_BLACK_CATKIN = new CatkinItem(BuiltinGemstoneColor.BLACK, true, resourcesUncommonItemSettings);
	public static final Item LUCID_WHITE_CATKIN = new CatkinItem(BuiltinGemstoneColor.WHITE, true, resourcesUncommonItemSettings);
	
	// Misc
	public static final Item MUSIC_DISC_SPECTRUM_THEME = new SpectrumMusicDiscItem(1, SpectrumSoundEvents.SPECTRUM_THEME, toolRareItemSettingsSingle);
	public static final Item MUSIC_DISC_DIMENSION_THEME = new SpectrumMusicDiscItem(2, SpectrumSoundEvents.BOSS_THEME, toolRareItemSettingsSingle);
	public static final Item SPAWNER = new SpectrumMobSpawnerItem(generalEpicItemSettingsSingle);
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
	public static final Item FANCIFUL_GLOVES = new Item(toolUncommonItemSettingsSixteen);
	
	public static final Item GLOW_VISION_GOGGLES = new GlowVisionGogglesItem(toolUncommonItemSettingsSingle);
	public static final Item JEOPARDANT = new AttackRingItem(toolUncommonItemSettingsSingle);
	public static final Item SEVEN_LEAGUE_BOOTS = new SevenLeagueBootsItem(toolUncommonItemSettingsSingle);
	public static final Item RADIANCE_PIN = new RadiancePinItem(toolUncommonItemSettingsSingle);
	public static final Item TOTEM_PENDANT = new TotemPendantItem(toolUncommonItemSettingsSingle);
	public static final Item TAKE_OFF_BELT = new TakeOffBeltItem(toolUncommonItemSettingsSingle);
	public static final Item AZURE_DIKE_BELT = new AzureDikeBeltItem(toolUncommonItemSettingsSingle);
	public static final Item AZURE_DIKE_RING = new AzureDikeRingItem(toolUncommonItemSettingsSingle);
	public static final AzureDikeAmuletItem SHIELDGRASP_AMULET = new AzureDikeAmuletItem(toolUncommonItemSettingsSingle);
	public static final ExtraHealthRingItem HEARTSINGERS_REWARD_RING = new ExtraHealthRingItem(toolUncommonItemSettingsSingle);
	public static final ExtraReachGlovesItem GLOVES_OF_DAWNS_GRASP = new ExtraReachGlovesItem(toolUncommonItemSettingsSingle);
	
	public static final InkFlaskItem INK_FLASK = new InkFlaskItem(toolItemSettingsSingle, 64 * 64 * 100); // 64 stacks of pigments (1 pigment => 100 energy)
	public static final InkAssortmentItem INK_ASSORTMENT = new InkAssortmentItem(toolItemSettingsSingle, 64 * 100);
	public static final PigmentPaletteItem PIGMENT_PALETTE = new PigmentPaletteItem(toolUncommonItemSettingsSingle, 64 * 64 * 100);
	public static final ArtistsPaletteItem ARTISTS_PALETTE = new ArtistsPaletteItem(toolUncommonItemSettingsSingle, 64 * 64 * 64 * 64 * 100);
	public static final CreativeInkAssortmentItem CREATIVE_INK_ASSORTMENT = new CreativeInkAssortmentItem(toolEpicItemSettingsSingle);
	
	public static final Item GLEAMING_PIN = new GleamingPinItem(toolUncommonItemSettingsSingle);
	public static final Item LESSER_POTION_PENDANT = new PotionPendantItem(toolUncommonItemSettingsSingle, 1, 2, SpectrumCommon.locate("progression/unlock_lesser_potion_pendant"));
	public static final Item GREATER_POTION_PENDANT = new PotionPendantItem(toolUncommonItemSettingsSingle, 3, 0, SpectrumCommon.locate("progression/unlock_greater_potion_pendant"));
	public static final Item ASHEN_CIRCLET = new AshenCircletItem(toolUncommonItemSettingsSingleFireproof);
	public static final Item TIDAL_CIRCLET = new TidalCircletItem(toolUncommonItemSettingsSingle);
	public static final Item PUFF_CIRCLET = new PuffCircletItem(toolUncommonItemSettingsSingle);
	public static final Item WHISPY_CIRCLET = new WhispyCircletItem(toolUncommonItemSettingsSingle);
	public static final Item NEAT_RING = new NeatRingItem(toolRareItemSettingsSingle);
	
	// Native Clusters
	public static final Item NATIVE_EMERALD = new Item(resourcesItemSettings);
	public static final Item NATIVE_PRISMARINE = new Item(resourcesItemSettings);
	public static final Item NATIVE_COAL = new Item(resourcesItemSettings);
	public static final Item NATIVE_REDSTONE = new Item(resourcesItemSettings);
	public static final Item NATIVE_GLOWSTONE = new Item(resourcesItemSettings);
	public static final Item NATIVE_LAPIS = new Item(resourcesItemSettings);
	public static final Item NATIVE_COPPER = new Item(resourcesItemSettings);
	public static final Item NATIVE_QUARTZ = new Item(resourcesItemSettings);
	public static final Item NATIVE_GOLD = new Item(resourcesItemSettings);
	public static final Item NATIVE_DIAMOND = new Item(resourcesItemSettings);
	public static final Item NATIVE_IRON = new Item(resourcesItemSettings);
	public static final Item NATIVE_NETHERITE = new Item(resourcesItemSettings);
	
	public static final Item NATIVE_CERTUS_QUARTZ = new Item(resourcesItemSettings);
	public static final Item NATIVE_FLUIX = new Item(resourcesItemSettings);
	public static final Item NATIVE_GLOBETTE = new Item(resourcesItemSettings);
	public static final Item NATIVE_GLOBETTE_NETHER = new Item(resourcesItemSettings);
	public static final Item NATIVE_GLOBETTE_END = new Item(resourcesItemSettings);
	
	
	private static void register(String name, Item item, DyeColor dyeColor) {
		Registry.register(Registry.ITEM, SpectrumCommon.locate(name), item);
		ItemColors.ITEM_COLORS.registerColorMapping(item, dyeColor);
	}
	
	public static void register() {
		register("manual", MANUAL, DyeColor.WHITE);
		
		registerStructurePlacers();
		registerGemstoneItems();
		registerGraces();
		registerPigments();
		registerCatkin();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerMagicalTools();
		registerTrinkets();
		registerFluidBuckets();
		registerBannerPatterns();
		registerNativeClusters();
		
		register("crafting_tablet", CRAFTING_TABLET, DyeColor.LIGHT_GRAY);
		register("void_bundle", BOTTOMLESS_BUNDLE, DyeColor.LIGHT_GRAY);
		register("music_disc_spectrum_theme", MUSIC_DISC_SPECTRUM_THEME, DyeColor.GREEN);
		register("music_disc_dimension_theme", MUSIC_DISC_DIMENSION_THEME, DyeColor.GREEN);
		register("spawner", SPAWNER, DyeColor.LIGHT_GRAY);
		register("glistering_melon_seeds", GLISTERING_MELON_SEEDS, DyeColor.LIME);
		register("invisible_item_frame", INVISIBLE_ITEM_FRAME, DyeColor.YELLOW);
		register("invisible_glow_item_frame", INVISIBLE_GLOW_ITEM_FRAME, DyeColor.YELLOW);
		register("knowledge_gem", KNOWLEDGE_GEM, DyeColor.PURPLE);
		register("celestial_pocketwatch", CELESTIAL_POCKETWATCH, DyeColor.MAGENTA);
		register("gilded_book", GILDED_BOOK, DyeColor.PURPLE);
	}

	public static void registerNativeClusters() {
		register("native_coal", NATIVE_COAL, DyeColor.BROWN);
		register("native_iron", NATIVE_IRON, DyeColor.BROWN);
		register("native_gold", NATIVE_GOLD, DyeColor.BROWN);
		register("native_diamond", NATIVE_DIAMOND, DyeColor.CYAN);
		register("native_emerald", NATIVE_EMERALD, DyeColor.CYAN);
		register("native_redstone", NATIVE_REDSTONE, DyeColor.RED);
		register("native_lapis", NATIVE_LAPIS, DyeColor.PURPLE);
		register("native_copper", NATIVE_COPPER, DyeColor.BROWN);
		register("native_quartz", NATIVE_QUARTZ, DyeColor.BROWN);
		register("native_netherite", NATIVE_NETHERITE, DyeColor.BROWN);
		register("native_glowstone", NATIVE_GLOWSTONE, DyeColor.YELLOW);
		register("native_prismarine", NATIVE_PRISMARINE, DyeColor.CYAN);
		
		register("native_certus_quartz", NATIVE_CERTUS_QUARTZ, DyeColor.YELLOW);
		register("native_fluix", NATIVE_FLUIX, DyeColor.YELLOW);
		
		register("native_globette", NATIVE_GLOBETTE, DyeColor.BLUE);
		register("native_globette_nether", NATIVE_GLOBETTE_NETHER, DyeColor.RED);
		register("native_globette_end", NATIVE_GLOBETTE_END, DyeColor.GREEN);
	}
	
	public static void registerGraces() {
		register("white_crystal_grace", WHITE_CRYSTAL_GRACE, DyeColor.WHITE);
	}
	
	public static void registerStructurePlacers() {
		register("pedestal_tier_1_structure_placer", PEDESTAL_TIER_1_STRUCTURE_PLACER, DyeColor.WHITE);
		register("pedestal_tier_2_structure_placer", PEDESTAL_TIER_2_STRUCTURE_PLACER, DyeColor.WHITE);
		register("pedestal_tier_3_structure_placer", PEDESTAL_TIER_3_STRUCTURE_PLACER, DyeColor.WHITE);
		register("fusion_shrine_structure_placer", FUSION_SHRINE_STRUCTURE_PLACER, DyeColor.WHITE);
		register("enchanter_structure_placer", ENCHANTER_STRUCTURE_PLACER, DyeColor.WHITE);
		register("spirit_instiller_structure_placer", SPIRIT_INSTILLER_STRUCTURE_PLACER, DyeColor.WHITE);
		register("cinderhearth_structure_placer", cinderhearth_STRUCTURE_PLACER, DyeColor.WHITE);
	}
	
	public static void registerBannerPatterns() {
		register("logo_banner_pattern", LOGO_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("amethyst_shard_banner_pattern", AMETHYST_SHARD_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("amethyst_cluster_banner_pattern", AMETHYST_CLUSTER_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
	}
	
	public static void registerGemstoneItems() {
		register("topaz_shard", TOPAZ_SHARD, DyeColor.CYAN);
		register("citrine_shard", CITRINE_SHARD, DyeColor.YELLOW);
		register("onyx_shard", ONYX_SHARD, DyeColor.BLACK);
		register("moonstone_shard", MOONSTONE_SHARD, DyeColor.WHITE);
		register("spectral_shard", SPECTRAL_SHARD, DyeColor.WHITE);
		
		register("topaz_powder", TOPAZ_POWDER, DyeColor.CYAN);
		register("amethyst_powder", AMETHYST_POWDER, DyeColor.MAGENTA);
		register("citrine_powder", CITRINE_POWDER, DyeColor.YELLOW);
		register("onyx_powder", ONYX_POWDER, DyeColor.BLACK);
		register("moonstone_powder", MOONSTONE_POWDER, DyeColor.WHITE);
	}
	
	public static void registerPigments() {
		register("white_pigment", WHITE_PIGMENT, DyeColor.WHITE);
		register("orange_pigment", ORANGE_PIGMENT, DyeColor.ORANGE);
		register("magenta_pigment", MAGENTA_PIGMENT, DyeColor.MAGENTA);
		register("light_blue_pigment", LIGHT_BLUE_PIGMENT, DyeColor.LIGHT_BLUE);
		register("yellow_pigment", YELLOW_PIGMENT, DyeColor.YELLOW);
		register("lime_pigment", LIME_PIGMENT, DyeColor.LIME);
		register("pink_pigment", PINK_PIGMENT, DyeColor.PINK);
		register("gray_pigment", GRAY_PIGMENT, DyeColor.GRAY);
		register("light_gray_pigment", LIGHT_GRAY_PIGMENT, DyeColor.LIGHT_GRAY);
		register("cyan_pigment", CYAN_PIGMENT, DyeColor.CYAN);
		register("purple_pigment", PURPLE_PIGMENT, DyeColor.PURPLE);
		register("blue_pigment", BLUE_PIGMENT, DyeColor.BLUE);
		register("brown_pigment", BROWN_PIGMENT, DyeColor.BROWN);
		register("green_pigment", GREEN_PIGMENT, DyeColor.GREEN);
		register("red_pigment", RED_PIGMENT, DyeColor.RED);
		register("black_pigment", BLACK_PIGMENT, DyeColor.BLACK);
	}
	
	public static void registerCatkin() {
		register("vibrant_cyan_catkin", VIBRANT_CYAN_CATKIN, DyeColor.CYAN);
		register("vibrant_magenta_catkin", VIBRANT_MAGENTA_CATKIN, DyeColor.MAGENTA);
		register("vibrant_yellow_catkin", VIBRANT_YELLOW_CATKIN, DyeColor.YELLOW);
		register("vibrant_black_catkin", VIBRANT_BLACK_CATKIN, DyeColor.BLACK);
		register("vibrant_white_catkin", VIBRANT_WHITE_CATKIN, DyeColor.WHITE);
		
		register("lucid_cyan_catkin", LUCID_CYAN_CATKIN, DyeColor.CYAN);
		register("lucid_magenta_catkin", LUCID_MAGENTA_CATKIN, DyeColor.MAGENTA);
		register("lucid_yellow_catkin", LUCID_YELLOW_CATKIN, DyeColor.YELLOW);
		register("lucid_black_catkin", LUCID_BLACK_CATKIN, DyeColor.BLACK);
		register("lucid_white_catkin", LUCID_WHITE_CATKIN, DyeColor.WHITE);
	}
	
	public static void registerResources() {
		register("sparklestone_gem", SPARKLESTONE_GEM, DyeColor.YELLOW);
		register("raw_azurite", RAW_AZURITE, DyeColor.BLUE);
		register("refined_azurite", REFINED_AZURITE, DyeColor.BLUE);
		register("paletur_fragments", PALETUR_FRAGMENTS, DyeColor.LIGHT_BLUE);
		register("paletur_gem", PALETUR_GEM, DyeColor.LIGHT_BLUE);
		register("scarlet_fragments", SCARLET_FRAGMENTS, DyeColor.RED);
		register("scarlet_gem", SCARLET_GEM, DyeColor.RED);
		
		register("quitoxic_powder", QUITOXIC_POWDER, DyeColor.PURPLE);
		register("mermaids_gem", MERMAIDS_GEM, DyeColor.LIGHT_BLUE);
		register("lightning_stone", LIGHTNING_STONE, DyeColor.YELLOW);
		register("shooting_star", SHOOTING_STAR, DyeColor.PURPLE);
		register("stardust", STARDUST, DyeColor.PURPLE);
		
		register("hibernating_jade_vine_seeds", HIBERNATING_JADE_VINE_SEEDS, DyeColor.GRAY);
		register("germinated_jade_vine_seeds", GERMINATED_JADE_VINE_SEEDS, DyeColor.LIME);
		register("jade_vine_petals", JADE_VINE_PETALS, DyeColor.LIME);
		register("moonstruck_nectar", MOONSTRUCK_NECTAR, DyeColor.LIME);
		
		register("vegetal", VEGETAL, DyeColor.LIME);
		register("neolith", NEOLITH, DyeColor.PINK);
		register("bedrock_dust", BEDROCK_DUST, DyeColor.BLACK);
		register("midnight_aberration", MIDNIGHT_ABERRATION, DyeColor.GRAY);
		register("midnight_chip", MIDNIGHT_CHIP, DyeColor.GRAY);
		register("bismuth_crystal", BISMUTH_CRYSTAL, DyeColor.CYAN);
		
		register("raw_malachite", RAW_MALACHITE, DyeColor.GREEN);
		register("malachite_crystal", MALACHITE_CRYSTAL, DyeColor.GREEN);
		
		register("fiery_powder", FIERY_POWDER, DyeColor.ORANGE);
		register("blizzard_powder", BLIZZARD_POWDER, DyeColor.LIGHT_BLUE);
	}
	
	public static void registerDecayBottles() {
		register("bottle_of_fading", BOTTLE_OF_FADING, DyeColor.GRAY);
		register("bottle_of_failing", BOTTLE_OF_FAILING, DyeColor.GRAY);
		register("bottle_of_ruin", BOTTLE_OF_RUIN, DyeColor.GRAY);
		register("bottle_of_terror", BOTTLE_OF_TERROR, DyeColor.GRAY);
		register("bottle_of_decay_away", BOTTLE_OF_DECAY_AWAY, DyeColor.PINK);
	}
	
	public static void registerPreEnchantedTools() {
		register("multitool", MULTITOOL, DyeColor.BROWN);
		register("silker_pickaxe", SILKER_PICKAXE, DyeColor.BLUE);
		register("fortune_pickaxe", FORTUNE_PICKAXE, DyeColor.LIGHT_BLUE);
		register("looting_falchion", LOOTING_FALCHION, DyeColor.RED);
		register("voiding_pickaxe", VOIDING_PICKAXE, DyeColor.GRAY);
		register("resonant_pickaxe", RESONANT_PICKAXE, DyeColor.WHITE);
		
		register("emergency_helmet", EMERGENCY_HELMET, DyeColor.BLUE);
		register("emergency_chestplate", EMERGENCY_CHESTPLATE, DyeColor.BLUE);
		register("emergency_leggings", EMERGENCY_LEGGINGS, DyeColor.BLUE);
		register("emergency_boots", EMERGENCY_BOOTS, DyeColor.BLUE);
		
		register("bedrock_pickaxe", BEDROCK_PICKAXE, DyeColor.BLACK);
		register("bedrock_axe", BEDROCK_AXE, DyeColor.BLACK);
		register("bedrock_shovel", BEDROCK_SHOVEL, DyeColor.BLACK);
		register("bedrock_sword", BEDROCK_SWORD, DyeColor.BLACK);
		register("bedrock_hoe", BEDROCK_HOE, DyeColor.BLACK);
		register("bedrock_bow", BEDROCK_BOW, DyeColor.BLACK);
		register("bedrock_crossbow", BEDROCK_CROSSBOW, DyeColor.BLACK);
		register("bedrock_shears", BEDROCK_SHEARS, DyeColor.BLACK);
		register("bedrock_fishing_rod", BEDROCK_FISHING_ROD, DyeColor.BLACK);
		
		register("bedrock_helmet", BEDROCK_HELMET, DyeColor.BLACK);
		register("bedrock_chestplate", BEDROCK_CHESTPLATE, DyeColor.BLACK);
		register("bedrock_leggings", BEDROCK_LEGGINGS, DyeColor.BLACK);
		register("bedrock_boots", BEDROCK_BOOTS, DyeColor.BLACK);
		
		register("dreamflayer", DREAMFLAYER, DyeColor.RED);
	}
	
	public static void registerMagicalTools() {
		register("ender_bag", ENDER_BAG, DyeColor.PURPLE);
		register("light_staff", RADIANCE_STAFF, DyeColor.YELLOW);
		register("natures_staff", NATURES_STAFF, DyeColor.LIME);
		register("placement_staff", PLACEMENT_STAFF, DyeColor.LIGHT_GRAY);
		register("exchange_staff", EXCHANGE_STAFF, DyeColor.LIGHT_GRAY);
		register("block_flooder", BLOCK_FLOODER, DyeColor.LIGHT_GRAY);
		register("ender_splice", ENDER_SPLICE, DyeColor.PURPLE);
		register("end_portal_cracker", END_PORTAL_CRACKER, DyeColor.RED);
		register("crescent_clock", CRESCENT_CLOCK, DyeColor.MAGENTA);
		register("restoration_tea", RESTORATION_TEA, DyeColor.PINK);
		register("jade_jelly", JADE_JELLY, DyeColor.LIME);
	}
	
	public static void registerTrinkets() {
		register("fanciful_stone_ring", FANCIFUL_STONE_RING, DyeColor.GREEN);
		register("fanciful_belt", FANCIFUL_BELT, DyeColor.GREEN);
		register("fanciful_pendant", FANCIFUL_PENDANT, DyeColor.GREEN);
		register("fanciful_circlet", FANCIFUL_CIRCLET, DyeColor.GREEN);
		register("fanciful_gloves", FANCIFUL_GLOVES, DyeColor.GREEN);
		
		register("glow_vision_helmet", GLOW_VISION_GOGGLES, DyeColor.WHITE);
		register("jeopardant", JEOPARDANT, DyeColor.RED);
		register("seven_league_boots", SEVEN_LEAGUE_BOOTS, DyeColor.PURPLE);
		register("radiance_pin", RADIANCE_PIN, DyeColor.BLUE);
		register("totem_pendant", TOTEM_PENDANT, DyeColor.BLUE);
		register("take_off_belt", TAKE_OFF_BELT, DyeColor.YELLOW);
		register("azure_dike_belt", AZURE_DIKE_BELT, DyeColor.BLUE);
		register("azure_dike_ring", AZURE_DIKE_RING, DyeColor.BLUE);
		register("shieldgrasp_amulet", SHIELDGRASP_AMULET, DyeColor.BLUE);
		register("heartsingers_reward", HEARTSINGERS_REWARD_RING, DyeColor.PINK);
		register("gloves_of_dawns_grasp", GLOVES_OF_DAWNS_GRASP, DyeColor.YELLOW);
		register("gleaming_pin", GLEAMING_PIN, DyeColor.YELLOW);
		register("lesser_potion_pendant", LESSER_POTION_PENDANT, DyeColor.PINK);
		register("greater_potion_pendant", GREATER_POTION_PENDANT, DyeColor.PINK);
		register("ashen_circlet", ASHEN_CIRCLET, DyeColor.ORANGE);
		register("tidal_circlet", TIDAL_CIRCLET, DyeColor.LIGHT_BLUE);
		register("puff_circlet", PUFF_CIRCLET, DyeColor.WHITE);
		register("whispy_circlet", WHISPY_CIRCLET, DyeColor.BROWN);
		register("neat_ring", NEAT_RING, DyeColor.GREEN);
		
		register("ink_flask", INK_FLASK, DyeColor.WHITE);
		register("ink_assortment", INK_ASSORTMENT, DyeColor.WHITE);
		register("pigment_palette", PIGMENT_PALETTE, DyeColor.WHITE);
		register("artists_palette", ARTISTS_PALETTE, DyeColor.WHITE);
		register("creative_ink_assortment", CREATIVE_INK_ASSORTMENT, DyeColor.WHITE);
	}
	
	public static void registerFluidBuckets() {
		register("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET, DyeColor.LIGHT_GRAY);
		register("mud_bucket", MUD_BUCKET, DyeColor.BROWN);
		register("midnight_solution_bucket", MIDNIGHT_SOLUTION_BUCKET, DyeColor.GRAY);
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
