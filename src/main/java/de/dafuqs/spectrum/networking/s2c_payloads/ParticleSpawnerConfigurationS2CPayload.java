package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

public record ParticleSpawnerConfigurationS2CPayload(BlockPos pos, ParticleSpawnerConfiguration configuration) implements CustomPacketPayload {
	
	public static final Type<ParticleSpawnerConfigurationS2CPayload> ID = SpectrumC2SPackets.makeId("change_particle_spawner_settings_client");
	public static final StreamCodec<FriendlyByteBuf, ParticleSpawnerConfigurationS2CPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ParticleSpawnerConfigurationS2CPayload::pos,
			ParticleSpawnerConfiguration.PACKET_CODEC, ParticleSpawnerConfigurationS2CPayload::configuration,
			ParticleSpawnerConfigurationS2CPayload::new
	);
	
	@SuppressWarnings("resource")
	public static void execute(ParticleSpawnerConfigurationS2CPayload payload, IPayloadContext context) {
		if (context.player().level().getBlockEntity(payload.pos()) instanceof ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
			particleSpawnerBlockEntity.applySettings(payload.configuration());
		}
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}