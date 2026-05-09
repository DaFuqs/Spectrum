package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

public record PlayPedestalCraftingFinishedParticlePayload(BlockPos pedestalPos, ItemStack craftedStack) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalCraftingFinishedParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_crafting_finished_particle");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayPedestalCraftingFinishedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalCraftingFinishedParticlePayload::pedestalPos,
			ItemStack.STREAM_CODEC, PlayPedestalCraftingFinishedParticlePayload::craftedStack,
			PlayPedestalCraftingFinishedParticlePayload::new
	);
	
	public static void sendPlayPedestalCraftingFinishedParticle(ServerLevel world, BlockPos pedestalPos, ItemStack craftedStack) {
		PacketDistributor.sendToPlayersTrackingChunk(world, new ChunkPos(pedestalPos), new PlayPedestalCraftingFinishedParticlePayload(pedestalPos, craftedStack));
	}
	
	@SuppressWarnings("resource")
	public static void execute(PlayPedestalCraftingFinishedParticlePayload payload, IPayloadContext context) {
		Level level = context.player().level();
		RandomSource random = level.random;
		
		for (int i = 0; i < 10; i++) {
			level.addParticle(
					new ItemParticleOption(ParticleTypes.ITEM, payload.craftedStack), payload.pedestalPos.getX() + 0.5,
					payload.pedestalPos.getY() + 1, payload.pedestalPos.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3,
					random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
			);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}