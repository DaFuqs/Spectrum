package de.dafuqs.pigment.blocks;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PigmentBlockTags {

    public static Tag<Block> DECAY;
    public static Tag<Block> DECAY_MAGICAL_LEAVES;
    public static Tag<Block> DECAY2_SAFE;
    public static Tag<Block> DECAY_OBSIDIAN_CONVERSIONS;
    public static Tag<Block> DECAY_CRYING_OBSIDIAN_CONVERSIONS;
    public static Tag<Block> DECAY3_SAFE;
    public static Tag<Block> DECAY_BEDROCK_CONVERSIONS;

    private static Tag<Block> register(String id) {
        return TagRegistry.block(new Identifier(PigmentCommon.MOD_ID, id));
    }

    public static void register() {
        DECAY = register("decay");
        DECAY2_SAFE = register("decay2_safe");
        DECAY_MAGICAL_LEAVES = register("magical_leaves");
        DECAY_OBSIDIAN_CONVERSIONS = register("decay_obsidian_conversions");
        DECAY_CRYING_OBSIDIAN_CONVERSIONS = register("decay_crying_obsidian_conversions");
        DECAY3_SAFE = register("decay3_safe");
        DECAY_BEDROCK_CONVERSIONS = register("decay_bedrock_conversions");
    }
}
