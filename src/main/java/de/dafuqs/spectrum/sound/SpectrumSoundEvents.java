package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

public class SpectrumSoundEvents {
	
	public static SoundEvent PEDESTAL_CRAFTING = register("pedestal_crafting");;
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_GENERIC = register("pedestal_crafting_finished_generic");
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_AMETHYST = register("pedestal_crafting_finished_amethyst");
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_CITRINE = register("pedestal_crafting_finished_citrine");
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_TOPAZ = register("pedestal_crafting_finished_topaz");
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_ONYX = register("pedestal_crafting_finished_onyx");
	public static SoundEvent PEDESTAL_CRAFTING_FINISHED_MOONSTONE = register("pedestal_crafting_finished_moonstone");
	public static SoundEvent PEDESTAL_UPGRADE = register("pedestal_upgrade");
	
	public static SoundEvent FUSION_SHRINE_CRAFTING = register("fusion_shrine_crafting");
	public static SoundEvent FUSION_SHRINE_CRAFTING_FINISHED = register("fusion_shrine_crafting_finished");
	public static SoundEvent FUSION_SHRINE_CRAFTING_ABORTED = register("fusion_shrine_crafting_aborted");

	public static SoundEvent NEW_REVELATION = register("new_revelation");
	public static final SoundEvent NEW_RECIPE = register("new_recipe");
	public static SoundEvent SPECTRUM_THEME = register("spectrum_theme");
	public static SoundEvent DIMENSION_SOUNDS = register("dimension_sounds");
	public static SoundEvent ITEM_ARMOR_EQUIP_GLOW_VISION = register("armor_equip_glow_vision");
	public static SoundEvent PLAYER_TELEPORTS = register("player_teleports");
	public static SoundEvent ENDER_SPLICE_CHARGES = register("ender_splice_charges");
	public static SoundEvent ENDER_SPLICE_BOUND = register("ender_splice_bound");
	public static SoundEvent NATURES_STAFF_USE = register("natures_staff_use");
	public static SoundEvent EXCHANGE_STAFF_SELECT = register("exchange_staff_select");

	public static SoundEvent SUCKING_CHEST_OPEN = register("sucking_chest_open");
	public static SoundEvent SUCKING_CHEST_CLOSE = register("sucking_chest_close");

	public static SoundEvent LIGHT_STAFF_CHARGING = register("light_staff_charging");
	public static SoundEvent LIGHT_STAFF_PLACE = register("light_staff_place");
	public static SoundEvent LIGHT_STAFF_BREAK = register("light_staff_break");

	public static SoundEvent LIQUID_CRYSTAL_AMBIENT = register("liquid_crystal_ambient");
	public static SoundEvent MUD_AMBIENT = register("mud_ambient");
	
	public static SoundEvent FADING_PLACED = register("fading_placed");;
	public static SoundEvent FAILING_PLACED = register("failing_placed");
	public static SoundEvent RUIN_PLACED = register("ruin_placed");
	
	public static SoundEvent BLOCK_CITRINE_BLOCK_BREAK = register("block_citrine_block_break");
	public static SoundEvent BLOCK_CITRINE_BLOCK_STEP = register("block_citrine_block_step");
	public static SoundEvent BLOCK_CITRINE_BLOCK_PLACE = register("block_citrine_block_place");
	public static SoundEvent BLOCK_CITRINE_BLOCK_HIT = register("block_citrine_block_hit");
	public static SoundEvent BLOCK_CITRINE_BLOCK_FALL = register("block_citrine_block_fall");
	public static SoundEvent BLOCK_CITRINE_CLUSTER_BREAK = register("block_citrine_cluster_break");
	public static SoundEvent BLOCK_CITRINE_CLUSTER_STEP = register("block_citrine_cluster_step");
	public static SoundEvent BLOCK_CITRINE_CLUSTER_PLACE = register("block_citrine_cluster_place");
	public static SoundEvent BLOCK_CITRINE_CLUSTER_HIT = register("block_citrine_cluster_hit");
	public static SoundEvent BLOCK_CITRINE_CLUSTER_FALL = register("block_citrine_cluster_fall");
	public static SoundEvent BLOCK_SMALL_CITRINE_BUD_BREAK = register("block_small_citrine_bud_break");
	public static SoundEvent BLOCK_SMALL_CITRINE_BUD_PLACE = register("block_small_citrine_bud_place");
	public static SoundEvent BLOCK_MEDIUM_CITRINE_BUD_BREAK = register("block_medium_citrine_bud_break");
	public static SoundEvent BLOCK_MEDIUM_CITRINE_BUD_PLACE = register("block_medium_citrine_bud_place");
	public static SoundEvent BLOCK_LARGE_CITRINE_BUD_BREAK = register("block_large_citrine_bud_break");
	public static SoundEvent BLOCK_LARGE_CITRINE_BUD_PLACE = register("block_large_citrine_bud_place");

