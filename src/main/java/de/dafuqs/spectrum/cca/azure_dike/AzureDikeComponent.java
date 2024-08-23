package de.dafuqs.spectrum.cca.azure_dike;

import de.dafuqs.spectrum.*;
import dev.onyxstudios.cca.api.v3.component.tick.*;
import net.minecraft.util.*;

public interface AzureDikeComponent extends ServerTickingComponent {
	
	Identifier AZURE_DIKE_BAR_TEXTURE = SpectrumCommon.locate("textures/gui/azure_dike_overlay.png");
	
	float getCurrentProtection();
	
	float getMaxProtection();
	
	int getTicksPerPointOfRecharge();
	
	int getCurrentRechargeDelay();
	
	int getRechargeDelayTicksAfterGettingHit();
	
	float absorbDamage(float incomingDamage);
	
	void set(float maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge);
}