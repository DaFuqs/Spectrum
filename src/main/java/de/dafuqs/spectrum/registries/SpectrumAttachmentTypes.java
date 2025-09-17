package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.attachment_types.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumAttachmentTypes {
	
	private static final DeferredRegister<AttachmentType<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(PrimordialFireAttachmentType.NAME, () -> PrimordialFireAttachmentType.ATTACHMENT_TYPE);
		REGISTRAR.register(AzureDikeAttachmentType.NAME, () -> AzureDikeAttachmentType.ATTACHMENT_TYPE);
		REGISTRAR.register(EverpromiseRibbonAttachmentType.NAME, () -> EverpromiseRibbonAttachmentType.ATTACHMENT_TYPE);
		REGISTRAR.register(LastKillAttachmentType.NAME, () -> LastKillAttachmentType.ATTACHMENT_TYPE);
		REGISTRAR.register(MiscPlayerDataAttachmentType.NAME, () -> MiscPlayerDataAttachmentType.ATTACHMENT_TYPE);
		REGISTRAR.register(HardcoreDeathAttachmentType.NAME, () -> HardcoreDeathAttachmentType.ATTACHMENT_TYPE);
	}
	
}
