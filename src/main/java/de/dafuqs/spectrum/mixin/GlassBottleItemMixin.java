package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BottleItem.class)
public abstract class GlassBottleItemMixin {
	
	@Shadow
	protected abstract ItemStack turnBottleIntoItem(ItemStack stack, Player player, ItemStack outputStack);
	
	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
	public void onUse(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local ItemStack handStack, @Local BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		
		if (blockState.is(SpectrumBlocks.FADING) && SpectrumConfig.CONFIG.CanBottleUpFading.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FADING)) {
			cir.setReturnValue(spectrum$bottleUpDecay(world, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_FADING.get()));
		} else if (blockState.is(SpectrumBlocks.FAILING) && SpectrumConfig.CONFIG.CanBottleUpFailing.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FAILING)) {
			cir.setReturnValue(spectrum$bottleUpDecay(world, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_FAILING.get()));
		} else if (blockState.is(SpectrumBlocks.RUIN) && SpectrumConfig.CONFIG.CanBottleUpRuin.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_RUIN)) {
			cir.setReturnValue(spectrum$bottleUpDecay(world, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_RUIN.get()));
		} else if (blockState.is(SpectrumBlocks.FORFEITURE) && SpectrumConfig.CONFIG.CanBottleUpForfeiture.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FORFEITURE)) {
			cir.setReturnValue(spectrum$bottleUpDecay(world, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_FORFEITURE.get()));
		}
	}
	
	@Unique
	private InteractionResultHolder<ItemStack> spectrum$bottleUpDecay(Level world, Player user, @Local ItemStack handStack, @Local BlockPos blockPos, BlockState blockState, Item item) {
		if(!world.isClientSide) {
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			blockState.spawnAfterBreak((ServerLevel) world, blockPos, handStack, false);
		}
		
		world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
		return InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(handStack, user, item.getDefaultInstance()), world.isClientSide());
	}
	
}
