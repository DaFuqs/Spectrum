package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class VoidingPickaxeItem extends SpectrumPickaxeItem {
	
	public VoidingPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		super.postMine(stack, world, state, pos, miner);
		
		// Break the tool if it is used without the voiding enchantment
		// Otherwise this would be a VERY cheap early game diamond tier tool
		if (!world.isClient && !EnchantmentHelper.get(stack).containsKey(SpectrumEnchantments.VOIDING)) {
			stack.damage(5000, miner, (e) -> {
				e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			});
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