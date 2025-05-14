package de.dafuqs.spectrum.blocks.mob_head;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public enum SpectrumSkullType implements SkullBlock.Type {
	
	// Vanilla
	ALLAY(EntityType.ALLAY, SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM.getLocation()),
	AXOLOTL_BLUE(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_WATER.getLocation()),
	AXOLOTL_CYAN(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_WATER.getLocation()),
	AXOLOTL_GOLD(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_WATER.getLocation()),
	AXOLOTL_LEUCISTIC(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_WATER.getLocation()),
	AXOLOTL_WILD(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_WATER.getLocation()),
	BAT(EntityType.BAT, SoundEvents.BAT_AMBIENT.getLocation()),
	BEE(EntityType.BEE, SoundEvents.BEE_POLLINATE.getLocation()),
	BLAZE(EntityType.BLAZE, SoundEvents.BLAZE_AMBIENT.getLocation()),
	CAMEL(EntityType.CAMEL, SoundEvents.CAMEL_AMBIENT.getLocation()),
	CAT(EntityType.CAT, SoundEvents.CAT_AMBIENT.getLocation()),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, SoundEvents.SPIDER_AMBIENT.getLocation()),
	CHICKEN(EntityType.CHICKEN, SoundEvents.CHICKEN_AMBIENT.getLocation()),
	COW(EntityType.COW, SoundEvents.COW_AMBIENT.getLocation()),
	DOLPHIN(EntityType.DOLPHIN, SoundEvents.DOLPHIN_AMBIENT.getLocation()),
	DONKEY(EntityType.DONKEY, SoundEvents.DONKEY_AMBIENT.getLocation()),
	DROWNED(EntityType.DROWNED, SoundEvents.DROWNED_AMBIENT.getLocation()),
	ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, SoundEvents.ELDER_GUARDIAN_AMBIENT.getLocation()),
	ENDERMAN(EntityType.ENDERMAN, SoundEvents.ENDERMAN_AMBIENT.getLocation()),
	ENDERMITE(EntityType.ENDERMITE, SoundEvents.ENDERMITE_AMBIENT.getLocation()),
	EVOKER(EntityType.EVOKER, SoundEvents.EVOKER_AMBIENT.getLocation()),
	FOX(EntityType.FOX, SoundEvents.FOX_AMBIENT.getLocation()),
	FOX_ARCTIC(EntityType.FOX, SoundEvents.FOX_AMBIENT.getLocation()),
	FROG_COLD(EntityType.FROG, SoundEvents.FROG_AMBIENT.getLocation()),
	FROG_TEMPERATE(EntityType.FROG, SoundEvents.FROG_AMBIENT.getLocation()),
	FROG_WARM(EntityType.FROG, SoundEvents.FROG_AMBIENT.getLocation()),
	GHAST(EntityType.GHAST, SoundEvents.GHAST_AMBIENT.getLocation()),
	GLOW_SQUID(EntityType.GLOW_SQUID, SoundEvents.GLOW_SQUID_AMBIENT.getLocation()),
	GOAT(EntityType.GOAT, SoundEvents.GOAT_AMBIENT.getLocation()),
	GUARDIAN(EntityType.GUARDIAN, SoundEvents.GUARDIAN_AMBIENT.getLocation()),
	HOGLIN(EntityType.HOGLIN, SoundEvents.HOGLIN_AMBIENT.getLocation()),
	HORSE(EntityType.HORSE, SoundEvents.HORSE_AMBIENT.getLocation()),
	HUSK(EntityType.HUSK, SoundEvents.HUSK_AMBIENT.getLocation()),
	ILLUSIONER(EntityType.ILLUSIONER, SoundEvents.ILLUSIONER_AMBIENT.getLocation()),
	IRON_GOLEM(EntityType.IRON_GOLEM, SoundEvents.IRON_GOLEM_STEP.getLocation()),
	LLAMA(EntityType.LLAMA, SoundEvents.LLAMA_AMBIENT.getLocation()),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, SoundEvents.MAGMA_CUBE_SQUISH.getLocation()),
	MOOSHROOM_BROWN(EntityType.MOOSHROOM, SoundEvents.COW_AMBIENT.getLocation()),
	MOOSHROOM_RED(EntityType.MOOSHROOM, SoundEvents.COW_AMBIENT.getLocation()),
	MULE(EntityType.MULE, SoundEvents.MULE_AMBIENT.getLocation()),
	OCELOT(EntityType.OCELOT, SoundEvents.OCELOT_AMBIENT.getLocation()),
	PANDA(EntityType.PANDA, SoundEvents.PANDA_AMBIENT.getLocation()),
	PARROT_BLUE(EntityType.PARROT, SoundEvents.PARROT_AMBIENT.getLocation()),
	PARROT_CYAN(EntityType.PARROT, SoundEvents.PARROT_AMBIENT.getLocation()),
	PARROT_GRAY(EntityType.PARROT, SoundEvents.PARROT_AMBIENT.getLocation()),
	PARROT_GREEN(EntityType.PARROT, SoundEvents.PARROT_AMBIENT.getLocation()),
	PARROT_RED(EntityType.PARROT, SoundEvents.PARROT_AMBIENT.getLocation()),
	PHANTOM(EntityType.PHANTOM, SoundEvents.PHANTOM_AMBIENT.getLocation()),
	PIG(EntityType.PIG, SoundEvents.PIG_AMBIENT.getLocation()),
	PILLAGER(EntityType.PILLAGER, SoundEvents.PILLAGER_AMBIENT.getLocation()),
	POLAR_BEAR(EntityType.POLAR_BEAR, SoundEvents.POLAR_BEAR_AMBIENT.getLocation()),
	PUFFERFISH(EntityType.PUFFERFISH, SoundEvents.PUFFER_FISH_AMBIENT.getLocation()),
	RABBIT(EntityType.RABBIT, SoundEvents.RABBIT_AMBIENT.getLocation()),
	RAVAGER(EntityType.RAVAGER, SoundEvents.RAVAGER_AMBIENT.getLocation()),
	SALMON(EntityType.SALMON, SoundEvents.SALMON_AMBIENT.getLocation()),
	SHEEP(EntityType.SHEEP, SoundEvents.SHEEP_AMBIENT.getLocation()),
	SHULKER(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_BLACK(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_BLUE(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_BROWN(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_CYAN(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_GRAY(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_GREEN(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_LIGHT_BLUE(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_LIGHT_GRAY(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_LIME(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_MAGENTA(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_ORANGE(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_PINK(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_PURPLE(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_RED(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_WHITE(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SHULKER_YELLOW(EntityType.SHULKER, SoundEvents.SHULKER_AMBIENT.getLocation()),
	SILVERFISH(EntityType.SILVERFISH, SoundEvents.SILVERFISH_AMBIENT.getLocation()),
	SKELETON_HORSE(EntityType.SKELETON_HORSE, SoundEvents.SKELETON_HORSE_AMBIENT.getLocation()),
	SLIME(EntityType.SLIME, SoundEvents.SLIME_SQUISH.getLocation()),
	SNIFFER(EntityType.SNIFFER, SoundEvents.SNIFFER_IDLE.getLocation()),
	SNOW_GOLEM(EntityType.SNOW_GOLEM, SoundEvents.SNOW_GOLEM_AMBIENT.getLocation()),
	SPIDER(EntityType.SPIDER, SoundEvents.SPIDER_AMBIENT.getLocation()),
	SQUID(EntityType.SQUID, SoundEvents.SQUID_AMBIENT.getLocation()),
	STRAY(EntityType.STRAY, SoundEvents.STRAY_AMBIENT.getLocation()),
	STRIDER(EntityType.STRIDER, SoundEvents.STRIDER_AMBIENT.getLocation()),
	TADPOLE(EntityType.TADPOLE, SoundEvents.TADPOLE_FLOP.getLocation()),
	TROPICAL_FISH(EntityType.TROPICAL_FISH, SoundEvents.TROPICAL_FISH_AMBIENT.getLocation()),
	TURTLE(EntityType.TURTLE, SoundEvents.TURTLE_AMBIENT_LAND.getLocation()),
	VEX(EntityType.VEX, SoundEvents.VEX_AMBIENT.getLocation()),
	VILLAGER(EntityType.VILLAGER, SoundEvents.VILLAGER_AMBIENT.getLocation()),
	VINDICATOR(EntityType.VINDICATOR, SoundEvents.VINDICATOR_AMBIENT.getLocation()),
	WANDERING_TRADER(EntityType.WANDERING_TRADER, SoundEvents.WANDERING_TRADER_AMBIENT.getLocation()),
	WARDEN(EntityType.WARDEN, SoundEvents.WARDEN_AMBIENT.getLocation()),
	WITCH(EntityType.WITCH, SoundEvents.WITCH_AMBIENT.getLocation()),
	WITHER(EntityType.WITHER, SoundEvents.WITHER_AMBIENT.getLocation()),
	WOLF(EntityType.WOLF, SoundEvents.WOLF_AMBIENT.getLocation()),
	ZOGLIN(EntityType.ZOGLIN, SoundEvents.ZOGLIN_AMBIENT.getLocation()),
	ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, SoundEvents.ZOMBIE_HORSE_AMBIENT.getLocation()),
	ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, SoundEvents.ZOMBIE_VILLAGER_AMBIENT.getLocation()),
	ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT.getLocation()),
	ARMADILLO(EntityType.ARMADILLO, SoundEvents.ARMADILLO_AMBIENT.getLocation()),
	BREEZE(EntityType.BREEZE, SoundEvents.BREEZE_IDLE_GROUND.getLocation()),
	BOGGED(EntityType.BOGGED, SoundEvents.BOGGED_AMBIENT.getLocation()),
	PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, SoundEvents.PIGLIN_BRUTE_AMBIENT.getLocation()),

    // Spectrum
	EGG_LAYING_WOOLY_PIG(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, SoundEvents.PIG_AMBIENT.getLocation()),
	ERASER(SpectrumEntityTypes.ERASER, SoundEvents.SPIDER_AMBIENT.getLocation()),
	KINDLING(SpectrumEntityTypes.KINDLING, SpectrumSoundEvents.ENTITY_KINDLING_AMBIENT.getLocation()),
	LIZARD_BLACK(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_BLUE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_BROWN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_CYAN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_GRAY(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_GREEN(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_LIGHT_BLUE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_LIGHT_GRAY(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_LIME(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_MAGENTA(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_ORANGE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_PINK(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_PURPLE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_RED(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_WHITE(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	LIZARD_YELLOW(SpectrumEntityTypes.LIZARD, SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT.getLocation()),
	MONSTROSITY(SpectrumEntityTypes.MONSTROSITY, SpectrumSoundEvents.ENTITY_MONSTROSITY_AMBIENT.getLocation()),
	PRESERVATION_TURRET(SpectrumEntityTypes.PRESERVATION_TURRET, SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_AMBIENT.getLocation());
	
	public static final Codec<SpectrumSkullType> CODEC = StringRepresentable.fromEnum(SpectrumSkullType::values);
	
	private final EntityType<?> entityType;
	private final ResourceLocation noteBlockSound;
	
	SpectrumSkullType(EntityType<?> entityType, ResourceLocation noteBlockSound) {
		this.entityType = entityType;
		this.noteBlockSound = noteBlockSound;
	}
	
	public EntityType<?> getEntityType() {
		return this.entityType;
	}
	
	public ResourceLocation getNoteBlockSound() {
		return this.noteBlockSound;
	}
	
	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
	
}
