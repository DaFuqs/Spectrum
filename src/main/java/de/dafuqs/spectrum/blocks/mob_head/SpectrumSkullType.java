package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

public enum SpectrumSkullType implements SkullBlock.SkullType {
    // Vanilla
	ALLAY(EntityType.ALLAY, SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM.getId()),
	AXOLOTL_BLUE(EntityType.AXOLOTL, SoundEvents.ENTITY_AXOLOTL_IDLE_WATER.getId()),
	AXOLOTL_WILD(EntityType.AXOLOTL, SoundEvents.ENTITY_AXOLOTL_IDLE_WATER.getId()),
	AXOLOTL_CYAN(EntityType.AXOLOTL, SoundEvents.ENTITY_AXOLOTL_IDLE_WATER.getId()),
	AXOLOTL_GOLD(EntityType.AXOLOTL, SoundEvents.ENTITY_AXOLOTL_IDLE_WATER.getId()),
	AXOLOTL_LEUCISTIC(EntityType.AXOLOTL, SoundEvents.ENTITY_AXOLOTL_IDLE_WATER.getId()),
	BAT(EntityType.BAT, SoundEvents.ENTITY_BAT_AMBIENT.getId()),
	BEE(EntityType.BEE, SoundEvents.ENTITY_BEE_POLLINATE.getId()),
	BLAZE(EntityType.BLAZE, SoundEvents.ENTITY_BLAZE_AMBIENT.getId()),
	CAMEL(EntityType.CAMEL, SoundEvents.ENTITY_CAMEL_AMBIENT.getId()),
	CAT(EntityType.CAT, SoundEvents.ENTITY_CAT_AMBIENT.getId()),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, SoundEvents.ENTITY_SPIDER_AMBIENT.getId()),
	CHICKEN(EntityType.CHICKEN, SoundEvents.ENTITY_CHICKEN_AMBIENT.getId()),
	COW(EntityType.COW, SoundEvents.ENTITY_COW_AMBIENT.getId()),
	DOLPHIN(EntityType.DOLPHIN, SoundEvents.ENTITY_DOLPHIN_AMBIENT.getId()),
	DONKEY(EntityType.DONKEY, SoundEvents.ENTITY_DONKEY_AMBIENT.getId()),
	DROWNED(EntityType.DROWNED, SoundEvents.ENTITY_DROWNED_AMBIENT.getId()),
	ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT.getId()),
	ENDERMAN(EntityType.ENDERMAN, SoundEvents.ENTITY_ENDERMAN_AMBIENT.getId()),
	ENDERMITE(EntityType.ENDERMITE, SoundEvents.ENTITY_ENDERMITE_AMBIENT.getId()),
	EVOKER(EntityType.EVOKER, SoundEvents.ENTITY_EVOKER_AMBIENT.getId()),
	FOX(EntityType.FOX, SoundEvents.ENTITY_FOX_AMBIENT.getId()),
	FOX_ARCTIC(EntityType.FOX, SoundEvents.ENTITY_FOX_AMBIENT.getId()),
	FROG_COLD(EntityType.FROG, SoundEvents.ENTITY_FROG_AMBIENT.getId()),
	FROG_TEMPERATE(EntityType.FROG, SoundEvents.ENTITY_FROG_AMBIENT.getId()),
	FROG_WARM(EntityType.FROG, SoundEvents.ENTITY_FROG_AMBIENT.getId()),
	GHAST(EntityType.GHAST, SoundEvents.ENTITY_GHAST_AMBIENT.getId()),
	GLOW_SQUID(EntityType.GLOW_SQUID, SoundEvents.ENTITY_GLOW_SQUID_AMBIENT.getId()),
	GOAT(EntityType.GOAT, SoundEvents.ENTITY_GOAT_AMBIENT.getId()),
	GUARDIAN(EntityType.GUARDIAN, SoundEvents.ENTITY_GUARDIAN_AMBIENT.getId()),
	HOGLIN(EntityType.HOGLIN, SoundEvents.ENTITY_HOGLIN_AMBIENT.getId()),
	HORSE(EntityType.HORSE, SoundEvents.ENTITY_HORSE_AMBIENT.getId()),
	HUSK(EntityType.HUSK, SoundEvents.ENTITY_HUSK_AMBIENT.getId()),
	ILLUSIONER(EntityType.ILLUSIONER, SoundEvents.ENTITY_ILLUSIONER_AMBIENT.getId()),
	IRON_GOLEM(EntityType.IRON_GOLEM, SoundEvents.ENTITY_IRON_GOLEM_STEP.getId()),
	LLAMA(EntityType.LLAMA, SoundEvents.ENTITY_LLAMA_AMBIENT.getId()),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, SoundEvents.ENTITY_MAGMA_CUBE_SQUISH.getId()),
	MOOSHROOM_BROWN(EntityType.MOOSHROOM, SoundEvents.ENTITY_COW_AMBIENT.getId()),
	MOOSHROOM_RED(EntityType.MOOSHROOM, SoundEvents.ENTITY_COW_AMBIENT.getId()),
	MULE(EntityType.MULE, SoundEvents.ENTITY_MULE_AMBIENT.getId()),
	OCELOT(EntityType.OCELOT, SoundEvents.ENTITY_OCELOT_AMBIENT.getId()),
	PANDA(EntityType.PANDA, SoundEvents.ENTITY_PANDA_AMBIENT.getId()),
	PARROT_BLUE(EntityType.PARROT, SoundEvents.ENTITY_PARROT_AMBIENT.getId()),
	PARROT_CYAN(EntityType.PARROT, SoundEvents.ENTITY_PARROT_AMBIENT.getId()),
	PARROT_GRAY(EntityType.PARROT, SoundEvents.ENTITY_PARROT_AMBIENT.getId()),
	PARROT_GREEN(EntityType.PARROT, SoundEvents.ENTITY_PARROT_AMBIENT.getId()),
	PARROT_RED(EntityType.PARROT, SoundEvents.ENTITY_PARROT_AMBIENT.getId()),
	PHANTOM(EntityType.PHANTOM, SoundEvents.ENTITY_PHANTOM_AMBIENT.getId()),
	PIG(EntityType.PIG, SoundEvents.ENTITY_PIG_AMBIENT.getId()),
	PILLAGER(EntityType.PILLAGER, SoundEvents.ENTITY_PILLAGER_AMBIENT.getId()),
	POLAR_BEAR(EntityType.POLAR_BEAR, SoundEvents.ENTITY_POLAR_BEAR_AMBIENT.getId()),
	PUFFERFISH(EntityType.PUFFERFISH, SoundEvents.ENTITY_PUFFER_FISH_AMBIENT.getId()),
	RABBIT(EntityType.RABBIT, SoundEvents.ENTITY_RABBIT_AMBIENT.getId()),
	RAVAGER(EntityType.RAVAGER, SoundEvents.ENTITY_RAVAGER_AMBIENT.getId()),
	SALMON(EntityType.SALMON, SoundEvents.ENTITY_SALMON_AMBIENT.getId()),
	SHEEP(EntityType.SHEEP, SoundEvents.ENTITY_SHEEP_AMBIENT.getId()),
	SHULKER(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_BLACK(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_BLUE(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_BROWN(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_CYAN(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_GRAY(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_GREEN(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_LIGHT_BLUE(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_LIGHT_GRAY(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_LIME(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_MAGENTA(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_ORANGE(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_PINK(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_PURPLE(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_RED(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_WHITE(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SHULKER_YELLOW(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_AMBIENT.getId()),
	SILVERFISH(EntityType.SILVERFISH, SoundEvents.ENTITY_SILVERFISH_AMBIENT.getId()),
	SKELETON_HORSE(EntityType.SKELETON_HORSE, SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT.getId()),
	SLIME(EntityType.SLIME, SoundEvents.ENTITY_SLIME_SQUISH.getId()),
	SNIFFER(EntityType.SNIFFER, SoundEvents.ENTITY_SNIFFER_IDLE.getId()),
	SNOW_GOLEM(EntityType.SNOW_GOLEM, SoundEvents.ENTITY_SNOW_GOLEM_AMBIENT.getId()),
	SPIDER(EntityType.SPIDER, SoundEvents.ENTITY_SPIDER_AMBIENT.getId()),
	SQUID(EntityType.SQUID, SoundEvents.ENTITY_SQUID_AMBIENT.getId()),
	STRAY(EntityType.STRAY, SoundEvents.ENTITY_STRAY_AMBIENT.getId()),
	STRIDER(EntityType.STRIDER, SoundEvents.ENTITY_STRIDER_AMBIENT.getId()),
	TADPOLE(EntityType.TADPOLE, SoundEvents.ENTITY_TADPOLE_FLOP.getId()),
	TROPICAL_FISH(EntityType.TROPICAL_FISH, SoundEvents.ENTITY_TROPICAL_FISH_AMBIENT.getId()),
	TURTLE(EntityType.TURTLE, SoundEvents.ENTITY_TURTLE_AMBIENT_LAND.getId()),
	VEX(EntityType.VEX, SoundEvents.ENTITY_VEX_AMBIENT.getId()),
	VILLAGER(EntityType.VILLAGER, SoundEvents.ENTITY_VILLAGER_AMBIENT.getId()),
	VINDICATOR(EntityType.VINDICATOR, SoundEvents.ENTITY_VINDICATOR_AMBIENT.getId()),
	WANDERING_TRADER(EntityType.WANDERING_TRADER, SoundEvents.ENTITY_WANDERING_TRADER_AMBIENT.getId()),
	WARDEN(EntityType.WARDEN, SoundEvents.ENTITY_WARDEN_AMBIENT.getId()),
	WITCH(EntityType.WITCH, SoundEvents.ENTITY_WITCH_AMBIENT.getId()),
	WITHER(EntityType.WITHER, SoundEvents.ENTITY_WITHER_AMBIENT.getId()),
	WOLF(EntityType.WOLF, SoundEvents.ENTITY_WOLF_AMBIENT.getId()),
	ZOGLIN(EntityType.ZOGLIN, SoundEvents.ENTITY_ZOGLIN_AMBIENT.getId()),
	ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT.getId()),
	ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT.getId()),
	ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT.getId()),

    // Spectrum
	EGG_LAYING_WOOLY_PIG(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, SoundEvents.ENTITY_PIG_AMBIENT.getId()),
	ERASER(SpectrumEntityTypes.ERASER, SoundEvents.ENTITY_SPIDER_AMBIENT.getId()),
	KINDLING(SpectrumEntityTypes.KINDLING, SpectrumSoundEvents.ENTITY_KINDLING_AMBIENT.getId()),
	LIZARD_BLACK(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_BLUE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_BROWN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_CYAN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_GRAY(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_GREEN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_LIGHT_BLUE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_LIGHT_GRAY(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_LIME(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_MAGENTA(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_ORANGE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_PINK(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_PURPLE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_RED(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_WHITE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	LIZARD_YELLOW(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getId()),
	MONSTROSITY(SpectrumEntityTypes.MONSTROSITY, SpectrumSoundEvents.ENTITY_MONSTROSITY_AMBIENT.getId()),
	PRESERVATION_TURRET(SpectrumEntityTypes.PRESERVATION_TURRET, SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_AMBIENT.getId());

    private final EntityType<?> entityType;
	private final Identifier noteBlockSound;
	
	SpectrumSkullType(EntityType<?> entityType, Identifier noteBlockSound) {
        this.entityType = entityType;
		this.noteBlockSound = noteBlockSound;
    }
    
    public EntityType<?> getEntityType() {
        return this.entityType;
    }
	
	public Identifier getNoteBlockSound() {
		return this.noteBlockSound;
	}
    
}
