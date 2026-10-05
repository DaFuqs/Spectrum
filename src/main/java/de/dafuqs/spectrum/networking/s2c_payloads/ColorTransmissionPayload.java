package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

public record ColorTransmissionPayload(BlockPos pos, ColoredTransmission transmission) implements CustomPacketPayload {
	
	public static final Type<ColorTransmissionPayload> ID = SpectrumC2SPackets.makeId("color_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, ColorTransmissionPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ColorTransmissionPayload::pos,
			ColoredTransmission.PACKET_CODEC, ColorTransmissionPayload::transmission,
			ColorTransmissionPayload::new
	);
	
	public static void playColorTransmissionParticle(ServerLevel world, ColoredTransmission transmission) {
		var pos = BlockPos.containing(transmission.getOrigin());
		PacketDistributor.sendToPlayersTrackingChunk(
				world, new ChunkPos(pos), new ColorTransmissionPayload(pos, transmission));
	}
	
	@SuppressWarnings("resource")
	public static void execute(ColorTransmissionPayload payload, IPayloadContext context) {
		var level = context.player().level();
		ColoredTransmission transmission = payload.transmission;
		level.addAlwaysVisibleParticle(new ColoredTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks(), transmission.getDyeColor()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
