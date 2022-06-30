package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface InkColorSelectedPacketReceiver {
	
	void onInkColorSelectedPacket(@Nullable InkColor inkColor);
	
	BlockEntity getBlockEntity();
	
}
