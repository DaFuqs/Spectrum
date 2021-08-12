package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

public class SpectrumC2SPackets {

    public static final Identifier RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "rename_item_in_bedrock_anvil");
    public static final Identifier ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "add_lore_to_item_in_bedrock_anvil");

    public static void registerC2SReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            SpectrumCommon.log(Level.INFO, "rename_packet");
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
    }

}
