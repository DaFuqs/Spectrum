package de.dafuqs.spectrum;

import java.util.UUID;

public interface PlayerOwned {

    public abstract void setPlayerUUID(UUID playerUUID);
    public abstract UUID getPlayerUUID();

}
