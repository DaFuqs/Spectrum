package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class OblivionPickaxeItem extends SpectrumPickaxeItem {
	
	public OblivionPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		super.postMine(stack, world, state, pos, miner);
		
		// Break the tool if it is used without the voiding enchantment
		// Otherwise this would be a VERY cheap early game diamond tier tool
		if (!world.isClient && !EnchantmentHelper.get(stack).containsKey(SpectrumEnchantments.VOIDING)) {
			stack.damage(5000, miner, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}
		
		return true;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(SpectrumEnchantments.VOIDING, 1);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
}