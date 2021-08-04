package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;

public class AddLoreToItemC2SPacket implements Packet<ServerPlayPacketListener> {

   private final String lore;

   public AddLoreToItemC2SPacket(String lore) {
      this.lore = lore;
   }

   public AddLoreToItemC2SPacket(PacketByteBuf buf) {
      this.lore = buf.readString();
   }

   public void write(PacketByteBuf buf) {
      buf.writeString(this.lore);
   }

   public void apply(ServerPlayPacketListener serverPlayPacketListener) {
      //NetworkThreadUtils.forceMainThread(this, this, (ServerWorld)MinecraftClient.getInstance().player.getEntityWorld());
      if (MinecraftClient.getInstance().player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
         BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler) MinecraftClient.getInstance().player.currentScreenHandler;
         String string = SharedConstants.stripInvalidChars(this.getLore());
         if (string.length() <= 50) {
            bedrockAnvilScreenHandler.setNewItemLore(string);
         }
      }
   }

   public String getLore() {
      return this.lore;
   }
}
