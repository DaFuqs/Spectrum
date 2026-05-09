package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import javax.annotation.*;

import java.util.*;
import java.util.function.*;

public class MalachiteCrossbowItem extends CrossbowItem implements Preenchanted, ArrowheadCrossbow {
	
	public static final Predicate<ItemStack> PROJECTILES = (stack) -> stack.is(ItemTags.ARROWS) || stack.is(SpectrumItemTags.GLASS_ARROWS);
	
	public MalachiteCrossbowItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.PIERCING, 5);
	}
	
	public static ItemStack getFirstProjectile(ItemStack crossbow) {
		var projectiles = crossbow.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems();
		return projectiles.isEmpty() ? ItemStack.EMPTY : projectiles.getFirst();
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return SpectrumToolTiers.MALACHITE.getRepairIngredient().test(ingredient) || super.isValidRepairItem(stack, ingredient);
	}
	
	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return PROJECTILES;
	}
	
	@Override
	public float getProjectileVelocityModifier(ItemStack stack, LivingEntity shooter) {
		return 1.5F;
	}
	
	@Override
	public float getPullTimeModifier(ItemStack stack, LivingEntity shooter) {
		return 2.0F;
	}
	
	@Override
	public float getDivergenceMod(ItemStack stack, LivingEntity shooter) {
		return 0.75F;
	}
	
}
