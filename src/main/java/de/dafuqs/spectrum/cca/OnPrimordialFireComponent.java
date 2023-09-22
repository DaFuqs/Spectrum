package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.component.sync.*;
import dev.onyxstudios.cca.api.v3.component.tick.*;
import net.fabricmc.api.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class OnPrimordialFireComponent implements Component, AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
	
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
		} else {
			this.primordialFireTicks = 0;
		}
	}
	
	public static void addPrimordialFireTicks(LivingEntity livingEntity, int ticks) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		ticks = ProtectionEnchantment.transformFireDuration(livingEntity, ticks);
		component.primordialFireTicks += ticks;
		
		ON_PRIMORDIAL_FIRE_COMPONENT.sync(component.provider);
	}
	
	public static boolean isOnPrimordialFire(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		return component.primordialFireTicks > 0;
	}
	
	public static void putOut(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		component.primordialFireTicks = 0;
		ON_PRIMORDIAL_FIRE_COMPONENT.sync(component);
	}
	
	@Override
	public void serverTick() {
		if (this.primordialFireTicks > 0) {
			if (this.primordialFireTicks % 10 == 0 && !provider.isInLava()) {
				provider.damage(SpectrumDamageSources.PRIMORDIAL_FIRE, 4);
			}
			
			this.primordialFireTicks -= this.provider.getFluidHeight(FluidTags.WATER) > 0 ? 4 : 1;
			
			// was on fire, but is not any longer
			if (this.primordialFireTicks == 0) {
				ON_PRIMORDIAL_FIRE_COMPONENT.sync(this.provider);
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void clientTick() {
		if (this.primordialFireTicks > 0) {
			double fluidHeight = this.provider.getFluidHeight(FluidTags.WATER);
			if (fluidHeight > 0) {
				
				World world = this.provider.world;
				Random random = world.random;
				Vec3d pos = this.provider.getPos();
				
				for (int i = 0; i < 2; i++) {
					world.addParticle(ParticleTypes.BUBBLE_POP, this.provider.getParticleX(1), pos.getY() + Math.min(fluidHeight, provider.getHeight()) * random.nextFloat(), this.provider.getParticleZ(1), 0.0, 0.04, 0.0);
					world.addParticle(ParticleTypes.SMOKE, this.provider.getParticleX(1), pos.getY() + Math.min(fluidHeight, provider.getHeight()) * random.nextFloat(), this.provider.getParticleZ(1), 0.0, 0.04, 0.0);
				}
				if (world.random.nextInt(12) == 0) {
					provider.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
				}
			}
		}
	}
	
}
