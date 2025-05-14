package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;

import java.util.*;

public record GuidebookHintBoughtPayload(ResourceLocation completionAdvancement, IngredientStack payment) implements CustomPacketPayload {
	
	public static final Type<GuidebookHintBoughtPayload> ID = SpectrumC2SPackets.makeId("guidebook_hint_bought");
	public static final StreamCodec<RegistryFriendlyByteBuf, GuidebookHintBoughtPayload> CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, GuidebookHintBoughtPayload::completionAdvancement,
			IngredientStack.Serializer.PACKET_CODEC, GuidebookHintBoughtPayload::payment,
			GuidebookHintBoughtPayload::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.PlayPayloadHandler<GuidebookHintBoughtPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = context.player();
			for (ItemStack remainder : InventoryHelper.removeIngredientStacksFromInventoryWithRemainders(List.of(payload.payment()), player.getInventory())) {
				InventoryHelper.smartAddToInventory(remainder, player.getInventory(), null);
			}
			
			// give the player the hidden "used_tip" advancement and play a sound
			Support.grantAdvancementCriterion(player, "hidden/used_tip", "used_tip");
			Support.grantAdvancementCriterion(player, payload.completionAdvancement(), "hint_purchased");
			player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
		};
	}
	
}
