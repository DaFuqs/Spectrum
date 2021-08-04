package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.AddLoreToItemC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SpectrumPackets {

	public static final Identifier PLAY_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "particle");
	public static final Identifier ADD_LORE_TO_ITEM_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "add_lore_to_item");


	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {

			BlockPos position = buf.readBlockPos();

			client.execute(() -> {
				// Everything in this lambda is run on the render thread
				 MinecraftClient.getInstance().player.getEntityWorld().addParticle(ParticleTypes.EXPLOSION, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
			});
		});
	}

	public static void registerC2SReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(ADD_LORE_TO_ITEM_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			Packet packet = new AddLoreToItemC2SPacket(buf);
			packet.apply(handler);
		});
	}

}