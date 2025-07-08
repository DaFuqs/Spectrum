package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
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
		REGISTRAR.register("enchantments_exist", () -> EnchantmentsExistResourceCondition.CODEC);
		REGISTRAR.register("integration_pack_active", () -> IntegrationPackActiveResourceCondition.CODEC);
		
		REGISTRAR.register(modBus);
	}
	
	public record EnchantmentsExistResourceCondition(List<ResourceKey<Enchantment>> enchantments) implements ICondition {
		
		public static MapCodec<EnchantmentsExistResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ResourceKey.codec(Registries.ENCHANTMENT).listOf().fieldOf("values").forGetter(EnchantmentsExistResourceCondition::enchantments)
		).apply(instance, EnchantmentsExistResourceCondition::new));
		
		@Override
		public MapCodec<? extends ICondition> codec() {
			return CODEC;
		}
		
		@Override
		public boolean test(IContext iContext) {
			// TODO: fix
			// according to https://docs.neoforged.net/docs/resources/server/conditions we only have access to tags here, not registries.
			
			/*if (wrapperLookup == null || wrapperLookup.lookup(Registries.ENCHANTMENT).isEmpty())
				return false;
			HolderLookup.RegistryLookup<Enchantment> impl = wrapperLookup.lookup(Registries.ENCHANTMENT).get();
			return enchantments.stream().allMatch(key -> impl.get(key).isPresent());*/
			return false;
		}
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
			// TODO: port
			// return PastelIntegrationPacks.isIntegrationPackActive(integrationPack);
			return false;
		}
	}
	
}
