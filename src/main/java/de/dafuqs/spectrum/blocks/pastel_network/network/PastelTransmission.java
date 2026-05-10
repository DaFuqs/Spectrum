package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PastelTransmission implements SchedulerMap.Callback {
	
	public static final Codec<PastelTransmission> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockPos.CODEC.listOf().fieldOf("node_positions").forGetter(PastelTransmission::getNodePositions),
			ItemVariant.CODEC.fieldOf("variant").forGetter(PastelTransmission::getVariant),
			Codec.LONG.fieldOf("amount").forGetter(PastelTransmission::getAmount),
			Codec.INT.fieldOf("vertex_time").forGetter(PastelTransmission::getVertexTime)
	).apply(i, PastelTransmission::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelTransmission> PACKET_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), PastelTransmission::getNodePositions,
			ItemVariant.PACKET_CODEC, PastelTransmission::getVariant,
			ByteBufCodecs.VAR_LONG, PastelTransmission::getAmount,
			ByteBufCodecs.VAR_INT, PastelTransmission::getVertexTime,
			PastelTransmission::new
	);
	
	private @Nullable ServerPastelNetwork network;
    private final List<BlockPos> nodePositions;
    private final ItemVariant variant;
    private final long amount;
    private final int vertexTime;
	
	public PastelTransmission(List<BlockPos> nodePositions, ItemVariant variant, long amount, int vertexTime) {
        this.nodePositions = nodePositions;
        this.variant = variant;
        this.amount = amount;
        this.vertexTime = vertexTime;
    }
	
	public void setNetwork(ServerPastelNetwork network) {
        this.network = network;
    }
	
	public @Nullable PastelNetwork<ServerLevel> getNetwork() {
        return this.network;
    }

    public List<BlockPos> getNodePositions() {
        return nodePositions;
    }

    public int getVertexTime() {
        return vertexTime;
    }

    public int getTransmissionDuration() {
        return vertexTime * (nodePositions.size() - 1);
    }

    public ItemVariant getVariant() {
        return this.variant;
    }

    public long getAmount() {
        return this.amount;
    }

    public BlockPos getStartPos() {
		return this.nodePositions.get(0);
    }

    @Override
    public void trigger() {
        arriveAtDestination();
    }

    private void arriveAtDestination() {
		if (nodePositions.isEmpty()) {
            return;
        }
		
		BlockPos destinationPos = nodePositions.get(nodePositions.size() - 1);
		@Nullable PastelNodeBlockEntity destinationNode = this.network.getLoadedNodeAt(destinationPos);
		Level world = this.network.getWorld();
		
		int inserted = 0;
		if (destinationNode != null) {
			Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
			if (destinationStorage != null) {
				try (Transaction transaction = Transaction.openOuter()) {
					if (destinationStorage.supportsInsertion()) {
						inserted = (int) destinationStorage.insert(variant, amount, transaction);
						destinationNode.addItemCountUnderway(-inserted);
						transaction.commit();
					}
				}
			}
		}
		if (inserted != amount) {
			long diff = amount - inserted;
			InWorldInteractionHelper.scatter(world, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, variant, diff);
			if (destinationNode != null) {
				destinationNode.addItemCountUnderway(-diff);
			}
		}
	}

}
