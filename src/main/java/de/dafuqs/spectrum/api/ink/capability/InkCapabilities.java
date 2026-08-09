package de.dafuqs.spectrum.api.ink.capability;

import de.dafuqs.spectrum.*;
import net.neoforged.neoforge.capabilities.*;

public class InkCapabilities {
	
	public static final BlockCapability<InkCapability, Void> BLOCK =
			BlockCapability.create(
					SpectrumCommon.locate("ink_handler"),
					InkCapability.class,
					Void.class
			);
	
	public static final ItemCapability<InkCapability, Void> ITEM =
			ItemCapability.create(
					SpectrumCommon.locate("ink_handler"),
					InkCapability.class,
					Void.class
			);
	
}
