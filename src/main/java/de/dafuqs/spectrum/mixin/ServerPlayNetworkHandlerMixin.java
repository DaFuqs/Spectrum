package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.MergeableItem;
import de.dafuqs.spectrum.api.item.SplittableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {


    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", ordinal = 0), cancellable = true)
    private void handleSwapInteractions(PlayerActionC2SPacket packet, CallbackInfo ci) {

        var mainStack = player.getStackInHand(Hand.MAIN_HAND);
        var offStack = player.getStackInHand(Hand.OFF_HAND);
        var mainItem = mainStack.getItem();
        var offItem = offStack.getItem();

        if (mainItem instanceof SplittableItem splittable && splittable.canSplit(player, Hand.MAIN_HAND, mainStack)) {
           splitItem(mainStack, splittable);
            ci.cancel();
        }
        else if (offItem instanceof SplittableItem splittable && splittable.canSplit(player, Hand.OFF_HAND, offStack)) {
            splitItem(offStack, splittable);
            ci.cancel();
        }
        else if(mainItem instanceof MergeableItem mergeable && offItem instanceof MergeableItem && mergeable.canMerge(player, mainStack, offStack)) {
           mergeItems(mainStack, offStack, mergeable);
            ci.cancel();
        }
    }

    @Unique
    private void splitItem(ItemStack stack, SplittableItem splittable) {
        var split = splittable.getResult(player, stack);
        player.setStackInHand(Hand.MAIN_HAND, split);
        player.setStackInHand(Hand.OFF_HAND, split.copy());
        player.clearActiveItem();
        player.playSound(splittable.getSplitSound(), SoundCategory.PLAYERS, 1, 0.8F + player.getRandom().nextFloat() * 0.4F);
    }

    @Unique
    private void mergeItems(ItemStack firstHalf, ItemStack secondHalf, MergeableItem mergeable) {
        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        player.setStackInHand(Hand.MAIN_HAND, mergeable.getResult(player, firstHalf, secondHalf));
        player.clearActiveItem();
        player.playSound(mergeable.getMergeSound(), SoundCategory.PLAYERS, 1, 0.8F + player.getRandom().nextFloat() * 0.4F);
    }
}
