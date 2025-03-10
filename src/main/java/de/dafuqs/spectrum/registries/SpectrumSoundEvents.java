package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

public class SpectrumSoundEvents {
	
	// Music
	public static final SoundEvent MUSIC_DISCOVERY = register("music.discovery");
	public static final SoundEvent MUSIC_CREDITS = register("music.credits");
	public static final SoundEvent MUSIC_DIVINITY = register("music.divinity");
	
	// Music referenced in the biome.jsons
	public static final SoundEvent MUSIC_DEEPER_DOWN_BLACK_LANGAST = register("music.deeper_down.black_langast");
	public static final SoundEvent MUSIC_DEEPER_DOWN_CRYSTAL_GARDENS = register("music.deeper_down.crystal_gardens");
	public static final SoundEvent MUSIC_DEEPER_DOWN_DEEP_BARRENS = register("music.deeper_down.deep_barrens");
	public static final SoundEvent MUSIC_DEEPER_DOWN_DEEP_DRIPSTONE_CAVES = register("music.deeper_down.deep_dripstone_caves");
	public static final SoundEvent MUSIC_DEEPER_DOWN_DRAGONROT_SWAMP = register("music.deeper_down.dragonrot_swamp");
	public static final SoundEvent MUSIC_DEEPER_DOWN_HOWLING_SPIRES = register("music.deeper_down.howling_spires");
	public static final SoundEvent MUSIC_DEEPER_DOWN_NOXSHROOM_FOREST = register("music.deeper_down.noxshroom_forest");
	public static final SoundEvent MUSIC_DEEPER_DOWN_RAZOR_EDGE = register("music.deeper_down.razor_edge");
	
	// Sounds
	public static final SoundEvent PEDESTAL_CRAFTING = register("pedestal_crafting");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_GENERIC = register("pedestal_crafting_finished_generic");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_AMETHYST = register("pedestal_crafting_finished_amethyst");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_CITRINE = register("pedestal_crafting_finished_citrine");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_TOPAZ = register("pedestal_crafting_finished_topaz");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_ONYX = register("pedestal_crafting_finished_onyx");
	public static final SoundEvent PEDESTAL_CRAFTING_FINISHED_MOONSTONE = register("pedestal_crafting_finished_moonstone");
	public static final SoundEvent PEDESTAL_UPGRADE = register("pedestal_upgrade");
	
	public static final SoundEvent FUSION_SHRINE_CRAFTING = register("fusion_shrine_crafting");
	public static final SoundEvent FUSION_SHRINE_CRAFTING_FINISHED = register("fusion_shrine_crafting_finished");
	public static final SoundEvent CRAFTING_ABORTED = register("fusion_shrine_crafting_aborted");
	public static final SoundEvent BUTTON_CLICK = register("spectrum.ui.button_click");
	public static final SoundEvent BOTTOMLESS_BUNDLE_ZIP = register("bottomless_bundle_zip");
	public static final SoundEvent BIDENT_MIRROR_IMAGE_THROWN = register("bident_mirror_image_thrown");
	public static final SoundEvent BIDENT_HIT_GROUND = register("bident_hit_ground");
	public static final SoundEvent MOONSTONE_STRIKE = register("moonstone_strike");
	public static final SoundEvent ENTITY_BLOCK_FLOODER_THROW = register("entity.block_flooder.throw");
	public static final SoundEvent OMNI_ACCELERATOR_SHOOT = register("item.omni_accelerator.shoot");
	public static final SoundEvent REDSTONE_MECHANISM_TRIGGER = register("block.redstone_mechanism.trigger");
	public static final SoundEvent REDSTONE_MECHANISM_BREAK_BLOCK = register("block.redstone_mechanism.break_block");

	public static final SoundEvent GUIDEBOOK_PAGES = register("item.guidebook.pages");

	public static final SoundEvent ENCHANTER_WORKING = register("enchanter_working");
	public static final SoundEvent ENCHANTER_FINISH = register("enchanter_finish");
	public static final SoundEvent CRAFTING_DING = register("crafting_ding");
	
