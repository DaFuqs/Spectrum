package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class OblivionPickaxeItem extends SpectrumPickaxeItem {
	
	public OblivionPickaxeItem(Tier material, Properties settings) {
		super(material, settings);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
		super.mineBlock(stack, world, state, pos, miner);
		
		// Break the tool if it is used without the voiding enchantment
		// Otherwise this would be a VERY cheap early game diamond tier tool
		if (!world.isClientSide && !EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.NO_BLOCK_DROPS)) {
			stack.hurtAndBreak(5000, miner, EquipmentSlot.MAINHAND);
		}
		
		return true;
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(SpectrumEnchantments.VOIDING, 1);
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
}
