package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.protocol.common.custom.*;

public class SpectrumC2SPackets {
	
	public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> makeId(String id) {
		return new CustomPacketPayload.Type<>(SpectrumCommon.locate(id));
	}
	
	public static void register() {
		PayloadTypeRegistry.playC2S().register(AddLoreBedrockAnvilPayload.ID, AddLoreBedrockAnvilPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(BindEnderSpliceToPlayerPayload.ID, BindEnderSpliceToPlayerPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ChangeCompactingChestSettingsPayload.ID, ChangeCompactingChestSettingsPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(GuidebookConfirmationButtonPressedPayload.ID, GuidebookConfirmationButtonPressedPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(GuidebookHintBoughtPayload.ID, GuidebookHintBoughtPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(InkColorSelectedC2SPayload.ID, InkColorSelectedC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ParticleSpawnerConfigurationC2SPayload.ID, ParticleSpawnerConfigurationC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(RenameItemInBedrockAnvilPayload.ID, RenameItemInBedrockAnvilPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetShadowSlotPayload.ID, SetShadowSlotPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(WorkstaffToggleSelectedPayload.ID, WorkstaffToggleSelectedPayload.CODEC);
		
		ServerPlayNetworking.registerGlobalReceiver(AddLoreBedrockAnvilPayload.ID, AddLoreBedrockAnvilPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(BindEnderSpliceToPlayerPayload.ID, BindEnderSpliceToPlayerPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(ChangeCompactingChestSettingsPayload.ID, ChangeCompactingChestSettingsPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(GuidebookConfirmationButtonPressedPayload.ID, GuidebookConfirmationButtonPressedPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(GuidebookHintBoughtPayload.ID, GuidebookHintBoughtPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(InkColorSelectedC2SPayload.ID, InkColorSelectedC2SPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(ParticleSpawnerConfigurationC2SPayload.ID, ParticleSpawnerConfigurationC2SPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(RenameItemInBedrockAnvilPayload.ID, RenameItemInBedrockAnvilPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(SetShadowSlotPayload.ID, SetShadowSlotPayload.getPayloadHandler());
		ServerPlayNetworking.registerGlobalReceiver(WorkstaffToggleSelectedPayload.ID, WorkstaffToggleSelectedPayload.getPayloadHandler());
	}
	
}
