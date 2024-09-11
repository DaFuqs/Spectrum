package de.dafuqs.spectrum.datafixer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.energy.color.InkColor;
import de.dafuqs.spectrum.datafixer.fix.InkStorageBlockFix;
import de.dafuqs.spectrum.datafixer.fix.InkStorageItemFix;
import de.dafuqs.spectrum.datafixer.quilt_dfu.api.QuiltDataFixes;
import de.dafuqs.spectrum.datafixer.quilt_dfu.impl.ServerFreezer;
import de.dafuqs.spectrum.datafixer.quilt_dfu.impl.client.ClientFreezer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpectrumDataFixers {
    public static final int DATA_FIXER_VERSION = 1;

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

    public static void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(new ClientFreezer());
        ServerLifecycleEvents.SERVER_STARTING.register(new ServerFreezer());

        DataFixerBuilder builder = new DataFixerBuilder(DATA_FIXER_VERSION);
        addFixers(builder);

        ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Spectrum DataFixer Bootstrap").setDaemon(true).setPriority(1).build());
        QuiltDataFixes.registerFixer(SpectrumCommon.MOD_ID, DATA_FIXER_VERSION, builder.buildOptimized(SharedConstants.requiredDataFixTypes, executor));
    }

    private static void addFixers(DataFixerBuilder builder) {
        builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);

        Schema schemaV1 = builder.addSchema(1, IdentifierNormalizingSchema::new);
        builder.addFixer(new InkStorageBlockFix(schemaV1));
        builder.addFixer(new InkStorageItemFix(schemaV1));
    }

    public static Dynamic<?> processSingle(Dynamic<?> dynamic, Dynamic<?> dynamicColor) {
        Optional<String> optionalColorId = dynamicColor.asString().result();
        if (optionalColorId.isPresent()) {
            Optional<InkColor> optionalColor = InkColor.ofIdString(SpectrumCommon.MOD_ID+":"+optionalColorId.get());
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
                    Optional<InkColor> optionalColor = InkColor.ofIdString(SpectrumCommon.MOD_ID+":"+optionalColorId.get());
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
