package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.joml.*;
import org.joml.Math;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ClientPastelNetworkManager implements PastelNetworkManager {
	
	private final List<PastelNetwork> networks = new ArrayList<>();
	
	@Override
	public Optional<? extends PastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}
	
	@Override
	public PastelNetwork createNetwork(World world, UUID uuid) {
		PastelNetwork network = new PastelNetwork(world, uuid);
		this.networks.add(network);
		return network;
	}
	
	public void renderLines(WorldRenderContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		for (PastelNetwork network : this.networks) {
			if (network.getWorld().getDimension() != context.world().getDimension()) continue;
			Graph<BlockPos, DefaultEdge> graph = network.getGraph();
			int color = network.getColor();
			float[] colors = PastelRenderHelper.unpackNormalizedColor(color);
			
			for (DefaultEdge edge : graph.edgeSet()) {
				BlockPos source = graph.getEdgeSource(edge);
				BlockPos target = graph.getEdgeTarget(edge);
				
				final MatrixStack matrices = context.matrixStack();
				final Vec3d pos = context.camera().getPos();
				matrices.push();
				matrices.translate(-pos.x, -pos.y, -pos.z);
				var cross = source.crossProduct(target);
				var interval = (cross.getX() + cross.getY() + cross.getZ() + network.world.getTime()) % 1000000F;
				var alpha = 1 - (Math.max(Math.sin((interval / 17F)) * 2.5 - 2, 0));
				colors[0] = (float) alpha;
				PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, source, target);
				
				if (client.options.debugEnabled) {
					Vec3d offset = Vec3d.ofCenter(target).subtract(Vec3d.of(source));
					Vec3d normalized = offset.normalize();
					Matrix4f positionMatrix = context.matrixStack().peek().getPositionMatrix();
					PastelRenderHelper.renderDebugLine(context.consumers(), color, offset, normalized, positionMatrix);
				}
				matrices.pop();
			}
		}
	}
	
}