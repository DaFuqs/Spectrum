package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.network.*;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @ModifyExpressionValue(method = "getArmPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/util/UseAction;"))
    private static UseAction modifyUseAction(UseAction original, @Local ItemStack itemStack, @Local(argsOnly = true) AbstractClientPlayerEntity player) {
        if (itemStack.getItem() == SpectrumItems.NIGHT_SALTS) {
            return UseAction.TOOT_HORN;
        }
        return original;
    }

    @ModifyReturnValue(method = "getArmPose", at = @At(value = "RETURN", ordinal = 1))
    private static BipedEntityModel.ArmPose cumAction(BipedEntityModel.ArmPose original, @Local ItemStack itemStack, @Local(argsOnly = true) AbstractClientPlayerEntity player) {
        if (itemStack.getItem() instanceof LightGreatswordItem) {
            return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        }

        return original;
    }

    @ModifyReturnValue(method = "getArmPose", at = @At(value = "TAIL"))
    private static BipedEntityModel.ArmPose lungeAction(BipedEntityModel.ArmPose original, @Local(argsOnly = true) AbstractClientPlayerEntity player) {
        if (MiscPlayerDataComponent.get(player).isLunging()) {
            return BipedEntityModel.ArmPose.BOW_AND_ARROW;
        }

        return original;
    }
}
