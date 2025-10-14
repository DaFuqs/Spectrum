package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.level.dimension.*;
import org.jetbrains.annotations.*;

public record StartSkyLerpingPayload(long startTime, long endTime) implements CustomPacketPayload {
	
	public static final Type<StartSkyLerpingPayload> ID = SpectrumC2SPackets.makeId("start_sky_lerping");
	public static final StreamCodec<FriendlyByteBuf, StartSkyLerpingPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, StartSkyLerpingPayload::startTime,
			ByteBufCodecs.VAR_LONG, StartSkyLerpingPayload::endTime,
			StartSkyLerpingPayload::new
	);
	
	public static void startSkyLerping(@NotNull ServerLevel serverWorld, int additionalTime) {
		long timeOfDay = serverWorld.getDayTime();
		PacketDistributor.sendToPlayersInDimension(
				serverWorld, new StartSkyLerpingPayload(timeOfDay, timeOfDay + additionalTime));
	}
	
	@SuppressWarnings("resource")
	public static void execute(StartSkyLerpingPayload payload, IPayloadContext context) {
		var client = Minecraft.getInstance();
		Level level = context.player()
				.level();
		DimensionType dimensionType = level.dimensionType();
		
		SpectrumClient.skyLerper.trigger(
				dimensionType, payload.startTime, client.getTimer()
						.getGameTimeDeltaPartialTick(false), payload.endTime
		);
		if (level.canSeeSky(client.player.blockPosition())) {
			level.playSound(
					null, client.player.blockPosition(), SpectrumSounds.CELESTIAL_POCKET_WATCH_FLY_BY,
					SoundSource.NEUTRAL, 0.15F, 1.0F
			);
		}
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}