package de.dafuqs.spectrum.mixin.accessors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.shapes.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LevelRenderer.class)
public interface WorldRendererAccessor {
	
	@Invoker
	static void invokeRenderShape(PoseStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
		throw new AssertionError();
	}
}