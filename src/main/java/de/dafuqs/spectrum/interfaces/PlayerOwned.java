package de.dafuqs.spectrum.interfaces;

import java.util.UUID;

public interface PlayerOwned {

    public abstract void setPlayerUUID(UUID playerUUID);
    public abstract UUID getPlayerUUID();

}
