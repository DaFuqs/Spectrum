package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

public record PastelNetworkEdgeSyncPayload(UUID networkUUID, int color, Graph<BlockPos, DefaultEdge> graph) implements CustomPacketPayload {
	
	public static final StreamCodec<RegistryFriendlyByteBuf, Graph<BlockPos, DefaultEdge>> GRAPH_PACKET_CODEC = new StreamCodec<>() {
		
		@Override
		public void encode(RegistryFriendlyByteBuf buf, Graph<BlockPos, DefaultEdge> graph) {
			ArrayList<BlockPos> vertices = new ArrayList<>(graph.vertexSet());
			
			// Vertices
			buf.writeInt(vertices.size());
			for (BlockPos vertex : vertices) {
				buf.writeBlockPos(vertex);
			}
			
			// Edges (as index in vertices)
			Set<DefaultEdge> edges = graph.edgeSet();
			buf.writeInt(edges.size());
			for (DefaultEdge edge : edges) {
				buf.writeInt(vertices.indexOf(graph.getEdgeSource(edge)));
				buf.writeInt(vertices.indexOf(graph.getEdgeTarget(edge)));
			}
		}
		
		@Override
		public Graph<BlockPos, DefaultEdge> decode(RegistryFriendlyByteBuf buf) {
			Graph<BlockPos, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
			
			int vertexCount = buf.readInt();
			BlockPos[] vertices = new BlockPos[vertexCount];
			for (int i = 0; i < vertexCount; i++) {
				BlockPos vertex = buf.readBlockPos();
				vertices[i] = vertex;
				graph.addVertex(vertex);
			}
			
			int edgeCount = buf.readInt();
			for (int i = 0; i < edgeCount; i++) {
				BlockPos source = vertices[buf.readInt()];
				BlockPos target = vertices[buf.readInt()];
				graph.addEdge(source, target);
			}
			
			return graph;
		}
	};
	
	public static final Type<PastelNetworkEdgeSyncPayload> ID = SpectrumC2SPackets.makeId("pastel_network_edge_sync");
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelNetworkEdgeSyncPayload> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, PastelNetworkEdgeSyncPayload::networkUUID,
			ByteBufCodecs.INT, PastelNetworkEdgeSyncPayload::color,
			GRAPH_PACKET_CODEC, PastelNetworkEdgeSyncPayload::graph,
			PastelNetworkEdgeSyncPayload::new
	);
	
	public static void send(ServerPastelNetwork network, BlockPos pos) {
		PacketDistributor.sendToPlayersTrackingChunk(
				network.getLevel(), new ChunkPos(pos), new PastelNetworkEdgeSyncPayload(
						network.getUUID(),
						network.getColor(),
						network.getGraph()
				)
		);
	}
	
	public static void execute(PastelNetworkEdgeSyncPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			var level = context.player()
					.level();
			Optional<? extends ClientPastelNetwork> network = Pastel.getClientInstance()
					.getNetwork(payload.networkUUID);
			if (network.isPresent()) {
				network.get()
						.setGraph(payload.graph);
			} else {
				ClientPastelNetwork newNetwork = Pastel.getClientInstance()
						.createNetwork(
								(ClientLevel) level, payload.networkUUID, payload.color);
				newNetwork.setGraph(payload.graph);
			}
		});
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}