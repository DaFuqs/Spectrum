package de.dafuqs.spectrum.registries;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class SpectrumBlockSoundGroups {
	
	public static BlockSoundGroup CITRINE_BLOCK;
	public static BlockSoundGroup CITRINE_CLUSTER;
	public static BlockSoundGroup SMALL_CITRINE_BUD;
	public static BlockSoundGroup MEDIUM_CITRINE_BUD;
	public static BlockSoundGroup LARGE_CITRINE_BUD;
	
	public static BlockSoundGroup TOPAZ_BLOCK;
	public static BlockSoundGroup TOPAZ_CLUSTER;
	public static BlockSoundGroup SMALL_TOPAZ_BUD;
	public static BlockSoundGroup MEDIUM_TOPAZ_BUD;
	public static BlockSoundGroup LARGE_TOPAZ_BUD;
	
	public static BlockSoundGroup ONYX_BLOCK;
	public static BlockSoundGroup ONYX_CLUSTER;
	public static BlockSoundGroup SMALL_ONYX_BUD;
	public static BlockSoundGroup MEDIUM_ONYX_BUD;
	public static BlockSoundGroup LARGE_ONYX_BUD;
	
	public static BlockSoundGroup MOONSTONE_BLOCK;
	public static BlockSoundGroup MOONSTONE_CLUSTER;
	public static BlockSoundGroup SMALL_MOONSTONE_BUD;
	public static BlockSoundGroup MEDIUM_MOONSTONE_BUD;
	public static BlockSoundGroup LARGE_MOONSTONE_BUD;
	
	public static BlockSoundGroup SPECTRAL_BLOCK;
	public static BlockSoundGroup WAND_LIGHT;
	
	// Mob Blocks
	public static BlockSoundGroup AXOLOTL_MOB_BLOCK;
	public static BlockSoundGroup BAT_MOB_BLOCK;
	public static BlockSoundGroup BEE_MOB_BLOCK;
	public static BlockSoundGroup BLAZE_MOB_BLOCK;
	public static BlockSoundGroup CAT_MOB_BLOCK;
	public static BlockSoundGroup CHICKEN_MOB_BLOCK;
	public static BlockSoundGroup COW_MOB_BLOCK;
	public static BlockSoundGroup CREEPER_MOB_BLOCK;
	public static BlockSoundGroup ENDER_DRAGON_MOB_BLOCK;
	public static BlockSoundGroup ENDERMAN_MOB_BLOCK;
	public static BlockSoundGroup ENDERMITE_MOB_BLOCK;
	public static BlockSoundGroup EVOKER_MOB_BLOCK;
	public static BlockSoundGroup FISH_MOB_BLOCK;
	public static BlockSoundGroup FOX_MOB_BLOCK;
	public static BlockSoundGroup GHAST_MOB_BLOCK;
	public static BlockSoundGroup GLOW_SQUID_MOB_BLOCK;
	public static BlockSoundGroup GOAT_MOB_BLOCK;
	public static BlockSoundGroup GUARDIAN_MOB_BLOCK;
	public static BlockSoundGroup HORSE_MOB_BLOCK;
	public static BlockSoundGroup ILLUSIONER_MOB_BLOCK;
	public static BlockSoundGroup LLAMA_MOB_BLOCK;
	public static BlockSoundGroup OCELOT_MOB_BLOCK;
	public static BlockSoundGroup PARROT_MOB_BLOCK;
	public static BlockSoundGroup PHANTOM_MOB_BLOCK;
	public static BlockSoundGroup PIG_MOB_BLOCK;
	public static BlockSoundGroup PIGLIN_MOB_BLOCK;
	public static BlockSoundGroup POLAR_BEAR_MOB_BLOCK;
	public static BlockSoundGroup PUFFERFISH_MOB_BLOCK;
	public static BlockSoundGroup RABBIT_MOB_BLOCK;
	public static BlockSoundGroup SHEEP_MOB_BLOCK;
	public static BlockSoundGroup SHULKER_MOB_BLOCK;
	public static BlockSoundGroup SILVERFISH_MOB_BLOCK;
	public static BlockSoundGroup SKELETON_MOB_BLOCK;
	public static BlockSoundGroup SLIME_MOB_BLOCK;
	public static BlockSoundGroup SNOW_GOLEM_MOB_BLOCK;
	public static BlockSoundGroup SPIDER_MOB_BLOCK;
	public static BlockSoundGroup SQUID_MOB_BLOCK;
	public static BlockSoundGroup STRAY_MOB_BLOCK;
	public static BlockSoundGroup STRIDER_MOB_BLOCK;
	public static BlockSoundGroup TURTLE_MOB_BLOCK;
	public static BlockSoundGroup WITCH_MOB_BLOCK;
	public static BlockSoundGroup WITHER_MOB_BLOCK;
	public static BlockSoundGroup WITHER_SKELETON_MOB_BLOCK;
	public static BlockSoundGroup ZOMBIE_MOB_BLOCK;
	
	public static void register() {
		CITRINE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_BREAK, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_STEP, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_PLACE, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_FALL);
		CITRINE_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_BREAK, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_PLACE, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
		SMALL_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_SMALL_CITRINE_BUD_BREAK, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_SMALL_CITRINE_BUD_PLACE, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
		MEDIUM_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MEDIUM_CITRINE_BUD_BREAK, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_MEDIUM_CITRINE_BUD_PLACE, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
		LARGE_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_LARGE_CITRINE_BUD_BREAK, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_LARGE_CITRINE_BUD_PLACE, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
		
		TOPAZ_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_BREAK, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_STEP, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_PLACE, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_FALL);
		TOPAZ_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_BREAK, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_PLACE, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
		SMALL_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_SMALL_TOPAZ_BUD_BREAK, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_SMALL_TOPAZ_BUD_PLACE, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
		MEDIUM_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MEDIUM_TOPAZ_BUD_BREAK, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_MEDIUM_TOPAZ_BUD_PLACE, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
		LARGE_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_LARGE_TOPAZ_BUD_BREAK, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_LARGE_TOPAZ_BUD_PLACE, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
		
		ONYX_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_BREAK, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_STEP, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_PLACE, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_HIT, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_FALL);
		ONYX_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_BREAK, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_PLACE, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
		SMALL_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_SMALL_ONYX_BUD_BREAK, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_SMALL_ONYX_BUD_PLACE, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
		MEDIUM_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MEDIUM_ONYX_BUD_BREAK, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_MEDIUM_ONYX_BUD_PLACE, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
		LARGE_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_LARGE_ONYX_BUD_BREAK, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_LARGE_ONYX_BUD_PLACE, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
		
		MOONSTONE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_BREAK, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_STEP, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_PLACE, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_FALL);
		MOONSTONE_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_PLACE, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
		SMALL_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_SMALL_MOONSTONE_BUD_BREAK, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_SMALL_MOONSTONE_BUD_PLACE, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
		MEDIUM_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_MEDIUM_MOONSTONE_BUD_BREAK, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_MEDIUM_MOONSTONE_BUD_PLACE, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
		LARGE_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_LARGE_MOONSTONE_BUD_BREAK, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, SpectrumSoundEvents.BLOCK_LARGE_MOONSTONE_BUD_PLACE, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
		
		SPECTRAL_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.SPECTRAL_BLOCK_BREAK, SpectrumSoundEvents.SPECTRAL_BLOCK_STEP, SpectrumSoundEvents.SPECTRAL_BLOCK_PLACE, SpectrumSoundEvents.SPECTRAL_BLOCK_HIT, SpectrumSoundEvents.SPECTRAL_BLOCK_FALL);
		WAND_LIGHT = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.LIGHT_STAFF_BREAK, SoundEvents.BLOCK_AMETHYST_CLUSTER_STEP, SpectrumSoundEvents.LIGHT_STAFF_PLACE, SpectrumSoundEvents.LIGHT_STAFF_BREAK, SpectrumSoundEvents.LIGHT_STAFF_BREAK);
		
		// float volume, float pitch, SoundEvent breakSound, SoundEvent stepSound, SoundEvent placeSound, SoundEvent hitSound, SoundEvent fallSound
		AXOLOTL_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_AXOLOTL_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_AXOLOTL_IDLE_AIR, SoundEvents.ENTITY_AXOLOTL_HURT, SoundEvents.ENTITY_AXOLOTL_IDLE_AIR);
		BAT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_BAT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_BAT_AMBIENT, SoundEvents.ENTITY_BAT_HURT, SoundEvents.ENTITY_BAT_AMBIENT);
		BEE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_BEE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_BEE_POLLINATE, SoundEvents.ENTITY_BEE_HURT, SoundEvents.ENTITY_BEE_POLLINATE);
		BLAZE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_BLAZE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_BLAZE_AMBIENT, SoundEvents.ENTITY_BLAZE_HURT, SoundEvents.ENTITY_BLAZE_AMBIENT);
		CAT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_CAT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_CAT_AMBIENT, SoundEvents.ENTITY_CAT_HURT, SoundEvents.ENTITY_CAT_AMBIENT);
		CHICKEN_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_CHICKEN_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundEvents.ENTITY_CHICKEN_HURT, SoundEvents.ENTITY_CHICKEN_AMBIENT);
		COW_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_COW_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_COW_AMBIENT, SoundEvents.ENTITY_COW_HURT, SoundEvents.ENTITY_COW_AMBIENT);
		CREEPER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_CREEPER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_CREEPER_HURT, SoundEvents.ENTITY_CREEPER_HURT, SoundEvents.ENTITY_CREEPER_HURT);
		ENDER_DRAGON_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_ENDER_DRAGON_HURT, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, SoundEvents.ENTITY_ENDER_DRAGON_HURT, SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT);
		ENDERMAN_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_ENDERMAN_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundEvents.ENTITY_ENDERMAN_HURT, SoundEvents.ENTITY_ENDERMAN_AMBIENT);
		ENDERMITE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_ENDERMITE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_ENDERMITE_AMBIENT, SoundEvents.ENTITY_ENDERMITE_HURT, SoundEvents.ENTITY_ENDERMITE_AMBIENT);
		EVOKER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_EVOKER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_EVOKER_AMBIENT, SoundEvents.ENTITY_EVOKER_HURT, SoundEvents.ENTITY_EVOKER_AMBIENT);
		FISH_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_COD_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_COD_AMBIENT, SoundEvents.ENTITY_COD_HURT, SoundEvents.ENTITY_COD_AMBIENT);
		FOX_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_FOX_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_FOX_AMBIENT, SoundEvents.ENTITY_FOX_HURT, SoundEvents.ENTITY_FOX_AMBIENT);
		GHAST_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_GHAST_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_GHAST_AMBIENT, SoundEvents.ENTITY_GHAST_HURT, SoundEvents.ENTITY_GHAST_AMBIENT);
		GLOW_SQUID_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_GLOW_SQUID_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_GLOW_SQUID_AMBIENT, SoundEvents.ENTITY_GLOW_SQUID_HURT, SoundEvents.ENTITY_GLOW_SQUID_AMBIENT);
		GOAT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_GOAT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_GOAT_AMBIENT, SoundEvents.ENTITY_GOAT_HURT, SoundEvents.ENTITY_GOAT_AMBIENT);
		GUARDIAN_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_GUARDIAN_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_GUARDIAN_AMBIENT, SoundEvents.ENTITY_GUARDIAN_HURT, SoundEvents.ENTITY_GUARDIAN_AMBIENT);
		HORSE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_HORSE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_HORSE_AMBIENT, SoundEvents.ENTITY_HORSE_HURT, SoundEvents.ENTITY_HORSE_AMBIENT);
		ILLUSIONER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_ILLUSIONER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_ILLUSIONER_AMBIENT, SoundEvents.ENTITY_ILLUSIONER_HURT, SoundEvents.ENTITY_ILLUSIONER_AMBIENT);
		LLAMA_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_LLAMA_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_LLAMA_AMBIENT, SoundEvents.ENTITY_LLAMA_HURT, SoundEvents.ENTITY_LLAMA_AMBIENT);
		OCELOT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_OCELOT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_OCELOT_AMBIENT, SoundEvents.ENTITY_OCELOT_HURT, SoundEvents.ENTITY_OCELOT_AMBIENT);
		PARROT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_PARROT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_PARROT_AMBIENT, SoundEvents.ENTITY_PARROT_HURT, SoundEvents.ENTITY_PARROT_AMBIENT);
		PHANTOM_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_PHANTOM_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundEvents.ENTITY_PHANTOM_HURT, SoundEvents.ENTITY_PHANTOM_AMBIENT);
		PIG_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_PIG_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_PIG_AMBIENT, SoundEvents.ENTITY_PIG_HURT, SoundEvents.ENTITY_PIG_AMBIENT);
		PIGLIN_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_PIGLIN_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_PIGLIN_AMBIENT, SoundEvents.ENTITY_PIGLIN_HURT, SoundEvents.ENTITY_PIGLIN_AMBIENT);
		POLAR_BEAR_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_POLAR_BEAR_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_POLAR_BEAR_AMBIENT, SoundEvents.ENTITY_POLAR_BEAR_HURT, SoundEvents.ENTITY_POLAR_BEAR_AMBIENT);
		PUFFERFISH_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_PUFFER_FISH_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_PUFFER_FISH_AMBIENT, SoundEvents.ENTITY_PUFFER_FISH_HURT, SoundEvents.ENTITY_PUFFER_FISH_AMBIENT);
		RABBIT_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_RABBIT_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_RABBIT_AMBIENT, SoundEvents.ENTITY_RABBIT_HURT, SoundEvents.ENTITY_RABBIT_AMBIENT);
		SHEEP_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SHEEP_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SHEEP_AMBIENT, SoundEvents.ENTITY_SHEEP_HURT, SoundEvents.ENTITY_SHEEP_AMBIENT);
		SHULKER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SHULKER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SHULKER_AMBIENT, SoundEvents.ENTITY_SHULKER_HURT, SoundEvents.ENTITY_SHULKER_AMBIENT);
		SILVERFISH_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SILVERFISH_AMBIENT, SoundEvents.ENTITY_SILVERFISH_HURT, SoundEvents.ENTITY_SILVERFISH_AMBIENT);
		SKELETON_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SKELETON_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SKELETON_AMBIENT, SoundEvents.ENTITY_SKELETON_HURT, SoundEvents.ENTITY_SKELETON_AMBIENT);
		SLIME_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SLIME_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SLIME_SQUISH_SMALL, SoundEvents.ENTITY_SLIME_HURT, SoundEvents.ENTITY_SLIME_SQUISH_SMALL);
		SNOW_GOLEM_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SNOW_GOLEM_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SNOW_GOLEM_AMBIENT, SoundEvents.ENTITY_SNOW_GOLEM_HURT, SoundEvents.ENTITY_SNOW_GOLEM_AMBIENT);
		SPIDER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SPIDER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SPIDER_AMBIENT, SoundEvents.ENTITY_SPIDER_HURT, SoundEvents.ENTITY_SPIDER_AMBIENT);
		SQUID_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_SQUID_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_SQUID_AMBIENT, SoundEvents.ENTITY_SQUID_HURT, SoundEvents.ENTITY_SQUID_AMBIENT);
		STRAY_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_STRAY_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_STRAY_AMBIENT, SoundEvents.ENTITY_STRAY_HURT, SoundEvents.ENTITY_STRAY_AMBIENT);
		STRIDER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_STRIDER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_STRIDER_AMBIENT, SoundEvents.ENTITY_STRIDER_HURT, SoundEvents.ENTITY_STRIDER_AMBIENT);
		TURTLE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_TURTLE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_TURTLE_AMBIENT_LAND, SoundEvents.ENTITY_TURTLE_HURT, SoundEvents.ENTITY_TURTLE_AMBIENT_LAND);
		WITCH_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_WITCH_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_WITCH_AMBIENT, SoundEvents.ENTITY_WITCH_HURT, SoundEvents.ENTITY_WITCH_AMBIENT);
		WITHER_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_WITHER_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_WITHER_AMBIENT, SoundEvents.ENTITY_WITHER_HURT, SoundEvents.ENTITY_WITHER_AMBIENT);
		WITHER_SKELETON_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_WITHER_SKELETON_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT, SoundEvents.ENTITY_WITHER_SKELETON_HURT, SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT);
		ZOMBIE_MOB_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.ENTITY_ZOMBIE_DEATH, SoundEvents.BLOCK_WOOL_STEP, SoundEvents.ENTITY_ZOMBIE_AMBIENT, SoundEvents.ENTITY_ZOMBIE_HURT, SoundEvents.ENTITY_ZOMBIE_AMBIENT);
	}
	
}
