package de.dafuqs.spectrum.explosion;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public enum ExplosionArchetype implements StringRepresentable {
	COSMETIC("cosmetic", false, false),
	DESTROY_BLOCKS("destroy_blocks", true, false),
	DAMAGE_ENTITIES("damage_entities", false, true),
	ALL("all", true, true);
	
	public final boolean affectsBlocks;
	public final boolean affectsEntities;
	private final Component name;
	
	public static Codec<ExplosionArchetype> CODEC = StringRepresentable.fromEnum(ExplosionArchetype::values);
	public static final StreamCodec<ByteBuf, ExplosionArchetype> PACKET_CODEC = PacketCodecHelper.enumOf(ExplosionArchetype::values);
	
	ExplosionArchetype(String id, boolean affectsBlocks, boolean affectsEntities) {
		this.affectsBlocks = affectsBlocks;
		this.affectsEntities = affectsEntities;
		this.name = Component.translatable("explosion_archetype.spectrum." + id);
	}
	
	public static ExplosionArchetype tryParse(String name) {
		try {
			return ExplosionArchetype.valueOf(name.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException e) {
			return COSMETIC;
		}
	}
	
	public static ExplosionArchetype get(boolean affectsBlocks, boolean affectsEntities) {
		if (affectsBlocks) {
			return affectsEntities ? ALL : DESTROY_BLOCKS;
		}
		return affectsEntities ? DAMAGE_ENTITIES : COSMETIC;
	}
	
	public Component getName() {
		return name;
	}
	
	@Override
	public @NotNull String getSerializedName() {
		return name().toLowerCase();
	}
	
}
