package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.neoforged.neoforge.network.registration.*;

public class SpectrumS2CPackets {
	
	public static void register(PayloadRegistrar registrar) {
		// Attachment Types
		registrar.playToClient(PrimordialFireAttachmentType.Payload.TYPE, PrimordialFireAttachmentType.Payload.CODEC, PrimordialFireAttachmentType.Payload::execute);
		registrar.playToClient(AzureDikeAttachmentType.Payload.TYPE, AzureDikeAttachmentType.Payload.CODEC, AzureDikeAttachmentType.Payload::execute);
		registrar.playToClient(EverpromiseRibbonAttachmentType.Payload.TYPE, EverpromiseRibbonAttachmentType.Payload.CODEC, EverpromiseRibbonAttachmentType.Payload::execute);
		registrar.playToClient(LastKillAttachmentType.Payload.TYPE, LastKillAttachmentType.Payload.CODEC, LastKillAttachmentType.Payload::execute);
		registrar.playToClient(MiscPlayerDataAttachmentType.Payload.TYPE, MiscPlayerDataAttachmentType.Payload.CODEC, MiscPlayerDataAttachmentType.Payload::execute);
		registrar.playToClient(HardcoreDeathAttachmentType.Payload.TYPE, HardcoreDeathAttachmentType.Payload.CODEC, HardcoreDeathAttachmentType.Payload::execute);
		
		// S2C Packets
		registrar.playToClient(PlayParticleWithRandomOffsetAndVelocityPayload.ID, PlayParticleWithRandomOffsetAndVelocityPayload.CODEC, PlayParticleWithRandomOffsetAndVelocityPayload::execute);
		registrar.playToClient(PlayParticleWithExactVelocityPayload.ID, PlayParticleWithExactVelocityPayload.CODEC, PlayParticleWithExactVelocityPayload::execute);
		registrar.playToClient(PlayParticleWithPatternAndVelocityPayload.ID, PlayParticleWithPatternAndVelocityPayload.CODEC, PlayParticleWithPatternAndVelocityPayload::execute);
		registrar.playToClient(PlayParticleAroundBlockSidesPayload.ID, PlayParticleAroundBlockSidesPayload.CODEC, PlayParticleAroundBlockSidesPayload::execute);
		registrar.playToClient(StartSkyLerpingPayload.ID, StartSkyLerpingPayload.CODEC, StartSkyLerpingPayload::execute);
		registrar.playToClient(PlayPedestalCraftingFinishedParticlePayload.ID, PlayPedestalCraftingFinishedParticlePayload.CODEC, PlayPedestalCraftingFinishedParticlePayload::execute);
		registrar.playToClient(PlayShootingStarParticlesPayload.ID, PlayShootingStarParticlesPayload.CODEC, PlayShootingStarParticlesPayload::execute);
		registrar.playToClient(PlayFusionCraftingInProgressParticlePayload.ID, PlayFusionCraftingInProgressParticlePayload.CODEC, PlayFusionCraftingInProgressParticlePayload::execute);
		registrar.playToClient(PlayFusionCraftingFinishedParticlePayload.ID, PlayFusionCraftingFinishedParticlePayload.CODEC, PlayFusionCraftingFinishedParticlePayload::execute);
		registrar.playToClient(PlayMemoryManifestingParticlesPayload.ID, PlayMemoryManifestingParticlesPayload.CODEC, PlayMemoryManifestingParticlesPayload::execute);
		registrar.playToClient(PlayPedestalUpgradedParticlePayload.ID, PlayPedestalUpgradedParticlePayload.CODEC, PlayPedestalUpgradedParticlePayload::execute);
		registrar.playToClient(PlayPedestalStartCraftingParticlePayload.ID, PlayPedestalStartCraftingParticlePayload.CODEC, PlayPedestalStartCraftingParticlePayload::execute);
		registrar.playToClient(ParticleSpawnerConfigurationS2CPayload.ID, ParticleSpawnerConfigurationS2CPayload.CODEC, ParticleSpawnerConfigurationS2CPayload::execute);
		registrar.playToClient(PastelTransmissionPayload.ID, PastelTransmissionPayload.CODEC, PastelTransmissionPayload::execute);
		registrar.playToClient(TypedTransmissionPayload.ID, TypedTransmissionPayload.CODEC, TypedTransmissionPayload::execute);
		registrar.playToClient(ColorTransmissionPayload.ID, ColorTransmissionPayload.CODEC, ColorTransmissionPayload::execute);
		registrar.playToClient(PlayBlockBoundSoundInstancePayload.ID, PlayBlockBoundSoundInstancePayload.CODEC, PlayBlockBoundSoundInstancePayload::execute);
		registrar.playToClient(PlayTakeOffBeltSoundInstancePayload.ID, PlayTakeOffBeltSoundInstancePayload.CODEC, PlayTakeOffBeltSoundInstancePayload::execute);
		registrar.playToClient(UpdateBlockEntityInkPayload.ID, UpdateBlockEntityInkPayload.CODEC, UpdateBlockEntityInkPayload::execute);
		registrar.playToClient(InkColorSelectedS2CPayload.ID, InkColorSelectedS2CPayload.CODEC, InkColorSelectedS2CPayload::execute);
		registrar.playToClient(PlayPresentOpeningParticlesPayload.ID, PlayPresentOpeningParticlesPayload.CODEC, PlayPresentOpeningParticlesPayload::execute);
		registrar.playToClient(PlayAscensionAppliedEffectsPayload.ID, PlayAscensionAppliedEffectsPayload.CODEC, PlayAscensionAppliedEffectsPayload::execute);
		registrar.playToClient(PlayDivinityAppliedEffectsPayload.ID, PlayDivinityAppliedEffectsPayload.CODEC, PlayDivinityAppliedEffectsPayload::execute);
		registrar.playToClient(MoonstoneStrikePayload.ID, MoonstoneStrikePayload.CODEC, MoonstoneStrikePayload::execute);
		registrar.playToClient(SyncArtisansAtlasPayload.ID, SyncArtisansAtlasPayload.CODEC, SyncArtisansAtlasPayload::execute);
		registrar.playToClient(SyncMentalPresencePayload.ID, SyncMentalPresencePayload.CODEC, SyncMentalPresencePayload::execute);
		registrar.playToClient(CompactingChestStatusUpdatePayload.ID, CompactingChestStatusUpdatePayload.CODEC, CompactingChestStatusUpdatePayload::execute);
		registrar.playToClient(FabricationChestStatusUpdatePayload.ID, FabricationChestStatusUpdatePayload.CODEC, FabricationChestStatusUpdatePayload::execute);
		registrar.playToClient(BlackHoleChestStatusUpdatePayload.ID, BlackHoleChestStatusUpdatePayload.CODEC, BlackHoleChestStatusUpdatePayload::execute);
		registrar.playToClient(PastelNodeStatusUpdatePayload.ID, PastelNodeStatusUpdatePayload.CODEC, PastelNodeStatusUpdatePayload::execute);
		registrar.playToClient(PastelNetworkEdgeSyncPayload.ID, PastelNetworkEdgeSyncPayload.CODEC, PastelNetworkEdgeSyncPayload::execute);
		registrar.playToClient(PastelNetworkRemovedPayload.ID, PastelNetworkRemovedPayload.CODEC, PastelNetworkRemovedPayload::execute);
	}
	
}
