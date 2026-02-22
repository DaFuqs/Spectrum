package de.dafuqs.spectrum.render;

import de.dafuqs.spectrum.*;
import net.minecraft.resources.*;

import java.util.*;

public class WorthinessChecker {
	
	public enum CapeType {
		LUCKY_STARS("lucky_stars"),
		LUNAR("lunar"),
		LUNARIAN("lunarian"),
		PALE_ASTRONOMY("pale_astronomy"),
		UNDERGROUND_ASTRONOMY("underground_astronomy"),
		V1("v1");
		
		public final ResourceLocation capePath;
		
		CapeType(String name) {
			this.capePath = SpectrumCommon.locate("textures/capes/%s.png".formatted(name));
		}
	}
	
	public static class Players {
		public static final UUID AZZY = UUID.fromString("f7957087-549e-4ca3-878e-48f36569dd3e");
		public static final UUID DAF = UUID.fromString("5010ad09-0229-4d70-8a2c-bc254821dcb3");
		public static final UUID KRAK = UUID.fromString("6105cb83-5d33-4e45-8adb-f24ee0085bf5");
		public static final UUID DRA = UUID.fromString("f962000a-ee12-40ea-abd5-e15f7492f039");
		public static final UUID OPL = UUID.fromString("f791d11d-5415-4c28-99e7-ac6a0b2fec28");
		public static final UUID MAYA = UUID.fromString("a1732122-e22e-4edf-883c-09673eb55de8");
		
		public static final UUID DEV = UUID.fromString("380df991-f603-344c-a090-369bad2a924a");
	}
	
	private static final HashMap<UUID, CapeType> PLAYER_MAP = new HashMap<>();
	
	public static Optional<CapeType> getCapeType(UUID uuid) {
		return Optional.ofNullable(PLAYER_MAP.get(uuid));
	}
	
	private static void putPlayer(UUID id, CapeType cape) {
		PLAYER_MAP.put(id, cape);
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
		
		putPlayer(Players.DEV, CapeType.V1);
	}
	
}
