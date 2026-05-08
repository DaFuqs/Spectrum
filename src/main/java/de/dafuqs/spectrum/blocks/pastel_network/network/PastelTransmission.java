package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.PastelPayload;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelTransmission implements SchedulerMap.Callback {
	
	public static final Codec<PastelTransmission> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockPos.CODEC.listOf().fieldOf("node_positions").forGetter(PastelTransmission::getNodePositions),
			PastelPayload.CODEC.fieldOf("payload").forGetter(PastelTransmission::getPayload),
			Codec.INT.fieldOf("vertex_time").forGetter(PastelTransmission::getVertexTime)
	).apply(i, PastelTransmission::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelTransmission> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), PastelTransmission::getNodePositions,
			PastelPayload.STREAM_CODEC, PastelTransmission::getPayload,
			ByteBufCodecs.VAR_INT, PastelTransmission::getVertexTime,
			PastelTransmission::new
	);
	
	private @Nullable ServerPastelNetwork network;
	private final List<BlockPos> nodePositions;
	private final PastelPayload payload;
	private final int vertexTime;
	
	public PastelTransmission(List<BlockPos> nodePositions, PastelPayload payload, int vertexTime) {
		this.nodePositions = nodePositions;
		this.payload = payload;
		this.vertexTime = vertexTime;
	}
	
	public void setNetwork(@NotNull ServerPastelNetwork network) {
		this.network = network;
	}
	
	public @Nullable PastelNetwork<ServerLevel> getNetwork() {
		return this.network;
	}
	
	public List<BlockPos> getNodePositions() {
		return nodePositions;
	}
	
	public int getVertexTime() {
		return vertexTime;
	}
	
	public int getTransmissionDuration() {
		return vertexTime * (nodePositions.size() - 1);
	}
	
	public PastelPayload getPayload() {
		return this.payload;
	}
	
	public BlockPos getStartPos() {
		return this.nodePositions.get(0);
	}
	
	@Override
	public void trigger() {
		arriveAtDestination();
	}
	
	private void arriveAtDestination() {
		if (nodePositions.isEmpty()) {
			return;
		}
		
		@NotNull BlockPos destinationPos = nodePositions.getLast();
		@Nullable PastelNodeBlockEntity destinationNode = this.network.getLoadedNodeAt(destinationPos);
		Level level = this.network.getLevel();
		payload.arriveAtDestination(level, destinationPos, destinationNode);
	}
	
}
