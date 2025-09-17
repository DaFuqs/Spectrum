package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.attachment.*;

public class LastKillAttachmentType {
	
	public static final String NAME = "last_kill";
	public static final AttachmentType<Long> ATTACHMENT_TYPE = AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build();
	
	public static void rememberKillTick(LivingEntity livingEntity, long tick) {
		livingEntity.setData(ATTACHMENT_TYPE, tick);
	}
	
	public static long getLastKillTick(LivingEntity livingEntity) {
		return livingEntity.getData(ATTACHMENT_TYPE);
	}
	
}
