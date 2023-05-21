package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.*;
import net.minecraft.block.entity.*;
import org.jetbrains.annotations.*;

public interface InkColorSelectedPacketReceiver {
	
	void onInkColorSelectedPacket(@Nullable InkColor inkColor);
	
	BlockEntity getBlockEntity();
	
}
