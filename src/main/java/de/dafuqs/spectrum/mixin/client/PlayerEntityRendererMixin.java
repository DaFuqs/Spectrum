package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.attachment_types.*;
import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
	
	@ModifyReturnValue(method = "getArmPose", at = @At(value = "TAIL"))
	private static HumanoidModel.ArmPose spectrum$lungeAction(HumanoidModel.ArmPose original, @Local(argsOnly = true) AbstractClientPlayer player) {
		if (MiscPlayerDataAttachmentType.get(player).isLunging()) {
			return HumanoidModel.ArmPose.BOW_AND_ARROW;
		}
		
		return original;
	}
}
