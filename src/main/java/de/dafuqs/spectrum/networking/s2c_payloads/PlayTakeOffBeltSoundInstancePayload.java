package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

public record PlayTakeOffBeltSoundInstancePayload() implements CustomPacketPayload {
	
	public static final Type<PlayTakeOffBeltSoundInstancePayload> ID = SpectrumC2SPackets.makeId(
			"play_take_off_belt_sound_instance");
	public static final StreamCodec<FriendlyByteBuf, PlayTakeOffBeltSoundInstancePayload> CODEC = StreamCodec.of(
			(buf, value) -> {
			}, buf -> new PlayTakeOffBeltSoundInstancePayload()
	);
	
	public static void sendPlayTakeOffBeltSoundInstance(ServerPlayer playerEntity) {
		PacketDistributor.sendToPlayer(playerEntity, new PlayTakeOffBeltSoundInstancePayload());
	}
	
	public static void execute(PlayTakeOffBeltSoundInstancePayload payload, IPayloadContext context) {
		TakeOffBeltSoundInstance.startSoundInstance();
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}