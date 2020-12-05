package de.dafuqs.spectrum.sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumSoundEvents {

    public static final Identifier DECAY1_PLACED_ID = new Identifier("spectrum:decay1_placed");
    public static SoundEvent DECAY1_PLACED_EVENT = new SoundEvent(DECAY1_PLACED_ID);

    public static final Identifier DECAY2_PLACED_ID = new Identifier("spectrum:decay2_placed");
    public static SoundEvent DECAY2_PLACED_EVENT = new SoundEvent(DECAY2_PLACED_ID);

    public static final Identifier DECAY3_PLACED_ID = new Identifier("spectrum:decay3_placed");
    public static SoundEvent DECAY3_PLACED_EVENT = new SoundEvent(DECAY3_PLACED_ID);


    private static void register(Identifier identifier, SoundEvent soundEvent) {
        Registry.register(Registry.SOUND_EVENT, identifier, soundEvent);
    }

    public static void register() {
        register(DECAY1_PLACED_ID, DECAY1_PLACED_EVENT);
        register(DECAY2_PLACED_ID, DECAY2_PLACED_EVENT);
        register(DECAY3_PLACED_ID, DECAY3_PLACED_EVENT);
    }

}
