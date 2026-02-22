package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.registry.common.block.*;
import com.sammy.malum.registry.common.item.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;
import net.neoforged.api.distmarker.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register() {
		
	}
	
	@OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient() {
    
    }
}
