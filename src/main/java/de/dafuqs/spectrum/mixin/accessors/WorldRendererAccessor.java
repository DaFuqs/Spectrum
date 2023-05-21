package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.shape.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
	
	@Invoker("drawCuboidShapeOutline")
	static void invokeDrawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
		throw new AssertionError();
	}
}