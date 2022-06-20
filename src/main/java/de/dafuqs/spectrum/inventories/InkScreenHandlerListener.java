package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.screen.ScreenHandler;

public interface InkScreenHandlerListener extends LongPropertyDelegate {
    
    void onPropertyUpdate(ScreenHandler handler, InkColor property, long value);
    
}
