package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    @Invoker
    double callGetFov(Camera camera, float tickDelta, boolean changingFov);

    @Invoker
    void callTiltViewWhenHurt(MatrixStack matrices, float tickDelta);

    @Accessor
    int getTicks();
}
