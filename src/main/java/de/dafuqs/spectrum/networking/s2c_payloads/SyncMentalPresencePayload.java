package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

// TODO: Why tf is that a packet and not handled in the synced component instead?
public record SyncMentalPresencePayload(double value) implements CustomPacketPayload {
	
	public static final Type<SyncMentalPresencePayload> ID = SpectrumC2SPackets.makeId("sync_mental_presence");
	public static final StreamCodec<FriendlyByteBuf, SyncMentalPresencePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, SyncMentalPresencePayload::value,
			SyncMentalPresencePayload::new
	);
	
	public static void sendMentalPresenceSync(ServerPlayer player, double value) {
		PacketDistributor.sendToPlayer(player, new SyncMentalPresencePayload(value));
	}
	
	public static void execute(SyncMentalPresencePayload payload, IPayloadContext context) {
		Player player = context.player();
		MiscPlayerDataAttachmentType.get(player).setLastSyncedSleepPotency(payload.value);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}