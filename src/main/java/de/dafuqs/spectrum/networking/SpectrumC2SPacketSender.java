package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;

public class SpectrumC2SPacketSender {
	
	@Environment(EnvType.CLIENT)
	public static void sendGuidebookHintBoughtPaket(Ingredient ingredient) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		ingredient.write(packetByteBuf);
		ClientPlayNetworking.send(SpectrumC2SPackets.GUIDEBOOK_HINT_BOUGHT, packetByteBuf);
	}
	
	@Environment(EnvType.CLIENT)
	public static void sendBindEnderSpliceToPlayer(PlayerEntity playerEntity) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeInt(playerEntity.getId());
		ClientPlayNetworking.send(SpectrumC2SPackets.BIND_ENDER_SPLICE_TO_PLAYER, packetByteBuf);
	}
	
	@Environment(EnvType.CLIENT)
	public static void sendInkColorSelectedInGUI(PlayerEntity playerEntity, InkColor color) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeString(color.toString());
		ClientPlayNetworking.send(SpectrumC2SPackets.INK_COLOR_SELECTED_IN_GUI, packetByteBuf);
	}
	
}
