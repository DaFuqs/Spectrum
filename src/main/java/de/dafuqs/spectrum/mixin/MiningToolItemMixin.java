package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
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
	
	@Inject(at = @At("HEAD"), method = "postMine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
	public void countInertiaBlocks(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null) { // thank you, gobber
			long inertiaAmount = 0;
			
			if (SpectrumEnchantments.INERTIA.canEntityUse(miner) && EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack) > 0) {
				NbtCompound compound = stack.getOrCreateNbt();
				Identifier brokenBlockIdentifier = Registry.BLOCK.getId(state.getBlock());
				if (compound.getString("Inertia_LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
					inertiaAmount = compound.getLong("Inertia_LastMinedBlockCount") + 1;
					compound.putLong("Inertia_LastMinedBlockCount", inertiaAmount);
				} else {
					compound.putString("Inertia_LastMinedBlock", brokenBlockIdentifier.toString());
					compound.putLong("Inertia_LastMinedBlockCount", 1);
					inertiaAmount = 1;
				}
			}
			
			if (miner instanceof ServerPlayerEntity serverPlayerEntity) {
				SpectrumAdvancementCriteria.INERTIA_USED.trigger(serverPlayerEntity, state, (int) inertiaAmount);
			}
			
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getMiningSpeedMultiplier(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)F", cancellable = true)
	public void applyInertiaMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		if (stack != null) { // thank you, gobber
			int inertiaLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack);
			inertiaLevel = Math.min(4, inertiaLevel); // inertia is capped at 5 levels. Higher and the formula would do weird stuff
			if (inertiaLevel > 0) {
				NbtCompound compound = stack.getOrCreateNbt();
				Identifier brokenBlockIdentifier = Registry.BLOCK.getId(state.getBlock());
				if (compound.getString("Inertia_LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
					long lastMinedBlockCount = compound.getLong("Inertia_LastMinedBlockCount");
					double additionalSpeedPercent = 1.5 * Math.log(lastMinedBlockCount) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);
					
					cir.setReturnValue(cir.getReturnValue() * (0.5F + (float) additionalSpeedPercent));
				} else {
					cir.setReturnValue(cir.getReturnValue() / 4.0F);
				}
			}
		}
	}
	
}
