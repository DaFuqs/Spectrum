package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.particle.render.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ParticleManager.class)
public class MixinParticleManager implements ExtendedParticleManager {

    @Unique
    private final EarlyRenderingParticleContainer spectrum$earlyRenderingParticleContainer = new EarlyRenderingParticleContainer();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void earlyRenderingHook(final CallbackInfo ci, final Particle particle) {
        spectrum$earlyRenderingParticleContainer.add(particle);
    }


    @Inject(method = "tick", at = @At("RETURN"))
    private void removeDeadHook(final CallbackInfo ci) {
        spectrum$earlyRenderingParticleContainer.removeDead();
    }

    @Override
    public void render(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Camera camera, final float tickDelta) {
        spectrum$earlyRenderingParticleContainer.render(matrices, vertexConsumers, camera, tickDelta);
    }

}