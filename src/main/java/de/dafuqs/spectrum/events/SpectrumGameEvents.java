package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.gameevent.*;

public class SpectrumGameEvents {
	
	public static Holder<GameEvent> ENTITY_SPAWNED;
	public static Holder<GameEvent> BLOCK_CHANGED;
	
	public static Holder<GameEvent> HUMMINGSTONE_HUMMING;
	public static Holder<GameEvent> HUMMINGSTONE_HYMN;
	
	public static Holder<GameEvent> WIRELESS_REDSTONE_SIGNAL;
	
	public static void register() {
		ENTITY_SPAWNED = register("entity_spawned", 16);
		BLOCK_CHANGED = register("block_changed", 16);
		
		HUMMINGSTONE_HUMMING = register("hummingstone_humming", 16);
		HUMMINGSTONE_HYMN = register("hummingstone_hymn", 16);
		
		WIRELESS_REDSTONE_SIGNAL = register("wireless_redstone_signal", 16);
	}
	
	private static Holder<GameEvent> register(String id, int range) {
		return Registry.registerForHolder(BuiltInRegistries.GAME_EVENT, SpectrumCommon.locate(id), new GameEvent(range));
	}
	
}