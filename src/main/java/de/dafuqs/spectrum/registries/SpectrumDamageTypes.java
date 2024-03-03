package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

// Damage Types handle the logic of how the damage behaves, determined via tags
// Damage Sources decide how death messages are handled
// Make a custom damage source if you want a custom message, otherwise just return a damage source with the type you want
public class SpectrumDamageTypes {
	
	public static boolean recursiveDamageFlag = false;
	
	public static final RegistryKey<DamageType> DECAY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("decay"));
	public static final RegistryKey<DamageType> FLOATBLOCK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("floatblock"));
	public static final RegistryKey<DamageType> SHOOTING_STAR = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("shooting_star"));
	public static final RegistryKey<DamageType> MIDNIGHT_SOLUTION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("midnight_solution"));
	public static final RegistryKey<DamageType> DRAGONROT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("dragonrot"));
	public static final RegistryKey<DamageType> DIKE_GATE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("dike_gate"));
	public static final RegistryKey<DamageType> INK_PROJECTILE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("ink_projectile"));
	public static final RegistryKey<DamageType> DEADLY_POISON = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("deadly_poison"));
	public static final RegistryKey<DamageType> INCANDESCENCE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("incandescence")); // explosions with that type cause Primordial Fire
	public static final RegistryKey<DamageType> MOONSTONE_STRIKE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("moonstone_strike"));
	public static final RegistryKey<DamageType> BRISTLE_SPROUTS = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("bristle_sprouts"));
	public static final RegistryKey<DamageType> SAWTOOTH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("sawtooth"));
	public static final RegistryKey<DamageType> SET_HEALTH_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("set_health_damage"));
	public static final RegistryKey<DamageType> IRRADIANCE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("irradiance"));
	public static final RegistryKey<DamageType> KINDLING_COUGH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("kindling_cough"));
	public static final RegistryKey<DamageType> SNAPPING_IVY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("snapping_ivy"));
	public static final RegistryKey<DamageType> PRIMORDIAL_FIRE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("primordial_fire"));
	
	public static DamageSource sawtooth(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(SAWTOOTH));
	}
	
	public static DamageSource dragonrot(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(DRAGONROT));
	}
	
	public static DamageSource inkProjectile(InkProjectileEntity projectile, @Nullable Entity attacker) {
		return new DamageSource(projectile.getDamageSources().registry.entryOf(INK_PROJECTILE), projectile, attacker);
	}
	
	public static DamageSource moonstoneStrike(World world, @Nullable MoonstoneStrike moonstoneStrike) {
		return moonstoneStrike(world, moonstoneStrike != null ? moonstoneStrike.getCausingEntity() : null);
	}
	
	public static DamageSource moonstoneStrike(World world, @Nullable LivingEntity attacker) {
		return new MoonstoneStrikeDamageSource(world, attacker);
	}
	
	public static DamageSource irradiance(World world, @Nullable LivingEntity attacker) {
		return new IrradianceDamageSource(world, attacker);
	}

	public static DamageSource setHealth(World world, @Nullable LivingEntity attacker) {
		return new SetHealthDamageSource(world, attacker);
	}

	public static DamageSource floatblock(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(FLOATBLOCK));
	}

	public static DamageSource shootingStar(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(SHOOTING_STAR));
	}

	public static DamageSource incandescence(World world) {
		return incandescence(world, null);
	}

	public static DamageSource incandescence(World world, @Nullable Entity attacker) {
		return new DamageSource(world.getDamageSources().registry.entryOf(INCANDESCENCE), attacker);
	}

	public static DamageSource midnightSolution(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(MIDNIGHT_SOLUTION));
	}

	public static DamageSource decay(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(DECAY));
	}

	public static DamageSource deadlyPoison(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(DEADLY_POISON));
	}

	public static DamageSource dike(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(DIKE_GATE));
	}

	public static DamageSource bristeSprouts(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(BRISTLE_SPROUTS));
	}

	public static DamageSource kindlingCough(World world, @Nullable LivingEntity attacker) {
		return new KindlingCoughDamageSource(world, attacker);
	}

	public static DamageSource snappingIvy(World world) {
		return new DamageSource(world.getDamageSources().registry.entryOf(SNAPPING_IVY));
	}

	public static DamageSource primordialFire(World world) {
		return new PrimordialFireDamageSource(world, null);
	}

	public static DamageSource primordialFire(World world, @Nullable LivingEntity attacker) {
		return new PrimordialFireDamageSource(world, attacker);
	}
	
	public static class SetHealthDamageSource extends DamageSource {
		
		public SetHealthDamageSource(World world, @Nullable LivingEntity attacker) {
			super(world.getDamageSources().registry.entryOf(SET_HEALTH_DAMAGE), attacker);
		}
	}

	public static class MoonstoneStrikeDamageSource extends DamageSource {

		public MoonstoneStrikeDamageSource(World world, LivingEntity attacker) {
			super(world.getDamageSources().registry.entryOf(MOONSTONE_STRIKE), attacker);
		}

		public MoonstoneStrikeDamageSource(MoonstoneStrike moonstoneStrike) {
			super(moonstoneStrike.getDamageSource().getTypeRegistryEntry(), moonstoneStrike.getCausingEntity());
		}
	}

	public static class IrradianceDamageSource extends DamageSource {

		public IrradianceDamageSource(World world, @Nullable LivingEntity attacker) {
			super(world.getDamageSources().registry.entryOf(IRRADIANCE), attacker);
		}
	}

	public static class KindlingCoughDamageSource extends DamageSource {

		public KindlingCoughDamageSource(World world, @Nullable LivingEntity attacker) {
			super(world.getDamageSources().registry.entryOf(KINDLING_COUGH), attacker);
		}
	}
	
	public static class PrimordialFireDamageSource extends DamageSource {
		
		public PrimordialFireDamageSource(World world, @Nullable LivingEntity attacker) {
			super(world.getDamageSources().registry.entryOf(PRIMORDIAL_FIRE), attacker);
		}
	}

}
