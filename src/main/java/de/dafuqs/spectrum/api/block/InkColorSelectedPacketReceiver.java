package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public interface InkColorSelectedPacketReceiver {
	
	void onInkColorSelectedPacket(Optional<Holder<InkColor>> inkColor);
	
	BlockEntity getBlockEntity();
	
}
