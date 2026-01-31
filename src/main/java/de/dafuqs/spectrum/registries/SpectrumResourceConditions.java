package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.conditions.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;

public class SpectrumResourceConditions {
	
	public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register("integration_pack_active", () -> IntegrationPackActiveResourceCondition.CODEC);
		
		REGISTRAR.register(modBus);
	}
	
	public record IntegrationPackActiveResourceCondition(String integrationPack) implements ICondition {
		
		public static MapCodec<IntegrationPackActiveResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf("integration_pack").forGetter(IntegrationPackActiveResourceCondition::integrationPack)
		).apply(instance, IntegrationPackActiveResourceCondition::new));
		
		@Override
		public MapCodec<? extends ICondition> codec() {
			return CODEC;
		}
		
		@Override
		public boolean test(IContext iContext) {
			return SpectrumIntegrationPacks.isIntegrationPackActive(integrationPack);
		}
	}
	
}