	public static final SoundEvent SPIRIT_INSTILLER_CRAFTING = register("spirit_instiller_crafting");
	public static final SoundEvent SPIRIT_INSTILLER_CRAFTING_FINISHED = register("spirit_instiller_crafting_finished");
	
	public static final SoundEvent CELESTIAL_POCKET_WATCH_TICKING = register("celestial_pocket_watch_ticking");
	public static final SoundEvent CELESTIAL_POCKET_WATCH_FLY_BY = register("celestial_pocket_watch_sky_fly_by");
	
	public static final SoundEvent PAINTBRUSH_TRIGGER = register("paintbrush_trigger");
	public static final SoundEvent PAINTBRUSH_PAINT = register("paintbrush_paint");
	public static final SoundEvent PAINTBRUSH_SWITCH = register("paintbrush_switch");
	public static final SoundEvent PAINTBRUSH_SELECT = register("paintbrush_select");
	
	public static final SoundEvent INK_PROJECTILE_HIT = register("ink_projectile_hit");
	public static final SoundEvent INK_PROJECTILE_LAUNCH = register("ink_projectile_launch");
	
	public static final SoundEvent GROUND_SLAM_CHARGE = register("ground_slam_charge");
	public static final SoundEvent GROUND_SLAM = register("ground_slam");

	public static final SoundEvent DEEP_CRYSTAL_RING = register("deep_crystal_ring");
	public static final SoundEvent MEDIUM_CRYSTAL_RING = register("mid_crystal_ring");
	public static final SoundEvent LIGHT_CRYSTAL_RING = register("light_crystal_ring");

	public static final SoundEvent CRYSTAL_STRIKE = register("crystal_strike");

	public static final SoundEvent SHATTER_LIGHT = register("shatter_light");
	public static final SoundEvent SHATTER_HEAVY = register("shatter_heavy");
	public static final SoundEvent GLASS_SHIMMER = register("glass_shimmer");
	public static final SoundEvent SOFT_HUM = register("soft_hum");
	
	public static final SoundEvent CRITICAL_CRUNCH = register("critical_crunch");
	public static final SoundEvent IMPACT_BASE = register("impact_base");
	public static final SoundEvent IMPALING_HIT = register("impaling_hit");
	public static final SoundEvent LEGENDARY_WEAPON_CRAFT = register("legendary_weapon_craft");
	public static final SoundEvent METAL_HIT = register("metal_hit");
	public static final SoundEvent METAL_TAP = register("metal_tap");
	public static final SoundEvent METALLIC_UNSHEATHE = register("metallic_unsheathe");
	
	public static final SoundEvent ELECTRIC_DISCHARGE = register("discharge");

	public static final SoundEvent PERFECT_PARRY = register("perfect_parry");
	public static final SoundEvent LUNGE = register("lunge");
	public static final SoundEvent LUNGE_CRIT = register("lunge_crit");
	public static final SoundEvent SWORD_BLOCK = register("sword_block");


	public static final SoundEvent INCANDESCENT_CHARGE = register("incandescent_charge");
	public static final SoundEvent INCANDESCENT_ARM = register("incandescent_arm");

