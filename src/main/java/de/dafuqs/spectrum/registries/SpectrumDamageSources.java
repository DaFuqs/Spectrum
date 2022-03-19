package de.dafuqs.spectrum.registries;

import net.minecraft.entity.damage.DamageSource;

public class SpectrumDamageSources {

	public static final DamageSource DECAY = new SpectrumDamageSource("spectrum_decay").setUnblockable().setBypassesArmor();
	public static final DamageSource FLOATBLOCK = new SpectrumDamageSource("spectrum_floatblock").setFromFalling().setNeutral();
	public static final DamageSource SHOOTING_STAR = new SpectrumDamageSource("spectrum_shooting_star").setFromFalling().setNeutral().setProjectile();
	public static final DamageSource MIDNIGHT_SOLUTION = new SpectrumDamageSource("spectrum_midnight_solution").setDropsPlayerLoot().setUnblockable().setBypassesArmor().setNeutral().setUsesMagic();
	
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
