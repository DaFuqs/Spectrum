package de.dafuqs.spectrum.blocks.pastel_network.network;

import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ClientPastelNetwork extends PastelNetwork<ClientLevel> {
	
	protected long lastChangeTick;
	
	public ClientPastelNetwork(ClientLevel world, UUID uuid, int color) {
		super(world, uuid, color);
		this.lastChangeTick = world.getGameTime();
	}
	
	public void setGraph(Graph<BlockPos, DefaultEdge> graph) {
		this.graph = graph;
		this.lastChangeTick = world.getGameTime();
	}
	
}
