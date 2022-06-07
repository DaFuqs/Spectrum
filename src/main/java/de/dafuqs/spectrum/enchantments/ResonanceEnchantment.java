package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.items.SpectrumMobSpawnerItem;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResonanceEnchantment extends SpectrumEnchantment {
	
	public ResonanceEnchantment(Enchantment.Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static boolean checkResonanceForSpawnerMining(World world, BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
		if (blockState.equals(Blocks.SPAWNER.getDefaultState())) {
			if (EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
				if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
					ItemStack itemStack = SpectrumMobSpawnerItem.toItemStack(mobSpawnerBlockEntity);
					
					Block.dropStack(world, pos, itemStack);
					world.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					return true;
				}
			}
		}
		return false;
	}
	
	public int getMinPower(int level) {
		return 15;
	}
	
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 50;
	}
	
	public int getMaxLevel() {
		return 1;
	}
	
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != SpectrumEnchantments.FOUNDRY && other != Enchantments.FORTUNE;
	}
	
}