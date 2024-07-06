package de.dafuqs.spectrum.blocks.pastel_network.network;

public enum NodeRemovalReason {
    UNLOADED(false),
    BROKEN(true),
    DISCONNECT(true),
    DISABLED(false),
    MOVED(true);

    public final boolean destructive;

    NodeRemovalReason(boolean destructive) {
        this.destructive = destructive;
    }
}