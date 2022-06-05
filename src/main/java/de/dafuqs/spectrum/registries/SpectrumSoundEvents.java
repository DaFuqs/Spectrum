package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumSoundEvents {
	
	// Music
	public static final SoundEvent SPECTRUM_THEME = register("spectrum_theme");
	public static final SoundEvent BOSS_THEME = register("boss_theme");
	public static final SoundEvent DEEPER_DOWN_THEME = register("deeper_down_theme");
	
	// Sounds
	public static final SoundEvent PEDESTAL_CRAFTING = register("pedestal_crafting");
	;
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
	
	public static final SoundEvent ENCHANTER_WORKING = register("enchanter_working");
	public static final SoundEvent ENCHANTER_DING = register("enchanter_ding");
	
	public static final SoundEvent SPIRIT_INSTILLER_CRAFTING = register("spirit_instiller_crafting");
	public static final SoundEvent SPIRIT_INSTILLER_CRAFTING_FINISHED = register("spirit_instiller_crafting_finished");
	
	public static final SoundEvent CELESTIAL_POCKET_WATCH_TICKING = register("celestial_pocket_watch_ticking");
	public static final SoundEvent CELESTIAL_POCKET_WATCH_FLY_BY = register("celestial_pocket_watch_sky_fly_by");
	
	public static final SoundEvent SHOOTING_STAR_CRACKER = register("shooting_star_cracker");
	public static final SoundEvent TEXT_REVEALED = register("text_revealed");
	public static final SoundEvent USE_FAIL = register("use_fail");
	public static final SoundEvent NEW_REVELATION = register("new_revelation");
	public static final SoundEvent NEW_RECIPE = register("new_recipe");
	public static final SoundEvent DIMENSION_SOUNDS = register("dimension_sounds");
	public static final SoundEvent ITEM_ARMOR_EQUIP_GLOW_VISION = register("armor_equip_glow_vision");
	public static final SoundEvent PLAYER_TELEPORTS = register("player_teleports");
	public static final SoundEvent ENDER_SPLICE_CHARGES = register("ender_splice_charges");
	public static final SoundEvent ENDER_SPLICE_BOUND = register("ender_splice_bound");
	public static final SoundEvent NATURES_STAFF_USE = register("natures_staff_use");
	public static final SoundEvent EXCHANGE_STAFF_SELECT = register("exchange_staff_select");
	public static final SoundEvent RADIANCE_PIN_TRIGGER = register("radiance_pin_trigger");
	public static final SoundEvent AIR_LAUNCH_BELT_CHARGING = register("air_launch_belt_charging");
	public static final SoundEvent PUFF_CIRCLET_PFFT = register("puff_circlet");
	public static final SoundEvent MIDNIGHT_ABERRATION_CRUMBLING = register("midnight_aberration_crumbling");
	public static final SoundEvent STRUCTURE_SUCCESS = register("structure_success");
	
	public static final SoundEvent SUCKING_CHEST_OPEN = register("sucking_chest_open");
	public static final SoundEvent SUCKING_CHEST_CLOSE = register("sucking_chest_close");
	
	public static final SoundEvent LIGHT_STAFF_CHARGING = register("light_staff_charging");
	public static final SoundEvent LIGHT_STAFF_PLACE = register("light_staff_place");
	public static final SoundEvent LIGHT_STAFF_BREAK = register("light_staff_break");
	
	public static final SoundEvent LIQUID_CRYSTAL_AMBIENT = register("liquid_crystal_ambient");
	public static final SoundEvent MUD_AMBIENT = register("mud_ambient");
	public static final SoundEvent MIDNIGHT_SOLUTION_AMBIENT = register("midnight_solution_ambient");
	
	public static final SoundEvent FADING_PLACED = register("fading_placed");
	;
	public static final SoundEvent FAILING_PLACED = register("failing_placed");
	public static final SoundEvent RUIN_PLACED = register("ruin_placed");
	
	public static final SoundEvent BLOCK_MEMORY_ADVANCE = register("block.memory.advance");
	
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
	
	public static final SoundEvent SPECTRAL_BLOCK_BREAK = register("block.spectral_block.break");
	public static final SoundEvent SPECTRAL_BLOCK_STEP = register("block.spectral_block.step");
	public static final SoundEvent SPECTRAL_BLOCK_PLACE = register("block.spectral_block.place");
	public static final SoundEvent SPECTRAL_BLOCK_FALL = register("block.spectral_block.fall");
	public static final SoundEvent SPECTRAL_BLOCK_HIT = register("block.spectral_block.hit");
	public static final SoundEvent SPECTRAL_BLOCK_CHIME = register("block.spectral_block.chime");
	
	public static final SoundEvent BLOCK_CITRINE_BLOCK_CHIME = register("block.citrine_block.chime");
	public static final SoundEvent BLOCK_TOPAZ_BLOCK_CHIME = register("block.topaz_block.chime");
	public static final SoundEvent BLOCK_ONYX_BLOCK_CHIME = register("block.onyx_block.chime");
	public static final SoundEvent BLOCK_MOONSTONE_BLOCK_CHIME = register("block.moonstone_block.chime");
	
	private static SoundEvent register(String id) {
		Identifier identifier = new Identifier(SpectrumCommon.MOD_ID, id);
		return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
	}
	
	public static void register() {
		SpectrumCommon.logInfo("Registering Sound Events...");
	}
	
}
