package de.dafuqs.spectrum.graces;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.minecraft.util.Identifier;

public class GraceBearerComponent extends Component {



    @Override
    public void init(LarvalComponentData data) {
        data.loadChildren("effects");
    }
}
