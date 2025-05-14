package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.storage.loot.parameters.*;

import java.util.function.*;

public class SpectrumLootContextTypes {
	
	public static final LootContextParamSet FISHING = register("fishing", (builder) -> {
		builder
				.required(LootContextParams.ORIGIN) // the pos of the fishing bobber
				.required(LootContextParams.TOOL) // the fishing rod
				.optional(LootContextParams.THIS_ENTITY) // the fishing bobber
				.optional(LootContextParams.DIRECT_ATTACKING_ENTITY); // the player that is fishing
	});
	
	private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> type) {
		LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
		type.accept(builder);
		LootContextParamSet lootContextType = builder.build();
		ResourceLocation identifier = SpectrumCommon.locate(name);
		LootContextParamSet lootContextType2 = LootContextParamSets.REGISTRY.put(identifier, lootContextType);
		if (lootContextType2 != null) {
			throw new IllegalStateException("Loot table parameter set " + identifier + " is already registered");
		} else {
			return lootContextType;
		}
	}
	
	public static void register() {
	
	}
	
}
