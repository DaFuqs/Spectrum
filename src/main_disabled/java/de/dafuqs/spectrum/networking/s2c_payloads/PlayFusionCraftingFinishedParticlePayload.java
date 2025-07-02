package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public record PlayFusionCraftingFinishedParticlePayload(BlockPos pos, InkColor color) implements CustomPacketPayload {
	
	public static final Type<PlayFusionCraftingFinishedParticlePayload> ID = SpectrumC2SPackets.makeId("play_fusion_crafting_finished_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayFusionCraftingFinishedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayFusionCraftingFinishedParticlePayload::pos,
			InkColor.PACKET_CODEC, PlayFusionCraftingFinishedParticlePayload::color,
			PlayFusionCraftingFinishedParticlePayload::new
	);
	
	public static void sendPlayFusionCraftingFinishedParticles(Level world, BlockPos pos, @NotNull ItemStack itemStack) {
		InkColor inkColor = ColorRegistry.ITEM_COLORS.getMapping(itemStack.getItem(), InkColors.LIGHT_GRAY);
		
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) world, pos)) {
			ServerPlayNetworking.send(player, new PlayFusionCraftingFinishedParticlePayload(pos, inkColor));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayFusionCraftingFinishedParticlePayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		BlockPos pos = payload.pos;
		Vec3 sourcePos = new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		
		Vector3f color = payload.color.getColorVec();
		float velocityModifier = 0.25F;
		for (Vec3 velocity : VectorPattern.SIXTEEN.getVectors()) {
			client.level.addParticle(
					new DynamicParticleEffect(ColoredCraftingParticleEffect.of(payload.color.getColorInt()).getType(), 0.0F, color, 1.5F, 40, false, true),
					sourcePos.x, sourcePos.y, sourcePos.z,
					velocity.x * velocityModifier, 0.0F, velocity.z * velocityModifier
			);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
