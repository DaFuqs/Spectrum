package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public record PlayPedestalCraftingFinishedParticlePayload(BlockPos pedestalPos, ItemStack craftedStack) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalCraftingFinishedParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_crafting_finished_particle");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayPedestalCraftingFinishedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalCraftingFinishedParticlePayload::pedestalPos,
			ItemStack.STREAM_CODEC, PlayPedestalCraftingFinishedParticlePayload::craftedStack,
			PlayPedestalCraftingFinishedParticlePayload::new
	);
	
	public static void sendPlayPedestalCraftingFinishedParticle(ServerLevel world, BlockPos pedestalPos, ItemStack craftedStack) {
		for (ServerPlayer player : PlayerLookup.tracking(world, pedestalPos)) {
			ServerPlayNetworking.send(player, new PlayPedestalCraftingFinishedParticlePayload(pedestalPos, craftedStack));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayPedestalCraftingFinishedParticlePayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		ClientLevel world = client.level;
		RandomSource random = world.random;
		
		for (int i = 0; i < 10; i++) {
			world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, payload.craftedStack), payload.pedestalPos.getX() + 0.5, payload.pedestalPos.getY() + 1, payload.pedestalPos.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