	public static SoundEvent BLOCK_TOPAZ_BLOCK_BREAK = register("block_topaz_block_break");
	public static SoundEvent BLOCK_TOPAZ_BLOCK_STEP = register("block_topaz_block_step");
	public static SoundEvent BLOCK_TOPAZ_BLOCK_PLACE = register("block_topaz_block_place");
	public static SoundEvent BLOCK_TOPAZ_BLOCK_HIT = register("block_topaz_block_hit");
	public static SoundEvent BLOCK_TOPAZ_BLOCK_FALL = register("block_topaz_block_fall");
	public static SoundEvent BLOCK_TOPAZ_CLUSTER_BREAK = register("block_topaz_cluster_break");
	public static SoundEvent BLOCK_TOPAZ_CLUSTER_STEP = register("block_topaz_cluster_step");
	public static SoundEvent BLOCK_TOPAZ_CLUSTER_PLACE = register("block_topaz_cluster_place");
	public static SoundEvent BLOCK_TOPAZ_CLUSTER_HIT = register("block_topaz_cluster_hit");
	public static SoundEvent BLOCK_TOPAZ_CLUSTER_FALL = register("block_topaz_cluster_fall");
	public static SoundEvent BLOCK_SMALL_TOPAZ_BUD_BREAK = register("block_small_topaz_bud_break");
	public static SoundEvent BLOCK_SMALL_TOPAZ_BUD_PLACE = register("block_small_topaz_bud_place");
	public static SoundEvent BLOCK_MEDIUM_TOPAZ_BUD_BREAK = register("block_medium_topaz_bud_break");
	public static SoundEvent BLOCK_MEDIUM_TOPAZ_BUD_PLACE = register("block_medium_topaz_bud_place");
	public static SoundEvent BLOCK_LARGE_TOPAZ_BUD_BREAK = register("block_large_topaz_bud_break");
	public static SoundEvent BLOCK_LARGE_TOPAZ_BUD_PLACE = register("block_large_topaz_bud_place");
	public static SoundEvent BLOCK_ONYX_BLOCK_BREAK = register("block_onyx_block_break");
	public static SoundEvent BLOCK_ONYX_BLOCK_STEP = register("block_onyx_block_step");
	public static SoundEvent BLOCK_ONYX_BLOCK_PLACE = register("block_onyx_block_place");
	public static SoundEvent BLOCK_ONYX_BLOCK_HIT = register("block_onyx_block_hit");
	public static SoundEvent BLOCK_ONYX_BLOCK_FALL = register("block_onyx_block_fall");
	public static SoundEvent BLOCK_ONYX_CLUSTER_BREAK = register("block_onyx_cluster_break");
	public static SoundEvent BLOCK_ONYX_CLUSTER_STEP = register("block_onyx_cluster_step");
	public static SoundEvent BLOCK_ONYX_CLUSTER_PLACE = register("block_onyx_cluster_place");
	public static SoundEvent BLOCK_ONYX_CLUSTER_HIT = register("block_onyx_cluster_hit");
	public static SoundEvent BLOCK_ONYX_CLUSTER_FALL = register("block_onyx_cluster_fall");
	public static SoundEvent BLOCK_SMALL_ONYX_BUD_BREAK = register("block_small_onyx_bud_break");
	public static SoundEvent BLOCK_SMALL_ONYX_BUD_PLACE = register("block_small_onyx_bud_place");
	public static SoundEvent BLOCK_MEDIUM_ONYX_BUD_BREAK = register("block_medium_onyx_bud_break");
	public static SoundEvent BLOCK_MEDIUM_ONYX_BUD_PLACE = register("block_medium_onyx_bud_place");
	public static SoundEvent BLOCK_LARGE_ONYX_BUD_BREAK = register("block_large_onyx_bud_break");
	public static SoundEvent BLOCK_LARGE_ONYX_BUD_PLACE = register("block_large_onyx_bud_place");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_BREAK = register("block_moonstone_block_break");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_STEP = register("block_moonstone_block_step");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_PLACE = register("block_moonstone_block_place");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_HIT = register("block_moonstone_block_hit");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_FALL = register("block_moonstone_block_fall");
	public static SoundEvent BLOCK_MOONSTONE_CLUSTER_BREAK = register("block_moonstone_cluster_break");
	public static SoundEvent BLOCK_MOONSTONE_CLUSTER_STEP = register("block_moonstone_cluster_step");
	public static SoundEvent BLOCK_MOONSTONE_CLUSTER_PLACE = register("block_moonstone_cluster_place");
	public static SoundEvent BLOCK_MOONSTONE_CLUSTER_HIT = register("block_moonstone_cluster_hit");
	public static SoundEvent BLOCK_MOONSTONE_CLUSTER_FALL = register("block_moonstone_cluster_fall");
	public static SoundEvent BLOCK_SMALL_MOONSTONE_BUD_BREAK = register("block_small_moonstone_bud_break");
	public static SoundEvent BLOCK_SMALL_MOONSTONE_BUD_PLACE = register("block_small_moonstone_bud_place");
	public static SoundEvent BLOCK_MEDIUM_MOONSTONE_BUD_BREAK = register("block_medium_moonstone_bud_break");
	public static SoundEvent BLOCK_MEDIUM_MOONSTONE_BUD_PLACE = register("block_medium_moonstone_bud_place");
	public static SoundEvent BLOCK_LARGE_MOONSTONE_BUD_BREAK = register("block_large_moonstone_bud_break");
	public static SoundEvent BLOCK_LARGE_MOONSTONE_BUD_PLACE = register("block_large_moonstone_bud_place");

