package de.dafuqs.spectrum.compat.liminal_library;

import de.dafuqs.spectrum.deeper_down.DDDimension;
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
		LiminalEffects liminalEffects = new LiminalEffects(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new ReverbSettings().setEnabled(true).setDecayTime(8).setDensity(0.5F)));
		if(clientWorld.getDimension().getEffects().equals(DDDimension.DEEPER_DOWN_EFFECTS_ID)) {
			((DimensionEffectsAccess) clientWorld.getDimension()).setLiminalEffects(liminalEffects);
		}
	}
	
}
