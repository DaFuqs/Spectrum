package de.dafuqs.spectrum.sound;

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

    public static BlockSoundGroup AMMOLITE_BLOCK;
    public static BlockSoundGroup WAND_LIGHT;

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

        AMMOLITE_BLOCK = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.BLOCK_AMMOLITE_BLOCK_BREAK, SpectrumSoundEvents.BLOCK_AMMOLITE_BLOCK_STEP, SpectrumSoundEvents.BLOCK_AMMOLITE_BLOCK_PLACE, SpectrumSoundEvents.BLOCK_AMMOLITE_BLOCK_HIT, SpectrumSoundEvents.BLOCK_AMMOLITE_BLOCK_FALL);
        WAND_LIGHT = new BlockSoundGroup(1.0F, 1.0F, SpectrumSoundEvents.LIGHT_STAFF_BREAK, SoundEvents.BLOCK_AMETHYST_CLUSTER_STEP, SpectrumSoundEvents.LIGHT_STAFF_PLACE, SpectrumSoundEvents.LIGHT_STAFF_BREAK, SpectrumSoundEvents.LIGHT_STAFF_BREAK);
    }

}
