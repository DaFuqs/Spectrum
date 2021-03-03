package de.dafuqs.pigment.blocks.head;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.SkullBlock;

public class PigmentSkullBlock extends SkullBlock {

    public PigmentSkullBlock(SkullType skullType, Settings settings) {
        super(skullType, settings);
    }

    public static enum Type implements SkullBlock.SkullType {
        CHICKEN,
        COW,
        DONKEY,
        FOX,
        HORSE,
        MOOSHROOM,
        MULE,
        OCELOT,
        PARROT,
        PIG,
        PIGLIN,
        POLAR_BEAR,
        RABBIT,
        SHEEP,
        SQUID,
        STRIDER,
        TURTLE,
        VILLAGER,

        BEE,
        CAVE_SPIDER,
        ENDERMAN,
        IRON_GOLEM,
        LLAMA,
        PANDA,
        SPIDER,
        WOLF,
        ZOMBIFIED_PIGLIN,

        BLAZE,
        DROWNED,
        ELDER_GUARDIAN,
        ENDERMITE,
        EVOKER,
        GHAST,
        GUARDIAN,
        HOGLIN,
        HUSK,
        MAGMA_CUBE,
        PHANTOM,
        SHULKER,
        SILVERFISH,
        SLIME,
        STRAY,
        ZOGLIN;

        private Type() {
        }
    }

}
