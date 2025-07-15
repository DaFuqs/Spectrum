package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.particle.render.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ParticleEngine.class)
public class MixinParticleManager implements ExtendedParticleManager {
	
	@Unique
	private final EarlyRenderingParticleContainer spectrum$earlyRenderingParticleContainer = new EarlyRenderingParticleContainer();
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
	private void earlyRenderingHook(final CallbackInfo ci, @Local final Particle particle) {
		spectrum$earlyRenderingParticleContainer.add(particle);
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void removeDeadHook(final CallbackInfo ci) {
		spectrum$earlyRenderingParticleContainer.removeDead();
	}
	
	@Override
	public void render(final PoseStack matrices, final MultiBufferSource vertexConsumers, final Camera camera, final float tickDelta) {
		spectrum$earlyRenderingParticleContainer.render(matrices, vertexConsumers, camera, tickDelta);
	}
	
}