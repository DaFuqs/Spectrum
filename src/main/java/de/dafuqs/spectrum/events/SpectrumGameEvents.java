package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class SpectrumGameEvents {

    public static GameEvent ITEM_TRANSFER;
    public static List<GameEvent> WIRELESS_REDSTONE_SIGNALS = new ArrayList<>(); // a list of 16 events, meaning redstone strength 0-15

    public static void register() {
        ITEM_TRANSFER = register("item_spawned");

        for(int i = 0; i < 16; i++) {
            WIRELESS_REDSTONE_SIGNALS.add(register("wireless_redstone_signal_"+i));
        }
    }

    private static GameEvent register(String id) {
        return register(id, 16);
    }

    private static GameEvent register(String id, int range) {
        return  Registry.register(Registry.GAME_EVENT, new Identifier(SpectrumCommon.MOD_ID, id), new GameEvent(id, range));
    }

}