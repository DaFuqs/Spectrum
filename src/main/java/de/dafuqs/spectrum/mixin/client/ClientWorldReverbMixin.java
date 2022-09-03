package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.liminal_library.LiminalDimensionReverb;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldReverbMixin {
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void spectrum$init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry registryEntry, int loadDistance, int simulationDistance, Supplier profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
		ClientWorld clientWorld = (ClientWorld) (Object) this;
		if(clientWorld.getDimension().getEffects().equals(SpectrumCommon.locate("deeper_down")) && FabricLoader.getInstance().isModLoaded("limlib")) {
			LiminalDimensionReverb.setReverbForClientDimension(clientWorld);
		}
	}
	
}
