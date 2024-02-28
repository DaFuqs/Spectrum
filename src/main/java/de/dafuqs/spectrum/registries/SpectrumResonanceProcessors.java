package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.data_loaders.resonance_processors.*;

public class SpectrumResonanceProcessors {
	
	public static void register() {
		ResonanceDropProcessors.register(SpectrumCommon.locate("drop_self"), new DropSelfResonanceProcessor.Serializer());
		ResonanceDropProcessors.register(SpectrumCommon.locate("modify_drops"), new ModifyDropsResonanceProcessor.Serializer());
	}
	
}
