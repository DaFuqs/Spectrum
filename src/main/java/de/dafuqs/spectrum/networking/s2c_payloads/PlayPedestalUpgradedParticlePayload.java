package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

public record PlayPedestalUpgradedParticlePayload(BlockPos pedestalPos) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalStartCraftingParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_start_crafting_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayPedestalStartCraftingParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalStartCraftingParticlePayload::pedestalPos,
			PlayPedestalStartCraftingParticlePayload::new
	);
	
	// TODO: use
	public static void spawnPedestalStartCraftingParticles(PedestalBlockEntity pedestalBlockEntity) {
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) pedestalBlockEntity.getLevel(), new ChunkPos(pedestalBlockEntity.getBlockPos()),
				new PlayPedestalStartCraftingParticlePayload(pedestalBlockEntity.getBlockPos())
		);
	}
	
	public static void execute(PlayPedestalStartCraftingParticlePayload payload, IPayloadContext context) {
		PedestalBlock.spawnUpgradeParticleEffectsForTier(payload.pedestalPos, payload.newTier);
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}