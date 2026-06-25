package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.neoforge.common.util.*;

import javax.annotation.*;

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
	public boolean isEnchantable(ItemStack stack) {
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