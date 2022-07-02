package de.dafuqs.spectrum.graces.crystal;

import de.dafuqs.spectrum.sar.modifier.SimpleDamageMultiplier;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;

public abstract class CrystalEffectComponent extends Component {

    public final String name;
    public final ColorPool pool;

    public CrystalEffectComponent(String name, ColorPool pool) {
        this.name = name;
        this.pool = pool;
    }
}
