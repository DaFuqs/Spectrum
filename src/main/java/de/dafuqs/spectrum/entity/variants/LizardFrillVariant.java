package de.dafuqs.spectrum.entity.variants;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;

public enum LizardFrillVariant implements StringRepresentable {
	
	SIMPLE("simple", "textures/entity/lizard/frills_simple.png"),
	FANCY("fancy", "textures/entity/lizard/frills_fancy.png"),
	RUFFLED("ruffled", "textures/entity/lizard/frills_ruffled.png"),
	MODEST("modest", "textures/entity/lizard/frills_modest.png"),
	NONE("none", "textures/entity/lizard/frills_none.png");
	
	public static Codec<LizardFrillVariant> CODEC = StringRepresentable.fromEnum(LizardFrillVariant::values);
	
	private final String name;
	private final ResourceLocation id;
	private final ResourceLocation texture;
	
	LizardFrillVariant(String name, String texture) {
		this.name = name;
		this.id = SpectrumCommon.locate(name);
		this.texture = SpectrumCommon.locate(texture);
		Registry.register(SpectrumRegistries.LIZARD_FRILL_VARIANT, id, this);
	}
	
	public TagKey<LizardFrillVariant> getReference() {
		return TagKey.create(SpectrumRegistries.LIZARD_FRILL_VARIANT.key(), id);
	}
	
	public ResourceLocation getTextureLocation() {
		return texture;
	}
	
	@Override
	public String getSerializedName() {
		return name;
	}
	
}
