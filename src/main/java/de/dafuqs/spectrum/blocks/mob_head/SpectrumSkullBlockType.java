package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;

public enum SpectrumSkullBlockType implements SkullBlock.SkullType {
    // Vanilla
    ALLAY(EntityType.ALLAY),
    AXOLOTL_BLUE(EntityType.AXOLOTL),
    AXOLOTL_WILD(EntityType.AXOLOTL),
    AXOLOTL_CYAN(EntityType.AXOLOTL),
    AXOLOTL_GOLD(EntityType.AXOLOTL),
    AXOLOTL_LEUCISTIC(EntityType.AXOLOTL),
    BAT(EntityType.BAT),
    BEE(EntityType.BEE),
    BLAZE(EntityType.BLAZE),
    CAT(EntityType.CAT),
    CAVE_SPIDER(EntityType.CAVE_SPIDER),
    CHICKEN(EntityType.CHICKEN),
    CLOWNFISH(EntityType.TROPICAL_FISH),
    COW(EntityType.COW),
    DONKEY(EntityType.DONKEY),
    DROWNED(EntityType.DROWNED),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN),
    ENDERMAN(EntityType.ENDERMAN),
    ENDERMITE(EntityType.ENDERMITE),
    EVOKER(EntityType.EVOKER),
    FOX(EntityType.FOX),
    FOX_ARCTIC(EntityType.FOX),
    FROG_COLD(EntityType.FROG),
    FROG_TEMPERATE(EntityType.FROG),
    FROG_WARM(EntityType.FROG),
    GHAST(EntityType.GHAST),
    GLOW_SQUID(EntityType.GLOW_SQUID),
    GOAT(EntityType.GOAT),
    GUARDIAN(EntityType.GUARDIAN),
    HOGLIN(EntityType.HOGLIN),
    HORSE(EntityType.HORSE),
    HUSK(EntityType.HUSK),
    ILLUSIONER(EntityType.ILLUSIONER),
    IRON_GOLEM(EntityType.IRON_GOLEM),
    LLAMA(EntityType.LLAMA),
    MAGMA_CUBE(EntityType.MAGMA_CUBE),
    MOOSHROOM_BROWN(EntityType.MOOSHROOM),
    MOOSHROOM_RED(EntityType.MOOSHROOM),
    MULE(EntityType.MULE),
    OCELOT(EntityType.OCELOT),
    PANDA(EntityType.PANDA),
    PARROT_BLUE(EntityType.PARROT),
    PARROT_CYAN(EntityType.PARROT),
    PARROT_GRAY(EntityType.PARROT),
    PARROT_GREEN(EntityType.PARROT),
    PARROT_RED(EntityType.PARROT),
    PHANTOM(EntityType.PHANTOM),
    PIG(EntityType.PIG),
    POLAR_BEAR(EntityType.POLAR_BEAR),
    PUFFERFISH(EntityType.PUFFERFISH),
    RABBIT(EntityType.RABBIT),
    RAVAGER(EntityType.RAVAGER),
    SALMON(EntityType.SALMON),
    SHEEP(EntityType.SHEEP),
    SHULKER(EntityType.SHULKER),
    SHULKER_BLACK(EntityType.SHULKER),
    SHULKER_BLUE(EntityType.SHULKER),
    SHULKER_BROWN(EntityType.SHULKER),
    SHULKER_CYAN(EntityType.SHULKER),
    SHULKER_GRAY(EntityType.SHULKER),
    SHULKER_GREEN(EntityType.SHULKER),
    SHULKER_LIGHT_BLUE(EntityType.SHULKER),
    SHULKER_LIGHT_GRAY(EntityType.SHULKER),
    SHULKER_LIME(EntityType.SHULKER),
    SHULKER_MAGENTA(EntityType.SHULKER),
    SHULKER_ORANGE(EntityType.SHULKER),
    SHULKER_PINK(EntityType.SHULKER),
    SHULKER_PURPLE(EntityType.SHULKER),
    SHULKER_RED(EntityType.SHULKER),
    SHULKER_WHITE(EntityType.SHULKER),
    SHULKER_YELLOW(EntityType.SHULKER),
    SILVERFISH(EntityType.SILVERFISH),
    SLIME(EntityType.SLIME),
    SNOW_GOLEM(EntityType.SNOW_GOLEM),
    SPIDER(EntityType.SPIDER),
    SQUID(EntityType.SQUID),
    STRAY(EntityType.STRAY),
    STRIDER(EntityType.STRIDER),
    TADPOLE(EntityType.TADPOLE),
    TURTLE(EntityType.TURTLE),
    VEX(EntityType.VEX),
    VILLAGER(EntityType.VILLAGER),
    VINDICATOR(EntityType.VINDICATOR),
    WANDERING_TRADER(EntityType.WANDERING_TRADER),
    WARDEN(EntityType.WARDEN),
    WITCH(EntityType.WITCH),
    WITHER(EntityType.WITHER),
    WOLF(EntityType.WOLF),
    ZOGLIN(EntityType.ZOGLIN),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER),
    ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN),

    // Spectrum
    EGG_LAYING_WOOLY_PIG(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG),
    KINDLING(SpectrumEntityTypes.KINDLING),
	PRESERVATION_TURRET(SpectrumEntityTypes.PRESERVATION_TURRET),
    MONSTROSITY(SpectrumEntityTypes.MONSTROSITY),
    LIZARD(SpectrumEntityTypes.LIZARD),
	ERASER(SpectrumEntityTypes.ERASER);

    private final EntityType<?> entityType;

    SpectrumSkullBlockType(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }

}
