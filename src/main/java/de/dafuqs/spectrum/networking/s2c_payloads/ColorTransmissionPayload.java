package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import org.jetbrains.annotations.*;

public record ColorTransmissionPayload(BlockPos pos, ColoredTransmission transmission) implements CustomPacketPayload {
	
	public static final Type<ColorTransmissionPayload> ID = SpectrumC2SPackets.makeId("color_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, ColorTransmissionPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ColorTransmissionPayload::pos,
			ColoredTransmission.PACKET_CODEC, ColorTransmissionPayload::transmission,
			ColorTransmissionPayload::new
	);
	
	public static void playColorTransmissionParticle(ServerLevel world, @NotNull ColoredTransmission transmission) {
		BlockPos pos = BlockPos.containing(transmission.getOrigin());
		
		var buf = new RegistryFriendlyByteBuf(PacketByteBufs.create(), world.registryAccess());
		ColoredTransmission.PACKET_CODEC.encode(buf, transmission);
		
		for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
			//TODO should we be creating a new payload object for each?
			ServerPlayNetworking.send(player, new ColorTransmissionPayload(pos, transmission));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(ColorTransmissionPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		if (client.level == null) return;
		ColoredTransmission transmission = payload.transmission;
		client.level.addAlwaysVisibleParticle(new ColoredTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks(), transmission.getDyeColor()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
