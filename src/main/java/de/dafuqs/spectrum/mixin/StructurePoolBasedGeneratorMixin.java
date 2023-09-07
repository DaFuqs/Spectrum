package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.structures.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.noise.*;
import org.apache.commons.lang3.mutable.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(StructurePoolBasedGenerator.StructurePoolGenerator.class)
public class StructurePoolBasedGeneratorMixin {
	
	@Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/HeightLimitView;Lnet/minecraft/world/gen/noise/NoiseConfig;)V", at = @At("HEAD"))
	private void spectrum$saveStructureGenerationDepth(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, boolean modifyBoundingBox, HeightLimitView world, NoiseConfig noiseConfig, CallbackInfo ci) {
		StructureGenerationDepthStore.currentDepth = minY;
	}
	
}
