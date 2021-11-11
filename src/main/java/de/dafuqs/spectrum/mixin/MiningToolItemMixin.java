package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

	@Inject(at=@At("HEAD"), method= "postMine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
	public void countInertiaBlocks(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
		if(EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack) > 0) {
			NbtCompound compound = stack.getOrCreateNbt();
			Identifier brokenBlockIdentifier = Registry.BLOCK.getId(state.getBlock());
			if(compound.getString("LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
				compound.putLong("LastMinedBlockCount", compound.getLong("LastMinedBlockCount") + 1);
			} else {
				compound.putString("LastMinedBlock", brokenBlockIdentifier.toString());
				compound.putLong("LastMinedBlockCount", 1);
			}

		}
	}

	@Inject(at = @At("RETURN"), method= "getMiningSpeedMultiplier(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)F", cancellable = true)
	public void applyInertiaMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		int inertiaLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack);
		if(inertiaLevel > 0) {
			NbtCompound compound = stack.getOrCreateNbt();
			Identifier brokenBlockIdentifier = Registry.BLOCK.getId(state.getBlock());
			if(compound.getString("LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
				long lastMinedBlockCount = compound.getLong("LastMinedBlockCount");
				double additionalSpeedPercent = Math.log(lastMinedBlockCount) / Math.log(Math.max(2, (6-inertiaLevel) * (6-inertiaLevel)));
				cir.setReturnValue(cir.getReturnValue() * (0.5F + (float) additionalSpeedPercent)); // (float) Math.log10(lastMinedBlockCount));
			} else {
				cir.setReturnValue(cir.getReturnValue() / 4.0F);
			}
		}
	}

}
