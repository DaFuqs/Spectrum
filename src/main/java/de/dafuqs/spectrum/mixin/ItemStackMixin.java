package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.inventories.slots.ShadowSlot;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Will be merged to fabric API: https://github.com/FabricMC/fabric/pull/1322/files#
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	// Injecting into onStackClicked instead of onClicked because onStackClicked is called first
	@Inject(at = @At("HEAD"), method = "onStackClicked", cancellable = true)
	public void onStackClicked(Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

		if (slot instanceof ShadowSlot) {
			if (((ShadowSlot) slot).onClicked((ItemStack) (Object) this, clickType, player)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V")
	public void postMine(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
		if(miner instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
		}
	}

}