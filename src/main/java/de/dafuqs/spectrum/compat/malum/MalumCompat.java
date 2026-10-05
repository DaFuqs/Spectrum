package de.dafuqs.spectrum.compat.malum;

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
