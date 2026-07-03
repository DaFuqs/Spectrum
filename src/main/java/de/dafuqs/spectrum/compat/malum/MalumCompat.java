package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.registry.common.block.*;
import com.sammy.malum.registry.common.item.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register(IEventBus modBus) {
		
	}
	
	@OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(FMLClientSetupEvent event) {
    
    }
}
