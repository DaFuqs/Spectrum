package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.neoforged.neoforge.registries.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("UnstableApiUsage")
public class PastelTransmissionLogic {
	
	public enum TransferMode {
		PUSH,
		PULL,
		PUSH_PULL
	}
	
	public static final int DEFAULT_MAX_TRANSFER_AMOUNT = 1;
	public static final int DEFAULT_TRANSFER_TICKS_PER_NODE = 30;
	public static final int DEFAULT_TRANSFER_RATE = 20;
	
	private final ServerPastelNetwork network;
	
	private DijkstraShortestPath<BlockPos, DefaultEdge> dijkstra;
	private Map<BlockPos, Map<BlockPos, GraphPath<BlockPos, DefaultEdge>>> pathCache = new HashMap<>();
	
	
	public PastelTransmissionLogic(ServerPastelNetwork network) {
		this.network = network;
	}
	
	public void invalidateCache() {
		this.dijkstra = null;
		this.pathCache = new HashMap<>();
	}
	
	public @Nullable GraphPath<BlockPos, DefaultEdge> getPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
		return this.getPath(this.network.getGraph(), source, destination);
	}
	
	protected @Nullable GraphPath<BlockPos, DefaultEdge> getPath(Graph<BlockPos, DefaultEdge> graph, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
		if (this.dijkstra == null) {
			this.dijkstra = new DijkstraShortestPath<>(graph);
		}
		
		// cache hit?
		Map<BlockPos, GraphPath<BlockPos, DefaultEdge>> e = this.pathCache.getOrDefault(source.getBlockPos(), null);
		if (e != null) {
			if (e.containsKey(destination.getBlockPos())) {
				return e.get(destination.getBlockPos());
			}
		}
		
		// calculate and cache
		ShortestPathAlgorithm.SingleSourcePaths<BlockPos, DefaultEdge> paths = this.dijkstra.getPaths(source.getBlockPos());
		GraphPath<BlockPos, DefaultEdge> path = paths.getPath(destination.getBlockPos());
		if (this.pathCache.containsKey(source.getBlockPos())) {
			this.pathCache.get(source.getBlockPos()).put(destination.getBlockPos(), path);
		} else {
			Map<BlockPos, GraphPath<BlockPos, DefaultEdge>> newMap = new HashMap<>();
			newMap.put(destination.getBlockPos(), path);
			this.pathCache.put(source.getBlockPos(), newMap);
		}
		
		return path;
	}
	
	public void tick(boolean longTick) {
		for(Map.Entry<ResourceKey<PastelPayloadType>, PastelPayloadType> payloadType : SpectrumRegistries.PASTEL_PAYLOAD_TYPE.entrySet()) {
			if(longTick || payloadType.getValue().runsEveryTick()) {
				payloadType.getValue().tick(this);
			}
		}
	}
	
	private Set<PastelNodeBlockEntity> getLoadedNodes(@Nullable PastelNodeType type) {
		if(type == null) {
			Set<PastelNodeBlockEntity> set = new ObjectArraySet<>();
			for(Set<PastelNodeBlockEntity> e : this.network.loadedNodes.values()) {
				set.addAll(e);
			}
			return set;
		}
		return this.network.getLoadedNodes(type);
	}
	
	public Set<PastelNodeBlockEntity> getLoadedNodes(DeferredHolder<PastelPayloadType, ?> payloadType, @Nullable PastelNodeType nodeType) {
		Set<PastelNodeBlockEntity> nodes = new HashSet<>();
		var loaded = this.getLoadedNodes(nodeType);
		for(PastelNodeBlockEntity entity : loaded) {
			for(Supplier<? extends PastelPayloadType> typeSupplier : entity.getSupportedPayloads()) {
				if(typeSupplier.get().equals(payloadType.get())) {
					nodes.add(entity);
					break;
				}
			}
		}
		return nodes;
	}
	
	public void addTransmission(PastelNodeBlockEntity sourceNode, PastelNodeBlockEntity destinationNode, PastelTransmissionLogic.TransferMode transferMode, PastelTransmission transmission) {
		PastelNodeStatusUpdatePayload.sendPastelNodeStatusUpdate(List.of(sourceNode), true);
		destinationNode.markTransferred(transferMode != PastelTransmissionLogic.TransferMode.PUSH);
		sourceNode.markTransferred(transferMode != PastelTransmissionLogic.TransferMode.PULL);
		
		network.addTransmission(transmission, transmission.getTransmissionDuration());
		PastelTransmissionPayload.sendPastelTransmissionParticle(network, transmission.getTransmissionDuration(), transmission);
	}
	
}
