package de.dafuqs.spectrum.mixin;

import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkNoiseSampler.class)
public abstract class ChunkNoiseSamplerMixin {
	
	// TODO: readd
	@Shadow protected abstract DensityFunction getActualDensityFunction(DensityFunction densityFunction);
	
	/*@Inject(method = "<init>(ILnet/minecraft/world/gen/noise/NoiseConfig;IILnet/minecraft/world/gen/chunk/GenerationShapeConfig;Lnet/minecraft/world/gen/densityfunction/DensityFunctionTypes$Beardifying;Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/world/gen/chunk/AquiferSampler$FluidLevelSampler;Lnet/minecraft/world/gen/chunk/Blender;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;oreVeins()Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$init(int horizontalCellCount, NoiseConfig noiseConfig, int startX, int startZ, GenerationShapeConfig generationShapeConfig, DensityFunctionTypes.Beardifying beardifying, ChunkGeneratorSettings chunkGeneratorSettings, AquiferSampler.FluidLevelSampler fluidLevelSampler, Blender blender, CallbackInfo ci, ImmutableList.Builder<ChunkNoiseSampler.BlockStateSampler> builder) {
		if (chunkGeneratorSettings.defaultBlock() == Blocks.DEEPSLATE.getDefaultState()) {
			NoiseRouter noiseRouter = noiseConfig.getNoiseRouter();
			builder.add(DDOreVeinSampler.create(noiseRouter.veinToggle(), noiseRouter.veinRidged(), noiseRouter.veinGap(), noiseConfig.getOreRandomDeriver()));
		}
	}*/
	
}