	public static SoundEvent BLOCK_AMMOLITE_BLOCK_BREAK = register("block_spectral_block_break");
	public static SoundEvent BLOCK_AMMOLITE_BLOCK_STEP = register("block_spectral_block_step");
	public static SoundEvent BLOCK_AMMOLITE_BLOCK_PLACE = register("block_spectral_block_place");
	public static SoundEvent BLOCK_AMMOLITE_BLOCK_FALL = register("block_spectral_block_fall");
	public static SoundEvent BLOCK_AMMOLITE_BLOCK_HIT = register("block_spectral_block_hit");
	public static SoundEvent BLOCK_AMMOLITE_BLOCK_CHIME = register("block_spectral_block_chime");

	public static SoundEvent BLOCK_CITRINE_BLOCK_CHIME = register("block_citrine_block_chime");
	public static SoundEvent BLOCK_TOPAZ_BLOCK_CHIME = register("block_topaz_block_chime");
	public static SoundEvent BLOCK_ONYX_BLOCK_CHIME = register("block_onyx_block_chime");
	public static SoundEvent BLOCK_MOONSTONE_BLOCK_CHIME = register("block_moonstone_block_chime");

	private static SoundEvent register(String id) {
		Identifier identifier = new Identifier(SpectrumCommon.MOD_ID, id);
		return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
	}

	public static void register() {
		SpectrumCommon.log(Level.INFO, "Registering Sound Events...");
	}

}
