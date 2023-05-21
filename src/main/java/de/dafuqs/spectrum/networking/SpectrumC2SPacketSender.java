package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.items.tools.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import org.jetbrains.annotations.*;

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
		if (color == null) {
			packetByteBuf.writeBoolean(false);
		} else {
			packetByteBuf.writeBoolean(true);
			packetByteBuf.writeString(color.toString());
		}
		ClientPlayNetworking.send(SpectrumC2SPackets.INK_COLOR_SELECTED, packetByteBuf);
	}

    public static void sendWorkstaffToggle(WorkstaffItem.GUIToggle toggle) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(toggle.ordinal());
        ClientPlayNetworking.send(SpectrumC2SPackets.WORKSTAFF_TOGGLE_SELECTED, packetByteBuf);
    }
	
}
