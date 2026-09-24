package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.conditions.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumResourceConditions {
	
	public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register("integration_pack_active", () -> IntegrationPackActiveResourceCondition.CODEC);
		REGISTRAR.register("fluid_tag_empty", () -> FluidTagEmptyCondition.CODEC);
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
	
	public record FluidTagEmptyCondition(TagKey<Fluid> tag) implements ICondition {
		public static final MapCodec<FluidTagEmptyCondition> CODEC = RecordCodecBuilder.mapCodec(
				builder -> builder
						.group(ResourceLocation.CODEC.xmap(loc -> TagKey.create(Registries.FLUID, loc), TagKey::location).fieldOf("tag").forGetter(FluidTagEmptyCondition::tag))
						.apply(builder, FluidTagEmptyCondition::new));
		
		@Override
		public boolean test(ICondition.IContext context) {
			return context.getTag(tag).isEmpty();
		}
		
		@Override
		public MapCodec<? extends ICondition> codec() {
			return CODEC;
		}
		
		@Override
		public String toString() {
			return "fluid_tag_empty(\"" + tag.location() + "\")";
		}
	}
	
}
