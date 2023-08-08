package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.component.sync.*;
import dev.onyxstudios.cca.api.v3.component.tick.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

public class OnPrimordialFireComponent implements Component, AutoSyncedComponent, ServerTickingComponent {
	
	public static final ComponentKey<OnPrimordialFireComponent> ON_PRIMORDIAL_FIRE_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("on_primordial_fire"), OnPrimordialFireComponent.class);
	
	private LivingEntity provider;
	private long primordialFireTicks = 0;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	public OnPrimordialFireComponent() {
	
	}
	
	public OnPrimordialFireComponent(LivingEntity entity) {
		this.provider = entity;
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (this.primordialFireTicks > 0) {
			tag.putLong("ticks", this.primordialFireTicks);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("ticks", NbtElement.LONG_TYPE)) {
			this.primordialFireTicks = tag.getLong("ticks");
		}
	}
	
	public static void addPrimordialFireTicks(LivingEntity livingEntity, int ticks) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		ticks = ProtectionEnchantment.transformFireDuration(livingEntity, ticks);
		component.primordialFireTicks += ticks;
	}
	
	public static boolean isOnPrimordialFire(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		return component.primordialFireTicks > 0;
	}
	
	public static void putOut(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		component.primordialFireTicks = 0;
	}
	
	@Override
	public void serverTick() {
		if (this.primordialFireTicks > 0 && this.primordialFireTicks % 4 == 0) {
			provider.damage(SpectrumDamageSources.PRIMORDIAL_FIRE, 4);
		}
	}
	
}
