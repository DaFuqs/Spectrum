package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.gameevent.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumGameEvents {
	
	private static final DeferredRegister<GameEvent> REGISTRAR = DeferredRegister.create(Registries.GAME_EVENT, SpectrumCommon.MOD_ID);
	
	public static Holder<GameEvent> ENTITY_SPAWNED = register("entity_spawned", 16);
	public static Holder<GameEvent> BLOCK_CHANGED = register("block_changed", 16);
	
	public static Holder<GameEvent> HUMMINGSTONE_HUMMING = register("hummingstone_humming", 16);
	public static Holder<GameEvent> HUMMINGSTONE_HYMN = register("hummingstone_hymn", 16);
	
	public static Holder<GameEvent> WIRELESS_REDSTONE_SIGNAL = register("wireless_redstone_signal", 16);
	
	private static Holder<GameEvent> register(String name, int range) {
		return REGISTRAR.register(name, () -> new GameEvent(range));
	}
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
}