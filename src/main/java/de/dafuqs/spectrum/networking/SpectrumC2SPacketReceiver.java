package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import de.dafuqs.spectrum.inventories.CompactingChestScreenHandler;
import de.dafuqs.spectrum.inventories.ParticleSpawnerScreenHandler;
import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class SpectrumC2SPacketReceiver {
	
	public static void registerC2SReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			String name = buf.readString();
			
			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
				BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler) player.currentScreenHandler;
				String string = SharedConstants.stripInvalidChars(name);
				if (string.length() <= 50) {
					bedrockAnvilScreenHandler.setNewItemName(string);
				}
			}
		});
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			String lore = buf.readString();
			
			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
				BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler) player.currentScreenHandler;
				String string = SharedConstants.stripInvalidChars(lore);
				if (string.length() <= 256) {
					bedrockAnvilScreenHandler.setNewItemLore(string);
				}
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			// receive the client packet...
			if (player.currentScreenHandler instanceof ParticleSpawnerScreenHandler particleSpawnerScreenHandler) {
				ParticleSpawnerBlockEntity blockEntity = particleSpawnerScreenHandler.getBlockEntity();
				if (blockEntity != null) {
					/// ...apply the new settings...
					blockEntity.applySettings(buf);
					
					// ...and distribute it to all clients again
					PacketByteBuf packetByteBuf = PacketByteBufs.create();
					packetByteBuf.writeBlockPos(blockEntity.getPos());
					blockEntity.writeSettings(packetByteBuf);
					
					// Iterate over all players tracking a position in the world and send the packet to each player
					for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) blockEntity.getWorld(), blockEntity.getPos())) {
						ServerPlayNetworking.send(serverPlayerEntity, SpectrumS2CPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, packetByteBuf);
					}
				}
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.CHANGE_COMPACTING_CHEST_SETTINGS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			// receive the client packet...
			if (player.currentScreenHandler instanceof CompactingChestScreenHandler compactingChestScreenHandler) {
				BlockEntity blockEntity = compactingChestScreenHandler.getBlockEntity();
				if (blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
					/// ...apply the new settings...
					compactingChestBlockEntity.applySettings(buf);
				}
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.GUIDEBOOK_HINT_BOUGHT, (server, player, handler, buf, responseSender) -> {
			// pay cost
			Ingredient payment = Ingredient.fromPacket(buf);
			if (InventoryHelper.removeFromInventory(List.of(payment), player.getInventory(), false)) {
				// give the player the hidden "used_tip" advancement and play a sound
				Support.grantAdvancementCriterion(player, "hidden/used_tip", "used_tip");
				player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.BIND_ENDER_SPLICE_TO_PLAYER, (server, player, handler, buf, responseSender) -> {
			int entityId = buf.readInt();
			Entity entity = player.getWorld().getEntityById(entityId);
			if (entity instanceof ServerPlayerEntity targetPlayerEntity
					&& player.distanceTo(targetPlayerEntity) < 8
					&& player.getMainHandStack().isOf(SpectrumItems.ENDER_SPLICE)) {
				
				EnderSpliceItem.setTeleportTargetPlayer(player.getMainHandStack(), targetPlayerEntity);
				
				player.playSound(SpectrumSoundEvents.ENDER_SPLICE_BOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
				targetPlayerEntity.playSound(SpectrumSoundEvents.ENDER_SPLICE_BOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		});
		
	}
	
}
