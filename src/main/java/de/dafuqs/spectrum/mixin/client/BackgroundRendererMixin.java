package de.dafuqs.spectrum.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
	
	/*@Inject(at=@At("HEAD"), method= "applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZ)V")
	public void spectrum$modifyFluidFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
		//TODO
		// CameraSubmersionType.LAVA
		// CameraSubmersionType.WATER
	}*/
	
}