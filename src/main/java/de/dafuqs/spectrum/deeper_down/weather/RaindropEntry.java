package de.dafuqs.spectrum.deeper_down.weather;

import net.minecraft.util.collection.Weighted;

public class RaindropEntry extends Weighted.Absent {

    private final RaindropType type;

    public RaindropEntry(RaindropType type, int weight) {
        super(weight);
        this.type = type;
    }

    public RaindropType getRaindropType() {
        return type;
    }
}
