package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.level.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.stream.*;

public class PastelNetwork<W extends Level> {
	
	protected Graph<BlockPos, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
	protected final W level;
	protected final UUID uuid;
	protected final int color;
	
	public enum NodePriority {
		GENERIC,
		MODERATE,
		HIGH
	}
	
	public PastelNetwork(W level, UUID uuid, int color) {
		this.level = level;
		this.uuid = uuid;
		this.color = color;
	}
	
	public int size() {
		return graph.vertexSet().size();
	}
	
	public W getLevel() {
		return this.level;
	}
	
	public Graph<BlockPos, DefaultEdge> getGraph() {
		return this.graph;
	}
	
	public boolean addEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		return addEdge(node.getBlockPos(), parent.getBlockPos());
	}
	
	public boolean addEdge(BlockPos pos1, BlockPos pos2) {
		if (!hasEdge(pos1, pos2)) {
			graph.addEdge(pos1, pos2);
			return true;
		}
		return false;
	}
	
	public boolean hasEdge(BlockPos pos1, BlockPos pos2) {
		if (!graph.containsVertex(pos1) || !graph.containsVertex(pos2))
			return false;
		
		return graph.containsEdge(pos1, pos2);
	}
	
	public boolean removeEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		return graph.removeEdge(node.getBlockPos(), parent.getBlockPos()) != null;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setGraph(Graph<BlockPos, DefaultEdge> graph) {
		this.graph = graph;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof PastelNetwork<?> p) {
			return this.uuid.equals(p.uuid);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}
	
	public CompoundTag graphToNbt() {
		var vertices = new ArrayList<>(graph.vertexSet());
		var graphStorage = new CompoundTag();
		graphStorage.putInt("Size", vertices.size());
		for (int i = 0; i < vertices.size(); i++) {
			var vertex = vertices.get(i);
			
			// Store the Vertex
			graphStorage.putLong("Vertex" + i, vertex.asLong());
			
			// Save the edges
			int currentVertex = i;
			var edgeIndexes = graph.edgesOf(vertex)
					.stream()
					.map(graph::getEdgeTarget)
					.mapToInt(vertices::indexOf)
					.filter(v -> v != currentVertex)
					.boxed()
					.collect(Collectors.toList());
			
			if (edgeIndexes.isEmpty())
				continue;
			
			edgeIndexes.add(0, vertices.indexOf(vertex));
			
			graphStorage.putIntArray("EdgeIndexes" + i, edgeIndexes);
		}
		
		return graphStorage;
	}
	
	public static Graph<BlockPos, DefaultEdge> graphFromNbt(CompoundTag nbt) {
		Graph<BlockPos, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		
		var size = nbt.getInt("Size");
		var vertices = new ArrayList<BlockPos>();
		for (int i = 0; i < size; i++) {
			var vertex = BlockPos.of(nbt.getLong("Vertex" + i));
			vertices.add(vertex);
			graph.addVertex(vertex);
		}
		
		for (int i = 0; i < size; i++) {
			if (!nbt.contains("EdgeIndexes" + i))
				continue;
			var edgeIndexes = nbt.getIntArray("EdgeIndexes" + i);
			var source = vertices.get(edgeIndexes[0]);
			for (int targetIndex = 1; targetIndex < edgeIndexes.length; targetIndex++) {
				var target = vertices.get(edgeIndexes[targetIndex]);
				if (!graph.containsEdge(source, target))
					graph.addEdge(source, target);
			}
		}
		
		return graph;
	}
	
	public String getNodeDebugText() {
		return "UUID: " +
				uuid.toString() +
				" - Edges: " +
				graph.edgeSet().size() +
				" - Vertices: " +
				graph.vertexSet().size();
	}
	
}
