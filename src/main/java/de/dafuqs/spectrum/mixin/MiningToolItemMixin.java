package de.dafuqs.spectrum.mixin;

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
	public void applyMiningSpeedMultipliers(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		if (stack != null) { // thank you, gobber

			// INERTIA GAMING

			int inertiaLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack);
			inertiaLevel = Math.min(4, inertiaLevel); // inertia is capped at 5 levels. Higher and the formula would do weird stuff
			if (inertiaLevel > 0) {
				NbtCompound compound = stack.getOrCreateNbt();
				Identifier brokenBlockIdentifier = Registries.BLOCK.getId(state.getBlock());
				if (compound.getString("Inertia_LastMinedBlock").equals(brokenBlockIdentifier.toString())) {
					long lastMinedBlockCount = compound.getLong("Inertia_LastMinedBlockCount");
					double additionalSpeedPercent = 1.5 * Math.log(lastMinedBlockCount) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);

					cir.setReturnValue(cir.getReturnValue() * (0.5F + (float) additionalSpeedPercent));
				} else {
					cir.setReturnValue(cir.getReturnValue() / 4.0F);
				}
			}

			// CRUMBLING GAMING

			var crumbling = EnchantmentHelper.getLevel(SpectrumEnchantments.RAZING, stack);
			if (crumbling > 0) {
				var hardness = state.getBlock().getHardness();
				var effectiveness = state.isIn(SpectrumBlockTags.CRUMBLING_SUPER_EFFECTIVE) ? 4.125F : 0.255F;
				float speedMod;

				if (hardness <= 0) {
					speedMod = 0F;
				} else if (hardness < 13) {
					speedMod = (float) (Math.pow(1.225, hardness) - 0.75);
				} else {
					speedMod = (float) (((hardness - 12) / Math.sqrt(1 + Math.pow(hardness, 2.5) / 10000)) * 10);
					effectiveness = 0.375F;
				}

				if (cir.getReturnValue() <= 1)
					speedMod *= 0.25F;

				cir.setReturnValue(Math.max(cir.getReturnValue() * speedMod * effectiveness, 0.125F));
			}
		}
	}

}
