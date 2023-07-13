package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static de.dafuqs.spectrum.enchantments.InertiaEnchantment.*;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin {

	@Inject(at = @At("HEAD"), method = "postMine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
	public void countInertiaBlocks(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null) { // thank you, gobber
			long inertiaAmount = 0;

			if (SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.INERTIA, stack, miner) > 0) {
				NbtCompound compound = stack.getOrCreateNbt();
				Identifier brokenBlockIdentifier = Registries.BLOCK.getId(state.getBlock());
				if (compound.getString("Inertia_LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
					inertiaAmount = compound.getLong(INERTIA_COUNT) + 1;
					compound.putLong(INERTIA_COUNT, inertiaAmount);
				} else {
					compound.putString(INERTIA_BLOCK, brokenBlockIdentifier.toString());
					compound.putLong(INERTIA_COUNT, 1);
					inertiaAmount = 1;
				}
			}

			if (miner instanceof ServerPlayerEntity serverPlayerEntity) {
				SpectrumAdvancementCriteria.INERTIA_USED.trigger(serverPlayerEntity, state, (int) inertiaAmount);
			}

		}
	}

	@ModifyReturnValue(method = "getMiningSpeedMultiplier(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)F", at = @At("RETURN"))
	public float applyMiningSpeedMultipliers(float original, ItemStack stack, BlockState state) {
		if (stack != null) { // thank you, gobber

			// INERTIA GAMING
			int inertiaLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack);
			inertiaLevel = Math.min(4, inertiaLevel); // inertia is capped at 5 levels. Higher and the formula would do weird stuff
			if (inertiaLevel > 0) {
				NbtCompound compound = stack.getOrCreateNbt();
				Identifier brokenBlockIdentifier = Registries.BLOCK.getId(state.getBlock());
				if (compound.getString(INERTIA_BLOCK).equals(brokenBlockIdentifier.toString())) {
					long lastMinedBlockCount = compound.getLong(INERTIA_COUNT);
					double additionalSpeedPercent = 2.0 * Math.log(lastMinedBlockCount) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);
					
					original = original * (0.5F + (float) additionalSpeedPercent);
				} else {
					original = original / 4;
				}
			}
			
			// RAZING GAMING
			int razingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.RAZING, stack);
			if (razingLevel > 0) {
				float hardness = state.getBlock().getHardness();
				original = (float) Math.max(1 + hardness, Math.pow(2, 1 + razingLevel / 8F));
			}

		}

		return original;
	}

}
