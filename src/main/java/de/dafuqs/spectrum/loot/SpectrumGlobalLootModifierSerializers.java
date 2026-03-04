package de.dafuqs.spectrum.loot;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.loot.modifiers.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.loot.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumGlobalLootModifierSerializers {
	
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register("sniffer_digging_additions", () -> SnifferDiggingAdditionsLootModifier.CODEC);
		REGISTRAR.register("treasure_hunter", () -> TreasureHunterLootModifier.CODEC);
		
		REGISTRAR.register(modBus);
	}
	
}
