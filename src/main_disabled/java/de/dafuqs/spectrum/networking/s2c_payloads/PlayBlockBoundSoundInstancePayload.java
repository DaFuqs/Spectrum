package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

// MaxDurationTicks of <1 means "stop playing"
public record PlayBlockBoundSoundInstancePayload(SoundEvent soundEvent, BlockPos pos, Holder<Block> block, int maxDurationTicks) implements CustomPacketPayload {
	
	public static final Type<PlayBlockBoundSoundInstancePayload> ID = SpectrumC2SPackets.makeId("play_block_bound_sound_instance");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayBlockBoundSoundInstancePayload> CODEC = StreamCodec.composite(
			SoundEvent.DIRECT_STREAM_CODEC, PlayBlockBoundSoundInstancePayload::soundEvent,
			BlockPos.STREAM_CODEC, PlayBlockBoundSoundInstancePayload::pos,
			ByteBufCodecs.holderRegistry(Registries.BLOCK), PlayBlockBoundSoundInstancePayload::block,
			ByteBufCodecs.INT, PlayBlockBoundSoundInstancePayload::maxDurationTicks,
			PlayBlockBoundSoundInstancePayload::new
	);
	
	public static void sendPlayBlockBoundSoundInstance(SoundEvent soundEvent, @NotNull ServerLevel world, BlockPos pos, int maxDurationTicks) {
		for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
			ServerPlayNetworking.send(player, new PlayBlockBoundSoundInstancePayload(soundEvent, pos, world.getBlockState(pos).getBlock().builtInRegistryHolder(), maxDurationTicks));
		}
	}
	
	public static void sendCancelBlockBoundSoundInstance(@NotNull ServerLevel world, BlockPos pos) {
		for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
			ServerPlayNetworking.send(player, new PlayBlockBoundSoundInstancePayload(SoundEvents.EMPTY, pos, world.getBlockState(pos).getBlock().builtInRegistryHolder(), -1));
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static void execute(PlayBlockBoundSoundInstancePayload payload, ClientPlayNetworking.Context context) {
		if (payload.maxDurationTicks < 0) {
			CraftingBlockSoundInstance.stopPlayingOnPos(payload.pos);
		} else {
			CraftingBlockSoundInstance.startSoundInstance(payload.soundEvent, payload.pos, payload.block.value(), payload.maxDurationTicks);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
