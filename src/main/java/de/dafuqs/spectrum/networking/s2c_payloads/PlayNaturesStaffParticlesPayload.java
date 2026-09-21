package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.joml.*;

public record PlayNaturesStaffParticlesPayload(BlockPos pos) implements CustomPacketPayload {
	
	public static final Type<PlayNaturesStaffParticlesPayload> ID = SpectrumC2SPackets.makeId("natures_staff_particles");
	public static final StreamCodec<FriendlyByteBuf, PlayNaturesStaffParticlesPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayNaturesStaffParticlesPayload::pos,
			PlayNaturesStaffParticlesPayload::new
	);
	
	public static void sendPlayParticles(Level world, BlockPos pos) {
		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) world, new ChunkPos(pos), new PlayNaturesStaffParticlesPayload(pos));
	}
	
	public static void execute(PlayNaturesStaffParticlesPayload payload, IPayloadContext context) {
		NaturesStaffItem.spawnParticlesAndEffect(context.player().level(), payload.pos);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}