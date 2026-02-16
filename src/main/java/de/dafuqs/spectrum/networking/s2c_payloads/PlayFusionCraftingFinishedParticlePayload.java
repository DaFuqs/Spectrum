package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public record PlayFusionCraftingFinishedParticlePayload(BlockPos pos, InkColor color) implements CustomPacketPayload {
	
	public static final Type<PlayFusionCraftingFinishedParticlePayload> ID = SpectrumC2SPackets.makeId("play_fusion_crafting_finished_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayFusionCraftingFinishedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayFusionCraftingFinishedParticlePayload::pos,
			InkColor.PACKET_CODEC, PlayFusionCraftingFinishedParticlePayload::color,
			PlayFusionCraftingFinishedParticlePayload::new
	);
	
	public static void sendPlayFusionCraftingFinishedParticles(
			Level world, BlockPos pos, @NotNull ItemStack itemStack) {
		InkColor inkColor = ColorRegistry.ITEM_COLORS.getInkColor(itemStack.getItem(), InkColors.LIGHT_GRAY);
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) world, new ChunkPos(pos), new PlayFusionCraftingFinishedParticlePayload(pos, inkColor));
	}
	
	@SuppressWarnings("resource")
	public static void execute(PlayFusionCraftingFinishedParticlePayload payload, IPayloadContext context) {
		BlockPos pos = payload.pos;
		Vec3 sourcePos = new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		
		Vector3f color = payload.color.getColorVec();
		float velocityModifier = 0.25F;
		for (Vec3 velocity : VectorPattern.SIXTEEN.getVectors()) {
			context.player().level().addParticle(
					new DynamicParticleEffect(ColoredCraftingParticleEffect.of(payload.color.getColorInt()).getType(), 0.0F, color, 1.5F, 40, false, true),
					sourcePos.x, sourcePos.y, sourcePos.z,
					velocity.x * velocityModifier, 0.0F, velocity.z * velocityModifier
			);
		}
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}