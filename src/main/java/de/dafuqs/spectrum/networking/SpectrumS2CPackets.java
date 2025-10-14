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
		registrar.playToClient(PlayParticleWithExactVelocityPayload.ID, PlayParticleWithExactVelocityPayload.CODEC);
		registrar.playToClient(PlayParticleWithPatternAndVelocityPayload.ID, PlayParticleWithPatternAndVelocityPayload.CODEC);
		registrar.playToClient(PlayParticleAroundBlockSidesPayload.ID, PlayParticleAroundBlockSidesPayload.CODEC);
		registrar.playToClient(StartSkyLerpingPayload.ID, StartSkyLerpingPayload.CODEC);
		registrar.playToClient(PlayPedestalCraftingFinishedParticlePayload.ID, PlayPedestalCraftingFinishedParticlePayload.CODEC);
		registrar.playToClient(PlayShootingStarParticlesPayload.ID, PlayShootingStarParticlesPayload.CODEC);
		registrar.playToClient(PlayFusionCraftingInProgressParticlePayload.ID, PlayFusionCraftingInProgressParticlePayload.CODEC);
		registrar.playToClient(PlayFusionCraftingFinishedParticlePayload.ID, PlayFusionCraftingFinishedParticlePayload.CODEC);
		registrar.playToClient(PlayMemoryManifestingParticlesPayload.ID, PlayMemoryManifestingParticlesPayload.CODEC);
		registrar.playToClient(PlayPedestalUpgradedParticlePayload.ID, PlayPedestalUpgradedParticlePayload.CODEC);
		registrar.playToClient(PlayPedestalStartCraftingParticlePayload.ID, PlayPedestalStartCraftingParticlePayload.CODEC);
		registrar.playToClient(ParticleSpawnerConfigurationS2CPayload.ID, ParticleSpawnerConfigurationS2CPayload.CODEC);
		registrar.playToClient(PastelTransmissionPayload.ID, PastelTransmissionPayload.CODEC);
		registrar.playToClient(TypedTransmissionPayload.ID, TypedTransmissionPayload.CODEC);
		registrar.playToClient(ColorTransmissionPayload.ID, ColorTransmissionPayload.CODEC);
		registrar.playToClient(PlayBlockBoundSoundInstancePayload.ID, PlayBlockBoundSoundInstancePayload.CODEC);
		registrar.playToClient(PlayTakeOffBeltSoundInstancePayload.ID, PlayTakeOffBeltSoundInstancePayload.CODEC);
		registrar.playToClient(UpdateBlockEntityInkPayload.ID, UpdateBlockEntityInkPayload.CODEC);
		registrar.playToClient(InkColorSelectedS2CPayload.ID, InkColorSelectedS2CPayload.CODEC);
		registrar.playToClient(PlayPresentOpeningParticlesPayload.ID, PlayPresentOpeningParticlesPayload.CODEC);
		registrar.playToClient(PlayAscensionAppliedEffectsPayload.ID, PlayAscensionAppliedEffectsPayload.CODEC);
		registrar.playToClient(PlayDivinityAppliedEffectsPayload.ID, PlayDivinityAppliedEffectsPayload.CODEC);
		registrar.playToClient(MoonstoneBlastPayload.ID, MoonstoneBlastPayload.CODEC);
		registrar.playToClient(SyncArtisansAtlasPayload.ID, SyncArtisansAtlasPayload.CODEC);
		registrar.playToClient(SyncMentalPresencePayload.ID, SyncMentalPresencePayload.CODEC);
		registrar.playToClient(CompactingChestStatusUpdatePayload.ID, CompactingChestStatusUpdatePayload.CODEC);
		registrar.playToClient(FabricationChestStatusUpdatePayload.ID, FabricationChestStatusUpdatePayload.CODEC);
		registrar.playToClient(BlackHoleChestStatusUpdatePayload.ID, BlackHoleChestStatusUpdatePayload.CODEC);
		registrar.playToClient(PastelNodeStatusUpdatePayload.ID, PastelNodeStatusUpdatePayload.CODEC);
		registrar.playToClient(PastelNetworkEdgeSyncPayload.ID, PastelNetworkEdgeSyncPayload.CODEC);
		registrar.playToClient(PastelNetworkRemovedPayload.ID, PastelNetworkRemovedPayload.CODEC);
	}
	
}
