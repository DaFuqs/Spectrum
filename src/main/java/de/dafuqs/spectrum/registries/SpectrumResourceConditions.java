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
		//REGISTRAR.register("registered", () -> Registered.CODEC);
		
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
	
	
	/*public record Registered(ResourceLocation registry, ResourceLocation value) implements ICondition {
		public static final MapCodec<Registered> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
						ResourceLocation.CODEC.fieldOf("registry").orElse(Registries.ITEM.location()).forGetter(Registered::registry),
						ResourceLocation.CODEC.fieldOf("value").forGetter(Registered::value))
				.apply(instance, Registered::new));
		
		@Override
		public boolean test(@NotNull IContext context) {
			return registryEntryRegistered(context, this.registry(), this.value());
		}
		
		@Override
		public @NotNull MapCodec<? extends ICondition> codec() {
			return Registered.CODEC;
		}
		
		public static boolean registryEntryRegistered(IContext context, ResourceLocation registryId, ResourceLocation value) {
			ResourceKey<Registry<Registry<?>>> registryKey = ResourceKey.createRegistryKey(registryId);
			
			// TODO: cursed as of 1.21.1
			// In 1.21.11+ context has info about registries, so this can be cleaned up and the accessors removed
			// also the `neoforge:registered` condition exists there
			ConditionContextAccessor conditionContextAccessor = (ConditionContextAccessor) context;
			TagManager tagManager = conditionContextAccessor.getTagManager();
			TagManagerAccessor tagManagerAccessor = (TagManagerAccessor) tagManager;
			Optional<Registry<Registry<?>>> registry = tagManagerAccessor.getRegistryAccess().registry(registryKey);
			if (registry == null) {
				return false;
			}
			
			return registry.get().containsKey(value);
		}
		
		public ResourceLocation registry() {
			return this.registry;
		}
		
		public ResourceLocation value() {
			return this.value;
		}
	}*/
	
}
