package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.effect.ItemTransfer;
import de.dafuqs.spectrum.particle.effect.ItemTransferParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SpectrumS2CPackets {

	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_anvil_crafting_finished_particle");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings_client");
	public static final Identifier INITIATE_ITEM_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "initiate_item_transfer");
	public static final Identifier PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "item_entity_absorbed");

	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				 MinecraftClient.getInstance().player.getEntityWorld().addParticle(ParticleTypes.EXPLOSION, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			ItemStack itemStack = buf.readItemStack(); // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
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

		ClientPlayNetworking.registerGlobalReceiver(INITIATE_ITEM_TRANSFER, (client, handler, buf, responseSender) -> {
			ItemTransfer itemTransfer = ItemTransfer.readFromBuf(buf);
			BlockPos blockPos = itemTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addImportantParticle(new ItemTransferParticleEffect(itemTransfer), true, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();

			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addParticle(SpectrumParticleTypes.BLUE_BUBBLE_POP, posX, posY, posZ, 0, 0, 0);
				}
			});
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

	public static void sendItemTransferPacket(ServerWorld world, ItemTransfer itemTransfer) {
		BlockPos blockPos = itemTransfer.getOrigin();

		PacketByteBuf buf = PacketByteBufs.create();
		ItemTransfer.writeToBuf(buf, itemTransfer);

		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_ITEM_TRANSFER, buf);
		}
	}

	public static void sendPlayItemEntityAbsorbedParticle(World world, ItemEntity itemEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(itemEntity.getPos().x);
		buf.writeDouble(itemEntity.getPos().y);
		buf.writeDouble(itemEntity.getPos().z);

		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, itemEntity.getBlockPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, buf);
		}
	}

}