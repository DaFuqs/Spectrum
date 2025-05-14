package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class PastelNetworkCreationCriterion extends SimpleCriterionTrigger<PastelNetworkCreationCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("pastel_network_creation");
	
	public void trigger(ServerPlayer player, ServerPastelNetwork network) {
		this.trigger(player, (conditions) -> conditions.matches(network.getLoadedNodes(PastelNodeType.CONNECTION).size(), network.getLoadedNodes(PastelNodeType.PROVIDER).size(),
				network.getLoadedNodes(PastelNodeType.STORAGE).size(), network.getLoadedNodes(PastelNodeType.SENDER).size(), network.getLoadedNodes(PastelNodeType.GATHER).size(), network.getLoadedNodes(PastelNodeType.BUFFER).size()));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			MinMaxBounds.Ints totalNodes,
			MinMaxBounds.Ints connectionNodes,
			MinMaxBounds.Ints providerNodes,
			MinMaxBounds.Ints storageNodes,
			MinMaxBounds.Ints senderNodes,
			MinMaxBounds.Ints gatherNodes,
			MinMaxBounds.Ints bufferNodes
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("total_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::totalNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("connection_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::connectionNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("provider_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::providerNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("storage_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::storageNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("sender_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::senderNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("gather_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::gatherNodes),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("buffer_nodes", MinMaxBounds.Ints.ANY).forGetter(Conditions::bufferNodes)
		).apply(instance, Conditions::new));
		
		
		public boolean matches(int connectionNodes, int providerNodes, int storageNodes, int senderNodes, int gatherNodes, int bufferNodes) {
			return this.totalNodes.matches(connectionNodes + providerNodes + storageNodes + senderNodes + gatherNodes) && this.connectionNodes.matches(connectionNodes) && this.providerNodes.matches(providerNodes) && this.storageNodes.matches(storageNodes) && this.senderNodes.matches(senderNodes) && this.gatherNodes.matches(gatherNodes) && this.bufferNodes.matches(bufferNodes);
		}
		
	}
	
}
