package de.dafuqs.spectrum.azure_dike;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface AzureDikeComponent extends Component, ServerTickingComponent {
    int getProtection();
    int getMaxProtection();
    float getRechargeRate();
    void damage(int usedProtection);
    void set(int maxProtection, float rechargeRate, boolean resetCharge);
}