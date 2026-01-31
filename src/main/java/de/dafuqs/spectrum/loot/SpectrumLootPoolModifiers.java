package de.dafuqs.spectrum.loot;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.loot.functions.*;
import de.dafuqs.spectrum.loot.loot_modifiers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.loot.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumLootPoolModifiers {
	
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register("sniffer_digging_additions", () -> SnifferDiggingAdditionsModifier.CODEC);
		REGISTRAR.register("treasure_hunter", () -> TreasureHunterModifier.CODEC);
		
		REGISTRAR.register(modBus);
	}
	
}
