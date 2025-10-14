package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

public record BindEnderSpliceToPlayerPayload(int entityId) implements CustomPacketPayload {
	
	public static final Type<BindEnderSpliceToPlayerPayload> ID = SpectrumC2SPackets.makeId("bind_ender_splice_to_player");
	public static final StreamCodec<FriendlyByteBuf, BindEnderSpliceToPlayerPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, BindEnderSpliceToPlayerPayload::entityId, BindEnderSpliceToPlayerPayload::new);
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static IPayloadHandler<BindEnderSpliceToPlayerPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = (ServerPlayer) context.player();
			Entity entity = player.level().getEntity(payload.entityId());
			if (entity instanceof ServerPlayer targetPlayerEntity && player.distanceTo(targetPlayerEntity) < 8 && player.getMainHandItem().is(SpectrumItems.ENDER_SPLICE.get())) {
				EnderSpliceItem.setTeleportTargetPlayer(player.getMainHandItem(), targetPlayerEntity);
				player.playSound(SpectrumSoundEvents.ENDER_SPLICE_BOUND, 1.0F, 1.0F);
				targetPlayerEntity.playSound(SpectrumSoundEvents.ENDER_SPLICE_BOUND, 1.0F, 1.0F);
			}
		};
	}
	
}