package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SpectrumGameEvents {
	
	public static GameEvent ENTITY_SPAWNED;
	public static GameEvent CRYSTAL_APOTHECARY_HARVESTABLE_GROWN;
	
	public static HashMap<DyeColor, List<RedstoneTransferGameEvent>> WIRELESS_REDSTONE_SIGNALS = new HashMap<>(); // a list of 16 * 16 events, meaning redstone strength 0-15 with each dye color
	
	public static void register() {
		ENTITY_SPAWNED = register("entity_spawned");
		CRYSTAL_APOTHECARY_HARVESTABLE_GROWN = register("crystal_apothecary_harvestable_grown");
		
		for (DyeColor dyeColor : DyeColor.values()) {
			List<RedstoneTransferGameEvent> list = new ArrayList<>();
			for (int power = 0; power < 16; power++) {
				list.add(Registry.register(Registry.GAME_EVENT, SpectrumCommon.locate("wireless_redstone_signal_" + dyeColor.name().toLowerCase(Locale.ROOT) + "_" + power), new RedstoneTransferGameEvent("wireless_redstone_signal_" + dyeColor.name().toLowerCase(Locale.ROOT) + "_" + power, 16, dyeColor, power)));
			}
			WIRELESS_REDSTONE_SIGNALS.put(dyeColor, list);
		}
	}
	
	private static GameEvent register(String id) {
		return register(id, 16);
	}
	
	private static GameEvent register(String id, int range) {
		return Registry.register(Registry.GAME_EVENT, SpectrumCommon.locate(id), new GameEvent(id, range));
	}
	
}