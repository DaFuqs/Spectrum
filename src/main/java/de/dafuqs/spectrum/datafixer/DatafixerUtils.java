package de.dafuqs.spectrum.datafixer;

import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.api.energy.color.InkColor;

import java.util.List;
import java.util.Optional;

public class DatafixerUtils {
    private static final List<String> INK_COLOR_IDS = List.of(
            "black",
            "blue",
            "brown",
            "cyan",
            "gray",
            "green",
            "light_blue",
            "light_gray",
            "lime",
            "magenta",
            "orange",
            "pink",
            "purple",
            "red",
            "white",
            "yellow"
    );

    public static Dynamic<?> processSingle(Dynamic<?> dynamic, Dynamic<?> dynamicColor) {
        Optional<String> optionalColorId = dynamicColor.asString().result();
        if (optionalColorId.isPresent()) {
            Optional<InkColor> optionalColor = InkColor.ofIdString("spectrum:"+optionalColorId.get());
            if (optionalColor.isPresent()) {
                InkColor color = optionalColor.get();
                return dynamic.remove("Color").set("Color", dynamic.createString(color.getID().toString()));
            }
        }

        return null;
    }

    public static Dynamic<?> processMultiple(Dynamic<?> dynamic) {
        boolean changed = false;
        Dynamic<?> processed = dynamic;

        for (String id : INK_COLOR_IDS) {
            Optional<? extends Dynamic<?>> optionalDynamicColor = dynamic.get(id).result();
            if (optionalDynamicColor.isPresent()) {
                Dynamic<?> dynamicColor = optionalDynamicColor.get();
                Optional<String> optionalColorId = dynamicColor.asString().result();
                if (optionalColorId.isPresent()) {
                    Optional<InkColor> optionalColor = InkColor.ofIdString("spectrum:"+optionalColorId.get());
                    if (optionalColor.isPresent()) {
                        InkColor color = optionalColor.get();
                        long amount = dynamicColor.asLong(0);

                        processed = processed.remove(id);
                        if (amount != 0L) {
                            processed = processed.set(color.getID().toString(), processed.createLong(amount));
                        }

                        if (!changed) {
                            changed = true;
                        }
                    }
                }
            }
        }

        return changed ? processed : null;
    }
}
