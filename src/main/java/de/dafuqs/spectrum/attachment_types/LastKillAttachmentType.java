package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.network.handling.*;

import java.util.*;

public class LastKillAttachmentType {
	
	public static final String NAME = "last_kill";
	public static final AttachmentType<Long> ATTACHMENT_TYPE = AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build();
	
	public static void rememberKillTick(LivingEntity livingEntity, long tick) {
		livingEntity.setData(ATTACHMENT_TYPE, tick);
	}
	
	public static long getLastKillTick(LivingEntity livingEntity) {
		return livingEntity.getData(ATTACHMENT_TYPE);
	}
	
	public record Payload(int entityId, long killTime) implements CustomPacketPayload {
		
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, Payload::entityId,
				ByteBufCodecs.VAR_LONG, Payload::killTime,
				Payload::new
		);
		
		public static final Type<LastKillAttachmentType.Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static void execute(Payload payload, IPayloadContext context) {
			Level level = context.player().level();
			Optional.ofNullable(level.getEntity(payload.entityId)).ifPresent(e -> e.setData(ATTACHMENT_TYPE, payload.killTime));
		}
		
		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
}
