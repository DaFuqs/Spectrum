package de.dafuqs.spectrum.graces.crystal.component;

import de.dafuqs.spectrum.graces.crystal.ColorPool;
import de.dafuqs.spectrum.graces.crystal.CrystalEffectComponent;
import de.dafuqs.spectrum.sar.modifier.SimpleDamageMultiplier;
import net.immortaldevs.sar.api.LarvalComponentData;

public class RedUniversalDamageBuffComponent extends CrystalEffectComponent {

    public RedUniversalDamageBuffComponent() {
        super("red_universal_damage_buff", ColorPool.RED);
    }

    @Override
    public void init(LarvalComponentData data) {
        super.init(data);
        data.addModifier(new SimpleDamageMultiplier(1, (damageSource -> true), entity -> true));
        data.getOrCreateNbt().putFloat("potency", 0);
    }
}
