package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public record RenameItemInBedrockAnvilPayload(String name) implements CustomPacketPayload {
	
	public static final Type<RenameItemInBedrockAnvilPayload> ID = SpectrumC2SPackets.makeId("rename_item_in_bedrock_anvil");
	public static final StreamCodec<FriendlyByteBuf, RenameItemInBedrockAnvilPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, RenameItemInBedrockAnvilPayload::name, RenameItemInBedrockAnvilPayload::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.@NotNull PlayPayloadHandler<RenameItemInBedrockAnvilPayload> getPayloadHandler() {
		return (payload, context) -> {
			if (context.player().containerMenu instanceof BedrockAnvilScreenHandler bedrockAnvilScreenHandler) {
				String string = StringUtil.filterText(payload.name);
				if (string.length() <= 50) {
					bedrockAnvilScreenHandler.setNewItemName(string);
				}
			}
		};
	}
	
}
