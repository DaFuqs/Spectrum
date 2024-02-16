package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.block.entity.*;
import org.jetbrains.annotations.*;

public interface InkColorSelectedPacketReceiver {
	
	void onInkColorSelectedPacket(@Nullable InkColor inkColor);
	
	BlockEntity getBlockEntity();
	
}
