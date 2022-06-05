package de.dafuqs.spectrum.azure_dike;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.util.Identifier;

public interface AzureDikeComponent extends Component, ServerTickingComponent {
	
	Identifier AZURE_DIKE_BAR_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/azure_dike_overlay.png");
	
	int getProtection();
	
	int getMaxProtection();
	
	int getRechargeDelayDefault();
	
	int getCurrentRechargeDelay();
	
	int getRechargeDelayTicksAfterDamage();
	
	float absorbDamage(float incomingDamage);
	
	void set(int maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge);
}