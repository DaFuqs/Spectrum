package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.items.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelTransmission implements SchedulerMap.Callback {
	
	public static final Codec<PastelTransmission> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockPos.CODEC.listOf().fieldOf("node_positions").forGetter(PastelTransmission::getNodePositions),
			ItemStack.CODEC.fieldOf("stack").forGetter(PastelTransmission::getVariant),
			Codec.INT.fieldOf("vertex_time").forGetter(PastelTransmission::getVertexTime)
	).apply(i, PastelTransmission::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelTransmission> PACKET_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), PastelTransmission::getNodePositions,
			ItemStack.STREAM_CODEC, PastelTransmission::getVariant,
			ByteBufCodecs.VAR_INT, PastelTransmission::getVertexTime,
			PastelTransmission::new
	);
	
	private @Nullable ServerPastelNetwork network;
	private final List<BlockPos> nodePositions;
	private final ItemStack stack;
	private final int vertexTime;
	
	public PastelTransmission(List<BlockPos> nodePositions, ItemStack stack, int vertexTime) {
		this.nodePositions = nodePositions;
		this.stack = stack;
		this.vertexTime = vertexTime;
	}
	
	public void setNetwork(@NotNull ServerPastelNetwork network) {
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
	
	public ItemStack getVariant() {
		return this.stack;
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
		
		@NotNull BlockPos destinationPos = nodePositions.get(nodePositions.size() - 1);
		@Nullable PastelNodeBlockEntity destinationNode = this.network.getLoadedNodeAt(destinationPos);
		Level world = this.network.getLevel();
		
		int inserted = 0;
		int count = stack.getCount();
		if (destinationNode != null) {
			IItemHandler destinationStorage = destinationNode.getConnectedStorage();
			if (destinationStorage != null) {
				inserted = count;
				inserted -= ItemHandlerHelper.insertItemStacked(destinationStorage, stack.copyWithCount(count), false).getCount();
				destinationNode.addItemCountUnderway(-count);
			}
		}
		it amount = stack.getCount();
		if (inserted != amount) {
			long diff = amount - inserted;
			InWorldInteractionHelper.scatter(world, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, variant, diff);
			if (destinationNode != null) {
				destinationNode.addItemCountUnderway(-diff);
			}
		}
	}
	
}
