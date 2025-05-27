package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.*;

import java.util.function.*;

public class SpectrumLootContextTypes {
	
	public static final LootContextType FISHING = register("fishing", (builder) -> {
		builder
				.require(LootContextParameters.ORIGIN) // the pos of the fishing bobber
				.require(LootContextParameters.TOOL) // the fishing rod
				.allow(LootContextParameters.THIS_ENTITY) // the fishing bobber
				.allow(LootContextParameters.DIRECT_ATTACKING_ENTITY); // the player that is fishing
	});
	
	private static LootContextType register(String name, Consumer<LootContextType.Builder> type) {
		LootContextType.Builder builder = new LootContextType.Builder();
		type.accept(builder);
		LootContextType lootContextType = builder.build();
		Identifier identifier = SpectrumCommon.locate(name);
		LootContextType lootContextType2 = LootContextTypes.MAP.put(identifier, lootContextType);
		if (lootContextType2 != null) {
			throw new IllegalStateException("Loot table parameter set " + identifier + " is already registered");
		} else {
			return lootContextType;
		}
	}
	
	public static void register() {
	
	}
	
}
