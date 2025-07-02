package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;

public class SpectrumS2CPackets {
	
	public static void register() {
		register(PlayParticleWithRandomOffsetAndVelocityPayload.ID, PlayParticleWithRandomOffsetAndVelocityPayload.CODEC);
		register(PlayParticleWithExactVelocityPayload.ID, PlayParticleWithExactVelocityPayload.CODEC);
		register(PlayParticleWithPatternAndVelocityPayload.ID, PlayParticleWithPatternAndVelocityPayload.CODEC);
		register(PlayParticleAroundBlockSidesPayload.ID, PlayParticleAroundBlockSidesPayload.CODEC);
		register(StartSkyLerpingPayload.ID, StartSkyLerpingPayload.CODEC);
		register(PlayPedestalCraftingFinishedParticlePayload.ID, PlayPedestalCraftingFinishedParticlePayload.CODEC);
		register(PlayShootingStarParticlesPayload.ID, PlayShootingStarParticlesPayload.CODEC);
		register(PlayFusionCraftingInProgressParticlePayload.ID, PlayFusionCraftingInProgressParticlePayload.CODEC);
		register(PlayFusionCraftingFinishedParticlePayload.ID, PlayFusionCraftingFinishedParticlePayload.CODEC);
		register(PlayMemoryManifestingParticlesPayload.ID, PlayMemoryManifestingParticlesPayload.CODEC);
		register(PlayPedestalUpgradedParticlePayload.ID, PlayPedestalUpgradedParticlePayload.CODEC);
		register(PlayPedestalStartCraftingParticlePayload.ID, PlayPedestalStartCraftingParticlePayload.CODEC);
		register(ParticleSpawnerConfigurationS2CPayload.ID, ParticleSpawnerConfigurationS2CPayload.CODEC);
		register(PastelTransmissionPayload.ID, PastelTransmissionPayload.CODEC);
		register(TypedTransmissionPayload.ID, TypedTransmissionPayload.CODEC);
		register(ColorTransmissionPayload.ID, ColorTransmissionPayload.CODEC);
		register(PlayBlockBoundSoundInstancePayload.ID, PlayBlockBoundSoundInstancePayload.CODEC);
		register(PlayTakeOffBeltSoundInstancePayload.ID, PlayTakeOffBeltSoundInstancePayload.CODEC);
		register(UpdateBlockEntityInkPayload.ID, UpdateBlockEntityInkPayload.CODEC);
		register(InkColorSelectedS2CPayload.ID, InkColorSelectedS2CPayload.CODEC);
		register(PlayPresentOpeningParticlesPayload.ID, PlayPresentOpeningParticlesPayload.CODEC);
		register(PlayAscensionAppliedEffectsPayload.ID, PlayAscensionAppliedEffectsPayload.CODEC);
		register(PlayDivinityAppliedEffectsPayload.ID, PlayDivinityAppliedEffectsPayload.CODEC);
		register(MoonstoneBlastPayload.ID, MoonstoneBlastPayload.CODEC);
		register(SyncArtisansAtlasPayload.ID, SyncArtisansAtlasPayload.CODEC);
		register(SyncMentalPresencePayload.ID, SyncMentalPresencePayload.CODEC);
		register(CompactingChestStatusUpdatePayload.ID, CompactingChestStatusUpdatePayload.CODEC);
		register(FabricationChestStatusUpdatePayload.ID, FabricationChestStatusUpdatePayload.CODEC);
		register(BlackHoleChestStatusUpdatePayload.ID, BlackHoleChestStatusUpdatePayload.CODEC);
		register(PastelNodeStatusUpdatePayload.ID, PastelNodeStatusUpdatePayload.CODEC);
		register(PastelNetworkEdgeSyncPayload.ID, PastelNetworkEdgeSyncPayload.CODEC);
		register(PastelNetworkRemovedPayload.ID, PastelNetworkRemovedPayload.CODEC);
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		register(PlayParticleWithRandomOffsetAndVelocityPayload.ID, PlayParticleWithRandomOffsetAndVelocityPayload::execute);
		register(PlayParticleWithExactVelocityPayload.ID, PlayParticleWithExactVelocityPayload::execute);
		register(PlayParticleWithPatternAndVelocityPayload.ID, PlayParticleWithPatternAndVelocityPayload::execute);
		register(PlayParticleAroundBlockSidesPayload.ID, PlayParticleAroundBlockSidesPayload::execute);
		register(StartSkyLerpingPayload.ID, StartSkyLerpingPayload::execute);
		register(PlayPedestalCraftingFinishedParticlePayload.ID, PlayPedestalCraftingFinishedParticlePayload::execute);
		register(PlayShootingStarParticlesPayload.ID, PlayShootingStarParticlesPayload::execute);
		register(PlayFusionCraftingInProgressParticlePayload.ID, PlayFusionCraftingInProgressParticlePayload::execute);
		register(PlayFusionCraftingFinishedParticlePayload.ID, PlayFusionCraftingFinishedParticlePayload::execute);
		register(PlayMemoryManifestingParticlesPayload.ID, PlayMemoryManifestingParticlesPayload::execute);
		register(PlayPedestalUpgradedParticlePayload.ID, PlayPedestalUpgradedParticlePayload::execute);
		register(PlayPedestalStartCraftingParticlePayload.ID, PlayPedestalStartCraftingParticlePayload::execute);
		register(ParticleSpawnerConfigurationS2CPayload.ID, ParticleSpawnerConfigurationS2CPayload::execute);
		register(PastelTransmissionPayload.ID, PastelTransmissionPayload::execute);
		register(TypedTransmissionPayload.ID, TypedTransmissionPayload::execute);
		register(ColorTransmissionPayload.ID, ColorTransmissionPayload::execute);
		register(PlayBlockBoundSoundInstancePayload.ID, PlayBlockBoundSoundInstancePayload::execute);
		register(PlayTakeOffBeltSoundInstancePayload.ID, PlayTakeOffBeltSoundInstancePayload::execute);
		register(UpdateBlockEntityInkPayload.ID, UpdateBlockEntityInkPayload::execute);
		register(InkColorSelectedS2CPayload.ID, InkColorSelectedS2CPayload::execute);
		register(PlayPresentOpeningParticlesPayload.ID, PlayPresentOpeningParticlesPayload::execute);
		register(PlayAscensionAppliedEffectsPayload.ID, PlayAscensionAppliedEffectsPayload::execute);
		register(PlayDivinityAppliedEffectsPayload.ID, PlayDivinityAppliedEffectsPayload::execute);
		register(MoonstoneBlastPayload.ID, MoonstoneBlastPayload::execute);
		register(SyncArtisansAtlasPayload.ID, SyncArtisansAtlasPayload::execute);
		register(SyncMentalPresencePayload.ID, SyncMentalPresencePayload::execute);
		register(CompactingChestStatusUpdatePayload.ID, CompactingChestStatusUpdatePayload::execute);
		register(FabricationChestStatusUpdatePayload.ID, FabricationChestStatusUpdatePayload::execute);
		register(BlackHoleChestStatusUpdatePayload.ID, BlackHoleChestStatusUpdatePayload::execute);
		register(PastelNodeStatusUpdatePayload.ID, PastelNodeStatusUpdatePayload::execute);
		register(PastelNetworkEdgeSyncPayload.ID, PastelNetworkEdgeSyncPayload::execute);
		register(PastelNetworkRemovedPayload.ID, PastelNetworkRemovedPayload::execute);
	}
	
	private static <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PayloadTypeRegistry.playS2C().register(id, codec);
	}
	
	@SuppressWarnings("resource")
	private static <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> id, ClientPlayNetworking.PlayPayloadHandler<T> receiver) {
		ClientPlayNetworking.registerGlobalReceiver(id, (payload, context) -> context.client().execute(() -> receiver.receive(payload, context)));
	}
	
}
