package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public class SpectrumStampDataCategories {

    public static StampDataCategory UNIQUE = register(StampDataCategory.UNIQUE, StampDataCategory.UNIQUE.getId());
    public static StampDataCategory PASTEL = register("pastel");

    public static void register() {}

    private static StampDataCategory register(String name) {
        var id = SpectrumCommon.locate(name);
        return register(StampDataCategory.create(id), id);
    }

    private static StampDataCategory register(StampDataCategory category, Identifier id) {
        return Registry.register(SpectrumRegistries.STAMP_DATA_CATEGORY, id, category);
    }
}
