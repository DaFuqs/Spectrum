package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
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
		
		if (blockState.is(SpectrumBlocks.FADING)
				&& SpectrumConfig.CONFIG.CanBottleUpFading.get()
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FADING)) {
			
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(handStack, user, SpectrumItems.BOTTLE_OF_FADING.get().getDefaultInstance()), world.isClientSide()));
			
		} else if (blockState.is(SpectrumBlocks.FAILING)
				&& SpectrumConfig.CONFIG.CanBottleUpFailing.get()
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FAILING)) {
			
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(handStack, user, SpectrumItems.BOTTLE_OF_FAILING.get().getDefaultInstance()), world.isClientSide()));
			
		} else if (blockState.is(SpectrumBlocks.RUIN)
				&& SpectrumConfig.CONFIG.CanBottleUpRuin.get()
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_RUIN)) {
			
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(handStack, user, SpectrumItems.BOTTLE_OF_RUIN.get().getDefaultInstance()), world.isClientSide()));
			
		} else if (blockState.is(SpectrumBlocks.FORFEITURE)
				&& SpectrumConfig.CONFIG.CanBottleUpForfeiture.get()
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FORFEITURE)) {
			
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(handStack, user, SpectrumItems.BOTTLE_OF_FORFEITURE.get().getDefaultInstance()), world.isClientSide()));
		}
	}
	
}
