package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import de.dafuqs.spectrum.inventories.CompactingChestScreenHandler;
import de.dafuqs.spectrum.inventories.ParticleSpawnerScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;

public class SpectrumC2SPackets {

	public static final Identifier RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "rename_item_in_bedrock_anvil");
	public static final Identifier ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "add_lore_to_item_in_bedrock_anvil");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings");
	public static final Identifier CHANGE_COMPACTING_CHEST_SETTINGS_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_compacting_chest_settings");
	public static final Identifier GUIDEBOOK_HINT_BOUGHT = new Identifier(SpectrumCommon.MOD_ID, "guidebook_tip_used");

	public static void registerC2SReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			String name = buf.readString();

			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
				BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler)player.currentScreenHandler;
				String string = SharedConstants.stripInvalidChars(name);
				if (string.length() <= 50) {
					bedrockAnvilScreenHandler.setNewItemName(string);
				}
			}
		});
		ServerPlayNetworking.registerGlobalReceiver(ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			String lore = buf.readString();

			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
				BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler) player.currentScreenHandler;
				String string = SharedConstants.stripInvalidChars(lore);
				if (string.length() <= 256) {
					bedrockAnvilScreenHandler.setNewItemLore(string);
				}
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			// receive the client packet...
			if(player.currentScreenHandler instanceof ParticleSpawnerScreenHandler particleSpawnerScreenHandler) {
				ParticleSpawnerBlockEntity blockEntity = particleSpawnerScreenHandler.getBlockEntity();
				if(blockEntity != null) {
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

		ServerPlayNetworking.registerGlobalReceiver(CHANGE_COMPACTING_CHEST_SETTINGS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			// receive the client packet...
			if(player.currentScreenHandler instanceof CompactingChestScreenHandler compactingChestScreenHandler) {
				BlockEntity blockEntity = compactingChestScreenHandler.getBlockEntity();
				if(blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
					/// ...apply the new settings...
					compactingChestBlockEntity.applySettings(buf);
				}
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(GUIDEBOOK_HINT_BOUGHT, (server, player, handler, buf, responseSender) -> {
			// pay cost
			Ingredient payment = Ingredient.fromPacket(buf);
			if(InventoryHelper.removeFromInventory(List.of(payment), player.getInventory(), false)) {
				// give the player the hidden "used_tip" advancement and play a sound
				Support.grantAdvancementCriterion(player, "hidden/used_tip", "used_tip");
				player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		});
		
	}
	
	@Environment(EnvType.CLIENT)
	public static void sendGuidebookHintBoughtPaket(Ingredient ingredient) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		ingredient.write(packetByteBuf);
		ClientPlayNetworking.send(SpectrumC2SPackets.GUIDEBOOK_HINT_BOUGHT, packetByteBuf);
	}

}