	public static final SoundEvent SHOOTING_STAR_CRACKER = register("shooting_star_cracker");
	public static final SoundEvent PRIMORDIAL_FIRE_CRACKLE = register("primordial_fire_crackle");
	public static final SoundEvent PRIMORDIAL_FIRE_DOT = register("primordial_fire_dot");
	public static final SoundEvent TEXT_REVEALED = register("spectrum.ui.text_revealed");
	public static final SoundEvent USE_FAIL = register("use_fail");
	public static final SoundEvent NEW_REVELATION = register("spectrum.ui.new_revelation");
	public static final SoundEvent NEW_RECIPE = register("spectrum.ui.new_recipe");
	public static final SoundEvent ITEM_ARMOR_EQUIP_GLOW_VISION = register("armor_equip_glow_vision");
	public static final SoundEvent PLAYER_TELEPORTS = register("player_teleports");
	public static final SoundEvent ENDER_SPLICE_CHARGES = register("ender_splice_charges");
	public static final SoundEvent ENDER_SPLICE_BOUND = register("ender_splice_bound");
	public static final SoundEvent NATURES_STAFF_USE = register("natures_staff_use");
	public static final SoundEvent EXCHANGING_STAFF_SELECT = register("exchanging_staff_select");
	public static final SoundEvent RADIANCE_PIN_TRIGGER = register("radiance_pin_trigger");
	public static final SoundEvent AIR_LAUNCH_BELT_CHARGING = register("air_launch_belt_charging");
	public static final SoundEvent OVERCHARGING = register("overcharging");
	public static final SoundEvent PUFF_CIRCLET_PFFT = register("puff_circlet");
	public static final SoundEvent MIDNIGHT_ABERRATION_CRUMBLING = register("midnight_aberration_crumbling");
	public static final SoundEvent STRUCTURE_SUCCESS = register("structure_success");
	public static final SoundEvent UNLOCK = register("unlock");
	public static final SoundEvent HUMMINGSTONE_HUM = register("hummingstone_hum");
	public static final SoundEvent COLOR_PICKER_PROCESSING = register("color_picker_processing");

	// Environmental Sounds
	public static final SoundEvent DIMENSION_SOUNDS = register("ambient.dimension_sounds");
	public static final SoundEvent HOWLING_WIND_HIGH = register("ambient.howling_wind_high");
	public static final SoundEvent HOWLING_WIND_LOW = register("ambient.howling_wind_low");
	public static final SoundEvent SHOWER = register("ambient.rain_shower");
	public static final SoundEvent LAMENTS = register("ambient.laments");


	public static final SoundEvent CRYSTAL_AURA = register("ambient.crystal_aura");

	public static final SoundEvent BLACK_HOLE_CHEST_OPEN = register("black_hole_chest_open");
	public static final SoundEvent BLACK_HOLE_CHEST_CLOSE = register("black_hole_chest_close");
	public static final SoundEvent COMPACTING_CHEST_OPEN = register("compacting_chest_open");
	public static final SoundEvent COMPACTING_CHEST_CLOSE = register("compacting_chest_close");
	
	public static final SoundEvent DREAMFLAYER_ACTIVATE = register("dreamflayer_activate");
	public static final SoundEvent DREAMFLAYER_DEACTIVATE = register("dreamflayer_deactivate");
	
	public static final SoundEvent RADIANCE_STAFF_CHARGING = register("radiance_staff_charging");
	public static final SoundEvent RADIANCE_STAFF_PLACE = register("radiance_staff_place");
	public static final SoundEvent RADIANCE_STAFF_BREAK = register("radiance_staff_break");
	
	public static final SoundEvent LIQUID_CRYSTAL_AMBIENT = register("liquid_crystal_ambient");
	public static final SoundEvent MUD_AMBIENT = register("mud_ambient");
	public static final SoundEvent MIDNIGHT_SOLUTION_AMBIENT = register("midnight_solution_ambient");
	
	public static final SoundEvent FADING_PLACED = register("fading_placed");
	public static final SoundEvent FAILING_PLACED = register("failing_placed");
	public static final SoundEvent RUIN_PLACED = register("ruin_placed");
	public static final SoundEvent FORFEITURE_PLACED = register("forfeiture_placed");
	
	public static final SoundEvent DEEPER_DOWN_PORTAL_OPEN = register("deeper_down_portal_open");
	public static final SoundEvent SQUEAKER = register("squeaker");
	
	public static final SoundEvent BLOCK_MEMORY_ADVANCE = register("block.memory.advance");
	public static final SoundEvent ITEM_PRIMORDIAL_LIGHTER_USE = register("item.primordial_lighter.use");

