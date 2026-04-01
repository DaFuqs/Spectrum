package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.Graph;
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
	
	private DijkstraShortestPath<BlockPos, DefaultEdge> dijkstra;
	private Map<BlockPos, Map<BlockPos, GraphPath<BlockPos, DefaultEdge>>> pathCache = new HashMap<>();
	
	
	public PastelTransmissionLogic(ServerPastelNetwork network) {
		this.network = network;
	}
	
	public void invalidateCache() {
		this.dijkstra = null;
		this.pathCache = new HashMap<>();
	}
	
	public @Nullable GraphPath<BlockPos, DefaultEdge> getPath(Graph<BlockPos, DefaultEdge> graph, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
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
	
	public void tick(PastelNetwork.NodePriority priority) {
		transferBetween(PastelNodeType.SENDER, PastelNodeType.GATHER, TransferMode.PUSH_PULL, priority);
		transferBetween(PastelNodeType.PROVIDER, PastelNodeType.GATHER, TransferMode.PULL, priority);
		transferBetween(PastelNodeType.STORAGE, PastelNodeType.GATHER, TransferMode.PULL, priority);
		transferBetween(PastelNodeType.SENDER, PastelNodeType.STORAGE, TransferMode.PUSH, priority);
	}
	
	private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType, TransferMode transferMode, PastelNetwork.NodePriority priority) {
		for (PastelNodeBlockEntity sourceNode : this.network.getLoadedNodes(sourceType, priority)) {
			if (!sourceNode.canTransfer()) {
				continue;
			}
			
			IItemHandler sourceStorage = sourceNode.getConnectedStorage();
			if (sourceStorage != null) {
				tryTransferToType(sourceNode, sourceStorage, destinationType, transferMode);
			}
		}
	}
	
	private void tryTransferToType(PastelNodeBlockEntity sourceNode, IItemHandler sourceStorage, PastelNodeType type, TransferMode transferMode) {
		for (PastelNodeBlockEntity destinationNode : this.network.getLoadedNodes(type, PastelNetwork.NodePriority.GENERIC)) {
			if (!destinationNode.canTransfer()) {
				continue;
			}
			
			IItemHandler destinationStorage = destinationNode.getConnectedStorage();
			if (destinationStorage != null) {
				boolean success = transferBetween(sourceNode, sourceStorage, destinationNode, destinationStorage, transferMode);
				if (success && transferMode != TransferMode.PULL) {
					return;
				}
			}
		}
	}
	
	private boolean transferBetween(PastelNodeBlockEntity sourceNode, IItemHandler sourceStorage, PastelNodeBlockEntity destinationNode, IItemHandler destinationStorage, TransferMode transferMode) {
		// check how much room is in the target inventory
		long totalAvailableStorage = -destinationNode.getItemCountUnderway();
		for (int d = 0; d < destinationStorage.getSlots(); d++) {
			ItemStack stack = destinationStorage.getStackInSlot(d);
			
			if (stack.isEmpty()) {
				totalAvailableStorage += destinationStorage.getSlotLimit(d);
			} else {
				totalAvailableStorage += Math.min(destinationStorage.getSlotLimit(d), stack.getMaxStackSize()) - stack.getCount();
			}
		}
		
		if (totalAvailableStorage <= 0)
			return false;
		
		Predicate<ItemStack> filter = sourceNode.getTransferFilterTo(destinationNode);
		Map<ItemStack, Long> proposals = new HashMap<>();
		for (int s = 0; s < sourceStorage.getSlots(); s++) {
			ItemStack stack = sourceStorage.extractItem(s, DEFAULT_MAX_TRANSFER_AMOUNT, true);
			
			if (stack.isEmpty())
				continue;
			if (!filter.test(stack))
				continue;
			
			proposals.put(stack, proposals.getOrDefault(stack, 0L) + stack.getCount());
		}
		
		for (ItemStack stack : proposals.keySet()) {
			long proposedAmount = Math.min(Math.min(proposals.get(stack), sourceNode.getMaxTransferredAmount()), totalAvailableStorage);
			if (proposedAmount == 0)
				continue;
			
			ItemStack proposedStack = stack.copyWithCount((int) proposedAmount);
			int simulatedAmount = (int) (proposedAmount - ItemHandlerHelper.insertItemStacked(destinationStorage, proposedStack, true).getCount());
			Tuple<Integer, List<ItemStack>> matchingStacks = InventoryHelper.getStackCountInInventory(proposedStack, sourceStorage, simulatedAmount);
			
			if (matchingStacks.getA() == 0)
				continue;
			
			Optional<PastelTransmission> transmission = createTransmissionOnValidPath(sourceNode, destinationNode, proposedStack.copyWithCount(simulatedAmount), sourceNode.getTransferTime());
			if (transmission.isPresent()) {
				int toRemove = simulatedAmount;
				while (toRemove > 0) {
					for (ItemStack matchingStack : matchingStacks.getB()) {
						int amountToShrink = Math.min(toRemove, matchingStack.getCount());
						matchingStack.shrink(amountToShrink);
						toRemove -= amountToShrink;
					}
				}
				
				Optional<PastelTransmission> optionalTransmission = createTransmissionOnValidPath(sourceNode, destinationNode, proposedStack.copyWithCount(simulatedAmount), sourceNode.getTransferTime());
				if (optionalTransmission.isPresent()) {
					PastelTransmission trans = optionalTransmission.get();
					int travelTime = trans.getTransmissionDuration();
					this.network.addTransmission(trans, travelTime);
					PastelTransmissionPayload.sendPastelTransmissionParticle(this.network, trans.getTransmissionDuration(), transmission.get());
					
					destinationNode.markTransferred(transferMode != TransferMode.PUSH);
					sourceNode.markTransferred(transferMode != TransferMode.PULL);
					
					destinationNode.addItemCountUnderway(simulatedAmount);
					return true;
				}
			}
		}
		return false;
	}
	
	public Optional<PastelTransmission> createTransmissionOnValidPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemStack stack, int vertexTime) {
		GraphPath<BlockPos, DefaultEdge> graphPath = getPath(this.network.getGraph(), source, destination);
		if (graphPath != null) {
			PastelNodeStatusUpdatePayload.sendPastelNodeStatusUpdate(List.of(source), true);
			return Optional.of(new PastelTransmission(graphPath.getVertexList(), stack, vertexTime));
		}
		return Optional.empty();
	}
	
}
