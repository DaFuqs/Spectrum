package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.enchantment.*;
import net.minecraft.particle.*;

public class EnchantmentAddingModifier extends ExplosionModifier {
	
	private final Enchantment enchantment;
	private final int level;
	
	public EnchantmentAddingModifier(ExplosionModifierType type, Enchantment enchantment, int level, ParticleEffect particleEffect, int displayColor) {
		super(type, displayColor);
		this.enchantment = enchantment;
		this.level = level;
	}
	
	
}
