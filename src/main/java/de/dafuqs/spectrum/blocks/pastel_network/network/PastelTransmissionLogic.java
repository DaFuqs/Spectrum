package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("UnstableApiUsage")
public class PastelTransmissionLogic {
	
	private enum TransferMode {
		PUSH,
		PULL,
		PUSH_PULL
	}

	public static final int DEFAULT_MAX_TRANSFER_AMOUNT = 1;
	public static final int DEFAULT_TRANSFER_TICKS_PER_NODE = 30;
	private final ServerPastelNetwork network;
	
	private DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstra;
	private Map<PastelNodeBlockEntity, Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>>> pathCache = new HashMap<>();
	
	
	public PastelTransmissionLogic(ServerPastelNetwork network) {
		this.network = network;
	}
	
	public void invalidateCache() {
		this.dijkstra = null;
		this.pathCache = new HashMap<>();
	}
	
	public @Nullable GraphPath<PastelNodeBlockEntity, DefaultEdge> getPath(Graph<PastelNodeBlockEntity, DefaultEdge> graph, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
		if (this.dijkstra == null) {
			this.dijkstra = new DijkstraShortestPath<>(graph);
		}
		
		// cache hit?
		Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> e = this.pathCache.getOrDefault(source, null);
		if (e != null) {
			if (e.containsKey(destination)) {
				return e.get(destination);
			}
		}
		
		// calculate and cache
		ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> paths = this.dijkstra.getPaths(source);
		GraphPath<PastelNodeBlockEntity, DefaultEdge> path = paths.getPath(destination);
		if (this.pathCache.containsKey(source)) {
			this.pathCache.get(source).put(destination, path);
		} else {
			Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> newMap = new HashMap<>();
			newMap.put(destination, path);
			this.pathCache.put(source, newMap);
		}
		
		return path;
	}
	
	public void tick(PastelNetwork.Priority priority) {
		transferBetween(PastelNodeType.BUFFER, PastelNodeType.GATHER, TransferMode.PULL, priority);
		transferBetween(PastelNodeType.SENDER, PastelNodeType.GATHER, TransferMode.PUSH_PULL, priority);
		transferBetween(PastelNodeType.PROVIDER, PastelNodeType.GATHER, TransferMode.PULL, priority);
		transferBetween(PastelNodeType.STORAGE, PastelNodeType.GATHER, TransferMode.PULL, priority);

		transferBetween(PastelNodeType.SENDER, PastelNodeType.BUFFER, TransferMode.PUSH_PULL, priority);
		transferBetween(PastelNodeType.PROVIDER, PastelNodeType.BUFFER, TransferMode.PULL, priority);
		transferBetween(PastelNodeType.STORAGE, PastelNodeType.BUFFER, TransferMode.PULL, priority);

		transferBetween(PastelNodeType.SENDER, PastelNodeType.STORAGE, TransferMode.PUSH, priority);
	}
	
	private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType, TransferMode transferMode, PastelNetwork.Priority priority) {
		for (PastelNodeBlockEntity sourceNode : this.network.getNodes(sourceType, priority)) {
			if (!sourceNode.canTransfer()) {
				continue;
			}
			
			Storage<ItemVariant> sourceStorage = sourceNode.getConnectedStorage();
			if (sourceStorage != null && sourceStorage.supportsExtraction()) {
				tryTransferToType(sourceNode, sourceStorage, destinationType, transferMode);
			}
		}
	}
	
	private void tryTransferToType(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeType type, TransferMode transferMode) {
		for (PastelNodeBlockEntity destinationNode : this.network.getNodes(type, PastelNetwork.Priority.GENERIC)) {
			if (!destinationNode.canTransfer()) {
				continue;
			}
			
			Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
			if (destinationStorage != null && destinationStorage.supportsInsertion()) {
				boolean success = transferBetween(sourceNode, sourceStorage, destinationNode, destinationStorage, transferMode);
				if (success && transferMode != TransferMode.PULL) {
					return;
				}
			}
		}
	}
	
	private boolean transferBetween(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeBlockEntity destinationNode, Storage<ItemVariant> destinationStorage, TransferMode transferMode) {
		Predicate<ItemVariant> filter = sourceNode.getTransferFilterTo(destinationNode);
		
		try (Transaction transaction = Transaction.openOuter()) {
			for (StorageView<ItemVariant> view : sourceStorage) {
				if (view.isResourceBlank()) {
					continue;
				}
				
				ItemVariant storedResource = view.getResource(); // Current resource
				if (storedResource.isBlank() || !filter.test(storedResource)) {
					continue;
				}
				
				long storedAmount = view.getAmount();
				if (storedAmount <= 0) {
					continue;
				}

				// Transfer details are always decided by the sender
				long transferrableAmount = sourceNode.getMaxTransferredAmount();
				int vertexTime = sourceNode.getTransferTime();

				long itemCountUnderway = destinationNode.getItemCountUnderway();
				transferrableAmount = (int) StorageUtil.simulateInsert(destinationStorage, storedResource, transferrableAmount + itemCountUnderway, transaction);
				transferrableAmount = transferrableAmount - itemCountUnderway; // prevention to not overfill the container (send more transfers when the existing ones would fill it already)
				
				if (transferrableAmount <= 0) {
					continue;
				}
				
				transferrableAmount = sourceStorage.extract(storedResource, transferrableAmount, transaction);
				if (transferrableAmount <= 0) {
					continue;
				}
				
				Optional<PastelTransmission> optionalTransmission = createTransmissionOnValidPath(sourceNode, destinationNode, storedResource, transferrableAmount, vertexTime);
				if (optionalTransmission.isPresent()) {
					PastelTransmission transmission = optionalTransmission.get();
					int travelTime = transmission.getTransmissionDuration();
					this.network.addTransmission(transmission, travelTime);
					SpectrumS2CPacketSender.sendPastelTransmissionParticle(this.network, travelTime, transmission);
					
					if (transferMode == TransferMode.PULL) {
						destinationNode.markTransferred();
					} else if (transferMode == TransferMode.PUSH) {
						sourceNode.markTransferred();
					} else {
						destinationNode.markTransferred();
						sourceNode.markTransferred();
					}
					
					destinationNode.addItemCountUnderway(transferrableAmount);
					transaction.commit();
					return true;
				}
			}
			transaction.abort();
		}
		return false;
	}
	
	public Optional<PastelTransmission> createTransmissionOnValidPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemVariant variant, long amount, int vertexTime) {
		GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(this.network.getGraph(), source, destination);
		if (graphPath != null) {
			List<BlockPos> vertexPositions = new ArrayList<>();
			for (PastelNodeBlockEntity vertex : graphPath.getVertexList()) {
				vertexPositions.add(vertex.getPos());
			}

			SpectrumS2CPacketSender.sendPastelNodeStatusUpdate(List.of(source), true);
			return Optional.of(new PastelTransmission(vertexPositions, variant, amount, vertexTime));
		}
		return Optional.empty();
	}
	
}
