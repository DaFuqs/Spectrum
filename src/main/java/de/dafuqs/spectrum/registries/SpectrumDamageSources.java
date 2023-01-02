package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.entity.entity.InkProjectileEntity;
import de.dafuqs.spectrum.entity.entity.MoonstoneBlast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public class SpectrumDamageSources {

	public static boolean recursiveDamage = false;

	public static final DamageSource DECAY = new SpectrumDamageSource("spectrum_decay");
	public static final DamageSource FLOATBLOCK = new SpectrumDamageSource("spectrum_floatblock").setFromFalling().setNeutral();
	public static final DamageSource SHOOTING_STAR = new SpectrumDamageSource("spectrum_shooting_star").setFromFalling().setNeutral().setProjectile();
	public static final DamageSource MIDNIGHT_SOLUTION = new SpectrumDamageSource("spectrum_midnight_solution").setDropsPlayerLoot().setBypassesArmor().setNeutral().setUsesMagic();
	public static final DamageSource DIKE_GATE = new SpectrumDamageSource("spectrum_dike_gate").setNeutral();

	public static final DamageSource INCANDESCENCE = new SpectrumDamageSource("spectrum_incandescence").setNeutral().setUsesMagic().setExplosive();

	public static DamageSource inkProjectile(InkProjectileEntity projectile, @Nullable Entity attacker) {
		return (new ProjectileDamageSource("spectrum_ink_projectile", projectile, attacker)).setProjectile();
	}

	public static DamageSource moonstoneBlast(@Nullable MoonstoneBlast moonstoneBlast) {
		return moonstoneBlast(moonstoneBlast != null ? moonstoneBlast.getCausingEntity() : null);
	}

	public static DamageSource moonstoneBlast(@Nullable LivingEntity attacker) {
		return attacker != null ? (new EntityDamageSource("moonstone_blast.player", attacker).setExplosive()) : (new DamageSource("moonstone_blast").setExplosive());
	}

	public static DamageSource setHealth(LivingEntity attacker) {
		return new SetHealthDamageSource("set_health", attacker);
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
