package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.attachment.*;

public class EverpromiseRibbonAttachmentType {
	
	public static final String NAME = "everpromise_ribbon";
	public static final AttachmentType<Boolean> ATTACHMENT_TYPE = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();
	
	public static void attachRibbon(LivingEntity livingEntity) {
		livingEntity.setData(ATTACHMENT_TYPE, true);
	}
	
	public static boolean hasRibbon(LivingEntity livingEntity) {
		return livingEntity.getData(ATTACHMENT_TYPE);
	}
	
}
