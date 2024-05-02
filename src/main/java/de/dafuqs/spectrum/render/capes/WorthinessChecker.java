package de.dafuqs.spectrum.render.capes;

import java.util.*;

public class WorthinessChecker {

    private static final HashMap<UUID, Entry> PLAYER_MAP = new HashMap<>();

    public static CapeType getCapeType(UUID uuid) {
        return Optional.ofNullable(PLAYER_MAP.get(uuid)).map(entry -> entry.capeType).orElse(CapeType.NONE);
    }

    private static void putPlayer(UUID id, CapeType cape) {
        PLAYER_MAP.put(id, new Entry(id, cape));
    }

    public record Entry(UUID playerId, CapeType capeType) {
    }

    public static void init() {
    }

    static {
        // Spectrum Devs
        putPlayer(Players.AZZY, CapeType.LUNAR);
        putPlayer(Players.DAF, CapeType.UNDERGROUND_ASTRONOMY);

        // Spectrum contributors, supporters & raffle winners
        putPlayer(Players.KRAK, CapeType.LUCKY_STARS);
        putPlayer(Players.DRA, CapeType.PALE_ASTRONOMY);
        putPlayer(Players.OPL, CapeType.PALE_ASTRONOMY);
        putPlayer(Players.MAYA, CapeType.PALE_ASTRONOMY);

        // What was once Immortal Devs
        putPlayer(Players.PIE, CapeType.V1);
        putPlayer(Players.GUDY, CapeType.GUDY);
        putPlayer(Players.REO, CapeType.IMMORTAL);
        putPlayer(Players.SOLLY, CapeType.IMMORTAL);
        putPlayer(Players.ASH, CapeType.IMMORTAL);
        putPlayer(Players.KALUCKY, CapeType.IMMORTAL);
        putPlayer(Players.JACK, CapeType.IMMORTAL);
        putPlayer(Players.SUNSETTE, CapeType.IMMORTAL);
        putPlayer(Players.TWENTYFOUR, CapeType.IMMORTAL);
    }
}
