package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

public class LastKillComponent implements Component, EntityComponentInitializer {
	
	public static final ComponentKey<LastKillComponent> LAST_KILL_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("last_kill"), LastKillComponent.class);
	
	private long lastKillTick = -1;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	public LastKillComponent() {
	
	}
	
	public LastKillComponent(LivingEntity entity) {
	
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, LAST_KILL_COMPONENT, LastKillComponent::new);
		registry.registerForPlayers(LAST_KILL_COMPONENT, LastKillComponent::new, RespawnCopyStrategy.NEVER_COPY);
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if(this.lastKillTick >= 0) {
			tag.putLong("last_kill_tick", this.lastKillTick);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if(tag.contains("last_kill_tick", NbtElement.LONG_TYPE)) {
			this.lastKillTick = tag.getLong("last_kill_tick");
		}
	}
	
	public static void rememberKillTick(LivingEntity livingEntity, long tick) {
		LastKillComponent component = LAST_KILL_COMPONENT.get(livingEntity);
		component.lastKillTick = tick;
	}
	
	public static long getLastKillTick(LivingEntity livingEntity) {
		LastKillComponent component = LAST_KILL_COMPONENT.get(livingEntity);
		return component.lastKillTick;
	}
	
}
