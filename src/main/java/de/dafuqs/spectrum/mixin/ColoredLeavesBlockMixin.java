package de.dafuqs.spectrum.mixin;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.blocks.conditional.ColoredLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.block.IHornHarvestable;

@Mixin(ColoredLeavesBlock.class)
public abstract class ColoredLeavesBlockMixin implements IHornHarvestable {
	
	@Override
	public boolean hasSpecialHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return true;
	}
	
	@Override
	public boolean hasSpecialHornHarvest(World level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		return true;
	}
	
	@Override
	public boolean canHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}
	
	@Override
	public boolean canHornHarvest(World level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		if(hornType == EnumHornType.CANOPY && user instanceof PlayerEntity player) {
			BlockState blockState = level.getBlockState(pos);
			if (blockState.getBlock() instanceof ColoredLeavesBlock coloredLeavesBlock) {
				return AdvancementHelper.hasAdvancement(player, coloredLeavesBlock.getCloakAdvancementIdentifier());
			}
		}
		return false;
	}
	
	@Override
	public void harvestByHorn(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		this.harvestByHorn(world, pos, stack, hornType, null);
	}
	
	@Override
	public void harvestByHorn(World level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		BlockState blockState = level.getBlockState(pos);
		BlockEntity blockEntity = level.getBlockEntity(pos);
		Block.dropStacks(blockState, level, pos, blockEntity, user, stack);
	}
	
}
