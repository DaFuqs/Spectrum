package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumS2CPackets {
	
	public static final Identifier PLAY_LIGHT_CREATED_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_light_created_particle");
	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_upgraded_particle");
	public static final Identifier PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_fusion_crafting_finished_particle");
	public static final Identifier PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle_with_offset");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle_with_random_offset_and_velocity");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle_with_pattern_and_velocity");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings_client");
	public static final Identifier INITIATE_ITEM_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "initiate_item_transfer");
	public static final Identifier INITIATE_TRANSPHERE = new Identifier(SpectrumCommon.MOD_ID, "initiate_transphere");
	public static final Identifier INITIATE_EXPERIENCE_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "initiate_experience_transfer");
	public static final Identifier INITIATE_BLOCK_POS_EVENT_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "block_pos_event_transfer");
	public static final Identifier INITIATE_WIRELESS_REDSTONE_TRANSMISSION = new Identifier(SpectrumCommon.MOD_ID, "initiate_wireless_redstone_transmission");
	public static final Identifier PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "item_entity_absorbed");
	public static final Identifier PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "experience_orb_entity_absorbed");
	public static final Identifier PLAY_BLOCK_BOUND_SOUND_INSTANCE = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_sound_instance");
	public static final Identifier PLAY_TAKE_OFF_BELT_SOUND_INSTANCE = new Identifier(SpectrumCommon.MOD_ID, "play_take_off_belt_sound_instance");
	public static final Identifier PLAY_SHOOTING_STAR_PARTICLES = new Identifier(SpectrumCommon.MOD_ID, "play_shooting_star_particles");
	public static final Identifier START_SKY_LERPING = new Identifier(SpectrumCommon.MOD_ID, "start_sky_lerping");
	public static final Identifier PLAY_MEMORY_MANIFESTING_PARTICLES = new Identifier(SpectrumCommon.MOD_ID, "play_memory_manifesting_particles");
	public static final Identifier UPDATE_BOSS_BAR = new Identifier(SpectrumCommon.MOD_ID, "update_boss_bar");
	
}
