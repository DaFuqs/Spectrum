package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.cca.azure_dike.*;
import net.minecraft.world.entity.*;
import org.ladysnake.cca.api.v3.entity.*;
import org.ladysnake.cca.api.v3.level.*;

public class SpectrumComponentInitializers implements EntityComponentInitializer, LevelComponentInitializer {

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, AzureDikeProvider.AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new);
		registry.registerForPlayers(AzureDikeProvider.AZURE_DIKE_COMPONENT, DefaultAzureDikeComponent::new, (original, clone, wrapperLookup, lossless, keepInventory, sameCharacter) -> clone.set(original.getMaxProtection(), original.getTicksPerPointOfRecharge(), original.getRechargeDelayTicksAfterGettingHit(), lossless));
		
		registry.beginRegistration(LivingEntity.class, EverpromiseRibbonComponent.EVERPROMISE_RIBBON_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(EverpromiseRibbonComponent::new);

		registry.registerFor(LivingEntity.class, LastKillComponent.LAST_KILL_COMPONENT, LastKillComponent::new);
		registry.registerForPlayers(LastKillComponent.LAST_KILL_COMPONENT, LastKillComponent::new, RespawnCopyStrategy.NEVER_COPY);
		
		registry.registerFor(LivingEntity.class, OnPrimordialFireComponent.ON_PRIMORDIAL_FIRE_COMPONENT, OnPrimordialFireComponent::new);
		registry.registerForPlayers(OnPrimordialFireComponent.ON_PRIMORDIAL_FIRE_COMPONENT, OnPrimordialFireComponent::new, RespawnCopyStrategy.NEVER_COPY);

		registry.registerForPlayers(MiscPlayerDataComponent.MISC_PLAYER_DATA_COMPONENT, MiscPlayerDataComponent::new, RespawnCopyStrategy.NEVER_COPY);
	}
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(HardcoreDeathComponent.HARDCORE_DEATHS_COMPONENT, e -> new HardcoreDeathComponent());
	}
	
}
