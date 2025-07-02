package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class EnchantmentAddingModifier extends ExplosionModifier {
	
	private final ResourceKey<Enchantment> enchantment;
	private final int level;
	
	public EnchantmentAddingModifier(ExplosionModifierType type, ResourceKey<Enchantment> enchantment, int level, ParticleOptions particleEffect, int displayColor) {
		super(type, displayColor);
		this.enchantment = enchantment;
		this.level = level;
	}
	
	@Override
	public void addEnchantments(ServerLevel world, ItemStack stack) {
		Optional<Holder<Enchantment>> enchant = SpectrumEnchantmentHelper.getEntry(world.registryAccess(), enchantment);
		enchant.ifPresent(enchantmentHolder -> stack.enchant(enchantmentHolder, level));
	}
	
}
