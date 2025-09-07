package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
	
	@ModifyExpressionValue(method = "getArmPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;"))
	private static UseAnim spectrum$modifyUseAction(UseAnim original, @Local ItemStack itemStack, @Local(argsOnly = true) AbstractClientPlayer player) {
        if (itemStack.getItem() == SpectrumItems.NIGHT_SALTS) {
			return UseAnim.TOOT_HORN;
        }
        return original;
    }

    @ModifyReturnValue(method = "getArmPose", at = @At(value = "RETURN", ordinal = 1))
	private static HumanoidModel.ArmPose spectrum$modifyArmPose(HumanoidModel.ArmPose original, @Local ItemStack itemStack, @Local(argsOnly = true) AbstractClientPlayer player) {
        if (itemStack.getItem() instanceof LightGreatswordItem) {
			return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }

        return original;
    }

    @ModifyReturnValue(method = "getArmPose", at = @At(value = "TAIL"))
	private static HumanoidModel.ArmPose spectrum$lungeAction(HumanoidModel.ArmPose original, @Local(argsOnly = true) AbstractClientPlayer player) {
        if (MiscPlayerDataComponent.get(player).isLunging()) {
			return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }

        return original;
    }
}
