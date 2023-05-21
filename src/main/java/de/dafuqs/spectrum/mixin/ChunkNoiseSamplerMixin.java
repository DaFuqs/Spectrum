package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import de.dafuqs.spectrum.deeper_down.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.*;
import net.minecraft.world.gen.noise.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ChunkNoiseSampler.class)
public abstract class ChunkNoiseSamplerMixin {
	
	@Inject(method = "<init>(ILnet/minecraft/world/gen/noise/NoiseConfig;IILnet/minecraft/world/gen/chunk/GenerationShapeConfig;Lnet/minecraft/world/gen/densityfunction/DensityFunctionTypes$Beardifying;Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/world/gen/chunk/AquiferSampler$FluidLevelSampler;Lnet/minecraft/world/gen/chunk/Blender;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;oreVeins()Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void spectrum$init(int horizontalCellCount, NoiseConfig noiseConfig, int startX, int startZ, GenerationShapeConfig generationShapeConfig, DensityFunctionTypes.Beardifying beardifying, ChunkGeneratorSettings chunkGeneratorSettings, AquiferSampler.FluidLevelSampler fluidLevelSampler, Blender blender, CallbackInfo ci, NoiseRouter noiseRouter, NoiseRouter noiseRouter2, ImmutableList.Builder builder, DensityFunction densityFunction) {
		if (chunkGeneratorSettings.defaultBlock() == Blocks.DEEPSLATE.getDefaultState() && chunkGeneratorSettings.generationShapeConfig().minimumY() == -448) {

			builder.add(DDOreVeinSampler.create(noiseRouter.veinToggle(), noiseRouter.veinRidged(), noiseRouter.veinGap(), noiseConfig.getOreRandomDeriver()));
		}
	}
	
}
