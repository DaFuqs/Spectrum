package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.resources.*;

public abstract class PastelPayloadType {
	
	protected final ResourceLocation id;
	protected final MapCodec<? extends PastelPayload> codec;
	
	public PastelPayloadType(ResourceLocation id, MapCodec<? extends PastelPayload> codec) {
		this.id = id;
		this.codec = codec;
	}
	
	public abstract void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode);
	
}