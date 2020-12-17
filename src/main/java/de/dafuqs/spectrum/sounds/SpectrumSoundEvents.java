package de.dafuqs.spectrum.sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumSoundEvents {

    private static final Identifier DECAY1_PLACED_ID = new Identifier("spectrum:decay1_placed");
    public static SoundEvent DECAY1_PLACED = new SoundEvent(DECAY1_PLACED_ID);

    private static final Identifier DECAY2_PLACED_ID = new Identifier("spectrum:decay2_placed");
    public static SoundEvent DECAY2_PLACED = new SoundEvent(DECAY2_PLACED_ID);

    private static final Identifier DECAY3_PLACED_ID = new Identifier("spectrum:decay3_placed");
    public static SoundEvent DECAY3_PLACED = new SoundEvent(DECAY3_PLACED_ID);

    private static final Identifier LIQUID_CRYSTAL_AMBIENT_ID = new Identifier("spectrum:liquid_crystal_ambient");
    public static SoundEvent LIQUID_CRYSTAL_AMBIENT = new SoundEvent(LIQUID_CRYSTAL_AMBIENT_ID);

    private static final Identifier ALTAR_USE_ID = new Identifier("spectrum:altar_use");
    public static SoundEvent ALTAR_USE = new SoundEvent(ALTAR_USE_ID);


    private static void register(Identifier identifier, SoundEvent soundEvent) {
        Registry.register(Registry.SOUND_EVENT, identifier, soundEvent);
    }

    public static void register() {
        register(DECAY1_PLACED_ID, DECAY1_PLACED);
        register(DECAY2_PLACED_ID, DECAY2_PLACED);
        register(DECAY3_PLACED_ID, DECAY3_PLACED);

        register(LIQUID_CRYSTAL_AMBIENT_ID, LIQUID_CRYSTAL_AMBIENT); // TODO: sound
        register(ALTAR_USE_ID, ALTAR_USE); // TODO: sound
    }

}
