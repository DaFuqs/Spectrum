package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;

public record AddLoreBedrockAnvilPayload(String lore) implements CustomPacketPayload {
	
	public static final Type<AddLoreBedrockAnvilPayload> ID = SpectrumC2SPackets.makeId("add_lore_to_item_in_bedrock_anvil");
	public static final StreamCodec<FriendlyByteBuf, AddLoreBedrockAnvilPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, AddLoreBedrockAnvilPayload::lore, AddLoreBedrockAnvilPayload::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.PlayPayloadHandler<AddLoreBedrockAnvilPayload> getPayloadHandler() {
		return (payload, context) -> {
			if (context.player().containerMenu instanceof BedrockAnvilScreenHandler bedrockAnvilScreenHandler) {
				if (!bedrockAnvilScreenHandler.stillValid(context.player())) {
					SpectrumCommon.LOGGER.debug("Player {} interacted with invalid menu {} while setting lore", context.player(), bedrockAnvilScreenHandler);
				}
				bedrockAnvilScreenHandler.setNewItemLore(payload.lore());
			}
		};
	}
	
}
