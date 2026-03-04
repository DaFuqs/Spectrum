package de.dafuqs.spectrum.loot;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.loot.functions.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumLootFunctionTypes {
	
	private static final DeferredRegister<LootItemFunctionType<?>> REGISTRAR = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SpectrumCommon.MOD_ID);
	
	public static final LootItemFunctionType<DyeRandomlyLootFunction> DYE_RANDOMLY = register("dye_randomly", DyeRandomlyLootFunction.CODEC);
	public static final LootItemFunctionType<FermentRandomlyLootFunction> FERMENT_RANDOMLY = register("ferment_randomly", FermentRandomlyLootFunction.CODEC);
	public static final LootItemFunctionType<SetComponentsRandomlyLootFunction> SET_COMPONENTS_RANDOMLY = register("set_components_randomly", SetComponentsRandomlyLootFunction.CODEC);
	public static final LootItemFunctionType<FillPotionFillableLootFunction> FILL_POTION_FILLABLE = register("fill_potion_fillable", FillPotionFillableLootFunction.CODEC);
	public static final LootItemFunctionType<GrantAdvancementLootFunction> GRANT_ADVANCEMENT = register("grant_advancement", GrantAdvancementLootFunction.CODEC);
	
	private static <T extends LootItemFunction> LootItemFunctionType<T> register(String id, MapCodec<T> codec) {
		LootItemFunctionType<T> type = new LootItemFunctionType<>(codec);
		REGISTRAR.register(id, () -> type);
		return type;
	}
	
	public static void register(IEventBus bus) {
		REGISTRAR.register(bus);
	}
	
}
