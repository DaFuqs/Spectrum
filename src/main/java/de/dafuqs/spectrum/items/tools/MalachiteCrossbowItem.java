package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tag.*;
import net.minecraft.util.collection.*;

import java.util.*;
import java.util.function.*;

public class MalachiteCrossbowItem extends CrossbowItem implements Preenchanted, ArrowheadCrossbow {
	
	public static final Predicate<ItemStack> PROJECTILES = (stack) -> stack.isIn(ItemTags.ARROWS) || stack.isIn(SpectrumItemTags.GLASS_ARROWS);
	
	public MalachiteCrossbowItem(Settings settings) {
        super(settings);
    }
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.PIERCING, 5);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
	public static ItemStack getFirstProjectile(ItemStack crossbow) {
		NbtCompound nbtCompound = crossbow.getNbt();
		if (nbtCompound != null && nbtCompound.contains("ChargedProjectiles", 9)) {
			NbtList nbtList = nbtCompound.getList("ChargedProjectiles", 10);
			if (nbtList != null && nbtList.size() > 0) {
				NbtCompound nbtCompound2 = nbtList.getCompound(0);
				return ItemStack.fromNbt(nbtCompound2);
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public Predicate<ItemStack> getProjectiles() {
		return PROJECTILES;
	}

	@Override
	public float getProjectileVelocityModifier(ItemStack stack) {
		return 1.25F;
	}

	@Override
	public float getPullTimeModifier(ItemStack stack) {
		return 1.0F;
	}

	@Override
	public float getDivergenceMod(ItemStack stack) {
		return 0.75F;
	}
	
}