	// Block Sounds
	public static final SoundEvent BLOCK_THREAT_CONFLUX_ARM = register("block.threat_conflux.arm");
	public static final SoundEvent BLOCK_THREAT_CONFLUX_PRIME = register("block.threat_conflux.prime");
	public static final SoundEvent BLOCK_THREAT_CONFLUX_DISARM = register("block.threat_conflux.disarm");
	public static final SoundEvent BLOCK_PARAMETRIC_MINING_DEVICE_THROWN = register("block.parametric_mining_device.throw");
	public static final SoundEvent BLOCK_MODULAR_EXPLOSIVE_EXPLODE = register("block.modular_explosive.explode");
	
	public static final SoundEvent BLOCK_CITRINE_BLOCK_BREAK = register("block.citrine_block.break");
	public static final SoundEvent BLOCK_CITRINE_BLOCK_STEP = register("block.citrine_block.step");
	public static final SoundEvent BLOCK_CITRINE_BLOCK_PLACE = register("block.citrine_block.place");
	public static final SoundEvent BLOCK_CITRINE_BLOCK_HIT = register("block.citrine_block.hit");
	public static final SoundEvent BLOCK_CITRINE_BLOCK_FALL = register("block.citrine_block.fall");
	public static final SoundEvent BLOCK_CITRINE_CLUSTER_BREAK = register("block.citrine_cluster.break");
	public static final SoundEvent BLOCK_CITRINE_CLUSTER_STEP = register("block.citrine_cluster.step");
	public static final SoundEvent BLOCK_CITRINE_CLUSTER_PLACE = register("block.citrine_cluster.place");
	public static final SoundEvent BLOCK_CITRINE_CLUSTER_HIT = register("block.citrine_cluster.hit");
	public static final SoundEvent BLOCK_CITRINE_CLUSTER_FALL = register("block.citrine_cluster.fall");
	public static final SoundEvent BLOCK_SMALL_CITRINE_BUD_BREAK = register("block.small_citrine_bud.break");
	public static final SoundEvent BLOCK_SMALL_CITRINE_BUD_PLACE = register("block.small_citrine_bud.place");
	public static final SoundEvent BLOCK_MEDIUM_CITRINE_BUD_BREAK = register("block.medium_citrine_bud.break");
	public static final SoundEvent BLOCK_MEDIUM_CITRINE_BUD_PLACE = register("block.medium_citrine_bud.place");
	public static final SoundEvent BLOCK_LARGE_CITRINE_BUD_BREAK = register("block.large_citrine_bud.break");
	public static final SoundEvent BLOCK_LARGE_CITRINE_BUD_PLACE = register("block.large_citrine_bud.place");
	public static final SoundEvent BLOCK_CITRINE_BLOCK_CHIME = register("block.citrine_block.chime");
	
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_BREAK = register("block.topaz_block.break");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_STEP = register("block.topaz_block.step");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_PLACE = register("block.topaz_block.place");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_HIT = register("block.topaz_block.hit");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_FALL = register("block.topaz_block.fall");
	public static final SoundEvent BLOCK_TOPAZ_CLUSTER_BREAK = register("block.topaz_cluster.break");
	public static final SoundEvent BLOCK_TOPAZ_CLUSTER_STEP = register("block.topaz_cluster.step");
	public static final SoundEvent BLOCK_TOPAZ_CLUSTER_PLACE = register("block.topaz_cluster.place");
	public static final SoundEvent BLOCK_TOPAZ_CLUSTER_HIT = register("block.topaz_cluster.hit");
	public static final SoundEvent BLOCK_TOPAZ_CLUSTER_FALL = register("block.topaz_cluster.fall");
	public static final SoundEvent BLOCK_SMALL_TOPAZ_BUD_BREAK = register("block.small_topaz_bud.break");
	public static final SoundEvent BLOCK_SMALL_TOPAZ_BUD_PLACE = register("block.small_topaz_bud.place");
	public static final SoundEvent BLOCK_MEDIUM_TOPAZ_BUD_BREAK = register("block.medium_topaz_bud.break");
	public static final SoundEvent BLOCK_MEDIUM_TOPAZ_BUD_PLACE = register("block.medium_topaz_bud.place");
	public static final SoundEvent BLOCK_LARGE_TOPAZ_BUD_BREAK = register("block.large_topaz_bud.break");
	public static final SoundEvent BLOCK_LARGE_TOPAZ_BUD_PLACE = register("block.large_topaz_bud.place");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_CHIME = register("block.topaz_block.chime");
	
