package de.dafuqs.spectrum.mixin.client.accessors;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(RenderLayer.class)
public interface RenderLayerAccessor {
	
	@Invoker
	static RenderLayer.MultiPhase invokeOf(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases) {
		throw new IllegalStateException();
	}

    @Accessor()
    @Mutable
    static ImmutableList<RenderLayer> getBLOCK_LAYERS() {
		throw new IllegalStateException();
	}

    @Accessor()
    @Mutable
    static void setBLOCK_LAYERS(ImmutableList<RenderLayer> blockLayers) {
		throw new IllegalStateException();
	}
}