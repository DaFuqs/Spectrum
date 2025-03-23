package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.component.sync.*;
import dev.onyxstudios.cca.api.v3.component.tick.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.fabricmc.loader.api.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OnPrimordialFireComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {

	// 1% of max health as damage every tick as a base.
	public static final float BASE_PERCENT_DAMAGE = 0.01F;

	// Base damage reduction applied by fire resistance
	public static final float FIRE_RESISTANCE_DAMAGE_RESISTANCE = 0.25F;
	// Per-level damage reduction added by fire prot. Caps at 50%
	public static final float FIRE_PROT_DAMAGE_RESISTANCE = 0.05F;
	
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

	public static void setPrimordialFireTicks(LivingEntity livingEntity, int ticks) {
		OnPrimordialFireComponent component = ON_PRIMORDIAL_FIRE_COMPONENT.get(livingEntity);
		component.primordialFireTicks = ticks;
		ON_PRIMORDIAL_FIRE_COMPONENT.sync(component.provider);
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
		if (provider.getType().isIn(SpectrumEntityTypeTags.PRIMORDIAL_FIRE_IMMUNE)) {
			primordialFireTicks = 0;
			ON_PRIMORDIAL_FIRE_COMPONENT.sync(this.provider);
			return;
		}

		if (this.primordialFireTicks > 0) {
			if (!isAffectingConstruct()) {
				var damageScaling = getDamageHealthScaling(provider);
				provider.damage(SpectrumDamageTypes.primordialFire(this.provider.getWorld()), AzureDikeProvider.absorbDamage(provider, damageScaling * provider.getMaxHealth()));
			}
			//Primordial fire is so strong because it rends the soul. No soul = just slightly spicier fire
			//Constructs have no soul, thus you get 2 dps and no more
			else if (provider.age % 10 == 0) {
				provider.damage(SpectrumDamageTypes.primordialFire(this.provider.getWorld()), 1);
			}

			this.primordialFireTicks -= this.provider.getFluidHeight(FluidTags.WATER) > 0 ? 3 : 1;
			// was on fire, but is not any longer
			if (this.primordialFireTicks <= 0) {
				ON_PRIMORDIAL_FIRE_COMPONENT.sync(this.provider);
			}
		}
	}

	public boolean isAffectingConstruct() {
		return provider.getType().isIn(SpectrumEntityTypeTags.SOULLESS);
	}

	/**
	 * Primordial fire's base DPS is 1/t, for a kill time of 5 seconds on a base hp player.
	 */
	public float getDamageHealthScaling(LivingEntity entity) {
		float baseDamage = BASE_PERCENT_DAMAGE;

		//Bosses have great and exceptional souls that can resist a lot more.
		//95% less damage to them before reductions and caps
		if (entity.getType().isIn(ConventionalEntityTypeTags.BOSSES))
			baseDamage /= 20F;

        return baseDamage * getDamagePenalties(entity) * getDamageBonuses(entity);
	}

	public float getDamagePenalties(LivingEntity entity) {
		//fire prot has a cap of 50% DR, requiring fire protection 10 on an armor piece
		float fireProt = Math.min(FIRE_PROT_DAMAGE_RESISTANCE * EnchantmentHelper.getEquipmentLevel(Enchantments.FIRE_PROTECTION, provider), 0.5F);
		int fireResLevel = Optional.ofNullable(provider.getStatusEffect(StatusEffects.FIRE_RESISTANCE)).map(StatusEffectInstance::getAmplifier).orElse(-1) + 1;
		float fireRes = 0;

		// flat 25% for a start on fire res
		if (fireResLevel > 0)
			fireRes = FIRE_RESISTANCE_DAMAGE_RESISTANCE;

		//Fire resistance has diminishing returns
		for (int i = 1; i < fireResLevel; i++) {
			fireRes += (float) (0.05 * (i) + (0.25F * Math.pow(0.5F, i)));
		}

		//Fire immune entities can have a lil res, as a treat
		float immunityReduction = entity.isFireImmune() ? 0.25F : 0;

		//Primordial fire has an overall cap of 90% DR
		return Math.max(1 - (fireRes + fireProt + immunityReduction), 0.10F);
	}

	/**
	 * Here for completeness.
	 * <p>
	 * Unused... for now...
	 */
	public float getDamageBonuses(LivingEntity entity) {
		return 1F;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void clientTick() {
		if (this.primordialFireTicks > 0) {
			if (provider.equals(MinecraftClient.getInstance().player) && primordialFireTicks > 2 && soundInstance.isEmpty()) {
				soundInstance = Optional.of(new OnPrimordialFireSoundInstance((PlayerEntity) provider));
				MinecraftClient.getInstance().getSoundManager().play(soundInstance.get());
			}
			
			double fluidHeight = this.provider.getFluidHeight(FluidTags.WATER);
			if (fluidHeight > 0) {
				World world = this.provider.getWorld();
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
		else if(provider.equals(MinecraftClient.getInstance().player) && soundInstance.isPresent()) {
			soundInstance = Optional.empty();
		}
	}

}