	public static final SoundEvent BLOCK_ONYX_BLOCK_BREAK = register("block.onyx_block.break");
	public static final SoundEvent BLOCK_ONYX_BLOCK_STEP = register("block.onyx_block.step");
	public static final SoundEvent BLOCK_ONYX_BLOCK_PLACE = register("block.onyx_block.place");
	public static final SoundEvent BLOCK_ONYX_BLOCK_HIT = register("block.onyx_block.hit");
	public static final SoundEvent BLOCK_ONYX_BLOCK_FALL = register("block.onyx_block.fall");
	public static final SoundEvent BLOCK_ONYX_CLUSTER_BREAK = register("block.onyx_cluster.break");
	public static final SoundEvent BLOCK_ONYX_CLUSTER_STEP = register("block.onyx_cluster.step");
	public static final SoundEvent BLOCK_ONYX_CLUSTER_PLACE = register("block.onyx_cluster.place");
	public static final SoundEvent BLOCK_ONYX_CLUSTER_HIT = register("block.onyx_cluster.hit");
	public static final SoundEvent BLOCK_ONYX_CLUSTER_FALL = register("block.onyx_cluster.fall");
	public static final SoundEvent BLOCK_SMALL_ONYX_BUD_BREAK = register("block.small_onyx_bud.break");
	public static final SoundEvent BLOCK_SMALL_ONYX_BUD_PLACE = register("block.small_onyx_bud.place");
	public static final SoundEvent BLOCK_MEDIUM_ONYX_BUD_BREAK = register("block.medium_onyx_bud.break");
	public static final SoundEvent BLOCK_MEDIUM_ONYX_BUD_PLACE = register("block.medium_onyx_bud.place");
	public static final SoundEvent BLOCK_LARGE_ONYX_BUD_BREAK = register("block.large_onyx_bud.break");
	public static final SoundEvent BLOCK_LARGE_ONYX_BUD_PLACE = register("block.large_onyx_bud.place");
	public static final SoundEvent BLOCK_ONYX_BLOCK_CHIME = register("block.onyx_block.chime");
	
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_BREAK = register("block.moonstone_block.break");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_STEP = register("block.moonstone_block.step");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_PLACE = register("block.moonstone_block.place");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_HIT = register("block.moonstone_block.hit");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_FALL = register("block.moonstone_block.fall");
	public static final SoundEvent BLOCK_MOONSTONE_CLUSTER_BREAK = register("block.moonstone_cluster.break");
	public static final SoundEvent BLOCK_MOONSTONE_CLUSTER_STEP = register("block.moonstone_cluster.step");
	public static final SoundEvent BLOCK_MOONSTONE_CLUSTER_PLACE = register("block.moonstone_cluster.place");
	public static final SoundEvent BLOCK_MOONSTONE_CLUSTER_HIT = register("block.moonstone_cluster.hit");
	public static final SoundEvent BLOCK_MOONSTONE_CLUSTER_FALL = register("block.moonstone_cluster.fall");
	public static final SoundEvent BLOCK_SMALL_MOONSTONE_BUD_BREAK = register("block.small_moonstone_bud.break");
	public static final SoundEvent BLOCK_SMALL_MOONSTONE_BUD_PLACE = register("block.small_moonstone_bud.place");
	public static final SoundEvent BLOCK_MEDIUM_MOONSTONE_BUD_BREAK = register("block.medium_moonstone_bud.break");
	public static final SoundEvent BLOCK_MEDIUM_MOONSTONE_BUD_PLACE = register("block.medium_moonstone_bud.place");
	public static final SoundEvent BLOCK_LARGE_MOONSTONE_BUD_BREAK = register("block.large_moonstone_bud.break");
	public static final SoundEvent BLOCK_LARGE_MOONSTONE_BUD_PLACE = register("block.large_moonstone_bud.place");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_CHIME = register("block.moonstone_block.chime");
	
