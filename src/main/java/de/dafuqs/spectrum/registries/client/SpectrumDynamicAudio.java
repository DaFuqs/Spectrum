package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.sound.music.SpectrumAudioManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;

import java.util.function.Predicate;

import static de.dafuqs.spectrum.registries.SpectrumSoundEvents.*;

@Environment(EnvType.CLIENT)
public class SpectrumDynamicAudio {

    public static void init() {
        register(TEST_MUSIC, minecraftClient -> true);
    }

    private static void register(SoundEvent event, Predicate<MinecraftClient> predicate) {
        SpectrumAudioManager.getInstance().registerEvent(event, predicate);
    }
}
