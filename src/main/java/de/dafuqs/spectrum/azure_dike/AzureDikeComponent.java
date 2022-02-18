package de.dafuqs.spectrum.azure_dike;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface AzureDikeComponent extends Component, ServerTickingComponent {
    int getProtection();
    int getMaxProtection();
    int getRechargeDelayDefault();
    int getCurrentRechargeDelay();
    int getRechargeDelayTicksAfterDamage();
    
    float absorbDamage(float incomingDamage);
    void set(int maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge);
}