	public static final SoundEvent SPECTRAL_BLOCK_BREAK = register("block.spectral_block.break");
	public static final SoundEvent SPECTRAL_BLOCK_STEP = register("block.spectral_block.step");
	public static final SoundEvent SPECTRAL_BLOCK_PLACE = register("block.spectral_block.place");
	public static final SoundEvent SPECTRAL_BLOCK_FALL = register("block.spectral_block.fall");
	public static final SoundEvent SPECTRAL_BLOCK_HIT = register("block.spectral_block.hit");
	public static final SoundEvent SPECTRAL_BLOCK_CHIME = register("block.spectral_block.chime");
	
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_AMBIENT = register("entity.preservation_turret.ambient");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_DEATH = register("entity.preservation_turret.death");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_HURT_CLOSED = register("entity.preservation_turret.hurt_closed");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_HURT = register("entity.preservation_turret.hurt");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_CLOSE = register("entity.preservation_turret.close");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_OPEN = register("entity.preservation_turret.open");
	public static final SoundEvent ENTITY_PRESERVATION_TURRET_SHOOT = register("entity.preservation_turret.shoot");
	
	public static final SoundEvent ENTITY_MONSTROSITY_SHOOT = register("entity.monstrosity.shoot");
	public static final SoundEvent ENTITY_MONSTROSITY_SWOOP = register("entity.monstrosity.swoop");
	public static final SoundEvent ENTITY_MONSTROSITY_AMBIENT = register("entity.monstrosity.ambient");
	public static final SoundEvent ENTITY_MONSTROSITY_HURT = register("entity.monstrosity.hurt");
	public static final SoundEvent ENTITY_MONSTROSITY_GROWL = register("entity.monstrosity.growl");
	
	public static final SoundEvent ENTITY_LIZARD_AMBIENT = register("entity.lizard.ambient");
	public static final SoundEvent ENTITY_LIZARD_HURT = register("entity.lizard.hurt");
	public static final SoundEvent ENTITY_LIZARD_DEATH = register("entity.lizard.death");
	
	public static final SoundEvent ENTITY_KINDLING_AMBIENT = register("entity.kindling.ambient");
	public static final SoundEvent ENTITY_KINDLING_HURT = register("entity.kindling.hurt");
	public static final SoundEvent ENTITY_KINDLING_DEATH = register("entity.kindling.death");
	public static final SoundEvent ENTITY_KINDLING_SHOOT = register("entity.kindling.shoot");
	public static final SoundEvent ENTITY_KINDLING_ANGRY = register("entity.kindling.angry");
	public static final SoundEvent ENTITY_KINDLING_JUMP = register("entity.kindling.jump");
	
	public static final SoundEvent ENTITY_EGG_LAYING_WOOLY_PIG_AMBIENT = register("entity.egg_laying_wooly_pig.ambient");
	public static final SoundEvent ENTITY_EGG_LAYING_WOOLY_HURT = register("entity.egg_laying_wooly_pig.hurt");
	public static final SoundEvent ENTITY_EGG_LAYING_WOOLY_DEATH = register("entity.egg_laying_wooly_pig.death");
	public static final SoundEvent ENTITY_EGG_LAYING_WOOLY_STEP = register("entity.egg_laying_wooly_pig.step");
	
	public static final SoundEvent ENTITY_ERASER_AMBIENT = register("entity.eraser.ambient");
	public static final SoundEvent ENTITY_ERASER_HURT = register("entity.eraser.hurt");
	public static final SoundEvent ENTITY_ERASER_DEATH = register("entity.eraser.death");
	public static final SoundEvent ENTITY_ERASER_STEP = register("entity.eraser.step");
	
	private static SoundEvent register(String id) {
		Identifier identifier = SpectrumCommon.locate(id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
	}
	
	public static void register() {
		SpectrumCommon.logInfo("Registering Sound Events...");
	}
	
}
