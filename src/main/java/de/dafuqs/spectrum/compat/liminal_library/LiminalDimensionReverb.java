package de.dafuqs.spectrum.compat.liminal_library;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class LiminalDimensionReverb {
	
	public static void setReverbForClientDimension(ClientWorld clientWorld) {
		// TODO: enable dimension reverb when DD is released
		/*if(clientWorld.getDimension().effects().equals(DDDimension.DEEPER_DOWN_EFFECTS_ID)) {
			LiminalEffects liminalEffects = new LiminalEffects(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new ReverbSettings().setEnabled(true).setDecayTime(8).setDensity(0.5F)));
			((DimensionTypeAccess)(Object) clientWorld.getDimension()).setLiminalEffects(liminalEffects);
		}*/
	}
	
}
