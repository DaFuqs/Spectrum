package de.dafuqs.spectrum;

import de.dafuqs.spectrum.config.*;
import me.shedaniel.autoconfig.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.gui.*;

@Mod(value = SpectrumCommon.MOD_ID, dist = Dist.CLIENT)
public class SpectrumClient {
	public SpectrumClient(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class,
				(modCont, parent) -> AutoConfig.getConfigScreen(SpectrumConfig.class, parent).get());
	}
}
