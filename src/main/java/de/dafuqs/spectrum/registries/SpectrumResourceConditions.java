package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.fabric.api.resource.conditions.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumResourceConditions {
	
	public static void register() {
		ResourceConditions.register(EnchantmentsExistResourceCondition.TYPE);
		ResourceConditions.register(IntegrationPackActiveResourceCondition.TYPE);
	}
	
	public record EnchantmentsExistResourceCondition(List<ResourceKey<Enchantment>> enchantments) implements ResourceCondition {
		
		public static MapCodec<EnchantmentsExistResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ResourceKey.codec(Registries.ENCHANTMENT).listOf().fieldOf("values").forGetter(EnchantmentsExistResourceCondition::enchantments)
		).apply(instance, EnchantmentsExistResourceCondition::new));
		public static ResourceConditionType<EnchantmentsExistResourceCondition> TYPE = ResourceConditionType.create(locate("enchantments_exist"), CODEC);
		
		@Override
		public ResourceConditionType<?> getType() {
			return TYPE;
		}
		
		@Override
		public boolean test(@Nullable HolderLookup.Provider wrapperLookup) {
			if (wrapperLookup == null || wrapperLookup.lookup(Registries.ENCHANTMENT).isEmpty())
				return false;
			HolderLookup.RegistryLookup<Enchantment> impl = wrapperLookup.lookup(Registries.ENCHANTMENT).get();
			return enchantments.stream().allMatch(key -> impl.get(key).isPresent());
		}
	}
	
	public record IntegrationPackActiveResourceCondition(String integrationPack) implements ResourceCondition {
		
		public static MapCodec<IntegrationPackActiveResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf("integration_pack").forGetter(IntegrationPackActiveResourceCondition::integrationPack)
		).apply(instance, IntegrationPackActiveResourceCondition::new));
		public static ResourceConditionType<IntegrationPackActiveResourceCondition> TYPE = ResourceConditionType.create(locate("integration_pack_active"), CODEC);
		
		@Override
		public ResourceConditionType<?> getType() {
			return TYPE;
		}
		
		@Override
		public boolean test(@Nullable HolderLookup.Provider wrapperLookup) {
			return SpectrumIntegrationPacks.isIntegrationPackActive(integrationPack);
		}
	}
	
}
