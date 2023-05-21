package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class PastelNetworkCreatingCriterion extends AbstractCriterion<PastelNetworkCreatingCriterion.Conditions> {

	static final Identifier ID = SpectrumCommon.locate("pastel_network_creation");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public PastelNetworkCreatingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange totalNodes = NumberRange.IntRange.fromJson(jsonObject.get("total_nodes"));
		NumberRange.IntRange connectionNodes = NumberRange.IntRange.fromJson(jsonObject.get("connection_nodes"));
		NumberRange.IntRange providerNodes = NumberRange.IntRange.fromJson(jsonObject.get("provider_nodes"));
		NumberRange.IntRange storageNodes = NumberRange.IntRange.fromJson(jsonObject.get("storage_nodes"));
		NumberRange.IntRange senderNodes = NumberRange.IntRange.fromJson(jsonObject.get("sender_nodes"));
		NumberRange.IntRange gatherNodes = NumberRange.IntRange.fromJson(jsonObject.get("gather_nodes"));
		
		return new PastelNetworkCreatingCriterion.Conditions(extended, totalNodes, connectionNodes, providerNodes, storageNodes, senderNodes, gatherNodes);
	}

	public void trigger(ServerPlayerEntity player, ServerPastelNetwork network) {
		this.trigger(player, (conditions) -> conditions.matches(network.getNodes(PastelNodeType.CONNECTION).size(), network.getNodes(PastelNodeType.PROVIDER).size(),
				network.getNodes(PastelNodeType.STORAGE).size(), network.getNodes(PastelNodeType.SENDER).size(), network.getNodes(PastelNodeType.GATHER).size()));
	}

	public static class Conditions extends AbstractCriterionConditions {

		private final NumberRange.IntRange totalNodes;
		private final NumberRange.IntRange connectionNodes;
		private final NumberRange.IntRange providerNodes;
		private final NumberRange.IntRange storageNodes;
		private final NumberRange.IntRange senderNodes;
		private final NumberRange.IntRange gatherNodes;

		public Conditions(Extended player, NumberRange.IntRange totalNodes, NumberRange.IntRange connectionNodes, NumberRange.IntRange providerNodes, NumberRange.IntRange storageNodes, NumberRange.IntRange senderNodes, NumberRange.IntRange gatherNodes) {
			super(PastelNetworkCreatingCriterion.ID, player);
			this.totalNodes = totalNodes;
			this.connectionNodes = connectionNodes;
			this.providerNodes = providerNodes;
			this.storageNodes = storageNodes;
			this.senderNodes = senderNodes;
			this.gatherNodes = gatherNodes;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("total_nodes", this.totalNodes.toJson());
			jsonObject.add("connection_nodes", this.connectionNodes.toJson());
			jsonObject.add("provider_nodes", this.providerNodes.toJson());
			jsonObject.add("storage_nodes", this.storageNodes.toJson());
			jsonObject.add("sender_nodes", this.senderNodes.toJson());
			jsonObject.add("gather_nodes", this.gatherNodes.toJson());
			return jsonObject;
		}

		public boolean matches(int connectionNodes, int providerNodes, int storageNodes, int senderNodes, int gatherNodes) {
			return this.totalNodes.test(connectionNodes + providerNodes + storageNodes + senderNodes + gatherNodes) && this.connectionNodes.test(connectionNodes) && this.providerNodes.test(providerNodes) && this.storageNodes.test(storageNodes) && this.senderNodes.test(senderNodes) && this.gatherNodes.test(gatherNodes);
		}

	}

}
