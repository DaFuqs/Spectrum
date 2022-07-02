package de.dafuqs.spectrum.graces.crystal;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import org.jetbrains.annotations.NotNull;

public class CrystalGraceComponent extends Component {

    @Override
    public void init(@NotNull LarvalComponentData data) {
        super.init(data);
        data.loadChildren("crystalEffects");
        var dataNbt = data.getOrCreateNbt();
        dataNbt.putFloat("aberration", 0);
        dataNbt.putFloat("vitreous-radiance", 0);
        dataNbt.putFloat("absorption", 0);
        dataNbt.putBoolean("genRequired", true);
    }
}
