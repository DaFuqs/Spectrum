package de.dafuqs.spectrum.graces;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;

public class GraceBearerComponent extends Component {



    @Override
    public void init(LarvalComponentData data) {
        data.loadChildren("effects");
    }
}
