package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

public record EnderSpliceComponent(Optional<Vec3> pos, Optional<ResourceKey<Level>> dimension, Optional<String> targetName, Optional<UUID> targetUUID) {
	
	public static final EnderSpliceComponent DEFAULT = new EnderSpliceComponent(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	
	public static final Codec<EnderSpliceComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Vec3.CODEC.optionalFieldOf("pos").forGetter(c -> c.pos),
			ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(c -> c.dimension),
			Codec.STRING.optionalFieldOf("target_name").forGetter(c -> c.targetName),
			UUIDUtil.AUTHLIB_CODEC.optionalFieldOf("target_uuid").forGetter(c -> c.targetUUID)
	).apply(i, EnderSpliceComponent::new));
	
	public static final StreamCodec<ByteBuf, EnderSpliceComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(PacketCodecHelper.VEC3D), EnderSpliceComponent::pos,
			ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)), EnderSpliceComponent::dimension,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), EnderSpliceComponent::targetName,
			ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), EnderSpliceComponent::targetUUID,
			EnderSpliceComponent::new
	);
	
	public EnderSpliceComponent(Vec3 pos, ResourceKey<Level> dimension) {
		this(Optional.of(pos), Optional.of(dimension), Optional.empty(), Optional.empty());
	}
	
	public EnderSpliceComponent(String targetName, UUID targetUUID) {
		this(Optional.empty(), Optional.empty(), Optional.of(targetName), Optional.of(targetUUID));
	}
	
}
