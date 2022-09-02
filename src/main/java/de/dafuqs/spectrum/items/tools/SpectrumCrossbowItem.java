package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.CrossbowItem;

public class SpectrumCrossbowItem extends CrossbowItem {
	
	public SpectrumCrossbowItem(Settings settings) {
		super(settings);
	}
	
	/**
	 * The higher this value, the more velocity the shot projectile gets
	 * Note that this directly relates to damage with most projectiles, like arrows
	 * The normal crossbow equals a velocity mod of 1.0
	 */
	public float getProjectileVelocityModifier() {
		return 1.5F;
	}
	
	/**
	 * The higher this value, the longer it takes for the crossbow to load
	 * The normal crossbow equals a pull time mod of 1.0
	 */
	public float getPullTimeModifier() {
		return 2.0F;
	}
	
}
