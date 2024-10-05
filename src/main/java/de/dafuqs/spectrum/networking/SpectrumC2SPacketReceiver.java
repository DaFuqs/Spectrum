package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

import java.util.*;

public class SpectrumC2SPacketReceiver {
	
	public static void registerC2SReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler bedrockAnvilScreenHandler) {
				String name = buf.readString();
				String string = SharedConstants.stripInvalidChars(name);
				if (string.length() <= 50) {
					bedrockAnvilScreenHandler.setNewItemName(string);
				}
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			if (player.currentScreenHandler instanceof BedrockAnvilScreenHandler bedrockAnvilScreenHandler) {
				String lore = buf.readString();
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
					ParticleSpawnerConfiguration configuration = ParticleSpawnerConfiguration.fromBuf(buf);
					blockEntity.applySettings(configuration);
					
					// ...and distribute it to all clients again
					PacketByteBuf outgoingBuf = PacketByteBufs.create();
					outgoingBuf.writeBlockPos(blockEntity.getPos());
					configuration.write(outgoingBuf);
					
					// Iterate over all players tracking a position in the world and send the packet to each player
					for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) blockEntity.getWorld(), blockEntity.getPos())) {
						ServerPlayNetworking.send(serverPlayerEntity, SpectrumS2CPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, outgoingBuf);
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
			Identifier completionAdvancement = buf.readIdentifier();
			Ingredient payment = Ingredient.fromPacket(buf);

			for (ItemStack remainder : InventoryHelper.removeFromInventoryWithRemainders(List.of(payment), player.getInventory())) {
				InventoryHelper.smartAddToInventory(remainder, player.getInventory(), null);
			}
			
			// give the player the hidden "used_tip" advancement and play a sound
			Support.grantAdvancementCriterion(player, "hidden/used_tip", "used_tip");
			Support.grantAdvancementCriterion(player, completionAdvancement, "hint_purchased");
			player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.CONFIRMATION_BUTTON_PRESSED, (server, player, handler, buf, responseSender) -> {
			String confirmationString = buf.readString();
			SpectrumAdvancementCriteria.CONFIRMATION_BUTTON_PRESSED.trigger(player, confirmationString);
			player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
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
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.INK_COLOR_SELECTED, (server, player, handler, buf, responseSender) -> {
			ScreenHandler screenHandler = player.currentScreenHandler;
			if (screenHandler instanceof InkColorSelectedPacketReceiver inkColorSelectedPacketReceiver) {
				boolean isSelection = buf.readBoolean();
				
				InkColor color = null;
				if (isSelection) {
					Identifier inkColor = buf.readIdentifier();
					Optional<InkColor> optionalColor = InkColor.ofId(inkColor);
					if (optionalColor.isPresent()) {
						color = optionalColor.get();
					}
				}
				
				// send the newly selected color to all players that have the same gui open
				// this is minus the player that selected that entry (since they have that info already)
				inkColorSelectedPacketReceiver.onInkColorSelectedPacket(color);
				for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList()) {
					if (serverPlayer.currentScreenHandler instanceof InkColorSelectedPacketReceiver receiver && receiver.getBlockEntity() != null && receiver.getBlockEntity() == inkColorSelectedPacketReceiver.getBlockEntity()) {
						SpectrumS2CPacketSender.sendInkColorSelected(color, serverPlayer);
					}
				}
			}
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.WORKSTAFF_TOGGLE_SELECTED, (server, player, handler, buf, responseSender) -> {
			ScreenHandler screenHandler = player.currentScreenHandler;
			if (screenHandler instanceof WorkstaffScreenHandler workstaffScreenHandler) {
                WorkstaffItem.GUIToggle toggle = WorkstaffItem.GUIToggle.values()[buf.readInt()];
				workstaffScreenHandler.onWorkstaffToggleSelectionPacket(toggle);
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(SpectrumC2SPackets.SET_SHADOW_SLOT, (server, player, handler, buf, responseSender) -> {
			ScreenHandler screenHandler = player.currentScreenHandler;

			int syncId = buf.readInt();
			if (screenHandler == null || screenHandler.syncId != syncId) {
				buf.skipBytes(buf.readableBytes());
				return;
			}

			Slot slot = screenHandler.getSlot(buf.readInt());
			if (slot == null || !(slot instanceof ShadowSlot) || !(slot.inventory instanceof FilterConfigurable.FilterInventory filterInventory)) {
				buf.skipBytes(buf.readableBytes());
				return;
			}

            @SuppressWarnings("UnstableApiUsage")
			ItemStack shadowStack = ItemVariant.fromPacket(buf).toStack(buf.readInt());

			filterInventory.getClicker().clickShadowSlot(syncId, slot, shadowStack);
		});
		
	}
	
}
