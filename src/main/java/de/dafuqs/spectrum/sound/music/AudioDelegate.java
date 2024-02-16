package de.dafuqs.spectrum.sound.music;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;

public class AudioDelegate extends AbstractSoundInstance implements TickableSoundInstance {

    //private final StreamMimic mimic;


    protected AudioDelegate(ControlledAudioInstance parent) {
        super(parent.getId(), parent.getCategory(), parent.getRandom());
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void tick() {

    }
}
