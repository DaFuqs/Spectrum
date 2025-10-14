package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.joml.Math;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ClientPastelNetworkManager implements PastelNetworkManager<ClientLevel, ClientPastelNetwork>, Clearable {
	
	protected static final int MAX_RENDER_DISTANCE_SQUARED = 48 * 48;
	
	private final List<ClientPastelNetwork> networks = new ArrayList<>();
	
	@Override
	public Optional<? extends ClientPastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}
	
	@Override
	public ClientPastelNetwork createNetwork(ClientLevel world, UUID uuid, int color) {
		ClientPastelNetwork network = new ClientPastelNetwork(world, uuid, color);
		this.networks.add(network);
		return network;
	}
	
	public void renderLines(WorldRenderContext context, LivingEntity cameraEntity, boolean paintbrushInHand) {
		BlockPos cameraEntityPos = cameraEntity.blockPosition();
		
		ClientLevel world = context.world();
		long worldTime = world.getGameTime();
		for (ClientPastelNetwork network : this.networks) {
			if (network.getLevel().dimensionType() != world.dimensionType()) continue;
			
			float alphaMod = paintbrushInHand ? 1.0F : (network.lastChangeTick - worldTime + 20) * 0.05F;
			if (alphaMod <= 0.0F) continue;
			
			Graph<BlockPos, DefaultEdge> graph = network.getGraph();
			int color = network.getColor();
			float[] colors = PastelRenderHelper.unpackNormalizedColor(color);
			
			for (DefaultEdge edge : graph.edgeSet()) {
				BlockPos source = graph.getEdgeSource(edge);
				BlockPos target = graph.getEdgeTarget(edge);
				
				// do not render lines that are far away to save a lot of fps
				if (cameraEntityPos.distSqr(source) > MAX_RENDER_DISTANCE_SQUARED && cameraEntityPos.distSqr(target) > MAX_RENDER_DISTANCE_SQUARED) {
					continue;
				}
				
				final PoseStack matrices = context.matrixStack();
				final Vec3 pos = context.camera().getPosition();
				matrices.pushPose();
				matrices.translate(-pos.x, -pos.y, -pos.z);
				var cross = source.cross(target);
				var interval = (cross.getX() + cross.getY() + cross.getZ() + network.level.getGameTime()) % 1000000F;
				var alpha = alphaMod * (1.0 - (Math.max(Math.sin((interval / 17F)) * 2.5 - 2, 0)));
				colors[0] = (float) alpha;
				PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, source, target);
				
				matrices.popPose();
			}
		}
	}
	
	@Override
	public void clearContent() {
		this.networks.clear();
	}
	
	@Override
	public void removeNetwork(UUID uuid) {
		ClientPastelNetwork foundNetwork = null;
		for (ClientPastelNetwork network : this.networks) {
			if (network.uuid.equals(uuid)) {
				foundNetwork = network;
				break;
			}
		}
		if (foundNetwork != null) {
			this.networks.remove(foundNetwork);
		}
	}
	
}