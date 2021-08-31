package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class SpectrumGameEvents {

    public static GameEvent ITEM_TRANSFER;

    public static void register() {
        ITEM_TRANSFER = register("item_spawned");
    }

    private static GameEvent register(String id) {
        return register(id, 16);
    }

    private static GameEvent register(String id, int range) {
        return  Registry.register(Registry.GAME_EVENT, new Identifier(SpectrumCommon.MOD_ID, id), new GameEvent(id, range));
    }

}
