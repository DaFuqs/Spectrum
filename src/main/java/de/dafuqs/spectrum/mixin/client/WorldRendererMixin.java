package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import de.dafuqs.spectrum.mixin.client.accessors.GameRendererAccessor;
import de.dafuqs.spectrum.render.SpectrumRenderPhases;
import de.dafuqs.spectrum.render.SpectrumShaderPrograms;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow protected abstract void renderLayer(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix);

    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLorg/joml/Matrix4f;)V", ordinal = 2))
    private void injectBlockRenderLayersBetweenWorldBlockRenderLayerRendering(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        var pos = camera.getPos();
        ShaderProgram genericStars = SpectrumShaderPrograms.getGenericStars();

        if (genericStars.viewRotationMat != null)
            genericStars.viewRotationMat.set(RenderSystem.getInverseViewRotationMatrix());

        if (genericStars.screenSize != null) {
            Window window = MinecraftClient.getInstance().getWindow();
            genericStars.screenSize.set((float)window.getFramebufferWidth(), (float)window.getFramebufferHeight());
        }

        var cameraUniform = genericStars.getUniform("CameraPos");
        if (cameraUniform != null)
            cameraUniform.set((float) pos.x, (float) pos.y, (float) pos.z);

        var accessor = (GameRendererAccessor) gameRenderer;
        double fov = accessor.callGetFov(camera, tickDelta, true);
        var matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(gameRenderer.getBasicProjectionMatrix(fov));
        accessor.callTiltViewWhenHurt(matrixStack, tickDelta);
        spectrum$applyNauseaTransforms(matrixStack, tickDelta, accessor.getTicks());

        this.renderLayer(SpectrumRenderPhases.STARFIELD, matrices, pos.getX(), pos.getY(), pos.getZ(), matrixStack.peek().getPositionMatrix());
    }

    @Unique
    private void spectrum$applyNauseaTransforms(MatrixStack matrixStack, float tickDelta, int ticks) {
        var client = MinecraftClient.getInstance();
        float distortionScale = client.options.getDistortionEffectScale().getValue().floatValue();
        float nauseaIntensity = MathHelper.lerp(tickDelta, client.player.prevNauseaIntensity, client.player.nauseaIntensity) * distortionScale * distortionScale;
        if (nauseaIntensity > 0.0F) {
            int nauseaAmplifier = client.player.hasStatusEffect(StatusEffects.NAUSEA) ? 7 : 20;
            float scaleModifier = 5.0F / (nauseaIntensity * nauseaIntensity + 5.0F) - nauseaIntensity * 0.04F;
            scaleModifier *= scaleModifier;
            RotationAxis rotationAxis = RotationAxis.of(new Vector3f(0.0F, MathHelper.SQUARE_ROOT_OF_TWO / 2.0F, MathHelper.SQUARE_ROOT_OF_TWO / 2.0F));
            matrixStack.multiply(rotationAxis.rotationDegrees((ticks + tickDelta) * nauseaAmplifier));
            matrixStack.scale(1.0F / scaleModifier, 1.0F, 1.0F);
            float rotationAngle = -(ticks + tickDelta) * nauseaAmplifier;
            matrixStack.multiply(rotationAxis.rotationDegrees(rotationAngle));
        }
    }
}
