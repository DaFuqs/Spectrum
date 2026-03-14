package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockBowItem extends BowItem implements Preenchanted, ArrowheadBow {
	
	public BedrockBowItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.POWER, 6);
	}
	
	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return true;
	}
	
	@Override
	public float getZoom(ItemStack stack, LivingEntity shooter) {
		return 30F;
	}
	
	@Override
	public float getProjectileVelocityModifier(ItemStack stack, LivingEntity shooter) {
		return 1.3F;
	}
	
	@Override
	public float getDivergenceMod(ItemStack stack, LivingEntity shooter) {
		return 0.8F;
	}
	
}