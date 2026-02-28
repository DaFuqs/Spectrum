package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HardcoreDeathAttachmentType {
	
	public static final String NAME = "hardcore_death";
	public static final AttachmentType<Boolean> ATTACHMENT_TYPE = AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build();
	
	public static void setHardcoreDeath(ServerPlayer serverPlayer) {
		serverPlayer.setData(ATTACHMENT_TYPE, true);
		serverPlayer.setGameMode(GameType.SPECTATOR);
	}
	
	public static void clearHardcoreDeath(ServerPlayer serverPlayer) {
		serverPlayer.setData(ATTACHMENT_TYPE, false);
	}
	
	public static boolean hasHardcoreDeath(LivingEntity livingEntity) {
		return livingEntity.getData(ATTACHMENT_TYPE);
	}
	
	public static boolean isInHardcore(Player player) {
		return player.hasEffect(SpectrumStatusEffects.DIVINITY);
	}
	
	public record Payload(int entityId, boolean ribbon) implements CustomPacketPayload {
		
		public static final StreamCodec<FriendlyByteBuf, HardcoreDeathAttachmentType.Payload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, HardcoreDeathAttachmentType.Payload::entityId,
				ByteBufCodecs.BOOL, HardcoreDeathAttachmentType.Payload::ribbon,
				HardcoreDeathAttachmentType.Payload::new
		);
		
		public static final Type<HardcoreDeathAttachmentType.Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static void execute(HardcoreDeathAttachmentType.Payload payload, IPayloadContext context) {
			Optional.ofNullable(context.player().level().getEntity(payload.entityId)).ifPresent(e -> e.setData(ATTACHMENT_TYPE, payload.ribbon));
		}
		
		@Override
		public @NotNull Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
}
