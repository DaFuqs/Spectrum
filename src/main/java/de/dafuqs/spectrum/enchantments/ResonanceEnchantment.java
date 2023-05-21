package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ResonanceEnchantment extends SpectrumEnchantment {
	
	public ResonanceEnchantment(Enchantment.Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 25;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 75;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != SpectrumEnchantments.FOUNDRY && other != Enchantments.FORTUNE;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof ShearsItem;
	}
	
	public static boolean checkResonanceForSpawnerMining(World world, BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
		if (blockState.isIn(SpectrumBlockTags.SPAWNERS) && EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
				ItemStack itemStack = SpectrumMobSpawnerItem.toItemStack(mobSpawnerBlockEntity);
				
				Block.dropStack(world, pos, itemStack);
				world.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				return true;
			}
		}
		return false;
	}
	
}