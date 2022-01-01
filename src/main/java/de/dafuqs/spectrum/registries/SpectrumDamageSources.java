package de.dafuqs.spectrum.registries;

import net.minecraft.entity.damage.DamageSource;

public class SpectrumDamageSources {

	public static final DamageSource DECAY = new SpectrumDamageSource("spectrum_decay").setUnblockable().setBypassesArmor();
	public static final DamageSource FLOATBLOCK = new SpectrumDamageSource("spectrum_floatblock").setFromFalling();

	public static class SpectrumDamageSource extends DamageSource {
		
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
		
	}

}
