package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.level.saveddata.maps.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MapRenderer.MapInstance.class)
public class MapRendererMapTextureMixin {
	
	@WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
	public void scaleDecorations(PoseStack instance, float x, float y, float z, Operation<Void> original, @Local MapDecoration decoration) {
		float scale = (float) ((int) decoration.spectrum$getScale() + 128) / 128;
		original.call(instance, 4f * scale, 4f * scale, 3f * scale);
	}
	
}
