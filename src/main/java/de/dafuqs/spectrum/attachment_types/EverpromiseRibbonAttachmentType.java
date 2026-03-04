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
import org.jetbrains.annotations.*;

import java.util.*;

public class EverpromiseRibbonAttachmentType {
	
	public static final String NAME = "everpromise_ribbon";
	public static final AttachmentType<Boolean> ATTACHMENT_TYPE = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();
	
	public static void attachRibbon(LivingEntity livingEntity) {
		livingEntity.setData(ATTACHMENT_TYPE, true);
	}
	
	public static boolean hasRibbon(LivingEntity livingEntity) {
		return livingEntity.getData(ATTACHMENT_TYPE);
	}
	
	public record Payload(int entityId, boolean hasRibbonAttached) implements CustomPacketPayload {
		
		public static final Type<Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, Payload::entityId,
				ByteBufCodecs.BOOL, Payload::hasRibbonAttached,
				Payload::new
		);
		
		public static void execute(Payload payload, IPayloadContext context) {
			Level level = context.player().level();
			Optional.ofNullable(level.getEntity(payload.entityId)).ifPresent(e -> e.setData(ATTACHMENT_TYPE, payload.hasRibbonAttached));
		}
		
		@Override
		public @NotNull Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
}
