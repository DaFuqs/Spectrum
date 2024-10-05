package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.joml.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ClientPastelNetworkManager implements PastelNetworkManager {
	
	private final List<PastelNetwork> networks = new ArrayList<>();
	
	@Override
	public PastelNetwork JoinOrCreateNetwork(PastelNodeBlockEntity node, UUID uuid) {
		PastelNetwork foundNetwork = null;
		for (int i = 0; i < this.networks.size(); i++) {
			PastelNetwork network = this.networks.get(i);
			if (network.getUUID().equals(uuid)) {
				network.addNodeAndLoadMemory(node);
				foundNetwork = network;
			} else {
				if (network.removeNode(node, NodeRemovalReason.MOVED)) {
					i--;
				}
				// network empty => delete
				if (!network.hasNodes()) {
					this.networks.remove(network);
				}
			}
		}
		if (foundNetwork != null) {
			return foundNetwork;
		}
		
		PastelNetwork network = createNetwork(node.getWorld(), uuid);
		network.addNodeAndLoadMemory(node);
		return network;
	}

	@Override
	public Optional<? extends PastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}

	@Override
	public void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		PastelNetwork parentNetwork, otherNetwork;

		if (parent.getParentNetwork() != null) {
			parentNetwork = parent.getParentNetwork();
			otherNetwork = node.getParentNetwork();

			if (otherNetwork == null) {
				parentNetwork.addNodeAndConnect(node, parent);
				node.setParentNetwork(parentNetwork);
				return;
			}
		}
		else if (node.getParentNetwork() != null) {
			parentNetwork = node.getParentNetwork();
			otherNetwork = parent.getParentNetwork();

			if (otherNetwork == null) {
				parentNetwork.addNodeAndConnect(parent, node);
				parent.setParentNetwork(parentNetwork);
				return;
			}
		}
		else {
			parentNetwork = createNetwork(node.getWorld(), null);
			parentNetwork.addNode(parent);
			parent.setParentNetwork(parentNetwork);
			parentNetwork.addNodeAndConnect(node, parent);
			node.setParentNetwork(parentNetwork);
			return;
		}

		if (parentNetwork == otherNetwork) {
			return;
		}

		parentNetwork.incorporate(otherNetwork, node, parent);
		this.networks.remove(otherNetwork);
	}

	@Override
	public void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		PastelNetwork network = node.getParentNetwork();
		if (network != null) {
			network.removeNode(node, reason);
			if (network.nodes.size() == 0) {
				this.networks.remove(network);
			}
		}
	}
	
	private PastelNetwork createNetwork(World world, UUID uuid) {
		PastelNetwork network = new PastelNetwork(world, uuid);
		this.networks.add(network);
		return network;
	}
	
	public void renderLines(WorldRenderContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		for (PastelNetwork network : this.networks) {
			if (network.getWorld().getDimension() != context.world().getDimension()) continue;
			Graph<PastelNodeBlockEntity, DefaultEdge> graph = network.getGraph();
			int color = network.getColor();
			float[] colors = PastelRenderHelper.unpackNormalizedColor(color);
			
			for (DefaultEdge edge : graph.edgeSet()) {
				PastelNodeBlockEntity source = graph.getEdgeSource(edge);
				PastelNodeBlockEntity target = graph.getEdgeTarget(edge);
				
				final MatrixStack matrices = context.matrixStack();
				final Vec3d pos = context.camera().getPos();
				matrices.push();
				matrices.translate(-pos.x, -pos.y, -pos.z);
				PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, source.getPos(), target.getPos());
				PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, target.getPos(), source.getPos());
				
				if (client.options.debugEnabled) {
					Vec3d offset = Vec3d.ofCenter(target.getPos()).subtract(Vec3d.of(source.getPos()));
					Vec3d normalized = offset.normalize();
					Matrix4f positionMatrix = context.matrixStack().peek().getPositionMatrix();
					PastelRenderHelper.renderDebugLine(context.consumers(), color, offset, normalized, positionMatrix);
				}
				matrices.pop();
			}
		}
	}
	
}