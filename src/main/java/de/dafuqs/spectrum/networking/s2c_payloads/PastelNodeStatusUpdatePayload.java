package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

import java.util.*;

public record PastelNodeStatusUpdatePayload(boolean longSpin, Map<BlockPos, Integer> spinTimes) implements CustomPacketPayload {
	
	public static final Type<PastelNodeStatusUpdatePayload> ID = SpectrumC2SPackets.makeId("pastel_node_status_update");
	public static final StreamCodec<FriendlyByteBuf, PastelNodeStatusUpdatePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, PastelNodeStatusUpdatePayload::longSpin,
			ByteBufCodecs.map(Object2IntArrayMap::new, BlockPos.STREAM_CODEC, ByteBufCodecs.INT),
			PastelNodeStatusUpdatePayload::spinTimes,
			PastelNodeStatusUpdatePayload::new
	);
	
	public static void sendPastelNodeStatusUpdate(List<PastelNodeBlockEntity> nodes, boolean longSpin) {
		Map<BlockPos, Integer> spinTimes = new Object2IntArrayMap<>();
		for (PastelNodeBlockEntity node : nodes) {
			Level world = node.getLevel();
			if (world == null) {
				continue;
			}
			int time = longSpin ? 24 + world.getRandom().nextInt(11) : 10 + world.getRandom().nextInt(11);
			spinTimes.put(node.getBlockPos(), time);
		}
		
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) nodes.getFirst().getLevel(), new ChunkPos(nodes.getFirst().getBlockPos()),
				new PastelNodeStatusUpdatePayload(longSpin, spinTimes)
		);
	}
	
	public static void execute(PastelNodeStatusUpdatePayload payload, IPayloadContext context) {
		Level level = context.player().level();
		for (Map.Entry<BlockPos, Integer> e : payload.spinTimes.entrySet()) {
			BlockEntity entity = level.getBlockEntity(e.getKey());
			if (!(entity instanceof PastelNodeBlockEntity node))
				continue;
			
			node.setSpinTicks(e.getValue());
			
			if (payload.longSpin && node.isTriggerTransfer()) {
				node.markTriggered();
			}
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}