package de.dafuqs.spectrum;

import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.autoconfig.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.gui.*;

@Mod(value = SpectrumCommon.MOD_ID, dist = Dist.CLIENT)
public class SpectrumClient {
	
	public SpectrumClient(IEventBus modBus, ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (modCont, parent) -> AutoConfig.getConfigScreen(SpectrumConfig.class, parent).get());
		
		modBus.addListener(SpectrumFluids::registerClient);
	}
}
