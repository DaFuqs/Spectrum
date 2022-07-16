package de.dafuqs.spectrum.compat.liminal_library;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.LiminalEffects;
import net.ludocrypt.limlib.api.sound.ReverbSettings;
import net.minecraft.client.world.ClientWorld;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class LiminalDimensionReverb {
	
	public static void setReverbForClientDimension(ClientWorld clientWorld) {
		LiminalEffects liminalEffects = new LiminalEffects(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new ReverbSettings().setEnabled(true).setDecayTime(10).setDensity(0.75F)));
		((DimensionEffectsAccess) clientWorld.getDimension()).setLiminalEffects(liminalEffects);
	}
	
}
