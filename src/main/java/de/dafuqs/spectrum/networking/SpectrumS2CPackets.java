package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;

public class SpectrumS2CPackets {

	// Simple particles
	public static final Identifier PLAY_PARTICLE_WITH_RANDOM_OFFSET_AND_VELOCITY = SpectrumCommon.locate("play_particle_random");
	public static final Identifier PLAY_PARTICLE_WITH_EXACT_VELOCITY = SpectrumCommon.locate("play_particle_exact");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID = SpectrumCommon.locate("play_particle_with_pattern_and_velocity");
	public static final Identifier PLAY_PARTICLE_AROUND_BLOCK_SIDES = SpectrumCommon.locate("play_sided_block_particles");
	public static final Identifier PLAY_PARTICLE_AROUND_AREA = SpectrumCommon.locate("play_area_particles");


	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_upgraded_particle");
	public static final Identifier PLAY_PEDESTAL_START_CRAFTING_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_pedestal_start_crafting_particle");
	public static final Identifier PLAY_FUSION_CRAFTING_IN_PROGRESS_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_fusion_crafting_in_progress_particle");
	public static final Identifier PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID = SpectrumCommon.locate("play_fusion_crafting_finished_particle");
	public static final Identifier PLAY_SHOOTING_STAR_PARTICLES = SpectrumCommon.locate("play_shooting_star_particles");
	public static final Identifier PLAY_INK_EFFECT_PARTICLES = SpectrumCommon.locate("play_ink_effect_particles");
	public static final Identifier PLAY_PRESENT_OPENING_PARTICLES = SpectrumCommon.locate("play_present_opening_particles");

	// Complex particles
	public static final Identifier TYPED_TRANSMISSION = SpectrumCommon.locate("typed_transmission");
	public static final Identifier COLOR_TRANSMISSION = SpectrumCommon.locate("color_transmission");
	public static final Identifier PASTEL_TRANSMISSION = SpectrumCommon.locate("pastel_transmission");

	// Sounds
	public static final Identifier PLAY_BLOCK_BOUND_SOUND_INSTANCE = SpectrumCommon.locate("play_pedestal_crafting_sound_instance");
	public static final Identifier PLAY_TAKE_OFF_BELT_SOUND_INSTANCE = SpectrumCommon.locate("play_take_off_belt_sound_instance");

	// Others
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = SpectrumCommon.locate("change_particle_spawner_settings_client");
	public static final Identifier START_SKY_LERPING = SpectrumCommon.locate("start_sky_lerping");
	public static final Identifier PLAY_MEMORY_MANIFESTING_PARTICLES = SpectrumCommon.locate("play_memory_manifesting_particles");
	public static final Identifier UPDATE_BOSS_BAR = SpectrumCommon.locate("update_boss_bar");
	public static final Identifier UPDATE_BLOCK_ENTITY_INK = SpectrumCommon.locate("update_block_entity_ink");
	public static final Identifier INK_COLOR_SELECTED = SpectrumCommon.locate("ink_color_select");
	public static final Identifier PLAY_ASCENSION_APPLIED_EFFECTS = SpectrumCommon.locate("play_ascension_applied_effects");
	public static final Identifier PLAY_DIVINITY_APPLIED_EFFECTS = SpectrumCommon.locate("play_divinity_applied_effects");
	public static final Identifier MOONSTONE_BLAST = SpectrumCommon.locate("moonstone_blast");
	public static final Identifier SYNC_ARTISANS_ATLAS = SpectrumCommon.locate("sync_artisans_atlas");
	public static final Identifier SYNC_MENTAL_PRESENCE = SpectrumCommon.locate("sync_mental_presence");

	// Logic
	public static final Identifier COMPACTING_CHEST_STATUS_UPDATE = SpectrumCommon.locate("compacting_chest_status");
	public static final Identifier BLACK_HOLE_CHEST_STATUS_UPDATE = SpectrumCommon.locate("black_hole_chest_status");
	public static final Identifier RESTOCKING_CHEST_STATUS_UPDATE = SpectrumCommon.locate("restocking_chest_status");
	public static final Identifier PASTEL_NODE_STATUS_UPDATE = SpectrumCommon.locate("pastel_node_status");
	public static final Identifier PASTEL_NETWORK_EDGE_SYNC = SpectrumCommon.locate("pastel_edge_sync");

}
