package de.dafuqs.pigment.registries;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.stat.Stats.CUSTOM;

public class PigmentStats {

    /*
    public Identifier OPEN_MANUAL;

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    private static <T> StatType<T> registerType(String id, Registry<T> registry) {
        return (StatType)Registry.register(Registry.STAT_TYPE, id, new StatType(registry));
    }


    public static void register() {
        OPEN_MANUAL = registerType("used", Registry.ITEM);
    }
*/
}
