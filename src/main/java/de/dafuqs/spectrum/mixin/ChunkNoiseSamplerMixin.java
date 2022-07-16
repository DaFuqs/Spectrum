package de.dafuqs.spectrum.mixin;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.deeper_down.DDOreVeinSampler;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkNoiseSampler.class)
public abstract class ChunkNoiseSamplerMixin {
	
	@Shadow protected abstract DensityFunction method_40529(DensityFunction densityFunction);
	
	@Inject(method = "<init>(IIILnet/minecraft/world/gen/noise/NoiseRouter;IILnet/minecraft/world/gen/densityfunction/DensityFunctionTypes$class_7050;Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/world/gen/chunk/AquiferSampler$FluidLevelSampler;Lnet/minecraft/world/gen/chunk/Blender;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;oreVeins()Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void randomTick(int horizontalSize, int height, int minimumY, NoiseRouter noiseRouter, int x, int z, DensityFunctionTypes.class_7050 noiseType, ChunkGeneratorSettings chunkGeneratorSettings, AquiferSampler.FluidLevelSampler fluidLevelSampler, Blender blender, CallbackInfo callbackInfo, ImmutableList.Builder<ChunkNoiseSampler.BlockStateSampler> builder) {
		if (chunkGeneratorSettings.defaultBlock() == Blocks.DEEPSLATE.getDefaultState()) {
			builder.add(DDOreVeinSampler.create(noiseRouter.veinToggle().apply(this::method_40529), noiseRouter.veinRidged().apply(this::method_40529), noiseRouter.veinGap().apply(this::method_40529), noiseRouter.oreVeinsPositionalRandomFactory()));
		}
	}
	
}
