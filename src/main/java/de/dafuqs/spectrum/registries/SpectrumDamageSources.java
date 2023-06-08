package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import org.jetbrains.annotations.*;

public class SpectrumDamageSources {
	
	public static boolean recursiveDamage = false;
	
	public static final DamageSource DECAY = new SpectrumDamageSource("spectrum_decay");
	public static final DamageSource FLOATBLOCK = new SpectrumDamageSource("spectrum_floatblock").setFromFalling().setNeutral();
	public static final DamageSource SHOOTING_STAR = new SpectrumDamageSource("spectrum_shooting_star").setFromFalling().setNeutral().setProjectile();
	public static final DamageSource MIDNIGHT_SOLUTION = new SpectrumDamageSource("spectrum_midnight_solution").setDropsPlayerLoot().setBypassesArmor().setNeutral().setUsesMagic();
	public static final DamageSource DRAGONROT = new SpectrumDamageSource("spectrum_dragonrot").setBypassesProtection().setBypassesArmor().setNeutral().setUsesMagic();
	public static final DamageSource DIKE_GATE = new SpectrumDamageSource("spectrum_dike_gate").setNeutral();
	public static final DamageSource DEADLY_POISON = new SpectrumDamageSource("spectrum_deadly_poison").setBypassesArmor().setUsesMagic();
	public static final DamageSource INCANDESCENCE = new SpectrumDamageSource("spectrum_incandescence").setNeutral().setUsesMagic().setExplosive();
	public static final DamageSource BRISTLE_SPROUTS = new SpectrumDamageSource("spectrum_bristle_sprouts");
	public static final DamageSource SAWTOOTH = new SpectrumDamageSource("spectrum_sawtooth");
	public static final DamageSource SNAPPING_IVY = new SpectrumDamageSource("spectrum_snapping_ivy").setUnblockable().setNeutral();
	public static final DamageSource IRRADIANCE_DEFAULT = new SpectrumDamageSource("spectrum_irradiance").setBypassesArmor().setBypassesProtection().setUnblockable();
	
	public static DamageSource inkProjectile(InkProjectileEntity projectile, @Nullable Entity attacker) {
		return (new ProjectileDamageSource("spectrum_ink_projectile", projectile, attacker)).setProjectile();
	}
	
	public static DamageSource moonstoneBlast(@Nullable MoonstoneStrike moonstoneStrike) {
		return moonstoneBlast(moonstoneStrike != null ? moonstoneStrike.getCausingEntity() : null);
	}
	
	public static DamageSource moonstoneBlast(@Nullable LivingEntity attacker) {
		return attacker != null ? (new EntityDamageSource("spectrum_moonstone_blast.player", attacker).setExplosive()) : (new DamageSource("moonstone_blast").setExplosive());
	}
	
	public static DamageSource irradiance(@Nullable LivingEntity attacker) {
		return attacker == null ? IRRADIANCE_DEFAULT :
				new EntityDamageSource("spectrum_irradiance.player", attacker).setBypassesArmor().setBypassesProtection().setUnblockable();
	}
	
	public static DamageSource setHealth(LivingEntity attacker) {
		return new SetHealthDamageSource("spectrum_set_health", attacker);
	}
	
	public static DamageSource kindlingCough(KindlingCoughEntity kindlingCoughEntity, @Nullable Entity attacker) {
		return attacker == null ?
				new ProjectileDamageSource("onFire", kindlingCoughEntity, kindlingCoughEntity).setFire().setProjectile() :
				new ProjectileDamageSource("spectrum_kindling_cough", kindlingCoughEntity, attacker).setFire().setProjectile();
	}
	
	public static class SetHealthDamageSource extends EntityDamageSource {
		
		public SetHealthDamageSource(String name, LivingEntity attacker) {
			super(name, attacker);
		}
	}
	
	public static class SpectrumDamageSource extends DamageSource {
		
		private boolean dropsPlayerLoot = false;

		protected SpectrumDamageSource(String name) {
			super(name);
		}

		@Override
		public SpectrumDamageSource setUnblockable() {
			super.setUnblockable();
			return this;
		}
		
		@Override
		public SpectrumDamageSource setBypassesArmor() {
			super.setBypassesArmor();
			return this;
		}
		
		@Override
		public SpectrumDamageSource setFromFalling() {
			super.setFromFalling();
			return this;
		}
		
		public SpectrumDamageSource setDropsPlayerLoot() {
			this.dropsPlayerLoot = true;
			return this;
		}
		
		public boolean dropsPlayerLoot() {
			return this.dropsPlayerLoot;
		}
		
	}
	
}
