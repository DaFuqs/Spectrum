package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.BowItem;

public class SpectrumBowItem extends BowItem {
	
	public SpectrumBowItem(Settings settings) {
		super(settings);
	}
	
	/**
	 * The higher this value, the more does the players view zoom in when using
	 * The normal bow has a zoom of 20
	 */
	public float getZoom() {
		return 30F;
	}
	
	/**
	 * The higher this value, the more velocity the shot projectile gets
	 * Note that this directly relates to damage with most projectiles, like arrows
	 * The normal bow equals a velocity mod of 1.0
	 */
	public float getProjectileVelocityModifier() {
		return 1.3F;
	}
	
	/**
	 * The lower this value, the more precise projectiles become
	 * The normal bow has uses divergence of 1.0
	 */
	public float getDivergence() {
		return 0.8F;
	}
	
}
