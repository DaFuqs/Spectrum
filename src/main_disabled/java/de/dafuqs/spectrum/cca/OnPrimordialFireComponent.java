package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.fabricmc.loader.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.*;
import org.ladysnake.cca.api.v3.component.sync.*;
import org.ladysnake.cca.api.v3.component.tick.*;

import java.util.*;

public class OnPrimordialFireComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
	
	public static final float BASE_PERCENT_DAMAGE = 0.1F;
	
	@Environment(EnvType.CLIENT)
	private static Optional<OnPrimordialFireSoundInstance> soundInstance;
	
	/* prevent the static initializer from attempting to write to the client-only field in common code */
	static {
		if (EnvType.CLIENT == FabricLoader.getInstance().getEnvironmentType()) soundInstance = Optional.empty();
	}

	public static final ComponentKey<OnPrimordialFireComponent> ON_PRIMORDIAL_FIRE_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("on_primordial_fire"), OnPrimordialFireComponent.class);
	
	private LivingEntity provider;
	private long primordialFireTicks = 0;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	@SuppressWarnings("unused")
	public OnPrimordialFireComponent() {
	}
	
	public OnPrimordialFireComponent(LivingEntity entity) {
		this.provider = entity;
	}

	@Override
	public void writeToNbt(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider wrapperLookup) {
		if (this.primordialFireTicks > 0) {
			tag.putLong("ticks", this.primordialFireTicks);
		}
	}
	
	@Override
	public void readFromNbt(CompoundTag tag, @NotNull HolderLookup.Provider wrapperLookup) {
		if (tag.contains("ticks", Tag.TAG_LONG)) {
			this.primordialFireTicks = tag.getLong("ticks");
		} else {
			this.primordialFireTicks = 0;
		}
	}

	public static void setPrimordialFireTicks(LivingEntity livingEntity, int ticks) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		component.primordialFireTicks = ticks;
		ON_PRIMORDIAL_FIRE_COMPONENT.sync(component.provider);
	}

	public static void addPrimordialFireTicks(LivingEntity livingEntity, int ticks) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		int i = SpectrumEnchantmentHelper.getEquipmentLevel(livingEntity.level().registryAccess(), Enchantments.FIRE_PROTECTION, livingEntity);
		if (i > 0) {
			ticks -= Mth.floor(ticks * i * 0.15F);
		}
		component.primordialFireTicks += ticks;


		ON_PRIMORDIAL_FIRE_COMPONENT.sync(component.provider);
	}
	
	public static boolean isOnPrimordialFire(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		return component.primordialFireTicks > 0;
	}
	
	public static boolean putOut(LivingEntity livingEntity) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		if (component.primordialFireTicks > 0) {
			component.primordialFireTicks = 0;
			ON_PRIMORDIAL_FIRE_COMPONENT.sync(component.provider);
			return true;
		}
		return false;
	}

	@Override
	public void serverTick() {

		//Immune creatures get spared. If we ever add any.
		if (provider.getType().is(SpectrumEntityTypeTags.PRIMORDIAL_FIRE_IMMUNE)) {
			primordialFireTicks = 0;
			ON_PRIMORDIAL_FIRE_COMPONENT.sync(this.provider);
			return;
		}

		if (this.primordialFireTicks > 0) {
			if (!isAffectingConstruct()) {
				var damageScaling = getDamage(provider);
				provider.hurt(SpectrumDamageTypes.primordialFire(this.provider.level()), damageScaling);
			}
			//Primordial fire is so strong because it rends the soul. No soul = just slightly spicier fire
			//Constructs have no soul, thus you get 2 dps and no more
			else if (provider.tickCount % 20 == 0) {
				provider.hurt(SpectrumDamageTypes.primordialFire(this.provider.level()), 2.0F);
			}

			this.primordialFireTicks -= this.provider.getFluidHeight(FluidTags.WATER) > 0 ? 3 : 1;
			
			// was on fire, but is not any longer
			if (this.primordialFireTicks <= 0) {
				ON_PRIMORDIAL_FIRE_COMPONENT.sync(this.provider);
			}
		}
	}

	public boolean isAffectingConstruct() {
		return provider.getType().is(SpectrumEntityTypeTags.SOULLESS);
	}
	
	public float getDamage(LivingEntity entity) {
		float baseDamage = BASE_PERCENT_DAMAGE;

		//Bosses have great and exceptional souls that can resist a lot more.
		//95% less damage to them
		if (entity.getType().is(ConventionalEntityTypeTags.BOSSES))
			baseDamage /= 20F;
		
		//Fire immune entities can have a lil res, as a treat
		float fireImmunityMultiplier = entity.fireImmune() ? 0.25F : 1;
		return baseDamage * fireImmunityMultiplier * provider.getMaxHealth();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void clientTick() {
		if (this.primordialFireTicks > 0) {
			if (provider.equals(Minecraft.getInstance().player) && primordialFireTicks > 2 && soundInstance.isEmpty()) {
				soundInstance = Optional.of(new OnPrimordialFireSoundInstance((Player) provider));
				Minecraft.getInstance().getSoundManager().play(soundInstance.get());
			}
			
			double fluidHeight = this.provider.getFluidHeight(FluidTags.WATER);
			if (fluidHeight > 0) {
				
				Level world = this.provider.level();
				RandomSource random = world.random;
				Vec3 pos = this.provider.position();

				for (int i = 0; i < 2; i++) {
					world.addParticle(ParticleTypes.BUBBLE_POP, this.provider.getRandomX(1), pos.y() + Math.min(fluidHeight, provider.getBbHeight()) * random.nextFloat(), this.provider.getRandomZ(1), 0.0, 0.04, 0.0);
					world.addParticle(ParticleTypes.SMOKE, this.provider.getRandomX(1), pos.y() + Math.min(fluidHeight, provider.getBbHeight()) * random.nextFloat(), this.provider.getRandomZ(1), 0.0, 0.04, 0.0);
				}
				if (world.random.nextInt(12) == 0) {
					provider.playSound(SoundEvents.FIRE_EXTINGUISH, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
				}
			}
		} else if (provider.equals(Minecraft.getInstance().player) && soundInstance.isPresent()) {
			soundInstance = Optional.empty();
		}
	}

}
