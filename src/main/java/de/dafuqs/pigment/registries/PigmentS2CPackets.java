package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PigmentS2CPackets {
	
	public static final Identifier PLAY_PARTICLE_PACKET_ID = new Identifier(PigmentCommon.MOD_ID, "particle");


	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {

			BlockPos position = buf.readBlockPos();

			client.execute(() -> {
				// Everything in this lambda is run on the render thread
				 MinecraftClient.getInstance().player.getEntityWorld().addParticle(ParticleTypes.EXPLOSION, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
			});
		});
	}

}