package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.gravity.GravityItem;
import de.dafuqs.pigment.entity.PigmentEntityTypes;
import de.dafuqs.pigment.items.armor.BedrockArmorMaterial;
import de.dafuqs.pigment.items.armor.LowHealthMaterial;
import de.dafuqs.pigment.items.misc.EndPortalCrackerItem;
import de.dafuqs.pigment.items.misc.InvisibleItemFrameItem;
import de.dafuqs.pigment.items.misc.PigmentMusicDiscItem;
import de.dafuqs.pigment.items.misc.Spawner;
import de.dafuqs.pigment.items.tools.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static de.dafuqs.pigment.registries.PigmentFluids.STILL_LIQUID_CRYSTAL;
import static de.dafuqs.pigment.registries.PigmentFluids.STILL_MUD;

public class PigmentItems {

    public static FabricItemSettings generalItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).maxCount(64);
    public static FabricItemSettings fluidBucketItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).maxCount(1);
    public static FabricItemSettings pigmentWorldgenItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_WORLDGEN).maxCount(64);
    public static FabricItemSettings pigmentUncommonItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.UNCOMMON).maxCount(64);
    public static FabricItemSettings pigmentRareItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.RARE).maxCount(64);
    public static FabricItemSettings decayPlacerItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).maxCount(16);
    public static FabricItemSettings musicDiscItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_GENERAL).maxCount(1).rarity(Rarity.RARE);

    public static FabricItemSettings preEnchantedItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS);
    public static FabricItemSettings pigmentBedrockItemSettings = new FabricItemSettings().group(PigmentItemGroups.ITEM_GROUP_PREENCHANTED_TOOLS).rarity(Rarity.RARE).fireproof();

    // Pigment
    public static Item MAGENTA_PIGMENT = new Item(generalItemSettings);
    public static Item YELLOW_PIGMENT = new Item(generalItemSettings);
    public static Item CYAN_PIGMENT = new Item(generalItemSettings);
    public static Item BLACK_PIGMENT = new Item(generalItemSettings);
    public static Item WHITE_PIGMENT = new Item(generalItemSettings);

    // Bedrock Tools
    public static ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BedrockToolMaterial.INSTANCE, 1, -2.8F, pigmentBedrockItemSettings);
    public static ToolItem BEDROCK_AXE = new BedrockAxeItem(BedrockToolMaterial.INSTANCE, 6, -3.0F, pigmentBedrockItemSettings);
    public static ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BedrockToolMaterial.INSTANCE, 2, -3.0F, pigmentBedrockItemSettings);
    public static ToolItem BEDROCK_SWORD = new BedrockSwordItem(BedrockToolMaterial.INSTANCE, 5, -2.4F, pigmentBedrockItemSettings);
    public static ToolItem BEDROCK_HOE = new BedrockHoeItem(BedrockToolMaterial.INSTANCE, -2, -0.0F, pigmentBedrockItemSettings);

    // Bedrock Items
    public static BedrockBowItem BEDROCK_BOW = new BedrockBowItem(pigmentBedrockItemSettings);
    public static BedrockCrossbowItem BEDROCK_CROSSBOW = new BedrockCrossbowItem(pigmentBedrockItemSettings);
    public static BedrockShearsItem BEDROCK_SHEARS = new BedrockShearsItem(pigmentBedrockItemSettings); // TODO: wait for fabric pull request to get shears drop: https://github.com/FabricMC/fabric/pull/1287
    public static FishingRodItem BEDROCK_FISHING_ROD = new BedrockFishingRodItem(pigmentBedrockItemSettings);

    // Bedrock Armor
    public static final BedrockArmorMaterial BEDROCK_ARMOR_MATERIAL = new BedrockArmorMaterial();
    public static final Item BEDROCK_HELMET = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, pigmentBedrockItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, pigmentBedrockItemSettings);
    public static final Item BEDROCK_LEGGINGS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, pigmentBedrockItemSettings);
    public static final Item BEDROCK_BOOTS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, pigmentBedrockItemSettings);

    // Gem shards
    public static final Item CITRINE_SHARD_ITEM = new Item(pigmentWorldgenItemSettings);
    public static final Item TOPAZ_SHARD_ITEM = new Item(pigmentWorldgenItemSettings);
    public static final Item ONYX_SHARD_ITEM = new Item(pigmentWorldgenItemSettings);
    public static final Item MOONSTONE_SHARD_ITEM = new Item(pigmentWorldgenItemSettings);
    public static final Item RAINBOW_MOONSTONE_ITEM = new Item(pigmentWorldgenItemSettings);

    // DECAY DROPS
    public static final Item VEGETAL = new Item(generalItemSettings);
    public static final Item CORRUPTED_OBSIDIAN_DUST = new Item(pigmentUncommonItemSettings);
    public static final Item CORRUPTED_BEDROCK_DUST = new Item(pigmentRareItemSettings);

    // FLUIDS
    public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(STILL_LIQUID_CRYSTAL, fluidBucketItemSettings);
    public static final Item MUD_BUCKET = new BucketItem(STILL_MUD, fluidBucketItemSettings);

    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, pigmentUncommonItemSettings);
    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(PigmentBlocks.GLISTERING_MELON_STEM, pigmentUncommonItemSettings);

    // DECAY
    public static final Item DECAY_1_PLACER = new AliasedBlockItem(PigmentBlocks.DECAY1, decayPlacerItemSettings);
    public static final Item DECAY_2_PLACER = new AliasedBlockItem(PigmentBlocks.DECAY2, decayPlacerItemSettings);
    public static final Item DECAY_3_PLACER = new AliasedBlockItem(PigmentBlocks.DECAY3, decayPlacerItemSettings);

    // ORE
    public static final Item SPARKLESTONE_GEM = new Item(pigmentWorldgenItemSettings);
    public static final Item RAW_KOENIGSBLAU = new Item(pigmentWorldgenItemSettings);
    public static final Item SHAPED_KOENIGSBLAU = new Item(pigmentWorldgenItemSettings);

    public static final Item SHATTERED_PALETUR_FRAGMENTS = new GravityItem(generalItemSettings, 0.003F);
    public static final Item PALETUR_GEM = new GravityItem(generalItemSettings, 0.02F);
    public static final Item SHATTERED_SCARLET_FRAGMENTS = new GravityItem(generalItemSettings, -0.003F);
    public static final Item SCARLET_GEM = new GravityItem(generalItemSettings, -0.02F);

    // SPECIAL TOOLS
    public static final LowHealthMaterial LOW_HEALTH_MATERIAL = new LowHealthMaterial();
    public static final Item MULTITOOL = new MultiToolItem(ToolMaterials.IRON, 2, -2.4F, preEnchantedItemSettings);
    public static final Item SILKER_PICKAXE = new PigmentPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, preEnchantedItemSettings);
    public static final Item FORTUNE_PICKAXE = new PigmentPickaxeItem(LOW_HEALTH_MATERIAL, 1, -2.8F, preEnchantedItemSettings);
    public static final Item LOOTING_FALCHION = new SwordItem(LOW_HEALTH_MATERIAL, 4, -2.2F, preEnchantedItemSettings);

    public static final Item END_PORTAL_CRACKER = new EndPortalCrackerItem(generalItemSettings);
    public static final Item QUITOXIC_POWDER = new Item(generalItemSettings);
    public static final Item MERMAIDS_GEM = new Item(generalItemSettings);
    public static final Item SHOOTING_STAR = new Item(generalItemSettings);

    public static final Item MUSIC_DISC_PIGMENT_THEME = new PigmentMusicDiscItem(1,PigmentSoundEvents.PIGMENT_THEME, (musicDiscItemSettings));

    public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(PigmentEntityTypes.INVISIBLE_ITEM_FRAME, (generalItemSettings));

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(PigmentCommon.MOD_ID, name), item);
    }

    public static void register() {
        registerItem("magenta_pigment", MAGENTA_PIGMENT);
        registerItem("yellow_pigment", YELLOW_PIGMENT);
        registerItem("cyan_pigment", CYAN_PIGMENT);
        registerItem("black_pigment", BLACK_PIGMENT);
        registerItem("white_pigment", WHITE_PIGMENT);

        registerItem("bedrock_pickaxe", BEDROCK_PICKAXE);
        registerItem("bedrock_axe", BEDROCK_AXE);
        registerItem("bedrock_shovel", BEDROCK_SHOVEL);
        registerItem("bedrock_sword", BEDROCK_SWORD);
        registerItem("bedrock_hoe", BEDROCK_HOE);

        registerItem("bedrock_bow", BEDROCK_BOW);
        registerItem("bedrock_crossbow", BEDROCK_CROSSBOW);
        registerItem("bedrock_shears", BEDROCK_SHEARS);
        registerItem("bedrock_fishing_rod", BEDROCK_FISHING_ROD);

        registerItem("bedrock_helmet", BEDROCK_HELMET);
        registerItem("bedrock_chestplate", BEDROCK_CHESTPLATE);
        registerItem("bedrock_leggings", BEDROCK_LEGGINGS);
        registerItem("bedrock_boots", BEDROCK_BOOTS);

        registerItem("spawner", SPAWNER);
        registerItem("glistering_melon_seeds", GLISTERING_MELON_SEEDS);

        registerItem("citrine_shard", CITRINE_SHARD_ITEM);
        registerItem("topaz_shard", TOPAZ_SHARD_ITEM);
        registerItem("onyx_shard", ONYX_SHARD_ITEM);
        registerItem("moonstone_shard", MOONSTONE_SHARD_ITEM);
        registerItem("rainbow_moonstone_shard", RAINBOW_MOONSTONE_ITEM);

        registerItem("vegetal", VEGETAL);
        registerItem("corrupted_obsidian_dust", CORRUPTED_OBSIDIAN_DUST);
        registerItem("corrupted_bedrock_dust", CORRUPTED_BEDROCK_DUST);

        registerItem("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET);
        registerItem("mud_bucket", MUD_BUCKET);

        registerItem("decay_1_placer", DECAY_1_PLACER);
        registerItem("decay_2_placer", DECAY_2_PLACER);
        registerItem("decay_3_placer", DECAY_3_PLACER);

        registerItem("sparklestone_gem", SPARKLESTONE_GEM);
        registerItem("raw_koenigsblau", RAW_KOENIGSBLAU);
        registerItem("shaped_koenigsblau", SHAPED_KOENIGSBLAU);

        registerItem("shattered_paletur_fragments", SHATTERED_PALETUR_FRAGMENTS);
        registerItem("paletur_gem", PALETUR_GEM);
        registerItem("shattered_scarlet_fragments", SHATTERED_SCARLET_FRAGMENTS);
        registerItem("scarlet_gem", SCARLET_GEM);

        registerItem("quitoxic_powder", QUITOXIC_POWDER);
        registerItem("mermaids_gem", MERMAIDS_GEM);
        registerItem("shooting_star", SHOOTING_STAR);

        registerItem("end_portal_cracker", END_PORTAL_CRACKER);
        registerItem("multitool", MULTITOOL);
        registerItem("silker_pickaxe", SILKER_PICKAXE);
        registerItem("fortune_pickaxe", FORTUNE_PICKAXE);
        registerItem("looting_falchion", LOOTING_FALCHION);

        registerItem("music_disc_pigment_theme", MUSIC_DISC_PIGMENT_THEME);
        registerItem("invisible_item_frame", INVISIBLE_ITEM_FRAME);
    }


}
