package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.Random;

public class SpectrumS2CPackets {

	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_anvil_crafting_finished_particle");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings_client");


	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			client.execute(() -> {
				// Everything in this lambda is run on the render thread
				 MinecraftClient.getInstance().player.getEntityWorld().addParticle(ParticleTypes.EXPLOSION, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			ItemStack itemStack = buf.readItemStack(); // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is run on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			if(MinecraftClient.getInstance().world.getBlockEntity(pos) instanceof ParticleSpawnerBlockEntity) {
				((ParticleSpawnerBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos)).applySettings(buf);
			}

		});
	}

	/**
	 *
	 * @param world the world of the pedestal
	 * @param blockPos the blockpos of the pedestal
	 * @param itemStack the itemstack that was crafted
	 */
	public static void sendPlayPedestalCraftingFinishedParticle(World world, BlockPos blockPos, ItemStack itemStack) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		buf.writeItemStack(itemStack);
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, buf);
		}
	}

}