package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.NoiseChunk.*;
import net.minecraft.world.level.levelgen.blending.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(NoiseChunk.class)
public abstract class ChunkNoiseSamplerMixin {
	
	@Inject(method = "<init>",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;oreVeinsEnabled()Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$init(int cellCountXZ, RandomState random, int firstNoiseX, int firstNoiseZ, NoiseSettings noiseSettings, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blendifier, CallbackInfo ci, NoiseRouter noiseRouter, NoiseRouter noiseRouter2, ImmutableList.Builder<BlockStateFiller> builder, DensityFunction densityFunction) {
		if (noiseGeneratorSettings.defaultBlock() == SpectrumBlocks.BLACKSLAG.defaultBlockState()) {
			builder.add(DDOreVeinSampler.create(noiseRouter.veinToggle(), noiseRouter.veinRidged(), noiseRouter.veinGap(), random.oreRandom()));
		}
	}
	
}
