package de.dafuqs.spectrum.api.block;

import net.minecraft.network.chat.*;

import java.util.*;

public interface PaintbrushHoverInfo {
	
	List<Component> getPaintbrushHoverInfo(boolean includeDebugInfo);
	
}
