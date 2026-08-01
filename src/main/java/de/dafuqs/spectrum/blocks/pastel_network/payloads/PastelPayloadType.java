package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.resources.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public abstract class PastelPayloadType {
	
	protected final ResourceLocation id;
	protected final MapCodec<? extends PastelPayload> codec;
	
	public PastelPayloadType(ResourceLocation id, MapCodec<? extends PastelPayload> codec) {
		this.id = id;
		this.codec = codec;
	}
	
	public abstract DeferredHolder<PastelPayloadType, ?> getPayloadType();
	
	public boolean runsEveryTick() {
		return false;
	}
	
	public void tick(PastelTransmissionLogic logic) {
		transferBetween(logic, PastelNodeType.SENDER, PastelNodeType.GATHER, PastelTransmissionLogic.TransferMode.PUSH_PULL);
		transferBetween(logic, PastelNodeType.PROVIDER, PastelNodeType.GATHER, PastelTransmissionLogic.TransferMode.PULL);
		transferBetween(logic, PastelNodeType.STORAGE, PastelNodeType.GATHER, PastelTransmissionLogic.TransferMode.PULL);
		transferBetween(logic, PastelNodeType.SENDER, PastelNodeType.STORAGE, PastelTransmissionLogic.TransferMode.PUSH);
	}
	
	protected void transferBetween(PastelTransmissionLogic logic, PastelNodeType sourceType, PastelNodeType destinationType, PastelTransmissionLogic.TransferMode transferMode) {
		for (PastelNodeBlockEntity sourceNode : logic.getLoadedNodes(getPayloadType(), sourceType)) {
			if (!sourceNode.isEnabled()) {
				continue;
			}
			
			for(Supplier<? extends PastelPayloadType> payloadType : sourceNode.getSupportedPayloads()) {
				payloadType.get().tryTransferToType(logic, sourceNode, destinationType, transferMode);
			}
		}
	}
	
	protected void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode){
	
	}
	
}