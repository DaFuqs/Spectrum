package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

public record PlayFusionCraftingInProgressParticlePayload(BlockPos pos) implements CustomPacketPayload {
	
	public static final Type<PlayFusionCraftingInProgressParticlePayload> ID = SpectrumC2SPackets.makeId("play_fusion_crafting_in_progress_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayFusionCraftingInProgressParticlePayload> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, PlayFusionCraftingInProgressParticlePayload::pos, PlayFusionCraftingInProgressParticlePayload::new);
	
	public static void sendPlayFusionCraftingInProgressParticles(ServerLevel world, BlockPos pos) {
		PacketDistributor.sendToPlayersTrackingChunk(
				world, new ChunkPos(pos), new PlayFusionCraftingInProgressParticlePayload(pos));
	}
	
	@SuppressWarnings("resource")
	public static void execute(PlayFusionCraftingInProgressParticlePayload payload, IPayloadContext context) {
		BlockEntity blockEntity = context.player().level().getBlockEntity(payload.pos);
		if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
			fusionShrineBlockEntity.spawnCraftingParticles();
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}