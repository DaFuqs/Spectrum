package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.client.models.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.render.armor.BedrockArmorModel;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumModelLayers {
	
	/**
	 * Entities
	 */
	public static final EntityModelLayer WOOLY_PIG = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "main");
	public static final EntityModelLayer WOOLY_PIG_HAT = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "hat");
	public static final EntityModelLayer WOOLY_PIG_WOOL = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "wool");
	
	public static final EntityModelLayer PRESERVATION_TURRET = new EntityModelLayer(SpectrumCommon.locate("preservation_turret"), "main");
	public static final EntityModelLayer MONSTROSITY = new EntityModelLayer(SpectrumCommon.locate("monstrosity"), "main");
	public static final EntityModelLayer LIZARD_SCALES = new EntityModelLayer(SpectrumCommon.locate("lizard"), "main");
	public static final EntityModelLayer LIZARD_FRILLS = new EntityModelLayer(SpectrumCommon.locate("lizard"), "frills");
	public static final EntityModelLayer LIZARD_HORNS = new EntityModelLayer(SpectrumCommon.locate("lizard"), "horns");
	public static final EntityModelLayer KINDLING = new EntityModelLayer(SpectrumCommon.locate("kindling"), "main");
	public static final EntityModelLayer KINDLING_SADDLE = new EntityModelLayer(SpectrumCommon.locate("kindling_saddle"), "main");
	public static final EntityModelLayer KINDLING_ARMOR = new EntityModelLayer(SpectrumCommon.locate("kindling_armor"), "main");
	public static final EntityModelLayer KINDLING_COUGH = new EntityModelLayer(SpectrumCommon.locate("kindling_cough"), "main");
	public static final EntityModelLayer ERASER = new EntityModelLayer(SpectrumCommon.locate("eraser"), "body");

	
	/**
	 * Mob Heads
	 */
	public static final EntityModelLayer ALLAY_HEAD = new EntityModelLayer(SpectrumCommon.locate("allay_head"), "main");
	public static final EntityModelLayer AXOLOTL_BLUE_HEAD = new EntityModelLayer(SpectrumCommon.locate("axolotl_blue_head"), "main");
	public static final EntityModelLayer AXOLOTL_CYAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("axolotl_cyan_head"), "main");
	public static final EntityModelLayer AXOLOTL_GOLD_HEAD = new EntityModelLayer(SpectrumCommon.locate("axolotl_gold_head"), "main");
	public static final EntityModelLayer AXOLOTL_LEUCISTIC_HEAD = new EntityModelLayer(SpectrumCommon.locate("axolotl_lucy_head"), "main");
	public static final EntityModelLayer AXOLOTL_WILD_HEAD = new EntityModelLayer(SpectrumCommon.locate("axolotl_wild_head"), "main");
	public static final EntityModelLayer BAT_HEAD = new EntityModelLayer(SpectrumCommon.locate("bat_head"), "main");
	public static final EntityModelLayer BEE_HEAD = new EntityModelLayer(SpectrumCommon.locate("bee_head"), "main");
	public static final EntityModelLayer BLAZE_HEAD = new EntityModelLayer(SpectrumCommon.locate("blaze_head"), "main");
	public static final EntityModelLayer CAMEL_HEAD = new EntityModelLayer(SpectrumCommon.locate("camel_head"), "main");
	public static final EntityModelLayer CAT_HEAD = new EntityModelLayer(SpectrumCommon.locate("cat_head"), "main");
	public static final EntityModelLayer CAVE_SPIDER_HEAD = new EntityModelLayer(SpectrumCommon.locate("cave_spider_head"), "main");
	public static final EntityModelLayer CHICKEN_HEAD = new EntityModelLayer(SpectrumCommon.locate("chicken_head"), "main");
	public static final EntityModelLayer COW_HEAD = new EntityModelLayer(SpectrumCommon.locate("cow_head"), "main");
	public static final EntityModelLayer DOLPHIN_HEAD = new EntityModelLayer(SpectrumCommon.locate("dolphin_head"), "main");
	public static final EntityModelLayer DONKEY_HEAD = new EntityModelLayer(SpectrumCommon.locate("donkey_head"), "main");
	public static final EntityModelLayer DROWNED_HEAD = new EntityModelLayer(SpectrumCommon.locate("drowned_head"), "main");
	public static final EntityModelLayer ELDER_GUARDIAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("elder_guardian_head"), "main");
	public static final EntityModelLayer ENDERMAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("enderman_head"), "main");
	public static final EntityModelLayer ENDERMITE_HEAD = new EntityModelLayer(SpectrumCommon.locate("endermite_head"), "main");
	public static final EntityModelLayer EVOKER_HEAD = new EntityModelLayer(SpectrumCommon.locate("evoker_head"), "main");
	public static final EntityModelLayer FOX_ARCTIC_HEAD = new EntityModelLayer(SpectrumCommon.locate("fox_arctic_head"), "main");
	public static final EntityModelLayer FOX_HEAD = new EntityModelLayer(SpectrumCommon.locate("fox_head"), "main");
	public static final EntityModelLayer FROG_COLD_HEAD = new EntityModelLayer(SpectrumCommon.locate("frog_cold_head"), "main");
	public static final EntityModelLayer FROG_TEMPERATE_HEAD = new EntityModelLayer(SpectrumCommon.locate("frog_temperate_head"), "main");
	public static final EntityModelLayer FROG_WARM_HEAD = new EntityModelLayer(SpectrumCommon.locate("frog_warm_head"), "main");
	public static final EntityModelLayer GHAST_HEAD = new EntityModelLayer(SpectrumCommon.locate("ghast_head"), "main");
	public static final EntityModelLayer GLOW_SQUID_HEAD = new EntityModelLayer(SpectrumCommon.locate("glow_squid_head"), "main");
	public static final EntityModelLayer GOAT_HEAD = new EntityModelLayer(SpectrumCommon.locate("goat_head"), "main");
	public static final EntityModelLayer GUARDIAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("guardian_head"), "main");
	public static final EntityModelLayer HOGLIN_HEAD = new EntityModelLayer(SpectrumCommon.locate("hoglin_head"), "main");
	public static final EntityModelLayer HORSE_HEAD = new EntityModelLayer(SpectrumCommon.locate("horse_head"), "main");
	public static final EntityModelLayer HUSK_HEAD = new EntityModelLayer(SpectrumCommon.locate("husk_head"), "main");
	public static final EntityModelLayer ILLUSIONER_HEAD = new EntityModelLayer(SpectrumCommon.locate("illusioner_head"), "main");
	public static final EntityModelLayer IRON_GOLEM_HEAD = new EntityModelLayer(SpectrumCommon.locate("iron_golem_head"), "main");
	public static final EntityModelLayer LLAMA_HEAD = new EntityModelLayer(SpectrumCommon.locate("llama_head"), "main");
	public static final EntityModelLayer MAGMA_CUBE_HEAD = new EntityModelLayer(SpectrumCommon.locate("magma_cube_head"), "main");
	public static final EntityModelLayer MOOSHROOM_BROWN_HEAD = new EntityModelLayer(SpectrumCommon.locate("mooshroom_brown_head"), "main");
	public static final EntityModelLayer MOOSHROOM_RED_HEAD = new EntityModelLayer(SpectrumCommon.locate("mooshroom_red_head"), "main");
	public static final EntityModelLayer MULE_HEAD = new EntityModelLayer(SpectrumCommon.locate("mule_head"), "main");
	public static final EntityModelLayer OCELOT_HEAD = new EntityModelLayer(SpectrumCommon.locate("ocelot_head"), "main");
	public static final EntityModelLayer PANDA_HEAD = new EntityModelLayer(SpectrumCommon.locate("panda_head"), "main");
	public static final EntityModelLayer PARROT_BLUE_HEAD = new EntityModelLayer(SpectrumCommon.locate("parrot_blue_head"), "main");
	public static final EntityModelLayer PARROT_CYAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("parrot_cyan_head"), "main");
	public static final EntityModelLayer PARROT_GRAY_HEAD = new EntityModelLayer(SpectrumCommon.locate("parrot_gray_head"), "main");
	public static final EntityModelLayer PARROT_GREEN_HEAD = new EntityModelLayer(SpectrumCommon.locate("parrot_green_head"), "main");
	public static final EntityModelLayer PARROT_RED_HEAD = new EntityModelLayer(SpectrumCommon.locate("parrot_red_head"), "main");
	public static final EntityModelLayer PHANTOM_HEAD = new EntityModelLayer(SpectrumCommon.locate("phantom_head"), "main");
	public static final EntityModelLayer PIG_HEAD = new EntityModelLayer(SpectrumCommon.locate("pig_head"), "main");
	public static final EntityModelLayer PILLAGER_HEAD = new EntityModelLayer(SpectrumCommon.locate("pillager_head"), "main");
	public static final EntityModelLayer POLAR_BEAR_HEAD = new EntityModelLayer(SpectrumCommon.locate("polar_bear_head"), "main");
	public static final EntityModelLayer PUFFERFISH_HEAD = new EntityModelLayer(SpectrumCommon.locate("pufferfish_head"), "main");
	public static final EntityModelLayer RABBIT_HEAD = new EntityModelLayer(SpectrumCommon.locate("rabbit_head"), "main");
	public static final EntityModelLayer RAVAGER_HEAD = new EntityModelLayer(SpectrumCommon.locate("ravager_head"), "main");
	public static final EntityModelLayer SALMON_HEAD = new EntityModelLayer(SpectrumCommon.locate("salmon_head"), "main");
	public static final EntityModelLayer SHEEP_HEAD = new EntityModelLayer(SpectrumCommon.locate("sheep_head"), "main");
	public static final EntityModelLayer SHULKER_BLACK_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_black_head"), "main");
	public static final EntityModelLayer SHULKER_BLUE_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_blue_head"), "main");
	public static final EntityModelLayer SHULKER_BROWN_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_brown_head"), "main");
	public static final EntityModelLayer SHULKER_CYAN_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_cyan_head"), "main");
	public static final EntityModelLayer SHULKER_GRAY_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_gray_head"), "main");
	public static final EntityModelLayer SHULKER_GREEN_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_green_head"), "main");
	public static final EntityModelLayer SHULKER_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_head"), "main");
	public static final EntityModelLayer SHULKER_LIGHT_BLUE_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_light_blue_head"), "main");
	public static final EntityModelLayer SHULKER_LIGHT_GRAY_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_light_gray_head"), "main");
	public static final EntityModelLayer SHULKER_LIME_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_lime_head"), "main");
	public static final EntityModelLayer SHULKER_MAGENTA_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_magenta_head"), "main");
	public static final EntityModelLayer SHULKER_ORANGE_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_orange_head"), "main");
	public static final EntityModelLayer SHULKER_PINK_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_pink_head"), "main");
	public static final EntityModelLayer SHULKER_PURPLE_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_purple_head"), "main");
	public static final EntityModelLayer SHULKER_RED_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_red_head"), "main");
	public static final EntityModelLayer SHULKER_WHITE_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_white_head"), "main");
	public static final EntityModelLayer SHULKER_YELLOW_HEAD = new EntityModelLayer(SpectrumCommon.locate("shulker_yellow_head"), "main");
	public static final EntityModelLayer SILVERFISH_HEAD = new EntityModelLayer(SpectrumCommon.locate("silverfish_head"), "main");
	public static final EntityModelLayer SKELETON_HORSE_HEAD = new EntityModelLayer(SpectrumCommon.locate("skeleton_horse_head"), "main");
	public static final EntityModelLayer SLIME_HEAD = new EntityModelLayer(SpectrumCommon.locate("slime_head"), "main");
	public static final EntityModelLayer SNIFFER_HEAD = new EntityModelLayer(SpectrumCommon.locate("sniffer_head"), "main");
	public static final EntityModelLayer SNOW_GOLEM_HEAD = new EntityModelLayer(SpectrumCommon.locate("snow_golem_head"), "main");
	public static final EntityModelLayer SPIDER_HEAD = new EntityModelLayer(SpectrumCommon.locate("spider_head"), "main");
	public static final EntityModelLayer SQUID_HEAD = new EntityModelLayer(SpectrumCommon.locate("squid_head"), "main");
	public static final EntityModelLayer STRAY_HEAD = new EntityModelLayer(SpectrumCommon.locate("stray_head"), "main");
	public static final EntityModelLayer STRIDER_HEAD = new EntityModelLayer(SpectrumCommon.locate("strider_head"), "main");
	public static final EntityModelLayer TADPOLE_HEAD = new EntityModelLayer(SpectrumCommon.locate("tadpole_head"), "main");
	public static final EntityModelLayer TROPICAL_FISH_HEAD = new EntityModelLayer(SpectrumCommon.locate("tropical_fish_head"), "main");
	public static final EntityModelLayer TROPICAL_FISH_HEAD_PATTERN = new EntityModelLayer(SpectrumCommon.locate("tropical_fish_head"), "pattern");
	public static final EntityModelLayer TURTLE_HEAD = new EntityModelLayer(SpectrumCommon.locate("turtle_head"), "main");
	public static final EntityModelLayer VEX_HEAD = new EntityModelLayer(SpectrumCommon.locate("vex_head"), "main");
	public static final EntityModelLayer VILLAGER_HEAD = new EntityModelLayer(SpectrumCommon.locate("villager_head"), "main");
	public static final EntityModelLayer VINDICATOR_HEAD = new EntityModelLayer(SpectrumCommon.locate("vindicator_head"), "main");
	public static final EntityModelLayer WANDERING_TRADER_HEAD = new EntityModelLayer(SpectrumCommon.locate("wandering_trader_head"), "main");
	public static final EntityModelLayer WARDEN_HEAD = new EntityModelLayer(SpectrumCommon.locate("warden_head"), "main");
	public static final EntityModelLayer WITCH_HEAD = new EntityModelLayer(SpectrumCommon.locate("witch_head"), "main");
	public static final EntityModelLayer WITHER_HEAD = new EntityModelLayer(SpectrumCommon.locate("wither_head"), "main");
	public static final EntityModelLayer WOLF_HEAD = new EntityModelLayer(SpectrumCommon.locate("wolf_head"), "main");
	public static final EntityModelLayer ZOGLIN_HEAD = new EntityModelLayer(SpectrumCommon.locate("zoglin_head"), "main");
	public static final EntityModelLayer ZOMBIE_HORSE_HEAD = new EntityModelLayer(SpectrumCommon.locate("zombie_horse_head"), "main");
	public static final EntityModelLayer ZOMBIE_VILLAGER_HEAD = new EntityModelLayer(SpectrumCommon.locate("zombie_villager_head"), "main");
	public static final EntityModelLayer ZOMBIFIED_PIGLIN_HEAD = new EntityModelLayer(SpectrumCommon.locate("zombified_piglin_head"), "main");
	
	public static final EntityModelLayer EGG_LAYING_WOOLY_PIG_HEAD = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig_head"), "main");
	public static final EntityModelLayer ERASER_HEAD = new EntityModelLayer(SpectrumCommon.locate("eraser_head"), "body");
	public static final EntityModelLayer KINDLING_HEAD = new EntityModelLayer(SpectrumCommon.locate("kindling_head"), "main");
	public static final EntityModelLayer LIZARD_HEAD = new EntityModelLayer(SpectrumCommon.locate("lizard_head"), "main");
	public static final EntityModelLayer LIZARD_HEAD_FRILLS = new EntityModelLayer(SpectrumCommon.locate("lizard_head"), "frills");
	public static final EntityModelLayer MONSTROSITY_HEAD = new EntityModelLayer(SpectrumCommon.locate("monstrosity_head"), "main");
	public static final EntityModelLayer PRESERVATION_TURRET_HEAD = new EntityModelLayer(SpectrumCommon.locate("preservation_turret_head"), "main");

	/**
	 * Armor
	 */
	public static final EntityModelLayer MAIN_BEDROCK_LAYER = new EntityModelLayer(SpectrumCommon.locate("bedrock_armor"), "main");
	public static final Identifier BEDROCK_ARMOR_MAIN_ID = SpectrumCommon.locate("textures/armor/bedrock_armor_main.png");
	
	
	public static void register() {
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG, EggLayingWoolyPigEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG_HAT, EggLayingWoolyPigHatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG_WOOL, EggLayingWoolyPigWoolEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PRESERVATION_TURRET, PreservationTurretEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MONSTROSITY, MonstrosityEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LIZARD_SCALES, LizardEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LIZARD_FRILLS, LizardEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LIZARD_HORNS, LizardEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KINDLING, KindlingEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KINDLING_SADDLE, KindlingEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KINDLING_ARMOR, KindlingEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KINDLING_COUGH, KindlingCoughEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ERASER, EraserEntityModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(ALLAY_HEAD, AllayHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AXOLOTL_BLUE_HEAD, AxolotlHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AXOLOTL_CYAN_HEAD, AxolotlHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AXOLOTL_GOLD_HEAD, AxolotlHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AXOLOTL_WILD_HEAD, AxolotlHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AXOLOTL_LEUCISTIC_HEAD, AxolotlHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BAT_HEAD, BatHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BEE_HEAD, BeeHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BLAZE_HEAD, BlazeHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CAT_HEAD, CatHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CAVE_SPIDER_HEAD, SpiderHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CHICKEN_HEAD, ChickenHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TROPICAL_FISH_HEAD, TropicalFishHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TROPICAL_FISH_HEAD_PATTERN, TropicalFishHeadModel::getTexturedModelDataPattern);
		EntityModelLayerRegistry.registerModelLayer(COW_HEAD, CowHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DONKEY_HEAD, HorseHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DROWNED_HEAD, DrownedHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ELDER_GUARDIAN_HEAD, GuardianHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ENDERMAN_HEAD, EndermanHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ENDERMITE_HEAD, EndermiteHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(EVOKER_HEAD, IllagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FOX_HEAD, FoxHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FOX_ARCTIC_HEAD, FoxHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FROG_COLD_HEAD, FrogHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FROG_TEMPERATE_HEAD, FrogHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FROG_WARM_HEAD, FrogHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(GHAST_HEAD, GhastHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(GLOW_SQUID_HEAD, SquidHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(GOAT_HEAD, GoatHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(GUARDIAN_HEAD, GuardianHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HOGLIN_HEAD, HoglinHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HORSE_HEAD, HorseHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HUSK_HEAD, ZombieHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ILLUSIONER_HEAD, IllagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(IRON_GOLEM_HEAD, IronGolemHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LLAMA_HEAD, LlamaHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MAGMA_CUBE_HEAD, SlimeHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MOOSHROOM_BROWN_HEAD, CowHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MOOSHROOM_RED_HEAD, CowHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MULE_HEAD, HorseHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(OCELOT_HEAD, CatHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PANDA_HEAD, PandaHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PARROT_BLUE_HEAD, ParrotHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PARROT_CYAN_HEAD, ParrotHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PARROT_GRAY_HEAD, ParrotHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PARROT_GREEN_HEAD, ParrotHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PARROT_RED_HEAD, ParrotHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PHANTOM_HEAD, PhantomHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PIG_HEAD, PigHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(POLAR_BEAR_HEAD, BearHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PUFFERFISH_HEAD, PufferFishHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(RABBIT_HEAD, RabbitHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(RAVAGER_HEAD, RavagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SALMON_HEAD, SalmonHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHEEP_HEAD, SheepHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_BLACK_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_BLUE_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_BROWN_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_CYAN_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_GRAY_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_GREEN_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_LIGHT_BLUE_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_LIGHT_GRAY_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_LIME_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_MAGENTA_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_ORANGE_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_PINK_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_PURPLE_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_RED_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_WHITE_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SHULKER_YELLOW_HEAD, ShulkerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SILVERFISH_HEAD, SilverfishHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SLIME_HEAD, SlimeHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SNOW_GOLEM_HEAD, ZombieHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SPIDER_HEAD, SpiderHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SQUID_HEAD, SquidHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(STRAY_HEAD, SkullEntityModel::getSkullTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(STRIDER_HEAD, StriderHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TADPOLE_HEAD, TadpoleHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TURTLE_HEAD, TurtleHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(VEX_HEAD, VexHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(VILLAGER_HEAD, VillagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(VINDICATOR_HEAD, IllagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WANDERING_TRADER_HEAD, VillagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WARDEN_HEAD, WardenHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WITCH_HEAD, WitchHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WITHER_HEAD, ZombieHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WOLF_HEAD, WolfHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZOGLIN_HEAD, HoglinHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZOMBIE_VILLAGER_HEAD, VillagerHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZOMBIFIED_PIGLIN_HEAD, PiglinHeadModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(CAMEL_HEAD, CamelHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SNIFFER_HEAD, SnifferHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SKELETON_HORSE_HEAD, HorseHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZOMBIE_HORSE_HEAD, HorseHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DOLPHIN_HEAD, DolphinHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PILLAGER_HEAD, IllagerHeadModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(EGG_LAYING_WOOLY_PIG_HEAD, EggLayingWoolyPigHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MONSTROSITY_HEAD, MonstrosityHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KINDLING_HEAD, KindlingHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ERASER_HEAD, EraserHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LIZARD_HEAD, LizardHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LIZARD_HEAD_FRILLS, LizardHeadModel::getTexturedModelDataFrills);
		EntityModelLayerRegistry.registerModelLayer(PRESERVATION_TURRET_HEAD, PreservationTurretHeadModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(MAIN_BEDROCK_LAYER, () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
	}
	
}
