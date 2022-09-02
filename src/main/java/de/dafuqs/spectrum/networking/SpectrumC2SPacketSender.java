package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpectrumC2SPacketSender {
	
	public static void sendGuidebookHintBoughtPaket(Ingredient ingredient) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		ingredient.write(packetByteBuf);
		ClientPlayNetworking.send(SpectrumC2SPackets.GUIDEBOOK_HINT_BOUGHT, packetByteBuf);
	}
	
	public static void sendConfirmationButtonPressedPaket(String queryToTrigger) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeString(queryToTrigger);
		ClientPlayNetworking.send(SpectrumC2SPackets.CONFIRMATION_BUTTON_PRESSED, packetByteBuf);
	}
	
	public static void sendBindEnderSpliceToPlayer(PlayerEntity playerEntity) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeInt(playerEntity.getId());
		ClientPlayNetworking.send(SpectrumC2SPackets.BIND_ENDER_SPLICE_TO_PLAYER, packetByteBuf);
	}
	
	public static void sendInkColorSelectedInGUI(@Nullable InkColor color) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		if(color == null) {
			packetByteBuf.writeBoolean(false);
		} else {
			packetByteBuf.writeBoolean(true);
			packetByteBuf.writeString(color.toString());
		}
		ClientPlayNetworking.send(SpectrumC2SPackets.INK_COLOR_SELECTED, packetByteBuf);
	}
	
}
