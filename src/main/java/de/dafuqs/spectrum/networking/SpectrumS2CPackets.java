package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumS2CPackets {
	
	public static final Identifier PLAY_LIGHT_CREATED_PACKET_ID = SpectrumCommon.locate("play_light_created_particle");
	public static final Identifier PLAY_SMALL_LIGHT_CREATED_PACKET_ID = SpectrumCommon.locate("play_small_light_created_particle");
	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_upgraded_particle");
	public static final Identifier PLAY_PEDESTAL_START_CRAFTING_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_start_crafting_particle");
	public static final Identifier PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_fusion_crafting_finished_particle");
	public static final Identifier PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID = SpectrumCommon.locate("play_particle");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID = SpectrumCommon.locate("play_particle_with_offset");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID = SpectrumCommon.locate("play_particle_with_random_offset_and_velocity");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID = SpectrumCommon.locate("play_particle_with_pattern_and_velocity");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = SpectrumCommon.locate("change_particle_spawner_settings_client");
	public static final Identifier ITEM_TRANSMISSION = SpectrumCommon.locate("initiate_item_transfer");
	public static final Identifier COLOR_TRANSMISSION = SpectrumCommon.locate("initiate_transphere");
	public static final Identifier EXPERIENCE_TRANSMISSION = SpectrumCommon.locate("initiate_experience_transfer");
	public static final Identifier BLOCK_POS_EVENT_TRANSMISSION = SpectrumCommon.locate("block_pos_event_transfer");
	public static final Identifier WIRELESS_REDSTONE_TRANSMISSION = SpectrumCommon.locate("initiate_wireless_redstone_transmission");
	public static final Identifier PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = SpectrumCommon.locate("item_entity_absorbed");
	public static final Identifier PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = SpectrumCommon.locate("experience_orb_entity_absorbed");
	public static final Identifier PLAY_BLOCK_BOUND_SOUND_INSTANCE = SpectrumCommon.locate("play_pedestal_crafting_sound_instance");
	public static final Identifier PLAY_TAKE_OFF_BELT_SOUND_INSTANCE = SpectrumCommon.locate("play_take_off_belt_sound_instance");
	public static final Identifier PLAY_SHOOTING_STAR_PARTICLES = SpectrumCommon.locate("play_shooting_star_particles");
	public static final Identifier START_SKY_LERPING = SpectrumCommon.locate("start_sky_lerping");
	public static final Identifier PLAY_MEMORY_MANIFESTING_PARTICLES = SpectrumCommon.locate("play_memory_manifesting_particles");
	public static final Identifier UPDATE_BOSS_BAR = SpectrumCommon.locate("update_boss_bar");
	public static final Identifier UPDATE_BLOCK_ENTITY_INK = SpectrumCommon.locate("update_block_entity_ink");
	public static final Identifier INK_COLOR_SELECTED = SpectrumCommon.locate("ink_color_select");
	public static final Identifier PLAY_INK_EFFECT_PARTICLES = SpectrumCommon.locate("play_ink_effect_particles");
	public static final Identifier PLAY_PRESENT_OPENING_PARTICLES = SpectrumCommon.locate("play_present_opening_particles");
	public static final Identifier PLAY_ASCENSION_APPLIED_EFFECTS = SpectrumCommon.locate("play_ascension_applied_effects");
	public static final Identifier PLAY_DIVINITY_APPLIED_EFFECTS = SpectrumCommon.locate("play_divinity_applied_effects");
	public static final Identifier MOONSTONE_BLAST = SpectrumCommon.locate("moonstone_blast");
}
