package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;

public class StampDataCategory {

    public static final StampDataCategory UNIQUE = new StampDataCategory(SpectrumCommon.locate("unique"), true);
    private final Identifier id;
    private final boolean unique;

    private StampDataCategory(Identifier id, boolean unique) {
        this.id = id;
        this.unique = unique;
    }

    public static StampDataCategory create(Identifier id) {
        return new StampDataCategory(id, false);
    }

    public Identifier getId() {
        return id;
    }

    public boolean isUnique() {
        return unique;
    }
}
