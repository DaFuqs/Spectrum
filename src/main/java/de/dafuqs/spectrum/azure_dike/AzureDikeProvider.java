package de.dafuqs.spectrum.azure_dike;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class AzureDikeProvider implements EntityComponentInitializer {
	
	public static final ComponentKey<AzureDikeComponent> AZURE_DIKE_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(SpectrumCommon.MOD_ID, "azure_dike"), AzureDikeComponent.class); // See the "Registering your component" section
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new);
		registry.registerForPlayers(AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
	
	/**
	 * Uses as much Azure Dike as possible to protect the Provider from incoming damage
	 * @param provider The Component Provider
	 * @param damage The incoming damage
	 * @return All damage that could not be protected from
	 */
	public static float useAzureDike(LivingEntity provider, float damage) {
		// Retrieve a provided component
		int protection = AZURE_DIKE_COMPONENT.get(provider).getProtection();
		
		int usedProtection = Math.min(protection, (int) damage);
		AZURE_DIKE_COMPONENT.get(provider).damage(usedProtection);
		return Math.max(0, damage - usedProtection);
	}
	
	public static int getProtection(LivingEntity provider) {
		return AZURE_DIKE_COMPONENT.get(provider).getProtection();
	}
	
}
