package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.screen.AnvilScreenHandler;

public class RenameItemInBedrockAnvilC2SPacket implements Packet<ServerPlayPacketListener> {
    private final String name;

    public RenameItemInBedrockAnvilC2SPacket(String name) {
        this.name = name;
    }

    public RenameItemInBedrockAnvilC2SPacket(PacketByteBuf buf) {
        this.name = buf.readString();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.name);
    }

    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        //NetworkThreadUtils.forceMainThread(this, this, (ServerWorld)MinecraftClient.getInstance().player.getEntityWorld());
        if (MinecraftClient.getInstance().player.currentScreenHandler instanceof BedrockAnvilScreenHandler) {
            BedrockAnvilScreenHandler bedrockAnvilScreenHandler = (BedrockAnvilScreenHandler) MinecraftClient.getInstance().player.currentScreenHandler;
            String string = SharedConstants.stripInvalidChars(this.getName());
            if (string.length() <= 50) {
                bedrockAnvilScreenHandler.setNewItemName(string);
            }
        }
    }

    public String getName() {
        return this.name;
    }
}
