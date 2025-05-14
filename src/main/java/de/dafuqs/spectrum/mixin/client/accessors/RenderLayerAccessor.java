package de.dafuqs.spectrum.mixin.client.accessors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(RenderType.class)
public interface RenderLayerAccessor {
	
	@Invoker
	static RenderType.CompositeRenderType invokeCreate(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderType.CompositeState phases) {
		throw new IllegalStateException();
	}
	
}