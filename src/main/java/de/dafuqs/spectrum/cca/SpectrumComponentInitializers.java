package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.cca.azure_dike.*;
import dev.onyxstudios.cca.api.v3.entity.*;
import dev.onyxstudios.cca.api.v3.level.*;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.*;

public class SpectrumComponentInitializers implements EntityComponentInitializer, LevelComponentInitializer, WorldComponentInitializer {

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, AzureDikeProvider.AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new);
		registry.registerForPlayers(AzureDikeProvider.AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		
		registry.registerFor(LivingEntity.class, EverpromiseRibbonComponent.EVERPROMISE_RIBBON_COMPONENT, EverpromiseRibbonComponent::new);
		
		registry.registerFor(LivingEntity.class, LastKillComponent.LAST_KILL_COMPONENT, LastKillComponent::new);
		registry.registerForPlayers(LastKillComponent.LAST_KILL_COMPONENT, LastKillComponent::new, RespawnCopyStrategy.NEVER_COPY);
		
		registry.registerFor(LivingEntity.class, OnPrimordialFireComponent.ON_PRIMORDIAL_FIRE_COMPONENT, OnPrimordialFireComponent::new);
		registry.registerForPlayers(OnPrimordialFireComponent.ON_PRIMORDIAL_FIRE_COMPONENT, OnPrimordialFireComponent::new, RespawnCopyStrategy.NEVER_COPY);
	}
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(HardcoreDeathComponent.HARDCORE_DEATHS_COMPONENT, e -> new HardcoreDeathComponent());
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(DDWorldEffectsComponent.DD_WORLD_EFFECTS_COMPONENT, DDWorldEffectsComponent::new);
	}
}
