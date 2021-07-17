package de.dafuqs.pigment.sound;

import net.minecraft.sound.BlockSoundGroup;

public class PigmentBlockSoundGroups {

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

    public static BlockSoundGroup RAINBOW_MOONSTONE_BLOCK;

    public static void register() {
        CITRINE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_BREAK, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_STEP, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_PLACE, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_HIT, PigmentSoundEvents.BLOCK_CITRINE_BLOCK_FALL);
        CITRINE_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_BREAK, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_PLACE, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
        SMALL_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_SMALL_CITRINE_BUD_BREAK, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_SMALL_CITRINE_BUD_PLACE, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
        MEDIUM_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MEDIUM_CITRINE_BUD_BREAK, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_MEDIUM_CITRINE_BUD_PLACE, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
        LARGE_CITRINE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_LARGE_CITRINE_BUD_BREAK, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_LARGE_CITRINE_BUD_PLACE, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_CITRINE_CLUSTER_FALL);
    
        TOPAZ_BLOCK = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_BREAK, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_STEP, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_PLACE, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, PigmentSoundEvents.BLOCK_TOPAZ_BLOCK_FALL);
        TOPAZ_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_BREAK, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_PLACE, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
        SMALL_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_SMALL_TOPAZ_BUD_BREAK, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, PigmentSoundEvents.BLOCK_SMALL_TOPAZ_BUD_PLACE, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
        MEDIUM_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MEDIUM_TOPAZ_BUD_BREAK, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, PigmentSoundEvents.BLOCK_MEDIUM_TOPAZ_BUD_PLACE, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
        LARGE_TOPAZ_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_LARGE_TOPAZ_BUD_BREAK, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_STEP, PigmentSoundEvents.BLOCK_LARGE_TOPAZ_BUD_PLACE, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_HIT, PigmentSoundEvents.BLOCK_TOPAZ_CLUSTER_FALL);
    
        ONYX_BLOCK = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_ONYX_BLOCK_BREAK, PigmentSoundEvents.BLOCK_ONYX_BLOCK_STEP, PigmentSoundEvents.BLOCK_ONYX_BLOCK_PLACE, PigmentSoundEvents.BLOCK_ONYX_BLOCK_HIT, PigmentSoundEvents.BLOCK_ONYX_BLOCK_FALL);
        ONYX_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_BREAK, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_STEP, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_PLACE, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_HIT, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
        SMALL_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_SMALL_ONYX_BUD_BREAK, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_STEP, PigmentSoundEvents.BLOCK_SMALL_ONYX_BUD_PLACE, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_HIT, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
        MEDIUM_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MEDIUM_ONYX_BUD_BREAK, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_STEP, PigmentSoundEvents.BLOCK_MEDIUM_ONYX_BUD_PLACE, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_HIT, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
        LARGE_ONYX_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_LARGE_ONYX_BUD_BREAK, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_STEP, PigmentSoundEvents.BLOCK_LARGE_ONYX_BUD_PLACE, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_HIT, PigmentSoundEvents.BLOCK_ONYX_CLUSTER_FALL);
    
        MOONSTONE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_BREAK, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_STEP, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_PLACE, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_BLOCK_FALL);
        MOONSTONE_CLUSTER = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_PLACE, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
        SMALL_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_SMALL_MOONSTONE_BUD_BREAK, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_SMALL_MOONSTONE_BUD_PLACE, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
        MEDIUM_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_MEDIUM_MOONSTONE_BUD_BREAK, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_MEDIUM_MOONSTONE_BUD_PLACE, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);
        LARGE_MOONSTONE_BUD = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_LARGE_MOONSTONE_BUD_BREAK, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_STEP, PigmentSoundEvents.BLOCK_LARGE_MOONSTONE_BUD_PLACE, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_HIT, PigmentSoundEvents.BLOCK_MOONSTONE_CLUSTER_FALL);

        RAINBOW_MOONSTONE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_BREAK, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_STEP, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_PLACE, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_HIT, PigmentSoundEvents.BLOCK_RAINBOW_MOONSTONE_BLOCK_FALL);

    }

}
