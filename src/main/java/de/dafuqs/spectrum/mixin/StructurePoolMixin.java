package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.structures.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(StructurePool.class)
public class StructurePoolMixin {
	
	@Shadow
	@Final
	private ObjectArrayList<StructurePoolElement> elements;
	
	@Inject(method = "getElementIndicesInRandomOrder(Lnet/minecraft/util/math/random/Random;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	private void spectrum$saveStructureGenerationDepth(Random random, CallbackInfoReturnable<List<StructurePoolElement>> cir) {
		ObjectArrayList<StructurePoolElement> list = new ObjectArrayList<>();
		boolean depthAwareIncluded = false;
		for (StructurePoolElement e : this.elements) {
			if (e instanceof DepthAwareSinglePoolElement depthAwareSinglePoolElement) {
				depthAwareIncluded = true;
				if (StructureGenerationDepthStore.currentDepth > depthAwareSinglePoolElement.getMaxDepth()) {
					continue;
				}
			}
			list.add(e);
			
			if (depthAwareIncluded) {
				// only cancel on spectrum's pools in case of mod compat issues
				cir.setReturnValue(Util.copyShuffled(list, random));
			}
		}
	}
	
}
